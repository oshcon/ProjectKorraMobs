package com.jedk1.projectkorra.mobs;

import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.earthbending.EarthMethods;

import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MobMethods {
	
	public static List<String> disabledWorlds = new ArrayList<String>();

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
