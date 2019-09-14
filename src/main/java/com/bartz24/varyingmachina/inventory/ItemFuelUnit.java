package com.bartz24.varyingmachina.inventory;

import com.bartz24.varyingmachina.gui.GuiMachine;
import com.bartz24.varyingmachina.tile.TileEntityMachine;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemFuelUnit extends FuelUnit<ItemHandlerFiltered> {

    protected HashMap<ItemStack, Double> energyValues = new HashMap<>();

    double energy = 0, maxEnergy = 0;

    ItemHandlerFiltered handler;

    public ItemFuelUnit(int size) {
        handler = new ItemHandlerFiltered(size, false, true);
    }

    public ItemFuelUnit addItemValue(ItemStack stack, double value) {
        ItemStack s = stack.copy();
        s.setCount(1);
        energyValues.put(s, value);
        for (int i = 0; i < handler.getSlots(); i++) {
            handler.getFilter(i).add(s);
        }
        return this;
    }

    @Override
    public double getEnergy() {
        double energyStored = energy + 0;
        for (int i = 0; i < handler.getSlots(); i++) {
            energyStored += getEnergyValue(handler.getStackInSlot(i));
        }
        return energyStored;
    }

    public double getEnergyValue(ItemStack stack) {
        for (ItemStack s : energyValues.keySet()) {
            if (stack.isItemEqual(s))
                return energyValues.get(s) * stack.getCount();
        }
        return 0;
    }

    @Override
    public double drainEnergy(double amount, boolean simulate) {
        double drainedEnergy = 0, startingEnergy = energy + 0;
        energy -= amount;
        while (getItemsLeft() > 0 && energy < 0) {
            for (int slot = handler.getSlots() - 1; slot >= 0; slot--) {
                if (!handler.getStackInSlot(slot).isEmpty() && getEnergyValue(handler.getStackInSlot(slot)) > 0) {
                    ItemStack stack = handler.getStackInSlot(slot).copy();
                    stack.setCount(1);
                    if (!simulate)
                        handler.getStackInSlot(slot).shrink(1);
                    energy += getEnergyValue(stack);
                    maxEnergy = getEnergyValue(stack);
                    drainedEnergy += getEnergyValue(stack);
                    break;
                }
            }
        }
        double a = startingEnergy + drainedEnergy - energy;
        if (simulate)
            energy = startingEnergy;
        return a;
    }

    private int getItemsLeft() {
        int count = 0;
        for (int i = 0; i < handler.getSlots(); i++) {
            if (getEnergyValue(handler.getStackInSlot(i)) > 0)
                count += handler.getStackInSlot(i).getCount();
        }
        return count;
    }

    @Override
    public ItemHandlerFiltered getContainer() {
        return handler;
    }

    @Override
    public ContainerArea getArea() {
        return new AreaItemFuelUnit(handler);
    }

    @Override
    public CompoundNBT writeNBT() {
        CompoundNBT compound = new CompoundNBT();
        ListNBT energyVals = new ListNBT();
        for (ItemStack stack : energyValues.keySet()) {
            CompoundNBT data = new CompoundNBT();
            stack.write(data);
            data.putDouble("energyVal", energyValues.get(stack));
            energyVals.add(data);
        }
        compound.put("energyVals", energyVals);
        compound.putDouble("energy", energy);
        compound.putDouble("maxEnergy", maxEnergy);
        compound.put("handler", handler.serializeNBT());
        return compound;
    }

    @Override
    public void readNBT(CompoundNBT compound) {
        ListNBT energyVals = compound.getList("energyVals", Constants.NBT.TAG_COMPOUND);
        energyValues = new HashMap<>();
        for (int i = 0; i < energyVals.size(); i++) {
            energyValues.put(ItemStack.read(energyVals.getCompound(i)), energyVals.getCompound(i).getDouble("energyVal"));
        }
        energy = compound.getDouble("energy");
        maxEnergy = compound.getDouble("maxEnergy");
        handler.deserializeNBT(compound.getCompound("handler"));
    }

    @Override
    public List<ITextComponent> getTooltips() {
        List<ITextComponent> list = new ArrayList<>();
        if (energyValues.size() > 1) {
            for (ItemStack stack : energyValues.keySet()) {
                list.add(new StringTextComponent(TextFormatting.RED + new TranslationTextComponent("varyingmachina.fuel.item.multiple", stack.getDisplayName().getFormattedText()).getFormattedText()));
                break;
            }
        }
        if (energyValues.size() > 0) {
            for (ItemStack stack : energyValues.keySet()) {
                list.add(new StringTextComponent(TextFormatting.RED + new TranslationTextComponent("varyingmachina.fuel.item.single", stack.getDisplayName().getFormattedText()).getFormattedText()));
                break;
            }
        }
        return list;
    }

    public static class AreaItemFuelUnit extends AreaItemHandler {

        AreaSmallBar smallBar = new AreaSmallBar(ContainerArea.XAnchorDirection.LEFT, ContainerArea.YAnchorDirection.TOP);

        public AreaItemFuelUnit(ItemHandlerFiltered handler) {
            super(handler, XAnchorDirection.CENTER, ContainerArea.YAnchorDirection.TOP, -50);
        }

        @Override
        public void update(TileEntityMachine tile) {
            super.update(tile);

            ItemFuelUnit fuelUnit = (ItemFuelUnit) tile.getFuelUnit();

            smallBar.setPos(super.getWidth() + x - 1, getHeight() - 19 + y);
            smallBar.update(fuelUnit.energy, fuelUnit.maxEnergy);
        }

        @Override
        public void setPos(int x, int y) {
            super.setPos(x, y);
        }

        @Override
        public void drawBackground(GuiMachine gui) {
            super.drawBackground(gui);
            smallBar.drawBackground(gui);
        }

        @Override
        public void drawForeground(GuiMachine gui, int mouseX, int mouseY) {
            super.drawForeground(gui, mouseX, mouseY);
            smallBar.drawForeground(gui, mouseX, mouseY);
        }

        @Override
        public int getWidth() {
            return super.getWidth() + 3;
        }
    }
}
