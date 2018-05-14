package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Serializes data in between sessions
 * 
 * @author Robert Barbaro
 * 
 * @author Christopher Gonzalez
 *
 */
public class Persistance {
	
	/**
	 * Holds all the user objects in the application
	 */
	private static List<User> users = new ArrayList<User>();  // Array which holds all the User objects that were created
	
	
	/**
	 * @param user takes in user object
	 */
	public static void addUser(User user) {
		
		users.add(user);
	}
	
	/**
	 * @param index location of the user to be deleted
	 */
	public static void delUser(int index) {
		
		users.remove(index);
	}
	
	/**
	 * @param index location of where to get the User
	 * @return returns the User at that location
	 */
	public static User getUser(int index) {
		
		return users.get(index);
	}
	
	/**
	 * @return returns iterator to iterate through each user
	 */
	public static Iterator<User> userIterator() {
		
		return users.iterator();
	}
	
	/**
	 * @throws IOException throws if file does not exist
	 */
	public static void writeUser() throws IOException {
		
		String fileName = "users.dat";
				
		ObjectOutputStream os = null;

		try {
			
			 os = new ObjectOutputStream(new FileOutputStream(fileName));
			 
			 os.writeObject(users);
			
		} catch(Exception e) {
			
			e.printStackTrace();
		}

		
		os.close();
		
	}
	
	/**
	 * @throws IOException throws if file does not exist
	 * @throws ParseException throws if date cannot be parsed
	 */
	public static void readUser() throws IOException, ParseException {
		
		String fileName = "users.dat";
		
		File file = new File("users.dat");
		
		String[] names = {"Island", "Mountain", "Warrior", "Git", "Anime","Class","Ground","Deck","Friends","Love"};
		
		String[] fileLoc = {"src/stock/photo1.jpg", "src/stock/photo2.jpg", "src/stock/photo3.png", "src/stock/photo4.png", "src/stock/photo5.jpg",
				 "src/stock/photo6.jpg", "src/stock/photo7.jpg", "src/stock/photo8.jpg", "src/stock/photo9.jpg", "src/stock/photo10.jpg"};
		
		User.Album stockAlbum = new User.Album("stock album");
		
		if(file.length() == 0) {
			
			User stock = new User("stock");
			
			users.add(stock);
			
			for(int i = 0; i < fileLoc.length; i++) {
		
				User.Album.Photo image = new User.Album.Photo(names[i],fileLoc[i]);
				
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				
				String s = sdf.format(file.lastModified());
				
				Date date = sdf.parse(s);
				
				image.setPhotoDate(date); 
				
				stockAlbum.setBeginDate(image.getPhotoDate());
				stockAlbum.setEndDate(image.getPhotoDate());
				
				if(image.getPhotoDate().before(stockAlbum.getBeginDate())) {
					
					stockAlbum.setBeginDate(image.getPhotoDate());
				}
				
				if(image.getPhotoDate().after(stockAlbum.getEndDate())) {
										
					stockAlbum.setEndDate(image.getPhotoDate());
				}
				
				stockAlbum.addPhoto(image);	
			
			}
			
			//users.add(stock);
			
			stock.addAlbum(stockAlbum);
			
			//users.add(stock);
			
			
			
			return;
		}
				
		try {
			
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
			 
	        users = (List<User>) in.readObject(); 	
	        
	        in.close();
	        
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
