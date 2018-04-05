package com.bartz24.varyingmachina.base.recipe;

import com.bartz24.varyingmachina.ItemHelper;

import net.minecraft.item.ItemStack;

public class RecipeItem implements RecipeObject {

	private ItemStack stack;

	public RecipeItem(ItemStack stack) {
		this.stack = stack;
	}

	public ItemStack getRepresentativeObject() {
		return stack.copy();
	}

	@Override
	public boolean matches(RecipeObject check) {
		if (!(check instanceof RecipeItem))
			return false;
		return ItemHelper.itemStacksEqualOD(stack, (ItemStack) check.getRepresentativeObject())
				&& ((ItemStack) check.getRepresentativeObject()).getCount() >= stack.getCount();
	}

	@Override
	public boolean matchesExact(RecipeObject check) {
		if (!(check instanceof RecipeItem))
			return false;
		return ItemHelper.itemStacksEqualOD(stack, (ItemStack) check.getRepresentativeObject())
				&& ((ItemStack) check.getRepresentativeObject()).getCount() == stack.getCount();
	}

	@Override
	public float getRatio(RecipeObject check) {
		if (!(check instanceof RecipeItem))
			return -1;

		return ((float) ((ItemStack) check.getRepresentativeObject()).getCount() / (float) stack.getCount());
	}

	@Override
	public boolean isValid() {
		return !stack.isEmpty();
	}
}
