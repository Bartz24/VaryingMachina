package com.bartz24.varyingmachina.recipe;

import com.bartz24.varyingmachina.machine.MachineStat;
import com.bartz24.varyingmachina.tile.IRecipeProcessor;
import com.bartz24.varyingmachina.tile.TileEntityMachine;
import javafx.util.Pair;
import net.minecraft.util.text.TranslationTextComponent;

public class StatReq extends RecipeReqBase {

    public enum StatReqComp {
        LESS,
        LESSEQUAL,
        GREATER,
        GREATEREQUAL,
        EQUAL
    }

    private MachineStat stat;
    private double value;
    private StatReqComp comp;

    public StatReq(MachineStat stat, double value, StatReqComp comp) {
        this.stat = stat;
        this.value = value;
        this.comp = comp;
    }

    @Override
    public boolean requirementsMet(IRecipeProcessor tile) {
        if (tile instanceof TileEntityMachine) {
            TileEntityMachine machine = (TileEntityMachine) tile;
            double statVal = stat.calculateStat(machine.getMachine(), machine.getMainMachineVariant(), machine.getCasingMachineVariant());
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
        if (!(req2 instanceof StatReq))
            return false;
        switch (comp) {
            case LESS:
                return ((StatReq) req2).value < value;
            case LESSEQUAL:
                return ((StatReq) req2).value <= value;
            case GREATER:
                return ((StatReq) req2).value > value;
            case GREATEREQUAL:
                return ((StatReq) req2).value >= value;
            case EQUAL:
                return ((StatReq) req2).value == value;
        }
        return false;
    }

    @Override
    public Pair<String, Integer> getDrawText() {
        String disp = new TranslationTextComponent("varyingmachina.stat." + stat.getName(), value).getFormattedText();
        disp = stat.getTextColor() + disp;
        return new Pair<>(disp, stat.getTextColor().getColor());
    }
}
