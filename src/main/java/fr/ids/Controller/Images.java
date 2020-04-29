/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ids.Controller;

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
                if("UP".equals(move))    img = P1_UP;
                if("DOWN".equals(move))  img = P1_DOWN;
                if("RIGHT".equals(move)) img = P1_RIGHT;
                if("LEFT".equals(move))  img = P1_LEFT;
                break;  
            case 2:
                if("UP".equals(move))    img = P2_UP;
                if("DOWN".equals(move))  img = P2_DOWN;
                if("RIGHT".equals(move)) img = P2_RIGHT;
                if("LEFT".equals(move))  img = P2_LEFT;
                break;
            case 3:
                if("UP".equals(move))    img = P3_UP;
                if("DOWN".equals(move))  img = P3_DOWN;
                if("RIGHT".equals(move)) img = P3_RIGHT;
                if("LEFT".equals(move))  img = P3_LEFT;    
                break;
            case 4:
                if("UP".equals(move))    img = P4_UP;
                if("DOWN".equals(move))  img = P4_DOWN;
                if("RIGHT".equals(move)) img = P4_RIGHT;
                if("LEFT".equals(move))  img = P4_LEFT;
                break;
        }
        return img;
    }
    
    /**
     * return a timeline that plays an explosion gif image
     * @param player ImageView of the player who caused the explosion
     * @param gif ImageView of the explosion
     * @return the timeline
     */
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
