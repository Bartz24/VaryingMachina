package com.bartz24.varyingmachina.modules;

import com.bartz24.varyingmachina.base.item.ItemModule;
import com.bartz24.varyingmachina.base.machine.MachineStat;
import com.bartz24.varyingmachina.base.machine.MachineVariant;

public class ModuleGearbox extends ItemModule {

	public ModuleGearbox() {
		super("gearbox", MachineStat.PRODUCTION, MachineStat.SPEED, MachineStat.MAXHU, MachineStat.PRESSURE);
	}

	public float getStat(MachineVariant variant, MachineStat stat) {
		if (stat == MachineStat.MAXHU || stat == MachineStat.PRESSURE)
			return -variant.getStat(stat) / getStat(variant, MachineStat.PRODUCTION);
		else if (stat == MachineStat.SPEED)
			return .5f * variant.getStat(MachineStat.SPEED) / variant.getStat(MachineStat.PRODUCTION);
		else if (stat == MachineStat.PRODUCTION)
			return (float) (Math.pow(2.7f * variant.getStat(MachineStat.PRODUCTION), 1.2f)
					/ variant.getStat(MachineStat.SPEED));

		return super.getStat(variant, stat);
	}
}
