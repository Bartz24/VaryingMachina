package com.bartz24.varyingmachina.jei.display;

import com.bartz24.varyingmachina.inventory.ContainerArea;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.util.LinkedHashMap;

public class JEIAreaReqs extends ContainerArea {

    private LinkedHashMap<String, Integer> displays;
    private int spacing = 12;

    public JEIAreaReqs(XAnchorDirection xDirection, YAnchorDirection yDirection, LinkedHashMap<String, Integer> displays) {
        super(xDirection, yDirection, 50);
        this.displays = displays;
    }

    @Override
    public void drawJEI(IGuiHelper helper, int leftX, int leftY) {
        super.drawJEI(helper, leftX, leftY);
        FontRenderer fr = Minecraft.getInstance().fontRenderer;

        int row = 0;
        for (String s : displays.keySet()) {
            fr.drawString(s, leftX + x + getWidth() - fr.getStringWidth(s), leftY + y + spacing * row, displays.get(s));
            row++;
        }
    }

    @Override
    public int getWidth() {
        FontRenderer fr = Minecraft.getInstance().fontRenderer;

        int width = 0;
        for (String s : displays.keySet()) {
            if (fr.getStringWidth(s) > width)
                width = fr.getStringWidth(s);
        }

        return width;
    }

    @Override
    public int getHeight() {
        return spacing * displays.size();
    }
}
