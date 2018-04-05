package com.bartz24.varyingmachina.jei.machines;

import com.bartz24.varyingmachina.base.recipe.ProcessRecipe;
import com.bartz24.varyingmachina.jei.JEIHelper;
import com.bartz24.varyingmachina.jei.ProcessRecipeWrapper;
import com.bartz24.varyingmachina.machines.recipes.PresserProcessRecipe;
import com.bartz24.varyingmachina.machines.recipes.PresserProcessRecipe.PresserPattern;

import mezz.jei.api.IGuiHelper;

public class PresserProcessRecipeWrapper extends ProcessRecipeWrapper {

	public PresserProcessRecipeWrapper(IGuiHelper guiHelper, ProcessRecipe recipe) {
		super(guiHelper, recipe);
		if (recipe instanceof PresserProcessRecipe) {
			PresserPattern pattern = ((PresserProcessRecipe) recipe).getPattern();

			for (int y = 0; y < pattern.pattern.length; y++) {
				for (int x = 0; x < pattern.pattern[y].length; x++) {
					recipeSpecificDrawables.add(JEIHelper.createPattern(104 + 5 * x,
							26 - (5 * pattern.pattern.length / 2) + 5 * y, guiHelper, pattern.pattern[y][x] == 1));
				}
			}
		}
	}
}
