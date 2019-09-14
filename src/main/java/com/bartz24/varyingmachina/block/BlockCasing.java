package com.bartz24.varyingmachina.block;

import com.bartz24.varyingmachina.machine.MachineVariant;
import com.bartz24.varyingmachina.tile.TileEntityCasing;
import com.bartz24.varyingmachina.tile.TileEntityMachine;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SixWayBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class BlockCasing extends Block {
    public static final BooleanProperty NORTH = SixWayBlock.NORTH;
    public static final BooleanProperty EAST = SixWayBlock.EAST;
    public static final BooleanProperty SOUTH = SixWayBlock.SOUTH;
    public static final BooleanProperty WEST = SixWayBlock.WEST;
    public static final BooleanProperty UP = SixWayBlock.UP;
    public static final BooleanProperty DOWN = SixWayBlock.DOWN;

    private MachineVariant variant;

    public BlockCasing(Properties properties, MachineVariant variant) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(NORTH, Boolean.valueOf(false)).with(EAST, Boolean.valueOf(false)).with(SOUTH, Boolean.valueOf(false)).with(WEST, Boolean.valueOf(false)).with(UP, Boolean.valueOf(false)).with(DOWN, Boolean.valueOf(false)));
        this.setRegistryName(new ResourceLocation("varyingmachina", "casing." + variant.getName()));
        this.variant = variant;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        int count = 0;
        for (Direction dir : Direction.values()) {
            if (state.get(SixWayBlock.FACING_TO_PROPERTY_MAP.get(dir)))
                count++;
        }
        return count < 6 && count > 0;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (hasTileEntity(state))
            return new TileEntityCasing();
        return null;
    }

    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        for (Direction dir : Direction.values()) {
            if (state.get(SixWayBlock.FACING_TO_PROPERTY_MAP.get(dir)))
                return Collections.EMPTY_LIST;
        }

        return Collections.singletonList(new ItemStack(this));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, WEST, SOUTH, UP, DOWN);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        for (Direction dir : Direction.values()) {
            if (state.get(SixWayBlock.FACING_TO_PROPERTY_MAP.get(dir)) && !attachesTo(worldIn.getBlockState(pos.offset(dir)))) {
                for (Direction dir2 : Direction.values()) {
                    if (state.get(SixWayBlock.FACING_TO_PROPERTY_MAP.get(dir2)) && worldIn.getBlockState(pos.offset(dir2)).getBlock() instanceof BlockMachine)
                        worldIn.destroyBlock(pos.offset(dir2), true);
                }
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                break;
            }
        }
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
    }

    @Override
    public boolean canBeConnectedTo(BlockState state, IBlockReader world, BlockPos pos, Direction facing) {
        BlockState other = world.getBlockState(pos.offset(facing));
        return attachesTo(other);
    }

    public boolean attachesTo(BlockState state) {
        return state.getBlock() instanceof BlockMachine || state.getBlock() instanceof BlockCasing;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (handIn == Hand.MAIN_HAND && worldIn.getTileEntity(pos) instanceof TileEntityCasing) {
            TileEntityCasing casing = (TileEntityCasing) worldIn.getTileEntity(pos);
            if(casing.getMachine() != null) {
                if (!worldIn.isRemote()) {
                    NetworkHooks.openGui((ServerPlayerEntity) player, casing.getMachine(), buf -> buf.writeBlockPos(casing.getMachine().getPos()));
                }
            }
            return true;

        }

        return false;
    }
}
