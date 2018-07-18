package com.bartz24.varyingmachina.base.inventory;

import com.bartz24.varyingmachina.base.tile.FluidTankFiltered;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SidedFluidInventory implements IFluidHandler {

    private FluidTankFiltered[] tanks;

    public SidedFluidInventory(int size) {
        tanks = new FluidTankFiltered[size];
    }

    public SidedFluidInventory(String[] fluidFilters, int[] tankCapacities) {
        tanks = new FluidTankFiltered[fluidFilters.length];
        for (int i = 0; i < fluidFilters.length; i++)
            tanks[i] = new FluidTankFiltered(tankCapacities[i], fluidFilters[i]);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("size", tanks.length);
        NBTTagList list = new NBTTagList();
        for (FluidTankFiltered tank : tanks)
            list.appendTag(tank.writeToNBT(new NBTTagCompound()));
        compound.setTag("tanks", list);
        return compound;
    }

    public void readFromNBT(NBTTagCompound compound) {
        tanks = new FluidTankFiltered[compound.getInteger("size")];
        NBTTagList list = compound.getTagList("tanks", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tanks.length; i++)
            tanks[i] = (FluidTankFiltered) new FluidTankFiltered(0, null).readFromNBT(list.getCompoundTagAt(i));
    }

    public FluidTankFiltered getTankInSlot(int slot) {
        if (slot < 0 || slot >= tanks.length)
            return null;
        return tanks[slot];
    }

    public void setTankSlot(int slot, FluidTankFiltered tank) {
        if (slot >= 0 && slot < tanks.length)
            tanks[slot] = tank;
    }

    public int getSize() {
        return tanks.length;
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        List<IFluidTankProperties> tankPropertiesList = new ArrayList();
        for (FluidTankFiltered tank : tanks)
            tankPropertiesList.addAll(Arrays.asList(tank.getTankProperties()));
        return tankPropertiesList.toArray(new IFluidTankProperties[tankPropertiesList.size()]);
    }

    @Override
    public int fill(FluidStack fluidStack, boolean b) {
        for (FluidTankFiltered tank : tanks) {
            int filled = tank.fill(fluidStack, b);
            if (filled > 0)
                return filled;
        }
        return 0;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack fluidStack, boolean b) {
        for (FluidTankFiltered tank : tanks) {
            FluidStack drained = tank.drain(fluidStack, b);
            if (drained != null && drained.amount > 0)
                return drained;
        }
        return null;
    }

    @Nullable
    @Override
    public FluidStack drain(int i, boolean b) {
        for (FluidTankFiltered tank : tanks) {
            FluidStack drained = tank.drain(i, b);
            if (drained != null && drained.amount > 0)
                return drained;
        }
        return null;
    }
}
