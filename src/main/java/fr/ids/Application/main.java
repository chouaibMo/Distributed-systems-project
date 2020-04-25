/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ids.Application;

import fr.ids.Network.Client;
import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
 

public class main extends Application {
    
        private static Stage primaryStage;
        private static int ID;
        
	@Override
	public void start(Stage stage) {
                primaryStage = stage;
		try {   
                    //Parent root = FXMLLoader.load(getClass().getResource("/fr/ids/Vue/home.fxml"));
                    Parent root = FXMLLoader.load(getClass().getResource("/fr/ids/Vue/playground.fxml"));
                    Scene scene = new Scene(root);
                    scene.getStylesheets().add(getClass().getResource("/fr/ids/Vue/style.css").toExternalForm());
                    Font.loadFont(getClass().getResourceAsStream("/fr/ids/Resources/Fonts/EVER_LOOSER_UNTEXTURED.ttf"), 14);
                    Font.loadFont(getClass().getResourceAsStream("/fr/ids/Resources/Fonts/EVER_LOOSER.ttf"), 14);
                    primaryStage.setScene(scene);
                    primaryStage.setResizable(false);
                    primaryStage.setTitle("IDS - Game project");
                    primaryStage.show();
    
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
        public static Stage getStage() { 
            return primaryStage; 
        }
        
        public static int getArgs(){
            return ID;
        }
        
	public static void main(String[] args) {
                if(args.length != 1){
                    System.out.println("usage : java -jar <jarname>.jar <userID> ");
                    System.exit(0);
                }
                ID = Integer.parseInt(args[0]);
		launch(args);
	}
        
        @Override
        public void stop() throws Exception{
            if(Client.getClient() != null)
                Client.getClient().close();
        }
}
