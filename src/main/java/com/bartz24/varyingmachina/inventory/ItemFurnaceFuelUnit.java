package com.bartz24.varyingmachina.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class ItemFurnaceFuelUnit extends ItemFuelUnit {

    public ItemFurnaceFuelUnit(int size) {
        super(size);
        handler = new ItemHandlerFiltered(size, false, true) {
            @Override
            public List<ItemStack> getFilter(int slot) {
                List<ItemStack> stacks = new ArrayList<>();
                FurnaceTileEntity.getBurnTimes().keySet().forEach((i) -> stacks.add(new ItemStack(i)));
                return stacks;
            }
        };
        handler.setSlotMultiplier(() -> 8);
    }


    public double getEnergyValue(ItemStack stack) {
        return FurnaceTileEntity.isFuel(stack) ? FurnaceTileEntity.getBurnTimes().get(stack.getItem()) / 3.5d : 0;
    }

    @Override
    public List<ITextComponent> getTooltips() {
        List<ITextComponent> list = new ArrayList<>();
        list.add(new StringTextComponent(TextFormatting.RED + new TranslationTextComponent("varyingmachina.fuel.furnace").getFormattedText()));
        return list;
    }
}
