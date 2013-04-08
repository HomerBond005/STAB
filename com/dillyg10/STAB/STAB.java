package com.dillyg10.STAB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_4_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

/**
 * Main plugin class
 * Simple Tab API for Bukkit
 * @author dillyg10.
 * 
 */
public class STAB extends JavaPlugin {

	/**
	 * Instance of the class
	 */
	public static STAB stab;
	protected static HashMap<Player, TabObject> players = new HashMap<Player, TabObject>();
	protected static HashMap<Plugin,List<TabObject>> plugins = new HashMap<Plugin,List<TabObject>>();
	protected static TabObject defaultObject;
	public void onEnable(){
		Bukkit.getPluginManager().registerEvents(new TabListener(), this);
		stab = this;
		defaultObject = new TabObject(this, TabPriority.LOWEST);
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ConnectionSide.SERVER_SIDE, 0xC9){
			@Override
			public void onPacketSending(PacketEvent e){
				if (!e.getPacket().getStrings().read(0).contains("»")){
					e.setCancelled(true);
					return;
				}
				else {
					String withoutSpecial = e.getPacket().getStrings().read(0).replace("»", "");
					e.getPacket().getStrings().write(0, withoutSpecial.substring(0,withoutSpecial.length() > 16 ? 16 : withoutSpecial.length()));
				}
			}
		});
		Player[] onlinePlayers = Bukkit.getOnlinePlayers();
		for(Player player : onlinePlayers){
			addPlayer(player);
		}
	}

	protected static void addPlugin(Plugin p, TabObject o){
		if (!plugins.containsKey(p)){
			plugins.put(p, new ArrayList<TabObject>());
		}
		List<TabObject> tabsForPlugin = plugins.get(p);
		tabsForPlugin.add(o);
		plugins.put(p, tabsForPlugin);
	}

	/**
	 * Get all the TabObjects associated with a pluign
	 * @param p The plugin
	 * @return A blank ArrayList if the plugin isn't in the map, or all of the TabObjects
	 */
	public List<TabObject> getTabsForPlugin(Plugin p){
		return (plugins.get(p) == null ? new ArrayList<TabObject>() : plugins.get(p));
	}

	protected static void addPlayer(Player p){
		players.put(p, defaultObject);
	}

	/**
	 * Get the tab object for a player
	 * @param p The player
	 * @return Tab Object for a player
	 */
	public static TabObject getTabForPlayer(Player p){
		return players.get(p);
	}

	protected static void removePlayer(Player p){
		players.remove(p);
	}

	/**
	 * Send a tab object to all players
	 * @param o The TabObject
	 */
	public static void sendTabToAll(TabObject o){
		for (Player p : Bukkit.getOnlinePlayers()){
			sendTabToPlayer(o,p);
		}
	}
	
	/**
	 * Sends a tab object to all players in a specific world
	 * @param o The TabObject
	 * @param w The World
	 */
	public static void sendTabToAllInWorld(TabObject o, World w){
		for (Player p : w.getPlayers()){
			sendTabToPlayer(o,p);
		}
	}

	/**
	 * Sends a tab object to all players with a specific permission in a specific world
	 * @param o The TabObject
	 * @param w The World
	 * @param permission The permission node
	 */
	public static void sendTabToAllInWorldWithPermission(TabObject o, World w, String permission){
		for (Player p : w.getPlayers()){
			if (p.hasPermission(permission)){
				sendTabToPlayer(o,p);
			}
		}
	}

	/**
	 * Sends all the players the tab object with a specific permission
	 * @param o The TabObject
	 * @param permission The permission node
	 */ 
	public static void sentTabToAllWithPermission(TabObject o, String permission){
		for (Player p : Bukkit.getOnlinePlayers()){
			if (p.hasPermission(permission)){
				sendTabToPlayer(o,p);
			}
		}
	}

	/**
	 * Clears the tab list for a player
	 * PLEASE ONLY USE WHEN NEEDED!
	 * @param p The Player
	 */
	public static void clearLastTabForPlayer(Player p){
		for (TabPacket pack : getTabForPlayer(p).getPackets()){
			CraftPlayer cp = (CraftPlayer)p;
			pack.online = false;
			cp.getHandle().playerConnection.sendPacket(pack.getNMSPacket());
		}
	}

	/**
	 * Sends a TabObject to a single player
	 * @param o The TabObject
	 * @param p The Player
	 */
	public static void sendTabToPlayer(TabObject o, Player p){
		if (getTabForPlayer(p).getPriority() > o.getPriority()){
			return;
		}
		if (!getTabForPlayer(p).equals(defaultObject)){
			clearLastTabForPlayer(p);
		}
		players.put(p, o);
		for (TabPacket pack : o.getPackets()){
			CraftPlayer cp = (CraftPlayer)p;
			cp.getHandle().playerConnection.sendPacket(pack.getNMSPacket());
		}
	}



}
