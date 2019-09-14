package com.bartz24.varyingmachina.guide;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiPageInfo extends ExtendedList<GuiPageInfo.PageInfo> {

    private GuiGuide guiGuide;

    public GuiPageInfo(GuiGuide guiGuide, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
        super(guiGuide.getMinecraft(), widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.renderSelection = false;
        this.guiGuide = guiGuide;
    }

    @Override
    protected void renderBackground() {

    }

    @Override
    protected void renderHeader(int p_renderHeader_1_, int p_renderHeader_2_, Tessellator p_renderHeader_3_) {

    }

    public static class PageInfo extends ExtendedList.AbstractListEntry<GuiPageInfo.PageInfo> {

        GuiPageInfo parent;

        public PageInfo(GuiPageInfo parent) {
            this.parent = parent;
        }

        @Override
        public void render(int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks) {
            GlStateManager.translated(0, -parent.getScrollAmount(), 0);
            if (parent.guiGuide.getSelectedPage() != null)
                parent.guiGuide.getSelectedPage().drawSlot(parent, parent.x0 + 4, parent.getTop() + 4, mouseX, mouseY);
            GlStateManager.translated(0, parent.getScrollAmount(), 0);
        }

        @Override
        public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
            if (parent.guiGuide.getSelectedPage() != null)
                return parent.guiGuide.getSelectedPage().mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
            return false;
        }

        public List<String> getTooltip(double mouseX, double mouseY) {
            if (parent.guiGuide.getSelectedPage() != null)
                return parent.guiGuide.getSelectedPage().getTooltip(mouseX, mouseY);
            return new ArrayList<>();
        }
    }

    public List<String> getTooltip(double mouseX, double mouseY) {
        List<String> list = new ArrayList<>();
        if (mouseY >= getTop() && mouseX >= getLeft()) {
            mouseY += getScrollAmount();
            for (PageInfo pageInfo : children()) {
                list.addAll(pageInfo.getTooltip(mouseX, mouseY));
            }
        }
        return list;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int clickType) {
        this.updateScrollingState(mouseX, mouseY, clickType);

        if (mouseY >= getTop() && mouseX >= getLeft()) {
            return super.mouseClicked(mouseX, mouseY + getScrollAmount(), clickType);
        }
        return false;
    }

    public boolean isMouseInList(double p_195079_1_, double p_195079_3_) {
        return p_195079_3_ >= (double) this.getTop() && p_195079_1_ >= (double) this.getLeft();
    }

    @Override
    protected int getScrollbarPosition() {
        return getRowWidth() + getLeft();
    }

    @Override
    public int getRowWidth() {
        return width - 6;
    }

    public GuiGuide getGuiGuide() {
        return guiGuide;
    }

    public void setTop(int val) {
        y0 = val;
    }

    public void update() {
        clearEntries();
        addEntry(new PageInfo(this));
    }
}
