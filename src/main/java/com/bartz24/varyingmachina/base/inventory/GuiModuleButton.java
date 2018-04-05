package com.bartz24.varyingmachina.base.inventory;

import com.bartz24.varyingmachina.base.machine.MachineVariant;
import com.bartz24.varyingmachina.registry.ModBlocks;
import com.bartz24.varyingmachina.registry.ModItems;
import com.bartz24.varyingmachina.registry.ModVariants;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GuiModuleButton extends GuiButton {

	private RenderItem itemRender;

	public GuiModuleButton(int buttonId, int x, int y, RenderItem itemRender) {
		super(buttonId, x, y, 20, 20, "");
		this.itemRender = itemRender;
	}

	public void drawButtonForegroundLayer(int mouseX, int mouseY) {
		RenderHelper.enableGUIStandardItemLighting();
		itemRender.renderItemAndEffectIntoGUI(new ItemStack(Blocks.TRIPWIRE_HOOK), x + 2, y + 2);
		RenderHelper.disableStandardItemLighting();
	}
}
