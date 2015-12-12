package com.jedk1.projectkorra.mobs.manager;

import com.jedk1.projectkorra.mobs.MobMethods;
import com.jedk1.projectkorra.mobs.ProjectKorraMobs;
import com.jedk1.projectkorra.mobs.ability.AvatarAbility;
import com.jedk1.projectkorra.mobs.ability.air.AirAbility;
import com.jedk1.projectkorra.mobs.ability.earth.EarthAbility;
import com.jedk1.projectkorra.mobs.ability.fire.FireAbility;
import com.jedk1.projectkorra.mobs.ability.water.WaterAbility;
import com.jedk1.projectkorra.mobs.object.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.waterbending.Bloodbending;

import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;

import java.util.concurrent.ConcurrentHashMap;

public class EntityManager {

	public static ConcurrentHashMap<LivingEntity, LivingEntity> entityarray = new ConcurrentHashMap<LivingEntity, LivingEntity>();

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
			if (e == null || e.isDead() || target == null || target.isDead() || e.getTarget() == null || Bloodbending.isBloodbended(entity) || !MobMethods.hasElement(entity)) {
				entityarray.remove(entity);
				continue;
			}
			if (entity.getMetadata("element").size() > 0) {
				if (GeneralMethods.rand.nextInt(chance) == 0) {
					switch(Element.getType(entity.getMetadata("element").get(0).asInt())) {
						case Air:
							AirAbility.execute(entity, target);
							break;
						case Earth:
							EarthAbility.execute(entity, target);
							break;
						case Fire:
							FireAbility.execute(entity, target);
							break;
						case Water:
							WaterAbility.execute(entity, target);
							break;
						case Avatar:
							AvatarAbility.execute(entity, target);
							break;
					}
				}
			}
		}
	}

	public static void remove() {
		entityarray.clear();
	}

}
