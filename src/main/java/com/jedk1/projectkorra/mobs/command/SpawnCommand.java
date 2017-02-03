package com.jedk1.projectkorra.mobs.command;

import com.jedk1.projectkorra.mobs.MobMethods;
import com.jedk1.projectkorra.mobs.object.Element;
import com.jedk1.projectkorra.mobs.object.SubElement;
import com.projectkorra.projectkorra.GeneralMethods;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.List;

public class SpawnCommand extends MobCommand {

	public SpawnCommand() {
		super("spawn", "/projectkorramobs spawn [amount] <type> <element/subelement> [<x> <y> <z> [world]]", "Spawns a mob with an element/subelement.", new String[]{"spawn", "s"});
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!hasPermission(sender) || !correctLength(sender, args.size(), 1, 7)) {
			return;
		}
		
		int argoffset = 0;
		int amount = 1;
		if (isNumeric(args.get(0))) {
			if (args.size() == 1) {
				help(sender, false);
				return;
			}
			argoffset++;
			amount = Integer.parseInt(args.get(0));
		}
		
		if (args.size() < 5 + argoffset && !isPlayer(sender)) {
			return;
		}

		Location location = null;
		Element element = null;
		SubElement sub = null;
		EntityType type = EntityType.fromName(args.get(0 + argoffset));
		
		if (type == null) {
			invalid(sender, "Invalid Entity Type!");
			return;
		}

		if (!MobMethods.canEntityBend(type)) {
			invalid(sender, "That Entity Type can't bend!");
			return;
		}
		
		if (args.size() >= 1 + argoffset && isPlayer(sender)) {
			location = GeneralMethods.getTargetedLocation((Player) sender, 15);
			if (location.getBlock() == null) {
				invalid(sender, "Can't spawn an entity there!");
				return;
			}
		}
		
		if (args.size() >= 2 + argoffset) {
			element = Element.getType(args.get(1 + argoffset));
			sub = SubElement.getType(args.get(1 + argoffset));
			if (sub == null && element == null) {
				invalid(sender, "Invalid Element/SubElement!");
				return;
			}
		}

		if (args.size() >= 5 + argoffset) {
			World world = null;
			double x = 0;
			double y = 0;
			double z = 0;
			
			Location temp = null;
			if (sender instanceof BlockCommandSender) {
				temp = ((BlockCommandSender) sender).getBlock().getLocation();
			} else if (sender instanceof Player) {
				temp = ((Player) sender).getLocation();
			} else {
				help(sender, false);
				return;
			}
			
			if (args.size() < 6 + argoffset) {
				world = temp.getWorld();
				if (args.get(2 + argoffset).contains("~")) x = temp.getBlockX() + 0.5;
				if (!args.get(2 + argoffset).equalsIgnoreCase("~")) x = x + Double.parseDouble(args.get(2 + argoffset).replace("~", ""));
				if (args.get(3 + argoffset).contains("~")) y = temp.getY();
				if (!args.get(3 + argoffset).equalsIgnoreCase("~")) y = y + Double.parseDouble(args.get(3 + argoffset).replace("~", ""));
				if (args.get(4 + argoffset).contains("~")) z = temp.getBlockZ() + 0.5;
				if (!args.get(4 + argoffset).equalsIgnoreCase("~")) z = z + Double.parseDouble(args.get(4 + argoffset).replace("~", ""));
				location = new Location(world, x, y, z);
			} else if (args.size() >= 6) {
				world = Bukkit.getWorld(args.get(5 + argoffset));
				location = new Location(world, Double.parseDouble(args.get(2 + argoffset).replace("~", "")), Double.parseDouble(args.get(3 + argoffset).replace("~", "")), Double.parseDouble(args.get(4 + argoffset).replace("~", "")));
			}

			if (world == null) {
				help(sender, false);
				return;
			}
		}

		if (amount > 10) {
			amount = 10;
		}

		boolean fail = false;
		for (int a = 0; a < amount; a++) {
			if (sub != null) {
				if (!MobMethods.spawnEntity(location, type, sub.getElement(), sub)) {
					fail = true;
					break;
				}
			} else if (element != null) {
				if (!MobMethods.spawnEntity(location, type, element, null)) {
					fail = true;
					break;
				}
			} else {
				if (!MobMethods.spawnEntity(location, type, null, null)) {
					fail = true;
					break;
				}
			}
		}

		if (!fail) {
			StringBuilder sb = new StringBuilder();
			sb.append("Spawned");
			sb.append(" ");
			sb.append(amount);
			sb.append(" ");
			if (sub != null) {
				sb.append(sub.name());
			} else if (element != null){
				sb.append(element.name());
			} else {
				sb.append("Random");
			}
			sb.append(" ");
			sb.append(type.getName());
			if (amount > 1) {
				sb.append("s");
			}
			sb.append("!");
			sender.sendMessage(sb.toString());
		}
	}
}