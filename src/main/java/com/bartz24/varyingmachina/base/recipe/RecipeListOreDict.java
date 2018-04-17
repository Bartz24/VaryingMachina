package com.bartz24.varyingmachina.base.recipe;

import com.bartz24.varyingmachina.ItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class RecipeListOreDict implements RecipeObject {

    private String[] oreTypes;
    private int amount;

    public RecipeListOreDict(int count, String... oreTypes) {
        this.oreTypes = oreTypes;
        amount = count;
    }

    public List<ItemStack> getRepresentativeObject() {
        List<ItemStack> stacks = new ArrayList();
        for (String oreType : oreTypes) {
            for (ItemStack stack : OreDictionary.getOres(oreType)) {
                ItemStack copy = stack.copy();
                copy.setCount(amount);
                stacks.add(copy);
            }
        }
        return stacks;
    }

    @Override
    public boolean matches(RecipeObject check) {
        if (!(check instanceof RecipeItem))
            return false;
        for (ItemStack stack : getRepresentativeObject()) {
            if (ItemHelper.itemStacksEqualOD(stack, (ItemStack) check.getRepresentativeObject())
                    && ((ItemStack) check.getRepresentativeObject()).getCount() >= amount)
                return true;
        }
        return false;
    }

    @Override
    public boolean matchesExact(RecipeObject check) {
        if (!(check instanceof RecipeItem))
            return false;
        for (ItemStack stack : getRepresentativeObject()) {
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
        if (getRepresentativeObject().size() == 0)
            return false;
        for (ItemStack stack : getRepresentativeObject()) {
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
        return new RecipeListOreDict(amount + 0, oreTypes.clone());
    }
}