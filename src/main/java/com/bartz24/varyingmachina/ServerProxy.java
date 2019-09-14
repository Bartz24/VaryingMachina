package com.bartz24.varyingmachina;

import com.EmosewaPixel.pixellib.proxy.IModProxy;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;

@Mod.EventBusSubscriber(Dist.DEDICATED_SERVER)
public class ServerProxy implements IModProxy {
    @Override
    public void enque(InterModEnqueueEvent interModEnqueueEvent) {

    }

    @Override
    public void process(InterModProcessEvent interModProcessEvent) {

    }

    @Override
    public void init() {

    }
}
