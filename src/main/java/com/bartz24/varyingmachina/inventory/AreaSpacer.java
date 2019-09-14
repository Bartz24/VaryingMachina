package com.bartz24.varyingmachina.inventory;

public class AreaSpacer extends ContainerArea {

    public AreaSpacer(XAnchorDirection xDirection, YAnchorDirection yDirection) {
        super(xDirection, yDirection, 50);
    }

    @Override
    public int getWidth() {
        return 10;
    }

    @Override
    public int getHeight() {
        return 10;
    }
}
