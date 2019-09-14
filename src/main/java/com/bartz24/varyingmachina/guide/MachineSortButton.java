package com.bartz24.varyingmachina.guide;

import com.bartz24.varyingmachina.Helpers;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class MachineSortButton extends GuiPageButton {

    private int sortType;
    private int clicks = 0;

    public MachineSortButton(GuiGuide guiGuide, int buttonId, int x, int y, int sortType, String... args) {
        super(guiGuide, buttonId, x, y, args);
        this.sortType = sortType;
    }

    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {

        if (guiGuide.getSelectedPage() instanceof MachineGuidePage) {
            clicks++;
            if (clicks > 2)
                clicks = 0;
            if (clicks > 0)
                ((MachineGuidePage) guiGuide.getSelectedPage()).sort(sortType, clicks == 2);

            return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
        }
        return false;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {

        this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
                && mouseY < this.y + this.height;

        this.fillGradient(this.x, this.y, this.x + width, this.y + height, -1072689136, -804253680);

        Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation("textures/gui/server_selection.png"));

        if (clicks > 0) {
            if (clicks == 1)
                blit(x, y, 96, 3, 18, 18);
            else
                blit(x, y, 64, 11, 18, 18);
        }
        Helpers.drawCompressedString(Minecraft.getInstance().fontRenderer, display.getFormattedText(), this.x + 20 + (width - 20) / 2, this.y + 4, true, this.width - 20, 16777215);

        if (this.isHovered) {
            GlStateManager.disableDepthTest();
            this.fillGradient(this.x, this.y, this.x + this.width, this.y + this.height, 2138733178, 2138733178);
            GlStateManager.color4f(1, 1, 1, 1);
            GlStateManager.enableDepthTest();


        }
    }

    @Override
    List<String> getTooltip(double mouseX, double mouseY) {
        List<String> list = new ArrayList<>();
        list.add(display.getFormattedText());
        list.add(new TranslationTextComponent("varyingmachina.guide.sort").getFormattedText());
        return list;
    }

    public int getSortType() {
        return sortType;
    }

    public void resetClicks() {
        clicks = 0;
    }
}
