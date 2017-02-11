package com.jedk1.projectkorra.mobs.command;

import com.projectkorra.projectkorra.command.PKCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Interface representation of a command executor.
 */
public interface SubCommand {
	/**
	 * Gets the name of the command.
	 * 
	 * @return The command's name
	 */
    String getName();

	/**
	 * Gets the aliases for the command.
	 * 
	 * @return All aliases for the command
	 */
    String[] getAliases();

	/**
	 * Gets the proper use of the command, in the format '/b
	 * {@link PKCommand#name name} arg1 arg2 ... '
	 * 
	 * @return the proper use of the command
	 */
    String getProperUse();

	/**
	 * Gets the description of the command.
	 * 
	 * @return the description
	 */
    String getDescription();

	/**
	 * Outputs the correct usage, and optionally the description, of a command
	 * to the given {@link CommandSender}.
	 * 
	 * @param sender The CommandSender to output the help to
	 * @param description Whether or not to output the description of the
	 *            command
	 */
    void help(CommandSender sender, boolean description);

	/**
	 * Executes the command.
	 * 
	 * @param sender The CommandSender who issued the command
	 * @param args the command's arguments
	 */
    void execute(CommandSender sender, List<String> args);
}
