package com.bartz24.varyingmachina.registry;

import com.bartz24.varyingmachina.References;
import com.bartz24.varyingmachina.base.block.BlockBase;
import com.bartz24.varyingmachina.base.block.BlockCasing;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(References.ModID)
@EventBusSubscriber
public class ModBlocks {
	@ObjectHolder("casing")
	public static Block casing;
	@ObjectHolder("darkmatterblock")
	public static Block darkmatterblock;
	@ObjectHolder("lightmatterblock")
	public static Block lightmatterblock;

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(new BlockCasing());
		event.getRegistry().register(new BlockBase("darkmatterblock", Material.IRON, MapColor.BLACK, 8, 16));
		event.getRegistry().register(
				new BlockBase("lightmatterblock", Material.IRON, MapColor.WHITE_STAINED_HARDENED_CLAY, 12, 8));
	}
}
