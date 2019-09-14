package com.bartz24.varyingmachina.guide;

import com.bartz24.varyingmachina.Helpers;
import com.bartz24.varyingmachina.VaryingMachina;
import com.bartz24.varyingmachina.inventory.FuelUnit;
import com.bartz24.varyingmachina.item.ItemBlockMachine;
import com.bartz24.varyingmachina.machine.MachineStat;
import com.bartz24.varyingmachina.machine.MachineType;
import com.bartz24.varyingmachina.machine.MachineVariant;
import com.bartz24.varyingmachina.machine.ModVariants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MachineGuidePage extends TextGuidePage {

    private MachineType type;

    private List<MachineInfo> infoList = new ArrayList<>();

    public MachineGuidePage(ResourceLocation id, ItemStack stackIcon, MachineType type) {
        super(id, stackIcon);
        this.type = type;
    }

    public void sort(int sortType, boolean descending) {

        Comparator<MachineInfo> comparing;
        if (sortType == 0) {
            comparing = Comparator.comparing(MachineInfo::getName);
        } else if (sortType - 1 < type.getDisplayedStats().size()) {
            MachineStat stat = (MachineStat) type.getDisplayedStats().get(sortType - 1);
            comparing = Comparator.comparing(o -> o.calculateStat(stat));
        } else if (sortType - 1 - type.getDisplayedStats().size() < type.getSpecialTooltipTypes().size()) {
            String stat = type.getSpecialTooltipTypes().get(sortType - 1 - type.getDisplayedStats().size()).toString();
            comparing = Comparator.comparing(o -> o.calculateSpecialStat(stat));
        } else
            comparing = Comparator.comparing(MachineInfo::getFuelUnit);
        if (descending)
            comparing = comparing.reversed();
        infoList.sort(comparing);
        for (int i = 0; i < getCategories().size(); i++) {
            if (i == sortType)
                continue;
            if (getSortButton(i) != null)
                getSortButton(i).resetClicks();
        }
    }

    @Override
    public void load(GuiPageInfo gui) {
        super.load(gui);

        infoList.clear();
        VaryingMachina.itemMachines.values().forEach(item -> {
            if (item instanceof ItemBlockMachine && ((ItemBlockMachine) item).getBlockMachine().getMachineType().equals(type.getId())) {
                ItemStack stack = new ItemStack(item);
                JEIRecipeButton button = new JEIRecipeButton(gui.getGuiGuide(), 0, -100, -100, stack, "", stack.getDisplayName().getFormattedText()) {
                    @Override
                    public void resetWidth() {

                    }
                };
                infoList.add(new MachineInfo((ItemBlockMachine) item, button));
                buttons.add(button);
            }
        });

        sort(1, false);

        List<String> categories = getCategories();
        for (int i = 0; i < categories.size(); i++) {
            buttons.add(new MachineSortButton(gui.getGuiGuide(), 0, -100, -100, i, "", Helpers.removeTextFormatting(categories.get(i))));
        }
    }

    @Override
    public void drawSlot(GuiPageInfo gui, int x, int y, double mouseX, double mouseY) {
        super.drawSlot(gui, x, y, mouseX, mouseY);

        int curY = getYStart(gui.getWidth()) + y;
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;

        int nameWidth = (int) ((gui.getRowWidth() - 10) / getCategories().size() * 1.5);
        int maxColumnWidth = (gui.getRowWidth() - 10 - nameWidth) / (getCategories().size() - 1);
        {
            MachineSortButton button = getSortButton(0);
            button.setWidth(nameWidth);
            button.x = x;
            button.y = curY;
            button.render((int) mouseX, (int) mouseY, 0);
        }
        for (int i = 1; i < getCategories().size(); i++) {
            MachineSortButton button = getSortButton(i);
            button.setWidth(maxColumnWidth);
            button.x = x + nameWidth + (i - 1) * maxColumnWidth;
            button.y = curY;
            button.render((int) mouseX, (int) mouseY, 0);
        }
        curY += 20;
        for (MachineInfo info : infoList) {
            JEIRecipeButton button = info.button;
            button.x = x;
            button.y = curY-4;
            button.setWidth(nameWidth);
            button.render((int) mouseX, (int) mouseY, 0);
            int column = 0;
            for (int i = 0; i < type.getDisplayedStats().size(); i++) {
                Helpers.drawCompressedString(fontRenderer, Helpers.removeTextFormatting(info.getStat((MachineStat) type.getDisplayedStats().get(i))), x + nameWidth + column * maxColumnWidth + maxColumnWidth / 2, curY, true, maxColumnWidth, 0xffffff);
                column++;
            }
            for (int i = 0; i < type.getSpecialTooltipTypes().size(); i++) {
                Helpers.drawCompressedString(fontRenderer, Helpers.removeTextFormatting(info.getSpecialStat(type.getSpecialTooltipTypes().get(i).toString())), x + nameWidth + column * maxColumnWidth + maxColumnWidth / 2, curY, true, maxColumnWidth, 0xffffff);
                column++;
            }
            if (!type.isNoFuel())
                Helpers.drawCompressedString(fontRenderer, Helpers.removeTextFormatting(info.getFuelUnit()), x + nameWidth + column * maxColumnWidth + maxColumnWidth / 2, curY, true, maxColumnWidth, 0xffffff);
            curY += 20;
        }
    }

    private MachineSortButton getSortButton(int sortType) {
        for (GuiPageButton button : buttons) {
            if (button instanceof MachineSortButton && ((MachineSortButton) button).getSortType() == sortType)
                return (MachineSortButton) button;
        }
        return null;
    }

    @Override
    public int getHeight(int width) {
        int size = infoList.size();
        if (size == 0) {
            for (Item item : VaryingMachina.itemMachines.values()) {
                if (item instanceof ItemBlockMachine && ((ItemBlockMachine) item).getBlockMachine().getMachineType().equals(type.getId()))
                    size++;
            }
        }
        return super.getHeight(width) + 40 + size * 20;
    }

    public int getYStart(int width) {
        return super.getHeight(width) + 20;
    }

    public List<String> getCategories() {
        List<String> list = new ArrayList<>();
        list.add(new TranslationTextComponent("varyingmachina.guide.machinename").getFormattedText());
        for (Object stat : type.getDisplayedStats())
            list.add(new TranslationTextComponent("varyingmachina.guide.stat." + ((MachineStat) stat).getName().toLowerCase()).getFormattedText());
        for (Object stat : type.getSpecialTooltipTypes())
            list.add(new TranslationTextComponent("varyingmachina.guide.stat." + stat.toString().toLowerCase()).getFormattedText());
        if (!type.isNoFuel())
            list.add(new TranslationTextComponent("varyingmachina.guide.fueltype").getFormattedText());
        return list;
    }

    private class MachineInfo {
        public ItemBlockMachine machineItem;
        public JEIRecipeButton button;

        public MachineInfo(ItemBlockMachine item, JEIRecipeButton button) {
            machineItem = item;
            this.button = button;
        }

        public String getName() {
            return machineItem.getDisplayName(new ItemStack(machineItem)).getFormattedText();
        }

        public double calculateStat(MachineStat stat) {
            return stat.calculateStat(type, ModVariants.types.get(machineItem.getBlockMachine().getMachineVariant()), ModVariants.types.get(machineItem.getBlockMachine().getCasingVariant()));
        }

        public String getStat(MachineStat stat) {
            return stat.getText(type, ModVariants.types.get(machineItem.getBlockMachine().getMachineVariant()), ModVariants.types.get(machineItem.getBlockMachine().getCasingVariant()));
        }

        public double calculateSpecialStat(String stat) {
            return type.calculateSpecialStat(stat, ModVariants.types.get(machineItem.getBlockMachine().getMachineVariant()), ModVariants.types.get(machineItem.getBlockMachine().getCasingVariant()));
        }

        public String getSpecialStat(String stat) {
            return type.getTextSpecialStat(stat, ModVariants.types.get(machineItem.getBlockMachine().getMachineVariant()), ModVariants.types.get(machineItem.getBlockMachine().getCasingVariant()));
        }

        public String getFuelUnit() {
            MachineVariant variant = ModVariants.types.get(machineItem.getBlockMachine().getMachineVariant());
            FuelUnit fuelUnit = variant.getFuelUnitSupplier().apply(variant.getFuelUnitSize());
            if (fuelUnit != null)
                return ((ITextComponent) fuelUnit.getTooltips().get(0)).getFormattedText();
            return "-";
        }
    }
}
