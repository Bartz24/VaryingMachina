package com.bartz24.varyingmachina.registry;

import java.util.ArrayList;
import java.util.List;

import com.bartz24.varyingmachina.base.item.IItemBase;
import com.bartz24.varyingmachina.base.render.CasingTESR;
import com.bartz24.varyingmachina.base.tile.TileCasing;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(Side.CLIENT)
public class ModRenderers {
	private static List<IItemBase> itemsToRegisterRenderers = new ArrayList();

	@SideOnly(Side.CLIENT)
	public static void addItemToRender(IItemBase item) {
		itemsToRegisterRenderers.add(item);
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		for (IItemBase i : itemsToRegisterRenderers)
			i.initModel();
		itemsToRegisterRenderers.clear();
	}

	public static void registerEntityTESRs() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileCasing.class, new CasingTESR());
	}

}
