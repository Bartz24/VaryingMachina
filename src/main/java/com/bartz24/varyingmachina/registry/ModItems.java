package com.bartz24.varyingmachina.registry;

import com.bartz24.varyingmachina.References;
import com.bartz24.varyingmachina.base.item.*;
import com.bartz24.varyingmachina.machines.*;
import com.bartz24.varyingmachina.modules.*;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.oredict.OreDictionary;

@ObjectHolder(References.ModID)
@EventBusSubscriber
public class ModItems {
    @ObjectHolder("darkmatter")
    public static Item darkmatter;
    @ObjectHolder("lightmatter")
    public static Item lightmatter;
    @ObjectHolder("plantmatter")
    public static Item plantmatter;
    @ObjectHolder("stonedust")
    public static Item stonedust;
    @ObjectHolder("frozenironingot")
    public static Item frozenironingot;

    @ObjectHolder("smelter")
    public static Item smelter;
    @ObjectHolder("grinder")
    public static Item grinder;
    @ObjectHolder("presser")
    public static Item presser;
    @ObjectHolder("assembler")
    public static Item assembler;
    @ObjectHolder("mixer")
    public static Item mixer;
    @ObjectHolder("combustion")
    public static Item combustion;
    @ObjectHolder("itembuffer")
    public static Item itembuffer;
    @ObjectHolder("extractor")
    public static Item extractor;

    @ObjectHolder("regulator")
    public static Item regulator;
    @ObjectHolder("inserter")
    public static Item inserter;
    @ObjectHolder("remover")
    public static Item remover;
    @ObjectHolder("bellow")
    public static Item bellow;
    @ObjectHolder("gearbox")
    public static Item gearbox;
    @ObjectHolder("worldinserter")
    public static Item worldinserter;
    @ObjectHolder("worldremover")
    public static Item worldremover;

    @ObjectHolder("circuit")
    public static Item circuit;


    public static void registerMachines() {
        MachineRegistry
                .registerItemMachineType(new MachineItemBuilder(MachineSmelter.class, References.ModID, "smelter"));
        MachineRegistry
                .registerItemMachineType(new MachineItemBuilder(MachineGrinder.class, References.ModID, "grinder"));
        MachineRegistry
                .registerItemMachineType(new MachineItemBuilder(MachinePresser.class, References.ModID, "presser"));
        MachineRegistry
                .registerItemMachineType(new MachineItemBuilder(MachineAssembler.class, References.ModID, "assembler"));
        MachineRegistry
                .registerItemMachineType(new MachineItemBuilder(MachineMixer.class, References.ModID, "mixer"));
        MachineRegistry
                .registerItemMachineType(new MachineItemBuilder(MachineCombustion.class, References.ModID, "combustion"));
        MachineRegistry
                .registerItemMachineType(new MachineItemBuilder(MachineItemBuffer.class, References.ModID, "itembuffer"));
        MachineRegistry
                .registerItemMachineType(new MachineItemBuilder(MachineExtractor.class, References.ModID, "extractor"));

        MachineRegistry
                .registerItemModuleType(new ModuleItemBuilder(ModuleRegulator.class, References.ModID, "regulator"));
        MachineRegistry
                .registerItemModuleType(new ModuleItemBuilder(ModuleInserter.class, References.ModID, "inserter"));
        MachineRegistry.registerItemModuleType(new ModuleItemBuilder(ModuleRemover.class, References.ModID, "remover"));
        MachineRegistry.registerItemModuleType(new ModuleItemBuilder(ModuleBellow.class, References.ModID, "bellow"));
        MachineRegistry.registerItemModuleType(new ModuleItemBuilder(ModuleGearbox.class, References.ModID, "gearbox"));
        MachineRegistry.registerItemModuleType(new ModuleItemBuilder(ModuleWorldInserter.class, References.ModID, "worldinserter"));
        MachineRegistry.registerItemModuleType(new ModuleItemBuilder(ModuleWorldRemover.class, References.ModID, "worldremover"));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        registerItem(event, new ItemBase(), "darkmatter");
        registerItem(event, new ItemBase(), "lightmatter");
        registerItem(event, new ItemSuperBonemeal(), "plantmatter");
        registerItem(event, new ItemBase(), "stonedust", "dustStone");
        registerItem(event, new ItemBase(), "frozenironingot", "ingotFrozenIron");
        registerItem(event, new ItemBlockCasing(ModBlocks.casing));
        registerItem(event, new ItemBlockBase(ModBlocks.darkmatterblock), "darkmatterblock");
        registerItem(event, new ItemBlockBase(ModBlocks.lightmatterblock), "lightmatterblock");
        registerItem(event, new ItemBlockBase(ModBlocks.frozenironblock), "frozenironblock", "blockFrozenIron");
        registerItem(event, new ItemMeta(new String[]{"weak", "basic", "improved", "advanced", "ultra", "unstable"}), "circuit");
    }

    private static void registerItem(RegistryEvent.Register<Item> event, Item item) {
        event.getRegistry().register(item);
    }

    private static void registerItem(RegistryEvent.Register<Item> event, Item item, String name) {
       registerItem(event, item.setRegistryName(References.ModID, name)
                .setUnlocalizedName(References.ModID + "." + name));
    }

    private static void registerItem(RegistryEvent.Register<Item> event, Item item, String name, String oreDictName) {
        registerItem(event, item, name);
        if(item instanceof IItemBase)
            ((IItemBase) item).registerOreDict(oreDictName);
    }
}
