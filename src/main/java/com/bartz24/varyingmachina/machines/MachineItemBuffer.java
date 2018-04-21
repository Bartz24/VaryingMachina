package com.bartz24.varyingmachina.machines;

import com.bartz24.varyingmachina.References;
import com.bartz24.varyingmachina.base.inventory.GuiArrowProgress;
import com.bartz24.varyingmachina.base.inventory.GuiCasing;
import com.bartz24.varyingmachina.base.inventory.GuiHeatBar;
import com.bartz24.varyingmachina.base.inventory.GuiStatsComp;
import com.bartz24.varyingmachina.base.item.ItemMachine;
import com.bartz24.varyingmachina.base.machine.MachineStat;
import com.bartz24.varyingmachina.base.recipe.ProcessRecipe;
import com.bartz24.varyingmachina.base.recipe.RecipeItem;
import com.bartz24.varyingmachina.base.tile.TileCasing;
import com.bartz24.varyingmachina.machines.recipes.SmelterRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MachineItemBuffer extends ItemMachine {

    public MachineItemBuffer() {
        super("itembuffer", MachineStat.SIZE);
    }


    public void update(World world, BlockPos pos, ItemStack machineStored, NBTTagCompound machineData) {
    }

    public int getInputItemSlots(TileCasing casing) {
        return (int) getCombinedStat(MachineStat.SIZE, casing.machineStored, casing.getWorld(), casing.getPos()) + super.getInputItemSlots(casing);
    }

    public List<String> getInputItemNames(TileCasing casing) {
        List<String> names = new ArrayList();
        for (int i = 1; i <= (int) getCombinedStat(MachineStat.SIZE, casing.machineStored, casing.getWorld(), casing.getPos()) + 1; i++)
            names.add("Input " + i);
        names.addAll(super.getInputItemNames(casing));
        return names;
    }

    public List<Slot> getSlots(TileCasing tile, List<Slot> slots) {
        for (int i = 0; i < (int) getCombinedStat(MachineStat.SIZE, tile.machineStored, tile.getWorld(), tile.getPos()); i++) {
            int y = i / 12;
            int x = i % 12;
            slots.add(new SlotItemHandler(getInputInventory(tile), i, 8 + 18 * x, 20 + 18 * y));
        }
        return super.getSlots(tile, slots);
    }

    public ItemStackHandler getOutputInventory(TileCasing casing) {
        return casing.getInputInventory();
    }

    public int[] getInvPos(ItemStack stack) {
        return new int[]{35, 76};
    }

    public int[] getGuiSize(ItemStack stack) {
        return new int[]{246, 242};
    }


    public void drawBackgroundGui(TileCasing tile, GuiCasing gui, FontRenderer fontRenderer, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager()
                .bindTexture(new ResourceLocation(References.ModID, "textures/gui/blankLargeInventory.png"));
        gui.drawTexturedModalRect(gui.getGuiLeft(), gui.getGuiTop(), 0, 0, gui.getXSize(), gui.getYSize());

        Minecraft.getMinecraft().getTextureManager()
                .bindTexture(new ResourceLocation(References.ModID, "textures/gui/guiicons.png"));

        for (Slot s : gui.inventorySlots.inventorySlots) {
            gui.drawTexturedModalRect(gui.getGuiLeft() + s.xPos - 1, gui.getGuiTop() + s.yPos - 1, 59, 60, 18, 18);
        }
    }


    @SideOnly(Side.CLIENT)
    public void initGui(GuiCasing gui, List buttonList, TileCasing casing) {
        gui.addComponent("stats", new GuiStatsComp(225, 25, getCombinedStats(), casing));
    }

    @SideOnly(Side.CLIENT)
    public void updateGuiComps(GuiCasing gui, List buttonList, TileCasing casing) {
        gui.updateComponent("stats", getCombinedStats(), casing);
    }
}
