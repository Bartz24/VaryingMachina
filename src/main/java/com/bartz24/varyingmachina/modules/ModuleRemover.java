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

public class ModuleRemover extends ModuleTransfer {

	public ModuleRemover() {
		super("remover");
	}

	public void update(TileCasing casing, EnumFacing installedSide) {
		super.update(casing, installedSide);
		if (!casing.getWorld().isRemote) {
			long ticks = casing.moduleData.get(installedSide.getIndex()).getLong("ticks");
			int itemFilter = casing.moduleData.get(installedSide.getIndex()).getInteger("itemFilter");
			ItemStack thisStack = casing.modules.getStackInSlot(installedSide.getIndex());
			MachineVariant variant = MachineVariant.readFromNBT(thisStack.getTagCompound());
			if (canTick(variant, ticks)) {
				if (hasCapabilityFromSide(casing, installedSide, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) {
					IItemHandler handler = getCapabilityFromSide(casing, installedSide,
							CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
					if (handler != null) {
						int maxInsert = (int) getStat(variant, MachineStat.SIZE);
						for (int i = 0; i < casing.getMachine().getOutputInventory(casing).getSlots(); i++) {
							if (itemFilter >= 0 && itemFilter != i)
								continue;
							ItemStack extracted = casing.getMachine().getOutputInventory(casing).extractItem(i, maxInsert, true);
							ItemStack attempt = ItemHandlerHelper.insertItemStacked(handler, extracted, true);
							if (!casing.getMachine().getOutputInventory(casing).getStackInSlot(i).isEmpty() && !extracted.isEmpty()
									&& maxInsert - attempt.getCount() > 0) {
								ItemHandlerHelper.insertItemStacked(handler, casing.getMachine().getOutputInventory(casing).extractItem(i,
										maxInsert - attempt.getCount(), false), false);
								casing.markDirtyBlockUpdate();
								break;
							}
						}
					}
				}
				if (hasCapabilityFromSide(casing, installedSide, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)) {
					IFluidHandler handler = getCapabilityFromSide(casing, installedSide,
							CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
					if (handler != null) {
						int maxExtract = (int) getStat(variant, MachineStat.SIZE) * 100;
						getCapability(casing, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, installedSide).drain(handler.fill(getCapability(casing, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, installedSide).drain(maxExtract, false), true), true);
						casing.markDirtyBlockUpdate();
					}
				}
			}
		}
	}

	public boolean hasCapability(TileCasing casing, Capability<?> capability, EnumFacing installedSide) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return casing.getMachine().getOutputItemSlots(casing) > 0;
		return false;
	}

	public <T> T getCapability(TileCasing casing, Capability<T> capability, EnumFacing installedSide) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T) casing.getMachine().getOutputInventory(casing);
		else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			int fluidFilter = casing.moduleData.get(installedSide.getIndex()).getInteger("fluidFilter");
			return fluidFilter == -1 ? (T) casing.outputFluids : (T) casing.outputFluids.getTankInSlot(fluidFilter);
		}
		else if (capability == CapabilityEnergy.ENERGY)
			return (T) casing.energyStorage;
		return super.getCapability(casing, capability, installedSide);
	}

	public void onAddToCasing(TileCasing casing, EnumFacing installedSide) {
		casing.energyStorage.setSideTransferType(TransferType.EXTRACT, installedSide);
	}

	public void onRemoveFromCasing(TileCasing casing, EnumFacing installedSide) {
		casing.energyStorage.setSideTransferType(TransferType.NONE, installedSide);
	}
}
