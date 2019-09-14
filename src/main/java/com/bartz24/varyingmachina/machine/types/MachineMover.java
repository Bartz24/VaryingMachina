package com.bartz24.varyingmachina.machine.types;

import com.bartz24.varyingmachina.Helpers;
import com.bartz24.varyingmachina.block.BlockMachine;
import com.bartz24.varyingmachina.entity.EntityMoverItem;
import com.bartz24.varyingmachina.inventory.OmniItemHandler;
import com.bartz24.varyingmachina.machine.MachineExtraComponents;
import com.bartz24.varyingmachina.machine.MachineType;
import com.bartz24.varyingmachina.machine.MachineVariant;
import com.bartz24.varyingmachina.machine.ModMachines;
import com.bartz24.varyingmachina.tile.TileEntityMachine;
import javafx.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public class MachineMover extends MachineType<TileEntityMachine> {
    public MachineMover() {
        super("mover", ModMachines.Stats.rating);
        this.addFilterItemHandler("filter", 0);
        this.setNoFuel();
        this.setNoRecipes();
        this.setAllDirectionFacing();
        this.setCraftingMachineMultiplier(4);
        this.setOverrideUpdateFunction(this::update);
        this.setCustomBoundingShape(this::getBoundingBoxes);
        this.setDisplayRange(this::getDisplayRange);
        this.setParentModel(new ResourceLocation("varyingmachina", "block/mover"));
        this.setExtraInput(MachineExtraComponents.mover);
        this.setNoMultiblock();
    }


    @Override
    public OmniItemHandler createItemHandler(TileEntityMachine tile) {
        OmniItemHandler handler = super.createItemHandler(tile);
        handler.getHandler("filter").setSize((int) calculateSpecialStat("filterSize", tile.getMainMachineVariant(), tile.getCasingMachineVariant()));
        return handler;
    }

    @Override
    public List<String> getSpecialTooltipTypes() {
        List<String> list = new ArrayList<>();
        list.add("filterSize");
        list.add("stackSize");
        list.add("maxRange");
        list.add("cooldown");
        list.add("shootSpeed");
        return list;
    }

    @Override
    public double calculateSpecialStat(String statName, MachineVariant mainVariant, MachineVariant casingVariant) {
        if (statName.equals("stackSize"))
            return (int) (ModMachines.Stats.size.calculateStat(this, mainVariant, casingVariant) * casingVariant.getStat(ModMachines.Stats.pressure) / 260d * Math.pow(ModMachines.Stats.production.calculateStat(this, mainVariant, casingVariant), 1.7)) + 1;
        else if (statName.equals("maxRange"))
            return (int) (Math.pow(ModMachines.Stats.speed.calculateStat(this, mainVariant, casingVariant), 1.68) * casingVariant.getStat(ModMachines.Stats.pressure) / 140d) + 1;
        else if (statName.equals("cooldown"))
            return (int) 40 / ((Math.pow(ModMachines.Stats.efficiency.calculateStat(this, mainVariant, casingVariant), .8)) + 0.6 * casingVariant.getStat(ModMachines.Stats.speed) / 100d) + 1;
        else if (statName.equals("shootSpeed"))
            return (mainVariant.getStat(ModMachines.Stats.speed) + casingVariant.getStat(ModMachines.Stats.efficiency)) / 200d / 2.5d;
        else if (statName.equals("filterSize"))
            return (int) ModMachines.Stats.size.calculateStat(this, mainVariant, casingVariant);

        return super.calculateSpecialStat(statName, mainVariant, casingVariant);
    }

    @Override
    public String getTextSpecialStat(String statName, MachineVariant mainVariant, MachineVariant casingVariant) {
        if (statName.equals("stackSize"))
            return (int) calculateSpecialStat("stackSize", mainVariant, casingVariant) + "";
        else if (statName.equals("maxRange"))
            return (int) calculateSpecialStat("maxRange", mainVariant, casingVariant) + "";
        else if (statName.equals("cooldown"))
            return (int) calculateSpecialStat("cooldown", mainVariant, casingVariant) + "";
        else if (statName.equals("shootSpeed"))
            return Helpers.round(calculateSpecialStat("shootSpeed", mainVariant, casingVariant), 2) + "";
        else if (statName.equals("filterSize"))
            return (int) calculateSpecialStat("filterSize", mainVariant, casingVariant) + "";
        return super.getTextSpecialStat(statName, mainVariant, casingVariant);
    }

    @Override
    public TextFormatting getColorSpecialStat(String statName) {
        if (statName.equals("filterSize"))
            return TextFormatting.BLUE;
        else if (statName.equals("stackSize"))
            return TextFormatting.AQUA;
        else if (statName.equals("maxRange"))
            return TextFormatting.LIGHT_PURPLE;
        else if (statName.equals("cooldown"))
            return TextFormatting.GREEN;
        else if (statName.equals("shootSpeed"))
            return TextFormatting.GOLD;
        return super.getColorSpecialStat(statName);
    }

    protected static final VoxelShape VERTICAL_AABB = Block.makeCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    protected static final VoxelShape NS_AABB = Block.makeCuboidShape(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 16.0D);
    protected static final VoxelShape EW_AABB = Block.makeCuboidShape(0.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D);

    private VoxelShape getBoundingBoxes(BlockState state) {
        switch (state.get(BlockMachine.FACING).getAxis()) {
            case X:
            default:
                return EW_AABB;
            case Z:
                return NS_AABB;
            case Y:
                return VERTICAL_AABB;
        }

    }

    ;

    private Pair<BlockPos[], Integer> getDisplayRange(TileEntityMachine tile) {
        List<BlockPos> list = new ArrayList<>();
        BlockPos pos = tile.getPos();
        for (int i = 1; i <= (int) ModMachines.mover.calculateSpecialStat("maxRange", tile.getMainMachineVariant(), tile.getCasingMachineVariant()); i++) {
            pos = pos.add(tile.getDirection().getDirectionVec());
            list.add(new BlockPos(pos.getX(), pos.getY(), pos.getZ()));
        }
        return new Pair<>(list.toArray(new BlockPos[list.size()]), 0x2eab0c);
    }

    ;

    private void update(TileEntityMachine tile) {
        mainUpdateMover(tile);

        if (!tile.getWorld().isRemote) {
            List<DestinationCooldown> destinationCooldowns = new ArrayList<>();

            ListNBT list = tile.getExtraData().getList("dests", Constants.NBT.TAG_COMPOUND);
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++)
                    destinationCooldowns.add(new DestinationCooldown(list.getCompound(i)));
                list = new ListNBT();
                for (int i = destinationCooldowns.size() - 1; i >= 0; i--) {
                    destinationCooldowns.get(i).ticksLeft--;
                    if (destinationCooldowns.get(i).ticksLeft <= 0)
                        destinationCooldowns.remove(i);
                    else
                        list.add(destinationCooldowns.get(i).writeToNBT());
                }
                tile.getExtraData().put("dests", list);
                tile.markDirty();
            }
        }
    }

    ;

    private void mainUpdateMover(TileEntityMachine tile) {

        if (!tile.getWorld().isRemote()) {
            if (tile.getProgress() >= getCooldown(tile)) {
                if (tile.getProgress() > getCooldown(tile)) {
                    tile.setProgress(getCooldown(tile));
                    tile.markDirty();
                }

                IItemHandler handler = getHandlerBehind(tile);
                if (handler != null) {
                    int maxExtract = getMaxExtract(tile);
                    for (int i = 0; i < handler.getSlots(); i++) {
                        Pair<Boolean, Integer> pair = sendItem(tile, handler.extractItem(i, maxExtract, true), handler);
                        if (pair.getKey()) {
                            handler.extractItem(i, pair.getValue(), false);
                            return;
                        }
                    }
                }

                List<ItemEntity> items = getItemsBehind(tile);
                if (items.size() > 0) {
                    //int maxExtract = getMaxExtract(tile);
                    for (int i = 0; i < items.size(); i++) {
                        Pair<Boolean, Integer> pair = sendItem(tile, items.get(i).getItem().copy(), null);
                        if (pair.getKey()) {
                            ItemStack stack = items.get(i).getItem();
                            stack.shrink(pair.getValue());
                            if (stack.isEmpty())
                                items.get(i).remove();
                            else
                                items.get(i).setItem(stack);
                            return;
                        }
                    }
                }

            } else {
                tile.incrementProgress(1);
                tile.markDirty();
            }
        }
    }

    private Pair<Boolean, Integer> sendItem(TileEntityMachine tile, ItemStack send, IItemHandler source) {
        if (!send.isEmpty() && matchesFilter(tile, send)) {

            IItemHandler handler = getHandlerAt(tile.getWorld(), tile.getPos().add(tile.getDirection().getDirectionVec()), tile.getDirection().getOpposite());
            if (handler != null && ItemHandlerHelper.insertItemStacked(handler, send, true).getCount() != send.getCount()) {
                int amount = send.getCount();
                send = ItemHandlerHelper.insertItemStacked(handler, send, false);
                amount -= send.getCount();
                tile.markDirty();
                tile.setProgress(0);
                tile.getWorld().playSound(null, tile.getPos(), SoundEvents.BLOCK_DISPENSER_LAUNCH, SoundCategory.BLOCKS, 0.4F, 1.0F + tile.getWorld().rand.nextFloat() * 0.4F);
                return new Pair<>(true, amount);
            }

            Pair<LogicData, List<BlockPos>> data = getDataToSend(tile, send, source);
            if (data.getKey().amount > 0) {
                EntityMoverItem moverItem = EntityMoverItem.createNewMoverItem(tile.getWorld(), tile.getPos().getX() + 0.5 + tile.getDirection().getDirectionVec().getX() * 0.83, tile.getPos().getY() + 0.5 + tile.getDirection().getDirectionVec().getY() * 0.83, tile.getPos().getZ() + 0.5 + tile.getDirection().getDirectionVec().getZ() * 0.83, tile.getDirection(), data.getValue(), ModMachines.mover.calculateSpecialStat("shootSpeed", tile.getMainMachineVariant(), tile.getCasingMachineVariant()), (int) (1.05d * getMaxRange(tile) / ModMachines.mover.calculateSpecialStat("shootSpeed", tile.getMainMachineVariant(), tile.getCasingMachineVariant())));
                ItemStack stack = send.copy();
                stack.setCount(data.getKey().amount);
                moverItem.setItem(stack);
                tile.getWorld().addEntity(moverItem);
                tile.markDirty();
                tile.setProgress(0);
                tile.getWorld().playSound(null, tile.getPos(), SoundEvents.BLOCK_DISPENSER_LAUNCH, SoundCategory.BLOCKS, 0.4F, 1.0F + tile.getWorld().rand.nextFloat() * 0.4F);
                return new Pair<>(true, data.getKey().amount);
            }
        }
        return new Pair<>(false, 0);
    }

    private Pair<LogicData, List<BlockPos>> getDataToSend(TileEntityMachine tile, ItemStack send, IItemHandler source) {
        List<TileEntityMachine> moverStack = new ArrayList<>();
        List<TileEntityMachine> moversChecked = new ArrayList<>();
        List<DestinationCooldown> destinationCooldowns = new ArrayList<>();

        ListNBT list = tile.getExtraData().getList("dests", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++)
            destinationCooldowns.add(new DestinationCooldown(list.getCompound(i)));

        moverStack.add(tile);

        LogicData data = getDataToSend(tile, send, source, moverStack, moversChecked, destinationCooldowns);
        List<BlockPos> posList = new ArrayList<>();
        moverStack.forEach(t -> {
            if (t != tile)
                posList.add(t.getPos());
        });

        if (data.destination != null) {
            list.add(new DestinationCooldown(data.destination.getKey(), data.destination.getValue(), (int) (data.timeToDestination * 1.05f)).writeToNBT());
            tile.getExtraData().put("dests", list);
            tile.markDirty();
        }

        return new Pair<>(data, posList);
    }

    /**
     * @param tile
     * @param moverStack
     * @param moversChecked
     * @return pair. first one is the amount of items to send. second is the distance
     */
    private LogicData getDataToSend(TileEntityMachine tile, ItemStack send, IItemHandler source, List<TileEntityMachine> moverStack, List<TileEntityMachine> moversChecked, List<DestinationCooldown> destinationCooldowns) {
        Direction direction = tile.getDirection();
        BlockPos nextPos = tile.getPos();
        for (int i = 1; i <= getMaxRange(tile); i++) {
            nextPos = nextPos.add(direction.getDirectionVec());

            IItemHandler handler = getHandlerAt(tile.getWorld(), nextPos, direction.getOpposite());
            if (handler != null) {
                ItemStack remaining = ItemHandlerHelper.insertItemStacked(handler, send, true);
                if (remaining.getCount() == send.getCount() || handler == source || hasDestination(destinationCooldowns, nextPos, direction.getOpposite()))
                    return new LogicData(0, 0, null);
                else
                    return new LogicData(send.getCount() - remaining.getCount(), (int) Math.ceil(i / ModMachines.mover.calculateSpecialStat("shootSpeed", tile.getMainMachineVariant(), tile.getCasingMachineVariant())), nextPos, direction.getOpposite());
            }

            if (!tile.getWorld().getBlockState(nextPos).isAir(tile.getWorld(), nextPos))
                return new LogicData(0, 0, null);

            for (Direction facing : Helpers.getOrthogonalWithForwardVectors(direction)) {
                TileEntityMachine mover = getMover(tile.getWorld(), nextPos.add(facing.getDirectionVec()));
                if (mover != null && mover.getDirection().equals(facing) && !moverStack.contains(mover) && !moversChecked.contains(mover) && matchesFilter(mover, send)) {
                    moverStack.add(mover);
                    LogicData data = getDataToSend(mover, send, source, moverStack, moversChecked, destinationCooldowns);
                    if (data.amount == 0) {
                        moversChecked.add(mover);
                        moverStack.remove(mover);
                    } else
                        return new LogicData(Math.min(getMaxExtract(mover), data.amount), data.timeToDestination + (int) Math.ceil(i / ModMachines.mover.calculateSpecialStat("shootSpeed", tile.getMainMachineVariant(), tile.getCasingMachineVariant())), data.destination);
                }
            }
        }
        return new LogicData(0, 0, null);
    }

    private boolean hasDestination(List<DestinationCooldown> destinationCooldowns, BlockPos pos, Direction side) {
        for (DestinationCooldown dest : destinationCooldowns) {
            if (dest.pos.equals(pos) && dest.side.equals(side))
                return true;
        }
        return false;
    }

    private static class LogicData {
        public int amount;
        public int timeToDestination;

        public Pair<BlockPos, Direction> destination;

        public LogicData(int amount, int timeToDestination, Pair<BlockPos, Direction> destination) {
            this.amount = amount;
            this.timeToDestination = timeToDestination;
            this.destination = destination;
        }

        public LogicData(int amount, int timeToDestination, BlockPos destinationPos, Direction destinationInputSide) {
            this.amount = amount;
            this.timeToDestination = timeToDestination;
            this.destination = new Pair<>(destinationPos, destinationInputSide);
        }
    }

    private static class DestinationCooldown {
        public BlockPos pos;
        public Direction side;
        public int ticksLeft;

        public DestinationCooldown(BlockPos pos, Direction side, int ticksLeft) {
            this.pos = pos;
            this.side = side;
            this.ticksLeft = ticksLeft;
        }

        public DestinationCooldown(CompoundNBT compound) {

            this.pos = new BlockPos(compound.getInt("x"), compound.getInt("y"), compound.getInt("z"));
            this.side = Direction.byName(compound.getString("side"));
            this.ticksLeft = compound.getInt("ticks");
        }

        public CompoundNBT writeToNBT() {
            CompoundNBT compound = new CompoundNBT();
            compound.putInt("x", pos.getX());
            compound.putInt("y", pos.getY());
            compound.putInt("z", pos.getZ());
            compound.putString("side", side.getName());
            compound.putInt("ticks", ticksLeft);
            return compound;
        }
    }


    private boolean matchesFilter(TileEntityMachine mover, ItemStack stack) {
        ItemStackHandler handler = mover.getItemHandler().getHandler("filter");
        if (handler != null) {
            boolean isEmpty = true;
            for (int i = 0; i < handler.getSlots(); i++) {
                if (!handler.getStackInSlot(i).isEmpty()) {
                    isEmpty = false;
                    break;
                }
            }
            if (isEmpty)
                return true;
            for (int i = 0; i < handler.getSlots(); i++) {
                if (handler.getStackInSlot(i).isItemEqual(stack)) {
                    return true;
                }
            }
        }
        return false;
    }

    private IItemHandler getHandlerAt(World world, BlockPos pos, Direction from) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null)
            return tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, from).orElse(null);
        return null;
    }

    private IItemHandler getHandlerBehind(TileEntityMachine tile) {
        return getHandlerAt(tile.getWorld(), tile.getPos().subtract(tile.getDirection().getDirectionVec()), tile.getDirection());
    }

    private List<ItemEntity> getItemsBehind(TileEntityMachine tile) {
        AxisAlignedBB bb = new AxisAlignedBB(tile.getPos().subtract(tile.getDirection().getDirectionVec()));
        return tile.getWorld().getEntitiesWithinAABB(ItemEntity.class, bb);
    }

    private TileEntityMachine getMover(World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null && tileEntity instanceof TileEntityMachine && ((TileEntityMachine) tileEntity).getMachine().equals(ModMachines.mover))
            return (TileEntityMachine) tileEntity;
        return null;
    }

    private int getMaxExtract(TileEntityMachine tile) {
        return (int) ModMachines.mover.calculateSpecialStat("stackSize", tile.getMainMachineVariant(), tile.getCasingMachineVariant());
    }

    private int getMaxRange(TileEntityMachine tile) {
        return (int) ModMachines.mover.calculateSpecialStat("maxRange", tile.getMainMachineVariant(), tile.getCasingMachineVariant());
    }

    private double getCooldown(TileEntityMachine tile) {
        return (int) ModMachines.mover.calculateSpecialStat("cooldown", tile.getMainMachineVariant(), tile.getCasingMachineVariant());
    }
}
