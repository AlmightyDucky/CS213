package view;

import java.io.IOException;
import java.util.Iterator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Persistance;
import model.User;


/**
 * Controller for the Login Scene
 * 
 * @author Robert Barbaro
 * 
 * @author Christopher Gonzalez
 *
 */
public class LoginController {
	
	/**
	 * TextField to input user name
	 */
	@FXML
	private TextField userText;  
	
	/**
	 * Buttons used for the scene
	 */
	@FXML
	private Button loginBtn, quitBtn;
	
	/**
	 * Index of the user that is logged in
	 */
	private static int userIndex;
	
	/**
	 * @param e takes in an ActionEvent
	 * @throws IOException throws IO exception if file is not found
	 */
	public void handleLogin(ActionEvent e) throws IOException {
		
		String userName = userText.getText();
		
		if(userName.equals("admin") ) {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/Admin.fxml"));
			BorderPane root = (BorderPane) loader.load();
			
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) loginBtn.getScene().getWindow();  // gets the current stage 
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
			
			return;
			
		}
		
		
		Iterator<User> userIter = Persistance.userIterator();
		
		for(int i = 0; userIter.hasNext(); i++) {
						
			if(userIter.next().toString().equals(userName)) {
				
				userIndex = i;  // index in user array where current user is located
						
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/view/Album.fxml"));
				BorderPane root = (BorderPane) loader.load();
				
				Scene scene = new Scene(root);
				Stage primaryStage = (Stage) loginBtn.getScene().getWindow();  // gets the current stage 
				primaryStage.setScene(scene);
				primaryStage.setResizable(false);
				primaryStage.show();
				
				return;
							
			}
						
		}
		
		Alert error = new Alert(AlertType.ERROR);
		error.setTitle("Input Error");
		error.setContentText("Nonexistent User Name");
		error.show();
		
		userText.clear();
	}
	
	/**
	 * @param e takes in an ActionEvent
	 * @throws IOException throws if file is not found
	 */
	public void handleQuit(ActionEvent e) throws IOException {
		
		Persistance.writeUser();
		
		e.consume();
		
		Stage primaryStage = (Stage) quitBtn.getScene().getWindow();  // gets the current stage
		primaryStage.close();
		
	}
	
	/**
	 * @return returns the user index
	 */
	public static int getUserIndex() {
		
		return userIndex;
	}

}
