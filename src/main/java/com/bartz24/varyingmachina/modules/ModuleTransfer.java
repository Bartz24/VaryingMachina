package com.bartz24.varyingmachina.modules;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import com.bartz24.varyingmachina.References;
import com.bartz24.varyingmachina.base.inventory.GuiGoodButton;
import com.bartz24.varyingmachina.base.inventory.GuiModules;
import com.bartz24.varyingmachina.base.inventory.ItemHandlerNamed;
import com.bartz24.varyingmachina.base.inventory.SidedFluidInventory;
import com.bartz24.varyingmachina.base.item.ItemModule;
import com.bartz24.varyingmachina.base.machine.MachineStat;
import com.bartz24.varyingmachina.base.machine.MachineVariant;
import com.bartz24.varyingmachina.base.tile.TileCasing;
import com.bartz24.varyingmachina.network.ModuleDataMessage;
import com.bartz24.varyingmachina.network.VaryingMachinaPacketHandler;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ModuleTransfer extends ItemModule {
    public ModuleTransfer(String machineID) {
        super(machineID, MachineStat.SPEED, MachineStat.SIZE);
    }

    public float getStat(MachineVariant variant, MachineStat stat) {
        if (stat == MachineStat.SPEED) {
            return getStat(variant, MachineStat.SIZE) == 0 ? 0
                    : (int) Math.max(1, 100f / (variant.getStat(stat) * variant.getStat(stat)));
        } else if (stat == MachineStat.SIZE) {
            return (int) Math.min(Math.max(0, variant.getStat(stat) - 2f), 64);
        } else
            return super.getStat(variant, stat);
    }

    public void addSingleInfo(MachineStat stat, ItemStack stack, List<String> tooltip) {
        MachineVariant variant = MachineVariant.readFromNBT(stack.getTagCompound());
        if (variant != null) {
            switch (stat) {
                case SPEED:
                    tooltip.add(TextFormatting.BLUE + "Speed: " + (int) getStat(variant, stat) + " ticks");
                    break;
                case SIZE:
                    tooltip.add(TextFormatting.DARK_BLUE + "Items: " + (int) getStat(variant, stat));
                    tooltip.add(TextFormatting.DARK_BLUE + "Fluids: " + (int) (getStat(variant, stat) * 100f));
                    tooltip.add(TextFormatting.DARK_BLUE + "RF: " + (int) (Math.pow(getStat(variant, stat), 2.12f) * 100f));
                    break;
                default:
                    super.addSingleInfo(stat, stack, tooltip);
                    break;
            }
        }
    }

    public float manipulateStat(MachineVariant moduleVariant, MachineStat stat, float value) {
        return value;
    }

    public void update(TileCasing casing, EnumFacing installedSide) {
        if (!casing.getWorld().isRemote) {
            long ticks = casing.moduleData.get(installedSide.getIndex()).getLong("ticks");
            ticks++;
            casing.moduleData.get(installedSide.getIndex()).setLong("ticks", ticks);
            int itemFilter = casing.moduleData.get(installedSide.getIndex()).hasKey("itemFilter")
                    ? casing.moduleData.get(installedSide.getIndex()).getInteger("itemFilter") : -1;
            if (getCapability(casing, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, installedSide) == null
                    || getCapability(casing, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, installedSide).getSlots() == 0
                    || itemFilter >= getCapability(casing, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, installedSide).getSlots())
                itemFilter = -1;
            casing.moduleData.get(installedSide.getIndex()).setInteger("itemFilter", itemFilter);
            int fluidFilter = casing.moduleData.get(installedSide.getIndex()).hasKey("fluidFilter")
                    ? casing.moduleData.get(installedSide.getIndex()).getInteger("fluidFilter") : -1;
            casing.moduleData.get(installedSide.getIndex()).setInteger("fluidFilter", -1);

            IFluidHandler fluidHandler = getCapability(casing, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, installedSide);
            SidedFluidInventory fluidInventory = fluidHandler instanceof SidedFluidInventory ? (SidedFluidInventory) fluidHandler : null;
            if (fluidInventory == null || fluidInventory.getSize() == 0 || fluidFilter >= fluidInventory.getSize())
                fluidFilter = -1;
            casing.moduleData.get(installedSide.getIndex()).setInteger("fluidFilter", fluidFilter);
        }
    }

    protected boolean canTick(MachineVariant variant, long ticks) {
        return getStat(variant, MachineStat.SPEED) == 0 ? false
                : (ticks % (int) getStat(variant, MachineStat.SPEED) == 0);
    }

    protected boolean hasCapabilityFromSide(TileCasing casing, EnumFacing installedSide, Capability<?> capability) {

        if (casing.getWorld().getTileEntity(casing.getPos().add(installedSide.getDirectionVec())) == null)
            return false;
        return casing.getWorld().getTileEntity(casing.getPos().add(installedSide.getDirectionVec()))
                .hasCapability(capability, installedSide.getOpposite());
    }

    protected <T> T getCapabilityFromSide(TileCasing casing, EnumFacing installedSide, Capability<T> capability) {

        return casing.getWorld().getTileEntity(casing.getPos().add(installedSide.getDirectionVec()))
                .getCapability(capability, installedSide.getOpposite());
    }

    public boolean hasCapability(TileCasing casing, Capability<?> capability, EnumFacing installedSide) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
                || capability == CapabilityEnergy.ENERGY)
            return true;
        return super.hasCapability(casing, capability, installedSide);
    }

    @SideOnly(Side.CLIENT)
    public void initGui(GuiModules gui, List buttonList, TileCasing casing) {
        buttonList.add(new GuiGoodButton(10, gui.getGuiLeft() + 40, gui.getGuiTop() + 48, 8, 8, "",
                new ResourceLocation(References.ModID, "textures/gui/guiicons.png"), 72, 87));
        buttonList.add(new GuiGoodButton(11, gui.getGuiLeft() + 70, gui.getGuiTop() + 48, 8, 8, "",
                new ResourceLocation(References.ModID, "textures/gui/guiicons.png"), 72, 79));
        buttonList.add(new GuiGoodButton(12, gui.getGuiLeft() + 40, gui.getGuiTop() + 58, 8, 8, "",
                new ResourceLocation(References.ModID, "textures/gui/guiicons.png"), 72, 87));
        buttonList.add(new GuiGoodButton(13, gui.getGuiLeft() + 70, gui.getGuiTop() + 58, 8, 8, "",
                new ResourceLocation(References.ModID, "textures/gui/guiicons.png"), 72, 79));
    }

    @SideOnly(Side.CLIENT)
    public void updateGuiComps(GuiModules gui, List buttonList, TileCasing casing) {
    }

    @SideOnly(Side.CLIENT)
    public void actionPerformed(TileCasing tile, GuiModules gui, int buttonClicked) throws IOException {
        if (buttonClicked == 10 || buttonClicked == 11) {

            int itemFilter = tile.moduleData.get(gui.selectedModule).hasKey("itemFilter")
                    ? tile.moduleData.get(gui.selectedModule).getInteger("itemFilter") : -1;
            itemFilter = itemFilter + (buttonClicked == 10 ? -1 : 1);
            int slots = getCapability(tile, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.values()[gui.selectedModule]).getSlots();
            if (itemFilter < -1)
                itemFilter = slots - 1;
            else if (itemFilter > slots - 1)
                itemFilter = -1;

            VaryingMachinaPacketHandler.instance.sendToServer(new ModuleDataMessage(
                    EnumFacing.values()[gui.selectedModule], tile.getPos(), "itemFilter", new NBTTagInt(itemFilter)));
        } else if (buttonClicked == 12 || buttonClicked == 13) {

            int fluidFilter = tile.moduleData.get(gui.selectedModule).hasKey("fluidFilter")
                    ? tile.moduleData.get(gui.selectedModule).getInteger("fluidFilter") : -1;
            fluidFilter = fluidFilter + (buttonClicked == 12 ? -1 : 1);
            tile.moduleData.get(gui.selectedModule).setInteger("fluidFilter", -1);
            IFluidHandler fluidHandler = getCapability(tile, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.values()[gui.selectedModule]);
            int slots = fluidHandler instanceof SidedFluidInventory ? ((SidedFluidInventory) fluidHandler).getSize() : 0;
            if (fluidFilter < -1)
                fluidFilter = slots - 1;
            else if (fluidFilter > slots - 1)
                fluidFilter = -1;

            VaryingMachinaPacketHandler.instance.sendToServer(new ModuleDataMessage(
                    EnumFacing.values()[gui.selectedModule], tile.getPos(), "fluidFilter", new NBTTagInt(fluidFilter)));
        }
    }

    public void drawBackgroundGui(TileCasing tile, GuiModules gui, FontRenderer fontRenderer, int mouseX, int mouseY) {
    }

    public void drawForegroundGui(TileCasing tile, GuiModules gui, FontRenderer fontRenderer, int mouseX, int mouseY) {

        int itemFilter = tile.moduleData.get(gui.selectedModule).hasKey("itemFilter")
                ? tile.moduleData.get(gui.selectedModule).getInteger("itemFilter") : -1;

        String s = itemFilter == -1 ? "Any" : Integer.toString(itemFilter);
        int stringWidth = fontRenderer.getStringWidth(s);
        fontRenderer.drawString(s, 60 - stringWidth / 2, 48, Color.white.getRGB());

        IItemHandler handler = getCapability(tile, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.values()[gui.selectedModule]);

        s = itemFilter == -1 ? ""
                : (handler instanceof ItemHandlerNamed ? ((ItemHandlerNamed) handler).getNameInSlot(itemFilter) : "");
        stringWidth = fontRenderer.getStringWidth(s);
        fontRenderer.drawString(s, 80, 48, Color.white.getRGB());

        s = "Items";
        stringWidth = fontRenderer.getStringWidth(s);
        fontRenderer.drawString(s, 8, 48, Color.white.getRGB());

        s = "Fluids";
        stringWidth = fontRenderer.getStringWidth(s);
        fontRenderer.drawString(s, 8, 58, Color.white.getRGB());

        IFluidHandler fluidHandler = getCapability(tile, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.values()[gui.selectedModule]);

        int fluidFilter = tile.moduleData.get(gui.selectedModule).hasKey("fluidFilter")
                ? tile.moduleData.get(gui.selectedModule).getInteger("fluidFilter") : -1;

        s = fluidFilter == -1 ? "Any" : Integer.toString(fluidFilter);
        stringWidth = fontRenderer.getStringWidth(s);
        fontRenderer.drawString(s, 60 - stringWidth / 2, 58, Color.white.getRGB());

		/*s = fluidFilter == -1 ? ""
				: (handler instanceof SidedFluidInventory ? ((SidedFluidInventory) handler).getNameInSlot(fluidFilter) : "");
		stringWidth = fontRenderer.getStringWidth(s);
		fontRenderer.drawString(s, 80, 58, Color.white.getRGB());*/
    }
}
