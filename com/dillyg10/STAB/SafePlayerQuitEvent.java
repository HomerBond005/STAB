package com.dillyg10.STAB;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
/**
 * This is used for a safe time to edit the tab when a player logs out.
 * 
 * NOTE: This is 2 ticks after the QuitEvent, this is becuase the default tab is sent at tick 1
 * and I do not want the 2 to interfere.
 * @author dillyg10
 *
 */
public class SafePlayerQuitEvent extends PlayerEvent {
	private static final HandlerList handlers = new HandlerList();
	public SafePlayerQuitEvent(Player who) {
		super(who);
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}
}
