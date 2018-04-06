package com.bartz24.varyingmachina.network;

import com.bartz24.varyingmachina.References;
import com.bartz24.varyingmachina.network.ModuleDataMessage.ModuleDataMessageHandler;
import com.bartz24.varyingmachina.network.OpenGUIMessage.OpenGUIMessageHandler;
import com.bartz24.varyingmachina.network.PresserPatternMessage.PresserPatternMessageHandler;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class VaryingMachinaPacketHandler {

	public static final SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(References.ModID);

	public static void preInit() {
		instance.registerMessage(OpenGUIMessageHandler.class, OpenGUIMessage.class, 0, Side.SERVER);
		instance.registerMessage(PresserPatternMessageHandler.class, PresserPatternMessage.class, 1, Side.SERVER);
		instance.registerMessage(ModuleDataMessageHandler.class, ModuleDataMessage.class, 2, Side.SERVER);
	}
}
