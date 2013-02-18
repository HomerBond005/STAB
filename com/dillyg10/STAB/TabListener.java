package com.dillyg10.STAB;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TabListener implements Listener {

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerJoin(final PlayerJoinEvent e){
		STAB.addPlayer(e.getPlayer());
		int x = 0, y = 0;
		for (Player p : Bukkit.getOnlinePlayers()){
			if (x == 3) {
				x = 0;
				y++;
			}
			if (y == 20) break;
			STAB.defaultObject.setTab(x, y, p.getName(), true);
			x++;
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(STAB.stab, new Runnable(){
			public void run(){
				STAB.sendTabToAll(STAB.getTabForPlayer(e.getPlayer()));
			}
		}, 1L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(STAB.stab, new Runnable(){
			public void run(){
				Bukkit.getPluginManager().callEvent(new SafePlayerJoinEvent(e.getPlayer()));
			}
		}, 2L);
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerQuit(final PlayerQuitEvent e){

		if (STAB.defaultObject.equals(STAB.getTabForPlayer(e.getPlayer()))){
			Bukkit.getScheduler().scheduleSyncDelayedTask(STAB.stab, new Runnable(){
				public void run(){
					int x = 0, y = 0;
					for (Player p : Bukkit.getOnlinePlayers()){
						STAB.clearLastTabForPlayer(p);
						if (x == 3) {
							x = 0;
							y++;
						}
						if (y == 20) break;
						STAB.defaultObject.setTab(x, y, p.getName(), true);
						x++;
					}
					STAB.sendTabToAll(STAB.defaultObject);
				}
			},1L);
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(STAB.stab, new Runnable(){
			public void run(){
				Bukkit.getPluginManager().callEvent(new SafePlayerQuitEvent(e.getPlayer()));
				STAB.removePlayer(e.getPlayer());	
			}
		}, 2L);
	}
}
