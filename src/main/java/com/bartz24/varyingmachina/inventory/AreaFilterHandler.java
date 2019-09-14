package com.bartz24.varyingmachina.inventory;

import com.bartz24.varyingmachina.PacketHandler;
import com.bartz24.varyingmachina.gui.GuiMachine;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class AreaFilterHandler extends ContainerArea {
    public AreaFilterHandler(FilterItemHandler handler, XAnchorDirection xDirection, YAnchorDirection yDirection, int weight) {
        super(xDirection, yDirection, weight);

        int maxWidth = (int) Math.ceil(Math.sqrt((double) handler.getSlots()) * 1.3);

        int x = 0, y = 0;
        for (int i = 0; i < handler.getSlots(); i++) {
            slots.add(new FilterSlot(handler, i, x * 18, y * 18));
            x++;
            if (x >= maxWidth) {
                x = 0;
                y++;
            }
        }
    }

    @Override
    public boolean mouseClicked(GuiMachine gui, double mouseX, double mouseY, int clickType) {
        ItemStack stack = Minecraft.getInstance().player.inventory.getItemStack();
        for (Slot slot : slots) {
            if (slot instanceof FilterSlot) {
                if (mouseX >= slot.xPos + gui.getGuiLeft() && mouseX < slot.xPos + gui.getGuiLeft() + 18 && mouseY >= slot.yPos + gui.getGuiTop() && mouseY < slot.yPos + gui.getGuiTop() + 18) {
                    ItemStack put = stack.copy();
                    if (clickType == 1)
                        put = ItemStack.EMPTY;
                    put.setCount(1);
                    slot.putStack(put);
                    PacketHandler.INSTANCE.sendToServer(new PacketSetFilter(gui.getContainer().tile.getPos(), put, slot.getSlotIndex()));
                    return true;
                }
            }
        }
        return super.mouseClicked(gui, mouseX, mouseY, clickType);
    }
}
