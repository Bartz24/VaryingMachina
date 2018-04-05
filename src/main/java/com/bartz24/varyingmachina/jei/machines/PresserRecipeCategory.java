package com.bartz24.varyingmachina.jei.machines;

import java.util.ArrayList;
import java.util.List;

import com.bartz24.varyingmachina.jei.JEIHelper;
import com.bartz24.varyingmachina.jei.ProcessRecipeCategory;
import com.bartz24.varyingmachina.jei.ProcessRecipeWrapper;

import mezz.jei.api.IGuiHelper;
import net.minecraft.util.math.Vec2f;

public class PresserRecipeCategory extends ProcessRecipeCategory<ProcessRecipeWrapper> {

	public PresserRecipeCategory(IGuiHelper guiHelper) {
		super("presser", guiHelper, 140, 50);
		drawables.add(JEIHelper.createArrow(30, 17, guiHelper));
	}

	protected List<Vec2f> getInputSlotLocations() {
		List<Vec2f> list = new ArrayList();
		list.add(new Vec2f(0, 17));
		return list;
	}

	protected List<Vec2f> getOutputSlotLocations() {
		List<Vec2f> list = new ArrayList();
		list.add(new Vec2f(66, 17));
		return list;
	}
}
