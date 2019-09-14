package com.bartz24.varyingmachina.jei;

import com.bartz24.varyingmachina.Helpers;
import com.bartz24.varyingmachina.PacketHandler;
import com.bartz24.varyingmachina.VaryingMachina;
import com.bartz24.varyingmachina.gui.GuiMachine;
import com.bartz24.varyingmachina.inventory.ContainerArea;
import com.bartz24.varyingmachina.inventory.InventoryManager;
import com.bartz24.varyingmachina.item.ItemBlockMachine;
import com.bartz24.varyingmachina.jei.display.*;
import com.bartz24.varyingmachina.machine.ModMachines;
import com.bartz24.varyingmachina.recipe.RecipeBase;
import com.bartz24.varyingmachina.recipe.RecipeReqBase;
import com.bartz24.varyingmachina.tile.TileEntityMachine;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.ITooltipCallback;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.gui.recipes.RecipesGui;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Supplier;

public class MachineCategory implements IRecipeCategory<RecipeBase> {

    private String type;
    private IDrawable icon;
    private IGuiHelper helper;
    private int maxWidth = 0, maxHeight = 0;


    public MachineCategory(IGuiHelper helper, String type) {
        this.type = type;
        this.helper = helper;
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation("varyingmachina", type);
    }

    @Override
    public Class<? extends RecipeBase> getRecipeClass() {
        return RecipeBase.class;
    }

    @Override
    public String getTitle() {
        return Translator.translateToLocal("varyingmachina.jei." + type);
    }

    @Override
    public IDrawable getBackground() {
        return helper.createDrawable(new ResourceLocation("varyingmachina", "textures/gui/blankjei.png"), 0, 0, getMaxWidth(), getMaxHeight() - 16);
    }

    private int getMaxWidth() {
        if (maxWidth == 0) {
            for (Object recipe : ModMachines.types.get(type).getRecipes().values()) {
                int width = getInvFromRecipe((RecipeBase) recipe).getMaxWidth();
                if (width > maxWidth)
                    maxWidth = width;
            }
        }
        return maxWidth;
    }

    private int getMaxHeight() {
        if (maxHeight == 0) {
            for (Object recipe : ModMachines.types.get(type).getRecipes().values()) {
                int height = getInvFromRecipe((RecipeBase) recipe).getMaxHeight();
                if (height > maxHeight)
                    maxHeight = height;
            }
        }
        return maxHeight;
    }


    private InventoryManager getInvFromRecipe(RecipeBase recipe) {
        InventoryManager inv = new InventoryManager(16);
        for (Object s : ModMachines.types.get(type).getInputInvs()) {
            inv.addArea(new JEIAreaItemHandler(recipe, s.toString(), ContainerArea.XAnchorDirection.CENTER, ContainerArea.YAnchorDirection.TOP, 0, true));
        }
        for (Object s : ModMachines.types.get(type).getOutputInvs()) {
            inv.addArea(new JEIAreaItemHandler(recipe, s.toString(), ContainerArea.XAnchorDirection.CENTER, ContainerArea.YAnchorDirection.TOP, 100, false));
        }
        for (Object s : ModMachines.types.get(type).getEnergyInputInvs()) {
            inv.addArea(new JEIAreaEnergy(ContainerArea.XAnchorDirection.CENTER, ContainerArea.YAnchorDirection.TOP, -20, ((Integer) ((List) recipe.getInputs(Integer.class, s.toString()).get(0)).get(0)).intValue()));
        }
        for (Object s : ModMachines.types.get(type).getEnergyOutputInvs()) {
            inv.addArea(new JEIAreaEnergy(ContainerArea.XAnchorDirection.CENTER, ContainerArea.YAnchorDirection.TOP, 120, ((Integer) recipe.getOutputs(Integer.class, s.toString()).get(0)).intValue()));
        }
        inv.addArea(new JEIAreaArrow(ContainerArea.XAnchorDirection.CENTER, ContainerArea.YAnchorDirection.TOP, recipe.getTime()));

        LinkedHashMap<String, Integer> disps = new LinkedHashMap<>();
        if (recipe.getTime() > 0) {
            disps.put(new TranslationTextComponent("varyingmachina.jei.time", Helpers.round(recipe.getTime() / 20d, 3)).getFormattedText(), TextFormatting.BLUE.getColor());
        }
        if (recipe.getFuelUsage() != 1) {
            disps.put(new TranslationTextComponent("varyingmachina.jei.fuel", Helpers.round(recipe.getFuelUsage(), 2)).getFormattedText(), TextFormatting.RED.getColor());
        }
        for (int i = 0; i < recipe.getRequirements().size(); i++) {
            RecipeReqBase req = (RecipeReqBase) recipe.getRequirements().get(i);
            disps.put(req.getDrawText().getKey(), req.getDrawText().getValue());
        }
        inv.addArea(new JEIAreaReqs(ContainerArea.XAnchorDirection.RIGHT, ContainerArea.YAnchorDirection.BOTTOM, disps));
        inv.determineLayout();

        for (ContainerArea a : inv.getAreas()) {
            for (int s = 0; s < a.getSize(); s++) {
                a.getSlot(s).xPos += a.getX();
                a.getSlot(s).yPos += a.getY() - 16;
            }
        }


        return inv;
    }

