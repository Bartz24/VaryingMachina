package com.bartz24.varyingmachina.tile;

import com.bartz24.varyingmachina.VaryingMachina;
import com.bartz24.varyingmachina.block.BlockCasing;
import net.minecraft.block.SixWayBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class TileEntityCasing extends TileEntity {

    private TileEntityMachine machine = null;

    public TileEntityCasing() {
        super(VaryingMachina.extenderTE);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        TileEntityMachine target = getMachine();
        return target == null ? super.getCapability(cap, side) : target.getCapability(cap, side);
    }

    public TileEntityMachine getMachine() {
        if (machine == null) {
            List<BlockPos> searchStack = new ArrayList<>();
            List<BlockPos> marked = new ArrayList<>();

            searchStack.add(pos);

            while (searchStack.size() > 0) {
                BlockPos search = searchStack.remove(0);
                marked.add(search);
                for (Direction dir : Direction.values()) {
                    if (isConnected(search, dir)) {
                        if (world.getTileEntity(search.offset(dir)) instanceof TileEntityMachine) {
                            machine = (TileEntityMachine) world.getTileEntity(search.offset(dir));
                            return machine;
                        } else {
                            if (!searchStack.contains(search.offset(dir)) && !marked.contains(search.offset(dir)))
                                searchStack.add(search.offset(dir));
                        }
                    }
                }
            }
        }
        return machine;
    }

    private boolean isConnected(BlockPos pos, Direction dir) {
        return world.getBlockState(pos).getBlock() instanceof BlockCasing && world.getBlockState(pos).get(SixWayBlock.FACING_TO_PROPERTY_MAP.get(dir));
    }
}
