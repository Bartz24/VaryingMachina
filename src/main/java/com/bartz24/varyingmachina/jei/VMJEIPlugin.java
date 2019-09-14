package com.bartz24.varyingmachina.jei;

import com.bartz24.varyingmachina.VaryingMachina;
import com.bartz24.varyingmachina.block.BlockMachine;
import com.bartz24.varyingmachina.item.ItemBlockMachine;
import com.bartz24.varyingmachina.machine.ModMachines;
import com.bartz24.varyingmachina.recipe.RecipeBase;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.gui.Focus;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.function.Consumer;

@JeiPlugin
public class VMJEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("varyingmachina", "jei");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        for (String s : ModMachines.types.keySet())
            registration.addRecipeCategories(new MachineCategory(helper, s));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        for (String s : ModMachines.types.keySet())
            ModMachines.types.get(s).getRecipes().values().forEach((r) -> {
                if (r instanceof RecipeBase && ((RecipeBase) r).isValidRecipe())
                    registration.addRecipes(Collections.singletonList(r), new ResourceLocation("varyingmachina", s));
                else
                    System.out.println(((ItemStack) ((RecipeBase) r).getAllOutputs(ItemStack.class, s).get(0)).getDisplayName());
            });

    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        for (String type : ModMachines.types.keySet()) {
            int max = 16;
            for (Item i : VaryingMachina.itemMachines.values()) {
                ItemBlockMachine blockMachine = (ItemBlockMachine) i;
                if (((BlockMachine) blockMachine.getBlock()).getMachineType().equals(type)) {
                    registration.addRecipeCatalyst(new ItemStack(i), new ResourceLocation("varyingmachina", type));
                    max--;
                    if (max <= 0)
                        break;
                }
            }
        }
    }

    public static Consumer<String> openCategory;
    public static Consumer<Object> openRecipeFor;

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {

        openCategory = type -> {
            jeiRuntime.getRecipesGui().showCategories(Collections.singletonList(new ResourceLocation(type)));
        };


        openRecipeFor = obj -> {
            jeiRuntime.getRecipesGui().show(new Focus<>(IFocus.Mode.OUTPUT, obj));
        };
    }
}
