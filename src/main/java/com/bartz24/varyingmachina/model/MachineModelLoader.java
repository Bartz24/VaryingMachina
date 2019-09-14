package com.bartz24.varyingmachina.model;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;

import java.util.HashMap;
import java.util.Map;

public class MachineModelLoader implements ICustomModelLoader {
    private Map<ResourceLocation, ModelInfo> texturePathMap = new HashMap();
    private ResourceLocation baseLocation;

    public MachineModelLoader(ResourceLocation baseModelLocation) {
        baseLocation = baseModelLocation;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return texturePathMap.containsKey(modelLocation);
    }

    @Override
    public IUnbakedModel loadModel(ResourceLocation modelLocation) throws Exception {
        IUnbakedModel model = ModelLoaderRegistry.getModel(baseLocation);
        ImmutableMap<String, String> map = ImmutableMap.<String, String>builder()
                .put("machine", texturePathMap.get(modelLocation).machine)
                .put("casing", texturePathMap.get(modelLocation).casing)
                .put("overlay", texturePathMap.get(modelLocation).overlay)
                .put("overlaytop", texturePathMap.get(modelLocation).overlaytop).build();
        return model.retexture(map);
    }

    public void addVariant(ResourceLocation location, ModelInfo modelInfo) {
        texturePathMap.put(location, modelInfo);
    }
    public static class ModelInfo {
        private String machine, casing, overlay = "#all", overlaytop = "#all";

        public ModelInfo(String machine, String casing) {
            this.machine = machine;
            this.casing = casing;
            this.overlay = overlay;
            this.overlaytop = overlaytop;
        }

        public String getMachine() {
            return machine;
        }

        public String getCasing() {
            return casing;
        }

        public String getOverlay() {
            return overlay;
        }

        public String getOverlaytop() {
            return overlaytop;
        }

        public ModelInfo setOverlay(String overlay) {
            this.overlay = overlay;
            return this;
        }

        public ModelInfo setOverlaytop(String overlaytop) {
            this.overlaytop = overlaytop;
            return this;
        }
    }
}
