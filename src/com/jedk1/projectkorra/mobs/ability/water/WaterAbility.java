package com.jedk1.projectkorra.mobs.ability.water;

import com.jedk1.projectkorra.mobs.MobMethods;
import com.jedk1.projectkorra.mobs.ProjectKorraMobs;
import com.jedk1.projectkorra.mobs.object.SubElement;

import org.bukkit.entity.LivingEntity;

public class WaterAbility {

	private static boolean water = ProjectKorraMobs.plugin.getConfig().getBoolean("Abilities.Water.Enabled");
	private static boolean ice = ProjectKorraMobs.plugin.getConfig().getBoolean("Properties.SubElements.Water.Ice.Enabled");
	
	public static void execute(LivingEntity entity, LivingEntity target) {
		if (!water) return;
		if (!MobMethods.canBend(entity)) return;
		if (MobMethods.hasSubElement(entity)) {
			switch (MobMethods.getSubElement(entity)) {
				case Ice:
					if (ice && MobMethods.getRandomSourceBlock(entity.getLocation(), 3, null, SubElement.Ice) != null) {
						new IceBlast(entity, target.getLocation());
						return;
					}
				default:
					break;
			}
		}
		new WaterBlast(entity, target.getLocation());
	}
	
	public static void progress() {
		WaterBlast.progressAll();
		IceBlast.progressAll();
	}
	
	public static void remove() {
		WaterBlast.removeAll();
		IceBlast.removeAll();
	}
}
