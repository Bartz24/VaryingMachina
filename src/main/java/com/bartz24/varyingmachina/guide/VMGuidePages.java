package com.bartz24.varyingmachina.guide;

import com.bartz24.varyingmachina.VaryingMachina;
import com.bartz24.varyingmachina.machine.ModMachines;
import com.bartz24.varyingmachina.machine.ModVariants;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class VMGuidePages {
    public static void setup() {
        new TextGuidePage(new ResourceLocation("varyingmachina", "gettingstarted"), new ItemStack(Blocks.COARSE_DIRT));
        for (String type : ModMachines.types.keySet()) {
            new MachineGuidePage(new ResourceLocation("varyingmachina", type), VaryingMachina.createMachineStack(ModMachines.types.get(type), ModVariants.iron, ModVariants.gold, 1), ModMachines.types.get(type));
        }

        GuideRegistry.addCategoryIcon("varyingmachina", VaryingMachina.machines.getIcon());
    }
}
