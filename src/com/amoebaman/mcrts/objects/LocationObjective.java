package com.amoebaman.mcrts.objects;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.amoebaman.mcrts.RTSPlugin;
import com.amoebaman.mcrts.utilities.S_Location;

public class LocationObjective extends Objective{

	private static final long serialVersionUID = -3757619871062896606L;
	private S_Location targetLoc;

	public LocationObjective(Location target, Aggression transitAggression, Aggression destinationAggression, int freedom, int sightRange) {
		super(transitAggression, destinationAggression, freedom, sightRange);
		targetLoc = S_Location.serialize(target);
	}
	
	public void update(Unit unit){
		Creature unitEntity = unit.getEntity();
		if(unitEntity == null){
			if(unit.getLastLocation() != null && !unit.getLastLocation().getChunk().isLoaded())
				return;
			else
				unit.die();
		}
		unit.setLastLocation(unitEntity.getLocation());
		unit.moveTo(getTargetLoc());
		if(unit.getAggro() == Aggression.AGGRESSIVE){
			LivingEntity closest = null;
			double distance = getSightRange();
			for(Entity other : unitEntity.getNearbyEntities(getSightRange(), getSightRange()/3, getSightRange()))
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
	
	public Location getTargetLoc(){ return S_Location.deserialize(targetLoc); }

}
