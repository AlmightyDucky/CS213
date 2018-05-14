package view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Persistance;
import model.Tag;
import model.User;


/**
 * Controller for the Album Search Scene
 * 
 * @author Robert Barbaro
 * 
 * @author Christopher Gonzalez
 *
 */
public class AlbumSearchController implements Initializable{
	
	/**
	 * Contains the list of photos in search results
	 */
	@FXML
	private ListView<User.Album.Photo> photoList;
	
	/**
	 * Displays the photo image
	 */
	@FXML
    private ImageView photoImage;
	
	/**
	 * Buttons within the scene
	 */
	@FXML 
	private Button addBtn, returnToAlbumButton, logoutButton, quitAppButton;
	
	/**
	 * Labels that display photo information
	 */
	@FXML
	private Label captionText, dateText, personTagText, locationTagText;
	
	/**
	 * Observable list for photo list view
	 */
	private static ObservableList<User.Album.Photo> photoObsList;
	
	/**
	 * Data used to search for photos
	 */
	public static String personTag, locTag, toDate, fromDate;
	
	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		photoObsList = FXCollections.observableArrayList();
		
		photoList.setItems(photoObsList);
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		
		Date startDate = null;
		Date endDate = null;
	
		try {
			
			if(fromDate != null) {
				
			 startDate = sdf.parse(fromDate);
			 
			}
			
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		try {
			
			if(toDate != null) {
				
				endDate = sdf.parse(toDate);
				
			}
			
			
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		
		Iterator<User.Album> albumIter = Persistance.getUser(LoginController.getUserIndex()).albumIterator();
		
		while(albumIter.hasNext()) {
			
			User.Album currAlbum = albumIter.next();
			
			Iterator<User.Album.Photo> photoIter = currAlbum.photoIterator();
			
			while(photoIter.hasNext()) {
				
				User.Album.Photo photo = photoIter.next();
				
				Date photoDate = photo.getPhotoDate();
					
				
				if(!personTag.isEmpty()) {
					
					boolean tagFound = false;
					
					Iterator<Tag> tagIter = photo.tagIterator();
					
					while(tagIter.hasNext()) {
						
						Tag tag = tagIter.next();
						
						if(tag.getName().equals("person") && tag.getValue().equals(personTag)) {
							
								tagFound = true;		
						}
					}
					
					if(!tagFound) {
						
						continue;
					}
				}
				
				
				
				if(!locTag.isEmpty()) {
					
					boolean tagFound = false;
					
					Iterator<Tag> tagIter = photo.tagIterator();
					
					while(tagIter.hasNext()) {
						
						Tag tag = tagIter.next();
						
						if(tag.getName().equals("location") && tag.getValue().equals(locTag)) {
							
								tagFound = true;		
						}
					}
					
					if(!tagFound) {
						
						continue;
					}
				
				}
				
				
				
				if(startDate != null && endDate != null) {
					
					if(!(photoDate.after(startDate) && photoDate.before(endDate))  && !photoDate.equals(startDate) && !photoDate.equals(endDate)) {
						
						continue;
						
					}
					
				} else if(startDate != null) {
					
					if(!photoDate.after(startDate) && !photoDate.equals(startDate)) {
						
						continue;
					}
					
				} else if(endDate != null) {
					
					if(!photoDate.before(endDate) && !photoDate.equals(endDate)) {
						
						continue;
					}
				}
				
				
				photoObsList.add(photo);
				
				
			}	
			
			if(photoObsList.size() == 0) {
				
				blankDisplay();
			}
		}
		
		
		photoList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateDisplay());
		
		if(!photoObsList.isEmpty()) {
			
			photoList.getSelectionModel().select(0);
			
		}
		
	
	}
	
	/**
	 * @param e takes in an Action Event
	 * @throws IOException throws exception if scene cannot be loaded
	 */
	public void handleAdd(ActionEvent e) throws IOException {
		
		int userIndex = LoginController.getUserIndex();
		
		String albumName = AlbumController.addDialog();  // pop up window for album name
		
		if(albumName == null) {  // invalid; duplicate album
			
			return; 
		}
		
		User.Album album = new User.Album(albumName);  // create new album instance
		
	//	album.setBeginDate(image.getPhotoDate());
		//album.setEndDate(image.getPhotoDate());
		
		int i = 0;
		
		for(User.Album.Photo p: photoObsList) {
			
			if(i == 0) {
				
				album.setBeginDate(p.getPhotoDate());
				album.setEndDate(p.getPhotoDate());
				
			}
			
			if(p.getPhotoDate().before(album.getBeginDate())) {
				
				album.setBeginDate(p.getPhotoDate());
			}
			
			if(p.getPhotoDate().after(album.getEndDate())) {
									
				album.setEndDate(p.getPhotoDate());
			}
			
			album.addPhoto(p);
			
			i++;
		}
				
		User currUser = Persistance.getUser(userIndex);
	
		currUser.addAlbum(album);
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/Album.fxml"));
		BorderPane root = (BorderPane) loader.load();
		
		Scene scene = new Scene(root);
		Stage primaryStage = (Stage) logoutButton.getScene().getWindow();  // gets the current stage 
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
			
		
	}
	
	/**
	 * @param e takes in an Action Event
	 * @throws IOException throws exception if scene cannot be loaded
	 */
	public void handleReturn(ActionEvent e) throws IOException {
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/Album.fxml"));
		BorderPane root = (BorderPane) loader.load();
		
		Scene scene = new Scene(root);
		Stage primaryStage = (Stage) logoutButton.getScene().getWindow();  // gets the current stage 
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		
		
	}
	
	/**
	 * @param e takes in an Action Event
	 * @throws IOException throws exception if scene cannot be loaded
	 */
	public void handleLogout(ActionEvent e) throws IOException {
		
		//albumObs = FXCollections.observableArrayList();  // delete album contents upon logging out
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/Login.fxml"));
		BorderPane root = (BorderPane) loader.load();
		
		Scene scene = new Scene(root);
		Stage primaryStage = (Stage) logoutButton.getScene().getWindow();  // gets the current stage 
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		
	}
	
	/**
	 * @param e takes in an Action Event
	 * @throws IOException throws exception if scene cannot be loaded
	 */
	public void handleQuit(ActionEvent e) throws IOException {
		
		Persistance.writeUser();
		
		e.consume();
		
		Stage primaryStage = (Stage) logoutButton.getScene().getWindow();  // gets the current stage
		primaryStage.close();
	}
	

	/**
	 * Updates the image view display when photo is clicked in list view
	 */
	private void updateDisplay() {
		
		if(!photoObsList.isEmpty())
		{
			User.Album.Photo photo = photoList.getSelectionModel().getSelectedItem();	
			File file = new File(photo.getPhotoLocation());

			if (file.exists()) //check if photo still exists at its location on computer
			{
				Image display = new Image(file.toURI().toString());
				photoImage.setImage(display);
				captionText.setText(photo.getCaption());
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				dateText.setText(sdf.format(photo.getPhotoDate()));
				personTagText.setText(photo.getTags("person"));
				locationTagText.setText(photo.getTags("location"));
				
			} 
		}
	}
	
	public void blankDisplay()
	{
		photoImage.setImage(null);
		captionText.setText("");
		dateText.setText("");
		personTagText.setText("");
		locationTagText.setText("");
	}

}
