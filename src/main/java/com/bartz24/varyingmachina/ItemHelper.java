package com.bartz24.varyingmachina;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemHelper
{
	public static IBlockState getBlockStateFromStack(ItemStack stack)
	{
		int meta = stack.getMetadata();
		if (!(stack.getItem() instanceof ItemBlock))
			return null;

		Block block = ((ItemBlock) stack.getItem()).getBlock();

		return block.getStateFromMeta(meta);
	}

	public static boolean itemStacksEqualOD(ItemStack stack1, ItemStack stack2)
	{
		if (stack1.isItemEqual(stack2))
			return true;

		if (!stack1.isEmpty() && !stack2.isEmpty() && stack1.getMetadata() == OreDictionary.WILDCARD_VALUE
				|| stack2.getMetadata() == OreDictionary.WILDCARD_VALUE)
		{
			return stack1.getItem() == stack2.getItem();
		}
		return false;
	}
}