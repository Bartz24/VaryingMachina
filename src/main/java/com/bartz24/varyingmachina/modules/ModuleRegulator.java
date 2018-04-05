package com.bartz24.varyingmachina.modules;

import com.bartz24.varyingmachina.base.item.ItemModule;
import com.bartz24.varyingmachina.base.machine.MachineStat;
import com.bartz24.varyingmachina.base.machine.MachineVariant;

public class ModuleRegulator extends ItemModule {

	public ModuleRegulator() {
		super("regulator", MachineStat.SPEED, MachineStat.EFFICIENCY, MachineStat.MAXHU);
	}

	public float getStat(MachineVariant variant, MachineStat stat) {
		if (stat != MachineStat.SPEED && stat != MachineStat.EFFICIENCY && stat != MachineStat.MAXHU)
			return super.getStat(variant, stat);

		float stat1 = variant.getStat(stat);
		float stat2 = variant.getStat(stat == MachineStat.SPEED ? MachineStat.EFFICIENCY : MachineStat.SPEED);
		float stat3 = variant.getStat(MachineStat.EFFICIENCY); // used for MAXHU
																// only
		if (stat == MachineStat.MAXHU)
			return (float) (stat1 / 3f * Math.log1p(Math.sqrt(Math.abs((stat2 * stat2 / stat3))))
					* Math.signum(stat2 - stat3));
		return stat1 * stat1 / stat2;
	}
}
