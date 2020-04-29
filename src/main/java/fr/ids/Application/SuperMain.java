/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ids.Application;

/**
 *
 * @author chouaib
 */
public class SuperMain {
    public static void main(String[] args) {
        
        if(args.length < 1 || Integer.parseInt(args[0]) < 0 || Integer.parseInt(args[0]) > 4){
            System.out.println("one argument is required (0 - 4) ");
            System.out.println("usage : java -jar <jarname>.jar <userID> ");
            System.exit(0);
        }
        
        main.main(args);
    }
}

