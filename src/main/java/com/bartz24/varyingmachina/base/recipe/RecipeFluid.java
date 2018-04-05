package com.bartz24.varyingmachina.base.recipe;

import net.minecraftforge.fluids.FluidStack;

public class RecipeFluid implements RecipeObject {

	private FluidStack stack;

	public RecipeFluid(FluidStack stack) {
		this.stack = stack;
	}

	public FluidStack getRepresentativeObject() {
		return stack.copy();
	}

	@Override
	public boolean matches(RecipeObject check) {
		if (!(check instanceof RecipeFluid))
			return false;
		return stack.isFluidEqual((FluidStack) check.getRepresentativeObject())
				&& ((FluidStack) check.getRepresentativeObject()).amount <= stack.amount;
	}

	@Override
	public boolean matchesExact(RecipeObject check) {
		if (!(check instanceof RecipeFluid))
			return false;
		return stack.isFluidEqual((FluidStack) check.getRepresentativeObject())
				&& ((FluidStack) check.getRepresentativeObject()).amount == stack.amount;
	}

	@Override
	public float getRatio(RecipeObject check) {
		if (!(check instanceof RecipeFluid))
			return -1;

		return ((float) ((FluidStack) check.getRepresentativeObject()).amount / (float) stack.amount);
	}

	@Override
	public boolean isValid() {
		return stack != null && stack.amount > 0;
	}

}
