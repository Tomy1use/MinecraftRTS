package com.amoebaman.mcrts.objects;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.amoebaman.mcrts.RTSPlugin;

public class EntityObjective extends Objective{

	private static final long serialVersionUID = 1555503696344628852L;
	private UUID targetUUID;

	public EntityObjective(LivingEntity target, Aggression transitAggression, Aggression destinationAggression, int freedom, int sightRange) {
		super(transitAggression, destinationAggression, freedom, sightRange);
		targetUUID = target.getUniqueId();
	}
	
	public void update(Unit unit){
		Creature unitEntity = unit.getEntity();
		unit.moveTo(getTargetLoc());
		if(unit.getAggro() == Aggression.AGGRESSIVE_EXCLUSIVE)
			unit.targetEntity(getTargetEntity());
		if(unit.getAggro() == Aggression.AGGRESSIVE){
			LivingEntity closest = null;
			double distance = getSightRange();
			for(Entity other : unitEntity.getNearbyEntities(getSightRange(), getSightRange(), getSightRange()))
				if(RTSPlugin.armyUnits.contains(other.getType()) && !unit.isFriendly((LivingEntity) other)){
					double d = RTSPlugin.distance(unitEntity, (LivingEntity) other);
					if(d < distance){
						closest = (LivingEntity) other;
						distance = d;
					}
				}
			if(closest != null)
				unit.targetEntity(closest);
		}
	}
	
	public LivingEntity getTargetEntity(){ return RTSPlugin.getEntityFromUUID(targetUUID); }
	public Location getTargetLoc(){ return getTargetEntity().getLocation(); }

}
