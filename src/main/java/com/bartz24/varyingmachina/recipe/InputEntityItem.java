package com.bartz24.varyingmachina.recipe;

import com.bartz24.varyingmachina.tile.TileEntityMachine;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InputEntityItem extends InputBase<TileEntityMachine, ItemStack> {
    private ItemStack stack;
    protected BlockPos offset;

    public InputEntityItem(ItemStack stack, BlockPos offset) {
        this.stack = stack.copy();
        this.offset = offset;
    }

    public InputEntityItem(ItemStack stack) {
        this(stack, new BlockPos(0, 1, 0));
    }


    @Override
    public String getId() {
        return "entity";
    }

    @Override
    public boolean hasInput(TileEntityMachine inventory) {
        List<ItemStack> stacks = getStacksOffset(inventory);
        ItemStack remove = getStack().copy();
        for (int s = stacks.size() - 1; s >= 0; s--) {
            if (stacks.get(s).isItemEqual(remove)) {
                int count = Math.min(stacks.get(s).getCount(), remove.getCount());
                remove.shrink(count);
                stacks.get(s).shrink(count);
                if (remove.isEmpty())
                    return true;
            }
        }
        return false;
    }

    @Override
    public void drawItemsFromInventory(TileEntityMachine inventory) {
        List<ItemStack> stacks = getStacksOffset(inventory);
        ItemStack remove = getStack().copy();
        for (int s = stacks.size() - 1; s >= 0; s--) {
            if (stacks.get(s).isItemEqual(remove)) {
                int count = Math.min(stacks.get(s).getCount(), remove.getCount());
                remove.shrink(count);
                stacks.get(s).shrink(count);
                if (remove.isEmpty())
                    break;
            }
        }
        BlockPos pos = inventory.getPos().add(offset);
        for (ItemStack stack : stacks) {
            while (!stack.isEmpty() && !inventory.getWorld().isRemote) {
                ItemStack spawn = stack.copy();
                spawn.setCount(Math.min(spawn.getMaxStackSize(), spawn.getCount()));
                stack.shrink(spawn.getCount());
                InventoryHelper.spawnItemStack(inventory.getWorld(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, spawn);
            }
        }
    }

    protected List<ItemStack> getStacksOffset(TileEntityMachine tile) {
        List<ItemStack> stacks = new ArrayList<>();
        BlockPos pos = tile.getPos().add(offset);
        List<ItemEntity> list = tile.getWorld().getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1));
        for (ItemEntity entityItem : list) {
            boolean found = false;
            for (ItemStack stack : stacks) {
                if (stack.isItemEqual(entityItem.getItem())) {
                    stack.grow(entityItem.getItem().getCount());
                    found = true;
                    break;
                }
            }
            if (!found)
                stacks.add(entityItem.getItem().copy());
        }
        return stacks;
    }

    @Override
    public boolean hasEnough(InputBase in2) {
        if (!(in2 instanceof InputItem))
            return false;
        InputItem inItem2 = (InputItem) in2;
        return getStack().isItemEqual(inItem2.getStack()) && inItem2.getStack().getCount() >= getStack().getCount();
    }

    @Override
    public List<ItemStack> getInputs() {
        return Collections.singletonList(getStack().copy());
    }

    @Override
    public boolean isValid() {
        return !getStack().isEmpty();
    }

    public ItemStack getStack() {
        return stack;
    }

    @Override
    public InputEntityItem scale(int factor) {
        stack.setCount(stack.getCount() * factor);
        return this;
    }

    @Override
    public boolean combineWith(InputBase input2) {
        if (input2 instanceof InputEntityItem && getStack().isItemEqual(((InputEntityItem) input2).getStack()) && offset.equals(((InputEntityItem) input2).offset)) {
            stack.grow(((InputEntityItem) input2).getStack().getCount());
            return true;
        }
        return false;
    }

    @Override
    public InputEntityItem copy() {
        return new InputEntityItem(stack, offset);
    }

    @Override
    public Class<TileEntityMachine> getInvType() {
        return TileEntityMachine.class;
    }
}
