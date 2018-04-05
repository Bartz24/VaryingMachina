package com.bartz24.varyingmachina.machines;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

public class MachinePresser extends ItemMachine {

	public MachinePresser() {
		super("presser", MachineStat.SPEED, MachineStat.EFFICIENCY);
	}

	public void update(World world, BlockPos pos, ItemStack machineStack, NBTTagCompound data) {
		press(world, pos, machineStack, data);
	}

	void press(World world, BlockPos pos, ItemStack machineStack, NBTTagCompound data) {
		if (!world.isRemote) {
			boolean changedValue = false;
			heatUp(machineStack, world, pos, data);
			int time = data.getInteger("time");
			float curHU = data.getFloat("curHU");
			float huTick = data.getFloat("huTick");

			ItemStack press = getCasingTile(world, pos).getInputInventory().getStackInSlot(0);
			PresserProcessRecipe input = new PresserProcessRecipe(Collections.singletonList(new RecipeItem(press)),
					"presser", getPattern(data), getCombinedStat(MachineStat.PRESSURE, machineStack, world, pos),
					Integer.MAX_VALUE);
			ProcessRecipe recipe = PresserRecipes.presserRecipes.getRecipe(input);
			if (recipe != null && getCasingTile(world, pos).getOutputInventory()
					.insertItem(0, recipe.getItemOutputs().get(0), true).isEmpty()) {
				data.setBoolean("running", true);
				time++;
				curHU -= getHUDrain(world, pos, machineStack);
				if (time >= getTimeToProcess(world, pos, getCasingTile(world, pos).machineStored, recipe)) {
					getCasingTile(world, pos).getOutputInventory().insertItem(0, recipe.getItemOutputs().get(0), false);
					press.shrink(recipe.getItemInputs().get(0).get(0).getCount());
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
				data.setFloat("huTick", huTick);
			}
		}
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

	public int getInputItemSlots(ItemStack stack) {
		return 1 + super.getInputItemSlots(stack);
	}

	public int getOutputItemSlots(ItemStack stack) {
		return 1;
	}

	public MachineStat[] getCombinedStats() {
		List<MachineStat> combinedStats = new ArrayList(Arrays.asList(stats));
		combinedStats.add(MachineStat.PRESSURE);
		return combinedStats.toArray(new MachineStat[combinedStats.size()]);
	}

	public List<Slot> getSlots(TileCasing tile, List<Slot> slots) {
		slots.add(new SlotItemHandler(tile.getInputInventory(), 0, 35, 40));
		slots.add(new SlotItemHandler(tile.getOutputInventory(), 0, 95, 40));
		return super.getSlots(tile, slots);
	}

	@SideOnly(Side.CLIENT)
	public void initGui(GuiCasing gui, List buttonList, TileCasing casing) {
		super.initGui(gui, buttonList, casing);
		float curHU = casing.machineData.getFloat("curHU");
		float itemHU = casing.machineData.getFloat("itemHU");
		boolean running = casing.machineData.getBoolean("running");
		ItemStack press = casing.getInputInventory().getStackInSlot(0);
		PresserProcessRecipe input = new PresserProcessRecipe(Collections.singletonList(new RecipeItem(press)),
				"presser", getPattern(casing.machineData),
				getCombinedStat(MachineStat.PRESSURE, casing.machineStored, casing.getWorld(), casing.getPos()),
				Integer.MAX_VALUE);
		ProcessRecipe recipe = PresserRecipes.presserRecipes.getRecipe(input);
		int time = casing.machineData.getInteger("time");
		gui.guiComponents.add(new GuiStatsComp(155, 25, getCombinedStats(), casing));
		gui.guiComponents.add(new GuiArrowProgress(60, 40,
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
		float curHU = casing.machineData.getFloat("curHU");
		float itemHU = casing.machineData.getFloat("itemHU");
		boolean running = casing.machineData.getBoolean("running");

		ItemStack press = casing.getInputInventory().getStackInSlot(0);
		PresserProcessRecipe input = new PresserProcessRecipe(Collections.singletonList(new RecipeItem(press)),
				"presser", getPattern(casing.machineData),
				getCombinedStat(MachineStat.PRESSURE, casing.machineStored, casing.getWorld(), casing.getPos()),
				Integer.MAX_VALUE);
		ProcessRecipe recipe = PresserRecipes.presserRecipes.getRecipe(input);
		int time = casing.machineData.getInteger("time");
		gui.guiComponents.get(1).updateData(getCombinedStats(), casing);
		gui.guiComponents.get(2)
				.updateData(getTimeToProcess(casing.getWorld(), casing.getPos(), casing.machineStored, recipe), time);
	}
}
