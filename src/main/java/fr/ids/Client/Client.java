/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ids.Client;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import fr.ids.Server.Queues;

/**
 *
 * @author chouaib
 */
public class Client {
    private Connection connection;
    private Channel channel;
    private String username;
    private String clientQueue;
    private String nodeQueue;
    
    private int clientID;
    
    public Client(String name, int id) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Queues.HOST);
        
        this.connection = factory.newConnection();
        this.channel = connection.createChannel();
        this.username = name;
        this.clientID = id;
        this.clientQueue = Queues.getClientQueue(id);
        this.nodeQueue   = Queues.getPublicQueue(id);
        
         //Queues declarations :
        channel.queueDeclare(this.nodeQueue, false, false, false, null);
        channel.queueDeclare(this.clientQueue, false, false, false, null);
        
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(message);
        };
        
        channel.basicConsume(this.clientQueue, true, deliverCallback, consumerTag -> { });
    }
    
    
    public static void main(String[] args) throws Exception {
        Client cl = new Client("kopp", Integer.parseInt(args[0]));
        System.out.println("Client "+cl.clientID+" started ...");
        String message = "UP";
        cl.channel.basicPublish("", cl.nodeQueue, null, message.getBytes("UTF-8"));
    }
    
    public void setNodeQueue(String queue){
        this.nodeQueue = queue;
    }
    
    public void setUsername(String name){
        this.username = name;
    }
    
}
