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
            this.factory.setUri("amqp://cjgpefjw:97sGX0az9f63oY0jdO8FNbQgTOlSgqOe@chinook.rmq.cloudamqp.com/cjgpefjw");
        }catch(Exception e){
            System.out.println("Client constructor : "+e);
        }
    
    }
        
    
    /**
     * This methode is used to get a Client object (singleton)
     * @return a Client object
     */
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
     * This method is used to send a message to a specific queue
     * @param message a String
     * @param queueName a String
     * @throws Exception 
     */
    public void sendMessage(Message message, String queueName) throws Exception {
        this.channel.basicPublish("", queueName, null, SerializationUtils.serialize(message));
    }
    
    
    /**
     * This Method is used to send a message to the node connected to this client
     * @param message a String
     * @throws Exception 
     */
    public void sendMessage(Message message) throws Exception {
        this.channel.basicPublish("", this.nodeQueue, null, SerializationUtils.serialize(message));
    }   
    
   
    /**
     * This method is used to set up 
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
             /*
            //Callback declaration :
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                Message msg = SerializationUtils.deserialize(delivery.getBody());
                
                System.out.println("[ FROM "+this.nodeQueue+" ] --> "+msg);
            };
        

            //Callback starting : 
            channel.basicConsume(this.clientQueue, true, deliverCallback, consumerTag -> { });
            */
        }
        catch(Exception e){
            System.out.println(e);
            System.exit(0);
        }
    }
    
    
    /**
     * This method is used to close the connection (channel also)
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
     * 
     * @param dc
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
