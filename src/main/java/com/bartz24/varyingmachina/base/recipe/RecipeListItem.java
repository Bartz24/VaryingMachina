package com.bartz24.varyingmachina.base.recipe;

import java.util.ArrayList;
import java.util.List;

import com.bartz24.varyingmachina.ItemHelper;

import net.minecraft.item.ItemStack;

public class RecipeListItem implements RecipeObject {

	private List<ItemStack> stacks;
	private int amount;

	public RecipeListItem(List<ItemStack> stacks, int count) {
		this.stacks = stacks;
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
}
