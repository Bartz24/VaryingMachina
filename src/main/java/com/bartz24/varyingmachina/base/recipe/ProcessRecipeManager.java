package com.bartz24.varyingmachina.base.recipe;

import com.bartz24.varyingmachina.RandomHelper;
import com.bartz24.varyingmachina.VaryingMachina;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProcessRecipeManager {
    private String type;

    private static List<ProcessRecipeManager> managers;

    public static List<ProcessRecipeManager> getManagers() {
        return managers;
    }

    public ProcessRecipeManager(String recipeType) {
        recipes = new ArrayList();
        ctRemovedRecipes = new ArrayList();
        ctAddedRecipes = new ArrayList();
        type = recipeType;
        if (managers == null)
            managers = new ArrayList<>();
        managers.add(this);
    }

    public static ProcessRecipeManager getManagerFromType(String type) {
        for (ProcessRecipeManager m : managers) {
            if (m.type.equals(type))
                return m;
        }
        return null;
    }

    private List<ProcessRecipe> recipes;
    private List<ProcessRecipe> ctRemovedRecipes;
    private List<ProcessRecipe> ctAddedRecipes;

    public ProcessRecipe getRecipe(List<RecipeObject> input, boolean forceEqual, boolean mergeStacks,
                                   float... numVals) {
        input = mergeStacks ? mergeStacks(input) : input;

        ProcessRecipe rec = new ProcessRecipe(input, type, numVals);

        for (ProcessRecipe recipe : recipes) {
            if (rec.isInputRecipeEqualTo(recipe, forceEqual)) {
                return recipe;
            }
        }

        return null;
    }

    public ProcessRecipe getRecipe(ProcessRecipe rec) {

        for (ProcessRecipe recipe : recipes) {
            if (rec.isInputRecipeEqualTo(recipe, false)) {
                return recipe;
            }
        }

        return null;
    }

    @Deprecated
    public ProcessRecipe compareRecipeLess(List<RecipeObject> input, float intVal, boolean mergeStacks,
                                           ProcessRecipe recipe) {
        // input = mergeStacks ? mergeStacks(input) : input;

        // ProcessRecipe rec = new ProcessRecipe(input, intVal, type);

        // if (rec.isInputRecipeLess(recipe)) {
        // return recipe;
        // }

        return null;
    }

    public ProcessRecipe getRecipe(RecipeObject input, boolean forceEqual, boolean mergeStacks, float... numVals) {
        List<RecipeObject> inputs = mergeStacks ? mergeStacks(Collections.singletonList(input))
                : Collections.singletonList(input);

        ProcessRecipe rec = new ProcessRecipe(inputs, type, numVals);

        for (ProcessRecipe recipe : recipes) {
            if (recipe.isValid() && rec.isInputRecipeEqualTo(recipe, forceEqual)) {
                return recipe;
            }
        }

        return null;
    }

    public ProcessRecipe getMultiRecipe(List<RecipeObject> input, float... numVals) {
        ProcessRecipe rec = new ProcessRecipe(input, type, numVals);

        for (ProcessRecipe recipe : recipes) {
            if (recipe.isValid() && rec.isInputMultiRecipeEqualTo(recipe)) {
                return recipe;
            }
        }

        return null;
    }

    private List<RecipeObject> mergeStacks(List<RecipeObject> input) {
        int checks = 0;
        boolean merged = true;
        while (merged && checks < 50) {
            merged = false;
            for (int i = 0; i < input.size(); i++) {
                if (!(input.get(i) instanceof RecipeItem))
                    continue;
                ItemStack stack1 = (ItemStack) input.get(i).getRepresentativeObject();
                for (int i2 = i + 1; i2 < input.size(); i2++) {
                    if (!(input.get(i2) instanceof RecipeItem))
                        continue;
                    ItemStack stack2 = (ItemStack) input.get(i2).getRepresentativeObject();

                    if (RandomHelper.canStacksMerge(stack2, stack1)) {
                        stack1.grow(stack2.getCount());
                        if (stack2.getCount() <= 0)
                            stack2 = ItemStack.EMPTY;
                        input.set(i, new RecipeItem(stack1));
                        input.set(i2, new RecipeItem(stack2));
                        merged = true;
                        break;
                    }
                }
                if (merged)
                    break;
            }

            for (int i = input.size() - 1; i >= 0; i--) {
                if (!(input.get(i) instanceof RecipeItem))
                    continue;
                ItemStack stack = (ItemStack) input.get(i).getRepresentativeObject();
                if (stack == ItemStack.EMPTY)
                    input.remove(i);
            }

            checks++;
        }

        return input;
    }

    public List<ProcessRecipe> getRecipes() {
        return recipes;
    }

    public void addRecipe(List<RecipeObject> output, List<RecipeObject> input, float... numVals) {

        if (input == null) {
            VaryingMachina.logger.error("Need inputs for recipe. DID NOT ADD RECIPE.");
            return;
        }

        if (output == null) {
            VaryingMachina.logger.error("Need outputs for recipe. DID NOT ADD RECIPE.");
            return;
        }

        recipes.add(new ProcessRecipe(output, input, type, numVals));
    }

    public void addRecipe(RecipeObject output, RecipeObject input, float... numVals) {
        if (input == null) {
            VaryingMachina.logger.error("Need inputs for recipe. DID NOT ADD RECIPE.");
            return;
        }

        if (output == null) {
            VaryingMachina.logger.error("Need outputs for recipe. DID NOT ADD RECIPE.");
            return;
        }

        recipes.add(
                new ProcessRecipe(Collections.singletonList(output), Collections.singletonList(input), type, numVals));
    }

    public void addRecipe(List<RecipeObject> output, RecipeObject input, float... numVals) {
        if (input == null) {
            VaryingMachina.logger.error("Need inputs for recipe. DID NOT ADD RECIPE.");
            return;
        }

        if (output == null) {
            VaryingMachina.logger.error("Need outputs for recipe. DID NOT ADD RECIPE.");
            return;
        }

        recipes.add(new ProcessRecipe(output, Collections.singletonList(input), type, numVals));
    }

    public void addRecipe(RecipeObject output, List<RecipeObject> input, float... numVals) {

        if (input == null) {
            VaryingMachina.logger.error("Need inputs for recipe. DID NOT ADD RECIPE.");
            return;
        }

        if (output == null) {
            VaryingMachina.logger.error("Need outputs for recipe. DID NOT ADD RECIPE.");
            return;
        }

        recipes.add(new ProcessRecipe(Collections.singletonList(output), input, type, numVals));
    }

    public void addRecipe(ProcessRecipe recipe) {
        if (recipe.getItemInputs().size() == 0 && recipe.getFluidInputs().size() == 0) {
            VaryingMachina.logger.error("Need inputs for recipe. DID NOT ADD RECIPE.");
            return;
        }

        if (recipe.getItemOutputs().size() == 0 && recipe.getFluidOutputs().size() == 0) {
            VaryingMachina.logger.error("Need outputs for recipe. DID NOT ADD RECIPE.");
            return;
        }

        recipes.add(recipe);
    }

    public void addCTRecipe(ProcessRecipe recipe) {
        if (recipe.getItemInputs().size() == 0 && recipe.getFluidInputs().size() == 0) {
            VaryingMachina.logger.error("Need inputs for recipe. DID NOT ADD RECIPE.");
            return;
        }

        if (recipe.getItemOutputs().size() == 0 && recipe.getFluidOutputs().size() == 0) {
            VaryingMachina.logger.error("Need outputs for recipe. DID NOT ADD RECIPE.");
            return;
        }

        ctAddedRecipes.add(recipe);
    }

    public void processCTRecipes() {
        for (ProcessRecipe recipe : ctRemovedRecipes) {
            removeRecipe(recipe);
        }
        for (ProcessRecipe r : ctAddedRecipes) {
            addRecipe(r);
        }
    }

    public void removeRecipe(ProcessRecipe recipe) {
        if ((recipe.getItemOutputs() == null || recipe.getItemOutputs().size() == 0)
                && (recipe.getFluidOutputs() == null || recipe.getFluidOutputs().size() == 0)) {
            VaryingMachina.logger.error("Need outputs for recipe. DID NOT REMOVE RECIPE.");
            return;
        }

        if ((recipe.getItemInputs() == null || recipe.getItemInputs().size() == 0)
                && (recipe.getFluidInputs() == null || recipe.getFluidInputs().size() == 0)) {
            List<Integer> recipesToRemoveAt = new ArrayList<Integer>();
            for (int i = 0; i < recipes.size(); i++) {
                boolean valid = true;
                for (ItemStack iOut : recipes.get(i).getItemOutputs()) {
                    for (ItemStack rOut : recipe.getItemOutputs()) {
                        if (!iOut.isItemEqual(rOut))
                            valid = false;
                    }
                }
                for (FluidStack iOut : recipes.get(i).getFluidOutputs()) {
                    for (FluidStack rOut : recipe.getFluidOutputs()) {
                        if (!iOut.isFluidEqual(rOut))
                            valid = false;
                    }
                }
                if (valid)
                    recipesToRemoveAt.add(i);
            }
            for (int i = recipesToRemoveAt.size() - 1; i >= 0; i--) {
                recipes.remove((int) recipesToRemoveAt.get(i));
            }
            return;
        }

        List<Integer> recipesToRemoveAt = new ArrayList<Integer>();
        for (int i = 0; i < recipes.size(); i++) {
            if (recipes.get(i).isInputRecipeEqualTo(recipe, false)) {
                boolean valid = true;
                for (ItemStack iOut : recipes.get(i).getItemOutputs()) {
                    for (ItemStack rOut : recipe.getItemOutputs()) {
                        if (!(iOut.isEmpty() && rOut.isEmpty()) && !iOut.isItemEqual(rOut))
                            valid = false;
                    }
                }
                for (FluidStack iOut : recipes.get(i).getFluidOutputs()) {
                    for (FluidStack rOut : recipe.getFluidOutputs()) {
                        if (!iOut.isFluidEqual(rOut))
                            valid = false;
                    }
                }
                if (valid)
                    recipesToRemoveAt.add(i);
            }
        }
        for (int i = recipesToRemoveAt.size() - 1; i >= 0; i--) {
            recipes.remove((int) recipesToRemoveAt.get(i));
        }
    }

    public void removeCTRecipe(ProcessRecipe recipe) {
        ctRemovedRecipes.add(recipe);
    }

    public void drawJEIInfo(ProcessRecipe rec, Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX,
                            int mouseY) {

    }

    public String getType() {
        return type;
    }
}