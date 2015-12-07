package com.jedk1.projectkorra.mobs;

import com.projectkorra.projectkorra.BendingManager;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.earthbending.EarthMethods;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MobMethods {
	
	private static boolean avatar = ProjectKorraMobs.plugin.getConfig().getBoolean("Properties.Avatar.Enabled");
	private static int avatarFrequency = ProjectKorraMobs.plugin.getConfig().getInt("Properties.Avatar.Frequency");
	
	public static List<String> disabledWorlds = new ArrayList<String>();
	public static List<String> entityTypes = new ArrayList<String>();

	/**
	 * Assigns a random element to an entity.
	 * @param entity
	 */
	public static void assignElement(Entity entity) {
		int i = GeneralMethods.rand.nextInt(4);
		if (avatar && GeneralMethods.rand.nextInt(avatarFrequency) == 0) {
			i = 4;
		}
		if (!entity.hasMetadata("element")) {
			entity.setMetadata("element", new FixedMetadataValue(ProjectKorra.plugin, i));
		}
	}
	
	/**
	 * Returns true if the entity is an Air Bender.
	 * @param entity
	 * @return
	 */
	public static boolean isAirbender(LivingEntity entity) {
		if (entity.hasMetadata("element") && entity.getMetadata("element").size() > 0 && (entity.getMetadata("element").get(0).asInt() == 0)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if the entity is an Earth Bender.
	 * @param entity
	 * @return
	 */
	public static boolean isEarthbender(LivingEntity entity) {
		if (entity.hasMetadata("element") && entity.getMetadata("element").size() > 0 && (entity.getMetadata("element").get(0).asInt() == 1)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if the entity is a Fire Bender.
	 * @param entity
	 * @return
	 */
	public static boolean isFirebender(LivingEntity entity) {
		if (entity.hasMetadata("element") && entity.getMetadata("element").size() > 0 && (entity.getMetadata("element").get(0).asInt() == 2)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if the entity is a Water Bender.
	 * @param entity
	 * @return
	 */
	public static boolean isWaterbender(LivingEntity entity) {
		if (entity.hasMetadata("element") && entity.getMetadata("element").size() > 0 && (entity.getMetadata("element").get(0).asInt() == 3)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if the entity is an Avatar.
	 * @param entity
	 * @return
	 */
	public static boolean isAvatar(LivingEntity entity) {
		if (entity.hasMetadata("element") && entity.getMetadata("element").size() > 0 && (entity.getMetadata("element").get(0).asInt() == 4)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if the entity is enabled in the configuration.
	 * @param type
	 * @return
	 */
	public static boolean canEntityBend(EntityType type) {
		return entityTypes.contains(type.toString());
	}
	
	/**
	 * Returns true if the entity can bend.
	 * @param entity
	 * @return
	 */
	public static boolean canBend(LivingEntity entity) {
		if (BendingManager.events.get(entity.getWorld()) != null && BendingManager.events.get(entity.getWorld()).equalsIgnoreCase("SolarEclipse") && isFirebender(entity)) {
			return false;
		}
		if (BendingManager.events.get(entity.getWorld()) != null && BendingManager.events.get(entity.getWorld()).equalsIgnoreCase("LunarEclipse") && isWaterbender(entity)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if a block is transparent.
	 * @param block
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static boolean isTransparent(Block block) {
		if (!Arrays.asList(EarthMethods.transparentToEarthbending).contains(block.getTypeId())) {
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if a world is disabled in the PK configuration.
	 * @param world
	 * @return
	 */
	public static boolean isDisabledWorld(World world) {
		if (disabledWorlds.contains(world.getName())) {
			return true;
		}
		return false;
	}
	
	/**
	 * Registers disabled worlds on plugin load.
	 */
	public static void registerDisabledWorlds() {
		disabledWorlds.clear();
		if (ProjectKorra.plugin.getConfig().getStringList("Properties.DisabledWorlds") != null) {
			for (String s : ProjectKorra.plugin.getConfig().getStringList("Properties.DisabledWorlds")) {
				disabledWorlds.add(s);
			}
		}
	}
	
	/**
	 * Registers entity types that can bend.
	 */
	public static void registerEntityTypes() {
		entityTypes.clear();
		if (ProjectKorraMobs.plugin.getConfig().getShortList("Properties.EntityTypes") != null) {
			for (String s : ProjectKorraMobs.plugin.getConfig().getStringList("Properties.EntityTypes")) {
				entityTypes.add(s);
			}
		}
	}
}
