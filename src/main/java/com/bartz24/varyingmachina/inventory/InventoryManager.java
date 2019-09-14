package com.bartz24.varyingmachina.inventory;

import net.minecraft.inventory.container.Slot;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    protected List<ContainerArea> areas = new ArrayList<>();
    private int padding = 10;
    private int topPadding = 16;

    public InventoryManager(int padding) {
        topPadding = padding;
    }

    public List<ContainerArea> getAreas() {
        return areas;
    }

    public void addArea(ContainerArea area) {
        areas.add(area);
    }

    public void determineLayout() {

        areas.sort((a1, a2) -> Integer.compare(a1.getWeight(), a2.getWeight()));

        alignX();
        alignY();
    }

    private void alignX() {
        int maxWidth = getMaxWidth();

        for (ContainerArea.YAnchorDirection yDir : ContainerArea.YAnchorDirection.values()) {
            for (ContainerArea.XAnchorDirection xDir : ContainerArea.XAnchorDirection.values()) {
                int x;
                if (xDir == ContainerArea.XAnchorDirection.LEFT)
                    x = padding;
                else if (xDir == ContainerArea.XAnchorDirection.CENTER)
                    x = maxWidth / 2 - getWidthOfAreas(xDir, yDir) / 2;
                else
                    x = maxWidth - getWidthOfAreas(xDir, yDir) - padding;
                for (ContainerArea a : areas) {
                    if (a.getXDirection() != xDir || a.getYDirection() != yDir)
                        continue;
                    int xPos = x + 0, yPos = a.getY() + 0;
                    if (xDir == ContainerArea.XAnchorDirection.RIGHT)
                        x -= a.getWidth() + padding;
                    else
                        x += a.getWidth() + padding;
                    a.setPos(xPos, yPos);
                }
            }
        }
    }

    public int getMaxWidth() {
        int maxWidth = 0;

        for (ContainerArea.YAnchorDirection yDir : ContainerArea.YAnchorDirection.values()) {
            int sectionWidth = padding;
            for (ContainerArea.XAnchorDirection xDir : ContainerArea.XAnchorDirection.values()) {
                sectionWidth += getWidthOfAreas(xDir, yDir);
                if (getWidthOfAreas(xDir, yDir) > 0)
                    sectionWidth += padding;
                if (sectionWidth > maxWidth)
                    maxWidth = sectionWidth;
            }
        }
        return maxWidth;
    }

    private int getWidthOfAreas(ContainerArea.XAnchorDirection xDir, ContainerArea.YAnchorDirection yDir) {
        int width = 0;
        for (ContainerArea a : areas) {
            if (a.getXDirection() == xDir && a.getYDirection() == yDir)
                width += a.getWidth() + padding;
        }
        if (width == 0)
            return 0;
        return width - padding;
    }

    private void alignY() {
        int maxHeight = getMaxHeight();

        for (ContainerArea.YAnchorDirection yDir : ContainerArea.YAnchorDirection.values()) {
            int y;
            if (yDir == ContainerArea.YAnchorDirection.TOP)
                y = padding + getHeightOfAreas(yDir) / 2 + topPadding;
            else if (yDir == ContainerArea.YAnchorDirection.CENTER)
                y = maxHeight / 2 + topPadding;
            else
                y = maxHeight - getHeightOfAreas(yDir) / 2 - padding;
            for (ContainerArea a : areas) {
                if (a.getYDirection() != yDir)
                    continue;
                a.setPos(a.getX(), y - a.getHeight() / 2);
            }
        }
    }

    public int getMaxHeight() {
        int maxHeight = padding;

        for (ContainerArea.YAnchorDirection yDir : ContainerArea.YAnchorDirection.values()) {
            maxHeight += getHeightOfAreas(yDir);
            if (getHeightOfAreas(yDir) > 0)
                maxHeight += padding;
        }
        return maxHeight + topPadding;
    }

    private int getHeightOfAreas(ContainerArea.YAnchorDirection yDir) {
        int height = 0;
        for (ContainerArea a : areas) {
            if (a.getYDirection() == yDir && a.getHeight() > height)
                height = a.getHeight();
        }
        return height;
    }

    public Slot getSlot(int index) {
        for (ContainerArea area : areas) {
            if (index >= area.slots.size()) {
                index -= area.slots.size();
            } else {
                return area.getSlot(index);
            }

        }
        return null;
    }
}
