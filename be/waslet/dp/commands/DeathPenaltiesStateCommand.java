package be.waslet.dp.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import be.waslet.dp.main.DeathPenalties;
import be.waslet.dp.main.DeathPenaltiesWorld;

public class DeathPenaltiesStateCommand implements CommandExecutor
{

	private DeathPenalties plugin;
	
	public DeathPenaltiesStateCommand (DeathPenalties plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand (CommandSender sender, Command command, String label, String[] args)
	{
		// player sender without specific world (use current player world)
		if (sender instanceof Player && args.length == 0)
		{
			String worldName = ((Player) sender).getWorld().getName();
			sender.sendMessage(getStateMessage(this.plugin.getDeathPenaltiesWorld(worldName), worldName));
		}
		// console sender without specific world (break)
		else if (args.length == 0) return false;
		// player or console sender with specific world
		else
		{
			World senderWorld = sender.getServer().getWorld(args[0]);
			// see all worlds state
			if (args[0].equalsIgnoreCase("all")) for (World world : sender.getServer().getWorlds()) sender.sendMessage(getStateMessage(this.plugin.getDeathPenaltiesWorld(world.getName()), world.getName()));
			// specific world does not exist
			else if (senderWorld == null) sender.sendMessage(ChatColor.RED + "The world named \"" + args[0] + "\" does not exist");
			// send specific world state
			else sender.sendMessage(getStateMessage(this.plugin.getDeathPenaltiesWorld(senderWorld.getName()), senderWorld.getName()));
		}
		return true;
	}

	private static String getStateMessage (DeathPenaltiesWorld world, String worldName)
	{
		StringBuilder buffer = new StringBuilder();
		buffer.append(ChatColor.GREEN).append("--------------------\n");
		buffer.append("Death penalties are currently ").append(ChatColor.DARK_GREEN).append(((world.isEnabled()) ? "enabled" : "disabled")).append(ChatColor.GREEN).append(" in world: ").append(ChatColor.YELLOW).append(worldName).append(ChatColor.GREEN).append("\n");
		buffer.append("--------------------\n");
		if (world.isEnabled())
		{
			buffer.append("Players penalties there are:\n");
			
			if (world.getRespawnHealthFlat() > 0) buffer.append("- ").append(ChatColor.YELLOW).append(world.getRespawnHealthFlat()).append(ChatColor.GREEN).append(" health (flat)").append("\n");
			else buffer.append("- ").append(ChatColor.YELLOW).append(world.getRespawnHealthPercentage()).append(ChatColor.GREEN).append(" health (percentage)").append("\n");
			if (world.getRespawnFoodFlat() > 0) buffer.append("- ").append(ChatColor.YELLOW).append(world.getRespawnFoodFlat()).append(ChatColor.GREEN).append(" food (flat)").append("\n");
			else buffer.append("- ").append(ChatColor.YELLOW).append(world.getRespawnFoodPercentage()).append(ChatColor.GREEN).append(" food (percentage)").append("\n");
			if (world.getDeathMoneyLostFlat() > 0) buffer.append("- ").append(ChatColor.YELLOW).append(world.getDeathMoneyLostFlat()).append(ChatColor.GREEN).append(" money lost (flat)").append("\n");
			else buffer.append("- ").append(ChatColor.YELLOW).append(world.getDeathMoneyLostPercentage()).append(ChatColor.GREEN).append(" money lost (percentage)").append("\n");
			if (world.getDeathItemsDroppedFlat() > 0) buffer.append("- ").append(ChatColor.YELLOW).append(world.getDeathItemsDroppedFlat()).append(ChatColor.GREEN).append(" items dropped (flat)").append("\n");
			else buffer.append("- ").append(ChatColor.YELLOW).append(world.getDeathItemsDroppedPercentage()).append(ChatColor.GREEN).append(" items dropped (percentage)").append("\n");
			buffer.append("- ").append(ChatColor.YELLOW).append(world.getDeathItemsDroppedChancePercentage()).append(ChatColor.GREEN).append(" items dropped chance (percentage)\n");
			if (world.getDeathItemsDestroyedFlat() > 0) buffer.append("- ").append(ChatColor.YELLOW).append(world.getDeathItemsDestroyedFlat()).append(ChatColor.GREEN).append(" items destroyed (flat)").append("\n");
			else buffer.append("- ").append(ChatColor.YELLOW).append(world.getDeathItemsDestroyedPercentage()).append(ChatColor.GREEN).append(" items destroyed (percentage)").append("\n");
			buffer.append("- ").append(ChatColor.YELLOW).append(world.getDeathItemsDestroyedChancePercentage()).append(ChatColor.GREEN).append(" items destroyed chance (percentage)\n");
			if (world.getDeathExperienceDroppedFlat() > 0) buffer.append("- ").append(ChatColor.YELLOW).append(world.getDeathExperienceDroppedFlat()).append(ChatColor.GREEN).append(" experience dropped (flat)").append("\n");
			else buffer.append("- ").append(ChatColor.YELLOW).append(world.getDeathExperienceDroppedPercentage()).append(ChatColor.GREEN).append(" experience dropped (percentage)").append("\n");
			buffer.append("- ").append(ChatColor.YELLOW).append(world.getDeathExperienceDroppedChancePercentage()).append(ChatColor.GREEN).append(" experience dropped chance (percentage)\n");
			if (world.getDeathExperienceDestroyedFlat() > 0) buffer.append("- ").append(ChatColor.YELLOW).append(world.getDeathExperienceDestroyedFlat()).append(ChatColor.GREEN).append(" experience destroyed (flat)").append("\n");
			else buffer.append("- ").append(ChatColor.YELLOW).append(world.getDeathExperienceDestroyedPercentage()).append(ChatColor.GREEN).append(" experience destroyed (percentage)").append("\n");
			buffer.append("- ").append(ChatColor.YELLOW).append(world.getDeathExperienceDestroyedChancePercentage()).append(ChatColor.GREEN).append(" experience destroyed chance (percentage)\n");
			if (world.getDeathLevelsDestroyedFlat() > 0) buffer.append("- ").append(ChatColor.YELLOW).append(world.getDeathLevelsDestroyedFlat()).append(ChatColor.GREEN).append(" levels destroyed (flat)").append("\n");
			else buffer.append("- ").append(ChatColor.YELLOW).append(world.getDeathLevelsDestroyedPercentage()).append(ChatColor.GREEN).append(" levels destroyed (percentage)").append("\n");
			buffer.append("- ").append(ChatColor.YELLOW).append(world.getDeathLevelsDestroyedChancePercentage()).append(ChatColor.GREEN).append(" levels destroyed chance (percentage)\n");
			buffer.append("- Whitelisted items:\n");
			if (world.getWhitelistedItems().length == 0) buffer.append("- No items whitelisted\n");
			else for (Material material : world.getWhitelistedItems()) buffer.append("- ").append(material).append("\n");
			buffer.append("- Processed commands on respawn:\n");
			if (world.getRespawnProcessedCommands().length == 0) buffer.append("- No commands\n");
			else for (String command : world.getRespawnProcessedCommands()) buffer.append("- ").append(command).append("\n");
			buffer.append("- Processed commands on death:\n");
			if (world.getDeathProcessedCommands().length == 0) buffer.append("- No commands\n");
			else for (String command : world.getDeathProcessedCommands()) buffer.append("- ").append(command).append("\n");
		}
		return buffer.toString();
	}

}
