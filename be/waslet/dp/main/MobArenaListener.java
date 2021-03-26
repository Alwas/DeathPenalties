package be.waslet.dp.main;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.garbagemule.MobArena.events.ArenaPlayerJoinEvent;
import com.garbagemule.MobArena.events.ArenaPlayerLeaveEvent;

public class MobArenaListener implements Listener
{

	private DeathPenalties plugin;
	
	public MobArenaListener (DeathPenalties plugin)
	{
		this.plugin = plugin;
	}
	
	/*
	 * MOB ARENA MANAGMENT
	 * since mob arena plugin arena managers players functions does not seem to be working we will use the event system to keep track of in arena players
	 */
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerJoinsArena (ArenaPlayerJoinEvent event)
	{
		this.plugin.getArenaPlayers().add(event.getPlayer().getUniqueId().toString());
	}

	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerLeavesArena (ArenaPlayerLeaveEvent event)
	{
		this.plugin.getArenaPlayers().remove(event.getPlayer().getUniqueId().toString());
	}

}
