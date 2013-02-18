package com.dillyg10.STAB;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.Plugin;
/**
 * This represents the Tab list used throughout the plugin.
 * @author dillyg10
 *
 */
public class TabObject {
	/**
	 * The max row size for the tab list.
	 */
	public static final int MAX_X = 3;
	/**
	 * The max collum size for the tab list
	 */
	public static final int MAX_Y = 20;
	/**
	 * The plugin that is using this TabObject
	 */
	private Plugin plugin;
	protected TabPacket[][] tabs = new TabPacket[3][20];
	/**
	 * The Tab Priority
	 * @see TabPriority
	 */
	private int priority;
	/**
	 * The id, usefull if you have multiple TabObjects
	 */
	private int id;
	protected String space = "";
	/**
	 * Create a TabObject
	 * @param p The Plugin
	 * @param priority The Priority @see TabPriority
	 */
	public TabObject(Plugin p, int priority){
		this.plugin = p;
		STAB.addPlugin(p, this);
		this.priority = priority;	
	}
	/**
	 * Create a TabObject
	 * @param p The Plugin
	 * @param priority The Priority @see TabPriority
	 * @param id The id
	 */
	public TabObject(Plugin p, int priority, int id){
		this(p,priority);
		this.setId(id);
	}
	/**
	 * Returns the priority of this TabObject
	 * @return The Priority
	 */
	public int getPriority() {
		return priority;
	}
	/**
	 * Sets the priority for this TabObject
	 * @param priority
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	/**
	 * Sets a tab at the x and y, it is online and ping 9999 by default
	 * @param x X location
	 * @param y Y location
	 * @param tab What you want to set
	 */
	public void setTab(int x, int y, String tab){
		tabs[x][y] = new TabPacket(tab,true,9999);
	}
	/**
	 * Sets a tab at the x and y, and online equal to online, the ping is 9999 by default
	 * @param x X location
	 * @param y Y location
	 * @param tab What you want to set
	 * @param online Whether it is online or not, offline will show as blank
	 */
	public void setTab(int x, int y, String tab, boolean online){
		tabs[x][y] = new TabPacket(tab,online,9999);
	}
	/**
	 * Set a tab at the x and y, with online equal to online, and ping equal to ping
	 * @param x X location
	 * @param y Y location
	 * @param tab What you want to set
	 * @param online Whether it is online or not, offline will show as blank
	 * @param ping The ping
	 */
	public void setTab(int x, int y, String tab, boolean online, int ping){
		tabs[x][y] = new TabPacket(tab,online,ping);
	}
	/**
	 * Returns the String found at x, and y
	 * @param x X location
	 * @param y Y location
	 * @return the tab at that location
	 */
	public String getTab(int x, int y){
		return (tabs[x][y] == null ? null : tabs[x][y].name);
	}
	/**
	 * Get the plugin
	 * @return The Plugin
	 */
	public Plugin getPlugin() {
		return plugin;
	}
	/**
	 * Get the ID
	 * @return the ID (0 by default)
	 */
	public int getId() {
		return id;
	}
	/**
	 * Sets the ID
	 * @param id The ID you want to set it as
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * Clears the tabs, does not require any packets!
	 */
	public void clear(){
		tabs = new TabPacket[3][20];
	}
	private void nextSpace(){
		if (space.length() == 16){
			space.replace(" ", "");
			space += "¤";
		}
		else {
			space += " ";
		}
	}
	protected List<TabPacket> getPackets(){
		List<TabPacket> packets = new ArrayList<TabPacket>();
		for (int y = 0; y < 20; y++){
			for (int x = 0; x < 3; x++){
				TabPacket pp = tabs[x][y];
				if (pp == null){
					pp = new TabPacket(space,false,0);
				}
				pp.name = "»"+pp.name;
				packets.add(pp);
				nextSpace();
			}
		}
		space = "";
		return packets;
	}
	
}
