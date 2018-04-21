package com.bartz24.varyingmachina.base.item;

import com.bartz24.varyingmachina.RandomHelper;
import com.bartz24.varyingmachina.References;
import com.bartz24.varyingmachina.base.block.BlockCasing;
import com.bartz24.varyingmachina.base.inventory.*;
import com.bartz24.varyingmachina.base.machine.FuelType;
import com.bartz24.varyingmachina.base.machine.MachineStat;
import com.bartz24.varyingmachina.base.machine.MachineVariant;
import com.bartz24.varyingmachina.base.machine.MachineVariant.FuelInfo;
import com.bartz24.varyingmachina.base.models.MachineModelLoader;
import com.bartz24.varyingmachina.base.recipe.ProcessRecipe;
import com.bartz24.varyingmachina.base.tile.FluidTankFiltered;
import com.bartz24.varyingmachina.base.tile.TileCasing;
import com.bartz24.varyingmachina.registry.MachineRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

public class ItemMachine extends ItemBase {

    private String machineID;

    public MachineStat[] stats;

    public ItemMachine(String machineID, MachineStat... machineStats) {
        this.machineID = machineID;
        stats = machineStats;
        setHasSubtypes(true);
    }

    public String getModAddingMachine() {
        return References.ModID;
    }

    public String getMachineID() {
        return machineID;
    }

