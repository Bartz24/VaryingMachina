package com.bartz24.varyingmachina.base.inventory;

import java.util.ArrayList;
import java.util.List;

import com.bartz24.varyingmachina.References;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiArrowProgress implements GuiComp {
	public float maxProgress, curProgress, xPos, yPos, height, width;

	public GuiArrowProgress(int x, int y, int maxProgress, int curProgress) {
		xPos = x;
		yPos = y;
		height = 16;
		width = 23;
		updateData(maxProgress, curProgress);
	}

	@Override
	public void drawBackgroundGui(float partialTicks, GuiContainer gui, FontRenderer fontRenderer, int mouseX,
			int mouseY) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		Minecraft.getMinecraft().getTextureManager()
				.bindTexture(new ResourceLocation(References.ModID, "textures/gui/guiicons.png"));
		gui.drawTexturedModalRect(gui.getGuiLeft() + (int) xPos, gui.getGuiTop() + (int) yPos, 35, 77, (int) width,
				(int) height);
	}

	@Override
	public void drawForegroundGui(GuiContainer gui, FontRenderer fontRenderer, int mouseX, int mouseY) {
		int length = (int) (((float) curProgress / (float) maxProgress) * (width));
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		Minecraft.getMinecraft().getTextureManager()
				.bindTexture(new ResourceLocation(References.ModID, "textures/gui/guiicons.png"));
		gui.drawTexturedModalRect((int) xPos, (int) yPos, 35, 61, length, (int) height);
	}

	@Override
	public void updateData(Object... data) {
		if (data[0] instanceof Integer)
			maxProgress = (int) data[0];
		if (data[1] instanceof Integer)
			curProgress = (int) data[1];
	}

	@Override
	public List<String> getTextTooltip(GuiContainer gui, FontRenderer fontRenderer, int mouseX, int mouseY) {

		return new ArrayList();
	}
}
