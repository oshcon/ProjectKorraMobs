package com.jedk1.projectkorra.mobs;

import com.jedk1.projectkorra.mobs.object.Element;
import com.jedk1.projectkorra.mobs.object.SubElement;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.EarthAbility;
import com.projectkorra.projectkorra.ability.ElementalAbility;
import com.projectkorra.projectkorra.ability.FireAbility;
import com.projectkorra.projectkorra.ability.WaterAbility;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MobMethods {

	private static boolean avatar = ProjectKorraMobs.plugin.getConfig().getBoolean("Properties.Avatar.Enabled");
	private static int avatarFrequency = ProjectKorraMobs.plugin.getConfig().getInt("Properties.Avatar.Frequency");
	private static boolean subelements = ProjectKorraMobs.plugin.getConfig().getBoolean("Properties.SubElements.Enabled");
	private static int metalFrequency = ProjectKorraMobs.plugin.getConfig().getInt("Properties.SubElements.Earth.Lava.Frequency");
	private static int lavaFrequency = ProjectKorraMobs.plugin.getConfig().getInt("Properties.SubElements.Earth.Metal.Frequency");
	private static int combustionFrequency = ProjectKorraMobs.plugin.getConfig().getInt("Properties.SubElements.Fire.Combustion.Frequency");
	private static int lightningFrequency = ProjectKorraMobs.plugin.getConfig().getInt("Properties.SubElements.Fire.Lightning.Frequency");
	private static int iceFrequency = ProjectKorraMobs.plugin.getConfig().getInt("Properties.SubElements.Water.Ice.Frequency");

	public static List<String> disabledWorlds = new ArrayList<String>();
	public static List<String> entityTypes = new ArrayList<String>();

	public static Random rand = new Random();

	/**
	 * Assigns a random element to an entity.
	 * Also assigns a random subelement if enabled.
	 * @param entity
	 */
	public static void assignElement(Entity entity) {
		int i = rand.nextInt(4);
		if (avatar && rand.nextInt(avatarFrequency) == 0) {
			i = 4;
		}
		if (!entity.hasMetadata("element")) {
			entity.setMetadata("element", new FixedMetadataValue(ProjectKorraMobs.plugin, i));
			if (subelements) {
				if (!entity.hasMetadata("subelement")) {
					switch (getElement((LivingEntity) entity)) {
						case Air:
							break;
						case Earth:
							switch (rand.nextInt(2)) {
								case 0:
									if (rand.nextInt(lavaFrequency) == 0) {
										entity.setMetadata("subelement", new FixedMetadataValue(ProjectKorraMobs.plugin, SubElement.Lava.ordinal()));
									}
									break;
								case 1:
									if (rand.nextInt(metalFrequency) == 0) {
										entity.setMetadata("subelement", new FixedMetadataValue(ProjectKorraMobs.plugin, SubElement.Metal.ordinal()));
									}
									break;
							}
							break;
						case Fire:
							switch (rand.nextInt(2)) {
								case 0:
									if (rand.nextInt(combustionFrequency) == 0) {
										entity.setMetadata("subelement", new FixedMetadataValue(ProjectKorraMobs.plugin, SubElement.Combustion.ordinal()));
									}
									break;
								case 1:
									if (rand.nextInt(lightningFrequency) == 0) {
										entity.setMetadata("subelement", new FixedMetadataValue(ProjectKorraMobs.plugin, SubElement.Lightning.ordinal()));
									}
									break;
							}
							break;
						case Water:
							if (rand.nextInt(iceFrequency) == 0) {
								entity.setMetadata("subelement", new FixedMetadataValue(ProjectKorraMobs.plugin, SubElement.Ice.ordinal()));
							}
							break;
						default:
							break;
					}
				}
			}
		}
	}

	/**
	 * Assigns an entity with provided element and subelement.
	 * @param entity
	 * @param element
	 * @param sub
	 */
	public static boolean assignElement(Entity entity, Element element, SubElement sub) {
		if (entity == null) return false;
		if (element != null) {
			entity.setMetadata("element", new FixedMetadataValue(ProjectKorraMobs.plugin, element.ordinal()));
			if (sub == null) return true;
		}
		if (sub != null && sub.getElement().equals(element)) {
			entity.setMetadata("subelement", new FixedMetadataValue(ProjectKorraMobs.plugin, sub.ordinal()));
			return true;
		}
		return false;
	}

	/**
	 * Spawns an entity with provided element and subelement.
	 * @param location
	 * @param type
	 * @param element
	 * @param sub
	 */
	public static boolean spawnEntity(Location location, EntityType type, Element element, SubElement sub) {
		if (LivingEntity.class.isAssignableFrom(type.getEntityClass())) {
			LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location, type);
			if (entity instanceof Ageable) {
				((Ageable) entity).setAdult();
			}
			if (element == null && sub == null) return true;
			return assignElement(entity, element, sub);
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
	 * Returns true if the entity has an element.
	 * @param entity
	 * @return
	 */
	public static boolean hasElement(LivingEntity entity) {
		if (entity.hasMetadata("element") && entity.getMetadata("element").size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the entity's element.
	 * @param entity
	 * @return
	 */
	public static Element getElement(LivingEntity entity) {
		if ((entity.hasMetadata("element") && entity.getMetadata("element").size() > 0)) {
			return Element.getType(entity.getMetadata("element").get(0).asInt());
		}
		return null;
	}

	/**
	 * Returns true if the entity has a subelement.
	 * @param entity
	 * @return
	 */
	public static boolean hasSubElement(LivingEntity entity) {
		if (entity.hasMetadata("subelement") && entity.getMetadata("subelement").size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the entity's subelement.
	 * @param entity
	 * @return
	 */
	public static SubElement getSubElement(LivingEntity entity) {
		if ((entity.hasMetadata("subelement") && entity.getMetadata("subelement").size() > 0)) {
			return SubElement.getType(entity.getMetadata("subelement").get(0).asInt());
		}
		return null;
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
		if (!hasElement(entity)) return false;
		if (GeneralMethods.hasRPG()) {
			if (FireAbility.isSolarEclipse(entity.getWorld()) && getElement(entity).isFirebender()) {
				return false;
			}
			if (WaterAbility.isLunarEclipse(entity.getWorld()) && getElement(entity).isWaterbender()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if a block is transparent.
	 * @param block
	 * @return
	 */
	public static boolean isTransparent(Block block) {
		if (!Arrays.asList(EarthAbility.getTransparentMaterial()).contains(block.getType())) {
			return false;
		}
		return true;
	}

	/**
	 * Returns a random block for an element or subelement.
	 * @param location
	 * @param radius
	 * @param element
	 * @param sub
	 * @return
	 */
	public static Block getRandomSourceBlock(Location location, int radius, Element element, SubElement sub) {
		List<Integer> checked = new ArrayList<Integer>();
		List<Block> blocks = GeneralMethods.getBlocksAroundPoint(location, radius);
		for (int i = 0; i < blocks.size(); i++) {
			int index = rand.nextInt(blocks.size());
			while (checked.contains(index)) {
				index = rand.nextInt(blocks.size());
			}
			checked.add(index);
			Block block = blocks.get(index);
			if (block == null || block.getLocation().distance(location) < 2) {
				continue;
			}
			if (sub != null) {
				switch (sub) {
					case Lava:
						if (ElementalAbility.isLava(block) && isTransparent(block.getRelative(BlockFace.UP))) {
							return block;
						}
					case Metal:
						if (ElementalAbility.isMetal(block) && isTransparent(block.getRelative(BlockFace.UP))) {
							return block;
						}
					case Ice:
						if (ElementalAbility.isIce(block) && isTransparent(block.getRelative(BlockFace.UP))) {
							return block;
						}
					default:
						break;
				}
			} else {
				switch (element) {
					case Earth:
						if (EarthAbility.isEarthbendable(block.getType()) && isTransparent(block.getRelative(BlockFace.UP))) {
							return block;
						}
					case Water:
						if (ElementalAbility.isWater(block) && isTransparent(block.getRelative(BlockFace.UP))) {
							return block;
						}
					default:
						break;
				}
			}
		}
		return null;
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
