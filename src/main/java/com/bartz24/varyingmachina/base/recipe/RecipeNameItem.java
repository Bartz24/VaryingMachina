package com.bartz24.varyingmachina.base.recipe;

import com.bartz24.varyingmachina.ItemHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Resource;

public class RecipeNameItem implements RecipeObject {

    private ResourceLocation name;
    private int amount, meta;

    public RecipeNameItem(ResourceLocation name, int amount) {
        this(name, amount, 0);
    }

    public RecipeNameItem(ResourceLocation name, int amount, int meta) {
        this.name = name;
        this.amount = amount;
        this.meta=meta;
    }

    public ItemStack getRepresentativeObject() {
        Item item = Item.REGISTRY.getObject(name);
        return new ItemStack(item, amount, meta);
    }

    @Override
    public boolean matches(RecipeObject check) {
        if (!(check instanceof RecipeItem))
            return false;
        return ItemHelper.itemStacksEqualOD(getRepresentativeObject(), (ItemStack) check.getRepresentativeObject())
                && ((ItemStack) check.getRepresentativeObject()).getCount() >= amount;
    }

    @Override
    public boolean matchesExact(RecipeObject check) {
        if (!(check instanceof RecipeItem))
            return false;
        return ItemHelper.itemStacksEqualOD(getRepresentativeObject(), (ItemStack) check.getRepresentativeObject())
                && ((ItemStack) check.getRepresentativeObject()).getCount() == amount;
    }

    @Override
    public float getRatio(RecipeObject check) {
        if (!(check instanceof RecipeItem))
            return -1;

        return ((float) ((ItemStack) check.getRepresentativeObject()).getCount() / (float) amount);
    }

    @Override
    public boolean isValid() {
        return !getRepresentativeObject().isEmpty();
    }

    @Override
    public int getCount() {
        return amount;
    }

    @Override
    public RecipeObject setCount(int count) {
        amount = count;
        return this;
    }

    @Override
    public RecipeObject copy() {
        return new RecipeNameItem(new ResourceLocation(name.toString()), amount + 0, meta + 0);
    }
}
