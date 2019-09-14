package com.bartz24.varyingmachina.guide;

import com.bartz24.varyingmachina.Helpers;
import com.bartz24.varyingmachina.gui.GuiTextButton;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiGuideNavigationBar extends Screen implements INestedGuiEventHandler {

    private GuiGuide guiGuide;
    private Button backButton;
    private int left, top, width, height;

    private List<ResourceLocation> pageStack = new ArrayList<>();

    public GuiGuideNavigationBar(GuiGuide guiGuide, int left, int top, int width, int height) {
        super(new StringTextComponent(""));
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;

        backButton = new GuiTextButton(left + 8, top + 8, 80, 20, new TranslationTextComponent("varyingmachina.guide.back").getFormattedText()) {
            @Override
            public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
                playDownSound(Minecraft.getInstance().getSoundHandler());
                guiGuide.previousPage(pageStack.size() == 0 ? null : pageStack.get(pageStack.size() - 1));
                pageStack.remove(pageStack.size() - 1);
                if (pageStack.size() == 0)
                    backButton.active = false;
                return false;
            }
        };

        backButton.active = false;
        this.guiGuide = guiGuide;
    }

    public void render(int mouseX, int mouseY, float partialTicks) {
        if (guiGuide != null) {
            GlStateManager.translated(0, 0, 200);
            this.fillGradient(left, top, left + width, top + height, -1072689136, -804253680);


            backButton.render(mouseX, mouseY, partialTicks);

            String disp = "";

            int i = 0;
            for (ResourceLocation id : pageStack) {
                disp += GuideRegistry.getPage(id).getDisplayName().getFormattedText();
                if (i < pageStack.size() - 1)
                    disp += '\u2192';
                i++;
            }
            if (guiGuide.getSelectedPage() != null) {


                FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
                fontRenderer.drawString("\u2192", left + 8, top + 42 + 8 - fontRenderer.FONT_HEIGHT / 2, 0xffffff);
                RenderHelper.enableGUIStandardItemLighting();
                GlStateManager.enableDepthTest();


                Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(guiGuide.getSelectedPage().getItemStackIcon(), left + 14, top + 42);
                fontRenderer.drawString(guiGuide.getSelectedPage().getDisplayName().getFormattedText(), left + 34, top + 42 + 8 - fontRenderer.FONT_HEIGHT / 2, 0xffffff);

                boolean removed = false;
                while (fontRenderer.getStringWidth(disp) > (width - 8 - 20) * 0.9 && disp.contains("\u2192")) {
                    disp = disp.substring(disp.indexOf('\u2192') + 1);
                    removed = true;
                }
                if (removed)
                    disp = "...\u2192" + disp;

                Helpers.drawCompressedString(fontRenderer, disp, left + 8, top + 32, false, width - 8 - 20, 0xffffff);
            }
            GlStateManager.translated(0, 0, -200);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int clickType) {
        if (backButton.active && mouseX >= backButton.x && mouseX < backButton.x + backButton.getWidth() && mouseY >= backButton.y && mouseY < backButton.y + backButton.getHeight())
            return backButton.mouseClicked(mouseX, mouseY, clickType);
        return false;
    }

    @Nullable
    @Override
    public IGuiEventListener getFocused() {
        return this;
    }

    public void addPageToStack(ResourceLocation id) {
        if ((guiGuide.getSelectedPage() == null || !guiGuide.getSelectedPage().getId().equals(id)) && (pageStack.size() == 0 || !pageStack.get(pageStack.size() - 1).equals(id))) {
            pageStack.add(id);
            backButton.active = true;
        }
    }
}
