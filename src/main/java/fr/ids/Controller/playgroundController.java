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
import fr.ids.Vue.Images;
        
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
    private Button helpCloseBtn;
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
    
    // IMAGES : 
    //private static Image up;
    //private static Image down;
    //private static Image right;
    //private static Image left;
    
    // COORDINATES : 
    private int x;
    private int y;
    
    // REF. TO CLIENT PLAYER : 
    private ImageView player;

    
    //TIMELINE
    Timeline timeline;
    
   @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Client.getClient().setID(main.getArgs());
        Client.getClient().setUsername(homeController.name);
        Client.getClient().setUp();
        
        switch(Client.getClient().getID()){
            case 1:
                x=0; y=0;
                player = player1;
                break;
            case 2:
                x=0; y=11;
                player = player2;
                break;
            case 3:
                x=11; y=0;
                player = player3;
                break;
            case 4:
                x=11; y=11;
                player = player4;
                break;
        } 
        
        player1.setVisible(false);
        player2.setVisible(false);
        player3.setVisible(false);
        player4.setVisible(false);
    /*    
        up    = new Image("/fr/ids/Resources/Images/p"+Client.getClient().getID()+"-U.png");
        down  = new Image("/fr/ids/Resources/Images/p"+Client.getClient().getID()+"-D.png");
        right = new Image("/fr/ids/Resources/Images/p"+Client.getClient().getID()+"-R.png");
        left  = new Image("/fr/ids/Resources/Images/p"+Client.getClient().getID()+"-L.png");
    */
    
        player.setImage(Images.getImage(Client.getClient().getID(), "DOWN"));
        player.setVisible(true);
        setLayouts(player,x,y);
        
        timeline = new Timeline(
            new KeyFrame(Duration.ZERO, e -> { 
                gif.setVisible(true);
                gif.setLayoutX(player.getLayoutX());
                gif.setLayoutY(player.getLayoutY()-1);
            }),
            new KeyFrame(Duration.seconds(1), e -> { 
                gif.setVisible(false);
            })
        );
        
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                Message msg = SerializationUtils.deserialize(delivery.getBody());
                updateLayouts(msg.getPlayerID(), msg.getPlayerX(), msg.getPlayerY());
                System.out.println("[ FROM "+Client.getClient().getNodeQueue()+" ] --> "+msg);
        };
        
        try {
            Client.getClient().startConsuming(deliverCallback);
            Message m = new Message(Client.getClient().getID(),Client.getClient().getClientQueue(), this.x, this.y);
            Client.getClient().sendMessage(m);
        } catch (Exception ex) {
            Logger.getLogger(playgroundController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void keyPressed(KeyEvent event) throws Exception {
        makeMovement(player,event.getCode().toString()); 
        if(this.x ==4 && this.y == 4)
            timeline.play();
    }

    @FXML
    private void quitOnAction(ActionEvent e) throws Exception {
        media.playClip();
        Lighting light = new Lighting();
        light.setDiffuseConstant(0.9);
        light.setSurfaceScale(1);
        componentsPane.setEffect(light);
        ButtonType yesBtn = new ButtonType("yes");
        ButtonType cancelBtn = new ButtonType("Cancel");
        Alert alert = new Alert(AlertType.WARNING, "Are you sure you want to quit ?", yesBtn, cancelBtn);
        alert.showAndWait();
        media.playClip();

        if (alert.getResult() == yesBtn) {
            Client.getClient().close();
            Parent root = FXMLLoader.load(getClass().getResource("/fr/ids/Vue/home.fxml"));
            fadeTransition(root,1200,0,1);
            Stage stage = (Stage) quitBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        }
        componentsPane.setEffect(null);
        
    }

    @FXML
    private void helpOnAction(ActionEvent event) {
        media.playClip();
        Lighting light = new Lighting();
        light.setDiffuseConstant(0.9);
        light.setSurfaceScale(1);
        componentsPane.setEffect(light);
        helpPane.setVisible(true);
    }
    

    @FXML
    private void helpCloseOnAction(ActionEvent event) {
        media.playClip();
        componentsPane.setEffect(null);
        helpPane.setVisible(false);
    }
    
    
    /**
     * 
     * @param player an ImageView
     * @param direction a String : up, down, left or right
     * @throws Exception 
     */
    public void makeMovement(ImageView player, String direction) throws Exception {
        double x = player.getLayoutX();
        double y = player.getLayoutY();
        
        int id = Client.getClient().getID();
        String queue = Client.getClient().getClientQueue(); 
        
        switch(direction){
            case "UP":
                player.setImage(Images.getImage(id, direction));
                if(this.x>0 && Map.isFree(this.x-1,this.y)){
                    if(Map.isChangeZone(this.x, this.y, direction))
                        Client.getClient().setNodeQueue(Queues.getPublicQueue(Map.getZone(this.x-1, this.y)));
                    Message m = new Message(id, queue, this.x-1, this.y);
                    Client.getClient().sendMessage(m);
                    player.setLayoutY(y-46.5);
                    this.x--;
                }
                break;
            case "DOWN":
                player.setImage(Images.getImage(id, direction));
                if(this.x<11 && Map.isFree(this.x+1,this.y)){
                    if(Map.isChangeZone(this.x, this.y, direction))
                        Client.getClient().setNodeQueue(Queues.getPublicQueue(Map.getZone(this.x+1, this.y)));
                    Message m = new Message(id, queue, this.x+1, this.y);
                    Client.getClient().sendMessage(m);
                    player.setLayoutY(y+46.5);
                    this.x++;
                }
                break;
            case "RIGHT":
                player.setImage(Images.getImage(id, direction));
                if(this.y<11 && Map.isFree(this.x,this.y+1)){
                    if(Map.isChangeZone(this.x, this.y, direction))
                        Client.getClient().setNodeQueue(Queues.getPublicQueue(Map.getZone(this.x, this.y+1)));
                    Message m = new Message(id, queue, this.x, this.y+1);
                    Client.getClient().sendMessage(m);
                    player.setLayoutX(x+46.5);
                    this.y++;
                }
                break;
            case "LEFT":
                player.setImage(Images.getImage(id, direction));
                if(this.y>0 && Map.isFree(this.x,this.y-1)){
                    if(Map.isChangeZone(this.x, this.y, direction))
                        Client.getClient().setNodeQueue(Queues.getPublicQueue(Map.getZone(this.x, this.y-1)));
                    Message m = new Message(id, queue, this.x, this.y-1);
                    Client.getClient().sendMessage(m);
                    player.setLayoutX(x-46.5);
                    this.y--;
                }
                break;  
        }
    }
      
    /**
     * This method is used to play a transition on a node
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
     * This method is used to set layout of an ImageView
     * @param img an ImageView
     * @param i an integer : new x coordinate
     * @param j an integer : new y coordinate
     */
    public void setLayouts(ImageView img, int i, int j){
        img.setLayoutX(j*46.5);
        img.setLayoutY(i*46.5);
        if(img.equals(player)){
            this.x = i;
            this.y = j;
        }
            
    }
    

    public void updateLayouts(int id, int i, int j){
        switch(id){
            case 1:
                if(!player1.isVisible())
                    player1.setVisible(true);
                setLayouts(player1,i,j);
                //player1.setImage(Images.getImage(id, direction));
                break;
            case 2:
                if(!player2.isVisible())
                    player2.setVisible(true);
                setLayouts(player2,i,j);
                //player2.setImage(Images.getImage(id, direction));
                break;
            case 3:
                if(!player3.isVisible())
                    player3.setVisible(true);
                setLayouts(player3,i,j);
                //player3.setImage(Images.getImage(id, direction));
                break;
            case 4:
                if(!player4.isVisible())
                    player4.setVisible(true);
                setLayouts(player4,i,j);
                //player4.setImage(Images.getImage(id, direction));
                break;
        }
    }
}