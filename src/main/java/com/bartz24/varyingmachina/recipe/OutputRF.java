package com.bartz24.varyingmachina.recipe;

import com.bartz24.varyingmachina.inventory.BetterEnergyStorage;

public class OutputRF extends OutputBase<BetterEnergyStorage, Integer> {

    private int value;
    private String invId;

    public OutputRF(int value) {
        this(value, "output");
    }

    public OutputRF(int value, String invId) {
        this.value = value;
        this.invId = invId;
    }

    @Override
    public String getId() {
        return invId;
    }

    @Override
    public boolean hasRoomForOutput(BetterEnergyStorage inventory, RecipeBase recipeBase) {
        return inventory.receiveInternalEnergy(getOutput(), true) == getOutput();
    }

    @Override
    public void putItemsIntoInventory(BetterEnergyStorage inventory, RecipeBase recipeBase) {
        inventory.receiveInternalEnergy(getOutput(), false);
    }

    @Override
    public Integer getOutput() {
        return value;
    }

    @Override
    public boolean isValid() {
        return value > 0;
    }

    @Override
    public OutputBase<BetterEnergyStorage, Integer> scale(int factor) {
        value *= factor;
        return this;
    }

    @Override
    public OutputBase<BetterEnergyStorage, Integer> copy() {
        return new OutputRF(value, invId);
    }

    @Override
    public Class<BetterEnergyStorage> getInvType() {
        return BetterEnergyStorage.class;
    }
}
