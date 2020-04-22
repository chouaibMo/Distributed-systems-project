/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ids.Server;
import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author chouaib
 */
public class Node {

    private Connection connection;
    private Channel channel;
    private int nodeID;
    //private ArrayList<Client> clients;
    private String privateQueue;
    private String publicQueue;
    private String nextNodeQueue;


    public Node(int id) throws IOException, TimeoutException, Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Queues.HOST);
        
        this.connection = factory.newConnection();
        this.channel = connection.createChannel();
        this.nodeID  = id;
        //this.clients = new ArrayList<>();
        this.privateQueue  = Queues.getPrivateQueue(id);
        this.publicQueue   = Queues.getPublicQueue(id);
        this.nextNodeQueue = Queues.getPrivateQueue((id%4) + 1);
        
        //Queues declarations :
        channel.queueDeclare(this.privateQueue, false, false, false, null);
        channel.queueDeclare(this.publicQueue, false, false, false, null);
        channel.queueDeclare(this.nextNodeQueue, false, false, false, null);
        
        //Consuming callback declaration :
        DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("From client :"+message);
            channel.basicPublish("", this.nextNodeQueue, null, message.getBytes("UTF-8"));
        };
        
        //Consuming callback declaration :
        DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("From Nodes :"+message);
            channel.basicPublish("", this.nextNodeQueue, null, message.getBytes("UTF-8"));
        };
        
        //Start consuming callback :
        channel.basicConsume(this.publicQueue, true, deliverCallback1, consumerTag -> { });
        channel.basicConsume(this.privateQueue, true, deliverCallback2, consumerTag -> { });
    }
    
    public static void main(String[] args) throws Exception {
        Node node1 = new Node(Integer.parseInt(args[0]));
        System.out.println("Node "+args[0]+" ready ...");
    }

}
