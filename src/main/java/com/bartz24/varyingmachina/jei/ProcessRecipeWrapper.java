package com.bartz24.varyingmachina.jei;

import com.bartz24.varyingmachina.base.recipe.ProcessRecipe;
import com.bartz24.varyingmachina.base.recipe.ProcessRecipeManager;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class ProcessRecipeWrapper implements IRecipeWrapper {

    protected ProcessRecipe recipe;

    protected List<JEIDrawable> recipeSpecificDrawables = new ArrayList();

    protected IGuiHelper guiHelper;

    public ProcessRecipeWrapper(IGuiHelper guiHelper, ProcessRecipe recipe) {
        this.recipe = recipe;
        this.guiHelper = guiHelper;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, recipe.getItemInputs());
        ingredients.setInputs(FluidStack.class, recipe.getFluidInputs());
        ingredients.setOutputs(ItemStack.class, recipe.getItemOutputs());
        ingredients.setOutputs(FluidStack.class, recipe.getFluidOutputs());
    }

    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

        for (JEIDrawable drawable : recipeSpecificDrawables)
            drawable.draw(minecraft);
        ProcessRecipeManager.getManagerFromType(recipe.getRecipeType()).drawJEIInfo(recipe, minecraft, recipeWidth,
                recipeHeight, mouseX, mouseY);
    }
}
