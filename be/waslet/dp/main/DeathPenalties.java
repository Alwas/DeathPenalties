package be.waslet.dp.main;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DeathPenalties extends JavaPlugin implements Listener
{

	private boolean enabled;

	public void onEnable ()
	{
		saveDefaultConfig();
		enabled = getConfig().getBoolean("enabled");
		List<String> potionEffectsFormats = getConfig().getStringList("effects");
		PotionEffect[] potionEffects = new PotionEffect[potionEffectsFormats.size()];
		int position = 0;
		for (String effectFormat : potionEffectsFormats)
		{
			potionEffects[position++] = parsePotionEffect(effectFormat, getConfig().getBoolean("effectsDurationFormatInSeconds"), getConfig().getBoolean("effectsLevelFormatTrueLevel"));
		}
		DeathPenaltiesRunnable.updateValues(getConfig().getDouble("respawnHealthPercentage"), getConfig().getDouble("respawnFoodPercentage"), potionEffects);
		getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerRespawn (PlayerRespawnEvent event)
	{
		if (!enabled || event.getPlayer().hasPermission("dp.ignore")) return;
		new DeathPenaltiesRunnable(event.getPlayer()).runTaskLater(this, 20);
	}

	@Override
	public boolean onCommand (CommandSender sender, Command command, String label, String[] args)
	{
		if ((label.equalsIgnoreCase("dp") || label.equalsIgnoreCase("dphelp")) && sender.hasPermission("dp.commands.infos"))
		{
			sender.sendMessage("Available commands for DeathPenalties plugin are:\n"
					+ "/dphelp (Shows all the commands available)\n"
					+ "/dpstate (Shows if penalties are enabled or disabled)\n"
					+ "/dpenable (Enables death penalties)\n"
					+ "/dpdisable (Disables death penalties)\n"
					+ "/dptoggle (Changes the current state of death penalties)");
			return true;
		}
		if (label.equalsIgnoreCase("dpstate") && sender.hasPermission("dp.commands.infos"))
		{
			sender.sendMessage("Death penalties are currently " + ((enabled) ? "enabled" : "disabled"));
			return true;
		}
		if (label.equalsIgnoreCase("dpenable") && sender.hasPermission("dp.commands.toggle"))
		{
			if (enabled)
			{
				sender.sendMessage("Death penalties are already enabled !");
				return true;
			}
			setState(sender, true);
			return true;
		}
		if (label.equalsIgnoreCase("dpdisable") && sender.hasPermission("dp.commands.toggle"))
		{
			if (!enabled)
			{
				sender.sendMessage("Death penalties are already disabled !");
				return true;
			}
			setState(sender, false);
			return true;
		}
		if (label.equalsIgnoreCase("dptoggle") && sender.hasPermission("dp.commands.toggle"))
		{
			setState(sender, !enabled);
			return true;
		}
		return false;
	}

	private void setState (CommandSender sender, boolean state)
	{
		enabled = state;
		sender.sendMessage("Death penalties are now " + ((enabled) ? "enabled" : "disabled"));
		getConfig().set("enabled", state);
		saveConfig();
	}

	private PotionEffect parsePotionEffect (String potionEffectFormat, boolean durationInSeconds, boolean trueLevel)
	{
		String[] args = potionEffectFormat.split(":");
		if (args.length != 4)
		{
			getServer().getLogger().log(Level.WARNING, "[" + getName() + "] An effect line is not properly formatted: \"" + potionEffectFormat + "\". Please check it");
			return null;
		}
		PotionEffectType potionEffect = PotionEffectType.getByName(args[0]);
		if (potionEffect == null)
		{
			getServer().getLogger().log(Level.WARNING, "[" + getName() + "] Effect " + args[0] + " does not exist. Please check config");
			return null;
		}
		try
		{
			return new PotionEffect(potionEffect, ((durationInSeconds) ? Integer.parseInt(args[1]) * 20 : Integer.parseInt(args[1])), ((trueLevel) ? Integer.parseInt(args[2]) - 1 : Integer.parseInt(args[2])), false, Boolean.getBoolean(args[3]));
		}
		catch (NumberFormatException e)
		{
			getServer().getLogger().log(Level.WARNING, "[" + getName() + "] An effect line is not properly formatted: " + potionEffectFormat);
			return null;
		}
	}

}
