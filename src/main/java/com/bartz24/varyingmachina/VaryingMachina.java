package com.bartz24.varyingmachina;

import com.EmosewaPixel.pixellib.materialsystem.lists.Materials;
import com.EmosewaPixel.pixellib.proxy.IModProxy;
import com.EmosewaPixel.pixellib.resources.JSONAdder;
import com.bartz24.varyingmachina.block.BlockCasing;
import com.bartz24.varyingmachina.block.BlockMachine;
import com.bartz24.varyingmachina.container.ContainerMachine;
import com.bartz24.varyingmachina.entity.EntityMoverItem;
import com.bartz24.varyingmachina.entity.EntityRange;
import com.bartz24.varyingmachina.gui.GuiMachine;
import com.bartz24.varyingmachina.guide.GuiGuide;
import com.bartz24.varyingmachina.guide.VMGuidePages;
import com.bartz24.varyingmachina.item.ItemBlockMachine;
import com.bartz24.varyingmachina.item.tools.*;
import com.bartz24.varyingmachina.machine.MachineType;
import com.bartz24.varyingmachina.machine.MachineVariant;
import com.bartz24.varyingmachina.machine.ModMachines;
import com.bartz24.varyingmachina.machine.ModVariants;
import com.bartz24.varyingmachina.pixellib.PixelPlugin;
import com.bartz24.varyingmachina.recipe.ModRecipes;
import com.bartz24.varyingmachina.render.RenderMoverItem;
import com.bartz24.varyingmachina.render.RenderRange;
import com.bartz24.varyingmachina.render.TERMachine;
import com.bartz24.varyingmachina.tile.TileEntityMachine;
import com.bartz24.varyingmachina.world.LargeCountRange;
import com.bartz24.varyingmachina.world.OreGeneration;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod("varyingmachina")
public class VaryingMachina {
    private static final Logger LOGGER = LogManager.getLogger();

    private static IModProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

