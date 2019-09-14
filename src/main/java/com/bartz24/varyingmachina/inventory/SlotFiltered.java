package com.bartz24.varyingmachina.inventory;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class SlotFiltered extends SlotRestricted {

    private List<ItemStack> filters;


    public List<ItemStack> getFilters() {
        return filters;
    }

    private ItemHandlerRestricted handlerRestricted;

    public SlotFiltered(ItemHandlerRestricted itemHandler, int index, int xPosition, int yPosition, List<ItemStack> allowedItems) {
        super(itemHandler, index, xPosition, yPosition);

        filters = allowedItems;

        handlerRestricted = itemHandler;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        if(getFilters().size()==0)
            return super.isItemValid(stack) && !handlerRestricted.isNoInsert();
        for (ItemStack filter : getFilters()) {
            if (filter.isItemEqual(stack))
                return super.isItemValid(stack) && !handlerRestricted.isNoInsert();
        }
        return false;
    }


}
