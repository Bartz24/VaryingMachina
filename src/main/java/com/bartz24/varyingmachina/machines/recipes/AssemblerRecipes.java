package com.bartz24.varyingmachina.machines.recipes;

import java.util.Arrays;

import com.bartz24.varyingmachina.References;
import com.bartz24.varyingmachina.base.machine.MachineVariant;
import com.bartz24.varyingmachina.base.recipe.*;
import com.bartz24.varyingmachina.registry.ModBlocks;
import com.bartz24.varyingmachina.registry.ModItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

@ProcessRecipeRegistry
public class AssemblerRecipes {

    /**
     * Num Params: [0] = Time
     */
    public static ProcessRecipeManager assemblerRecipes = new ProcessRecipeManager("assembler") {
        public void drawJEIInfo(ProcessRecipe rec, Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX,
                                int mouseY) {
            String s = Integer.toString((int) rec.getNumParameters()[0]) + " ticks";
            FontRenderer fontRendererObj = minecraft.fontRenderer;
            int stringWidth = fontRendererObj.getStringWidth(s);
            fontRendererObj.drawString(s, 72, 8, java.awt.Color.blue.getRGB());
        }
    };

    @ProcessRecipeRegistry
    public static void registerRecipes() {

        for (MachineVariant variant : MachineVariant.REGISTRY.getValuesCollection()) {
            float tierRate = 2.44f;
            assemblerRecipes.addRecipe(
                    new RecipeItem(MachineVariant.writeVariantToStack(new ItemStack(ModBlocks.casing), variant)),
                    Arrays.asList(variant.getRodRecipeItem(12), variant.getGearRecipeItem(1), getGlassItem(variant.getMachineTier())),
                    (float) (180f * Math.pow(tierRate, variant.getMachineTier())));
            assemblerRecipes.addRecipe(
                    new RecipeItem(MachineVariant.writeVariantToStack(new ItemStack(ModItems.smelter), variant)),
                    Arrays.asList(variant.getPlateRecipeItem(14),
                            getHeatItem(variant.getMachineTier()), getCircuitItem(variant.getMachineTier())),
                    (float) (200f * Math.pow(tierRate, variant.getMachineTier())));
            assemblerRecipes.addRecipe(
                    new RecipeItem(MachineVariant.writeVariantToStack(new ItemStack(ModItems.grinder), variant)),
                    Arrays.asList(variant.getPlateRecipeItem(16), getGrinderItem(variant.getMachineTier()), getCircuitItem(variant.getMachineTier())),
                    (float) (232f * Math.pow(tierRate, variant.getMachineTier())));
            assemblerRecipes.addRecipe(
                    new RecipeItem(MachineVariant.writeVariantToStack(new ItemStack(ModItems.presser), variant)),
                    Arrays.asList(variant.getPlateRecipeItem(16),
                            new RecipeItem(new ItemStack(Blocks.PISTON)), getCircuitItem(variant.getMachineTier())),
                    (float) (368f * Math.pow(tierRate, variant.getMachineTier())));
            assemblerRecipes.addRecipe(
                    new RecipeItem(MachineVariant.writeVariantToStack(new ItemStack(ModItems.mixer), variant)),
                    Arrays.asList(variant.getPlateRecipeItem(18),
                            getGlassItem(variant.getMachineTier()), getCircuitItem(variant.getMachineTier())),
                    (float) (312f * Math.pow(tierRate, variant.getMachineTier())));
            assemblerRecipes.addRecipe(
                    new RecipeItem(MachineVariant.writeVariantToStack(new ItemStack(ModItems.combustion), variant)),
                    Arrays.asList(variant.getPlateRecipeItem(22),
                            getHeatItem(variant.getMachineTier()), getSuperHeatItem(variant.getMachineTier()), getCircuitItem(variant.getMachineTier())),
                    (float) (418f * Math.pow(tierRate, variant.getMachineTier())));
            assemblerRecipes.addRecipe(
                    new RecipeItem(MachineVariant.writeVariantToStack(new ItemStack(ModItems.itembuffer), variant)),
                    Arrays.asList(variant.getPlateRecipeItem(8),
                            new RecipeItem(new ItemStack(Blocks.CHEST)), getCircuitItem(variant.getMachineTier())),
                    (float) (176f * Math.pow(tierRate, variant.getMachineTier())));

            assemblerRecipes.addRecipe(
                    new RecipeItem(MachineVariant.writeVariantToStack(new ItemStack(ModItems.regulator), variant)),
                    Arrays.asList(variant.getPlateRecipeItem(4),
                            getGlassItem(variant.getMachineTier())),
                    (float) (90f * Math.pow(tierRate, variant.getMachineTier())));

            assemblerRecipes.addRecipe(
                    new RecipeItem(MachineVariant.writeVariantToStack(new ItemStack(ModItems.inserter), variant)),
                    Arrays.asList(variant.getPlateRecipeItem(4),
                            new RecipeItem(new ItemStack(Blocks.HOPPER))),
                    (float) (60f * Math.pow(tierRate, variant.getMachineTier())));

            assemblerRecipes.addRecipe(
                    new RecipeItem(MachineVariant.writeVariantToStack(new ItemStack(ModItems.remover), variant)),
                    Arrays.asList(variant.getPlateRecipeItem(4),
                            new RecipeItem(new ItemStack(Blocks.DROPPER))),
                    (float) (60f * Math.pow(tierRate, variant.getMachineTier())));

            assemblerRecipes.addRecipe(
                    new RecipeItem(MachineVariant.writeVariantToStack(new ItemStack(ModItems.bellow), variant)),
                    Arrays.asList(variant.getPlateRecipeItem(4),
                            new RecipeItem(new ItemStack(Items.LEATHER))),
                    (float) (110f * Math.pow(tierRate, variant.getMachineTier())));

            assemblerRecipes.addRecipe(
                    new RecipeItem(MachineVariant.writeVariantToStack(new ItemStack(ModItems.gearbox), variant)),
                    Arrays.asList(variant.getPlateRecipeItem(4),
                            variant.getGearRecipeItem(1)),
                    (float) (110f * Math.pow(tierRate, variant.getMachineTier())));

            assemblerRecipes.addRecipe(
                    new RecipeItem(MachineVariant.writeVariantToStack(new ItemStack(ModItems.worldinserter), variant)),
                    Arrays.asList(variant.getPlateRecipeItem(4),
                            new RecipeItem(new ItemStack(Blocks.HOPPER)),
                            new RecipeItem(new ItemStack(Items.ENDER_EYE, 2))),
                    (float) (180f * Math.pow(tierRate, variant.getMachineTier())));

            assemblerRecipes.addRecipe(
                    new RecipeItem(MachineVariant.writeVariantToStack(new ItemStack(ModItems.worldremover), variant)),
                    Arrays.asList(variant.getPlateRecipeItem(4),
                            new RecipeItem(new ItemStack(Blocks.DROPPER)),
                    new RecipeItem(new ItemStack(Items.ENDER_EYE, 2))),
                    (float) (180f * Math.pow(tierRate, variant.getMachineTier())));
        }


        assemblerRecipes.addRecipe(
                getCircuitItem(1),
                Arrays.asList(new RecipeItem(new ItemStack(Blocks.STONE, 4)),
                        new RecipeItem(new ItemStack(Items.GUNPOWDER, 4)),
                        new RecipeItem(new ItemStack(Items.CLAY_BALL, 6))),
                80f);

        assemblerRecipes.addRecipe(
                getCircuitItem(2),
                Arrays.asList(getCircuitItem(1),
                        new RecipeListOreDict(4, "plateBronze", "plateLead", "plateIron"),
                        new RecipeItem(new ItemStack(Items.BLAZE_POWDER, 3)),
                        new RecipeListOreDict(2, "wireCopper", "wireTin", "wireGold")),
                220f);

        assemblerRecipes.addRecipe(
                getCircuitItem(3),
                Arrays.asList(getCircuitItem(2),
                        new RecipeListOreDict(4, "plateElectricalSteel", "plateConstantan", "plateInvar", "plateSteel"),
                        new RecipeItem(new ItemStack(Items.REDSTONE, 8)),
                        new RecipeListOreDict(6, "wireEnergeticAlloy", "wireOsmium", "wireSilver", "wireConductiveIron", "wireAluminium")),
                590f);

        assemblerRecipes.addRecipe(
                getCircuitItem(4),
                Arrays.asList(getCircuitItem(3),
                        new RecipeListOreDict(4, "plateSoularium", "plateManyullyn", "plateTitanium", "platePlatinum"),
                        new RecipeItem(new ItemStack(Items.GLOWSTONE_DUST, 12)),
                        new RecipeListOreDict(6, "wireSignalum", "wireCobalt", "wirePulsatingIron", "wireDiamond")),
                1610f);

        assemblerRecipes.addRecipe(
                getCircuitItem(5),
                Arrays.asList(getCircuitItem(4),
                        new RecipeListOreDict(4, "plateRefinedObsidian", "plateTungstensteel", "plateEnderium", "plateDarkSteel", "plateEndSteel"),
                        new RecipeOreDictPriority(8, "dustAerotheum", "dustCoal", "dustGlowstone"),
                        new RecipeListOreDict(6, "wireVibrantAlloy", "wireLumium", "wireEmerald")),
                4390f);

        assemblerRecipes.addRecipe(
                getCircuitItem(6),
                Arrays.asList(getCircuitItem(5),
                        new RecipeItem(new ItemStack(ModItems.lightmatter, 4)),
                        new RecipeOreDictPriority(12, "dustCryotheum", "dustCoal", "dustGlowstone"),
                        new RecipeItem(new ItemStack(ModItems.darkmatter, 6))),
                11930f);
    }

