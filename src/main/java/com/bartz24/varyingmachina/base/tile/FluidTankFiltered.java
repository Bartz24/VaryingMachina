package com.bartz24.varyingmachina.base.tile;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class FluidTankFiltered extends FluidTank {
	private String filteredFluid;

	public FluidTankFiltered(int capacity, String filterName) {
		this(null, capacity, filterName);
	}

	public FluidTankFiltered(Fluid fluid, int amount, int capacity, String filterName) {
		this(new FluidStack(fluid, amount), capacity, filterName);
	}

	public FluidTankFiltered(@Nullable FluidStack fluidStack, int capacity, String filterName) {
		super(fluidStack, capacity);
		filteredFluid = filterName;
	}

	public boolean canFillFluidType(FluidStack fluid) {
		if (filteredFluid.equals("")
				|| (fluid != null && fluid.getFluid() != null && fluid.getFluid().getName().equals(filteredFluid)))
			return canFill();
		else
			return false;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt = super.writeToNBT(nbt);
			nbt.setString("filter", filteredFluid);
		nbt.setInteger("capacity", capacity);
		return nbt;
	}

	public FluidTank readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		filteredFluid = nbt.getString("filter");
		capacity = nbt.getInteger("capacity");
		return this;
	}

	public String getFilter() {
		return filteredFluid;
	}
}
