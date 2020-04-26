/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ids.Controller;

import fr.ids.Network.Client;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 *
 * @author chouaib
 */
public class Images {
            
    public static Image P1_UP = new Image("/fr/ids/Resources/Images/p1-U.png");
    public static Image P1_DOWN = new Image("/fr/ids/Resources/Images/p1-D.png");
    public static Image P1_RIGHT= new Image("/fr/ids/Resources/Images/p1-R.png");
    public static Image P1_LEFT = new Image("/fr/ids/Resources/Images/p1-L.png");
    
    public static Image P2_UP = new Image("/fr/ids/Resources/Images/p2-U.png");
    public static Image P2_DOWN = new Image("/fr/ids/Resources/Images/p2-D.png");
    public static Image P2_RIGHT= new Image("/fr/ids/Resources/Images/p2-R.png");
    public static Image P2_LEFT = new Image("/fr/ids/Resources/Images/p2-L.png");
    
    public static Image P3_UP = new Image("/fr/ids/Resources/Images/p3-U.png");
    public static Image P3_DOWN = new Image("/fr/ids/Resources/Images/p3-D.png");
    public static Image P3_RIGHT= new Image("/fr/ids/Resources/Images/p3-R.png");
    public static Image P3_LEFT = new Image("/fr/ids/Resources/Images/p3-L.png");
    
    public static Image P4_UP = new Image("/fr/ids/Resources/Images/p4-U.png");
    public static Image P4_DOWN = new Image("/fr/ids/Resources/Images/p4-D.png");
    public static Image P4_RIGHT= new Image("/fr/ids/Resources/Images/p4-R.png");
    public static Image P4_LEFT = new Image("/fr/ids/Resources/Images/p4-L.png");
    
    
    public static Image getImage(int id, String move){
        Image img = null;
        switch(id){
            case 1:
                if(move == "UP") img = P1_UP;
                if(move == "DOWN") img = P1_DOWN;
                if(move == "RIGHT") img = P1_RIGHT;
                if(move == "LEFT") img = P1_LEFT;
                break;  
            case 2:
                if(move == "UP") img = P2_UP;
                if(move == "DOWN") img = P2_DOWN;
                if(move == "RIGHT") img = P2_RIGHT;
                if(move == "LEFT") img = P2_LEFT;
                break;
            case 3:
                if(move == "UP") img = P3_UP;
                if(move == "DOWN") img = P3_DOWN;
                if(move == "RIGHT") img = P3_RIGHT;
                if(move == "LEFT") img = P3_LEFT;    
                break;
            case 4:
                if(move == "UP") img = P4_UP;
                if(move == "DOWN") img = P4_DOWN;
                if(move == "RIGHT") img = P4_RIGHT;
                if(move == "LEFT") img = P4_LEFT;
                break;
        }
        return img;
    }
    
    public static Timeline explosionTimeline(ImageView player, ImageView gif){
                
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> { 
                    gif.setVisible(true);
                    gif.setLayoutX(player.getLayoutX());
                    gif.setLayoutY(player.getLayoutY()-1);
                }),
                new KeyFrame(Duration.seconds(0.5), e -> { 
                    gif.setVisible(false);
                })
        );
        
        return timeline;
    }

}
