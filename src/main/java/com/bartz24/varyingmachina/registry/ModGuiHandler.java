package com.bartz24.varyingmachina.registry;

import java.util.HashMap;
import java.util.Map;

import com.bartz24.varyingmachina.base.inventory.ContainerBase;
import com.bartz24.varyingmachina.base.inventory.ContainerCasing;
import com.bartz24.varyingmachina.base.inventory.ContainerModules;
import com.bartz24.varyingmachina.base.inventory.GuiCasing;
import com.bartz24.varyingmachina.base.inventory.GuiModules;
import com.bartz24.varyingmachina.base.tile.TileCasing;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGuiHandler implements IGuiHandler {
	private static final Map<Integer, GuiMapping> guiMappings = mapGUIIDs();

	private static Map<Integer, GuiMapping> mapGUIIDs() {
		Map<Integer, GuiMapping> map = new HashMap();
		map.put(0, new GuiMapping(GuiCasing.class, ContainerCasing.class, TileCasing.class));
		map.put(1, new GuiMapping(GuiModules.class, ContainerModules.class, TileCasing.class));
		return map;
	}

	public static int getIDForTile(Class<? extends TileEntity> tileClass) {
		for (int i : guiMappings.keySet()) {
			if (guiMappings.get(i).tileEntity.equals(tileClass))
				return i;
		}
		return -1;
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		return createContainer(id, player, world, new BlockPos(x, y, z));
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		return createGui(id, player, world, new BlockPos(x, y, z));
	}

	private static ContainerBase createContainer(int id, EntityPlayer player, World world, BlockPos pos) {
		try {
			return guiMappings.get(id).container
					.getDeclaredConstructor(EntityPlayer.class, guiMappings.get(id).tileEntity)
					.newInstance(player, guiMappings.get(id).tileEntity.cast(world.getTileEntity(pos)));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static GuiContainer createGui(int id, EntityPlayer player, World world, BlockPos pos) {
		try {
			return guiMappings.get(id).gui.getDeclaredConstructor(EntityPlayer.class, guiMappings.get(id).tileEntity)
					.newInstance(player, guiMappings.get(id).tileEntity.cast(world.getTileEntity(pos)));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static class GuiMapping {
		public Class<? extends TileEntity> tileEntity;
		public Class<? extends ContainerBase> container;
		public Class<? extends GuiContainer> gui;

		public GuiMapping(Class<? extends GuiContainer> gui, Class<? extends ContainerBase> container,
				Class<? extends TileEntity> tileEntity) {
			this.tileEntity = tileEntity;
			this.container = container;
			this.gui = gui;
		}
	}
}