package com.bartz24.varyingmachina.machines;

import com.bartz24.varyingmachina.base.inventory.GuiArrowProgress;
import com.bartz24.varyingmachina.base.inventory.GuiCasing;
import com.bartz24.varyingmachina.base.inventory.GuiHeatBar;
import com.bartz24.varyingmachina.base.item.ItemMachine;
import com.bartz24.varyingmachina.base.machine.MachineStat;
import com.bartz24.varyingmachina.base.recipe.ProcessRecipe;
import com.bartz24.varyingmachina.base.recipe.RecipeItem;
import com.bartz24.varyingmachina.base.tile.TileCasing;
import com.bartz24.varyingmachina.machines.recipes.SmelterRecipes;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.SlotItemHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MachineSmelter extends ItemMachine {

    public MachineSmelter() {
        super("smelter", MachineStat.SPEED, MachineStat.EFFICIENCY);
    }

    public ProcessRecipe getRecipe(World world, BlockPos pos, ItemStack machineStack) {
        float curHU = getCasingTile(world, pos).machineData.getFloat("curHU");
        ItemStack input = getInputInventory(getCasingTile(world, pos)).getStackInSlot(0);
        return SmelterRecipes.smelterRecipes.getRecipe(new RecipeItem(input), false, false, curHU,
                Integer.MAX_VALUE, getCombinedStat(MachineStat.PRESSURE, machineStack, world, pos));
    }

    public void processFinish(World world, BlockPos pos, ItemStack machineStack, ProcessRecipe recipe) {
        ItemStack input = getInputInventory(getCasingTile(world, pos)).getStackInSlot(0);
        getOutputInventory(getCasingTile(world, pos)).insertItem(0, recipe.getItemOutputs().get(0), false);
        input.shrink(recipe.getItemInputs().get(0).get(0).getCount());
    }

    public float getBaseTimeToProcess(World world, BlockPos pos, ItemStack machineStack, ProcessRecipe recipe) {
        return recipe == null ? super.getBaseTimeToProcess(world, pos, machineStack, recipe)
                : recipe.getNumParameters()[1];
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

    public MachineStat[] getCombinedStats() {
        List<MachineStat> combinedStats = new ArrayList(Arrays.asList(stats));
        combinedStats.add(MachineStat.MAXHU);
        combinedStats.add(MachineStat.PRESSURE);
        return combinedStats.toArray(new MachineStat[combinedStats.size()]);
    }

    public List<Slot> getSlots(TileCasing tile, List<Slot> slots) {
        slots.add(new SlotItemHandler(getInputInventory(tile), 0, 50, 40));
        slots.add(new SlotItemHandler(getOutputInventory(tile), 0, 110, 40));
        return super.getSlots(tile, slots);
    }

    @SideOnly(Side.CLIENT)
    public void initGui(GuiCasing gui, List buttonList, TileCasing casing) {
        super.initGui(gui, buttonList, casing);
        float curHU = casing.machineData.getFloat("curHU");
        float itemHU = casing.machineData.getFloat("itemHU");
        boolean running = casing.machineData.getBoolean("running");
        ItemStack smelt = getInputInventory(casing).getStackInSlot(0);
        ProcessRecipe recipe = SmelterRecipes.smelterRecipes.getRecipe(new RecipeItem(smelt), false, false, curHU,
                Integer.MAX_VALUE,
                getCombinedStat(MachineStat.PRESSURE, casing.machineStored, casing.getWorld(), casing.getPos()));
        int time = casing.machineData.getInteger("time");
        int huTick = (int) (casing.machineData.getFloat("huTick")
                - (running ? getHUDrain(casing.getWorld(), casing.getPos(), casing.machineStored) : 0));
        gui.guiComponents.add(new GuiHeatBar(136, 25,
                (int) getCombinedStat(MachineStat.MAXHU, casing.machineStored, casing.getWorld(), casing.getPos()),
                curHU, huTick));
        gui.guiComponents.add(new GuiArrowProgress(75, 40,
                getTimeToProcess(casing.getWorld(), casing.getPos(), casing.machineStored, recipe), time));
    }

    @SideOnly(Side.CLIENT)
    public void updateGuiComps(GuiCasing gui, List buttonList, TileCasing casing) {
        super.updateGuiComps(gui, buttonList, casing);
        float curHU = casing.machineData.getFloat("curHU");
        float itemHU = casing.machineData.getFloat("itemHU");
        boolean running = casing.machineData.getBoolean("running");
        ItemStack smelt = getInputInventory(casing).getStackInSlot(0);
        ProcessRecipe recipe = SmelterRecipes.smelterRecipes.getRecipe(new RecipeItem(smelt), false, false, curHU,
                Integer.MAX_VALUE,
                getCombinedStat(MachineStat.PRESSURE, casing.machineStored, casing.getWorld(), casing.getPos()));
        int time = casing.machineData.getInteger("time");
        int huTick = (int) (casing.machineData.getFloat("huTick")
                - (running ? getHUDrain(casing.getWorld(), casing.getPos(), casing.machineStored) : 0));
        gui.guiComponents.get(2).updateData(
                (int) getCombinedStat(MachineStat.MAXHU, casing.machineStored, casing.getWorld(), casing.getPos()),
                curHU, huTick);
        gui.guiComponents.get(3)
                .updateData(getTimeToProcess(casing.getWorld(), casing.getPos(), casing.machineStored, recipe), time);
    }
}
