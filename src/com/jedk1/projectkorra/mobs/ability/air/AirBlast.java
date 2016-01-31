package com.jedk1.projectkorra.mobs.ability.air;

import com.jedk1.projectkorra.mobs.MobMethods;
import com.jedk1.projectkorra.mobs.ProjectKorraMobs;
import com.projectkorra.projectkorra.GeneralMethods;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.concurrent.ConcurrentHashMap;

public class AirBlast {

	public static ConcurrentHashMap<Integer, AirBlast> instances = new ConcurrentHashMap<Integer, AirBlast>();
	
	private static double knockback = ProjectKorraMobs.plugin.getConfig().getDouble("Abilities.Air.AirBlast.Knockback");
	
	private LivingEntity entity;
	private Location origin;
	private Location head;
	private Vector dir;
	
	private static int ID = Integer.MIN_VALUE;
	private int id;
	
	public AirBlast(LivingEntity entity, Location target) {
		this.entity = entity;
		origin = entity.getEyeLocation();
		head = entity.getEyeLocation();
		dir = GeneralMethods.getDirection(entity.getLocation(), target).normalize();
		id = ID;
		instances.put(id, this);
		if (ID == Integer.MAX_VALUE) {
			ID = Integer.MIN_VALUE;
		}
		ID++;
	}
	
	private boolean progress() {
		if (entity == null) {
			return false;
		}
		if (entity.getWorld() != head.getWorld()) {
			return false;
		}
		if (origin.distance(head) > 20) {
			return false;
		}
		if (!MobMethods.isTransparent(head.getBlock())) {
			return false;
		}
		head.add(dir.multiply(1));
		com.projectkorra.projectkorra.ability.AirAbility.playAirbendingParticles(head, 5);
		com.projectkorra.projectkorra.ability.AirAbility.playAirbendingSound(head);
		for (Entity entity : GeneralMethods.getEntitiesAroundPoint(head, 2)) {
			if (entity instanceof LivingEntity && entity.getEntityId() != this.entity.getEntityId()) {
				entity.setVelocity(dir.multiply(knockback));
				if (entity instanceof Creature) {
					((Creature) entity).setTarget(this.entity);
				}
				return false;
			}
		}
		return true;
	}
	
	private void remove() {
		instances.remove(id);
	}
	
	public static void progressAll() {
		for (int id : instances.keySet()) {
			if (!instances.get(id).progress()) {
				instances.get(id).remove();
			}
		}
	}
	
	public static void removeAll() {
		for (int id : instances.keySet()) {
			instances.remove(id);
		}
	}
}
