package com.bartz24.varyingmachina.machines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.bartz24.varyingmachina.base.inventory.GuiArrowProgress;
import com.bartz24.varyingmachina.base.inventory.GuiCasing;
import com.bartz24.varyingmachina.base.inventory.GuiStatsComp;
import com.bartz24.varyingmachina.base.item.ItemMachine;
import com.bartz24.varyingmachina.base.machine.MachineStat;
import com.bartz24.varyingmachina.base.machine.MachineVariant;
import com.bartz24.varyingmachina.base.recipe.ProcessRecipe;
import com.bartz24.varyingmachina.base.recipe.RecipeItem;
import com.bartz24.varyingmachina.base.tile.TileCasing;
import com.bartz24.varyingmachina.machines.recipes.GrinderRecipes;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class MachineGrinder extends ItemMachine {

	public MachineGrinder() {
		super("grinder", MachineStat.SPEED, MachineStat.EFFICIENCY, MachineStat.PRODUCTION);
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

	public void update(World world, BlockPos pos, ItemStack machineStack, NBTTagCompound data) {
		grind(world, pos, machineStack, data);
	}

	void grind(World world, BlockPos pos, ItemStack machineStack, NBTTagCompound data) {
		if (!world.isRemote) {
			heatUp(machineStack, world, pos, data);
			int time = data.getInteger("time");
			float curHU = data.getFloat("curHU");
			float huTick = data.getFloat("huTick");
			boolean changedValue = addToOutput(world, pos, machineStack, data);

			ItemStack input = getCasingTile(world, pos).getInputInventory().getStackInSlot(0);

			if (hasRecipes(world, pos, machineStack) && canOutput(world, pos, machineStack, data)) {
				data.setBoolean("running", true);
				time++;
				curHU -= getHUDrain(world, pos, machineStack);
				if (time >= getTimeToProcess(world, pos, machineStack, null)) {
					ItemStack recipeInput = ItemStack.EMPTY;
					for (ProcessRecipe r : getValidRecipes(world, pos, machineStack)) {
						recipeInput = r.getItemInputs().get(0).get(0);
						float chance = getCombinedStat(MachineStat.PRODUCTION, machineStack, world, pos);
						chance = Math.max(Math.min(r.getNumParameters()[2], chance), r.getNumParameters()[1]);
						ItemStackHandler buffer = getBufferStacks(data);
						while (chance >= 1) {
							ItemHandlerHelper.insertItemStacked(buffer, r.getItemOutputs().get(0), false);
							chance -= 1;
						}
						if (world.rand.nextFloat() <= chance)
							ItemHandlerHelper.insertItemStacked(buffer, r.getItemOutputs().get(0), false);
						setBufferStacks(data, buffer);
					}
					input.shrink(recipeInput.getCount());
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

	public boolean addToOutput(World world, BlockPos pos, ItemStack machineStack, NBTTagCompound data) {
		ItemStackHandler buffer = getBufferStacks(data);

		boolean changed = false;
		for (int i = 0; i < buffer.getSlots(); i++) {
			if (!buffer.getStackInSlot(i).isEmpty()) {
				if (getCasingTile(world, pos).getOutputInventory().insertItem(0, buffer.getStackInSlot(i), true)
						.isEmpty()) {
					getCasingTile(world, pos).getOutputInventory().insertItem(0, buffer.getStackInSlot(i), false);
					buffer.setStackInSlot(i, ItemStack.EMPTY);
					changed = true;
				}
			}
		}
		setBufferStacks(data, buffer);
		return changed;
	}

	private boolean canOutput(World world, BlockPos pos, ItemStack machineStack, NBTTagCompound data) {
		ItemStackHandler buffer = getBufferStacks(data);
		boolean empty = true;
		for (int i = 0; i < buffer.getSlots(); i++) {
			if (!buffer.getStackInSlot(i).isEmpty())
				empty = false;
		}
		if (empty)
			return true;
		return false;
	}

	private List<ProcessRecipe> getValidRecipes(World world, BlockPos pos, ItemStack machineStack) {
		List<ProcessRecipe> recipes = new ArrayList();
		ItemStack input = getCasingTile(world, pos).getInputInventory().getStackInSlot(0);
		if (input.isEmpty())
			return recipes;
		ProcessRecipe recMachine = new ProcessRecipe(Collections.singletonList(new RecipeItem(input)), "grinder",
				getCombinedStat(MachineStat.SPEED, machineStack, world, pos), Integer.MAX_VALUE, Integer.MAX_VALUE);
		for (ProcessRecipe r : GrinderRecipes.grinderRecipes.getRecipes()) {
			if (r.isValid() && recMachine.isInputRecipeEqualTo(r, false)) {
				recipes.add(r);
			}
		}
		return recipes;
	}

	private boolean hasRecipes(World world, BlockPos pos, ItemStack machineStack) {
		return getValidRecipes(world, pos, machineStack).size() > 0;
	}

	private ItemStackHandler getBufferStacks(NBTTagCompound data) {
		ItemStackHandler stacks = new ItemStackHandler(20);
		stacks.deserializeNBT(data.getCompoundTag("buffer"));
		return stacks;
	}

	private void setBufferStacks(NBTTagCompound data, ItemStackHandler stacks) {
		data.setTag("buffer", stacks.serializeNBT());
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

	public List<String> getInputItemNames(ItemStack stack) {
		List<String> names = new ArrayList();
		names.add("Input");
		names.addAll(super.getInputItemNames(stack));
		return names;
	}

	public List<String> getOutputItemNames(ItemStack stack) {
		List<String> names = new ArrayList();
		names.add("Output");
		return names;
	}

	public List<Slot> getSlots(TileCasing tile, List<Slot> slots) {
		slots.add(new SlotItemHandler(tile.getInputInventory(), 0, 50, 40));
		slots.add(new SlotItemHandler(tile.getOutputInventory(), 0, 110, 40));
		return super.getSlots(tile, slots);
	}

	@SideOnly(Side.CLIENT)
	public void initGui(GuiCasing gui, List buttonList, TileCasing casing) {
		super.initGui(gui, buttonList, casing);
		float curHU = casing.machineData.getFloat("curHU");
		float itemHU = casing.machineData.getFloat("itemHU");
		boolean running = casing.machineData.getBoolean("running");
		int time = casing.machineData.getInteger("time");
		int huTick = (int) (casing.machineData.getFloat("huTick") - (running ? (4f
				* (float) getCombinedStat(MachineStat.SPEED, casing.machineStored, casing.getWorld(), casing.getPos()))
				: 0));
		gui.guiComponents.add(new GuiStatsComp(155, 25, getCombinedStats(), casing) {
			@Override
			public void addStatTooltip(TileCasing casing, MachineStat stat, List<String> text) {
				if (stat == MachineStat.SPEED) {
					text.add(
							TextFormatting.BLUE + "Speed: "
									+ Integer.toString((int) (MachineGrinder.this.getCombinedStat(MachineStat.SPEED,
											casing.machineStored, casing.getWorld(), casing.getPos()) * 1000f))
									+ " rad/s");
				} else
					super.addStatTooltip(casing, stat, text);
			}
		});
		gui.guiComponents.add(new GuiArrowProgress(75, 40,
				getTimeToProcess(casing.getWorld(), casing.getPos(), casing.machineStored, null), time));
	}

	@SideOnly(Side.CLIENT)
	public void updateGuiComps(GuiCasing gui, List buttonList, TileCasing casing) {
		super.updateGuiComps(gui, buttonList, casing);
		float curHU = casing.machineData.getFloat("curHU");
		float itemHU = casing.machineData.getFloat("itemHU");
		boolean running = casing.machineData.getBoolean("running");
		int time = casing.machineData.getInteger("time");
		int huTick = (int) (casing.machineData.getFloat("huTick") - (running ? (4f
				* (float) getCombinedStat(MachineStat.SPEED, casing.machineStored, casing.getWorld(), casing.getPos()))
				: 0));
		gui.guiComponents.get(1).updateData(getCombinedStats(), casing);
		gui.guiComponents.get(2)
				.updateData(getTimeToProcess(casing.getWorld(), casing.getPos(), casing.machineStored, null), time);
	}
}
