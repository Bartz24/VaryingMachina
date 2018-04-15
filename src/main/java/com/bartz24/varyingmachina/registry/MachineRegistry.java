package com.bartz24.varyingmachina.registry;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.bartz24.varyingmachina.References;
import com.bartz24.varyingmachina.base.item.MachineItemBuilder;
import com.bartz24.varyingmachina.base.item.ModuleItemBuilder;
import com.bartz24.varyingmachina.base.machine.MachineVariant;
import com.bartz24.varyingmachina.base.recipe.ProcessRecipeRegistry;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.RegistryBuilder;

@EventBusSubscriber
public class MachineRegistry {

	private static List<MachineVariant> variants = new ArrayList();
	private static List<MachineItemBuilder> machineBuilders = new ArrayList();
	private static List<ModuleItemBuilder> moduleBuilders = new ArrayList();
	private static List<Method> recipeRegistries = new ArrayList();

	@SubscribeEvent
	public static void newRegistries(RegistryEvent.NewRegistry event) {
		RegistryBuilder<MachineVariant> b = new RegistryBuilder();
		b.setName(new ResourceLocation(References.ModID, "variants"));
		b.setType(MachineVariant.class);
		b.setIDRange(0, 1000);
		MachineVariant.setRegistry(b.create());
	}

	public static void registerMachineVariant(MachineVariant var) {
		if (!variants.contains(var))
			variants.add(var);
	}

	@SubscribeEvent
	public static void registerVariants(RegistryEvent.Register<MachineVariant> event) {
		event.getRegistry().registerAll(variants.toArray(new MachineVariant[variants.size()]));
	}

	public static void registerItemMachineType(MachineItemBuilder builder) {
		machineBuilders.add(builder);
	}

	public static void registerItemModuleType(ModuleItemBuilder builder) {
		moduleBuilders.add(builder);
	}

	@SubscribeEvent
	public static void registerItemMachines(RegistryEvent.Register<Item> event) {
		for (MachineItemBuilder b : machineBuilders) {
			event.getRegistry().register(b.buildItem());
		}
		machineBuilders.clear();
		for (ModuleItemBuilder b : moduleBuilders) {
			event.getRegistry().register(b.buildItem());
		}
		machineBuilders.clear();
	}

	/**
	 * {@link #WARN} Only use if you need access to the variants before the
	 * registry events are called after pre-init!
	 */
	public static MachineVariant[] getAllVariantsRegistered() {
		return variants.toArray(new MachineVariant[variants.size()]);
	}

	@SubscribeEvent
	public static void missingVariantMappings(RegistryEvent.MissingMappings<MachineVariant> event) {
		System.out.println(event.getMappings());
	}

	@SuppressWarnings("finally")
	public static void getRegistryRecipes(FMLPreInitializationEvent event) {
		List<Method> methods = new ArrayList();
		Set<ASMDataTable.ASMData> asmDatas = event.getAsmData().getAll(ProcessRecipeRegistry.class.getCanonicalName());
		for (ASMDataTable.ASMData asmData : asmDatas) {
			Class clazz;
			try {
				clazz = Class.forName(asmData.getClassName());
				for (Method method : clazz.getMethods()) {
					if (method.isAnnotationPresent(ProcessRecipeRegistry.class) && !methods.contains(method)) {
						methods.add(method);
					}
				}
			} finally {
				continue;
			}
		}
		recipeRegistries = methods;
	}

	@SuppressWarnings("finally")
	public static void registerRecipes() {
		for (Method method : recipeRegistries) {
			try {
				method.invoke(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
