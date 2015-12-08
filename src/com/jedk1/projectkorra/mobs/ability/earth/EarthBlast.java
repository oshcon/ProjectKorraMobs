package com.jedk1.projectkorra.mobs.ability.earth;

import com.jedk1.projectkorra.mobs.MobMethods;
import com.jedk1.projectkorra.mobs.ProjectKorraMobs;
import com.jedk1.projectkorra.mobs.object.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.airbending.AirShield;
import com.projectkorra.projectkorra.earthbending.EarthMethods;
import com.projectkorra.projectkorra.util.TempBlock;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import java.util.concurrent.ConcurrentHashMap;

public class EarthBlast {

	public static ConcurrentHashMap<Integer, EarthBlast> instances = new ConcurrentHashMap<Integer, EarthBlast>();
	
	private static double damage = ProjectKorraMobs.plugin.getConfig().getDouble("Abilities.Earth.EarthBlast.Damage");
	
	private LivingEntity entity;
	private Location origin;
	private Location head;
	private Vector dir;
	private Material type;
	private byte data;
	private Block block;
	private Block source;
	
	private static int ID = Integer.MIN_VALUE;
	private int id;
	
	@SuppressWarnings("deprecation")
	public EarthBlast(LivingEntity entity, Location target) {
		this.entity = entity;
		//origin = entity.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation().getBlock().getRelative(GeneralMethods.getCardinalDirection(entity.getLocation().getDirection())).getLocation();
		//if (!EarthMethods.isEarthbendable(origin.getBlock().getType())) {
		Block targetorigin = MobMethods.getRandomSourceBlock(entity.getLocation(), 3, Element.Earth, null);
		if (targetorigin == null) {
			return;
		}
		origin = targetorigin.getLocation();
		type = origin.getBlock().getType();
		data = origin.getBlock().getData();
		new TempBlock(origin.getBlock(), Material.AIR, (byte) 0);
		source = origin.getBlock();
		head = entity.getEyeLocation();
		dir = GeneralMethods.getDirection(origin.add(0, 1, 0), target).normalize();
		id = ID;
		EarthMethods.playEarthbendingSound(origin);
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
		new TempBlock(head.getBlock(), type, data);
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
		if (source != null) {
			TempBlock.revertBlock(source, Material.AIR);
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
