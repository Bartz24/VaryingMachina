package com.bartz24.varyingmachina.base.machine;

import java.util.ArrayList;
import java.util.List;

import com.bartz24.varyingmachina.VaryingMachina;

import net.minecraft.item.ItemStack;

public class WrenchHelper {
	private static List<Class> wrenchClasses = new ArrayList<Class>();

	public static void registerClasses() {
		for (String className : new String[] { "buildcraft.api.tools.IToolWrench", // Buildcraft
				"immersiveengineering.api.tool.ITool", //IE
				"ic2.core.item.tool.ItemToolWrench", // IC2
				"ic2.core.item.tool.ItemToolWrenchElectric", // IC2
				"mods.railcraft.api.core.items.IToolCrowbar", // Railcraft
				"cofh.api.item.IToolHammer", // Thermal Expansion
				"appeng.api.implementations.items.IAEWrench", // AE
				"crazypants.enderio.api.tool.ITool", // Ender IO
				"mekanism.api.IMekWrench", // Mekanism
		}) {
			try {
				wrenchClasses.add(Class.forName(className));
			} catch (ClassNotFoundException e) {
				VaryingMachina.logger.info("Could not find " + className + " for a wrench");
			}
		}
	}

	public static boolean isValidWrench(ItemStack stack) {
		for (Class c : wrenchClasses) {
			if (!stack.isEmpty() && c.isAssignableFrom(stack.getItem().getClass()))
				return true;
		}
		return false;
	}
}
