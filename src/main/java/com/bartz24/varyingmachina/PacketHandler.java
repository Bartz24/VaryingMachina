package com.bartz24.varyingmachina;

import com.bartz24.varyingmachina.inventory.PacketSetFilter;
import com.bartz24.varyingmachina.jei.PacketSetMachineRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("varyingmachina", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void setup() {
        INSTANCE.registerMessage(jeiSetRecipeID, PacketSetMachineRecipe.class, PacketSetMachineRecipe::encode, PacketSetMachineRecipe::decode, PacketSetMachineRecipe::processPacket);
        INSTANCE.registerMessage(filterSetID, PacketSetFilter.class, PacketSetFilter::encode, PacketSetFilter::decode, PacketSetFilter::processPacket);
    }

    public static final int jeiSetRecipeID = 0;
    public static final int filterSetID = 1;
}
