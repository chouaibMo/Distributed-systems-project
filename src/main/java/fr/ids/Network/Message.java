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
  
    private int playerID;           // player id
    private int playerX;            // x coordinate of the player
    private int playerY;            // x coordinate of the player
    private String name;            // player name
    private String senderQueue;     // queue name of the player who sent the message
    private String direction;       // direction of the move performed by the player
    private String request;         // request of the message ( IN - OUT - DEFAULT )
    
        
    /**
     * Create a new Message instance
     * @param id the player ID
     * @param queue the queue name of the player 
     * @param x the x position of the player
     * @param name the player name
     * @param y the y position of the player
     * @param direction the movement performed by the player
     * @param request the request of the sender 
     */
    public Message(int id, String queue, String name, int x, int y, String direction, String request){ 
        this.playerID = id;
        this.senderQueue = queue;
        this.name = name;
        this.request = request;
        this.direction = direction;
        this.playerX = x;
        this.playerY = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    
    public String getRequest() {
        return request;
    }
    
    public void setRequest(String req) {
        request = req;
    }
   
    @Override
    public String toString(){
        return "Player no."+this.playerID+" : "+this.direction+" ("+this.playerX+";"+this.playerY+") "+this.request;
    }
}
