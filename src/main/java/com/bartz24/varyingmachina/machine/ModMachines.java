package com.bartz24.varyingmachina.machine;

import com.bartz24.varyingmachina.inventory.EnergyFuelUnit;
import com.bartz24.varyingmachina.inventory.FuelUnit;
import com.bartz24.varyingmachina.inventory.ItemFurnaceFuelUnit;
import com.bartz24.varyingmachina.machine.types.*;
import net.minecraft.util.text.TextFormatting;

import java.util.HashMap;
import java.util.function.Function;

public class ModMachines {
    public static HashMap<String, MachineType> types = new HashMap<>();

    public static MachineSmelter smelter = new MachineSmelter();

    public static MachineFreezer freezer = new MachineFreezer();

    public static MachineGrinder grinder = new MachineGrinder();

    public static MachineFabricator fabricator = new MachineFabricator();

    public static MachineGenerator generator = new MachineGenerator();

    public static MachineMiner miner = new MachineMiner();

    public static MachineReserve reserve = new MachineReserve();

    public static MachineStorageBuffer storagebuffer = new MachineStorageBuffer();

    public static MachineMover mover = new MachineMover();

    public static MachineEnergyRelay energyrelay = new MachineEnergyRelay();

    public static class Stats {

        public static final MachineStat speed = new MachineStat("speed", TextFormatting.BLUE) {
            @Override
            public double calculateStat(MachineType type, MachineVariant main, MachineVariant casing) {
                return main.getStat(this) / 100d;
            }
        };
        public static final MachineStat size = new MachineStat("size", TextFormatting.DARK_BLUE) {
            @Override
            public double calculateStat(MachineType type, MachineVariant main, MachineVariant casing) {
                return (int) Math.pow(2, main.getStat(speed) / 100d);
            }

            @Override
            public String getText(MachineType type, MachineVariant main, MachineVariant casing) {
                return (int) calculateStat(type, main, casing) + "";
            }
        };
        public static final MachineStat efficiency = new MachineStat("efficiency", TextFormatting.GREEN);
        public static final MachineStat fuelefficiency = new MachineStat("fuelefficiency", TextFormatting.GREEN) {
            @Override
            public double calculateStat(MachineType type, MachineVariant main, MachineVariant casing) {
                return efficiency.calculateStat(type, main, casing) / speed.calculateStat(type, main, casing) / type.getEnergyRateMultiplier();
            }
        };
        public static final MachineStat production = new MachineStat("production", TextFormatting.DARK_GREEN) {
            @Override
            public double calculateStat(MachineType type, MachineVariant main, MachineVariant casing) {
                return (main.getStat(efficiency) * 0.8d + casing.getStat(efficiency) * 0.2d) / Math.pow(main.getStat(speed), 1.05f);
            }
        };
        public static final MachineStat hu = new MachineStat("hu", TextFormatting.RED) {
            @Override
            public double calculateStat(MachineType type, MachineVariant main, MachineVariant casing) {
                return (int) (main.getStat(pressure) + casing.getStat(pressure)) * (casing.getStat(efficiency) * 0.80d + main.getStat(speed) * 0.20d) / 100d;
            }

            @Override
            public String getText(MachineType type, MachineVariant main, MachineVariant casing) {
                return (int) calculateStat(type, main, casing) + "";
            }

            @Override
            public String getReqText(double value) {
                return (int) value + "";
            }
        };
        public static final MachineStat pressure = new MachineStat("pressure", TextFormatting.YELLOW) {
            @Override
            public double calculateStat(MachineType type, MachineVariant main, MachineVariant casing) {
                return (int) main.getStat(pressure);
            }

            @Override
            public String getText(MachineType type, MachineVariant main, MachineVariant casing) {
                return (int) calculateStat(type, main, casing) + "";
            }

            @Override
            public String getReqText(double value) {
                return (int) value + "";
            }
        };
        public static final MachineStat rating = new MachineStat("rating", TextFormatting.YELLOW) {
            @Override
            public double calculateStat(MachineType type, MachineVariant main, MachineVariant casing) {
                double stat = speed.calculateStat(type, main, casing);
                stat += efficiency.calculateStat(type, main, casing);
                stat += pressure.calculateStat(type, main, casing) / 250d;
                return stat / 3 + 1;
            }

            @Override
            public String getText(MachineType type, MachineVariant main, MachineVariant casing) {
                String text = "";
                for (int i = 0; i < (int) calculateStat(type, main, casing); i++)
                    text += "\u2605";
                return text + " (" + (int) calculateStat(type, main, casing) + ")";
            }

            @Override
            public String getReqText(double value) {
                String text = "";
                for (int i = 0; i < (int) value; i++)
                    text += "\u2605";
                return text + " (" + (int) value + ")";
            }
        };
    }

    public static class FuelUnits {
        public static Function<Integer, FuelUnit> createNewFurnaceFuelUnit = (size) -> {
            return new ItemFurnaceFuelUnit(size);
        };
        public static Function<Integer, FuelUnit> createNewEnergyFuelUnit = (capacity) -> {
            return new EnergyFuelUnit(capacity);
        };
    }
}
