package be.waslet.dp.main;

import org.bukkit.potion.PotionEffect;

public class DeathPenaltiesWorld
{

	private boolean enabled;
	private double respawnHealthFlat;
	private int respawnFoodFlat;
	private double respawnMoneyLostFlat;
	private int respawnItemsLostFlat;
	private double respawnHealthPercentage;
	private double respawnFoodPercentage;
	private double respawnMoneyLostPercentage;
	private double respawnItemsLostPercentage;
	private PotionEffect[] respawnEffects;

	public DeathPenaltiesWorld (boolean enabled, double respawnHealthFlat, int respawnFoodFlat, double respawnMoneyLostFlat, int respawnItemsLostFlat, double respawnHealthPercentage, double respawnFoodPercentage, double respawnMoneyLostPercentage, double respawnItemsLostPercentage, PotionEffect[] respawnEffects)
	{
		this.enabled = enabled;
		this.respawnHealthFlat = respawnHealthFlat;
		this.respawnFoodFlat = respawnFoodFlat;
		this.respawnMoneyLostFlat = respawnMoneyLostFlat;
		this.respawnItemsLostFlat = respawnItemsLostFlat;
		this.respawnHealthPercentage = respawnHealthPercentage;
		this.respawnFoodPercentage = respawnFoodPercentage;
		this.respawnMoneyLostPercentage = respawnMoneyLostPercentage;
		this.respawnItemsLostPercentage = respawnItemsLostPercentage;
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

	public double getRespawnMoneyLostFlat ()
	{
		return respawnMoneyLostFlat;
	}
	
	public int getRespawnItemsLostFlat ()
	{
		return respawnItemsLostFlat;
	}
	
	public double getRespawnHealthPercentage ()
	{
		return respawnHealthPercentage;
	}

	public double getRespawnFoodPercentage ()
	{
		return respawnFoodPercentage;
	}

	public double getRespawnMoneyLostPercentage ()
	{
		return respawnMoneyLostPercentage;
	}
	
	public double getRespawnItemsLostPercentage ()
	{
		return respawnItemsLostPercentage;
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

	public void setRespawnMoneyLostFlat (double respawnMoneyLostFlat)
	{
		this.respawnMoneyLostFlat = respawnMoneyLostFlat;
	}
	
	public void setRespawnItemsLostFlat (int respawnItemsLostFlat)
	{
		this.respawnItemsLostFlat = respawnItemsLostFlat;
	}
	
	public void setRespawnHealthPercentage (double respawnHealthPercentage)
	{
		this.respawnHealthPercentage = respawnHealthPercentage;
	}

	public void setRespawnFoodPercentage (double respawnFoodPercentage)
	{
		this.respawnFoodPercentage = respawnFoodPercentage;
	}

	public void setRespawnMoneyLostPercentage (double respawnMoneyLostPercentage)
	{
		this.respawnMoneyLostPercentage = respawnMoneyLostPercentage;
	}
	
	public void setRespawnItemsLostPercentage (double respawnItemsLostPercentage)
	{
		this.respawnItemsLostPercentage = respawnItemsLostPercentage;
	}
	
	public void setRespawnEffects (PotionEffect[] respawnEffects)
	{
		this.respawnEffects = respawnEffects;
	}
	
	public DeathPenaltiesWorld getCopy ()
	{
		return new DeathPenaltiesWorld(this.enabled, this.respawnHealthFlat, this.respawnFoodFlat, this.respawnMoneyLostFlat, this.respawnItemsLostFlat, this.respawnHealthPercentage, this.respawnFoodPercentage, this.respawnMoneyLostPercentage, this.respawnItemsLostPercentage, this.respawnEffects);
	}

}
