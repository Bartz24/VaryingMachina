package com.bartz24.varyingmachina.base.recipe;

import com.bartz24.varyingmachina.ItemHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeListItem implements RecipeObject {

    private List<ItemStack> stacks;
    private int amount;

    public RecipeListItem(List<ItemStack> stacks, int count) {
        this.stacks = stacks;
        amount = count;
    }
    public RecipeListItem(ItemStack[] stacks, int count) {
        this.stacks= Arrays.asList(stacks);
        amount = count;
    }

    public List<ItemStack> getRepresentativeObject() {
        List<ItemStack> stacks = new ArrayList();
        for (ItemStack stack : this.stacks) {
            ItemStack copy = stack.copy();
            copy.setCount(amount);
            stacks.add(copy);
        }
        return stacks;
    }

    @Override
    public boolean matches(RecipeObject check) {
        if (!(check instanceof RecipeItem))
            return false;
        for (ItemStack stack : stacks) {
            if (ItemHelper.itemStacksEqualOD(stack, (ItemStack) check.getRepresentativeObject())
                    && ((ItemStack) check.getRepresentativeObject()).getCount() <= amount)
                return true;
        }
        return false;
    }

    @Override
    public boolean matchesExact(RecipeObject check) {
        if (!(check instanceof RecipeItem))
            return false;
        for (ItemStack stack : stacks) {
            if (ItemHelper.itemStacksEqualOD(stack, (ItemStack) check.getRepresentativeObject())
                    && ((ItemStack) check.getRepresentativeObject()).getCount() == amount)
                return true;
        }
        return false;
    }

    @Override
    public float getRatio(RecipeObject check) {
        if (!(check instanceof RecipeItem))
            return -1;

        return ((float) ((ItemStack) check.getRepresentativeObject()).getCount() / (float) amount);
    }

    @Override
    public boolean isValid() {
        if (stacks.size() == 0)
            return false;
        for (ItemStack stack : stacks) {
            if (stack.isEmpty())
                return false;
        }
        return true;
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
        RecipeListItem list = new RecipeListItem(new ArrayList(), amount + 0);
        list.stacks.addAll(stacks);
        return list;
    }
}
