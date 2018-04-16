package com.bartz24.varyingmachina;

import org.apache.logging.log4j.Logger;

import com.bartz24.varyingmachina.proxy.CommonProxy;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = References.ModID, name = References.ModName, version = References.Version, dependencies = "after:mekanism;required-after:jaopca", useMetadata = true)
public class VaryingMachina {
	@SidedProxy(clientSide = "com.bartz24.varyingmachina.proxy.ClientProxy", serverSide = "com.bartz24.varyingmachina.proxy.ServerProxy")
	public static CommonProxy proxy;

	@Mod.Instance
	public static VaryingMachina instance;

	public static Logger logger;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	public VaryingMachina() {
		FluidRegistry.enableUniversalBucket();
	}
}