package com.bartz24.varyingmachina.machine.types;

import com.bartz24.varyingmachina.inventory.OmniItemHandler;
import com.bartz24.varyingmachina.machine.MachineExtraComponents;
import com.bartz24.varyingmachina.machine.MachineType;
import com.bartz24.varyingmachina.machine.MachineVariant;
import com.bartz24.varyingmachina.machine.ModMachines;
import com.bartz24.varyingmachina.tile.TileEntityMachine;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collections;
import java.util.List;

public class MachineReserve extends MachineType<TileEntityMachine> {
    public MachineReserve() {
        super("reserve", ModMachines.Stats.rating);
        this.setNoRecipes();
        this.setNoFuel();
        this.setTextureFront(new ResourceLocation("varyingmachina", "block/reserve"));
        this.addDoubleItemHandler("inv", 1);
        this.setExtraInput(MachineExtraComponents.reserve);
    }

    @Override
    public OmniItemHandler createItemHandler(TileEntityMachine tile) {
        OmniItemHandler handler = super.createItemHandler(tile);
        handler.getHandlerRestricted("inv").setSlotMultiplier(() -> (int) calculateSpecialStat("size", tile.getMainMachineVariant(), tile.getCasingMachineVariant()));
        return handler;
    }

    @Override
    public double calculateSpecialStat(String statName, MachineVariant mainVariant, MachineVariant casingVariant) {
        if (statName.equals("size"))
            return (int) Math.pow(2, (int) Math.min(16, ModMachines.Stats.size.calculateStat(this, mainVariant, casingVariant))) * 16;
        return super.calculateSpecialStat(statName, mainVariant, casingVariant);
    }

    @Override
    public TextFormatting getColorSpecialStat(String statName) {
        if (statName.equals("size"))
            return TextFormatting.DARK_BLUE;
        return super.getColorSpecialStat(statName);
    }

    @Override
    public List<String> getSpecialTooltipTypes() {
        return Collections.singletonList("size");
    }

    @Override
    public String getTextSpecialStat(String statName, MachineVariant mainVariant, MachineVariant casingVariant) {
        if (statName.equals("size"))
            return (int) (calculateSpecialStat("size", mainVariant, casingVariant) * 64) + "";
        return super.getTextSpecialStat(statName, mainVariant, casingVariant);
    }
}
