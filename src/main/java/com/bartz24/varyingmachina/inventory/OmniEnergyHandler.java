package com.bartz24.varyingmachina.inventory;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.HashMap;

public class OmniEnergyHandler implements IEnergyStorage {

    private HashMap<String, BetterEnergyStorage> handlers = new HashMap<>();

    public void setHandler(String id, BetterEnergyStorage handler) {
        handlers.put(id, handler);
    }

    public String[] getHandlerIds() {
        return handlers.keySet().toArray(new String[handlers.size()]);
    }

    private BetterEnergyStorage[] getHandlers() {
        return handlers.values().toArray(new BetterEnergyStorage[handlers.size()]);
    }

    public BetterEnergyStorage getHandler(String id) {
        return handlers.get(id);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;

        int energyReceived = 0;

        for (BetterEnergyStorage energyStorage : handlers.values())
            energyReceived += energyStorage.receiveEnergy(maxReceive, simulate);
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;

        int energyExtracted = 0;

        for (BetterEnergyStorage energyStorage : handlers.values())
            energyExtracted += energyStorage.extractEnergy(maxExtract, simulate);
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        int energy = 0;
        for (BetterEnergyStorage energyStorage : handlers.values())
            energy += energyStorage.getEnergyStored();
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        int capacity = 0;
        for (BetterEnergyStorage energyStorage : handlers.values())
            capacity += energyStorage.getMaxEnergyStored();
        return capacity;
    }

    @Override
    public boolean canExtract() {
        for (BetterEnergyStorage energyStorage : handlers.values()) {
            if (energyStorage.canExtract())
                return true;
        }
        return false;
    }

    @Override
    public boolean canReceive() {
        for (BetterEnergyStorage energyStorage : handlers.values()) {
            if (energyStorage.canReceive())
                return true;
        }
        return false;
    }

    public CompoundNBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();
        for (int i = 0; i < handlers.size(); i++) {
            compound.put("inv" + i, getHandlers()[i].serializeNBT());
        }
        return compound;
    }

    public void deserializeNBT(CompoundNBT compound) {
        for (int i = 0; i < handlers.size(); i++) {
            getHandlers()[i].deserializeNBT(compound.getCompound("inv" + i));
        }
    }
}
