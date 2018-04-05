package com.bartz24.varyingmachina.network;

import com.bartz24.varyingmachina.VaryingMachina;
import com.bartz24.varyingmachina.base.tile.TileCasing;
import com.bartz24.varyingmachina.machines.MachinePresser;
import com.bartz24.varyingmachina.machines.recipes.PresserProcessRecipe.PresserPattern;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PresserPatternMessage implements IMessage {
	public PresserPatternMessage() {
	}

	public PresserPatternMessage(int patternX, int patternY, BlockPos machinePos) {
		this.patternX = patternX;
		this.patternY = patternY;
		x = machinePos.getX();
		y = machinePos.getY();
		z = machinePos.getZ();
	}

	public int patternX, patternY;
	public int x;
	public int y;
	public int z;

	@Override
	public void fromBytes(ByteBuf buf) {
		patternX = buf.readInt();
		patternY = buf.readInt();
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(patternX);
		buf.writeInt(patternY);
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}

	public BlockPos getPos() {
		return new BlockPos(x, y, z);
	}

	public static class PresserPatternMessageHandler implements IMessageHandler<PresserPatternMessage, IMessage> {
		@Override
		public IMessage onMessage(PresserPatternMessage message, MessageContext ctx) {
			WorldServer world = ctx.getServerHandler().player.getServerWorld();
			if (!world.isBlockLoaded(message.getPos()))
				return null;
			
			TileCasing casing = (TileCasing) world.getTileEntity(message.getPos());
			MachinePresser presser = (MachinePresser) casing.getMachine();
			PresserPattern pattern = presser.getPattern(casing.machineData);
			int cur = pattern.pattern[message.patternY][message.patternX];
			pattern.pattern[message.patternY][message.patternX] = cur == 1 ? 0 : 1;
			((MachinePresser) casing.getMachine()).writePattern(casing.machineData, pattern);

			return null;
		}

	}
}