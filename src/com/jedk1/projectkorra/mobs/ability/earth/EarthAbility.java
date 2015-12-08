package com.jedk1.projectkorra.mobs.ability.earth;

import com.jedk1.projectkorra.mobs.MobMethods;
import com.jedk1.projectkorra.mobs.ProjectKorraMobs;
import com.jedk1.projectkorra.mobs.object.SubElement;
import com.projectkorra.projectkorra.GeneralMethods;

import org.bukkit.entity.LivingEntity;

public class EarthAbility {

	private static boolean earth = ProjectKorraMobs.plugin.getConfig().getBoolean("Abilities.Earth.Enabled");
	private static boolean lava = ProjectKorraMobs.plugin.getConfig().getBoolean("Properties.SubElements.Earth.Lava.Enabled");
	private static boolean metal = ProjectKorraMobs.plugin.getConfig().getBoolean("Properties.SubElements.Earth.Metal.Enabled");
	private static int frequency = ProjectKorraMobs.plugin.getConfig().getInt("Properties.SubElements.Earth.AbilityFrequency");
	
	public static void execute(LivingEntity entity, LivingEntity target) {
		if (!earth) return;
		if (!MobMethods.canBend(entity)) return;
		if (entity.getWorld() != target.getWorld()) return;
		if (GeneralMethods.rand.nextInt(frequency) == 0 && MobMethods.hasSubElement(entity)) {
			switch (MobMethods.getSubElement(entity)) {
				case Lava:
					if (lava && MobMethods.getRandomSourceBlock(entity.getLocation(), 3, null, SubElement.Lava) != null) {
						new LavaBlast(entity, target.getLocation());
						return;
					}
				case Metal:
					if (metal && MobMethods.getRandomSourceBlock(entity.getLocation(), 3, null, SubElement.Metal) != null) {
						new MetalBlast(entity, target.getLocation());
						return;
					}
				default:
					break;
			}
		} else {
			new EarthBlast(entity, target.getLocation());
		}
	}
	
	public static void progress() {
		EarthBlast.progressAll();
		MetalBlast.progressAll();
		LavaBlast.progressAll();
	}
	
	public static void remove() {
		EarthBlast.removeAll();
		MetalBlast.removeAll();
		LavaBlast.removeAll();
	}
}
