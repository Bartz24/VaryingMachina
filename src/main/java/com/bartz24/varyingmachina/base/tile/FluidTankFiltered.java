package com.bartz24.varyingmachina.base.tile;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class FluidTankFiltered extends FluidTank {
	private Fluid filteredFluid;

	public FluidTankFiltered(int capacity, Fluid filter) {
		this(null, capacity, filter);
	}

	public FluidTankFiltered(Fluid fluid, int amount, int capacity, Fluid filter) {
		this(new FluidStack(fluid, amount), capacity, filter);
	}

	public FluidTankFiltered(@Nullable FluidStack fluidStack, int capacity, Fluid filter) {
		super(fluidStack, capacity);
		filteredFluid = filter;
	}

	public boolean canFillFluidType(FluidStack fluid) {
		if (filteredFluid == null
				|| (fluid != null && fluid.getFluid() != null && fluid.getFluid().equals(filteredFluid)))
			return canFill();
		else
			return false;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt = super.writeToNBT(nbt);
		if (filteredFluid == null)
			nbt.setString("filter", "null");
		else
			nbt.setString("filter", FluidRegistry.getFluidName(filteredFluid));
		nbt.setInteger("capacity", capacity);
		return nbt;
	}

	public FluidTank readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		String filterName = nbt.getString("filter");
		if (!filterName.equals("null"))
			filteredFluid = FluidRegistry.getFluid(filterName);
		else
			filteredFluid = null;
		capacity = nbt.getInteger("capacity");
		return this;
	}

	public Fluid getFilter() {
		return filteredFluid;
	}
}
