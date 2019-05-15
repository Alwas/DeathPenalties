package be.waslet.dp.main;

public enum DeathPenaltiesOption
{
	
	ENABLED("enabled", true),
	HEALTH_FLAT("respawn-health-flat", true),
	FOOD_FLAT("respawn-food-flat", true),
	MONEY_LOST_FLAT("death-money-lost-flat", true),
	ITEMS_DROPPED_FLAT("death-items-dropped-flat", true),
	ITEMS_DESTROYED_FLAT("death-items-destroyed-flat", true),
	HEALTH_PERCENTAGE("respawn-health-percentage", true),
	FOOD_PERCENTAGE("respawn-food-percentage", true),
	MONEY_LOST_PERCENTAGE("death-money-lost-percentage", true),
	ITEMS_DROPPED_PERCENTAGE("death-items-dropped-percentage", true),
	ITEMS_DESTROYED_PERCENTAGE("death-items-destroyed-percentage", true),
	WHITELISTED_ITEMS("whitelisted-items", false),
	RESPAWN_PROCESSED_COMMANDS("respawn-processed-commands", false),
	DEATH_PROCESSED_COMMANDS("death-processed-commands", false);
	
	private String configPath;
	private boolean editable;
	
	DeathPenaltiesOption (String configPath, boolean editable)
	{
		this.configPath = configPath;
		this.editable = editable;
	}
	
	public String getConfigPath ()
	{
		return configPath;
	}
	
	public boolean isValid (String value)
	{
		if (!this.editable) return false;
		if (this.equals(ENABLED) && (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"))) return true;
		else
		{
			try
			{
				Double.parseDouble(value);
				return true;
			}
			catch (NumberFormatException exc)
			{
				return false;
			}
		}
	}
	
	public static DeathPenaltiesOption getOption (String string)
	{
		DeathPenaltiesOption option = null;
		try
		{
			option = DeathPenaltiesOption.valueOf(string.toUpperCase());
		}
		catch (IllegalArgumentException exc)
		{
			return null;
		}
		return (option.editable) ? option : null;
	}
	
}
