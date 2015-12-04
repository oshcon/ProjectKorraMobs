package com.jedk1.projectkorra.mobs.ability;

import com.projectkorra.projectkorra.util.ParticleEffect;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.concurrent.ConcurrentHashMap;

public class FireJet {

	public static ConcurrentHashMap<Integer, FireJet> instances = new ConcurrentHashMap<Integer, FireJet>();
	
	private static long duration = 1000;
	private static double speed = 0.8;
	
	private LivingEntity entity;
	private Location target;
	private long time;
	
	private static int ID = Integer.MIN_VALUE;
	private int id;
	
	public FireJet(LivingEntity entity, Location target) {
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
		entity.setVelocity(entity.getLocation().getDirection().multiply(speed));
		ParticleEffect.FLAME.display(entity.getLocation(), 0.7F, 0.7F, 0.7F, 0, 6);
		ParticleEffect.SMOKE.display(entity.getLocation(), 0.7F, 0.7F, 0.7F, 0, 3);
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
