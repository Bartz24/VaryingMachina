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
import net.minecraftforge.fluids.FluidStack;

public class GuiFluidTank implements GuiComp {

	public float maxFluid, curFluid, xPos, yPos, height, width;
	private FluidStack fluid;

	public GuiFluidTank(int x, int y, int maxFluid, int curFluid, FluidStack fluid) {
		xPos = x;
		yPos = y;
		height = 37;
		width = 18;
		updateData(maxFluid, curFluid, fluid);
	}

	@Override
	public void drawBackgroundGui(float partialTicks, GuiContainer gui, FontRenderer fontRenderer, int mouseX,
			int mouseY) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		Minecraft.getMinecraft().getTextureManager()
				.bindTexture(new ResourceLocation(References.ModID, "textures/gui/guiicons.png"));
		gui.drawTexturedModalRect(gui.getGuiLeft() + (int) xPos, gui.getGuiTop() + (int) yPos, 91, 0, (int) width,
				(int) height);
	}

	@Override
	public void drawForegroundGui(GuiContainer gui, FontRenderer fontRenderer, int mouseX, int mouseY) {
		RandomHelper.renderGuiTank(fluid, (int) maxFluid, (int) curFluid, (double) xPos + 1d, (double) yPos + 1d,
				(double) width - 2d, (double) height - 2d);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		Minecraft.getMinecraft().getTextureManager()
				.bindTexture(new ResourceLocation(References.ModID, "textures/gui/guiicons.png"));
		gui.drawTexturedModalRect((int) xPos + 1, (int) yPos + 1, 34, 0,
				(int) width - 2, (int) height - 2);
	}

	@Override
	public void updateData(Object... data) {
		if(data[0] instanceof Integer)
		maxFluid = (int) data[0];
		if(data[1] instanceof Integer)
		curFluid = (int) data[1];
		if(data[2] instanceof FluidStack)
		fluid = (FluidStack) data[2];
	}

	@Override
	public List<String> getTextTooltip(GuiContainer gui, FontRenderer fontRenderer, int mouseX, int mouseY) {

		List<String> text = new ArrayList();
		if (mouseX > gui.getGuiLeft() + xPos && mouseX < gui.getGuiLeft() + xPos + width
				&& mouseY > gui.getGuiTop() + yPos && mouseY < gui.getGuiTop() + yPos + height) {
			if (fluid != null && fluid.getFluid() != null && curFluid > 0)
				text.add(fluid.getFluid().getLocalizedName(new FluidStack(fluid, (int) curFluid)) + ": "
						+ (int)curFluid + "/" + (int)maxFluid + " mB");
			else
				text.add("Empty");
		}
		return text;
	}

}
