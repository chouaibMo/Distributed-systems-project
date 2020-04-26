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
import static fr.ids.Controller.homeController.name;
import fr.ids.Network.Message;
import fr.ids.Network.Queues;
        
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        this.client.setUp();
        
        switch(client.getID()){
            case 1:
                x=0; y=0;
                player = player1;
                this.name1.setText(homeController.name);
                break;
            case 2:
                x=0; y=11;
                player = player2;
                this.name2.setText(homeController.name);
                break;
            case 3:
                x=11; y=0;
                player = player3;
                this.name3.setText(homeController.name);
                break;
            case 4:
                x=11; y=11;
                player = player4;
                this.name4.setText(homeController.name);
                break;
        } 
        
        player1.setVisible(false);
        player2.setVisible(false);
        player3.setVisible(false);
        player4.setVisible(false);
    
        player.setImage(Images.getImage(client.getID(), "DOWN"));
        player.setVisible(true);
        setLayouts(player,x,y);
        
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                Message msg = SerializationUtils.deserialize(delivery.getBody());
                    updateLayouts(msg.getPlayerID(), msg.getPlayerX(), msg.getPlayerY(), msg.getDirection());
                    Platform.runLater( () -> {
                        updateName(msg.getPlayerID(), msg.getName());
                    });
                System.out.println("[ FROM "+client.getNodeQueue()+" ] --> "+msg);
        };
        
        try {
            client.startConsuming(deliverCallback);
            Message m = new Message(client.getID(), client.getClientQueue(), name, this.x, this.y,"DOWN");
            client.sendMessage(m);
        } catch (Exception ex) {
            Logger.getLogger(playgroundController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void keyPressed(KeyEvent event) throws Exception {
        makeMovement(player,event.getCode().toString());  
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
        
        int id = client.getID();
        String queue = client.getClientQueue(); 
        
        switch(direction){
            case "UP":
                player.setImage(Images.getImage(id, direction));
                if(this.x>0 && Map.isFree(this.x-1,this.y)){
                    if(Map.isChangeZone(this.x, this.y, direction)){
                        client.setNodeQueue(Queues.getPublicQueue(Map.getZone(this.x-1, this.y)));
                    }
                    Message m = new Message(id, queue, homeController.name, this.x-1, this.y,direction);
                    client.sendMessage(m);
                    player.setLayoutY(y-46.5);
                    this.x--;
                }
                break;
            case "DOWN":
                player.setImage(Images.getImage(id, direction));
                if(this.x<11 && Map.isFree(this.x+1,this.y)){
                    if(Map.isChangeZone(this.x, this.y, direction)){
                        client.setNodeQueue(Queues.getPublicQueue(Map.getZone(this.x+1, this.y)));
                    }
                    Message m = new Message(id, queue, homeController.name, this.x+1, this.y,direction);
                    client.sendMessage(m);
                    player.setLayoutY(y+46.5);
                    this.x++;
                }
                break;
            case "RIGHT":
                player.setImage(Images.getImage(id, direction));
                if(this.y<11 && Map.isFree(this.x,this.y+1)){
                    if(Map.isChangeZone(this.x, this.y, direction)){
                        client.setNodeQueue(Queues.getPublicQueue(Map.getZone(this.x, this.y+1)));
                    }
                    Message m = new Message(id, queue, homeController.name, this.x, this.y+1,direction);
                    client.sendMessage(m);
                    player.setLayoutX(x+46.5);
                    this.y++;
                }
                break;
            case "LEFT":
                player.setImage(Images.getImage(id, direction));
                if(this.y>0 && Map.isFree(this.x,this.y-1)){
                    if(Map.isChangeZone(this.x, this.y, direction)){
                        client.setNodeQueue(Queues.getPublicQueue(Map.getZone(this.x, this.y-1)));
                    }
                    Message m = new Message(id, queue, homeController.name, this.x, this.y-1, direction);
                    client.sendMessage(m);
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

    public void updateLayouts(int id, int i, int j, String move){
        switch(id){
            case 1:
                if(!player1.isVisible())
                    player1.setVisible(true);
                setLayouts(player1,i,j);
                break;
            case 2:
                if(!player2.isVisible())
                    player2.setVisible(true);
                setLayouts(player2,i,j);
                break;
            case 3:
                if(!player3.isVisible())
                    player3.setVisible(true);
                setLayouts(player3,i,j);
                break;
            case 4:
                if(!player4.isVisible())
                    player4.setVisible(true);
                setLayouts(player4,i,j);
                break;
        }
    }
    
        public void updateImage(int id, String direction){
        switch(id){
            case 1:
                player1.setImage( Images.getImage(id, direction) );
                break;
            case 2:
                player2.setImage( Images.getImage(id, direction) );
                break;
            case 3:
                player3.setImage( Images.getImage(id, direction) );
                break;
            case 4:
                player4.setImage( Images.getImage(id, direction) );
                break;
        }
    }
}