    public String getItemStackDisplayName(ItemStack stack) {
        MachineVariant variant = MachineVariant.readFromNBT(stack.getTagCompound());
        if (variant == null)
            return super.getItemStackDisplayName(stack);
        return String.format(super.getItemStackDisplayName(stack),
                I18n.translateToLocal(variant.getRegistryName().toString().replace(":", ".")));
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ResourceLocation baseLocation = new ResourceLocation(getModAddingMachine(), "item/" + getMachineID());
        MachineModelLoader loader = new MachineModelLoader(baseLocation, "all");
        Map<MachineVariant, ModelResourceLocation> locations = new HashMap();
        for (int i = 0; i < MachineRegistry.getAllVariantsRegistered().length; i++) {
            ModelResourceLocation location = new ModelResourceLocation(
                    getRegistryName().toString()
                            + MachineRegistry.getAllVariantsRegistered()[i].getRegistryName().getResourcePath(),
                    "inventory");
            locations.put(MachineRegistry.getAllVariantsRegistered()[i], location);
            loader.addVariant(location, MachineRegistry.getAllVariantsRegistered()[i].getTexturePath());
        }
        ModelLoader.registerItemVariants(this, locations.values().toArray(new ModelResourceLocation[locations.size()]));
        ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                MachineVariant variant = MachineVariant.readFromNBT(stack.getTagCompound());
                if (variant != null)
                    return locations.get(variant);
                return locations.get(0);
            }
        });
        ModelLoaderRegistry.registerLoader(loader);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs creativeTab, NonNullList<ItemStack> list) {
        if (isInCreativeTab(creativeTab))
            for (MachineVariant variant : MachineVariant.REGISTRY.getValuesCollection()) {
                ItemStack stack = new ItemStack(this);
                MachineVariant.writeVariantToStack(stack, variant);
                list.add(stack);
            }
    }

    public NonNullList<ItemStack> getItemTypes() {
        NonNullList<ItemStack> list = NonNullList.create();
        for (MachineVariant variant : MachineVariant.REGISTRY.getValuesCollection()) {
            ItemStack stack = new ItemStack(this);
            MachineVariant.writeVariantToStack(stack, variant);
            list.add(stack);
        }
        return list;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        for (MachineStat machineStat : stats)
            machineStat.addSingleInfo(stack, tooltip);
        MachineVariant.readFromNBT(stack.getTagCompound()).addFuelTooltip(tooltip);
    }

    // Machine Functions

    // GUI/Inventory

    public List<Slot> getSlots(TileCasing tile, List<Slot> slots) {
        FuelType type = MachineVariant.readFromNBT(tile.machineStored.getTagCompound()).getFuel().type;
        if (requiresFuel(tile.machineStored) && (type == FuelType.FURNACE || type == FuelType.ITEM))
            slots.add(new SlotItemHandler(getInputInventory(tile), getFuelSlotID(tile), 8, 53));
        return slots;
    }

    public int[] getInvPos(ItemStack stack) {
        return new int[]{0, 0};
    }

    public int[] getGuiSize(ItemStack stack) {
        return new int[]{176, 166};
    }

    @SideOnly(Side.CLIENT)
    public void initGui(GuiCasing gui, List buttonList, TileCasing casing) {
        float curHU = casing.machineData.getFloat("curHU");
        float itemHU = casing.machineData.getFloat("itemHU");
        float maxHU = casing.machineData.getFloat("maxHU");
        if (requiresFuel(casing.machineStored)) {
            FuelInfo fuelInfo = MachineVariant.readFromNBT(casing.machineStored.getTagCompound()).getFuel();
            switch (fuelInfo.type) {
                case FLUID:
                    FluidTankFiltered tank = casing.inputFluids.getTankInSlot(0);
                    gui.addComponent("fuel", new GuiFluidTank(7, 33, tank.getCapacity(), tank.getFluidAmount(),
                            tank.getFluid()));
                    break;
                case FURNACE:
                    gui.addComponent("fuel", new GuiFuel(9, 37, (int) maxHU, (int) itemHU));
                    break;
                case ITEM:
                    gui.addComponent("fuel", new GuiFuel(9, 37, (int) maxHU, (int) itemHU));
                    break;
                case MANA:
                    break;
                case RF:
                    gui.addComponent("fuel", new GuiRFBar(8, 25, casing.energyStorage.getMaxEnergyStored(),
                            casing.energyStorage.getEnergyStored(), 0, false));
                    break;
            }
        }
        gui.addComponent("stats", new GuiStatsComp(155, 25, getCombinedStats(), casing));
    }

    @SideOnly(Side.CLIENT)
    public void updateGuiComps(GuiCasing gui, List buttonList, TileCasing casing) {
        float curHU = casing.machineData.getFloat("curHU");
        float itemHU = casing.machineData.getFloat("itemHU");
        float maxHU = casing.machineData.getFloat("maxHU");
        if (requiresFuel(casing.machineStored)) {
            FuelInfo fuelInfo = MachineVariant.readFromNBT(casing.machineStored.getTagCompound()).getFuel();
            switch (fuelInfo.type) {
                case FLUID:
                    FluidTankFiltered tank = casing.inputFluids.getTankInSlot(0);
                    gui.updateComponent("fuel", tank.getCapacity(), tank.getFluidAmount(),
                            tank.getFluid());
                    break;
                case FURNACE:
                    gui.updateComponent("fuel", (int) maxHU, (int) itemHU);
                    break;
                case ITEM:
                    gui.updateComponent("fuel", (int) maxHU, (int) itemHU);
                    break;
                case MANA:
                    break;
                case RF:
                    gui.updateComponent("fuel", casing.energyStorage.getMaxEnergyStored(),
                            casing.energyStorage.getEnergyStored(), 0);
                    break;
            }
        }
        gui.updateComponent("stats", getCombinedStats(), casing);
    }

    @SideOnly(Side.CLIENT)
    public void actionPerformed(TileCasing tile, GuiCasing gui, int buttonClicked) throws IOException {
    }

    public void drawBackgroundGui(TileCasing tile, GuiCasing gui, FontRenderer fontRenderer, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager()
                .bindTexture(new ResourceLocation(References.ModID, "textures/gui/blankInventory.png"));
        gui.drawTexturedModalRect(gui.getGuiLeft(), gui.getGuiTop(), 0, 0, gui.getXSize(), gui.getYSize());

        Minecraft.getMinecraft().getTextureManager()
                .bindTexture(new ResourceLocation(References.ModID, "textures/gui/guiicons.png"));

        for (Slot s : gui.inventorySlots.inventorySlots) {
            gui.drawTexturedModalRect(gui.getGuiLeft() + s.xPos - 1, gui.getGuiTop() + s.yPos - 1, 59, 60, 18, 18);
        }
    }

    public void drawForegroundGui(TileCasing tile, GuiCasing gui, FontRenderer fontRenderer, int mouseX, int mouseY) {
    }

    public TileCasing getCasingTile(World world, BlockPos pos) {
        return (TileCasing) world.getTileEntity(pos);
    }

    public boolean requiresFuel(ItemStack stack) {
        return false;
    }

    public int getFuelSlotID(TileCasing casing) {
        return requiresFuel(casing.machineStored) ? getInputItemSlots(casing) - 1 : 0;
    }

    // RF Handler
    public int getMaxEnergy(ItemStack stack) {
        FuelType type = MachineVariant.readFromNBT(stack.getTagCompound()).getFuel().type;
        return (requiresFuel(stack) && type == FuelType.RF)
                ? MachineVariant.readFromNBT(stack.getTagCompound()).getFuel().type.defaultMaxEnergy() : 0;
    }

    public int getMaxExtract(ItemStack stack) {
        FuelType type = MachineVariant.readFromNBT(stack.getTagCompound()).getFuel().type;
        return (requiresFuel(stack) && type == FuelType.RF)
                ? MachineVariant.readFromNBT(stack.getTagCompound()).getFuel().type.defaultMaxExtract() : 0;
    }

    public int getMaxReceive(ItemStack stack) {
        FuelType type = MachineVariant.readFromNBT(stack.getTagCompound()).getFuel().type;
        return (requiresFuel(stack) && type == FuelType.RF)
                ? MachineVariant.readFromNBT(stack.getTagCompound()).getFuel().type.defaultMaxRecieve() : 0;
    }

    // Item Handler
    public int getInputItemSlots(TileCasing casing) {
        FuelType type = MachineVariant.readFromNBT(casing.machineStored.getTagCompound()).getFuel().type;
        return (type == FuelType.FURNACE || type == FuelType.ITEM ? 1 : 0);
    }

    public int getOutputItemSlots(TileCasing casing) {
        return 0;
    }

    public List<String> getInputItemNames(TileCasing casing) {
        List<String> names = new ArrayList();
        FuelType type = MachineVariant.readFromNBT(casing.machineStored.getTagCompound()).getFuel().type;
        if (type == FuelType.FURNACE || type == FuelType.ITEM)
            names.add("Fuel");
        return names;
    }

    public List<String> getOutputItemNames(ItemStack stack) {
        return new ArrayList();
    }

    // Fluid Handler
    public Fluid[] getInputFluids(ItemStack stack) {
        List<Fluid> fluids = new ArrayList();
        FuelType type = MachineVariant.readFromNBT(stack.getTagCompound()).getFuel().type;
        if (requiresFuel(stack) && type == FuelType.FLUID)
            fluids.add(MachineVariant.readFromNBT(stack.getTagCompound()).getFuel().getFluid());
        return fluids.toArray(new Fluid[fluids.size()]);
    }

    public int[] getMaxInputFluids(ItemStack stack) {
        List<Integer> amounts = new ArrayList();
        FuelType type = MachineVariant.readFromNBT(stack.getTagCompound()).getFuel().type;
        if (requiresFuel(stack) && type == FuelType.FLUID)
            amounts.add(type.defaultMaxFluid());
        return RandomHelper.toIntArray(amounts);
    }

    public Fluid[] getOutputFluids(ItemStack stack) {
        return new Fluid[0];
    }

    public int[] getMaxOutputFluids(ItemStack stack) {
        return new int[0];
    }

    // Heat Source
    public int getHeatProv(ItemStack stack, World world, BlockPos pos) {
        return 0;
    }

    public float getHUPerTick(ItemStack stack, World world, BlockPos pos, boolean useSpeedInfo) {
        float spd = useSpeedInfo ? (float) getCombinedStat(MachineStat.SPEED, stack, world, pos) : 1;

        return spd * 10;
    }

    // RF Calculations

    public float rfgetRFPerHU(ItemStack stack, World world, BlockPos pos, boolean useFuelInfo,
                              boolean useEfficiencyInfo) {
        if (useFuelInfo) {
            float rate = MachineVariant.readFromNBT(stack.getTagCompound()).getFuel().rate;
            float eff = useEfficiencyInfo ? (float) getCombinedStat(MachineStat.EFFICIENCY, stack, world, pos) : 1;

            return rate / eff;
        }
        return 0;
    }

    public float rfgetRFPerTick(ItemStack stack, World world, BlockPos pos, boolean useFuelInfo, boolean useSpeedInfo,
                                boolean useEfficiencyInfo) {
        return getHUPerTick(stack, world, pos, useSpeedInfo)
                * rfgetRFPerHU(stack, world, pos, useFuelInfo, useEfficiencyInfo);
    }

    // Fuel Calculations

    public float fuelgetFuelTimePercent(ItemStack stack, World world, BlockPos pos, boolean useFuelInfo,
                                        boolean useSpeedInfo, boolean useEfficiencyInfo) {
        float rate = useFuelInfo ? (MachineVariant.readFromNBT(stack.getTagCompound()).getFuel().type == FuelType.ITEM
                ? MachineVariant.readFromNBT(stack.getTagCompound()).getFuel().rate : 1) : 1;
        float spd = useSpeedInfo ? (float) getCombinedStat(MachineStat.SPEED, stack, world, pos) : 1;
        float eff = useEfficiencyInfo ? (float) getCombinedStat(MachineStat.EFFICIENCY, stack, world, pos) : 1;
        return rate / getHUPerTick(stack, world, pos, useSpeedInfo) * eff;
    }

    public float fuelgetAmountPerHU(ItemStack stack, World world, BlockPos pos, boolean useSpeedInfo,
                                    boolean useEfficiencyInfo) {
        return getHUPerTick(stack, world, pos, useSpeedInfo);
    }

    public float getCombinedStat(MachineStat stat, ItemStack stack, World world, BlockPos pos) {
        boolean casingHasStat = Arrays.asList(BlockCasing.stats).contains(stat);
        boolean machineHasStat = Arrays.asList(stats).contains(stat);
        boolean combinedHasStat = Arrays.asList(getCombinedStats()).contains(stat);
        TileCasing casing = getCasingTile(world, pos);
        float value = 0;

        if (!combinedHasStat) {
            if (stat == MachineStat.MAXHU)
                return 1000;
            value = 0;
        } else if (casingHasStat && !machineHasStat)
            value = (float) casing.getVariant().getStat(stat);
        else if (!casingHasStat && machineHasStat)
            value = (float) MachineVariant.readFromNBT(stack.getTagCompound()).getStat(stat);
        else {
            value = (float) MachineVariant.readFromNBT(stack.getTagCompound()).getStat(stat)
                    * (float) casing.getVariant().getStat(stat);
        }
        for (EnumFacing side : EnumFacing.values()) {
            if (!casing.modules.getStackInSlot(side.getIndex()).isEmpty())
                value = casing.getModule(side).manipulateStat(
                        MachineVariant.readFromNBT(casing.modules.getStackInSlot(side.getIndex()).getTagCompound()),
                        stat, value);
        }
        return value;
    }

    public MachineFuelData getMachineFuelData(ItemStack stack, World world, BlockPos pos) {

        if (MachineVariant.readFromNBT(stack.getTagCompound()).getFuel().type == FuelType.RF) {
            return new MachineFuelData(getHUPerTick(stack, world, pos, true),
                    rfgetRFPerTick(stack, world, pos, true, true, true), rfgetRFPerHU(stack, world, pos, true, true));
        } else {
            return new MachineFuelData(getHUPerTick(stack, world, pos, true),
                    fuelgetFuelTimePercent(stack, world, pos, true, true, true),
                    fuelgetAmountPerHU(stack, world, pos, true, true), true);

        }
    }

    protected void heatUp(ItemStack machineStack, World world, BlockPos pos, NBTTagCompound data) {
        boolean changedValue = false;
        float curHU = data.getFloat("curHU");
        float itemHU = data.getFloat("itemHU");
        float huTick = data.getFloat("huTick");
        float huAdded = 0;

        boolean added = true;
        while (added && (huAdded < huTick || huTick == 0)) {
            added = false;
            if (curHU < (int) getCombinedStat(MachineStat.MAXHU, machineStack, world, pos) && itemHU > 0) {
                float amt = Math.max(
                        Math.min(Math.min((int) getCombinedStat(MachineStat.MAXHU, machineStack, world, pos) - curHU,
                                huTick - huAdded), itemHU),
                        0);
                if (amt > 0) {
                    curHU += amt;
                    huAdded += amt;
                    itemHU -= amt;
                    added = true;
                    changedValue = true;
                }
            } else if (itemHU <= 0) {
                data.setFloat("itemHU", itemHU);
                data.setFloat("huTick", huTick);
                MachineVariant.readFromNBT(machineStack.getTagCompound()).getFuel().type
                        .getFuel(getCasingTile(world, pos), world, pos, data);
                itemHU = data.getFloat("itemHU");
                huTick = data.getFloat("huTick");
                changedValue = true;
                if (itemHU > 0)
                    added = true;
            }

        }
        if (changedValue) {
            data.setFloat("curHU", curHU);
            data.setFloat("itemHU", itemHU);
            data.setFloat("huTick", huTick);
            getCasingTile(world, pos).markDirty();
        }
    }

    protected void process(World world, BlockPos pos, ItemStack machineStack, NBTTagCompound data) {
        if (!world.isRemote) {
            boolean changedValue = false;
            heatUp(machineStack, world, pos, data);
            int time = data.getInteger("time");
            float curHU = data.getFloat("curHU");
            float huTick = data.getFloat("huTick");

            ProcessRecipe recipe = getRecipe(world, pos, machineStack);
            if (canRun(world, pos, machineStack, recipe)) {
                data.setBoolean("running", true);
                time++;
                curHU -= getHUDrain(world, pos, machineStack);
                data.setFloat("curHU", curHU);
                if (time >= getTimeToProcess(world, pos, getCasingTile(world, pos).machineStored, recipe)) {
                    processFinish(world, pos, machineStack, recipe);
                    curHU = data.getFloat("curHU");
                    time = 0;
                }
                changedValue = true;
            } else if (time != 0) {
                time = 0;
                data.setBoolean("running", false);
                changedValue = true;
            }
            if (changedValue) {
                getCasingTile(world, pos).markDirty();
                data.setInteger("time", time);
                data.setFloat("curHU", curHU);
                data.setFloat("huTick", huTick);
            }
        }
    }

    public ItemStackHandler getInputInventory(TileCasing casing) {
        return casing.getInputInventory();
    }

    public ItemStackHandler getOutputInventory(TileCasing casing) {
        return casing.getOutputInventory();
    }

    public ProcessRecipe getRecipe(World world, BlockPos pos, ItemStack machineStack) {
        return null;
    }

    public void processFinish(World world, BlockPos pos, ItemStack machineStack, ProcessRecipe recipe) {
    }

    public boolean hasMultiblock() {
        return false;
    }

    public boolean hasValidMultiblock(World world, BlockPos pos, ItemStack machineStack) {
        return false;
    }

    public boolean canRun(World world, BlockPos pos, ItemStack machineStack, ProcessRecipe recipe) {
        float curHU = getCasingTile(world, pos).machineData.getFloat("curHU");
        return getCasingTile(world, pos).getRedstoneSignal() == 0 && (!hasMultiblock() || (hasMultiblock() && hasValidMultiblock(world, pos, machineStack))) && recipe != null && curHU > 0 && getOutputInventory(getCasingTile(world, pos))
                .insertItem(0, recipe.getItemOutputs().get(0), true).isEmpty();
    }

    public float getBaseTimeToProcess(World world, BlockPos pos, ItemStack machineStack, ProcessRecipe recipe) {
        return 100f;
    }

    public int getTimeToProcess(World world, BlockPos pos, ItemStack machineStack, ProcessRecipe recipe) {
        return (int) (getBaseTimeToProcess(world, pos, machineStack, recipe)
                / (float) getCombinedStat(MachineStat.SPEED, machineStack, world, pos));
    }

    public float getHUDrain(World world, BlockPos pos, ItemStack machineStack) {
        return 4f * (float) getCombinedStat(MachineStat.SPEED, machineStack, world, pos);
    }

    public static class MachineFuelData {
        public float huPerTick;
        public float rfPerTick;
        public float rfPerHU;
        public float fuelTimePercent;
        public float fuelPerHU;

        public MachineFuelData(float huTick, float rfTick, float rfHU) {
            huPerTick = huTick;
            rfPerTick = rfTick;
            rfPerHU = rfHU;
        }

        public MachineFuelData(float huTick, float fuelTimePer, float fuelHU, boolean fuel) {
            huPerTick = huTick;
            fuelTimePercent = fuelTimePer;
            fuelPerHU = fuelHU;
        }
    }

    public MachineStat[] getCombinedStats() {
        return stats;
    }

    public void update(World world, BlockPos pos, ItemStack machineStored, NBTTagCompound machineData) {
        process(world, pos, machineStored, machineData);
    }
}
