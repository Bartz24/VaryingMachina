package com.bartz24.varyingmachina.jei;

import com.bartz24.varyingmachina.References;
import com.bartz24.varyingmachina.base.item.ItemMachine;
import com.bartz24.varyingmachina.jei.machines.GrinderRecipeCategory;
import com.bartz24.varyingmachina.jei.machines.PresserProcessRecipeWrapper;
import com.bartz24.varyingmachina.jei.machines.PresserRecipeCategory;
import com.bartz24.varyingmachina.jei.machines.SmelterRecipeCategory;
import com.bartz24.varyingmachina.registry.ModItems;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JEIPluginVM implements IModPlugin {
	public static IJeiHelpers jeiHelpers;
	public static IGuiHelper guiHelper;
	public static IJeiRuntime jeiRuntime;

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {

		jeiHelpers = registry.getJeiHelpers();
		guiHelper = jeiHelpers.getGuiHelper();

		registry.addRecipeCategories(new SmelterRecipeCategory(guiHelper));
		registry.addRecipeCategories(new GrinderRecipeCategory(guiHelper));
		registry.addRecipeCategories(new PresserRecipeCategory(guiHelper));
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

		// TODO add click areas
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
