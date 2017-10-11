/* Software Methodology Assignment 1: SongLib
 * Christopher Gonzalez
 * Robert Barbaro
 * Last modified: 10/3/17
 */
package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import songObject.Song;
import songObject.SongCompare;

public class Controller {
	
	
	@FXML
	ListView<Song> songLibrary;
	
	@FXML
	Button addBtn;
	
	@FXML
	TextField titleField, artistField, albumField, yearField;
	
	@FXML
	Label titleDisplay, artistDisplay, albumDisplay, yearDisplay;
	
	private ObservableList<Song> songList = FXCollections.observableArrayList();
		
	String file = "src/songfile.txt";
	
	
	public void start(Stage primaryStage) {
		
		String line;
		Song song;
		
		try {
			
			FileReader fr = new FileReader(file);
			
			BufferedReader bw = new BufferedReader(fr);
			
			while((line = bw.readLine()) != null) {
				
				
				String[] songArgs = line.split(",");
				
				//song = new Song(songArgs[0],songArgs[1],songArgs[2],songArgs[3]);				
				
				//songList.add(song);
				
				//decide how to initialize song object based on args in the line
				switch (songArgs.length)
				{
				case 2:
					song = new Song(
							songArgs[0], songArgs[1]);
					songList.add(song);
					break;
				case 3:
					song = new Song(
							songArgs[0], songArgs[1], songArgs[2]);
					songList.add(song);
					break;
				case 4:
					song = new Song(
							songArgs[0], songArgs[1], songArgs[2], songArgs[3]);
					songList.add(song);
					break;
				default:
					System.out.println("ERROR");
}
			}
			
			bw.close();
			
		} catch(FileNotFoundException ex) {
			
			System.out.println("Cannot find '" + file + "'");
			
		} catch(IOException ex) {
			
			System.out.println("Error reading '" + file + "'");
			
		}
		
		/// On close; write new songList to file
		
		songLibrary.setItems(songList);
		
		FXCollections.sort(songList, new SongCompare());  /// MAY WANT TO GET RID OF SINCE THEY WILL ALWAYS BE SORTED!!!
		
		songLibrary.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> displaySong());
		
		if(!songList.isEmpty()) { // if list is not empty; select first song
			
			songLibrary.getSelectionModel().select(0);
		}
		
