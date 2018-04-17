package com.bartz24.varyingmachina.registry;

import com.bartz24.varyingmachina.References;
import com.bartz24.varyingmachina.base.machine.MachineVariant;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thelm.jaopca.api.utils.Utils;

public class ModRecipes {
    public static void init() {
        Utils.addShapedOreRecipe(new ItemStack(ModBlocks.darkmatterblock), new Object[]{
                "ooo",
                "ooo",
                "ooo",
                'o', new ItemStack(ModItems.darkmatter),
        });
        Utils.addShapelessOreRecipe(new ItemStack(ModItems.darkmatter, 9), new Object[]{new ItemStack(ModBlocks.darkmatterblock)});
        Utils.addShapedOreRecipe(new ItemStack(ModBlocks.lightmatterblock), new Object[]{
                "ooo",
                "ooo",
                "ooo",
                'o', new ItemStack(ModItems.lightmatter),
        });
        Utils.addShapelessOreRecipe(new ItemStack(ModItems.lightmatter, 9), new Object[]{new ItemStack(ModBlocks.lightmatterblock)});
        Utils.addShapedOreRecipe(new ItemStack(ModBlocks.frozenironblock), new Object[]{
                "ooo",
                "ooo",
                "ooo",
                'o', new ItemStack(ModItems.frozenironingot),
        });
        Utils.addShapelessOreRecipe(new ItemStack(ModItems.frozenironingot, 9), new Object[]{new ItemStack(ModBlocks.frozenironblock)});

        Utils.addShapedOreRecipe(MachineVariant.writeVariantToStack(new ItemStack(ModBlocks.casing), MachineVariant.REGISTRY.getValue(new ResourceLocation(References.ModID, "dirt"))), new Object[]{
                "ooo",
                "oxo",
                "ooo",
                'o', "dirt",
                'x', "plankWood"});
        Utils.addShapedOreRecipe(MachineVariant.writeVariantToStack(new ItemStack(ModBlocks.casing), MachineVariant.REGISTRY.getValue(new ResourceLocation(References.ModID, "wood"))), new Object[]{
                "ooo",
                "oxo",
                "ooo",
                'o', "logWood",
                'x', "plankWood"});
        Utils.addShapedOreRecipe(MachineVariant.writeVariantToStack(new ItemStack(ModItems.assembler), MachineVariant.REGISTRY.getValue(new ResourceLocation(References.ModID, "dirt"))), new Object[]{
                "ooo",
                "oxo",
                "ooo",
                'o', "dirt",
                'x', new ItemStack(Items.GUNPOWDER)});
        Utils.addShapedOreRecipe(MachineVariant.writeVariantToStack(new ItemStack(ModItems.assembler), MachineVariant.REGISTRY.getValue(new ResourceLocation(References.ModID, "wood"))), new Object[]{
                "ooo",
                "oxo",
                "ooo",
                'o', "logWood",
                'x', new ItemStack(Items.GUNPOWDER)});
    }
}
