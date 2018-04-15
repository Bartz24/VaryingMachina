package com.bartz24.varyingmachina.jei;

import java.util.ArrayList;
import java.util.List;

import com.bartz24.varyingmachina.References;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.text.translation.I18n;

public class ProcessRecipeCategory<T extends IRecipeWrapper> implements IRecipeCategory<ProcessRecipeWrapper> {

	protected String recipeID;

	protected IDrawable background;

	protected List<JEIDrawable> drawables = new ArrayList();

	public ProcessRecipeCategory(String recipeID, IGuiHelper guiHelper, int width, int height) {
		this.recipeID = recipeID;
		this.background = guiHelper.createDrawable(new ResourceLocation(References.ModID, "textures/gui/blankjei.png"),
				0, 0, width, height);
		createSlots(guiHelper);
	}

	public ProcessRecipeCategory(String recipeID, IGuiHelper guiHelper, IDrawable background) {
		this.recipeID = recipeID;
		this.background = background;
		createSlots(guiHelper);
	}

	public String getUid() {

		return References.ModID + ":" + recipeID;
	}

	@Override
	public String getTitle() {
		return I18n.translateToLocalFormatted("jei.varyingmachina.recipe." + recipeID);
	}

	@Override
	public String getModName() {
		return References.ModID;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void setRecipe(IRecipeLayout layout, ProcessRecipeWrapper recipeWrapper, IIngredients ingredients) {
		List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
		for (int slot = 0; slot < getInputSlotLocations().size(); slot++) {
			Vec2f pos = getInputSlotLocations().get(slot);
			if (slot < inputs.size())
			{
				layout.getItemStacks().init(slot, true, (int) pos.x, (int) pos.y);
				layout.getItemStacks().set(slot, inputs.get(slot));
			}
		}

		List<List<ItemStack>> outputs = ingredients.getOutputs(ItemStack.class);
		for (int slot = getInputSlotLocations().size(); slot < getInputSlotLocations().size()
				+ getOutputSlotLocations().size(); slot++) {
			Vec2f pos = getOutputSlotLocations().get(slot - getInputSlotLocations().size());
			layout.getItemStacks().init(slot, false, (int) pos.x, (int) pos.y);
			layout.getItemStacks().set(slot, outputs.get(slot - getInputSlotLocations().size()));
		}
	}

	@Override
	public void drawExtras(Minecraft minecraft) {
		for (JEIDrawable drawable : drawables)
			drawable.draw(minecraft);
	}

	protected List<Vec2f> getInputSlotLocations() {
		return new ArrayList();
	}

	protected List<Vec2f> getOutputSlotLocations() {
		return new ArrayList();
	}

	protected void createSlots(IGuiHelper guiHelper) {
		for (Vec2f pos : getInputSlotLocations()) {
			drawables.add(JEIHelper.createSlot((int) pos.x - 1, (int) pos.y - 1, guiHelper));
		}
		for (Vec2f pos : getOutputSlotLocations()) {
			drawables.add(JEIHelper.createSlot((int) pos.x - 1, (int) pos.y - 1, guiHelper));
		}
	}

}