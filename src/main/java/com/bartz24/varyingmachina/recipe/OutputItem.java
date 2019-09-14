package com.bartz24.varyingmachina.recipe;

import com.bartz24.varyingmachina.inventory.ItemHandlerRestricted;
import com.bartz24.varyingmachina.recipe.OutputBase;
import net.minecraft.item.ItemStack;

import java.util.List;

public class OutputItem extends OutputBase<ItemHandlerRestricted,ItemStack> {
    private ItemStack stack;
    private String invId;

    public OutputItem(ItemStack stack) {
        this(stack, "output");
    }

    public OutputItem(ItemStack stack, String invId) {
        this.stack = stack.copy();
        this.invId = invId;
    }


    @Override
    public String getId() {
        return invId;
    }

    @Override
    public boolean hasRoomForOutput(ItemHandlerRestricted inventory, RecipeBase recipeBase) {
        return inventory.insertItemStacked(getOutput(), true).isEmpty();
    }

    @Override
    public void putItemsIntoInventory(ItemHandlerRestricted inventory, RecipeBase recipeBase) {
        inventory.insertItemStacked(getOutput(), false);
    }

    @Override
    public ItemStack getOutput() {
        return stack.copy();
    }

    @Override
    public boolean isValid() {
        return !stack.isEmpty();
    }

    @Override
    public OutputBase<ItemHandlerRestricted, ItemStack> scale(int factor) {
        stack.setCount(stack.getCount() * factor);
        return this;
    }

    @Override
    public OutputBase<ItemHandlerRestricted, ItemStack> copy() {
        return new OutputItem(stack, invId);
    }

    @Override
    public Class<ItemHandlerRestricted> getInvType() {
        return ItemHandlerRestricted.class;
    }
}
