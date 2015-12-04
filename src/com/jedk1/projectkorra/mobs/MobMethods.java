package com.jedk1.projectkorra.mobs;

import com.projectkorra.projectkorra.earthbending.EarthMethods;

import org.bukkit.block.Block;

import java.util.Arrays;

public class MobMethods {

	@SuppressWarnings("deprecation")
	public static boolean isTransparent(Block block) {
		if (!Arrays.asList(EarthMethods.transparentToEarthbending).contains(block.getTypeId())) {
			return false;
		}
		return true;
	}
}
