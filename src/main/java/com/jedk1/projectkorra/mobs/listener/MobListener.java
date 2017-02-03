package com.jedk1.projectkorra.mobs.listener;

import com.jedk1.projectkorra.mobs.ProjectKorraMobs;
import com.jedk1.projectkorra.mobs.MobMethods;
import com.jedk1.projectkorra.mobs.manager.EntityManager;
import com.projectkorra.projectkorra.event.BendingReloadEvent;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.world.ChunkLoadEvent;

public class MobListener implements Listener {

	ProjectKorraMobs plugin;

	private static boolean airFallDamage = ProjectKorraMobs.plugin.getConfig().getBoolean("Properties.Air.NoFallDamage");
	private static boolean villagerFightBack = ProjectKorraMobs.plugin.getConfig().getBoolean("Properties.Entity.Villager.FightBack");
	private static boolean isDisguisingEnabled = ProjectKorraMobs.plugin.getConfig().getBoolean("Properties.LibsDisguises.DisguiseMob");
	private static boolean clearMobDrops = ProjectKorraMobs.plugin.getConfig().getBoolean("Properties.LibsDisguises.ClearDisguisedMobDrops");
	private static int spawnChance = ProjectKorraMobs.plugin.getConfig().getInt("Properties.SpawnFrequency");

	public MobListener(ProjectKorraMobs plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onChunkLoad(ChunkLoadEvent event) {
		if (isDisguisingEnabled) {
			for (Entity e : event.getChunk().getEntities()) {
				if (MobMethods.hasElement((LivingEntity) e)) {
					MobMethods.assignDisguise(e);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onFireAttack(EntityTargetEvent event) {

		if (event.getTarget() == null) {
			return;
		}

		if (event.getEntity() instanceof LivingEntity && event.getTarget() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) event.getEntity();
			if (MobMethods.canEntityBend(entity.getType())) {
				EntityManager.addEntity(entity, (LivingEntity) event.getTarget());
				if (event.getTarget().getType().equals(EntityType.VILLAGER) && villagerFightBack) {
					EntityManager.addEntity((LivingEntity) event.getTarget(), entity);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event) {
		if (MobMethods.hasElement(event.getEntity())) {
			if (clearMobDrops) {
				if (MobMethods.isDisguised(event.getEntity())) {
					event.getDrops().clear();
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMobSpawn(CreatureSpawnEvent event) {
		LivingEntity entity = event.getEntity();
		CreatureSpawnEvent.SpawnReason reason = event.getSpawnReason();

		if (MobMethods.isDisabledWorld(entity.getWorld())) {
			return;
		}

		if (reason.equals(CreatureSpawnEvent.SpawnReason.SPAWNER) || reason.equals(CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) ) {
			return;
		}

		if (MobMethods.canEntityBend(entity.getType())) {

			if (MobMethods.rand.nextInt(100) <= spawnChance) {

				MobMethods.assignElement(entity);
				MobMethods.assignDisguise(entity);

				if (clearMobDrops) {
					if (MobMethods.isDisguised(event.getEntity())) {
						entity.setCanPickupItems(false);
						entity.getEquipment().clear();
					}
				}
			}
		}
	}

	@EventHandler
	public void onCombust(EntityCombustEvent event) {
		if (event.getEntity() instanceof LivingEntity) {
			if (MobMethods.hasElement((LivingEntity) event.getEntity())) {
				if (MobMethods.isDisguised(event.getEntity())) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) event.getEntity();
			if (MobMethods.canEntityBend(entity.getType())) {
				if (((MobMethods.getElement(entity) != null && MobMethods.getElement(entity).isAirbender()) || MobMethods.isAvatar(entity))  && airFallDamage) {
					if (event.getCause() == DamageCause.FALL) {
						event.setCancelled(true);
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPKReload(BendingReloadEvent event) {
		ProjectKorraMobs.plugin.reloadConfig();
		MobMethods.registerDisabledWorlds();
		MobMethods.registerEntityTypes();
		MobMethods.registerDisguisableEntities();
	}
}
