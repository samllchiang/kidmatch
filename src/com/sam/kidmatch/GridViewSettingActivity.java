package com.sam.kidmatch;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

 
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class GridViewSettingActivity extends Activity {
	// Declare Variables
	public String cloudObiectId=null;
	
	Context context;
	GridView gridview;
	List<ParseObject> ob;
	ProgressDialog mProgressDialog;
	GridViewAdapter adapter;
	ParseFile imageFile;
	private List<Photo> photoarraylist = null;
	private  List<Elem> listQueryElem;
	private static KidMatchApp appState;
	private Bitmap bitmapdata;
	private byte[] parseFile2ByteArray;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from gridview_main.xml
		setContentView(R.layout.gridview_main);
		context = this;
		// Execute RemoteDataTask AsyncTask		
		new RemoteDataTask().execute();
//		gridview.setOnItemClickListener(new OnItemClickListener() {        
//			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {            
//				Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();        
//				}    
//			});
		
	}

	// RemoteDataTask AsyncTask
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progressdialog
			mProgressDialog = new ProgressDialog(GridViewSettingActivity.this);
			// Set progressdialog title
			mProgressDialog.setTitle("Select photo from cloud(parse.com).");
			// Set progressdialog message
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setIndeterminate(false);
			// Show progressdialog
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Create the array
			photoarraylist = new ArrayList<Photo>();
			try {
				// Locate the class table named "SamsungPhones" in Parse.com
				ParseQuery<Elem> query = new ParseQuery<Elem>("Elem");
				// Locate the column named "position" in Parse.com and order list
				// by ascending
				query.orderByAscending("createAt");
			 
				
				listQueryElem = query.find();			
				//sizelistQueryElem = listQueryElem.size();
				
				 
				for (ParseObject ele : listQueryElem) {						
				   
				Boolean   booischecked = (Boolean)   ele.get("isChecked");
				Integer   intPosition  = (Integer)   ele.get("position");
				String    txtTitle     = (String)    ele.get("title");
				String    txtContent   = (String)    ele.get("content");
				ParseFile image        = (ParseFile) ele.get("image");
				 
					
				Photo map = new Photo();
				map.setphoto(image.getUrl());
				photoarraylist.add(map);
				}//for (ParseObject ele : listQueryElem)
				
				
			} catch (ParseException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// Locate the gridview in gridview_main.xml
			gridview = (GridView) findViewById(R.id.gridview);
			// Pass the results into ListViewAdapter.java
			adapter = new GridViewAdapter(GridViewSettingActivity.this,photoarraylist);
			// Binds the Adapter to the ListView
			gridview.setAdapter(adapter);
			gridview.setOnItemClickListener(new OnItemClickListener() {        
				public void onItemClick(AdapterView<?> parent, View v, int position, long id) { 
					
					Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show(); 
				 
					//appState.uriGrid = photoUriArraylist.get(position).getphoto();			 
					cloudObiectId = listQueryElem.get(position).getObjectId();
					imageFile = listQueryElem.get(position).getImageFile();
					try {
						parseFile2ByteArray = imageFile.getData();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // convert to byte Array
					 
					bitmapdata = BitmapFactory.decodeByteArray(parseFile2ByteArray, 0, parseFile2ByteArray.length);
					appState = (KidMatchApp) context.getApplicationContext();
					appState.isCloud = true;
		          	if(cloudObiectId!=null) Log.e("---cloudObiectId--","--cloudObiectId--"+cloudObiectId);
		           					 
		          	Intent intent = new Intent(context, AddElemlFragment.class);		          	
		          	intent.putExtra("parseFile2ByteArray", parseFile2ByteArray);
		          	intent.putExtra("cloudObiectId", listQueryElem.get(position).getObjectId());
		          	setResult(RESULT_OK, intent);
		            finish();
		          			       
					}    
				});//gridview.setOnItemClickListener
			// Close the progressdialog
			mProgressDialog.dismiss();
		}//onPostExecute
	}//RemoteDataTask

	 
}//end