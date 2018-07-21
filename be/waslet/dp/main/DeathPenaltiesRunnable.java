package be.waslet.dp.main;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathPenaltiesRunnable extends BukkitRunnable
{

	private static final int FOOD_LEVEL_MAX_VALUE = 20;
	private static double healthValue;
	private static double foodValue;
	private static double healthPercentage;
	private static double foodPercentage;
	private static PotionEffect[] potionEffects;
	private Player player;
	
	public DeathPenaltiesRunnable (Player player)
	{
		this.player = player;
	}
	
	@Override
	public void run ()
	{
		if (healthValue <= 0)
		{
			if (healthPercentage < 1) player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * healthPercentage);
		}
		else
		{
			player.setHealth(healthValue);
		}
		if (foodValue <= 0)
		{
			if (foodPercentage < 1) player.setFoodLevel((int) (FOOD_LEVEL_MAX_VALUE * foodPercentage));
		}
		else
		{
			player.setFoodLevel((int) foodValue);
		}
		for (PotionEffect potionEffect : potionEffects)
		{
			if (potionEffect != null)
			{
				player.addPotionEffect(potionEffect);
			}
		}
	}
	
	public static void updateValues (double healthFlat, double foodFlat, double health, double food, PotionEffect[] effects)
	{
		healthValue = healthFlat;
		foodValue = foodFlat;
		healthPercentage = health;
		foodPercentage = food;
		potionEffects = effects;
	}
	
}
