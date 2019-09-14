package com.bartz24.varyingmachina.guide;

import com.bartz24.varyingmachina.Helpers;
import com.google.common.base.Strings;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiPageButton extends Button {
    protected ITextComponent display;
    protected ItemStack displayStack;
    protected GuiGuide guiGuide;

    public GuiPageButton(GuiGuide guiGuide, int buttonId, int x, int y, String... args) {
        super(x, y, 200, 16, "", Button::onPress);
        this.display = new TranslationTextComponent(args[1].trim());
        this.guiGuide = guiGuide;

        if (args.length > 2 && !Strings.isNullOrEmpty(args[2]) && args[2].contains(":")) {
            String trimmed = args[2].replaceAll(" ", "");
            String itemName;
            if (trimmed.contains("*"))
                itemName = trimmed.split("\\*")[0];
            else
                itemName = trimmed;

            ResourceLocation resourcelocation = new ResourceLocation(itemName);
            Item item = ForgeRegistries.ITEMS.getValue(resourcelocation);

            if (item == null)
                this.displayStack = ItemStack.EMPTY;
            else
                this.displayStack = new ItemStack(item, 1);
        } else displayStack = ItemStack.EMPTY;

        resetWidth();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {

        this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
                && mouseY < this.y + this.height;

        if (!displayStack.isEmpty()) {
            RenderHelper.enableGUIStandardItemLighting();
            Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(displayStack, this.x, this.y);

            Helpers.drawCompressedString(Minecraft.getInstance().fontRenderer, display.getFormattedText(), this.x + 20 + (width - 20) / 2, this.y + 4, true, this.width - 20, 16777215);

            RenderHelper.disableStandardItemLighting();
        }

        resetWidth();

        if (this.isHovered) {
            GlStateManager.disableDepthTest();
            this.fillGradient(this.x, this.y, this.x + this.width, this.y + this.height, 2138733178, 2138733178);
            GlStateManager.color4f(1, 1, 1, 1);
            GlStateManager.enableDepthTest();


        }
    }

    public void resetWidth() {
        this.width = Minecraft.getInstance().fontRenderer.getStringWidth(display.getFormattedText()) + 22;
    }

    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        return false;
    }

    List<String> getTooltip(double mouseX, double mouseY) {
        return Collections.singletonList(new TranslationTextComponent("varyingmachina.guide.gotopage").getFormattedText());
    }
}