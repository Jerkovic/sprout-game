package com.binarybrains.sprout.level.caves;


import com.badlogic.gdx.math.MathUtils;

public class Map
{
    public int width = 32;
    public int height = 32;
    public Integer map[][] = new Integer[width][height];


    public Map() {
        init();
    }

    //Returns the number of cells in a ring around (x,y) that are alive.
    public int countAliveNeighbours(Integer[][] map, int x, int y){
        int count = 0;
        for(int i=-1; i < 2; i++){
            for(int j=-1; j < 2; j++){
                int neighbour_x = x+i;
                int neighbour_y = y+j;
                //If we're looking at the middle point
                if(i == 0 && j == 0){
                    //Do nothing, we don't want to add ourselves in!
                }
                //In case the index we're looking at it off the edge of the map
                else if(neighbour_x < 0 || neighbour_y < 0 || neighbour_x >= map.length || neighbour_y >= map[0].length){
                    count = count + 1;
                }
                //Otherwise, a normal check of the neighbour
                else if(map[neighbour_x][neighbour_y] == 1){
                    count = count + 1;
                }
            }
        }
        return count;
    }


    public void init() {
        // Our random cave before any cellular automaton simulation steps.
        float chanceToStartAlive = 0.45f;
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                if(MathUtils.random(0f, 1f) < chanceToStartAlive){
                    map[x][y] = 1; // solid rock
                }
                else {
                    map[x][y] = 0; // walk space
                }
            }
        }
    }

    public void debug() {
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                if (map[x][y] == 1)
                    System.out.print("8"); // wall
                else if (map[x][y] == 7)
                    System.out.print("X"); // a valid entity start pos
                else System.out.print(" ");
            }
            System.out.println("");
        }

    }

    public Integer[][] generateMap(){

        for(int i=0; i< 50; i++){
            map = doSimulationStep(map);
        }
        return map;
    }

    public Integer[][] doSimulationStep(Integer[][] oldMap){

        Integer[][] newMap = new Integer[width][height];
        int killFewNeighbours = 5;
        int birthLimit = 3;
        //Loop over each row and column of the map
        for(int x=0; x < oldMap.length; x++){
            for(int y=0; y < oldMap[0].length; y++){
                int nbs = countAliveNeighbours(oldMap, x, y);
                //The new value is based on our simulation rules
                //First, if a cell is alive but has too few neighbours, kill it.
                if(oldMap[x][y]== 1) {
                    if(nbs < killFewNeighbours){
                        newMap[x][y] = 0;
                    }
                    else{
                        newMap[x][y] = 1;
                    }
                } //Otherwise, if the cell is dead now, check if it has the right number of neighbours to be 'born'
                else{
                    if(nbs > birthLimit){
                        newMap[x][y] = 1;
                    }
                    else{
                        newMap[x][y] = 0;
                    }
                }
            }
        }
        return newMap;
    }

    public void edges() {
        for (int x = 0; x < width; x++) {
            map[x][0] = 1; // top
            map[x][height-1] = 1; // bottom
        }
        for (int y = 0; y < height; y++) {
            map[0][y] = 1; // left
            map[width-1][y] = 1; // right
        }
    }

    public void placeEntity(){
        for (int x=0; x < width; x++){
            for (int y=0; y < height; y++){
                if(map[x][y] == 0){
                    int nbs = countAliveNeighbours(map, x, y);
                    if(nbs < 1){
                        map[x][y] = 7; // place player pos
                        return;
                    }
                }
            }
        }
    }


    public static void main(String[] args)
    {
        Map cave = new Map();
        cave.generateMap();
        cave.edges();
        cave.placeEntity();
        cave.debug();


        // System.out.println(cave.countAliveNeighbours(cave.map, 1,1));
        System.exit(0);
    }

}