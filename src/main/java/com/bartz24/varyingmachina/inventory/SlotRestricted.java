package com.bartz24.varyingmachina.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotRestricted extends SlotItemHandler {
    public SlotRestricted(ItemHandlerRestricted itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean canTakeStack(PlayerEntity playerIn) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int amount) {
        return ((ItemHandlerRestricted) getItemHandler()).extractItemInternal(getSlotIndex(), amount, false);
    }

    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack) {
        ItemStack maxAdd = stack.copy();
        int maxInput = getSlotStackLimit();
        maxAdd.setCount(maxInput);

        IItemHandler handler = this.getItemHandler();
        ItemStack currentStack = handler.getStackInSlot(getSlotIndex());
        if (handler instanceof IItemHandlerModifiable) {
            IItemHandlerModifiable handlerModifiable = (IItemHandlerModifiable) handler;

            handlerModifiable.setStackInSlot(getSlotIndex(), ItemStack.EMPTY);

            ItemStack remainder = handlerModifiable.insertItem(getSlotIndex(), maxAdd, true);

            handlerModifiable.setStackInSlot(getSlotIndex(), currentStack);

            return maxInput - remainder.getCount();
        } else {
            ItemStack remainder = handler.insertItem(getSlotIndex(), maxAdd, true);

            int current = currentStack.getCount();
            int added = maxInput - remainder.getCount();
            return current + added;
        }
    }
}
