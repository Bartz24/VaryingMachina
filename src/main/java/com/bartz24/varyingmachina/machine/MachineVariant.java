package com.bartz24.varyingmachina.machine;

import com.bartz24.varyingmachina.inventory.FuelUnit;
import com.bartz24.varyingmachina.recipe.InputBase;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class MachineVariant {
    private HashMap<MachineStat, Integer> values = new HashMap<>();
    private String name;
    private ResourceLocation texture;
    private Function<Integer, FuelUnit> fuelUnitSupplier;
    private int fuelUnitSize;
    private InputBase ingotItem, plateItem, gearItem;

    public MachineVariant(String name, ResourceLocation texture) {
        this.name = name;
        this.texture = texture;
        ModVariants.types.put(name, this);
    }

    public MachineVariant setStat(MachineStat stat, int val) {
        if (!values.containsKey(stat))
            values.put(stat, val);
        return this;
    }

    public int getStat(MachineStat stat) {
        if (values.containsKey(stat))
            return values.get(stat);
        return 1;
    }

    public String getName() {
        return name;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public MachineVariant setFuelUnitSupplier(Function<Integer, FuelUnit> fuelUnitSupplier) {
        this.fuelUnitSupplier = fuelUnitSupplier;
        return this;
    }

    public Function<Integer, FuelUnit> getFuelUnitSupplier() {
        return fuelUnitSupplier;
    }

    public int getFuelUnitSize() {
        return fuelUnitSize;
    }

    public MachineVariant setFuelUnitSize(int fuelUnitSize) {
        this.fuelUnitSize = fuelUnitSize;
        return this;
    }

    public InputBase getIngotItem() {
        return ingotItem.copy();
    }

    public InputBase getGearItem() {
        return gearItem.copy();
    }

    public InputBase getPlateItem() {
        return plateItem.copy();
    }

    public MachineVariant setRecipeItems(InputBase ingotItem, InputBase plateItem, InputBase gearItem) {
        this.ingotItem = ingotItem;
        this.gearItem = gearItem;
        this.plateItem = plateItem;
        return this;
    }

    public IItemTier getItemTier() {
        return new IItemTier() {
            @Override
            public int getMaxUses() {
                return (int) (MachineVariant.this.getStat(ModMachines.Stats.pressure) * 0.8);
            }

            @Override
            public float getEfficiency() {
                return (float) (MachineVariant.this.getStat(ModMachines.Stats.speed) * 3d / 100d * 1.6d);
            }

            @Override
            public float getAttackDamage() {
                return (float) (Math.sqrt((double) MachineVariant.this.getStat(ModMachines.Stats.pressure)) / 10d);
            }

            @Override
            public int getHarvestLevel() {
                return (int) Math.round((MachineVariant.this.getStat(ModMachines.Stats.efficiency) / 100d + MachineVariant.this.getStat(ModMachines.Stats.pressure) / 300d) / 1.5d);
            }

            @Override
            public int getEnchantability() {
                return (int) ((MachineVariant.this.getStat(ModMachines.Stats.speed) / 100d + MachineVariant.this.getStat(ModMachines.Stats.efficiency) / 100d + MachineVariant.this.getStat(ModMachines.Stats.pressure) / 300d) / 3d * 5d);
            }

            @Override
            public Ingredient getRepairMaterial() {
                List inputs = getIngotItem().getInputs();
                if (inputs.size() > 0 && inputs.get(0).getClass() == ItemStack.class) {
                    ItemStack[] stacks = new ItemStack[inputs.size()];
                    for (int i = 0; i < inputs.size(); i++)
                        stacks[i] = (ItemStack) inputs.get(i);
                    return Ingredient.fromStacks(stacks);
                }
                return Ingredient.EMPTY;
            }
        };
    }
}
