package be.waslet.dp.main;

import java.util.ArrayList;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DeathPenaltiesParser
{

	public PotionEffect[] getParsedPotionsEffects (String[] potionEffectsFormats, boolean durationInSeconds, boolean trueLevel) throws DeathPenaltiesParserException
	{
		ArrayList<PotionEffect> potionsEffects = new ArrayList<PotionEffect>();
		for (String effectFormat : potionEffectsFormats)
		{
			PotionEffect potionEffect = parsePotionEffect(effectFormat, durationInSeconds, trueLevel);
			potionsEffects.add(potionEffect);
		}
		return potionsEffects.toArray(new PotionEffect[potionsEffects.size()]);
	}

	private PotionEffect parsePotionEffect (String potionEffectFormat, boolean durationInSeconds, boolean trueLevel) throws DeathPenaltiesParserException
	{
		String[] args = potionEffectFormat.split(":");
		if (args.length != 4)
		{
			throw new DeathPenaltiesParserException("An effect line is not properly formatted: \"" + potionEffectFormat + "\". Please check it");
		}
		PotionEffectType potionEffect = PotionEffectType.getByName(args[0]);
		if (potionEffect == null)
		{
			throw new DeathPenaltiesParserException("Effect \" + args[0] + \" does not exist. Please check config");
		}
		try
		{
			return new PotionEffect(potionEffect, ((durationInSeconds) ? Integer.parseInt(args[1]) * 20 : Integer.parseInt(args[1])), ((trueLevel) ? Integer.parseInt(args[2]) - 1 : Integer.parseInt(args[2])), false, Boolean.getBoolean(args[3]));
		}
		catch (NumberFormatException e)
		{
			throw new DeathPenaltiesParserException("An effect line is not properly formatted: \"" + potionEffectFormat + "\". Please check it");
		}
	}

}
