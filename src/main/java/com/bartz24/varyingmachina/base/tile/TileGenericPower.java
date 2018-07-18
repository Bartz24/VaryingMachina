package com.bartz24.varyingmachina.base.tile;

import com.bartz24.varyingmachina.base.tile.EnergyContainer.TransferType;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

public class TileGenericPower extends TileBase {

	public EnergyContainer energyStorage;

	public TileGenericPower(String name, int maxPower, int maxIn, int maxOut) {
		super(name);
		energyStorage = new EnergyContainer(maxPower, maxIn, maxOut, 0).setSideTransferType(TransferType.BOTH,
				EnumFacing.values());
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);

		energyStorage.writeToNBT(compound);

		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		energyStorage.readFromNBT(compound);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return (T) energyStorage;
		}
		return super.getCapability(capability, facing);
	}
}