package com.bartz24.varyingmachina.network;

import com.bartz24.varyingmachina.VaryingMachina;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class OpenGUIMessage implements IMessage {
	public OpenGUIMessage() {
	}

	public OpenGUIMessage(int guiID, BlockPos machinePos) {
		this.guiID = guiID;
		x = machinePos.getX();
		y = machinePos.getY();
		z = machinePos.getZ();
	}

	public int guiID;
	public int x;
	public int y;
	public int z;

	@Override
	public void fromBytes(ByteBuf buf) {
		guiID = buf.readInt();
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(guiID);
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}

	public BlockPos getPos() {
		return new BlockPos(x, y, z);
	}

	public static class OpenGUIMessageHandler implements IMessageHandler<OpenGUIMessage, IMessage> {
		@Override
		public IMessage onMessage(OpenGUIMessage message, MessageContext ctx) {
			WorldServer world = ctx.getServerHandler().player.getServerWorld();
			if (!world.isBlockLoaded(message.getPos()))
				return null;

			ctx.getServerHandler().player.openGui(VaryingMachina.instance, message.guiID, world, message.x, message.y,
					message.z);

			return null;
		}

	}
}