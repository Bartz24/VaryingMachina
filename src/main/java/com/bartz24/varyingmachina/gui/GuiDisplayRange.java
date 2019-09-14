package com.bartz24.varyingmachina.gui;

import com.bartz24.varyingmachina.entity.EntityRange;
import com.bartz24.varyingmachina.tile.TileEntityMachine;
import com.mojang.blaze3d.platform.GlStateManager;
import javafx.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiDisplayRange extends Button {
    private TileEntityMachine tile;

    public GuiDisplayRange(int buttonId, int x, int y, TileEntityMachine tile) {
        super(x, y, 10, 10, "", null);
        this.tile = tile;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.getTextureManager().bindTexture(new ResourceLocation("varyingmachina", "textures/gui/guiicons.png"));
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = this.getYImage(this.isHovered);
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.blit(this.x, this.y, i * 10, 124, this.width, this.height);
            this.renderBg(minecraft, mouseX, mouseY);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (tile.getMachine().getDisplayRange() != null) {
            Pair<BlockPos[], Integer> data = (Pair<BlockPos[], Integer>) tile.getMachine().getDisplayRange().apply(tile);

            for (BlockPos pos : data.getKey()) {
                tile.getWorld().addEntity(EntityRange.createRangeEntity(tile.getWorld(), pos, data.getValue()));
            }
            Minecraft.getInstance().currentScreen.onClose();
        }
    }
}
