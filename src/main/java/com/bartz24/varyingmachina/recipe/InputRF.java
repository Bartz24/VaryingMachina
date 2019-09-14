package com.bartz24.varyingmachina.recipe;

import com.bartz24.varyingmachina.inventory.BetterEnergyStorage;

import java.util.Collections;
import java.util.List;

public class InputRF extends InputBase<BetterEnergyStorage, Integer> {
    private int value;
    protected String invId;

    public InputRF(int value, String invId) {
        this.value = value;
        this.invId = invId;
    }

    public InputRF(int value) {
        this(value, "input");
    }


    @Override
    public String getId() {
        return invId;
    }

    @Override
    public boolean hasInput(BetterEnergyStorage inventory) {
        return inventory.extractInternalEnergy(getRFValue(), true) == getRFValue();
    }

    @Override
    public void drawItemsFromInventory(BetterEnergyStorage inventory) {
        inventory.extractInternalEnergy(getRFValue(), false);
    }

    @Override
    public boolean hasEnough(InputBase in2) {
        if (!(in2 instanceof InputRF))
            return false;
        InputRF inRF2 = (InputRF) in2;
        return inRF2.getRFValue() >= getRFValue();
    }

    @Override
    public List<Integer> getInputs() {
        return Collections.singletonList(getRFValue());
    }

    @Override
    public boolean isValid() {
        return getRFValue() > 0;
    }

    public int getRFValue() {
        return value;
    }

    @Override
    public InputRF scale(int factor) {
        value *= factor;
        return this;
    }

    @Override
    public boolean combineWith(InputBase input2) {
        if (input2 instanceof InputRF) {
            value += ((InputRF) input2).getRFValue();
            return true;
        }
        return false;
    }

    @Override
    public InputRF copy() {
        return new InputRF(value, invId);
    }

    @Override
    public Class<BetterEnergyStorage> getInvType() {
        return BetterEnergyStorage.class;
    }
}
