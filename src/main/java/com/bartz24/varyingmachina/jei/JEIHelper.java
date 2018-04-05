package com.bartz24.varyingmachina.jei;

import com.bartz24.varyingmachina.References;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated.StartDirection;
import mezz.jei.api.gui.IDrawableStatic;
import net.minecraft.util.ResourceLocation;

public class JEIHelper {
	public static JEIDrawable createHeatBar(int x, int y, IGuiHelper guiHelper) {
		IDrawableStatic background = guiHelper
				.createDrawable(new ResourceLocation(References.ModID, "textures/gui/guiIcons.png"), 67, 0, 10, 45);
		IDrawableStatic drawable = guiHelper
				.createDrawable(new ResourceLocation(References.ModID, "textures/gui/guiIcons.png"), 59, 0, 8, 43);
		IDrawable animate = guiHelper.createAnimatedDrawable(drawable, 200, StartDirection.BOTTOM, false);
		return new JEIDrawable(drawable, background, x, y, 1, 1);
	}

	public static JEIDrawable createArrow(int x, int y, IGuiHelper guiHelper) {
		IDrawableStatic background = guiHelper
				.createDrawable(new ResourceLocation(References.ModID, "textures/gui/guiIcons.png"), 35, 77, 23, 16);
		IDrawableStatic drawable = guiHelper
				.createDrawable(new ResourceLocation(References.ModID, "textures/gui/guiIcons.png"), 35, 61, 23, 16);
		IDrawable animate = guiHelper.createAnimatedDrawable(drawable, 200, StartDirection.LEFT, false);
		return new JEIDrawable(animate, background, x, y, 0, 0);
	}

	public static JEIDrawable createSlot(int x, int y, IGuiHelper guiHelper) {
		IDrawableStatic background = guiHelper
				.createDrawable(new ResourceLocation(References.ModID, "textures/gui/guiIcons.png"), 59, 60, 18, 18);
		return new JEIDrawable(background, x + 1, y + 1);
	}

	public static JEIDrawable createPattern(int x, int y, IGuiHelper guiHelper, boolean active) {
		IDrawableStatic background = guiHelper.createDrawable(
				new ResourceLocation(References.ModID, "textures/gui/guiIcons.png"), 77 + (active ? 5 : 0), 60, 5, 5);
		return new JEIDrawable(background, x, y);
	}
}
