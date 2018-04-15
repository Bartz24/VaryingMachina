package com.bartz24.varyingmachina.machines;

import com.bartz24.varyingmachina.ItemHelper;
import com.bartz24.varyingmachina.base.inventory.GuiArrowProgress;
import com.bartz24.varyingmachina.base.inventory.GuiCasing;
import com.bartz24.varyingmachina.base.inventory.GuiHeatBar;
import com.bartz24.varyingmachina.base.inventory.GuiStatsComp;
import com.bartz24.varyingmachina.base.item.ItemMachine;
import com.bartz24.varyingmachina.base.machine.MachineStat;
import com.bartz24.varyingmachina.base.recipe.ProcessRecipe;
import com.bartz24.varyingmachina.base.recipe.RecipeItem;
import com.bartz24.varyingmachina.base.recipe.RecipeObject;
import com.bartz24.varyingmachina.base.tile.TileCasing;
import com.bartz24.varyingmachina.machines.recipes.AssemblerRecipes;
import com.bartz24.varyingmachina.machines.recipes.SmelterRecipes;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.SlotItemHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MachineAssembler extends ItemMachine {

    public MachineAssembler() {
        super("assembler", MachineStat.SPEED, MachineStat.EFFICIENCY);
    }

    public void update(World world, BlockPos pos, ItemStack machineStack, NBTTagCompound data) {
        assemble(world, pos, machineStack, data);
    }

    void assemble(World world, BlockPos pos, ItemStack machineStack, NBTTagCompound data) {
        if (!world.isRemote) {
            boolean changedValue = false;
            heatUp(machineStack, world, pos, data);
            int time = data.getInteger("time");
            float curHU = data.getFloat("curHU");

            ProcessRecipe recipe = AssemblerRecipes.assemblerRecipes.getRecipe(getInputs(world, pos), false, false, Integer.MAX_VALUE);
            if (recipe != null && getCasingTile(world, pos).getOutputInventory()
                    .insertItem(0, recipe.getItemOutputs().get(0), true).isEmpty()) {
                data.setBoolean("running", true);
                time++;
                curHU -= getHUDrain(world, pos, machineStack);
                if (time >= getTimeToProcess(world, pos, getCasingTile(world, pos).machineStored, recipe)) {
                    getCasingTile(world, pos).getOutputInventory().insertItem(0, recipe.getItemOutputs().get(0), false);
                    shrinkInputs(recipe.getItemInputs(), world, pos);
                    time = 0;
                }
                changedValue = true;
            } else if (time != 0) {
                time = 0;
                data.setBoolean("running", false);
                changedValue = true;
            }
            if (changedValue) {
                getCasingTile(world, pos).markDirty();
                data.setInteger("time", time);
                data.setFloat("curHU", curHU);
            }
        }
    }

    private void shrinkInputs(List<List<ItemStack>> inputs, World world, BlockPos pos) {
        for (int i = 0; i < 9; i++) {
            for (List<ItemStack> input : inputs) {
                boolean success = false;
                for (ItemStack s : input) {
                    if (!s.isEmpty() && ItemHelper.itemStacksEqualOD(s, getCasingTile(world, pos).getInputInventory().getStackInSlot(i))) {
                        getCasingTile(world, pos).getInputInventory().getStackInSlot(i).shrink(s.getCount());
                        success = true;
                        break;
                    }
                }
                if (success)
                    break;
            }
        }
    }

    private List<RecipeObject> getInputs(World world, BlockPos pos) {
        List<RecipeObject> inputs = new ArrayList();
        for (int i = 0; i < 9; i++) {
            if (!getCasingTile(world, pos).getInputInventory().getStackInSlot(i).isEmpty())
                inputs.add(new RecipeItem(getCasingTile(world, pos).getInputInventory().getStackInSlot(i)));
        }
        return inputs;
    }

    public float getBaseTimeToProcess(World world, BlockPos pos, ItemStack machineStack, ProcessRecipe recipe) {
        return recipe == null ? super.getBaseTimeToProcess(world, pos, machineStack, recipe)
                : recipe.getNumParameters()[0];
    }

    public boolean requiresFuel(ItemStack stack) {
        return true;
    }

    public int getInputItemSlots(ItemStack stack) {
        return 9 + super.getInputItemSlots(stack);
    }

    public int getOutputItemSlots(ItemStack stack) {
        return 1;
    }

    public List<String> getInputItemNames(ItemStack stack) {
        List<String> names = new ArrayList();
        for (int i = 1; i <= 9; i++)
            names.add("Input " + i);
        names.addAll(super.getInputItemNames(stack));
        return names;
    }

    public List<String> getOutputItemNames(ItemStack stack) {
        List<String> names = new ArrayList();
        names.add("Output");
        return names;
    }

    public List<Slot> getSlots(TileCasing tile, List<Slot> slots) {
        for (int y = 0; y < 3; y++)
            for (int x = 0; x < 3; x++)
                slots.add(new SlotItemHandler(tile.getInputInventory(), y * 3 + x, 34 + 18 * x, 20 + 18 * y));
        slots.add(new SlotItemHandler(tile.getOutputInventory(), 0, 130, 38));
        return super.getSlots(tile, slots);
    }

    @SideOnly(Side.CLIENT)
    public void initGui(GuiCasing gui, List buttonList, TileCasing casing) {
        super.initGui(gui, buttonList, casing);
        int time = casing.machineData.getInteger("time");
        ProcessRecipe recipe = AssemblerRecipes.assemblerRecipes.getRecipe(getInputs(casing.getWorld(), casing.getPos()), false, false, Integer.MAX_VALUE);
        gui.guiComponents.add(new GuiStatsComp(155, 25, getCombinedStats(), casing));
        gui.guiComponents.add(new GuiArrowProgress(95, 38,
                getTimeToProcess(casing.getWorld(), casing.getPos(), casing.machineStored, recipe), time));
    }

    @SideOnly(Side.CLIENT)
    public void updateGuiComps(GuiCasing gui, List buttonList, TileCasing casing) {
        super.updateGuiComps(gui, buttonList, casing);
        int time = casing.machineData.getInteger("time");
        ProcessRecipe recipe = AssemblerRecipes.assemblerRecipes.getRecipe(getInputs(casing.getWorld(), casing.getPos()), false, false, Integer.MAX_VALUE);
        gui.guiComponents.get(1).updateData(getCombinedStats(), casing);
        gui.guiComponents.get(2)
                .updateData(getTimeToProcess(casing.getWorld(), casing.getPos(), casing.machineStored, recipe), time);
    }
}
