/* Software Methodology Assignment 1: SongLib
 * Christopher Gonzalez
 * Robert Barbaro
 * Last modified: 10/3/17
 */
package songObject;

public class Song {
	
	private String title;  // holds the name of song
	private String artist; // holds the artist's name
	private String album;  // holds the album's name
	private String year;  // holds the year album was created
	
	public Song(String name, String artist) { // constructor for Song
		
		this(name, artist, "", "");
	}
	
	public Song(String name, String artist,  String album) { // 2nd constructor for Song
		
		this(name, artist, album, "");
	}
	
	public Song(String name, String artist, String album, String year) { // 3rd constructor for Song
		
		this.title = name;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}
	
	public String getTitle() {  // gets the name of song
		
		return title;
	}
	
	public String getArtist() { // gets the artist's name
		
		return artist;
	}
	
	public String getAlbum() {  // gets the name of album
		
		return album;
	}
	
	public String getYear() {  // gets the year album was created
		
		return year;
	}
	
	public void setTitle(String songName) {  // sets the name of song
		
		title = songName;
	}
	
	public void setArtist(String artistName) {  // sets the name of artist
		
		artist = artistName;
	}
	
	public void setAlbum(String albumName) {  // sets the name of album
		
		album = albumName;
	}
	
	public void setYear(String songYear) {  // sets the year album was created 
		
		this.year = songYear;
	}
	
	
	public String toString() {
		
		return title + " by " + artist;
	}

}
