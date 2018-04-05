package com.bartz24.varyingmachina.base.inventory;

import java.io.IOException;

import com.bartz24.varyingmachina.References;
import com.bartz24.varyingmachina.base.machine.MachineVariant;
import com.bartz24.varyingmachina.base.tile.TileCasing;
import com.bartz24.varyingmachina.network.OpenGUIMessage;
import com.bartz24.varyingmachina.network.VaryingMachinaPacketHandler;
import com.bartz24.varyingmachina.registry.ModBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiModules extends GuiBase {

	private EntityPlayer player;
	private TileCasing tile;
	private GuiButton moduleButton;

	public GuiModules(EntityPlayer player, TileCasing te) {
		super(new ContainerModules(player, te), te);

		this.player = player;
		this.tile = te;

		this.xSize = te.machineStored.isEmpty() ? 176 : te.getMachine().getGuiSize(te.machineStored)[0];
		this.ySize = te.machineStored.isEmpty() ? 166 : te.getMachine().getGuiSize(te.machineStored)[0];
	}

	public void initGui() {
		super.initGui();
		moduleButton = addButton(new GuiModuleButton(0, this.guiLeft + 150, this.guiTop + 40, itemRender));
	}

	protected void updateComponents() {
		super.updateComponents();
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if (button == moduleButton) {
			VaryingMachinaPacketHandler.instance.sendToServer(new OpenGUIMessage(0, tile.getPos()));
		}
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		Minecraft.getMinecraft().getTextureManager()
				.bindTexture(new ResourceLocation(References.ModID, "textures/gui/blankInventory.png"));
		drawTexturedModalRect(getGuiLeft(), getGuiTop(), 0, 0, getXSize(), getYSize());

		Minecraft.getMinecraft().getTextureManager()
				.bindTexture(new ResourceLocation(References.ModID, "textures/gui/guiicons.png"));

		for (Slot s : inventorySlots.inventorySlots) {
			drawTexturedModalRect(getGuiLeft() + s.xPos - 1, getGuiTop() + s.yPos - 1, 59, 60, 18, 18);
		}
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		IBlockState state = tile.getWorld().getBlockState(tile.getPos());
		this.fontRenderer.drawString(
				MachineVariant.writeVariantToStack(new ItemStack(ModBlocks.casing), tile.getVariant()).getDisplayName(),
				8, 6, 4210752);
		this.fontRenderer.drawString(tile.machineStored.getDisplayName(), 8, 14, 4210752);
		this.fontRenderer.drawString(this.player.inventory.getDisplayName().getUnformattedText(), 8,
				72 + (tile.machineStored.isEmpty() ? 0 : tile.getMachine().getInvPos(tile.machineStored)[1]), 4210752);
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

	}
}