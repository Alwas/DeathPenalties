package be.waslet.dp.main;

public enum DeathPenaltiesOption
{
	
	APPLY_DEATH_WORLD("apply-penalties-from-death-world"),
	EFFECT_FORMAT_SECONDS("effects-duration-format-in-seconds"),
	EFFECT_FORMAT_LEVELS("effects-level-format-true-level"),
	ENABLED("enabled"),
	HEALTH_FLAT("respawn-health-flat"),
	FOOD_FLAT("respawn-food-flat"),
	HEALTH_PERCENTAGE("respawn-health-percentage"),
	FOOD_PERCENTAGE("respawn-food-percentage"),
	EFFECTS("respawn-effects");
	
	private String configPath;
	
	DeathPenaltiesOption (String configPath)
	{
		this.configPath = configPath;
	}
	
	public String getConfigPath ()
	{
		return configPath;
	}
	
	public boolean isValid (String value)
	{
		if (this.equals(ENABLED) && (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"))) return true;
		if (this.equals(HEALTH_FLAT) || this.equals(FOOD_FLAT) || this.equals(HEALTH_PERCENTAGE) || this.equals(FOOD_PERCENTAGE))
		{
			try
			{
				Double.parseDouble(value);
				return true;
			}
			catch (NumberFormatException exc) {return false;}
		}
		return false;
	}
	
	public static DeathPenaltiesOption getOption (String string)
	{
		for (DeathPenaltiesOption option : DeathPenaltiesOption.values())
		{
			if (string.equalsIgnoreCase(option.name()))
			{
				return option;
			}
		}
		return null;
	}
	
}
