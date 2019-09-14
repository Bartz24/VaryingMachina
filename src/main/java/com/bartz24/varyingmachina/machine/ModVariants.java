package com.bartz24.varyingmachina.machine;

import com.bartz24.varyingmachina.recipe.InputItemTag;
import com.bartz24.varyingmachina.recipe.InputItemTagListPriority;
import com.bartz24.varyingmachina.recipe.InputNameItem;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

public class ModVariants {
    public static HashMap<String, MachineVariant> types = new HashMap<>();

    public static final MachineVariant dirt = new MachineVariant(
            "dirt",
            new ResourceLocation("varyingmachina", "block/dirtmachine"))
            .setStat(ModMachines.Stats.speed, 68)
            .setStat(ModMachines.Stats.efficiency, 34)
            .setStat(ModMachines.Stats.pressure, 20)
            .setFuelUnitSupplier(ModMachines.FuelUnits.createNewFurnaceFuelUnit).setFuelUnitSize(1)
            .setRecipeItems(
                    new InputNameItem(new ResourceLocation("dirt"), 1),
                    new InputNameItem(new ResourceLocation("dirt"), 1),
                    new InputNameItem(new ResourceLocation("dirt"), 3)
            );
    public static final MachineVariant wood = new MachineVariant(
            "wood",
            new ResourceLocation("varyingmachina", "block/woodmachine"))
            .setStat(ModMachines.Stats.speed, 55)
            .setStat(ModMachines.Stats.efficiency, 78)
            .setStat(ModMachines.Stats.pressure, 95)
            .setFuelUnitSupplier(ModMachines.FuelUnits.createNewFurnaceFuelUnit).setFuelUnitSize(1)
            .setRecipeItems(
                    new InputItemTag(new ResourceLocation("planks"), 1),
                    new InputItemTag(new ResourceLocation("logs"), 1),
                    new InputItemTagListPriority(1, new ResourceLocation("forge", "gears/wood"), new ResourceLocation("forge", "gears/wooden"), new ResourceLocation("logs"))
            );
    public static final MachineVariant stone = new MachineVariant(
            "stone",
            new ResourceLocation("varyingmachina", "block/stonemachine"))
            .setStat(ModMachines.Stats.speed, 72)
            .setStat(ModMachines.Stats.efficiency, 94)
            .setStat(ModMachines.Stats.pressure, 165)
            .setFuelUnitSupplier(ModMachines.FuelUnits.createNewFurnaceFuelUnit).setFuelUnitSize(1)
            .setRecipeItems(
                    new InputNameItem(new ResourceLocation("stone"), 1),
                    new InputItemTag(new ResourceLocation("stone_bricks"), 1),
                    new InputItemTagListPriority(1, new ResourceLocation("forge", "gears/stone"), new ResourceLocation("stone_bricks"))
            );
    public static final MachineVariant endstone = new MachineVariant(
            "endstone",
            new ResourceLocation("varyingmachina", "block/endstonemachine"))
            .setStat(ModMachines.Stats.speed, 148)
            .setStat(ModMachines.Stats.efficiency, 182)
            .setStat(ModMachines.Stats.pressure, 330)
            .setFuelUnitSupplier(ModMachines.FuelUnits.createNewFurnaceFuelUnit).setFuelUnitSize(1)
            .setRecipeItems(
                    new InputNameItem(new ResourceLocation("end_stone"), 1),
                    new InputNameItem(new ResourceLocation("end_stone_bricks"), 1),
                    new InputNameItem(new ResourceLocation("end_stone_bricks"), 2)
            );
    public static final MachineVariant brick = new MachineVariant(
            "brick",
            new ResourceLocation("varyingmachina", "block/brickmachine"))
            .setStat(ModMachines.Stats.speed, 44)
            .setStat(ModMachines.Stats.efficiency, 132)
            .setStat(ModMachines.Stats.pressure, 880)
            .setFuelUnitSupplier(ModMachines.FuelUnits.createNewFurnaceFuelUnit).setFuelUnitSize(1)
            .setRecipeItems(
                    new InputNameItem(new ResourceLocation("brick"), 1),
                    new InputNameItem(new ResourceLocation("bricks"), 1),
                    new InputNameItem(new ResourceLocation("bricks"), 1)
            );
    public static final MachineVariant iron = new MachineVariant(
            "iron",
            new ResourceLocation("varyingmachina", "block/ironmachine"))
            .setStat(ModMachines.Stats.speed, 120)
            .setStat(ModMachines.Stats.efficiency, 112)
            .setStat(ModMachines.Stats.pressure, 540)
            .setFuelUnitSupplier(ModMachines.FuelUnits.createNewFurnaceFuelUnit).setFuelUnitSize(1)
            .setRecipeItems(
                    new InputItemTag(new ResourceLocation("forge", "ingots/iron"), 1),
                    new InputItemTag(new ResourceLocation("forge", "plates/iron"), 1),
                    new InputItemTag(new ResourceLocation("forge", "gears/iron"), 1)
            );
    public static final MachineVariant gold = new MachineVariant(
            "gold",
            new ResourceLocation("varyingmachina", "block/goldmachine"))
            .setStat(ModMachines.Stats.speed, 205)
            .setStat(ModMachines.Stats.efficiency, 31)
            .setStat(ModMachines.Stats.pressure, 120)
            .setFuelUnitSupplier(ModMachines.FuelUnits.createNewFurnaceFuelUnit).setFuelUnitSize(1)
            .setRecipeItems(
                    new InputItemTag(new ResourceLocation("forge", "ingots/gold"), 1),
                    new InputItemTag(new ResourceLocation("forge", "plates/gold"), 1),
                    new InputItemTag(new ResourceLocation("forge", "gears/gold"), 1)
            );
    public static final MachineVariant silver = new MachineVariant(
            "silver",
            new ResourceLocation("varyingmachina", "block/silvermachine"))
            .setStat(ModMachines.Stats.speed, 136)
            .setStat(ModMachines.Stats.efficiency, 144)
            .setStat(ModMachines.Stats.pressure, 140)
            .setFuelUnitSupplier(ModMachines.FuelUnits.createNewFurnaceFuelUnit).setFuelUnitSize(1)
            .setRecipeItems(
                    new InputItemTag(new ResourceLocation("forge", "ingots/silver"), 1),
                    new InputItemTag(new ResourceLocation("forge", "plates/silver"), 1),
                    new InputItemTag(new ResourceLocation("forge", "gears/silver"), 1)
            );
    public static final MachineVariant copper = new MachineVariant(
            "copper",
            new ResourceLocation("varyingmachina", "block/coppermachine"))
            .setStat(ModMachines.Stats.speed, 104)
            .setStat(ModMachines.Stats.efficiency, 88)
            .setStat(ModMachines.Stats.pressure, 210)
            .setFuelUnitSupplier(ModMachines.FuelUnits.createNewFurnaceFuelUnit).setFuelUnitSize(1)
            .setRecipeItems(
                    new InputItemTag(new ResourceLocation("forge", "ingots/copper"), 1),
                    new InputItemTag(new ResourceLocation("forge", "plates/copper"), 1),
                    new InputItemTag(new ResourceLocation("forge", "gears/copper"), 1)
            );
    public static final MachineVariant tin = new MachineVariant(
            "tin",
            new ResourceLocation("varyingmachina", "block/tinmachine"))
            .setStat(ModMachines.Stats.speed, 92)
            .setStat(ModMachines.Stats.efficiency, 105)
            .setStat(ModMachines.Stats.pressure, 220)
            .setFuelUnitSupplier(ModMachines.FuelUnits.createNewFurnaceFuelUnit).setFuelUnitSize(1)
            .setRecipeItems(
                    new InputItemTag(new ResourceLocation("forge", "ingots/tin"), 1),
                    new InputItemTag(new ResourceLocation("forge", "plates/tin"), 1),
                    new InputItemTag(new ResourceLocation("forge", "gears/tin"), 1)
            );
    public static final MachineVariant bronze = new MachineVariant(
            "bronze",
            new ResourceLocation("varyingmachina", "block/bronzemachine"))
            .setStat(ModMachines.Stats.speed, 114)
            .setStat(ModMachines.Stats.efficiency, 100)
            .setStat(ModMachines.Stats.pressure, 305)
            .setFuelUnitSupplier(ModMachines.FuelUnits.createNewFurnaceFuelUnit).setFuelUnitSize(1)
            .setRecipeItems(
                    new InputItemTag(new ResourceLocation("forge", "ingots/bronze"), 1),
                    new InputItemTag(new ResourceLocation("forge", "plates/bronze"), 1),
                    new InputItemTag(new ResourceLocation("forge", "gears/bronze"), 1)
            );
    public static final MachineVariant ferrotin = new MachineVariant(
            "ferrotin",
            new ResourceLocation("varyingmachina", "block/ferrotinmachine"))
            .setStat(ModMachines.Stats.speed, 126)
            .setStat(ModMachines.Stats.efficiency, 118)
            .setStat(ModMachines.Stats.pressure, 415)
            .setFuelUnitSupplier(ModMachines.FuelUnits.createNewEnergyFuelUnit).setFuelUnitSize(2500)
            .setRecipeItems(
                    new InputItemTag(new ResourceLocation("forge", "ingots/ferrotin"), 1),
                    new InputItemTag(new ResourceLocation("forge", "plates/ferrotin"), 1),
                    new InputItemTag(new ResourceLocation("forge", "gears/ferrotin"), 1)
            );
    public static final MachineVariant mythril = new MachineVariant(
            "mythril",
            new ResourceLocation("varyingmachina", "block/mythrilmachine"))
            .setStat(ModMachines.Stats.speed, 165)
            .setStat(ModMachines.Stats.efficiency, 98)
            .setStat(ModMachines.Stats.pressure, 410)
            .setFuelUnitSupplier(ModMachines.FuelUnits.createNewEnergyFuelUnit).setFuelUnitSize(6000)
            .setRecipeItems(
                    new InputItemTag(new ResourceLocation("forge", "ingots/mythril"), 1),
                    new InputItemTag(new ResourceLocation("forge", "plates/mythril"), 1),
                    new InputItemTag(new ResourceLocation("forge", "gears/mythril"), 1)
            );
    public static final MachineVariant steel = new MachineVariant(
            "steel",
            new ResourceLocation("varyingmachina", "block/steelmachine"))
            .setStat(ModMachines.Stats.speed, 140)
            .setStat(ModMachines.Stats.efficiency, 136)
            .setStat(ModMachines.Stats.pressure, 370)
            .setFuelUnitSupplier(ModMachines.FuelUnits.createNewFurnaceFuelUnit).setFuelUnitSize(1)
            .setRecipeItems(
                    new InputItemTag(new ResourceLocation("forge", "ingots/steel"), 1),
                    new InputItemTag(new ResourceLocation("forge", "plates/steel"), 1),
                    new InputItemTag(new ResourceLocation("forge", "gears/steel"), 1)
            );
    public static final MachineVariant redcopper = new MachineVariant(
            "redcopper",
            new ResourceLocation("varyingmachina", "block/redcoppermachine"))
            .setStat(ModMachines.Stats.speed, 235)
            .setStat(ModMachines.Stats.efficiency, 109)
            .setStat(ModMachines.Stats.pressure, 320)
            .setFuelUnitSupplier(ModMachines.FuelUnits.createNewEnergyFuelUnit).setFuelUnitSize(8000)
            .setRecipeItems(
                    new InputItemTag(new ResourceLocation("forge", "ingots/redcopper"), 1),
                    new InputItemTag(new ResourceLocation("forge", "plates/redcopper"), 1),
                    new InputItemTag(new ResourceLocation("forge", "gears/redcopper"), 1)
            );
    public static final MachineVariant mythrilsteel = new MachineVariant(
            "mythrilsteel",
            new ResourceLocation("varyingmachina", "block/mythrilsteelmachine"))
            .setStat(ModMachines.Stats.speed, 190)
            .setStat(ModMachines.Stats.efficiency, 162)
            .setStat(ModMachines.Stats.pressure, 720)
            .setFuelUnitSupplier(ModMachines.FuelUnits.createNewEnergyFuelUnit).setFuelUnitSize(12000)
            .setRecipeItems(
                    new InputItemTag(new ResourceLocation("forge", "ingots/mythrilsteel"), 1),
                    new InputItemTag(new ResourceLocation("forge", "plates/mythrilsteel"), 1),
                    new InputItemTag(new ResourceLocation("forge", "gears/mythrilsteel"), 1)
            );
}
