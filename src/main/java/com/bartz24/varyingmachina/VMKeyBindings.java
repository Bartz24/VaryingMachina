package com.bartz24.varyingmachina;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.awt.event.KeyEvent;

public class VMKeyBindings {
    public static KeyBinding guideKey;

    public static void setup() {
        guideKey = new KeyBinding("key.varyingmachina.guide", KeyEvent.VK_G, "varyingmachina");
        ClientRegistry.registerKeyBinding(guideKey);
    }
}
