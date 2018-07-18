package com.bartz24.varyingmachina.jei;

import com.bartz24.varyingmachina.References;
import com.bartz24.varyingmachina.base.inventory.ContainerCasing;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.inventory.Slot;

import java.util.ArrayList;
import java.util.List;

public class MachineTransferInfo implements IRecipeTransferInfo<ContainerCasing> {

    private String recipeCategory, machineID;

    private int slotRecipeStart, slotRecipeCount, slotInvStart, slotInvCount;

    public MachineTransferInfo(String recipeCategory, String machineID, int slotRecipeStart, int slotRecipeCount, int slotInvStart, int slotInvCount) {
        this.recipeCategory = recipeCategory;
        this.machineID = machineID;
        this.slotRecipeStart = slotRecipeStart;
        this.slotRecipeCount = slotRecipeCount;
        this.slotInvStart = slotInvStart;
        this.slotInvCount = slotInvCount;
    }

    public MachineTransferInfo(String id, int slotRecipeStart, int slotRecipeCount, int slotInvStart, int slotInvCount) {
        this.recipeCategory = References.ModID + ":" + id;
        this.machineID = id;
        this.slotRecipeStart = slotRecipeStart;
        this.slotRecipeCount = slotRecipeCount;
        this.slotInvStart = slotInvStart;
        this.slotInvCount = slotInvCount;
    }

    @Override
    public Class<ContainerCasing> getContainerClass() {
        return ContainerCasing.class;
    }

    @Override
    public String getRecipeCategoryUid() {
        return recipeCategory;
    }

    @Override
    public boolean canHandle(ContainerCasing container) {
        if (container.machineID.equals(machineID))
            return true;
        return false;
    }

    @Override
    public List<Slot> getRecipeSlots(ContainerCasing container) {
        List<Slot> slots = new ArrayList();
        for (int i = slotRecipeStart; i < slotRecipeStart + slotRecipeCount; i++)
            slots.add(container.getSlot(i));

        return slots;
    }

    @Override
    public List<Slot> getInventorySlots(ContainerCasing container) {
        List<Slot> slots = new ArrayList();
        for (int i = slotInvStart; i < slotInvStart + slotInvCount; i++)
            slots.add(container.getSlot(i));

        return slots;
    }
}
