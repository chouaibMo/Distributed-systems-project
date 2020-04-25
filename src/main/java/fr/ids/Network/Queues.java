/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ids.Network;

/**
 *
 * @author chouaib
 */
public class Queues {
    //nodes number
    public static int NB_NODES = 2;
    // Host name
    public static String HOST = "localhost";
    
    // Player's queue names
    public static String P1_QUEUE = "player1_queue";
    public static String P2_QUEUE = "player2_queue";
    public static String P3_QUEUE = "player3_queue";
    public static String P4_QUEUE = "player4_queue";
    
    //node's queue names
    public static String N1_QUEUE = "node1_queue";
    public static String N2_QUEUE = "node2_queue";
    public static String N3_QUEUE = "node3_queue";
    public static String N4_QUEUE = "node4_queue";
    
    //node's public queue names
    public static String NP1_QUEUE = "node1_pub_queue";
    public static String NP2_QUEUE = "node2_pub_queue";
    public static String NP3_QUEUE = "node3_pub_queue";
    public static String NP4_QUEUE = "node4_pub_queue";
    

    /**
     * This method is used to get the public queue associated to a node
     * @param id node identifier
     * @return the name of public queue associated to the node
     * @throws Exception if the node identifier is not valid
     */
    public static String getPublicQueue(int id) throws Exception {
        String queue;
        switch(id){
            case 1:
                queue = NP1_QUEUE;
                break;
            case 2:
                queue = NP2_QUEUE;
                break;
            case 3:
                queue = NP3_QUEUE;
                break;
            case 4:
                queue = NP4_QUEUE;
                break;   
            default:
                throw new Exception("node id is not valid");
        }
        return queue;
    }
    
    /**
     * This method is used to get the private queue associated to a node
     * @param id node identifier
     * @return the name of private queue associated to the node
     * @throws Exception if the node identifier is not valid
     */
    public static String getPrivateQueue(int id) throws Exception {
        String queue;
        switch(id){
            case 1:
                queue = N1_QUEUE;
                break;
            case 2:
                queue = N2_QUEUE;
                break;
            case 3:
                queue = N3_QUEUE;
                break;
            case 4:
                queue = N4_QUEUE;
                break;   
            default:
                throw new Exception("node id is not valid");
        }
        return queue;
    }
    
   /**
     * This method is used to get the of the queue associated to a client
     * @param id client identifier
     * @return the name of the queue associated to the client
     * @throws Exception if the client identifier is not valid
     */
    public static String getClientQueue(int id) throws Exception {
        String queue;
        switch(id){
            case 1:
                queue = P1_QUEUE;
                break;
            case 2:
                queue = P2_QUEUE;
                break;
            case 3:
                queue = P3_QUEUE;
                break;
            case 4:
                queue = P4_QUEUE;
                break;   
            default:
                throw new Exception("node id is not valid");
        }
        return queue;
    }
}
