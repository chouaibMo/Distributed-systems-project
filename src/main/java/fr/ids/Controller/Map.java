/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ids.Controller;

/**
 *
 * @author chouaib
 */
public class Map {
    
    static int[] posX = new int[4];
    static int[] posY = new int[4];
    
    static byte[][] map = {
                      {0,0,0,1,1,0,0,0,0,0,0,0},
                      {0,0,0,1,1,1,1,0,0,0,0,0},
                      {0,1,0,0,0,0,1,0,0,1,1,1},
                      {0,1,0,0,0,0,1,0,0,1,0,0},
                      {0,1,1,1,0,0,1,0,0,0,0,0},
                      {0,0,0,0,0,0,0,0,0,1,1,1},                    
                      {1,1,0,0,1,1,0,0,0,0,0,0},
                      {0,1,0,0,1,1,0,0,1,1,1,1},
                      {0,1,0,0,0,0,0,0,0,1,1,0},
                      {0,1,0,0,0,1,1,0,0,1,1,0},
                      {0,0,0,0,1,1,1,0,0,0,0,0},
                      {0,0,0,0,1,1,1,1,0,0,0,0}
                    };
    
    
    /**
     * This method is used to check whether a player has neighbour(s) 
     * @param x coordinate x of the player
     * @param y coordinate y of the player
     * @return true if the player in (x,y) has a least one neighbour, false otherwise
     */
    public static boolean hasNeighbour(int x, int y){
        return (x-1<=11 && x-1>=0 && y<=11 && y>=0 && map[x-1][y] == 2 ) ||  
               (x+1<=11 && x+1>=0 && y<=11 && y>=0 && map[x+1][y] == 2 ) || 
               (x<=11 && x>=0 && y-1<=11 && y-1>=0 && map[x][y-1] == 2 ) || 
               (x<=11 && x>=0 && y+1<=11 && y+1>=0 && map[x][y+1] == 2 );
    }
    /**
     * This method is used to check if a cell is free 
     * @param x coordinate x
     * @param y coordinate y
     * @return true if the cell i (x,y) is free, false otherwise
     */
    public static boolean isFree(int x, int y){
        return (map[x][y] == 0);
                
    }
    
    /**
     *  this method is used when a player is leaving the game
     * @param id player id
     */
    public static void setFree(int id){
        map[posX[id-1]][posY[id-1]] = 0;
        posX[id-1] = -1;
        posY[id-1] = -1;
    }
    
    /**
     * This method is used to update the map matrix accoding a player's new position
     * @param id the player id
     * @param x coordinate x
     * @param y coordinate y
     * @param val new value of the cell
     */
    public static void updateMap(int id, int x, int y, int val){
        if(posX[id-1] != -1 && posY[id-1] != -1)
            map[posX[id-1]][posY[id-1]] = 0;
        posX[id-1] = x;
        posY[id-1] = y;
        map[x][y] = (byte) val;
    }
    
   /**
     * This method is used to check if a movement will change the zone of the player
     * @param x integer : x coordinate
     * @param y integer : y coordinate
     * @param direction  move direction
     * @return true if the player at (x,y) change the zone after a movement , false otherwise
     */
    public static boolean isChangeZone(int x, int y, String direction){
        int zone = getZone(x,y);
        boolean result = false;
        switch(direction){
            case "UP":
                result = (zone != getZone(x-1,y));
                break;
            case "DOWN":
                result = (zone != getZone(x+1,y));
                break;
            case "LEFT":
                result = (zone != getZone(x,y-1));
                break;
            case "RIGHT":
                result = (zone != getZone(x,y+1));
                break;
        }
        return result;       
    }
    
    /**
     * This method is used get the zone no. from x and y coordinate
     * @param x integer : x coordinate
     * @param y integer : y coordinate
     * @return integer : the zone no. associated to (x,y)
     */
    public static int getZone(int x, int y){
        if(x <= 5 && y <= 5)
            return 1;
        if(x <= 5 && y > 5)
            return 2;
        if(x > 5 && y<= 5)
            return 3;
        else
            return 4;
    }

    public static void print(){
        for(int i=0; i<12; i++){
            for(int j=0; j<12; j++){
                System.out.print(map[i][j]+" ");
            }
            System.out.println("");
        }
        System.out.print("-------------------\n");
    }
    
}
