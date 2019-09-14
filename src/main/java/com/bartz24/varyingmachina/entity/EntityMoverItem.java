package com.bartz24.varyingmachina.entity;

import com.bartz24.varyingmachina.Helpers;
import com.bartz24.varyingmachina.VaryingMachina;
import com.bartz24.varyingmachina.machine.ModMachines;
import com.bartz24.varyingmachina.render.RenderMoverItem;
import com.bartz24.varyingmachina.tile.TileEntityMachine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntityMoverItem extends Entity {
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(EntityMoverItem.class, DataSerializers.ITEMSTACK);

    private Direction direction = Direction.NORTH;
    private List<BlockPos> targets = new ArrayList<>();
    private int age, ageRange;

    private double speedMultiplier = 0.25f;

    public float hoverStart = (float) (Math.random() * Math.PI * 2.0D);

    public EntityMoverItem(EntityType<EntityMoverItem> type, World worldIn) {
        super(type, worldIn);
        this.noClip = false;
    }

    public static EntityMoverItem createNewMoverItem(World worldIn, double x, double y, double z, Direction direction, List<BlockPos> targets, double speedMultiplier, int ageRange) {
        EntityMoverItem entity = new EntityMoverItem(VaryingMachina.moverItemEntityType, worldIn);
        entity.setPosition(x, y, z);
        entity.speedMultiplier = speedMultiplier;
        entity.setDirection(direction);
        entity.rotationYaw = (float) (Math.random() * 360.0D);
        entity.targets = targets;
        entity.ageRange = ageRange;
        entity.age = 0;
        return entity;
    }

    protected boolean canTriggerWalking() {
        return false;
    }


    @OnlyIn(Dist.CLIENT)
    public int getAge() {
        return this.age;
    }

    @Override
    protected void registerData() {
        this.getDataManager().register(ITEM, ItemStack.EMPTY);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        this.age = compound.getShort("Age");
        targets.clear();
        ListNBT tagList = compound.getList("targets", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            targets.add(new BlockPos(tagList.getCompound(i).getInt("x"), tagList.getCompound(i).getInt("y"), tagList.getCompound(i).getInt("z")));
        }
        direction = Direction.byName(compound.getString("direction"));
        speedMultiplier = compound.getDouble("speedMult");
        ageRange = compound.getInt("ageRange");
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putShort("Age", (short) this.age);
        ListNBT tagList = new ListNBT();
        for (BlockPos target : targets) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("x", target.getX());
            nbt.putInt("y", target.getY());
            nbt.putInt("z", target.getZ());
            tagList.add(nbt);
        }
        compound.put("targets", tagList);
        compound.putString("direction", direction.getName());
        compound.putDouble("speedMult", speedMultiplier);
        compound.putInt("ageRange", ageRange);

    }

    @Override
    public void tick() {
        if (this.getItem().isEmpty())
            this.remove();
        else {
            super.tick();

            age++;


            if (age > ageRange && !world.isRemote) {
                world.addEntity(new ItemEntity(world, Math.floor(posX) + 0.5, Math.floor(posY) + 0.5, Math.floor(posZ) + 0.5, getItem()));
                this.remove();
                return;
            }

            if (this.getMotion().x == 0 && getMotion().y == 0 && getMotion().z == 0)
                setDirection(direction);

            Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d vec3d1 = new Vec3d(this.posX + this.getMotion().x, this.posY + this.getMotion().y, this.posZ + this.getMotion().z);
            RayTraceResult raytraceresult = this.world.rayTraceBlocks(new RayTraceContext(vec3d, vec3d1, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
            if (raytraceresult != null && raytraceresult.getType() != RayTraceResult.Type.MISS) {
                this.onImpact(raytraceresult);
                return;
            }


            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            double d0 = this.getMotion().x;
            double d1 = this.getMotion().y;
            double d2 = this.getMotion().z;

            if (targets.size() > 0) {
                for (BlockPos pos : getPosesComing()) {
                    for (Direction facing : Helpers.getOrthogonalWithForwardVectors(direction)) {
                        TileEntityMachine mover = getMover(world, pos.add(facing.getDirectionVec()));
                        if (mover != null && mover.getDirection().equals(facing) && pos.add(facing.getDirectionVec()).equals(targets.get(0)) && mover.getProgress() >= (int) ModMachines.mover.calculateSpecialStat("cooldown", mover.getMainMachineVariant(), mover.getCasingMachineVariant())) {


                            IItemHandler handler = getHandlerAt(world, mover.getPos().add(mover.getDirection().getDirectionVec()), mover.getDirection().getOpposite());
                            int amountInserted = 0;
                            if (handler != null && ItemHandlerHelper.insertItemStacked(handler, getItem(), true).getCount() != getItem().getCount()) {
                                amountInserted = getItem().getCount();
                                setItem(ItemHandlerHelper.insertItemStacked(handler, getItem(), false));
                                amountInserted -= getItem().getCount();
                                if (getItem().isEmpty()) {
                                    world.playSound(null, mover.getPos(), SoundEvents.BLOCK_DISPENSER_LAUNCH, SoundCategory.BLOCKS, 0.4F, 1.0F + world.rand.nextFloat() * 0.4F);
                                    this.remove();
                                    return;
                                }
                            }
                            int amount = Math.max(0, Math.min((int) ModMachines.mover.calculateSpecialStat("stackSize", mover.getMainMachineVariant(), mover.getCasingMachineVariant()), getItem().getCount()) - amountInserted);
                            if (amount > 0) {

                                List<BlockPos> newTargets = new ArrayList<>();
                                for (int i = 1; i < targets.size(); i++)
                                    newTargets.add(targets.get(i));
                                EntityMoverItem entityMoverItem = EntityMoverItem.createNewMoverItem(world,
                                        mover.getPos().getX() + 0.5 + mover.getDirection().getDirectionVec().getX() * 0.83,
                                        mover.getPos().getY() + 0.5 + mover.getDirection().getDirectionVec().getY() * 0.83,
                                        mover.getPos().getZ() + 0.5 + mover.getDirection().getDirectionVec().getZ() * 0.83,
                                        mover.getDirection(),
                                        newTargets,
                                        ModMachines.mover.calculateSpecialStat("shootSpeed", mover.getMainMachineVariant(), mover.getCasingMachineVariant()),
                                        (int) Math.ceil(1.05d * (int) ModMachines.mover.calculateSpecialStat("maxRange", mover.getMainMachineVariant(), mover.getCasingMachineVariant()) / speedMultiplier));


                                ItemStack stack = getItem().copy();
                                stack.setCount(amount);
                                entityMoverItem.setItem(stack);
                                world.addEntity(entityMoverItem);

                                stack = getItem();
                                stack.setCount(stack.getCount() - amount);
                                setItem(stack);

                                if (stack.isEmpty())
                                    this.remove();

                                world.playSound(null, mover.getPos(), SoundEvents.BLOCK_DISPENSER_LAUNCH, SoundCategory.BLOCKS, 0.4F, 0.8F + rand.nextFloat() * 0.8F);
                            }
                            return;
                        }
                    }
                }
            }

            this.move(MoverType.SELF, this.getMotion());
            boolean flag = (int) this.prevPosX != (int) this.posX || (int) this.prevPosY != (int) this.posY || (int) this.prevPosZ != (int) this.posZ;
            if (flag || this.ticksExisted % 25 == 0) {
                if (this.world.getFluidState(new BlockPos(this)).isTagged(FluidTags.LAVA)) {
                    this.setMotion(
                            (this.rand.nextDouble() - this.rand.nextDouble()) * 0.2,
                            0.2,
                            ((this.rand.nextDouble() - this.rand.nextDouble()) * 0.2));
                    this.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
                }
            }

        }
    }

    private List<BlockPos> getPosesComing() {
        BlockPos pos = new BlockPos(posX, posY, posZ);
        BlockPos end = new BlockPos(posX + getMotion().x, posY + getMotion().y, posZ + getMotion().z);
        List<BlockPos> list = new ArrayList<>();
        list.add(new BlockPos(pos.getX(), pos.getY(), pos.getZ()));
        while (!pos.equals(end) && (list.size() - 3) / speedMultiplier <= ageRange) {
            if (!list.contains(new BlockPos(pos.getX(), pos.getY(), pos.getZ())))
                list.add(new BlockPos(pos.getX(), pos.getY(), pos.getZ()));
            pos = pos.add(direction.getDirectionVec());
        }
        if ((list.size() - 3) / speedMultiplier > ageRange)
            return Collections.singletonList(list.get(0));
        return list;
    }

    private int getTicksFromDistance(int distance) {
        return (int) Math.ceil(1.05d * distance / speedMultiplier);
    }

    private void setDirection(Direction direction) {
        this.setMotion(
                direction.getDirectionVec().getX() * speedMultiplier,
                direction.getDirectionVec().getY() * speedMultiplier,
                direction.getDirectionVec().getZ() * speedMultiplier
        );
        this.direction = direction;
    }

    private TileEntityMachine getMover(World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null && tileEntity instanceof TileEntityMachine && ((TileEntityMachine) tileEntity).getMachine().equals(ModMachines.mover))
            return (TileEntityMachine) tileEntity;
        return null;
    }

    private static IItemHandler getHandlerAt(World world, BlockPos pos, Direction from) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null)
            return tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, from).orElse(null);
        return null;
    }


    public void setItem(ItemStack stack) {
        this.getDataManager().set(ITEM, stack);
    }

    public ItemStack getItem() {
        return this.getDataManager().get(ITEM);
    }

    public ITextComponent getName() {
        ITextComponent itextcomponent = this.getCustomName();
        return (itextcomponent != null ? itextcomponent : new TranslationTextComponent(this.getItem().getTranslationKey()));
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    protected void onImpact(RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.BLOCK && !world.isRemote) {
            BlockRayTraceResult rayTraceResult = (BlockRayTraceResult) result;
            ItemStack stack = getItem();
            IItemHandler handler = getHandlerAt(world, rayTraceResult.getPos(), direction.getOpposite());
            if (handler != null)
                stack = ItemHandlerHelper.insertItemStacked(handler, stack, false);
            if (!stack.isEmpty())
                world.addEntity(new ItemEntity(world, Math.floor(posX) + 0.5, Math.floor(posY) + 0.5, Math.floor(posZ) + 0.5, stack));

            this.world.setEntityState(this, (byte) 3);
            this.remove();
        }
    }
}
