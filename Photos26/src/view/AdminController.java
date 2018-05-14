package view;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.User;
import model.Persistance;
import model.Photos;

/**
 * Controller for the Admin Scene
 * 
 * @author Robert Barbaro
 * 
 * @author Christopher Gonzalez
 *
 */
public class AdminController implements Initializable{
	
	/**
	 * Buttons used in within scene
	 */
	@FXML 
	private Button addBtn ,delBtn, editBtn, logoutBtn, quitBtn;  // button controls
	
	/**
	 * Text Field to input User name
	 */
	@FXML
	private TextField userText;  // user name text box
	
	/**
	 * ListView which contains all the User objects
	 */
	@FXML
	private ListView<User> userList;  // lists all the users
	

	/**
	 * Observable list which observes the User ListView
	 */
	private ObservableList<User> userObs;
	
	
	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {  // Runs this function every time scene starts
		
		userObs = FXCollections.observableArrayList(); 
		
		Iterator<User> userIter = Persistance.userIterator();  // iterator to go through user array
		
		while(userIter.hasNext()) {  // loads all the users into the listview 
			
			userObs.add(userIter.next());
		}
		
		userList.setItems(userObs);  // sets Observable list to the listview
		
		if(!userObs.isEmpty()) {  // if list is not empty; select first item
			
			userList.getSelectionModel().select(0);
		}
		
	}
	
	/**
	 * @param e takes in an ActionEvent
	 */
	public void handleAdd(ActionEvent e) {
				
		String userName = userText.getText().trim();  // get text input from text box
		
		
		
		if(userName.isEmpty()) {  // nothing entered; invalid user name
			
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Input Error");
			error.setContentText("Must Enter User Name");
			error.show();
			
			return;
		}
		
		String[] userCheck = userName.split(" ");
		
		if(userCheck.length > 1) {
			
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Input Error");
			error.setContentText("User Name must be one word");
			error.show();
			
			userText.clear();
			
			return;
		}
		
		for(User u: userObs) {  // check for duplicate user name
			
			if(userName.equals(u.toString())) {   // if duplicate, do not create user
				
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Input Error");
				error.setContentText("Existing Username");
				error.show();
				
				userText.clear();
				
				return;
				
			}
						
		}
		
		User newUser = new User(userName);  // creates new user object
		
		userObs.add(newUser);  // add new user to observable list
				
		Persistance.addUser(newUser);  // add user to user array
		
		userList.getSelectionModel().select(newUser);  // select the user in listview
		
		userText.clear();  // clear text box
		
		
	}
	
	/**
	 * @param e takes in an ActionEvent
	 */
	public void handleDel(ActionEvent e) {
		
		if(userObs.isEmpty()) {  // if list empty; nothing to delete
			
			return;
		}
		
		User selectedAlbum = userList.getSelectionModel().getSelectedItem();  // gets selected album
		
		int i = 0;
		for(User u: userObs) {  // finds album in observable list
			
			if(selectedAlbum.toString().equals(u.toString())) {  
				
				break;
				
			}
			
			i++;
		}
		
		userObs.remove(i);  // remove song from observable list
		
		Persistance.delUser(i);  // delete user from array
		
		if(userObs.size() == i) {  // if last item in list is deleted
			
			userList.getSelectionModel().select(--i);  // select previous item in list
			
		} else {
			
			userList.getSelectionModel().select(i);  // select next item in list
			
		}
	}
	
	/**
	 * @param e takes in an ActionEvent
	 */
	public void handleEdit(ActionEvent e) {
		
		if(userObs.isEmpty()) {
			
			return;
		}
				
		String userName = editDialog();  // pop up window for album name
		
		if(userName == null) { // invalid; duplicate album
			
			return;
		}
				
		int selectedIndex = userList.getSelectionModel().getSelectedIndex();
		
		User selectedUser = userList.getSelectionModel().getSelectedItem();
		
		selectedUser.setUserName(userName);
		
		userObs.get(selectedIndex).setUserName(userName);
								
		//userObs.get(selectedIndex).setUserName(userName, selectedIndex);
		userList.setItems(null);
		userList.setItems(userObs);
		
		userList.getSelectionModel().select(selectedIndex);  // select user that was edited 
	}
	
	/**
	 * @param e takes in an ActionEvent
	 */
	public void handleLogout(ActionEvent e) throws IOException {
		
		// Switches to login scene
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/Login.fxml"));
		BorderPane root = (BorderPane) loader.load();
		
		Scene scene = new Scene(root);
		Stage primaryStage = (Stage) logoutBtn.getScene().getWindow();  // gets the current stage 
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		
	}
	
	/**
	 * @param e takes in an ActionEvent
	 */
	public void handleQuit(ActionEvent e) throws IOException {
		
		Persistance.writeUser();
		
		e.consume();
		
		Stage primaryStage = (Stage) quitBtn.getScene().getWindow();  // gets the current stage
		primaryStage.close();
		
	}
	
	/**
	 * @return returns user name to be edited from dialog box. Returns null if value is invalid. 
	 */
	/**
	 * @return
	 */
	public static String editDialog() {
		
		TextInputDialog addDialog = new TextInputDialog();
		addDialog.setTitle("Edit User Name");
		addDialog.setHeaderText("Edit User Name");
		addDialog.setContentText("User Name:");
		
		Optional<String> result = addDialog.showAndWait();
		
		if(result.isPresent()) {
			
			if(result.get().isEmpty()) {
				
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Input Error");
				error.setContentText("Must Enter User Name");
				error.show();
				
				return null;
			}
						
			Iterator<User> userIter = Persistance.userIterator();
			
			while(userIter.hasNext()) {
				
				
				if(result.get().equals(userIter.next().toString())) {
					
					Alert error = new Alert(AlertType.ERROR);
					error.setTitle("Input Error");
					error.setContentText("Existing User Name");
					error.show();
					
					return null;
					
				}
				
			}
			
			return result.get().trim();
		}
		
		return null;
	}

	
	
	
}
