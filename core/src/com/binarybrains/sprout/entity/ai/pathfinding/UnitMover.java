package com.binarybrains.sprout.entity.ai.pathfinding;


/**
 * A mover to represent one of a ID based unit in our example
 * game map
 *
 */
public class UnitMover implements Mover {
	/** The unit ID moving */
	private int type;
	
	/**
	 * Create a new mover to be used while path finder
	 * 
	 * @param type The ID of the unit moving
	 */
	public UnitMover(int type) {
		this.type = type;
	}
	
	/**
	 * Get the ID of the unit moving
	 * 
	 * @return The ID of the unit moving
	 */
	public int getType() {
		return type;
	}
}
