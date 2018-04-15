package com.bartz24.varyingmachina.base.item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.bartz24.varyingmachina.References;
import com.bartz24.varyingmachina.base.block.BlockCasing;
import com.bartz24.varyingmachina.base.inventory.GuiCasing;
import com.bartz24.varyingmachina.base.inventory.GuiFluidTank;
import com.bartz24.varyingmachina.base.inventory.GuiFuel;
import com.bartz24.varyingmachina.base.inventory.GuiRFBar;
import com.bartz24.varyingmachina.base.machine.FuelType;
import com.bartz24.varyingmachina.base.machine.MachineStat;
import com.bartz24.varyingmachina.base.machine.MachineVariant;
import com.bartz24.varyingmachina.base.machine.MachineVariant.FuelInfo;
import com.bartz24.varyingmachina.base.models.MachineModelLoader;
import com.bartz24.varyingmachina.base.recipe.ProcessRecipe;
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
import net.minecraftforge.items.SlotItemHandler;

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
            slots.add(new SlotItemHandler(tile.getInputInventory(), getFuelSlotID(tile.machineStored), 8, 53));
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
                    gui.guiComponents.add(new GuiFluidTank(7, 33, casing.getTank().getCapacity(),
                            casing.getTank().getFluidAmount(), casing.getTank().getFluid()));
                    break;
                case FURNACE:
                    gui.guiComponents.add(new GuiFuel(9, 37, (int) maxHU, (int) itemHU));
                    break;
                case ITEM:
                    gui.guiComponents.add(new GuiFuel(9, 37, (int) maxHU, (int) itemHU));
                    break;
                case MANA:
                    break;
                case RF:
                    gui.guiComponents.add(new GuiRFBar(8, 25, casing.energyStorage.getMaxEnergyStored(),
                            casing.energyStorage.getEnergyStored(), 0, false));
                    break;
            }
        }
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
                    gui.guiComponents.get(0).updateData(casing.getTank().getCapacity(), casing.getTank().getFluidAmount(),
                            casing.getTank().getFluid());
                    break;
                case FURNACE:
                    gui.guiComponents.get(0).updateData((int) maxHU, (int) itemHU);
                    break;
                case ITEM:
                    gui.guiComponents.get(0).updateData((int) maxHU, (int) itemHU);
                    break;
                case MANA:
                    break;
                case RF:
                    gui.guiComponents.get(0).updateData(casing.energyStorage.getMaxEnergyStored(),
                            casing.energyStorage.getEnergyStored(), 0);
                    break;
            }
        }
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

    public int getFuelSlotID(ItemStack stack) {
        return requiresFuel(stack) ? getInputItemSlots(stack) - 1 : 0;
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
    public int getInputItemSlots(ItemStack stack) {
        FuelType type = MachineVariant.readFromNBT(stack.getTagCompound()).getFuel().type;
        return (type == FuelType.FURNACE || type == FuelType.ITEM ? 1 : 0);
    }

    public int getOutputItemSlots(ItemStack stack) {
        return 0;
    }

    public List<String> getInputItemNames(ItemStack stack) {
        List<String> names = new ArrayList();
        FuelType type = MachineVariant.readFromNBT(stack.getTagCompound()).getFuel().type;
        if (type == FuelType.FURNACE || type == FuelType.ITEM)
            names.add("Fuel");
        return names;
    }

    public List<String> getOutputItemNames(ItemStack stack) {
        return new ArrayList();
    }

    // Fluid Handler
    public Fluid getFluid(ItemStack stack) {
        FuelType type = MachineVariant.readFromNBT(stack.getTagCompound()).getFuel().type;
        return (requiresFuel(stack) && type == FuelType.FLUID)
                ? MachineVariant.readFromNBT(stack.getTagCompound()).getFuel().getFluid() : null;
    }

    public int getMaxFluid(ItemStack stack) {
        FuelType type = MachineVariant.readFromNBT(stack.getTagCompound()).getFuel().type;
        return (requiresFuel(stack) && type == FuelType.FLUID) ? type.defaultMaxFluid() : 0;
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
        TileCasing casing = getCasingTile(world, pos);
        float value = 0;
        if (!casingHasStat && !machineHasStat)
            value = 0;
        else if (casingHasStat && !machineHasStat)
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

    }
}
