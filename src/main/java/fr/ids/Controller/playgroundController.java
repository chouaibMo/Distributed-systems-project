/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ids.Controller;

import fr.ids.Client.Client;
import static fr.ids.Controller.homeController.media;
        
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    
    private int playerID = 4;
    private Client client;
    
    // IMAGES : 
    private static Image up;
    private static Image down;
    private static Image right;
    private static Image left;
    
    // COORDINATES : 
    private int x;
    private int y;
    
   @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        up    = new Image("/fr/ids/Resources/Images/p"+playerID+"-U.png");
        down  = new Image("/fr/ids/Resources/Images/p"+playerID+"-D.png");
        right = new Image("/fr/ids/Resources/Images/p"+playerID+"-R.png");
        left  = new Image("/fr/ids/Resources/Images/p"+playerID+"-L.png");
        player1.setImage(down);
        
        //client = new Client(name,playerID);
        x=0;
        y=0;
    }

    @FXML
    private void keyPressed(KeyEvent event) throws Exception {
        makeMovement(player1,event.getCode().toString()); 
    }

    @FXML
    private void quitOnAction(ActionEvent e) throws IOException {
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
    
    
    public void makeMovement(ImageView player, String direction) throws Exception {
        double x = player.getLayoutX();
        double y = player.getLayoutY();
        switch(direction){
            case "ENTER":
                player.setImage(down);
                    setLayouts(player,10,10);
                break;
                
            case "UP":
                player.setImage(up);
                if(this.x>0 && Map.isFree(this.x-1,this.y)){
                    player.setLayoutY(y-46.5);
                    this.x--;
                }
                break;
            case "DOWN":
                player.setImage(down);
                if(this.x<11 && Map.isFree(this.x+1,this.y)){
                    player.setLayoutY(y+46.5);
                    this.x++;
                }
                break;
            case "RIGHT":
                player.setImage(right);
                if(this.y<11 && Map.isFree(this.x,this.y+1)){
                    player.setLayoutX(x+46.5);
                    this.y++;
                }
                break;
            case "LEFT":
                player.setImage(left);
                if(this.y>0 && Map.isFree(this.x,this.y-1)){
                    player.setLayoutX(x-46.5);
                    this.y--;
                }
                break;  
        }
    }
    
    
    public void fadeTransition(Node root,int milis, double i, int j){
        FadeTransition ft = new FadeTransition(Duration.millis(milis), root);
        ft.setFromValue(i);
        ft.setToValue(j);
        ft.play();
    }
    
    
    public void setLayouts(ImageView img, int i, int j){
        img.setLayoutX(j*46.5);
        img.setLayoutY(i*46.5);
        this.x = i;
        this.y = j;
        
    }

}