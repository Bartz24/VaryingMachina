package com.bartz24.varyingmachina.jei;

import com.bartz24.varyingmachina.tile.TileEntityMachine;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSetMachineRecipe {

    public BlockPos pos;
    public ResourceLocation recipe;

    public PacketSetMachineRecipe(BlockPos pos, ResourceLocation recipe) {
        this.pos = pos;
        this.recipe = recipe;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeResourceLocation(recipe);
    }

    public static PacketSetMachineRecipe decode(PacketBuffer buffer) {
        return new PacketSetMachineRecipe(buffer.readBlockPos(), buffer.readResourceLocation());
    }

    public static void processPacket(PacketSetMachineRecipe msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            TileEntityMachine tile = (TileEntityMachine) player.world.getTileEntity(msg.pos);
            tile.changeRecipe(player, msg.recipe);
        });

        ctx.get().setPacketHandled(true);
    }
}
