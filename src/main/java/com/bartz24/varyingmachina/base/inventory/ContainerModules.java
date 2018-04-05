package com.bartz24.varyingmachina.base.inventory;

import javax.annotation.Nonnull;

import com.bartz24.varyingmachina.base.item.ItemModule;
import com.bartz24.varyingmachina.base.tile.TileCasing;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerModules extends ContainerBase {
	protected TileCasing tile;

	public ContainerModules(EntityPlayer player, TileCasing te) {
		super(player, te);
		tile = te;
		this.addSlotToContainer(getNewModuleSlot(EnumFacing.UP.ordinal(), 70, 20));
		this.addSlotToContainer(getNewModuleSlot(EnumFacing.WEST.ordinal(), 52, 38));
		this.addSlotToContainer(getNewModuleSlot(EnumFacing.SOUTH.ordinal(), 70, 38));
		this.addSlotToContainer(getNewModuleSlot(EnumFacing.EAST.ordinal(), 88, 38));
		this.addSlotToContainer(getNewModuleSlot(EnumFacing.DOWN.ordinal(), 70, 56));
		this.addSlotToContainer(getNewModuleSlot(EnumFacing.NORTH.ordinal(), 88, 56));
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