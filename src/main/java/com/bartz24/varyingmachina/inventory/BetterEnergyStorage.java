package com.bartz24.varyingmachina.inventory;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.EnergyStorage;

public class BetterEnergyStorage extends EnergyStorage {

    private TileEntity parent;

    public BetterEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public void setParent(TileEntity parent) {
        this.parent = parent;
    }

    public CompoundNBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();
        compound.putInt("energy", energy);
        compound.putInt("capacity", capacity);
        compound.putInt("maxExtract", maxExtract);
        compound.putInt("maxReceive", maxReceive);
        return compound;
    }

    public void deserializeNBT(CompoundNBT nbt) {
        energy = nbt.getInt("energy");
        capacity = nbt.getInt("capacity");
        maxExtract = nbt.getInt("maxExtract");
        maxReceive = nbt.getInt("maxReceive");
    }

    public void setCapacity(int value) {
        capacity = value;
        if (energy > capacity)
            energy = capacity;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int energy = super.extractEnergy(maxExtract, simulate);
        if (energy > 0 && !simulate && parent != null)
            parent.markDirty();
        return energy;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energy = super.receiveEnergy(maxReceive, simulate);
        if (energy > 0 && !simulate && parent != null)
            parent.markDirty();
        return energy;
    }

    public int extractInternalEnergy(int maxExtract, boolean simulate) {
        int energyExtracted = Math.min(energy, maxExtract);
        if (!simulate && parent != null) {
            energy -= energyExtracted;
            parent.markDirty();
        }
        return energyExtracted;
    }

    public int receiveInternalEnergy(int maxReceive, boolean simulate) {
        int energyReceived = Math.min(capacity - energy, maxReceive);
        if (!simulate && parent != null) {
            energy += energyReceived;
            parent.markDirty();
        }
        return energyReceived;
    }

    public int getMaxReceive() {
        return maxReceive;
    }

    public void setMaxReceive(int maxReceive) {
        this.maxReceive = maxReceive;
    }

    public int getMaxExtract() {
        return maxExtract;
    }

    public void setMaxExtract(int maxExtract) {
        this.maxExtract = maxExtract;
    }
}
