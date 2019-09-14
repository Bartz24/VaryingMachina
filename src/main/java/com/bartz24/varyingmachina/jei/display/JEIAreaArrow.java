package com.bartz24.varyingmachina.jei.display;

import com.bartz24.varyingmachina.gui.GuiMachine;
import com.bartz24.varyingmachina.inventory.ContainerArea;
import com.bartz24.varyingmachina.jei.JEIHelper;
import com.mojang.blaze3d.platform.GlStateManager;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class JEIAreaArrow extends ContainerArea {

    private int time;

    public JEIAreaArrow(XAnchorDirection xDirection, YAnchorDirection yDirection, int time) {
        super(xDirection, yDirection, 50);
        this.time = time;
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
    public void drawJEI(IGuiHelper helper, int leftX, int leftY) {
        super.drawJEI(helper, leftX, leftY);
        JEIHelper.createArrow(leftX + x, leftY + y, time, helper).draw();
    }

    @Override
    public int getWidth() {
        return 23;
    }

    @Override
    public int getHeight() {
        return 16;
    }
}
