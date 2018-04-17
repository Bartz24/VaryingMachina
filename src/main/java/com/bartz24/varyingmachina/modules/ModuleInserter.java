package com.bartz24.varyingmachina.modules;

import com.bartz24.varyingmachina.base.machine.MachineStat;
import com.bartz24.varyingmachina.base.machine.MachineVariant;
import com.bartz24.varyingmachina.base.tile.EnergyContainer.TransferType;
import com.bartz24.varyingmachina.base.tile.TileCasing;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class ModuleInserter extends ModuleTransfer {

	public ModuleInserter() {
		super("inserter");
	}

	public void update(TileCasing casing, EnumFacing installedSide) {
		super.update(casing, installedSide);
		if (!casing.getWorld().isRemote) {
			long ticks = casing.moduleData.get(installedSide.getIndex()).getLong("ticks");
			int itemFilter = casing.moduleData.get(installedSide.getIndex()).getInteger("itemFilter");
			int fluidFilter = casing.moduleData.get(installedSide.getIndex()).getInteger("fluidFilter");
			ItemStack thisStack = casing.modules.getStackInSlot(installedSide.getIndex());
			MachineVariant variant = MachineVariant.readFromNBT(thisStack.getTagCompound());
			if (canTick(variant, ticks)) {
				if (hasCapabilityFromSide(casing, installedSide, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) {
					IItemHandler handler = getCapabilityFromSide(casing, installedSide,
							CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
					if (handler != null) {
						int maxExtract = (int) getStat(variant, MachineStat.SIZE);
						if (itemFilter == -1) {
							for (int i = 0; i < handler.getSlots(); i++) {
								ItemStack extracted = handler.extractItem(i, maxExtract, true);
								ItemStack attempt = ItemHandlerHelper.insertItemStacked(casing.getMachine().getInputInventory(casing),
										extracted, true);
								if (!handler.getStackInSlot(i).isEmpty() && !extracted.isEmpty()
										&& maxExtract - attempt.getCount() > 0) {
									ItemHandlerHelper.insertItemStacked(casing.getMachine().getInputInventory(casing),
											handler.extractItem(i, maxExtract - attempt.getCount(), false), false);
									casing.markDirtyBlockUpdate();
									break;
								}
							}
						} else {
							for (int i = 0; i < handler.getSlots(); i++) {
								ItemStack extracted = handler.extractItem(i, maxExtract, true);
								ItemStack attempt = casing.getMachine().getInputInventory(casing).insertItem(itemFilter, extracted, true);
								if (!handler.getStackInSlot(i).isEmpty() && !extracted.isEmpty()
										&& maxExtract - attempt.getCount() > 0) {
                                    casing.getMachine().getInputInventory(casing).insertItem(itemFilter,
											handler.extractItem(i, maxExtract - attempt.getCount(), false), false);
									casing.markDirtyBlockUpdate();
									break;
								}
							}
						}
					}
				}
				if (hasCapabilityFromSide(casing, installedSide, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)) {
					IFluidHandler handler = getCapabilityFromSide(casing, installedSide,
							CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
					if (handler != null) {
						int maxExtract = (int) getStat(variant, MachineStat.SIZE) * 100;
						handler.drain(getCapability(casing, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, installedSide).fill(handler.drain(maxExtract, false), true), true);
					}
				}
			}
		}
	}

	public boolean hasCapability(TileCasing casing, Capability<?> capability, EnumFacing installedSide) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return casing.getMachine().getInputItemSlots(casing) > 0;
		return super.hasCapability(casing, capability, installedSide);
	}

	public <T> T getCapability(TileCasing casing, Capability<T> capability, EnumFacing installedSide) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T) casing.getMachine().getInputInventory(casing);
		else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			int fluidFilter = casing.moduleData.get(installedSide.getIndex()).getInteger("fluidFilter");
			return fluidFilter == -1 ? (T) casing.inputFluids : (T) casing.inputFluids.getTankInSlot(fluidFilter);
		}
		else if (capability == CapabilityEnergy.ENERGY)
			return (T) casing.energyStorage;
		return super.getCapability(casing, capability, installedSide);
	}

	public void onAddToCasing(TileCasing casing, EnumFacing installedSide) {
		casing.energyStorage.setSideTransferType(TransferType.INSERT, installedSide);
	}

	public void onRemoveFromCasing(TileCasing casing, EnumFacing installedSide) {
		casing.energyStorage.setSideTransferType(TransferType.NONE, installedSide);
	}
}
