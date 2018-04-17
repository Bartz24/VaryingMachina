package com.bartz24.varyingmachina.base.machine;

import com.bartz24.varyingmachina.base.item.ItemMachine.MachineFuelData;
import com.bartz24.varyingmachina.base.tile.TileCasing;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public enum FuelType {
	FURNACE, RF, FLUID, ITEM, MANA;
	public boolean isValidFuel(ItemStack machineStack, Object stack) {
		MachineVariant variant = MachineVariant.readFromNBT(machineStack.getTagCompound());
		if (variant.getFuel().type == FURNACE && stack instanceof ItemStack
				&& TileEntityFurnace.getItemBurnTime((ItemStack) stack) <= 0)
			return false;
		else if (variant.getFuel().type == ITEM && stack instanceof ItemStack
				&& !variant.getFuel().getStack().isItemEqual((ItemStack) stack))
			return false;
		else if (variant.getFuel().type == FuelType.FLUID && stack instanceof FluidStack
				&& (Fluid) variant.getFuel().getFluid() != ((FluidStack) stack).getFluid())
			return false;
		return true;
	}

	public void getFuel(TileCasing tile, World world, BlockPos pos, NBTTagCompound data) {
		MachineVariant variant = MachineVariant.readFromNBT(tile.machineStored.getTagCompound());
		MachineFuelData fuelData = tile.getMachine().getMachineFuelData(tile.machineStored, world, pos);
		ItemStack fuelStack = tile.getMachine().getInputInventory(tile).getStackInSlot(tile.getMachine().getFuelSlotID(tile));
		if (variant.getFuel().type == RF) {
			float extract = tile.energyStorage.extractInternalEnergy((int) Math.ceil(fuelData.rfPerTick), false);			
			if (extract > 0) {
				data.setFloat("itemHU", extract / fuelData.rfPerHU);
				data.setFloat("huTick", fuelData.huPerTick);
			}
		} else if (variant.getFuel().type == ITEM && isValidFuel(tile.machineStored, fuelStack)) {

			data.setFloat("itemHU", fuelData.fuelTimePercent * fuelData.huPerTick);
			data.setFloat("huTick", fuelData.huPerTick);
			fuelStack.shrink(1);
		} else if (variant.getFuel().type == FLUID && isValidFuel(tile.machineStored, tile.inputFluids.getTankInSlot(0).getFluid())
				&& tile.inputFluids.getTankInSlot(0).drainInternal(1, false) != null && tile.inputFluids.getTankInSlot(0).drainInternal(1, true).amount > 0) {
			data.setFloat("itemHU", fuelData.fuelTimePercent * fuelData.huPerTick);
			data.setFloat("huTick", fuelData.huPerTick);
		} else if (variant.getFuel().type == FURNACE && isValidFuel(tile.machineStored, fuelStack)) {
			float huStored = fuelData.fuelTimePercent * fuelData.huPerTick
					* TileEntityFurnace.getItemBurnTime(fuelStack);
			data.setFloat("itemHU",
					fuelData.fuelTimePercent * fuelData.huPerTick * TileEntityFurnace.getItemBurnTime(fuelStack));
			data.setFloat("huTick", fuelData.huPerTick);
			fuelStack.shrink(1);
		} else {
			data.setFloat("itemHU", 0);
			data.setFloat("huTick", 0);
		}
		data.setFloat("maxHU", data.getFloat("itemHU"));
		tile.markDirty();
	}

	public int defaultMaxEnergy() {
		return this == RF ? 100000 : 0;
	}

	public int defaultMaxRecieve() {
		return this == RF ? 10000 : 0;
	}

	public int defaultMaxExtract() {
		return 0;
	}

	public int defaultMaxFluid() {
		return this == FLUID ? 4000 : 0;
	}
}
