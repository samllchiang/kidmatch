package com.sam.kidmatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;


import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
 


/*
 * The ElemListAdapter is an extension of ParseQueryAdapter
 * that has a custom layout for selected photo by class, including a 
 * bigger preview image. 
 */

public class ElemListAdapter extends ParseQueryAdapter<Elem> {
	
	int countAns = 1001;
	static int sizelistQueryElem;
	boolean isChecked0 = false;
	boolean isAnswer[];
	
	private static KidMatchApp appState;
	List<Photo> photoUriArraylist,ansPhotoUriArraylist ; //uri array to get uri of selected question photo and answer photo
	static List<Elem> listQueryElem;    //selected question photos in Elem class of parse.com
	List<Asem> listQueryAsem;   //select answer photos in Asem class of parse.com
	
	static List<Boolean> ansPhotoChecked; 
	
	 
	
	public ElemListAdapter(final Context context) {
		super(context, new ParseQueryAdapter.QueryFactory<Elem>() {
			public ParseQuery<Elem> create() {
								
				ParseQuery<Elem> query = new ParseQuery<Elem>("Elem");
//				query.whereContainedIn("class", Arrays.asList("face", "math"));
//				query.orderByDescending("rating");
				
				appState = (KidMatchApp) context.getApplicationContext();
				if(appState.value1==null){
					appState.value1="face,math,lang,shap,clor" ;
				}
				
				String [] splitClass =  appState.value1.split(",");
				  
				query.whereContainedIn("class", Arrays.asList(splitClass));
				
				try {
					listQueryElem = query.find();			
					sizelistQueryElem = listQueryElem.size();
					
					int count=0;
					for (ParseObject ele : listQueryElem) {						
					   
					Boolean   booischecked = (Boolean)   ele.get("isChecked");
					Integer   intPosition  = (Integer)   ele.get("position");
					String    txtTitle     = (String)    ele.get("title");
					String    txtContent   = (String)    ele.get("content");
					ParseFile image        = (ParseFile) ele.get("image");
					 
						
					Photo map = new Photo();
					map.setphoto(image.getUrl());
					 
					
					ele.put("isChecked", false);//initial setup to false!!!	
					ele.put("position", count++);//initial setup to  ordering number!!!
					ele.saveEventually();
					ele.saveInBackground();			
				 
					 ansPhotoChecked = new ArrayList<Boolean>();  
				        for(int i=0;i<sizelistQueryElem;i++){
				        	//ansPhotoChecked.clear();
				            //ansPhotoChecked.add(false);
				            //ansPhotoChecked.set(i, false);
				            ansPhotoChecked.add(booischecked);
				        
				        }// for(int i=0;i<sizelistQueryElem;i++) 
										 
					}//for (ParseObject ele : listQueryElem)
 
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return query;
			}
		});
		       
	}
	
	 
	@Override
	public View getItemView(final Elem elem, View v, ViewGroup parent) {

		if (v == null) {
			v = View.inflate(getContext(), R.layout.listview_item, null);
		}

		super.getItemView(elem, v, parent);

		isAnswer = new boolean[sizelistQueryElem];  
        ansPhotoChecked = new ArrayList<Boolean>(sizelistQueryElem);  
        for(int i=0;i<isAnswer.length;i++){
        	//ansPhotoChecked.clear();
            ansPhotoChecked.add(i,false);
        	isAnswer[i]=false;
            
        }// for(int i=0;i<isAnswer.length;i++) 
		
        appState.isAnswerApp=isAnswer;//initial isAnswer[i] and store to appState.isAnswerApp
        
        //--question
		ParseImageView pictureImage = (ParseImageView) v.findViewById(R.id.qusicon);
		ParseFile imageFile = elem.getParseFile("image");//image-------------------
		if (imageFile != null) {
			pictureImage.setParseFile(imageFile);//put imageFile to ParseImageView component.
			pictureImage.loadInBackground(new GetDataCallback() {
				@Override
				public void done(byte[] data, ParseException e) {
					// nothing to do
//					System.gc();
//					Runtime.getRuntime().gc();
				}
			});
		} 
		
		//--answer
		ParseImageView pictureAnsImage = (ParseImageView) v.findViewById(R.id.ansicon);
		ParseFile ansimageFile = elem.getParseFile("ansimage");//ansimage----------------
		if (ansimageFile != null) {
			pictureAnsImage.setParseFile(ansimageFile);//put imageFile to ParseImageView component.
			pictureAnsImage.loadInBackground(new GetDataCallback() {
				@Override
				public void done(byte[] data, ParseException e) {
					// nothing to do
//					System.gc();
//					Runtime.getRuntime().gc();
				}
			});
		}//if-ansimageFile
		
		//--describe
				ParseImageView pictureTxtImage = (ParseImageView) v.findViewById(R.id.txticon);
				ParseFile txtimageFile = elem.getParseFile("txtimage");//txtimage----------------
				if (txtimageFile != null) {
					pictureTxtImage.setParseFile(txtimageFile);//put imageFile to ParseImageView component.
					pictureTxtImage.loadInBackground(new GetDataCallback() {
						@Override
						public void done(byte[] data, ParseException e) {
							// nothing to do
//							System.gc();
//							Runtime.getRuntime().gc();
						}
					});
				}//if-ansimageFile
				
		 
		TextView titleTextView = (TextView) v.findViewById(R.id.titletext);
		titleTextView.setText(elem.getTitle());
		
		TextView classTextView = (TextView) v.findViewById(R.id.classtext);
		classTextView.setText(elem.getClas());
		
		TextView contentTextView = (TextView) v.findViewById(R.id.contenttext);
		contentTextView.setText(elem.getContent());
		
		CheckBox chkbx = (CheckBox) v.findViewById(R.id.checkbox1);
		 
		chkbx.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				
				//////////////////////////////////////////////
				ParseObject asem =ParseObject.create("Asem");
				 
				((Asem) asem).setImageFile((ParseFile) elem.getParseFile("image"));//image
				if(((ParseFile) elem.getParseFile("ansimage"))!=null)
				       ((Asem) asem).setAnsImageFile((ParseFile) elem.getParseFile("ansimage"));//ansimage
				if(((ParseFile) elem.getParseFile("txtimage"))!=null)
				       ((Asem) asem).setTxtImageFile((ParseFile) elem.getParseFile("txtimage"));//txtimage
				((Asem) asem).setContent((String) elem.get("content"));
				((Asem) asem).setPosition(268);
				asem.saveInBackground();
				try {
					asem.fetch();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/////////////////////////////////////////////
				
				isChecked = !isChecked0;
				elem.getObjectId();
				elem.setIsChecked(isChecked);
				 
				elem.saveInBackground();
				isAnswer[elem.getPosition()] = isChecked ;
				appState.isAnswerApp=isAnswer;
 
			    
               if( ansPhotoChecked!=null){ 
				      
				     ansPhotoChecked.set(elem.getPosition(), isChecked);
				      
               };
                             
			}});
		
		System.gc();
		return v;
	}

}
