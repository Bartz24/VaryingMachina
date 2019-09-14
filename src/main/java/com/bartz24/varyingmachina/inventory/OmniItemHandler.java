package com.bartz24.varyingmachina.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;

public class OmniItemHandler implements IItemHandlerModifiable {

    private LinkedHashMap<String, ItemStackHandler> handlers = new LinkedHashMap<>();

    public void setHandler(String id, ItemStackHandler handler) {
        handlers.put(id, handler);
    }

    public String[] getHandlerIds() {
        return handlers.keySet().toArray(new String[handlers.size()]);
    }

    private ItemStackHandler[] getHandlers() {
        return handlers.values().toArray(new ItemStackHandler[handlers.size()]);
    }


    public ItemStackHandler getHandler(String id) {
        return handlers.get(id);
    }

    public ItemHandlerRestricted getHandlerRestricted(String id) {
        ItemStackHandler handler = getHandler(id);
        return handler instanceof ItemHandlerRestricted ? (ItemHandlerRestricted) handler : null;
    }

    @Override
    public int getSlots() {
        int slots = 0;
        for (ItemStackHandler handler : handlers.values()) {
            slots += handler.getSlots();
        }
        return slots;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        int s = slot, i = 0;
        for (; s >= 0 && s >= getHandlers()[i].getSlots(); ) {
            s -= getHandlers()[i].getSlots();
            i++;
        }
        return getHandlers()[i].getStackInSlot(s);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        int s = slot, i = 0;
        for (; s >= 0 && s >= getHandlers()[i].getSlots(); ) {
            s -= getHandlers()[i].getSlots();
            i++;
        }
        return getHandlers()[i].insertItem(s, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        int s = slot, i = 0;
        for (; s >= 0 && s >= getHandlers()[i].getSlots(); ) {
            s -= getHandlers()[i].getSlots();
            i++;
        }
        return getHandlers()[i].extractItem(s, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        int s = slot, i = 0;
        for (; s >= 0 && s >= getHandlers()[i].getSlots(); ) {
            s -= getHandlers()[i].getSlots();
            i++;
        }
        return getHandlers()[i].getSlotLimit(s);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        int s = slot, i = 0;
        for (; s >= 0 && s >= getHandlers()[i].getSlots(); ) {
            s -= getHandlers()[i].getSlots();
            i++;
        }
        return getHandlers()[i].isItemValid(s, stack);
    }

    public CompoundNBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();
        for (int i = 0; i < handlers.size(); i++) {
            compound.put("inv" + i, getHandlers()[i].serializeNBT());
        }
        return compound;
    }

    public void deserializeNBT(CompoundNBT compound) {
        for (int i = 0; i < handlers.size(); i++) {
            getHandlers()[i].deserializeNBT(compound.getCompound("inv" + i));
        }
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        int s = slot, i = 0;
        for (; s >= getHandlers()[i].getSlots(); ) {
            s -= getHandlers()[i].getSlots();
            i++;
        }
        getHandlers()[i].setStackInSlot(s, stack);
    }
}
