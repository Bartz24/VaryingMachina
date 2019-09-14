package com.bartz24.varyingmachina.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class InputItemTag extends InputItemList {

    private ItemTags.Wrapper tag;
    private int count;

    public InputItemTag(Tag<Item> tag, int count, String invId) {
        super(new ArrayList<>(), invId);
        this.tag = new ItemTags.Wrapper(tag.getId());
        this.count = count;
    }

    public InputItemTag(Tag<Item> tag, int count) {
        this(tag, count, "input");
    }

    public InputItemTag(ResourceLocation tag, int count) {
        this(new ItemTags.Wrapper(tag), count, "input");
    }

    public InputItemTag(String tag, int count) {
        this(new ResourceLocation(tag), count);
    }

    @Override
    public List<ItemStack> getInputs() {
        List<ItemStack> inputs = new ArrayList<>();
        for (Item i : tag.getAllElements())
            inputs.add(new ItemStack(i, count));
        return inputs;
    }

    @Override
    public InputItemTag scale(int factor) {
        count *= factor;
        return this;
    }

    @Override
    public boolean combineWith(InputBase input2) {
        if (input2 instanceof InputItemTag && tag.getId().equals(input2.getId())) {
            count += ((InputItemTag) input2).count;
            return true;
        }
        return false;
    }

    @Override
    public InputItemTag copy() {
        return new InputItemTag(tag, count, invId);
    }
}
