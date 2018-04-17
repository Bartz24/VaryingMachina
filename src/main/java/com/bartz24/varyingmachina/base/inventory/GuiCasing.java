package com.bartz24.varyingmachina.base.inventory;

import java.io.IOException;

import com.bartz24.varyingmachina.References;
import com.bartz24.varyingmachina.base.machine.MachineVariant;
import com.bartz24.varyingmachina.base.tile.TileCasing;
import com.bartz24.varyingmachina.network.OpenGUIMessage;
import com.bartz24.varyingmachina.network.VaryingMachinaPacketHandler;
import com.bartz24.varyingmachina.registry.ModBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiCasing extends GuiBase {

	private EntityPlayer player;
	private TileCasing tile;
	private GuiButton moduleButton;

	public GuiCasing(EntityPlayer player, TileCasing te) {
		super(new ContainerCasing(player, te), te);

		this.player = player;
		this.tile = te;

		this.xSize = te.machineStored.isEmpty() ? 176 : te.getMachine().getGuiSize(te.machineStored)[0];
		this.ySize = te.machineStored.isEmpty() ? 166 : te.getMachine().getGuiSize(te.machineStored)[0];
	}

	public void initGui() {
		super.initGui();
		if (!tile.machineStored.isEmpty()) {
			guiComponents.clear();
			tile.getMachine().initGui(this, buttonList, tile);
		}
		moduleButton = addButton(new GuiGoodButton(0, this.guiLeft + xSize - 22, this.guiTop + 40, 12, 12, "",
				new ResourceLocation(References.ModID, "textures/gui/guiicons.png"), 59, 78));
	}

	protected void updateComponents() {
		super.updateComponents();
		if (!tile.machineStored.isEmpty())
			tile.getMachine().updateGuiComps(this, buttonList, tile);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if (button == moduleButton) {
			VaryingMachinaPacketHandler.instance.sendToServer(new OpenGUIMessage(1, tile.getPos()));
		}

		if (!tile.machineStored.isEmpty())
			tile.getMachine().actionPerformed(tile, this, button.id);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		if (!tile.machineStored.isEmpty())
			tile.getMachine().drawBackgroundGui(tile, this, fontRenderer, mouseX, mouseY);
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRenderer.drawString(
				MachineVariant.writeVariantToStack(new ItemStack(ModBlocks.casing), tile.getVariant()).getDisplayName(),
				8, 3, 4210752);
		this.fontRenderer.drawString(tile.machineStored.getDisplayName(), 8, 11, 4210752);
		this.fontRenderer.drawString(this.player.inventory.getDisplayName().getUnformattedText(), 8 + (tile.machineStored.isEmpty() ? 0 : tile.getMachine().getInvPos(tile.machineStored)[0]),
				72 + (tile.machineStored.isEmpty() ? 0 : tile.getMachine().getInvPos(tile.machineStored)[1]), 4210752);
		if (!tile.machineStored.isEmpty())
			tile.getMachine().drawForegroundGui(tile, this, fontRenderer, mouseX, mouseY);
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

	}
}