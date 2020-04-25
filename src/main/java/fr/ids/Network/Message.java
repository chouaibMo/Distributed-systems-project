/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ids.Network;

import java.io.Serializable;

/**
 *
 * @author chouaib
 */
public class Message implements Serializable{
  
    private int playerID;
    private int playerX;
    private int playerY;
    private String senderQueue;
    private String direction;
    
    
    /**
     * Message contructor
     * @param id an integer : player ID
     * @param x an integer : x coordinate
     * @param y an integer : y coordinate
     */
    public Message(int id, String sender, int x, int y){ 
        this.playerID = id;
        this.senderQueue = sender;
        this.playerX = x;
        this.playerY = y;
    }
    
        
    /**
     * Message contructor
     * @param id an integer : player ID
     * @param x an integer : x coordinate
     * @param y an integer : y coordinate
     */
    public Message(int id, String sender, int x, int y, String direction){ 
        this.playerID = id;
        this.senderQueue = sender;
        this.direction = direction;
        this.playerX = x;
        this.playerY = y;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public void setSenderQueue(String senderQueue) {
        this.senderQueue = senderQueue;
    }

    public void setPlayerX(int playerX) {
        this.playerX = playerX;
    }

    public void setPlayerY(int playerY) {
        this.playerY = playerY;
    }

    public int getPlayerID() {
        return playerID;
    }
    
    public String getSenderQueue() {
        return senderQueue;
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }
   
    @Override
    public String toString(){
        return "Player no."+this.playerID+" : ("+this.playerX+";"+this.playerY+")";
    }
}