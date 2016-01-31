package com.jedk1.projectkorra.mobs.ability;

import com.jedk1.projectkorra.mobs.MobMethods;
import com.jedk1.projectkorra.mobs.ability.air.AirAbility;
import com.jedk1.projectkorra.mobs.ability.earth.EarthAbility;
import com.jedk1.projectkorra.mobs.ability.fire.FireAbility;
import com.jedk1.projectkorra.mobs.ability.water.WaterAbility;

import org.bukkit.entity.LivingEntity;

public class AvatarAbility {
	
	public static void execute(LivingEntity entity, LivingEntity target) {
		switch (MobMethods.rand.nextInt(4)) {
			case 0:
				AirAbility.execute(entity, target);
				break;
			case 1:
				EarthAbility.execute(entity, target);
				break;
			case 2:
				FireAbility.execute(entity, target);
				break;
			case 3:
				WaterAbility.execute(entity, target);
				break;
		}
	}
}
