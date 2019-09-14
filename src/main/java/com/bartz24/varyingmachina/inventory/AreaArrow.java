package com.bartz24.varyingmachina.inventory;

import com.bartz24.varyingmachina.Helpers;
import com.bartz24.varyingmachina.gui.GuiMachine;
import com.bartz24.varyingmachina.tile.TileEntityMachine;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class AreaArrow extends ContainerArea {

    private double progress = 0;
    private int maxProgress = 0;

    public AreaArrow(XAnchorDirection xDirection, YAnchorDirection yDirection) {
        super(xDirection, yDirection, 50);
    }

    @Override
    public void drawBackground(GuiMachine gui) {
        super.drawBackground(gui);

        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getInstance().getTextureManager()
                .bindTexture(new ResourceLocation("varyingmachina", "textures/gui/guiicons.png"));
        gui.blit(gui.getGuiLeft() + x, gui.getGuiTop() + y, 35, 77, 23,
                16);
    }

    @Override
    public void drawForeground(GuiMachine gui, int mouseX, int mouseY) {
        super.drawForeground(gui, mouseX, mouseY);
        float length = (((float) progress / (float) maxProgress) * 23);
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getInstance().getTextureManager()
                .bindTexture(new ResourceLocation("varyingmachina", "textures/gui/guiicons.png"));
        Helpers.drawTexturedModalRect(x, y, 35, 61, length, 16, gui.getZLevel());
    }

    @Override
    public void update(TileEntityMachine tile) {
        super.update(tile);

        progress = tile.getProgress();
        maxProgress = tile.getRecipe().getTime();
    }

    @Override
    public int getWidth() {
        return 23;
    }
}
