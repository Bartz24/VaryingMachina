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
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class MachineStorageBuffer extends MachineType<TileEntityMachine> {
    public MachineStorageBuffer() {
        super("storagebuffer", ModMachines.Stats.rating);
        this.setNoRecipes();
        this.setNoFuel();
        this.setTextureFront(new ResourceLocation("varyingmachina", "block/storagebufferfront"));
        this.setTextureSide(new ResourceLocation("varyingmachina", "block/storagebuffer"));
        this.setTextureTop(new ResourceLocation("varyingmachina", "block/storagebuffer"));
        this.setTextureBottom(new ResourceLocation("varyingmachina", "block/storagebuffer"));
        this.addDoubleItemHandler("inv", 1);
        this.setExtraInput(MachineExtraComponents.storagebuffer);
    }

    @Override
    public OmniItemHandler createItemHandler(TileEntityMachine tile) {
        OmniItemHandler handler = super.createItemHandler(tile);
        handler.getHandlerRestricted("inv").setSize((int) calculateSpecialStat("size", tile.getMainMachineVariant(), tile.getCasingMachineVariant()));
        handler.getHandlerRestricted("inv").setSlotMultiplier(() -> (int) calculateSpecialStat("stackSize", tile.getMainMachineVariant(), tile.getCasingMachineVariant()));
        return handler;
    }

    @Override
    public List<String> getSpecialTooltipTypes() {
        List<String> list = new ArrayList<>();
        list.add("size");
        list.add("stackSize");
        return list;
    }


    @Override
    public double calculateSpecialStat(String statName, MachineVariant mainVariant, MachineVariant casingVariant) {
        if (statName.equals("size"))
            return (int) (ModMachines.Stats.size.calculateStat(this, mainVariant, casingVariant) * mainVariant.getStat(ModMachines.Stats.pressure) / 50d * Math.pow(casingVariant.getStat(ModMachines.Stats.efficiency) / 100d, 0.7) + 1);
        else if (statName.equals("stackSize"))
            return (int) (ModMachines.Stats.size.calculateStat(this, mainVariant, casingVariant));
        return super.calculateSpecialStat(statName, mainVariant, casingVariant);
    }

    @Override
    public String getTextSpecialStat(String statName, MachineVariant mainVariant, MachineVariant casingVariant) {
        if (statName.equals("size"))
            return (int) calculateSpecialStat("size", mainVariant, casingVariant) + "";
        else if (statName.equals("stackSize"))
            return (int) calculateSpecialStat("stackSize", mainVariant, casingVariant) + "";
        return super.getTextSpecialStat(statName, mainVariant, casingVariant);
    }

    @Override
    public TextFormatting getColorSpecialStat(String statName) {
        if (statName.equals("size"))
            return TextFormatting.DARK_BLUE;
        else if (statName.equals("stackSize"))
            return TextFormatting.AQUA;
        return super.getColorSpecialStat(statName);
    }
}
