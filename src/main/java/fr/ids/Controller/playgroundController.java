/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ids.Controller;

import com.rabbitmq.client.DeliverCallback;
import fr.ids.Application.main;
import fr.ids.Network.Client;
import static fr.ids.Controller.homeController.media;
import fr.ids.Network.Message;
import fr.ids.Network.Queues;
        
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.Lighting;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.lang3.SerializationUtils;

/**
 *
 * @author chouaib
 */
public class playgroundController implements Initializable{

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button quitBtn;
    @FXML
    private Button helpBtn;
    @FXML
    private AnchorPane componentsPane;
    @FXML
    private AnchorPane helpPane;
    @FXML
    private AnchorPane mapPane;
    @FXML
    private Button helpCloseBtn;
    @FXML
    private Label name1;
    @FXML
    private Label name2;
    @FXML
    private Label name3;
    @FXML
    private Label name4;
    @FXML
    private Label helloLabel;  
    @FXML
    private ImageView player1;
    @FXML
    private ImageView player4;
    @FXML
    private ImageView player3;
    @FXML
    private ImageView player2;
    @FXML
    private ImageView gif;
    
    // CLIENT :
    private Client client;
    
    // PLAYER NAME :
    private String name;
    
    // COORDINATES : 
    private int x;
    private int y;
    
    // REF. TO CLIENT PLAYER : 
    private ImageView player;
    

   @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        this.client = Client.getClient();              
        this.client.setID(main.getArgs());
        this.client.setUsername(homeController.name);
        this.client.setUp();                                     // set up client's connection, channel and queues
        
        this.name = homeController.name;
        
        init(client.getID());
        
