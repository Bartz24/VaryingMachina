package com.bartz24.varyingmachina.machine;

import com.bartz24.varyingmachina.inventory.*;
import com.bartz24.varyingmachina.item.ItemBlockMachine;
import com.bartz24.varyingmachina.recipe.InputBase;
import com.bartz24.varyingmachina.recipe.RecipeBase;
import com.bartz24.varyingmachina.recipe.RecipeReqBase;
import com.bartz24.varyingmachina.tile.IRecipeProcessor;
import javafx.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class MachineType<T extends IRecipeProcessor> {
    private String id;
    private HashMap<ResourceLocation, RecipeBase<T>> recipes = new HashMap<>();
    private HashMap<String, ItemHandlerType> itemHandlerTypes = new HashMap<>();
    private HashMap<String, EnergyHandlerType> energyHandlerTypes = new HashMap<>();
    private List<String> inputInvs = new ArrayList<>();
    private List<String> outputInvs = new ArrayList<>();
    private List<String> energyInputInvs = new ArrayList<>();
    private List<String> energyOutputInvs = new ArrayList<>();
    private List<MachineStat> displayedStats = new ArrayList<>();
    private boolean noRecipes = false, noFuel = false, allDirectionFacing = false;
    private Consumer<T> overrideUpdateFunction;
    private Function<T, Pair<BlockPos[], Integer>> displayRange;
    private Function<BlockState, VoxelShape> customBoundingShape;
    private ResourceLocation textureFront = null, textureTop = null, textureSide = null, textureBottom = null, parentModel = new ResourceLocation("varyingmachina:block/machine");
    private double energyRateMultiplier = 1;
    private int craftingMachineMultiplier = 1; // Used for determining how many machines of this type is created in the fabricator
    private Consumer<T> renderTER;
    private Function<Item, InputBase> extraInput;
    private Predicate<ItemBlockMachine> blacklisted;
    private boolean isMultiblock = true;

    public MachineType(String id, MachineStat... stats) {
        this.id = id;

        displayedStats = Arrays.asList(stats);
        ModMachines.types.put(id, this);
    }

    public void addRecipe(ResourceLocation name, RecipeBase<T> recipe) {
        if (recipes.containsKey(name))
            throw new IllegalStateException(id + " already has a recipe named " + name.toString());
        recipes.put(name, recipe);
    }

    public List<ResourceLocation> getAvailableRecipes(InputBase[] inputs, RecipeReqBase[] reqs) {
        List<ResourceLocation> list = new ArrayList<>();
        RecipeBase<T> compareRecipe = new RecipeBase<T>();
        compareRecipe.addInputs(inputs);
        for (ResourceLocation location : recipes.keySet()) {
            if (recipes.get(location).matchesInputsReqs(compareRecipe))
                list.add(location);
        }
        return list;
    }

    public RecipeBase<T> getRecipe(ResourceLocation name) {
        return recipes.get(name);
    }

    public ResourceLocation getNameOfRecipe(RecipeBase recipe) {
        for (ResourceLocation location : recipes.keySet()) {
            if (recipes.get(location) == recipe)
                return location;
        }
        return null;
    }

    public HashMap<ResourceLocation, RecipeBase<T>> getRecipes() {
        return recipes;
    }

    public MachineType addItemHandler(String id, Class<? extends ItemStackHandler> handlerClass, Class[] argTypes, Object[] args) {
        itemHandlerTypes.put(id, new ItemHandlerType(handlerClass, argTypes, args));
        return this;
    }

    public MachineType setTextureFront(ResourceLocation textureFront) {
        this.textureFront = textureFront;
        return this;
    }

    public MachineType setTextureTop(ResourceLocation textureTop) {
        this.textureTop = textureTop;
        return this;
    }

    public MachineType setTextureSide(ResourceLocation textureSide) {
        this.textureSide = textureSide;
        return this;
    }

    public MachineType setTextureBottom(ResourceLocation textureBottom) {
        this.textureBottom = textureBottom;
        return this;
    }

    public MachineType setParentModel(ResourceLocation parentModel) {
        this.parentModel = parentModel;
        return this;
    }

    public MachineType setNoMultiblock() {
        isMultiblock = false;
        return this;
    }

    public boolean isMultiblock() {
        return isMultiblock;
    }

    public ResourceLocation getParentModel() {
        return parentModel;
    }

    public ResourceLocation getTextureFront() {
        return textureFront;
    }

    public ResourceLocation getTextureTop() {
        return textureTop;
    }

    public ResourceLocation getTextureBottom() {
        return textureBottom;
    }

    public ResourceLocation getTextureSide() {
        return textureSide;
    }

    public OmniItemHandler createItemHandler(T tile) {
        OmniItemHandler handler = new OmniItemHandler();
        for (String s : itemHandlerTypes.keySet())
            handler.setHandler(s, itemHandlerTypes.get(s).createHandler());
        return handler;
    }


    private class ItemHandlerType {
        Class<? extends ItemStackHandler> handlerClass;
        Class[] argTypes;
        Object[] args;

        public ItemHandlerType(Class<? extends ItemStackHandler> handlerClass, Class[] argTypes, Object[] args) {
            this.handlerClass = handlerClass;
            this.argTypes = argTypes;
            this.args = args;
        }

        public ItemStackHandler createHandler() {
            try {
                return handlerClass.getDeclaredConstructor(argTypes).newInstance(args);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public MachineType addInputItemHandler(String id) {
        addItemHandler(id, ItemHandlerFiltered.class, new Class[]{int.class, boolean.class, boolean.class}, new Object[]{0, false, true});
        inputInvs.add(id);
        return this;
    }

    public MachineType addOutputItemHandler(String id) {
        addItemHandler(id, ItemHandlerFiltered.class, new Class[]{int.class, boolean.class, boolean.class}, new Object[]{0, true, false});
        outputInvs.add(id);
        return this;
    }

    public MachineType addOutputItemHandler(String id, int size) {
        addItemHandler(id, ItemHandlerFiltered.class, new Class[]{int.class, boolean.class, boolean.class}, new Object[]{size, true, false});
        outputInvs.add(id);
        return this;
    }

    public MachineType addFilterItemHandler(String id, int size) {
        addItemHandler(id, FilterItemHandler.class, new Class[]{int.class}, new Object[]{size});
        return this;
    }

    public MachineType addDoubleItemHandler(String id, int size) {
        addItemHandler(id, ItemHandlerFiltered.class, new Class[]{int.class, boolean.class, boolean.class}, new Object[]{size, false, false});
        outputInvs.add(id);
        inputInvs.add(id);
        return this;
    }


    public OmniEnergyHandler createEnergyHandler(T tile) {
        OmniEnergyHandler handler = new OmniEnergyHandler();
        for (String s : energyHandlerTypes.keySet())
            handler.setHandler(s, energyHandlerTypes.get(s).createHandler());
        return handler;
    }


    private class EnergyHandlerType {
        Class<? extends BetterEnergyStorage> handlerClass;
        Class[] argTypes;
        Object[] args;

        public EnergyHandlerType(Class<? extends BetterEnergyStorage> handlerClass, Class[] argTypes, Object[] args) {
            this.handlerClass = handlerClass;
            this.argTypes = argTypes;
            this.args = args;
        }

        public BetterEnergyStorage createHandler() {
            try {
                return handlerClass.getDeclaredConstructor(argTypes).newInstance(args);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public MachineType addEnergyHandler(String id, Class<? extends BetterEnergyStorage> handlerClass, Class[] argTypes, Object[] args) {
        energyHandlerTypes.put(id, new EnergyHandlerType(handlerClass, argTypes, args));
        return this;
    }

    public MachineType addInputEnergyHandler(String id) {
        addEnergyHandler(id, BetterEnergyStorage.class, new Class[]{int.class, int.class, int.class, int.class}, new Object[]{0, Integer.MAX_VALUE, 0, 0});
        energyInputInvs.add(id);
        return this;
    }

    public MachineType addOutputEnergyHandler(String id) {
        addEnergyHandler(id, BetterEnergyStorage.class, new Class[]{int.class, int.class, int.class, int.class}, new Object[]{0, 0, Integer.MAX_VALUE, 0});
        energyOutputInvs.add(id);
        return this;
    }

    public MachineType addDoubleEnergyHandler(String id) {
        addEnergyHandler(id, BetterEnergyStorage.class, new Class[]{int.class, int.class, int.class, int.class}, new Object[]{0, Integer.MAX_VALUE, Integer.MAX_VALUE, 0});
        energyInputInvs.add(id);
        energyOutputInvs.add(id);
        return this;
    }

    public MachineType setBlacklist(Predicate<ItemBlockMachine> itemBlacklist) {
        blacklisted = itemBlacklist;
        return this;
    }

    public boolean isBlacklisted(ItemBlockMachine item) {
        if (blacklisted == null)
            return false;
        return blacklisted.test(item);
    }

    public MachineType setNoRecipes() {
        this.noRecipes = true;
        return this;
    }

    public MachineType setNoFuel() {
        this.noFuel = true;
        return this;
    }

    public MachineType setAllDirectionFacing() {
        this.allDirectionFacing = true;
        return this;
    }

    public boolean isNoRecipes() {
        return noRecipes;
    }

    public boolean isNoFuel() {
        return noFuel;
    }

    public boolean isAllDirectionFacing() {
        return allDirectionFacing;
    }

    public MachineType<T> setOverrideUpdateFunction(Consumer<T> overrideUpdateFunction) {
        this.overrideUpdateFunction = overrideUpdateFunction;
        return this;
    }

    public List<String> getInputInvs() {
        return inputInvs;
    }

    public List<String> getOutputInvs() {
        return outputInvs;
    }

    public List<String> getEnergyInputInvs() {
        return energyInputInvs;
    }

    public List<String> getEnergyOutputInvs() {
        return energyOutputInvs;
    }

    public List<MachineStat> getDisplayedStats() {
        return displayedStats;
    }

    public Consumer<T> getOverrideUpdateFunction() {
        return overrideUpdateFunction;
    }

    public MachineType setEnergyRateMultiplier(double energyRateMultiplier) {
        this.energyRateMultiplier = energyRateMultiplier;
        return this;
    }

    public MachineType setCraftingMachineMultiplier(int craftingMachineMultiplier) {
        this.craftingMachineMultiplier = craftingMachineMultiplier;
        return this;
    }

    public int getCraftingMachineMultiplier() {
        return craftingMachineMultiplier;
    }

    public double getEnergyRateMultiplier() {
        return energyRateMultiplier;
    }

    public String getId() {
        return id;
    }

    public double calculateSpecialStat(String statName, MachineVariant mainVariant, MachineVariant casingVariant) {
        return 0;
    }

    public String getTextSpecialStat(String statName, MachineVariant mainVariant, MachineVariant casingVariant) {
        return "";
    }

    public TextFormatting getColorSpecialStat(String statName) {
        return TextFormatting.RESET;
    }

    public List<ITextComponent> getSpecialTooltips(MachineVariant mainVariant, MachineVariant casingVariant) {
        List<ITextComponent> list = new ArrayList<>();

        getSpecialTooltipTypes().forEach(type -> {
            list.add(new StringTextComponent(getColorSpecialStat(type) + new TranslationTextComponent("varyingmachina.stat." + type.toLowerCase(), getTextSpecialStat(type, mainVariant, casingVariant)).getFormattedText()));
        });

        return list;
    }

    public List<String> getSpecialTooltipTypes() {
        return new ArrayList<>();
    }

    public MachineType setCustomBoundingShape(Function<BlockState, VoxelShape> customBoundingShape) {
        this.customBoundingShape = customBoundingShape;
        return this;
    }

    public MachineType setDisplayRange(Function<T, Pair<BlockPos[], Integer>> displayRange) {
        this.displayRange = displayRange;
        return this;
    }

    public Function<T, Pair<BlockPos[], Integer>> getDisplayRange() {
        return displayRange;
    }

    public Function<BlockState, VoxelShape> getCustomBoundingShape() {
        return customBoundingShape;
    }

    @OnlyIn(Dist.CLIENT)
    public void setRenderTER(Consumer<T> renderTER) {
        this.renderTER = renderTER;
    }

    @OnlyIn(Dist.CLIENT)
    public Consumer<T> getRenderTER() {
        return renderTER;
    }

    public MachineType setExtraInput(Function<Item, InputBase> extraInput) {
        this.extraInput = extraInput;
        return this;
    }

    public Function<Item, InputBase> getExtraInput() {
        return extraInput;
    }
}
