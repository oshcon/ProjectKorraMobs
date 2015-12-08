package com.jedk1.projectkorra.mobs.config;

import com.jedk1.projectkorra.mobs.ProjectKorraMobs;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

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

		config.addDefault("Properties.EntityTypes", new String[] {
				EntityType.ZOMBIE.toString(),
				EntityType.PIG_ZOMBIE.toString(),
				EntityType.VILLAGER.toString()
		});
		
		config.addDefault("Properties.BendFrequency", 150);
		config.addDefault("Properties.Air.NoFallDamage", true);
		config.addDefault("Properties.Avatar.Enabled", true);
		config.addDefault("Properties.Avatar.Frequency", 150);
		config.addDefault("Properties.Entity.Villager.FightBack", true);
		config.addDefault("Properties.SubElements.Enabled", true);
		config.addDefault("Properties.SubElements.Earth.Frequency", 10);
		config.addDefault("Properties.SubElements.Earth.Lava.Enabled", true);
		config.addDefault("Properties.SubElements.Earth.Lava.Frequency", 15);
		config.addDefault("Properties.SubElements.Earth.Metal.Enabled", true);
		config.addDefault("Properties.SubElements.Earth.Metal.Frequency", 10);
		config.addDefault("Properties.SubElements.Fire.Frequency", 10);
		config.addDefault("Properties.SubElements.Fire.Combustion.Enabled", true);
		config.addDefault("Properties.SubElements.Fire.Combustion.Frequency", 15);
		config.addDefault("Properties.SubElements.Fire.Lightning.Enabled", true);
		config.addDefault("Properties.SubElements.Fire.Lightning.Frequency", 10);
		config.addDefault("Properties.SubElements.Water.Frequency", 4);
		config.addDefault("Properties.SubElements.Water.Ice.Enabled", true);
		config.addDefault("Properties.SubElements.Water.Ice.Frequency", 4);
		
		config.addDefault("Abilities.Air.Enabled", true);
		config.addDefault("Abilities.Air.AirBlast.Knockback", 1.0);
		config.addDefault("Abilities.Air.AirScooter.Duration", 1000);
		config.addDefault("Abilities.Air.AirScooter.Speed", 0.7);
		
		config.addDefault("Abilities.Earth.Enabled", true);
		config.addDefault("Abilities.Earth.EarthBlast.Damage", 4.0);
		config.addDefault("Abilities.Earth.LavaBlast.Damage", 6.0);
		config.addDefault("Abilities.Earth.MetalBlast.Damage", 5.0);
		
		config.addDefault("Abilities.Fire.Enabled", true);
		config.addDefault("Abilities.Fire.FireBlast.Damage", 2.0);
		config.addDefault("Abilities.Fire.FireBlast.FireTick", 1000);
		config.addDefault("Abilities.Fire.FireJet.Duration", 1000);
		config.addDefault("Abilities.Fire.FireJet.Speed", 0.8);
		config.addDefault("Abilities.Fire.Combustion.Damage", 6.0);
		config.addDefault("Abilities.Fire.Combustion.FireTick", 2000);
		
		config.addDefault("Abilities.Water.Enabled", true);
		config.addDefault("Abilities.Water.WaterBlast.Damage", 3.0);
		config.addDefault("Abilities.Water.IceBlast.Damage", 4.0);
		
		config.options().copyDefaults(true);
		plugin.saveConfig();
	}
}
