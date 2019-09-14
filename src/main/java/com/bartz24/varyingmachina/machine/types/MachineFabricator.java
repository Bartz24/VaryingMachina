package com.bartz24.varyingmachina.machine.types;

import com.bartz24.varyingmachina.machine.MachineExtraComponents;
import com.bartz24.varyingmachina.machine.MachineType;
import com.bartz24.varyingmachina.machine.ModMachines;
import com.bartz24.varyingmachina.tile.TileEntityMachine;
import net.minecraft.util.ResourceLocation;

public class MachineFabricator extends MachineType<TileEntityMachine> {
    public MachineFabricator() {
        super("fabricator", ModMachines.Stats.rating, ModMachines.Stats.speed, ModMachines.Stats.fuelefficiency);
        this.setTextureFront(new ResourceLocation("varyingmachina", "block/fabricator"));
        this.addInputItemHandler("input");
        this.addOutputItemHandler("output");
        this.setExtraInput(MachineExtraComponents.assembler);
    }
}
