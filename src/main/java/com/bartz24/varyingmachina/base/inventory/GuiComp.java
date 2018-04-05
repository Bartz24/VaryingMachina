package com.bartz24.varyingmachina.base.inventory;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;

public interface GuiComp {
	
	public void drawBackgroundGui(float partialTicks, GuiContainer gui, FontRenderer fontRenderer, int mouseX, int mouseY);

	public void drawForegroundGui(GuiContainer gui, FontRenderer fontRenderer, int mouseX, int mouseY);
	
	public List<String> getTextTooltip(GuiContainer gui, FontRenderer fontRenderer, int mouseX, int mouseY);

	public void updateData(Object... data);
}
