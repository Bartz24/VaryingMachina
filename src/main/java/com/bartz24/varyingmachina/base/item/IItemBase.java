package com.bartz24.varyingmachina.base.item;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IItemBase {
	@SideOnly(Side.CLIENT)
	public void initModel();
}
