package com.jedk1.projectkorra.mobs;

import com.jedk1.projectkorra.mobs.ability.AirBlast;
import com.jedk1.projectkorra.mobs.ability.EarthBlast;
import com.jedk1.projectkorra.mobs.ability.FireBlast;
import com.jedk1.projectkorra.mobs.ability.WaterBlast;
import com.jedk1.projectkorra.mobs.listener.MobListener;
import com.jedk1.projectkorra.mobs.manager.AbilityManager;
import com.jedk1.projectkorra.mobs.manager.ConfigManager;
import com.jedk1.projectkorra.mobs.manager.EntityManager;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class ProjectKorraMobs extends JavaPlugin {

	public static ProjectKorraMobs plugin;
	public static Logger log;
	
	@Override
	public void onEnable(){
		plugin = this;
		ProjectKorraMobs.log = this.getLogger();
		
		new ConfigManager(this);
		getServer().getPluginManager().registerEvents(new MobListener(this), this);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new AbilityManager(),0L, 1L);
	}
	
	@Override
	public void onDisable(){
		FireBlast.removeAll();
		AirBlast.removeAll();
		WaterBlast.removeAll();
		EarthBlast.removeAll();
		EntityManager.remove();
	}
}
