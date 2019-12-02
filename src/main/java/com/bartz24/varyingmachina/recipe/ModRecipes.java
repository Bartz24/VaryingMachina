package com.bartz24.varyingmachina.recipe;

import com.bartz24.varyingmachina.Helpers;
import com.bartz24.varyingmachina.VaryingMachina;
import com.bartz24.varyingmachina.item.ItemBlockMachine;
import com.bartz24.varyingmachina.machine.MachineVariant;
import com.bartz24.varyingmachina.machine.ModMachines;
import com.bartz24.varyingmachina.machine.ModVariants;
import com.bartz24.varyingmachina.pixellib.PixelPlugin;
import com.emosewapixel.pixellib.materialsystem.addition.BaseMaterials;
import com.emosewapixel.pixellib.materialsystem.addition.BaseObjTypes;
import com.emosewapixel.pixellib.materialsystem.lists.MaterialBlocks;
import com.emosewapixel.pixellib.materialsystem.lists.MaterialItems;
import com.emosewapixel.pixellib.materialsystem.lists.Materials;
import com.emosewapixel.pixellib.materialsystem.main.IMaterialObject;
import com.emosewapixel.pixellib.materialsystem.main.Material;
import com.emosewapixel.pixellib.resources.RecipeMaker;
import com.emosewapixel.pixellib.resources.TagMaps;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ModRecipes {
    public static void setup() {


        /*for (Material mat : Materials.getAll()) {
            if (extract.isMaterialCompatible(mat)) {
                TagMaps.addItemToTag("ores/" + mat.getName(), MaterialItems.getItem(mat, extract));
            }
        }*/


        for (Material mat : Materials.getAll()) {
            for (Item item : MaterialItems.getAll()) {
                if (item instanceof IMaterialObject /*&& ((IMaterialItem) item).getMaterial().equals(mat)*/)
                    TagMaps.addMatItemToTag((IMaterialObject) item);
            }
            for (Block block : MaterialBlocks.getAll())
                if (block instanceof IMaterialObject /*&& ((IMaterialItem) block).getMaterial().equals(mat)*/)
                    TagMaps.addMatItemToTag((IMaterialObject) block);
        }

        for (Material mat : Materials.getAll()) {
            if (mat.getSimpleProcessing()) {
                if (mat.getHasOre()) {
                    if (mat.getHasDust()) {
                        ModMachines.grinder.addRecipe(new ResourceLocation("varyingmachina", "oretodust." + mat.getName()), new RecipeBase<>(new OutputItemChance(Helpers.createMaterialItemStack(mat, BaseObjTypes.DUST, 1), 1.0f, 3.0f)).addInputs(new InputItemTag(mat.getItemTag(BaseObjTypes.ORE), 1)).setTime(40));
                    }
                    if (mat.isIngotMaterial()) {
                        ModMachines.smelter.addRecipe(new ResourceLocation("varyingmachina", "oretoingot." + mat.getName()), new RecipeBase<>(new OutputItem(Helpers.createMaterialItemStack(mat, BaseObjTypes.INGOT, 1))).addInputs(new InputItemTag(mat.getItemTag(BaseObjTypes.ORE), 1)).setTime(60));
                        RecipeMaker.addFurnaceRecipe(new ResourceLocation("varyingmachina", mat.getName() + "_ingot_smelt"), mat.getHasOre(), Helpers.createMaterialItemStack(mat, BaseObjTypes.INGOT, 1).getItem());
                    }
                    if (mat.isGemMaterial()) {
                        ModMachines.smelter.addRecipe(new ResourceLocation("varyingmachina", "oretogem." + mat.getName()), new RecipeBase<>(new OutputItem(Helpers.createMaterialItemStack(mat, BaseObjTypes.GEM, 1))).addInputs(new InputItemTag(mat.getItemTag(BaseObjTypes.ORE), 1)).setTime(100));
                        ModMachines.smelter.addRecipe(new ResourceLocation("varyingmachina", "dusttogem." + mat.getName()), new RecipeBase<>(new OutputItem(Helpers.createMaterialItemStack(mat, BaseObjTypes.GEM, 1))).addInputs(new InputItemTag(mat.getItemTag(BaseObjTypes.DUST), 1)).setTime(1000).addReqs(new StatReq(ModMachines.Stats.pressure, (int) (Math.pow(1.5f, mat.getTier()) * 800), StatReq.StatReqComp.GREATEREQUAL)));
                    }
                }
            }

/*            if (PixelPlugin.gear.isMaterialCompatible(mat)) {
                if (mat instanceof IngotMaterial)
                    ModMachines.fabricator.addRecipe(new ResourceLocation("varyingmachina", "ingottogear." + mat.getName()), new RecipeBase<>(new OutputItem(Helpers.createMaterialItemStack(mat, PixelPlugin.gear, 1))).addInputs(new InputItemTag(mat.getTag(BaseObjTypes.INGOT), 4)).setTime(30));
                if (mat instanceof GemMaterial)
                    ModMachines.fabricator.addRecipe(new ResourceLocation("varyingmachina", "gemtogear." + mat.getName()), new RecipeBase<>(new OutputItem(Helpers.createMaterialItemStack(mat, PixelPlugin.gear, 1))).addInputs(new InputItemTag(mat.getTag(BaseObjTypes.GEM), 4)).setTime(60));
            }
            if (PixelPlugin.plate.isMaterialCompatible(mat)) {
                if (mat instanceof IngotMaterial)
                    ModMachines.fabricator.addRecipe(new ResourceLocation("varyingmachina", "ingottoplate." + mat.getName()), new RecipeBase<>(new OutputItem(Helpers.createMaterialItemStack(mat, PixelPlugin.plate, 1))).addInputs(new InputItemTag(mat.getTag(BaseObjTypes.INGOT), 1)).setTime(10));
            }
            if (PixelPlugin.cable.isMaterialCompatible(mat)) {
                if (mat instanceof IngotMaterial)
                    ModMachines.fabricator.addRecipe(new ResourceLocation("varyingmachina", "ingottocable." + mat.getName()), new RecipeBase<>(new OutputItem(Helpers.createMaterialItemStack(mat, PixelPlugin.cable, 3))).addInputs(new InputItemTag(mat.getTag(BaseObjTypes.INGOT), 1)).setTime(10));
            }

            if (BaseObjTypes.DUST.isMaterialCompatible(mat)) {
                if (mat instanceof IngotMaterial)
                    ModMachines.grinder.addRecipe(new ResourceLocation("varyingmachina", "ingottodust." + mat.getName()), new RecipeBase<>(new OutputItem(Helpers.createMaterialItemStack(mat, BaseObjTypes.DUST, 1))).addInputs(new InputItemTag(mat.getTag(BaseObjTypes.INGOT), 1)).setTime(30));
                if (mat instanceof GemMaterial)
                    ModMachines.grinder.addRecipe(new ResourceLocation("varyingmachina", "gemtodust." + mat.getName()), new RecipeBase<>(new OutputItem(Helpers.createMaterialItemStack(mat, BaseObjTypes.DUST, 1))).addInputs(new InputItemTag(mat.getTag(BaseObjTypes.GEM), 1)).setTime(40));
            }

            if (PixelPlugin.frozeningot.isMaterialCompatible(mat)) {
                if (mat instanceof IngotMaterial)
                    ModMachines.freezer.addRecipe(new ResourceLocation("varyingmachina", "ingottofrozeningot." + mat.getName()), new RecipeBase<>(new OutputItem(Helpers.createMaterialItemStack(mat, PixelPlugin.frozeningot, 1))).addInputs(new InputItemTag(mat.getTag(BaseObjTypes.INGOT), 1)).setTime(100 * (mat.getTier() + 1)).addReqs(new SpecialStatReq("maxCU", "freezer", (int) (2 * (mat.getTier() + 120) * Math.pow(1.4, mat.getTier())), SpecialStatReq.StatReqComp.GREATEREQUAL)));
            }

            if (PixelPlugin.frozengem.isMaterialCompatible(mat)) {
                if (mat instanceof GemMaterial)
                    ModMachines.freezer.addRecipe(new ResourceLocation("varyingmachina", "gemtofrozengem." + mat.getName()), new RecipeBase<>(new OutputItem(Helpers.createMaterialItemStack(mat, PixelPlugin.frozengem, 1))).addInputs(new InputItemTag(mat.getTag(BaseObjTypes.GEM), 1)).setTime(150 * (mat.getTier() + 1)).addReqs(new SpecialStatReq("maxCU", "freezer", (int) (3.6 * (mat.getTier() + 120) * Math.pow(1.48, mat.getTier())), SpecialStatReq.StatReqComp.GREATEREQUAL)));
            }*/
        }


        for (String type : ModMachines.types.keySet()) {
            for (String variant : ModVariants.types.keySet()) {
                for (String casingVariant : ModVariants.types.keySet()) {
                    Item machine = VaryingMachina.itemMachines.get(type + "." + variant + "." + casingVariant);
                    if (machine == null)
                        continue;
                    List<InputBase> inputs = new ArrayList<>();
                    int size = machine instanceof ItemBlockMachine ? ((ItemBlockMachine) machine).getMultiblockSize() * 2 + 1 : 1;
                    int casingCount = size == 1 ? 1 : 2 * (size * size + 2 * size - 2);
                    int centerCount = size == 1 ? 1 : (int) Math.pow(Math.max(1, size), 3) - casingCount;
                    inputs.add(ModVariants.types.get(casingVariant).getPlateItem().scale(casingCount));
                    inputs.add(ModVariants.types.get(variant).getGearItem().scale(centerCount));

                    int rating = (int) ModMachines.Stats.rating.calculateStat(ModMachines.types.get(type), ModVariants.types.get(variant), ModVariants.types.get(casingVariant));

                    if (rating == 2)
                        inputs.add(new InputItem(new ItemStack(VaryingMachina.circuit1)));
                    else if (rating == 3)
                        inputs.add(new InputItem(new ItemStack(VaryingMachina.circuit2)));
                    else if (rating == 4)
                        inputs.add(new InputItem(new ItemStack(VaryingMachina.circuit3)));
                    else if (rating == 5)
                        inputs.add(new InputItem(new ItemStack(VaryingMachina.circuit4)));
                    else if (rating == 6)
                        inputs.add(new InputItem(new ItemStack(VaryingMachina.circuit5)));
                    else if (rating >= 7)
                        inputs.add(new InputItem(new ItemStack(VaryingMachina.circuit6)));
                    if (ModMachines.types.get(type).getExtraInput() != null)
                        inputs.add(((InputBase) ModMachines.types.get(type).getExtraInput().apply(machine)).copy());


                    RecipeBase recipe = new RecipeBase<>(new OutputItem(new ItemStack(machine, ModMachines.types.get(type).getCraftingMachineMultiplier()))).addInputs(inputs.toArray(new InputBase[inputs.size()])).setTime(100);
                    ModMachines.fabricator.addRecipe(new ResourceLocation("varyingmachina", type + "." + variant + "." + casingVariant), recipe);
                    ModMachines.grinder.addRecipe(new ResourceLocation("varyingmachina", type + "." + variant + "." + casingVariant + ".destroy"), new RecipeMachineDestroy<>(recipe).setTime(50));
                }
            }
        }

        for (MachineVariant variant : ModVariants.types.values()) {
            if (Materials.contains(variant.getName())) {
                Item pick = MaterialItems.get(Materials.get(variant.getName()), PixelPlugin.pickaxe);
                ModMachines.fabricator.addRecipe(new ResourceLocation("varyingmachina", "pickaxe." + variant.getName()), new RecipeBase<>(new OutputItem(new ItemStack(pick))).addInputs(new InputItem(new ItemStack(Items.STICK, 2)), variant.getIngotItem().scale(3)).setTime(10));

                Item axe = MaterialItems.get(Materials.get(variant.getName()), PixelPlugin.axe);
                ModMachines.fabricator.addRecipe(new ResourceLocation("varyingmachina", "axe." + variant.getName()), new RecipeBase<>(new OutputItem(new ItemStack(axe))).addInputs(new InputItem(new ItemStack(Items.STICK, 2)), variant.getIngotItem().scale(3)).setTime(10));

                Item shovel = MaterialItems.get(Materials.get(variant.getName()), PixelPlugin.shovel);
                ModMachines.fabricator.addRecipe(new ResourceLocation("varyingmachina", "shovel." + variant.getName()), new RecipeBase<>(new OutputItem(new ItemStack(shovel))).addInputs(new InputItem(new ItemStack(Items.STICK, 2)), variant.getIngotItem()).setTime(10));

                Item sword = MaterialItems.get(Materials.get(variant.getName()), PixelPlugin.sword);
                ModMachines.fabricator.addRecipe(new ResourceLocation("varyingmachina", "sword." + variant.getName()), new RecipeBase<>(new OutputItem(new ItemStack(sword))).addInputs(new InputItem(new ItemStack(Items.STICK, 1)), variant.getIngotItem().scale(2)).setTime(10));

                Item hoe = MaterialItems.get(Materials.get(variant.getName()), PixelPlugin.hoe);
                ModMachines.fabricator.addRecipe(new ResourceLocation("varyingmachina", "hoe." + variant.getName()), new RecipeBase<>(new OutputItem(new ItemStack(hoe))).addInputs(new InputItem(new ItemStack(Items.STICK, 2)), variant.getIngotItem().scale(2)).setTime(10));
            }

            ModMachines.fabricator.addRecipe(new ResourceLocation("varyingmachina", "casing." + variant.getName()), new RecipeBase<>(new OutputItem(new ItemStack(VaryingMachina.itemCasings.get(variant.getName())))).addInputs(variant.getPlateItem().scale(1)).setTime(10));
        }

        ModMachines.fabricator.addRecipe(new ResourceLocation("varyingmachina", "circuit1"), new RecipeBase<>(new OutputItem(new ItemStack(VaryingMachina.circuit1))).addInputs(new InputItemList(new ItemStack(Blocks.STONE_BRICK_SLAB), new ItemStack(Blocks.SANDSTONE_SLAB)), new InputItemTagList(1, new ResourceLocation("forge", "plates/ferrotin"), new ResourceLocation("forge", "plates/steel")), new InputItemTagList(2, new ResourceLocation("forge", "dusts/clay"), new ResourceLocation("forge", "dusts/flint")), new InputItemTagList(2, new ResourceLocation("forge", "cables/copper"), new ResourceLocation("forge", "cables/tin"))).setTime(10));

        ModMachines.fabricator.addRecipe(new ResourceLocation("varyingmachina", "circuit2"), new RecipeBase<>(new OutputItem(new ItemStack(VaryingMachina.circuit2))).addInputs(new InputItem(new ItemStack(VaryingMachina.circuit1)), new InputItemTagList(1, new ResourceLocation("forge", "plates/gold"), new ResourceLocation("forge", "plates/bronze")), new InputItemTagList(2, new ResourceLocation("forge", "dusts/redstone"), new ResourceLocation("forge", "dusts/quartz")), new InputItemTagList(2, new ResourceLocation("forge", "cables/redcopper"), new ResourceLocation("forge", "cables/electrum"))).setTime(40));

        ModMachines.fabricator.addRecipe(new ResourceLocation("varyingmachina", "circuit3"), new RecipeBase<>(new OutputItem(new ItemStack(VaryingMachina.circuit3))).addInputs(new InputItem(new ItemStack(VaryingMachina.circuit2)), new InputItemTag(new ResourceLocation("forge", "plates/mythrilsteel"), 1), new InputItemTag(new ResourceLocation("forge", "dusts/diamond"), 2), new InputItemTag(new ResourceLocation("forge", "cables/gold"), 2)).setTime(180));


        ModMachines.smelter.addRecipe(new ResourceLocation("varyingmachina", "oretocoal"), new RecipeBase<>(new OutputItem(new ItemStack(Items.COAL))).addInputs(new InputItemTag(BaseMaterials.COAL.getItemTag(BaseObjTypes.ORE), 1)).setTime(40));

        //ModMachines.smelter.addRecipe(new ResourceLocation("varyingmachina", "steelalloy"), new RecipeBase<>(new OutputItem(Helpers.createMaterialItemStack(PixelPlugin.steel, BaseObjTypes.INGOT, 1))).addInputs(new InputItemTag(BaseObjTypes.IRON.getTag(BaseObjTypes.INGOT), 1), new InputItemTag(BaseObjTypes.COAL.getTag(BaseObjTypes.DUST), 1)).setTime(400).addReqs(new StatReq(ModMachines.Stats.hu, 1600, StatReq.StatReqComp.GREATEREQUAL)));

        ModMachines.smelter.addRecipe(new ResourceLocation("varyingmachina", "bronzealloy"), new RecipeBase<>(new OutputItem(Helpers.createMaterialItemStack(PixelPlugin.bronze, BaseObjTypes.INGOT, 4))).addInputs(new InputItemTag(PixelPlugin.copper.getItemTag(BaseObjTypes.INGOT), 3), new InputItemTag(PixelPlugin.tin.getItemTag(BaseObjTypes.INGOT), 1)).setTime(80).addReqs(new StatReq(ModMachines.Stats.hu, 230, StatReq.StatReqComp.GREATEREQUAL)));

        //ModMachines.smelter.addRecipe(new ResourceLocation("varyingmachina", "electrumalloy"), new RecipeBase<>(new OutputItem(Helpers.createMaterialItemStack(PixelPlugin.electrum, BaseObjTypes.INGOT, 2))).addInputs(new InputItemTag(PixelPlugin.silver.getTag(BaseObjTypes.INGOT), 1), new InputItemTag(BaseObjTypes.GOLD.getTag(BaseObjTypes.INGOT), 1)).setTime(90).addReqs(new StatReq(ModMachines.Stats.hu, 730, StatReq.StatReqComp.GREATEREQUAL)));

        ModMachines.smelter.addRecipe(new ResourceLocation("varyingmachina", "ferrotinalloy"), new RecipeBase<>(new OutputItem(Helpers.createMaterialItemStack(PixelPlugin.ferrotin, BaseObjTypes.INGOT, 3))).addInputs(new InputItemTag(PixelPlugin.tin.getItemTag(BaseObjTypes.INGOT), 2), new InputItemTag(BaseMaterials.IRON.getItemTag(BaseObjTypes.INGOT), 1)).setTime(100).addReqs(new StatReq(ModMachines.Stats.hu, 510, StatReq.StatReqComp.GREATEREQUAL)));

        //ModMachines.smelter.addRecipe(new ResourceLocation("varyingmachina", "redcopperalloy"), new RecipeBase<>(new OutputItem(Helpers.createMaterialItemStack(PixelPlugin.redcopper, BaseObjTypes.INGOT, 1))).addInputs(new InputItemTag(PixelPlugin.copper.getTag(BaseObjTypes.INGOT), 1), new InputItemTag(BaseObjTypes.REDSTONE.getTag(BaseObjTypes.DUST), 3)).setTime(80).addReqs(new StatReq(ModMachines.Stats.hu, 910, StatReq.StatReqComp.GREATEREQUAL), new StatReq(ModMachines.Stats.speed, 2.0, StatReq.StatReqComp.GREATEREQUAL)));

        //ModMachines.smelter.addRecipe(new ResourceLocation("varyingmachina", "mythrilsteel"), new RecipeBase<>(new OutputItem(Helpers.createMaterialItemStack(PixelPlugin.mythrilsteel, BaseObjTypes.INGOT, 2))).addInputs(new InputItemTag(PixelPlugin.mythril.getTag(BaseObjTypes.INGOT), 1), new InputItemTag(PixelPlugin.steel.getTag(BaseObjTypes.INGOT), 1)).setTime(500).addReqs(new StatReq(ModMachines.Stats.hu, 1880, StatReq.StatReqComp.GREATEREQUAL), new StatReq(ModMachines.Stats.pressure, 760, StatReq.StatReqComp.GREATEREQUAL)));

        ModMachines.smelter.addRecipe(new ResourceLocation("varyingmachina", "bread"), new RecipeBase<>(new OutputItem(new ItemStack(Items.BREAD))).addInputs(new InputItem(new ItemStack(Items.WHEAT))).setTime(40).addReqs(new StatReq(ModMachines.Stats.hu, 400, StatReq.StatReqComp.GREATEREQUAL)));

        //ModMachines.grinder.addRecipe(new ResourceLocation("varyingmachina", "claydust"), new RecipeBase<>(new OutputItem(Helpers.createMaterialItemStack(PixelPlugin.clay, BaseObjTypes.DUST, 1))).addInputs(new InputItem(new ItemStack(Items.CLAY_BALL))).setTime(10));

        ModMachines.grinder.addRecipe(new ResourceLocation("varyingmachina", "coaldust"), new RecipeBase<>(new OutputItem(Helpers.createMaterialItemStack(BaseMaterials.COAL, BaseObjTypes.DUST, 1))).addInputs(new InputItem(new ItemStack(Items.COAL))).setTime(10));

        ModMachines.grinder.addRecipe(new ResourceLocation("varyingmachina", "flintdust"), new RecipeBase<>(new OutputItem(Helpers.createMaterialItemStack(BaseMaterials.FLINT, BaseObjTypes.DUST, 1))).addInputs(new InputItem(new ItemStack(Items.FLINT))).setTime(10));

        ModMachines.grinder.addRecipe(new ResourceLocation("varyingmachina", "bonemeal"), new RecipeBase<>(new OutputItemChance(new ItemStack(Items.BONE_MEAL, 3), 1, 6)).addInputs(new InputItem(new ItemStack(Items.BONE))).setTime(12));

        for (int i = 10; i <= 1e8; i *= 10) {
            ModMachines.generator.addRecipe(new ResourceLocation("varyingmachina", "power" + i), new RecipeGenerator<>(i).setTime((int) (10 * Math.log10(i * 1.2))).setFuelUsage(Math.pow(i, 0.75) / 10d));
        }

        RecipeMaker.addShapedRecipe(new ResourceLocation("varyingmachina", "dirtdirtassembler"), VaryingMachina.createMachineStack(ModMachines.fabricator, ModVariants.dirt, ModVariants.dirt, 1), "DD", "DD", 'D', new ItemStack(Blocks.COARSE_DIRT));
    }


    private static boolean loadedRecipes = false;

    public static void onWorldStart(IWorld world) {
        if (!loadedRecipes && world instanceof World) {
            loadedRecipes = true;

            for (IRecipe recipe : ((World) world).getRecipeManager().getRecipes()) {
                if (recipe.getClass().equals(ShapedRecipe.class) || recipe.getClass().equals(ShapelessRecipe.class)) {
                    addCraftingRecipe(recipe);
                } else if (recipe.getClass().equals(FurnaceRecipe.class)) {
                    addFurnaceRecipe(recipe);
                }
            }
        }
    }

    private static void addFurnaceRecipe(IRecipe recipe) {
        FurnaceRecipe furnaceRecipe = (FurnaceRecipe) recipe;
        for (RecipeBase recipeBase : ModMachines.smelter.getRecipes().values()) {
            if (recipeBase.isValidRecipe()) {
                boolean sameInput = false;
                for (ItemStack itemStack : (List<ItemStack>) (recipeBase.getAllInputs(ItemStack.class, "smelter").get(0))) {
                    if (sameInput)
                        break;
                    for (ItemStack stack : furnaceRecipe.getIngredients().get(0).getMatchingStacks()) {
                        if (stack.isItemEqual(itemStack)) {
                            sameInput = true;
                            break;
                        }
                    }
                }
                if (sameInput) {
                    for (ItemStack itemStack : (List<ItemStack>) (recipeBase.getAllOutputs(ItemStack.class, "smelter"))) {
                        if (recipe.getRecipeOutput().isItemEqual(itemStack))
                            return;

                    }
                }
            }
        }

        ModMachines.smelter.addRecipe(new ResourceLocation(furnaceRecipe.getId().toString() + ".furnace"), new RecipeBase<>(new OutputItem(furnaceRecipe.getRecipeOutput())).addInputs(new InputItemList(((Ingredient) recipe.getIngredients().get(0)).getMatchingStacks())).setTime(40));
    }

    private static void addCraftingRecipe(IRecipe recipe) {
        List<InputBase> inputItems = new ArrayList<>();
        for (Object o : recipe.getIngredients()) {
            Ingredient i = (Ingredient) o;
            if (i.getMatchingStacks().length > 0)
                inputItems.add(new InputItemList(i.getMatchingStacks()));
        }
        ModMachines.fabricator.addRecipe(new ResourceLocation(recipe.getId().toString() + ".crafting"), new RecipeBase<>(new OutputItem(recipe.getRecipeOutput())).addInputs(inputItems.toArray(new InputBase[inputItems.size()])).setTime(10));
    }
}
