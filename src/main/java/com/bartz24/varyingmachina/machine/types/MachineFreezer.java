package com.bartz24.varyingmachina.machine.types;

import com.bartz24.varyingmachina.machine.MachineExtraComponents;
import com.bartz24.varyingmachina.machine.MachineType;
import com.bartz24.varyingmachina.machine.MachineVariant;
import com.bartz24.varyingmachina.machine.ModMachines;
import com.bartz24.varyingmachina.tile.TileEntityMachine;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import javax.xml.soap.Text;
import java.util.ArrayList;
import java.util.List;

public class MachineFreezer extends MachineType<TileEntityMachine> {
    public MachineFreezer() {
        super("freezer", ModMachines.Stats.rating, ModMachines.Stats.speed, ModMachines.Stats.fuelefficiency);
        this.setTextureFront(new ResourceLocation("varyingmachina", "block/freezer"));
        this.addInputItemHandler("input");
        this.addOutputItemHandler("output");
        this.setExtraInput(MachineExtraComponents.smelter);
        this.setEnergyRateMultiplier(1.6);
    }


    @Override
    public List<String> getSpecialTooltipTypes() {
        List<String> list = new ArrayList<>();
        list.add("maxCU");
        return list;
    }

    @Override
    public double calculateSpecialStat(String statName, MachineVariant mainVariant, MachineVariant casingVariant) {
        if (statName.equals("maxCU"))
            return (int) (casingVariant.getStat(ModMachines.Stats.pressure) * casingVariant.getStat(ModMachines.Stats.pressure) / ModMachines.Stats.hu.calculateStat(this, mainVariant, casingVariant) / ModMachines.Stats.speed.calculateStat(this, mainVariant, casingVariant));

        return super.calculateSpecialStat(statName, mainVariant, casingVariant);
    }

    @Override
    public String getTextSpecialStat(String statName, MachineVariant mainVariant, MachineVariant casingVariant) {
        if (statName.equals("maxCU"))
            return (int) calculateSpecialStat("maxCU", mainVariant, casingVariant) + "";
        return super.getTextSpecialStat(statName, mainVariant, casingVariant);
    }

    @Override
    public TextFormatting getColorSpecialStat(String statName) {
        if (statName.equals("maxCU"))
            return TextFormatting.AQUA;
        return super.getColorSpecialStat(statName);
    }
}
