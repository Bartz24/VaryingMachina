package com.bartz24.varyingmachina.guide;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class GuideRegistry {
    private static LinkedHashMap<ResourceLocation, IGuidePage> pages = new LinkedHashMap<>();
    private static HashMap<String, ItemStack> categoryStackIcons = new HashMap<>();

    public static void addPage(ResourceLocation id, IGuidePage page) {
        if (pages.containsKey(id))
            throw new IllegalStateException("Guide already has " + id.toString() + " loaded!");
        pages.put(id, page);
    }

    public static void addPage(String id, IGuidePage page) {
        addPage(new ResourceLocation(id), page);
    }

    public static void addCategoryIcon(String category, ItemStack stack) {
        categoryStackIcons.put(category, stack);
    }

    public static ItemStack getCategoryIcon(String category) {
        if (!categoryStackIcons.containsKey(category))
            return ItemStack.EMPTY;
        return categoryStackIcons.get(category);
    }

    public static IGuidePage getPage(ResourceLocation id) {
        return pages.get(id);
    }

    public static IGuidePage getPage(String id) {
        return getPage(new ResourceLocation(id));
    }

    public static List<ResourceLocation> getPageIds(String category) {
        List<ResourceLocation> list = new ArrayList<>();
        pages.keySet().forEach(location -> {
            if (category == null || location.getNamespace().equals(category))
                list.add(location);
        });
        return list;
    }

    public static List<ResourceLocation> getPageIds() {
        return getPageIds(null);
    }

    public static List<String> getCategoryIds() {
        List<String> list = new ArrayList<>();
        pages.keySet().forEach(location -> {
            String category = location.getNamespace();
            if (!list.contains(category))
                list.add(category);
        });
        return list;
    }
}
