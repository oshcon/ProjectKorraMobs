package com.jedk1.projectkorra.mobs.ability.fire;

import com.jedk1.projectkorra.mobs.ProjectKorraMobs;
import com.projectkorra.projectkorra.firebending.FireMethods;
import com.projectkorra.projectkorra.util.ParticleEffect;
import com.projectkorra.projectkorra.waterbending.WaterMethods;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.concurrent.ConcurrentHashMap;

public class FireJet {

	public static ConcurrentHashMap<Integer, FireJet> instances = new ConcurrentHashMap<Integer, FireJet>();
	
	private static long duration = ProjectKorraMobs.plugin.getConfig().getLong("Abilities.Fire.FireJet.Duration");
	private static double speed = ProjectKorraMobs.plugin.getConfig().getDouble("Abilities.Fire.FireJet.Speed");
	
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
		if (WaterMethods.isWater(entity.getLocation().getBlock())) {
			return false;
		}
		FireMethods.playFirebendingSound(entity.getLocation());
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
