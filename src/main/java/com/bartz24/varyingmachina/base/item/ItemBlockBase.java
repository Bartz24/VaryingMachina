package com.bartz24.varyingmachina.base.item;

import com.bartz24.varyingmachina.registry.ModRenderers;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ItemBlockBase extends ItemBlock implements IItemBase {

	public ItemBlockBase(Block block) {
		super(block);
		ModRenderers.addItemToRender(this);
		this.setCreativeTab(CreativeTabs.MISC);
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@Override
	public ItemBlockBase registerOreDict(String name) {
		OreDictionary.registerOre(name, this);
		return this;
	}

}
