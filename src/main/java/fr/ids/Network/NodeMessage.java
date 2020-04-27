/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ids.Network;

import java.io.Serializable;

/**
 * This class is a wrapper of Message.
 * It is used only for communications in the server side
 * @author chouaib
 */
public class NodeMessage implements Serializable {
    
    private int sourceNode;
    private Message message;
    
    
    /**
     * Create a NodeMessage instance.
     * @param src id of the source node
     * @param msg the message to send
     */
    public NodeMessage(int src, Message msg){
        this.sourceNode = src;
        this.message = msg;
    }

    public int getSourceNode() {
        return sourceNode;
    }

    public void setSourceNode(int sourceNode) {
        this.sourceNode = sourceNode;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
