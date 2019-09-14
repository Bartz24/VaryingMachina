package com.bartz24.varyingmachina.container;

import com.bartz24.varyingmachina.VaryingMachina;
import com.bartz24.varyingmachina.inventory.*;
import com.bartz24.varyingmachina.tile.TileEntityMachine;
import com.google.common.collect.Sets;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import java.util.List;
import java.util.Set;

public class ContainerMachine extends Container {

    private int dragEvent;
    private int dragMode = -1;
    private final Set<Slot> dragSlots = Sets.newHashSet();
    public TileEntityMachine tile;

    protected InventoryManager inv = new InventoryManager(16);

    public ContainerMachine(int id, PlayerInventory playerInventory, PacketBuffer extraData) {
        this(id, playerInventory, (TileEntityMachine) playerInventory.player.world.getTileEntity(extraData.readBlockPos()));
    }

    public ContainerMachine(int id, PlayerInventory playerInventory, TileEntityMachine te) {
        super(VaryingMachina.machineContainer, id);
        tile = te;
        inv.addArea(new AreaPlayerInventory(playerInventory.player));
        if (tile.getMachine().isNoRecipes() || tile.getRecipe() != null) {
            for (String s : tile.getItemHandler().getHandlerIds()) {
                if (s.contains("filter") && tile.getItemHandler().getHandler(s) instanceof FilterItemHandler) {
                    inv.addArea(new AreaFilterHandler((FilterItemHandler) tile.getItemHandler().getHandler(s), ContainerArea.XAnchorDirection.CENTER, ContainerArea.YAnchorDirection.TOP, 50));
                    continue;
                }
                if (!(tile.getItemHandler().getHandler(s) instanceof ItemHandlerFiltered))
                    continue;
                ItemHandlerFiltered handler = (ItemHandlerFiltered) tile.getItemHandler().getHandler(s);
                if (s.contains("input"))
                    inv.addArea(new AreaItemHandler(handler, ContainerArea.XAnchorDirection.CENTER, ContainerArea.YAnchorDirection.TOP, 0));
                else if (s.contains("output"))
                    inv.addArea(new AreaItemHandler(handler, ContainerArea.XAnchorDirection.CENTER, ContainerArea.YAnchorDirection.TOP, 100));
                else if (s.contains("inv"))
                    inv.addArea(new AreaItemHandler(handler, ContainerArea.XAnchorDirection.CENTER, ContainerArea.YAnchorDirection.TOP, 50));
            }
            for (String s : tile.getEnergy().getHandlerIds()) {
                BetterEnergyStorage handler = tile.getEnergy().getHandler(s);
                if (s.contains("input"))
                    inv.addArea(new AreaEnergyHandler(handler, s, ContainerArea.XAnchorDirection.CENTER, ContainerArea.YAnchorDirection.TOP, 0));
                else if (s.contains("output"))
                    inv.addArea(new AreaEnergyHandler(handler, s, ContainerArea.XAnchorDirection.CENTER, ContainerArea.YAnchorDirection.TOP, 100));
                else if (s.contains("energy"))
                    inv.addArea(new AreaEnergyHandler(handler, s, ContainerArea.XAnchorDirection.CENTER, ContainerArea.YAnchorDirection.TOP, 50));
            }
        }
        if (!tile.getMachine().isNoRecipes() && tile.getRecipe() != null)
            inv.addArea(new AreaArrow(ContainerArea.XAnchorDirection.CENTER, ContainerArea.YAnchorDirection.TOP));
        if (tile.getFuelUnit() != null)
            inv.addArea(tile.getFuelUnit().getArea());
        if (inv.getAreas().size() == 1)
            inv.addArea(new AreaSpacer(ContainerArea.XAnchorDirection.CENTER, ContainerArea.YAnchorDirection.TOP));
        for (ContainerArea a : inv.getAreas()) {
            for (int s = 0; s < a.getSize(); s++) {
                this.addSlot(a.getSlot(s));
            }
        }
        inv.determineLayout();

        for (ContainerArea a : inv.getAreas()) {
            for (int s = 0; s < a.getSize(); s++) {
                a.getSlot(s).xPos += a.getX();
                a.getSlot(s).yPos += a.getY();
            }
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return tile != null && playerIn.getDistanceSq(tile.getPos().getX() + 0.5, tile.getPos().getY() + 0.5, tile.getPos().getZ() + 0.5) <= 64;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int fromSlot) {
        ItemStack previous = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(fromSlot);

        if (slot != null && slot.getHasStack()) {
            ItemStack current = slot.getStack();
            previous = current.copy();

            if (fromSlot < 36) {
                if (!this.mergeItemStack(current, 36, inventorySlots.size(), false))
                    return ItemStack.EMPTY;
            } else {
                if (!this.mergeItemStack(current, 0, 36, false))
                    return ItemStack.EMPTY;
            }

            if (current.getCount() == 0)
                slot.putStack(ItemStack.EMPTY);
            else {
                slot.onSlotChanged();
            }

            if (current.getCount() == previous.getCount())
                return ItemStack.EMPTY;
            slot.onTake(playerIn, current);
        }
        return previous;
    }

    @Override
    protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean flag = false;
        int i = startIndex;
        if (reverseDirection) {
            i = endIndex - 1;
        }

        if (stack.isStackable()) {
            while (!stack.isEmpty()) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }

                Slot slot = this.inventorySlots.get(i);
                ItemStack itemstack = slot.getStack();
                if (!itemstack.isEmpty() && areItemsAndTagsEqual(stack, itemstack)) {
                    int j = itemstack.getCount() + stack.getCount();
                    int maxSize = slot.getSlotStackLimit();
                    if (j <= maxSize) {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        slot.putStack(itemstack);
                        flag = true;
                    } else if (itemstack.getCount() < maxSize) {
                        stack.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        slot.putStack(itemstack);
                        flag = true;
                    }
                }
                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        if (!stack.isEmpty()) {
            if (reverseDirection) {
                i = endIndex - 1;
            } else {
                i = startIndex;
            }

            while (true) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }

                Slot slot1 = this.inventorySlots.get(i);
                ItemStack itemstack1 = slot1.getStack();
                if (itemstack1.isEmpty() && slot1.isItemValid(stack)) {
                    if (stack.getCount() > slot1.getSlotStackLimit()) {
                        slot1.putStack(stack.split(slot1.getSlotStackLimit()));
                    } else {
                        slot1.putStack(stack.split(stack.getCount()));
                    }

                    slot1.onSlotChanged();
                    flag = true;
                    break;
                }

                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        return flag;
    }

    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
        ItemStack itemstack = ItemStack.EMPTY;
        PlayerInventory inventoryplayer = player.inventory;
        if (clickTypeIn == ClickType.QUICK_CRAFT) {
            int j1 = dragEvent;
            dragEvent = getDragEvent(dragType);
            if ((j1 != 1 || dragEvent != 2) && j1 != dragEvent) {
                this.resetDrag();
            } else if (inventoryplayer.getItemStack().isEmpty()) {
                this.resetDrag();
            } else if (dragEvent == 0) {
                dragMode = extractDragMode(dragType);
                if (isValidDragMode(dragMode, player)) {
                    dragEvent = 1;
                    dragSlots.clear();
                } else {
                    this.resetDrag();
                }
            } else if (dragEvent == 1) {
                Slot slot7 = this.inventorySlots.get(slotId);
                ItemStack itemstack12 = inventoryplayer.getItemStack();
                if (slot7 != null && canAddItemToSlot(slot7, itemstack12, true) && slot7.isItemValid(itemstack12) && (dragMode == 2 || itemstack12.getCount() > dragSlots.size()) && this.canDragIntoSlot(slot7)) {
                    dragSlots.add(slot7);
                }
            } else if (dragEvent == 2) {
                if (!dragSlots.isEmpty()) {
                    ItemStack itemstack9 = inventoryplayer.getItemStack().copy();
                    int k1 = inventoryplayer.getItemStack().getCount();

                    for (Slot slot8 : dragSlots) {
                        ItemStack itemstack13 = inventoryplayer.getItemStack();
                        if (slot8 != null && canAddItemToSlot(slot8, itemstack13, true) && slot8.isItemValid(itemstack13) && (dragMode == 2 || itemstack13.getCount() >= dragSlots.size()) && this.canDragIntoSlot(slot8)) {
                            ItemStack itemstack14 = itemstack9.copy();
                            int j3 = slot8.getHasStack() ? slot8.getStack().getCount() : 0;
                            computeStackSize(dragSlots, dragMode, itemstack14, j3);
                            int k3 = Math.min(itemstack14.getMaxStackSize(), slot8.getItemStackLimit(itemstack14));
                            if (itemstack14.getCount() > k3) {
                                itemstack14.setCount(k3);
                            }

                            k1 -= itemstack14.getCount() - j3;
                            slot8.putStack(itemstack14);
                        }
                    }

                    itemstack9.setCount(k1);
                    inventoryplayer.setItemStack(itemstack9);
                }

                this.resetDrag();
            } else {
                this.resetDrag();
            }
        } else if (dragEvent != 0) {
            this.resetDrag();
        } else if ((clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1)) {
            if (slotId == -999) {
                if (!inventoryplayer.getItemStack().isEmpty()) {
                    if (dragType == 0) {
                        player.dropItem(inventoryplayer.getItemStack(), true);
                        inventoryplayer.setItemStack(ItemStack.EMPTY);
                    }

                    if (dragType == 1) {
                        player.dropItem(inventoryplayer.getItemStack().split(1), true);
                    }
                }
            } else if (clickTypeIn == ClickType.QUICK_MOVE) {
                if (slotId < 0) {
                    return ItemStack.EMPTY;
                }

                Slot slot5 = this.inventorySlots.get(slotId);
                if (slot5 == null || !slot5.canTakeStack(player)) {
                    return ItemStack.EMPTY;
                }

                for (ItemStack itemstack7 = this.transferStackInSlot(player, slotId); !itemstack7.isEmpty() && ItemStack.areItemsEqual(slot5.getStack(), itemstack7); itemstack7 = this.transferStackInSlot(player, slotId)) {
                    itemstack = itemstack7.copy();
                }
            } else {
                if (slotId < 0) {
                    return ItemStack.EMPTY;
                }

                Slot slot6 = this.inventorySlots.get(slotId);
                if (slot6 != null) {
                    ItemStack itemstack8 = slot6.getStack();
                    ItemStack itemstack11 = inventoryplayer.getItemStack();
                    if (!itemstack8.isEmpty()) {
                        itemstack = itemstack8.copy();
                    }

                    if (itemstack8.isEmpty()) {
                        if (!itemstack11.isEmpty() && slot6.isItemValid(itemstack11)) {
                            int j2 = dragType == 0 ? itemstack11.getCount() : 1;
                            if (j2 > slot6.getItemStackLimit(itemstack11)) {
                                j2 = slot6.getItemStackLimit(itemstack11);
                            }

                            slot6.putStack(itemstack11.split(j2));
                        }
                    } else if (slot6.canTakeStack(player)) {
                        if (itemstack11.isEmpty()) {
                            if (itemstack8.isEmpty()) {
                                slot6.putStack(ItemStack.EMPTY);
                                inventoryplayer.setItemStack(ItemStack.EMPTY);
                            } else {
                                int k2 = dragType == 0 ? Math.min(itemstack8.getCount(), itemstack8.getMaxStackSize()) : Math.min(itemstack8.getCount() + 1, itemstack8.getMaxStackSize() + 1) / 2;
                                inventoryplayer.setItemStack(slot6.decrStackSize(k2));
                                if (itemstack8.isEmpty()) {
                                    slot6.putStack(ItemStack.EMPTY);
                                }

                                slot6.onTake(player, inventoryplayer.getItemStack());
                            }
                        } else if (slot6.isItemValid(itemstack11)) {
                            if (areItemsAndTagsEqual(itemstack8, itemstack11)) {
                                int l2 = dragType == 0 ? itemstack11.getCount() : 1;
                                if (l2 > slot6.getItemStackLimit(itemstack11) - itemstack8.getCount()) {
                                    l2 = slot6.getItemStackLimit(itemstack11) - itemstack8.getCount();
                                }

                                if (l2 > itemstack11.getMaxStackSize() - itemstack8.getCount()) {
                                    l2 = itemstack11.getMaxStackSize() - itemstack8.getCount();
                                }

                                itemstack11.shrink(l2);
                                itemstack8.grow(l2);
                            } else if (itemstack11.getCount() <= slot6.getItemStackLimit(itemstack11)) {
                                slot6.putStack(itemstack11);
                                inventoryplayer.setItemStack(itemstack8);
                            }
                        } else if (itemstack11.getMaxStackSize() > 1 && areItemsAndTagsEqual(itemstack8, itemstack11) && !itemstack8.isEmpty()) {
                            int i3 = itemstack8.getCount();
                            if (i3 + itemstack11.getCount() <= itemstack11.getMaxStackSize()) {
                                itemstack11.grow(i3);
                                itemstack8 = slot6.decrStackSize(i3);
                                if (itemstack8.isEmpty()) {
                                    slot6.putStack(ItemStack.EMPTY);
                                }

                                slot6.onTake(player, inventoryplayer.getItemStack());
                            }
                        }
                    }

                    slot6.onSlotChanged();
                }
            }
        } else if (clickTypeIn == ClickType.SWAP && dragType >= 0 && dragType < 9) {
            Slot slot4 = this.inventorySlots.get(slotId);
            ItemStack itemstack6 = inventoryplayer.getStackInSlot(dragType);
            ItemStack itemstack10 = slot4.getStack();
            if (!itemstack6.isEmpty() || !itemstack10.isEmpty()) {
                if (itemstack6.isEmpty()) {
                    if (slot4.canTakeStack(player)) {
                        inventoryplayer.setInventorySlotContents(dragType, itemstack10);
                        //slot4.onSwapCraft(itemstack10.getCount());
                        slot4.putStack(ItemStack.EMPTY);
                        slot4.onTake(player, itemstack10);
                    }
                } else if (itemstack10.isEmpty()) {
                    if (slot4.isItemValid(itemstack6)) {
                        int l1 = slot4.getItemStackLimit(itemstack6);
                        if (itemstack6.getCount() > l1) {
                            slot4.putStack(itemstack6.split(l1));
                        } else {
                            slot4.putStack(itemstack6);
                            inventoryplayer.setInventorySlotContents(dragType, ItemStack.EMPTY);
                        }
                    }
                } else if (slot4.canTakeStack(player) && slot4.isItemValid(itemstack6)) {
                    int i2 = slot4.getItemStackLimit(itemstack6);
                    if (itemstack6.getCount() > i2) {
                        slot4.putStack(itemstack6.split(i2));
                        slot4.onTake(player, itemstack10);
                        if (!inventoryplayer.addItemStackToInventory(itemstack10)) {
                            player.dropItem(itemstack10, true);
                        }
                    } else {
                        slot4.putStack(itemstack6);
                        inventoryplayer.setInventorySlotContents(dragType, itemstack10);
                        slot4.onTake(player, itemstack10);
                    }
                }
            }
        } else if (clickTypeIn == ClickType.CLONE && player.abilities.isCreativeMode && inventoryplayer.getItemStack().isEmpty() && slotId >= 0) {
            Slot slot3 = this.inventorySlots.get(slotId);
            if (slot3 != null && slot3.getHasStack()) {
                ItemStack itemstack5 = slot3.getStack().copy();
                itemstack5.setCount(itemstack5.getMaxStackSize());
                inventoryplayer.setItemStack(itemstack5);
            }
        } else if (clickTypeIn == ClickType.THROW && inventoryplayer.getItemStack().isEmpty() && slotId >= 0) {
            Slot slot2 = this.inventorySlots.get(slotId);
            if (slot2 != null && slot2.getHasStack() && slot2.canTakeStack(player)) {
                ItemStack itemstack4 = slot2.decrStackSize(dragType == 0 ? 1 : slot2.getStack().getCount());
                slot2.onTake(player, itemstack4);
                player.dropItem(itemstack4, true);
            }
        } else if (clickTypeIn == ClickType.PICKUP_ALL && slotId >= 0) {
            Slot slot = this.inventorySlots.get(slotId);
            ItemStack itemstack1 = inventoryplayer.getItemStack();
            if (!itemstack1.isEmpty() && (slot == null || !slot.getHasStack() || !slot.canTakeStack(player))) {
                int i = dragType == 0 ? 0 : this.inventorySlots.size() - 1;
                int j = dragType == 0 ? 1 : -1;

                for (int k = 0; k < 2; ++k) {
                    for (int l = i; l >= 0 && l < this.inventorySlots.size() && itemstack1.getCount() < itemstack1.getMaxStackSize(); l += j) {
                        Slot slot1 = this.inventorySlots.get(l);
                        if (slot1.getHasStack() && canAddItemToSlot(slot1, itemstack1, true) && slot1.canTakeStack(player) && this.canMergeSlot(itemstack1, slot1)) {
                            ItemStack itemstack2 = slot1.getStack();
                            if (k != 0 || itemstack2.getCount() != itemstack2.getMaxStackSize()) {
                                int i1 = Math.min(itemstack1.getMaxStackSize() - itemstack1.getCount(), itemstack2.getCount());
                                ItemStack itemstack3 = slot1.decrStackSize(i1);
                                itemstack1.grow(i1);
                                if (itemstack3.isEmpty()) {
                                    slot1.putStack(ItemStack.EMPTY);
                                }

                                slot1.onTake(player, itemstack3);
                            }
                        }
                    }
                }
            }

            this.detectAndSendChanges();
        }

        return itemstack;
    }

    @Override
    protected void resetDrag() {
        this.dragEvent = 0;
        this.dragSlots.clear();
    }

    public List<ContainerArea> getAreas() {
        return inv.getAreas();
    }

    public int getMaxHeight() {
        return inv.getMaxHeight();
    }

    public int getMaxWidth() {
        return inv.getMaxWidth();
    }

}
