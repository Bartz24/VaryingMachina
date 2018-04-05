package com.bartz24.varyingmachina.registry;

import com.bartz24.varyingmachina.References;
import com.bartz24.varyingmachina.base.machine.FuelType;
import com.bartz24.varyingmachina.base.machine.MachineStat;
import com.bartz24.varyingmachina.base.machine.MachineVariant;
import com.bartz24.varyingmachina.base.machine.MachineVariant.FuelInfo;

import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(References.ModID)
public class ModVariants {
	// Vanilla
	public static MachineVariant dirt;
	public static MachineVariant wood;
	public static MachineVariant stone;
	public static MachineVariant brick;
	public static MachineVariant netherbrick;
	public static MachineVariant endstone;
	public static MachineVariant iron;
	// Basic/Thermal
	public static MachineVariant bronze;
	public static MachineVariant steel;
	public static MachineVariant lead;
	public static MachineVariant constantan;
	public static MachineVariant invar;
	public static MachineVariant electrum;
	public static MachineVariant signalum;
	public static MachineVariant enderium;
	// Mekanism
	public static MachineVariant osmium;
	public static MachineVariant refinedobsidian;
	// Botania
	public static MachineVariant manasteel;
	public static MachineVariant terrasteel;
	public static MachineVariant elementium;
	// Tinkers
	public static MachineVariant cobalt;
	public static MachineVariant ardite;
	public static MachineVariant manyullyn;
	// EnderIO
	public static MachineVariant electricalsteel;
	public static MachineVariant energeticalloy;
	public static MachineVariant vibrantalloy;
	public static MachineVariant soularium;
	public static MachineVariant darksteel;
	// VaryingMachina
	public static MachineVariant darkmatter;
	public static MachineVariant lightmatter;

