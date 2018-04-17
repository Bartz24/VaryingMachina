package com.bartz24.varyingmachina.machines.recipes;

import com.bartz24.varyingmachina.base.recipe.*;
import com.bartz24.varyingmachina.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import scala.actors.threadpool.Arrays;

import java.awt.*;

@ProcessRecipeRegistry
public class CombustionRecipes {

    /**
     * Num Params: [0] = HU [1] = Pressure
     */
    public static ProcessRecipeManager combustionRecipes = new ProcessRecipeManager("combustion") {
        public void drawJEIInfo(ProcessRecipe rec, Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX,
                                int mouseY) {
            String s = Integer.toString((int) rec.getNumParameters()[0]) + " HU";
            FontRenderer fontRendererObj = minecraft.fontRenderer;
            int stringWidth = fontRendererObj.getStringWidth(s);
            fontRendererObj.drawString(s, 72, 8, Color.red.getRGB());

            s = Integer.toString((int) rec.getNumParameters()[1]) + " kPa";
            fontRendererObj = minecraft.fontRenderer;
            stringWidth = fontRendererObj.getStringWidth(s);
            fontRendererObj.drawString(s, 72, 0, Color.yellow.getRGB());
        }
    };

    @ProcessRecipeRegistry
    public static void registerRecipes() {
        combustionRecipes.addRecipe(new RecipeItem(new ItemStack(Items.GUNPOWDER, 3)), new RecipeItem(new ItemStack(Items.FLINT)), 1120, 1875);
        combustionRecipes.addRecipe(new RecipeItem(new ItemStack(Items.DIAMOND, 1)), new RecipeItem(new ItemStack(Blocks.COAL_BLOCK)), 800, 3644);
//TODO Plant matter, slime ball
        combustionRecipes.addRecipe(new RecipeItem(new ItemStack(ModItems.darkmatter)), Arrays.asList(new RecipeObject[]{new RecipeListOreDict(6, "ingotRefinedObsidian", "ingotTungstensteel", "ingotEnderium", "ingotEndSteel"), new RecipeListOreDict(11, "ingotTitanium", "ingotPlatinum", "ingotSoularium", "ingotDarkSteel"), new RecipeListOreDict(15, "ingotInvar", "ingotElectricalSteel", "ingotSteel", "ingotDarkSteel"), new RecipeOreDictPriority(6, "dustObsidian", "dustCoal")}), 3678, 2476);
        combustionRecipes.addRecipe(new RecipeItem(new ItemStack(ModItems.lightmatter)), Arrays.asList(new RecipeObject[]{new RecipeListOreDict(6, "ingotVibrantAlloy", "ingotLumium"), new RecipeListOreDict(11, "ingotSignalum", "ingotPulsatingIron", "ingotOsmium", "ingotConstantan"), new RecipeListOreDict(15, "ingotBronze", "ingotElectrum", "ingotCobalt", "ingotAluminium"), new RecipeOreDictPriority(6, "dustAerotheum", "dustSaltpeter")}), 4592, 1145);
    }
}