		primaryStage.setOnCloseRequest(event -> {
			
			writeToFile();
		});

	}
	
	
	public void handleAdd(ActionEvent e) {
		
		// Get values inputed in text box
		String title = titleField.getText().trim();
		String artist = artistField.getText().trim();
		String album = albumField.getText().trim();
		String year =  yearField.getText().trim();
		
		
		// Check if name and artist match an existing song
		
		
		
		Song newSong = new Song(title,artist,album,year);  // create new song object
		
		if(!checkInput(newSong,true)) {
			
			return;
			
		}
			
		songList.add(newSong);  // add song title to observable list
		
		int i = 0;
		
		for(Song s: songList) {
			
			if(s.getTitle().equals(newSong.getTitle()) && s.getArtist().equals(newSong.getArtist())) {
				
				songLibrary.getSelectionModel().select(i);
				
				break;
			}
			
			i++;
		}
		
		
		FXCollections.sort(songList, new SongCompare());
		
		
		
		
		titleField.clear();
		artistField.clear();
		albumField.clear();
		yearField.clear();
				
			
	}
	
	public void handleEdit(ActionEvent e) {
		
		
		// Get values inputed in text box
		String title = titleField.getText().trim();
		String artist = artistField.getText().trim();
		String album = albumField.getText().trim();
		String year =  yearField.getText().trim();
		
		
				
				
		int selectedIndex = songLibrary.getSelectionModel().getSelectedIndex();
		
		int i = 0;
		for(Song s: songList) {
			
			
			
			if(s.getTitle().equals(title) && s.getArtist().equals(artist) 
					&& selectedIndex != i) { // skips over check for song being edited
				
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Song Errror");
				error.setContentText("Cannot edit song to match title and artist");
				error.show();
				
				return;
			}
			i++;
			
		}
		
		Song selectedSong = songLibrary.getSelectionModel().getSelectedItem();
		
		if(title.isEmpty()) {
			
			title = selectedSong.getTitle();
		}
		
		if(artist.isEmpty()) {
			
			artist = selectedSong.getArtist();
		}
		
		if(album.isEmpty()) {
			
			album = selectedSong.getAlbum();
		}
		
		if(year.isEmpty()) {
			
			year = selectedSong.getYear();
		}


		Song updatedSong = new Song(title,artist,album,year);
		
			
		if(!checkInput(updatedSong,false)) {
			
			return;
			
		}

		
		
		songList.set(selectedIndex, updatedSong);
		
		FXCollections.sort(songList, new SongCompare());
		
		
		i = 0;
		for(Song s: songList) {
			
			
			
			if(s.getTitle().equals(title) && s.getArtist().equals(artist)) {
				
				break;
			}
			i++;
			
		}
		
		songLibrary.getSelectionModel().select(i);
		
				
		titleField.clear();
		artistField.clear();
		albumField.clear();
		yearField.clear();	
		
	
	}
	
	public void handleDelete(ActionEvent e) {
		

		if(songList.size() == 0) {
			
			return;
		}
		
		Song selectedTitle = songLibrary.getSelectionModel().getSelectedItem();  // gets title of song that is selected
		
		int i = 0;
		for(Song s: songList) {  // find song in observable list
			
			if(selectedTitle.getTitle().equals(s.getTitle()) && selectedTitle.getArtist().equals(s.getArtist())) {  
				
				break;
				
			}
			i++;
		}
		
		songList.remove(i);
		
		if(songList.size() == i) {  // if last item in list is deleted
			
			songLibrary.getSelectionModel().select(--i);  // select previous item in list
			
		} else {
			
			songLibrary.getSelectionModel().select(i);  // select next item in list
			
		}
			
		
	}
	
	public void handleClear(ActionEvent e) {
		
		titleField.clear();
		artistField.clear();
		albumField.clear();
		yearField.clear();
		
	}
	
	public boolean checkInput(Song s, boolean isAdd) {
		
		if(s.getTitle().trim().isEmpty() || s.getArtist().trim().isEmpty()) {
			
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Song Errror");
			error.setContentText("Must enter Title and Artist");
			error.show();
			
			return false;
			
		}
		
		
		if(isAdd) {
		// Check if name and artist match an existing song
			for(Song t: songList) {
									
				// If match, then display error
				if(t.getTitle().equals(s.getTitle()) && t.getArtist().equals(s.getArtist())) { 
						
						
					Alert error = new Alert(AlertType.ERROR);
					error.setTitle("Song Errror");
					error.setContentText("Cannot add song with matching title and artist");
					error.show();
				
					return false;
				}			
			}
		
		}
		
		
		
		
		String year = s.getYear();
		
		if(year.length() == 0) {
			
			return true;
		}
		
		
		if(year.length() != 4) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Year Error");
			error.setContentText("Year must be YYYY format");
			error.show();
			
			return false;
		}
		
		for(int i = 0; i < 4; i++) {
			
			if(!Character.isDigit(year.charAt(i))) {
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Year Error");
				error.setContentText("Year must be digits");
				error.show();
				
				return false;
			}
			
		}
		
		return true;
		
	}
	
	
	public void writeToFile() {
		
		try {
			
			FileWriter fw = new FileWriter(file);
			PrintWriter pw = new PrintWriter(fw);
			
			for(Song s: songList) {
				
				
				
				 if(s.getAlbum().trim().isEmpty() && s.getYear().trim().isEmpty()) {
					 					
					pw.print(s.getTitle() + "," + s.getArtist() + "," + " " + "," + " " + "," + "\n");
			
				 } else if(s.getAlbum().trim().isEmpty()) {
					 
					pw.print(s.getTitle() + "," + s.getArtist() + "," + " " + "," + s.getYear() + "," + "\n");
					
				} else if(s.getYear().trim().isEmpty()) {
								
					pw.print(s.getTitle() + "," + s.getArtist() + "," + s.getAlbum() + "," + " " + "," + "\n");
					
				} else {
					
					pw.printf("%s,%s,%s,%s,\n",s.getTitle(),s.getArtist(),s.getAlbum(),s.getYear());
				}
				
				
				
			}
			
			pw.close();
			
		} catch(FileNotFoundException ex) {
			
			System.out.println("Cannot find '" + file + "'");
			
		} catch(IOException ex) {
			
			System.out.println("Error reading '" + file + "'");
			
		}
	}
	
	public void displaySong() {  // displays song when selected
		
		if(songList.isEmpty()) {
			
			titleDisplay.setText("");
			artistDisplay.setText("");
			albumDisplay.setText("");
			yearDisplay.setText("");
			
		}
		
		
		Song s = songLibrary.getSelectionModel().getSelectedItem();  // gets title of song that is selected
		
		if(s != null) {
			
			titleDisplay.setText(s.getTitle());
			artistDisplay.setText(s.getArtist());
			albumDisplay.setText(s.getAlbum());
			yearDisplay.setText(s.getYear());
		}
	}
	
}
