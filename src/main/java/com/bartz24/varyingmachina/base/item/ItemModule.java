package com.bartz24.varyingmachina.base.item;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.bartz24.varyingmachina.References;
import com.bartz24.varyingmachina.base.inventory.GuiCasing;
import com.bartz24.varyingmachina.base.inventory.GuiModules;
import com.bartz24.varyingmachina.base.machine.MachineStat;
import com.bartz24.varyingmachina.base.machine.MachineVariant;
import com.bartz24.varyingmachina.base.models.MachineModelLoader;
import com.bartz24.varyingmachina.base.tile.TileCasing;
import com.bartz24.varyingmachina.registry.MachineRegistry;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemModule extends ItemBase {

	private String moduleID;

	public MachineStat[] stats;

	public ItemModule(String machineID, MachineStat... machineStats) {
		this.moduleID = machineID;
		stats = machineStats;
		setHasSubtypes(true);
	}

	public String getModAddingModule() {
		return References.ModID;
	}

	public String getModuleID() {
		return moduleID;
	}

	public String getItemStackDisplayName(ItemStack stack) {
		MachineVariant variant = MachineVariant.readFromNBT(stack.getTagCompound());
		if (variant == null)
			return super.getItemStackDisplayName(stack);
		return String.format(super.getItemStackDisplayName(stack),
				I18n.translateToLocal(variant.getRegistryName().toString().replace(":", ".")));
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ResourceLocation baseLocation = new ResourceLocation(getModAddingModule(), "item/" + getModuleID());
		MachineModelLoader loader = new MachineModelLoader(baseLocation, "all");
		Map<MachineVariant, ModelResourceLocation> locations = new HashMap();
		for (int i = 0; i < MachineRegistry.getAllVariantsRegistered().length; i++) {
			ModelResourceLocation location = new ModelResourceLocation(
					getRegistryName().toString()
							+ MachineRegistry.getAllVariantsRegistered()[i].getRegistryName().getResourcePath(),
					"inventory");
			locations.put(MachineRegistry.getAllVariantsRegistered()[i], location);
			loader.addVariant(location, MachineRegistry.getAllVariantsRegistered()[i].getTexturePath());
		}
		ModelLoader.registerItemVariants(this, locations.values().toArray(new ModelResourceLocation[locations.size()]));
		ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				MachineVariant variant = MachineVariant.readFromNBT(stack.getTagCompound());
				if (variant != null)
					return locations.get(variant);
				return locations.get(0);
			}
		});
		ModelLoaderRegistry.registerLoader(loader);

	}

	public float getStat(MachineVariant variant, MachineStat stat) {
		switch (stat.displayType) {
		case ACTUALFLOAT:
			return 1;
		case INT:
			return 0;
		case PERCENT:
			return 1;
		}
		return 1;
	}

	public float manipulateStat(MachineVariant moduleVariant, MachineStat stat, float value) {
		switch (stat.displayType) {
		case ACTUALFLOAT:
			return value * getStat(moduleVariant, stat);
		case INT:
			return value + getStat(moduleVariant, stat);
		case PERCENT:
			return value * getStat(moduleVariant, stat);
		}
		return value;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs creativeTab, NonNullList<ItemStack> list) {
		if (isInCreativeTab(creativeTab))
			for (MachineVariant variant : MachineVariant.REGISTRY.getValuesCollection()) {
				ItemStack stack = new ItemStack(this);
				MachineVariant.writeVariantToStack(stack, variant);
				list.add(stack);
			}
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		for (MachineStat machineStat : stats)
			addSingleInfo(machineStat, stack, tooltip);
	}

	public void addSingleInfo(MachineStat stat, ItemStack stack, List<String> tooltip) {
		MachineVariant variant = MachineVariant.readFromNBT(stack.getTagCompound());
		if (variant != null) {
			switch (stat) {
			case SPEED:
				tooltip.add(TextFormatting.BLUE + "Speed: " + stat.getDisplayStat(getStat(variant, stat)));
				break;
			case SIZE:
				tooltip.add(TextFormatting.DARK_BLUE + "Size: " + stat.getDisplayStat(getStat(variant, stat)));
				break;
			case EFFICIENCY:
				tooltip.add(TextFormatting.GREEN + "Efficiency: " + stat.getDisplayStat(getStat(variant, stat)));
				break;
			case PRODUCTION:
				tooltip.add(TextFormatting.DARK_GREEN + "Production: " + stat.getDisplayStat(getStat(variant, stat)));
				break;
			case MAXHU:
				tooltip.add(TextFormatting.RED + "Max HU: " + stat.getDisplayStat(getStat(variant, stat)));
				break;
			case PRESSURE:
				tooltip.add(
						TextFormatting.YELLOW + "Pressure: " + stat.getDisplayStat(getStat(variant, stat)) + " kPa");
				break;
			}
		}
	}

	public boolean hasCapability(TileCasing casing, Capability<?> capability) {
		return false;
	}

	public <T> T getCapability(TileCasing casing, Capability<T> capability) {
		return null;
	}

	public void update(TileCasing casing, EnumFacing installedSide) {

	}

	public void onAddToCasing(TileCasing casing, EnumFacing installedSide) {

	}

	public void onRemoveFromCasing(TileCasing casing, EnumFacing installedSide) {

	}

	@SideOnly(Side.CLIENT)
	public void initGui(GuiModules gui, List buttonList, TileCasing casing) {
	}

	@SideOnly(Side.CLIENT)
	public void updateGuiComps(GuiModules gui, List buttonList, TileCasing casing) {
	}

	@SideOnly(Side.CLIENT)
	public void actionPerformed(TileCasing tile, GuiModules gui, int buttonClicked) throws IOException {
	}

	public void drawBackgroundGui(TileCasing tile, GuiModules gui, FontRenderer fontRenderer, int mouseX, int mouseY) {
	}

	public void drawForegroundGui(TileCasing tile, GuiModules gui, FontRenderer fontRenderer, int mouseX, int mouseY) {
	}
}
