/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ids.Application;

import fr.ids.Network.Node;


/**
 *
 * @author chouaib
 */
public class Server {
    
    public static void main(String[] args) throws Exception {
        System.out.println("Node 1 starting ...");
        Node node1 = new Node(1);
        System.out.println("Node 2 starting ...");
        Node node2 = new Node(2);
        System.out.println("Node 3 starting ...");
        Node node3 = new Node(3);
        System.out.println("Node 4 starting ...");
        Node node4 = new Node(4);
        System.out.println("Server is now operational ...");
    }
}
