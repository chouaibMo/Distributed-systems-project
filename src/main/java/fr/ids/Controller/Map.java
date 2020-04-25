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
     * This method is used to check if a cell is free 
     * @param x 
     * @param y
     * @return true if the cell i (x,y) is free, false otherwise
     */
    public static boolean isFree(int x, int y){
        return (map[x][y] == 0);
                
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

    
}
