package be.waslet.dp.commands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

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
		String enabled = "Death penalties are currently " + ChatColor.DARK_GREEN + ((world.isEnabled()) ? "enabled" : "disabled") + ChatColor.GREEN + " in world: " + ChatColor.YELLOW + worldName + ChatColor.GREEN;
		if (!world.isEnabled()) return ChatColor.GREEN + "--------------------\n" + enabled + "\n--------------------";
		String respawningHealth = "- " + ((world.getRespawnHealthFlat() > 0) ? ChatColor.YELLOW + "" + world.getRespawnHealthFlat() + ChatColor.GREEN + " health (flat)" : ChatColor.YELLOW + "" + world.getRespawnHealthPercentage() + ChatColor.GREEN + " health (percentage)");
		String respawningFood = "- " + ((world.getRespawnFoodFlat() > 0) ? ChatColor.YELLOW + "" + world.getRespawnFoodFlat() + ChatColor.GREEN + " food (flat)" : ChatColor.YELLOW + "" + world.getRespawnFoodPercentage() + ChatColor.GREEN + " food (percentage)");
		String respawningMoneyLost = "- " + ((world.getRespawnMoneyLostFlat() > 0) ? ChatColor.YELLOW + "" + world.getRespawnMoneyLostFlat() + ChatColor.GREEN + " money lost (flat)" : ChatColor.YELLOW + "" + world.getRespawnMoneyLostPercentage() + ChatColor.GREEN + " money lost (percentage)");
		String respawningItemsLost = "- " + ((world.getRespawnItemsLostFlat() > 0) ? ChatColor.YELLOW + "" + world.getRespawnItemsLostFlat() + ChatColor.GREEN + " items lost (flat)" : ChatColor.YELLOW + "" + world.getRespawnItemsLostPercentage() + ChatColor.GREEN + " items lost (percentage)");
		String respawningEffects = "";
		if (world.getRespawnEffects() == null || world.getRespawnEffects().length == 0) respawningEffects = respawningEffects.concat("- No effects");
		else
		{
			StringBuffer buffer = new StringBuffer();
			for (PotionEffect effect : world.getRespawnEffects()) buffer.append("- "+getPotionEffectMessage(effect) + "\n");
			respawningEffects = buffer.toString();
		}
		return ChatColor.GREEN + "--------------------\n" + enabled + "\n--------------------\n" + "Players respawn here with:\n" + respawningHealth + "\n" + respawningFood + "\n" + respawningMoneyLost + "\n" + respawningItemsLost + "\n" + respawningEffects;
	}

	private String getPotionEffectMessage (PotionEffect effect)
	{
		return ChatColor.YELLOW + effect.getType().getName() + ChatColor.GREEN + " during " + ChatColor.YELLOW + effect.getDuration() / 20 + ChatColor.GREEN + " seconds (level " + (effect.getAmplifier() + 10) / 10 + ")";
	}

}
