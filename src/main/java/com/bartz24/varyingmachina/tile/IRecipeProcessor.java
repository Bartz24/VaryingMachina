package com.bartz24.varyingmachina.tile;

public interface IRecipeProcessor {
    public <T> T getInventory(String id, Class<T> type);
}
