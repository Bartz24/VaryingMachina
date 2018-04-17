package com.bartz24.varyingmachina.base.item;

import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IItemBase<T extends Item> {
	@SideOnly(Side.CLIENT)
	public void initModel();

	public T registerOreDict(String name);
}
