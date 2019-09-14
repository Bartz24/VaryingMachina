package com.bartz24.varyingmachina.recipe;

import com.bartz24.varyingmachina.machine.types.MachineGenerator;
import com.bartz24.varyingmachina.tile.TileEntityMachine;

public class RecipeGenerator<T extends TileEntityMachine> extends RecipeNoInputs<T> {

    private int rfGenerated;

    public RecipeGenerator(int rfGenerated) {
        super(new OutputBase[]{new OutputRF(rfGenerated)});
        this.rfGenerated = rfGenerated;
    }

    @Override
    public boolean isValidRecipe() {
        return super.isValidRecipe();
    }

    @Override
    public boolean canProcessJEI(T tile) {

        if (tile.getMachine() instanceof MachineGenerator) {
            int maxRF = (int) tile.getMachine().calculateSpecialStat("maxRFProcess", tile.getMainMachineVariant(), tile.getCasingMachineVariant());
            if (maxRF < rfGenerated)
                return false;
        }

        return super.canProcessJEI(tile);
    }
}
