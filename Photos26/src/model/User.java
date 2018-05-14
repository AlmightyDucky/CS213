package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.User.Album.Photo;
import view.LoginController;

public class User implements Serializable{
	
	String userName;  // contains the user's user name
	
	private List<Album> albums = new ArrayList<Album>();  // holds all the albums that a User has
	
	private List<Album.Photo> userPhotos = new ArrayList<Album.Photo>();
	
	public User(String userName) { // User constructor
		
		this.userName = userName;
	}
	
	public String toString() {
		
		return userName;
	}
	
	public void setUserName(String userName) {
		
		this.userName = userName;
	}
		
	
	public void addAlbum(Album album) {
		
		albums.add(album);
	}
	
	public void delAlbum(int index) {
		
		albums.remove(index);
	}
	
	public Album getAlbum(int index) {
		
		return albums.get(index);
	}
	
	public Iterator<Album> albumIterator() {
		
		return albums.iterator();
	}
	
	public Iterator<Photo> userPhotosIterator() {
		
		return userPhotos.iterator();
	}
	
	
	public void updateUserPhotos()
	{
		boolean photoExistsInAnAlbum = false;
		
		Iterator<Album> albumsToCheck = albumIterator();
		Iterator<Photo> userPhotosIter = userPhotosIterator();
		
		if (userPhotosIter.hasNext())
		{
			if (albumsToCheck.hasNext())
			{
				while(userPhotosIter.hasNext())
				{
					photoExistsInAnAlbum = false;
					Photo userPhotoToCheck = userPhotosIter.next();
					
					while (albumsToCheck.hasNext())
					{
						Album currentAlbum = albumsToCheck.next();
						Iterator<Photo> photosToCheck = currentAlbum.photoIterator();
						
						while (photosToCheck.hasNext())
						{
							Photo currentPhoto = photosToCheck.next();
							
							if (currentPhoto.isEqual(userPhotoToCheck))
							{
								photoExistsInAnAlbum = true;
								break;
							}
					
						}
						
						if (photoExistsInAnAlbum)
						{
							break;
						}
					}
					
					
					if (!photoExistsInAnAlbum)
					{
						userPhotos.remove(userPhotoToCheck);
					}
					
				}
			} else {
				while (userPhotosIter.hasNext())
				{
					userPhotosIter.next();
					userPhotosIter.remove();
				}
			}
		}
	}
	
	
	public static class Album implements Serializable{  // Album in which Photos are contained
		
		private List<Photo> photos = new ArrayList<Photo>();  // holds all the photos within the album
		
		private String albumName;
		
		private Date beginDate;

		private Date endDate;
		
		private String dateRange; 
		
		private int numOfPhotos;
		
		public Album(String albumName) {
			
			this.albumName = albumName;
			
			numOfPhotos = 0;
		}
		
		public void setAlbumName(String albumName) {
			
			this.albumName = albumName;
		}
		
		public String getAlbumName() {
			
			return albumName;
		}
		
		public String getNumOfPhotos() {
			
			return Integer.toString(numOfPhotos);
		}
		
		public void opNumOfPhotos(int num, char op)
		{
			if (op == '+')
			{
				numOfPhotos += num;
			} else if (op == '-')
			{
				numOfPhotos -= num;
			}
		}
		
		public void addPhoto(Photo p)
		{
			User currentUser = Persistance.getUser(LoginController.getUserIndex());
			Iterator<Photo> photoIter = currentUser.userPhotos.iterator();
			Photo photoToAdd = null;
			
			while (photoIter.hasNext())
			{
				photoToAdd = photoIter.next();
				if(p.isFileLocationEqual(photoToAdd))
				{
					p.setCaption(photoToAdd.caption);
					//p.setTags("name", photoToAdd.getTags("name"));
					//p.setTags("location", photoToAdd.getTags("location"));
					break;
				} else {
					photoToAdd = null;
				}
			}
			
			if (photoToAdd != null)
			{
				photos.add(photoToAdd);
			} else {
				currentUser.userPhotos.add(p);
				photos.add(p);
			}
			
			numOfPhotos++;
		}
		
		public void deletePhoto(Photo p)
		{
			User currentUser = Persistance.getUser(LoginController.getUserIndex());
			int lastCopy = 0;
			
			photos.remove(p);
			
			/*remove photo from user*/
			Iterator<Album> userAlbums = currentUser.albumIterator();
			while(userAlbums.hasNext())
			{
				Album i = userAlbums.next();
				Iterator<Photo> albumPhotos = i.photoIterator();
				while (albumPhotos.hasNext())
				{
					Photo j = albumPhotos.next();
					if (p.isEqual(j))
					{
						lastCopy = 1;
						break;
					}
				}
				
				if (lastCopy == 1)
				{
					break;
				}
			}
			
			if (lastCopy == 0)
			{
				currentUser.userPhotos.remove(p);
			}

			if (numOfPhotos <= 0)
			{
				numOfPhotos = 0;
			} else {
				numOfPhotos--;
			}
		}
		
