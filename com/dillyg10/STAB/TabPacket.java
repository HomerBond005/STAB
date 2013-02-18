package com.dillyg10.STAB;

import net.minecraft.server.v1_4_R1.Packet201PlayerInfo;

public class TabPacket {
	public String name;
	public boolean online;
	public int ping;
	public TabPacket(String name, boolean online, int ping){
		this.name = name;
		this.online = online;
		this.ping = ping;
	}
	
	public Packet201PlayerInfo getNMSPacket(){
		Packet201PlayerInfo p201 = new Packet201PlayerInfo();
		p201.a = this.name;
		p201.b = this.online;
		p201.c = this.ping;
		return p201;
	}
}
