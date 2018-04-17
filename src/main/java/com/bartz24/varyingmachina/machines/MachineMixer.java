package com.bartz24.varyingmachina.machines;

import com.bartz24.varyingmachina.ItemHelper;
import com.bartz24.varyingmachina.RandomHelper;
import com.bartz24.varyingmachina.base.inventory.GuiArrowProgress;
import com.bartz24.varyingmachina.base.inventory.GuiCasing;
import com.bartz24.varyingmachina.base.inventory.GuiFluidTank;
import com.bartz24.varyingmachina.base.inventory.GuiStatsComp;
import com.bartz24.varyingmachina.base.item.ItemMachine;
import com.bartz24.varyingmachina.base.machine.FuelType;
import com.bartz24.varyingmachina.base.machine.MachineStat;
import com.bartz24.varyingmachina.base.machine.MachineVariant;
import com.bartz24.varyingmachina.base.recipe.ProcessRecipe;
import com.bartz24.varyingmachina.base.recipe.RecipeFluid;
import com.bartz24.varyingmachina.base.recipe.RecipeItem;
import com.bartz24.varyingmachina.base.recipe.RecipeObject;
import com.bartz24.varyingmachina.base.tile.FluidTankFiltered;
import com.bartz24.varyingmachina.base.tile.TileCasing;
import com.bartz24.varyingmachina.machines.recipes.AssemblerRecipes;
import com.bartz24.varyingmachina.machines.recipes.MixerRecipes;
import com.bartz24.varyingmachina.machines.recipes.SmelterRecipes;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MachineMixer extends ItemMachine {

    public MachineMixer() {
        super("mixer", MachineStat.SPEED, MachineStat.EFFICIENCY, MachineStat.PRESSURE);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        for (MachineStat machineStat : stats) {
            if (machineStat == MachineStat.SPEED) {
                MachineVariant variant = MachineVariant.readFromNBT(stack.getTagCompound());
                tooltip.add(TextFormatting.BLUE + "Speed: "
                        + Integer.toString((int) (variant.getStat(MachineStat.SPEED) * 1000f)) + " rad/s");
            } else
                machineStat.addSingleInfo(stack, tooltip);
        }
        MachineVariant.readFromNBT(stack.getTagCompound()).addFuelTooltip(tooltip);
    }

    public ProcessRecipe getRecipe(World world, BlockPos pos, ItemStack machineStack) {
        return MixerRecipes.mixerRecipes.getRecipe(getInputs(world, pos), false, false, getCombinedStat(MachineStat.SPEED, machineStack, world, pos), getCombinedStat(MachineStat.PRESSURE, machineStack, world, pos));
    }

    public void processFinish(World world, BlockPos pos, ItemStack machineStack, ProcessRecipe recipe) {
        getOutputInventory(getCasingTile(world, pos)).insertItem(0, recipe.getItemOutputs().get(0), false);
        shrinkInputs(recipe.getItemInputs(), world, pos);
        if(recipe.getFluidInputs().size() > 0)
            getCasingTile(world, pos).inputFluids.getTankInSlot(getInputFluids(machineStack).length - 1).drain(recipe.getFluidInputs().get(0), true);
    }

    private void shrinkInputs(List<List<ItemStack>> inputs, World world, BlockPos pos) {
        for (int i = 0; i < 9; i++) {
            for (List<ItemStack> input : inputs) {
                boolean success = false;
                for (ItemStack s : input) {
                    if (!s.isEmpty() && ItemHelper.itemStacksEqualOD(s, getInputInventory(getCasingTile(world, pos)).getStackInSlot(i))) {
                        getInputInventory(getCasingTile(world, pos)).getStackInSlot(i).shrink(s.getCount());
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
        TileCasing casing = getCasingTile(world, pos);
        if (casing.inputFluids.getTankInSlot(getInputFluids(casing.machineStored).length - 1).getFluidAmount() > 0)
            inputs.add(new RecipeFluid(casing.inputFluids.getTankInSlot(getInputFluids(casing.machineStored).length - 1).getFluid()));
        for (int i = 0; i < 9; i++) {
            if (!getInputInventory(getCasingTile(world, pos)).getStackInSlot(i).isEmpty())
                inputs.add(new RecipeItem(getInputInventory(getCasingTile(world, pos)).getStackInSlot(i)));
        }
        return inputs;
    }

    public float getBaseTimeToProcess(World world, BlockPos pos, ItemStack machineStack, ProcessRecipe recipe) {
        return 600;
    }

    public boolean requiresFuel(ItemStack stack) {
        return true;
    }

    public int getInputItemSlots(TileCasing casing) {
        return 9 + super.getInputItemSlots(casing);
    }

    public int getOutputItemSlots(TileCasing casing) {
        return 1;
    }

    public Fluid[] getInputFluids(ItemStack stack) {
        List<Fluid> fluids = new ArrayList(Arrays.asList(super.getInputFluids(stack)));
        fluids.add(null);
        return fluids.toArray(new Fluid[fluids.size()]);
    }

    public int[] getMaxInputFluids(ItemStack stack) {
        List<Integer> amounts = RandomHelper.toIntList(super.getMaxInputFluids(stack));
        amounts.add(16000);
        return RandomHelper.toIntArray(amounts);
    }

    public List<String> getInputItemNames(TileCasing casing) {
        List<String> names = new ArrayList();
        for (int i = 1; i <= 9; i++)
            names.add("Input " + i);
        names.addAll(super.getInputItemNames(casing));
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
                slots.add(new SlotItemHandler(getInputInventory(tile), y * 3 + x, 34 + 18 * x, 20 + 18 * y));
        slots.add(new SlotItemHandler(getOutputInventory(tile), 0, 130, 38));
        return super.getSlots(tile, slots);
    }

    @SideOnly(Side.CLIENT)
    public void initGui(GuiCasing gui, List buttonList, TileCasing casing) {
        super.initGui(gui, buttonList, casing);
        int time = casing.machineData.getInteger("time");
        ProcessRecipe recipe = MixerRecipes.mixerRecipes.getRecipe(getInputs(casing.getWorld(), casing.getPos()), false, false, getCombinedStat(MachineStat.SPEED, casing.machineStored, casing.getWorld(), casing.getPos()), getCombinedStat(MachineStat.PRESSURE, casing.machineStored, casing.getWorld(), casing.getPos()));
        FluidTankFiltered tank = casing.inputFluids.getTankInSlot(getInputFluids(casing.machineStored).length - 1);
        gui.guiComponents.set(1, new GuiStatsComp(155, 25, getCombinedStats(), casing) {
            @Override
            public void addStatTooltip(TileCasing casing, MachineStat stat, List<String> text) {
                if (stat == MachineStat.SPEED) {
                    text.add(
                            TextFormatting.BLUE + "Speed: "
                                    + Integer.toString((int) (MachineMixer.this.getCombinedStat(MachineStat.SPEED,
                                    casing.machineStored, casing.getWorld(), casing.getPos()) * 1000f))
                                    + " rad/s");
                } else
                    super.addStatTooltip(casing, stat, text);
            }
        });
        gui.guiComponents.add(new GuiFluidTank(20, 33, tank.getCapacity(),
                tank.getFluidAmount(), tank.getFluid()));
        gui.guiComponents.add(new GuiArrowProgress(95, 38,
                getTimeToProcess(casing.getWorld(), casing.getPos(), casing.machineStored, recipe), time));
    }

    @SideOnly(Side.CLIENT)
    public void updateGuiComps(GuiCasing gui, List buttonList, TileCasing casing) {
        super.updateGuiComps(gui, buttonList, casing);
        int time = casing.machineData.getInteger("time");
        ProcessRecipe recipe = MixerRecipes.mixerRecipes.getRecipe(getInputs(casing.getWorld(), casing.getPos()), false, false, getCombinedStat(MachineStat.SPEED, casing.machineStored, casing.getWorld(), casing.getPos()), getCombinedStat(MachineStat.PRESSURE, casing.machineStored, casing.getWorld(), casing.getPos()));
        FluidTankFiltered tank = casing.inputFluids.getTankInSlot(getInputFluids(casing.machineStored).length - 1);
        gui.guiComponents.get(2).updateData(tank.getCapacity(),
                tank.getFluidAmount(), tank.getFluid());
        gui.guiComponents.get(3)
                .updateData(getTimeToProcess(casing.getWorld(), casing.getPos(), casing.machineStored, recipe), time);
    }
}
