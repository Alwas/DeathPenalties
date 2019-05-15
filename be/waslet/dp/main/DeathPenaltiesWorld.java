package be.waslet.dp.main;

import org.bukkit.Material;

public class DeathPenaltiesWorld
{

	private boolean enabled;
	private double respawnHealthFlat;
	private int respawnFoodFlat;
	private double deathMoneyLostFlat;
	private int deathItemsDroppedFlat;
	private int deathItemsDestroyedFlat;
	private double respawnHealthPercentage;
	private double respawnFoodPercentage;
	private double deathMoneyLostPercentage;
	private double deathItemsDroppedPercentage;
	private double deathItemsDestroyedPercentage;
	private Material[] whitelistedItems;
	private String[] respawnProcessedCommands;
	private String[] deathProcessedCommands;

	public DeathPenaltiesWorld (boolean enabled, double respawnHealthFlat, int respawnFoodFlat, double deathMoneyLostFlat, int deathItemsDroppedFlat, int deathItemsDestroyedFlat, double respawnHealthPercentage, double respawnFoodPercentage, double deathMoneyLostPercentage, double deathItemsDroppedPercentage, double deathItemsDestroyedPercentage, Material[] whitelistedItems, String[] respawnProcessedCommands, String[] deathProcessedCommands)
	{
		this.enabled = enabled;
		this.respawnHealthFlat = respawnHealthFlat;
		this.respawnFoodFlat = respawnFoodFlat;
		this.deathMoneyLostFlat = deathMoneyLostFlat;
		this.deathItemsDroppedFlat = deathItemsDroppedFlat;
		this.deathItemsDestroyedFlat = deathItemsDestroyedFlat;
		this.respawnHealthPercentage = respawnHealthPercentage;
		this.respawnFoodPercentage = respawnFoodPercentage;
		this.deathMoneyLostPercentage = deathMoneyLostPercentage;
		this.deathItemsDroppedPercentage = deathItemsDroppedPercentage;
		this.deathItemsDestroyedPercentage = deathItemsDestroyedPercentage;
		this.whitelistedItems = whitelistedItems;
		this.respawnProcessedCommands = respawnProcessedCommands;
		this.deathProcessedCommands = deathProcessedCommands;
	}

	public boolean isEnabled ()
	{
		return this.enabled;
	}

	public double getRespawnHealthFlat ()
	{
		return this.respawnHealthFlat;
	}

	public int getRespawnFoodFlat ()
	{
		return this.respawnFoodFlat;
	}

	public double getDeathMoneyLostFlat ()
	{
		return this.deathMoneyLostFlat;
	}

	public int getDeathItemsDroppedFlat ()
	{
		return this.deathItemsDroppedFlat;
	}

	public int getDeathItemsDestroyedFlat ()
	{
		return this.deathItemsDestroyedFlat;
	}
	
	public double getRespawnHealthPercentage ()
	{
		return this.respawnHealthPercentage;
	}

	public double getRespawnFoodPercentage ()
	{
		return this.respawnFoodPercentage;
	}

	public double getDeathMoneyLostPercentage ()
	{
		return this.deathMoneyLostPercentage;
	}

	public double getDeathItemsDroppedPercentage ()
	{
		return this.deathItemsDroppedPercentage;
	}

	public double getDeathItemsDestroyedPercentage ()
	{
		return this.deathItemsDestroyedPercentage;
	}

	public Material[] getWhitelistedItems ()
	{
		return this.whitelistedItems;
	}
	
	public String[] getRespawnProcessedCommands ()
	{
		return this.respawnProcessedCommands;
	}
	
	public String[] getDeathProcessedCommands ()
	{
		return this.deathProcessedCommands;
	}
	
	public void setEnabled (boolean enabled)
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

	public void setDeathMoneyLostFlat (double deathMoneyLostFlat)
	{
		this.deathMoneyLostFlat = deathMoneyLostFlat;
	}

	public void setDeathItemsDroppedFlat (int deathItemsDroppedFlat)
	{
		this.deathItemsDroppedFlat = deathItemsDroppedFlat;
	}

	public void setDeathItemsDestroyedFlat (int deathItemsDestroyedFlat)
	{
		this.deathItemsDestroyedFlat = deathItemsDestroyedFlat;
	}
	
	public void setRespawnHealthPercentage (double respawnHealthPercentage)
	{
		this.respawnHealthPercentage = respawnHealthPercentage;
	}

	public void setRespawnFoodPercentage (double respawnFoodPercentage)
	{
		this.respawnFoodPercentage = respawnFoodPercentage;
	}

	public void setDeathMoneyLostPercentage (double deathMoneyLostPercentage)
	{
		this.deathMoneyLostPercentage = deathMoneyLostPercentage;
	}

	public void setDeathItemsDroppedPercentage (double deathItemsDroppedPercentage)
	{
		this.deathItemsDroppedPercentage = deathItemsDroppedPercentage;
	}

	public void setDeathItemsDestroyedPercentage (double deathItemsDestroyedPercentage)
	{
		this.deathItemsDestroyedPercentage = deathItemsDestroyedPercentage;
	}

	public void setWhitelistedItems (Material[] whitelistedItems)
	{
		this.whitelistedItems = whitelistedItems;
	}
	
	public void setRespawnProcessedCommands (String[] respawnProcessedCommands)
	{
		this.respawnProcessedCommands = respawnProcessedCommands;
	}
	
	public void setDeathProcessedCommands (String[] deathProcessedCommands)
	{
		this.deathProcessedCommands = deathProcessedCommands;
	}
	
	public DeathPenaltiesWorld getCopy ()
	{
		return new DeathPenaltiesWorld(this.enabled, this.respawnHealthFlat, this.respawnFoodFlat, this.deathMoneyLostFlat, this.deathItemsDroppedFlat, this.deathItemsDestroyedFlat, this.respawnHealthPercentage, this.respawnFoodPercentage, this.deathMoneyLostPercentage, this.deathItemsDroppedPercentage, this.deathItemsDestroyedPercentage, this.whitelistedItems, this.respawnProcessedCommands, this.deathProcessedCommands);
	}

}
