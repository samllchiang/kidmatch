package com.sam.kidmatch;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

/*
 * An extension of ParseObject that makes
 * it more convenient to access information
 * about a given Meal 
 */

@ParseClassName("Elem")
public class Elem extends ParseObject {

	//public static final String objectId = null;

	public Elem() {
		// A default constructor is required.
	}
    //----title---String
	public String getTitle() {
		return getString("title");
	}

	public void setTitle(String title) {
		put("title", title);
	}
    //----auther---
	public ParseUser getAuthor() {
		return getParseUser("author");
	}

	public void setAuthor(ParseUser user) {
		put("author", user);
	}   
	//---image---A question picture----> getImageFile()
	public ParseFile getImageFile() {
		return getParseFile("image");
	}

	public void setImageFile(ParseFile file) {
		put("image", file);
	}
	//---image---B answer picturee----> getAnsImageFile()
	public ParseFile getAnsImageFile() {
		return getParseFile("ansimage");
	}

	public void setAnsImageFile(ParseFile file) {
		put("ansimage", file);
	}
	//---image---c text picture----> getTxtImageFile()
		public ParseFile getTxtImageFile() {
			return getParseFile("txtimage");
		}

		public void setTxtImageFile(ParseFile file) {
			put("txtimage", file);
		}
	
	//---content---C content
	public String getContent() {
		return getString("content");
	}
	public void setContent(String cont) {
		put("content", cont);
	}
	//---class---String
	public String getClas() {
		return getString("class");
	}

	public void setClas(String clas) {
		put("class", clas);
	}
	//---isChecked-------boolean
	public void setIsChecked(boolean isChecked) {
		put("isChecked", isChecked);
	}
	
	public boolean getIsChecked() {
		return getBoolean("isChecked");
	}	
	//----position---integer
	public Integer getPosition() {
		return getInt("position");
	}
    
	public void setPosition(Integer position) {
		put("position", position);
	}
		 	
}
