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
public class NodeMessage implements Serializable {
    
    private int sourceNode;
    private String destination;
    private Message message;
    
    public NodeMessage(int src, String dest, Message msg){
        this.sourceNode = src;
        this.destination = dest;
        this.message = msg;
    }

    public int getSourceNode() {
        return sourceNode;
    }

    public void setSourceNode(int sourceNode) {
        this.sourceNode = sourceNode;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
