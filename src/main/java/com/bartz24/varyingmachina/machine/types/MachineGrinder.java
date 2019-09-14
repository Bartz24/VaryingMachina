package com.bartz24.varyingmachina.machine.types;

import com.bartz24.varyingmachina.machine.MachineExtraComponents;
import com.bartz24.varyingmachina.machine.MachineType;
import com.bartz24.varyingmachina.machine.ModMachines;
import com.bartz24.varyingmachina.tile.TileEntityMachine;
import net.minecraft.util.ResourceLocation;

public class MachineGrinder extends MachineType<TileEntityMachine> {
    public MachineGrinder() {
        super("grinder", ModMachines.Stats.rating, ModMachines.Stats.speed, ModMachines.Stats.fuelefficiency, ModMachines.Stats.production);
        this.setTextureFront(new ResourceLocation("varyingmachina", "block/grinder"));
        this.addInputItemHandler("input");
        this.addOutputItemHandler("output");
        this.setExtraInput(MachineExtraComponents.grinder);
    }
}
