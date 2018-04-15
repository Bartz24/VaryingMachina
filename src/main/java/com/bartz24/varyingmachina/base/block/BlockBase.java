package com.bartz24.varyingmachina.base.block;

import com.bartz24.varyingmachina.References;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockBase extends Block {
	public BlockBase(String blockName, Material material, MapColor mapColor, int hardness, int resistance) {
		super(material, mapColor);
		setHardness(hardness);
		setResistance(resistance);
		setCreativeTab(CreativeTabs.MISC);
		setRegistryName(References.ModID, blockName);
		setUnlocalizedName(References.ModID + "." + blockName);
	}
}
