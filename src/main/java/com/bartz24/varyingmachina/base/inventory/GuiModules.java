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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class GuiModules extends GuiBase {

	private EntityPlayer player;
	private TileCasing tile;
	private GuiButton moduleButton, leftButton, rightButton;
	public int selectedModule = -1;

	public GuiModules(EntityPlayer player, TileCasing te, int moduleIndex) {
		super(new ContainerModules(player, te, moduleIndex), te);
		selectedModule = moduleIndex - 1;
		this.player = player;
		this.tile = te;

		this.xSize = te.machineStored.isEmpty() ? 176 : te.getMachine().getGuiSize(te.machineStored)[0];
		this.ySize = te.machineStored.isEmpty() ? 166 : te.getMachine().getGuiSize(te.machineStored)[0];
	}

	public void initGui() {
		super.initGui();
		moduleButton = addButton(new GuiModuleButton(0, this.guiLeft + 150, this.guiTop + 40, itemRender));
		leftButton = addButton(new GuiButton(1, guiLeft + 40, guiTop + 19, 20, 20, "<"));
		rightButton = addButton(new GuiButton(2, guiLeft + 116, guiTop + 19, 20, 20, ">"));
		if (selectedModule >= 0 && !tile.modules.getStackInSlot(selectedModule).isEmpty()) {
			tile.getModule(EnumFacing.values()[selectedModule]).initGui(this, buttonList, tile);
		}
	}

	protected void updateComponents() {
		super.updateComponents();
		if (selectedModule >= 0 && !tile.modules.getStackInSlot(selectedModule).isEmpty()) {
			tile.getModule(EnumFacing.values()[selectedModule]).updateGuiComps(this, buttonList, tile);
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if (button == moduleButton) {
			VaryingMachinaPacketHandler.instance.sendToServer(new OpenGUIMessage(0, tile.getPos()));
		} else if (button == leftButton) {
			changeModule(selectedModule - 1);
		} else if (button == rightButton) {
			changeModule(selectedModule + 1);
		}
		if (selectedModule >= 0 && !tile.modules.getStackInSlot(selectedModule).isEmpty()) {
			tile.getModule(EnumFacing.values()[selectedModule]).actionPerformed(tile, this, button.id);
		}
	}

	private void changeModule(int moduleSlot) {
		if (moduleSlot < -1)
			moduleSlot = 5;
		else if (moduleSlot > 5)
			moduleSlot = -1;
		VaryingMachinaPacketHandler.instance.sendToServer(new OpenGUIMessage(2 + moduleSlot, tile.getPos()));
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
		if (selectedModule >= 0 && !tile.modules.getStackInSlot(selectedModule).isEmpty()) {
			tile.getModule(EnumFacing.values()[selectedModule]).drawBackgroundGui(tile, this, fontRenderer, mouseX,
					mouseY);
		}
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		IBlockState state = tile.getWorld().getBlockState(tile.getPos());
		this.fontRenderer.drawString(
				MachineVariant.writeVariantToStack(new ItemStack(ModBlocks.casing), tile.getVariant()).getDisplayName(),
				8, 3, 4210752);
		this.fontRenderer.drawString(tile.machineStored.getDisplayName(), 8, 11, 4210752);
		this.fontRenderer.drawString(this.player.inventory.getDisplayName().getUnformattedText(), 8,
				72 + (tile.machineStored.isEmpty() ? 0 : tile.getMachine().getInvPos(tile.machineStored)[1]), 4210752);
		if (selectedModule >= 0)
			this.fontRenderer.drawString(EnumFacing.values()[selectedModule].getName().toUpperCase(), 8, 24, 4210752);
		if (selectedModule >= 0 && !tile.modules.getStackInSlot(selectedModule).isEmpty()) {
			tile.getModule(EnumFacing.values()[selectedModule]).drawForegroundGui(tile, this, fontRenderer, mouseX,
					mouseY);
		}
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

	}
}