    private static RecipeObject getHeatItem(int tier) {
        switch (tier) {
            case 0:
                return new RecipeItem(new ItemStack(Items.GUNPOWDER));
            case 1:
                return new RecipeItem(new ItemStack(Items.GUNPOWDER, 2));
            case 2:
                return new RecipeItem(new ItemStack(Items.BLAZE_POWDER));
            case 3:
                return new RecipeItem(new ItemStack(Blocks.REDSTONE_BLOCK));
            case 4:
                return new RecipeItem(new ItemStack(Blocks.MAGMA));
            case 5:
                return new RecipeOreDictPriority(2, "dustPyrotheum", "obsidian");
            case 6:
                return new RecipeOreDictPriority(6, "dustPyrotheum", "obsidian");
            default:
                return new RecipeItem(new ItemStack(Items.GUNPOWDER));
        }
    }

    private static RecipeObject getSuperHeatItem(int tier) {
        switch (tier) {
            case 0:
                return new RecipeItem(new ItemStack(Items.COAL, 1, 1));
            case 1:
                return new RecipeItem(new ItemStack(Items.COAL, 2, 1));
            case 2:
                return new RecipeItem(new ItemStack(Items.GLOWSTONE_DUST));
            case 3:
                return new RecipeOreDictPriority(2, "dustCharcoal", "dustCoal");
            case 4:
                return new RecipeItem(new ItemStack(Blocks.OBSIDIAN));
            case 5:
                return new RecipeOreDictPriority(2, "dustCoke", "dustObsidian");
            case 6:
                return new RecipeOreDictPriority(4, "dustPetrotheum", "dustObsidian");
            default:
                return new RecipeItem(new ItemStack(Items.COAL, 1, 1));
        }
    }

