package com.bartz24.varyingmachina.block;

import com.bartz24.varyingmachina.machine.ModMachines;
import com.bartz24.varyingmachina.machine.ModVariants;
import com.bartz24.varyingmachina.model.MachineModelLoader;
import com.bartz24.varyingmachina.tile.TileEntityMachine;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class BlockMachine extends Block {
    public static final DirectionProperty FACING = DirectionalBlock.FACING;

    private String machineType, machineVariant, casingVariant;

    public BlockMachine(String machineType, String machineVariant, String casingVariant, Properties properties) {
        super(properties);
        this.machineType = machineType;
        this.machineVariant = machineVariant;
        this.casingVariant = casingVariant;
        this.setRegistryName("varyingmachina", machineType + "." + machineVariant + "." + casingVariant);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityMachine();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        if (ModMachines.types.get(machineType).isAllDirectionFacing()) {
            {
                Direction direction;
                direction = context.getNearestLookingDirection().getOpposite();
                return this.getDefaultState().with(FACING, (context.getPlayer() != null && context.getPlayer().isSneaking()) ? direction.getOpposite() : direction);
            }
        } else
            return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Function<BlockState, VoxelShape> func = ModMachines.types.get(machineType).getCustomBoundingShape();
        if (func != null)
            return func.apply(state);
        else
            return super.getShape(state, worldIn, pos, context);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }


    @Override
    public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);

        if (te instanceof TileEntityMachine)
            ((TileEntityMachine) te).onBreak(this);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {

        if (!worldIn.isRemote() && handIn == Hand.MAIN_HAND && worldIn.getTileEntity(pos) instanceof TileEntityMachine) {
            NetworkHooks.openGui((ServerPlayerEntity) player, (TileEntityMachine) worldIn.getTileEntity(pos), buf -> buf.writeBlockPos(pos));
        }

        return true;
    }

    public String getMachineType() {
        return machineType;
    }

    public String getMachineVariant() {
        return machineVariant;
    }

    public String getCasingVariant() {
        return casingVariant;
    }

    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return Collections.singletonList(new ItemStack(this));
    }

    @Override
    public boolean isSolid(BlockState state) {
        return true;
    }
}
