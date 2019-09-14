package com.bartz24.varyingmachina.guide;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiGuide extends Screen {

    GuiGuidePageNavigation navigation;
    GuiPageInfo pageInfo;
    GuiGuideNavigationBar navigationBar;

    private ResourceLocation selectedPage;

    public GuiGuide() {
        super(new StringTextComponent(""));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        pageInfo.render(mouseX, mouseY, partialTicks);
        navigation.render(mouseX, mouseY, partialTicks);
        navigationBar.render(mouseX, mouseY, partialTicks);

        renderTooltip(pageInfo.getTooltip(mouseX, mouseY), mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();

        navigation = new GuiGuidePageNavigation(this, Math.min(200, width / 4), height, 40, height - 8, 20);
        navigation.setLeftPos(0);
        navigation.updateList();

        navigationBar = new GuiGuideNavigationBar(this, Math.min(200, width / 4) + 6, 0, width - Math.min(200, width / 4) - 6, 60);

        refreshPageInfo();
    }

    private void refreshPageInfo() {
        pageInfo = new GuiPageInfo(this, width - navigation.getWidth() - 6, height - 60, 0, height - 8, getSelectedPage() == null ? 1 : getSelectedPage().getHeight(width - navigation.getWidth() - 6));
        pageInfo.setLeftPos(navigation.getWidth() + 6);
        pageInfo.setTop(60);
        pageInfo.update();
        if (getSelectedPage() != null)
            getSelectedPage().load(pageInfo);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int clickType) {
        if (!navigation.mouseClicked(mouseX, mouseY, clickType) && !pageInfo.mouseClicked(mouseX, mouseY, clickType) && !navigationBar.mouseClicked(mouseX, mouseY, clickType))
            return super.mouseClicked(mouseX, mouseY, clickType);
        return true;
    }

    @Override
    public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
        if (!navigation.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_) && !pageInfo.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_))
            return super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
        return true;
    }

    @Override
    public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
        if (!navigation.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_) && !pageInfo.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_))
            return super.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
        return true;
    }

    @Override
    public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double p_mouseScrolled_5_) {
        return pageInfo.mouseScrolled(p_mouseScrolled_1_, p_mouseScrolled_3_, p_mouseScrolled_5_ * 0.02);
    }

    public void previousPage(ResourceLocation page) {
        selectedPage = page;
        navigation.setSelected(selectedPage);
        refreshPageInfo();
    }

    public void changePage(ResourceLocation page) {
        ResourceLocation selected = selectedPage;
        selectedPage = page;
        navigation.setSelected(selectedPage);
        if (selected != null)
            navigationBar.addPageToStack(selected);
        refreshPageInfo();

    }

    public IGuidePage getSelectedPage() {
        return GuideRegistry.getPage(selectedPage);
    }
}
