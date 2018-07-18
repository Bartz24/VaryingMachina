package com.bartz24.varyingmachina.jei.machines;

import com.bartz24.varyingmachina.jei.JEIHelper;
import com.bartz24.varyingmachina.jei.ProcessRecipeCategory;
import com.bartz24.varyingmachina.jei.ProcessRecipeWrapper;
import mezz.jei.api.IGuiHelper;
import net.minecraft.util.math.Vec2f;

import java.util.ArrayList;
import java.util.List;

public class ExtractorRecipeCategory extends ProcessRecipeCategory<ProcessRecipeWrapper> {

	public ExtractorRecipeCategory(IGuiHelper guiHelper) {
		super("extractor", guiHelper, 150, 50);
		drawables.add(JEIHelper.createArrow(60, 17, guiHelper));
		drawables.add(JEIHelper.createTank(2, 10, guiHelper));
		drawables.add(JEIHelper.createTank(120, 10, guiHelper));
		drawables.add(JEIHelper.createTank(130, 10, guiHelper));
	}

	protected List<Vec2f> getInputSlotLocations() {
		List<Vec2f> list = new ArrayList();
		list.add(new Vec2f(30, 17));
		return list;
	}

	protected List<Vec2f> getOutputSlotLocations() {
		List<Vec2f> list = new ArrayList();
		list.add(new Vec2f(96, 17));
		return list;
	}

	protected List<Vec2f> getInputFluidLocations() {

		List<Vec2f> list = new ArrayList();
		list.add(new Vec2f(2, 10));
		return list;
	}

	protected List<Vec2f> getOutputFluidLocations() {

		List<Vec2f> list = new ArrayList();
		list.add(new Vec2f(120, 10));
		list.add(new Vec2f(130, 10));
		return list;
	}
}
