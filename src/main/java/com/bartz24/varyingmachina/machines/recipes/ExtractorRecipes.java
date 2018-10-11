package com.bartz24.varyingmachina.machines.recipes;

import com.bartz24.varyingmachina.base.recipe.*;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;

import java.awt.*;
import java.util.Arrays;

@ZenClass("mods.varyingmachina.extractor")
@ZenRegister
@ProcessRecipeRegistry
public class ExtractorRecipes extends RecipeManager {

    /**
     * Num Params: [0] = Time [1] = Min Production [2] = Max Production [3] HU
     */
    public ExtractorRecipes() {
        super(new ProcessRecipeManager("extractor") {
            public void drawJEIInfo(ProcessRecipe rec, Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX,
                                    int mouseY) {
                String s = Integer.toString((int) rec.getNumParameters()[3]) + " HU";
                FontRenderer fontRendererObj = minecraft.fontRenderer;
                int stringWidth = fontRendererObj.getStringWidth(s);
                fontRendererObj.drawString(s, 24 - stringWidth, 17, java.awt.Color.red.getRGB());

                s = Integer.toString((int) rec.getNumParameters()[0]) + " ticks";
                fontRendererObj = minecraft.fontRenderer;
                stringWidth = fontRendererObj.getStringWidth(s);
                fontRendererObj.drawString(s, 24 - stringWidth, 27, java.awt.Color.blue.getRGB());

                s = Integer.toString((int) (rec.getNumParameters()[1] * 100f)) + " % - "
                        + Integer.toString((int) (rec.getNumParameters()[2] * 100f)) + " %";
                fontRendererObj = minecraft.fontRenderer;
                stringWidth = fontRendererObj.getStringWidth(s);
                fontRendererObj.drawString(s, 72 - stringWidth / 2, 36, new Color(23, 180, 4).getRGB());
            }
        });
    }

    @ProcessRecipeRegistry
    public void registerRecipes() {
        addRecipe(new RecipeFluid(new FluidStack(FluidRegistry.getFluid("redstone"), 100)), Arrays.asList(new RecipeObject[]{new RecipeOreDict("dustRedstone", 1)}), 400, 1f, 1f, 1680);
        addRecipe(new RecipeFluid(new FluidStack(FluidRegistry.getFluid("redstone"), 900)), Arrays.asList(new RecipeObject[]{new RecipeOreDict("blockRedstone", 1)}), 3200, 1f, 1f, 1890);
        addRecipe(new RecipeFluid(new FluidStack(FluidRegistry.getFluid("glowstone"), 250)), Arrays.asList(new RecipeObject[]{new RecipeOreDict("dustGlowstone", 1)}), 800, 1f, 1f, 1740);
        addRecipe(new RecipeFluid(new FluidStack(FluidRegistry.getFluid("glowstone"), 1000)), Arrays.asList(new RecipeObject[]{new RecipeOreDict("glowstone", 1)}), 3000, 1f, 1f, 2020);
        addRecipe(new RecipeFluid(new FluidStack(FluidRegistry.getFluid("ender"), 250)), Arrays.asList(new RecipeObject[]{new RecipeItem(new ItemStack(Items.ENDER_PEARL))}), 900, 1f, 1f, 2100);
        addRecipe(new RecipeOreDict("dustSaltpeter", 1), Arrays.asList(new RecipeObject[]{new RecipeOreDict("sand", 1)}), 620, .2f, 2f, 80);
        addRecipe(new RecipeOreDict("dustSulfur", 1), Arrays.asList(new RecipeObject[]{new RecipeItem(new ItemStack(Blocks.SOUL_SAND))}), 490, .2f, 2f, 112);
    }

    @Optional.Method(modid = "crafttweaker")
    @ZenMethod()
    @ZenSetter("addRecipe")
    public void addCTRecipe(IIngredient output, ILiquidStack output1, ILiquidStack output2, IIngredient[] inputs, ILiquidStack inputFluid, int time, float minProduction, float maxProduction, int huRequired) {
        addCTRecipe(new IIngredient[]{output}, new ILiquidStack[]{output1, output2}, inputs, new ILiquidStack[]{inputFluid}, time, minProduction, maxProduction, huRequired);
    }

    @Optional.Method(modid = "crafttweaker")
    @ZenMethod()
    @ZenSetter("removeRecipe")
    public void removeCTRecipe(IIngredient output) {
        removeCTRecipe(new IIngredient[]{output});
    }
}
