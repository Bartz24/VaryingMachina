package com.bartz24.varyingmachina.inventory;

import com.bartz24.varyingmachina.gui.GuiMachine;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AreaPlayerInventory extends ContainerArea {
    PlayerEntity player;

    public AreaPlayerInventory(PlayerEntity player) {
        super(XAnchorDirection.CENTER, YAnchorDirection.BOTTOM, 9999);

        for (int x = 0; x < 9; ++x) {
            slots.add(new Slot(player.inventory, x, x * 18, 58 + 12));
        }

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                slots.add(new Slot(player.inventory, x + y * 9 + 9, x * 18, y * 18 + 12));
            }
        }

        this.player = player;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(GuiMachine gui, int mouseX, int mouseY) {
        super.drawForeground(gui, mouseX, mouseY);

        Minecraft.getInstance().fontRenderer.drawString(player.inventory.getDisplayName().getFormattedText(), x, y, 4210752);
    }
}
