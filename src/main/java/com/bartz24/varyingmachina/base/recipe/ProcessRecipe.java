package com.bartz24.varyingmachina.base.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.bartz24.varyingmachina.ItemHelper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class ProcessRecipe {
	private List<RecipeObject> outputs;
	private List<RecipeObject> inputs;
	private float[] numParameters;
	private String type;

	public ProcessRecipe(List<RecipeObject> output, List<RecipeObject> input, String type, float... numVals) {
		outputs = output;
		inputs = input;
		numParameters = numVals;
		this.type = type;
	}

	public ProcessRecipe(List<RecipeObject> input, String type, float... numVals) {
		outputs = new ArrayList();
		inputs = input;
		numParameters = numVals;
		this.type = type;
	}

	public String getRecipeType() {
		return type;
	}

	public boolean isInputRecipeEqualTo(ProcessRecipe recipe, boolean forceEqual) {
		return inputsAreValid(recipe, forceEqual) && numsValid(recipe);
	}

	@Deprecated
	public boolean isInputRecipeLess(ProcessRecipe recipe) {
		return false;
	}

	public boolean isInputMultiRecipeEqualTo(ProcessRecipe recipe) {
		return inputsAreValidMulti(recipe) && numsValid(recipe);
	}

	boolean inputsAreValidMulti(ProcessRecipe recipe) {
		if (inputs.size() != recipe.inputs.size())
			return false;

		int itemsChecked = 0;
		float ratio = -1;
		for (RecipeObject i : inputs) {
			if (!i.isValid())
				continue;
			boolean valid = false;
			for (RecipeObject i2 : recipe.inputs) {
				if (!i2.isValid())
					continue;
				if (i2.matches(i) && (ratio == -1 || i2.getRatio(i) == ratio)) {
					valid = true;
					if (ratio == -1)
						ratio = i2.getRatio(i);
					break;
				}
			}
			if (!valid)
				return false;
			itemsChecked++;
		}

		return itemsChecked == inputs.size();
	}

	boolean inputsAreValid(ProcessRecipe recipe, boolean forceEqual) {
		if (inputs.size() != recipe.inputs.size())
			return false;

		List<Integer> itemsChecked = new ArrayList();
		int index = 0;
		for (RecipeObject i : inputs) {
			if (!i.isValid())
				continue;
			boolean valid = false;
			int index2 = 0;
			for (RecipeObject i2 : recipe.inputs) {
				if (!i2.isValid())
					continue;
				if (!itemsChecked.contains(index2)) {
					if (forceEqual ? i2.matchesExact(i) : i2.matches(i)) {
						valid = true;
						itemsChecked.add(index2);
					}
				}
				index2++;
			}
			if (!valid)
				return false;
			index++;
		}

		return itemsChecked.size() == inputs.size();
	}

	@Deprecated
	boolean stacksAreValidLess(ProcessRecipe recipe) {
		List<Integer> itemsChecked = new ArrayList();
		int index = 0;
		for (Object i2 : recipe.inputs) {
			boolean valid = false;
			int index2 = 0;
			for (Object i : inputs) {
				if (i instanceof ItemStack && ((ItemStack) i).isEmpty())
					continue;
				if (!itemsChecked.contains(index2)) {
					if (i instanceof ItemStack && i2 instanceof String) {
						int[] ids = OreDictionary.getOreIDs((ItemStack) i);
						for (int id : ids)
							if (id == OreDictionary.getOreID(i2.toString())) {
								valid = true;
								itemsChecked.add(index2);
							}
					} else if (i instanceof ItemStack && i2 instanceof ItemStack)
						if (ItemHelper.itemStacksEqualOD((ItemStack) i, (ItemStack) i2)
								&& (((ItemStack) i).getCount() >= ((ItemStack) i2).getCount())) {
							valid = true;
							itemsChecked.add(index2);
						}
				}
				index2++;
			}
			if (!valid)
				return false;
			index++;
		}

		return itemsChecked.size() == recipe.inputs.size();
	}

	boolean numsValid(ProcessRecipe recipe) {
		if (numParameters.length != recipe.numParameters.length)
			return false;
		for (int i = 0; i < numParameters.length; i++) {
			if (numParameters[i] < recipe.numParameters[i])
				return false;
		}
		return true;
	}

	public float[] getNumParameters() {
		return numParameters;
	}

	public List<ItemStack> getItemOutputs() {
		List<ItemStack> stacks = new ArrayList();
		for (RecipeObject recipeObject : outputs) {
			if (recipeObject.getRepresentativeObject() instanceof ItemStack)
				stacks.add((ItemStack) recipeObject.getRepresentativeObject());
			else if (recipeObject.getRepresentativeObject() instanceof List<?>) {
				List<ItemStack> outs = (List<ItemStack>) recipeObject.getRepresentativeObject();
				if (outs.size() > 0) {
					ItemStack stack = outs.get(0).copy();
					if (stack.getMetadata() == OreDictionary.WILDCARD_VALUE)
						stack.setItemDamage(0);
					stacks.add(stack);
				}
			}
		}
		return stacks;
	}

	public List<List<ItemStack>> getItemInputs() {
		List<List<ItemStack>> stacks = new ArrayList();
		for (RecipeObject recipeObject : inputs) {
			if (recipeObject.getRepresentativeObject() instanceof ItemStack)
				stacks.add(Collections.singletonList((ItemStack) recipeObject.getRepresentativeObject()));
			else if (recipeObject.getRepresentativeObject() instanceof List<?>)
				stacks.add((List<ItemStack>) recipeObject.getRepresentativeObject());
		}
		return stacks;
	}

	public List<FluidStack> getFluidOutputs() {
		List<FluidStack> stacks = new ArrayList();
		for (RecipeObject recipeObject : outputs) {
			if (recipeObject.getRepresentativeObject() instanceof FluidStack)
				stacks.add((FluidStack) recipeObject.getRepresentativeObject());
		}
		return stacks;
	}

	public List<FluidStack> getFluidInputs() {
		List<FluidStack> stacks = new ArrayList();
		for (RecipeObject recipeObject : inputs) {
			if (recipeObject.getRepresentativeObject() instanceof FluidStack)
				stacks.add((FluidStack) recipeObject.getRepresentativeObject());
		}
		return stacks;
	}

	public boolean isValid() {
		if (getItemInputs().size() == 0 && getFluidInputs().size() == 0)
			return false;
		if (getItemOutputs().size() == 0 && getFluidOutputs().size() == 0)
			return false;

		for (RecipeObject recipeObject : inputs) {

		}

		return true;
	}
}