package com.bartz24.varyingmachina.modules;

import com.bartz24.varyingmachina.base.inventory.GuiModules;
import com.bartz24.varyingmachina.base.inventory.SidedFluidInventory;
import com.bartz24.varyingmachina.base.machine.MachineStat;
import com.bartz24.varyingmachina.base.machine.MachineVariant;
import com.bartz24.varyingmachina.base.tile.EnergyContainer.TransferType;
import com.bartz24.varyingmachina.base.tile.TileCasing;
import com.bartz24.varyingmachina.network.ModuleDataMessage;
import com.bartz24.varyingmachina.network.VaryingMachinaPacketHandler;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.io.IOException;
import java.util.List;

public class ModuleWorldInserter extends ModuleWorldTransfer {

    public ModuleWorldInserter() {
        super("worldinserter");
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
                List<EntityItem> list = casing.getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(checkPos.getX(),
                        checkPos.getY(), checkPos.getZ(), checkPos.getX() + 1, checkPos.getY() + 1, checkPos.getZ() + 1));
                if (list.size() > 0) {
                    int maxExtract = (int) getStat(variant, MachineStat.SIZE);
                    if (itemFilter == -1) {
                        for (int i = 0; i < list.size(); i++) {
                            ItemStack extracted = list.get(i).getItem();
                            ItemStack attempt = ItemHandlerHelper.insertItemStacked(casing.getMachine().getInputInventory(casing),
                                    extracted, true);
                            if (!extracted.isEmpty()
                                    && maxExtract - attempt.getCount() > 0) {
                                list.get(i).setItem(ItemHandlerHelper.insertItemStacked(casing.getMachine().getInputInventory(casing),
                                        extracted, false));
                                casing.markDirtyBlockUpdate();
                                break;
                            }
                        }
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            ItemStack extracted = list.get(i).getItem();
                            ItemStack attempt = ItemHandlerHelper.insertItemStacked(casing.getMachine().getInputInventory(casing),
                                    extracted, true);
                            if (!extracted.isEmpty()
                                    && maxExtract - attempt.getCount() > 0) {
                                list.get(i).setItem(casing.getMachine().getInputInventory(casing).insertItem(itemFilter,
                                        extracted, false));
                                casing.markDirtyBlockUpdate();
                                break;
                            }
                        }
                    }
                }
                IFluidHandler handler = FluidUtil.getFluidHandler(casing.getWorld(), checkPos, installedSide.getOpposite());
                if (handler != null) {
                    int maxExtract = (int) getStat(variant, MachineStat.SIZE) * 100;
                    handler.drain(getTank(casing, installedSide).fill(handler.drain(maxExtract, false), true), true);
                }
            }
        }
    }
}
