package com.bartz24.varyingmachina.jaopca;

import thelm.jaopca.api.JAOPCAApi;

public class JAOPCAPlugin {
    public static void preInit()
    {
        JAOPCAApi.registerModule(new ModuleWire());
    }
}
