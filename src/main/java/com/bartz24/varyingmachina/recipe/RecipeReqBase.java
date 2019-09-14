package com.bartz24.varyingmachina.recipe;

import com.bartz24.varyingmachina.tile.IRecipeProcessor;
import javafx.util.Pair;

public abstract class RecipeReqBase {
    public abstract boolean requirementsMet(IRecipeProcessor tile);
    public abstract boolean requirementsMet(RecipeReqBase req2);
    public abstract Pair<String, Integer> getDrawText();
}
