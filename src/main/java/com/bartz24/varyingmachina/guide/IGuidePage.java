package com.bartz24.varyingmachina.guide;

import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public interface IGuidePage extends INestedGuiEventHandler {
    ITextComponent getDisplayName();

    ItemStack getItemStackIcon();

    void load(GuiPageInfo gui);

    void drawSlot(GuiPageInfo gui, int x, int y, double mouseX, double mouseY);

    List<String> getTooltip(double mouseX, double mouseY);

    int getHeight(int width);

    ResourceLocation getId();

    @Override
    default List<? extends IGuiEventListener> children() {
        return new ArrayList<>();
    }

    @Override
    default boolean isDragging() {
        return false;
    }

    @Override
    default void setDragging(boolean b) {

    }

    @Override
    default void setFocused(@Nullable IGuiEventListener iGuiEventListener) {

    }
}
