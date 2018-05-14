package view;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Persistance;
import model.User;
import view.LoginController;;

/**
 * Controller for the Album Scene
 * 
 * @author Robert Barbaro
 * 
 * @author Christopher Gonzalez
 *
 */
public class AlbumController implements Initializable{
	
	/**
	 * TableView which contains the album data
	 */
	@FXML
	private TableView<User.Album> albumTable;
	
	/**
	 * TableColumn which contains the name
	 */
	@FXML
	private TableColumn<User.Album, String> nameColumn;
	
	/**
	 * TableColumn which contains the number of photos
	 */
	@FXML
	private TableColumn<User.Album, String> photoColumn;
	
	/**
	 * TableColumn which contains the date range of photos within album
	 */
	@FXML
	private TableColumn<User.Album, String> dateColumn;
	
	/**
	 * Buttons used within the scene
	 */
	@FXML 
	private Button addBtn, delBtn, openBtn, editBtn, searchBtn, logoutBtn, quitBtn;
	
	/**
	 * ObservableList which observes the albumTable
	 */
	private static ObservableList<User.Album> albumObs;
	
	
	/**
	 * holds the index of the album that is open
	 */
	private static int openAlbumIndex = -1;
	
	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		albumObs = FXCollections.observableArrayList();

		User currUser = Persistance.getUser(LoginController.getUserIndex());  // holds the current user
		
		Iterator<User.Album> albumInter = currUser.albumIterator();
		
		while(albumInter.hasNext()) {  // Load all albums from current user
			
			albumObs.add(albumInter.next());
						
		}			
		
				
		albumTable.setItems(albumObs);
		
		albumTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);	
		
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("albumName"));
		nameColumn.setResizable(false);
		nameColumn.setSortable(false);
		
		photoColumn.setCellValueFactory(new PropertyValueFactory<>("numOfPhotos"));
		photoColumn.setResizable(false);
		photoColumn.setSortable(false);
		
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateRange"));  /// need to create getter to use
		dateColumn.setResizable(false);
		dateColumn.setSortable(false);
		
		nameColumn.setStyle( "-fx-alignment: CENTER;");
		
		photoColumn.setStyle( "-fx-alignment: CENTER;");
		
		dateColumn.setStyle( "-fx-alignment: CENTER;");
		
		if(!albumObs.isEmpty()) {  // if list is not empty; select first item
			
			albumTable.getSelectionModel().select(0);
		}
	}
	
	/**
	 * @param e takes in an ActionEvent
	 */
	public void handleAdd(ActionEvent e) {		
		
		int userIndex = LoginController.getUserIndex();
		
		String albumName = addDialog();  // pop up window for album name
		
		if(albumName == null) {  // invalid; duplicate album
			
			return; 
		}
		
		User.Album album = new User.Album(albumName);  // create new album instance
				
		User currUser = Persistance.getUser(userIndex);
	
		currUser.addAlbum(album);
		
		albumObs.add(album);  // insert album in observable list
		
		
		
		albumTable.getSelectionModel().select(albumObs.size()-1);  // selects the last album to be inserted into list

		
	}
	
	/**
	 * @param e takes in an ActionEvent
	 */
	public void handleDel(ActionEvent e) {
		
		if(albumObs.isEmpty()) {
			
			return;
		}
		
		User.Album selectedAlbum = albumTable.getSelectionModel().getSelectedItem();  // gets title of song that is selected
		
		int i = 0;
		for(User.Album a: albumObs) {  // find song in observable list
			
			if(selectedAlbum.toString().equals(a.toString())) {  
				
				break;
				
			}
			
			i++;
		}
		
		
		User currUser = Persistance.getUser(LoginController.getUserIndex());
		
		currUser.delAlbum(i);
		
		albumObs.remove(i);
		
		if(albumObs.size() == i) {  // if last item in list is deleted
			
			albumTable.getSelectionModel().select(--i);  // select previous item in list
			
		} else {
			
			albumTable.getSelectionModel().select(i);  // select next item in list
			
		}
		
		
		
	}
	
	/**
	 * @param e takes in an ActionEvent
	 */
	public void handleEdit(ActionEvent e) {
		
		if(albumObs.isEmpty()) {
			
			return;
		}
		
		int userIndex = LoginController.getUserIndex();
		
		String albumName = editDialog();  // pop up window for album name
		
		if(albumName == null) { // invalid; duplicate album
			
			return;
		}
				
		int selectedIndex = albumTable.getSelectionModel().getSelectedIndex();
				
		User currUser = Persistance.getUser(userIndex);
				
		currUser.getAlbum(selectedIndex).setAlbumName(albumName);
		
		
		albumTable.getColumns().get(selectedIndex).setVisible(false);  // refresh columns to display new value
		albumTable.getColumns().get(selectedIndex).setVisible(true);
		
		albumTable.getSelectionModel().select(selectedIndex);  // select album that was edited
		
	}
	
	/**
	 * @param e takes in an ActionEvent
	 * @throws IOException 
	 */
	public void handleOpen(ActionEvent e) throws IOException
	{
		if(albumObs.isEmpty()) {
			
			return;
		}
		
		openAlbumIndex = albumTable.getSelectionModel().getSelectedIndex();
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/Photo_Subsystem.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		
		Scene scene = new Scene(root);
		Stage primaryStage = (Stage) openBtn.getScene().getWindow();  // gets the current stage 
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		
	}
	
	
	/**
	 * @param e takes in an Action Event
	 * @throws IOException throws exception if scene cannot be loaded 
	 */
	public void handleSearch(ActionEvent e) throws IOException {
		
		boolean validInput = searchDialog();
		
		if(!validInput) {
			
			return;
		}
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/AlbumSearch.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		
		Scene scene = new Scene(root);
		Stage primaryStage = (Stage) logoutBtn.getScene().getWindow();  // gets the current stage 
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	/**
	 * @param e takes in an ActionEvent
	 */
	public void handleLogout(ActionEvent e) throws IOException {
		
		albumObs = FXCollections.observableArrayList();  // delete album contents upon logging out
		
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
	 * @return Returns album name to be added to albumTable. Returns null if invalid album name. 
	 */
	public static String addDialog() {
		
		
		TextInputDialog addDialog = new TextInputDialog();
		addDialog.setTitle("Add Album");
		addDialog.setHeaderText("Create Album");
		addDialog.setContentText("Album Name:");
		
		Optional<String> result = addDialog.showAndWait();
		
		if(result.isPresent()) {
			
			if(result.get().isEmpty()) {
				
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Input Error");
				error.setContentText("Must Enter Album Name");
				error.show();
				
				return null;
			}
			
			User currUser = Persistance.getUser(LoginController.getUserIndex());
			
			Iterator<User.Album> albumIter = currUser.albumIterator();
			
			while(albumIter.hasNext()) {
				
				
				if(result.get().equals(albumIter.next().getAlbumName())) {
					
					Alert error = new Alert(AlertType.ERROR);
					error.setTitle("Input Error");
					error.setContentText("Existing Album");
					error.show();
					
					return null;
					
				}
				
			}
			
			
			return result.get().trim();
		}
		
		return null;
		
	}
	
	/**
	 * @return returns the album name to edit. Returns null if album name is invalid. 
	 */
	public static String editDialog() {
		
		TextInputDialog addDialog = new TextInputDialog();
		addDialog.setTitle("Edit Album");
		addDialog.setHeaderText("Edit Album Name");
		addDialog.setContentText("Album Name:");
		
		Optional<String> result = addDialog.showAndWait();
		
		if(result.isPresent()) {
			
			if(result.get().isEmpty()) {
				
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Input Error");
				error.setContentText("Must Enter Album Name");
				error.show();
				
				return null;
			}
			
			User currUser = Persistance.getUser(LoginController.getUserIndex());
			
			Iterator<User.Album> albumIter = currUser.albumIterator();
			
			while(albumIter.hasNext()) {
				
				
				if(result.get().equals(albumIter.next().getAlbumName())) {
					
					Alert error = new Alert(AlertType.ERROR);
					error.setTitle("Input Error");
					error.setContentText("Existing Album");
					error.show();
					
					return null;
					
				}
				
			}
			
			return result.get().trim();
		}
		
		return null;
	}
	
	

	/**
	 * @return returns index of album that is open
	 */
	public static int getOpenAlbumIndex() {
		
		return openAlbumIndex;
	}
	
	/**
	 * @return returns true if the search criteria is valid
	 */
	public static boolean searchDialog() {
		
		AlbumSearchController.personTag = null;
		AlbumSearchController.locTag = null;
		AlbumSearchController.fromDate = null;
		AlbumSearchController.toDate = null;
		
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Search Photos");
		dialog.setHeaderText("Input Search Criteria\nAll Fields are Optional");
		dialog.setResizable(true);

		Label label1 = new Label("Person Tag: ");
		Label label2 = new Label("Location Tag: ");
		Label fromDate = new Label("Date Range: ");
		//Label toDate = new Label("Date: ");
		TextField personTag = new TextField();
		TextField locTag = new TextField();
		Label to = new Label("   to   ");
	//	TextField fromText = new TextField();
		
		DatePicker fromD = new DatePicker();
		DatePicker toD = new DatePicker();
	//	TextField toText = new TextField();
				
		GridPane grid = new GridPane();
		
		grid.add(label1, 1, 1);
		grid.add(personTag, 2, 1);
		grid.add(label2, 1, 2);
		grid.add(locTag, 2, 2);
		grid.add(fromDate, 1, 3);
		grid.add(fromD, 2, 3);
		grid.add(to, 3, 3);
	//	grid.add(toDate, 4, 3);
		grid.add(toD, 5, 3);
		
		grid.setVgap(15);
		dialog.getDialogPane().setContent(grid);
				
		ButtonType buttonTypeOk = new ButtonType("Okay", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
		
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);
		
		
		Optional<ButtonType> result = dialog.showAndWait();
		
		if(result.get() == buttonTypeCancel) {
			
			return false;
		}
				
		if(personTag.getText().isEmpty() && locTag.getText().isEmpty() 
				&& fromD.getValue() == null && toD.getValue() == null) {
			
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Input Error");
			error.setContentText("Must enter search criteria");
			error.show();
			
			return false;
		}
		
		AlbumSearchController.personTag = personTag.getText().trim();
		AlbumSearchController.locTag = locTag.getText().trim();
		
		if(fromD.getValue() != null) {
			
			AlbumSearchController.fromDate = fromD.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyy"));
			
		}
		
		if(toD.getValue() != null) {
			
			AlbumSearchController.toDate = toD.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyy"));
			
		}
		
		
 		return true;		
		
	}
	
	
}
