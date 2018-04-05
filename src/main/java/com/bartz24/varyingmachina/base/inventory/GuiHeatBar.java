package com.bartz24.varyingmachina.base.inventory;

import java.util.ArrayList;
import java.util.List;

import com.bartz24.varyingmachina.RandomHelper;
import com.bartz24.varyingmachina.References;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class GuiHeatBar implements GuiComp {

	public float maxHU, curHU, huTick, xPos, yPos, height, width;

	public GuiHeatBar(int x, int y, int maxHU, float curHU, int huTick) {
		xPos = x;
		yPos = y;
		height = 45;
		width = 10;
		updateData(maxHU, curHU, huTick);
	}

	@Override
	public void drawBackgroundGui(float partialTicks, GuiContainer gui, FontRenderer fontRenderer, int mouseX,
			int mouseY) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		Minecraft.getMinecraft().getTextureManager()
				.bindTexture(new ResourceLocation(References.ModID, "textures/gui/guiicons.png"));
		gui.drawTexturedModalRect(gui.getGuiLeft() + (int) xPos, gui.getGuiTop() + (int) yPos, 67, 0, (int) width,
				(int) height);
	}

	@Override
	public void drawForegroundGui(GuiContainer gui, FontRenderer fontRenderer, int mouseX, int mouseY) {
		int barHeight = maxHU <= 0 ? 0 : (int) ((curHU / (float) maxHU) * (height - 2));
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		Minecraft.getMinecraft().getTextureManager()
				.bindTexture(new ResourceLocation(References.ModID, "textures/gui/guiicons.png"));
		gui.drawTexturedModalRect((int) xPos + 1, (int) (yPos + height - 1 - barHeight), 59,
				(int) (height - 2) - barHeight, (int) (width - 2), barHeight);
	}

	@Override
	public void updateData(Object... data) {
		if (data[0] instanceof Integer)
			maxHU = (int) data[0];
		if (data[1] instanceof Float)
			curHU = (float) data[1];
		if (data[2] instanceof Integer)
			huTick = (int) data[2];
	}

	@Override
	public List<String> getTextTooltip(GuiContainer gui, FontRenderer fontRenderer, int mouseX, int mouseY) {

		List<String> text = new ArrayList();
		if (mouseX > gui.getGuiLeft() + xPos && mouseX < gui.getGuiLeft() + xPos + width
				&& mouseY > gui.getGuiTop() + yPos && mouseY < gui.getGuiTop() + yPos + height) {
			text.add(
					TextFormatting.YELLOW + "HU: " + RandomHelper.roundNumberDown(Math.max(curHU, 0), 1) + "/" + maxHU);
			text.add(TextFormatting.YELLOW + (huTick >= 0 ? "+ " : "- ") + Math.abs(huTick) + " HU/t");
		}
		return text;
	}

}
