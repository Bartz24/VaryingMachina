package com.bartz24.varyingmachina.machines.recipes;

import com.bartz24.varyingmachina.base.recipe.ProcessRecipe;
import com.bartz24.varyingmachina.base.recipe.ProcessRecipeManager;
import com.bartz24.varyingmachina.base.recipe.ProcessRecipeRegistry;
import com.bartz24.varyingmachina.base.recipe.RecipeItem;
import com.bartz24.varyingmachina.base.recipe.RecipeOreDict;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

@ProcessRecipeRegistry
public class SmelterRecipes {

	/**
	 * Num Params: [0] = HU [1] = Time [2] = Pressure
	 */
	public static ProcessRecipeManager smelterRecipes = new ProcessRecipeManager("smelter") {
		public void drawJEIInfo(ProcessRecipe rec, Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX,
				int mouseY) {
			String s = Integer.toString((int) rec.getNumParameters()[0]) + " HU";
			FontRenderer fontRendererObj = minecraft.fontRenderer;
			int stringWidth = fontRendererObj.getStringWidth(s);
			fontRendererObj.drawString(s, 24 - stringWidth, 17, java.awt.Color.red.getRGB());

			s = Integer.toString((int) rec.getNumParameters()[2]) + " kPa";
			fontRendererObj = minecraft.fontRenderer;
			stringWidth = fontRendererObj.getStringWidth(s);
			fontRendererObj.drawString(s, 24 - stringWidth, 27, java.awt.Color.yellow.getRGB());

			s = Integer.toString((int) rec.getNumParameters()[1]) + " ticks";
			fontRendererObj = minecraft.fontRenderer;
			stringWidth = fontRendererObj.getStringWidth(s);
			fontRendererObj.drawString(s, 72 - stringWidth / 2, 8, java.awt.Color.blue.getRGB());
		}
	};

	@ProcessRecipeRegistry
	public static void registerRecipes() {
		for (ItemStack stack : FurnaceRecipes.instance().getSmeltingList().keySet()) {
			smelterRecipes.addRecipe(new RecipeItem(FurnaceRecipes.instance().getSmeltingResult(stack)),
					new RecipeItem(stack), 0, 100, 0);
		}
		smelterRecipes.addRecipe(new RecipeOreDict("ingotSteel", 1), new RecipeOreDict("dustSteel", 1), 1370, 12000,
				890);
		smelterRecipes.addRecipe(new RecipeOreDict("ingotSignalum", 1), new RecipeOreDict("dustSignalum", 1), 1362, 1200,
				766);
		smelterRecipes.addRecipe(new RecipeOreDict("ingotLumium", 1), new RecipeOreDict("dustLumium", 1), 1160, 1200,
				1025);
		smelterRecipes.addRecipe(new RecipeOreDict("ingotEnderium", 1), new RecipeOreDict("dustEnderium", 1), 2164, 3600,
				1464);

	}
}
