package com.bartz24.varyingmachina.jei;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.gui.elements.DrawableAnimated;
import net.minecraft.util.ResourceLocation;

public class JEIHelper {
    public static JEIDrawable createHeatBar(int x, int y, IGuiHelper guiHelper) {
        IDrawableStatic background = guiHelper
                .createDrawable(new ResourceLocation("varyingmachina", "textures/gui/guiicons.png"), 67, 0, 10, 45);
        IDrawableStatic drawable = guiHelper
                .createDrawable(new ResourceLocation("varyingmachina", "textures/gui/guiicons.png"), 59, 0, 8, 43);
        IDrawable animate = createAnimatedDrawable(drawable, 200, IDrawableAnimated.StartDirection.BOTTOM, false);
        return new JEIDrawable(drawable, background, x, y, 1, 1);
    }
    public static JEIDrawable createEnergyBar(int x, int y, IGuiHelper guiHelper) {
        IDrawableStatic background = guiHelper
                .createDrawable(new ResourceLocation("varyingmachina", "textures/gui/guiicons.png"), 67, 0, 10, 45);
        IDrawableStatic drawable = guiHelper
                .createDrawable(new ResourceLocation("varyingmachina", "textures/gui/guiicons.png"), 51, 0, 8, 43);
        IDrawable animate = createAnimatedDrawable(drawable, 200, IDrawableAnimated.StartDirection.BOTTOM, false);
        return new JEIDrawable(animate, background, x, y, 1, 1);
    }

    public static JEIDrawable createArrow(int x, int y, IGuiHelper guiHelper) {
        IDrawableStatic background = guiHelper
                .createDrawable(new ResourceLocation("varyingmachina", "textures/gui/guiicons.png"), 35, 77, 23, 16);
        IDrawableStatic drawable = guiHelper
                .createDrawable(new ResourceLocation("varyingmachina", "textures/gui/guiicons.png"), 35, 61, 23, 16);
        IDrawable animate = createAnimatedDrawable(drawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
        return new JEIDrawable(animate, background, x, y, 0, 0);
    }

    public static JEIDrawable createArrow(int x, int y, int time, IGuiHelper guiHelper) {
        IDrawableStatic background = guiHelper
                .createDrawable(new ResourceLocation("varyingmachina", "textures/gui/guiicons.png"), 35, 77, 23, 16);
        IDrawableStatic drawable = guiHelper
                .createDrawable(new ResourceLocation("varyingmachina", "textures/gui/guiicons.png"), 35, 61, 23, 16);
        IDrawable animate = createAnimatedDrawable(drawable, time, IDrawableAnimated.StartDirection.LEFT, false);
        return new JEIDrawable(animate, background, x, y, 0, 0);
    }

    public static JEIDrawable createTank(int x, int y, IGuiHelper guiHelper) {
        IDrawableStatic background = guiHelper
                .createDrawable(new ResourceLocation("varyingmachina", "textures/gui/guiicons.png"), 91, 0, 10, 37);
        return new JEIDrawable(background, x - 1, y - 1);
    }

    public static JEIDrawable createSetButton(int x, int y, IGuiHelper guiHelper, int state) {
        IDrawableStatic background = guiHelper
                .createDrawable(new ResourceLocation("varyingmachina", "textures/gui/guiicons.png"), state * 10, 104, 10, 10);
        return new JEIDrawable(background, x, y);
    }

    public static JEIDrawable createSlot(int x, int y, IGuiHelper guiHelper) {
        IDrawableStatic background = guiHelper
                .createDrawable(new ResourceLocation("varyingmachina", "textures/gui/guiicons.png"), 59, 60, 18, 18);
        return new JEIDrawable(background, x + 1, y + 1);
    }

    public static JEIDrawable createPattern(int x, int y, IGuiHelper guiHelper, boolean active) {
        IDrawableStatic background = guiHelper.createDrawable(
                new ResourceLocation("varyingmachina", "textures/gui/guiicons.png"), 77 + (active ? 5 : 0), 60, 5, 5);
        return new JEIDrawable(background, x, y);
    }

    public static IDrawableAnimated createAnimatedDrawable(IDrawableStatic drawable, int ticksPerCycle, IDrawableAnimated.StartDirection startDirection, boolean inverted) {
        IDrawableAnimated.StartDirection animationStartDirection = startDirection;
        if (inverted) {
            if (startDirection == IDrawableAnimated.StartDirection.TOP) {
                animationStartDirection = IDrawableAnimated.StartDirection.BOTTOM;
            } else if (startDirection == IDrawableAnimated.StartDirection.BOTTOM) {
                animationStartDirection = IDrawableAnimated.StartDirection.TOP;
            } else if (startDirection == IDrawableAnimated.StartDirection.LEFT) {
                animationStartDirection = IDrawableAnimated.StartDirection.RIGHT;
            } else {
                animationStartDirection = IDrawableAnimated.StartDirection.LEFT;
            }
        }

        int tickTimerMaxValue;
        if (animationStartDirection != IDrawableAnimated.StartDirection.TOP && animationStartDirection != IDrawableAnimated.StartDirection.BOTTOM) {
            tickTimerMaxValue = drawable.getWidth();
        } else {
            tickTimerMaxValue = drawable.getHeight();
        }

        return new DrawableAnimated(drawable, new JEITickTimer(ticksPerCycle, tickTimerMaxValue, !inverted), startDirection);
    }
}