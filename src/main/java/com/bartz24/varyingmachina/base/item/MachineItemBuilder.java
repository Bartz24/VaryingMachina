package com.bartz24.varyingmachina.base.item;

import com.bartz24.varyingmachina.References;

public class MachineItemBuilder<T extends ItemMachine> {
	public T machineItem;

	private Class<T> clazz;
	private String modParent;
	private String id;

	public MachineItemBuilder(Class<T> clazz, String modAddingMachine, String id) {
		this.clazz = clazz;
		modParent = modAddingMachine;
		this.id = id;
	}

	public T buildItem() {
		T item = createObject();
		item.setRegistryName(item.getModAddingMachine(), item.getMachineID());
		item.setUnlocalizedName(References.ModID + "." + item.getMachineID());
		machineItem = item;
		return machineItem;
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
