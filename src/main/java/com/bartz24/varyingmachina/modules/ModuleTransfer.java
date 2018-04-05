package com.bartz24.varyingmachina.modules;

import java.util.List;

import com.bartz24.varyingmachina.base.item.ItemModule;
import com.bartz24.varyingmachina.base.machine.MachineStat;
import com.bartz24.varyingmachina.base.machine.MachineVariant;
import com.bartz24.varyingmachina.base.tile.TileCasing;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

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

	public boolean hasCapability(TileCasing casing, Capability<?> capability) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
				|| capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
				|| capability == CapabilityEnergy.ENERGY)
			return true;
		return super.hasCapability(casing, capability);
	}
}
