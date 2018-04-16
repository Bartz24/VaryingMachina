package com.bartz24.varyingmachina.jaopca;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import thelm.jaopca.api.*;
import thelm.jaopca.api.utils.Utils;

import java.util.List;

public class ModuleWire extends ModuleBase {

    public static final ItemEntry WIRE_ENTRY = new ItemEntry(EnumEntryType.ITEM, "wire", new ModelResourceLocation("jaopca:wire#inventory")).
            setOreTypes(EnumOreType.DUSTLESS);

    @Override
    public String getName() {
        return "wire";
    }

    @Override
    public List<ItemEntry> getItemRequests() {
        return Lists.<ItemEntry>newArrayList(WIRE_ENTRY);
    }

    @Override
    public void preInit() {
        for (IOreEntry entry : JAOPCAApi.ENTRY_NAME_TO_ORES_MAP.get("wire")) {
            OreDictionary.registerOre("wire" + entry.getOreName(), Utils.getOreStack("wire", entry, 1));
        }
    }

    @Override
    public void init() {
        for (IOreEntry entry : JAOPCAApi.ENTRY_NAME_TO_ORES_MAP.get("wire")) {
            String s = "ingot";
            switch (entry.getOreType()) {
                case GEM:
                case GEM_ORELESS:
                    s = "gem";
                    break;
                default:
                    break;
            }
            Utils.addShapedOreRecipe(Utils.getOreStack("wire", entry, 6), new Object[]{
                    "ooo",
                    'o', s + entry.getOreName(),
            });
        }
    }
}