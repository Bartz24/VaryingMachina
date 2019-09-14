package com.bartz24.varyingmachina.recipe;

import com.bartz24.varyingmachina.machine.ModMachines;
import com.bartz24.varyingmachina.tile.IRecipeProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeBase<T extends IRecipeProcessor> {
    protected List<InputBase> inputs = new ArrayList<>();
    protected List<OutputBase> outputs = new ArrayList<>();
    protected List<RecipeReqBase> requirements = new ArrayList<>();
    protected int time = 0;
    protected double fuelUsage = 1;


    public RecipeBase(OutputBase... outputs) {
        this.outputs.addAll(Arrays.asList(outputs));
    }

    public RecipeBase addInputs(InputBase... inputs) {
        for (InputBase inputBase : inputs) {
            if (inputBase == null)
                continue;
            boolean combined = false;
            for (InputBase in : this.inputs) {
                if (in.combineWith(inputBase)) {
                    combined = true;
                    break;
                }
            }
            if (!combined)
                this.inputs.add(inputBase);
        }
        return this;
    }

    public RecipeBase addReqs(RecipeReqBase... reqs) {
        this.requirements.addAll(Arrays.asList(reqs));
        return this;
    }


    public RecipeBase setTime(int time) {
        this.time = time;
        return this;
    }

    public RecipeBase setFuelUsage(double fuelUsage) {
        this.fuelUsage = fuelUsage;
        return this;
    }

    public double getFuelUsage() {
        return fuelUsage;
    }

    public int getTime() {
        return time;
    }

    public boolean isValidRecipe() {
        if (getInputObjects().size() == 0 || getOutputObjects().size() == 0)
            return false;
        for (InputBase i : getInputObjects()) {
            if (!i.isValid())
                return false;
        }
        for (OutputBase o : getOutputObjects()) {
            if (!o.isValid())
                return false;
        }

        return true;
    }

    public void drawInputs(T tile) {
        for (InputBase in : getInputObjects()) {
            in.drawItemsFromInventory(tile.getInventory(in.getId(), in.getInvType()));
        }
    }

    public void addOutputs(T tile) {
        for (OutputBase out : getOutputObjects()) {
            out.putItemsIntoInventory(tile.getInventory(out.getId(), out.getInvType()), this);
        }
    }

    public int getNumInputs(String id) {
        int count = 0;
        for (InputBase in : getInputObjects()) {
            if (in.getId().equals(id))
                count++;
        }
        return count;
    }

    public int getNumOutputs(String id) {
        int count = 0;
        for (OutputBase out : getOutputObjects()) {
            if (out.getId().equals(id))
                count++;
        }
        return count;
    }


    public boolean canProcess(T tile) {
        if (!canProcessJEI(tile))
            return false;
        for (InputBase in : getInputObjects()) {
            if (!in.hasInput(tile.getInventory(in.getId(), in.getInvType())))
                return false;
        }
        for (OutputBase out : getOutputObjects()) {
            if (!out.hasRoomForOutput(tile.getInventory(out.getId(), out.getInvType()), this))
                return false;
        }
        return true;
    }

    public boolean canProcessJEI(T tile){
        if (!isValidRecipe())
            return false;
        for (RecipeReqBase r : getRequirements()) {
            if (!r.requirementsMet(tile))
                return false;
        }
        return true;
    }

    public boolean matchesInputsReqs(RecipeBase<T> recipe2) {
        if (!isValidRecipe())
            return false;
        if (getInputObjects().size() != recipe2.getInputObjects().size())
            return false;
        for (int i = 0; i < getInputObjects().size(); i++) {
            if (!getInputObjects().get(i).hasEnough(recipe2.getInputObjects().get(i)))
                return false;
        }
        if (getRequirements().size() != recipe2.getRequirements().size())
            return false;
        for (int i = 0; i < getRequirements().size(); i++) {
            if (!getRequirements().get(i).requirementsMet(recipe2.getRequirements().get(i)))
                return false;
        }
        return true;
    }

    public <K> List<List<K>> getInputs(Class<K> type, String id) {
        List<List<K>> in = new ArrayList<>();
        for (InputBase i : getInputObjects()) {
            if (i.getInputs().size() > 0 && i.getInputs().get(0).getClass() == type && i.getId().equals(id))
                in.add(i.getInputs());
        }
        return in;
    }

    public <K> List<List<K>> getAllInputs(Class<K> type, String machineType) {
        List<List<K>> in = new ArrayList<>();
        for (Object s : ModMachines.types.get(machineType).getInputInvs()) {
            in.addAll(getInputs(type, s.toString()));
        }
        return in;
    }

    public <K> List<K> getOutputs(Class<K> type, String id) {
        List<K> out = new ArrayList<>();
        for (OutputBase o : getOutputObjects()) {
            if (o.getOutput() != null && o.getOutput().getClass() == type && o.getId().equals(id))
                out.add((K) o.getOutput());
        }
        return out;
    }

    public <K> List<K> getAllOutputs(Class<K> type, String machineType) {
        List<K> out = new ArrayList<>();
        for (Object s : ModMachines.types.get(machineType).getOutputInvs()) {
            out.addAll(getOutputs(type, s.toString()));
        }
        return out;
    }

    public List<InputBase> getInputObjects() {
        return inputs;
    }

    public List<OutputBase> getOutputObjects() {
        return outputs;
    }

    public List<OutputBase> getOutputObjects(String id) {
        List<OutputBase> out = new ArrayList<>();
        for (OutputBase o : getOutputObjects()) {
            if (o.getOutput() != null && o.getId().equals(id))
                out.add(o);
        }
        return out;
    }

    public List<RecipeReqBase> getRequirements() {
        return requirements;
    }
}
