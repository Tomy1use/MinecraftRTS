package com.amoebaman.mcrts.utilities;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class S_Location implements Serializable{
	
	private static final long serialVersionUID = -1331634475331270483L;
	public String worldName;
	public double x;
	public double y;
	public double z;
	public float yaw;
	public float pitch;
	
	private S_Location(Location loc){
		worldName = loc.getWorld().getName();
		x = loc.getX();
		y = loc.getY();
		z = loc.getZ();
		yaw = loc.getYaw();
		pitch = loc.getPitch();
	}
	
	public static S_Location serialize(Location loc){
		return new S_Location(loc);
	}
	
	public static Location deserialize(S_Location sLoc){
		if(sLoc == null)
			return null;
		return new Location(Bukkit.getWorld(sLoc.worldName), sLoc.x, sLoc.y, sLoc.z, sLoc.yaw, sLoc.pitch);
	}
	
	public String toString(){
		return worldName + "-(" + (int) x + "," + (int) y + "," + (int) z + ")";
	}
	
	public static String toString(Location loc){
		return serialize(loc).toString();
	}

}
