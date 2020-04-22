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

}
