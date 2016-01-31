package com.jedk1.projectkorra.mobs.ability.fire;

import com.jedk1.projectkorra.mobs.MobMethods;
import com.jedk1.projectkorra.mobs.ProjectKorraMobs;

import org.bukkit.entity.LivingEntity;

public class FireAbility {

	private static boolean fire = ProjectKorraMobs.plugin.getConfig().getBoolean("Abilities.Fire.Enabled");
	private static boolean lightning = ProjectKorraMobs.plugin.getConfig().getBoolean("Properties.SubElements.Fire.Lightning.Enabled");
	private static boolean combustion = ProjectKorraMobs.plugin.getConfig().getBoolean("Properties.SubElements.Fire.Combustion.Enabled");
	private static int frequency = ProjectKorraMobs.plugin.getConfig().getInt("Properties.SubElements.Fire.AbilityFrequency");

	public static void execute(LivingEntity entity, LivingEntity target) {
		if (!fire) return;
		if (!MobMethods.canBend(entity)) return;
		if (entity.getWorld() != target.getWorld()) return;
		if (entity.getLocation().distance(target.getLocation()) > 20) {
			new FireJet(entity, target.getLocation());
			return;
		}
		if (MobMethods.rand.nextInt(frequency) == 0 && MobMethods.hasSubElement(entity)) {
			switch (MobMethods.getSubElement(entity)) {
				case Lightning:
					if (lightning) {
						new Lightning(entity, target.getLocation());
						return;
					}
				case Combustion:
					if (combustion) {
						new Combustion(entity, target);
						return;
					}
				default:
					break;
			}
		} else {
			switch (MobMethods.rand.nextInt(2)) {
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
		Combustion.progressAll();
		FireBlast.progressAll();
		FireJet.progressAll();
		Lightning.progressAll();
	}

	public static void remove() {
		Combustion.removeAll();
		FireBlast.removeAll();
		FireJet.removeAll();
		Lightning.removeAll();
	}
}
