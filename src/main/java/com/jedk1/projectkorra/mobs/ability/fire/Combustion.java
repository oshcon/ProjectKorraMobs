package com.jedk1.projectkorra.mobs.ability.fire;

import com.jedk1.projectkorra.mobs.MobMethods;
import com.jedk1.projectkorra.mobs.ProjectKorraMobs;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.ElementalAbility;
import com.projectkorra.projectkorra.util.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import java.util.concurrent.ConcurrentHashMap;

public class Combustion {

	public static ConcurrentHashMap<Integer, Combustion> instances = new ConcurrentHashMap<Integer, Combustion>();
	
	private static double damage = ProjectKorraMobs.plugin.getConfig().getDouble("Abilities.Fire.Combustion.Damage");
	private static long firetick = ProjectKorraMobs.plugin.getConfig().getLong("Abilities.Fire.Combustion.FireTick");
	
	private LivingEntity entity;
	private LivingEntity target;
	private Location origin;
	private Location head;
	private Vector dir;
	
	private static int ID = Integer.MIN_VALUE;
	private int id;
	
	public Combustion(LivingEntity entity, LivingEntity target) {
		this.entity = entity;
		this.target = target;
		origin = entity.getEyeLocation();
		head = entity.getEyeLocation();
		dir = GeneralMethods.getDirection(entity.getLocation(), target.getLocation()).normalize();
		id = ID;
		instances.put(id, this);
		if (ID == Integer.MAX_VALUE) {
			ID = Integer.MIN_VALUE;
		}
		ID++;
	}
	
	@SuppressWarnings("deprecation")
	private boolean progress() {
		if (entity == null || target == null) {
			return false;
		}
		if (entity.getWorld() != head.getWorld()) {
			return false;
		}
		if (origin.distance(head) > 20) {
			explosion(head);
			return false;
		}
		if (!MobMethods.isTransparent(head.getBlock()) || ElementalAbility.isWater(head.getBlock())) {
			return false;
		}
		dir = GeneralMethods.getDirection(head, target.getLocation()).normalize();
		head.add(dir.multiply(1));
		com.projectkorra.projectkorra.ability.FireAbility.playFirebendingSound(head);
		ParticleEffect.FLAME.display(head, 0.1F, 0.1F, 0.1F, 0, 6);
		ParticleEffect.LARGE_SMOKE.display(head, 0.1F, 0.1F, 0.1F, 0, 6);
		ParticleEffect.FIREWORKS_SPARK.display(head, 0.1F, 0.1F, 0.1F, 0, 6);
		for (Entity entity : GeneralMethods.getEntitiesAroundPoint(head, 2)) {
			if (entity instanceof LivingEntity && entity.getEntityId() != this.entity.getEntityId()) {
				if (entity instanceof Creature) {
					((Creature) entity).setTarget(this.entity);
				}
				explosion(head);
				((LivingEntity) entity).damage(damage, this.entity);
				entity.setLastDamageCause(new EntityDamageByEntityEvent(this.entity, entity, DamageCause.CUSTOM, damage));
				entity.setFireTicks((int) (firetick/50));
				return false;
			}
		}
		return true;
	}
	
	public void explosion(Location loc) {
		ParticleEffect.FLAME.display(loc, 0.0F, 0.0F, 0.0F, 1.0F, 5);
		ParticleEffect.LARGE_SMOKE.display(loc, 0.0F, 0.0F, 0.0F, 1.0F, 5);
		ParticleEffect.FIREWORKS_SPARK.display(loc, 0.0F, 0.0F, 0.0F, 1.0F, 5);
		ParticleEffect.HUGE_EXPLOSION.display(loc, 0.0F, 0.0F, 0.0F, 0.1F, 2);
		loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 0.8F);
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
