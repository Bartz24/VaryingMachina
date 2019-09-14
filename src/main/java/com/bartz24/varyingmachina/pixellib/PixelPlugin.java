package com.bartz24.varyingmachina.pixellib;

import com.EmosewaPixel.pixellib.materialsystem.MaterialRegistry;
import com.EmosewaPixel.pixellib.materialsystem.lists.Materials;
import com.EmosewaPixel.pixellib.materialsystem.materials.*;
import com.EmosewaPixel.pixellib.materialsystem.types.ItemType;
import com.EmosewaPixel.pixellib.materialsystem.types.ObjectType;
import com.EmosewaPixel.pixellib.materialsystem.types.TextureType;

import java.util.ArrayList;
import java.util.List;

public class PixelPlugin {

    public static TextureType matter = new TextureType("matter");

    public static List<Material> newMaterials = new ArrayList<>();

    public static Material copper = getOrCreateMaterial(new IngotMaterial("copper", MaterialRegistry.REGULAR, 0xffc14d, 0).build(), MaterialRegistry.HAS_ORE);
    public static Material tin = getOrCreateMaterial(new IngotMaterial("tin", MaterialRegistry.REGULAR, 0xb3ffff, 0).build(), MaterialRegistry.HAS_ORE);
    public static Material silver = getOrCreateMaterial(new IngotMaterial("silver", MaterialRegistry.SHINY, 0xffffff, 2).build(), MaterialRegistry.HAS_ORE);
    public static Material bronze = getOrCreateMaterial(new IngotMaterial("bronze", MaterialRegistry.REGULAR, 0xb77816, 1).build());
    public static Material ferrotin = getOrCreateMaterial(new IngotMaterial("ferrotin", MaterialRegistry.REGULAR, 0xc7ffff, 1).build());

    public static Material electrum = getOrCreateMaterial(new IngotMaterial("electrum", MaterialRegistry.SHINY, 0xecf073, 2).build());
    public static Material mythril = getOrCreateMaterial(new IngotMaterial("mythril", MaterialRegistry.SHINY, 0x71eafb, 2).build(), MaterialRegistry.HAS_ORE);
    public static Material voidcrystal = getOrCreateMaterial(new GemMaterial("voidcrystal", MaterialRegistry.OCTAGONAL, 0x001b4d, 3).build());
    public static Material clay = getOrCreateMaterial(new DustMaterial("clay", MaterialRegistry.REGULAR, 0xb3c1d3, 0).blacklistTypes(MaterialRegistry.BLOCK).build());
    public static Material steel = getOrCreateMaterial(new IngotMaterial("steel", MaterialRegistry.REGULAR, 0x9d9d9d, 2).build());
    public static Material redcopper = getOrCreateMaterial(new IngotMaterial("redcopper", MaterialRegistry.SHINY, 0xb42508, 2).build());
    public static Material mythrilsteel = getOrCreateMaterial(new IngotMaterial("mythrilsteel", MaterialRegistry.SHINY, 0x586481, 3).build());
    public static Material varium = getOrCreateMaterial(new IngotMaterial("varium", MaterialRegistry.SHINY, 0x73fffd, 5).setUnrefinedColor(0x1c5c5b).build(), MaterialRegistry.HAS_ORE);

    public static ObjectType frozeningot = new ItemType("frozeningot", mat -> mat instanceof IngotMaterial).build();
    public static ObjectType frozengem = new ItemType("frozengem", mat -> mat instanceof GemMaterial).build();
    public static ObjectType plate = new ItemType("plate", mat -> mat instanceof IngotMaterial).build();
    public static ObjectType cable = new ItemType("cable", mat -> mat instanceof IngotMaterial).build();
    public static ObjectType gear = new ItemType("gear", mat -> mat instanceof IngotMaterial || mat instanceof GemMaterial).build();
    public static ObjectType crystalshard = new ItemType("crystalshard", mat -> mat.hasTag(MaterialRegistry.HAS_ORE)).build();
    public static ObjectType extract = new ItemType("extract", mat -> mat.hasTag(MaterialRegistry.HAS_ORE)).build();

    public static ObjectType axe = new ItemType("axe", mat -> false).build();
    public static ObjectType pickaxe = new ItemType("pickaxe", mat -> false).build();
    public static ObjectType sword = new ItemType("sword", mat -> false).build();
    public static ObjectType shovel = new ItemType("shovel", mat -> false).build();
    public static ObjectType hoe = new ItemType("hoe", mat -> false).build();

    public static void setup() {
        MaterialRegistry.BRICK.blacklistTypes(gear);
        MaterialRegistry.BRICK.blacklistTypes(plate);
        MaterialRegistry.BRICK.blacklistTypes(cable);
        MaterialRegistry.LAPIS.blacklistTypes(gear);
        MaterialRegistry.FLINT.blacklistTypes(gear);

        MaterialRegistry.DIAMOND.addTags(MaterialRegistry.HAS_ORE);
    }

    private static Material getOrCreateMaterial(Material material, String... tags) {
        Material actualMaterial = Materials.get(material.getName()) == null ? material : Materials.get(material.getName());
        if (Materials.get(material.getName()) == null)
            newMaterials.add(actualMaterial);
        for (String tag : tags) {
            if (!actualMaterial.hasTag(tag))
                actualMaterial.addTags(tags);
        }

        return actualMaterial;
    }
}
