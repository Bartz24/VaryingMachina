package com.bartz24.varyingmachina.machines.recipes;

import java.util.Collections;

import com.bartz24.varyingmachina.base.recipe.ProcessRecipe;
import com.bartz24.varyingmachina.base.recipe.ProcessRecipeManager;
import com.bartz24.varyingmachina.base.recipe.ProcessRecipeRegistry;
import com.bartz24.varyingmachina.base.recipe.RecipeOreDict;
import com.bartz24.varyingmachina.machines.recipes.PresserProcessRecipe.PresserPattern;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.oredict.OreDictionary;

@ProcessRecipeRegistry
public class PresserRecipes {

	/**
	 * Num Params: [0] = Pressure [1] = Time
	 */
	public static ProcessRecipeManager presserRecipes = new ProcessRecipeManager("presser") {
		public void drawJEIInfo(ProcessRecipe rec, Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX,
				int mouseY) {
			String s = Integer.toString((int) rec.getNumParameters()[0]) + " kPa";
			FontRenderer fontRendererObj = minecraft.fontRenderer;
			int stringWidth = fontRendererObj.getStringWidth(s);
			fontRendererObj.drawString(s, 42 - stringWidth / 2, 34, java.awt.Color.yellow.getRGB());

			s = Integer.toString((int) rec.getNumParameters()[1]) + " ticks";
			fontRendererObj = minecraft.fontRenderer;
			stringWidth = fontRendererObj.getStringWidth(s);
			fontRendererObj.drawString(s, 42 - stringWidth / 2, 8, java.awt.Color.blue.getRGB());
		}
	};

	@ProcessRecipeRegistry
	public static void registerRecipes() {

		for (String ore : OreDictionary.getOreNames()) {

			if (ore.startsWith("plate")) {
				for (String ingot : OreDictionary.getOreNames()) {
					if (ingot.equals("ingot" + ore.substring(5, ore.length())))
						presserRecipes.addRecipe(
								new PresserProcessRecipe(Collections.singletonList(new RecipeOreDict(ore, 1)),
										Collections.singletonList(new RecipeOreDict(ingot, 1)), "presser",
										PresserPattern.getDefaultPlatePattern(), 600, 800));
				}
				for (String gem : OreDictionary.getOreNames()) {
					if (gem.equals("gem" + ore.substring(5, ore.length())))
						presserRecipes.addRecipe(
								new PresserProcessRecipe(Collections.singletonList(new RecipeOreDict(ore, 1)),
										Collections.singletonList(new RecipeOreDict(gem, 1)), "presser",
										PresserPattern.getDefaultPlatePattern(), 600, 800));
				}
				for (String crystal : OreDictionary.getOreNames()) {
					if (crystal.equals("crystal" + ore.substring(5, ore.length())))
						presserRecipes.addRecipe(
								new PresserProcessRecipe(Collections.singletonList(new RecipeOreDict(ore, 1)),
										Collections.singletonList(new RecipeOreDict(crystal, 1)), "presser",
										PresserPattern.getDefaultPlatePattern(), 600, 800));
				}
			}

			if (ore.startsWith("rod")) {
				for (String ingot : OreDictionary.getOreNames()) {
					if (ingot.equals("ingot" + ore.substring(3, ore.length())))
						presserRecipes.addRecipe(
								new PresserProcessRecipe(Collections.singletonList(new RecipeOreDict(ore, 2)),
										Collections.singletonList(new RecipeOreDict(ingot, 1)), "presser",
										PresserPattern.getDefaultRodPattern(), 200, 200));
				}
				for (String gem : OreDictionary.getOreNames()) {
					if (gem.equals("gem" + ore.substring(3, ore.length())))
						presserRecipes.addRecipe(
								new PresserProcessRecipe(Collections.singletonList(new RecipeOreDict(ore, 2)),
										Collections.singletonList(new RecipeOreDict(gem, 1)), "presser",
										PresserPattern.getDefaultRodPattern(), 200, 200));
				}
				for (String crystal : OreDictionary.getOreNames()) {
					if (crystal.equals("crystal" + ore.substring(3, ore.length())))
						presserRecipes.addRecipe(
								new PresserProcessRecipe(Collections.singletonList(new RecipeOreDict(ore, 2)),
										Collections.singletonList(new RecipeOreDict(crystal, 1)), "presser",
										PresserPattern.getDefaultRodPattern(), 200, 200));
				}
			}
			if (ore.startsWith("gear")) {
				for (String ingot : OreDictionary.getOreNames()) {
					if (ingot.equals("ingot" + ore.substring(4, ore.length())))
						presserRecipes.addRecipe(
								new PresserProcessRecipe(Collections.singletonList(new RecipeOreDict(ore, 1)),
										Collections.singletonList(new RecipeOreDict(ingot, 4)), "presser",
										PresserPattern.getDefaultGearPattern(), 1440, 1200));
				}
				for (String gem : OreDictionary.getOreNames()) {
					if (gem.equals("gem" + ore.substring(4, ore.length())))
						presserRecipes.addRecipe(
								new PresserProcessRecipe(Collections.singletonList(new RecipeOreDict(ore, 1)),
										Collections.singletonList(new RecipeOreDict(gem, 4)), "presser",
										PresserPattern.getDefaultGearPattern(), 1440, 1200));
				}
				for (String crystal : OreDictionary.getOreNames()) {
					if (crystal.equals("crystal" + ore.substring(4, ore.length())))
						presserRecipes.addRecipe(
								new PresserProcessRecipe(Collections.singletonList(new RecipeOreDict(ore, 1)),
										Collections.singletonList(new RecipeOreDict(crystal, 4)), "presser",
										PresserPattern.getDefaultGearPattern(), 1440, 1200));
				}
			}
		}
	}
}