    @Override
    public IDrawable getIcon() {

        for (Item i : VaryingMachina.itemMachines.values()) {
            if (i instanceof ItemBlockMachine && ((ItemBlockMachine) i).getBlockMachine().getMachineType().equals(type))
                return helper.createDrawableIngredient(new ItemStack(i));
        }
        return helper.createDrawableIngredient(ItemStack.EMPTY);
    }


    @Override
    public void setIngredients(RecipeBase recipeBase, IIngredients iIngredients) {
        iIngredients.setInputLists(VanillaTypes.ITEM, recipeBase.getAllInputs(ItemStack.class, type));
        iIngredients.setOutputs(VanillaTypes.ITEM, recipeBase.getAllOutputs(ItemStack.class, type));
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, RecipeBase recipeBase, IIngredients iIngredients) {


        InventoryManager inv = getInvFromRecipe(recipeBase);

        List<List<ItemStack>> inputs = iIngredients.getInputs(VanillaTypes.ITEM);

        List<Supplier<List<String>>> tooltips = new ArrayList<>();

        int slotsBefore = 0;
        int totalSlots = inputs.size();

        for (int slot = slotsBefore; slot < totalSlots; slot++) {
            iRecipeLayout.getItemStacks().init(slot, true, getMaxWidth() / 2 - inv.getMaxWidth() / 2 + inv.getSlot(slot).xPos, getMaxHeight() / 2 - inv.getMaxHeight() / 2 + inv.getSlot(slot).yPos);
            iRecipeLayout.getItemStacks().set(slot, inputs.get(slot - slotsBefore));
            tooltips.add(((SlotJEI) inv.getSlot(slot)).getTooltips());
        }

        List<List<ItemStack>> outputs = iIngredients.getOutputs(VanillaTypes.ITEM);

        slotsBefore = totalSlots + 0;
        totalSlots += outputs.size();

        for (int slot = slotsBefore; slot < totalSlots; slot++) {
            iRecipeLayout.getItemStacks().init(slot, false, getMaxWidth() / 2 - inv.getMaxWidth() / 2 + inv.getSlot(slot).xPos, getMaxHeight() / 2 - inv.getMaxHeight() / 2 + inv.getSlot(slot).yPos);
            iRecipeLayout.getItemStacks().set(slot, outputs.get(slot - slotsBefore));
            tooltips.add(((SlotJEI) inv.getSlot(slot)).getTooltips());
        }

        iRecipeLayout.getItemStacks().addTooltipCallback(new ITooltipCallback<ItemStack>() {
            @Override
            public void onTooltip(int slot, boolean input, ItemStack stack, List<String> list) {
                list.add(TextFormatting.WHITE + new TranslationTextComponent("varyingmachina.gui.count", stack.getCount()).getFormattedText());
                list.addAll(tooltips.get(slot).get());
            }
        });
/*
        slotsBefore = totalSlots + 0;
        totalSlots += getInputFluidLocations().size();

        List<List<FluidStack>> fluidInputs = ingredients.getInputs(FluidStack.class);
        for (int slot = slotsBefore; slot < totalSlots; slot++) {
            if (slot - slotsBefore >= fluidInputs.size())
                break;
            Vec2f pos = getInputFluidLocations().get(slot - slotsBefore);
            layout.getFluidStacks().init(slot, false, (int) pos.x, (int) pos.y, 8, 35, fluidInputs.get(slot - slotsBefore).get(0).amount, true, null);
            layout.getFluidStacks().set(slot, fluidInputs.get(slot - slotsBefore));
        }

        slotsBefore = totalSlots + 0;
        totalSlots += getOutputFluidLocations().size();

        List<List<FluidStack>> fluidOutputs = ingredients.getOutputs(FluidStack.class);
        for (int slot = slotsBefore; slot < totalSlots; slot++) {
            if (slot - slotsBefore >= fluidOutputs.size())
                break;
            Vec2f pos = getOutputFluidLocations().get(slot - slotsBefore);
            layout.getFluidStacks().init(slot, false, (int) pos.x, (int) pos.y, 8, 35, fluidOutputs.get(slot - slotsBefore).get(0).amount, true, null);
            layout.getFluidStacks().set(slot, fluidOutputs.get(slot - slotsBefore));
        }
        */
    }