	public static void registerVariants() {
		MachineRegistry.registerMachineVariant(
				new MachineVariant("dirt", new FuelInfo(FuelType.FURNACE), "minecraft:blocks/dirt")
						.setStat(MachineStat.MAXHU, 220).setStat(MachineStat.SPEED, .65f)
						.setStat(MachineStat.EFFICIENCY, .39f));
		MachineRegistry.registerMachineVariant(
				new MachineVariant("wood", new FuelInfo(FuelType.FURNACE), "minecraft:blocks/log_oak")
						.setStat(MachineStat.MAXHU, 120).setStat(MachineStat.SPEED, .41f)
						.setStat(MachineStat.EFFICIENCY, .97f));
		MachineRegistry.registerMachineVariant(
				new MachineVariant("stone", new FuelInfo(FuelType.FURNACE), "minecraft:blocks/stone")
						.setStat(MachineStat.MAXHU, 680).setStat(MachineStat.SPEED, 1.00f)
						.setStat(MachineStat.EFFICIENCY, 1.00f));
		MachineRegistry.registerMachineVariant(
				new MachineVariant("brick", new FuelInfo(FuelType.FURNACE), "minecraft:blocks/brick")
						.setStat(MachineStat.MAXHU, 2200).setStat(MachineStat.SPEED, .28f)
						.setStat(MachineStat.EFFICIENCY, 1.43f));
		MachineRegistry.registerMachineVariant(
				new MachineVariant("netherbrick", new FuelInfo(FuelType.ITEM, 300, "blaze_powder"),
						"minecraft:blocks/nether_brick").setStat(MachineStat.MAXHU, 3072)
								.setStat(MachineStat.SPEED, .45f).setStat(MachineStat.EFFICIENCY, .27f));
		MachineRegistry
				.registerMachineVariant(new MachineVariant("endstone", new FuelInfo(FuelType.ITEM, 2200, "ender_pearl"),
						"minecraft:blocks/end_stone").setStat(MachineStat.MAXHU, 2670).setStat(MachineStat.SPEED, 3.47f)
								.setStat(MachineStat.EFFICIENCY, 3.42f));
		MachineRegistry.registerMachineVariant(
				new MachineVariant("iron", new FuelInfo(FuelType.FURNACE), "varyingmachina:blocks/ironmachine")
						.setStat(MachineStat.MAXHU, 1538).setStat(MachineStat.SPEED, 1.21f)
						.setStat(MachineStat.EFFICIENCY, 1.16f));
		MachineRegistry.registerMachineVariant(new MachineVariant("bronze", new FuelInfo(FuelType.FLUID, 1, "steam"),
				"varyingmachina:blocks/bronzemachine").setStat(MachineStat.MAXHU, 950).setStat(MachineStat.SPEED, 1.46f)
						.setStat(MachineStat.EFFICIENCY, 1.34f));
		MachineRegistry.registerMachineVariant(
				new MachineVariant("steel", new FuelInfo(FuelType.FURNACE), "varyingmachina:blocks/steelmachine")
						.setStat(MachineStat.MAXHU, 1370).setStat(MachineStat.SPEED, 1.23f)
						.setStat(MachineStat.EFFICIENCY, 3.10f));
		MachineRegistry.registerMachineVariant(
				new MachineVariant("lead", new FuelInfo(FuelType.FURNACE), "varyingmachina:blocks/leadmachine")
						.setStat(MachineStat.MAXHU, 328).setStat(MachineStat.SPEED, 4.28f)
						.setStat(MachineStat.EFFICIENCY, .05f));
		MachineRegistry.registerMachineVariant(new MachineVariant("constantan", new FuelInfo(FuelType.RF, 10),
				"varyingmachina:blocks/constantanmachine").setStat(MachineStat.MAXHU, 1252)
						.setStat(MachineStat.SPEED, 2.00f).setStat(MachineStat.EFFICIENCY, 2.00f));
		MachineRegistry.registerMachineVariant(
				new MachineVariant("invar", new FuelInfo(FuelType.RF, 10), "varyingmachina:blocks/invarmachine")
						.setStat(MachineStat.MAXHU, 1427).setStat(MachineStat.SPEED, 1.82f)
						.setStat(MachineStat.EFFICIENCY, 2.49f));
		MachineRegistry.registerMachineVariant(
				new MachineVariant("electrum", new FuelInfo(FuelType.RF, 10), "varyingmachina:blocks/electrummachine")
						.setStat(MachineStat.MAXHU, 1878).setStat(MachineStat.SPEED, 3.37f)
						.setStat(MachineStat.EFFICIENCY, .83f));
		MachineRegistry.registerMachineVariant(
				new MachineVariant("signalum", new FuelInfo(FuelType.RF, 10), "varyingmachina:blocks/signalummachine")
						.setStat(MachineStat.MAXHU, 1362).setStat(MachineStat.SPEED, 3.82f)
						.setStat(MachineStat.EFFICIENCY, 1.49f));
		MachineRegistry.registerMachineVariant(
				new MachineVariant("enderium", new FuelInfo(FuelType.RF, 10), "varyingmachina:blocks/enderiummachine")
						.setStat(MachineStat.MAXHU, 2164).setStat(MachineStat.SPEED, 3.42f)
						.setStat(MachineStat.EFFICIENCY, 4.20f));
		MachineRegistry.registerMachineVariant(
				new MachineVariant("osmium", new FuelInfo(FuelType.RF, 10), "varyingmachina:blocks/osmiummachine")
						.setStat(MachineStat.MAXHU, 3033).setStat(MachineStat.SPEED, 1.63f)
						.setStat(MachineStat.EFFICIENCY, 0.45f));
		MachineRegistry.registerMachineVariant(new MachineVariant("refinedobsidian", new FuelInfo(FuelType.RF, 10),
				"varyingmachina:blocks/refinedobsidianmachine").setStat(MachineStat.MAXHU, 3768)
						.setStat(MachineStat.SPEED, 2.71f).setStat(MachineStat.EFFICIENCY, 5.46f));
		MachineRegistry.registerMachineVariant(new MachineVariant("manasteel", new FuelInfo(FuelType.MANA, 10),
				"varyingmachina:blocks/manasteelmachine").setStat(MachineStat.MAXHU, 1164)
						.setStat(MachineStat.SPEED, 1.48f).setStat(MachineStat.EFFICIENCY, 1.22f));
		MachineRegistry.registerMachineVariant(new MachineVariant("terrasteel", new FuelInfo(FuelType.MANA, 10),
				"varyingmachina:blocks/terrasteelmachine").setStat(MachineStat.MAXHU, 3277)
						.setStat(MachineStat.SPEED, 1.54f).setStat(MachineStat.EFFICIENCY, 3.36f));
		MachineRegistry.registerMachineVariant(new MachineVariant("elementium", new FuelInfo(FuelType.MANA, 10),
				"varyingmachina:blocks/elementiummachine").setStat(MachineStat.MAXHU, 864)
						.setStat(MachineStat.SPEED, 3.72f).setStat(MachineStat.EFFICIENCY, 2.21f));
		MachineRegistry.registerMachineVariant(new MachineVariant("cobalt", new FuelInfo(FuelType.FLUID, 1, "lava"),
				"varyingmachina:blocks/cobaltmachine").setStat(MachineStat.MAXHU, 1495).setStat(MachineStat.SPEED, 3.6f)
						.setStat(MachineStat.EFFICIENCY, 1.1f));
		MachineRegistry.registerMachineVariant(new MachineVariant("ardite", new FuelInfo(FuelType.FLUID, 1, "lava"),
				"varyingmachina:blocks/arditemachine").setStat(MachineStat.MAXHU, 3153).setStat(MachineStat.SPEED, 1.3f)
						.setStat(MachineStat.EFFICIENCY, 3.9f));
		MachineRegistry.registerMachineVariant(new MachineVariant("manyullyn", new FuelInfo(FuelType.FLUID, 1, "lava"),
				"varyingmachina:blocks/manyullynmachine").setStat(MachineStat.MAXHU, 2324)
						.setStat(MachineStat.SPEED, 2.45f).setStat(MachineStat.EFFICIENCY, 2.5f));
		MachineRegistry.registerMachineVariant(new MachineVariant("electricalsteel", new FuelInfo(FuelType.RF, 10),
				"varyingmachina:blocks/electricalsteelmachine").setStat(MachineStat.MAXHU, 1500)
						.setStat(MachineStat.SPEED, 1.14f).setStat(MachineStat.EFFICIENCY, 2.90f));
		MachineRegistry.registerMachineVariant(new MachineVariant("energeticalloy", new FuelInfo(FuelType.RF, 10),
				"varyingmachina:blocks/energeticalloymachine").setStat(MachineStat.MAXHU, 1023)
						.setStat(MachineStat.SPEED, 1.92f).setStat(MachineStat.EFFICIENCY, 1.01f));
		MachineRegistry.registerMachineVariant(new MachineVariant("vibrantalloy", new FuelInfo(FuelType.RF, 10),
				"varyingmachina:blocks/vibrantalloymachine").setStat(MachineStat.MAXHU, 1644)
						.setStat(MachineStat.SPEED, 5.45f).setStat(MachineStat.EFFICIENCY, 1.46f));
		MachineRegistry.registerMachineVariant(
				new MachineVariant("soularium", new FuelInfo(FuelType.ITEM, 180, "rotten_flesh"),
						"varyingmachina:blocks/soulariummachine").setStat(MachineStat.MAXHU, 1475)
								.setStat(MachineStat.SPEED, 1.48f).setStat(MachineStat.EFFICIENCY, 2.73f));
		MachineRegistry.registerMachineVariant(
				new MachineVariant("darksteel", new FuelInfo(FuelType.RF, 10), "varyingmachina:blocks/darksteelmachine")
						.setStat(MachineStat.MAXHU, 3765).setStat(MachineStat.SPEED, 1.38f)
						.setStat(MachineStat.EFFICIENCY, 3.40f));
		MachineRegistry.registerMachineVariant(
				new MachineVariant("darkmatter", new FuelInfo(FuelType.ITEM, 98696, "varyingmachina:darkmatter"),
						"varyingmachina:blocks/darkmatter").setStat(MachineStat.MAXHU, 4042)
								.setStat(MachineStat.SPEED, 10.00f).setStat(MachineStat.EFFICIENCY, 1.00f));
		MachineRegistry.registerMachineVariant(
				new MachineVariant("lightmatter", new FuelInfo(FuelType.ITEM, 73890, "varyingmachina:lightmatter"),
						"varyingmachina:blocks/lightmatter").setStat(MachineStat.MAXHU, 1566)
								.setStat(MachineStat.SPEED, 1.00f).setStat(MachineStat.EFFICIENCY, 10.00f));
	}
}
