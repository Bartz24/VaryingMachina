package com.bartz24.varyingmachina.base.tile;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.EnergyStorage;

public class EnergyContainer extends EnergyStorage {

	public static enum TransferType {
		NONE, INSERT, EXTRACT, BOTH;
	}

	private Map<EnumFacing, TransferType> energySides = new HashMap();

	public EnergyContainer(int capacity, int maxReceive, int maxExtract, int energy) {
		super(capacity, maxReceive, maxExtract, energy);
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("energy", energy);
		for (EnumFacing side : energySides.keySet()) {
			compound.setInteger(side.getName() + "Transfer", energySides.get(side).ordinal());
		}
		return compound;
	}

	public void readFromNBT(NBTTagCompound compound) {
		energy = compound.getInteger("energy");
		for (EnumFacing side : energySides.keySet()) {
			if (compound.hasKey(side.getName() + "Transfer"))
				energySides.put(side, TransferType.values()[compound.getInteger(side.getName() + "Transfer")]);
		}
	}

	public int extractInternalEnergy(int maxExtract, boolean simulate) {
		int energyExtracted = Math.min(energy, maxExtract);
		if (!simulate)
			energy -= energyExtracted;
		return energyExtracted;
	}

	public EnergyContainer setSideTransferType(TransferType type, EnumFacing... sides) {
		for (EnumFacing side : sides)
			energySides.put(side, type);
		return this;
	}

	public boolean canExtractSide(EnumFacing side) {
		if (energySides.get(side) == TransferType.EXTRACT || energySides.get(side) == TransferType.BOTH)
			return super.canExtract();
		return false;
	}

	public boolean canReceiveSide(EnumFacing side) {
		if (energySides.get(side) == TransferType.INSERT || energySides.get(side) == TransferType.BOTH)
			return super.canExtract();
		return false;
	}
}
