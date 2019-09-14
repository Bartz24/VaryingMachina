package com.bartz24.varyingmachina.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class OutputBase<T, K> {
    public abstract String getId();

    public abstract boolean hasRoomForOutput(T inventory, RecipeBase recipeBase);

    public abstract void putItemsIntoInventory(T inventory, RecipeBase recipeBase);

    public abstract K getOutput();

    public Supplier<List<String>> getJEITooltips() {
        return () -> new ArrayList<>();
    }

    public abstract boolean isValid();
    public abstract OutputBase<T, K> scale(int factor);
    public abstract OutputBase<T, K> copy();

    public abstract Class<T> getInvType();

}
