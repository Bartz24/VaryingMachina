package com.bartz24.varyingmachina.pixellib;

import com.emosewapixel.pixellib.materialsystem.addition.BaseMaterials;
import com.emosewapixel.pixellib.materialsystem.addition.BaseObjTypes;
import com.emosewapixel.pixellib.materialsystem.addition.BaseTextureTypes;
import com.emosewapixel.pixellib.materialsystem.builders.DustMaterialBuilder;
import com.emosewapixel.pixellib.materialsystem.builders.GemMaterialBuilder;
import com.emosewapixel.pixellib.materialsystem.builders.IngotMaterialBuilder;
import com.emosewapixel.pixellib.materialsystem.builders.ItemTypeBuilder;
import com.emosewapixel.pixellib.materialsystem.main.Material;
import com.emosewapixel.pixellib.materialsystem.main.ObjectType;

import java.util.ArrayList;
import java.util.List;

public class PixelPlugin {

    //public static TextureType matter = new TextureType("matter");

    public static List<Material> newMaterials = new ArrayList<>();

    public static Material copper = new IngotMaterialBuilder("copper")
            .tier(0).color(0xffc14d).textureType(BaseTextureTypes.REGULAR).buildAndRegister();
    public static Material tin = new IngotMaterialBuilder("tin")
            .tier(0).color(0xb3ffff).textureType(BaseTextureTypes.REGULAR).buildAndRegister();
    public static Material silver = new IngotMaterialBuilder("silver")
            .tier(2).color(0xffffff).textureType(BaseTextureTypes.SHINY).buildAndRegister();
    public static Material bronze = new IngotMaterialBuilder("bronze")
            .tier(1).color(0xb77816).textureType(BaseTextureTypes.REGULAR).hasOre(false).buildAndRegister();
    public static Material ferrotin = new IngotMaterialBuilder("ferrotin")
            .tier(1).color(0xc7ffff).textureType(BaseTextureTypes.REGULAR).hasOre(false).buildAndRegister();

    public static Material electrum = new IngotMaterialBuilder("electrum")
            .tier(2).color(0xecf073).textureType(BaseTextureTypes.SHINY).hasOre(false).buildAndRegister();
    public static Material mythril = new IngotMaterialBuilder("mythril")
            .tier(2).color(0x71eafb).textureType(BaseTextureTypes.SHINY).buildAndRegister();
    public static Material voidcrystal = new GemMaterialBuilder("voidcrystal")
            .tier(2).color(0x001b4d).textureType(BaseTextureTypes.OCTAGONAL).hasOre(false).buildAndRegister();
    public static Material clay = new DustMaterialBuilder("clay")
            .tier(0).color(0xb3c1d3).textureType(BaseTextureTypes.REGULAR).hasOre(false).blacklistTypes(BaseObjTypes.BLOCK).buildAndRegister();
    public static Material steel = new IngotMaterialBuilder("steel")
            .tier(1).color(0x9d9d9d).textureType(BaseTextureTypes.REGULAR).hasOre(false).buildAndRegister();
    public static Material redcopper = new IngotMaterialBuilder("redcopper")
            .tier(2).color(0xb42508).textureType(BaseTextureTypes.SHINY).hasOre(false).buildAndRegister();
    public static Material varium = new IngotMaterialBuilder("varium")
            .tier(2).color(0x73fffd).textureType(BaseTextureTypes.SHINY).buildAndRegister();

    public static ObjectType frozeningot = new ItemTypeBuilder("frozeningot", mat -> mat.isIngotMaterial()).buildAndRegister();
    public static ObjectType frozengem = new ItemTypeBuilder("frozengem", mat -> mat.isGemMaterial()).buildAndRegister();
    public static ObjectType plate = new ItemTypeBuilder("plate", mat -> mat.isIngotMaterial()).buildAndRegister();
    public static ObjectType cable = new ItemTypeBuilder("cable", mat -> mat.isIngotMaterial()).buildAndRegister();
    public static ObjectType gear = new ItemTypeBuilder("gear", mat -> mat.isIngotMaterial() || mat.isGemMaterial()).buildAndRegister();
    public static ObjectType crystalshard = new ItemTypeBuilder("crystalshard", mat -> mat.getHasOre()).buildAndRegister();
    public static ObjectType extract = new ItemTypeBuilder("extract", mat -> mat.getHasOre()).buildAndRegister();

    public static ObjectType axe = new ItemTypeBuilder("axe", mat -> false).buildAndRegister();
    public static ObjectType pickaxe = new ItemTypeBuilder("pickaxe", mat -> false).buildAndRegister();
    public static ObjectType sword = new ItemTypeBuilder("sword", mat -> false).buildAndRegister();
    public static ObjectType shovel = new ItemTypeBuilder("shovel", mat -> false).buildAndRegister();
    public static ObjectType hoe = new ItemTypeBuilder("hoe", mat -> false).buildAndRegister();

    public static void setup() {
        BaseMaterials.BRICK.getTypeBlacklist().add(gear);
        BaseMaterials.BRICK.getTypeBlacklist().add(plate);
        BaseMaterials.BRICK.getTypeBlacklist().add(cable);
        BaseMaterials.LAPIS.getTypeBlacklist().add(gear);
        BaseMaterials.FLINT.getTypeBlacklist().add(gear);
    }
}
