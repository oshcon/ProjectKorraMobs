package com.jedk1.projectkorra.mobs.manager;

import com.jedk1.projectkorra.mobs.MobMethods;
import com.jedk1.projectkorra.mobs.ProjectKorraMobs;
import com.jedk1.projectkorra.mobs.ability.AirBlast;
import com.jedk1.projectkorra.mobs.ability.EarthBlast;
import com.jedk1.projectkorra.mobs.ability.FireBlast;
import com.jedk1.projectkorra.mobs.ability.FireJet;
import com.jedk1.projectkorra.mobs.ability.WaterBlast;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.waterbending.Bloodbending;

import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;

import java.util.concurrent.ConcurrentHashMap;

public class EntityManager {

	public static ConcurrentHashMap<LivingEntity, LivingEntity> entityarray = new ConcurrentHashMap<LivingEntity, LivingEntity>();

	private static boolean air = ProjectKorraMobs.plugin.getConfig().getBoolean("Abilities.Air.Enabled");
	private static boolean fire = ProjectKorraMobs.plugin.getConfig().getBoolean("Abilities.Fire.Enabled");
	private static boolean water = ProjectKorraMobs.plugin.getConfig().getBoolean("Abilities.Water.Enabled");
	private static boolean earth = ProjectKorraMobs.plugin.getConfig().getBoolean("Abilities.Earth.Enabled");
	private static int chance = ProjectKorraMobs.plugin.getConfig().getInt("Properties.BendFrequency");

	public EntityManager() {

	}

	public static void addEntity(LivingEntity entity, LivingEntity target) {
		if (MobMethods.isDisabledWorld(entity.getWorld())) {
			return;
		}
		if (entityarray.containsKey(entity)) {
			entityarray.replace(entity, target);
			return;
		}
		entityarray.put(entity, target);
		MobMethods.assignElement(entity);
	}

	public static void removeEntity(LivingEntity entity) {
		if (entityarray.containsKey(entity)) {
			entityarray.remove(entity);
		}
	}

	public static void progress() {
		for (LivingEntity entity : entityarray.keySet()) {
			Creature e = (Creature) entity;
			LivingEntity target = entityarray.get(entity);
			if (e == null || e.isDead() || target == null || target.isDead() || e.getTarget() == null || Bloodbending.isBloodbended(entity) || !entity.hasMetadata("element")) {
				entityarray.remove(entity);
				continue;
			}
			if (entity.getMetadata("element").size() > 0) {
				if (GeneralMethods.rand.nextInt(chance) == 0) {
					switch(entity.getMetadata("element").get(0).asInt()) {
						case 0:
							if (!air) break;
							new AirBlast(entity, target.getLocation());
							break;
						case 1:
							if (!earth) break;
							new EarthBlast(entity, target.getLocation());
							break;
						case 2:
							if (!water) break;
							new WaterBlast(entity, target.getLocation());
							break;
						case 3:
							if (!fire) break;
							switch (GeneralMethods.rand.nextInt(2)) {
								case 0:
									new FireBlast(entity, target.getLocation());
									break;
								case 1:
									new FireJet(entity, target.getLocation());
									break;
							}
							break;
						case 4:
							switch (GeneralMethods.rand.nextInt(4)) {
								case 0:
									if (!air) break;
									new AirBlast(entity, target.getLocation());
									break;
								case 1:
									if (!earth) break;
									new EarthBlast(entity, target.getLocation());
									break;
								case 2:
									if (!water) break;
									new WaterBlast(entity, target.getLocation());
									break;
								case 3:
									if (!fire) break;
									switch (GeneralMethods.rand.nextInt(2)) {
										case 0:
											new FireBlast(entity, target.getLocation());
											break;
										case 1:
											new FireJet(entity, target.getLocation());
											break;
									}
									break;
						}
					}
				}
			}
		}
	}

	public static void remove() {
		entityarray.clear();
	}

}