    public static ItemGroup main = new ItemGroup("varyingmachina.main") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(circuit3);
        }
    };

    public static ItemGroup machines = new ItemGroup("varyingmachina.machines") {
        @Override
        public ItemStack createIcon() {
            return createMachineStack(ModMachines.fabricator, ModVariants.iron, ModVariants.wood, 1);
        }
    };

    public VaryingMachina() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);

        PacketHandler.setup();
        PixelPlugin.setup();

        LargeCountRange.register();
    }

    private void setup(final FMLCommonSetupEvent event) {
        ModRecipes.setup();
        OreGeneration.setup();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {

        ScreenManager.registerFactory(machineContainer, GuiMachine::new);

        RenderingRegistry.registerEntityRenderingHandler(EntityMoverItem.class, RenderMoverItem::new);

        RenderingRegistry.registerEntityRenderingHandler(EntityRange.class, RenderRange::new);

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachine.class, new TERMachine());

        VMGuidePages.setup();
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        proxy.enque(event);
    }

    private void processIMC(final InterModProcessEvent event) {
        proxy.process(event);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (VMKeyBindings.guideKey.isPressed()) {
            Minecraft.getInstance().displayGuiScreen(new GuiGuide());
        }

    }


    @SubscribeEvent
    public void addToTooltip(ItemTooltipEvent event) {
        if (!event.getItemStack().isEmpty() && event.getItemStack().getItem() instanceof TieredItem) {
            event.getToolTip().addAll(1, Helpers.getToolInfo(((TieredItem) event.getItemStack().getItem()).getTier()));
        }
    }


    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {

    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        ModRecipes.onWorldStart(event.getWorld());
    }

    public static HashMap<String, Block> blockMachines = new HashMap<>();
    public static HashMap<String, Item> itemMachines = new HashMap<>();
    public static HashMap<String, Block> blockCasings = new HashMap<>();
    public static HashMap<String, Item> itemCasings = new HashMap<>();

    public static Item circuit1;
    public static Item circuit2;
    public static Item circuit3;
    public static Item circuit4;
    public static Item circuit5;
    public static Item circuit6;

    public static ItemStack createMachineStack(MachineType type, MachineVariant main, MachineVariant casing, int count) {
        return new ItemStack(itemMachines.get(type.getId() + "." + main.getName() + "." + casing.getName()), count);
    }

    public static TileEntityType machine;

    public static TileEntityType extenderTE;

    public static EntityType moverItemEntityType;

    public static EntityType rangeEntityType;

    public static ContainerType machineContainer;


    public static <T, R> Supplier<R> bind(Function<T, R> fn, T val) {
        return () -> fn.apply(val);
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> e) {
            for (String variant : ModVariants.types.keySet()) {
                for (String type : ModMachines.types.keySet()) {
                    for (String casingVariant : ModVariants.types.keySet()) {
                        Block block = new BlockMachine(type, variant, casingVariant, Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 8));
                        blockMachines.put(type + "." + variant + "." + casingVariant, block);
                        e.getRegistry().register(block);
                    }
                }
                BlockCasing casing = new BlockCasing(Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 8), ModVariants.types.get(variant));
                blockCasings.put(variant, casing);
                e.getRegistry().register(casing);
            }
        }

        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> e) {
            for (String variant : ModVariants.types.keySet()) {
                for (String type : ModMachines.types.keySet()) {
                    for (String casingVariant : ModVariants.types.keySet()) {
                        ItemBlockMachine item = new ItemBlockMachine((BlockMachine) blockMachines.get(type + "." + variant + "." + casingVariant), new Item.Properties().group(machines));
                        if (ModMachines.types.get(type).isBlacklisted(item))
                            continue;
                        itemMachines.put(type + "." + variant + "." + casingVariant, item);
                        e.getRegistry().register(item);
                    }
                }
                Item casing = new BlockItem(blockCasings.get(variant), new Item.Properties().group(machines)).setRegistryName(blockCasings.get(variant).getRegistryName());
                itemCasings.put(variant, casing);
                e.getRegistry().register(casing);
            }

            e.getRegistry().register(circuit1 = new Item(new Item.Properties().group(main)).setRegistryName(new ResourceLocation("varyingmachina", "circuit1")));
            e.getRegistry().register(circuit2 = new Item(new Item.Properties().group(main)).setRegistryName(new ResourceLocation("varyingmachina", "circuit2")));
            e.getRegistry().register(circuit3 = new Item(new Item.Properties().group(main)).setRegistryName(new ResourceLocation("varyingmachina", "circuit3")));
            e.getRegistry().register(circuit4 = new Item(new Item.Properties().group(main)).setRegistryName(new ResourceLocation("varyingmachina", "circuit4")));
            e.getRegistry().register(circuit5 = new Item(new Item.Properties().group(main)).setRegistryName(new ResourceLocation("varyingmachina", "circuit5")));
            e.getRegistry().register(circuit6 = new Item(new Item.Properties().group(main)).setRegistryName(new ResourceLocation("varyingmachina", "circuit6")));

            for (MachineVariant variant : ModVariants.types.values()) {
                if (Materials.exists(variant.getName()) && variant != ModVariants.iron && variant != ModVariants.gold && variant != ModVariants.stone) {
                    e.getRegistry().register(new VMItemPickaxe(Materials.get(variant.getName()), variant));
                    e.getRegistry().register(new VMItemShovel(Materials.get(variant.getName()), variant));
                    e.getRegistry().register(new VMItemAxe(Materials.get(variant.getName()), variant));
                    e.getRegistry().register(new VMItemSword(Materials.get(variant.getName()), variant));
                    e.getRegistry().register(new VMItemHoe(Materials.get(variant.getName()), variant));
                }
            }
        }


        @SubscribeEvent(
                priority = EventPriority.LOWEST
        )
        public static void onLateItemRegistry(RegistryEvent.Register<Item> e) {
            for (String variant : ModVariants.types.keySet()) {
                for (String type : ModMachines.types.keySet()) {
                    for (String casingVariant : ModVariants.types.keySet()) {
                        addBlockItemModel(type, variant, casingVariant);
                        addBlockstate(type, variant, casingVariant);
                    }
                }

                addCasingBlockItemModel(variant);
                addCasingBlockstate(variant);
            }

            for (MachineVariant variant : ModVariants.types.values()) {
                if (Materials.exists(variant.getName()) && variant != ModVariants.iron && variant != ModVariants.gold && variant != ModVariants.stone) {
                    addToolItemModel("pickaxe", variant.getName());
                    addToolItemModel("shovel", variant.getName());
                    addToolItemModel("axe", variant.getName());
                    addToolItemModel("sword", variant.getName());
                    addToolItemModel("hoe", variant.getName());
                }
            }
        }

        private static void addToolItemModel(String toolType, String material) {
            ResourceLocation itemLocation = new ResourceLocation("varyingmachina", "models/item/" + toolType + "." + material + ".json");
            JsonObject itemModel = new JsonObject();
            itemModel.addProperty("parent", "pixellib:item/materialitems/" + Materials.get(material).getTextureType().toString() + "/" + toolType);
            JSONAdder.addAssetsJSON(itemLocation, itemModel);
        }


        private static void addBlockItemModel(String type, String variant, String casingVariant) {
            ResourceLocation blockLocation = new ResourceLocation("varyingmachina", "models/block/" + type + "." + variant + "." + casingVariant + ".json");
            JsonObject model = new JsonObject();
            JsonObject textures = new JsonObject();
            textures.addProperty("machine", ModVariants.types.get(variant).getTexture().toString());
            textures.addProperty("casing", ModVariants.types.get(casingVariant).getTexture().toString());
            textures.addProperty("overlay", ModMachines.types.get(type).getTextureFront() == null ? ModVariants.types.get(variant).getTexture().toString() : ModMachines.types.get(type).getTextureFront().toString());
            textures.addProperty("overlaytop", ModMachines.types.get(type).getTextureTop() == null ? ModVariants.types.get(variant).getTexture().toString() : ModMachines.types.get(type).getTextureTop().toString());
            textures.addProperty("overlayside", ModMachines.types.get(type).getTextureSide() == null ? ModVariants.types.get(variant).getTexture().toString() : ModMachines.types.get(type).getTextureSide().toString());
            textures.addProperty("overlaybottom", ModMachines.types.get(type).getTextureBottom() == null ? ModVariants.types.get(variant).getTexture().toString() : ModMachines.types.get(type).getTextureBottom().toString());
            model.addProperty("parent", ModMachines.types.get(type).getParentModel().toString());
            model.add("textures", textures);
            JSONAdder.addAssetsJSON(blockLocation, model);

            ResourceLocation itemLocation = new ResourceLocation("varyingmachina", "models/item/" + type + "." + variant + "." + casingVariant + ".json");
            JsonObject itemModel = new JsonObject();
            itemModel.addProperty("parent", "varyingmachina:block/" + type + "." + variant + "." + casingVariant);
            JSONAdder.addAssetsJSON(itemLocation, itemModel);
        }

        private static void addBlockstate(String type, String variant, String casingVariant) {
            ResourceLocation location = new ResourceLocation("varyingmachina", "blockstates/" + type + "." + variant + "." + casingVariant + ".json");
            JsonObject blockstate = new JsonObject();
            JsonObject variants = new JsonObject();
            int dir = 0;
            for (String s : new String[]{"north", "east", "south", "west", "up", "down"}) {
                JsonObject model = new JsonObject();
                model.addProperty("model", "varyingmachina:block/" + type + "." + variant + "." + casingVariant);
                if (s.equals("up"))
                    model.addProperty("x", 270);
                else if (s.equals("down"))
                    model.addProperty("x", 90);
                else
                    model.addProperty("y", dir);
                variants.add("facing=" + s, model);
                dir += 90;
            }

            blockstate.add("variants", variants);
            JSONAdder.addAssetsJSON(location, blockstate);
        }


        private static void addCasingBlockItemModel(String casingVariant) {
            ResourceLocation blockLocation = new ResourceLocation("varyingmachina", "models/block/casing." + casingVariant + ".json");
            JsonObject model = new JsonObject();
            JsonObject textures = new JsonObject();
            textures.addProperty("all", ModVariants.types.get(casingVariant).getTexture().toString());
            model.addProperty("parent", "block/cube_all");
            model.add("textures", textures);
            JSONAdder.addAssetsJSON(blockLocation, model);

            ResourceLocation itemLocation = new ResourceLocation("varyingmachina", "models/item/casing." + casingVariant + ".json");
            JsonObject itemModel = new JsonObject();
            itemModel.addProperty("parent", "varyingmachina:block/casing." + casingVariant);
            JSONAdder.addAssetsJSON(itemLocation, itemModel);
        }

        private static void addCasingBlockstate(String casingVariant) {
            ResourceLocation location = new ResourceLocation("varyingmachina", "blockstates/casing" + "." + casingVariant + ".json");
            JsonObject blockstate = new JsonObject();
            JsonObject variants = new JsonObject();
            JsonObject variant = new JsonObject();
            variant.addProperty("model", "varyingmachina:block/casing." + casingVariant);
            variants.add("", variant);
            blockstate.add("variants", variants);
            JSONAdder.addAssetsJSON(location, blockstate);
        }

        @SubscribeEvent
        public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> e) {
            e.getRegistry().register(machine = TileEntityType.Builder.create(TileEntityMachine::new, blockMachines.values().toArray(new Block[blockMachines.size()])).build(null).setRegistryName("varyingmachina:machine"));
            e.getRegistry().register(extenderTE = TileEntityType.Builder.create(TileEntityMachine::new, blockCasings.values().toArray(new
                    Block[blockCasings.size()])).build(null).setRegistryName("varyingmachina:casing"));
        }


        @SubscribeEvent
        public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> e) {
            e.getRegistry().register(machineContainer = IForgeContainerType.create(ContainerMachine::new).setRegistryName("varyingmachina:machine"));
        }

        @SubscribeEvent
        public static void onEntityRegistry(final RegistryEvent.Register<EntityType<?>> e) {
            e.getRegistry().register(moverItemEntityType = EntityType.Builder.create(EntityMoverItem::new, EntityClassification.MISC)
                    .size(0.25f, 0.25f)
                    .setUpdateInterval(20)
                    .setTrackingRange(24)
                    .setShouldReceiveVelocityUpdates(true)
                    .setCustomClientFactory((spawnEntity, world) -> {
                        return (EntityMoverItem) moverItemEntityType.create(world);
                    })
                    .build("varyingmachina:moveritem")
                    .setRegistryName("varyingmachina:moveritem"));
            e.getRegistry().register(rangeEntityType = EntityType.Builder.create(EntityRange::new, EntityClassification.MISC)
                    .size(1f, 1f)
                    .setUpdateInterval(10)
                    .setTrackingRange(16)
                    .setShouldReceiveVelocityUpdates(false)
                    .setCustomClientFactory((spawnEntity, world) -> {
                        return (EntityRange) rangeEntityType.create(world);
                    })
                    .build("varyingmachina:range")
                    .setRegistryName("varyingmachina:range"));
        }
    }
}
