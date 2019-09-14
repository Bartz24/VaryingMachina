package com.bartz24.varyingmachina.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputItemTagList extends InputItemList {

    protected List<ItemTags.Wrapper> tags = new ArrayList<>();
    protected int count;

    public InputItemTagList(List<ItemTags.Wrapper> tags, int count, String invId) {
        super(new ArrayList<>(), invId);
        this.tags.addAll(tags);
        this.count = count;
    }

    public InputItemTagList(int count, List<ResourceLocation> tags, String invId) {
        super(new ArrayList<>(), invId);
        tags.forEach(tag -> this.tags.add(new ItemTags.Wrapper(tag)));
        this.count = count;
    }

    public InputItemTagList(int count, ItemTags.Wrapper... tags) {
        this(Arrays.asList(tags), count, "input");
    }

    public InputItemTagList(int count, ResourceLocation... tags) {
        this(count, Arrays.asList(tags), "input");
    }

    @Override
    public List<ItemStack> getInputs() {
        List<ItemStack> inputs = new ArrayList<>();
        for (Tag<Item> tag : tags) {
            for (Item i : tag.getAllElements()) {
                inputs.add(new ItemStack(i, count));
            }
        }
        return inputs;
    }

    @Override
    public InputItemTagList scale(int factor) {
        count *= factor;
        return this;
    }

    @Override
    public boolean combineWith(InputBase input2) {
        if (input2 instanceof InputItemTagList && ((InputItemTagList) input2).tags.size() == tags.size()) {
            for (int i = 0; i < tags.size(); i++) {
                if (!tags.get(i).getId().equals(((InputItemTagList) input2).tags.get(i).getId()))
                    return false;
            }
            count += ((InputItemTagList) input2).count;
            return true;
        }
        return false;
    }
    @Override
    public InputItemTagList copy() {
        return new InputItemTagList(tags, count, invId);
    }
}
