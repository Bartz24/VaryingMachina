package com.bartz24.varyingmachina.inventory;

public class AreaItemHandler extends ContainerArea {
    public AreaItemHandler(ItemHandlerFiltered handler, XAnchorDirection xDirection, YAnchorDirection yDirection, int weight) {
        super(xDirection, yDirection, weight);

        int maxWidth = (int) Math.ceil(Math.sqrt((double) handler.getSlots()) * Math.exp((double) handler.getSlots() / 200));

        int x = 0, y = 0;
        for (int i = 0; i < handler.getSlots(); i++) {
            slots.add(new SlotFiltered(handler, i, x * 18, y * 18, handler.getFilter(i)));
            x++;
            if (x >= maxWidth) {
                x = 0;
                y++;
            }
        }
    }
}
