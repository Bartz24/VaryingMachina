package com.bartz24.varyingmachina.proxy;

import com.bartz24.varyingmachina.VaryingMachina;
import com.bartz24.varyingmachina.base.machine.WrenchHelper;
import com.bartz24.varyingmachina.jaopca.JAOPCAPlugin;
import com.bartz24.varyingmachina.network.VaryingMachinaPacketHandler;
import com.bartz24.varyingmachina.registry.*;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {

    ModGuiHandler handler;
    public void preInit(FMLPreInitializationEvent e) {
        ModVariants.registerVariants();
        ModItems.registerMachines();
        WrenchHelper.registerClasses();
        MachineRegistry.getRegistryRecipes(e);
        handler = new ModGuiHandler(e.getSide() == Side.SERVER);
        VaryingMachinaPacketHandler.preInit();
        JAOPCAPlugin.preInit();
    }

    public void init(FMLInitializationEvent e) {
        NetworkRegistry.INSTANCE.registerGuiHandler(VaryingMachina.instance, handler);
        ModEntities.registerEntities();
        MachineRegistry.registerRecipes();
        ModRecipes.init();
    }

    public void postInit(FMLPostInitializationEvent e) {
        MachineRegistry.postRegisterRecipes();
    }
}