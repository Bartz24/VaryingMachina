package com.bartz24.varyingmachina.base.machine;

import java.util.HashMap;
import java.util.Map;

import com.bartz24.varyingmachina.References;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class MachineVariant extends IForgeRegistryEntry.Impl<MachineVariant> {

	public static IForgeRegistry<MachineVariant> REGISTRY;

	private FuelInfo fuelInfo;
	private String texture;
	private Map<MachineStat, Float> statValues = new HashMap();

	public static class FuelInfo {
		public FuelType type;
		public float rate;
		public String fuel;

		public FuelInfo(FuelType type) {
			this.type = type;
		}

		public FuelInfo(FuelType type, float rate) {
			this.type = type;
			this.rate = rate;
		}

		public FuelInfo(FuelType type, float rate, String fuel) {
			this.type = type;
			this.rate = rate;
			this.fuel = fuel;
		}

		public Fluid getFluid() {
			return type != FuelType.FLUID ? null : FluidRegistry.getFluid(fuel);
		}

		public ItemStack getStack() {
			return type != FuelType.ITEM ? null : new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(fuel)));
		}
	}

	public static void setRegistry(IForgeRegistry<MachineVariant> iForgeRegistry) {
		REGISTRY = iForgeRegistry;
	}

	public MachineVariant(String modName, String variantName, FuelInfo fuel, String texture) {
		this(new ResourceLocation(modName, variantName), fuel, texture);
	}

	public MachineVariant(String variantName, FuelInfo fuel, String texture) {
		this(new ResourceLocation(References.ModID, variantName), fuel, texture);
	}

	public MachineVariant(ResourceLocation name, FuelInfo fuel, String texture) {
		this.setRegistryName(name);
		fuelInfo = fuel;
		this.texture = texture;
	}

	public MachineVariant setStat(MachineStat stat, float value) {
		statValues.put(stat, value);
		return this;
	}

	public float getStat(MachineStat stat) {
		if (stat == MachineStat.SIZE)
			return (int) ((float) getStat(MachineStat.SPEED) * 8f);
		else if (stat == MachineStat.PRODUCTION)
			return (float) getStat(MachineStat.EFFICIENCY) * 0.6f;
		else if (stat == MachineStat.PRESSURE)
			return (int) (getStat(MachineStat.EFFICIENCY) * 240f);
		return statValues.containsKey(stat) ? (float) statValues.get(stat) : 0;
	}

	public FuelInfo getFuel() {
		return fuelInfo;
	}

	public String getTexturePath() {
		return texture;
	}

	public static MachineVariant readFromNBT(NBTTagCompound compound) {
		if (compound == null || MachineVariant.REGISTRY == null)
			return null;

		String type = compound.getString("variant");
		if (type.equals("null"))
			return null;
		ResourceLocation location = new ResourceLocation(type);
		MachineVariant variant = MachineVariant.REGISTRY.getValue(location);
		return variant;
	}

	public static ItemStack writeVariantToStack(ItemStack stack, MachineVariant variant) {
		if (variant == null)
			return ItemStack.EMPTY;
		if (stack.getTagCompound() == null)
			stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setString("variant", variant.getRegistryName().toString());
		return stack;
	}
}