		public void setBeginDate(Date d) {
			
			beginDate = d;
		}
		
		public Date getBeginDate() {
			
			return beginDate;
		}
		
		public Date getEndDate() {
			
			return endDate;
		}
		
		public void setEndDate(Date d) {
			
			endDate = d;
		}
		
		public Iterator<Album.Photo> photoIterator() {
			
			return photos.iterator();
		}
		
		public String getDateRange() {
			
			if(beginDate == null) {
				
				return " - ";
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			
			return  sdf.format(beginDate) + " - " + sdf.format(endDate);
		}
		
		public String toString() {
			
			return String.format("%s %50s %s - %s", albumName,numOfPhotos,beginDate,endDate);
		}
		
			
		public static class Photo implements Serializable {  // Contains the Photos
			
			private String photoName;
			
			private String location;
			
			private String caption = "";
						
			private Date photoDate;
			
			private List<Tag> photoTags = new ArrayList<Tag>();
			
			public Photo(String name, String location)
			{
				photoName = name;
				this.location = location;
			}
			
			public boolean addTag(String name, String value)
			{
				for (Iterator<Tag> i = photoTags.iterator(); i.hasNext();)
				{
					Tag element = i.next();
					if(element.isEqual(name, value))
					{
						return false;
					}
				}
				photoTags.add(new Tag(name, value));
				
				return true;
			}
			
			public boolean removeTag(String name, String value)
			{
				for (Iterator<Tag> i = photoTags.iterator(); i.hasNext();)
				{
					Tag element = i.next();
					if(element.isEqual(name, value))
					{
						i.remove();
						return true;
					}
				}

				return false;
			}
			
			public Iterator<Tag> tagIterator() {
				
				return photoTags.iterator();
			}
			
			public String getPhotoName()
			{
				return photoName;
			}
			
			public void setPhotoName(String name)
			{
				photoName = name;
			}
			
			public String getPhotoLocation()
			{
				return location;
			}
			
			public void setPhotoLocation(String filePath)
			{
				location = filePath;
			}
			
			public String getCaption()
			{
				return caption;
			}
			
			public void setCaption(String caption)
			{
				this.caption = caption;
			}
			
			public Date getPhotoDate() {
				
				return photoDate;
			}
			
			public String getPhotoDateString()
			{
				return photoDate.toString();
			}
			
			public void setPhotoDate(Date d) {
				
				photoDate = d;
			}

			public String getTags(String name)
			{
				StringBuilder tagList = new StringBuilder();
				
				for (Iterator<Tag> i = photoTags.iterator(); i.hasNext();)
				{
					Tag element = i.next();
					String tagSet[] = element.toString().split("~");
					if (tagSet[0].equals(name))
					{
						tagList.append(tagSet[1] + ",");
					}
				}
				
				if (tagList.length() > 0)
				{
					tagList.deleteCharAt(tagList.length()-1);
				}
				
				return tagList.toString();
			}
			
			public void setTags(String name, String tags)
			{
				String[] tagArray = tags.split(",");
				
				for (int i = 0; i < tagArray.length; i++)
				{
					Tag tag = new Tag(name, tagArray[i]);
					photoTags.add(tag);
				}
			}
			
			public String getAllTags()
			{
				StringBuilder tagList = new StringBuilder();
				
				for (Iterator<Tag> i = photoTags.iterator(); i.hasNext();)
				{
					Tag element = i.next();
					String tagSet[] = element.toString().split("~");
					tagList.append(tagSet[1] + ",");
				}
				
				if (tagList.length() > 0)
				{
					tagList.deleteCharAt(tagList.length()-1);
				}
				
				return tagList.toString();
			}
			
			/**
			 * 
			 * @param p
			 * @return true if two photos share the same file location
			 */
			public boolean isFileLocationEqual(Photo p)
			{
				return this.getPhotoLocation().compareTo(p.getPhotoLocation()) == 0;
			}
			
			/**
			 * 
			 * @param p
			 * @return true if two photos are equal
			 */
			public boolean isEqual(Photo p)
			{
				return this.getPhotoName().compareTo(p.getPhotoName()) == 0
						&& this.getPhotoLocation().compareTo(p.getPhotoLocation()) == 0
						&& this.getPhotoDateString().compareTo(p.getPhotoDateString()) == 0
						&& this.getCaption().compareTo(p.getCaption()) == 0
						&& this.getAllTags().compareTo(p.getAllTags()) == 0;
			}
			
			/**
			 * returns the photoName
			 */
			public String toString()
			{
				return getPhotoName();
			}
		}
		
	}

}
