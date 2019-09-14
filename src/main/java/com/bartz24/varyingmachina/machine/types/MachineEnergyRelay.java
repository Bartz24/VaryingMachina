package com.bartz24.varyingmachina.machine.types;

import com.bartz24.varyingmachina.inventory.OmniEnergyHandler;
import com.bartz24.varyingmachina.machine.MachineExtraComponents;
import com.bartz24.varyingmachina.machine.MachineType;
import com.bartz24.varyingmachina.machine.MachineVariant;
import com.bartz24.varyingmachina.machine.ModMachines;
import com.bartz24.varyingmachina.tile.TileEntityMachine;
import javafx.util.Pair;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MachineEnergyRelay extends MachineType<TileEntityMachine> {
    public MachineEnergyRelay() {
        super("energyrelay", ModMachines.Stats.rating);
        this.setNoFuel();
        this.setNoRecipes();
        this.setOverrideUpdateFunction(this::update);
        this.setExtraInput(MachineExtraComponents.mover);
        this.setDisplayRange(this::getDisplayRange);
        this.addDoubleEnergyHandler("energy");
        this.setNoMultiblock();
    }


    @Override
    public OmniEnergyHandler createEnergyHandler(TileEntityMachine tile) {
        OmniEnergyHandler handler = super.createEnergyHandler(tile);
        handler.getHandler("energy").setCapacity((int) calculateSpecialStat("maxRF", tile.getMainMachineVariant(), tile.getCasingMachineVariant()));
        return handler;
    }

    @Override
    public List<String> getSpecialTooltipTypes() {
        List<String> list = new ArrayList<>();
        list.add("maxRF");
        list.add("rfTick");
        list.add("maxRange");
        return list;
    }

    @Override
    public double calculateSpecialStat(String statName, MachineVariant mainVariant, MachineVariant casingVariant) {
        if (statName.equals("maxRF"))
            return (int) (ModMachines.Stats.hu.calculateStat(this, mainVariant, casingVariant) / 10d * casingVariant.getStat(ModMachines.Stats.pressure) * Math.pow(ModMachines.Stats.production.calculateStat(this, mainVariant, casingVariant), 1.2)) + 1;
        else if (statName.equals("rfTick"))
            return (int) Math.min(calculateSpecialStat("maxRF", mainVariant, casingVariant), Math.pow(ModMachines.Stats.speed.calculateStat(this, mainVariant, casingVariant), 2.7) / Math.pow(ModMachines.Stats.production.calculateStat(this, mainVariant, casingVariant), 1.2) * 640 + 1);
        else if (statName.equals("maxRange"))
            return (int) (Math.pow(ModMachines.Stats.speed.calculateStat(this, mainVariant, casingVariant), 1.68) * casingVariant.getStat(ModMachines.Stats.pressure) / 140d) + 1;

        return super.calculateSpecialStat(statName, mainVariant, casingVariant);
    }

    @Override
    public String getTextSpecialStat(String statName, MachineVariant mainVariant, MachineVariant casingVariant) {
        if (statName.equals("maxRF"))
            return (int) calculateSpecialStat("maxRF", mainVariant, casingVariant) + "";
        else if (statName.equals("rfTick"))
            return (int) calculateSpecialStat("rfTick", mainVariant, casingVariant) + "";
        else if (statName.equals("maxRange"))
            return (int) calculateSpecialStat("maxRange", mainVariant, casingVariant) + "";
        return super.getTextSpecialStat(statName, mainVariant, casingVariant);
    }

    @Override
    public TextFormatting getColorSpecialStat(String statName) {
        if (statName.equals("maxRF"))
            return TextFormatting.GOLD;
        else if (statName.equals("rfTick"))
            return TextFormatting.BLUE;
        else if (statName.equals("maxRange"))
            return TextFormatting.LIGHT_PURPLE;
        return super.getColorSpecialStat(statName);
    }

    private Pair<BlockPos[], Integer> getDisplayRange(TileEntityMachine tile) {
        List<BlockPos> list = new ArrayList<>();
        int maxRange = getMaxRange(tile);
        for (int x = -maxRange; x <= maxRange; x++) {
            for (int y = -maxRange; y <= maxRange; y++) {
                for (int z = -maxRange; z <= maxRange; z++) {
                    if (x == 0 && y == 0 && z == 0)
                        continue;

                    BlockPos pos = tile.getPos().add(x, y, z);

                    if (tile.getPos().distanceSq(pos) <= (double) maxRange * maxRange &&
                            tile.getPos().distanceSq(pos) > (double) (maxRange - 1) * (maxRange - 1)) {
                        list.add(pos);
                    }
                }
            }
        }
        return new Pair<>(list.toArray(new BlockPos[list.size()]), 0xe98d2a);
    }

    private void update(TileEntityMachine tile) {
        if (!tile.getWorld().isRemote) {
            for (Direction facing : Direction.values()) {
                IEnergyStorage energyStorage = getEnergyStorageAt(tile.getWorld(), tile.getPos().offset(facing), facing.getOpposite());
                if (energyStorage != null) {
                    int extractAmount = tile.getEnergy().getHandler("energy").receiveInternalEnergy(energyStorage.extractEnergy(tile.getEnergy().getHandler("energy").getMaxReceive(), true), true);
                    extractAmount = getDifferenceExtract(extractAmount, energyStorage, tile.getEnergy().getHandler("energy"));
                    if (extractAmount > 0) {
                        tile.getEnergy().getHandler("energy").receiveInternalEnergy(energyStorage.extractEnergy(extractAmount, false), false);
                        tile.markDirty();
                    }

                }
            }

            int maxRange = getMaxRange(tile);

            HashMap<BlockPos, Direction> targets = readTargetPoses(tile);

            tile.incrementProgress(-1);
            if (tile.getProgress() <= 0) {
                tile.setProgress(100);

                targets.clear();

                for (int x = -maxRange; x <= maxRange; x++) {
                    for (int y = -maxRange; y <= maxRange; y++) {
                        for (int z = -maxRange; z <= maxRange; z++) {
                            if (x == 0 && y == 0 && z == 0)
                                continue;

                            BlockPos pos = tile.getPos().add(x, y, z);

                            if (tile.getPos().distanceSq(pos) <= (double) maxRange * maxRange) {
                                BlockPos subtract = tile.getPos().subtract(pos);
                                Direction dir = Direction.getFacingFromVector(subtract.getX(), subtract.getY(), subtract.getZ());
                                IEnergyStorage energyStorage = getEnergyStorageAt(tile.getWorld(), pos, dir);

                                if (energyStorage != null && energyStorage.canReceive()) {
                                    targets.put(pos, dir);
                                }
                            }
                        }
                    }
                }
                writeTargetPoses(tile, targets);
                tile.markDirty();
            }

            if (targets.size() > 0 && tile.getEnergy().getHandler("energy").getEnergyStored() > 0) {
                int rfTick = getRFt(tile);

                HashMap<BlockPos, Double> weights = new HashMap<>();
                double totalWeight = 0;
                for (BlockPos pos : targets.keySet()) {
                    if (getEnergyStorageAt(tile.getWorld(), pos, targets.get(pos)) != null && getEnergyStorageAt(tile.getWorld(), pos, targets.get(pos)).receiveEnergy(Integer.MAX_VALUE, true) > 0) {
                        weights.put(pos, getWeight(tile.getPos(), pos));
                        totalWeight += getWeight(tile.getPos(), pos);
                    }
                }
                if (weights.size() > 0) {
                    for (BlockPos pos : weights.keySet()) {
                        IEnergyStorage energyStorage = getEnergyStorageAt(tile.getWorld(), pos, targets.get(pos));
                        int extractAmount = (int) Math.floor(weights.get(pos) / totalWeight * rfTick);
                        extractAmount = energyStorage.receiveEnergy(tile.getEnergy().getHandler("energy").extractInternalEnergy(extractAmount, true), true);
                        extractAmount = getDifferenceExtract(extractAmount, tile.getEnergy().getHandler("energy"), energyStorage);
                        energyStorage.receiveEnergy(tile.getEnergy().getHandler("energy").extractInternalEnergy(extractAmount, false), false);
                    }
                    tile.markDirty();
                }
            }
        }
    }

    private int getDifferenceExtract(int extractAmount, IEnergyStorage source, IEnergyStorage dest) {
        double diff = Math.abs((double) (dest.getMaxEnergyStored() - dest.getEnergyStored()) / dest.getMaxEnergyStored());

        return (int) Math.ceil(diff * extractAmount);
    }

    private int getRFt(TileEntityMachine tile) {
        return (int) tile.getMachine().calculateSpecialStat("rfTick", tile.getMainMachineVariant(), tile.getCasingMachineVariant());
    }

    private int getMaxRange(TileEntityMachine tile) {
        return (int) tile.getMachine().calculateSpecialStat("maxRange", tile.getMainMachineVariant(), tile.getCasingMachineVariant());
    }

    private IEnergyStorage getEnergyStorageAt(World world, BlockPos pos, Direction from) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null)
            return tileEntity.getCapability(CapabilityEnergy.ENERGY, from).orElse(null);
        return null;
    }

    private double getWeight(BlockPos center, BlockPos check) {
        return 100d / (center.distanceSq(check));
    }

    private HashMap<BlockPos, Direction> readTargetPoses(TileEntityMachine tile) {
        HashMap<BlockPos, Direction> map = new HashMap<>();
        ListNBT tagList = tile.getExtraData().getList("targets", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            map.put(new BlockPos(tagList.getCompound(i).getInt("x"), tagList.getCompound(i).getInt("y"), tagList.getCompound(i).getInt("z")), Direction.byName(tagList.getCompound(i).getString("dir")));
        }
        return map;
    }

    private void writeTargetPoses(TileEntityMachine tile, HashMap<BlockPos, Direction> targets) {
        ListNBT tagList = new ListNBT();
        for (BlockPos pos : targets.keySet()) {
            CompoundNBT compound = new CompoundNBT();
            compound.putInt("x", pos.getX());
            compound.putInt("y", pos.getY());
            compound.putInt("z", pos.getZ());
            compound.putString("dir", targets.get(pos).getName());
            tagList.add(compound);
        }
        tile.getExtraData().put("targets", tagList);
    }
}
