package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


/**
 * Starts the application by displaying the login screen
 * 
 * @author Robert Barbaro
 *
 * @author Christopher Gonzalez
 */
public class Photos extends Application {
		
	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) {
		
		try {
			
			Persistance.readUser();
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/Login.fxml"));
			BorderPane root = (BorderPane) loader.load();
			
			
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
			
		} catch(Exception e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * @param args takes in command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
