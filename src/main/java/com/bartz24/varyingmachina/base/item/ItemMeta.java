package com.bartz24.varyingmachina.base.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;

public class ItemMeta extends ItemBase {

    private String[] variants;

    public ItemMeta(String[] variants) {
        this.variants = variants;
        this.hasSubtypes = true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs creativeTab, NonNullList<ItemStack> list) {
        if (isInCreativeTab(creativeTab))
            for (String variant : variants) {
                list.add(getStackOfVariant(variant, 1));
            }
    }

    public ItemStack getStackOfVariant(String variant, int count) {
        return new ItemStack(this, count, Arrays.asList(variants).indexOf(variant));
    }

    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName() + "." + variants[stack.getMetadata()];
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        for (int i = 0; i < variants.length; i++)
            ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation(getRegistryName() + "." + variants[i], "inventory"));
    }
}
