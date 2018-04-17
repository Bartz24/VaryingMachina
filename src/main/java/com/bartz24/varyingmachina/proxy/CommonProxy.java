package com.bartz24.varyingmachina.proxy;

import com.bartz24.varyingmachina.VaryingMachina;
import com.bartz24.varyingmachina.base.machine.WrenchHelper;
import com.bartz24.varyingmachina.jaopca.JAOPCAPlugin;
import com.bartz24.varyingmachina.network.VaryingMachinaPacketHandler;
import com.bartz24.varyingmachina.registry.*;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e) {
        ModVariants.registerVariants();
        ModItems.registerMachines();
        WrenchHelper.registerClasses();
        MachineRegistry.getRegistryRecipes(e);
        new ModGuiHandler();
        VaryingMachinaPacketHandler.preInit();
        JAOPCAPlugin.preInit();
    }

    public void init(FMLInitializationEvent e) {
        NetworkRegistry.INSTANCE.registerGuiHandler(VaryingMachina.instance, new ModGuiHandler());
        ModEntities.registerEntities();
        MachineRegistry.registerRecipes();
        ModRecipes.init();
    }

    public void postInit(FMLPostInitializationEvent e) {
    }
}