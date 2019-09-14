package com.bartz24.varyingmachina.world;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.placement.CountRange;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LargeCountRange extends CountRange {
    public static final Placement<CountRangeConfig> LARGE_COUNT_RANGE = register("large_count_range", new LargeCountRange(CountRangeConfig::deserialize));

    public LargeCountRange(Function<Dynamic<?>, ? extends CountRangeConfig> p_i51357_1_) {
        super(p_i51357_1_);
    }

    private final int rarity = 70;


    @Override
    public Stream<BlockPos> getPositions(Random p_212852_1_, CountRangeConfig p_212852_2_, BlockPos p_212852_3_) {
        return IntStream.range(0, p_212852_2_.count / rarity + (p_212852_1_.nextInt((int) Math.ceil(p_212852_2_.count / (double) rarity) * rarity) <= p_212852_2_.count ? 1 : 0)).mapToObj((p_215061_3_) -> {
            int lvt_4_1_ = p_212852_1_.nextInt(16);
            int lvt_5_1_ = p_212852_1_.nextInt(p_212852_2_.maximum - p_212852_2_.topOffset) + p_212852_2_.bottomOffset;
            int lvt_6_1_ = p_212852_1_.nextInt(16);
            return p_212852_3_.add(lvt_4_1_, lvt_5_1_, lvt_6_1_);
        });
    }

    public static void register() {

    }

    private static <T extends IPlacementConfig, G extends Placement<T>> G register(String key, G p_214999_1_) {
        return (G) (Registry.<Placement<?>>register(Registry.DECORATOR, key, p_214999_1_));
    }
}
