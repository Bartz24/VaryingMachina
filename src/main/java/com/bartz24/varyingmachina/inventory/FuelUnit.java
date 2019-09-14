package com.bartz24.varyingmachina.inventory;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public abstract class FuelUnit<T> {

    public abstract double getEnergy();

    /**
     *
     * @param amount
     * @param simulate
     * @return amount of energy drained
     */
    public abstract double drainEnergy(double amount, boolean simulate);

    public abstract T getContainer();

    public abstract ContainerArea getArea();

    public abstract CompoundNBT writeNBT();

    public abstract void readNBT(CompoundNBT compound);

    public abstract List<ITextComponent> getTooltips();
}
