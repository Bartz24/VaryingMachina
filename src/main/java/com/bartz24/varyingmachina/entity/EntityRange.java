package com.bartz24.varyingmachina.entity;

import com.bartz24.varyingmachina.VaryingMachina;
import com.bartz24.varyingmachina.render.RenderMoverItem;
import com.bartz24.varyingmachina.render.RenderRange;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityRange extends Entity {

    private int age;
    private int color;

    public EntityRange(EntityType<EntityRange> type, World worldIn) {
        super(type, worldIn);
        this.noClip = false;
    }

    public static EntityRange createRangeEntity(World worldIn, BlockPos pos, int color) {
        EntityRange entityRange = new EntityRange(VaryingMachina.rangeEntityType, worldIn);
        entityRange.setPosition(pos.getX(), pos.getY(), pos.getZ());
        entityRange.color = color;
        entityRange.age = 200;

        return entityRange;
    }

    @Override
    protected void registerData() {

    }

    @Override
    public void tick() {
        super.tick();

        age--;

        if (age <= 0)
            this.remove();

    }

    @OnlyIn(Dist.CLIENT)
    public int getAge() {
        return this.age;
    }

    public int getColor() {
        return color;
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        this.age = compound.getShort("Age");
        this.color = compound.getInt("color");

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putShort("Age", (short) this.age);
        compound.putInt("color", color);

    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
