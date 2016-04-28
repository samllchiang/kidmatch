package com.sam.kidmatch;

import android.app.Application;
import android.graphics.Bitmap;
 
 
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.sam.login.Constants;

public class KidMatchApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		//ParseCrashReporting.enable(this);

		/*
		 * In this tutorial, we'll subclass ParseObject for convenience to
		 * create and modify Elem objects and Asem objects
		 */
		ParseObject.registerSubclass(Elem.class);
		
		ParseObject.registerSubclass(Asem.class);
 		 
		
		// enable the Local Datastore
		// Parse.enableLocalDatastore(KidMatchApp.this);
		Parse.enableLocalDatastore(getApplicationContext());
		
		
		// Add your initialization code for PualPusher(sam)
		Parse.initialize(this,"xzlv6HeMQdpsG78Zk3Ey5XwMg3zF8pJAVHc7Lux9", 
		                      "uLXGRasYCCQAYvZlaro83rkgzXYB29HSSjbobVpa");

		/*
		 * This app lets an anonymous user create and save photos of pictures.
		 * An anonymous user is a user that can be created
		 * without a username and password but still has all of the same
		 * capabilities as any other ParseUser.
		 * 
		 * After logging out, an anonymous user is abandoned, and its data is no
		 * longer accessible. In your own app, you can convert anonymous users
		 * to regular users so that data persists.
		 * 
		 * Learn more about the ParseUser class:
		 * https://www.parse.com/docs/android_guide#users
		 */
		ParseUser.enableAutomaticUser();

		//Save the current user
		//ParseUser.getCurrentUser().saveInBackground();
		
		ParseACL defaultACL = new ParseACL();

		/*
		 * If you would like all objects to be private by default, remove this
		 * line
		 */
		defaultACL.setPublicReadAccess(true);
		defaultACL.setPublicWriteAccess(true);

		ParseACL.setDefaultACL(defaultACL, true);
		
		
		 
	}//onCreate()
	
	// Declaring the updateParseInstallation method
	public static void updateParseInstallation(ParseUser user) {
	// Declare a parse installation variable
	ParseInstallation intst = ParseInstallation.getCurrentInstallation();
	intst.put(Constants.KEY_USER_ID, user.getObjectId());
	// save this installation to parse backend
	intst.saveInBackground();
	}
	
	// Global parameter for class&border set up---------------------------------------
		public String value1="shap";
		public String [] valueClass;
		public String valueBorder1=null;
		public String borderNum="1";
		public String borderStyle;
		public String cloudObiectId;
		public String uriGrid;
		public boolean isBorder=true;
		public boolean isSquare=true;
		public boolean isCloud=false;
		public boolean isDefaule=false;
		public Integer numPng;
		public static String [] borderUri;
		public boolean isAnswerApp[];
		
		int score;
    // Global parameter for bitmap/parseFile data---------------------------------
		public Bitmap bitmapdata ; 
		public Bitmap cs ; 
		
		 
	
	

}
