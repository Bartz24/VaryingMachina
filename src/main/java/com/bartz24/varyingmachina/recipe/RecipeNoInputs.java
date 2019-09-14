package com.bartz24.varyingmachina.recipe;

import com.bartz24.varyingmachina.tile.IRecipeProcessor;

public class RecipeNoInputs<T extends IRecipeProcessor> extends RecipeBase<T> {

    public RecipeNoInputs(OutputBase... outputs){
        super(outputs);
    }

    @Override
    public boolean isValidRecipe() {
        if (outputs.size() == 0)
            return false;
        for (OutputBase o : outputs) {
            if (!o.isValid())
                return false;
        }

        return true;
    }
}
