package com.bartz24.varyingmachina.base.tile;

import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileItemInventory extends TileBase {
	private ItemStackHandler inputInventory;
	private ItemStackHandler outputInventory;

	public TileItemInventory(String name, int slots) {
		this(name, slots, 0);
	}

	public TileItemInventory(String name, int inputSlots, int outputSlots) {
		super(name);
		inputInventory = new ItemStackHandler(inputSlots) {
			protected void onContentsChanged(int slot) {
				super.onContentsChanged(slot);
				TileItemInventory.this.markDirty();
			}
		};
		outputInventory = new ItemStackHandler(outputSlots) {
			protected void onContentsChanged(int slot) {
				super.onContentsChanged(slot);
				TileItemInventory.this.markDirty();
			}
		};
	}

	public ItemStackHandler getInputInventory() {
		return inputInventory;
	}

	public ItemStackHandler getOutputInventory() {
		return outputInventory;
	}

	public void setInputInventory(ItemStackHandler handler) {
		inputInventory = handler;
	}

	public void setOutputInventory(ItemStackHandler handler) {
		outputInventory = handler;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);

		compound.setTag("inputInv", inputInventory.serializeNBT());
		compound.setTag("outputInv", outputInventory.serializeNBT());
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		inputInventory.deserializeNBT(compound.getCompoundTag("inputInv"));
		outputInventory.deserializeNBT(compound.getCompoundTag("outputInv"));
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) this.inputInventory;
		}
		return super.getCapability(capability, facing);
	}

	public void dropInventory() {
		for (int i = 0; i < inputInventory.getSlots(); ++i) {
			ItemStack itemstack = inputInventory.getStackInSlot(i);

			if (!itemstack.isEmpty()) {
				InventoryHelper.spawnItemStack(getWorld(), pos.getX(), pos.getY(), pos.getZ(), itemstack);
			}
		}
		for (int i = 0; i < outputInventory.getSlots(); ++i) {
			ItemStack itemstack = outputInventory.getStackInSlot(i);

			if (!itemstack.isEmpty()) {
				InventoryHelper.spawnItemStack(getWorld(), pos.getX(), pos.getY(), pos.getZ(), itemstack);
			}
		}
	}
}