        //Callback launched when a message is consumed 
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            Message msg = SerializationUtils.deserialize(delivery.getBody());             //deserialization of the message
            if(msg.getRequest().equals("OUT")){                                           // if a player is leaving, remove it's name
                getPlayer(msg.getPlayerID()).setVisible(false);                           // remove this player's image
                Map.setFree(msg.getPlayerID());                                           // remove him from the map matrix
                Platform.runLater( () -> {  updateName(msg.getPlayerID(), " - ");  });
            }
            if(msg.getRequest().equals("HELLO")){                                                                 // if it's a hello message
                Platform.runLater( () -> { 
                    Images.timeline( getPlayer(msg.getPlayerID()), Images.getLabel(mapPane), mapPane ).play();    // then play the timeline
                 });
            }
            if(msg.getRequest().equals("DEFAULT") || msg.getRequest().equals("IN")){
                Platform.runLater( () -> { 
                    Map.updateMap(msg.getPlayerID(), msg.getPlayerX(), msg.getPlayerY(), 2);                    //update the map matrix
                    updateLayouts(msg.getPlayerID(), msg.getPlayerX(), msg.getPlayerY(), msg.getDirection());   //update player's layouts
                    updateName(msg.getPlayerID(), msg.getName());  });                                          // update it's name
            }
        };
        
        //start consuming according to the callback above :
        client.startConsuming(deliverCallback);
        
        //Send a message to notify the node that the player joined the game
        Message m = new Message(client.getID(), client.getClientQueue(), this.name, this.x, this.y,"DOWN", "IN");
        client.sendMessage(m);
        
        //Update the map matrix with the position of the player
        Map.updateMap(client.getID(), this.x, this.y, 2);
    }

    /**
     * This handler is executed when a key is pressed
     * @param event a KeyEvent
     * @throws Exception 
     */
    @FXML
    private void keyPressed(KeyEvent event) throws Exception {
        String key = event.getCode().toString();
        if( "UP".equals(key) || "DOWN".equals(key) || "RIGHT".equals(key) || "LEFT".equals(key))   //if the key is a keybord arrow
            makeMovement(player,key);                                                              // execute makeMovement method
        else if("SPACE".equals(key)){
            if(Map.hasNeighbour(this.x, this.y)){                                                //if the player has a neighbour
                Images.timeline(player, Images.getLabel(mapPane), mapPane).play();              // player the timeline then send a hello msg
                Message m = new Message(client.getID(), client.getClientQueue(), this.name, this.x, this.y,"", "HELLO");
                client.sendMessage(m);
            }
        }
           
    }
    
    /**
     * This handler is executed when the button quit is pressed
     * @param e an ActionEvent
     * @throws Exception 
     */
    @FXML
    private void quitOnAction(ActionEvent e) throws Exception {
        media.playClip();                        //
        Lighting light = new Lighting();
        light.setDiffuseConstant(0.7);
        componentsPane.setEffect(light);
        ButtonType yesBtn = new ButtonType("yes");
        ButtonType cancelBtn = new ButtonType("Cancel");
        Alert alert = new Alert(AlertType.WARNING, "Are you sure you want to quit ?", yesBtn, cancelBtn);
        alert.showAndWait();
        media.playClip();

        if (alert.getResult() == yesBtn) {
            Message m = new Message(client.getID(), client.getClientQueue(), this.name, this.x, this.y, "DOWN", "OUT");
            client.sendMessage(m);
            Client.getClient().close();
            Parent root = FXMLLoader.load(getClass().getResource("/fr/ids/Vue/home.fxml"));
            fadeTransition(root,1200,0,1);
            Stage stage = (Stage) quitBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        }
        componentsPane.setEffect(null);
        
    }

    /**
     * this handler is executed when the help button is pressed
     * @param event an ActionEvent
     */
    @FXML
    private void helpOnAction(ActionEvent event) {
        media.playClip();                       //play a clic sound
        Lighting light = new Lighting();        
        light.setDiffuseConstant(0.7);
        componentsPane.setEffect(light);       //set a dark effect on componentsPane
        helpPane.setVisible(true);             //show the helpPane
    }
    
    
    /**
     * This handler is executed when the close button is pressed
     * @param event an ActionEvent
     */
    @FXML
    private void helpCloseOnAction(ActionEvent event) {
        media.playClip();                    //player a clic sound
        componentsPane.setEffect(null);      //remove dark effect
        helpPane.setVisible(false);          //remove the helpPane
    }
     
    /**
     * This method is used when the player try to make a mouvement 
     * @param player an ImageView
     * @param direction a String : up, down, left or right
     * @throws Exception 
     */
    public void makeMovement(ImageView player, String direction) throws Exception {
        double lX = player.getLayoutX();                                                  //current layoutX
        double lY = player.getLayoutY();                                                  //current layoutY
        
        int id = client.getID();                                                          //player id
        String queue = client.getClientQueue();                                           //client queue
        
        switch(direction){
            case "UP":
                player.setImage(Images.getImage(id, direction));                              //change the player image 
                if(x > 0 && Map.isFree(x-1, y)){
                    if(Map.isChangeZone(x, y, direction)){                                    //if the player is moving to another zone
                        client.setNodeQueue(Queues.getPublicQueue(Map.getZone(x-1, y)));      //then change it's client nodeQueue
                    }
                    player.setLayoutY(lY-46.5);                                               //update layouts of player's image
                    x--;
                }
                break;
            case "DOWN":
                player.setImage(Images.getImage(id, direction));
                if(x < 11 && Map.isFree(x+1, y)){
                    if(Map.isChangeZone(x, y, direction)){                                    // idem if moving down
                        client.setNodeQueue(Queues.getPublicQueue(Map.getZone(x+1, y)));
                    }
                    player.setLayoutY(lY+46.5);
                    x++;
                }
                break;
            case "RIGHT":
                player.setImage(Images.getImage(id, direction));
                if(y < 11 && Map.isFree(x,y+1)){
                    if(Map.isChangeZone(x, y, direction)){                                    // idem if moving right
                        client.setNodeQueue(Queues.getPublicQueue(Map.getZone(x, y+1)));
                    }
                    player.setLayoutX(lX+46.5);
                    y++;
                }
                break;
            case "LEFT":
                player.setImage(Images.getImage(id, direction));
                if(y > 0 && Map.isFree(x, y-1)){
                    if(Map.isChangeZone(x, y, direction)){                                    // idem if moving left
                        client.setNodeQueue(Queues.getPublicQueue(Map.getZone(x, y-1)));
                    }
                    player.setLayoutX(lX-46.5);
                    y--;
                }
                break;  
        }
        Map.updateMap(client.getID(), this.x, this.y, 2);
        Message m = new Message(id, queue, name, x, y, direction, "DEFAULT");             // finally, send a message to the node(manager)
        client.sendMessage(m);                                                            // to notify him
    }
      
    /**
     * This method is used to play a fade transition when the scene is changing (moving to home scene)
     * @param root a Node
     * @param milis an integer : transition duration
     * @param i an integer (0-1)
     * @param j an integer (0-1)
     */
    public void fadeTransition(Node root,int milis, int i, int j){
        FadeTransition ft = new FadeTransition(Duration.millis(milis), root);
        ft.setFromValue(i);
        ft.setToValue(j);
        ft.play();
    }
    
    
    /**
     * This method is used to update the name of the player at the bottom of the GUI
     * @param id player id
     * @param pname player name
     */
    public void updateName(int id, String pname){
         switch(id){
             case 1:
                this.name1.setText(pname);
                break;
            case 2:
                this.name2.setText(pname);
                break;
            case 3:
                this.name3.setText(pname);
                break;
            case 4:
                this.name4.setText(pname);
                break;
        }
    }
    
    
   /**
     * This method is used to set layout of an ImageView
     * @param img an ImageView
     * @param i new x coordinate
     * @param j new y coordinate
     */
    public void setLayouts(ImageView img, int i, int j){
        img.setLayoutX(j*46.5);
        img.setLayoutY(i*46.5);
        if(img.equals(player)){
            this.x = i;
            this.y = j;
        }  
    }
    
    /**
     * This method is used to update layouts of an ImageView
     * And change it's Image according to the performed move
     * @param id the id of the player 
     * @param i new x coordinate of the player
     * @param j new y coordinate of the player
     * @param move the move performed by the player
     */
    public void updateLayouts(int id, int i, int j, String move){
        switch(id){
            case 1:
                setLayouts(player1,i,j);                           // Change the layouts of the ImageView
                player1.setImage(Images.getImage(id, move));       // Set the image according to the move direction
                if(!player1.isVisible())
                    player1.setVisible(true);                      // if player ImageView is not visible, show it
                break;
            case 2:
                setLayouts(player2,i,j);
                player2.setImage(Images.getImage(id, move));      // idem if player 2
                if(!player2.isVisible())
                    player2.setVisible(true);
                break;
            case 3:
                setLayouts(player3,i,j);
                player3.setImage(Images.getImage(id, move));      // idem if player  3
                if(!player3.isVisible())
                    player3.setVisible(true);
                break;
            case 4:
                setLayouts(player4,i,j);
                player4.setImage(Images.getImage(id, move));      // idem if player 4
                if(!player4.isVisible())
                    player4.setVisible(true);
                break;
        }
    }
    
    
    /**
     * This method is used to get the ImageView of a player according to it's id
     * @param id the id of the player
     */
    public ImageView getPlayer(int id){
        ImageView pl = null;
        switch(id){
            case 1:
                pl = player1;
                break;
            case 2:
                pl = player2;
                break;
            case 3:
                pl = player3;
                break;
            case 4:
                pl = player4;
                break;
        }
        return pl;
    }
    
    
    /**
     * This method is used when the player move to this scene of the GUI
     * Initialize player name, coordinates, layouts ...
     * @param id player id
     */
    public void init(int id){
        switch(id){
            case 1:
                x=0; y=0;                          // initalize player coordinates
                player = player1;                  // reference to the player img
                this.name1.setText(this.name);     // set up player name
                break;
            case 2:
                x=0; y=11;                        // idem if player 2
                player = player2;                  
                this.name2.setText(this.name);
                break;
            case 3:
                x=11; y=0;                        // idem if player 3
                player = player3;                
                this.name3.setText(this.name);
                break;
            case 4:
                x=11; y=11;                       // idem if player 4
                player = player4;
                this.name4.setText(this.name);
                break;
        } 
        
        //set starting image, visibility and layouts for the player image
        player.setImage(Images.getImage(client.getID(), "DOWN"));
        player.setVisible(true);
        setLayouts(player,x,y);
    }
}