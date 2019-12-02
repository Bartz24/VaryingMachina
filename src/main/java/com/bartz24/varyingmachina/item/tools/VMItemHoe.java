package com.bartz24.varyingmachina.item.tools;

import com.bartz24.varyingmachina.VaryingMachina;
import com.bartz24.varyingmachina.item.IVariantItem;
import com.bartz24.varyingmachina.machine.MachineVariant;
import com.bartz24.varyingmachina.pixellib.PixelPlugin;
import com.emosewapixel.pixellib.materialsystem.lists.MaterialItems;
import com.emosewapixel.pixellib.materialsystem.main.IMaterialObject;
import com.emosewapixel.pixellib.materialsystem.main.Material;
import com.emosewapixel.pixellib.materialsystem.main.ObjectType;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class VMItemHoe extends HoeItem implements IVariantItem, IMaterialObject {

    private Material material;
    private MachineVariant variant;

    public VMItemHoe(Material material, MachineVariant variant) {
        super(variant.getItemTier(), variant.getItemTier().getHarvestLevel() - 3, (new Item.Properties()).group(VaryingMachina.main));
        setRegistryName("varyingmachina", "hoe." + variant.getName());
        this.material = material;
        this.variant = variant;
        MaterialItems.addItem(this);
    }

    @Override
    public MachineVariant getVariant() {
        return variant;
    }

    @Override
    public Material getMat() {
        return material;
    }

    @Override
    public ObjectType getObjType() {
        return PixelPlugin.hoe;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        return new TranslationTextComponent("itemtype.hoe", material.getName());
    }
}
