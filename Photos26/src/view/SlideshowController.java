package view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Persistance;
import model.User;

public class SlideshowController implements Initializable
{
	/**
	 * Displays the photo image
	 */
	@FXML
    private ImageView photoImage;
	
	/**
	 * Buttons within the scene
	 */
	@FXML 
	private Button leftButton, rightButton, returnToPhotoButton, logoutButton, quitAppButton;
	
	/**
	 * Array to hold all the photos in the slideshow
	 */
	private static User.Album.Photo[] slideshow;
	
	/**
	 * Current photo in slideshow array being displayed
	 */
	private static int currentPhoto = 0;
	
	/**
	 * Initializes the scene by displaying the first picture in the array
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		currentPhoto = 0;
		
		User currentUser = Persistance.getUser(LoginController.getUserIndex());
		
		Iterator<User.Album.Photo> photoIter = currentUser.getAlbum(AlbumController.getOpenAlbumIndex()).photoIterator();
		
		List<User.Album.Photo> photoList = new ArrayList<>();
		
		while(photoIter.hasNext())
		{
			photoList.add(photoIter.next());
		}
		
		slideshow = photoList.toArray(new User.Album.Photo[0]);
		
		updateDisplay("init");
		
	}
	
	/**
	 * Updates the image shown in the slideshow. Slideshow loops when it reaches either end of photos.
	 * @param keyword
	 */
	private void updateDisplay(String keyword)
	{
		if (slideshow.length != 0)
		{
			User.Album.Photo photo;
			File file;
			
			if (keyword.equals("init"))
			{
				photo = slideshow[currentPhoto];
				file = new File(photo.getPhotoLocation());
				
				if(file.exists())
				{
					Image display = new Image(file.toURI().toString());
					photoImage.setImage(display);
				}
			} else if (keyword.equals("left")) {
				if (currentPhoto <= 0)
				{
					currentPhoto = slideshow.length - 1;
				} else {
					currentPhoto --;
				}
				
				photo = slideshow[currentPhoto];
				file = new File(photo.getPhotoLocation());
				
				if(file.exists())
				{
					Image display = new Image(file.toURI().toString());
					photoImage.setImage(display);
				}
				
			} else if (keyword.equals("right")) {
				if (currentPhoto == slideshow.length - 1)
				{
					currentPhoto = 0;
				} else {
					currentPhoto ++;
				}
				
				photo = slideshow[currentPhoto];
				file = new File(photo.getPhotoLocation());
				
				if(file.exists())
				{
					Image display = new Image(file.toURI().toString());
					photoImage.setImage(display);
				}
			}
		} else {
			blankDisplay();
		}
	}
	
	/**
	 * Default display - a blank image
	 */
	public void blankDisplay()
	{
		photoImage.setImage(null);
	}
	
	/**
	 * Allows user navigation between photos with the buttons.
	 * @param e
	 */
	public void handleImageNavigation(ActionEvent e)
	{
		if(slideshow.length != 0)
		{
			if(e.getSource() == leftButton)
			{
				updateDisplay("left");
			} else if (e.getSource() == rightButton)
			{
				updateDisplay("right");
			}
		}
	}
	
	/**
	 * Returns to the previous scene - in this case, the Album scene
	 * @param e
	 * @throws IOException
	 */
	public void handleReturn(ActionEvent e) throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/Photo_Subsystem.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		
		Scene scene = new Scene(root);
		Stage primaryStage = (Stage) returnToPhotoButton.getScene().getWindow();  // gets the current stage 
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
}
