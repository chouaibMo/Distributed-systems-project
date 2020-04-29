/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ids.Network;
import com.rabbitmq.client.*;
import fr.ids.Controller.Map;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.lang3.SerializationUtils;

/**
 *
 * @author chouaib
 */
public class Node {

    private Connection connection;
    private Channel channel;
    private final int nodeID;
    private ArrayList<String> clientQueues;         //list of queue names of clients managed by this node
    private String privateQueue;                    // private queue name of the node
    private String publicQueue;                     // public  queue name of the node
    private String nextNodeQueue;                   // queue name of the next node
    
    private Message[] lastMessages ;                // array of last message sent by each client

    public Node(int id) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        //factory.setHost(Queues.HOST);
        factory.setUri(Queues.URI);
        
        this.connection = factory.newConnection();
        this.channel = connection.createChannel();
        this.nodeID  = id;
        this.clientQueues = new ArrayList<>();
        this.privateQueue  = Queues.getPrivateQueue(id);
        this.publicQueue   = Queues.getPublicQueue(id);
        this.nextNodeQueue = Queues.getPrivateQueue((id % Queues.NB_NODES ) + 1);
        
        //Queues declarations :
        channel.queueDeclare(this.privateQueue, false, false, false, null);
        channel.queueDeclare(this.publicQueue, false, false, false, null);
        channel.queueDeclare(this.nextNodeQueue, false, false, false, null);
        
        channel.queuePurge(privateQueue);
        channel.queuePurge(publicQueue);
        
        lastMessages = new Message[4];
        
        StartClientCallBack();
        StartNodeCallBack();
    }
       
    
    /**
     * This message to send les list of last message of each client, to the client that
     * just joined the party.
     */
    public void sendLastMessagesList(String clientQueue) throws IOException{
        for(int i=0; i<4; i++){
            if(lastMessages[i] != null){
                channel.basicPublish("", clientQueue, null, SerializationUtils.serialize(lastMessages[i]));
            }
        }
    }
    
    /**
     * Updated the list of last message send by each client
     * @param id client ID
     * @param msg the last message sent by this client
     */
    public void updateMsgList(int id, Message msg){
        lastMessages[id-1] = msg;
    }
    
    
    /**
     * This method is used to send a message to all clients managed by this node
     * (exept the client who sent this message)
     * @param msg the message to send
     * @throws IOException 
     */
    public void publishToClients(Message msg) throws IOException{
        for(String queue : clientQueues)
            if( !msg.getSenderQueue().equals(queue))
                channel.basicPublish("", queue, null, SerializationUtils.serialize(msg));
    }
    
    
    /**
     * Declaration of the Callback that will be launched when the node receive a message from
     * another node, and start consuming.
     * @throws IOException 
     */
    public void StartNodeCallBack() throws IOException{
        //Node callback declaration :
        DeliverCallback deliverCallbackServer = (consumerTag, delivery) -> {
            NodeMessage nodemsg = SerializationUtils.deserialize(delivery.getBody());
            Message msg= nodemsg.getMessage();
            
            if(nodemsg.getSourceNode() != this.nodeID){
                System.out.println("[ NODE "+nodeID+" : FROM NODE "+nodemsg.getSourceNode()+" ] --> "+msg);
                if( this.nodeID == Map.getZone(msg.getPlayerX(), msg.getPlayerY()) ) 
                    clientQueues.add(msg.getSenderQueue());            // assign the client to this node if he is this node's zone                    
                else 
                    clientQueues.remove(msg.getSenderQueue());        // else, remove client from this node's list of client
                
                if(msg.getRequest().equals("OUT")){
                    lastMessages[msg.getPlayerID()-1] = null;              // if a player if leaving the game, remove it's queue name
                    clientQueues.remove(msg.getSenderQueue());             // and it's last message from the node structures
                }
                
                updateMsgList(msg.getPlayerID(),msg);
                
                //publish the message to all the clients managed by this node :
                publishToClients(msg);
                
                //publish the message to the next node :
                channel.basicPublish("", this.nextNodeQueue, null, SerializationUtils.serialize(nodemsg));
            }
        };
        
        //Start consuming  :
        channel.basicConsume(this.privateQueue, true, deliverCallbackServer, consumerTag -> { });
        
    }
    
    /**
     * Declaration of the Callback that will be launched when the node receive a message from
     * a client, and start consuming.
     * @throws IOException 
     */
    public void StartClientCallBack() throws IOException{
        //Client callback declaration :
        DeliverCallback deliverCallbackClient = (consumerTag, delivery) -> {
            Message msg = SerializationUtils.deserialize(delivery.getBody());
            System.out.println("[ NODE "+nodeID+" : FROM CLIENT "+msg.getPlayerID()+" ] --> "+msg);
            
            //updating clients managed by the node :
            if(!clientQueues.contains(msg.getSenderQueue()))
                if( this.nodeID == Map.getZone(msg.getPlayerX(), msg.getPlayerY()) )
                    clientQueues.add(msg.getSenderQueue());
            else if( this.nodeID != Map.getZone(msg.getPlayerX(),msg.getPlayerY()) )
                clientQueues.remove(msg.getSenderQueue());
            
            //System.out.println("[ NODE "+nodeID+" CLIENTS ] --> "+clientQueues);
            
            updateMsgList(msg.getPlayerID(),msg);
            
            if(msg.getRequest().equals("IN"))
                sendLastMessagesList(msg.getSenderQueue());
            
            if(msg.getRequest().equals("OUT")){
                lastMessages[msg.getPlayerID()-1] = null;
                clientQueues.remove(msg.getSenderQueue());
            }
                
            //Notify other clients :
            publishToClients(msg);
            
            //Notify other nodes  : 
            NodeMessage nodemsg = new NodeMessage(this.nodeID, msg);             // wrap the client message in a Node message
            channel.basicPublish("", this.nextNodeQueue, null, SerializationUtils.serialize(nodemsg));
        };
        
        //Start consuming :
        channel.basicConsume(this.publicQueue, true, deliverCallbackClient, consumerTag -> { });
    }
    
    
    public static void main(String[] args) throws Exception {
        Node node1 = new Node(Integer.parseInt(args[0]));
        System.out.println("Node "+args[0]+" ready ...");
    }
   
}
