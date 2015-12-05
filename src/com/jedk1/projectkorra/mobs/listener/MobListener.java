package com.jedk1.projectkorra.mobs.listener;

import com.jedk1.projectkorra.mobs.MobMethods;
import com.jedk1.projectkorra.mobs.ProjectKorraMobs;
import com.jedk1.projectkorra.mobs.manager.EntityManager;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent;

public class MobListener implements Listener {

	ProjectKorraMobs plugin;

	public static boolean airFallDamage = ProjectKorraMobs.plugin.getConfig().getBoolean("Properties.Air.NoFallDamage");
	
	public MobListener(ProjectKorraMobs plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onFireAttack(EntityTargetEvent event) {
		if (event.getTarget() == null) {
			return;
		}
		if (event.getEntity() instanceof LivingEntity && event.getTarget() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) event.getEntity();
			if (entity instanceof Zombie) {
				EntityManager.addEntity(entity, (LivingEntity) event.getTarget());
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMobSpawn(CreatureSpawnEvent event) {
		LivingEntity entity = event.getEntity();
		if (MobMethods.isDisabledWorld(entity.getWorld())) {
			return;
		}
		if (entity instanceof Zombie) {
			MobMethods.assignElement(entity);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) event.getEntity();
			if (entity instanceof Zombie) {
				if (entity.hasMetadata("element") && entity.getMetadata("element").size() > 0 && (entity.getMetadata("element").get(0).asInt() == 0 || entity.getMetadata("element").get(0).asInt() == 4)  && airFallDamage) {
					if (event.getCause() == DamageCause.FALL) {
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}
}
