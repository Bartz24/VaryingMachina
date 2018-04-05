package com.bartz24.varyingmachina.base.inventory;

import java.util.ArrayList;
import java.util.List;

import com.bartz24.varyingmachina.base.machine.MachineStat;
import com.bartz24.varyingmachina.base.tile.TileCasing;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;

public class GuiStatsComp implements GuiComp {

	private MachineStat[] stats;
	private TileCasing casing;
	private int xPos, yPos, height, width;

	public GuiStatsComp(int x, int y, MachineStat[] stats, TileCasing casing) {
		xPos = x;
		yPos = y;
		height = 14;
		width = 14;
		updateData(stats, casing);
	}

	@Override
	public void drawBackgroundGui(float partialTicks, GuiContainer gui, FontRenderer fontRenderer, int mouseX,
			int mouseY) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		int stage = (int) ((Minecraft.getMinecraft().getSystemTime() % (stats.length * 3000)) / 3000);
		Minecraft.getMinecraft().getTextureManager().bindTexture(stats[stage].image.path);
		gui.drawTexturedModalRect(gui.getGuiLeft() + (int) xPos, gui.getGuiTop() + (int) yPos, stats[stage].image.xPos,
				stats[stage].image.yPos, (int) width, (int) height);
	}

	@Override
	public void drawForegroundGui(GuiContainer gui, FontRenderer fontRenderer, int mouseX, int mouseY) {
	}

	@Override
	public void updateData(Object... data) {
		if (data[0] instanceof MachineStat[])
			stats = (MachineStat[]) data[0];
		if (data[1] instanceof TileCasing)
			casing = (TileCasing) data[1];
	}

	public void addStatTooltip(TileCasing casing, MachineStat stat, List<String> text) {
		stat.addCombinedInfo(casing, text);
	}

	@Override
	public List<String> getTextTooltip(GuiContainer gui, FontRenderer fontRenderer, int mouseX, int mouseY) {

		List<String> text = new ArrayList();
		if (mouseX > gui.getGuiLeft() + xPos && mouseX < gui.getGuiLeft() + xPos + width
				&& mouseY > gui.getGuiTop() + yPos && mouseY < gui.getGuiTop() + yPos + height) {
			for (MachineStat machineStat : casing.getMachine().getCombinedStats())
				addStatTooltip(casing, machineStat, text);
		}
		return text;
	}
}
