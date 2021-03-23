package be.waslet.dp.main;

import org.bukkit.Material;

public class DeathPenaltiesWorld
{

	private boolean enabled;
	// FLAT VALUES
	private double respawnHealthFlat;
	
	private int respawnFoodFlat;
	
	private double deathMoneyLostFlat;
	
	private int deathItemsDroppedFlat;
	private int deathItemsDestroyedFlat;
	
	private double deathExperienceDroppedFlat;
	private double deathExperienceDestroyedFlat;
	private int deathLevelsDestroyedFlat;
	// PERCENTAGE VALUES
	private double respawnHealthPercentage;
	private double respawnHealthChancePercentage;
	
	private double respawnFoodPercentage;
	private double respawnFoodChancePercentage;
	
	private double deathMoneyLostPercentage;
	private double deathMoneyLostChancePercentage;
	
	private double deathItemsDroppedPercentage;
	private double deathItemsDroppedChancePercentage;
	
	private double deathItemsDestroyedPercentage;
	private double deathItemsDestroyedChancePercentage;
	
	private double deathExperienceDroppedPercentage;
	private double deathExperienceDroppedChancePercentage;
	private double deathExperienceDestroyedPercentage;
	private double deathExperienceDestroyedChancePercentage;
	private double deathLevelsDestroyedPercentage;
	private double deathLevelsDestroyedChancePercentage;
	
	private String moneyLostBankAccount;
	// OTHERS
	private Material[] whitelistedItems;
	private String[] respawnProcessedCommands;
	private String[] deathProcessedCommands;

	public DeathPenaltiesWorld (boolean enabled, double respawnHealthFlat, int respawnFoodFlat,
			double deathMoneyLostFlat, int deathItemsDroppedFlat, int deathItemsDestroyedFlat,
			double deathExperienceDroppedFlat, double deathExperienceDestroyedFlat, int deathLevelsDestroyedFlat,
			double respawnHealthPercentage, double respawnHealthChancePercentage, double respawnFoodPercentage,
			double respawnFoodChancePercentage, double deathMoneyLostPercentage, double deathMoneyLostChancePercentage,
			double deathItemsDroppedPercentage, double deathItemsDroppedChancePercentage,
			double deathItemsDestroyedPercentage, double deathItemsDestroyedChancePercentage,
			double deathExperienceDroppedPercentage, double deathExperienceDroppedChancePercentage,
			double deathExperienceDestroyedPercentage, double deathExperienceDestroyedChancePercentage,
			double deathLevelsDestroyedPercentage, double deathLevelsDestroyedChancePercentage,
			String moneyLostBankAccount, Material[] whitelistedItems, String[] respawnProcessedCommands,
			String[] deathProcessedCommands)
	{
		super();
		this.enabled = enabled;
		this.respawnHealthFlat = respawnHealthFlat;
		this.respawnFoodFlat = respawnFoodFlat;
		this.deathMoneyLostFlat = deathMoneyLostFlat;
		this.deathItemsDroppedFlat = deathItemsDroppedFlat;
		this.deathItemsDestroyedFlat = deathItemsDestroyedFlat;
		this.deathExperienceDroppedFlat = deathExperienceDroppedFlat;
		this.deathExperienceDestroyedFlat = deathExperienceDestroyedFlat;
		this.deathLevelsDestroyedFlat = deathLevelsDestroyedFlat;
		this.respawnHealthPercentage = respawnHealthPercentage;
		this.respawnHealthChancePercentage = respawnHealthChancePercentage;
		this.respawnFoodPercentage = respawnFoodPercentage;
		this.respawnFoodChancePercentage = respawnFoodChancePercentage;
		this.deathMoneyLostPercentage = deathMoneyLostPercentage;
		this.deathMoneyLostChancePercentage = deathMoneyLostChancePercentage;
		this.deathItemsDroppedPercentage = deathItemsDroppedPercentage;
		this.deathItemsDroppedChancePercentage = deathItemsDroppedChancePercentage;
		this.deathItemsDestroyedPercentage = deathItemsDestroyedPercentage;
		this.deathItemsDestroyedChancePercentage = deathItemsDestroyedChancePercentage;
		this.deathExperienceDroppedPercentage = deathExperienceDroppedPercentage;
		this.deathExperienceDroppedChancePercentage = deathExperienceDroppedChancePercentage;
		this.deathExperienceDestroyedPercentage = deathExperienceDestroyedPercentage;
		this.deathExperienceDestroyedChancePercentage = deathExperienceDestroyedChancePercentage;
		this.deathLevelsDestroyedPercentage = deathLevelsDestroyedPercentage;
		this.deathLevelsDestroyedChancePercentage = deathLevelsDestroyedChancePercentage;
		this.moneyLostBankAccount = moneyLostBankAccount;
		this.whitelistedItems = whitelistedItems;
		this.respawnProcessedCommands = respawnProcessedCommands;
		this.deathProcessedCommands = deathProcessedCommands;
	}

