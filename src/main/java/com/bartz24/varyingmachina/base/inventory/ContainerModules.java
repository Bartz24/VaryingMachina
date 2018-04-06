package com.bartz24.varyingmachina.base.inventory;

import javax.annotation.Nonnull;

import com.bartz24.varyingmachina.base.item.ItemModule;
import com.bartz24.varyingmachina.base.tile.TileCasing;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerModules extends ContainerBase {
	protected TileCasing tile;

	public ContainerModules(EntityPlayer player, TileCasing te, int moduleIndex) {
		super(player, te);
		moduleIndex--;
		tile = te;
		if (moduleIndex >= 0 && moduleIndex < 6) {
			this.addSlotToContainer(getNewModuleSlot(moduleIndex, 80, 21));
		} else {
			this.addSlotToContainer(getNewModuleSlot(EnumFacing.UP.ordinal(), 80, 21));
			this.addSlotToContainer(getNewModuleSlot(EnumFacing.WEST.ordinal(), 62, 39));
			this.addSlotToContainer(getNewModuleSlot(EnumFacing.SOUTH.ordinal(), 80, 39));
			this.addSlotToContainer(getNewModuleSlot(EnumFacing.EAST.ordinal(), 98, 39));
			this.addSlotToContainer(getNewModuleSlot(EnumFacing.DOWN.ordinal(), 80, 57));
			this.addSlotToContainer(getNewModuleSlot(EnumFacing.NORTH.ordinal(), 98, 57));
		}
	}

	private SlotItemHandler getNewModuleSlot(int index, int x, int y) {
		return new SlotItemHandler(tile.modules, index, x, y) {
			@Override
			public void putStack(@Nonnull ItemStack stack) {
				ContainerModules.this.tile.setModule(stack, EnumFacing.values()[index]);
			}

			public void onSlotChanged() {
				if (getStack().isEmpty())
					ContainerModules.this.tile.setModule(getStack(), EnumFacing.values()[index]);
				super.onSlotChanged();
			}

			@Override
			public int getSlotStackLimit() {
				return 1;
			}

			public boolean isItemValid(@Nonnull ItemStack stack) {
				if (!stack.isEmpty() && !(stack.getItem() instanceof ItemModule))
					return false;

				return super.isItemValid(stack);
			}
		};
	}

}