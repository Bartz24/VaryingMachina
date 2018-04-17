package com.bartz24.varyingmachina.machines;

import com.bartz24.varyingmachina.base.inventory.GuiArrowProgress;
import com.bartz24.varyingmachina.base.inventory.GuiCasing;
import com.bartz24.varyingmachina.base.inventory.GuiStatsComp;
import com.bartz24.varyingmachina.base.item.ItemMachine;
import com.bartz24.varyingmachina.base.machine.MachineStat;
import com.bartz24.varyingmachina.base.machine.MachineVariant;
import com.bartz24.varyingmachina.base.recipe.ProcessRecipe;
import com.bartz24.varyingmachina.base.recipe.RecipeItem;
import com.bartz24.varyingmachina.base.tile.TileCasing;
import com.bartz24.varyingmachina.machines.recipes.GrinderRecipes;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MachineGrinder extends ItemMachine {

    public MachineGrinder() {
        super("grinder", MachineStat.SPEED, MachineStat.EFFICIENCY, MachineStat.PRODUCTION);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        for (MachineStat machineStat : stats) {
            if (machineStat == MachineStat.SPEED) {
                MachineVariant variant = MachineVariant.readFromNBT(stack.getTagCompound());
                tooltip.add(TextFormatting.BLUE + "Speed: "
                        + Integer.toString((int) (variant.getStat(MachineStat.SPEED) * 1000f)) + " rad/s");
            } else
                machineStat.addSingleInfo(stack, tooltip);
        }
        MachineVariant.readFromNBT(stack.getTagCompound()).addFuelTooltip(tooltip);
    }

    public void processFinish(World world, BlockPos pos, ItemStack machineStack, ProcessRecipe recipe) {
        ItemStack recipeInput = ItemStack.EMPTY;
        ItemStack input = getInputInventory(getCasingTile(world, pos)).getStackInSlot(0);
        for (ProcessRecipe r : getValidRecipes(world, pos, machineStack)) {
            recipeInput = r.getItemInputs().get(0).get(0);
            float chance = getCombinedStat(MachineStat.PRODUCTION, machineStack, world, pos);
            chance = Math.max(Math.min(r.getNumParameters()[2], chance), r.getNumParameters()[1]);
            ItemStackHandler buffer = getBufferStacks(getCasingTile(world, pos).machineData);
            while (chance >= 1) {
                ItemHandlerHelper.insertItemStacked(buffer, r.getItemOutputs().get(0), false);
                chance -= 1;
            }
            if (world.rand.nextFloat() <= chance)
                ItemHandlerHelper.insertItemStacked(buffer, r.getItemOutputs().get(0), false);
            setBufferStacks(getCasingTile(world, pos).machineData, buffer);
        }
        input.shrink(recipeInput.getCount());
    }

    public boolean canRun(World world, BlockPos pos, ItemStack machineStack, ProcessRecipe recipe) {
        float curHU = getCasingTile(world, pos).machineData.getFloat("curHU");
        return hasRecipes(world, pos, machineStack) && canOutput(world, pos, machineStack, getCasingTile(world, pos).machineData) && curHU > 0;
    }

    protected void process(World world, BlockPos pos, ItemStack machineStack, NBTTagCompound data) {
        if (!world.isRemote) {
            addToOutput(world, pos, machineStack, data);
            super.process(world, pos, machineStack, data);
        }
    }

    public void addToOutput(World world, BlockPos pos, ItemStack machineStack, NBTTagCompound data) {
        ItemStackHandler buffer = getBufferStacks(data);

        boolean changed = false;
        for (int i = 0; i < buffer.getSlots(); i++) {
            if (!buffer.getStackInSlot(i).isEmpty()) {
                if (getOutputInventory(getCasingTile(world, pos)).insertItem(0, buffer.getStackInSlot(i), true)
                        .isEmpty()) {
                    getOutputInventory(getCasingTile(world, pos)).insertItem(0, buffer.getStackInSlot(i), false);
                    buffer.setStackInSlot(i, ItemStack.EMPTY);
                    changed = true;
                }
            }
        }
        setBufferStacks(data, buffer);
        if(changed)
            getCasingTile(world, pos).markDirty();
    }

    private boolean canOutput(World world, BlockPos pos, ItemStack machineStack, NBTTagCompound data) {
        ItemStackHandler buffer = getBufferStacks(data);
        boolean empty = true;
        for (int i = 0; i < buffer.getSlots(); i++) {
            if (!buffer.getStackInSlot(i).isEmpty())
                empty = false;
        }
        if (empty)
            return true;
        return false;
    }

    private List<ProcessRecipe> getValidRecipes(World world, BlockPos pos, ItemStack machineStack) {
        List<ProcessRecipe> recipes = new ArrayList();
        ItemStack input = getInputInventory(getCasingTile(world, pos)).getStackInSlot(0);
        if (input.isEmpty())
            return recipes;
        ProcessRecipe recMachine = new ProcessRecipe(Collections.singletonList(new RecipeItem(input)), "grinder",
                getCombinedStat(MachineStat.SPEED, machineStack, world, pos), Integer.MAX_VALUE, Integer.MAX_VALUE);
        for (ProcessRecipe r : GrinderRecipes.grinderRecipes.getRecipes()) {
            if (r.isValid() && recMachine.isInputRecipeEqualTo(r, false)) {
                recipes.add(r);
            }
        }
        return recipes;
    }

    private boolean hasRecipes(World world, BlockPos pos, ItemStack machineStack) {
        return getValidRecipes(world, pos, machineStack).size() > 0;
    }

    private ItemStackHandler getBufferStacks(NBTTagCompound data) {
        ItemStackHandler stacks = new ItemStackHandler(20);
        stacks.deserializeNBT(data.getCompoundTag("buffer"));
        return stacks;
    }

    private void setBufferStacks(NBTTagCompound data, ItemStackHandler stacks) {
        data.setTag("buffer", stacks.serializeNBT());
    }

    public boolean requiresFuel(ItemStack stack) {
        return true;
    }

    public int getInputItemSlots(TileCasing casing) {
        return 1 + super.getInputItemSlots(casing);
    }

    public int getOutputItemSlots(TileCasing casing) {
        return 1;
    }

    public List<String> getInputItemNames(TileCasing casing) {
        List<String> names = new ArrayList();
        names.add("Input");
        names.addAll(super.getInputItemNames(casing));
        return names;
    }

    public List<String> getOutputItemNames(ItemStack stack) {
        List<String> names = new ArrayList();
        names.add("Output");
        return names;
    }

    public List<Slot> getSlots(TileCasing tile, List<Slot> slots) {
        slots.add(new SlotItemHandler(getInputInventory(tile), 0, 50, 40));
        slots.add(new SlotItemHandler(getOutputInventory(tile), 0, 110, 40));
        return super.getSlots(tile, slots);
    }

    @SideOnly(Side.CLIENT)
    public void initGui(GuiCasing gui, List buttonList, TileCasing casing) {
        super.initGui(gui, buttonList, casing);
        float curHU = casing.machineData.getFloat("curHU");
        float itemHU = casing.machineData.getFloat("itemHU");
        boolean running = casing.machineData.getBoolean("running");
        int time = casing.machineData.getInteger("time");
        int huTick = (int) (casing.machineData.getFloat("huTick") - (running ? (4f
                * (float) getCombinedStat(MachineStat.SPEED, casing.machineStored, casing.getWorld(), casing.getPos()))
                : 0));
        gui.guiComponents.set(1, new GuiStatsComp(155, 25, getCombinedStats(), casing) {
            @Override
            public void addStatTooltip(TileCasing casing, MachineStat stat, List<String> text) {
                if (stat == MachineStat.SPEED) {
                    text.add(
                            TextFormatting.BLUE + "Speed: "
                                    + Integer.toString((int) (MachineGrinder.this.getCombinedStat(MachineStat.SPEED,
                                    casing.machineStored, casing.getWorld(), casing.getPos()) * 1000f))
                                    + " rad/s");
                } else
                    super.addStatTooltip(casing, stat, text);
            }
        });
        gui.guiComponents.add(new GuiArrowProgress(75, 40,
                getTimeToProcess(casing.getWorld(), casing.getPos(), casing.machineStored, null), time));
    }

    @SideOnly(Side.CLIENT)
    public void updateGuiComps(GuiCasing gui, List buttonList, TileCasing casing) {
        super.updateGuiComps(gui, buttonList, casing);
        float curHU = casing.machineData.getFloat("curHU");
        float itemHU = casing.machineData.getFloat("itemHU");
        boolean running = casing.machineData.getBoolean("running");
        int time = casing.machineData.getInteger("time");
        int huTick = (int) (casing.machineData.getFloat("huTick") - (running ? (4f
                * (float) getCombinedStat(MachineStat.SPEED, casing.machineStored, casing.getWorld(), casing.getPos()))
                : 0));
        gui.guiComponents.get(2)
                .updateData(getTimeToProcess(casing.getWorld(), casing.getPos(), casing.machineStored, null), time);
    }
}
