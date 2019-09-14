package com.bartz24.varyingmachina.jei.display;

import com.bartz24.varyingmachina.inventory.ContainerArea;
import com.bartz24.varyingmachina.jei.JEIHelper;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TranslationTextComponent;

public class JEIAreaEnergy extends ContainerArea {

    private int energy;

    public JEIAreaEnergy(XAnchorDirection xDirection, YAnchorDirection yDirection, int weight, int energy) {
        super(xDirection, yDirection, weight);
        this.energy = energy;
    }

    @Override
    public void drawJEI(IGuiHelper helper, int leftX, int leftY) {
        super.drawJEI(helper, leftX, leftY);
        JEIHelper.createEnergyBar(leftX + x, leftY + y, helper).draw();

        String text = new TranslationTextComponent("varyingmachina.jei.rf", energy).getFormattedText();

        Minecraft.getInstance().fontRenderer.drawString(text, leftX + x + 12, leftY + y, 4210752);
    }

    @Override
    public int getWidth() {
        return 12 + Minecraft.getInstance().fontRenderer.getStringWidth(new TranslationTextComponent("varyingmachina.jei.rf", energy).getFormattedText());
    }

    @Override
    public int getHeight() {
        return 45;
    }
}
