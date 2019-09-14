package com.bartz24.varyingmachina.gui;

import com.bartz24.varyingmachina.Helpers;
import com.bartz24.varyingmachina.container.ContainerMachine;
import com.bartz24.varyingmachina.inventory.ContainerArea;
import com.bartz24.varyingmachina.inventory.SlotFiltered;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiMachine extends ContainerScreen<ContainerMachine> {

    private int tick = 0;
    private ResourceLocation startingRecipe;
    private double scale = 1;

    public GuiMachine(ContainerMachine container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.xSize = container.getMaxWidth();
        this.ySize = container.getMaxHeight();
        startingRecipe = container.tile.getCurrentRecipe();
    }

    private void scaleDown() {
        if (xSize > 0.6 * width || ySize > 0.9 * height) {
            scale = 1 / Math.max(xSize / (0.6 * width), ySize / (0.9 * height));
            //GlStateManager.scaled(scale, scale, 1);
        }
    }

    private void scaleUp() {
        //GlStateManager.scaled(1 / scale, 1 / scale, 1);
    }

    @Override
    protected void init() {
        super.init();
        int buttonX = xSize - 5 - 20 + guiLeft;
        if (!container.tile.getMachine().isNoRecipes()) {
            if (container.tile.getRecipe() == null)
                addButton(new GuiOpenJEIButton(buttons.size(), xSize / 2 - 5 + guiLeft, 32 + guiTop, new ResourceLocation("varyingmachina", container.tile.getMachineType()).toString()));
            else {
                addButton(new GuiOpenJEIButton(buttons.size(), buttonX, 4 + guiTop, new ResourceLocation("varyingmachina", container.tile.getMachineType()).toString()));
                buttonX -= 20;
            }
        }
        if (container.tile.getMachine().getDisplayRange() != null)
            addButton(new GuiDisplayRange(buttons.size(), buttonX, 4 + guiTop, container.tile));

    }


    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        minecraft.getTextureManager().bindTexture(new ResourceLocation("varyingmachina", "textures/gui/blankinventory.png"));
        //corners
        blit(guiLeft, guiTop, 0, 0, 16, 16);
        blit(guiLeft + xSize - 16, guiTop, 175 - 16, 0, 16, 16);
        blit(guiLeft + xSize - 16, guiTop + ySize - 16, 175 - 16, 165 - 16, 16, 16);
        blit(guiLeft, guiTop + ySize - 16, 0, 165 - 16, 16, 16);

        //edges
        blit(guiLeft + 16, guiTop, xSize - 32, 16, 16, 0, 175 - 32, 16, 256, 256);
        blit(guiLeft + 16, guiTop + ySize - 16, xSize - 32, 16, 16, 165 - 16, 175 - 32, 16, 256, 256);
        blit(guiLeft, guiTop + 16, 16, ySize - 32, 0, 16, 16, 165 - 32, 256, 256);
        blit(guiLeft + xSize - 16, guiTop + 16, 16, ySize - 32, 175 - 16, 16, 16, 165 - 32, 256, 256);

        //center
        blit(guiLeft + 16, guiTop + 16, xSize - 32, ySize - 32, 16, 16, 175 - 32, 165 - 32, 256, 256);

        minecraft.getTextureManager().bindTexture(new ResourceLocation("varyingmachina", "textures/gui/guiicons.png"));
        for (ContainerArea a : container.getAreas()) {
            a.drawBackground(this);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        Helpers.drawCompressedString(font, container.tile.getDisplayName().getFormattedText(), xSize / 2, 6, true, (int) (0.6 * xSize), 4210752);

        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        minecraft.getTextureManager().bindTexture(new ResourceLocation("varyingmachina", "textures/gui/guiicons.png"));
        blit(xSize - 14, 4, 30, 104, 10, 10);

        if (container.tile.getRecipe() == null && !container.tile.getMachine().isNoRecipes()) {
            String emptyDisp = new TranslationTextComponent("varyingmachina.selectrecipe").getFormattedText();
            font.drawString(emptyDisp, xSize / 2 - font.getStringWidth(emptyDisp) / 2, 20, 4210752);
        }

        for (ContainerArea a : container.getAreas()) {
            a.drawForeground(this, mouseX, mouseY);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        tick++;
        if (tick >= 100000)
            tick = 0;
        renderBackground();
        scaleDown();
        if (container.tile.getCurrentRecipe() == null) {
            if (startingRecipe != null)
                this.onClose();
        } else if (!container.tile.getCurrentRecipe().equals(startingRecipe))
            this.onClose();
        if (container.tile.getRecipe() != null || container.tile.getMachine().isNoRecipes()) {
            for (ContainerArea a : container.getAreas())
                a.update(container.tile);
        }
        baseRender(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
        scaleUp();
    }

    private void baseRender(int mouseX, int mouseY, float partialTicks) {
        int i = this.guiLeft;
        int j = this.guiTop;
        this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiContainerEvent.DrawBackground(this, mouseX, mouseY));
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepthTest();
        for (int i2 = 0; i2 < this.buttons.size(); ++i2) {
            this.buttons.get(i2).render(mouseX, mouseY, partialTicks);
        }
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float) i, (float) j, 0.0F);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        this.hoveredSlot = null;
        int k = 240;
        int l = 240;
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 240.0F);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        for (int i1 = 0; i1 < this.container.inventorySlots.size(); ++i1) {
            Slot slot = this.container.inventorySlots.get(i1);
            if (slot.isEnabled()) {
                this.drawSlot(slot);
            }

            if (this.isSlotSelected(slot, (double) mouseX, (double) mouseY) && slot.isEnabled()) {
                this.hoveredSlot = slot;
                GlStateManager.disableLighting();
                GlStateManager.disableDepthTest();
                int j1 = slot.xPos;
                int k1 = slot.yPos;
                GlStateManager.colorMask(true, true, true, false);
                int slotColor = this.getSlotColor(i1);
                this.fillGradient(j1, k1, j1 + 16, k1 + 16, slotColor, slotColor);
                GlStateManager.colorMask(true, true, true, true);
                GlStateManager.enableLighting();
                GlStateManager.enableDepthTest();
            }
        }

        RenderHelper.disableStandardItemLighting();
        this.drawGuiContainerForegroundLayer(mouseX, mouseY);
        RenderHelper.enableGUIStandardItemLighting();
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiContainerEvent.DrawForeground(this, mouseX, mouseY));
        PlayerInventory inventoryplayer = this.minecraft.player.inventory;
        ItemStack draggedStack = ObfuscationReflectionHelper.getPrivateValue(ContainerScreen.class, this, "draggedStack");
        ItemStack returningStack = ObfuscationReflectionHelper.getPrivateValue(ContainerScreen.class, this, "returningStack");
        boolean isRightMouseClick = ObfuscationReflectionHelper.getPrivateValue(ContainerScreen.class, this, "isRightMouseClick");
        int dragSplittingRemnant = ObfuscationReflectionHelper.getPrivateValue(ContainerScreen.class, this, "dragSplittingRemnant");
        int touchUpX = ObfuscationReflectionHelper.getPrivateValue(ContainerScreen.class, this, "touchUpX");
        int touchUpY = ObfuscationReflectionHelper.getPrivateValue(ContainerScreen.class, this, "touchUpY");
        long returningStackTime = ObfuscationReflectionHelper.getPrivateValue(ContainerScreen.class, this, "returningStackTime");
        Slot returningStackDestSlot = ObfuscationReflectionHelper.getPrivateValue(ContainerScreen.class, this, "returningStackDestSlot");

        ItemStack itemstack = draggedStack.isEmpty() ? inventoryplayer.getItemStack() : draggedStack;
        if (!itemstack.isEmpty()) {
            int j2 = 8;
            int k2 = draggedStack.isEmpty() ? 8 : 16;
            String s = null;
            if (!draggedStack.isEmpty() && isRightMouseClick) {
                itemstack = itemstack.copy();
                itemstack.setCount(MathHelper.ceil((float) itemstack.getCount() / 2.0F));
            } else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
                itemstack = itemstack.copy();
                itemstack.setCount(dragSplittingRemnant);
                if (itemstack.isEmpty()) {
                    s = "" + TextFormatting.YELLOW + "0";
                }
            }

            this.drawItemStack(itemstack, mouseX - i - 8, mouseY - j - k2, s);
        }

        if (!returningStack.isEmpty()) {
            float f = (float) (Util.milliTime() - returningStackTime) / 100.0F;
            if (f >= 1.0F) {
                f = 1.0F;
                returningStack = ItemStack.EMPTY;
            }

            int l2 = returningStackDestSlot.xPos - touchUpX;
            int i3 = returningStackDestSlot.yPos - touchUpY;
            int l1 = touchUpX + (int) ((float) l2 * f);
            int i2 = touchUpY + (int) ((float) i3 * f);
            this.drawItemStack(returningStack, l1, i2, (String) null);
        }

        ObfuscationReflectionHelper.setPrivateValue(ContainerScreen.class, this, returningStack, "returningStack");

        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
        GlStateManager.enableDepthTest();
        RenderHelper.enableStandardItemLighting();
    }

    private void drawItemStack(ItemStack stack, int x, int y, int z, String altText) {
        ItemStack draggedStack = ObfuscationReflectionHelper.getPrivateValue(ContainerScreen.class, this, "draggedStack");
        GlStateManager.translatef(0.0F, 0.0F, 32.0F);
        this.blitOffset = z;
        this.itemRenderer.zLevel = z;
        net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = this.font;
        this.itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRenderer.renderItemOverlayIntoGUI(font, stack, x, y - (draggedStack.isEmpty() ? 0 : 8), altText);
        this.blitOffset = 0;
        this.itemRenderer.zLevel = 0.0F;
    }

    private void drawItemStack(ItemStack stack, int x, int y, String altText) {
        drawItemStack(stack, x, y, 200, altText);
    }

    private boolean isSlotSelected(Slot slot, double mouseX, double mouseY) {
        return this.isPointInRegion(slot.xPos, slot.yPos, 16, 16, mouseX, mouseY);
    }

    private void drawSlot(Slot slotIn) {
        ItemStack draggedStack = ObfuscationReflectionHelper.getPrivateValue(ContainerScreen.class, this, "draggedStack");
        boolean isRightMouseClick = ObfuscationReflectionHelper.getPrivateValue(ContainerScreen.class, this, "isRightMouseClick");
        int dragSplittingLimit = ObfuscationReflectionHelper.getPrivateValue(ContainerScreen.class, this, "dragSplittingLimit");
        Slot clickedSlot = ObfuscationReflectionHelper.getPrivateValue(ContainerScreen.class, this, "clickedSlot");
        int i = slotIn.xPos;
        int j = slotIn.yPos;
        ItemStack itemstack = slotIn.getStack();
        boolean flag = false;
        boolean flag1 = slotIn == clickedSlot && !draggedStack.isEmpty() && !isRightMouseClick;
        ItemStack itemstack1 = this.minecraft.player.inventory.getItemStack();
        String s = null;
        if (slotIn == clickedSlot && !draggedStack.isEmpty() && isRightMouseClick && !itemstack.isEmpty()) {
            itemstack = itemstack.copy();
            itemstack.setCount(itemstack.getCount() / 2);
        } else if (this.dragSplitting && this.dragSplittingSlots.contains(slotIn) && !itemstack1.isEmpty()) {
            if (this.dragSplittingSlots.size() == 1) {
                return;
            }

            if (Container.canAddItemToSlot(slotIn, itemstack1, true) && this.container.canDragIntoSlot(slotIn)) {
                itemstack = itemstack1.copy();
                flag = true;
                Container.computeStackSize(this.dragSplittingSlots, dragSplittingLimit, itemstack, slotIn.getStack().isEmpty() ? 0 : slotIn.getStack().getCount());
                int k = Math.min(itemstack.getMaxStackSize(), slotIn.getItemStackLimit(itemstack));
                if (itemstack.getCount() > k) {
                    s = TextFormatting.YELLOW.toString() + k;
                    itemstack.setCount(k);
                }
            } else {
                this.dragSplittingSlots.remove(slotIn);
                this.updateDragSplitting();
            }
        }

        this.blitOffset = 100;
        this.itemRenderer.zLevel = 100.0F;
        if (itemstack.isEmpty() && slotIn.isEnabled()) {
            TextureAtlasSprite textureatlassprite = slotIn.getBackgroundSprite();
            if (textureatlassprite != null) {
                GlStateManager.disableLighting();
                this.minecraft.getTextureManager().bindTexture(slotIn.getBackgroundLocation());
                blit(i, j, this.blitOffset, 16, 16, textureatlassprite);
                GlStateManager.enableLighting();
                flag1 = true;
            }
        }

        if (!flag1) {
            if (flag) {
                fill(i, j, i + 16, j + 16, -2130706433);
            }

            GlStateManager.enableDepthTest();
            this.itemRenderer.renderItemAndEffectIntoGUI(this.minecraft.player, itemstack, i, j);
            if (itemstack.getCount() > 99) {
                ItemStack drawStack = itemstack.copy();
                drawStack.setCount(1);
                this.itemRenderer.renderItemOverlayIntoGUI(this.font, drawStack, i, j, s);
                Helpers.drawScaledCount(itemstack.getCount(), i, j);
            } else {
                this.itemRenderer.renderItemOverlayIntoGUI(this.font, itemstack, i, j, s);
            }
        }

        this.itemRenderer.zLevel = 0.0F;
        this.blitOffset = 0;

        if (slotIn instanceof SlotFiltered) {
            SlotFiltered slotFiltered = (SlotFiltered) slotIn;
            if (slotFiltered.getStack().isEmpty() && slotFiltered.getFilters().size() > 0) {
                int xPos = slotFiltered.xPos;
                int yPos = slotFiltered.yPos;
                int stage = (int) ((tick % (slotFiltered.getFilters().size() * 200)) / 200);
                GlStateManager.disableDepthTest();
                this.itemRenderer.renderItemAndEffectIntoGUI(this.minecraft.player, slotFiltered.getFilters().get(stage), i, j);
                GlStateManager.colorMask(true, true, true, false);
                this.fillGradient(xPos, yPos, xPos + 16, yPos + 16, 0x7f7f7f80, 0x7f7f7f80);
                GlStateManager.enableDepthTest();
            }
        }

        this.itemRenderer.zLevel = 0.0F;
        this.blitOffset = 0;
    }

    private void updateDragSplitting() {
        int dragSplittingLimit = ObfuscationReflectionHelper.getPrivateValue(ContainerScreen.class, this, "dragSplittingLimit");
        int dragSplittingRemnant = ObfuscationReflectionHelper.getPrivateValue(ContainerScreen.class, this, "dragSplittingRemnant");
        ItemStack itemstack = this.minecraft.player.inventory.getItemStack();
        if (!itemstack.isEmpty() && this.dragSplitting) {
            if (dragSplittingLimit == 2) {
                dragSplittingRemnant = itemstack.getMaxStackSize();
            } else {
                dragSplittingRemnant = itemstack.getCount();

                for (Slot slot : this.dragSplittingSlots) {
                    ItemStack itemstack1 = itemstack.copy();
                    ItemStack itemstack2 = slot.getStack();
                    int i = itemstack2.isEmpty() ? 0 : itemstack2.getCount();
                    Container.computeStackSize(this.dragSplittingSlots, dragSplittingLimit, itemstack1, i);
                    int j = Math.min(itemstack1.getMaxStackSize(), slot.getItemStackLimit(itemstack1));
                    if (itemstack1.getCount() > j) {
                        itemstack1.setCount(j);
                    }

                    dragSplittingRemnant -= itemstack1.getCount() - i;
                }

            }
        }
        ObfuscationReflectionHelper.setPrivateValue(ContainerScreen.class, this, dragSplittingRemnant, "dragSplittingRemnant");
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY) {
        if (mouseX >= guiLeft + xSize - 14 && mouseX < guiLeft + xSize - 4 && mouseY >= guiTop + 4 && mouseY < guiTop + 14) {
            List<String> list = new ArrayList<>();
            Helpers.getMachineInfo(container.tile.getMachine(), container.tile.getMainMachineVariant(), container.tile.getCasingMachineVariant()).forEach(s -> list.add(s.getFormattedText()));
            this.renderTooltip(list, mouseX, mouseY, font);
        } else if (this.minecraft.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null) {
            if (this.hoveredSlot.getHasStack())
                this.renderTooltip(this.hoveredSlot.getStack(), mouseX, mouseY);
            else if (hoveredSlot instanceof SlotFiltered && ((SlotFiltered) hoveredSlot).getFilters().size() > 0) {
                int stage = (int) ((tick % (((SlotFiltered) hoveredSlot).getFilters().size() * 200)) / 200);
                this.renderTooltip(((SlotFiltered) hoveredSlot).getFilters().get(stage), mouseX, mouseY);
            }

        }
    }

    @Override
    protected void renderTooltip(ItemStack stack, int x, int y) {
        FontRenderer font = stack.getItem().getFontRenderer(stack);
        net.minecraftforge.fml.client.config.GuiUtils.preItemToolTip(stack);
        List<String> list = getTooltipFromItem(stack);
        Slot slot = getSlotUnderMouse();
        if (slot instanceof SlotFiltered)
            list.add(TextFormatting.WHITE + new TranslationTextComponent("varyingmachina.gui.count.total", stack.getCount(), getSlotUnderMouse().getSlotStackLimit()).getFormattedText());
        else
            list.add(TextFormatting.WHITE + new TranslationTextComponent("varyingmachina.gui.count", stack.getCount()).getFormattedText());
        this.renderTooltip(list, x, y, (font == null ? this.font : font));
        net.minecraftforge.fml.client.config.GuiUtils.postItemToolTip();
    }

    public float getZLevel() {
        return blitOffset;
    }

    public ItemStack getDraggedStack() {
        ItemStack draggedStack = ObfuscationReflectionHelper.getPrivateValue(ContainerScreen.class, this, "draggedStack");
        return draggedStack == null ? ItemStack.EMPTY : draggedStack;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int clickType) {
        for (ContainerArea area : container.getAreas()) {
            if (area.mouseClicked(this, mouseX, mouseY, clickType))
                return true;
        }

        return super.mouseClicked(mouseX, mouseY, clickType);
    }
}
