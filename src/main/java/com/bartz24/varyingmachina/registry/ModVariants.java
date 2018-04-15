package com.bartz24.varyingmachina.registry;

import com.bartz24.varyingmachina.References;
import com.bartz24.varyingmachina.base.machine.FuelType;
import com.bartz24.varyingmachina.base.machine.MachineStat;
import com.bartz24.varyingmachina.base.machine.MachineVariant;
import com.bartz24.varyingmachina.base.machine.MachineVariant.FuelInfo;
import com.bartz24.varyingmachina.base.recipe.RecipeItem;
import com.bartz24.varyingmachina.base.recipe.RecipeNameItem;
import com.bartz24.varyingmachina.base.recipe.RecipeOreDict;
import com.bartz24.varyingmachina.base.recipe.RecipeOreDictPriority;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(References.ModID)
public class ModVariants {
    // Vanilla
    public static MachineVariant dirt;
    public static MachineVariant wood;
    public static MachineVariant stone;
    public static MachineVariant brick;
    public static MachineVariant netherbrick;
    public static MachineVariant endstone;
    public static MachineVariant iron;
    // Basic/Thermal
    public static MachineVariant bronze;
    public static MachineVariant steel;
    public static MachineVariant lead;
    public static MachineVariant constantan;
    public static MachineVariant invar;
    public static MachineVariant electrum;
    public static MachineVariant signalum;
    public static MachineVariant enderium;
    // Mekanism
    public static MachineVariant osmium;
    public static MachineVariant refinedobsidian;
    // Botania
    public static MachineVariant manasteel;
    public static MachineVariant terrasteel;
    public static MachineVariant elementium;
    // Tinkers
    public static MachineVariant cobalt;
    public static MachineVariant ardite;
    public static MachineVariant manyullyn;
    // EnderIO
    public static MachineVariant electricalsteel;
    public static MachineVariant energeticalloy;
    public static MachineVariant vibrantalloy;
    public static MachineVariant soularium;
    public static MachineVariant darksteel;
    // VaryingMachina
    public static MachineVariant darkmatter;
    public static MachineVariant lightmatter;

