package com.bartz24.varyingmachina.jei;

import com.bartz24.varyingmachina.References;
import com.bartz24.varyingmachina.base.item.ItemMachine;
import com.bartz24.varyingmachina.jei.machines.*;
import com.bartz24.varyingmachina.registry.ModBlocks;
import com.bartz24.varyingmachina.registry.ModItems;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JEIPluginVM implements IModPlugin {
    public static IJeiHelpers jeiHelpers;
    public static IGuiHelper guiHelper;
    public static IJeiRuntime jeiRuntime;

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
        subtypeRegistry.useNbtForSubtypes(Item.REGISTRY.getObject(ModBlocks.casing.getRegistryName()));
        subtypeRegistry.useNbtForSubtypes(ModItems.smelter, ModItems.grinder, ModItems.presser, ModItems.assembler, ModItems.mixer, ModItems.combustion, ModItems.itembuffer, ModItems.extractor);
        subtypeRegistry.useNbtForSubtypes(ModItems.regulator, ModItems.inserter, ModItems.remover, ModItems.bellow, ModItems.gearbox, ModItems.worldinserter, ModItems.worldremover);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {

        jeiHelpers = registry.getJeiHelpers();
        guiHelper = jeiHelpers.getGuiHelper();

        registry.addRecipeCategories(new SmelterRecipeCategory(guiHelper));
        registry.addRecipeCategories(new GrinderRecipeCategory(guiHelper));
        registry.addRecipeCategories(new PresserRecipeCategory(guiHelper));
        registry.addRecipeCategories(new AssemblerRecipeCategory(guiHelper));
        registry.addRecipeCategories(new MixerRecipeCategory(guiHelper));
        registry.addRecipeCategories(new CombustionRecipeCategory(guiHelper));
        registry.addRecipeCategories(new ExtractorRecipeCategory(guiHelper));
    }

    @Override
    public void register(IModRegistry registry) {

        jeiHelpers = registry.getJeiHelpers();
        guiHelper = jeiHelpers.getGuiHelper();

        addProcessRecipes(ProcessRecipeWrapper.class, registry, "smelter");
        addCatalysts(registry, (ItemMachine) ModItems.smelter, "smelter");
        addProcessRecipes(ProcessRecipeWrapper.class, registry, "grinder");
        addCatalysts(registry, (ItemMachine) ModItems.grinder, "grinder");
        addProcessRecipes(PresserProcessRecipeWrapper.class, registry, "presser");
        addCatalysts(registry, (ItemMachine) ModItems.presser, "presser");
        addProcessRecipes(ProcessRecipeWrapper.class, registry, "assembler");
        addCatalysts(registry, (ItemMachine) ModItems.assembler, "assembler");
        addProcessRecipes(ProcessRecipeWrapper.class, registry, "mixer");
        addCatalysts(registry, (ItemMachine) ModItems.mixer, "mixer");
        addProcessRecipes(ProcessRecipeWrapper.class, registry, "combustion");
        addCatalysts(registry, (ItemMachine) ModItems.combustion, "combustion");
        addProcessRecipes(ProcessRecipeWrapper.class, registry, "extractor");
        addCatalysts(registry, (ItemMachine) ModItems.extractor, "extractor");

        // TODO add click areas

        registry.getRecipeTransferRegistry().addRecipeTransferHandler(new MachineTransferInfo("smelter", 36, 1, 0, 36));
        registry.getRecipeTransferRegistry().addRecipeTransferHandler(new MachineTransferInfo("grinder", 36, 1, 0, 36));
        registry.getRecipeTransferRegistry().addRecipeTransferHandler(new MachineTransferInfo("presser", 36, 1, 0, 36));
        registry.getRecipeTransferRegistry().addRecipeTransferHandler(new MachineTransferInfo("assembler", 36, 9, 0, 36));
        registry.getRecipeTransferRegistry().addRecipeTransferHandler(new MachineTransferInfo("mixer", 36, 9, 0, 36));
        registry.getRecipeTransferRegistry().addRecipeTransferHandler(new MachineTransferInfo("extractor", 36, 1, 0, 36));


    }

    public <T extends ProcessRecipeWrapper> void addProcessRecipes(Class<T> clazz, IModRegistry registry, String type) {
        jeiHelpers = registry.getJeiHelpers();
        guiHelper = jeiHelpers.getGuiHelper();
        registry.addRecipes(ProcessRecipeWrapperManager.getRecipes(clazz, guiHelper, type),
                References.ModID + ":" + type);
    }

    public void addCatalysts(IModRegistry registry, ItemMachine machine, String recipeType) {
        for (ItemStack stack : machine.getItemTypes())
            registry.addRecipeCatalyst(stack, References.ModID + ":" + recipeType);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {

        JEIPluginVM.jeiRuntime = jeiRuntime;
    }
}
