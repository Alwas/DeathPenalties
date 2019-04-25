package be.waslet.dp.main;

public enum DeathPenaltiesOption
{
	
	EFFECT_FORMAT_SECONDS("effects-duration-format-in-seconds", false),
	EFFECT_FORMAT_LEVELS("effects-level-format-true-level", false),
	ENABLED("enabled", true),
	HEALTH_FLAT("respawn-health-flat", true),
	FOOD_FLAT("respawn-food-flat", true),
	MONEY_LOST_FLAT("respawn-money-lost-flat", true),
	ITEMS_LOST_FLAT("respawn-items-lost-flat", true),
	HEALTH_PERCENTAGE("respawn-health-percentage", true),
	FOOD_PERCENTAGE("respawn-food-percentage", true),
	MONEY_LOST_PERCENTAGE("respawn-money-lost-percentage", true),
	ITEMS_LOST_PERCENTAGE("respawn-items-lost-percentage", true),
	EFFECTS("respawn-effects", true);
	
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
		else if (this.equals(EFFECTS)) return true;
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
