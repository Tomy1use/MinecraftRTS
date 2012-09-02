package com.amoebaman.mcrts.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import com.amoebaman.mcrts.RTSPlugin;
import com.amoebaman.mcrts.objects.EntityObjective;
import com.amoebaman.mcrts.objects.RTSPlayer;
import com.amoebaman.mcrts.objects.Unit;

public class RTSUnitListener implements Listener{
	
	@EventHandler
	public void deregisterDeadUnits(EntityDeathEvent event){
		Unit unit = RTSPlugin.getUnitFromUUID(event.getEntity().getUniqueId());
		if(unit == null)
			return;
		RTSPlayer rtsPlayer = unit.getCommander();
		rtsPlayer.army.remove(unit);
		rtsPlayer.selected.remove(unit);
		rtsPlayer.getPlayer().sendMessage(ChatColor.GRAY + "A(n) " + unit.getType().toString().replace("_", " ").toLowerCase() + " in your army has died");
	}
	
	@EventHandler
	public void manageEntityTargeting(EntityTargetLivingEntityEvent event){
		Unit unit = RTSPlugin.getUnitFromUUID(event.getEntity().getUniqueId());
		if(unit == null)
			return;
		switch(unit.getAggro()){
		case AGGRESSIVE_EXCLUSIVE:
			if(unit.getObjective() instanceof EntityObjective)
				if(!event.getTarget().equals(((EntityObjective) unit.getObjective()).getTargetEntity()))
					event.setCancelled(true);
			break;
		case AGGRESSIVE:
		case PASSIVE_AGGRESSIVE:
			event.setCancelled(false);
			break;
		case PASSIVE:
		case EVASIVE:
			event.setCancelled(true);
		if(unit.isFriendly(event.getTarget()))
			event.setCancelled(true);
		}
	}

}
