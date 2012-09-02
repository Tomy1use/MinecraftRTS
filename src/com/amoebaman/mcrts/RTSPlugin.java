package com.amoebaman.mcrts;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import com.amoebaman.mcrts.listeners.RTSPlayerListener;
import com.amoebaman.mcrts.listeners.RTSUnitListener;
import com.amoebaman.mcrts.executors.ArmyExecutor;
import com.amoebaman.mcrts.executors.CommanderExecutor;
import com.amoebaman.mcrts.objects.RTSPlayer;
import com.amoebaman.mcrts.objects.Unit;
import com.amoebaman.mcrts.utilities.FileUtils;

public class RTSPlugin extends JavaPlugin{
	
	public File configFile, playerFile;
	
	public static boolean DEBUG;
	public static int maxArmySize, groupSelectionRadius;
	public static Material singleSelectionTool, groupSelectionTool;
	public static ArrayList<RTSPlayer> rtsPlayers;
	public static ArrayList<EntityType> armyUnits;
	
	@SuppressWarnings("unchecked")
	public void onEnable(){
		String mainDirectory = "plugins/mcRTS";
		
		new File(mainDirectory).mkdir();
		configFile = new File(mainDirectory + "/config.yml");
		playerFile = new File(mainDirectory + "/players.dat");
		try{
			if(!configFile.exists())
				configFile.createNewFile();
			getConfig().load(configFile);
			getConfig().options().copyDefaults(true);
			DEBUG = getConfig().getBoolean("debug");
			maxArmySize = getConfig().getInt("armySize");
			groupSelectionRadius = getConfig().getInt("groupSelectionRadius");
			singleSelectionTool = Material.matchMaterial(getConfig().getString("singleSelectionTool"));
			groupSelectionTool = Material.matchMaterial(getConfig().getString("groupSelectionTool"));
			
			if(!playerFile.exists()){
				playerFile.createNewFile();
				FileUtils.save(new ArrayList<RTSPlayer>(), playerFile);
			}
			rtsPlayers = (ArrayList<RTSPlayer>) FileUtils.load(playerFile);
			if(rtsPlayers == null)
				rtsPlayers = new ArrayList<RTSPlayer>();
		}
		catch(Exception e){ e.printStackTrace(); }
		
		Bukkit.getPluginManager().registerEvents(new RTSPlayerListener(), this);
		Bukkit.getPluginManager().registerEvents(new RTSUnitListener(), this);
		getCommand("order").setExecutor(new CommanderExecutor());
		getCommand("army").setExecutor(new ArmyExecutor());
		armyUnits = new ArrayList<EntityType>();
		armyUnits.add(EntityType.ZOMBIE);
		armyUnits.add(EntityType.WOLF);
		armyUnits.add(EntityType.SPIDER);
		armyUnits.add(EntityType.SKELETON);
		armyUnits.add(EntityType.CAVE_SPIDER);
		armyUnits.add(EntityType.CREEPER);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){ public void run(){
			try{
				persistArmies();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		} }, 0, 100);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){ public void run(){
			try{
				for(RTSPlayer rtsPlayer : rtsPlayers)
					for(Unit unit : rtsPlayer.army)
						unit.getObjective().update(unit);
				showSelectedUnits();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		} }, 0, 20);
	}
	
	public void onDisable(){
		try{
			FileUtils.save(rtsPlayers, playerFile);
		}
		catch(Exception e){ e.printStackTrace(); }
	}
	
	public static PluginLogger logger(){
		return new PluginLogger(Bukkit.getPluginManager().getPlugin("MCRTS"));
	}
	
	public static Player getPlayer(RTSPlayer rtsPlayer){
		OfflinePlayer player = Bukkit.getOfflinePlayer(rtsPlayer.name);
		if(player.isOnline())
			return player.getPlayer();
		else
			return null;
	}
	
	public static RTSPlayer getRTSPlayer(Player player){
		for(RTSPlayer rtsPlayer : rtsPlayers)
			if(rtsPlayer.name.equals(player.getName()))
				return rtsPlayer;
		return null;
	}
	
	public static Creature getEntityFromUUID(UUID id){
		for(World world : Bukkit.getWorlds())
			for(LivingEntity entity : world.getLivingEntities())
				if(entity.getUniqueId().equals(id) && entity instanceof Creature)
					return (Creature) entity;
		return null;
	}
	
	public static Unit getUnitFromUUID(UUID id){
		for(RTSPlayer rtsPlayer : rtsPlayers)
			for(Unit unit : rtsPlayer.army)
				if(unit.getUUID().equals(id))
					return unit;
		return null;
	}
	
	public static double distance(Entity ent1, Entity ent2){
		if(!ent1.getWorld().equals(ent2.getWorld()))
			return Double.MAX_VALUE;
		return ent1.getLocation().distance(ent2.getLocation());
	}
	
	private void persistArmies(){
		for(RTSPlayer rtsPlayer : rtsPlayers){
			ArrayList<Unit> toReplace = new ArrayList<Unit>();
			for(Unit unit : rtsPlayer.army){
				LivingEntity entity = getEntityFromUUID(unit.getUUID());
				if(entity == null)
					toReplace.add(unit);
			}
			for(Unit unit : toReplace){
				rtsPlayer.army.remove(unit);
				Location loc = unit.getEntity().getLocation();
				Creature newSpawn = (Creature) loc.getWorld().spawnEntity(loc, unit.getType());
				new Unit(newSpawn, rtsPlayer);
			}
			
		}
	}
	
	private void showSelectedUnits(){
		for(RTSPlayer rtsPlayer : rtsPlayers)
			for(Unit unit : rtsPlayer.selected){
				Player player = getPlayer(rtsPlayer);
				if(player != null)
					player.playEffect(unit.getEntity().getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
			}
	}

}
