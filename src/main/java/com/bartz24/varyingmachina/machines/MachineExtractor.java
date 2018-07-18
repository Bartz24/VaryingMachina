package com.bartz24.varyingmachina.machines;

import com.bartz24.varyingmachina.RandomHelper;
import com.bartz24.varyingmachina.base.inventory.GuiArrowProgress;
import com.bartz24.varyingmachina.base.inventory.GuiCasing;
import com.bartz24.varyingmachina.base.inventory.GuiFluidTank;
import com.bartz24.varyingmachina.base.inventory.SlotMachina;
import com.bartz24.varyingmachina.base.item.ItemMachine;
import com.bartz24.varyingmachina.base.machine.MachineStat;
import com.bartz24.varyingmachina.base.recipe.ProcessRecipe;
import com.bartz24.varyingmachina.base.recipe.RecipeFluid;
import com.bartz24.varyingmachina.base.recipe.RecipeItem;
import com.bartz24.varyingmachina.base.recipe.RecipeObject;
import com.bartz24.varyingmachina.base.tile.FluidTankFiltered;
import com.bartz24.varyingmachina.base.tile.TileCasing;
import com.bartz24.varyingmachina.machines.recipes.ExtractorRecipes;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MachineExtractor extends ItemMachine {

    public MachineExtractor() {
        super("extractor", MachineStat.SPEED, MachineStat.EFFICIENCY, MachineStat.PRODUCTION);
    }

    public void processFinish(World world, BlockPos pos, ItemStack machineStack, ProcessRecipe recipe) {
        if (recipe.getItemOutputs().size() > 0) {
            float chance = getCombinedStat(MachineStat.PRODUCTION, machineStack, world, pos);
            chance = Math.max(Math.min(recipe.getNumParameters()[2], chance), recipe.getNumParameters()[1]);
            while (chance >= 1) {
                getOutputInventory(getCasingTile(world, pos)).insertItem(0, recipe.getItemOutputs().get(0), false);
                chance--;
            }
            if (world.rand.nextFloat() <= chance)
                getOutputInventory(getCasingTile(world, pos)).insertItem(0, recipe.getItemOutputs().get(0), false);
        }
        for (FluidStack fluidStack : recipe.getFluidOutputs()) {
            float chance = getCombinedStat(MachineStat.PRODUCTION, machineStack, world, pos);
            chance = Math.max(Math.min(recipe.getNumParameters()[2], chance), recipe.getNumParameters()[1]);
            FluidStack output = fluidStack.copy();
            output.amount *= chance;
            getCasingTile(world, pos).outputFluids.fill(output, true);
        }
        ItemStack input = getInputInventory(getCasingTile(world, pos)).getStackInSlot(0);
        if (recipe.getItemInputs().size() > 0)
            input.shrink(recipe.getItemInputs().get(0).get(0).getCount());
        if (recipe.getFluidInputs().size() > 0)
            getCasingTile(world, pos).inputFluids.getTankInSlot(getInputFluids(machineStack).length - 1).drain(recipe.getFluidInputs().get(0), true);
    }


    public ProcessRecipe getRecipe(World world, BlockPos pos, ItemStack machineStack) {
        return ExtractorRecipes.extractorRecipes.getRecipe(getInputs(world, pos), false, false, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    private List<RecipeObject> getInputs(World world, BlockPos pos) {
        List<RecipeObject> inputs = new ArrayList();
        TileCasing casing = getCasingTile(world, pos);
        if (casing.inputFluids.getTankInSlot(getInputFluids(casing.machineStored).length - 1).getFluidAmount() > 0)
            inputs.add(new RecipeFluid(casing.inputFluids.getTankInSlot(getInputFluids(casing.machineStored).length - 1).getFluid()));
        if (!getInputInventory(getCasingTile(world, pos)).getStackInSlot(0).isEmpty())
            inputs.add(new RecipeItem(getInputInventory(getCasingTile(world, pos)).getStackInSlot(0)));
        return inputs;
    }

    public float getBaseTimeToProcess(World world, BlockPos pos, ItemStack machineStack, ProcessRecipe recipe) {
        return recipe == null ? super.getBaseTimeToProcess(world, pos, machineStack, recipe)
                : recipe.getNumParameters()[0];
    }

    public boolean requiresFuel(ItemStack stack) {
        return true;
    }

    public int getInputItemSlots(TileCasing casing) {
        return 1 + super.getInputItemSlots(casing);
    }

    public int getOutputItemSlots(TileCasing casing) {
        return 1;
    }

    public List<String> getInputItemNames(TileCasing casing) {
        List<String> names = new ArrayList();
        names.add("Input");
        names.addAll(super.getInputItemNames(casing));
        return names;
    }

    public List<String> getOutputItemNames(ItemStack stack) {
        List<String> names = new ArrayList();
        names.add("Output");
        return names;
    }

    public String[] getInputFluids(ItemStack stack) {
        List<String> fluids = new ArrayList(Arrays.asList(super.getInputFluids(stack)));
        fluids.add("");
        return fluids.toArray(new String[fluids.size()]);
    }

    public int[] getMaxInputFluids(ItemStack stack) {
        List<Integer> amounts = RandomHelper.toIntList(super.getMaxInputFluids(stack));
        amounts.add(16000);
        return RandomHelper.toIntArray(amounts);
    }

    public String[] getOutputFluids(ItemStack stack) {
        List<String> fluids = new ArrayList(Arrays.asList(super.getOutputFluids(stack)));
        fluids.add("");
        fluids.add("");
        return fluids.toArray(new String[fluids.size()]);
    }

    public int[] getMaxOutputFluids(ItemStack stack) {
        List<Integer> amounts = RandomHelper.toIntList(super.getMaxOutputFluids(stack));
        amounts.add(16000);
        amounts.add(16000);
        return RandomHelper.toIntArray(amounts);
    }

    public List<Slot> getSlots(TileCasing tile, List<Slot> slots) {
        slots.add(new SlotMachina(getInputInventory(tile), 0, 50, 40, true));
        slots.add(new SlotMachina(getOutputInventory(tile), 0, 110, 40, false));
        return super.getSlots(tile, slots);
    }

    @SideOnly(Side.CLIENT)
    public void initGui(GuiCasing gui, List buttonList, TileCasing casing) {
        super.initGui(gui, buttonList, casing);
        int time = casing.machineData.getInteger("time");
        FluidTankFiltered tank = casing.inputFluids.getTankInSlot(getInputFluids(casing.machineStored).length - 1);
        gui.addComponent("tank", new GuiFluidTank(27, 33, tank.getCapacity(),
                tank.getFluidAmount(), tank.getFluid()));
        gui.addComponent("arrow", new GuiArrowProgress(75, 40,
                getTimeToProcess(casing.getWorld(), casing.getPos(), casing.machineStored, null), time));
        FluidTankFiltered tank1 = casing.outputFluids.getTankInSlot(0);
        FluidTankFiltered tank2 = casing.outputFluids.getTankInSlot(1);
        gui.addComponent("tank1", new GuiFluidTank(130, 33, tank1.getCapacity(),
                tank1.getFluidAmount(), tank1.getFluid()));
        gui.addComponent("tank2", new GuiFluidTank(140, 33, tank2.getCapacity(),
                tank2.getFluidAmount(), tank2.getFluid()));
    }

    @SideOnly(Side.CLIENT)
    public void updateGuiComps(GuiCasing gui, List buttonList, TileCasing casing) {
        super.updateGuiComps(gui, buttonList, casing);
        int time = casing.machineData.getInteger("time");
        FluidTankFiltered tank = casing.inputFluids.getTankInSlot(getInputFluids(casing.machineStored).length - 1);
        gui.updateComponent("tank", tank.getCapacity(),
                tank.getFluidAmount(), tank.getFluid());
        gui.updateComponent("arrow", getTimeToProcess(casing.getWorld(), casing.getPos(), casing.machineStored, null), time);
        FluidTankFiltered tank1 = casing.outputFluids.getTankInSlot(0);
        FluidTankFiltered tank2 = casing.outputFluids.getTankInSlot(1);
        gui.updateComponent("tank1", tank1.getCapacity(),
                tank1.getFluidAmount(), tank1.getFluid());
        gui.updateComponent("tank2", tank2.getCapacity(),
                tank2.getFluidAmount(), tank2.getFluid());
    }
}
