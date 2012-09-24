package com.amoebaman.mcrts.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import com.amoebaman.mcrts.RTSPlugin;
import com.amoebaman.mcrts.objects.EntityObjective;
import com.amoebaman.mcrts.objects.Unit;

public class RTSUnitListener implements Listener{
	
	@EventHandler
	public void deregisterDeadUnits(EntityDeathEvent event){
		Unit unit = RTSPlugin.getUnitFromUUID(event.getEntity().getUniqueId());
		if(unit == null)
			return;
		unit.die();
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
	
	@EventHandler
	public void preventFriendlyFire(EntityDamageEvent e){
		if(!(e instanceof EntityDamageByEntityEvent))
			return;
		EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
		Unit victim = RTSPlugin.getUnitFromUUID(event.getEntity().getUniqueId());
		if(victim == null)
			return;
		LivingEntity attacker = null;
		if(event.getDamager() instanceof LivingEntity)
			attacker = (LivingEntity) event.getEntity();
		if(event.getDamager() instanceof Projectile){
			Projectile proj = (Projectile) event.getDamager();
			if(proj.getShooter() != null)
				attacker = proj.getShooter();
		}
		if(attacker == null)
			return;
		if(victim.isFriendly(attacker))
			event.setCancelled(true);
	}

}
