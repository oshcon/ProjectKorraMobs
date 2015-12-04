package com.jedk1.projectkorra.mobs.ability;

import com.jedk1.projectkorra.mobs.MobMethods;
import com.jedk1.projectkorra.mobs.ProjectKorraMobs;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.airbending.AirShield;
import com.projectkorra.projectkorra.util.ParticleEffect;
import com.projectkorra.projectkorra.util.TempBlock;
import com.projectkorra.projectkorra.waterbending.WaterMethods;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.concurrent.ConcurrentHashMap;

public class WaterBlast {

	public static ConcurrentHashMap<Integer, WaterBlast> instances = new ConcurrentHashMap<Integer, WaterBlast>();
	
	private static double damage = ProjectKorraMobs.plugin.getConfig().getDouble("Abilities.Water.WaterBlast.Damage");
	
	private LivingEntity entity;
	private Location origin;
	private Location head;
	private Vector dir;
	
	private Block block;
	
	private static int ID = Integer.MIN_VALUE;
	private int id;
	
	public WaterBlast(LivingEntity entity, Location target) {
		this.entity = entity;
		origin = entity.getEyeLocation();
		head = entity.getEyeLocation();
		dir = GeneralMethods.getDirection(entity.getLocation(), target).normalize();
		EntityEquipment ee = entity.getEquipment();
		ee.setItemInHand(new ItemStack(Material.POTION, 1));
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
		if (origin.distance(head) > 20) {
			return false;
		}
		head.add(dir.multiply(1));
		if (!MobMethods.isTransparent(head.getBlock())) {
			return false;
		}
		if (AirShield.isWithinShield(head)) {
			return false;
		}
		if (WaterMethods.isWater(head.getBlock())) {
			ParticleEffect.WATER_BUBBLE.display((float) Math.random(), (float) Math.random(), (float) Math.random(), 0f, 5, head, 257D);
		}
		new TempBlock(head.getBlock(), Material.STATIONARY_WATER, (byte) 8);
		if (block != null) {
			TempBlock.revertBlock(block, Material.AIR);
		}
		block = head.getBlock();
		for (Entity entity : GeneralMethods.getEntitiesAroundPoint(head, 2)) {
			if (entity instanceof LivingEntity && entity.getEntityId() != this.entity.getEntityId()) {
				if (entity instanceof Creature) {
					((Creature) entity).setTarget(this.entity);
				}
				((LivingEntity) entity).damage(damage, this.entity);
				entity.setLastDamageCause(new EntityDamageByEntityEvent(this.entity, entity, DamageCause.CUSTOM, damage));
				return false;
			}
		}
		return true;
	}
	
	private void remove() {
		if (block != null) {
			TempBlock.revertBlock(block, Material.AIR);
		}
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
