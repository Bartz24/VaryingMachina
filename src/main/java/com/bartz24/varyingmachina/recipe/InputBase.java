package com.bartz24.varyingmachina.recipe;

import java.util.List;

public abstract class InputBase<T, K> {
    public abstract String getId();

    public abstract boolean hasInput(T inventory);

    public abstract void drawItemsFromInventory(T inventory);

    public abstract boolean hasEnough(InputBase in2);

    public abstract List<K> getInputs();

    public abstract boolean isValid();

    public abstract InputBase<T, K> scale(int factor);

    public abstract boolean combineWith(InputBase input2);

    public abstract InputBase<T, K> copy();

    public abstract Class<T> getInvType();
}
