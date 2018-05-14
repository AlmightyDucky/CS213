package view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Persistance;
import model.User;;

public class Photo_SubsystemController implements Initializable
{
	/**
	 * ListView which displays the names of the photos
	 */
	@FXML
	private ListView<User.Album.Photo> photoList;
	
	/**
	 * ImageView that displays an image
	 */
	@FXML
    private ImageView photoImage;
	
	/**
	 * Buttons used within the scene
	 */
	@FXML 
	private Button addPhotoButton, movePhotoButton, addTagButton, slideshowButton, returnToAlbumButton, logoutButton, quitAppButton;
	
	/**
	 * Labels that display information about a photo
	 */
	@FXML
	Label captionText, dateText, personTagText, locationTagText;
	
	/**
	 * ObservableList which observes the photoList
	 */
	private static ObservableList<User.Album.Photo> photoObsList;
	
	/**
	 * char that tags name and value are split between
	 */
	private static final String CHARSPLIT = "~";
	
	/**
	 * error codes used in errorPopup
	 */
	private static final int INVALDFILEPATH = 0;
	private static final int NOINPUT = 1;
	private static final int SAMETAG = 2;
	private static final int NONEXISTANTTAG = 3;
	
	/**
	 * Initializes the scene by populating the ListView and displaying an Image in the ImageView
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		photoObsList = FXCollections.observableArrayList();
		
		User currentUser = Persistance.getUser(LoginController.getUserIndex());
		
		User.Album currentAlbum = currentUser.getAlbum(AlbumController.getOpenAlbumIndex());
		
		Iterator<User.Album.Photo> photoIter = currentUser.getAlbum(AlbumController.getOpenAlbumIndex()).photoIterator();
		
		boolean invalidFileFlag = false;
		
		while(photoIter.hasNext())
		{
			User.Album.Photo photo = photoIter.next();
			File file = new File(photo.getPhotoLocation());
			
			if (file.exists())
			{
				photoObsList.add(photo);
			} else {
				invalidFileFlag = true;
				photoIter.remove();
				currentAlbum.opNumOfPhotos(1, '-');
			}
		}
		
		photoList.setItems(photoObsList);
		
		blankDisplay();
		photoList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateDisplay());
		
		photoList.getSelectionModel().select(0);
		
		updateDisplay();
		
		if (invalidFileFlag)
		{
			errorPopup(INVALDFILEPATH);
		}
		
	}
	
	/**
	 * Updates the photo display with the image, caption, date and tags
	 */
	private void updateDisplay()
	{
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
			} else {
				errorPopup(INVALDFILEPATH);
				deletePhoto();
				
				if (photoObsList.isEmpty())
				{
					blankDisplay();
				} else {
					updateDisplay();
				}
			}
		} else {
			blankDisplay();
		}
	}
	
	/**
	 * Default display - a blank image and blank labels
	 */
	public void blankDisplay()
	{
		photoImage.setImage(null);
		captionText.setText("");
		dateText.setText("");
		personTagText.setText("");
		locationTagText.setText("");
	}
	
	/**
	 * Adds a photo to an album
	 * @param e
	 * @throws IOException
	 * @throws ParseException
	 */
	public void handleAddPhoto(ActionEvent e) throws IOException, ParseException
	{
		FileChooser addPhoto = new FileChooser();
		configureFileChooser(addPhoto);
		File file = addPhoto.showOpenDialog(null);
		
		if (file != null)
		{
			User.Album.Photo image = new User.Album.Photo(file.getName(), file.toString());
			
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			
			String s = sdf.format(file.lastModified());
			
			Date date = sdf.parse(s);
			
			image.setPhotoDate(date); 
		
			User currentUser = Persistance.getUser(LoginController.getUserIndex());		
			User.Album currentAlbum = currentUser.getAlbum(AlbumController.getOpenAlbumIndex());
			currentAlbum.addPhoto(image);
		
			photoObsList.add(image);
		
			photoList.getSelectionModel().select(photoObsList.size() - 1);
		
			updateDisplay();
			
			if(photoObsList.size() == 1) {
				adjustDate(currentAlbum, image);
				return;
			}
			
			adjustDateRange(currentAlbum);
		}
	}
	
	/**
	 * Configures the file chooser with custom settings
	 * @param fc
	 */
	private static void configureFileChooser(final FileChooser fc)
	{
		fc.setTitle("Add in a Photo");
		
		/*initial directory set to user's home directory*/
		fc.setInitialDirectory(new File(System.getProperty("user.home")));
		
		/*allow users to search for different file extensions when adding photos*/
		fc.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("All Images", "*.*"),
				new FileChooser.ExtensionFilter("JPG", "*.jpg"),
				new FileChooser.ExtensionFilter("PNG", "*.png"));
	}
	
	/**
	 * Deletes a photo from an album - FXML
	 * @param e
	 * @throws IOException
	 */
	public void handleRemovePhoto(ActionEvent e) throws IOException
	{
		if(!photoObsList.isEmpty())
		{
			deletePhoto();
		
			updateDisplay();
		}
		
		
		
		User currentUser = Persistance.getUser(LoginController.getUserIndex());		
		User.Album currentAlbum = currentUser.getAlbum(AlbumController.getOpenAlbumIndex());

		adjustDateRange(currentAlbum);
		
		/*
		if(photoObsList.size() == 0) {
			
			currentAlbum.setBeginDate(null);
			currentAlbum.setEndDate(null);
			
			return;
		}
		
		Iterator<User.Album.Photo> photoIter = currentAlbum.photoIterator();
		
		User.Album.Photo photo = photoIter.next();
		
		currentAlbum.setBeginDate(photo.getPhotoDate());
		currentAlbum.setEndDate(photo.getPhotoDate());
		
		
		while(photoIter.hasNext()) { // updates the begin and end date
			
			photo = photoIter.next();
			
			
			if(photo.getPhotoDate().before(currentAlbum.getBeginDate())) {
									
				currentAlbum.setBeginDate(photo.getPhotoDate());
			}
			
			if(photo.getPhotoDate().after(currentAlbum.getEndDate())) {
									
				currentAlbum.setEndDate(photo.getPhotoDate());
			}
			
			
			
		}*/
	}
	
	/**
	 * Deletes a photo from an album
	 */
	public void deletePhoto()
	{
		User.Album.Photo photoToDelete = photoList.getSelectionModel().getSelectedItem();
		
		User currentUser = Persistance.getUser(LoginController.getUserIndex());		
		User.Album currentAlbum = currentUser.getAlbum(AlbumController.getOpenAlbumIndex());
		currentAlbum.deletePhoto(photoToDelete);

		photoObsList.remove(photoToDelete);
	}
	
	/**
	 * Copies a photo from one album to another
	 * @param e
	 */
	public void handleCopyPhoto(ActionEvent e)
	{
		User.Album.Photo photoToMove = photoList.getSelectionModel().getSelectedItem();
		User currentUser = Persistance.getUser(LoginController.getUserIndex());
		User.Album destAlbum;
		
		List<String> albumChoices = new ArrayList<>();
		
		Iterator<User.Album> albumInter = currentUser.albumIterator();
		
		String albumName = null;
		
		while(albumInter.hasNext())
		{
			User.Album album = albumInter.next();
			if (album != currentUser.getAlbum(AlbumController.getOpenAlbumIndex()))
			{
				albumChoices.add(album.getAlbumName());
			}
		}
		
		if (albumChoices.size() > 0)
		{
			albumName = moveDialog("copy", albumChoices);
			
			if (albumName != null)
			{
				albumInter = currentUser.albumIterator();
				destAlbum = albumSearch(albumName, albumInter);
			
				destAlbum.addPhoto(photoToMove);
				adjustDateRange(destAlbum);
			
				updateDisplay();
			}
		}
	}
	
	/**
	 * Moves a photo from one album to another
	 * @param e
	 */
	public void handleMovePhoto(ActionEvent e)
	{
		User.Album.Photo photoToMove = photoList.getSelectionModel().getSelectedItem();
		User currentUser = Persistance.getUser(LoginController.getUserIndex());
		User.Album currentAlbum = currentUser.getAlbum(AlbumController.getOpenAlbumIndex());
		User.Album destAlbum;
		
		List<String> albumChoices = new ArrayList<>();
		
		Iterator<User.Album> albumInter = currentUser.albumIterator();
		
		String albumName = null;
		
		while(albumInter.hasNext())
		{
			User.Album album = albumInter.next();
			if (album != currentUser.getAlbum(AlbumController.getOpenAlbumIndex()))
			{
				albumChoices.add(album.getAlbumName());
			}
		}
		
		if (albumChoices.size() > 0)
		{
			albumName = moveDialog("move", albumChoices);
			
			if (albumName != null)
			{
				albumInter = currentUser.albumIterator();
				destAlbum = albumSearch(albumName, albumInter);
			
				destAlbum.addPhoto(photoToMove);
				adjustDateRange(destAlbum);
			
				currentAlbum.deletePhoto(photoToMove);
				adjustDateRange(currentAlbum);
				photoObsList.remove(photoToMove);
			
				updateDisplay();
			}
		}
	}
	
	/**
	 * Dialog used when a user wants to copy/move a photo between albums
	 * @param keyword
	 * @param list
	 * @return the album the user wishes to move/copy a photo to
	 */
	public static String moveDialog(String keyword, List<String> list)
	{
		ChoiceDialog<String> moveDialog = new ChoiceDialog<String>(list.get(0), list);
		Optional<String> result;
		
		if (keyword == "move")
		{
			moveDialog.setTitle("Move a Photo");
			moveDialog.setHeaderText("Choose an album to move your photo to.");
			moveDialog.setContentText("Albums:");
		
			result = moveDialog.showAndWait();
			
			if (result.isPresent())
			{
				return result.get().trim();
			}
		} else if (keyword == "copy") {
			moveDialog.setTitle("Copy a Photo");
			moveDialog.setHeaderText("Choose an album to copy your photo to.");
			moveDialog.setContentText("Albums:");
		
			result = moveDialog.showAndWait();
			
			if (result.isPresent())
			{
				return result.get().trim();
			}
		}
		
		return null;
	}
	
	/**
	 * Searches for a specific album by its name - denoted as key
	 * @param key
	 * @param iter
	 * @return an album matching the key
	 */
	public User.Album albumSearch(String key, Iterator<User.Album> iter)
	{
		while(iter.hasNext())
		{
			User.Album album = iter.next();
			if (album.getAlbumName().equals(key))
			{
				return album;
			}
		}
		
		return null;
	}
	
	/**
	 * Adds a tag to a photo
	 * @param e
	 */
	public void handleAddTag(ActionEvent e)
	{
		String tag = tagTypeDialog("add");
		
		if (tag != null)
		{
			User.Album.Photo photo = photoList.getSelectionModel().getSelectedItem();
			
			String[] args = tag.split(CHARSPLIT);
			
			if(photo.addTag(args[0], args[1]))
			{
				updateDisplay();
			} else {
				errorPopup(SAMETAG);
			}
		}
	}
	
	/**
	 * User enters in a tag they want to edit.
	 * @param keyword
	 * @return the tag the user entered in
	 */
	public static String editDialog(String keyword)
	{
		TextInputDialog addDialog = new TextInputDialog();
		Optional<String> result;
		
		if (keyword == "person" || keyword == "location")
		{
			addDialog.setTitle("Tag Editing");
			addDialog.setHeaderText("Add a " + keyword + " Tag");
			addDialog.setContentText("Enter Tag:");
			
			result = addDialog.showAndWait();
			
			if(result.isPresent())
			{
				if(result.get().isEmpty())
				{
					errorPopup(NOINPUT);
					
					return null;
				}
				
				return keyword + CHARSPLIT + result.get().trim();
			}
		} else if (keyword == "caption") {
			addDialog.setTitle("Caption Editing");
			addDialog.setHeaderText("Edit your " + keyword);
			addDialog.setContentText("Enter Caption:");
			
			result = addDialog.showAndWait();
			
			if(result.isPresent())
			{
				return result.get().trim();
			}
		}
		
		return null;
		
	}
	
	/**
	 * User decides whether they want to add/delete a person or location tag.
	 * Operation (add/delete) is determined by operation argument.
	 * @param operation
	 * @return the tag the user wishes to edit
	 */
	public static String tagTypeDialog(String operation)
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Tag Editing");
		alert.setHeaderText("Tag Type");
		
		ButtonType personButton = new ButtonType("Person");
		ButtonType locationButton = new ButtonType("Location");
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		
		alert.getButtonTypes().setAll(personButton, locationButton, cancelButton);
		
		if (operation == "add")
		{
			alert.setContentText("Select the type of tag you want to add.");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == personButton)
			{
			    return editDialog("person");
			} else if (result.get() == locationButton) {
				return editDialog("location");
			} else {
			    // ... user chose CANCEL or closed the dialog
			}
		} else if (operation == "delete") {
			alert.setContentText("Select the type of tag you want to delete.");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == personButton)
			{
				return editDialog("person");
			} else if (result.get() == locationButton) {
				return editDialog("location");
			} else {
			    // ... user chose CANCEL or closed the dialog
			}
		}
		
		return null;
	}
	
	/**
	 * Removes a tag from a photo
	 * @param e
	 */
	public void handleRemoveTag(ActionEvent e)
	{
		String tag = tagTypeDialog("delete");
		
		if (tag != null)
		{
			User.Album.Photo photo = photoList.getSelectionModel().getSelectedItem();
			
			String[] args = tag.split(CHARSPLIT);
			
			System.out.println(args[0] + "," + args[1]);

			if(photo.removeTag(args[0], args[1]))
			{
				updateDisplay();
			} else {
				errorPopup(NONEXISTANTTAG);
			}
		}
	}
	
	/**
	 * 
	 * @param e
	 */
	public void handleCaptionOptions(ActionEvent e)
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Caption Options");
		alert.setHeaderText("Tag Type");
		
		ButtonType editButton = new ButtonType("Edit Caption");
		ButtonType removeButton = new ButtonType("Remove Caption");
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		
		alert.getButtonTypes().setAll(editButton, removeButton, cancelButton);
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == editButton)
		{
		    String caption = editDialog("caption");
		    
			if (caption != null)
			{
				User.Album.Photo photo = photoList.getSelectionModel().getSelectedItem();
				
				photo.setCaption(caption);
				updateDisplay();
			}
		} else if (result.get() == removeButton) {
			User.Album.Photo photo = photoList.getSelectionModel().getSelectedItem();
			
			if (photo.getCaption() != null)
			{
				photo.setCaption("");
				updateDisplay();
			}
		} else {
		    // ... user chose CANCEL or closed the dialog
		}
	}
	
	/**
	 * Changes scene to the slideshow scene
	 * @param e
	 * @throws IOException
	 */
	public void handeSlideshow(ActionEvent e) throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/Slideshow.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		
		Scene scene = new Scene(root);
		Stage primaryStage = (Stage) logoutButton.getScene().getWindow();  // gets the current stage 
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	/**
	 * Returns to the previous scene - in this case, the Album scene
	 * @param e
	 * @throws IOException
	 */
	public void handleReturn(ActionEvent e) throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/Album.fxml"));
		BorderPane root = (BorderPane) loader.load();
		
		Scene scene = new Scene(root);
		Stage primaryStage = (Stage) returnToAlbumButton.getScene().getWindow();  // gets the current stage 
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	/**
	 * Logs a user out
	 * @param e
	 * @throws IOException
	 */
	public void handleLogout(ActionEvent e) throws IOException
	{
		
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
	 * Quits the application and saves user data
	 * @param e
	 * @throws IOException
	 */
	public void handleQuit(ActionEvent e) throws IOException
	{
		
		Persistance.writeUser();
		
		e.consume();
		
		Stage primaryStage = (Stage) quitAppButton.getScene().getWindow();  // gets the current stage
		primaryStage.close();
	}
	
	/**
	 * Adjusts the date range of an album - single photo version
	 * @param album
	 * @param img
	 */
	public static void adjustDate(User.Album album, User.Album.Photo img)
	{
		album.setBeginDate(img.getPhotoDate());
		album.setEndDate(img.getPhotoDate());
	}
	
	/**
	 * Adjusts the date range of an album
	 * @param album
	 */
	public static void adjustDateRange(User.Album album)
	{
		Iterator<User.Album.Photo> photoIter = album.photoIterator();
		User.Album.Photo photo;
		
		if(!photoIter.hasNext())
		{
			album.setBeginDate(null);
			album.setEndDate(null);
		} else {
			if (album.getBeginDate() == null || album.getEndDate() == null)
			{
				photo = photoIter.next();
				adjustDate(album, photo);
			} else {
				album.setBeginDate(new Date());
				
				Date endDate = new Date();
				endDate.setYear(0);
				album.setEndDate(endDate);
				
				while(photoIter.hasNext())
				{
					photo = photoIter.next();
					if(photo.getPhotoDate().before(album.getBeginDate()))
					{				
						album.setBeginDate(photo.getPhotoDate());
					}
			
					if(photo.getPhotoDate().after(album.getEndDate())) 
					{		
						album.setEndDate(photo.getPhotoDate());
					}
				}
			}
		}
	}
	
	/**
	 * Shows an error popup depending on the error code passed in
	 * @param errorCode
	 */
	public static void errorPopup(int errorCode)
	{
		Alert error = new Alert(AlertType.ERROR);
		
		switch (errorCode)
		{
			case INVALDFILEPATH /*image does not exist at file path anymore*/:
				error.setTitle("Invalid File Path");
				error.setContentText("The file path is invalid for one or more photos. Photo information for those photos was deleted from application.");
				break;
			case NOINPUT /*tag input error*/:
				error.setTitle("Tag Input Error");
				error.setContentText("No tag was entered. Please enter in a tag.");
				break;
			case SAMETAG /*add tag same tag error*/:
				error.setTitle("Same Tag Error");
				error.setContentText("Duplicate tag detected. Tag not added.");
				break;
			case NONEXISTANTTAG /*remove tag error*/:
				error.setTitle("Invalid Tag Error");
				error.setContentText("Entered tag does not exist.");
				break;
			default:
				error.setTitle("Default error.");
				error.setContentText("An incorrect error code was called. Sorry about that!");
				break;
		}
		
		error.show();
	}
	
	/**
	 * 
	 * @return the char that indicates which part of a tag string is the name and value
	 */
	public static String getCharSplit()
	{
		return CHARSPLIT;
	}

}