package com.bartz24.varyingmachina.inventory;

import com.bartz24.varyingmachina.Helpers;
import com.bartz24.varyingmachina.gui.GuiMachine;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class AreaSmallBar extends ContainerArea {

    private double progress = 0;
    private double maxProgress = 0;

    public AreaSmallBar(XAnchorDirection xDirection, YAnchorDirection yDirection) {
        super(xDirection, yDirection, 50);
    }

    @Override
    public void drawBackground(GuiMachine gui) {
        super.drawBackground(gui);

        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getInstance().getTextureManager()
                .bindTexture(new ResourceLocation("varyingmachina", "textures/gui/guiicons.png"));
        gui.blit(gui.getGuiLeft() + x, gui.getGuiTop() + y, 29, 60, 3,
                18);
    }

    @Override
    public void drawForeground(GuiMachine gui, int mouseX, int mouseY) {
        super.drawForeground(gui, mouseX, mouseY);
        float length = (float) ((progress / maxProgress) * 16);
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getInstance().getTextureManager()
                .bindTexture(new ResourceLocation("varyingmachina", "textures/gui/guiicons.png"));
        Helpers.drawTexturedModalRect(x + 1, y + 1 + 16 - length, 26, 61 + 16 - length, 1, length, gui.getZLevel());
    }

    @Override
    public void update(Object... data) {
        super.update(data);

        progress = (double) data[0];
        maxProgress = (double) data[1];
    }

    @Override
    public int getWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return 18;
    }
}