	public boolean isEnabled ()
	{
		return this.enabled;
	}

	public void setEnabled (boolean enabled)
	{
		this.enabled = enabled;
	}

	public double getRespawnHealthFlat ()
	{
		return this.respawnHealthFlat;
	}

	public void setRespawnHealthFlat (double respawnHealthFlat)
	{
		this.respawnHealthFlat = respawnHealthFlat;
	}

	public int getRespawnFoodFlat ()
	{
		return this.respawnFoodFlat;
	}

	public void setRespawnFoodFlat (int respawnFoodFlat)
	{
		this.respawnFoodFlat = respawnFoodFlat;
	}

	public double getDeathMoneyLostFlat ()
	{
		return this.deathMoneyLostFlat;
	}

	public void setDeathMoneyLostFlat (double deathMoneyLostFlat)
	{
		this.deathMoneyLostFlat = deathMoneyLostFlat;
	}

	public int getDeathItemsDroppedFlat ()
	{
		return this.deathItemsDroppedFlat;
	}

	public void setDeathItemsDroppedFlat (int deathItemsDroppedFlat)
	{
		this.deathItemsDroppedFlat = deathItemsDroppedFlat;
	}

	public int getDeathItemsDestroyedFlat ()
	{
		return this.deathItemsDestroyedFlat;
	}

	public void setDeathItemsDestroyedFlat (int deathItemsDestroyedFlat)
	{
		this.deathItemsDestroyedFlat = deathItemsDestroyedFlat;
	}

	public double getDeathExperienceDroppedFlat ()
	{
		return this.deathExperienceDroppedFlat;
	}

	public void setDeathExperienceDroppedFlat (double deathExperienceDroppedFlat)
	{
		this.deathExperienceDroppedFlat = deathExperienceDroppedFlat;
	}

	public double getDeathExperienceDestroyedFlat ()
	{
		return this.deathExperienceDestroyedFlat;
	}

	public void setDeathExperienceDestroyedFlat (double deathExperienceDestroyedFlat)
	{
		this.deathExperienceDestroyedFlat = deathExperienceDestroyedFlat;
	}

	public int getDeathLevelsDestroyedFlat ()
	{
		return this.deathLevelsDestroyedFlat;
	}

	public void setDeathLevelsDestroyedFlat (int deathLevelsDestroyedFlat)
	{
		this.deathLevelsDestroyedFlat = deathLevelsDestroyedFlat;
	}

	public double getRespawnHealthPercentage ()
	{
		return this.respawnHealthPercentage;
	}

	public void setRespawnHealthPercentage (double respawnHealthPercentage)
	{
		this.respawnHealthPercentage = respawnHealthPercentage;
	}

	public double getRespawnFoodPercentage ()
	{
		return this.respawnFoodPercentage;
	}

	public void setRespawnFoodPercentage (double respawnFoodPercentage)
	{
		this.respawnFoodPercentage = respawnFoodPercentage;
	}

	public double getDeathMoneyLostPercentage ()
	{
		return this.deathMoneyLostPercentage;
	}

	public void setDeathMoneyLostPercentage (double deathMoneyLostPercentage)
	{
		this.deathMoneyLostPercentage = deathMoneyLostPercentage;
	}

	public double getDeathItemsDroppedPercentage ()
	{
		return this.deathItemsDroppedPercentage;
	}

	public void setDeathItemsDroppedPercentage (double deathItemsDroppedPercentage)
	{
		this.deathItemsDroppedPercentage = deathItemsDroppedPercentage;
	}

	public double getDeathItemsDroppedChancePercentage ()
	{
		return this.deathItemsDroppedChancePercentage;
	}

	public void setDeathItemsDroppedChancePercentage (double deathItemsDroppedChancePercentage)
	{
		this.deathItemsDroppedChancePercentage = deathItemsDroppedChancePercentage;
	}

	public double getDeathItemsDestroyedPercentage ()
	{
		return this.deathItemsDestroyedPercentage;
	}

	public void setDeathItemsDestroyedPercentage (double deathItemsDestroyedPercentage)
	{
		this.deathItemsDestroyedPercentage = deathItemsDestroyedPercentage;
	}

	public double getDeathItemsDestroyedChancePercentage ()
	{
		return this.deathItemsDestroyedChancePercentage;
	}

	public void setDeathItemsDestroyedChancePercentage (double deathItemsDestroyedChancePercentage)
	{
		this.deathItemsDestroyedChancePercentage = deathItemsDestroyedChancePercentage;
	}

	public double getDeathExperienceDroppedPercentage ()
	{
		return this.deathExperienceDroppedPercentage;
	}

	public void setDeathExperienceDroppedPercentage (double deathExperienceDroppedPercentage)
	{
		this.deathExperienceDroppedPercentage = deathExperienceDroppedPercentage;
	}

	public double getDeathExperienceDroppedChancePercentage ()
	{
		return this.deathExperienceDroppedChancePercentage;
	}

