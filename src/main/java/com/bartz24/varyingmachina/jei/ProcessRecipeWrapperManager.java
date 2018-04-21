package com.bartz24.varyingmachina.jei;

import java.util.ArrayList;
import java.util.List;

import com.bartz24.varyingmachina.VaryingMachina;
import com.bartz24.varyingmachina.base.recipe.ProcessRecipe;
import com.bartz24.varyingmachina.base.recipe.ProcessRecipeManager;

import mezz.jei.api.IGuiHelper;

public class ProcessRecipeWrapperManager {
    public static <T extends ProcessRecipeWrapper> List<T> getRecipes(Class<T> clazz, IGuiHelper guiHelper,
                                                                      String type) {
        List<T> recipes = new ArrayList();
        for (ProcessRecipe recipe : ProcessRecipeManager.getManagerFromType(type).getRecipes()) {
            if (recipe.isValid())
                recipes.add(createObject(clazz, guiHelper, recipe));
        }
        return recipes;
    }

    private static <T extends ProcessRecipeWrapper> T createObject(Class<T> clazz, IGuiHelper guiHelper,
                                                                   ProcessRecipe recipe) {
        try {
            return clazz.getDeclaredConstructor(IGuiHelper.class, ProcessRecipe.class).newInstance(guiHelper, recipe);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
