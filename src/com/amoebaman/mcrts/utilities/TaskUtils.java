package com.amoebaman.mcrts.utilities;

import org.bukkit.Bukkit;

import com.amoebaman.mcrts.RTSPlugin;

public class TaskUtils {
	
	private static RTSPlugin plugin;
	public TaskUtils(RTSPlugin instance){
		plugin = instance;
	}
	
	public static void scheduleSyncDelayedTask(Runnable task, long delay){
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, task, delay);
	}
	
	public static void scheduleSyncRepeatingTask(Runnable task, long period, long delay){
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, task, delay, period);
	}
	
	public static long tickValue(int seconds, int minutes, int hours){
		return 20 * seconds + 120 * minutes + 72000 * hours;
	}

}
