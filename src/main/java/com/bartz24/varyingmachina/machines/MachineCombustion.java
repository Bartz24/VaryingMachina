package com.bartz24.varyingmachina.machines;

import com.bartz24.varyingmachina.ItemHelper;
import com.bartz24.varyingmachina.base.block.BlockCasing;
import com.bartz24.varyingmachina.base.inventory.GuiArrowProgress;
import com.bartz24.varyingmachina.base.inventory.GuiCasing;
import com.bartz24.varyingmachina.base.inventory.GuiHeatBar;
import com.bartz24.varyingmachina.base.item.ItemMachine;
import com.bartz24.varyingmachina.base.item.ItemModule;
import com.bartz24.varyingmachina.base.machine.MachineStat;
import com.bartz24.varyingmachina.base.recipe.ProcessRecipe;
import com.bartz24.varyingmachina.base.recipe.RecipeItem;
import com.bartz24.varyingmachina.base.recipe.RecipeObject;
import com.bartz24.varyingmachina.base.tile.TileCasing;
import com.bartz24.varyingmachina.machines.recipes.CombustionRecipes;
import com.bartz24.varyingmachina.machines.recipes.SmelterRecipes;
import com.bartz24.varyingmachina.modules.ModuleWorldInserter;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MachineCombustion extends ItemMachine {

    public MachineCombustion() {
        super("combustion", MachineStat.SPEED, MachineStat.EFFICIENCY, MachineStat.PRESSURE);
    }

    public ProcessRecipe getRecipe(World world, BlockPos pos, ItemStack machineStack) {
        float curHU = getCasingTile(world, pos).machineData.getFloat("curHU");
        List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX(),
                pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1));
        List<ItemStack> items = new ArrayList<>();
        List<RecipeObject> itemObjs = new ArrayList<>();
        for (EntityItem o : list) {
            ItemStack i = o.getItem().copy();
            boolean added = false;
            for (ItemStack i2 : items) {
                if (i2.isItemEqual(i)) {
                    i2.setCount(i2.getCount() + i.getCount());
                    added = true;
                }
            }
            if (!added)
                items.add(i);
        }
        for (ItemStack i : items) {
            itemObjs.add(new RecipeItem(i));
        }
        return CombustionRecipes.combustionRecipes.getMultiRecipe(itemObjs, curHU, getCombinedStat(MachineStat.PRESSURE, machineStack, world, pos));
    }

    public void processFinish(World world, BlockPos pos, ItemStack machineStack, ProcessRecipe recipe) {
        world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, pos.getX(), pos.getY() + 1.5D, pos.getZ(), 0.0D,
                0.0D, 0.0D, new int[0]);

        world.playSound((EntityPlayer) null, pos.getX() + 0.5, pos.getY() + 1.5D, pos.getZ() + 0.5,
                SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F,
                (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);

        List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX(),
                pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2F, pos.getZ() + 1));

        HashMap<ItemStack, Integer> items = new HashMap();
        for (EntityItem o : list) {
            ItemStack i = o.getItem().copy();
            int count = i.getCount();
            i.setCount(1);
            boolean added = false;
            for (ItemStack i2 : items.keySet()) {
                if (i2.isItemEqual(i)) {
                    items.put(i2, items.get(i2) + count);
                    added = true;
                }
            }
            if (!added)
                items.put(i, count);
            o.setDead();
        }

        float curHU = getCasingTile(world, pos).machineData.getFloat("curHU");
        for (int times = 0; times < 100; times++) {
            boolean done = false;
            if (curHU < recipe.getNumParameters()[0])
                break;
            for (ItemStack item2 : items.keySet().toArray(new ItemStack[items.size()])) {
                if (items.get(item2) == 0) {
                    done = true;
                    break;
                }
                for (List<ItemStack> input : recipe.getItemInputs()) {
                    boolean success = false;
                    for (ItemStack s : input) {
                        if (!s.isEmpty() && ItemHelper.itemStacksEqualOD(s, item2)) {
                            items.put(item2, items.get(item2) - s.getCount());
                            success = true;
                            break;
                        }
                    }
                    if (success)
                        break;
                }
            }
            if (done)
                break;

            float mult = 1f - (1f / (1.5f + 0.8f * getCombinedStat(MachineStat.EFFICIENCY, machineStack, world, pos)));
            curHU *= mult;

            ItemStack stack = recipe.getItemOutputs().get(0).copy();

            getInserter(world, pos);

            TileCasing buffer = getInserter(world, pos);
            if (buffer != null) {
                for (int i = 0; i < buffer.getInputInventory().getSlots(); i++) {
                    if (!stack.isEmpty())
                        stack = buffer.getInputInventory().insertItem(i, stack, false);
                    else
                        break;
                }
            }
            if (!stack.isEmpty()) {
                Entity entity = new EntityItem(world, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F,
                        stack);
                world.spawnEntity(entity);
            }
        }
        for (ItemStack item2 : items.keySet().toArray(new ItemStack[items.size()])) {
            while (items.get(item2) > 0) {
                ItemStack spawn = item2.copy();
                int count = Math.min(items.get(item2), item2.getMaxStackSize());
                spawn.setCount(count);
                items.put(item2, items.get(item2) - count);
                Entity entity = new EntityItem(world, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F,
                        spawn);
                world.spawnEntity(entity);
            }
        }
        getCasingTile(world, pos).machineData.setFloat("curHU", curHU);
    }

    private TileCasing getInserter(World world, BlockPos pos) {
        BlockPos[] poses = new BlockPos[]{pos.add(-1, 1, 0), pos.add(1, 1, 0), pos.add(0, 1, -1), pos.add(0, 1, 1),
                pos.add(0, 2, 0)};
        for (BlockPos p : poses) {
            TileEntity t = world.getTileEntity(p);
            if (t != null && t instanceof TileCasing) {
                TileCasing c = (TileCasing) t;
                BlockPos dir = p.add(-pos.up().getX(), -pos.up().getY(), -pos.up().getZ());
                ItemModule m = c.getModule(EnumFacing.getFacingFromVector(dir.getX(), dir.getY(), dir.getZ()).getOpposite());
                if (m instanceof ModuleWorldInserter)
                    return c;
            }
        }
        return null;
    }

    public float getHUDrain(World world, BlockPos pos, ItemStack machineStack) {
        return 0;
    }

    public int getTimeToProcess(World world, BlockPos pos, ItemStack machineStack, ProcessRecipe recipe) {
        return 0;
    }

    public boolean canRun(World world, BlockPos pos, ItemStack machineStack, ProcessRecipe recipe) {
        float curHU = getCasingTile(world, pos).machineData.getFloat("curHU");
        return getCasingTile(world, pos).receivedPulse() && recipe != null && curHU > 0 && hasValidMultiblock(world, pos, machineStack);
    }

    public boolean hasMultiblock() {
        return true;
    }

    public boolean hasValidMultiblock(World world, BlockPos pos, ItemStack machineStack) {
        if (!isBlockValid(world, machineStack, pos.up(), pos.add(-1, 1, 0))
                || !isBlockValid(world, machineStack, pos.up(), pos.add(1, 1, 0))
                || !isBlockValid(world, machineStack, pos.up(), pos.add(0, 2, 0))
                || !isBlockValid(world, machineStack, pos.up(), pos.add(0, 1, -1))
                || !isBlockValid(world, machineStack, pos.up(), pos.add(0, 1, 1)) || !world.isAirBlock(pos.up()))
            return false;
        return true;
    }

    boolean isBlockValid(World world, ItemStack machineStack, BlockPos center, BlockPos pos) {
        BlockPos dir = center.subtract(pos);
        return world.getBlockState(pos).getBlockFaceShape(world, pos,
                EnumFacing.getFacingFromVector(dir.getX(), dir.getY(), dir.getZ())) == BlockFaceShape.SOLID;
    }

    public boolean requiresFuel(ItemStack stack) {
        return true;
    }

    public MachineStat[] getCombinedStats() {
        List<MachineStat> combinedStats = new ArrayList(Arrays.asList(stats));
        combinedStats.add(MachineStat.MAXHU);
        return combinedStats.toArray(new MachineStat[combinedStats.size()]);
    }

    @SideOnly(Side.CLIENT)
    public void initGui(GuiCasing gui, List buttonList, TileCasing casing) {
        super.initGui(gui, buttonList, casing);
        float curHU = casing.machineData.getFloat("curHU");
        boolean running = casing.machineData.getBoolean("running");
        int huTick = (int) (casing.machineData.getFloat("huTick")
                - (running ? getHUDrain(casing.getWorld(), casing.getPos(), casing.machineStored) : 0));
        gui.addComponent("heat", new GuiHeatBar(136, 25,
                (int) getCombinedStat(MachineStat.MAXHU, casing.machineStored, casing.getWorld(), casing.getPos()),
                curHU, huTick));
    }

    @SideOnly(Side.CLIENT)
    public void updateGuiComps(GuiCasing gui, List buttonList, TileCasing casing) {
        super.updateGuiComps(gui, buttonList, casing);
        float curHU = casing.machineData.getFloat("curHU");
        boolean running = casing.machineData.getBoolean("running");
        int huTick = (int) (casing.machineData.getFloat("huTick")
                - (running ? getHUDrain(casing.getWorld(), casing.getPos(), casing.machineStored) : 0));
        gui.updateComponent("heat",
                (int) getCombinedStat(MachineStat.MAXHU, casing.machineStored, casing.getWorld(), casing.getPos()),
                curHU, huTick);
    }
}
