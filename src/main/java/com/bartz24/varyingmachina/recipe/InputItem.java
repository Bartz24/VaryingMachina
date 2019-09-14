package com.bartz24.varyingmachina.recipe;

import com.bartz24.varyingmachina.inventory.ItemHandlerRestricted;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class InputItem extends InputBase<ItemHandlerRestricted, ItemStack> {
    private ItemStack stack;
    protected String invId;

    public InputItem(ItemStack stack, String invId) {
        this.stack = stack.copy();
        this.invId = invId;
    }

    public InputItem(ItemStack stack) {
        this(stack, "input");
    }


    @Override
    public String getId() {
        return invId;
    }

    @Override
    public boolean hasInput(ItemHandlerRestricted inventory) {
        ItemStack remove = getStack().copy();
        for (int s = inventory.getSlots() - 1; s >= 0; s--) {
            if (inventory.getStackInSlot(s).isItemEqual(remove)) {
                remove.shrink(inventory.extractItemInternal(s, remove.getCount(), true).getCount());
                if (remove.isEmpty())
                    return true;
            }
        }
        return false;
    }

    @Override
    public void drawItemsFromInventory(ItemHandlerRestricted inventory) {
        ItemStack remove = getStack().copy();
        for (int s = inventory.getSlots() - 1; s >= 0; s--) {
            if (inventory.getStackInSlot(s).isItemEqual(remove)) {
                remove.shrink(inventory.extractItemInternal(s, remove.getCount(), false).getCount());
                if (remove.isEmpty())
                    return;
            }
        }
    }

    @Override
    public boolean hasEnough(InputBase in2) {
        if (!(in2 instanceof InputItem))
            return false;
        InputItem inItem2 = (InputItem) in2;
        return getStack().isItemEqual(inItem2.getStack()) && inItem2.getStack().getCount() >= getStack().getCount();
    }

    @Override
    public List<ItemStack> getInputs() {
        return Collections.singletonList(getStack().copy());
    }

    @Override
    public boolean isValid() {
        return !getStack().isEmpty();
    }

    public ItemStack getStack() {
        return stack;
    }

    @Override
    public InputItem scale(int factor) {
        stack.setCount(stack.getCount() * factor);
        return this;
    }

    @Override
    public boolean combineWith(InputBase input2) {
        if (input2 instanceof InputItem && getStack().isItemEqual(((InputItem) input2).getStack())) {
            stack.grow(((InputItem) input2).getStack().getCount());
            return true;
        }
        return false;
    }

    @Override
    public InputItem copy() {
        return new InputItem(stack, invId);
    }

    @Override
    public Class<ItemHandlerRestricted> getInvType() {
        return ItemHandlerRestricted.class;
    }
}
