package com.jedk1.projectkorra.mobs.ability.fire;

import com.jedk1.projectkorra.mobs.MobMethods;
import com.jedk1.projectkorra.mobs.ProjectKorraMobs;
import com.projectkorra.projectkorra.GeneralMethods;

import org.bukkit.entity.LivingEntity;

public class FireAbility {

	private static boolean fire = ProjectKorraMobs.plugin.getConfig().getBoolean("Abilities.Fire.Enabled");
	private static boolean lightning = ProjectKorraMobs.plugin.getConfig().getBoolean("Properties.SubElements.Fire.Lightning.Enabled");
	private static boolean combustion = ProjectKorraMobs.plugin.getConfig().getBoolean("Properties.SubElements.Fire.Combustion.Enabled");
	
	public static void execute(LivingEntity entity, LivingEntity target) {
		if (!fire) return;
		if (!MobMethods.canBend(entity)) return;
		int i = GeneralMethods.rand.nextInt(2);
		if (MobMethods.hasSubElement(entity) && i == 0) {
			switch (MobMethods.getSubElement(entity)) {
				case Lightning:
					if (lightning) {
						return;
					}
				case Combustion:
					if (combustion) {
						return;
					}
				default:
					break;
			}
		} else {
			if (entity.getLocation().distance(target.getLocation()) > 20) {
				new FireJet(entity, target.getLocation());
				return;
			}
			switch (GeneralMethods.rand.nextInt(2)) {
				case 0:
					new FireBlast(entity, target.getLocation());
					break;
				case 1:
					new FireJet(entity, target.getLocation());
					break;
			}
		}
	}
	
	public static void progress() {
		FireBlast.progressAll();
		FireJet.progressAll();
	}
	
	public static void remove() {
		FireBlast.removeAll();
		FireJet.removeAll();
	}
}
