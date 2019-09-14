package com.bartz24.varyingmachina.world;

import com.EmosewaPixel.pixellib.materialsystem.MaterialRegistry;
import com.EmosewaPixel.pixellib.materialsystem.lists.MaterialBlocks;
import com.bartz24.varyingmachina.pixellib.PixelPlugin;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

public class OreGeneration {
    public static void setup() {
        for (Biome biome : ForgeRegistries.BIOMES) {
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                    Feature.ORE,
                    new OreFeatureConfig(
                            OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                            MaterialBlocks.getBlock(PixelPlugin.copper, MaterialRegistry.ORE).getDefaultState(),
                            12),
                    Placement.COUNT_RANGE,
                    new CountRangeConfig(
                            12,
                            40,
                            40,
                            80)));
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                    Feature.ORE,
                    new OreFeatureConfig(
                            OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                            MaterialBlocks.getBlock(PixelPlugin.tin, MaterialRegistry.ORE).getDefaultState(),
                            9),
                    Placement.COUNT_RANGE,
                    new CountRangeConfig(
                            8,
                            0,
                            0,
                            64)));
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                    Feature.ORE,
                    new OreFeatureConfig(
                            OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                            MaterialBlocks.getBlock(PixelPlugin.silver, MaterialRegistry.ORE).getDefaultState(),
                            10),
                    Placement.COUNT_RANGE,
                    new CountRangeConfig(
                            6,
                            0,
                            0,
                            26)));
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                    Feature.ORE,
                    new OreFeatureConfig(
                            OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                            MaterialBlocks.getBlock(PixelPlugin.mythril, MaterialRegistry.ORE).getDefaultState(),
                            8),
                    Placement.COUNT_RANGE,
                    new CountRangeConfig(
                            5,
                            0,
                            0,
                            18)));

            if (biome.getCategory() == Biome.Category.PLAINS || biome.getCategory() == Biome.Category.SWAMP) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                        Feature.ORE,
                        new OreFeatureConfig(
                                OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                                MaterialBlocks.getBlock(MaterialRegistry.IRON, MaterialRegistry.ORE).getDefaultState(),
                                240),
                        LargeCountRange.LARGE_COUNT_RANGE,
                        new CountRangeConfig(
                                1,
                                0,
                                0,
                                64)));
            }

            if (biome.getCategory() == Biome.Category.MESA || biome.getCategory() == Biome.Category.OCEAN) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                        Feature.ORE,
                        new OreFeatureConfig(
                                OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                                MaterialBlocks.getBlock(MaterialRegistry.GOLD, MaterialRegistry.ORE).getDefaultState(),
                                180),
                        LargeCountRange.LARGE_COUNT_RANGE,
                        new CountRangeConfig(
                                1,
                                0,
                                0,
                                40)));
            }

            if (biome.getCategory() == Biome.Category.TAIGA || biome.getCategory() == Biome.Category.JUNGLE) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                        Feature.ORE,
                        new OreFeatureConfig(
                                OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                                MaterialBlocks.getBlock(PixelPlugin.tin, MaterialRegistry.ORE).getDefaultState(),
                                220),
                        LargeCountRange.LARGE_COUNT_RANGE,
                        new CountRangeConfig(
                                1,
                                0,
                                0,
                                64)));
            }

            if (biome.getCategory() == Biome.Category.DESERT || biome.getCategory() == Biome.Category.RIVER) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                        Feature.ORE,
                        new OreFeatureConfig(
                                OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                                MaterialBlocks.getBlock(PixelPlugin.copper, MaterialRegistry.ORE).getDefaultState(),
                                260),
                        LargeCountRange.LARGE_COUNT_RANGE,
                        new CountRangeConfig(
                                1,
                                0,
                                0,
                                64)));
            }

            if (biome.getCategory() == Biome.Category.EXTREME_HILLS || biome.getCategory() == Biome.Category.SAVANNA) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                        Feature.ORE,
                        new OreFeatureConfig(
                                OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                                MaterialBlocks.getBlock(MaterialRegistry.COAL, MaterialRegistry.ORE).getDefaultState(),
                                280),
                        LargeCountRange.LARGE_COUNT_RANGE,
                        new CountRangeConfig(
                                1,
                                0,
                                0,
                                90)));
            }

            if (biome.getCategory() == Biome.Category.BEACH || biome.getCategory() == Biome.Category.FOREST) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                        Feature.ORE,
                        new OreFeatureConfig(
                                OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                                MaterialBlocks.getBlock(MaterialRegistry.DIAMOND, MaterialRegistry.ORE).getDefaultState(),
                                90),
                        LargeCountRange.LARGE_COUNT_RANGE,
                        new CountRangeConfig(
                                1,
                                0,
                                0,
                                16)));
            }

            if (biome.getCategory() == Biome.Category.EXTREME_HILLS || biome.getCategory() == Biome.Category.ICY) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                        Feature.ORE,
                        new OreFeatureConfig(
                                OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                                MaterialBlocks.getBlock(MaterialRegistry.EMERALD, MaterialRegistry.ORE).getDefaultState(),
                                75),
                        LargeCountRange.LARGE_COUNT_RANGE,
                        new CountRangeConfig(
                                1,
                                0,
                                0,
                                20)));
            }

            if (biome.getCategory() == Biome.Category.TAIGA || biome.getCategory() == Biome.Category.DESERT) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                        Feature.ORE,
                        new OreFeatureConfig(
                                OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                                MaterialBlocks.getBlock(PixelPlugin.mythril, MaterialRegistry.ORE).getDefaultState(),
                                80),
                        LargeCountRange.LARGE_COUNT_RANGE,
                        new CountRangeConfig(
                                1,
                                0,
                                0,
                                36)));
            }

            if (biome.getCategory() == Biome.Category.BEACH || biome.getCategory() == Biome.Category.RIVER) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                        Feature.ORE,
                        new OreFeatureConfig(
                                OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                                MaterialBlocks.getBlock(MaterialRegistry.LAPIS, MaterialRegistry.ORE).getDefaultState(),
                                110),
                        LargeCountRange.LARGE_COUNT_RANGE,
                        new CountRangeConfig(
                                1,
                                0,
                                0,
                                36)));
            }

            if (biome.getCategory() == Biome.Category.PLAINS || biome.getCategory() == Biome.Category.FOREST) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                        Feature.ORE,
                        new OreFeatureConfig(
                                OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                                MaterialBlocks.getBlock(MaterialRegistry.REDSTONE, MaterialRegistry.ORE).getDefaultState(),
                                160),
                        LargeCountRange.LARGE_COUNT_RANGE,
                        new CountRangeConfig(
                                1,
                                0,
                                0,
                                16)));
            }
        }
    }
}
