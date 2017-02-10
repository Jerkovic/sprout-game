package com.binarybrains.sprout.entity.ai.pathfinding;



/**
 * A heuristic that uses the tile that is closest to the target
 * as the next best tile.
 */
public class ClosestHeuristic implements AStarHeuristic 
{

	public float getCost(TileBasedMap map, Mover mover, int x, int y, int tx, int ty)
	{		
		float dx = tx - x;
		float dy = ty - y;
		float result = (float) (Math.sqrt((dx*dx)+(dy*dy)));
		return result;
	}

}
