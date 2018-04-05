package com.bartz24.varyingmachina.base.item;

import com.bartz24.varyingmachina.References;

public class ModuleItemBuilder<T extends ItemModule> {
	public T moduleItem;

	private Class<T> clazz;
	private String modParent;
	private String id;

	public ModuleItemBuilder(Class<T> clazz, String modAddingMachine, String id) {
		this.clazz = clazz;
		modParent = modAddingMachine;
		this.id = id;
	}

	public T buildItem() {
		T item = createObject();
		item.setRegistryName(item.getModAddingModule(), item.getModuleID());
		item.setUnlocalizedName(References.ModID + "." + item.getModuleID());
		moduleItem = item;
		return moduleItem;
	}

	private T createObject() {
		try {
			return clazz.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
