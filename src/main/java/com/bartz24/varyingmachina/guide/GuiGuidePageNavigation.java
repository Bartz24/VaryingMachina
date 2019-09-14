package com.bartz24.varyingmachina.guide;

import com.bartz24.varyingmachina.Helpers;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiGuidePageNavigation extends ExtendedList<GuiGuidePageNavigation.PageEntry> {

    private GuiGuide guiGuide;
    private int listWidth;

    public GuiGuidePageNavigation(GuiGuide guiGuide, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
        super(guiGuide.getMinecraft(), widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.listWidth = widthIn;
        this.guiGuide = guiGuide;
        this.renderHeader = false;
    }

    @Override
    protected void renderBackground() {

    }

    @Override
    protected void renderHeader(int p_renderHeader_1_, int p_renderHeader_2_, Tessellator p_renderHeader_3_) {

    }

    @Override
    protected int getScrollbarPosition() {
        return this.listWidth;
    }

    @Override
    public int getRowWidth() {
        return listWidth;
    }

    public void setSelected(ResourceLocation id) {
        for (PageEntry pageEntry : children()) {
            if (pageEntry instanceof CategoryEntry)
                setSelected(children().get(children().indexOf(pageEntry) + 1));
            else if (pageEntry.pageId.equals(id))
                setSelected(pageEntry);
        }
    }

    public static class PageEntry extends ExtendedList.AbstractListEntry<PageEntry> {

        GuiGuidePageNavigation parent;

        ResourceLocation pageId;

        public PageEntry(ResourceLocation id, GuiGuidePageNavigation parent) {
            this.pageId = id;
            this.parent = parent;
        }

        @Override
        public void render(int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks) {
            ItemStack stack = GuideRegistry.getPage(pageId).getItemStackIcon();
            if (!stack.isEmpty()) {
                RenderHelper.enableGUIStandardItemLighting();
                GlStateManager.enableDepthTest();
                Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(stack, left + 16, top + entryHeight / 2 - 8);
            }

            Helpers.drawCompressedString(Minecraft.getInstance().fontRenderer, GuideRegistry.getPage(pageId).getDisplayName().getFormattedText(), left + 34, top
                    + entryHeight / 2 - Minecraft.getInstance().fontRenderer.FONT_HEIGHT / 2, false, entryWidth - 40, 0xffffff);
        }

        @Override
        public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
            parent.setSelected(this);
            parent.guiGuide.changePage(pageId);
            return false;
        }
    }

    public static class CategoryEntry extends PageEntry {

        String category;

        public CategoryEntry(String category, GuiGuidePageNavigation parent) {
            super(null, parent);
            this.category = category;
            this.parent = parent;
        }

        @Override
        public void render(int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks) {
            ItemStack stack = GuideRegistry.getCategoryIcon(category);
            if (!stack.isEmpty()) {
                RenderHelper.enableGUIStandardItemLighting();
                Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(stack, left + 8, top + entryHeight / 2 - 8);
            }

            Helpers.drawCompressedString(Minecraft.getInstance().fontRenderer, new TranslationTextComponent(category + ".guidecategory.name").getFormattedText(), left + 28, top + entryHeight / 2 - Minecraft.getInstance().fontRenderer.FONT_HEIGHT / 2, false, entryWidth - 32, 0xffffff);
        }

        @Override
        public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
            parent.setSelected(parent.getEntry(parent.children().indexOf(this) + 1));
            parent.guiGuide.changePage(parent.children().get(parent.children().indexOf(this) + 1).pageId);
            return false;
        }
    }

    public void updateList() {
        this.clearEntries();
        for (String category : GuideRegistry.getCategoryIds()) {
            addEntry(new CategoryEntry(category, this));
            for (ResourceLocation id : GuideRegistry.getPageIds(category)) {
                addEntry(new PageEntry(id, this));
            }
        }
    }
}
