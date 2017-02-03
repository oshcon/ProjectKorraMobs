package com.jedk1.projectkorra.mobs.manager;

import com.jedk1.projectkorra.mobs.ProjectKorraMobs;
import com.jedk1.projectkorra.mobs.ability.AvatarAbility;
import com.jedk1.projectkorra.mobs.ability.earth.EarthAbility;
import com.jedk1.projectkorra.mobs.ability.fire.FireAbility;
import com.jedk1.projectkorra.mobs.object.Element;
import com.jedk1.projectkorra.mobs.MobMethods;
import com.jedk1.projectkorra.mobs.ability.air.AirAbility;
import com.jedk1.projectkorra.mobs.ability.water.WaterAbility;
import com.jedk1.projectkorra.mobs.object.PKEntity;
import com.projectkorra.projectkorra.waterbending.blood.Bloodbending;

import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EntityManager {

	public static ConcurrentHashMap<UUID, PKEntity> entityarray = new ConcurrentHashMap<UUID, PKEntity>();

	private static int chance = ProjectKorraMobs.plugin.getConfig().getInt("Properties.BendFrequency");
	static Random rand = new Random();

	public EntityManager() {

	}

	public static void addEntity(LivingEntity entity, LivingEntity target) {
		if (!(entity instanceof Creature)) {
			return;
		}
		if (MobMethods.isDisabledWorld(entity.getWorld())) {
			return;
		}
		if (entityarray.containsKey(entity.getUniqueId())) {
			entityarray.get(entity.getUniqueId()).setTarget(target);
			return;
		}
		entityarray.put(entity.getUniqueId(), new PKEntity(entity, target));
		MobMethods.assignElement(entity);
	}

	public static void removeEntity(LivingEntity entity) {
		if (entityarray.containsKey(entity.getUniqueId())) {
			entityarray.remove(entity.getUniqueId());
		}
	}

	public static void progress() {
		for (UUID uuid : entityarray.keySet()) {
			PKEntity pkentity = entityarray.get(uuid);
			LivingEntity entity = pkentity.getEntity();
			LivingEntity target = pkentity.getTarget();
			Creature e = (Creature) entity;
			if (entity == null || entity.isDead() || e.getTarget() == null || e.getTarget().isDead() || Bloodbending.isBloodbent(entity) || !MobMethods.hasElement(entity)) {
				entityarray.remove(uuid);
				return;
			}
			if (target == null || target.isDead()) {
				entityarray.remove(uuid);
				return;
			}
			
			if (entity.getMetadata("element").size() > 0) {
				if (rand.nextInt(chance) == 0) {
					if (!entity.hasLineOfSight(target)) {
						return;
					}
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
