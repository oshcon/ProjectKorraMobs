package com.jedk1.projectkorra.mobs.ability.fire;

import com.jedk1.projectkorra.mobs.ProjectKorraMobs;
import com.jedk1.projectkorra.mobs.MobMethods;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.ElementalAbility;
import com.projectkorra.projectkorra.util.ParticleEffect;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import java.util.concurrent.ConcurrentHashMap;

public class FireBlast {

	public static ConcurrentHashMap<Integer, FireBlast> instances = new ConcurrentHashMap<Integer, FireBlast>();
	
	private static double damage = ProjectKorraMobs.plugin.getConfig().getDouble("Abilities.Fire.FireBlast.Damage");
	private static long firetick = ProjectKorraMobs.plugin.getConfig().getLong("Abilities.Fire.FireBlast.FireTick");
	
	private LivingEntity entity;
	private Location origin;
	private Location head;
	private Vector dir;
	
	private static int ID = Integer.MIN_VALUE;
	private int id;
	
	public FireBlast(LivingEntity entity, Location target) {
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
	
	@SuppressWarnings("deprecation")
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
		if (!MobMethods.isTransparent(head.getBlock()) || ElementalAbility.isWater(head.getBlock())) {
			return false;
		}
		head.add(dir.multiply(1));
		com.projectkorra.projectkorra.ability.FireAbility.playFirebendingSound(head);
		ParticleEffect.FLAME.display(head, 0.275F, 0.275F, 0.275F, 0, 6);
		ParticleEffect.SMOKE.display(head, 0.3F, 0.3F, 0.3F, 0, 3);
		for (Entity entity : GeneralMethods.getEntitiesAroundPoint(head, 2)) {
			if (entity instanceof LivingEntity && entity.getEntityId() != this.entity.getEntityId()) {
				if (entity instanceof Creature) {
					((Creature) entity).setTarget(this.entity);
				}
				((LivingEntity) entity).damage(damage, this.entity);
				entity.setLastDamageCause(new EntityDamageByEntityEvent(this.entity, entity, DamageCause.CUSTOM, damage));
				entity.setFireTicks((int) (firetick/50));
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
