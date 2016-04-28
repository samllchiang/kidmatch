package com.sam.kidmatch;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.BitmapFactory;
import android.os.Environment;

public class MyFunc {

	public static File getQusFile(String tile,String clas){
		 
		File sdcard = Environment.getExternalStorageDirectory();
        File mydir = new File(sdcard.toString() + "/myQus");
        if (! mydir.exists())
        {
        	mydir.mkdir();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File outfQus = new File(mydir.toString() + "/" +tile+"_"+timeStamp+" " +clas+ ".png");
        return outfQus;
	}
	
	public static File getAnsFile(String tile,String clas){
		 
		File sdcard = Environment.getExternalStorageDirectory();
        File mydir = new File(sdcard.toString() + "/myAns");
        if (! mydir.exists())
        {
        	mydir.mkdir();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File outfAns = new File(mydir.toString() + "/" +tile+"_"+timeStamp+" " +clas+ ".png");
        return outfAns;
	}
	
	public static File getTxtFile(String tile,String clas){
		 
		File sdcard = Environment.getExternalStorageDirectory();
        File mydir = new File(sdcard.toString() + "/myTxt");
        if (! mydir.exists())
        {
        	mydir.mkdir();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File outfAns = new File(mydir.toString() + "/" +tile+"_"+timeStamp+" " +clas+ ".png");
        return outfAns;
	}
		 	
}
