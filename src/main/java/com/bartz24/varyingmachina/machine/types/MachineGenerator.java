package com.bartz24.varyingmachina.machine.types;

import com.bartz24.varyingmachina.inventory.EnergyFuelUnit;
import com.bartz24.varyingmachina.machine.*;
import com.bartz24.varyingmachina.tile.TileEntityMachine;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class MachineGenerator extends MachineType<TileEntityMachine> {
    public MachineGenerator() {
        super("generator", ModMachines.Stats.rating, ModMachines.Stats.speed, ModMachines.Stats.fuelefficiency);
        this.setTextureFront(new ResourceLocation("varyingmachina", "block/generator"));
        this.addOutputEnergyHandler("output");
        this.setExtraInput(MachineExtraComponents.assembler);
        this.setBlacklist(item -> ModVariants.types.get(item.getBlockMachine().getMachineVariant()).getFuelUnitSupplier().apply(ModVariants.types.get(item.getBlockMachine().getMachineVariant()).getFuelUnitSize()) instanceof EnergyFuelUnit);
    }



    @Override
    public List<String> getSpecialTooltipTypes() {
        List<String> list = new ArrayList<>();
        list.add("maxRFProcess");
        return list;
    }

    @Override
    public double calculateSpecialStat(String statName, MachineVariant mainVariant, MachineVariant casingVariant) {
        if (statName.equals("maxRFProcess"))
            return (int) (ModMachines.Stats.hu.calculateStat(this, mainVariant, casingVariant) / 10d * casingVariant.getStat(ModMachines.Stats.pressure) * Math.pow(ModMachines.Stats.production.calculateStat(this, mainVariant, casingVariant), 0.8)) + 1;

        return super.calculateSpecialStat(statName, mainVariant, casingVariant);
    }

    @Override
    public String getTextSpecialStat(String statName, MachineVariant mainVariant, MachineVariant casingVariant) {
        if (statName.equals("maxRFProcess"))
            return (int) calculateSpecialStat("maxRFProcess", mainVariant, casingVariant) + "";
        return super.getTextSpecialStat(statName, mainVariant, casingVariant);
    }

    @Override
    public TextFormatting getColorSpecialStat(String statName) {
        if (statName.equals("maxRFProcess"))
            return TextFormatting.GOLD;
        return super.getColorSpecialStat(statName);
    }
}
