package be.waslet.dp.main;

import org.bukkit.potion.PotionEffect;

public class DeathPenaltiesWorld
{

	private boolean enabled;
	private double respawnHealthFlat;
	private int respawnFoodFlat;
	private double respawnHealthPercentage;
	private double respawnFoodPercentage;
	private PotionEffect[] respawnEffects;

	public DeathPenaltiesWorld (boolean enabled, double respawnHealthFlat, int respawnFoodFlat, double respawnHealthPercentage, double respawnFoodPercentage, PotionEffect[] respawnEffects)
	{
		this.enabled = enabled;
		this.respawnHealthFlat = respawnHealthFlat;
		this.respawnFoodFlat = respawnFoodFlat;
		this.respawnHealthPercentage = respawnHealthPercentage;
		this.respawnFoodPercentage = respawnFoodPercentage;
		this.respawnEffects = respawnEffects;
	}

	public boolean isEnabled ()
	{
		return enabled;
	}

	public double getRespawnHealthFlat ()
	{
		return respawnHealthFlat;
	}

	public int getRespawnFoodFlat ()
	{
		return respawnFoodFlat;
	}

	public double getRespawnHealthPercentage ()
	{
		return respawnHealthPercentage;
	}

	public double getRespawnFoodPercentage ()
	{
		return respawnFoodPercentage;
	}

	public PotionEffect[] getRespawnEffects ()
	{
		return respawnEffects;
	}

	public void setEnabled (String worldName, boolean enabled)
	{
		this.enabled = enabled;
	}

	public void setRespawnHealthFlat (double respawnHealthFlat)
	{
		this.respawnHealthFlat = respawnHealthFlat;
	}

	public void setRespawnFoodFlat (int respawnFoodFlat)
	{
		this.respawnFoodFlat = respawnFoodFlat;
	}

	public void setRespawnHealthPercentage (double respawnHealthPercentage)
	{
		this.respawnHealthPercentage = respawnHealthPercentage;
	}

	public void setRespawnFoodPercentage (double respawnFoodPercentage)
	{
		this.respawnFoodPercentage = respawnFoodPercentage;
	}

	public void setRespawnEffects (PotionEffect[] respawnEffects)
	{
		this.respawnEffects = respawnEffects;
	}
	
	public DeathPenaltiesWorld getCopy ()
	{
		return new DeathPenaltiesWorld(this.enabled, this.respawnHealthFlat, this.respawnFoodFlat, this.respawnHealthPercentage, this.respawnFoodPercentage, this.respawnEffects);
	}

}
