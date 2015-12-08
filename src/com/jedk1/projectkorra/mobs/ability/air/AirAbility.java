package com.jedk1.projectkorra.mobs.ability.air;

import com.jedk1.projectkorra.mobs.MobMethods;
import com.jedk1.projectkorra.mobs.ProjectKorraMobs;
import com.projectkorra.projectkorra.GeneralMethods;

import org.bukkit.entity.LivingEntity;

public class AirAbility {

	private static boolean air = ProjectKorraMobs.plugin.getConfig().getBoolean("Abilities.Air.Enabled");
	
	public static void execute(LivingEntity entity, LivingEntity target) {
		if (!air) return;
		if (!MobMethods.canBend(entity)) return;
		if (entity.getLocation().distance(target.getLocation()) > 20) {
			new AirScooter(entity, target.getLocation());
			return;
		}
		switch (GeneralMethods.rand.nextInt(2)) {
			case 0:
				new AirBlast(entity, target.getLocation());
				break;
			case 1:
				new AirScooter(entity, target.getLocation());
				break;
		}
	}
	
	public static void progress() {
		AirBlast.progressAll();
		AirScooter.progressAll();
	}
	
	public static void remove() {
		AirBlast.removeAll();
		AirScooter.removeAll();
	}
}
