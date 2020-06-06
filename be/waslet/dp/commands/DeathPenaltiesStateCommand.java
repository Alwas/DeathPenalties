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
			sender.sendMessage(getStateMessage(plugin.getDeathPenaltiesWorld(worldName), worldName));
		}
		// console sender without specific world (break)
		else if (args.length == 0) return false;
		// player or console sender with specific world
		else
		{
			World senderWorld = sender.getServer().getWorld(args[0]);
			// see all worlds state
			if (args[0].equalsIgnoreCase("all")) for (World world : sender.getServer().getWorlds()) sender.sendMessage(getStateMessage(plugin.getDeathPenaltiesWorld(world.getName()), world.getName()));
			// specific world does not exist
			else if (senderWorld == null) sender.sendMessage(ChatColor.RED + "The world named \"" + args[0] + "\" does not exist");
			// send specific world state
			else sender.sendMessage(getStateMessage(plugin.getDeathPenaltiesWorld(senderWorld.getName()), senderWorld.getName()));
		}
		return true;
	}

	private String getStateMessage (DeathPenaltiesWorld world, String worldName)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(ChatColor.GREEN).append("--------------------\n");
		buffer.append("Death penalties are currently ").append(ChatColor.DARK_GREEN).append(((world.isEnabled()) ? "enabled" : "disabled")).append(ChatColor.GREEN).append(" in world: ").append(ChatColor.YELLOW).append(worldName).append(ChatColor.GREEN).append("\n");
		buffer.append("--------------------\n");
		if (world.isEnabled())
		{
			buffer.append("Players penalties there are:\n");
			buffer.append("- ").append(((world.getRespawnHealthFlat() > 0) ? ChatColor.YELLOW + "" + world.getRespawnHealthFlat() + ChatColor.GREEN + " health (flat)" : ChatColor.YELLOW + "" + world.getRespawnHealthPercentage() + ChatColor.GREEN + " health (percentage)") + "\n");
			buffer.append("- ").append(((world.getRespawnFoodFlat() > 0) ? ChatColor.YELLOW + "" + world.getRespawnFoodFlat() + ChatColor.GREEN + " food (flat)" : ChatColor.YELLOW + "" + world.getRespawnFoodPercentage() + ChatColor.GREEN + " food (percentage)") + "\n");
			buffer.append("- ").append(((world.getDeathMoneyLostFlat() > 0) ? ChatColor.YELLOW + "" + world.getDeathMoneyLostFlat() + ChatColor.GREEN + " money lost (flat)" : ChatColor.YELLOW + "" + world.getDeathMoneyLostPercentage() + ChatColor.GREEN + " money lost (percentage)") + "\n");
			buffer.append("- ").append(((world.getDeathItemsDroppedFlat() > 0) ? ChatColor.YELLOW + "" + world.getDeathItemsDroppedFlat() + ChatColor.GREEN + " items dropped (flat)" : ChatColor.YELLOW + "" + world.getDeathItemsDroppedPercentage() + ChatColor.GREEN + " items dropped (percentage)") + "\n");
			buffer.append("- ").append(ChatColor.YELLOW + "" + world.getDeathItemsDroppedChancePercentage() + ChatColor.GREEN + " items dropped chance (percentage)\n");
			buffer.append("- ").append(((world.getDeathItemsDestroyedFlat() > 0) ? ChatColor.YELLOW + "" + world.getDeathItemsDestroyedFlat() + ChatColor.GREEN + " items destroyed (flat)" : ChatColor.YELLOW + "" + world.getDeathItemsDestroyedPercentage() + ChatColor.GREEN + " items destroyed (percentage)") + "\n");
			buffer.append("- ").append(ChatColor.YELLOW + "" + world.getDeathItemsDestroyedChancePercentage() + ChatColor.GREEN + " items destroyed chance (percentage)\n");
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
