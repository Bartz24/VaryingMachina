package com.bartz24.varyingmachina.jei.display;

import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.function.Supplier;

public class SlotJEI extends Slot {
    List<ItemStack> stacks;
    Supplier<List<String>> tooltips;

    public SlotJEI(int index, int xPosition, int yPosition, List<ItemStack> stacks, Supplier<List<String>> tooltips) {
        super(null, index, xPosition, yPosition);
        this.stacks = stacks;
        this.tooltips = tooltips;
    }

    public List<ItemStack> getStacks() {
        return stacks;
    }

    public Supplier<List<String>> getTooltips() {
        return tooltips;
    }

}
