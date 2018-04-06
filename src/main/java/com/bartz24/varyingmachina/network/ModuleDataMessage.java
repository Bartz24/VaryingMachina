package com.bartz24.varyingmachina.network;

import com.bartz24.varyingmachina.VaryingMachina;
import com.bartz24.varyingmachina.base.tile.TileCasing;
import com.bartz24.varyingmachina.machines.MachinePresser;

import io.netty.buffer.ByteBuf;
import io.netty.channel.rxtx.RxtxChannelConfig.Databits;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ModuleDataMessage implements IMessage {
	public ModuleDataMessage() {
	}

	public ModuleDataMessage(EnumFacing side, BlockPos machinePos, String modDataTag, NBTBase modData) {
		NBTTagCompound data = new NBTTagCompound();
		data.setInteger("dataSIDE", side.ordinal());
		data.setInteger("dataX", machinePos.getX());
		data.setInteger("dataY", machinePos.getY());
		data.setInteger("dataZ", machinePos.getZ());
		data.setTag("dataMOD", modData);
		data.setString("dataMODTAG", modDataTag);
		this.data = data;

	}
	
	public NBTTagCompound data;

	@Override
	public void fromBytes(ByteBuf buf) {
		data = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, data);
	}

	public BlockPos getPos() {
		return new BlockPos(data.getInteger("dataX"), data.getInteger("dataY"), data.getInteger("dataZ"));
	}

	public EnumFacing getSide() {
		return EnumFacing.values()[data.getInteger("dataSIDE")];
	}

	public String getModTag() {
		return data.getString("dataMODTAG");
	}

	public static class ModuleDataMessageHandler implements IMessageHandler<ModuleDataMessage, IMessage> {
		@Override
		public IMessage onMessage(ModuleDataMessage message, MessageContext ctx) {
			WorldServer world = ctx.getServerHandler().player.getServerWorld();
			if (!world.isBlockLoaded(message.getPos()))
				return null;

			NBTBase newModData = message.data.getTag("dataMOD");
			TileCasing casing = (TileCasing) world.getTileEntity(message.getPos());
			NBTTagCompound modData = casing.moduleData.get(message.getSide().ordinal());			
			modData.setTag(message.getModTag(), newModData);

			return null;
		}

	}
}