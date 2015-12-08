package com.jedk1.projectkorra.mobs.manager;

import com.jedk1.projectkorra.mobs.ability.air.AirAbility;
import com.jedk1.projectkorra.mobs.ability.earth.EarthAbility;
import com.jedk1.projectkorra.mobs.ability.fire.FireAbility;
import com.jedk1.projectkorra.mobs.ability.water.WaterAbility;

public class AbilityManager implements Runnable {

	public void run() {
		EntityManager.progress();
		AirAbility.progress();
		EarthAbility.progress();
		FireAbility.progress();
		WaterAbility.progress();
	}
}