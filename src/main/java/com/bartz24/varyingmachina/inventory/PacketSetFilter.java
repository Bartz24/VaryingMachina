package com.bartz24.varyingmachina.inventory;

import com.bartz24.varyingmachina.tile.TileEntityMachine;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSetFilter {

    public BlockPos pos;
    public ItemStack stack;
    public int slot;

    public PacketSetFilter(BlockPos pos, ItemStack stack, int slot) {
        this.pos = pos;
        this.slot = slot;
        this.stack = stack.copy();
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeItemStack(stack);
        buffer.writeInt(slot);
    }

    public static PacketSetFilter decode(PacketBuffer buffer) {
        return new PacketSetFilter(buffer.readBlockPos(), buffer.readItemStack(), buffer.readInt());
    }

    public static void processPacket(PacketSetFilter msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            TileEntityMachine tile = (TileEntityMachine) player.world.getTileEntity(msg.pos);
            tile.getItemHandler().getHandler("filter").setStackInSlot(msg.slot, msg.stack);
        });

        ctx.get().setPacketHandled(true);
    }
}
