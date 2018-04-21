package com.bartz24.varyingmachina.machines.recipes;

import java.awt.Color;

import com.bartz24.varyingmachina.base.recipe.ProcessRecipe;
import com.bartz24.varyingmachina.base.recipe.ProcessRecipeManager;
import com.bartz24.varyingmachina.base.recipe.ProcessRecipeRegistry;
import com.bartz24.varyingmachina.base.recipe.RecipeItem;
import com.bartz24.varyingmachina.base.recipe.RecipeObject;
import com.bartz24.varyingmachina.base.recipe.RecipeOreDict;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

@ProcessRecipeRegistry
public class GrinderRecipes {

    /**
     * Num Params: [0] = Speed [1] = Min Production [2] = Max Production
     */
    public static ProcessRecipeManager grinderRecipes = new ProcessRecipeManager("grinder") {
        public void drawJEIInfo(ProcessRecipe rec, Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX,
                                int mouseY) {
            String s = Integer.toString((int) (rec.getNumParameters()[0] * 1000f));
            FontRenderer fontRendererObj = minecraft.fontRenderer;
            int stringWidth = fontRendererObj.getStringWidth(s);
            fontRendererObj.drawString(s, 24 - stringWidth, 17, Color.blue.getRGB());

            s = "rad/s";
            fontRendererObj = minecraft.fontRenderer;
            stringWidth = fontRendererObj.getStringWidth(s);
            fontRendererObj.drawString(s, 24 - stringWidth, 27, Color.blue.getRGB());

            s = Integer.toString((int) (rec.getNumParameters()[1] * 100f)) + " % - "
                    + Integer.toString((int) (rec.getNumParameters()[2] * 100f)) + " %";
            fontRendererObj = minecraft.fontRenderer;
            stringWidth = fontRendererObj.getStringWidth(s);
            fontRendererObj.drawString(s, 72 - stringWidth / 2, 8, new Color(23, 180, 4).getRGB());
        }
    };

    @ProcessRecipeRegistry
    public static void registerRecipes() {
        for (String ore : OreDictionary.getOreNames()) {

            if (ore.startsWith("ore")) {
                for (String dust : OreDictionary.getOreNames()) {
                    if (!ore.substring(3, ore.length()).equals("Aluminum") && dust.equals("dust" + ore.substring(3, ore.length())))
                        addOreToOreRecipe(dust, ore, 5.2f, 1f, 3f);
                }
                for (String gem : OreDictionary.getOreNames()) {
                    if (gem.equals("gem" + ore.substring(3, ore.length())))
                        addOreToOreRecipe(gem, ore, 7.75f, 1f, 5f);
                }
            }
            if (ore.startsWith("dust")) {
                for (String gem : OreDictionary.getOreNames()) {
                    if (gem.equals("gem" + ore.substring(4, ore.length())))
                        addOreToOreRecipe(ore, gem, 3.5f, 1f, 1f);
                }
                for (String ingot : OreDictionary.getOreNames()) {
                    if (!ore.substring(4, ore.length()).equals("Aluminum") && ingot.equals("ingot" + ore.substring(4, ore.length())))
                        addOreToOreRecipe(ore, ingot, 2f, 1f, 1f);
                }
            }
        }

        addOreToOreRecipe("gravel", "cobblestone", 1.6f, 1f, 1f);
        addOreToOreRecipe("sand", "gravel", 1.2f, 1f, 1f);
        addOreToOreRecipe("dustWood", "logWood", 0.9f, 0f, 9f);
        addOreToObjectRecipe(new RecipeItem(new ItemStack(Items.FLINT)), "gravel", 1.9f, 0f, 1f);
        addOreToOreRecipe("dustObsidian", "obsidian", 11.5f, 4f, 4f);
        addRecipe(new RecipeOreDict("dustCoal", 1), new RecipeItem(new ItemStack(Items.COAL)), 1.75f, 1f, 1f);
        addRecipe(new RecipeOreDict("dustCharcoal", 1), new RecipeItem(new ItemStack(Items.COAL, 1, 1)), 1.75f, 1f, 1f);
        addRecipe(new RecipeItem(new ItemStack(Items.BLAZE_POWDER)), new RecipeItem(new ItemStack(Items.BLAZE_ROD)),
                3f, 2f, 5f);
        addRecipe(new RecipeItem(new ItemStack(Items.SUGAR)), new RecipeItem(new ItemStack(Items.REEDS)),
                1f, 2f, 4f);
        addRecipe(new RecipeItem(new ItemStack(Items.DYE, 1, 15)), new RecipeItem(new ItemStack(Items.BONE)),
                1.4f, 3f, 8f);

    }

    private static void addRecipe(RecipeObject out, RecipeObject in, float hardness, float prod, float maxProd) {
        grinderRecipes.addRecipe(out, in, hardnessToSpeed(hardness), prod, maxProd);
    }

    private static void addOreToObjectRecipe(RecipeObject out, String in, float hardness, float prod, float maxProd) {
        grinderRecipes.addRecipe(out, new RecipeOreDict(in, 1), hardnessToSpeed(hardness), prod, maxProd);
    }

    private static void addOreToOreRecipe(String out, String in, float hardness, float prod, float maxProd) {
        grinderRecipes.addRecipe(new RecipeOreDict(out, 1), new RecipeOreDict(in, 1),
                hardnessToSpeed(hardness), prod, maxProd);
    }

    private static float hardnessToSpeed(float val) {
        return (float) Math.pow(Math.sqrt(val * .4f), 2.7f);
    }

    private static float getHardnessOfStack(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof ItemBlock) {
            Block block = ((ItemBlock) stack.getItem()).getBlock();
            IBlockState state = block.getStateFromMeta(stack.getMetadata());
            try {
                return state.getBlockHardness(null, null) * (block.getHarvestLevel(state) + 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 1.75f;
    }

    private static float getHardnessOfOre(String ore) {
        if (OreDictionary.getOres(ore).size() > 0)
            return getHardnessOfStack(OreDictionary.getOres(ore).get(0));
        return 1.75f;
    }
}
