package com.bartz24.varyingmachina.base.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

public class GuiBase extends GuiContainer {

	public List<GuiComp> guiComponents = new ArrayList();

	public GuiBase(Container inventorySlotsIn, TileEntity e) {
		super(inventorySlotsIn);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		updateComponents();
		super.drawScreen(mouseX, mouseY, partialTicks);
		for (GuiButton b : buttonList)
			b.drawButtonForegroundLayer(mouseX, mouseY);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	protected void updateComponents() {

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		for (GuiComp c : guiComponents)
			c.drawBackgroundGui(partialTicks, this, fontRenderer, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		List<String> textLines = new ArrayList();
		for (GuiComp c : guiComponents) {
			c.drawForegroundGui(this, fontRenderer, mouseX, mouseY);
			textLines.addAll(c.getTextTooltip(this, fontRenderer, mouseX, mouseY));
		}
		if (textLines.size() > 0)
			drawHoveringText(textLines, mouseX - guiLeft, mouseY - guiTop);
	}

	public void drawHoveringText(List<String> textLines, int x, int y, FontRenderer font) {
		super.drawHoveringText(textLines, x, y, font);
	}

}
