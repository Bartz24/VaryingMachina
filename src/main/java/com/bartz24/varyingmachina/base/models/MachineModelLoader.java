package com.bartz24.varyingmachina.base.models;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class MachineModelLoader implements ICustomModelLoader {

	private Map<ResourceLocation, String> texturePathMap = new HashMap();
	private ResourceLocation baseLocation;
	private String textureLocation;

	public MachineModelLoader(ResourceLocation baseModelLocation, String textureLocation) {
		baseLocation = baseModelLocation;
		this.textureLocation = textureLocation;
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {

	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		return texturePathMap.containsKey(modelLocation);
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		IModel model = ModelLoaderRegistry.getModel(baseLocation);
		ImmutableMap<String, String> map = ImmutableMap.<String, String> builder()
				.put(textureLocation, texturePathMap.get(modelLocation)).build();
		return model.retexture(map);
	}

	public void addVariant(ResourceLocation location, String texturePath)
	{
		texturePathMap.put(location, texturePath);
	}
}
