package com.jedk1.projectkorra.mobs.ability.fire;

import com.jedk1.projectkorra.mobs.MobMethods;
import com.jedk1.projectkorra.mobs.ProjectKorraMobs;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.firebending.FireMethods;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.concurrent.ConcurrentHashMap;

public class Lightning {
	
	private static ConcurrentHashMap<Integer, Bolt> bolts = new ConcurrentHashMap<Integer, Bolt>();
	
	private static double damage = ProjectKorraMobs.plugin.getConfig().getDouble("Abilities.Earth.Lightning.Damage");
	
	private int id;
	private static int ID = Integer.MIN_VALUE;
	
	public Lightning(LivingEntity entity, Location target) {
		for (int i = 0; i < 3; i++) {
			spawnBolt(entity, entity.getLocation().setDirection(GeneralMethods.getDirection(entity.getLocation(), target).normalize()), 15, 1, 20);
		}
	}
	
	private void spawnBolt(LivingEntity entity, Location location, double max, double gap, int arc){
		id = ID;
		bolts.put(id, new Bolt(entity, location, id, max, gap, arc));
		if (ID == Integer.MAX_VALUE)
			ID = Integer.MIN_VALUE;
		ID++;
	}

	public class Bolt {

		private LivingEntity entity;
		private Location location;
		private float initYaw;
		private float initPitch;
		private double step;
		private double max;
		private double gap;
		private int id;
		private int arc;

		public Bolt(LivingEntity entity, Location location, int id, double max, double gap, int arc) {
			this.entity = entity;
			this.location = location;
			this.id = id;
			this.max = max;
			this.arc = arc;
			this.gap = gap;
			initYaw = location.getYaw();
			initPitch = location.getPitch();
		}

		@SuppressWarnings("deprecation")
		private boolean progress() {
			if (this.step >= max) {
				return false;
			}
			if (!MobMethods.isTransparent(location.getBlock())) {
				return false;
			}
			double step = 0.2;
			for(double i = 0; i < gap; i+= step){
				this.step += step;
				location = location.add(location.getDirection().clone().multiply(step));
				FireMethods.playLightningbendingParticle(location, 0f, 0f, 0f);
			}
			switch (GeneralMethods.rand.nextInt(3)) {
			case 0:
				location.setYaw(initYaw - arc);
				break;
			case 1:
				location.setYaw(initYaw + arc);
				break;
			default:
				location.setYaw(initYaw);
				break;
			}
			switch (GeneralMethods.rand.nextInt(3)) {
			case 0:
				location.setPitch(initPitch - arc);
				break;
			case 1:
				location.setPitch(initPitch + arc);
				break;
			default:
				location.setPitch(initPitch);
				break;
			}

			if(GeneralMethods.rand.nextInt(3) == 0) {
				location.getWorld().playSound(location, Sound.CREEPER_HISS, 1, 0);
			}

			for (Entity entity : GeneralMethods.getEntitiesAroundPoint(location, 2)) {
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
	}
	
	public static void progressAll() {
		for (int id : bolts.keySet()) {
			if (!bolts.get(id).progress()) {
				bolts.remove(id);
			}
		}
	}
	
	public static void removeAll() {
		bolts.clear();
	}
}
