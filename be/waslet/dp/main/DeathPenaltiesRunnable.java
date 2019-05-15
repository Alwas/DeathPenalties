package be.waslet.dp.main;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathPenaltiesRunnable extends BukkitRunnable
{

	private static final int FOOD_LEVEL_MAX_VALUE = 20;
	private DeathPenaltiesWorld world;
	private Player player;
	
	/**
	 * Creates a new BukkitRunnable that applies death penalties when runned
	 * @param player Respawning player death penalties should be applied to
	 * @param respawnHealthValue Flat health value that player will have after respawn (0 or less for percentages use)
	 * @param respawnFoodValue Flat food value that player will have after respawn (0 or less for percentages use)
	 * @param respawnHealthPercentage Health value that player will have after respawn in percentage (ignored if flat value > 0)
	 * @param respawnFoodPercentage Food value that player will have after respawn in percentage (ignored if flat value > 0)
	 * @param respawnEffects Array of effects to apply to player after respawn
	 */
	public DeathPenaltiesRunnable (Player player, DeathPenaltiesWorld world)
	{
		this.player = player;
		this.world = world;
	}
	
	@Override
	public void run ()
	{
		// if flat value is disabled use percentage and only set value if we have a valid percentage
		if (this.world.getRespawnHealthFlat() <= 0)
		{
			if (this.world.getRespawnHealthPercentage() <= 1 && this.world.getRespawnHealthPercentage() > 0) this.player.setHealth(this.player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * this.world.getRespawnHealthPercentage());
		}
		// else updating player with flat value
		else this.player.setHealth(this.world.getRespawnHealthFlat());
		// same for food
		if (this.world.getRespawnFoodFlat() <= 0)
		{
			if (this.world.getRespawnFoodPercentage() <= 1 && this.world.getRespawnFoodPercentage() > 0) this.player.setFoodLevel((int) (FOOD_LEVEL_MAX_VALUE * this.world.getRespawnFoodPercentage()));
		}
		else this.player.setFoodLevel(this.world.getRespawnFoodFlat());
		// process commands at respawn
		for (String command : this.world.getRespawnProcessedCommands())
		{
			String[] data = command.split(";");
			if (data.length < 2) continue;
			if (data[0].equalsIgnoreCase("server")) this.player.getServer().dispatchCommand(this.player.getServer().getConsoleSender(), data[1].replace("%player%", this.player.getName()));
			else if (data[0].equalsIgnoreCase("player")) this.player.performCommand(data[1].replace("%player%", this.player.getName()));
		}
	}
	
}
