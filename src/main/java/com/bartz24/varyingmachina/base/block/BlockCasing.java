package com.bartz24.varyingmachina.base.block;

import java.util.Random;

import com.bartz24.varyingmachina.References;
import com.bartz24.varyingmachina.VaryingMachina;
import com.bartz24.varyingmachina.base.item.ItemBlockCasing;
import com.bartz24.varyingmachina.base.item.ItemMachine;
import com.bartz24.varyingmachina.base.item.ItemModule;
import com.bartz24.varyingmachina.base.machine.MachineStat;
import com.bartz24.varyingmachina.base.machine.MachineVariant;
import com.bartz24.varyingmachina.base.machine.WrenchHelper;
import com.bartz24.varyingmachina.base.tile.TileCasing;
import com.bartz24.varyingmachina.registry.ModBlocks;
import com.bartz24.varyingmachina.registry.ModGuiHandler;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlockCasing extends BlockMachine {

	public static MachineStat[] stats = new MachineStat[] { MachineStat.EFFICIENCY, MachineStat.PRODUCTION,
			MachineStat.MAXHU };
	public static MachineStat[] displayedStats = new MachineStat[] { MachineStat.EFFICIENCY, MachineStat.MAXHU };

	public BlockCasing() {
		super(Material.GROUND, MapColor.GRAY);
		setHardness(2);
		setResistance(6);
		setCreativeTab(CreativeTabs.MISC);
		setRegistryName(References.ModID, "casing");
		setUnlocalizedName(References.ModID + ".casing");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileCasing();
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(Blocks.AIR);
	}

	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) {
		MachineVariant variant = ((TileCasing) world.getTileEntity(pos)).getVariant();
		return MachineVariant.writeVariantToStack(new ItemStack(this), variant);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {

		if (!world.isRemote) {
			TileCasing casing = (TileCasing) world.getTileEntity(pos);

			if (WrenchHelper.isValidWrench(player.getHeldItem(hand))) {
				if (player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemBlockCasing) {
					MachineVariant variant = casing.getVariant();
					casing.setVariant(
							MachineVariant.readFromNBT(player.getHeldItem(EnumHand.OFF_HAND).getTagCompound()));
					player.getHeldItem(EnumHand.OFF_HAND).shrink(1);
					ItemStack drop = MachineVariant.writeVariantToStack(new ItemStack(ModBlocks.casing), variant);
					if (!player.addItemStackToInventory(drop))
						InventoryHelper.spawnItemStack(world, player.posX, player.posY, player.posZ, drop);
					casing.markDirty();
					return true;
				} else if (player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemMachine) {
					if (!player.addItemStackToInventory(casing.machineStored))
						InventoryHelper.spawnItemStack(world, player.posX, player.posY, player.posZ,
								casing.machineStored);
					ItemStack machine = player.getHeldItem(EnumHand.OFF_HAND).copy();
					machine.setCount(1);
					casing.setMachine(ItemStack.EMPTY);
					casing.setMachine(machine);
					player.getHeldItem(EnumHand.OFF_HAND).shrink(1);
					return true;
				} else if (player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemModule) {
					if (!player.addItemStackToInventory(casing.modules.getStackInSlot(side.getIndex())))
						InventoryHelper.spawnItemStack(world, player.posX, player.posY, player.posZ,
								casing.machineStored);
					ItemStack module = player.getHeldItem(EnumHand.OFF_HAND).copy();
					module.setCount(1);
					casing.setModule(ItemStack.EMPTY, side);
					casing.setModule(module, side);
					player.getHeldItem(EnumHand.OFF_HAND).shrink(1);
					return true;
				}
			}

			if (!super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ)) {
				if (casing.machineStored.isEmpty()) {
					if (player.getHeldItem(hand).getItem() instanceof ItemMachine) {
						ItemStack machine = player.getHeldItem(hand).copy();
						machine.setCount(1);
						casing.setMachine(machine);
						player.getHeldItem(hand).shrink(1);
						return true;
					}
				} else {
					if (!player.isSneaking() && player.getHeldItem(hand).getItem() instanceof ItemModule
							&& casing.modules.getStackInSlot(side.getIndex()).isEmpty()) {
						ItemStack module = player.getHeldItem(hand).copy();
						module.setCount(1);
						casing.setModule(module, side);
						player.getHeldItem(hand).shrink(1);

					} else if (player.isSneaking() && player.getHeldItem(hand).isEmpty()) {

						if (!casing.modules.getStackInSlot(side.getIndex()).isEmpty()) {
							if (!player.addItemStackToInventory(casing.modules.getStackInSlot(side.getIndex())))
								InventoryHelper.spawnItemStack(world, player.posX, player.posY, player.posZ,
										casing.machineStored);
							casing.setModule(ItemStack.EMPTY, side);
						} else {
							if (!player.addItemStackToInventory(casing.machineStored))
								InventoryHelper.spawnItemStack(world, player.posX, player.posY, player.posZ,
										casing.machineStored);
							casing.setMachine(ItemStack.EMPTY);
							for (int i = 0; i < casing.modules.getSlots(); ++i) {
								ItemStack itemstack = casing.modules.getStackInSlot(i);
								InventoryHelper.spawnItemStack(casing.getWorld(), player.posX, player.posY, player.posZ,
										itemstack);
							}
						}
					} else {
						casing.markDirty();
						player.openGui(VaryingMachina.instance, ModGuiHandler.getIDForTile(TileCasing.class), world,
								pos.getX(), pos.getY(), pos.getZ());
					}
					return true;
				}
				return false;
			}
		}
		return true;
	}
}
