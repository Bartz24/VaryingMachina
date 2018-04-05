package com.bartz24.varyingmachina.registry;

import com.bartz24.varyingmachina.References;
import com.bartz24.varyingmachina.base.item.ItemBase;
import com.bartz24.varyingmachina.base.item.ItemBlockCasing;
import com.bartz24.varyingmachina.base.item.MachineItemBuilder;
import com.bartz24.varyingmachina.base.item.ModuleItemBuilder;
import com.bartz24.varyingmachina.machines.MachineGrinder;
import com.bartz24.varyingmachina.machines.MachinePresser;
import com.bartz24.varyingmachina.machines.MachineSmelter;
import com.bartz24.varyingmachina.modules.ModuleBellow;
import com.bartz24.varyingmachina.modules.ModuleGearbox;
import com.bartz24.varyingmachina.modules.ModuleInserter;
import com.bartz24.varyingmachina.modules.ModuleRegulator;
import com.bartz24.varyingmachina.modules.ModuleRemover;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(References.ModID)
@EventBusSubscriber
public class ModItems {
	@ObjectHolder("darkmatter")
	public static Item darkmatter;
	@ObjectHolder("lightmatter")
	public static Item lightmatter;

	@ObjectHolder("smelter")
	public static Item smelter;
	@ObjectHolder("grinder")
	public static Item grinder;
	@ObjectHolder("presser")
	public static Item presser;

	@ObjectHolder("regulator")
	public static Item regulator;
	@ObjectHolder("inserter")
	public static Item inserter;
	@ObjectHolder("remover")
	public static Item remover;
	@ObjectHolder("bellow")
	public static Item bellow;
	@ObjectHolder("gearbox")
	public static Item gearbox;

	public static void registerMachines() {
		MachineRegistry
				.registerItemMachineType(new MachineItemBuilder(MachineSmelter.class, References.ModID, "smelter"));
		MachineRegistry
				.registerItemMachineType(new MachineItemBuilder(MachineGrinder.class, References.ModID, "grinder"));
		MachineRegistry
				.registerItemMachineType(new MachineItemBuilder(MachinePresser.class, References.ModID, "presser"));

		MachineRegistry
				.registerItemModuleType(new ModuleItemBuilder(ModuleRegulator.class, References.ModID, "regulator"));
		MachineRegistry
				.registerItemModuleType(new ModuleItemBuilder(ModuleInserter.class, References.ModID, "inserter"));
		MachineRegistry.registerItemModuleType(new ModuleItemBuilder(ModuleRemover.class, References.ModID, "remover"));
		MachineRegistry.registerItemModuleType(new ModuleItemBuilder(ModuleBellow.class, References.ModID, "bellow"));
		MachineRegistry.registerItemModuleType(new ModuleItemBuilder(ModuleGearbox.class, References.ModID, "gearbox"));
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(new ItemBase().setRegistryName(References.ModID, "darkmatter")
				.setUnlocalizedName(References.ModID + ".darkmatter"));
		event.getRegistry().register(new ItemBase().setRegistryName(References.ModID, "lightmatter")
				.setUnlocalizedName(References.ModID + ".lightmatter"));
		event.getRegistry().register(new ItemBlockCasing(ModBlocks.casing));
	}
}
