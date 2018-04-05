package com.bartz24.varyingmachina.base.tile;

import com.bartz24.varyingmachina.base.item.ItemMachine;
import com.bartz24.varyingmachina.base.item.ItemModule;
import com.bartz24.varyingmachina.base.machine.FuelType;
import com.bartz24.varyingmachina.base.machine.MachineVariant;
import com.bartz24.varyingmachina.base.machine.MachineVariant.FuelInfo;
import com.bartz24.varyingmachina.base.tile.EnergyContainer.TransferType;
import com.bartz24.varyingmachina.registry.ModBlocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileCasing extends TileGenericPower implements ITickable {

	private FluidTankFiltered tank;
	private MachineVariant casingVariant;
	public ItemStack machineStored = ItemStack.EMPTY;

	public ItemStackHandler modules;

	public NBTTagCompound machineData = new NBTTagCompound();
	public NonNullList<NBTTagCompound> moduleData = NonNullList.withSize(6, new NBTTagCompound());

	public TileCasing() {
		super("casing", 0, 0, 0);
		tank = new FluidTankFiltered(0, null);
		modules = new ItemStackHandler(6);
	}

	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		NBTTagCompound stackTag = compound.getCompoundTag("Machine");
		if (stackTag != null)
			machineStored = new ItemStack(stackTag);
		machineData = compound.getCompoundTag("MachineData");
		casingVariant = MachineVariant.readFromNBT(compound);
		tank.readFromNBT(compound.getCompoundTag("tank"));
		modules.deserializeNBT(compound.getCompoundTag("modules"));
		NBTTagList list = compound.getTagList("moduleData", 10);
		for (int i = 0; i < 6; i++)
			moduleData.set(i, list.getCompoundTagAt(i));
		updateHandlerData();
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		NBTTagCompound stackTag = new NBTTagCompound();
		if (!machineStored.isEmpty())
			machineStored.writeToNBT(stackTag);
		compound.setTag("Machine", stackTag);
		compound.setTag("MachineData", machineData);
		compound.setString("variant", casingVariant == null ? "null" : casingVariant.getRegistryName().toString());
		compound.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
		compound.setTag("modules", modules.serializeNBT());
		NBTTagList list = new NBTTagList();
		for (NBTTagCompound tag : moduleData)
			list.appendTag(tag);
		compound.setTag("moduleData", list);
		return compound;
	}

	public MachineVariant getVariant() {
		return casingVariant;
	}

	public void setVariant(MachineVariant variant) {
		casingVariant = variant;
	}

	public ItemMachine getMachine() {
		return (ItemMachine) machineStored.getItem();
	}

	public ItemModule getModule(EnumFacing side) {
		return (ItemModule) modules.getStackInSlot(side.getIndex()).getItem();
	}

	public void setMachine(ItemStack machine) {
		super.dropInventory();
		machineStored = machine;
		updateHandlerData();
	}

	public void setModule(ItemStack module, EnumFacing side) {
		if(!modules.getStackInSlot(side.getIndex()).isEmpty() && module.isEmpty())
			getModule(side).onRemoveFromCasing(this, side);
		modules.setStackInSlot(side.getIndex(), module);
		moduleData.set(side.getIndex(), new NBTTagCompound());
		if(!modules.getStackInSlot(side.getIndex()).isEmpty())
			getModule(side).onAddToCasing(this, side);
		markDirtyBlockUpdate();
	}

	public void dropInventory() {
		ItemStack drop = MachineVariant.writeVariantToStack(new ItemStack(ModBlocks.casing), casingVariant);
		InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), drop);
		if (!machineStored.isEmpty()) {
			InventoryHelper.spawnItemStack(getWorld(), pos.getX(), pos.getY(), pos.getZ(), machineStored);
		}
		for (int i = 0; i < modules.getSlots(); ++i) {
			ItemStack itemstack = modules.getStackInSlot(i);

			InventoryHelper.spawnItemStack(getWorld(), pos.getX(), pos.getY(), pos.getZ(), itemstack);
		}
		super.dropInventory();
	}

	public FluidTankFiltered getTank() {
		return tank;
	}

	@Override
	public void update() {
		if (!machineStored.isEmpty()) {
			getMachine().update(getWorld(), getPos(), machineStored, machineData);
			for (int i = 0; i < 6; i++) {
				if (!modules.getStackInSlot(i).isEmpty()) {
					getModule(EnumFacing.values()[i]).update(this, EnumFacing.values()[i]);
				}
			}
		} else if (machineData.getSize() > 0) {
			machineData = new NBTTagCompound();
			markDirty();
		}
	}

	public void updateHandlerData() {
		boolean changed = false;
		if (!machineStored.isEmpty()) {
			FuelInfo fuelInfo = MachineVariant.readFromNBT(machineStored.getTagCompound()).getFuel();
			if (fuelInfo.type == FuelType.RF && energyStorage.getMaxEnergyStored() == 0) {
				int maxEnergy = getMachine().getMaxEnergy(machineStored) != 0 ? getMachine().getMaxEnergy(machineStored)
						: 0;
				int energy = getMachine().getMaxEnergy(machineStored) != 0 ? energyStorage.getEnergyStored() : 0;
				int maxExtract = getMachine().getMaxEnergy(machineStored) != 0
						? getMachine().getMaxExtract(machineStored) : 0;
				int maxReceive = getMachine().getMaxEnergy(machineStored) != 0
						? getMachine().getMaxReceive(machineStored) : 0;
				energyStorage = new EnergyContainer(maxEnergy, maxReceive, maxExtract, energy)
						.setSideTransferType(TransferType.NONE, EnumFacing.values());
				changed = true;
			}
			if (this.getInputInventory().getSlots() == 0 && getMachine().getInputItemSlots(machineStored) > 0) {
				this.setInputInventory(new ItemStackHandler(getMachine().getInputItemSlots(machineStored)) {
					protected void onContentsChanged(int slot) {
						super.onContentsChanged(slot);
						TileCasing.this.markDirty();
					}
				});
				changed = true;
			}
			if (this.getOutputInventory().getSlots() == 0 && getMachine().getOutputItemSlots(machineStored) > 0) {
				this.setOutputInventory(new ItemStackHandler(getMachine().getOutputItemSlots(machineStored)) {
					protected void onContentsChanged(int slot) {
						super.onContentsChanged(slot);
						TileCasing.this.markDirty();
					}
				});
				changed = true;
			}
			if (fuelInfo.type == FuelType.FLUID && tank.getFilter() == null) {
				this.tank = new FluidTankFiltered(getMachine().getFluid(machineStored), 0,
						getMachine().getMaxFluid(machineStored), getMachine().getFluid(machineStored));
				changed = true;
			}
		} else {

			if (energyStorage.getMaxEnergyStored() > 0) {
				energyStorage = new EnergyContainer(0, 0, 0, 0).setSideTransferType(TransferType.NONE,
						EnumFacing.values());
				changed = true;
			}
			if (getInputInventory().getSlots() > 0) {
				setInputInventory(new ItemStackHandler(0));
				changed = true;
			}
			if (getOutputInventory().getSlots() > 0) {
				setOutputInventory(new ItemStackHandler(0));
				changed = true;
			}
			if (tank.getFluid() != null) {
				tank = new FluidTankFiltered(0, null);
				changed = true;
			}
		}
		if (changed)
			markDirtyBlockUpdate();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (facing == null || (!modules.getStackInSlot(facing.getIndex()).isEmpty()
				&& getModule(facing).hasCapability(this, capability))) {
			if (capability == CapabilityEnergy.ENERGY) {
				return machineStored.isEmpty() ? false : (getMachine().getMaxEnergy(machineStored) != 0);
			} else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
				return machineStored.isEmpty() ? false
						: (getMachine().getInputItemSlots(machineStored) != 0
								|| getMachine().getOutputItemSlots(machineStored) != 0);
			} else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
				return machineStored.isEmpty() ? false : getMachine().getFluid(machineStored) != null;
			}
		}
		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {

		if (facing == null) {
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
				return (T) tank;
			else
				return super.getCapability(capability, facing);
		}

		return getModule(facing).getCapability(this, capability);
	}
}