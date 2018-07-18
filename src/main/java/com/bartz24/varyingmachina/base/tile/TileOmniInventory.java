package com.bartz24.varyingmachina.base.tile;

import com.bartz24.varyingmachina.base.inventory.ItemHandlerNamed;
import com.bartz24.varyingmachina.base.inventory.ItemHandlerOmniDir;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileOmniInventory extends TileGenericPower {
	private ItemHandlerOmniDir inventory;

	public TileOmniInventory(String name, int slots, int[] noInsert, int[] noExtract) {
		this(name, slots, noInsert, noExtract, 0, 0, 0);
	}

	public TileOmniInventory(String name, int slots) {
		this(name, slots, 0, 0, 0);
	}

	public TileOmniInventory(String name, int slots, int maxPower, int maxIn, int maxOut) {
		this(name, slots, new int[0], new int[0], maxPower, maxIn, maxOut);
	}

	public TileOmniInventory(String name, int slots, int[] noInsert, int[] noExtract, int maxPower, int maxIn, int maxOut) {
		super(name, maxPower, maxIn, maxOut);
		inventory = new ItemHandlerOmniDir(slots, noInsert, noExtract) {
			protected void onContentsChanged(int slot) {
				super.onContentsChanged(slot);
				TileOmniInventory.this.markDirty();
			}
		};
	}

	public ItemHandlerOmniDir getInventory() {
		return inventory;
	}

	public void setInventory(ItemHandlerOmniDir handler) {
		inventory = handler;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);

		compound.setTag("inv", inventory.serializeNBT());
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		inventory.deserializeNBT(compound.getCompoundTag("inv"));
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
			return (T) this.inventory;
		}
		return super.getCapability(capability, facing);
	}

	public void dropInventory() {
		for (int i = 0; i < inventory.getSlots(); ++i) {
			ItemStack itemstack = inventory.getStackInSlot(i);

			if (!itemstack.isEmpty()) {
				InventoryHelper.spawnItemStack(getWorld(), pos.getX(), pos.getY(), pos.getZ(), itemstack);
			}
		}
	}
}