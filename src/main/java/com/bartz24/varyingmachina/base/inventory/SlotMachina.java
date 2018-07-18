package com.bartz24.varyingmachina.base.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotMachina extends SlotItemHandler {
    private boolean input;

    public SlotMachina(IItemHandler itemHandler, int index, int xPosition, int yPosition, boolean input) {
        super(itemHandler, index, xPosition, yPosition);
        this.input = input;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        if (!input)
            return false;
        return super.isItemValid(stack);
    }

}
