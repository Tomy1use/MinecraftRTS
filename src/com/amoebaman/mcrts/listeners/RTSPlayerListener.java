package com.amoebaman.mcrts.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.amoebaman.mcrts.RTSPlugin;
import com.amoebaman.mcrts.objects.RTSPlayer;
import com.amoebaman.mcrts.objects.Unit;
import com.amoebaman.mcrts.utilities.S_Location;

public class RTSPlayerListener implements Listener{
	
	@EventHandler
	public void registerEggSpawns(CreatureSpawnEvent event){
		if(event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG){
			if(!(event.getEntity() instanceof Creature && RTSPlugin.armyUnits.contains(event.getEntityType())))
				return;
			Location loc = event.getLocation();
			Player closest = null;
			double distance = Double.MAX_VALUE;
			for(Player player : loc.getWorld().getPlayers()){
				if(loc.distance(player.getLocation()) < distance){
					closest = player;
					distance = loc.distance(player.getLocation());
				}
			}
			RTSPlayer rtsPlayer = RTSPlugin.getRTSPlayer(closest);
			if(rtsPlayer.army.size() >= RTSPlugin.maxArmySize){
				event.setCancelled(true);
				closest.sendMessage(ChatColor.GRAY + "Your army is at maximum size already");
			}
			new Unit((Creature) event.getEntity(), rtsPlayer);
			closest.sendMessage(ChatColor.GRAY + "Spawned a " + event.getEntityType().getName().replace('_', ' ').toLowerCase() + " into your army");
		}
	}
	
	@EventHandler
	public void registerPlayers(PlayerJoinEvent event){
		Player player = event.getPlayer();
		if(RTSPlugin.getRTSPlayer(player) == null){
			RTSPlugin.rtsPlayers.add(new RTSPlayer(player));
			RTSPlugin.logger().fine("Added new player " + player.getName() + " to commanders");
		}
	}
	
	@EventHandler
	public void selectUnit(PlayerInteractEntityEvent event){
		Player player = event.getPlayer();
		if(!(player.getItemInHand().getType() == RTSPlugin.singleSelectionTool))
			return;
		Entity e = event.getRightClicked();
		RTSPlayer rtsPlayer = RTSPlugin.getRTSPlayer(player);
		if(!(e instanceof LivingEntity))
			return;
		LivingEntity entity = (LivingEntity)e;
		Unit unit = RTSPlugin.getUnitFromUUID(entity.getUniqueId());
		if(unit == null)
			return;
		if(rtsPlayer.selected.contains(unit)){
			rtsPlayer.selected.remove(unit);
			player.sendMessage(ChatColor.GRAY + "Deselected unit");
		}
		else{
			rtsPlayer.selected.add(unit);
			player.sendMessage(ChatColor.GRAY + "Selected unit");
		}
	}
	
	@EventHandler
	public void debugUnit(PlayerInteractEntityEvent event){
		Player player = event.getPlayer();
		if(!(player.getItemInHand().getType() == Material.DEAD_BUSH))
			return;
		Entity e = event.getRightClicked();
		if(!(e instanceof LivingEntity))
			return;
		LivingEntity entity = (LivingEntity) e;
		Unit unit = RTSPlugin.getUnitFromUUID(entity.getUniqueId());
		if(unit == null)
			return;
		player.sendMessage(unit.toString());
		player.sendMessage(unit.getObjective().getClass().getName() + " @ " + S_Location.toString(unit.getObjective().getTargetLoc()));
		player.sendMessage(unit.getAggro().name() + " - " + unit.getObjective().getSightRange());
	}
	
	@EventHandler
	public void selectUnits(PlayerInteractEvent event){
		Player player = event.getPlayer();
		Action action = event.getAction();
		if(!(action == Action.RIGHT_CLICK_AIR && player.getItemInHand().getType() == RTSPlugin.groupSelectionTool))
			return;
		RTSPlayer rtsPlayer = RTSPlugin.getRTSPlayer(player);
		Location loc = player.getLastTwoTargetBlocks(null, 100).get(0).getLocation();
		int c = 0;
		for(Unit unit : rtsPlayer.army)
			if(unit.getEntity().getLocation().distance(loc) <= RTSPlugin.groupSelectionRadius){
				if(!rtsPlayer.selected.contains(unit))
					rtsPlayer.selected.add(unit);
				c++;
			}
		player.sendMessage(ChatColor.GRAY + "Selected " + c + " units");
	}

}
