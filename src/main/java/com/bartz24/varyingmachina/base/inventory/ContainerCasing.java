package com.bartz24.varyingmachina.base.inventory;

import com.bartz24.varyingmachina.base.tile.TileCasing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

import java.util.ArrayList;

public class ContainerCasing extends ContainerBase {
    public String machineID = "";

    public ContainerCasing(EntityPlayer player, TileCasing te) {
        super(player, te, te.machineStored.isEmpty() ? 0 : te.getMachine().getInvPos(te.machineStored)[0],
                te.machineStored.isEmpty() ? 0 : te.getMachine().getInvPos(te.machineStored)[1]);

        if (!te.machineStored.isEmpty())
            machineID = te.getMachine().getMachineID();

        if (!te.machineStored.isEmpty()) {
            for (Slot s : te.getMachine().getSlots(te, new ArrayList<>()))
                this.addSlotToContainer(s);
        }
    }

}