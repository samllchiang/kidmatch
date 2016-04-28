package com.sam.utility;

import java.io.File;

import android.graphics.BitmapFactory;
import android.os.Environment;

public class MyFunc {

	public static File getPicName2myQus(int ID)
	{
		 
		File sdcard = Environment.getExternalStorageDirectory();
        File mydir = new File(sdcard.toString() + "/mylist");
        if (! mydir.exists())
        {
        	mydir.mkdir();
        }
        
        File pic = new File(mydir.toString() + "/" + "p" + ID + ".jpg");
        return pic;
	}
	
	public static File getPicName2myAns(int ID)
	{
		 
		File sdcard = Environment.getExternalStorageDirectory();
        File mydir = new File(sdcard.toString() + "/myDraw");
        if (! mydir.exists())
        {
        	mydir.mkdir();
        }
        
        File pic1 = new File(mydir.toString() + "/" + "dwg" + ID + ".jpg");
        return pic1;
	}
	
	
	
	
	
}
