package com.bartz24.varyingmachina.item;

import com.bartz24.varyingmachina.Helpers;
import com.bartz24.varyingmachina.VaryingMachina;
import com.bartz24.varyingmachina.block.BlockCasing;
import com.bartz24.varyingmachina.block.BlockMachine;
import com.bartz24.varyingmachina.machine.ModMachines;
import com.bartz24.varyingmachina.machine.ModVariants;
import com.bartz24.varyingmachina.model.MachineModelLoader;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoaderRegistry;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class ItemBlockMachine extends BlockItem {
    BlockMachine blockMachine;

    public ItemBlockMachine(BlockMachine blockIn, Item.Properties builder) {
        super(blockIn, builder);
        this.blockMachine = blockIn;
        this.setRegistryName(blockIn.getRegistryName());
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        String text = new TranslationTextComponent("block.varyingmachina.machine").getFormattedText();
        text = text.replace("VVV", new TranslationTextComponent("material." + blockMachine.getMachineVariant()).getFormattedText());
        text = text.replace("CCC", new TranslationTextComponent("material." + blockMachine.getCasingVariant()).getFormattedText());
        text = text.replace("MMM", new TranslationTextComponent("varyingmachina.machine." + blockMachine.getMachineType()).getFormattedText());
        return new StringTextComponent(text);
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.addAll(Helpers.getMachineInfo(ModMachines.types.get(blockMachine.getMachineType()), ModVariants.types.get(blockMachine.getMachineVariant()), ModVariants.types.get(blockMachine.getCasingVariant())));
    }

    @OnlyIn(Dist.CLIENT)
    public static void initModel() {
        ResourceLocation baseLocation = new ResourceLocation("varyingmachina", "item/machine");
        MachineModelLoader loader = new MachineModelLoader(baseLocation);
        for (String type : ModMachines.types.keySet()) {
            for (String variant : ModVariants.types.keySet()) {
                for (String casingVariant : ModVariants.types.keySet()) {

                    MachineModelLoader.ModelInfo modelInfo = new MachineModelLoader.ModelInfo(ModVariants.types.get(variant).getTexture().toString(), ModVariants.types.get(casingVariant).getTexture().toString());
                    if (ModMachines.types.get(type).getTextureFront() != null)
                        modelInfo.setOverlay(ModMachines.types.get(type).getTextureFront().toString());
                    if (ModMachines.types.get(type).getTextureTop() != null)
                        modelInfo.setOverlay(ModMachines.types.get(type).getTextureTop().toString());

                    ModelResourceLocation modelLocation = new ModelResourceLocation("varyingmachina:item/" + type + "." + variant + "." + casingVariant, "inventory");

                    loader.addVariant(modelLocation, modelInfo);
                }
            }
        }

        ModelLoaderRegistry.registerLoader(loader);
    }


    protected boolean canPlace(BlockItemUseContext p_195944_1_, BlockState p_195944_2_) {
        if (ModMachines.types.get(getBlockMachine().getMachineType()).isMultiblock()) {

            int size = getMultiblockSize();

            BlockPos pos = getCenterBottomPos(p_195944_1_);

            for (int x = pos.getX() - size; x <= pos.getX() + size; x++) {
                for (int y = pos.getY(); y <= pos.getY() + size * 2; y++) {
                    for (int z = pos.getZ() - size; z <= pos.getZ() + size; z++) {
                        if (!p_195944_1_.getPlayer().world.getBlockState(new BlockPos(x, y, z)).isReplaceable(p_195944_1_)) {
                            return false;
                        }
                    }
                }
            }
            return super.canPlace(p_195944_1_, p_195944_2_);
        } else
            return super.canPlace(p_195944_1_, p_195944_2_);

    }

    protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
        if (ModMachines.types.get(getBlockMachine().getMachineType()).isMultiblock()) {
            int size = getMultiblockSize();

            BlockPos pos = getCenterBottomPos(context);

            BlockPos masterPos = pos.offset(getPlayerFacingDir(context.getPlayer()).getOpposite(), size);

            BlockState casingState = VaryingMachina.blockCasings.get(getBlockMachine().getCasingVariant()).getDefaultState();

            for (int x = pos.getX() - size; x <= pos.getX() + size; x++) {
                for (int y = pos.getY(); y <= pos.getY() + size * 2; y++) {
                    for (int z = pos.getZ() - size; z <= pos.getZ() + size; z++) {
                        BlockPos newPos = new BlockPos(x, y, z);
                        if (newPos.equals(masterPos))
                            context.getWorld().setBlockState(context.getPos(), state, 11);
                        else {
                            context.getWorld().setBlockState(newPos,
                                    casingState
                                            .with(BlockCasing.NORTH, z > pos.getZ() - size)
                                            .with(BlockCasing.SOUTH, z < pos.getZ() + size)
                                            .with(BlockCasing.EAST, x < pos.getX() + size)
                                            .with(BlockCasing.WEST, x > pos.getX() - size)
                                            .with(BlockCasing.DOWN, y > pos.getY())
                                            .with(BlockCasing.UP, y < pos.getY() + size * 2), 11);
                        }
                    }
                }
            }
            return true;
        } else
            return context.getWorld().setBlockState(context.getPos(), state, 11);
    }

    private Direction getPlayerFacingDir(PlayerEntity player) {
        return Arrays.stream(Direction.getFacingDirections(player)).filter(dir -> dir.getHorizontalIndex() != -1).findFirst().get();
    }

    private BlockPos getCenterBottomPos(BlockItemUseContext context) {

        int size = getMultiblockSize();

        BlockPos pos = context.getPos();
        if (!context.getPlayer().world.getBlockState(context.getPos()).isReplaceable(context))
            pos = pos.offset(context.getFace());

        return pos.offset(getPlayerFacingDir(context.getPlayer()), size);
    }

    public int getMultiblockSize() {
        if (!ModMachines.types.get(getBlockMachine().getMachineType()).isMultiblock())
            return 0;
        return (int) ModMachines.Stats.rating.calculateStat(ModMachines.types.get(getBlockMachine().getMachineType()), ModVariants.types.get(getBlockMachine().getMachineVariant()), ModVariants.types.get(getBlockMachine().getCasingVariant())) - 1;
    }

    public BlockMachine getBlockMachine() {
        return blockMachine;
    }
}
