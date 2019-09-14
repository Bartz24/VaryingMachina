package com.bartz24.varyingmachina.gui;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiTextButton extends Button {
    public GuiTextButton(int x, int y, int width, int height, String buttonText) {
        super(x, y, width, height, buttonText, Button::onPress);
    }
}
