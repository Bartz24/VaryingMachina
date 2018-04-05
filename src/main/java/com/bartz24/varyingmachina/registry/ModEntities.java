package com.bartz24.varyingmachina.registry;

import java.util.HashMap;
import java.util.Map;

import com.bartz24.varyingmachina.References;
import com.bartz24.varyingmachina.base.tile.TileCasing;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModEntities {

	public static void registerEntities() {
		registerTE(TileCasing.class, "casing");
	}

	public static void registerTE(Class<? extends TileEntity> clazz, String id) {
		GameRegistry.registerTileEntity(clazz, new ResourceLocation(References.ModID, id).toString());
	}
}
