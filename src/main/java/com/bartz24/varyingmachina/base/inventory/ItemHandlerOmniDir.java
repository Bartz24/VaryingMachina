package com.bartz24.varyingmachina.base.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemHandlerOmniDir extends ItemStackHandler {

    protected int[] slotsNoInsert;
    protected int[] slotsNoExtract;

    public ItemHandlerOmniDir(int size, int[] slotsNoInsert, int[] slotsNoExtract) {
        super(size);
        this.slotsNoExtract = slotsNoExtract;
        this.slotsNoInsert = slotsNoInsert;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = super.serializeNBT();
        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < stacks.size(); i++) {
            nbtTagList.appendTag(new NBTTagInt(slotsNoInsert[i]));
        }
        nbt.setInteger("noInsertSize", slotsNoInsert.length);
        nbt.setTag("noInsert", nbtTagList);
        nbtTagList = new NBTTagList();
        for (int i = 0; i < stacks.size(); i++) {
            nbtTagList.appendTag(new NBTTagInt(slotsNoExtract[i]));
        }
        nbt.setInteger("noExtractSize", slotsNoExtract.length);
        nbt.setTag("noExtract", nbtTagList);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        super.deserializeNBT(nbt);
        slotsNoInsert = new int[nbt.getInteger("noInsertSize")];
        NBTTagList tagList = nbt.getTagList("noInsert", Constants.NBT.TAG_INT);
        for (int i = 0; i < tagList.tagCount(); i++) {
            slotsNoInsert[i] = tagList.getIntAt(i);
        }
        slotsNoExtract = new int[nbt.getInteger("noExtractSize")];
        tagList = nbt.getTagList("noExtract", Constants.NBT.TAG_INT);
        for (int i = 0; i < tagList.tagCount(); i++) {
            slotsNoExtract[i] = tagList.getIntAt(i);
        }
    }
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (Arrays.asList(slotsNoInsert).contains(slot)) {
            return stack;
        }
        return super.insertItem(slot, stack, simulate);
    }

    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (Arrays.asList(slotsNoExtract).contains(slot)) {
            return ItemStack.EMPTY;
        }
        return super.extractItem(slot, amount, simulate);
    }
}
