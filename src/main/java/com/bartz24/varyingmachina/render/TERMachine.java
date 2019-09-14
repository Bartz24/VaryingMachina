package com.bartz24.varyingmachina.render;

import com.bartz24.varyingmachina.tile.TileEntityMachine;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;

public class TERMachine extends TileEntityRenderer<TileEntityMachine> {
    @Override
    public void render(TileEntityMachine tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage) {
        //super.render(tileEntityIn, x, y, z, partialTicks, destroyStage);

        if (tileEntityIn.getMachine().getRenderTER() != null) {

            GlStateManager.pushMatrix();
            GlStateManager.translated(x + 0.5, y + 0.5, z + 0.5);
            tileEntityIn.getMachine().getRenderTER().accept(tileEntityIn);
            GlStateManager.popMatrix();
        }
    }
}
