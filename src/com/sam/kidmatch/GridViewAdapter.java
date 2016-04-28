package com.sam.kidmatch;

import java.util.ArrayList;
import java.util.List;

import com.sam.kidmatch.CameraFragment;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sam.utility.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GridViewAdapter extends BaseAdapter {

	// Declare Variables
	public String cloudObiectId=null;
	Context context;
	LayoutInflater inflater;
	ImageLoader imageLoader;
	private static KidMatchApp appState;
	Bitmap bitmapdata;
	byte[] parseFile2ByteArray;
	
	private List<Photo> photoUriArraylist = null;
	private ArrayList<Photo> arraylist;
	private List<Elem> listQueryElem;

	public GridViewAdapter(Context context, List<Photo> photoUriArraylist) {
		this.context = context;
		this.photoUriArraylist = photoUriArraylist;
		inflater = LayoutInflater.from(context);
		this.arraylist = new ArrayList<Photo>();
		this.arraylist.addAll(photoUriArraylist);
		imageLoader = new ImageLoader(context);
	}

	public class ViewHolder {
		ImageView photo;
	}

	@Override
	public int getCount() {
		return photoUriArraylist.size();
	}

	@Override
	public Object getItem(int position) {
		return photoUriArraylist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.gridview_item, null);
			// Locate the ImageView in gridview_item.xml
			holder.photo = (ImageView) view.findViewById(R.id.photo);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		// Load image into GridView
		imageLoader.DisplayImage(photoUriArraylist.get(position).getphoto(),
				holder.photo);
		// Capture GridView item click
//		view.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {			
//				
//				    
//				try {
//					ParseQuery<Elem> query = new ParseQuery<Elem>("Elem");
//					query.orderByAscending("createAt");
//					listQueryElem = query.find();
//					
//					appState = (KidMatchApp) context.getApplicationContext(); 
//					appState.uriGrid = photoUriArraylist.get(position).getphoto();			 
//					appState.cloudObiectId = listQueryElem.get(position).getObjectId();
//					ParseFile imageFile = listQueryElem.get(position).getImageFile();
//					parseFile2ByteArray = imageFile.getData(); // convert to byte Array
//					 
//					Bitmap bitmapdata = BitmapFactory.decodeByteArray(parseFile2ByteArray, 0, parseFile2ByteArray.length);
//					appState.bitmapdata = bitmapdata;
//					appState.isCloud = true;
//		          	if(appState.cloudObiectId!=null) Log.e("---cloudObiectId--","--cloudObiectId--"+appState.cloudObiectId);
//		           
//          			 
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			 
//				// Send single item click data to SingleItemView Class
//				Intent intent = new Intent(context, SingleItemView.class);
//				//Intent intent = new Intent(context, AddElemlFragment.class);
//				 
//				// Pass all data phone
//				intent.putExtra("photouri", photoUriArraylist.get(position).getphoto());
//				intent.putExtra("cloudObiectId", listQueryElem.get(position).getObjectId());
//				intent.putExtra("position", position);
//				intent.putExtra("bitmapdata", bitmapdata);
//				intent.putExtra("parseFile2ByteArray", parseFile2ByteArray);
//				context.startActivity(intent);
//			}
//		});
		return view;
	}//view.setOnClickListener
	
	
	
	
}//end of GridViewAdapt
