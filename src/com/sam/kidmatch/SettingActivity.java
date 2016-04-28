package com.sam.kidmatch;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class SettingActivity extends ListActivity {

	private ParseQueryAdapter<Elem> mainAdapter;
	private ElemListAdapter elemlistAdapter;
	private View customerViewSearch;
	protected AlertDialog dialogSearch;
	private static KidMatchApp appState;
	Context context;
	
	boolean chkSelect[];
	protected ParseObject parseObject;
	private ParseFile imageFile, ansimageFile, txtimageFile;
	private ParseImageView qusPreview,ansPreview,txtPreview;
	
	List<Photo> elemGVarraylist ;
	List<Elem> ob;
	ProgressDialog mProgressDialog;
	
	String str="Photo Class is face";
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getListView().setClickable(true);//instead of setContentView()
		 
		context = this.getApplicationContext(); 

		mainAdapter = new ParseQueryAdapter<Elem>(this, "Elem");
		mainAdapter.setTextKey("title");
		mainAdapter.setImageKey("image");
 
		// Subclass of ParseQueryAdapter
		elemlistAdapter = new ElemListAdapter(this);
		
		
		//客製化視窗「搜尋」，產生。xxxxxxx不可用 context 必須用  SettingActivity.this 否則當掉
        LayoutInflater factory = LayoutInflater.from(SettingActivity.this);
        customerViewSearch = factory.inflate(R.layout.dialog_search,null);
        dialogSearch = new AlertDialog.Builder(SettingActivity.this).create();
        qusPreview = (ParseImageView) customerViewSearch.findViewById(R.id.elem_qus_image);
        ansPreview = (ParseImageView) customerViewSearch.findViewById(R.id.elem_ans_image);
        txtPreview = (ParseImageView) customerViewSearch.findViewById(R.id.elem_txt_image);
        dialogSearch.setView(customerViewSearch);

		// Default view is all elems
		setListAdapter(mainAdapter);
  	
	}// onCreate
		 
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		  
		//-----------setOnItemLongClickListener--------------Methoe findInBackground-----------
		getListView().setOnItemLongClickListener(new OnItemLongClickListener(){
			 
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				
				// TODO Auto-generated method stub
				parent.requestFocusFromTouch();
				parent.setSelection(position);
				
				parent.setSelected(true);				
				ParseQuery<Elem> query = new ParseQuery<Elem>("Elem");				
				Elem elem = (Elem) parent.getItemAtPosition(position); 	 		 
				query.whereEqualTo("objectId",elem.getObjectId());				 
				//---begin---query.findInBackground
				query.findInBackground(new FindCallback<Elem>(){

					@Override
					public void done(final List<Elem> objects,ParseException e) {							
						// TODO Auto-generated method stub
						String firstItemTitle = objects.get(0).getTitle();
						 
						////////////////////////////////////////////////////////////////////
						
						if(objects!=null){
							//String firstItemTitle = objects.get(0).getTitle();							
							  
							imageFile =(ParseFile) objects.get(0).getImageFile();
							ansimageFile =(ParseFile) objects.get(0).getAnsImageFile();
							txtimageFile =(ParseFile) objects.get(0).getTxtImageFile();
		        	    }
	 				
						if (imageFile != null) {
    						qusPreview.setParseFile(imageFile);
    						qusPreview.loadInBackground(new GetDataCallback() {
    							@Override
    							public void done(byte[] data, ParseException e) {
    								//qusPreview.setVisibility(View.VISIBLE);
    							}
    						});
    					}//if (imageFile != null)------------------------------------------
						if (ansimageFile != null) {
    						ansPreview.setParseFile(ansimageFile);
    						ansPreview.loadInBackground(new GetDataCallback() {
    							@Override
    							public void done(byte[] data, ParseException e) {
    								//qusPreview.setVisibility(View.VISIBLE);
    							}
    						});
    					}//if (ansimageFile != null)---------------------------------------
						if (txtimageFile != null) {
    						txtPreview.setParseFile(txtimageFile);
    						txtPreview.loadInBackground(new GetDataCallback() {
    							@Override
    							public void done(byte[] data, ParseException e) {
    								//qusPreview.setVisibility(View.VISIBLE);
    							}
    						});
    					}//if (imageFile != null)-------------------------------------------
						
						 
						((Button)customerViewSearch.findViewById(R.id.Button_delete)).setOnClickListener(
								new OnClickListener(){
									@Override
									public void onClick(View v) {
										
									////--------------------------------------------------------
									DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener(){											 
										public void onClick(DialogInterface dialog, int which) {															
											if(objects.size()!=0)
												 Log.e("TAG","sizzzze"+objects.size());     //Showing 1								 								 
											objects.get(0).deleteInBackground(new DeleteCallback(){ //ok-2/2							      			 
										    @Override
											public void done(ParseException e) {
												// TODO Auto-generated method stub										
												 if(e==null){					                       					                                  
							                          Toast.makeText(getBaseContext(),"Deleted Successfully!", Toast.LENGTH_LONG).show();  					                                                         
							                          refreshPhoto();				                          
							                     }else{
							                          Toast.makeText(getBaseContext(),"Cant Delete Tickle!"+e.toString(), 10).show(); 					                                  
							                     }										
											}});
										}				
									};
									
									Builder MyAlertDialog = new AlertDialog.Builder(SettingActivity.this);
									MyAlertDialog.setTitle("Delete Dialog" );
									MyAlertDialog.setMessage("The title of delete item is "+ objects.get(0).getTitle()+"\n"+" Are your sure to delete?");
									MyAlertDialog.setPositiveButton("Delete", OkClick);				
									MyAlertDialog.setNegativeButton("Cancel",null);
									MyAlertDialog.show();
			 						////----------------------------------------------------	
										
										
										
									//結束dialog
									dialogSearch.dismiss();
										 
									}
								}
							);
						     													
							//設定使用倒回鍵會結束dialog
							dialogSearch.setOnKeyListener(new OnKeyListener(){
								@Override
								public boolean onKey(DialogInterface dialog, int keyCode,
										KeyEvent event) {
									if(KeyEvent.KEYCODE_BACK==keyCode)
										dialogSearch.dismiss();
									return false;
								}
							});
							
							//顯示dialog
							dialogSearch.show();
																					
						///////////////////////////////////////////////////////////////////////////////////////////	
						
						////--------------------------------------------------------
//						DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener(){											 
//							public void onClick(DialogInterface dialog, int which) {															
//								if(objects.size()!=0)
//									 Log.e("TAG","sizzzze"+objects.size());     //Showing 1								 								 
//								objects.get(0).deleteInBackground(new DeleteCallback(){ //ok-2/2							      			 
//							    @Override
//								public void done(ParseException e) {
//									// TODO Auto-generated method stub										
//									 if(e==null){					                       					                                  
//				                          Toast.makeText(getBaseContext(),"Deleted Successfully!", Toast.LENGTH_LONG).show();  					                                                         
//				                          refreshPhoto();				                          
//				                     }else{
//				                          Toast.makeText(getBaseContext(),"Cant Delete Tickle!"+e.toString(), 10).show(); 					                                  
//				                     }										
//								}});
//							}				
//						};
//						
//						Builder MyAlertDialog = new AlertDialog.Builder(SettingActivity.this);
//						MyAlertDialog.setTitle("Delete Dialog" );
//						MyAlertDialog.setMessage("The title of delete item is "+firstItemTitle+"\n"+" Are your sure to delete?");
//						MyAlertDialog.setPositiveButton("Delete", OkClick);				
//						MyAlertDialog.setNegativeButton("Cancel",null);
//						MyAlertDialog.show();
 						////----------------------------------------------------
			 
					}});//end of query.findInBackground
				 
				return true;
			}});//------------setOnItemLongClickListener--------------------------------------
			
	}//end of onResume()



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	/*
	 * Posting meals and refreshing the list will be controlled from the Action
	 * Bar.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 switch (item.getItemId()) {
         case R.id.action_refresh:
        	refreshPhoto();
 			break;
         case R.id.action_setup_ques:
              
        	setupQues();
 			break;
         case R.id.action_add:
              
        	addPhoto();
 			break;
          
         case R.id.action_setup_ans:
              
        	setupAns(); 
 			break;
 			
         }
         return false;
	}

	private void refreshPhoto() {
		mainAdapter.loadObjects();
		setListAdapter(mainAdapter);
		
	}

	private void setupQues() {
			
		//-------------------------------------------------------------------
		final CharSequence[] dilogList = { "face", "math", "lang", "shap", "clor" };
		
		AlertDialog.Builder multChoiceDialog = new AlertDialog.Builder(this);
		//set title for Alert box
		multChoiceDialog.setTitle("Class Select Dialog");
		boolean[] _selections = new boolean[dilogList.length];
		multChoiceDialog.setMultiChoiceItems(dilogList, _selections, 				                      
				    new DialogInterface.OnMultiChoiceClickListener() {
			
		              public void onClick(DialogInterface dialog,
		                          int whichButton, boolean isChecked) {
		                          }
		});

		// add positive button here-------------------------------
		multChoiceDialog.setPositiveButton("OK",new DialogInterface.OnClickListener() {
			
			 @Override
			 public void onClick(DialogInterface dialog, int which) {
				 // getting listview from alert box
				 ListView list = ((AlertDialog) dialog).getListView();
				 StringBuilder sb = new StringBuilder();
				 
				 for (int i = 0; i < list.getCount(); i++) {
					      boolean checked = list.isItemChecked(i);
					      // get checked list value
						  if (checked) {
								    if (sb.length() > 0){
								             sb.append(",");
								    }
								    sb.append(list.getItemAtPosition(i));					  
						  }
				 }
				 
				 if(sb.toString()!=""){
					 	
						 Toast.makeText(getApplicationContext(), 
						 sb.toString(),Toast.LENGTH_LONG).show();
						 
						 appState = (KidMatchApp) context.getApplicationContext();
						 appState.value1=sb.toString();
						  
						 quesLib();//*********************
				 }else{
						 appState = (KidMatchApp) context.getApplicationContext();
						 appState.value1="face,math,lang,shap,clor";
						 mainAdapter.loadObjects();
						 setListAdapter(mainAdapter);
						 Toast.makeText(getApplicationContext(),
							      "Please choise at least one class!", 
							      Toast.LENGTH_LONG).show();					
				 }				 
			 }//onClick(DialogInterface dialog, int which)
		});
		
		// add negative button------------------
		multChoiceDialog.setNegativeButton("Cancel",
		new DialogInterface.OnClickListener() {
				 @Override
				 public void onClick(DialogInterface dialog, int which) {
				 // cancel code here
				 }
		});
				
		AlertDialog alert1 = multChoiceDialog.create();
		alert1.show();

	}//setupQues()

	private void addPhoto() {
		Intent i = new Intent(this, AddElemActivity.class);
		startActivityForResult(i, 0);
	}
	
	 
	
	private void quesLib(){//////SetupQuesActivity.class//////////////
		Intent i = new Intent(this, SetupQuesActivity.class);
		startActivityForResult(i, 0);
	   	 Toast.makeText(getApplicationContext(), 
					 "setup Material",5).show();
	}
	
	private void setupAns(){
		Intent i = new Intent(this, SetupAsemActivity.class);
		startActivityForResult(i, 0);
	   	 Toast.makeText(getApplicationContext(), 
					 "setup answer",5).show();
	}
	
 
//-------ParseRelation relation------------------
	
//	private void updatePostList() {
//		// Create query for objects of type "Post"
//		ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
//
//		// Restrict to cases where the author is the current user.
//		// Note that you should pass in a ParseUser and not the
//		// String reperesentation of that user
//		query.whereEqualTo("author", ParseUser.getCurrentUser());
//		// Run the query
//		query.findInBackground(new FindCallback<ParseObject>() {
//
//			@Override
//			public void done(List<ParseObject> postList, ParseException e) {
//				if (e == null) {
//					// If there are results, update the list of posts
//					// and notify the adapter
//					posts.clear();
//					for (ParseObject post : postList) {
//						posts.add(post.getString("textContent"));
//					}
//					((ArrayAdapter<String>) getListAdapter())
//							.notifyDataSetChanged();
//				} else {
//					Log.d("Post retrieval", "Error: " + e.getMessage());
//				}
//
//			}
//
//		});
//
//	}
	
	//---------------------------------------    

	
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			// If a new post has been added, update
			// the list of posts
			refreshPhoto();
			
		}
	}

}
