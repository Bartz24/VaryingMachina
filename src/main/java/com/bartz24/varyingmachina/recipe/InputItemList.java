package com.bartz24.varyingmachina.recipe;

import com.bartz24.varyingmachina.inventory.ItemHandlerRestricted;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputItemList extends InputBase<ItemHandlerRestricted, ItemStack> {
    private List<ItemStack> stacks = new ArrayList<>();
    protected String invId;

    public InputItemList(List<ItemStack> stacks, String invId) {
        stacks.forEach(i -> this.stacks.add(i.copy()));
        this.invId = invId;
    }

    public InputItemList(List<ItemStack> stacks) {
        this(stacks, "input");
    }

    public InputItemList(ItemStack... stacks) {
        this(Arrays.asList(stacks));
    }


    @Override
    public String getId() {
        return invId;
    }

    @Override
    public boolean hasInput(ItemHandlerRestricted inventory) {
        for (int s = inventory.getSlots() - 1; s >= 0; s--) {
            for (ItemStack remove : getInputs()) {
                remove = remove.copy();
                if (inventory.getStackInSlot(s).isItemEqual(remove)) {
                    remove.shrink(inventory.extractItemInternal(s, remove.getCount(), true).getCount());
                    if (remove.isEmpty())
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public void drawItemsFromInventory(ItemHandlerRestricted inventory) {
        for (int s = inventory.getSlots() - 1; s >= 0; s--) {
            for (ItemStack remove : getInputs()) {
                remove = remove.copy();
                if (inventory.getStackInSlot(s).isItemEqual(remove)) {
                    remove.shrink(inventory.extractItemInternal(s, remove.getCount(), false).getCount());
                    if (remove.isEmpty())
                        return;
                }
            }
        }
    }

    @Override
    public boolean hasEnough(InputBase in2) {
        if (!(in2 instanceof InputItem))
            return false;
        InputItem inItem2 = (InputItem) in2;
        for (ItemStack stack : getInputs()) {
            if (stack.isItemEqual(inItem2.getStack()) && inItem2.getStack().getCount() >= stack.getCount())
                return true;
        }
        return false;
    }

    @Override
    public List<ItemStack> getInputs() {
        List<ItemStack> items = new ArrayList<>();
        stacks.forEach(i -> {
            if (!i.isEmpty())
                items.add(i.copy());
        });
        return items;
    }

    @Override
    public boolean combineWith(InputBase input2) {

        if (input2 instanceof InputItemList && stacks.size() == ((InputItemList) input2).stacks.size()) {
            for (int i = 0; i < stacks.size(); i++) {
                if (!stacks.get(i).isItemEqual(((InputItemList) input2).stacks.get(i)))
                    return false;
            }
            for (int i = 0; i < stacks.size(); i++) {
                stacks.get(i).grow(((InputItemList) input2).stacks.get(i).getCount());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isValid() {
        return getInputs().size() > 0;
    }

    @Override
    public InputItemList scale(int factor) {
        stacks.forEach(stack -> stack.setCount(stack.getCount() * factor));
        return this;
    }

    @Override
    public InputItemList copy() {
        return new InputItemList(stacks, invId);
    }

    @Override
    public Class<ItemHandlerRestricted> getInvType() {
        return ItemHandlerRestricted.class;
    }
}
