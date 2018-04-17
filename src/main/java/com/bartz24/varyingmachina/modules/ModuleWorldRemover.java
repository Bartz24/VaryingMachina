package com.bartz24.varyingmachina.modules;

import com.bartz24.varyingmachina.base.inventory.GuiModules;
import com.bartz24.varyingmachina.base.inventory.SidedFluidInventory;
import com.bartz24.varyingmachina.base.machine.MachineVariant;
import com.bartz24.varyingmachina.base.tile.TileCasing;
import com.bartz24.varyingmachina.network.ModuleDataMessage;
import com.bartz24.varyingmachina.network.VaryingMachinaPacketHandler;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

import java.io.IOException;

public class ModuleWorldRemover extends ModuleTransfer {

    public ModuleWorldRemover() {
        super("worldremover");
    }

    public void update(TileCasing casing, EnumFacing installedSide) {
        super.update(casing, installedSide);
        if (!casing.getWorld().isRemote) {
            long ticks = casing.moduleData.get(installedSide.getIndex()).getLong("ticks");
            int itemFilter = casing.moduleData.get(installedSide.getIndex()).getInteger("itemFilter");
            ItemStack thisStack = casing.modules.getStackInSlot(installedSide.getIndex());
            MachineVariant variant = MachineVariant.readFromNBT(thisStack.getTagCompound());
            if (canTick(variant, ticks)) {
                BlockPos checkPos = casing.getPos().add(installedSide.getDirectionVec());
                if (!casing.getWorld().isSideSolid(checkPos, installedSide.getOpposite())) {
                    for (int i = 0; i < casing.getMachine().getOutputInventory(casing).getSlots(); i++) {
                        if (itemFilter >= 0 && itemFilter != i)
                            continue;

                        if (!casing.getMachine().getOutputInventory(casing).getStackInSlot(i).isEmpty()) {
                            EntityItem item = new EntityItem(casing.getWorld(), checkPos.getX() + 0.5f, checkPos.getY() + 0.5f,
                                    checkPos.getZ() + 0.5f, casing.getMachine().getOutputInventory(casing).getStackInSlot(i));
                            item.motionY = 0;
                            item.motionX = 0;
                            item.motionZ = 0;
                            casing.getWorld().spawnEntity(item);
                            casing.getMachine().getOutputInventory(casing).setStackInSlot(i, ItemStack.EMPTY);
                            casing.markDirtyBlockUpdate();
                        }
                    }
                }
                if (FluidUtil.tryPlaceFluid(null, casing.getWorld(), checkPos, getTank(casing, installedSide), getTank(casing, installedSide).drain(1000, false))) {
                    getTank(casing, installedSide).drain(1000, true);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void actionPerformed(TileCasing tile, GuiModules gui, int buttonClicked) throws IOException {
        if (buttonClicked == 10 || buttonClicked == 11) {

            int itemFilter = tile.moduleData.get(gui.selectedModule).hasKey("itemFilter")
                    ? tile.moduleData.get(gui.selectedModule).getInteger("itemFilter") : -1;
            itemFilter = itemFilter + (buttonClicked == 10 ? -1 : 1);
            int slots = tile.getMachine().getInputInventory(tile).getSlots();
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
            IFluidHandler fluidHandler = getTank(tile, EnumFacing.values()[gui.selectedModule]);
            int slots = fluidHandler instanceof SidedFluidInventory ? ((SidedFluidInventory) fluidHandler).getSize() : 0;
            if (fluidFilter < -1)
                fluidFilter = slots - 1;
            else if (fluidFilter > slots - 1)
                fluidFilter = -1;

            VaryingMachinaPacketHandler.instance.sendToServer(new ModuleDataMessage(
                    EnumFacing.values()[gui.selectedModule], tile.getPos(), "fluidFilter", new NBTTagInt(fluidFilter)));
        }
    }


    protected IFluidHandler getTank(TileCasing casing, EnumFacing side)
    {
        int fluidFilter = casing.moduleData.get(side.getIndex()).getInteger("fluidFilter");
        return fluidFilter == -1 ? casing.inputFluids : casing.inputFluids.getTankInSlot(fluidFilter);
    }

    protected ItemStackHandler getItemHandler(TileCasing casing, EnumFacing side)
    {
        return casing.getMachine().getOutputInventory(casing);
    }

    public boolean hasCapability(TileCasing casing, Capability<?> capability, EnumFacing installedSide) {
        return false;
    }

    public <T> T getCapability(TileCasing casing, Capability<T> capability, EnumFacing installedSide) {
        return null;
    }
}
