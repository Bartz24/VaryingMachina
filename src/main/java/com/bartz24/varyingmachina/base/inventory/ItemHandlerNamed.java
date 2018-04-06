package com.bartz24.varyingmachina.base.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

public class ItemHandlerNamed extends ItemStackHandler {

	protected List<String> names;

	public ItemHandlerNamed(String... names) {
		this(1, names);
	}

	public ItemHandlerNamed(List<String> names) {
		this(1, names);
	}

	public ItemHandlerNamed(int size, List<String> names) {
		super(size);
		this.names = names;
	}

	public ItemHandlerNamed(int size, String... names) {
		this(size, Arrays.asList(names));
	}

	public ItemHandlerNamed(NonNullList<ItemStack> stacks, List<String> names) {
		super(stacks);
		this.names = names;
	}

	public ItemHandlerNamed(NonNullList<ItemStack> stacks, String... names) {
		this(stacks, Arrays.asList(names));
	}

	public String getNameInSlot(int slot) {
		if (slot >= names.size())
			return "";
		return names.get(slot);
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = super.serializeNBT();
		NBTTagList nbtTagList = new NBTTagList();
		for (int i = 0; i < stacks.size(); i++) {
			nbtTagList.appendTag(new NBTTagString(getNameInSlot(i)));
		}
		nbt.setTag("Names", nbtTagList);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		super.deserializeNBT(nbt);
		names = new ArrayList();
		NBTTagList tagList = nbt.getTagList("Names", Constants.NBT.TAG_STRING);
		for (int i = 0; i < tagList.tagCount(); i++) {
			names.add(tagList.getStringTagAt(i));
		}
	}
}
