package com.bartz24.varyingmachina.jei;

import javax.annotation.Nullable;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import net.minecraft.client.Minecraft;

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

	public void draw(Minecraft minecraft) {
		if (background != null)
		{
			background.draw(minecraft, x, y);
		}
		if (drawable != null)
			drawable.draw(minecraft, x + animXOff, y + animYOff);
	}
}
