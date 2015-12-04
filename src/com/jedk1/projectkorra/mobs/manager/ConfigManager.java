package com.jedk1.projectkorra.mobs.manager;

import com.jedk1.projectkorra.mobs.ProjectKorraMobs;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
	
	ProjectKorraMobs plugin;

	public ConfigManager(ProjectKorraMobs plugin) {
		this.plugin = plugin;
		init();
	}

	private void init() {
		FileConfiguration config;
		config = plugin.getConfig();

		config.addDefault("Version", 1);

		config.addDefault("Properties.BendFrequency", 150);
		config.addDefault("Abilities.Air.Enabled", true);
		config.addDefault("Abilities.Air.AirBlast.Knockback", 1.0);
		config.addDefault("Abilities.Fire.Enabled", true);
		config.addDefault("Abilities.Fire.FireBlast.Damage", 2.0);
		config.addDefault("Abilities.Fire.FireBlast.FireTick", 1000);
		config.addDefault("Abilities.Water.Enabled", true);
		config.addDefault("Abilities.Water.WaterBlast.Damage", 3.0);
		config.addDefault("Abilities.Earth.Enabled", true);
		config.addDefault("Abilities.Earth.EarthBlast.Damage", 4.0);
		
		config.options().copyDefaults(true);
		plugin.saveConfig();
	}
}
