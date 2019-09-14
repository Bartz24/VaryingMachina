package com.bartz24.varyingmachina.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class InputNameItem extends InputItem {

    ResourceLocation name;
    private int count;

    public InputNameItem(ResourceLocation name, int count, String invId) {
        super(ItemStack.EMPTY, invId);
        this.name = name;
        this.count = count;
    }

    public InputNameItem(ResourceLocation name, int count) {
        this(name, count, "input");
    }

    @Override
    public ItemStack getStack() {
        return new ItemStack(ForgeRegistries.ITEMS.getValue(name), count);
    }

    @Override
    public InputItem scale(int factor) {
        count *= factor;
        return this;
    }

    @Override
    public boolean combineWith(InputBase input2) {
        if (input2 instanceof InputNameItem && name.equals(((InputNameItem) input2).name)) {
            count += ((InputNameItem) input2).count;
            return true;
        }
        return false;
    }

    @Override
    public InputNameItem copy() {
        return new InputNameItem(name, count, invId);
    }
}