	public void setDeathExperienceDroppedChancePercentage (double deathExperienceDroppedChancePercentage)
	{
		this.deathExperienceDroppedChancePercentage = deathExperienceDroppedChancePercentage;
	}

	public double getDeathExperienceDestroyedPercentage ()
	{
		return this.deathExperienceDestroyedPercentage;
	}

	public void setDeathExperienceDestroyedPercentage (double deathExperienceDestroyedPercentage)
	{
		this.deathExperienceDestroyedPercentage = deathExperienceDestroyedPercentage;
	}

	public double getDeathExperienceDestroyedChancePercentage ()
	{
		return this.deathExperienceDestroyedChancePercentage;
	}

	public void setDeathExperienceDestroyedChancePercentage (double deathExperienceDestroyedChancePercentage)
	{
		this.deathExperienceDestroyedChancePercentage = deathExperienceDestroyedChancePercentage;
	}

	public double getDeathLevelsDestroyedPercentage ()
	{
		return this.deathLevelsDestroyedPercentage;
	}

	public void setDeathLevelsDestroyedPercentage (double deathLevelsDestroyedPercentage)
	{
		this.deathLevelsDestroyedPercentage = deathLevelsDestroyedPercentage;
	}

	public double getDeathLevelsDestroyedChancePercentage ()
	{
		return this.deathLevelsDestroyedChancePercentage;
	}

	public void setDeathLevelsDestroyedChancePercentage (double deathLevelsDestroyedChancePercentage)
	{
		this.deathLevelsDestroyedChancePercentage = deathLevelsDestroyedChancePercentage;
	}

	public double getRespawnHealthChancePercentage ()
	{
		return this.respawnHealthChancePercentage;
	}

	public void setRespawnHealthChancePercentage (double respawnHealthChancePercentage)
	{
		this.respawnHealthChancePercentage = respawnHealthChancePercentage;
	}

	public double getRespawnFoodChancePercentage ()
	{
		return this.respawnFoodChancePercentage;
	}

	public void setRespawnFoodChancePercentage (double respawnFoodChancePercentage)
	{
		this.respawnFoodChancePercentage = respawnFoodChancePercentage;
	}

	public double getDeathMoneyLostChancePercentage ()
	{
		return this.deathMoneyLostChancePercentage;
	}

	public void setDeathMoneyLostChancePercentage (double deathMoneyLostChancePercentage)
	{
		this.deathMoneyLostChancePercentage = deathMoneyLostChancePercentage;
	}

	public String getMoneyLostBankAccount ()
	{
		return this.moneyLostBankAccount;
	}

	public void setMoneyLostBankAccount (String moneyLostBankAccount)
	{
		this.moneyLostBankAccount = moneyLostBankAccount;
	}
	
	public boolean hasMoneyLostBankAccount ()
	{
		return this.moneyLostBankAccount != null && this.moneyLostBankAccount.length() > 0;
	}

	public Material[] getWhitelistedItems ()
	{
		return this.whitelistedItems;
	}

	public void setWhitelistedItems (Material[] whitelistedItems)
	{
		this.whitelistedItems = whitelistedItems;
	}

	public String[] getRespawnProcessedCommands ()
	{
		return this.respawnProcessedCommands;
	}

	public void setRespawnProcessedCommands (String[] respawnProcessedCommands)
	{
		this.respawnProcessedCommands = respawnProcessedCommands;
	}

	public String[] getDeathProcessedCommands ()
	{
		return this.deathProcessedCommands;
	}

	public void setDeathProcessedCommands (String[] deathProcessedCommands)
	{
		this.deathProcessedCommands = deathProcessedCommands;
	}

	public DeathPenaltiesWorld getCopy ()
	{
		return new DeathPenaltiesWorld (this.enabled, this.respawnHealthFlat, this.respawnFoodFlat,
				this.deathMoneyLostFlat, this.deathItemsDroppedFlat, this.deathItemsDestroyedFlat,
				this.deathExperienceDroppedFlat, this.deathExperienceDestroyedFlat, this.deathLevelsDestroyedFlat,
				this.respawnHealthPercentage, this.respawnHealthChancePercentage, this.respawnFoodPercentage,
				this.respawnFoodChancePercentage, this.deathMoneyLostPercentage, this.deathMoneyLostChancePercentage,
				this.deathItemsDroppedPercentage, this.deathItemsDroppedChancePercentage,
				this.deathItemsDestroyedPercentage, this.deathItemsDestroyedChancePercentage,
				this.deathExperienceDroppedPercentage, this.deathExperienceDroppedChancePercentage,
				this.deathExperienceDestroyedPercentage, this.deathExperienceDestroyedChancePercentage,
				this.deathLevelsDestroyedPercentage, this.deathLevelsDestroyedChancePercentage,
				this.moneyLostBankAccount, this.whitelistedItems, this.respawnProcessedCommands,
				this.deathProcessedCommands);
	}

}
