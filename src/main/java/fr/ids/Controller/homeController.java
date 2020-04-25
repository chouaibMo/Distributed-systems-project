/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ids.Controller;

import fr.ids.Application.main;
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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author chouaib
 */
public class homeController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField playerName;
    @FXML
    private Button continueBtn;
    
    public static String name;
    public static SoundEffect media = new SoundEffect("clic.wav");
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    }

    @FXML
    private void continueOnAction(ActionEvent event) throws IOException {
        media.playClip();
        if(!playerName.getText().equals("")){
            name = playerName.getText();
            playerName.clear();
            Parent root = FXMLLoader.load(getClass().getResource("/fr/ids/Vue/playground.fxml"));
            fadeTransition(root,1200,0,1);
            Stage stage = (Stage) continueBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        }
    }

    @FXML
    private void playerNameOnKeyPressed(KeyEvent event) throws IOException {
        if(event.getCode().equals(KeyCode.ENTER)){
            if(!playerName.getText().equals("")){
                name = playerName.getText();
                playerName.clear();
                Parent root = FXMLLoader.load(getClass().getResource("/fr/ids/Vue/playground.fxml"));
                fadeTransition(root,1200,0,1);
                Stage stage = (Stage) playerName.getScene().getWindow();
                stage.setScene(new Scene(root));
            }
        }
    }
    
    
    public void fadeTransition(Node root,int milis, double i, int j){
        FadeTransition ft = new FadeTransition(Duration.millis(milis), root);
        ft.setFromValue(i);
        ft.setToValue(j);
        ft.play();
    }
    
}
