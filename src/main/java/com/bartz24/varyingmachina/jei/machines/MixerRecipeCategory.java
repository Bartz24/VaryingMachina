package com.bartz24.varyingmachina.jei.machines;

import com.bartz24.varyingmachina.jei.JEIHelper;
import com.bartz24.varyingmachina.jei.ProcessRecipeCategory;
import com.bartz24.varyingmachina.jei.ProcessRecipeWrapper;
import mezz.jei.api.IGuiHelper;
import net.minecraft.util.math.Vec2f;

import java.util.ArrayList;
import java.util.List;

public class MixerRecipeCategory extends ProcessRecipeCategory<ProcessRecipeWrapper> {

	public MixerRecipeCategory(IGuiHelper guiHelper) {
		super("mixer", guiHelper, 140, 60);
		drawables.add(JEIHelper.createArrow(80, 21, guiHelper));
		drawables.add(JEIHelper.createTank(2, 10, guiHelper));
	}

	protected List<Vec2f> getInputSlotLocations() {
		List<Vec2f> list = new ArrayList();
		list.add(new Vec2f(14, 3));
		list.add(new Vec2f(32, 3));
		list.add(new Vec2f(50, 3));
		list.add(new Vec2f(14, 21));
		list.add(new Vec2f(32, 21));
		list.add(new Vec2f(50, 21));
		list.add(new Vec2f(14, 39));
		list.add(new Vec2f(32, 39));
		list.add(new Vec2f(50, 39));
		return list;
	}

	protected List<Vec2f> getOutputSlotLocations() {
		List<Vec2f> list = new ArrayList();
		list.add(new Vec2f(116, 21));
		return list;
	}

	protected List<Vec2f> getInputFluidLocations() {

		List<Vec2f> list = new ArrayList();
		list.add(new Vec2f(2, 10));
		return list;
	}
}
