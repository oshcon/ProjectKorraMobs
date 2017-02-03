package com.jedk1.projectkorra.mobs;

import com.jedk1.projectkorra.mobs.ability.earth.EarthAbility;
import com.jedk1.projectkorra.mobs.ability.fire.FireAbility;
import com.jedk1.projectkorra.mobs.ability.air.AirAbility;
import com.jedk1.projectkorra.mobs.ability.water.WaterAbility;
import com.jedk1.projectkorra.mobs.command.Commands;
import com.jedk1.projectkorra.mobs.config.ConfigManager;
import com.jedk1.projectkorra.mobs.listener.MobListener;
import com.jedk1.projectkorra.mobs.manager.AbilityManager;
import com.jedk1.projectkorra.mobs.manager.EntityManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

public class ProjectKorraMobs extends JavaPlugin {

	public static ProjectKorraMobs plugin;
	public static Logger log;
	
	@Override
	public void onEnable(){
		plugin = this;
		ProjectKorraMobs.log = this.getLogger();

		Compatibility.checkHooks();
		
		new ConfigManager(this);
		new Commands(this);
		MobMethods.registerDisabledWorlds();
		MobMethods.registerEntityTypes();
		MobMethods.registerDisguisableEntities();
		getServer().getPluginManager().registerEvents(new MobListener(this), this);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new AbilityManager(),0L, 1L);
	}
	
	@Override
	public void onDisable(){
		AirAbility.remove();
		EarthAbility.remove();
		FireAbility.remove();
		WaterAbility.remove();
		EntityManager.remove();
	}

	public static void log(String message) {
		ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
		message = "&8[&b" + plugin.getName() + "&8]&r " + message;
		message = addColor(message);
		console.sendMessage(message);
	}

	public static String addColor(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static void printError(String warning, Throwable ex) {
		log("&8[&b" + plugin.getName() + "&8]&r " + "&eERROR: &c" + warning);
		log("&8[&b" + plugin.getName() + "&8]&r " + " ");
		log("&8[&b" + plugin.getName() + "&8]&r " + "                    &c======= Copy and Paste =======");
		log("&8[&b" + plugin.getName() + "&8]&r " + " ");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);

		for (String l : sw.toString().replace("\r", "").split("\n")) {
			log("&8[&b" + plugin.getName() + "&8]&r " + l);
			pw.close();

			// Ex-in-ception!
			try {
				sw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		log("&8[&b" + plugin.getName() + "&8]&r " + " ");
		log("&8[&b" + plugin.getName() + "&8]&r " + "                    &c======= Copy and Paste =======");
		log("&8[&b" + plugin.getName() + "&8]&r " + " ");
	}
}
