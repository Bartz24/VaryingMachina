package com.bartz24.varyingmachina.machines.recipes;

import com.bartz24.varyingmachina.base.recipe.*;
import com.bartz24.varyingmachina.registry.MachineRegistry;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.item.IngredientStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecipeManager {
    
    private ProcessRecipeManager processRecipeManager;
    
    public RecipeManager(ProcessRecipeManager processRecipeManager)
    {
        this.processRecipeManager = processRecipeManager;
        MachineRegistry.registerRecipeClass(this);
    }

    @ProcessRecipeRegistry(ProcessRecipeRegistry.ProcessRecipeStage.POSTINIT)
    public void registerPostInit() {
        if (processRecipeManager != null)
            processRecipeManager.processCTRecipes();
    }

    public void addRecipe(RecipeObject output, List<RecipeObject> inputs, float... numParams) {
        addRecipe(Collections.singletonList(output), inputs, numParams);
    }

    public void addRecipe(List<RecipeObject> outputs, List<RecipeObject> inputs, float... numParams) {
        if (processRecipeManager != null)
            processRecipeManager.addRecipe(outputs, inputs, numParams);
    }

    public void addRecipe(ProcessRecipe processRecipe) {
        if (processRecipeManager != null)
            processRecipeManager.addRecipe(processRecipe);
    }

    public void removeRecipe(List<RecipeObject> outputs) {
        if (processRecipeManager != null)
            processRecipeManager.removeRecipe(new ProcessRecipe(outputs, Collections.EMPTY_LIST, processRecipeManager.getType()));
    }

    public void removeRecipe(List<RecipeObject> outputs, List<RecipeObject> inputs) {
        if (processRecipeManager != null)
            processRecipeManager.removeRecipe(new ProcessRecipe(outputs, inputs, processRecipeManager.getType()));
    }

    public void removeRecipe(ProcessRecipe processRecipe) {
        if (processRecipeManager != null)
            processRecipeManager.removeRecipe(processRecipe);
    }

    @Optional.Method(modid = "crafttweaker")
    public void addCTRecipe(IIngredient[] outputs, IIngredient[] inputs, float... numParams) {
        addCTRecipe(outputs, new ILiquidStack[0], inputs, new ILiquidStack[0], numParams);
    }

    @Optional.Method(modid = "crafttweaker")
    public void addCTRecipe(IIngredient[] outputs, ILiquidStack[] fluidOutputs, IIngredient[] inputs, ILiquidStack[] fluidInputs, float... numParams) {
        List<RecipeObject> recipeOutputs = new ArrayList<>();
        for (IIngredient ingredient : outputs)
            recipeOutputs.add(new RecipeListItem(toStacks(ingredient.getItemArray()), ingredient.getAmount()));
        for (ILiquidStack ingredient : fluidOutputs)
            recipeOutputs.add(new RecipeFluid(toFluidStack(ingredient)));
        List<RecipeObject> recipeInputs = new ArrayList<>();
        for (IIngredient ingredient : inputs)
            recipeInputs.add(new RecipeListItem(toStacks(ingredient.getItemArray()), ingredient.getAmount()));
        for (ILiquidStack ingredient : fluidInputs)
            recipeInputs.add(new RecipeFluid(toFluidStack(ingredient)));
        addCTRecipe(new ProcessRecipe(recipeOutputs, recipeInputs, processRecipeManager.getType(), numParams));
    }

    @Optional.Method(modid = "crafttweaker")
    public void addCTRecipe(ProcessRecipe processRecipe) {
        if (processRecipeManager != null)
            processRecipeManager.addCTRecipe(processRecipe);
    }

    @Optional.Method(modid = "crafttweaker")
    public void removeCTRecipe(IIngredient[] outputs) {

        List<RecipeObject> recipeOutputs = new ArrayList<>();
        for (IIngredient ingredient : outputs)
            recipeOutputs.add(new RecipeListItem(toStacks(ingredient.getItemArray()), ingredient.getAmount()));

        removeCTRecipe(new ProcessRecipe(recipeOutputs, Collections.EMPTY_LIST, processRecipeManager.getType()));
    }

    @Optional.Method(modid = "crafttweaker")
    public void removeCTRecipe(ProcessRecipe processRecipe) {
        if (processRecipeManager != null)
            processRecipeManager.removeCTRecipe(processRecipe);
    }

    @Optional.Method(modid = "crafttweaker")
    public ItemStack toStack(IItemStack iStack) {
        return CraftTweakerMC.getItemStack(iStack);
    }

    @Optional.Method(modid = "crafttweaker")
    public ItemStack[] toStacks(IItemStack[] iStacks) {
        List<ItemStack> stacks = new ArrayList<ItemStack>();
        for (IItemStack is : iStacks) {
            stacks.add(toStack(is));
        }
        return stacks.toArray(new ItemStack[stacks.size()]);
    }

    @Optional.Method(modid = "crafttweaker")
    public Object toObject(IIngredient iStack) {
        if (iStack == null)
            return null;
        else {
            if (iStack instanceof IOreDictEntry)
                return ((IOreDictEntry) iStack).getName();
            else if (iStack instanceof IItemStack)
                return CraftTweakerMC.getItemStack((IItemStack) iStack);
            else if (iStack instanceof IngredientStack) {
                IIngredient ingr = ReflectionHelper.getPrivateValue(
                        IngredientStack.class, (IngredientStack) iStack,
                        "ingredient");
                return toObject(ingr);
            } else
                return null;
        }
    }

    @Optional.Method(modid = "crafttweaker")
    public Object[] toObjects(IIngredient[] iStacks) {
        List<Object> stacks = new ArrayList<Object>();
        for (IIngredient is : iStacks) {
            stacks.add(toObject(is));
        }
        return stacks.toArray();
    }

    @Optional.Method(modid = "crafttweaker")
    public FluidStack toFluidStack(ILiquidStack iStack) {
        return CraftTweakerMC.getLiquidStack(iStack);
    }
}
