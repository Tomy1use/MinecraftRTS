package com.amoebaman.mcrts.objects;

import java.io.Serializable;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RTSPlayer implements Serializable{

	private static final long serialVersionUID = 7050203523709788095L;
	public String name;
	public ArrayList<Unit> army;
	public ArrayList<Unit> selected;
	
	public RTSPlayer(Player player){
		name = player.getName();
		army = new ArrayList<Unit>();
		selected = new ArrayList<Unit>();
	}
	
	public boolean equals(Object other){
		if(!(other instanceof RTSPlayer))
			return false;
		RTSPlayer otherPlayer = (RTSPlayer) other;
		return otherPlayer.name.equals(name);
	}
	
	public Player getPlayer(){
		return Bukkit.getPlayer(name);
	}

}
