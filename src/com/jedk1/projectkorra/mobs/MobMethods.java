package com.jedk1.projectkorra.mobs;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.earthbending.EarthMethods;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MobMethods {
	
	private static boolean avatar = ProjectKorraMobs.plugin.getConfig().getBoolean("Properties.Avatar.Enabled");
	
	public static List<String> disabledWorlds = new ArrayList<String>();

	public static void assignElement(Entity entity) {
		int i;
		if (avatar) {
			i = GeneralMethods.rand.nextInt(5);
		} else {
			i = GeneralMethods.rand.nextInt(4);
		}
		if (!entity.hasMetadata("element"))
			entity.setMetadata("element", new FixedMetadataValue(ProjectKorra.plugin, i));
	}
	
	@SuppressWarnings("deprecation")
	public static boolean isTransparent(Block block) {
		if (!Arrays.asList(EarthMethods.transparentToEarthbending).contains(block.getTypeId())) {
			return false;
		}
		return true;
	}
	
	public static boolean isDisabledWorld(World world) {
		if (disabledWorlds.contains(world.getName())) {
			return true;
		}
		return false;
	}
	
	public static void registerDisabledWorlds() {
		disabledWorlds.clear();
		if (ProjectKorra.plugin.getConfig().getStringList("Properties.DisabledWorlds") != null) {
			for (String s : ProjectKorra.plugin.getConfig().getStringList("Properties.DisabledWorlds")) {
				disabledWorlds.add(s);
			}
		}
	}
}
