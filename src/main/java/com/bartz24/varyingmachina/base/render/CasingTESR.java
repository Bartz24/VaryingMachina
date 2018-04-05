package com.bartz24.varyingmachina.base.render;

import org.lwjgl.opengl.GL11;

import com.bartz24.varyingmachina.base.machine.MachineVariant;
import com.bartz24.varyingmachina.base.tile.TileCasing;
import com.bartz24.varyingmachina.registry.ModBlocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class CasingTESR extends TileEntitySpecialRenderer<TileCasing> {

	@Override
	public void render(TileCasing te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5f, (float) y + 0.5f, (float) z + 0.5f);
		if (te.getVariant() != null) {
			Minecraft.getMinecraft().getRenderItem().renderItem(
					MachineVariant.writeVariantToStack(new ItemStack(ModBlocks.casing), te.getVariant()),
					TransformType.NONE);
		}
		if (!te.machineStored.isEmpty()) {
			Minecraft.getMinecraft().getRenderItem().renderItem(te.machineStored, TransformType.NONE);
		}
		for (int i = 0; i < 6; i++) {
			if (!te.modules.getStackInSlot(i).isEmpty()) {
				rotate(EnumFacing.values()[i]);
				Minecraft.getMinecraft().getRenderItem().renderItem(te.modules.getStackInSlot(i), TransformType.NONE);
				rotateBack(EnumFacing.values()[i]);
			}
		}
		GL11.glPopMatrix();
	}

	private void rotate(EnumFacing dir) {
		switch (dir) {
		case DOWN:
			GL11.glRotatef(-90, 1, 0, 0);
			break;
		case EAST:
			GL11.glRotatef(-90, 0, 1, 0);
			break;
		case NORTH:
			GL11.glRotatef(0, 0, 1, 0);
			break;
		case SOUTH:
			GL11.glRotatef(180, 0, 1, 0);
			break;
		case UP:
			GL11.glRotatef(90, 1, 0, 0);
			break;
		case WEST:
			GL11.glRotatef(90, 0, 1, 0);
			break;

		}
	}

	private void rotateBack(EnumFacing dir) {
		switch (dir) {
		case DOWN:
			GL11.glRotatef(90, 1, 0, 0);
			break;
		case EAST:
			GL11.glRotatef(90, 0, 1, 0);
			break;
		case NORTH:
			GL11.glRotatef(0, 0, 1, 0);
			break;
		case SOUTH:
			GL11.glRotatef(-180, 0, 1, 0);
			break;
		case UP:
			GL11.glRotatef(-90, 1, 0, 0);
			break;
		case WEST:
			GL11.glRotatef(-90, 0, 1, 0);
			break;

		}
	}
}