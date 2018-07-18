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
public class MixerRecipes {

    /**
     * Num Params: [0] = Speed [1] = Pressure
     */
    public static ProcessRecipeManager mixerRecipes = new ProcessRecipeManager("mixer") {
        public void drawJEIInfo(ProcessRecipe rec, Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX,
                                int mouseY) {
            String s = Integer.toString((int) (rec.getNumParameters()[0] * 1000f)) + " rad/s";
            FontRenderer fontRendererObj = minecraft.fontRenderer;
            int stringWidth = fontRendererObj.getStringWidth(s);
            fontRendererObj.drawString(s, 72, 8, Color.blue.getRGB());

            s = Integer.toString((int) rec.getNumParameters()[1]) + " kPa";
            fontRendererObj = minecraft.fontRenderer;
            stringWidth = fontRendererObj.getStringWidth(s);
            fontRendererObj.drawString(s, 72, 0, java.awt.Color.yellow.getRGB());
        }
    };

    @ProcessRecipeRegistry
    public static void registerRecipes() {
        mixerRecipes.addRecipe(new RecipeOreDict("dustBronze", 4), Arrays.asList(new RecipeObject[]{new RecipeOreDict("dustCopper", 3), new RecipeOreDict("dustTin", 1)}), 0.95f, 245);
        mixerRecipes.addRecipe(new RecipeOreDict("dustInvar", 3), Arrays.asList(new RecipeObject[]{new RecipeOreDict("dustIron", 2), new RecipeOreDict("dustNickel", 1)}), 1.55f, 586);
        mixerRecipes.addRecipe(new RecipeOreDict("dustConstantan", 2), Arrays.asList(new RecipeObject[]{new RecipeOreDict("dustCopper", 1), new RecipeOreDict("dustNickel", 1)}), 1.33f, 867);
        mixerRecipes.addRecipe(new RecipeOreDict("dustElectrum", 2), Arrays.asList(new RecipeObject[]{new RecipeOreDict("dustSilver", 1), new RecipeOreDict("dustGold", 1)}), 1.77f, 1252);
        mixerRecipes.addRecipe(new RecipeOreDict("dustSteel", 1), Arrays.asList(new RecipeObject[]{new RecipeOreDict("dustCoal", 4), new RecipeOreDict("dustIron", 1)}), 2.15f, 1053);
        mixerRecipes.addRecipe(new RecipeOreDict("dustLumium", 4), Arrays.asList(new RecipeObject[]{new RecipeFluid(new FluidStack(FluidRegistry.getFluid("glowstone"), 1000)), new RecipeOreDict("dustTin", 3), new RecipeOreDict("dustSilver", 1)}), 4.23f, 1769);
        mixerRecipes.addRecipe(new RecipeOreDict("dustSignalum", 4), Arrays.asList(new RecipeObject[]{new RecipeFluid(new FluidStack(FluidRegistry.getFluid("redstone"), 1000)), new RecipeOreDict("dustCopper", 3), new RecipeOreDict("dustSilver", 1)}), 3.87f, 1544);
        mixerRecipes.addRecipe(new RecipeOreDict("dustEnderium", 4), Arrays.asList(new RecipeObject[]{new RecipeFluid(new FluidStack(FluidRegistry.getFluid("ender"), 1000)), new RecipeOreDict("dustLead", 3), new RecipeOreDict("dustPlatinum", 1)}), 7.42f, 2864);
        mixerRecipes.addRecipe(new RecipeOreDict("dustBlitz", 1), Arrays.asList(new RecipeObject[]{new RecipeFluid(new FluidStack(FluidRegistry.getFluid("redstone"), 200)), new RecipeItem(new ItemStack(Blocks.SAND))}), 4.76f, 3165);
        mixerRecipes.addRecipe(new RecipeOreDict("dustBasalz", 1), Arrays.asList(new RecipeObject[]{new RecipeFluid(new FluidStack(FluidRegistry.getFluid("redstone"), 200)), new RecipeOreDict("dustObsidian", 1)}), 4.76f, 3165);
        mixerRecipes.addRecipe(new RecipeOreDict("dustBlizz", 1), Arrays.asList(new RecipeObject[]{new RecipeFluid(new FluidStack(FluidRegistry.getFluid("redstone"), 200)), new RecipeItem(new ItemStack(Items.SNOWBALL))}), 4.76f, 3165);
        mixerRecipes.addRecipe(new RecipeItem(new ItemStack(Items.BLAZE_POWDER)), Arrays.asList(new RecipeObject[]{new RecipeFluid(new FluidStack(FluidRegistry.getFluid("redstone"), 200)), new RecipeItem(new ItemStack(Items.GLOWSTONE_DUST))}), 4.76f, 3165);
        mixerRecipes.addRecipe(new RecipeOreDict("dustPyrotheum", 2), Arrays.asList(new RecipeObject[]{new RecipeFluid(new FluidStack(FluidRegistry.getFluid("redstone"), 100)), new RecipeItem(new ItemStack(Items.BLAZE_POWDER, 2)), new RecipeOreDict("dustSulfur", 1)}), 3.41f, 440);
        mixerRecipes.addRecipe(new RecipeOreDict("dustCryotheum", 2), Arrays.asList(new RecipeObject[]{new RecipeFluid(new FluidStack(FluidRegistry.getFluid("redstone"), 100)), new RecipeOreDict("dustBlizz", 2), new RecipeItem(new ItemStack(Items.SNOWBALL, 1))}), 3.41f, 440);
        mixerRecipes.addRecipe(new RecipeOreDict("dustAerotheum", 2), Arrays.asList(new RecipeObject[]{new RecipeFluid(new FluidStack(FluidRegistry.getFluid("redstone"), 100)), new RecipeOreDict("dustBlitz", 2), new RecipeOreDict("dustSaltpeter", 1)}), 3.41f, 440);
        mixerRecipes.addRecipe(new RecipeOreDict("dustPetrotheum", 2), Arrays.asList(new RecipeObject[]{new RecipeFluid(new FluidStack(FluidRegistry.getFluid("redstone"), 100)), new RecipeOreDict("dustBasalz", 2), new RecipeOreDict("dustObsidian", 1)}), 3.41f, 440);
    }
}
