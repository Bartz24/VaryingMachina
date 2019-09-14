package com.bartz24.varyingmachina.recipe;

import com.bartz24.varyingmachina.tile.TileEntityMachine;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RecipeMachineDestroy<T extends TileEntityMachine> extends RecipeBase<T> {

    private RecipeBase mainRecipe;

    public RecipeMachineDestroy(RecipeBase mainRecipe) {
        super();
        this.mainRecipe = mainRecipe;
    }

    @Override
    public List<InputBase> getInputObjects() {
        List<InputBase> inputs = new ArrayList<>();
        for (Object stack : mainRecipe.getOutputs(ItemStack.class, "output")) {
            if (stack instanceof ItemStack && !((ItemStack) stack).isEmpty())
                inputs.add(new InputItem(((ItemStack) stack).copy()));
            else
                return new ArrayList<>();
        }

        return inputs;
    }

    @Override
    public List<OutputBase> getOutputObjects() {
        List<OutputBase> outputs = new ArrayList<>();
        for (Object list : mainRecipe.getInputs(ItemStack.class, "input")) {
            if (list instanceof List && ((List) list).size() > 0) {
                for (Object stack : (List) list) {
                    if (stack instanceof ItemStack && !((ItemStack) stack).isEmpty()) {
                        ItemStack itemStack = ((ItemStack) stack).copy();
                        itemStack.shrink(itemStack.getCount() / 2);
                        if (!itemStack.isEmpty())
                            outputs.add(new OutputItem(itemStack));
                        break;
                    }
                }
            } else
                return new ArrayList<>();
        }

        return outputs;
    }
}
