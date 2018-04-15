package com.bartz24.varyingmachina.base.recipe;

import com.bartz24.varyingmachina.ItemHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeNameItemPriority implements RecipeObject {

    private ResourceLocation[] names;
    private int[] metas;
    private int amount;

    public RecipeNameItemPriority(int amount, ResourceLocation... names) {
        this(amount, names, new int[names.length]);
    }

    public RecipeNameItemPriority(int amount, ResourceLocation[] names, int... metas) {
        this.names = names;
        this.amount = amount;
        this.metas = metas;
    }

    public ItemStack getRepresentativeObject() {
        for (int i = 0; i < names.length; i++) {
            Item item = Item.REGISTRY.getObject(names[i]);
            if (item != null)
                return new ItemStack(item, amount, metas[i]);
        }
        return ItemStack.EMPTY;
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
        return new RecipeNameItemPriority(amount + 0, names.clone(), metas.clone());
    }
}
