/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ids.Network;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import org.apache.commons.lang3.SerializationUtils;

/**
 *
 * @author chouaib
 */
public class Client implements AutoCloseable{
    private Connection connection;
    private Channel channel;
    private ConnectionFactory factory;
    private String username;
    private String clientQueue;
    private String nodeQueue;
    private int clientID;
    
    
    //Client singleton : 
    private static Client INSTANCE = new Client();
    
    
    /**
     * Client constructor
     */
    public Client(){
        this.factory = new ConnectionFactory();
        //this.factory.setHost(Queues.HOST);
        try{
            this.factory.setUri(Queues.URI);
        }catch(Exception e){
            System.out.println("Client constructor : "+e);
        }
    
    }
        
    public static Client getClient(){
        return INSTANCE;
    }

    public Channel getChannel() {
        return channel;
    }
   
    public void setClientQueue(String queue){
        this.clientQueue = queue;
    }
    
    public String getClientQueue(){
        return this.clientQueue;
    }

    public void setNodeQueue(String queue){
        this.nodeQueue = queue;
    }
    
    public String getNodeQueue(){
        return this.nodeQueue;
    }
    
    public void setID(int id){
        this.clientID = id;
    }
    
    public int getID(){
        return this.clientID;
    }
    
    public void setUsername(String name){
        this.username = name;
    }
    
    public String getUsername(){
        return this.username;
    } 
    
    /**
     * This Method is used to send a message to the node managing the 
     * zone where the player is.
     * @param message the Message object to send 
     * @throws Exception 
     */
    public void sendMessage(Message message) throws Exception {
        this.channel.basicPublish("", this.nodeQueue, null, SerializationUtils.serialize(message));
    }   
    
   
    /**
     * This method is used to set up the connection, channel, and declare que client queues.
     */
    public void setUp(){
        try{
            this.connection = this.factory.newConnection();
            this.channel = connection.createChannel();

            this.clientQueue = Queues.getClientQueue(this.clientID);
            this.nodeQueue   = Queues.getPublicQueue(this.clientID);

            //Queues declarations :
            channel.queueDeclare(this.nodeQueue, false, false, false, null);
            channel.queueDeclare(this.clientQueue, false, false, false, null);
            
            channel.queuePurge(this.clientQueue);
        }
        catch(Exception e){
            System.out.println("Client setUp : "+e);
            System.exit(0);
        }
    }
    
    
    /**
     * This method is used to close the channel and/or the connection
     * @throws Exception 
     */
    @Override
    public void close() throws Exception {
        if(channel != null)
            channel.close();
        if(connection != null)
            connection.close();
    }
    
    
    /**
     * This method is used to set the callback and start consuming messages from the client queue.
     * @param dc the deliverCallback to execute after consuming a message
     * @throws java.io.IOException
     */
    public void startConsuming(DeliverCallback dc) throws IOException{
        try{
            channel.basicConsume(this.clientQueue, true, dc, consumerTag -> { });
        }
        catch(IOException e){
            System.out.println("client (startConsuming) : "+e);
        }
    }
}
