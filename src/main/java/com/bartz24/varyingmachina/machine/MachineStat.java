package com.bartz24.varyingmachina.machine;

import com.bartz24.varyingmachina.Helpers;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class MachineStat {
    private String name;
    private TextFormatting textColor;


    public MachineStat(String name, TextFormatting textColor) {
        this.name = name;
        this.textColor = textColor;
    }

    public double calculateStat(MachineType type, MachineVariant main, MachineVariant casing) {
        return main.getStat(this) * casing.getStat(this) / 10000d;
    }

    public String getText(MachineType type, MachineVariant main, MachineVariant casing) {
        return Helpers.round(calculateStat(type, main, casing) * 100, 2) + "%";
    }

    public String getReqText(double value) {
        return Helpers.round(value * 100, 2) + "%";
    }

    public String getName() {
        return name;
    }

    public TextFormatting getTextColor() {
        return textColor;
    }
}
