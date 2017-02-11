package com.jedk1.projectkorra.mobs.command;

import com.jedk1.projectkorra.mobs.ProjectKorraMobs;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import java.util.Arrays;
import java.util.List;

public class Commands {

	private ProjectKorraMobs plugin;

	public Commands(ProjectKorraMobs plugin) {
		this.plugin = plugin;
		init();
	}

	private void init() {
		PluginCommand pkm = plugin.getCommand("projectkorramobs");
		new SpawnCommand();

		// Set of all of the Classes which extend Command

		CommandExecutor exe;

		exe = new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
				for (int i = 0; i < args.length; i++) {
					args[i] = args[i];
				}

				if (args.length == 0) {
					s.sendMessage(ChatColor.GOLD + "ProjectKorra Mobs");
					s.sendMessage(ChatColor.GOLD + "Usage: " + ChatColor.DARK_AQUA + "/projectkorramobs spawn [amount] <type> [element/subelement] [<x> <y> <z> [world]]");
					s.sendMessage(ChatColor.GOLD + "Version: " + ChatColor.DARK_AQUA + plugin.getDescription().getVersion());
					return true;
				}

				List<String> sendingArgs = Arrays.asList(args).subList(1, args.length);
				for (MobCommand command : MobCommand.instances.values()) {
					if (Arrays.asList(command.getAliases()).contains(args[0].toLowerCase())) {
						command.execute(s, sendingArgs);
						return true;
					}
				}
				return true;
			}
		};
		pkm.setExecutor(exe);
	}
}