    @Override
    public void draw(RecipeBase recipe, double mouseX, double mouseY) {
        InventoryManager inv = getInvFromRecipe(recipe);

        for (ContainerArea area : inv.getAreas()) {
            area.drawJEI(helper, getMaxWidth() / 2 - inv.getMaxWidth() / 2, getMaxHeight() / 2 - inv.getMaxHeight() / 2 - 16);
        }

        if (showSetRecipeButton(recipe)) {
            int state;
            state = 1;
            if (mouseX >= 0 && mouseX <= 10 && mouseY >= getMaxHeight() - 10 - 16 && mouseY <= getMaxHeight() - 16)
                state = 2;
            if (getTile() != null && !recipe.canProcessJEI(getTile()))
                state = 0;
            JEIHelper.createSetButton(0, getMaxHeight() - 10 - 16, helper, state).draw();
        }
    }

    private boolean showSetRecipeButton(RecipeBase recipe) {
        if (Minecraft.getInstance().currentScreen instanceof RecipesGui) {
            RecipesGui gui = (RecipesGui) Minecraft.getInstance().currentScreen;
            if (gui.getParentScreen() instanceof GuiMachine) {
                GuiMachine guiMachine = (GuiMachine) gui.getParentScreen();
                TileEntityMachine tile = guiMachine.getContainer().tile;
                if (tile.getMachineType().equals(type))
                    return true;
            }
        }
        return false;
    }

    private TileEntityMachine getTile() {
        RecipesGui gui = (RecipesGui) Minecraft.getInstance().currentScreen;
        if (gui.getParentScreen() instanceof GuiMachine) {
            GuiMachine guiMachine = (GuiMachine) gui.getParentScreen();
            TileEntityMachine tile = guiMachine.getContainer().tile;
            return tile;
        }
        return null;
    }

    @Override
    public boolean handleClick(RecipeBase recipe, double mouseX, double mouseY, int mouseButton) {

        if (mouseX >= 0 && mouseX <= 10 && mouseY >= getMaxHeight() - 10 - 16 && mouseY <= getMaxHeight() - 16 && mouseButton == 0 && showSetRecipeButton(recipe) && getTile() != null && getTile().getMachineType().equals(type) && recipe.canProcessJEI(getTile())) {
            ResourceLocation recipeName = ModMachines.types.get(getTile().getMachineType()).getNameOfRecipe(recipe);
            PacketHandler.INSTANCE.sendToServer(new PacketSetMachineRecipe(getTile().getPos(), recipeName));
            Minecraft.getInstance().currentScreen.onClose();
            return true;
        }
        return false;
    }
}
