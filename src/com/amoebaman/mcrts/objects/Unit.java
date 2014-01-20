package com.amoebaman.mcrts.objects;

import java.io.Serializable;
import java.util.UUID;

<<<<<<< HEAD
import net.minecraft.server.v1_4_6.PathEntity;
import net.minecraft.server.v1_4_6.PathPoint;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftLivingEntity;
=======
import net.minecraft.server.v1_6_R3.PathPoint;
import net.minecraft.server.v1_6_R3.PathEntity;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftLivingEntity;
>>>>>>> abfc5df6cf84ab463fedcc9810a8dc4168077c90
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.amoebaman.mcrts.RTSPlugin;
import com.amoebaman.mcrts.objects.Objective.Aggression;
import com.amoebaman.mcrts.utilities.S_Location;

public class Unit implements Serializable{

	private static final long serialVersionUID = -5529577632758754665L;
	private UUID entityUUID;
	private EntityType type;
	private RTSPlayer commander;
	private Objective objective;
	private S_Location lastLoc;

	public Unit(Creature entity, RTSPlayer commander){
		type = entity.getType();
		entityUUID = entity.getUniqueId();
		this.commander = commander;
		commander.army.add(this);
	}
	
	public void die(){
		commander.army.remove(this);
		commander.selected.remove(this);
		if(getEntity() != null)
			getEntity().remove();
		commander.getPlayer().sendMessage(ChatColor.GRAY + "A(n) " + type.toString().replace("_", " ").toLowerCase() + " in your army has died");
	}
	
	public UUID getUUID(){ return entityUUID; }
	public Creature getEntity(){ return (Creature) RTSPlugin.getEntityFromUUID(entityUUID); }
	public Location getLastLocation(){ return S_Location.deserialize(lastLoc); }
	public void setLastLocation(Location loc){ if(loc != null) lastLoc = S_Location.serialize(loc); }
	public EntityType getType(){ return type; }
	public String toString(){ return RTSPlugin.getEntityFromUUID(entityUUID).getType().getName() + " for " + commander.name; }
	public RTSPlayer getCommander(){ return commander; }
	public Objective getObjective(){ return objective; }
	public void setObjective(Objective newObjective){ objective = newObjective; }

	public boolean equals(Object other){
		if(!(other instanceof Unit))
			return false;
		Unit otherUnit = (Unit) other;
		return otherUnit.entityUUID.equals(entityUUID);
	}

	
	public boolean hasNewPathAI(){
		switch(type){
		case CHICKEN:
		case COW:
		case CREEPER:
		case IRON_GOLEM:
		case MUSHROOM_COW:
		case OCELOT:
		case PIG:
		case SHEEP:
		case SKELETON:
		case SNOWMAN:
		case VILLAGER:
		case WOLF:
		case ZOMBIE:
			return true;
		default:
			return false;
		}
	}

	@SuppressWarnings("unused")
	public void moveTo(Location target){
		if(target == null)
			return;
		Location loc = getEntity().getLocation();
		while(loc.distance(target) >= 16){
			target = target.clone().toVector().midpoint(loc.clone().toVector()).toLocation(loc.getWorld());
		}
		if(hasNewPathAI()){
			((CraftCreature) getEntity()).getHandle().getNavigation().a(target.getX(), target.getY(), target.getZ(), 0.25F);
			((CraftCreature) getEntity()).getHandle().b(false ? ((CraftLivingEntity) getEntity()).getHandle() : null);
		}
		else{
			((CraftCreature) getEntity()).getHandle().setPathEntity(new PathEntity(new PathPoint[] { new PathPoint(target.getBlockX(), target.getBlockY(), target.getBlockZ()) }));
			getEntity().setTarget(null);
		}
	}
	
	public void targetEntity(LivingEntity target){
		if(target == null)
			return;
		if(RTSPlugin.distance(getEntity(), target) >= 14)
			moveTo(target.getLocation());
		else{
			if(hasNewPathAI())
				((CraftCreature) getEntity()).getHandle().b(target instanceof CraftLivingEntity ? ((CraftLivingEntity) target).getHandle() : null);
			else
				getEntity().setTarget(target);
		}
	}
	
	public boolean isFriendly(LivingEntity other){
		if(other.equals(getEntity()))
			return true;
		if(other instanceof Player){
			RTSPlayer rtsPlayer = RTSPlugin.getRTSPlayer((Player) other);
			if(rtsPlayer.army.contains(this))
				return true;
		}
		Unit unit = RTSPlugin.getUnitFromUUID(other.getUniqueId());
		if(unit == null)
			return false;
		if(unit.commander.equals(commander))
			return true;
		return false;
	}
	
	public Aggression getAggro(){
		if(objective == null)
			return Aggression.PASSIVE_AGGRESSIVE;
		Location targetLoc = objective.getTargetLoc();
		if(targetLoc == null)
			return Aggression.PASSIVE_AGGRESSIVE;
		double distance = getEntity().getLocation().distance(targetLoc);
		if(distance > objective.getFreedom())
			return objective.getTransitAggression();
		else
			return objective.getDestinationAggression();

	}

}