    public static void registerVariants() {
        MachineRegistry.registerMachineVariant(new MachineVariant("dirt", new FuelInfo(FuelType.FURNACE),
                "minecraft:blocks/dirt", new RecipeItem(new ItemStack(Blocks.DIRT, 8)),
                new RecipeOreDictPriority(1, "gearWood", "plankWood"), new RecipeItem(new ItemStack(Blocks.DIRT)), 0)
                .setStat(MachineStat.MAXHU, 220).setStat(MachineStat.SPEED, .65f)
                .setStat(MachineStat.EFFICIENCY, .39f));
        MachineRegistry.registerMachineVariant(
                new MachineVariant("wood", new FuelInfo(FuelType.FURNACE), "minecraft:blocks/log_oak",
                        new RecipeOreDict("logWood", 8), new RecipeOreDictPriority(1, "gearWood", "plankWood"),
                        new RecipeOreDict("stickWood", 8), 0).setStat(MachineStat.MAXHU, 120)
                        .setStat(MachineStat.SPEED, .41f).setStat(MachineStat.EFFICIENCY, .97f));
        MachineRegistry.registerMachineVariant(new MachineVariant("stone", new FuelInfo(FuelType.FURNACE),
                "minecraft:blocks/stone", new RecipeItem(new ItemStack(Blocks.STONE, 8)),
                new RecipeOreDictPriority(1, "gearStone", "cobblestone"),
                new RecipeOreDictPriority(1, "rodStone", "stickStone", "stone"), 1).setStat(MachineStat.MAXHU, 680)
                .setStat(MachineStat.SPEED, 1.00f).setStat(MachineStat.EFFICIENCY, 1.00f));
        MachineRegistry.registerMachineVariant(new MachineVariant("brick", new FuelInfo(FuelType.FURNACE),
                "minecraft:blocks/brick", new RecipeItem(new ItemStack(Items.BRICK, 8)),
                new RecipeItem(new ItemStack(Blocks.BRICK_BLOCK, 1)), new RecipeItem(new ItemStack(Items.BRICK, 8)), 1)
                .setStat(MachineStat.MAXHU, 2200).setStat(MachineStat.SPEED, .28f)
                .setStat(MachineStat.EFFICIENCY, 1.43f));
        MachineRegistry.registerMachineVariant(
                new MachineVariant("netherbrick", new FuelInfo(FuelType.ITEM, 300, "blaze_powder"),
                        "minecraft:blocks/nether_brick", new RecipeItem(new ItemStack(Items.NETHERBRICK, 8)),
                        new RecipeItem(new ItemStack(Blocks.NETHER_BRICK, 1)),
                        new RecipeItem(new ItemStack(Items.NETHERBRICK, 8)), 2).setStat(MachineStat.MAXHU, 3072)
                        .setStat(MachineStat.SPEED, .45f).setStat(MachineStat.EFFICIENCY, .27f));
        MachineRegistry.registerMachineVariant(new MachineVariant("endstone",
                new FuelInfo(FuelType.ITEM, 2200, "ender_pearl"), "minecraft:blocks/end_stone",
                new RecipeItem(new ItemStack(Blocks.END_STONE, 8)), new RecipeItem(new ItemStack(Blocks.END_BRICKS, 1)),
                new RecipeItem(new ItemStack(Blocks.END_STONE, 8)), 5).setStat(MachineStat.MAXHU, 2670)
                .setStat(MachineStat.SPEED, 3.47f).setStat(MachineStat.EFFICIENCY, 3.42f));
        MachineRegistry.registerMachineVariant(new MachineVariant("iron", new FuelInfo(FuelType.FURNACE),
                "varyingmachina:blocks/ironmachine", new RecipeOreDictPriority(1, "plateIron", "ingotIron"),
                new RecipeOreDictPriority(1, "gearIron", "blockIron"),
                new RecipeOreDictPriority(1, "rodIron", "plateIron", "ingotIron"), 3).setStat(MachineStat.MAXHU, 1538)
                .setStat(MachineStat.SPEED, 1.21f).setStat(MachineStat.EFFICIENCY, 1.16f));
        MachineRegistry.registerMachineVariant(new MachineVariant("bronze", new FuelInfo(FuelType.FLUID, 1, "steam"),
                "varyingmachina:blocks/bronzemachine", new RecipeOreDictPriority(1, "plateBronze", "ingotBronze"),
                new RecipeOreDictPriority(1, "gearBronze", "blockBronze"),
                new RecipeOreDictPriority(1, "rodBronze", "plateBronze", "ingotBronze"), 2)
                .setStat(MachineStat.MAXHU, 950).setStat(MachineStat.SPEED, 1.46f)
                .setStat(MachineStat.EFFICIENCY, 1.34f));
        MachineRegistry.registerMachineVariant(new MachineVariant("steel", new FuelInfo(FuelType.FURNACE),
                "varyingmachina:blocks/steelmachine", new RecipeOreDictPriority(1, "plateSteel", "ingotSteel"),
                new RecipeOreDictPriority(1, "gearSteel", "blockSteel"),
                new RecipeOreDictPriority(1, "rodSteel", "plateSteel", "ingotSteel"), 3)
                .setStat(MachineStat.MAXHU, 1370).setStat(MachineStat.SPEED, 1.23f)
                .setStat(MachineStat.EFFICIENCY, 3.10f));
        MachineRegistry.registerMachineVariant(new MachineVariant("lead", new FuelInfo(FuelType.FURNACE),
                "varyingmachina:blocks/leadmachine", new RecipeOreDictPriority(1, "plateLead", "ingotLead"),
                new RecipeOreDictPriority(1, "gearLead", "blockLead"),
                new RecipeOreDictPriority(1, "rodLead", "plateLead", "ingotLead"), 2).setStat(MachineStat.MAXHU, 328)
                .setStat(MachineStat.SPEED, 4.28f).setStat(MachineStat.EFFICIENCY, .05f));
        MachineRegistry.registerMachineVariant(new MachineVariant("constantan", new FuelInfo(FuelType.RF, 10),
                "varyingmachina:blocks/constantanmachine",
                new RecipeOreDictPriority(1, "plateConstantan", "ingotConstantan"),
                new RecipeOreDictPriority(1, "gearConstantan", "blockConstantan"),
                new RecipeOreDictPriority(1, "rodConstantan", "plateConstantan", "ingotConstantan"), 3)
                .setStat(MachineStat.MAXHU, 1252).setStat(MachineStat.SPEED, 2.00f)
                .setStat(MachineStat.EFFICIENCY, 2.00f));
        MachineRegistry.registerMachineVariant(new MachineVariant("invar", new FuelInfo(FuelType.RF, 10),
                "varyingmachina:blocks/invarmachine", new RecipeOreDictPriority(1, "plateInvar", "ingotInvar"),
                new RecipeOreDictPriority(1, "gearInvar", "blockInvar"),
                new RecipeOreDictPriority(1, "rodInvar", "plateInvar", "ingotInvar"), 3)
                .setStat(MachineStat.MAXHU, 1427).setStat(MachineStat.SPEED, 1.82f)
                .setStat(MachineStat.EFFICIENCY, 2.49f));
        MachineRegistry.registerMachineVariant(new MachineVariant("electrum", new FuelInfo(FuelType.RF, 10),
                "varyingmachina:blocks/electrummachine", new RecipeOreDictPriority(1, "plateElectrum", "ingotElectrum"),
                new RecipeOreDictPriority(1, "gearElectrum", "blockElectrum"),
                new RecipeOreDictPriority(1, "rodElectrum", "plateElectrum", "ingotElectrum"), 4)
                .setStat(MachineStat.MAXHU, 1878).setStat(MachineStat.SPEED, 3.37f)
                .setStat(MachineStat.EFFICIENCY, .83f));
        MachineRegistry.registerMachineVariant(new MachineVariant("signalum", new FuelInfo(FuelType.RF, 10),
                "varyingmachina:blocks/signalummachine", new RecipeOreDictPriority(1, "plateSignalum", "ingotSignalum"),
                new RecipeOreDictPriority(1, "gearSignalum", "blockSignalum"),
                new RecipeOreDictPriority(1, "rodSignalum", "plateSignalum", "ingotSignalum"), 4)
                .setStat(MachineStat.MAXHU, 1362).setStat(MachineStat.SPEED, 3.82f)
                .setStat(MachineStat.EFFICIENCY, 1.49f));
        MachineRegistry.registerMachineVariant(new MachineVariant("enderium", new FuelInfo(FuelType.RF, 10),
                "varyingmachina:blocks/enderiummachine", new RecipeOreDictPriority(1, "plateEnderium", "ingotEnderium"),
                new RecipeOreDictPriority(1, "gearEnderium", "blockEnderium"),
                new RecipeOreDictPriority(1, "rodEnderium", "plateEnderium", "ingotEnderium"), 5)
                .setStat(MachineStat.MAXHU, 2164).setStat(MachineStat.SPEED, 3.42f)
                .setStat(MachineStat.EFFICIENCY, 4.20f));
        MachineRegistry.registerMachineVariant(new MachineVariant("osmium", new FuelInfo(FuelType.RF, 10),
                "varyingmachina:blocks/osmiummachine", new RecipeOreDictPriority(1, "plateOsmium", "ingotOsmium"),
                new RecipeOreDictPriority(1, "gearOsmium", "blockOsmium"),
                new RecipeOreDictPriority(1, "rodOsmium", "plateOsmium", "ingotOsmium"), 3)
                .setStat(MachineStat.MAXHU, 3033).setStat(MachineStat.SPEED, 1.63f)
                .setStat(MachineStat.EFFICIENCY, 0.45f));
        MachineRegistry
                .registerMachineVariant(new MachineVariant("refinedobsidian", new FuelInfo(FuelType.RF, 10),
                        "varyingmachina:blocks/refinedobsidianmachine",
                        new RecipeOreDictPriority(1, "plateRefinedObsidian", "ingotRefinedObsidian"),
                        new RecipeOreDictPriority(1, "gearRefinedObsidian", "blockRefinedObsidian"),
                        new RecipeOreDictPriority(1, "rodRefinedObsidian", "plateRefinedObsidian",
                                "ingotRefinedObsidian"),
                        5).setStat(MachineStat.MAXHU, 3768).setStat(MachineStat.SPEED, 2.71f)
                        .setStat(MachineStat.EFFICIENCY, 5.46f));
        MachineRegistry.registerMachineVariant(new MachineVariant("manasteel", new FuelInfo(FuelType.MANA, 10),
                "varyingmachina:blocks/manasteelmachine",
                new RecipeOreDictPriority(1, "plateManasteel", "ingotManasteel"),
                new RecipeOreDictPriority(1, "gearManasteel", "blockManasteel"),
                new RecipeOreDictPriority(1, "rodManasteel", "plateManasteel", "ingotManasteel"), 3)
                .setStat(MachineStat.MAXHU, 1164).setStat(MachineStat.SPEED, 1.48f)
                .setStat(MachineStat.EFFICIENCY, 1.22f));
        MachineRegistry.registerMachineVariant(new MachineVariant("terrasteel", new FuelInfo(FuelType.MANA, 10),
                "varyingmachina:blocks/terrasteelmachine",
                new RecipeOreDictPriority(1, "plateTerrasteel", "ingotTerrasteel"),
                new RecipeOreDictPriority(1, "gearTerrasteel", "blockTerrasteel"),
                new RecipeOreDictPriority(1, "rodTerrasteel", "plateTerrasteel", "ingotTerrasteel"), 4)
                .setStat(MachineStat.MAXHU, 3277).setStat(MachineStat.SPEED, 1.54f)
                .setStat(MachineStat.EFFICIENCY, 3.36f));
        MachineRegistry.registerMachineVariant(new MachineVariant("elementium", new FuelInfo(FuelType.MANA, 10),
                "varyingmachina:blocks/elementiummachine",
                new RecipeOreDictPriority(1, "plateElementium", "ingotElementium"),
                new RecipeOreDictPriority(1, "gearElementium", "blockElementium"),
                new RecipeOreDictPriority(1, "rodElementium", "plateElementium", "ingotElementium"), 4)
                .setStat(MachineStat.MAXHU, 864).setStat(MachineStat.SPEED, 3.72f)
                .setStat(MachineStat.EFFICIENCY, 2.21f));
        MachineRegistry.registerMachineVariant(new MachineVariant("cobalt", new FuelInfo(FuelType.FLUID, 1, "lava"),
                "varyingmachina:blocks/cobaltmachine", new RecipeOreDictPriority(1, "plateCobalt", "ingotCobalt"),
                new RecipeOreDictPriority(1, "gearCobalt", "blockCobalt"),
                new RecipeOreDictPriority(1, "rodCobalt", "plateCobalt", "ingotCobalt"), 3)
                .setStat(MachineStat.MAXHU, 1495).setStat(MachineStat.SPEED, 3.6f)
                .setStat(MachineStat.EFFICIENCY, 1.1f));
        MachineRegistry.registerMachineVariant(new MachineVariant("ardite", new FuelInfo(FuelType.FLUID, 1, "lava"),
                "varyingmachina:blocks/arditemachine", new RecipeOreDictPriority(1, "plateArdite", "ingotArdite"),
                new RecipeOreDictPriority(1, "gearArdite", "blockArdite"),
                new RecipeOreDictPriority(1, "rodArdite", "plateArdite", "ingotArdite"), 3)
                .setStat(MachineStat.MAXHU, 3153).setStat(MachineStat.SPEED, 1.3f)
                .setStat(MachineStat.EFFICIENCY, 3.9f));
        MachineRegistry.registerMachineVariant(new MachineVariant("manyullyn", new FuelInfo(FuelType.FLUID, 1, "lava"),
                "varyingmachina:blocks/manyullynmachine",
                new RecipeOreDictPriority(1, "plateManyullyn", "ingotManyullyn"),
                new RecipeOreDictPriority(1, "gearManyullyn", "blockManyullyn"),
                new RecipeOreDictPriority(1, "rodManuyllyn", "plateManyullyn", "ingotManyullyn"), 4)
                .setStat(MachineStat.MAXHU, 2324).setStat(MachineStat.SPEED, 2.45f)
                .setStat(MachineStat.EFFICIENCY, 2.5f));
        MachineRegistry
                .registerMachineVariant(new MachineVariant("electricalsteel", new FuelInfo(FuelType.RF, 10),
                        "varyingmachina:blocks/electricalsteelmachine",
                        new RecipeOreDictPriority(1, "plateElectricalSteel", "ingotElectricalSteel"),
                        new RecipeOreDictPriority(1, "gearElectricalSteel", "blockElectricalSteel"),
                        new RecipeOreDictPriority(1, "rodElectricalSteel", "plateElectricalSteel",
                                "ingotElectricalSteel"),
                        3).setStat(MachineStat.MAXHU, 1500).setStat(MachineStat.SPEED, 1.14f)
                        .setStat(MachineStat.EFFICIENCY, 2.90f));
        MachineRegistry
                .registerMachineVariant(
                        new MachineVariant("energeticalloy", new FuelInfo(FuelType.RF, 10),
                                "varyingmachina:blocks/energeticalloymachine",
                                new RecipeOreDictPriority(1, "plateEnergeticAlloy", "ingotEnergeticAlloy"),
                                new RecipeOreDictPriority(1, "gearEnergeticAlloy", "blockEnergeticAlloy"),
                                new RecipeOreDictPriority(1, "rodEnergeticAlloy", "plateEnergeticAlloy",
                                        "ingotEnergeticAlloy"),
                                3).setStat(MachineStat.MAXHU, 1023).setStat(MachineStat.SPEED, 1.92f)
                                .setStat(MachineStat.EFFICIENCY, 1.01f));
        MachineRegistry
                .registerMachineVariant(
                        new MachineVariant("vibrantalloy", new FuelInfo(FuelType.RF, 10),
                                "varyingmachina:blocks/vibrantalloymachine",
                                new RecipeOreDictPriority(1, "plateVibrantAlloy", "ingotVibrantAlloy"),
                                new RecipeOreDictPriority(1, "gearVibrantAlloy", "blockVibrantAlloy"),
                                new RecipeOreDictPriority(1, "rodVibrantAlloy", "plateVibrantAlloy",
                                        "ingotVibrantAlloy"),
                                5).setStat(MachineStat.MAXHU, 1644).setStat(MachineStat.SPEED, 5.45f)
                                .setStat(MachineStat.EFFICIENCY, 1.46f));
        MachineRegistry.registerMachineVariant(new MachineVariant("soularium",
                new FuelInfo(FuelType.ITEM, 180, "rotten_flesh"), "varyingmachina:blocks/soulariummachine",
                new RecipeOreDictPriority(1, "plateSoularium", "ingotSoularium"),
                new RecipeOreDictPriority(1, "gearSoularium", "blockSoularium"),
                new RecipeOreDictPriority(1, "rodSoularium", "plateSoularium", "ingotSoularium"), 4)
                .setStat(MachineStat.MAXHU, 1475).setStat(MachineStat.SPEED, 1.48f)
                .setStat(MachineStat.EFFICIENCY, 2.73f));
        MachineRegistry.registerMachineVariant(
                new MachineVariant("darksteel", new FuelInfo(FuelType.RF, 10), "varyingmachina:blocks/darksteelmachine",
                        new RecipeOreDictPriority(1, "plateDarkSteel", "ingotDarkSteel"),
                        new RecipeOreDictPriority(1, "gearDarkSteel", "blockDarkSteel"),
                        new RecipeOreDictPriority(1, "rodDarkSteel", "plateDarkSteel", "ingotDarkSteel"), 5)
                        .setStat(MachineStat.MAXHU, 3765).setStat(MachineStat.SPEED, 1.88f)
                        .setStat(MachineStat.EFFICIENCY, 4.40f));
        MachineRegistry.registerMachineVariant(new MachineVariant("darkmatter",
                new FuelInfo(FuelType.ITEM, 98696, "varyingmachina:darkmatter"), "varyingmachina:blocks/darkmatter",
                new RecipeNameItem(new ResourceLocation(References.ModID, "darkmatter"), 1), new RecipeItem(new ItemStack(Items.NETHER_STAR)),
                new RecipeNameItem(new ResourceLocation(References.ModID, "darkmatter"), 1), 6).setStat(MachineStat.MAXHU, 4042)
                .setStat(MachineStat.SPEED, 10.00f).setStat(MachineStat.EFFICIENCY, 1.00f));
        MachineRegistry.registerMachineVariant(
                new MachineVariant("lightmatter", new FuelInfo(FuelType.ITEM, 73890, "varyingmachina:lightmatter"),
                        "varyingmachina:blocks/lightmatter", new RecipeNameItem(new ResourceLocation(References.ModID, "lightmatter"), 1),
                        new RecipeItem(new ItemStack(Blocks.PURPUR_BLOCK)),
                        new RecipeNameItem(new ResourceLocation(References.ModID, "lightmatter"), 1), 6).setStat(MachineStat.MAXHU, 1566)
                        .setStat(MachineStat.SPEED, 1.00f).setStat(MachineStat.EFFICIENCY, 10.00f));
    }
}
