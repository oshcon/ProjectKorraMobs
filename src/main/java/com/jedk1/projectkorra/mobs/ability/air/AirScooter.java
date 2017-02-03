package com.jedk1.projectkorra.mobs.ability.air;

import com.jedk1.projectkorra.mobs.ProjectKorraMobs;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.concurrent.ConcurrentHashMap;

public class AirScooter {

	public static ConcurrentHashMap<Integer, AirScooter> instances = new ConcurrentHashMap<Integer, AirScooter>();
	
	private static long duration = ProjectKorraMobs.plugin.getConfig().getLong("Abilities.Air.AirScooter.Duration");
	private static double speed = ProjectKorraMobs.plugin.getConfig().getDouble("Abilities.Air.AirScooter.Speed");
	
	private LivingEntity entity;
	private Location target;
	private long time;
	
	private static int ID = Integer.MIN_VALUE;
	private int id;
	
	public AirScooter(LivingEntity entity, Location target) {
		this.entity = entity;
		id = ID;
		time = System.currentTimeMillis() + duration;
		this.target = target;
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
		if (System.currentTimeMillis() > time) {
			return false;
		}
		if (entity.getLocation().distance(target) < 3) {
			return false;
		}
		if (com.projectkorra.projectkorra.ability.WaterAbility.isWater(entity.getLocation().getBlock())) {
			return false;
		}
		entity.setVelocity(entity.getLocation().getDirection().multiply(speed));
		com.projectkorra.projectkorra.ability.AirAbility.playAirbendingParticles(entity.getLocation(), 5, (float) Math.random(), (float) Math.random(), (float) Math.random());
		com.projectkorra.projectkorra.ability.AirAbility.playAirbendingSound(entity.getLocation());
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
