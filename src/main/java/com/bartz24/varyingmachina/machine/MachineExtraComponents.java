package com.bartz24.varyingmachina.machine;

import com.bartz24.varyingmachina.item.ItemBlockMachine;
import com.bartz24.varyingmachina.recipe.InputBase;
import com.bartz24.varyingmachina.recipe.InputItem;
import com.bartz24.varyingmachina.recipe.InputItemList;
import com.bartz24.varyingmachina.recipe.InputItemTag;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MachineExtraComponents {

    public static Function<Item, InputBase> smelter = item -> {
        ItemBlockMachine machine = getMachineItem(item);

        int rating = getRating(machine);
        if (machine != null) {
            if (rating == 1)
                return new InputItemTag("planks", 1);
            else if (rating == 2)
                return new InputItemTag("forge:dusts/coal", 1);
            else if (rating == 3)
                return new InputItem(new ItemStack(Items.GUNPOWDER));
            else if (rating == 4)
                return new InputItem(new ItemStack(Items.BLAZE_POWDER));
            //TODO Smelter
        }
        return null;
    };

    public static Function<Item, InputBase> grinder = item -> {
        ItemBlockMachine machine = getMachineItem(item);

        int rating = getRating(machine);
        if (machine != null) {
            if (rating == 1)
                return new InputItem(new ItemStack(Items.FLINT));
            else if (rating == 2)
                return new InputItemTag("forge:gears/iron", 1);
            else if (rating == 3)
                return new InputItemTag("forge:gears/diamond", 1);
            else if (rating == 4)
                return new InputItemTag("forge:gears/quartz", 1);
            else if (rating == 5)
                return new InputItemTag("forge:gears/emerald", 1);
            //TODO Grinder
        }
        return null;
    };

    public static Function<Item, InputBase> assembler = item -> {
        ItemBlockMachine machine = getMachineItem(item);

        int rating = getRating(machine);
        if (machine != null) {
            if (rating == 1)
                return new InputItemTag("logs", 1);
            else if (rating == 2)
                return new InputItemTag("forge:gears/copper", 1);
            else if (rating == 3)
                return new InputItemTag("forge:gears/gold", 1);
            else if (rating == 4)
                return new InputItemTag("forge:gears/redcopper", 1);
            //TODO Assembler
        }
        return null;
    };

    public static Function<Item, InputBase> reserve = item -> {
        ItemBlockMachine machine = getMachineItem(item);

        int rating = getRating(machine);
        if (machine != null) {
            if (rating == 1)
                return new InputItemTag("logs", 1);
            else if (rating == 2)
                return new InputItemTag("forge:storage_blocks/tin", 1);
            else if (rating == 3)
                return new InputItemTag("forge:storage_blocks/silver", 1);
            else if (rating == 4)
                return new InputItemTag("forge:storage_blocks/mythrilsteel", 1);
            //TODO Reserve
        }
        return null;
    };

    public static Function<Item, InputBase> storagebuffer = item -> {
        ItemBlockMachine machine = getMachineItem(item);

        int rating = getRating(machine);
        if (machine != null) {
            if (rating == 1)
                return new InputItem(new ItemStack(Blocks.DIRT, 4));
            else if (rating == 2)
                return new InputItemTag("forge:plates/bronze", 4);
            else if (rating == 3)
                return new InputItemTag("forge:plates/steel", 4);
            //TODO Storage Buffer
        }
        return null;
    };


    public static Function<Item, InputBase> miner = item -> {
        ItemBlockMachine machine = getMachineItem(item);

        int rating = getRating(machine);

        List<Item> pickaxes = ForgeRegistries.ITEMS.getValues().stream().filter(i -> i instanceof PickaxeItem && ((PickaxeItem) i).getTier().getHarvestLevel() == rating).collect(Collectors.toList());

        List<ItemStack> stacks = new ArrayList<>();

        pickaxes.forEach(i -> stacks.add(new ItemStack(i)));

        if (machine != null) {
            return new InputItemList(stacks);
        }
        return null;
    };


    public static Function<Item, InputBase> mover = item -> {
        ItemBlockMachine machine = getMachineItem(item);

        int rating = getRating(machine);
        if (machine != null) {
            if (rating == 1)
                return new InputItem(new ItemStack(Items.REDSTONE));
            else if (rating == 2)
                return new InputItemTag("forge:cables/copper", 1);
            else if (rating == 3)
                return new InputItemTag("forge:cables/ferrotin", 1);
            else if (rating == 4)
                return new InputItemTag("forge:cables/redcopper", 1);
            //TODO Mover
        }
        return null;
    };

    private static ItemBlockMachine getMachineItem(Item item) {
        if (item instanceof ItemBlockMachine)
            return (ItemBlockMachine) item;
        return null;
    }

    private static int getRating(ItemBlockMachine machine) {
        return (int) ModMachines.Stats.rating.calculateStat(ModMachines.types.get(machine.getBlockMachine().getMachineType()), ModVariants.types.get(machine.getBlockMachine().getMachineVariant()), ModVariants.types.get(machine.getBlockMachine().getCasingVariant()));
    }
}
