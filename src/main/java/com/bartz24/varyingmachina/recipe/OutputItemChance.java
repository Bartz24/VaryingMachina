package com.bartz24.varyingmachina.recipe;

import com.bartz24.varyingmachina.Helpers;
import com.bartz24.varyingmachina.inventory.ItemHandlerRestricted;
import com.bartz24.varyingmachina.machine.ModMachines;
import com.bartz24.varyingmachina.tile.TileEntityMachine;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.List;
import java.util.function.Supplier;

public class OutputItemChance extends OutputItem {
    private double chanceLow;
    private double chanceHigh;

    public OutputItemChance(ItemStack stack, double chanceLow, double chanceHigh) {
        this(stack, "output", chanceLow, chanceHigh);
    }

    public OutputItemChance(ItemStack stack, String invId, double chanceLow, double chanceHigh) {
        super(stack, invId);
        this.chanceLow = chanceLow;
        this.chanceHigh = chanceHigh;
    }

    @Override
    public boolean hasRoomForOutput(ItemHandlerRestricted inventory, RecipeBase recipeBase) {
        ItemStack stack = super.getOutput();
        stack.setCount(stack.getCount() * (int) Math.ceil(chanceHigh));
        return inventory.insertItemStacked(getOutput(), true).isEmpty();
    }

    @Override
    public void putItemsIntoInventory(ItemHandlerRestricted inventory, RecipeBase recipeBase) {
        ItemStack stack = super.getOutput();
        double mult = 1;
        if (inventory.getParent() != null && inventory.getParent() instanceof TileEntityMachine) {
            TileEntityMachine tile = (TileEntityMachine) inventory.getParent();
            double prod = ModMachines.Stats.production.calculateStat(tile.getMachine(), tile.getMainMachineVariant(), tile.getCasingMachineVariant());
            if (prod > 1) {
                mult = (int) Math.floor(Math.max(Math.min(prod, chanceHigh), chanceLow));
                prod -= mult;
            }
            if (tile.getWorld().rand.nextFloat() <= prod)
                mult++;
        } else if (inventory.getParent() != null) {
            double prod = inventory.getParent().getWorld().rand.nextFloat() * (chanceHigh - chanceLow) + chanceLow;
            mult = Math.floor(prod);
            prod -= mult;
            if (inventory.getParent().getWorld().rand.nextFloat() <= prod)
                mult++;
        }
        stack.setCount(stack.getCount() * (int) mult);

        inventory.insertItemStacked(stack.copy(), false);
    }

    @Override
    public Supplier<List<String>> getJEITooltips() {
        List<String> list = super.getJEITooltips().get();
        list.add(TextFormatting.WHITE + new TranslationTextComponent("varyingmachina.jei.chance").getFormattedText() + ": " + Helpers.round(chanceLow * 100, 1) + "%-" + Helpers.round(chanceHigh * 100, 1) + "%");
        return () -> list;
    }

    @Override
    public OutputBase<ItemHandlerRestricted, ItemStack> copy() {
        return new OutputItemChance(super.getOutput(), chanceLow, chanceHigh);
    }
}
