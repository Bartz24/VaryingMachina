package com.bartz24.varyingmachina.base.recipe;

import java.util.ArrayList;
import java.util.List;

import com.bartz24.varyingmachina.ItemHelper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeOreDictPriority implements RecipeObject {

    private String[] oreTypes;
    private int amount;

    public RecipeOreDictPriority(int count, String... oreTypes) {
        this.oreTypes = oreTypes;
        amount = count;
    }

    private String getFirstOreType() {
        for (int i = 0; i < oreTypes.length; i++) {
            if (OreDictionary.getOres(oreTypes[i]).size() > 0)
                return oreTypes[i];
        }
        return oreTypes[oreTypes.length - 1];
    }

    public List<ItemStack> getRepresentativeObject() {
        List<ItemStack> stacks = new ArrayList();
        for (ItemStack stack : OreDictionary.getOres(getFirstOreType())) {
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
        if (OreDictionary.getOres(getFirstOreType()).size() == 0)
            return false;
        for (ItemStack stack : OreDictionary.getOres(getFirstOreType())) {
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
        return new RecipeOreDictPriority(amount + 0, oreTypes.clone());
    }
}
