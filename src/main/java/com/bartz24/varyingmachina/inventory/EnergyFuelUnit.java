package com.bartz24.varyingmachina.inventory;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.Collections;
import java.util.List;

public class EnergyFuelUnit extends FuelUnit<BetterEnergyStorage> {

    BetterEnergyStorage energyStorage;

    private static double multiplier = 10d;

    public EnergyFuelUnit(int capacity) {
        energyStorage = new BetterEnergyStorage(capacity, capacity / 10, 0, 0);
    }

    @Override
    public double getEnergy() {
        return energyStorage.getEnergyStored() / multiplier;
    }

    @Override
    public double drainEnergy(double amount, boolean simulate) {
        return energyStorage.extractInternalEnergy((int) (amount * multiplier), simulate);
    }

    @Override
    public BetterEnergyStorage getContainer() {
        return energyStorage;
    }

    @Override
    public ContainerArea getArea() {
        return new AreaEnergyHandler(energyStorage, "fuel", ContainerArea.XAnchorDirection.CENTER, ContainerArea.YAnchorDirection.TOP, -50);
    }

    @Override
    public CompoundNBT writeNBT() {
        CompoundNBT compound = new CompoundNBT();
        compound.put("energy", energyStorage.serializeNBT());
        return compound;
    }

    @Override
    public void readNBT(CompoundNBT compound) {
        energyStorage.deserializeNBT(compound.getCompound("energy"));
    }

    @Override
    public List<ITextComponent> getTooltips() {
        return Collections.singletonList(new StringTextComponent(TextFormatting.GOLD + new TranslationTextComponent("varyingmachina.fuel.energy", energyStorage.getMaxEnergyStored()).getFormattedText()));
    }
}
