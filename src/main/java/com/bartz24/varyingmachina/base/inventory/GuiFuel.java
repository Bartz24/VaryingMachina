package com.bartz24.varyingmachina.base.inventory;

import java.util.ArrayList;
import java.util.List;

import com.bartz24.varyingmachina.References;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiFuel implements GuiComp {
	public float max, cur, xPos, yPos, height, width;

	public GuiFuel(int x, int y, int max, int cur) {
		xPos = x;
		yPos = y;
		height = 14;
		width = 14;
		updateData(max, cur);
	}

	@Override
	public void drawBackgroundGui(float partialTicks, GuiContainer gui, FontRenderer fontRenderer, int mouseX,
			int mouseY) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		Minecraft.getMinecraft().getTextureManager()
				.bindTexture(new ResourceLocation(References.ModID, "textures/gui/guiicons.png"));
		gui.drawTexturedModalRect(gui.getGuiLeft() + (int) xPos, gui.getGuiTop() + (int) yPos, 77, 0, (int) width,
				(int) height);
	}

	@Override
	public void drawForegroundGui(GuiContainer gui, FontRenderer fontRenderer, int mouseX, int mouseY) {
		int barHeight = (int) (((float) cur / (float) max) * height);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		Minecraft.getMinecraft().getTextureManager()
				.bindTexture(new ResourceLocation(References.ModID, "textures/gui/guiicons.png"));
		gui.drawTexturedModalRect((int) xPos, (int) (yPos + height - barHeight), 77, 14 + (int) height - barHeight,
				(int) width, barHeight);
	}

	@Override
	public void updateData(Object... data) {
		if (data[0] instanceof Integer)
			max = (int) data[0];
		if (data[1] instanceof Integer)
			cur = (int) data[1];
	}

	@Override
	public List<String> getTextTooltip(GuiContainer gui, FontRenderer fontRenderer, int mouseX, int mouseY) {

		return new ArrayList();
	}
}
