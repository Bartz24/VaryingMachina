package com.bartz24.varyingmachina.machine.types;

import com.bartz24.varyingmachina.inventory.OmniItemHandler;
import com.bartz24.varyingmachina.machine.MachineExtraComponents;
import com.bartz24.varyingmachina.machine.MachineType;
import com.bartz24.varyingmachina.machine.ModMachines;
import com.bartz24.varyingmachina.tile.TileEntityMachine;
import javafx.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class MachineMiner extends MachineType<TileEntityMachine> {
    public MachineMiner() {
        super("miner", ModMachines.Stats.rating, ModMachines.Stats.speed, ModMachines.Stats.fuelefficiency, ModMachines.Stats.size);
        this.setTextureFront(new ResourceLocation("varyingmachina", "block/miner"));
        this.addOutputItemHandler("output", 28);
        this.setNoRecipes();
        this.setOverrideUpdateFunction(this::update);
        this.setDisplayRange(this::getDisplayRange);
        this.setEnergyRateMultiplier(0.45);
        this.setExtraInput(MachineExtraComponents.miner);
    }

    @Override
    public OmniItemHandler createItemHandler(TileEntityMachine tile) {
        OmniItemHandler handler = super.createItemHandler(tile);
        handler.getHandlerRestricted("output").setSlotMultiplier(() -> (int) ModMachines.Stats.size.calculateStat(tile.getMachine(), tile.getMainMachineVariant(), tile.getCasingMachineVariant()));
        return handler;
    }

    private Pair<BlockPos[], Integer> getDisplayRange(TileEntityMachine tile) {
        List<BlockPos> list = new ArrayList<>();
        BlockPos pos = tile.getPos().subtract(tile.getDirection().getDirectionVec());
        int radius = (int) ModMachines.Stats.size.calculateStat(tile.getMachine(), tile.getMainMachineVariant(), tile.getCasingMachineVariant());
        for (int z = -radius; z <= radius; z++) {
            for (int x = -radius; x <= radius; x++) {
                Vec3i vec = tile.getDirection().getOpposite().getDirectionVec();
                list.add(pos.add(vec.getX() * radius, vec.getY() * radius, vec.getZ() * radius).add(x, -1, z));
            }
        }
        return new Pair<>(list.toArray(new BlockPos[list.size()]), 0xd91414);
    }

    ;

    private void update(TileEntityMachine tile) {

        if (!tile.getWorld().isRemote() && !tile.getExtraData().getBoolean("done")) {
            int radius = (int) ModMachines.Stats.size.calculateStat(tile.getMachine(), tile.getMainMachineVariant(), tile.getCasingMachineVariant());
            BlockPos offset = new BlockPos(tile.getExtraData().getInt("xOff"), tile.getExtraData().getInt("yOff"), tile.getExtraData().getInt("zOff"));
            NonNullList<ItemStack> list = readDropsNBT(tile.getExtraData().getCompound("drops"));

            if (offset.equals(new BlockPos(0, 0, 0)))
                offset = new BlockPos(radius % 2 == 0 ? -radius : radius, -1, -radius);
            Vec3i backOffset = tile.getDirection().getOpposite().getDirectionVec();
            BlockPos minePos = tile.getPos().add(offset).add(new Vec3i(backOffset.getX() * (radius + 1), backOffset.getY() * (radius + 1), backOffset.getZ() * (radius + 1)));
            if (tile.hasFuel() && list.size() == 0) {
                tile.getFuelUnit().drainEnergy(tile.getEnergyRate(), false);
                tile.incrementProgress(1);
                tile.markDirty();

                if (tile.getProgress() >= getTimeToBreakBlock(tile, minePos) || !canBreakBlock(tile, minePos)) {
                    tile.incrementProgress(-getTimeToBreakBlock(tile, minePos));
                    if (canBreakBlock(tile, minePos)) {
                        list.addAll(tile.getWorld().getBlockState(minePos).getDrops(new LootContext.Builder((ServerWorld) tile.getWorld()).withParameter(LootParameters.POSITION, minePos).withParameter(LootParameters.TOOL, new ItemStack(Items.DIAMOND_PICKAXE))));
                        tile.getWorld().destroyBlock(minePos, false);
                    }
                    offset = offset.add((offset.getY() % 2 == 0 ? -1 : 1) * (offset.getZ() % 2 == 0 ? 1 : -1), 0, 0);
                    if (Math.abs(offset.getX()) > radius) {
                        offset = offset.add(-(offset.getY() % 2 == 0 ? 1 : -1) * (offset.getZ() % 2 == 0 ? -1 : 1), 0, offset.getY() % 2 == 0 ? -1 : 1);
                        if (Math.abs(offset.getZ()) > radius) {
                            offset = offset.add(0, -1, offset.getY() % 2 == 0 ? 1 : -1);
                            if (tile.getPos().add(offset).getY() < 0)
                                tile.getExtraData().putBoolean("done", true);
                        }
                    }
                }
            }

            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    list.set(i, tile.getItemHandler().getHandlerRestricted("output").insertItemStacked(list.get(i), false));
                }
                for (int i = list.size() - 1; i >= 0; i--) {
                    if (list.get(i).isEmpty())
                        list.remove(i);
                }
            }
            tile.getExtraData().put("drops", writeDropsNBT(list));
            tile.getExtraData().putInt("xOff", offset.getX());
            tile.getExtraData().putInt("yOff", offset.getY());
            tile.getExtraData().putInt("zOff", offset.getZ());
        }
    }

    ;

    private boolean canBreakBlock(TileEntityMachine tile, BlockPos pos) {
        return tile.getWorld().getBlockState(pos).isAir(tile.getWorld(), pos) || tile.getWorld().getBlockState(pos).getBlockHardness(tile.getWorld(), pos) >= 0;
    }

    private int getTimeToBreakBlock(TileEntityMachine tile, BlockPos pos) {

        BlockState block = tile.getWorld().getBlockState(pos);
        if (block.isAir(tile.getWorld(), pos) || block.getBlock() instanceof FlowingFluidBlock)
            return 0;
        return (int) ((block.getBlockHardness(tile.getWorld(), pos) + 1) * 5d / ModMachines.Stats.speed.calculateStat(tile.getMachine(), tile.getMainMachineVariant(), tile.getCasingMachineVariant()));

    }

    private CompoundNBT writeDropsNBT(NonNullList<ItemStack> list) {
        ListNBT ListNBT = new ListNBT();
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).isEmpty()) {
                CompoundNBT itemTag = new CompoundNBT();
                list.get(i).write(itemTag);
                ListNBT.add(itemTag);
            }
        }
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("Items", ListNBT);
        return nbt;
    }

    private NonNullList<ItemStack> readDropsNBT(CompoundNBT compound) {
        NonNullList<ItemStack> drops = NonNullList.create();
        ListNBT tagList = compound.getList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundNBT itemTags = tagList.getCompound(i);
            drops.add(ItemStack.read(itemTags));
        }
        return drops;
    }
}
