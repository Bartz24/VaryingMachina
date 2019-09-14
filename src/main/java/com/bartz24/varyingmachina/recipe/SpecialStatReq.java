package com.bartz24.varyingmachina.recipe;

import com.bartz24.varyingmachina.machine.ModMachines;
import com.bartz24.varyingmachina.tile.IRecipeProcessor;
import com.bartz24.varyingmachina.tile.TileEntityMachine;
import javafx.util.Pair;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class SpecialStatReq extends RecipeReqBase {

    public enum StatReqComp {
        LESS,
        LESSEQUAL,
        GREATER,
        GREATEREQUAL,
        EQUAL
    }

    private String stat, machineType;
    private double value;
    private StatReqComp comp;

    public SpecialStatReq(String stat, String machineType, double value, StatReqComp comp) {
        this.stat = stat;
        this.machineType = machineType;
        this.value = value;
        this.comp = comp;
    }

    @Override
    public boolean requirementsMet(IRecipeProcessor tile) {
        if (tile instanceof TileEntityMachine) {
            TileEntityMachine machine = (TileEntityMachine) tile;
            double statVal = machine.getMachine().calculateSpecialStat(stat, machine.getMainMachineVariant(), machine.getCasingMachineVariant());
            switch (comp) {
                case LESS:
                    return statVal < value;
                case LESSEQUAL:
                    return statVal <= value;
                case GREATER:
                    return statVal > value;
                case GREATEREQUAL:
                    return statVal >= value;
                case EQUAL:
                    return statVal == value;
            }
        }
        return false;
    }

    @Override
    public boolean requirementsMet(RecipeReqBase req2) {
        if (!(req2 instanceof SpecialStatReq))
            return false;
        switch (comp) {
            case LESS:
                return ((SpecialStatReq) req2).value < value;
            case LESSEQUAL:
                return ((SpecialStatReq) req2).value <= value;
            case GREATER:
                return ((SpecialStatReq) req2).value > value;
            case GREATEREQUAL:
                return ((SpecialStatReq) req2).value >= value;
            case EQUAL:
                return ((SpecialStatReq) req2).value == value;
        }
        return false;
    }

    @Override
    public Pair<String, Integer> getDrawText() {
        String disp = new TranslationTextComponent("varyingmachina.stat." + stat.toLowerCase(), value).getFormattedText();
        disp = ModMachines.types.get(machineType).getColorSpecialStat(stat) + disp;
        return new Pair<>(disp, ModMachines.types.get(machineType).getColorSpecialStat(stat).getColor());
    }
}
