package com.bartz24.varyingmachina.base.machine;

import java.util.List;

import com.bartz24.varyingmachina.References;
import com.bartz24.varyingmachina.base.tile.TileCasing;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public enum MachineStat {

	SPEED(new ImageData(ImageData.iconPath, 0, 44, 14, 14), DisplayType.PERCENT), SIZE(
			new ImageData(ImageData.iconPath, 32, 43, 14, 14), DisplayType.ACTUALFLOAT), EFFICIENCY(
					new ImageData(ImageData.iconPath, 45, 43, 14, 14), DisplayType.PERCENT), PRODUCTION(
							new ImageData(ImageData.iconPath, 18, 43, 14, 14), DisplayType.PERCENT), MAXHU(
									new ImageData(ImageData.iconPath, 0, 58, 14, 14), DisplayType.INT), PRESSURE(
											new ImageData(ImageData.iconPath, 61, 46, 14, 14), DisplayType.INT);

	public ImageData image;
	public DisplayType displayType;

	public static class ImageData {
		public static final ResourceLocation iconPath = new ResourceLocation(References.ModID,
				"textures/gui/guiicons.png");
		public ResourceLocation path;
		public int xPos, yPos, width, height;

		public ImageData(ResourceLocation path, int x, int y, int w, int h) {
			this.path = path;
			xPos = x;
			yPos = y;
			width = w;
			height = h;
		}
	}

	public static enum DisplayType {
		PERCENT, // Multiply
		ACTUALFLOAT, // Multiply
		INT // Add
	}

	MachineStat(ImageData data, DisplayType displayType) {
		image = data;
		this.displayType = displayType;
	}

	public void addSingleInfo(ItemStack stack, List<String> tooltip) {
		MachineVariant variant = MachineVariant.readFromNBT(stack.getTagCompound());
		if (variant != null) {
			switch (this) {
			case SPEED:
				tooltip.add(TextFormatting.BLUE + "Speed: " + getDisplayStat(variant.getStat(SPEED)));
				break;
			case SIZE:
				tooltip.add(TextFormatting.DARK_BLUE + "Size: " + getDisplayStat(variant.getStat(SIZE)));
				break;
			case EFFICIENCY:
				tooltip.add(TextFormatting.GREEN + "Efficiency: " + getDisplayStat(variant.getStat(EFFICIENCY)));
				break;
			case PRODUCTION:
				tooltip.add(TextFormatting.DARK_GREEN + "Production: " + getDisplayStat(variant.getStat(PRODUCTION)));
				break;
			case MAXHU:
				tooltip.add(TextFormatting.RED + "Max HU: " + getDisplayStat(variant.getStat(MAXHU)));
				break;
			case PRESSURE:
				tooltip.add(TextFormatting.YELLOW + "Pressure: " + getDisplayStat(variant.getStat(PRESSURE)) + " kPa");
				break;
			}
		}
	}

	public void addCombinedInfo(TileCasing casing, List<String> tooltip) {
		if (casing != null) {
			switch (this) {
			case SPEED:
				tooltip.add(TextFormatting.BLUE + "Speed: " + getDisplayStat(casing.getMachine().getCombinedStat(SPEED,
						casing.machineStored, casing.getWorld(), casing.getPos())));
				break;
			case SIZE:
				tooltip.add(TextFormatting.DARK_BLUE + "Size: " + getDisplayStat(casing.getMachine()
						.getCombinedStat(SIZE, casing.machineStored, casing.getWorld(), casing.getPos())));
				break;
			case EFFICIENCY:
				tooltip.add(TextFormatting.GREEN + "Efficiency: " + getDisplayStat(casing.getMachine()
						.getCombinedStat(EFFICIENCY, casing.machineStored, casing.getWorld(), casing.getPos())));
				break;
			case PRODUCTION:
				tooltip.add(TextFormatting.DARK_GREEN + "Production: " + getDisplayStat(casing.getMachine()
						.getCombinedStat(PRODUCTION, casing.machineStored, casing.getWorld(), casing.getPos())));
				break;
			case MAXHU:
				tooltip.add(TextFormatting.RED + "Max HU: " + getDisplayStat(casing.getMachine().getCombinedStat(MAXHU,
						casing.machineStored, casing.getWorld(), casing.getPos())));
				break;
			case PRESSURE:
				tooltip.add(TextFormatting.YELLOW + "Pressure: " + getDisplayStat(casing.getMachine()
						.getCombinedStat(PRESSURE, casing.machineStored, casing.getWorld(), casing.getPos())) + " kPa");
				break;
			}
		}
	}

	public String getDisplayStat(float value) {
		switch (displayType) {
		case PERCENT:
			return ((int) (value * 100f) + "%");
		case ACTUALFLOAT:
			return Float.toString(value);
		case INT:
			return Integer.toString((int) value);
		}
		return "";
	}
}
