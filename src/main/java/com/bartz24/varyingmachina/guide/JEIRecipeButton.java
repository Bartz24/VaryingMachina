package com.bartz24.varyingmachina.guide;

import com.bartz24.varyingmachina.jei.VMJEIPlugin;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class JEIRecipeButton extends GuiPageButton {


    public JEIRecipeButton(GuiGuide guiGuide, int buttonId, int x, int y, ItemStack stack, String... args) {
        super(guiGuide, buttonId, x, y, args);
        this.displayStack = stack;
    }

    public JEIRecipeButton(GuiGuide guiGuide, int buttonId, int x, int y, String... args) {
        super(guiGuide, buttonId, x, y, args);
    }

    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        VMJEIPlugin.openRecipeFor.accept(displayStack);
        return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
    }

    @Override
    List<String> getTooltip(double mouseX, double mouseY) {
        return Collections.singletonList(new TranslationTextComponent("varyingmachina.guide.openjei").getFormattedText());
    }
}
