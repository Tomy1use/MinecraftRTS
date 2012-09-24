package com.amoebaman.mcrts.objects;

import java.io.Serializable;

import org.bukkit.Location;

public abstract class Objective implements Serializable{

	private static final long serialVersionUID = -1240019890047293472L;
	private Aggression transitAggression, destinationAggression;
	private int freedom, sightRange;
	
	protected Objective(Aggression transitAggression, Aggression destinationAggression, int freedom, int sightRange){
		this.transitAggression = transitAggression;
		this.destinationAggression = destinationAggression;
		this.freedom = freedom;
		this.sightRange = sightRange;
	}
	
	public Aggression getTransitAggression(){ return transitAggression; }
	public Aggression getDestinationAggression(){ return destinationAggression; }
	public int getSightRange(){ return sightRange; }
	public int getFreedom(){ return freedom; }
	public abstract Location getTargetLoc();
	public abstract void update(Unit unit);
	
	public enum Aggression{
		AGGRESSIVE_EXCLUSIVE,
		AGGRESSIVE,
		PASSIVE_AGGRESSIVE,
		PASSIVE,
		EVASIVE,
		;
	}

}
