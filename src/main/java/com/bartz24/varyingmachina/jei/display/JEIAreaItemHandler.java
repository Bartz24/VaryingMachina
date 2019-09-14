package com.bartz24.varyingmachina.jei.display;

import com.bartz24.varyingmachina.inventory.ContainerArea;
import com.bartz24.varyingmachina.jei.JEIHelper;
import com.bartz24.varyingmachina.recipe.OutputBase;
import com.bartz24.varyingmachina.recipe.RecipeBase;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class JEIAreaItemHandler extends ContainerArea {

    public JEIAreaItemHandler(RecipeBase recipeBase, String inv, XAnchorDirection xDirection, YAnchorDirection yDirection, int weight, boolean input) {
        super(xDirection, yDirection, weight);

        List stacks = input ? recipeBase.getInputs(ItemStack.class, inv) : recipeBase.getOutputs(ItemStack.class, inv);


        int maxWidth = (int) Math.ceil(Math.sqrt((double) stacks.size()) * Math.exp((double) stacks.size() / 200));

        int x = 0, y = 0;
        for (int i = 0; i < stacks.size(); i++) {
            if (!input) {
                Supplier<List<String>> tooltips = ((OutputBase) (recipeBase.getOutputObjects(inv).get(i))).getJEITooltips();
                slots.add(new SlotJEI(i, x * 18, y * 18, Collections.singletonList((ItemStack) stacks.get(i)), tooltips));
            } else {
                Supplier<List<String>> tooltips = /*((InputBase) (recipeBase.getInputObjects(inv).get(i))).getJEITooltips()*/ () -> new ArrayList<>();
                slots.add(new SlotJEI(i, x * 18, y * 18, (List<ItemStack>) stacks.get(i), tooltips));
            }
            x++;
            if (x >= maxWidth) {
                x = 0;
                y++;
            }
        }
    }

    @Override
    public void drawJEI(IGuiHelper helper, int leftX, int leftY) {
        super.drawJEI(helper, leftX, leftY);
        for (Slot slot : slots) {
            JEIHelper.createSlot(leftX + slot.xPos - 1, leftY + 16 + slot.yPos - 1, helper).draw();
        }
    }
}
