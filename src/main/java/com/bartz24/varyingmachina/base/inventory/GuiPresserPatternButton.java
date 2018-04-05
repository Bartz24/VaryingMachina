package com.bartz24.varyingmachina.base.inventory;

import com.bartz24.varyingmachina.References;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiPresserPatternButton extends GuiButton {

	protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation(References.ModID,
			"textures/gui/guiicons.png");

	public boolean on;

	public GuiPresserPatternButton(int buttonId, int x, int y, boolean on) {
		super(buttonId, x, y, 5, 5, "");
		this.on = on;
	}

	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			FontRenderer fontrenderer = mc.fontRenderer;
			mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
					GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
					GlStateManager.DestFactor.ZERO);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
					GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			this.drawTexturedModalRect(this.x, this.y, 77 + (on ? 5 : 0), 60, 5, 5);
		}
	}

	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean pressed = super.mousePressed(mc, mouseX, mouseY);
		if (pressed)
			on = !on;
		return pressed;
	}
}
