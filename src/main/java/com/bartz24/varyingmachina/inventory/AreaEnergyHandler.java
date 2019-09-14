package com.bartz24.varyingmachina.inventory;

import com.bartz24.varyingmachina.Helpers;
import com.bartz24.varyingmachina.gui.GuiMachine;
import com.bartz24.varyingmachina.tile.TileEntityMachine;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class AreaEnergyHandler extends ContainerArea {

    private BetterEnergyStorage energyStorage;
    private String id;


    public AreaEnergyHandler(BetterEnergyStorage energyStorage, String id, XAnchorDirection xDirection, YAnchorDirection yDirection, int weight) {
        super(xDirection, yDirection, weight);
        this.energyStorage = energyStorage;
        this.id = id;
    }


    @Override
    public void drawBackground(GuiMachine gui) {
        super.drawBackground(gui);

        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getInstance().getTextureManager()
                .bindTexture(new ResourceLocation("varyingmachina", "textures/gui/guiicons.png"));
        gui.blit(gui.getGuiLeft() + x, gui.getGuiTop() + y, 67, 0, 10,
                45);
    }

    @Override
    public void drawForeground(GuiMachine gui, int mouseX, int mouseY) {
        super.drawForeground(gui, mouseX, mouseY);
        float length = (((float) energyStorage.getEnergyStored() / (float) energyStorage.getMaxEnergyStored()) * 43);
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getInstance().getTextureManager()
                .bindTexture(new ResourceLocation("varyingmachina", "textures/gui/guiicons.png"));
        Helpers.drawTexturedModalRect(x + 1, y + 1 + 43 - length, 51, 0 + 43 - length, 8, length, gui.getZLevel());

        if (mouseX >= gui.getGuiLeft() + x && mouseX < gui.getGuiLeft() + x + 10 && mouseY >= gui.getGuiTop() + y && mouseY < gui.getGuiTop() + y + 45) {
            gui.renderTooltip(new TranslationTextComponent("varyingmachina.tooltip.energybar", energyStorage.getEnergyStored(), energyStorage.getMaxEnergyStored()).getFormattedText(), mouseX - gui.getGuiLeft(), mouseY - gui.getGuiTop());
            GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    @Override
    public void update(TileEntityMachine tile) {
        super.update(tile);
        energyStorage = tile.getEnergy().getHandler(id);
    }

    @Override
    public int getWidth() {
        return 10;
    }

    @Override
    public int getHeight() {
        return 45;
    }
}
