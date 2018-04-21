package com.bartz24.varyingmachina.machines;

import com.bartz24.varyingmachina.base.inventory.GuiArrowProgress;
import com.bartz24.varyingmachina.base.inventory.GuiCasing;
import com.bartz24.varyingmachina.base.inventory.GuiPresserPatternButton;
import com.bartz24.varyingmachina.base.inventory.GuiStatsComp;
import com.bartz24.varyingmachina.base.item.ItemMachine;
import com.bartz24.varyingmachina.base.machine.MachineStat;
import com.bartz24.varyingmachina.base.recipe.ProcessRecipe;
import com.bartz24.varyingmachina.base.recipe.RecipeItem;
import com.bartz24.varyingmachina.base.tile.TileCasing;
import com.bartz24.varyingmachina.machines.recipes.PresserProcessRecipe;
import com.bartz24.varyingmachina.machines.recipes.PresserProcessRecipe.PresserPattern;
import com.bartz24.varyingmachina.machines.recipes.PresserRecipes;
import com.bartz24.varyingmachina.network.PresserPatternMessage;
import com.bartz24.varyingmachina.network.VaryingMachinaPacketHandler;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.SlotItemHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MachinePresser extends ItemMachine {

    public MachinePresser() {
        super("presser", MachineStat.SPEED, MachineStat.EFFICIENCY);
    }

    public ProcessRecipe getRecipe(World world, BlockPos pos, ItemStack machineStack) {
        ItemStack input = getInputInventory(getCasingTile(world, pos)).getStackInSlot(0);
        return new PresserProcessRecipe(Collections.singletonList(new RecipeItem(input)),
                "presser", getPattern(getCasingTile(world, pos).machineData), getCombinedStat(MachineStat.PRESSURE, machineStack, world, pos),
                Integer.MAX_VALUE);
    }

    public void processFinish(World world, BlockPos pos, ItemStack machineStack, ProcessRecipe recipe) {
        ItemStack input = getInputInventory(getCasingTile(world, pos)).getStackInSlot(0);
        getOutputInventory(getCasingTile(world, pos)).insertItem(0, recipe.getItemOutputs().get(0), false);
        input.shrink(recipe.getItemInputs().get(0).get(0).getCount());
    }

    public PresserPattern getPattern(NBTTagCompound data) {

        NBTTagCompound pattern = data.getCompoundTag("pattern");
        int height = pattern.getInteger("height");
        int width = pattern.getInteger("width");
        int[][] pat = new int[height][width];
        NBTTagList list = pattern.getTagList("array", 11);
        for (int i = 0; i < height; i++) {
            pat[i] = list.getIntArrayAt(i);
        }
        if (pat.length != 5 || pat[0].length != 5)
            pat = new int[5][5];
        return new PresserPattern(pat);
    }

    public void writePattern(NBTTagCompound data, PresserPattern pattern) {

        int height = pattern.pattern.length;
        int width = pattern.pattern[0].length;

        NBTTagList list = new NBTTagList();
        for (int i = 0; i < height; i++) {
            list.appendTag(new NBTTagIntArray(pattern.pattern[i]));
        }

        NBTTagCompound patternData = new NBTTagCompound();
        patternData.setTag("array", list);
        patternData.setInteger("height", height);
        patternData.setInteger("width", width);
        data.setTag("pattern", patternData);
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
        combinedStats.add(MachineStat.PRESSURE);
        return combinedStats.toArray(new MachineStat[combinedStats.size()]);
    }

    public List<Slot> getSlots(TileCasing tile, List<Slot> slots) {
        slots.add(new SlotItemHandler(getInputInventory(tile), 0, 35, 40));
        slots.add(new SlotItemHandler(getOutputInventory(tile), 0, 95, 40));
        return super.getSlots(tile, slots);
    }

    @SideOnly(Side.CLIENT)
    public void initGui(GuiCasing gui, List buttonList, TileCasing casing) {
        super.initGui(gui, buttonList, casing);
        ItemStack press = getInputInventory(casing).getStackInSlot(0);
        PresserProcessRecipe input = new PresserProcessRecipe(Collections.singletonList(new RecipeItem(press)),
                "presser", getPattern(casing.machineData),
                getCombinedStat(MachineStat.PRESSURE, casing.machineStored, casing.getWorld(), casing.getPos()),
                Integer.MAX_VALUE);
        ProcessRecipe recipe = PresserRecipes.presserRecipes.getRecipe(input);
        int time = casing.machineData.getInteger("time");
        gui.addComponent("arrow", new GuiArrowProgress(60, 40,
                getTimeToProcess(casing.getWorld(), casing.getPos(), casing.machineStored, recipe), time));

        PresserPattern pattern = getPattern(casing.machineData);

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                buttonList.add(new GuiPresserPatternButton(y * 5 + x, gui.getGuiLeft() + 120 + 5 * x,
                        gui.getGuiTop() + 36 + 5 * y, pattern.pattern[y][x] == 1));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void actionPerformed(TileCasing tile, GuiCasing gui, int buttonClicked) throws IOException {

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                if (buttonClicked == y * 5 + x) {
                    VaryingMachinaPacketHandler.instance.sendToServer(new PresserPatternMessage(x, y, tile.getPos()));
                }
            }
        }

    }

    @SideOnly(Side.CLIENT)
    public void updateGuiComps(GuiCasing gui, List buttonList, TileCasing casing) {
        super.updateGuiComps(gui, buttonList, casing);
        ItemStack press = getInputInventory(casing).getStackInSlot(0);
        PresserProcessRecipe input = new PresserProcessRecipe(Collections.singletonList(new RecipeItem(press)),
                "presser", getPattern(casing.machineData),
                getCombinedStat(MachineStat.PRESSURE, casing.machineStored, casing.getWorld(), casing.getPos()),
                Integer.MAX_VALUE);
        ProcessRecipe recipe = PresserRecipes.presserRecipes.getRecipe(input);
        int time = casing.machineData.getInteger("time");
        gui.updateComponent("arrow", getTimeToProcess(casing.getWorld(), casing.getPos(), casing.machineStored, recipe), time);
    }
}
