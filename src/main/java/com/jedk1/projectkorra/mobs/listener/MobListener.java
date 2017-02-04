package com.jedk1.projectkorra.mobs.listener;

import com.jedk1.projectkorra.mobs.Compatibility;
import com.jedk1.projectkorra.mobs.ProjectKorraMobs;
import com.jedk1.projectkorra.mobs.MobMethods;
import com.jedk1.projectkorra.mobs.manager.EntityManager;
import com.projectkorra.projectkorra.event.BendingReloadEvent;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MiscDisguise;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Vex;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class MobListener implements Listener {

	ProjectKorraMobs plugin;

	private static boolean airFallDamage = ProjectKorraMobs.plugin.getConfig().getBoolean("Properties.Air.NoFallDamage");
	private static boolean villagerFightBack = ProjectKorraMobs.plugin.getConfig().getBoolean("Properties.Entity.Villager.FightBack");

    private static boolean isDisguisingEnabled = ProjectKorraMobs.plugin.getConfig().getBoolean("Properties.LibsDisguises.DisguiseMob");
    private static boolean clearMobDrops = ProjectKorraMobs.plugin.getConfig().getBoolean("Properties.LibsDisguises.ClearDisguisedMobDrops");
    private static boolean spawnSpirits = ProjectKorraMobs.plugin.getConfig().getBoolean("Properties.LibsDisguises.Spirits.Enabled");
    private static int spawnSpiritChance = ProjectKorraMobs.plugin.getConfig().getInt("Properties.LibsDisguises.Spirits.Chance");

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
                    event.getEntity().getEquipment().clear();
					event.getDrops().clear();
				}
			}

			if (spawnSpirits) {
                if (Compatibility.isHooked("LibsDisguises")) {

                    if (event.getEntity().hasMetadata("korramob_spirit")) {
                        return;
                    }

                    if (event.getEntity().hasMetadata("spawnermob")) {
                        return;
                    }

                    if (MobMethods.rand.nextInt(100) <= spawnSpiritChance) {
                        Location l = event.getEntity().getLocation();

                        Vex v = (Vex) l.getWorld().spawnEntity(l, EntityType.VEX);

                        v.setCustomName(ProjectKorraMobs.addColor("&7Spirit"));
                        v.setCustomNameVisible(false);
                        v.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1.0);
                        v.setMetadata("korramob_spirit", new FixedMetadataValue(plugin, "spirit"));

                        MiscDisguise d = new MiscDisguise(DisguiseType.SHULKER_BULLET);
                        DisguiseAPI.disguiseEntity(v, d);
                    }
                } else {
                    ProjectKorraMobs.log("&cAttempted to spawn a spirit, however LibsDisguises could not be found!");
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
            entity.setMetadata("spawnermob", new FixedMetadataValue(plugin, "ignoreme"));
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
