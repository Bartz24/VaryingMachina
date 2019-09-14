package com.bartz24.varyingmachina.jei;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;

import javax.annotation.Nullable;

public class JEIDrawable {
    public int x, y, animXOff, animYOff;
    IDrawableStatic background;
    IDrawable drawable;

    public JEIDrawable(@Nullable IDrawable drawable, @Nullable IDrawableStatic background, int x, int y, int animXOff,
                       int animYOff) {
        this.drawable = drawable;
        this.background = background;
        this.x = x;
        this.y = y;
        this.animXOff = animXOff;
        this.animYOff = animYOff;
    }

    public JEIDrawable(IDrawableStatic background, int x, int y) {
        this(null, background, x, y, 0, 0);
    }

    public void draw() {
        if (background != null)
            background.draw(x, y);
        if (drawable != null)
            drawable.draw(x + animXOff, y + animYOff);
    }
}