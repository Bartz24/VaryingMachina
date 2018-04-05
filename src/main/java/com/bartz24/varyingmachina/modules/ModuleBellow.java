package com.bartz24.varyingmachina.modules;

import com.bartz24.varyingmachina.base.item.ItemModule;
import com.bartz24.varyingmachina.base.machine.MachineStat;
import com.bartz24.varyingmachina.base.machine.MachineVariant;

public class ModuleBellow extends ItemModule {

	public ModuleBellow() {
		super("bellow", MachineStat.PRESSURE, MachineStat.MAXHU, MachineStat.SPEED);
	}

	public float getStat(MachineVariant variant, MachineStat stat) {

		if (stat == MachineStat.PRESSURE)
			return variant.getStat(stat);
		else if (stat == MachineStat.MAXHU)
			return -.14f * variant.getStat(stat);
		else if (stat == MachineStat.SPEED)
			return .021f * variant.getStat(stat) * variant.getStat(stat) + .6f;

		return super.getStat(variant, stat);
	}
}
