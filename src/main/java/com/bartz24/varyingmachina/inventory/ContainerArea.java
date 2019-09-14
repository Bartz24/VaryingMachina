package com.bartz24.varyingmachina.inventory;

import com.bartz24.varyingmachina.gui.GuiMachine;
import com.bartz24.varyingmachina.tile.TileEntityMachine;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class ContainerArea {

    public enum XAnchorDirection {
        LEFT,
        CENTER,
        RIGHT
    }

    public enum YAnchorDirection {
        TOP,
        CENTER,
        BOTTOM
    }

    protected List<Slot> slots;

    protected int x, y, weight;

    protected XAnchorDirection xDir;
    protected YAnchorDirection yDir;

    public ContainerArea(XAnchorDirection xDirection, YAnchorDirection yDirection, int weight) {
        slots = new ArrayList<>();
        x = y = 0;
        xDir = xDirection;
        yDir = yDirection;
        this.weight = weight;
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        int width = -1;
        for (Slot s : slots) {
            if (s.xPos - x > width)
                width = s.xPos - x;
        }
        return width + 18;
    }

    public int getHeight() {
        int height = -1;
        for (Slot s : slots) {
            if (s.yPos - y > height)
                height = s.yPos - y;
        }
        return height + 18;
    }

    public int getSize() {
        return slots.size();
    }

    public Slot getSlot(int i) {
        return slots.get(i);
    }

    public XAnchorDirection getXDirection() {
        return xDir;
    }

    public YAnchorDirection getYDirection() {
        return yDir;
    }

    public int compareTo(ContainerArea a2) {
        int yCompare = getYDirection().compareTo(a2.getYDirection());
        if (yCompare != 0)
            return yCompare;

        return getXDirection().compareTo(a2.getXDirection());
    }

    @OnlyIn(Dist.CLIENT)
    public void drawBackground(GuiMachine gui) {
        for (Slot s : slots) {
            gui.blit(s.xPos + gui.getGuiLeft() - 1, s.yPos + gui.getGuiTop() - 1, 59, 60, 18, 18);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void drawForeground(GuiMachine gui, int mouseX, int mouseY) {

    }

    public void drawJEI(IGuiHelper helper, int leftX, int leftY) {

    }

    public int getWeight() {
        return weight;
    }

    public void update(TileEntityMachine tile) {

    }

    public void update(Object... data) {

    }

    public boolean mouseClicked(GuiMachine gui, double mouseX, double mouseY, int clickType) {
        return false;
    }
}
