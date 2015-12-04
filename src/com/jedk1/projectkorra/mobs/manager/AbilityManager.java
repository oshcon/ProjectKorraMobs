package com.jedk1.projectkorra.mobs.manager;

import com.jedk1.projectkorra.mobs.ability.AirBlast;
import com.jedk1.projectkorra.mobs.ability.EarthBlast;
import com.jedk1.projectkorra.mobs.ability.FireBlast;
import com.jedk1.projectkorra.mobs.ability.WaterBlast;

public class AbilityManager implements Runnable {

	public void run() {
		EntityManager.progress();
		FireBlast.progressAll();
		AirBlast.progressAll();
		WaterBlast.progressAll();
		EarthBlast.progressAll();
	}
}