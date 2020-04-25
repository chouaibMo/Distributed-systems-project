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
import java.util.concurrent.TimeoutException;
import org.apache.commons.lang3.SerializationUtils;

/**
 *
 * @author chouaib
 */
public class Node {

    private Connection connection;
    private Channel channel;
    private final int nodeID;
    private ArrayList<String> clientQueues;
    private String privateQueue;
    private String publicQueue;
    private String nextNodeQueue;


    public Node(int id) throws IOException, TimeoutException, Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Queues.HOST);
        
        this.connection = factory.newConnection();
        this.channel = connection.createChannel();
        this.nodeID  = id;
        this.clientQueues = new ArrayList<>();
        this.privateQueue  = Queues.getPrivateQueue(id);
        this.publicQueue   = Queues.getPublicQueue(id);
        this.nextNodeQueue = Queues.getPrivateQueue((id% Queues.NB_NODES ) + 1);
        
        //Queues declarations :
        channel.queueDeclare(this.privateQueue, false, false, false, null);
        channel.queueDeclare(this.publicQueue, false, false, false, null);
        channel.queueDeclare(this.nextNodeQueue, false, false, false, null);
        
        channel.queuePurge(privateQueue);
        channel.queuePurge(publicQueue);
        
        //Client message callback declaration :
        DeliverCallback deliverCallbackClient = (consumerTag, delivery) -> {
            //deserialization of the message :
            Message msg = SerializationUtils.deserialize(delivery.getBody());
            System.out.println("[ FROM CLIENT"+msg.getPlayerID()+" ] --> "+msg);
            
            //updating clients managed by the node :
            if(!clientQueues.contains(msg.getSenderQueue()))
                if( this.nodeID == Map.getZone(msg.getPlayerX(), msg.getPlayerY()) )
                    clientQueues.add(msg.getSenderQueue());
            else if( this.nodeID != Map.getZone(msg.getPlayerX(),msg.getPlayerY()) )
                clientQueues.remove(msg.getSenderQueue());
            
            //Notify other clients :
            publishToClients(msg);
            
            //Notify other nodes  : 
            NodeMessage nodemsg = new NodeMessage(this.nodeID, "all", msg);
            channel.basicPublish("", this.nextNodeQueue, null, SerializationUtils.serialize(nodemsg));
        };
        
        //Server message callback declaration :
        DeliverCallback deliverCallbackServer = (consumerTag, delivery) -> {
            NodeMessage nodemsg = SerializationUtils.deserialize(delivery.getBody());
            Message msg= nodemsg.getMessage();
            if(nodemsg.getSourceNode() != this.nodeID){
                System.out.println("[ FROM NODE"+nodemsg.getSourceNode()+" ] --> "+msg);
                if( this.nodeID == Map.getZone(msg.getPlayerX(), msg.getPlayerY()) )
                    clientQueues.add(msg.getSenderQueue());
                publishToClients(msg);
                channel.basicPublish("", this.nextNodeQueue, null, SerializationUtils.serialize(nodemsg));
            }
        };
        
        //Start consuming callback :
        channel.basicConsume(this.publicQueue, true, deliverCallbackClient, consumerTag -> { });
        channel.basicConsume(this.privateQueue, true, deliverCallbackServer, consumerTag -> { });
    }
       
    
    public void publishToClients(Message msg) throws IOException{
        for(String queue : clientQueues)
            if( !msg.getSenderQueue().equals(queue))
                channel.basicPublish("", queue, null, SerializationUtils.serialize(msg));
    }
    
    public static void main(String[] args) throws Exception {
        Node node1 = new Node(Integer.parseInt(args[0]));
        System.out.println("Node "+args[0]+" ready ...");
    }
   
}