    private static RecipeObject getGrinderItem(int tier) {
        switch (tier) {
            case 0:
                return new RecipeItem(new ItemStack(Items.FLINT));
            case 1:
                return new RecipeItem(new ItemStack(Items.FLINT, 2));
            case 2:
                return new RecipeItem(new ItemStack(Items.FLINT, 4));
            case 3:
                return new RecipeOreDictPriority(1, "gearDiamond", "gemDiamond");
            case 4:
                return new RecipeOreDictPriority(2, "gearDiamond", "gemDiamond");
            case 5:
                return new RecipeOreDictPriority(4, "gearDiamond", "gemDiamond");
            case 6:
                return new RecipeOreDictPriority(8, "gearDiamond", "gemDiamond");
            default:
                return new RecipeItem(new ItemStack(Items.FLINT));
        }
    }


    private static RecipeObject getCircuitItem(int tier) {
        if (tier >= 1 && tier <= 6)
            return new RecipeItem(new ItemStack(ModItems.circuit, 1, tier - 1));
        return new RecipeItem(new ItemStack(Blocks.WOODEN_SLAB, 1, OreDictionary.WILDCARD_VALUE));
    }

    private static RecipeObject getGlassItem(int tier) {
        switch (tier) {
            case 0:
                return new RecipeOreDict("sand", 1);
            case 1:
                return new RecipeOreDict("blockGlass", 1);
            case 2:
                return new RecipeOreDict("blockGlass", 1);
            case 3:
                return new RecipeNameItemPriority(1, new ResourceLocation[]{new ResourceLocation("thermalfoundation", "glass"), new ResourceLocation("glass")}, 3, 0);
            case 4:
                return new RecipeNameItemPriority(1, new ResourceLocation[]{new ResourceLocation("thermalfoundation", "glass"), new ResourceLocation("glass")}, 5, 0);
            case 5:
                return new RecipeNameItemPriority(1, new ResourceLocation[]{new ResourceLocation("thermalfoundation", "glass"), new ResourceLocation("glass")}, 4, 0);
            case 6:
                return new RecipeNameItemPriority(1, new ResourceLocation[]{new ResourceLocation("thermalfoundation", "glass"), new ResourceLocation("glass")}, 6, 0);
            default:
                return new RecipeOreDict("blockGlass", 1);
        }
    }
}
