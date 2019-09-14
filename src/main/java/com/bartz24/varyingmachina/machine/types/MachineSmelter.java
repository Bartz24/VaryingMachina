package com.bartz24.varyingmachina.machine.types;

import com.bartz24.varyingmachina.machine.MachineExtraComponents;
import com.bartz24.varyingmachina.machine.MachineType;
import com.bartz24.varyingmachina.machine.ModMachines;
import com.bartz24.varyingmachina.tile.TileEntityMachine;
import net.minecraft.util.ResourceLocation;

public class MachineSmelter extends MachineType<TileEntityMachine> {
    public MachineSmelter() {
        super("smelter", ModMachines.Stats.rating, ModMachines.Stats.speed, ModMachines.Stats.fuelefficiency, ModMachines.Stats.hu, ModMachines.Stats.pressure);
        this.setTextureFront(new ResourceLocation("varyingmachina", "block/smelter"));
        this.addInputItemHandler("input");
        this.addOutputItemHandler("output");
        this.setExtraInput(MachineExtraComponents.smelter);
    }
}
