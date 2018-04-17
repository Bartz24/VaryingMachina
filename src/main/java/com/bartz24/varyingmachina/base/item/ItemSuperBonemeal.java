package com.bartz24.varyingmachina.base.item;

import com.bartz24.varyingmachina.registry.ModRenderers;
import net.minecraft.block.IGrowable;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSuperBonemeal extends ItemBase {

    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand,
                                      EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!playerIn.canPlayerEdit(pos.offset(facing), facing, playerIn.getHeldItem(hand))) {
            return EnumActionResult.FAIL;
        } else {
            if (applyBonemeal(playerIn.getHeldItem(hand), worldIn, pos, playerIn)) {
                if (!worldIn.isRemote) {
                    worldIn.playEvent(2005, pos, 0);
                }

                return EnumActionResult.SUCCESS;
            }

            return EnumActionResult.PASS;
        }
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + "Acts as bone meal");
        tooltip.add(TextFormatting.GRAY + "Grows instantly");
    }

    public static boolean applyBonemeal(ItemStack stack, World worldIn, BlockPos target, EntityPlayer player) {

        if (worldIn.getBlockState(target).getBlock() instanceof IGrowable && !worldIn.isRemote) {
            int tries = 100;
            while (worldIn.getBlockState(target).getBlock() instanceof IGrowable && tries > 0) {
                tries--;
                IGrowable igrowable = (IGrowable) worldIn.getBlockState(target).getBlock();
                if (igrowable.canGrow(worldIn, target, worldIn.getBlockState(target), false)) {
                    igrowable.grow(worldIn, worldIn.rand, target, worldIn.getBlockState(target));
                }
            }

            stack.shrink(1);

            return true;
        }

        return false;
    }
}
