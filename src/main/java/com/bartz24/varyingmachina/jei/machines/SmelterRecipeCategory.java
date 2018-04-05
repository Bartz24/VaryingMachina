package com.bartz24.varyingmachina.jei.machines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.ls.LSInput;

import com.bartz24.varyingmachina.jei.JEIHelper;
import com.bartz24.varyingmachina.jei.ProcessRecipeCategory;
import com.bartz24.varyingmachina.jei.ProcessRecipeWrapper;

import mezz.jei.api.IGuiHelper;
import net.minecraft.util.math.Vec2f;

public class SmelterRecipeCategory extends ProcessRecipeCategory<ProcessRecipeWrapper> {

	public SmelterRecipeCategory(IGuiHelper guiHelper) {
		super("smelter", guiHelper, 140, 50);
		drawables.add(JEIHelper.createHeatBar(130, 2, guiHelper));
		drawables.add(JEIHelper.createArrow(60, 17, guiHelper));
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
}
