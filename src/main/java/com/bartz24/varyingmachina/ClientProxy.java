package com.bartz24.varyingmachina;

import com.bartz24.varyingmachina.item.ItemBlockMachine;
import com.bartz24.varyingmachina.machine.ModMachines;
import com.bartz24.varyingmachina.tile.TileEntityMachine;
import com.emosewapixel.pixellib.materialsystem.lists.MaterialItems;
import com.emosewapixel.pixellib.materialsystem.main.IMaterialObject;
import com.emosewapixel.pixellib.proxy.IModProxy;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.*;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;

import java.util.Arrays;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientProxy implements IModProxy {
    @Override
    public void enque(InterModEnqueueEvent interModEnqueueEvent) {

        for (Item item : MaterialItems.getAll()) {
            if (item instanceof TieredItem)
                Minecraft.getInstance().getItemColors().register((ItemStack stack, int index) -> {
                    Item sItem = stack.getItem();
                    if (sItem instanceof IMaterialObject && index == 1)
                        return ((IMaterialObject) sItem).getMat().getColor();
                    return -1;
                }, item);
        }

        ModMachines.reserve.setRenderTER(reserveTER);

        VMKeyBindings.setup();
    }

    @Override
    public void init() {

    }

    @Override
    public void process(InterModProcessEvent interModProcessEvent) {

    }

    private static Consumer<TileEntityMachine> reserveTER = tile -> {

        if (tile.getItemHandler().getSlots() > 0) {
            GlStateManager.pushMatrix();
            GlStateManager.rotated(90 * tile.getDirection().getHorizontalIndex() + 180, 0, 1, 0);
            if (tile.getDirection().getAxis() == Direction.Axis.Z)
                GlStateManager.rotated(180, 0, 1, 0);
            GlStateManager.translated(-.325, .275, .45);
            GlStateManager.scaled(0.04, 0.04, -1 / 512);
            GlStateManager.rotated(180, 0, 0, 1);
            GlStateManager.rotated(180, 0, 1, 0);

            RenderHelper.enableGUIStandardItemLighting();

            /*
            GlStateManager.enableRescaleNormal();
            GlStateManager.disableRescaleNormal();
            GlStateManager.pushLightingAttrib();
            GlStateManager.enableRescaleNormal();
            GlStateManager.popAttrib();

            Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(tile.getItemHandler().getStackInSlot(0), 0, 0);
*/

            GlStateManager.pushMatrix();
            Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
            Minecraft.getInstance().textureManager.getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
            GlStateManager.disableRescaleNormal();
            GlStateManager.enableAlphaTest();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.translatef(0, 0, 100.0F + Minecraft.getInstance().getItemRenderer().zLevel);
            GlStateManager.translatef(8.0F, 8.0F, 0.0F);
            GlStateManager.scalef(1.0F, -1.0F, 1.0F);
            GlStateManager.scalef(16.0F, 16.0F, 16.0F);
            IBakedModel bakedModel = Minecraft.getInstance().getItemRenderer().getModelWithOverrides(tile.getItemHandler().getStackInSlot(0));
            if (bakedModel.isGui3d()) {
                GlStateManager.disableLighting();
            } else {
                GlStateManager.disableLighting();
            }
            bakedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(bakedModel, ItemCameraTransforms.TransformType.GUI, false);
            Minecraft.getInstance().getItemRenderer().renderItem(tile.getItemHandler().getStackInSlot(0), bakedModel);
            GlStateManager.disableAlphaTest();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableLighting();
            GlStateManager.popMatrix();
            Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
            Minecraft.getInstance().textureManager.getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();


            GlStateManager.scaled(0.25, 0.25, 1);
            String disp = tile.getItemHandler().getStackInSlot(0).getCount() + "";
            Minecraft.getInstance().fontRenderer.drawString(disp, 67 - Minecraft.getInstance().fontRenderer.getStringWidth(disp), -6, 0xFFFFFF);
            RenderHelper.enableStandardItemLighting();
            GlStateManager.popMatrix();
        }
    };

    @SubscribeEvent
    public static void renderMultiblockPreview(DrawBlockHighlightEvent event) {
        if (event.getSubID() == 0 && event.getTarget().getType() == RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult rayTraceResult = (BlockRayTraceResult) event.getTarget();
            ClientPlayerEntity player = Minecraft.getInstance().player;
            if (player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof ItemBlockMachine) {
                ItemBlockMachine item = (ItemBlockMachine) player.getHeldItem(Hand.MAIN_HAND).getItem();
                if (ModMachines.types.get(item.getBlockMachine().getMachineType()).isMultiblock()) {

                    int size = item.getMultiblockSize();

                    GlStateManager.pushMatrix();
                    AxisAlignedBB boundingBox = new AxisAlignedBB(-size, 0, -size, size + 1, size * 2 + 1, size + 1);

                    BlockPos pos = rayTraceResult.getPos();
                    if (!player.world.getBlockState(rayTraceResult.getPos()).isReplaceable(new BlockItemUseContext(new ItemUseContext(player, Hand.MAIN_HAND, rayTraceResult))))
                        pos = pos.offset(rayTraceResult.getFace());

                    pos = pos.offset(Arrays.stream(Direction.getFacingDirections(player)).filter(dir -> dir.getHorizontalIndex() != -1).findFirst().get(), size);


                    GlStateManager.disableTexture();
                    GlStateManager.enableBlend();
                    GlStateManager.disableDepthTest();
                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder bufferbuilder = tessellator.getBuffer();
                    int color = isValid(rayTraceResult, player, boundingBox, pos) ? 0x16f246 : 0xff1212;
                    int r = (color >> 8 >> 8) & 0xFF;
                    int g = (color >> 8) & 0xFF;
                    int b = color & 0xFF;
                    GlStateManager.color4f(r / 255f, g / 255f, b / 255f, 0.5f);
                    bufferbuilder.setTranslation(pos.getX() - TileEntityRendererDispatcher.staticPlayerX, pos.getY() - TileEntityRendererDispatcher.staticPlayerY, pos.getZ() - TileEntityRendererDispatcher.staticPlayerZ);
                    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_NORMAL);
                    bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
                    bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
                    bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
                    bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();

                    bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
                    bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
                    bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
                    bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();

                    bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(0.0F, -1.0F, 0.0F).endVertex();
                    bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(0.0F, -1.0F, 0.0F).endVertex();
                    bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(0.0F, -1.0F, 0.0F).endVertex();
                    bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(0.0F, -1.0F, 0.0F).endVertex();

                    bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(0.0F, 1.0F, 0.0F).endVertex();
                    bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(0.0F, 1.0F, 0.0F).endVertex();
                    bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(0.0F, 1.0F, 0.0F).endVertex();
                    bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(0.0F, 1.0F, 0.0F).endVertex();

                    bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
                    bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
                    bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
                    bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(-1.0F, 0.0F, 0.0F).endVertex();

                    bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(1.0F, 0.0F, 0.0F).endVertex();
                    bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(1.0F, 0.0F, 0.0F).endVertex();
                    bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(1.0F, 0.0F, 0.0F).endVertex();
                    bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(1.0F, 0.0F, 0.0F).endVertex();
                    tessellator.draw();
                    bufferbuilder.setTranslation(0.0D, 0.0D, 0.0D);
                    GlStateManager.disableBlend();
                    GlStateManager.enableDepthTest();
                    GlStateManager.enableTexture();
                    GlStateManager.popMatrix();
                }
            }
        }
    }

    private static boolean isValid(BlockRayTraceResult rayTraceResult, ClientPlayerEntity player, AxisAlignedBB boundingBox, BlockPos pos) {
        for (int x = pos.getX() + (int) boundingBox.minX; x < pos.getX() + boundingBox.maxX; x++) {
            for (int y = pos.getY() + (int) boundingBox.minY; y < pos.getY() + boundingBox.maxY; y++) {
                for (int z = pos.getZ() + (int) boundingBox.minZ; z < pos.getZ() + boundingBox.maxZ; z++) {
                    if (!player.world.getBlockState(new BlockPos(x, y, z)).isReplaceable(new BlockItemUseContext(new ItemUseContext(player, Hand.MAIN_HAND, rayTraceResult)))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
