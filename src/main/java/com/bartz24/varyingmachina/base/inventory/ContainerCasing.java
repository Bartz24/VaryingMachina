package com.bartz24.varyingmachina.base.inventory;

import java.util.ArrayList;

import com.bartz24.varyingmachina.base.tile.TileCasing;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerCasing extends ContainerBase
{

	public ContainerCasing(EntityPlayer player, TileCasing te)
	{
		super(player, te, te.machineStored.isEmpty() ? 0 : te.getMachine().getInvPos(te.machineStored)[0],
				te.machineStored.isEmpty() ? 0 : te.getMachine().getInvPos(te.machineStored)[1]);

		if (!te.machineStored.isEmpty())
		{
			for (Slot s : te.getMachine().getSlots(te, new ArrayList<>()))
				this.addSlotToContainer(s);
		}
	}

}