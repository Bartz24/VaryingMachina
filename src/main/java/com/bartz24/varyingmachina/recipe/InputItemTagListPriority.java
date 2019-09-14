package com.bartz24.varyingmachina.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputItemTagListPriority extends InputItemTagList {
    public InputItemTagListPriority(List<ItemTags.Wrapper> tags, int count, String invId) {
        super(tags, count, invId);
    }

    public InputItemTagListPriority(int count, List<ResourceLocation> tags, String invId) {
        super(count, tags, invId);
    }

    public InputItemTagListPriority(int count, ResourceLocation... tags) {
        super(count, Arrays.asList(tags), "input");
    }


    @Override
    public List<ItemStack> getInputs() {
        List<ItemStack> inputs = new ArrayList<>();
        for (Tag<Item> tag : tags) {
            if (tag.getAllElements().size() > 0) {
                for (Item i : tag.getAllElements()) {
                    inputs.add(new ItemStack(i, count));
                }
                break;
            }
        }
        return inputs;
    }
    @Override
    public InputItemTagListPriority copy() {
        return new InputItemTagListPriority(tags, count, invId);
    }
}
