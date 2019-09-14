package com.bartz24.varyingmachina.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

import static net.minecraftforge.items.ItemHandlerHelper.canItemStacksStackRelaxed;

public class ItemHandlerRestricted extends ItemHandlerLargeSlots {
    private boolean noInsert, noExtract;

    private TileEntity parent;
    private Supplier<Integer> slotMultiplier = () -> 1;
    private int slotMult;

    public ItemHandlerRestricted(int size, boolean noInsert, boolean noExtract) {
        super(size);
        this.noExtract = noExtract;
        this.noInsert = noInsert;
    }

    public void setParent(TileEntity parent) {
        this.parent = parent;
    }

    public void setSlotMultiplier(Supplier<Integer> slotMultiplier) {
        this.slotMultiplier = slotMultiplier;
    }

    public TileEntity getParent() {
        return parent;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (noExtract)
            return ItemStack.EMPTY;
        return super.extractItem(slot, amount, simulate);
    }

    @Nonnull
    public ItemStack extractItemInternal(int slot, int amount, boolean simulate) {
        return super.extractItemInternal(slot, amount, simulate);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (noInsert)
            return stack;
        return super.insertItem(slot, stack, simulate);
    }

    @Nonnull
    public ItemStack insertItemInternal(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return super.insertItem(slot, stack, simulate);
    }

    public boolean isNoExtract() {
        return noExtract;
    }

    public boolean isNoInsert() {
        return noInsert;
    }

    @Nonnull
    public ItemStack insertItemStacked(@Nonnull ItemStack stack, boolean simulate) {
        if (stack.isEmpty())
            return stack;
        int sizeInventory = getSlots();

        // go through the inventory and try to fill up already existing items
        for (int i = 0; i < sizeInventory; i++) {
            ItemStack slot = getStackInSlot(i);
            if (canItemStacksStackRelaxed(slot, stack)) {
                stack = insertItemInternal(i, stack, simulate);

                if (stack.isEmpty()) {
                    break;
                }
            }
        }

        // insert remainder into empty slots
        if (!stack.isEmpty()) {
            // find empty slot
            for (int i = 0; i < sizeInventory; i++) {
                if (getStackInSlot(i).isEmpty()) {
                    stack = insertItemInternal(i, stack, simulate);
                    if (stack.isEmpty()) {
                        break;
                    }
                }
            }
        }

        return stack;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compound = super.serializeNBT();
        compound.putBoolean("noExtract", noExtract);
        compound.putBoolean("noInsert", noInsert);
        compound.putInt("slotMult", getSlotMult());
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        noExtract = nbt.getBoolean("noExtract");
        noInsert = nbt.getBoolean("noInsert");
        slotMult = nbt.getInt("slotMult");
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        if (parent != null)
            parent.markDirty();
    }

    public int getSlotMult() {
        if (slotMultiplier != null && slotMultiplier.get() != 1)
            slotMult = slotMultiplier.get();
        return Math.max(1, slotMult);
    }

    @Override
    public int getSlotLimit(int slot) {
        return super.getSlotLimit(slot) * getSlotMult();
    }
}
