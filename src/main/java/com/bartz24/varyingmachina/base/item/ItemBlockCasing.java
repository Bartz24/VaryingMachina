package com.bartz24.varyingmachina.base.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.bartz24.varyingmachina.References;
import com.bartz24.varyingmachina.base.block.BlockCasing;
import com.bartz24.varyingmachina.base.machine.MachineStat;
import com.bartz24.varyingmachina.base.machine.MachineVariant;
import com.bartz24.varyingmachina.base.models.MachineModelLoader;
import com.bartz24.varyingmachina.base.tile.TileCasing;
import com.bartz24.varyingmachina.registry.MachineRegistry;
import com.bartz24.varyingmachina.registry.ModRenderers;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockCasing extends ItemBlock implements IItemBase {

	public ItemBlockCasing(Block block) {
		super(block);
		ModRenderers.addItemToRender(this);
		this.setHasSubtypes(true);
		setRegistryName(References.ModID, "casing");
	}

	public String getItemStackDisplayName(ItemStack stack) {
		MachineVariant variant = MachineVariant.readFromNBT(stack.getTagCompound());
		if (variant == null)
			return super.getItemStackDisplayName(stack);
		return String.format(super.getItemStackDisplayName(stack),
				I18n.translateToLocal(variant.getRegistryName().toString().replace(":", ".")));
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ResourceLocation baseLocation = new ResourceLocation(References.ModID, "block/casing");
		MachineModelLoader loader = new MachineModelLoader(baseLocation, "all");
		Map<MachineVariant, ModelResourceLocation> locations = new HashMap();
		for (int i = 0; i < MachineRegistry.getAllVariantsRegistered().length; i++) {
			ModelResourceLocation location = new ModelResourceLocation(
					getRegistryName().toString()
							+ MachineRegistry.getAllVariantsRegistered()[i].getRegistryName().getResourcePath(),
					"inventory");
			locations.put(MachineRegistry.getAllVariantsRegistered()[i], location);
			loader.addVariant(location, MachineRegistry.getAllVariantsRegistered()[i].getTexturePath());
		}
		ModelLoader.registerItemVariants(this, locations.values().toArray(new ModelResourceLocation[locations.size()]));
		ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				MachineVariant variant = MachineVariant.readFromNBT(stack.getTagCompound());
				if (variant != null)
					return locations.get(variant);
				return locations.get(0);
			}
		});
		ModelLoaderRegistry.registerLoader(loader);

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs creativeTab, NonNullList<ItemStack> list) {
		if (isInCreativeTab(creativeTab))
			for (MachineVariant variant : MachineVariant.REGISTRY.getValuesCollection()) {
				ItemStack stack = new ItemStack(this);
				MachineVariant.writeVariantToStack(stack, variant);
				list.add(stack);
			}
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		for (MachineStat machineStat : BlockCasing.displayedStats)
			machineStat.addSingleInfo(stack, tooltip);
	}

	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ, IBlockState newState) {
		if (super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState)) {
			TileEntity entity = world.getTileEntity(pos);
			if (entity instanceof TileCasing) {
				((TileCasing) entity).setVariant(MachineVariant.readFromNBT(stack.getTagCompound()));
				entity.markDirty();
			}
			return true;
		}
		return false;
	}
}
