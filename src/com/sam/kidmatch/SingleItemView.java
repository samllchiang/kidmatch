package com.sam.kidmatch;



import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sam.utility.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class SingleItemView extends Activity {
	public final int SELECT_CLOUDPHOTO=10;// requestCode for take uri picture 
	int position;
	String photoUri,cloudObiectId;
	Context context;
	private  List<Elem> listQueryElem;
	Bitmap bitmapdata;
	byte[] parseFile2ByteArray;
	ImageLoader imageLoader = new ImageLoader(this);
	private static KidMatchApp appState;
	private ParseFile imageFile;
	private Resources res=null;
	Bitmap cs = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from singleitemview.xml
		setContentView(R.layout.singleitemgridview);
		context = this;
		res = this.getResources();
		
		 
		Intent i = getIntent();
		// Get the intent from ListViewAdapter
		photoUri = i.getStringExtra("photouri");
		cloudObiectId = i.getStringExtra("cloudObiectId");
		position = i.getIntExtra("position", position);
		Log.e("---SingleItemView---","---cloudObiectId---"+cloudObiectId);
		Log.e("---SingleItemView---","---position---"+position);
		
		try {
			ParseQuery<Elem> query = new ParseQuery<Elem>("Elem");
			query.orderByAscending("createAt");
			listQueryElem = query.find();
			
			appState = (KidMatchApp) context.getApplicationContext(); 		 			 
			appState.cloudObiectId = listQueryElem.get(position).getObjectId();
			imageFile = listQueryElem.get(position).getImageFile();
			parseFile2ByteArray = imageFile.getData(); // convert to byte Array
			 
			Bitmap bitmapdata = BitmapFactory.decodeByteArray(parseFile2ByteArray, 0, parseFile2ByteArray.length);
			appState.bitmapdata = bitmapdata;
			appState.isCloud = true;
          	if(appState.cloudObiectId!=null) Log.e("---cloudObiectId--","--cloudObiectId--"+appState.cloudObiectId);
           
  			 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		// Locate the ImageView in singleitemview.xml
//		ImageView imgphoto = (ImageView) findViewById(R.id.singlephoto);
//		// Load image into the ImageView
//		imageLoader.DisplayImage(photoUri, imgphoto);
		
		Intent intent = new Intent(context, AddElemActivity.class);		 
		intent.putExtra("cloudObiectId", listQueryElem.get(position).getObjectId());
		intent.putExtra("position", position);
		intent.putExtra("bitmapdata", bitmapdata);
		intent.putExtra("parseFile2ByteArray", parseFile2ByteArray);
		context.startActivity(intent);
		startActivity(intent);
		
	}
}