package com.bartz24.varyingmachina.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ItemHandlerFiltered extends ItemHandlerRestricted {
    private List<List<ItemStack>> filters = new ArrayList<>();

    public ItemHandlerFiltered(int size, boolean noInsert, boolean noExtract) {
        this(size, noInsert, noExtract, new ArrayList<>());
        for (int i = 0; i < size; i++)
            filters.add(new ArrayList<>());
    }


    public ItemHandlerFiltered(int size, boolean noInsert, boolean noExtract, List<List<ItemStack>> filters) {
        super(size, noInsert, noExtract);
        this.filters = filters;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (getFilter(slot).size() == 0)
            return super.insertItem(slot, stack, simulate);
        for (ItemStack s : getFilter(slot)) {
            if (s.isItemEqual(stack)) {
                return super.insertItem(slot, stack, simulate);
            }
        }
        return stack;
    }

    public void setFilters(int slot, List<ItemStack> filter) {
        if (slot >= filters.size())
            filters.add(filter);
        else
            filters.set(slot, filter);
    }

    public List<ItemStack> getFilter(int slot) {
        if (slot >= filters.size())
            return new ArrayList<ItemStack>();
        return filters.get(slot);
    }

    @Override
    public void setSize(int size) {
        super.setSize(size);
        filters = new ArrayList<>(size);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compound = super.serializeNBT();
        ListNBT filterList = new ListNBT();
        for (int i = 0; i < filters.size(); i++) {
            ListNBT list = new ListNBT();
            for (ItemStack s : filters.get(i)) {
                CompoundNBT itemData = new CompoundNBT();
                s.write(itemData);
                itemData.putInt("filterSize", s.getCount());
                list.add(itemData);
            }
            filterList.add(list);
        }
        compound.put("filters", filterList);
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        filters = new ArrayList<>();
        ListNBT filterList = nbt.getList("filters", Constants.NBT.TAG_LIST);
        for (int i = 0; i < filterList.size(); i++) {
            List<ItemStack> stacks = new ArrayList<>();
            ListNBT list = filterList.getList(i);
            for (int s = 0; s < list.size(); s++) {
                stacks.add(ItemStack.read(list.getCompound(s)));
                stacks.get(stacks.size() - 1).setCount(list.getCompound(s).getInt("filterSize"));
            }
            filters.add(stacks);
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        if (getFilter(slot).size() > 0)
            return getFilter(slot).get(0).getCount() * getSlotMult();
        else
            return super.getSlotLimit(slot);
    }
}
