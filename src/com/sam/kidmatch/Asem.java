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

@ParseClassName("Asem")
public class Asem extends ParseObject {

	public static final Asem objectId = null;

	public Asem() {
		// A default constructor is required.
	}
    //----title---
	public String getTitle() {
		return getString("asemTitle");
	}

	public void setTitle(String title) {
		put("asemTitle", title);
	}
    //----auther---
	public ParseUser getAuthor() {
		return getParseUser("author");
	}

	public void setAuthor(ParseUser user) {
		put("author", user);
	}
   //---content---
	public String getContent() {
		return getString("asemContent");
	}

	public void setContent(String cont) {
		put("asemContent", cont);
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
	//---class---
	public String getClas() {
		return getString("asemClass");
	}

	public void setClas(String clas) {
		put("asemClass", clas);
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
			return getInt("asemPosition");
		}
	    
		public void setPosition(Integer position) {
			put("asemPosition", position);
		}
		 
	
}
