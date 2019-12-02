package com.bartz24.varyingmachina;

import com.bartz24.varyingmachina.inventory.FuelUnit;
import com.bartz24.varyingmachina.machine.MachineStat;
import com.bartz24.varyingmachina.machine.MachineType;
import com.bartz24.varyingmachina.machine.MachineVariant;
import com.emosewapixel.pixellib.materialsystem.lists.MaterialBlocks;
import com.emosewapixel.pixellib.materialsystem.lists.MaterialItems;
import com.emosewapixel.pixellib.materialsystem.main.Material;
import com.emosewapixel.pixellib.materialsystem.main.ObjectType;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class Helpers {
    public static double round(double val, int digits) {
        return ((int) (val * Math.pow(10, digits))) / Math.pow(10, digits);
    }

    public static void drawTexturedModalRect(float x, float y, float textureX, float textureY, float width, float height, float zLevel) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double) (x + 0), (double) (y + height), (double) zLevel).tex((double) ((float) (textureX + 0) * 0.00390625F), (double) ((float) (textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double) (x + width), (double) (y + height), (double) zLevel).tex((double) ((float) (textureX + width) * 0.00390625F), (double) ((float) (textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double) (x + width), (double) (y + 0), (double) zLevel).tex((double) ((float) (textureX + width) * 0.00390625F), (double) ((float) (textureY + 0) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double) (x + 0), (double) (y + 0), (double) zLevel).tex((double) ((float) (textureX + 0) * 0.00390625F), (double) ((float) (textureY + 0) * 0.00390625F)).endVertex();
        tessellator.draw();
    }

    public static void drawScaledCount(int count, int slotX, int slotY) {
        if (count > 1) {
            float scale = 1;
            if (count > 99)
                scale = 0.75f;
            GlStateManager.scalef(scale, scale, 1);
            GlStateManager.disableDepthTest();
            String s1 = Integer.toString(count);
            Minecraft.getInstance().fontRenderer.drawStringWithShadow(s1, (float) (slotX / scale + 17 / scale - Minecraft.getInstance().fontRenderer.getStringWidth(s1)), (float) (slotY / scale + 11 / scale), 16777215);
            GlStateManager.enableDepthTest();
            GlStateManager.scalef(1 / scale, 1 / scale, 1);
        }
    }

    public static ItemStack createMaterialItemStack(Material material, ObjectType type, int count) {
        if (MaterialItems.get(material, type) != null)
            return new ItemStack(MaterialItems.get(material, type), count);
        return new ItemStack(MaterialBlocks.get(material, type), count);
    }

    public static List<ITextComponent> getMachineInfo(MachineType type, MachineVariant mainVariant, MachineVariant casingVariant) {
        List<ITextComponent> list = new ArrayList<>();
        for (Object statObj : type.getDisplayedStats()) {
            MachineStat stat = (MachineStat) statObj;

            String disp = new TranslationTextComponent("varyingmachina.stat." + stat.getName(), stat.getText(type, mainVariant, casingVariant)).getFormattedText();
            list.add(new StringTextComponent(stat.getTextColor() + disp));
        }
        list.addAll(type.getSpecialTooltips(mainVariant, casingVariant));
        FuelUnit fuelUnit = mainVariant.getFuelUnitSupplier().apply(mainVariant.getFuelUnitSize());
        if (fuelUnit != null && !type.isNoFuel())
            list.addAll(fuelUnit.getTooltips());
        return list;
    }

    public static List<ITextComponent> getToolInfo(IItemTier itemTier) {
        List<ITextComponent> list = new ArrayList<>();

        list.add(new StringTextComponent(TextFormatting.GRAY + new TranslationTextComponent("varyingmachina.harvestlevel", itemTier.getHarvestLevel()).getFormattedText()));
        list.add(new StringTextComponent(TextFormatting.GRAY + new TranslationTextComponent("varyingmachina.durability", itemTier.getMaxUses()).getFormattedText()));
        list.add(new StringTextComponent(TextFormatting.GRAY + new TranslationTextComponent("varyingmachina.speed", Helpers.round(itemTier.getEfficiency(), 2)).getFormattedText()));

        return list;
    }

    public static Direction[] getOrthogonalWithForwardVectors(Direction facing) {
        if (facing.getAxis() == Direction.Axis.X)
            return new Direction[]{Direction.UP, Direction.NORTH, Direction.DOWN, Direction.SOUTH, facing};
        else if (facing.getAxis() == Direction.Axis.Y)
            return new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, facing};
        else
            return new Direction[]{Direction.UP, Direction.EAST, Direction.DOWN, Direction.WEST, facing};

    }

    public static void drawCompressedString(FontRenderer fontRenderer, String string, int x, int y, boolean centeredX, int maxWidth, int color) {

        double scale = 1;
        if (fontRenderer.getStringWidth(string) > maxWidth)
            scale = (double) maxWidth / fontRenderer.getStringWidth(string);
        GlStateManager.scaled(scale, 1, 1);
        if (centeredX)
            fontRenderer.drawString(string, (int) ((x / scale - fontRenderer.getStringWidth(string) / 2)), y, color);
        else
            fontRenderer.drawString(string, (int) (x / scale), y, color);

        GlStateManager.scaled(1 / scale, 1, 1);
    }

    public static String removeTextFormatting(String text) {
        return text.replaceAll("\\u00a7.", "");
    }
}
