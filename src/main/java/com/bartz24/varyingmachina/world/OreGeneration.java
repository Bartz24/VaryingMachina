package com.bartz24.varyingmachina.world;

import com.bartz24.varyingmachina.pixellib.PixelPlugin;
import com.emosewapixel.pixellib.materialsystem.addition.BaseMaterials;
import com.emosewapixel.pixellib.materialsystem.addition.BaseObjTypes;
import com.emosewapixel.pixellib.materialsystem.lists.MaterialBlocks;
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
                            MaterialBlocks.get(PixelPlugin.copper, BaseObjTypes.ORE).getDefaultState(),
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
                            MaterialBlocks.get(PixelPlugin.tin, BaseObjTypes.ORE).getDefaultState(),
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
                            MaterialBlocks.get(PixelPlugin.silver, BaseObjTypes.ORE).getDefaultState(),
                            10),
                    Placement.COUNT_RANGE,
                    new CountRangeConfig(
                            6,
                            0,
                            0,
                            26)));
            /*biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                    Feature.ORE,
                    new OreFeatureConfig(
                            OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                            MaterialBlocks.get(BaseMaterials.mythril, BaseObjTypes.ORE).getDefaultState(),
                            8),
                    Placement.COUNT_RANGE,
                    new CountRangeConfig(
                            5,
                            0,
                            0,
                            18)));*/

            if (biome.getCategory() == Biome.Category.PLAINS || biome.getCategory() == Biome.Category.SWAMP) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                        Feature.ORE,
                        new OreFeatureConfig(
                                OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                                MaterialBlocks.get(BaseMaterials.IRON, BaseObjTypes.ORE).getDefaultState(),
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
                                MaterialBlocks.get(BaseMaterials.GOLD, BaseObjTypes.ORE).getDefaultState(),
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
                                MaterialBlocks.get(PixelPlugin.tin, BaseObjTypes.ORE).getDefaultState(),
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
                                MaterialBlocks.get(PixelPlugin.copper, BaseObjTypes.ORE).getDefaultState(),
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
                                MaterialBlocks.get(BaseMaterials.COAL, BaseObjTypes.ORE).getDefaultState(),
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
                                MaterialBlocks.get(BaseMaterials.DIAMOND, BaseObjTypes.ORE).getDefaultState(),
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
                                MaterialBlocks.get(BaseMaterials.EMERALD, BaseObjTypes.ORE).getDefaultState(),
                                75),
                        LargeCountRange.LARGE_COUNT_RANGE,
                        new CountRangeConfig(
                                1,
                                0,
                                0,
                                20)));
            }

            /*if (biome.getCategory() == Biome.Category.TAIGA || biome.getCategory() == Biome.Category.DESERT) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                        Feature.ORE,
                        new OreFeatureConfig(
                                OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                                MaterialBlocks.get(PixelPlugin.mythril, BaseObjTypes.ORE).getDefaultState(),
                                80),
                        LargeCountRange.LARGE_COUNT_RANGE,
                        new CountRangeConfig(
                                1,
                                0,
                                0,
                                36)));
            }*/

            if (biome.getCategory() == Biome.Category.BEACH || biome.getCategory() == Biome.Category.RIVER) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                        Feature.ORE,
                        new OreFeatureConfig(
                                OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                                MaterialBlocks.get(BaseMaterials.LAPIS, BaseObjTypes.ORE).getDefaultState(),
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
                                MaterialBlocks.get(BaseMaterials.REDSTONE, BaseObjTypes.ORE).getDefaultState(),
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
