package com.bartz24.varyingmachina.machines.recipes;

import com.bartz24.varyingmachina.base.recipe.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.awt.*;
import java.util.Arrays;

@ProcessRecipeRegistry
public class ExtractorRecipes {

    /**
     * Num Params: [0] = Time [1] = Min Production [2] = Max Production
     */
    public static ProcessRecipeManager extractorRecipes = new ProcessRecipeManager("extractor") {
        public void drawJEIInfo(ProcessRecipe rec, Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX,
                                int mouseY) {
            String s = Integer.toString((int) rec.getNumParameters()[0]) + " ticks";
            FontRenderer fontRendererObj = minecraft.fontRenderer;
            int stringWidth = fontRendererObj.getStringWidth(s);
            fontRendererObj.drawString(s, 112 - stringWidth, 8, java.awt.Color.blue.getRGB());

            s = Integer.toString((int) (rec.getNumParameters()[1] * 100f)) + " % - "
                    + Integer.toString((int) (rec.getNumParameters()[2] * 100f)) + " %";
            fontRendererObj = minecraft.fontRenderer;
            stringWidth = fontRendererObj.getStringWidth(s);
            fontRendererObj.drawString(s, 72 - stringWidth / 2, 36, new Color(23, 180, 4).getRGB());
        }
    };

    @ProcessRecipeRegistry
    public static void registerRecipes() {
        extractorRecipes.addRecipe(new RecipeFluid(new FluidStack(FluidRegistry.getFluid("redstone"), 100)), Arrays.asList(new RecipeObject[]{new RecipeOreDict("dustRedstone", 1)}), 400, 1f, 1f);
        extractorRecipes.addRecipe(new RecipeFluid(new FluidStack(FluidRegistry.getFluid("redstone"), 900)), Arrays.asList(new RecipeObject[]{new RecipeOreDict("blockRedstone", 1)}), 3200, 1f, 1f);
        extractorRecipes.addRecipe(new RecipeFluid(new FluidStack(FluidRegistry.getFluid("glowstone"), 250)), Arrays.asList(new RecipeObject[]{new RecipeOreDict("dustGlowstone", 1)}), 800, 1f, 1f);
        extractorRecipes.addRecipe(new RecipeFluid(new FluidStack(FluidRegistry.getFluid("glowstone"), 1000)), Arrays.asList(new RecipeObject[]{new RecipeOreDict("glowstone", 1)}), 3000, 1f, 1f);
        extractorRecipes.addRecipe(new RecipeFluid(new FluidStack(FluidRegistry.getFluid("ender"), 250)), Arrays.asList(new RecipeObject[]{new RecipeItem(new ItemStack(Items.ENDER_PEARL))}), 900, 1f, 1f);
        extractorRecipes.addRecipe(new RecipeOreDict("dustSaltpeter", 1), Arrays.asList(new RecipeObject[]{new RecipeOreDict("sand", 1)}), 620, .2f, 2f);
        extractorRecipes.addRecipe(new RecipeOreDict("dustSulfur", 1), Arrays.asList(new RecipeObject[]{new RecipeItem(new ItemStack(Blocks.SOUL_SAND))}), 490, .2f, 2f);

    }
}
