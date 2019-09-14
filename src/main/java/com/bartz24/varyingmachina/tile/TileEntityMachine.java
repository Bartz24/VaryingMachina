package com.bartz24.varyingmachina.tile;

import com.bartz24.varyingmachina.VaryingMachina;
import com.bartz24.varyingmachina.block.BlockMachine;
import com.bartz24.varyingmachina.container.ContainerMachine;
import com.bartz24.varyingmachina.inventory.*;
import com.bartz24.varyingmachina.machine.MachineType;
import com.bartz24.varyingmachina.machine.MachineVariant;
import com.bartz24.varyingmachina.machine.ModMachines;
import com.bartz24.varyingmachina.machine.ModVariants;
import com.bartz24.varyingmachina.recipe.RecipeBase;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class TileEntityMachine extends TileEntity implements INamedContainerProvider, IRecipeProcessor, ITickableTileEntity {

    private ResourceLocation currentRecipe = null;
    private double progress = -1;
    private String type;
    private String mainVariant, casingVariant;
    private CompoundNBT extraData;
    private FuelUnit fuelUnit;

    OmniItemHandler items;
    OmniEnergyHandler energy;

    public TileEntityMachine() {
        super(VaryingMachina.machine);

        items = new OmniItemHandler();
        energy = new OmniEnergyHandler();

        extraData = new CompoundNBT();
    }


    public OmniItemHandler getItemHandler() {
        return items;
    }

    public OmniEnergyHandler getEnergy() {
        return energy;
    }

    public void onBreak(Block block) {
        for (String handler : items.getHandlerIds()) {
            if (items.getHandler(handler) instanceof FilterItemHandler)
                continue;
            for (int i = 0; i < items.getHandler(handler).getSlots(); i++) {
                if (!items.getHandler(handler).getStackInSlot(i).isEmpty()) {
                    ItemStack stack = items.getHandler(handler).getStackInSlot(i);
                    while (stack.getCount() > 0) {
                        ItemStack spawn = stack.copy();
                        int drain = Math.min(stack.getMaxStackSize(), stack.getCount());
                        spawn.setCount(drain);
                        InventoryHelper.spawnItemStack(getWorld(), pos.getX(), pos.getY(), pos.getZ(), spawn);
                        stack.shrink(drain);
                    }
                }
            }
        }
    }

    public BlockMachine getBlockMachine() {
        return (BlockMachine) world.getBlockState(pos).getBlock();
    }

    public String getMachineType() {
        if (type == null)
            type = getBlockMachine().getMachineType();
        return type;
    }

    public String getMainVariant() {
        if (mainVariant == null)
            mainVariant = getBlockMachine().getMachineVariant();
        return mainVariant;
    }


    public String getCasingVariant() {
        if (casingVariant == null)
            casingVariant = getBlockMachine().getCasingVariant();
        return casingVariant;
    }


    public MachineType getMachine() {
        return ModMachines.types.get(getMachineType());
    }

    public MachineVariant getMainMachineVariant() {
        return ModVariants.types.get(getMainVariant());
    }

    public MachineVariant getCasingMachineVariant() {
        return ModVariants.types.get(getCasingVariant());
    }

    public FuelUnit getFuelUnit() {
        return fuelUnit;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        if (currentRecipe != null)
            compound.putString("currentRecipe", currentRecipe.toString());
        else if (compound.contains("currentRecipe"))
            compound.remove("currentRecipe");
        compound.putString("type", getMachineType());
        compound.putString("mainVariant", getMainVariant());
        compound.putString("casingVariant", getCasingVariant());
        compound.putDouble("progress", progress);
        compound.put("items", items.serializeNBT());
        compound.put("energy", energy.serializeNBT());
        compound.put("extraData", extraData);
        if (fuelUnit != null)
            compound.put("fuelUnit", fuelUnit.writeNBT());
        else if (compound.contains("fuelUnit"))
            compound.remove("fuelUnit");
        return compound;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        if (compound.contains("currentRecipe"))
            currentRecipe = new ResourceLocation(compound.getString("currentRecipe"));
        else
            currentRecipe = null;
        type = compound.getString("type");
        mainVariant = compound.getString("mainVariant");
        casingVariant = compound.getString("casingVariant");
        progress = compound.getDouble("progress");
        createHandlers();
        items.deserializeNBT(compound.getCompound("items"));
        energy.deserializeNBT(compound.getCompound("energy"));
        extraData = compound.getCompound("extraData");
        if (compound.contains("fuelUnit") && fuelUnit != null)
            fuelUnit.readNBT(compound.getCompound("fuelUnit"));
        setItemFuelHandler();
        setEnergyFuelHandler();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> (T) items);
        if (cap == CapabilityEnergy.ENERGY)
            return LazyOptional.of(() -> (T) energy);
        return super.getCapability(cap, side);
    }

    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerIn) {
        return new ContainerMachine(i, playerInventory, this);
    }

    public String getGuiID() {
        return "varyingmachina:machine";
    }

    @Override
    public ITextComponent getDisplayName() {
        String text = new TranslationTextComponent(getGuiID().replace(":", ".")).getFormattedText();
        text = text.replace("VVV", new TranslationTextComponent("material." + getMainVariant()).getFormattedText());
        text = text.replace("CCC", new TranslationTextComponent("material." + getCasingVariant()).getFormattedText());
        text = text.replace("MMM", new TranslationTextComponent("varyingmachina.machine." + getMachineType()).getFormattedText());
        return new StringTextComponent(text);
    }

    @Override
    public <T> T getInventory(String id, Class<T> type) {
        if (type == ItemHandlerRestricted.class)
            return (T) items.getHandler(id);
        else if (type == BetterEnergyStorage.class)
            return (T) energy.getHandler(id);
        else
            return null;
    }

    @Override
    public void tick() {
        if (!world.isRemote && getMachine().isNoRecipes()) {
            if (items.getHandlerIds().length == 0 && energy.getHandlerIds().length == 0)
                createHandlers();
        }
        if (!world.isRemote() && getMachine().getOverrideUpdateFunction() != null) {
            getMachine().getOverrideUpdateFunction().accept(this);
        } else if (!world.isRemote() && getRecipe() != null && hasFuel()) {
            if (progress >= 0) {
                fuelUnit.drainEnergy(getEnergyRate(), false);
                progress += ModMachines.Stats.speed.calculateStat(getMachine(), getMainMachineVariant(), getCasingMachineVariant());
                if (progress >= getRecipe().getTime()) {
                    progress -= getRecipe().getTime();
                    getRecipe().addOutputs(this);
                    if (getRecipe().canProcess(this)) {
                        getRecipe().drawInputs(this);
                    } else
                        progress = -1;
                }
                markDirty();
            } else if (getRecipe().canProcess(this)) {
                fuelUnit.drainEnergy(getEnergyRate(), false);
                progress = ModMachines.Stats.speed.calculateStat(getMachine(), getMainMachineVariant(), getCasingMachineVariant());
                getRecipe().drawInputs(this);
                markDirty();
            }
        }
    }

    public double getEnergyRate() {
        return getMachine().getEnergyRateMultiplier() * ModMachines.Stats.speed.calculateStat(getMachine(), getMainMachineVariant(), getCasingMachineVariant()) / ModMachines.Stats.efficiency.calculateStat(getMachine(), getMainMachineVariant(), getCasingMachineVariant()) * (getRecipe() != null ? getRecipe().getFuelUsage() : 1d);
    }

    public boolean hasFuel() {
        return fuelUnit != null && fuelUnit.getEnergy() >= getEnergyRate();
    }

    public void changeRecipe(PlayerEntity player, ResourceLocation recipe) {
        if (!world.isRemote()) {
            for (int s = 0; s < items.getSlots(); s++) {
                if (player != null)
                    InventoryHelper.spawnItemStack(world, player.posX, player.posY, player.posZ, items.getStackInSlot(s));
                else
                    InventoryHelper.spawnItemStack(world, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, items.getStackInSlot(s));
            }
        }
        currentRecipe = recipe;

        if (currentRecipe != null) {
            RecipeBase recipeBase = getMachine().getRecipe(recipe);

            createHandlers();

            Supplier<Integer> slotMultiplier = () -> {
                double refillRate = 2 * 20; // sec * ticks
                int mult = (int) Math.ceil(refillRate / (recipeBase.getTime() / ModMachines.Stats.speed.calculateStat(getMachine(), getMainMachineVariant(), getCasingMachineVariant())));
                return Math.max(2, mult);
            };

            for (String handler : items.getHandlerIds()) {
                if (handler.contains("fuel"))
                    continue;
                if (items.getHandlerRestricted(handler).isNoExtract()) {
                    items.getHandler(handler).setSize(recipeBase.getNumInputs(handler));
                    if (items.getHandler(handler) instanceof ItemHandlerFiltered) {
                        List<List<ItemStack>> stacks = recipeBase.getInputs(ItemStack.class, handler);

                        for (int i = 0; i < stacks.size(); i++) {
                            ((ItemHandlerFiltered) items.getHandler(handler)).setFilters(i, stacks.get(i));
                            items.getHandlerRestricted(handler).setSlotMultiplier(slotMultiplier);
                        }
                    }
                } else {
                    items.getHandler(handler).setSize(recipeBase.getNumOutputs(handler));
                    if (items.getHandler(handler) instanceof ItemHandlerFiltered) {
                        List<ItemStack> stacks = recipeBase.getOutputs(ItemStack.class, handler);
                        for (int i = 0; i < stacks.size(); i++) {
                            ((ItemHandlerFiltered) items.getHandler(handler)).setFilters(i, Collections.singletonList(stacks.get(i)));
                            items.getHandlerRestricted(handler).setSlotMultiplier(() -> slotMultiplier.get() * 5);
                        }
                    }
                }
            }


            for (String handler : energy.getHandlerIds()) {
                if (handler.contains("fuel"))
                    continue;

                int extractReceiveDivider = 10;

                if (energy.getHandler(handler).canReceive()) {
                    energy.getHandler(handler).setCapacity(((Integer) recipeBase.getInputs(Integer.class, handler).get(0)).intValue() * slotMultiplier.get() * 2);
                    energy.getHandler(handler).setMaxReceive(((Integer) recipeBase.getInputs(Integer.class, handler).get(0)).intValue() * slotMultiplier.get() * 2 / extractReceiveDivider);
                } else {
                    energy.getHandler(handler).setCapacity(((Integer) recipeBase.getOutputs(Integer.class, handler).get(0)).intValue() * slotMultiplier.get() * slotMultiplier.get() * 2);
                    energy.getHandler(handler).setMaxExtract(((Integer) recipeBase.getOutputs(Integer.class, handler).get(0)).intValue() * slotMultiplier.get() * 2 / extractReceiveDivider);
                }
            }
        } else {
            items = new OmniItemHandler();
            energy = new OmniEnergyHandler();
        }
        progress = -1;

        markDirty();

    }

    private void createHandlers() {

        if (!getMachine().isNoFuel() && (getMachine().isNoRecipes() || (!getMachine().isNoRecipes() && currentRecipe != null))) {
            if (fuelUnit == null)
                fuelUnit = getMainMachineVariant().getFuelUnitSupplier().apply(getMainMachineVariant().getFuelUnitSize());
        }
        else
            fuelUnit = null;

        items = getMachine().createItemHandler(this);
        setItemFuelHandler();

        energy = getMachine().createEnergyHandler(this);
        setEnergyFuelHandler();

        for (String s : items.getHandlerIds()) {
            if (items.getHandlerRestricted(s) != null)
                items.getHandlerRestricted(s).setParent(this);
        }
        for (String s : energy.getHandlerIds()) {
            if (energy.getHandler(s) != null)
                energy.getHandler(s).setParent(this);
        }
    }

    private void setItemFuelHandler() {
        if (fuelUnit != null && fuelUnit.getContainer() instanceof ItemHandlerRestricted) {
            items.setHandler("fuel", (ItemHandlerRestricted) fuelUnit.getContainer());
            items.getHandlerRestricted("fuel").setParent(this);
        }
    }

    private void setEnergyFuelHandler() {
        if (fuelUnit != null && fuelUnit.getContainer() instanceof BetterEnergyStorage) {
            energy.setHandler("fuel", (BetterEnergyStorage) fuelUnit.getContainer());
            energy.getHandler("fuel").setParent(this);
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (world != null && !world.isRemote)
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbtTag = new CompoundNBT();
        write(nbtTag);
        return new SUpdateTileEntityPacket(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        super.onDataPacket(net, packet);
        read(packet.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT compound = super.getUpdateTag();
        write(compound);
        return compound;
    }

    public double getProgress() {
        return progress;
    }

    public void incrementProgress(double value) {
        progress += value;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public RecipeBase getRecipe() {
        if (currentRecipe != null && getMachine().getRecipe(currentRecipe) == null) {
            changeRecipe(null, null);
            return null;
        }
        return getMachine().getRecipe(currentRecipe);
    }

    public ResourceLocation getCurrentRecipe() {
        return currentRecipe;
    }

    public CompoundNBT getExtraData() {
        return extraData;
    }

    public Direction getDirection() {
        return world.getBlockState(pos).get(getBlockMachine().FACING);
    }
}
