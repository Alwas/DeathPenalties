package be.waslet.dp.main;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
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
		if (world.getRespawnHealthFlat() <= 0)
		{
			if (world.getRespawnHealthPercentage() <= 1 && world.getRespawnHealthPercentage() > 0) player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * world.getRespawnHealthPercentage());
		}
		// else updating player with flat value
		else player.setHealth(world.getRespawnHealthFlat());
		// same for food
		if (world.getRespawnFoodFlat() <= 0)
		{
			if (world.getRespawnFoodPercentage() <= 1 && world.getRespawnFoodPercentage() > 0) player.setFoodLevel((int) (FOOD_LEVEL_MAX_VALUE * world.getRespawnFoodPercentage()));
		}
		else player.setFoodLevel(world.getRespawnFoodFlat());
		// apply all potions effects
		if (world.getRespawnEffects() != null)
		for (PotionEffect potionEffect : world.getRespawnEffects()) if (potionEffect != null) player.addPotionEffect(potionEffect);
	}
	
}
