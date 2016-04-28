package com.sam.kidmatch;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sam.utility.Utils;


/*
 * This fragment manages the data entry for a
 * new elem object. It lets the user input a 
 * elem name, give it a class, and take a 
 * photo. If there is already a photo associated
 * with this elem, it will be displayed in the 
 * preview at the bottom, which is a standalone
 * ParseImageView.
 */
public class AddElemlFragment extends Fragment {
	
	public final int SELECT_URIPHOTO=1;// requestCode for take uri picture
	public final int SELECT_CLOUDPHOTO=1001;// requestCode for take uri picture
	
	public int outID;
	 
	public String styleType = "square";
	public String borderCustomerType = "bc1";
	public String borderNum = "1";
	public String borderStyle = "bq";
	public String ansText;
	public String photolistObiectId=null;
	public String newphoto="New";
	
	public String picTitleCloud,picContentCloud,classSpinnerCloud;
	
	String mCurrentPhotoPath, imgFile;
	
	public boolean isBorder = true;
	public boolean isSquare = true;
	public boolean isUserDefine = false;
	public boolean isTextAddin = false;
	public boolean isCloud = false;
	public float rotateAngle = 90;
	
	private static KidMatchApp appState;
	Context context;
	private Resources res=null;
	Bitmap photobmResizeWH;
	Bitmap cs = null;

	private ImageButton photoButton,uriButton,cloudButton,outputButton;
	private Button questionButton,answerButton,textButton;
	private Button cancelButton;
	private TextView picTitle,picContent;
	private Spinner classSpinner,newphotoSpinner,photolistCustomerSpinner,styleSpinner,borderCustomerSpinner,borderSquareCustomerSpinner;
	private ParseFile imageFile;
	private ParseImageView picPreview;
	Fragment fragment;
	
	static int sizelistQueryElem;
	static List<Elem> listQueryElem,listAnsElem,listTxtElem;    //selected question photos in Elem class of parse.com
	public  ArrayList<SpinnerModel> BorderArr,BorderSquareArr;
    CustomSpinnerAdapter customSpinnerAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle SavedInstanceState) {
		fragment = this;
		context = this.getActivity();
		res = this.getResources();
		
		View v = inflater.inflate(R.layout.fragment_add_elem, parent, false);

		picTitle = ((EditText) v.findViewById(R.id.elem_name));		
		picContent = ((EditText) v.findViewById(R.id.elem_content));
		picTitle.setText(" ");
      	picContent.setText(" ");
      	Log.e("---cloud---","---cloud  onCreateViewe----"); 
		// setup image class----
		classSpinner = ((Spinner) v.findViewById(R.id.class_spinner));
		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter
				.createFromResource(getActivity(), R.array.class_array,
						android.R.layout.simple_spinner_dropdown_item);
		classSpinner.setAdapter(spinnerAdapter);
		
		// setup image newphoto------------------------------------------------------------------------
		newphotoSpinner = ((Spinner) v.findViewById(R.id.newphoto_spinner));
		ArrayAdapter<CharSequence> newPhotospinnerAdapter = ArrayAdapter
				.createFromResource(getActivity(), R.array.newphoto_array,
						android.R.layout.simple_spinner_dropdown_item);
		newphotoSpinner.setAdapter(newPhotospinnerAdapter);
		newphotoSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	          @Override
	          public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
	              // your code here
	        	  newphoto = newphotoSpinner.getSelectedItem().toString();//switch question/answer
	      		  Log.e("***newphoto***","***newphoto***"+newphoto);
	      		  
		      		if(newphoto.equals("New")){
		            	questionButton.setEnabled(true);
		            	answerButton.setEnabled(false);
		            	textButton.setEnabled(false);
		            	photolistCustomerSpinner.setEnabled(false);
		            	photolistCustomerSpinner.setVisibility(View.GONE);
		    		}else{
		    			questionButton.setEnabled(false);
		    			answerButton.setEnabled(true);
		            	textButton.setEnabled(true);
		            	photolistCustomerSpinner.setEnabled(true);
		            	photolistCustomerSpinner.setVisibility(View.VISIBLE);
		    		}
	      		  
	          }

	          @Override
	          public void onNothingSelected(AdapterView<?> parentView) {
	              // your code here	        	   
	          }

	    });//borderCustomerSpinner.setOnItemSelectedListener
		
		// setup image border style----------------------------------------------------------------
		styleSpinner = ((Spinner) v.findViewById(R.id.style_spinner));
		ArrayAdapter<CharSequence> stylespinnerAdapter = ArrayAdapter
				.createFromResource(getActivity(), R.array.style_array,
						android.R.layout.simple_spinner_dropdown_item);
		styleSpinner.setAdapter(stylespinnerAdapter);

		//setup border Circle CustomerSpinner for image border---------------------------------------
		borderCustomerSpinner = ((Spinner) v.findViewById(R.id.border_customerspinner));
		BorderArr = new ArrayList<SpinnerModel>();
        //int numBorderArr = BorderArr.size()+1; //number count from 1(not 0)
		for (int i = 1; i < 36; i++) {//35 border in drawable
			
			final SpinnerModel border = new SpinnerModel();
			    
			border.setBorderName("bc "+i);
			border.setImage("bc"+i);
			   			   
			/******** Take Model Object in ArrayList **********/
			   BorderArr.add(border);
		}//for (int i = 1; i < 36; i++)
		
		customSpinnerAdapter = new CustomSpinnerAdapter(context, R.layout.spinner_border, BorderArr,res);
		borderCustomerSpinner.setAdapter(customSpinnerAdapter);
		
		borderCustomerSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                // your code here
            	String bordername = BorderArr.get(position).getBorderName();            	 
            	if(bordername!=null) Log.e("---bordername--","--bordername--"+bordername);
            	//正則運算-----------------------
				String regEx="[^0-9]";//非0-9數字
				Pattern p = Pattern.compile(regEx);//非0-9數字的pattern
				Matcher m = p.matcher(bordername);//對字串borderType作pattern運算,後放入m
				borderNum = m.replaceAll("").trim();//m內非0-9數字的字元以空值取代並壓縮---> 1 to 94
				appState = (KidMatchApp) context.getApplicationContext(); 
				appState.borderNum=borderNum;
				//正則運算-----------------------
				String regEx1="[0-9]";//0-9數字
				Pattern p1 = Pattern.compile(regEx1);//0-9數字的pattern
				Matcher m1 = p1.matcher(bordername);//對數字borderStyle作pattern運算,後放入m1
				borderStyle = m1.replaceAll("").trim();//m1內0-9數字的字元以空值取代並壓縮---->bd or bs				
				appState = (KidMatchApp) context.getApplicationContext(); 
				appState.borderStyle=borderStyle;			 	
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

      });//borderCustomerSpinner.setOnItemSelectedListener
		
	  //setup border Square CustomerSpinner for image border---------------------------------------
				borderSquareCustomerSpinner = ((Spinner) v.findViewById(R.id.bordersquare_customerspinner));
				BorderSquareArr = new ArrayList<SpinnerModel>();
		        //int numBorderArr = BorderSquareArr.size()+1; //number count from 1(not 0)
				for (int i = 1; i < 36; i++) {//35 square border in drawable
					
					final SpinnerModel border = new SpinnerModel();
					    					  
						border.setBorderName("bq "+i);
						border.setImage("bq"+i);
					    
					/******** Take Model Object in ArrayList **********/
					   BorderSquareArr.add(border);
				}//for (int i = 1; i < 35; i++)
				
				customSpinnerAdapter = new CustomSpinnerAdapter(context, R.layout.spinner_border, BorderSquareArr,res);
				borderSquareCustomerSpinner.setAdapter(customSpinnerAdapter);
				
				borderSquareCustomerSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		            @Override
		            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
		                // your code here
		            	String bordername = BorderSquareArr.get(position).getBorderName();            	 
		            	if(bordername!=null) Log.e("---bordername--","--bordername--"+bordername);
		            	//正則運算-----------------------
						String regEx="[^0-9]";//非0-9數字
						Pattern p = Pattern.compile(regEx);//非0-9數字的pattern
						Matcher m = p.matcher(bordername);//對字串borderType作pattern運算,後放入m
						borderNum = m.replaceAll("").trim();//m內非0-9數字的字元以空值取代並壓縮---> 1 to 94
						appState = (KidMatchApp) context.getApplicationContext(); 
						appState.borderNum=borderNum;
						//正則運算-----------------------
						String regEx1="[0-9]";//0-9數字
						Pattern p1 = Pattern.compile(regEx1);//0-9數字的pattern
						Matcher m1 = p1.matcher(bordername);//對數字borderStyle作pattern運算,後放入m1
						borderStyle = m1.replaceAll("").trim();//m1內0-9數字的字元以空值取代並壓縮---->bd or bs				
						appState = (KidMatchApp) context.getApplicationContext(); 
						appState.borderStyle=borderStyle;			 	
		            }

		            @Override
		            public void onNothingSelected(AdapterView<?> parentView) {
		                // your code here
		            }

		      });//borderCustomerSpinner.setOnItemSelectedListener
				
	  
	  //setup photolist CustomerSpinner for image ListView--------------------------------------------
	  photolistCustomerSpinner = ((Spinner) v.findViewById(R.id.photolist_customerspinner));	   
	  ParseQueryAdapter.QueryFactory<Elem> factory =
              new ParseQueryAdapter.QueryFactory<Elem>() {
		              public ParseQuery create() {
				             ParseQuery<Elem> query = new ParseQuery<Elem>("Elem");
				             
				             appState = (KidMatchApp) context.getApplicationContext();
							 if(appState.value1==null){
								appState.value1="shap" ;//default class = face
							 }
							 
							 //String [] splitClass =  appState.value1.split(",");
							 String [] splitClass =  {"shap"};
							 query.whereContainedIn("class", Arrays.asList("shap"));
							  
							 try {
									listQueryElem = query.find();			
								 
							 } catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
							 }							 				             
				             return query;
		              }
              };//new ParseQueryAdapter.QueryFactory<Elem>()
                          
      ParseQueryAdapter<Elem> photolistadapter = new ParseQueryAdapter<Elem>(context, factory);
      photolistadapter.setTextKey("title");
      photolistadapter.setImageKey("image");      		
      photolistCustomerSpinner.setAdapter(photolistadapter);
      photolistCustomerSpinner.setVisibility(View.GONE);
    	 
      photolistCustomerSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
              // your code here
          	photolistObiectId = listQueryElem.get(position).getObjectId();            	 
          	if(photolistObiectId!=null) Log.e("---photolistObiectId--","--photolistObiectId--"+photolistObiectId);
          	
          	if(newphoto.equals("New")){
          		picTitle.setText("");
              	picContent.setText("");
    		}else{
    			picTitle.setText(listQueryElem.get(position).getTitle());
              	picContent.setText(listQueryElem.get(position).getContent());
              	listQueryElem.get(position).getClas();
    		}
          	 
          	 		 	
          }

          @Override
          public void onNothingSelected(AdapterView<?> parentView) {
              // your code here       	   
          }

    });//borderCustomerSpinner.setOnItemSelectedListener
		
	//----------------------------------------------------------------------------------------------
	photoButton = ((ImageButton) v.findViewById(R.id.photo_button));
	photoButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// for camera picture setting---xxxxx---
				appState = (KidMatchApp) context.getApplicationContext();
				if(appState.numPng !=null){
					appState.numPng++;
				}else{
					appState.numPng=0;
				}
				
				
				styleType = styleSpinner.getSelectedItem().toString();//get item string--for camera only
				if(styleType.equals("square")){
					isUserDefine = false;
					isTextAddin = false;
					isSquare = true;
					appState = (KidMatchApp) context.getApplicationContext();
					appState.isSquare=isSquare;
				}else if(styleType.equals("userDefine")){
					isUserDefine = true;
					isTextAddin = false;
					isSquare = false;
					appState = (KidMatchApp) context.getApplicationContext();
					appState.isSquare=isSquare;
				}else if(styleType.equals("textAddin")){
					isUserDefine = false;
					isTextAddin = true;
					isSquare = false;
					appState = (KidMatchApp) context.getApplicationContext();
					appState.isSquare=isSquare;
				}else{
					isUserDefine = false;
					isTextAddin = false;
					isSquare = false;
					appState = (KidMatchApp) context.getApplicationContext();
					appState.isSquare=isSquare;
				}		 		
				
				InputMethodManager imm = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(picTitle.getWindowToken(), 0);
				startCamera();
			}
	});
    //--------------------------------------------------------------
	uriButton = ((ImageButton) v.findViewById(R.id.uri_button));
	uriButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("-----cloud----","----cloud in addelement-----");
				picTitleCloud=picTitle.getText().toString();
				picContentCloud=picContent.getText().toString();
				classSpinnerCloud=classSpinner.getSelectedItem().toString();
				Intent intent = new Intent();
	            intent.setType("image/*");
	            intent.setAction(Intent.ACTION_GET_CONTENT);
	            startActivityForResult(Intent.createChooser(intent,"Select Picture"),SELECT_URIPHOTO);				
			}
	});
	//-----------------------------------------------------------------	
	cloudButton = ((ImageButton) v.findViewById(R.id.cloud_button));
	cloudButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//isCloud = true;
				Log.e("-----cloud----","----cloud in addelement-----");
				picTitleCloud=picTitle.getText().toString();
				picContentCloud=picContent.getText().toString();
				classSpinnerCloud=classSpinner.getSelectedItem().toString();
				Intent intent = new Intent(context,GridViewSettingActivity.class);
				startActivityForResult(intent,SELECT_CLOUDPHOTO); 			
			}
	});
	//----------------------------------------------------------------
	outputButton = ((ImageButton) v.findViewById(R.id.output_button));
	outputButton.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			appState = (KidMatchApp) context.getApplicationContext();
			picTitleCloud=picTitle.getText().toString();
			picContentCloud=picContent.getText().toString();
			classSpinnerCloud=classSpinner.getSelectedItem().toString();
			 
			Log.e("-----output----","----output in addelement-----");
			if(cs!=null){ 
					final FileOutputStream fos;
			    	File outfQus = MyFunc.getQusFile( picTitleCloud,classSpinnerCloud);//define fileName and location
			    	  
			    	try {
						    fos = new FileOutputStream(outfQus);
						    cs.compress(Bitmap.CompressFormat.PNG, 90, fos);
						    
					} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
					}
			}else if(appState.cs!=null){
				    Bitmap csphoto = appState.cs;
					final FileOutputStream fos;
			    	File outfQus = MyFunc.getQusFile( picTitleCloud,classSpinnerCloud);//define fileName and location
			    	  
			    	try {
						    fos = new FileOutputStream(outfQus);
						    csphoto.compress(Bitmap.CompressFormat.PNG, 90, fos);						    
						    appState.cs=null;
					} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
					}
				
			}else{
				Log.e("-----output----","----output error-----");
			}
			
			
		}//onClick(View v)
    });//outputButton.setOnClickListener
	//----------------------------------------------------------------
	questionButton = ((Button) v.findViewById(R.id.question_button));
	answerButton = ((Button) v.findViewById(R.id.answer_button));
	textButton = ((Button) v.findViewById(R.id.text_button));
		
		  				
	if(newphoto.equals("New")){
        	questionButton.setEnabled(true);
        	answerButton.setEnabled(false);
        	textButton.setEnabled(false);
        	photolistCustomerSpinner.setEnabled(false);
	}else{
			questionButton.setEnabled(false);
			answerButton.setEnabled(true);
        	textButton.setEnabled(true);
        	photolistCustomerSpinner.setEnabled(true);
	}
		
		
	questionButton.setOnClickListener(new View.OnClickListener() {//---getImageFile()

			@Override
			public void onClick(View v) {
				Elem elem = ((AddElemActivity) getActivity()).getCurrentElem();

				// When the user clicks "QuestionSave," upload the photo to Parse
				// Add data to the photo object:
				if(picTitle.getText().toString().equals("")){
					   elem.setTitle(picTitleCloud);
				}else{
				       elem.setTitle(picTitle.getText().toString());
				}
				if(picContent.getText().toString().equals("")){
					   elem.setTitle(picContentCloud);
				}else{
				       elem.setContent(picContent.getText().toString());
				}
				if(classSpinner.getSelectedItem().toString().equals("")){
					   elem.setTitle(classSpinnerCloud);
				}else{
				       elem.setClas(classSpinner.getSelectedItem().toString());
				}
				
				// Associate the elem with the current user
				//elem.setAuthor(ParseUser.getCurrentUser());

				// Add the class
				

				// If the user added a photo, that data will be
				// added in the CameraFragment
				ParseFile imageFile = ((AddElemActivity) getActivity())
						.getCurrentElem().getImageFile();
				if (imageFile != null) {
				 				
						// Save the photo and return--------
						elem.saveInBackground(new SaveCallback() {
		
							@Override
							public void done(ParseException e) {
								if (e == null) {
									getActivity().setResult(Activity.RESULT_OK);
									getActivity().finish();
								} else {
									Toast.makeText(
											getActivity().getApplicationContext(),
											"Error saving: " + e.getMessage(),
											Toast.LENGTH_SHORT).show();
								}
							}
		
						});//end of elem.saveInBackground(new SaveCallback()---
				

				}else{
						getActivity().finish();
				}
				
			}//onClick(View v)
	});//questionButton.setOnClickListener

		
	answerButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
	 		 
				Elem elem = ((AddElemActivity) getActivity()).getCurrentElem();
			    // When the user clicks "QuestionSave," upload the photo to Parse
				// Add data to the photo object:
				elem.setTitle(picTitle.getText().toString());				
				elem.setContent(picContent.getText().toString());
				// Associate the elem with the current user
				//elem.setAuthor(ParseUser.getCurrentUser());

				// Add the class
				elem.setClas(classSpinner.getSelectedItem().toString());
                
				// If the user added a photo, that data will be
				// added in the CameraFragment
				//ParseFile imageFile = ((AddElemActivity) getActivity()).getCurrentElem().getImageFile();				
				ParseFile imageFile = ((AddElemActivity) getActivity()).getCurrentElem().getAnsImageFile();
				
				if (imageFile != null) {
				 				
						// Save the photo and return--------
						elem.saveInBackground(new SaveCallback() {
		
							@Override
							public void done(ParseException e) {
								if (e == null) {
									getActivity().setResult(Activity.RESULT_OK);
									getActivity().finish();
								} else {
									Toast.makeText(
											getActivity().getApplicationContext(),
											"Error saving: " + e.getMessage(),
											Toast.LENGTH_SHORT).show();
								}
							}
		
						});//end of elem.saveInBackground(new SaveCallback()---
				
						 
						
						try {
							//fetchAll()
							elem.fetch();
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
				}else{
						getActivity().finish();
				}
				
				
				
			}//onClick(View v)
	});//answerButton.setOnClickListener
		
		
	textButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Elem elem = ((AddElemActivity) getActivity()).getCurrentElem();
				    // When the user clicks "QuestionSave," upload the photo to Parse
					// Add data to the photo object:
					elem.setTitle(picTitle.getText().toString());				
					elem.setContent(picContent.getText().toString());
					// Associate the elem with the current user
					//elem.setAuthor(ParseUser.getCurrentUser());

					// Add the class
					elem.setClas(classSpinner.getSelectedItem().toString());
	                
					// If the user added a photo, that data will be
					// added in the CameraFragment
					//ParseFile imageFile = ((AddElemActivity) getActivity()).getCurrentElem().getImageFile();				
					//ParseFile imageFile = ((AddElemActivity) getActivity()).getCurrentElem().getAnsImageFile();
					ParseFile imageFile = ((AddElemActivity) getActivity()).getCurrentElem().getTxtImageFile();
					if (imageFile != null) {
					 				
							// Save the photo and return--------
							elem.saveInBackground(new SaveCallback() {
			
								@Override
								public void done(ParseException e) {
									if (e == null) {
										getActivity().setResult(Activity.RESULT_OK);
										getActivity().finish();
									} else {
										Toast.makeText(
												getActivity().getApplicationContext(),
												"Error saving: " + e.getMessage(),
												Toast.LENGTH_SHORT).show();
									}
								}
			
							});//end of elem.saveInBackground(new SaveCallback()---
					
							 
							
							try {
								 
								elem.fetch();
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
					}else{
							getActivity().finish();
					}
				}//onClick(View v)
		});//answerButton.setOnClickListener
	
	
//	cancelButton.setOnClickListener(new View.OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			getActivity().setResult(Activity.RESULT_CANCELED);
//			getActivity().finish();
//		}//onClick(View v)
//	});//cancelButton.setOnClickListener   

		// Until the user has taken a photo, hide the preview
		picPreview = (ParseImageView) v.findViewById(R.id.elem_preview_image);
		picPreview.setVisibility(View.INVISIBLE);

		return v;
	}//onCreateView

	/*
	 * All data entry about a elem object is managed from the AddElemActivity.
	 * When the user wants to add a photo, we'll start up a custom
	 * CameraFragment that will let them take the photo and save it to the elem
	 * object owned by the AddElemActivity. Create a new CameraFragment, swap
	 * the contents of the fragmentContainer (see activity_add_elem.xml), then
	 * add the AddElemFragment to the back stack so we can return to it when the
	 * camera is finished.
	 */
	public void startCamera() {//相當於intent到CameraFragment,定義堆棧的順序於fragmentContainer
		Fragment cameraFragment = new CameraFragment();
		FragmentTransaction transaction = getActivity().getFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.fragmentContainer, cameraFragment);
		transaction.addToBackStack("AddElemFragment");
		transaction.commit();
	}

	/*
	 * On resume, check and see if a elem photo has been set from the
	 * CameraFragment. If it has, load the image in this fragment and make the
	 * preview image visible.
	 */
	@Override
	public void onResume() {
		super.onResume();
		// cloud---------
        Log.e("---cloud---","---cloud  onResume----");
      
		// For camera receive imageFile to picPreview display.
        // the iamageFile already save to Elem just take to show.
		ParseFile imageFile = ((AddElemActivity) getActivity())
				.getCurrentElem().getImageFile();
		if (imageFile != null) {
			picPreview.setParseFile(imageFile);
			picPreview.loadInBackground(new GetDataCallback() {
				@Override
				public void done(byte[] data, ParseException e) {
					picPreview.setVisibility(View.VISIBLE);
				}
			});
		} 
	}
	

	
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
		 if (cs != null){///////
			 cs.recycle();
			 cs = null;
  	     }/////////////////////
		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		 
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);				
		//---for pick up  photo from device-----				 
		if(resultCode==Activity.RESULT_OK){			 
			if(requestCode==SELECT_URIPHOTO){
				Log.e("---cloud---","---cloud  onResume onActivityResult URIPHOTO----");
				// for pickup file setting---xxxxx---
				appState = (KidMatchApp) context.getApplicationContext();
				if(appState.numPng !=null){
					appState.numPng++;
				}else{
					appState.numPng=0;
				}
				
				
				styleType = styleSpinner.getSelectedItem().toString();//get item string--for pickup file
				if(styleType.equals("square")){
					isUserDefine = false;
					isTextAddin = false;
					isSquare = true;
					appState = (KidMatchApp) context.getApplicationContext();
					appState.isSquare=isSquare;
				}else if(styleType.equals("userDefine")){
					isUserDefine = true;
					isTextAddin = false;
					isSquare = false;
					appState = (KidMatchApp) context.getApplicationContext();
					appState.isSquare=isSquare;
				}else if(styleType.equals("textAddin")){
					isUserDefine = false;
					isTextAddin = true;
					isSquare = false;
					appState = (KidMatchApp) context.getApplicationContext();
					appState.isSquare=isSquare;
				}else{
					isUserDefine = false;
					isTextAddin = false;
					isSquare = false;
					appState = (KidMatchApp) context.getApplicationContext();
					appState.isSquare=isSquare;
				}
				
	 		
				//get storage filepath uri-------------------------------------111111---xxx      filepath 
				Uri selectedImageUri = data.getData();
				 
				String filepath = Utils.getPath(context, selectedImageUri);
				
				//transfer to bimap and setup shrink option----file2bitma------222222---xxx      photobm
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inDither=false; //Disable Dithering mode
			    options.inPurgeable=true; //Tell to gc that whether it needs free memory, the Bitmap can be cleared
			    options.inInputShareable=true; //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
			    options.inTempStorage=new byte[32 * 1024]; 
			    options.inJustDecodeBounds = false;  
			    options.inSampleSize = 1;//***
				
        	    
        	    
        	    
        	    Bitmap photobm = BitmapFactory.decodeFile(filepath, options);//decodeFile(file2bitmap)
        	      	   
	            //fix bitmap size by 400 x 400 for pickup file----------------------333333---xxx    photobmResize	           
 
        	    if(photobm.getWidth()<photobm.getHeight()){
	        	    	photobmResizeWH = Bitmap.createScaledBitmap(photobm,
								                            				 400,
								   400* photobm.getHeight() / photobm.getWidth(), false);
        	    	
	        	    	rotateAngle=0f;
        	    }else{
	        	    	photobmResizeWH = Bitmap.createScaledBitmap(photobm,
	  	            		        400* photobm.getWidth() / photobm.getHeight(),
	  	            		                                                  400, false);
	        	    	rotateAngle=90f;
        	    }
        	    
        	    Bitmap photobmResize = 	photobmResizeWH; 
        	    
        	    if ( photobm != null){///////
       	    	 photobm.recycle();
       	    	 photobm = null;
       	        }///////////////////////////
        	    
	            //rotate bitmap to 90(landscape to portait)-------------------------44444---xxx  photobmResizeRotate
	            Matrix matrix = new Matrix();
	    		matrix.postRotate(rotateAngle);
	    		Bitmap photobmResizeRotate = Bitmap.createBitmap(photobmResize, 0,
	    				0, photobmResize.getWidth(), photobmResize.getHeight(),
	    				matrix, true);
	    		
	    		if ( photobmResize != null){/////
	    			 photobmResize.recycle();
	    			 photobmResize = null;
	       	    }///////////////////////////////
	    		
	    		if (photobmResizeWH != null){/////
	    			photobmResizeWH.recycle();
	    			photobmResizeWH = null;
	       	    }///////////////////////////////
	    		
	    		//resize border-----------------------------------------------------55555---xxx border2Resize/border3Resize
	    		 
	    		
	    		//get border from drawable by constant drawable-------------------------------0000---xxx   border1/border2/border3/border4	 
	    		//Bitmap border1 = BitmapFactory.decodeResource(res,bdid);//userDefine only for pickup file
	    		//Bitmap border2 = BitmapFactory.decodeResource(res,R.drawable.bk44);//bk44-square-border2
	    		//Bitmap border3 = BitmapFactory.decodeResource(res,R.drawable.bk9);//bk9-circle-border3
	    		//Bitmap border4 = BitmapFactory.decodeResource(res,bdid);//text
	    		
	    		//get border from drawable by spinner drawable-------------------------------0000---xxx   border2/border3
	    		
	    	if(isSquare){ //square rounded corner for picture---///1\\\
		    		  //square rounded corner----xxxxx
	    			  Bitmap border2 = BitmapFactory.decodeResource(res,R.drawable.aa44);//bk44-square-border2
	    			
		    		  int width = border2.getWidth();
		    		  int height = border2.getHeight();
		    		  // 設置想要的大小
		    		  int newWidth  = photobmResizeRotate.getWidth();
		    		  int newHeight = photobmResizeRotate.getHeight();
		    		  // 計算缩放比例
		    		  float scaleWidth  =(((float) newWidth) / width)*1.2f;
		    		  float scaleHeight =(((float) newHeight) / height)*1.2f;
		    		  matrix.postScale(scaleWidth, scaleHeight);
		    		  Bitmap border2Resize = Bitmap.createBitmap(border2, 0, 0, width, height, matrix,true);//bk44-square-border2
			    
			    	  //rouded corners image---------------------------------------6666---xxx111  roundCornerImage	    		 
			    	  Bitmap roundCornerImage = Bitmap.createBitmap(photobmResizeRotate.getWidth(), photobmResizeRotate.getHeight(), Bitmap.Config.ARGB_4444);
			    	  Canvas canvas = new Canvas(roundCornerImage);
			    	  int color = 0xff424242;
			    	  Paint paint = new Paint();
			    	  Rect rect = new Rect(0, 0, photobmResizeRotate.getWidth(), photobmResizeRotate.getHeight());
			    	  RectF rectF = new RectF(rect);
			    	  float roundPx = 20;//rounded angle
			    		
			    	  paint.setAntiAlias(true);
			    	  canvas.drawARGB(0, 0, 0, 0);
			    	  paint.setColor(color);
			    	  canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			    		
			    	  paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			    	  canvas.drawBitmap(photobmResizeRotate, rect, rect, paint);
		 	  
			    	  //combine squareBorder and rotatedScaledphotoImage to cs--------------------------777-----xxxxxx imageCombine
			  		  //for roundCornerImage pick file-xxx---roundCornerImage.getWidth()+100--b1.2(10,-5)   	   	    	   
			    	  cs = Bitmap.createBitmap(roundCornerImage.getWidth()+100, roundCornerImage.getHeight()+150, Bitmap.Config.ARGB_4444);//define canvas size
			    	  Canvas comboImage = new Canvas(cs);
			    		
			    	  comboImage.drawBitmap(border2Resize, 10f,5f, null);//b1.2/p+100/150(10,5)---bottom
			    	  comboImage.drawBitmap(roundCornerImage, 50f, 75f, null);//(50,75)---top  最上面(photo)加在上面
			    	  
			    	  if (photobmResizeRotate != null){//////
			    		  photobmResizeRotate.recycle();
			    		  photobmResizeRotate = null;
				  	    }//////////////////////////////////////////
						
						if (roundCornerImage != null){///////
							roundCornerImage.recycle();
							roundCornerImage = null;
				  	    }///////////////////////////////////
						
						if (border2Resize != null){///////
							border2Resize.recycle();
							border2Resize = null;
				  	    }/////////////////////////////////
						
						if (border2 != null){///////
							border2.recycle();
							border2 = null;
				  	    }/////////////////////////////////
			    	  
	    	 }else if(isUserDefine){//user define for drawing---///2\\\
	    		 
	    		      //user define----xxxxx
	    		      if(borderStyle.equals("bc")){/////cicle outline for drawing//////--///2-1\\\
	    		    	  
							  int bdid = getResources().getIdentifier("bc" + borderNum, "drawable", this.context.getPackageName());
							  Bitmap border1 = BitmapFactory.decodeResource(res,bdid);
							  //Config config = border1.getConfig();
							  /////for cicle outline//////
							  int width = border1.getWidth();
				 	    	  int height = width;		
				 	    	  // 設置想要的大小
		 			    	  int newWidth = (photobmResizeRotate.getWidth()>photobmResizeRotate.getHeight())?photobmResizeRotate.getWidth()+500
		 			    				                                                                     :photobmResizeRotate.getHeight()+250;
		 			    	  int newHeight =newWidth;		
		 			    	  // 計算缩放比例
		 			    	  if(photobmResizeRotate.getWidth()==photobmResizeRotate.getHeight()){
			 			    	  float scaleWidth  =( (float) ((float) newWidth ) / width)*1.3f;//1.3_photo正方 
			 			    	  float scaleHeight = scaleWidth;
			 			    	  matrix.postScale(scaleWidth, scaleHeight);
		 			    	  }else{
		 			    		  float scaleWidth  =( (float) ((float) newWidth ) / width)*1.0f;//1.2_photo長方
		 			    		  float scaleHeight = scaleWidth;
			 			    	  matrix.postScale(scaleWidth, scaleHeight);
		 			    	  }					  		    		  
				    		  Bitmap border1Resize = Bitmap.createBitmap(border1, 0, 0, width, height, matrix,true);//user define
					   	    		  
				    		  //combine squareBorder and rotatedScaledphotoImage to cs--------------------------777-----xxxxxx imageCombine		    		  
				    		  cs = Bitmap.createBitmap(photobmResizeRotate.getWidth()+500, photobmResizeRotate.getHeight()+250, Bitmap.Config.ARGB_4444);//define canvas size
					    	  Canvas comboImage = new Canvas(cs);
					    	  
					    	  if(photobmResizeRotate.getWidth()==photobmResizeRotate.getHeight()){
						    	  comboImage.drawBitmap(border1Resize, -10f,-10f, null);//b1.3/p+250(-10,-10)正方---bottom						    	  
						    	  comboImage.drawBitmap(photobmResizeRotate, 250f, 125f, null);//(125,125)---top  最上面(photo)加在上面
					    	  }else{					   		 
						    	  comboImage.drawBitmap(border1Resize, 10f,10f, null);//b1.2/p+250(0,0)長方---bottom
						    	  comboImage.drawBitmap(photobmResizeRotate, 250f, 125f, null);//(125,125)---top  最上面(photo)加在上面
					    	  }
					  
	    	         }else{ /////square outline for drawing/////---//2-2\\
	    	        	  
		    	        	  int bdid = getResources().getIdentifier("bq" + borderNum, "drawable", this.context.getPackageName());
							  Bitmap border1 = BitmapFactory.decodeResource(res,bdid);
							  /////for square outline/////
							  int width = border1.getWidth();
				    		  int height = border1.getHeight();
				    		  // 設置想要的大小
				    		  int newWidth  = photobmResizeRotate.getWidth();
				    		  int newHeight = photobmResizeRotate.getHeight();
				    		  // 計算缩放比例
				    		  float scaleWidth  =(((float) newWidth) / width)*1.2f;
				    		  float scaleHeight =(((float) newHeight) / height)*1.2f;
				    		  matrix.postScale(scaleWidth, scaleHeight);
							  Bitmap border1Resize = Bitmap.createBitmap(border1, 0, 0, width, height, matrix,true);//user define
							  
							  //combine squareBorder and rotatedScaledphotoImage to cs--------------------------777-----xxxxxx imageCombine
					    	  cs = Bitmap.createBitmap(photobmResizeRotate.getWidth()+100, photobmResizeRotate.getHeight()+150, Bitmap.Config.ARGB_4444);//define canvas size
					    	  Canvas comboImage = new Canvas(cs);
					    	  	
					    	  comboImage.drawBitmap(border1Resize, 10f,5f, null);//b1.2/p+100/150(10,5)---bottom---
					    	  comboImage.drawBitmap(photobmResizeRotate, 50f, 75f, null);//(50,75)-->p+100/150---top  最上面(photo)加在上面					    	  
	    	          }
	    	 	      
	    	}else if(isTextAddin){//user define for textAddin---///3\\\
	    		      //ansText = picContent.getText().toString();//google add "\" for every "\n" automatic 
	    		      ansText = picContent.getText().toString().replace("\\n","\n");//so,need replace "\\n" to "\n" 
	    		      
	    		      String[] lines = ansText.split("\n");
	    		      
	    		      
	    		      //float scale = res.getDisplayMetrics().density;
	    		      float scale = 5.0f;
	    		      
	    		      
	    		      // new antialised Paint---沒有鋸齒狀的畫筆
	  	              Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		    		  
		  	          
		  	          //paint.setColor(Color.rgb(110,110, 110));// Text Color
	  	              paint.setColor(Color.RED); // text color - #3D3D3D
		  	          //
		  	          paint.setTextSize((int) (12 * scale));//  text size in pixels
		  	          // text shadow
		  	          paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);
	  	              
		  	          paint.setStrokeWidth(12); // Text line width
		  	          
		  	          // draw text to the Canvas center
//		              Rect bounds = new Rect();
//		              int yoff = 0;
//		              paint.getTextBounds(ansText, 0, ansText.length(), bounds);
		              //int x = (photobmResizeRotate.getWidth() - bounds.width())/2;//6,2,10
		              //int y = (photobmResizeRotate.getHeight() + bounds.height())/2;//5,2,6
		              //canvas.drawText(mText, x * scale, y * scale, paint);
 	                  //paint.setStyle(Style.FILL);                
 
		              //Rect bounds = new Rect();
		              
//		                  String[] lines = str.split("\n");
//
//		                  int yoff = 0;
//		                  for (int i = 0; i < lines.length; ++i) {
//		                      canvas.drawText(lines[i], x, y + yoff, paint);
//		                      paint.getTextBounds(lines[i], 0, lines[i].length(), bounds);
//		                      yoff += bounds.height();
//		                  }
		               
		              
		              
		              
			    	  //Text Add in Border and convert to image----xxxxx
	    		      //use border which size decided photo and combine text bitmap 
		   		      if(borderStyle.equals("bc")){/////cicle outline border for text//////--//3-1\\
		   		    	  
								  int bdid = getResources().getIdentifier("bc" + borderNum, "drawable", this.context.getPackageName());
								  Bitmap border4 = BitmapFactory.decodeResource(res,bdid);	
								  
								  /////for cicle outline//////
								  int width = border4.getWidth();
					 	    	  int height = width;		
					 	    	  // 設置想要的大小
			 			    	  int newWidth = (photobmResizeRotate.getWidth()>photobmResizeRotate.getHeight())?photobmResizeRotate.getWidth()
			 			    				                                                                       :photobmResizeRotate.getHeight();
			 			    	  int newHeight =newWidth;		
			 			    	  // 計算缩放比例
			 			    	  if(photobmResizeRotate.getWidth()==photobmResizeRotate.getHeight()){
				 			    	  float scaleWidth  =(((float) newWidth ) / width)*1.3f;//1.3_正方  0.7_長方
				 			    	  float scaleHeight =(((float) newHeight) /height)*1.3f;
				 			    	  matrix.postScale(scaleWidth, scaleHeight);
			 			    	  }else{
			 			    		  float scaleWidth  =(((float) newWidth ) / width)*0.7f;//1.3_正方  0.7_長方
				 			    	  float scaleHeight =(((float) newHeight) /height)*0.7f;
				 			    	  matrix.postScale(scaleWidth, scaleHeight);
			 			    	  }					  		    		  
					    		  Bitmap border4Resize = Bitmap.createBitmap(border4, 0, 0, width, height, matrix,true);//user define
						   	     
					    		  
					    		  // draw text to the Canvas center
					              Rect bounds = new Rect();
					              int yoff = 0;
					               
					    		  //combine squareBorder and bmpText to cs--------------------------777-----xxxxxx imageCombine		    		  
					    		  cs = Bitmap.createBitmap(photobmResizeRotate.getWidth()+100, photobmResizeRotate.getHeight()+100, Bitmap.Config.ARGB_4444);//define canvas size
						    	  Canvas comboImage = new Canvas(cs);
						    	  
						    	  if(photobmResizeRotate.getWidth()==photobmResizeRotate.getHeight()){
							    	  comboImage.drawBitmap(border4Resize, -10f,-10f, null);//b1.3/p+100(-10,-10)正方---bottom		    	   
							    	  //comboImage.drawText(ansText, x * scale, y * scale, paint);
							    	  for (int i = 0; i < lines.length; ++i) {
							    		  paint.getTextBounds(lines[i], 0, lines[i].length(), bounds);
							    		  int x = (border4Resize.getWidth() - bounds.width())/12;//(2)10
							              int y = (border4Resize.getHeight() + bounds.height())/8;//(2)6
							    		  
								    	  comboImage.drawText(lines[i], x * scale, y * scale+yoff, paint);							    	  
								    	  yoff += bounds.height()*1.5;
							    	  }
						    	  }else{	   
							    	  comboImage.drawBitmap(border4Resize, -0f,155f, null);//b0.7/p+100(-10,-10)長方---bottom
							    	  //comboImage.drawText(ansText, x * scale, y * scale, paint);    	  
							    	  for (int i = 0; i < lines.length; ++i) {
							    		  paint.getTextBounds(lines[i], 0, lines[i].length(), bounds);
							    		  int x = (border4Resize.getWidth() - bounds.width())/12;//(2)10
							    		  int y = (border4Resize.getHeight() + bounds.height())/8;//(2)6
							    		   
								    	  comboImage.drawText(lines[i], x * scale, y * scale+yoff, paint);							    	  
								    	  yoff += bounds.height()*1.5;
							    	  }// for (int i = 0; i < lines.length; ++i)
							    	  
						    	  }
						  
		   	          }else{ /////square outline border for text/////--//3-2\\
		   	        	  
			    	        	  int bdid = getResources().getIdentifier("bq" + borderNum, "drawable", this.context.getPackageName());
								  Bitmap border4 = BitmapFactory.decodeResource(res,bdid);
								  
								  /////for square outline/////
								  int width = border4.getWidth();
					    		  int height = border4.getHeight();
					    		  // 設置想要的大小
					    		  int newWidth  = photobmResizeRotate.getWidth();
					    		  int newHeight = photobmResizeRotate.getHeight();
					    		  // 計算缩放比例
					    		  float scaleWidth  =(((float) newWidth) / width)*1.2f;
					    		  float scaleHeight =(((float) newHeight) / height)*1.2f;
					    		  matrix.postScale(scaleWidth, scaleHeight);
								  Bitmap border4Resize = Bitmap.createBitmap(border4, 0, 0, width, height, matrix,true);//user define
								  
								  // draw text to the Canvas center
					              Rect bounds = new Rect();
					              int yoff = 0;
					              paint.getTextBounds(ansText, 0, ansText.length(), bounds);
								  int x = (border4Resize.getWidth() - bounds.width())/10;//10
					              int y = (border4Resize.getHeight() + bounds.height())/10;//10
								  
								  //combine squareBorder and rotatedScaledphotoImage to cs--------------------------777-----xxxxxx imageCombine
						    	  cs = Bitmap.createBitmap(photobmResizeRotate.getWidth()+100, photobmResizeRotate.getHeight()+150, Bitmap.Config.ARGB_4444);//define canvas size
						    	  Canvas comboImage = new Canvas(cs);
						    	 
						    	  comboImage.drawBitmap(border4Resize, 10f,5f, null);//b1.2/(10,5)---bottom---
						    	  //comboImage.drawBitmap(bmpText, 50f, 50f, null);//p+100/150(50,75)---top  最上面(photo)加在上面	
						    	  comboImage.drawText(ansText, x * scale, y * scale, paint);
						    	  
		   	          }//end of //3-2\\
	    		      
	    		      
	    		       
    		}else{ //circle outline for picture---///4\\\ 
    			      //circle----xxxxx
    			      Bitmap border3 = BitmapFactory.decodeResource(res,R.drawable.aa9);//bk9-circle-border3
    			 
		 	          int width = border3.getWidth();
		 	    	  int height = width;		
		 	    	  // 設置想要的大小
 			    	  int newWidth = (photobmResizeRotate.getWidth()>photobmResizeRotate.getHeight())?photobmResizeRotate.getWidth()
 			    				                                                                       :photobmResizeRotate.getHeight();
 			    	  int newHeight =newWidth;		
 			    	  // 計算缩放比例
 			    	  if(photobmResizeRotate.getWidth()==photobmResizeRotate.getHeight()){
	 			    	  float scaleWidth  =(((float) newWidth ) / width)*1.3f;//1.3_正方  0.7_長方
	 			    	  float scaleHeight =(((float) newHeight) /height)*1.3f;
	 			    	  matrix.postScale(scaleWidth, scaleHeight);
 			    	  }else{
 			    		  float scaleWidth  =(((float) newWidth ) / width)*0.7f;//1.3_正方  0.7_長方
	 			    	  float scaleHeight =(((float) newHeight) /height)*0.7f;
	 			    	  matrix.postScale(scaleWidth, scaleHeight);
 			    	  }
 			    	  
 			    	  Bitmap border3Resize = Bitmap.createBitmap(border3, 0, 0, width, height, matrix,true);//bk9-circle-border3
 			    
    		 
		    		  //circle image------------------------------------------------6666---xxx222 circleImage
			    	  BitmapShader shader = new BitmapShader (photobmResizeRotate,  TileMode.CLAMP, TileMode.CLAMP);
			    	  Paint paint = new Paint();
			    	  paint.setShader(shader);
			    		
			    	  Bitmap circleImage = Bitmap.createBitmap(photobmResizeRotate.getWidth(), photobmResizeRotate.getHeight(), Bitmap.Config.ARGB_4444);
			    	  Canvas c = new Canvas(circleImage);
			    	  c.drawCircle(photobmResizeRotate.getWidth()/2, photobmResizeRotate.getHeight()/2, photobmResizeRotate.getWidth()/2, paint);
			    	  
			    	  //combine squareBorder and rotatedScaledphotoImage to cs--------------------------777-----xxxxxx imageCombine
			  		  //for roundCornerImage pick file-xxx---roundCornerImage.getWidth()+100--b1.2(10,-5)   	   	    	   
			    	  cs = Bitmap.createBitmap(circleImage.getWidth()+100, circleImage.getHeight()+100, Bitmap.Config.ARGB_4444);//define canvas size
			    	  Canvas comboImage = new Canvas(cs);
			    	  
			    	  if(photobmResizeRotate.getWidth()==photobmResizeRotate.getHeight()){
				    	  comboImage.drawBitmap(border3Resize, -10f,-10f, null);//b1.3/p+100(-10,-10)正方---bottom
				    	  //comboImage.drawBitmap(border3Resize, -0f,155f, null);//b1.3/p+100(-10,-10)長方---bottom
				    	  comboImage.drawBitmap(circleImage, 50f, 50f, null);//(50,50)---top  最上面(photo)加在上面
			    	  }else{
			    		//comboImage.drawBitmap(border3Resize, -10f,-10f, null);//b1.3/p+100(-10,-10)正方---bottom
				    	  comboImage.drawBitmap(border3Resize, -0f,155f, null);//b1.3/p+100(-10,-10)長方---bottom
				    	  comboImage.drawBitmap(circleImage, 50f, 50f, null);//(50,50)---top  最上面(photo)加在上面
			    	  }
			    	  
			    	  if (photobmResizeRotate != null){///////
			    		  photobmResizeRotate.recycle();
			    		  photobmResizeRotate = null;
				  	    }///////////////////////////////////////////
						
						if (circleImage != null){///////////
							circleImage.recycle();
							circleImage = null;
				  	    }///////////////////////////////////
						
						if (border3Resize != null){///////
							border3Resize.recycle();
							border3Resize = null;
				  	    }/////////////////////////////////
						
						if (border3 != null){///////
							border3.recycle();
							border3 = null;
				  	    }/////////////////////////////////
			    	  
 		 	    	  		    	   
    		  }// end of circle---//4\\
	    	//----////---transfer bytefosdata to SQLite/json Format--------xxxnBase64.DEFAULT		    		 
//            ByteArrayOutputStream bytefos = new ByteArrayOutputStream();
//            bm.compress(Bitmap.CompressFormat.PNG, 100, bytefos);
//            byte [] bytefosdata = bytefos.toByteArray();
            
            //transfer bytefosdata to SQLite Format---------sqlitedata------6 sqlitedata
//            String base64 = Base64.encodeToString( bytefosdata, Base64.DEFAULT);
            // 把base64變成byte------Transfer to SQLite by Byte
//            byte[] sqlitedata = Base64.decode(base64, Base64.DEFAULT);
	    	  //bitmap to PNG type's FileOutputStream --------------------818181---xxx   fos -> outputFileAutoSave 
//	    	  Log.e("-----output----","----output in addelement-----");
//			 
//				final FileOutputStream fos;
//		    	File outfQus = MyFunc.getQusFile( picTitleCloud,classSpinnerCloud);//define fileName and location
//		    	  
//		    	try {
//					    fos = new FileOutputStream(outfQus);
//					    cs.compress(Bitmap.CompressFormat.PNG, 90, fos);
//					    
//				} catch (FileNotFoundException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//				}
			  
	    		  	    	 	    	    	     	  		
	    	  //bitmap to PNG type's ByteArrayOutputStream --------------------8888---xxx   bos -> scaledData
	    	  ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    	  cs.compress(Bitmap.CompressFormat.PNG, 100, bos);//combine border
	    	  byte[] scaledData = bos.toByteArray();
	    		 	    		
	    	  //scaled image to Parse and setup name to pickup file photo(pcf)-----999---xxx    imageFile
	    	  imageFile = new ParseFile("pcf"+appState.numPng+".png", scaledData);
	    	  
	    	  
	    	  //save parseFile to image,ansimage,txtimage-----------------------101010---xxx setImageFile,setAnsImageFile,setTxtImageFile
	    	  // create or retrieve parseObject from parse.com
	    	  
	    	 
	    	  
//			    query.getInBackground(photolistObiectId, new GetCallback<Elem>() {
//
//					@Override
//					public void done(Elem arg0, ParseException arg1) {
//						// TODO Auto-generated method stub
//						listAnsElem = (List<Elem>) arg0;
//					}
//			    	});
			    
			    
	    	  if(newphoto.equals("New")){// for imageFile
  
			    	  imageFile.saveInBackground(new SaveCallback() {//save parseFile is not equal to save elem class to parse.com. it's just prepare to save it.
		
				    		  public void done(ParseException e) {
				    				if (e != null) {
				    					Toast.makeText(getActivity(),
				    							"Error saving: " + e.getMessage(),
				    							Toast.LENGTH_LONG).show();
				    				} else {
				    					 
				    					if (cs != null){///////
//				    						cs.recycle();
//				    						cs = null;
				    			  	    }/////////////////////
				    					
				    					if (imageFile != null) {
				    						((AddElemActivity) getActivity()).getCurrentElem().setImageFile(imageFile);//save to image colmun
					    					//((AddElemActivity) getActivity()).getCurrentElem().setAnsImageFile(imageFile);//save to ansimage colmun
					    					//((AddElemActivity) getActivity()).getCurrentElem().setTxtImageFile(imageFile);//save to txtimage colmun
					    					
				    						
				    						picPreview.setParseFile(imageFile);
				    						picPreview.loadInBackground(new GetDataCallback() {
				    							@Override
				    							public void done(byte[] data, ParseException e) {
				    								picPreview.setVisibility(View.VISIBLE);
				    							}
				    						});
				    					}
				    					
				    		 
				    				}
				    			}// done(ParseException e)
			    	   });//imageFile.saveInBackground
	    	  
	    	   }else if(newphoto.equals("answer")){// for ansImageFile
	    		   
		    		   imageFile.saveInBackground(new SaveCallback() {//save parseFile is not equal to save elem class to parse.com. it's just prepare to save it.
		    				
				    		  public void done(ParseException e) {
				    				if (e != null) {
				    					Toast.makeText(getActivity(),
				    							"Error saving: " + e.getMessage(),
				    							Toast.LENGTH_LONG).show();
				    				} else {
				    					 				    					 
				    					if (cs != null){///////
//				    						cs.recycle();
//				    						cs = null;
				    			  	    }///////////////////// 
				    					
				    					ParseQuery<Elem> query = new ParseQuery<Elem>("Elem");				 
				    					query.whereEqualTo("objectId",photolistObiectId);			    
				    					query.findInBackground(new FindCallback<Elem>() {
				    					        public void done(List<Elem> objects, ParseException e) {
				    					        				        	
				    					        	if (e == null) {
			    						            	if(objects!=null){
				    							        		//objects.get(0).getImageFile();
				    							        		objects.get(0).setAnsImageFile(imageFile);				    							        		
				    							        		objects.get(0).saveInBackground();
				    							        		objects.get(0).saveEventually();
			    							        	}
				    					            } else {
					    					            	Toast.makeText(
																	getActivity().getApplicationContext(),
																	"Error saving: " + e.getMessage(),
																	Toast.LENGTH_SHORT).show();
				    					            }
				    					            
				    					        }
				    					});
				    					
				    					 
				    					if (imageFile != null) {
				    						picPreview.setParseFile(imageFile);
				    						picPreview.loadInBackground(new GetDataCallback() {
				    							@Override
				    							public void done(byte[] data, ParseException e) {
				    								picPreview.setVisibility(View.VISIBLE);
				    							}
				    						});
				    					}
				    					
				    		 
				    				}
				    			}// done(ParseException e)
			    	   });//imageFile.saveInBackground
	    		       	    		   
	    	   }else{// for txtImageFile
	    		       
	    		   imageFile.saveInBackground(new SaveCallback() {//save parseFile is not equal to save elem class to parse.com. it's just prepare to save it.
	    				
			    		  public void done(ParseException e) {
			    				if (e != null) {
			    					Toast.makeText(getActivity(),
			    							"Error saving: " + e.getMessage(),
			    							Toast.LENGTH_LONG).show();
			    				} else {
			    					
			    					if (cs != null){///////
//			    						cs.recycle();
//			    						cs = null;
			    			  	    }/////////////////////
			    					 				    					 
			    					 ParseQuery<Elem> query = new ParseQuery<Elem>("Elem");				 
			    					 query.whereEqualTo("objectId",photolistObiectId);			    
			    					 query.findInBackground(new FindCallback<Elem>() {
			    					        public void done(List<Elem> objects, ParseException e) {
			    					        				        	
			    					            if (e == null) {
			    						            	if(objects!=null){
				    							        		//objects.get(0).getImageFile();
				    							        		objects.get(0).setTxtImageFile(imageFile);
				    							        		objects.get(0).saveInBackground();
				    							        		objects.get(0).saveEventually();
			    							        	}
			    					            } else {
				    					            	Toast.makeText(
																getActivity().getApplicationContext(),
																"Error saving: " + e.getMessage(),
																Toast.LENGTH_SHORT).show();
			    					            }
			    					            
			    					        }
			    					 });
			    					
			    					
			    					if (imageFile != null) {
			    						picPreview.setParseFile(imageFile);
			    						picPreview.loadInBackground(new GetDataCallback() {
			    							@Override
			    							public void done(byte[] data, ParseException e) {
			    								picPreview.setVisibility(View.VISIBLE);
			    							}
			    						});
			    					}
			    					
			    		 
			    				}
			    			}// done(ParseException e)
		    	   });//imageFile.saveInBackground  
	    		        
	    		       
	    		   
	    	   }// if(newphoto.equals("New"))
	    	  	    	  				 
		    }//requestCode==SELECT_URIPHOTO
			
			////////////////////////////////////////////////////////////////////////////////////////////////
			if(requestCode==SELECT_CLOUDPHOTO){
			  
				    // for pickup file setting---xxxxx---
					appState = (KidMatchApp) context.getApplicationContext();
					if(appState.numPng !=null){
						appState.numPng++;
					}else{
						appState.numPng=0;
					}
										
					styleType = styleSpinner.getSelectedItem().toString();//get item string--for pickup file
					if(styleType.equals("square")){
						isUserDefine = false;
						isTextAddin = false;
						isSquare = true;
						appState = (KidMatchApp) context.getApplicationContext();
						appState.isSquare=isSquare;
					}else if(styleType.equals("userDefine")){
						isUserDefine = true;
						isTextAddin = false;
						isSquare = false;
						appState = (KidMatchApp) context.getApplicationContext();
						appState.isSquare=isSquare;
					}else if(styleType.equals("textAddin")){
						isUserDefine = false;
						isTextAddin = true;
						isSquare = false;
						appState = (KidMatchApp) context.getApplicationContext();
						appState.isSquare=isSquare;
					}else{
						isUserDefine = false;
						isTextAddin = false;
						isSquare = false;
						appState = (KidMatchApp) context.getApplicationContext();
						appState.isSquare=isSquare;
					}							 		
					  
					//get data from intent-----------------------------------------111111---xxx    databytearrey   
					 byte[] databytearrey = data.getExtras().getByteArray("parseFile2ByteArray");					 
		
					//transfer to bimap and setup shrink option----file2bitma------222222---xxx     photobm
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inDither=false; //Disable Dithering mode
				    options.inPurgeable=true; //Tell to gc that whether it needs free memory, the Bitmap can be cleared
				    options.inInputShareable=true; //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
				    options.inTempStorage=new byte[32 * 1024]; 
				    options.inJustDecodeBounds = false;  
				    options.inSampleSize = 1;//***
						        	     
	        	    Bitmap photobm = BitmapFactory.decodeByteArray(databytearrey,0, databytearrey.length,options); 
					 if(photobm!=null)
					 Log.e("---cloud---","---cloud  onResume onActivityResult CLOUDPHOTO*****3----"
                            +data.getExtras().getString("cloudObiectId"));
					 
					//fix bitmap size by 400 x 400 for pickup file------------------333333---xxx    photobmResize	           
					 
			        	    if(photobm.getWidth()<photobm.getHeight()){
				        	    	photobmResizeWH = Bitmap.createScaledBitmap(photobm,
											                            				 400,
											   400* photobm.getHeight() / photobm.getWidth(), false);
			        	    	
				        	    	rotateAngle=0f;
			        	    }else{
				        	    	photobmResizeWH = Bitmap.createScaledBitmap(photobm,
				  	            		        400* photobm.getWidth() / photobm.getHeight(),
				  	            		                                                  400, false);
				        	    	rotateAngle=90f;
			        	    }
			        	    
			        	    Bitmap photobmResize = 	photobmResizeWH;	
			        	    
			        	    if ( photobm != null){///////
			          	    	 photobm.recycle();
			          	    	 photobm = null;
			          	    }///////////////////////////
		        	 
	        	    //rotate bitmap to 90(landscape to portait)----------------------44444---xxx  photobmResizeRotate
		            Matrix matrix = new Matrix();
		    		matrix.postRotate(rotateAngle);
		    		Bitmap photobmResizeRotate = Bitmap.createBitmap(photobmResize, 0,
		    				0, photobmResize.getWidth(), photobmResize.getHeight(),
		    				matrix, true);	
		    		
		    		if ( photobmResize != null){/////
		    			 photobmResize.recycle();
		    			 photobmResize = null;
		       	    }///////////////////////////////
		    		
		    		if (photobmResizeWH != null){/////
		    			photobmResizeWH.recycle();
		    			photobmResizeWH = null;
		       	    }///////////////////////////////
		    		
		    		//resize border--------------------------------------------------55555---xxx border2Resize/border3Resize
		    		if(isSquare){ //square rounded corner for picture---///1\\\
			    		  //square rounded corner----xxxxx
		    			  Bitmap border2 = BitmapFactory.decodeResource(res,R.drawable.aa44);//bk44-square-border2
		    			
			    		  int width = border2.getWidth();
			    		  int height = border2.getHeight();
			    		  // 設置想要的大小
			    		  int newWidth  = photobmResizeRotate.getWidth();
			    		  int newHeight = photobmResizeRotate.getHeight();
			    		  // 計算缩放比例
			    		  float scaleWidth  =(((float) newWidth) / width)*1.2f;
			    		  float scaleHeight =(((float) newHeight) / height)*1.2f;
			    		  matrix.postScale(scaleWidth, scaleHeight);
			    		  Bitmap border2Resize = Bitmap.createBitmap(border2, 0, 0, width, height, matrix,true);//bk44-square-border2
				    
				    	  //rouded corners image---------------------------------------6666---xxx111  roundCornerImage	    		 
				    	  Bitmap roundCornerImage = Bitmap.createBitmap(photobmResizeRotate.getWidth(), photobmResizeRotate.getHeight(), Bitmap.Config.ARGB_4444);
				    	  Canvas canvas = new Canvas(roundCornerImage);
				    	  int color = 0xff424242;
				    	  Paint paint = new Paint();
				    	  Rect rect = new Rect(0, 0, photobmResizeRotate.getWidth(), photobmResizeRotate.getHeight());
				    	  RectF rectF = new RectF(rect);
				    	  float roundPx = 20;//rounded angle
				    		
				    	  paint.setAntiAlias(true);
				    	  canvas.drawARGB(0, 0, 0, 0);
				    	  paint.setColor(color);
				    	  canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
				    		
				    	  paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
				    	  canvas.drawBitmap(photobmResizeRotate, rect, rect, paint);
			 	  
				    	  //combine squareBorder and rotatedScaledphotoImage to cs--------------------------777-----xxxxxx imageCombine
				  		  //for roundCornerImage pick file-xxx---roundCornerImage.getWidth()+100--b1.2(10,-5)   	   	    	   
				    	  cs = Bitmap.createBitmap(roundCornerImage.getWidth()+100, roundCornerImage.getHeight()+150, Bitmap.Config.ARGB_4444);//define canvas size
				    	  Canvas comboImage = new Canvas(cs);
				    		
				    	  comboImage.drawBitmap(border2Resize, 10f,5f, null);//b1.2/p+100/150(10,5)---bottom
				    	  comboImage.drawBitmap(roundCornerImage, 50f, 75f, null);//(50,75)---top  最上面(photo)加在上面
				    	  
				    	  if (photobmResizeRotate != null){//////
				    		  photobmResizeRotate.recycle();
				    		  photobmResizeRotate = null;
					  	  }//////////////////////////////////////////
							
						  if (roundCornerImage != null){///////
								roundCornerImage.recycle();
								roundCornerImage = null;
					  	  }///////////////////////////////////
							
						  if (border2Resize != null){///////
								border2Resize.recycle();
								border2Resize = null;
					  	  }/////////////////////////////////
							
						  if (border2 != null){///////
								border2.recycle();
								border2 = null;
					  	  }/////////////////////////////////
				    	  
		    	 }else if(isUserDefine){//user define for drawing---///2\\\
		    		 
		    		      //user define----xxxxx
		    		      if(borderStyle.equals("bc")){/////cicle outline for drawing//////--///2-1\\\
		    		    	  
								  int bdid = getResources().getIdentifier("bc" + borderNum, "drawable", this.context.getPackageName());
								  Bitmap border1 = BitmapFactory.decodeResource(res,bdid);
								  //Config config = border1.getConfig();
								  /////for cicle outline//////
								  int width = border1.getWidth();
					 	    	  int height = width;		
					 	    	  // 設置想要的大小
			 			    	  int newWidth = (photobmResizeRotate.getWidth()>photobmResizeRotate.getHeight())?photobmResizeRotate.getWidth()+500
			 			    				                                                                     :photobmResizeRotate.getHeight()+250;
			 			    	  int newHeight =newWidth;		
			 			    	  // 計算缩放比例
			 			    	  if(photobmResizeRotate.getWidth()==photobmResizeRotate.getHeight()){
				 			    	  float scaleWidth  =( (float) ((float) newWidth ) / width)*1.3f;//1.3_photo正方 
				 			    	  float scaleHeight = scaleWidth;
				 			    	  matrix.postScale(scaleWidth, scaleHeight);
			 			    	  }else{
			 			    		  float scaleWidth  =( (float) ((float) newWidth ) / width)*1.0f;//1.2_photo長方
			 			    		  float scaleHeight = scaleWidth;
				 			    	  matrix.postScale(scaleWidth, scaleHeight);
			 			    	  }					  		    		  
					    		  Bitmap border1Resize = Bitmap.createBitmap(border1, 0, 0, width, height, matrix,true);//user define
						   	    		  
					    		  //combine squareBorder and rotatedScaledphotoImage to cs--------------------------777-----xxxxxx imageCombine		    		  
					    		  cs = Bitmap.createBitmap(photobmResizeRotate.getWidth()+500, photobmResizeRotate.getHeight()+250, Bitmap.Config.ARGB_4444);//define canvas size
						    	  Canvas comboImage = new Canvas(cs);
						    	  
						    	  if(photobmResizeRotate.getWidth()==photobmResizeRotate.getHeight()){
							    	  comboImage.drawBitmap(border1Resize, -10f,-10f, null);//b1.3/p+250(-10,-10)正方---bottom						    	  
							    	  comboImage.drawBitmap(photobmResizeRotate, 250f, 125f, null);//(125,125)---top  最上面(photo)加在上面
						    	  }else{					   		 
							    	  comboImage.drawBitmap(border1Resize, 10f,10f, null);//b1.2/p+250(0,0)長方---bottom
							    	  comboImage.drawBitmap(photobmResizeRotate, 250f, 125f, null);//(125,125)---top  最上面(photo)加在上面
						    	  }
						  
		    	         }else{ /////square outline for drawing/////---//2-2\\
		    	        	  
			    	        	  int bdid = getResources().getIdentifier("bq" + borderNum, "drawable", this.context.getPackageName());
								  Bitmap border1 = BitmapFactory.decodeResource(res,bdid);
								  /////for square outline/////
								  int width = border1.getWidth();
					    		  int height = border1.getHeight();
					    		  // 設置想要的大小
					    		  int newWidth  = photobmResizeRotate.getWidth();
					    		  int newHeight = photobmResizeRotate.getHeight();
					    		  // 計算缩放比例
					    		  float scaleWidth  =(((float) newWidth) / width)*1.2f;
					    		  float scaleHeight =(((float) newHeight) / height)*1.2f;
					    		  matrix.postScale(scaleWidth, scaleHeight);
								  Bitmap border1Resize = Bitmap.createBitmap(border1, 0, 0, width, height, matrix,true);//user define
								  
								  //combine squareBorder and rotatedScaledphotoImage to cs--------------------------777-----xxxxxx imageCombine
						    	  cs = Bitmap.createBitmap(photobmResizeRotate.getWidth()+100, photobmResizeRotate.getHeight()+150, Bitmap.Config.ARGB_4444);//define canvas size
						    	  Canvas comboImage = new Canvas(cs);
						    	  	
						    	  comboImage.drawBitmap(border1Resize, 10f,5f, null);//b1.2/p+100/150(10,5)---bottom---
						    	  comboImage.drawBitmap(photobmResizeRotate, 50f, 75f, null);//(50,75)-->p+100/150---top  最上面(photo)加在上面					    	  
		    	          }
		    	 	      
		    	}else if(isTextAddin){//user define for textAddin---///3\\\
		    		      //ansText = picContent.getText().toString();//google add "\" for every "\n" automatic 
		    		      ansText = picContent.getText().toString().replace("\\n","\n");//so,need replace "\\n" to "\n" 
		    		      
		    		      String[] lines = ansText.split("\n");		    		      
		    		      
		    		      //float scale = res.getDisplayMetrics().density;
		    		      float scale = 5.0f;
		    		      		    		      
		    		      // new antialised Paint---沒有鋸齒狀的畫筆
		  	              Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);			    		  			  	          
			  	          //paint.setColor(Color.rgb(110,110, 110));// Text Color
		  	              paint.setColor(Color.RED); // text color - #3D3D3D			  	          
			  	          paint.setTextSize((int) (12 * scale));//  text size in pixels
			  	          // text shadow
			  	          paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);		  	              
			  	          paint.setStrokeWidth(12); // Text line width			              
				    	  //Text Add in Border and convert to image----xxxxx
		    		      //use border which size decided photo and combine text bitmap 
			   		      if(borderStyle.equals("bc")){/////cicle outline border for text//////--//3-1\\
			   		    	  
									  int bdid = getResources().getIdentifier("bc" + borderNum, "drawable", this.context.getPackageName());
									  Bitmap border4 = BitmapFactory.decodeResource(res,bdid);	
									  
									  /////for cicle outline//////
									  int width = border4.getWidth();
						 	    	  int height = width;		
						 	    	  // 設置想要的大小
				 			    	  int newWidth = (photobmResizeRotate.getWidth()>photobmResizeRotate.getHeight())?photobmResizeRotate.getWidth()
				 			    				                                                                       :photobmResizeRotate.getHeight();
				 			    	  int newHeight =newWidth;		
				 			    	  // 計算缩放比例
				 			    	  if(photobmResizeRotate.getWidth()==photobmResizeRotate.getHeight()){
					 			    	  float scaleWidth  =(((float) newWidth ) / width)*1.3f;//1.3_正方  0.7_長方
					 			    	  float scaleHeight =(((float) newHeight) /height)*1.3f;
					 			    	  matrix.postScale(scaleWidth, scaleHeight);
				 			    	  }else{
				 			    		  float scaleWidth  =(((float) newWidth ) / width)*0.7f;//1.3_正方  0.7_長方
					 			    	  float scaleHeight =(((float) newHeight) /height)*0.7f;
					 			    	  matrix.postScale(scaleWidth, scaleHeight);
				 			    	  }					  		    		  
						    		  Bitmap border4Resize = Bitmap.createBitmap(border4, 0, 0, width, height, matrix,true);//user define
							   	     						    		  
						    		  // draw text to the Canvas center
						              Rect bounds = new Rect();
						              int yoff = 0;
						               
						    		  //combine squareBorder and bmpText to cs--------------------------777-----xxxxxx imageCombine		    		  
						    		  cs = Bitmap.createBitmap(photobmResizeRotate.getWidth()+100, photobmResizeRotate.getHeight()+100, Bitmap.Config.ARGB_4444);//define canvas size
							    	  Canvas comboImage = new Canvas(cs);
							    	  
							    	  if(photobmResizeRotate.getWidth()==photobmResizeRotate.getHeight()){
								    	  comboImage.drawBitmap(border4Resize, -10f,-10f, null);//b1.3/p+100(-10,-10)正方---bottom		    	   
								    	  //comboImage.drawText(ansText, x * scale, y * scale, paint);
								    	  for (int i = 0; i < lines.length; ++i) {
								    		  paint.getTextBounds(lines[i], 0, lines[i].length(), bounds);
								    		  int x = (border4Resize.getWidth() - bounds.width())/12;//(2)10
								              int y = (border4Resize.getHeight() + bounds.height())/8;//(2)6								    		  
									    	  comboImage.drawText(lines[i], x * scale, y * scale+yoff, paint);							    	  
									    	  yoff += bounds.height()*1.5;
								    	  }
							    	  }else{	   
								    	  comboImage.drawBitmap(border4Resize, -0f,155f, null);//b0.7/p+100(-10,-10)長方---bottom
								    	  //comboImage.drawText(ansText, x * scale, y * scale, paint);    	  
								    	  for (int i = 0; i < lines.length; ++i) {
								    		  paint.getTextBounds(lines[i], 0, lines[i].length(), bounds);
								    		  int x = (border4Resize.getWidth() - bounds.width())/12;//(2)10
								    		  int y = (border4Resize.getHeight() + bounds.height())/8;//(2)6								    		   
									    	  comboImage.drawText(lines[i], x * scale, y * scale+yoff, paint);							    	  
									    	  yoff += bounds.height()*1.5;
								    	  }// for (int i = 0; i < lines.length; ++i)
								    	  
							    	  }
							  
			   	          }else{ /////square outline border for text/////--//3-2\\
			   	        	  
				    	        	  int bdid = getResources().getIdentifier("bq" + borderNum, "drawable", this.context.getPackageName());
									  Bitmap border4 = BitmapFactory.decodeResource(res,bdid);
									  
									  /////for square outline/////
									  int width = border4.getWidth();
						    		  int height = border4.getHeight();
						    		  // 設置想要的大小
						    		  int newWidth  = photobmResizeRotate.getWidth();
						    		  int newHeight = photobmResizeRotate.getHeight();
						    		  // 計算缩放比例
						    		  float scaleWidth  =(((float) newWidth) / width)*1.2f;
						    		  float scaleHeight =(((float) newHeight) / height)*1.2f;
						    		  matrix.postScale(scaleWidth, scaleHeight);
									  Bitmap border4Resize = Bitmap.createBitmap(border4, 0, 0, width, height, matrix,true);//user define
									  
									  // draw text to the Canvas center
						              Rect bounds = new Rect();
						              int yoff = 0;
						              paint.getTextBounds(ansText, 0, ansText.length(), bounds);
									  int x = (border4Resize.getWidth() - bounds.width())/10;//10
						              int y = (border4Resize.getHeight() + bounds.height())/10;//10
									  
									  //combine squareBorder and rotatedScaledphotoImage to cs--------------------------777-----xxxxxx imageCombine
							    	  cs = Bitmap.createBitmap(photobmResizeRotate.getWidth()+100, photobmResizeRotate.getHeight()+150, Bitmap.Config.ARGB_4444);//define canvas size
							    	  Canvas comboImage = new Canvas(cs);
							    	 
							    	  comboImage.drawBitmap(border4Resize, 10f,5f, null);//b1.2/(10,5)---bottom---
							    	  //comboImage.drawBitmap(bmpText, 50f, 50f, null);//p+100/150(50,75)---top  最上面(photo)加在上面	
							    	  comboImage.drawText(ansText, x * scale, y * scale, paint);
							    	  
			   	          }//end of //3-2\\
		    		      
		    		      
		    		       
	    		}else{ //circle outline for picture---///4\\\ 
	    			      //circle----xxxxx
	    			      Bitmap border3 = BitmapFactory.decodeResource(res,R.drawable.aa9);//bk9-circle-border3
	    			 
			 	          int width = border3.getWidth();
			 	    	  int height = width;		
			 	    	  // 設置想要的大小
	 			    	  int newWidth = (photobmResizeRotate.getWidth()>photobmResizeRotate.getHeight())?photobmResizeRotate.getWidth()
	 			    				                                                                       :photobmResizeRotate.getHeight();
	 			    	  int newHeight =newWidth;		
	 			    	  // 計算缩放比例
	 			    	  if(photobmResizeRotate.getWidth()==photobmResizeRotate.getHeight()){
		 			    	  float scaleWidth  =(((float) newWidth ) / width)*1.3f;//1.3_正方  0.7_長方
		 			    	  float scaleHeight =(((float) newHeight) /height)*1.3f;
		 			    	  matrix.postScale(scaleWidth, scaleHeight);
	 			    	  }else{
	 			    		  float scaleWidth  =(((float) newWidth ) / width)*0.7f;//1.3_正方  0.7_長方
		 			    	  float scaleHeight =(((float) newHeight) /height)*0.7f;
		 			    	  matrix.postScale(scaleWidth, scaleHeight);
	 			    	  }
	 			    	  
	 			    	  Bitmap border3Resize = Bitmap.createBitmap(border3, 0, 0, width, height, matrix,true);//bk9-circle-border3	 			    
	    		 
			    		  //circle image------------------------------------------------6666---xxx222 circleImage
				    	  BitmapShader shader = new BitmapShader (photobmResizeRotate,  TileMode.CLAMP, TileMode.CLAMP);
				    	  Paint paint = new Paint();
				    	  paint.setShader(shader);
				    		
				    	  Bitmap circleImage = Bitmap.createBitmap(photobmResizeRotate.getWidth(), photobmResizeRotate.getHeight(), Bitmap.Config.ARGB_4444);
				    	  Canvas c = new Canvas(circleImage);
				    	  c.drawCircle(photobmResizeRotate.getWidth()/2, photobmResizeRotate.getHeight()/2, photobmResizeRotate.getWidth()/2, paint);
				    	  
				    	  //combine squareBorder and rotatedScaledphotoImage to cs--------------------------777-----xxxxxx imageCombine
				  		  //for roundCornerImage pick file-xxx---roundCornerImage.getWidth()+100--b1.2(10,-5)   	   	    	   
				    	  cs = Bitmap.createBitmap(circleImage.getWidth()+100, circleImage.getHeight()+100, Bitmap.Config.ARGB_4444);//define canvas size
				    	  Canvas comboImage = new Canvas(cs);
				    	  
				    	  if(photobmResizeRotate.getWidth()==photobmResizeRotate.getHeight()){
					    	  comboImage.drawBitmap(border3Resize, -10f,-10f, null);//b1.3/p+100(-10,-10)正方---bottom
					    	  //comboImage.drawBitmap(border3Resize, -0f,155f, null);//b1.3/p+100(-10,-10)長方---bottom
					    	  comboImage.drawBitmap(circleImage, 50f, 50f, null);//(50,50)---top  最上面(photo)加在上面
				    	  }else{
				    		//comboImage.drawBitmap(border3Resize, -10f,-10f, null);//b1.3/p+100(-10,-10)正方---bottom
					    	  comboImage.drawBitmap(border3Resize, -0f,155f, null);//b1.3/p+100(-10,-10)長方---bottom
					    	  comboImage.drawBitmap(circleImage, 50f, 50f, null);//(50,50)---top  最上面(photo)加在上面
				    	  }
				    	  
				    	  if (photobmResizeRotate != null){///////
				    		  photobmResizeRotate.recycle();
				    		  photobmResizeRotate = null;
					  	    }///////////////////////////////////////////
							
							if (circleImage != null){///////////
								circleImage.recycle();
								circleImage = null;
					  	    }///////////////////////////////////
							
							if (border3Resize != null){///////
								border3Resize.recycle();
								border3Resize = null;
					  	    }/////////////////////////////////
							
							if (border3 != null){///////
								border3.recycle();
								border3 = null;
					  	    }/////////////////////////////////
	 			 	    	  		    	   
	    		  }// end of circle---//4\\
		    	  //bitmap to PNG type's FileOutputStream --------------------818181---xxx   fos -> outputFileAutoSave 
//			    	  FileOutputStream fos;
//			    	  File outfQus = MyFunc.getQusFile(picTitleCloud,classSpinnerCloud);//define fileName and location
//			    	  
//			    	  try {
//						    fos = new FileOutputStream(outfQus);
//						    
//						    cs.compress(Bitmap.CompressFormat.PNG, 90, fos);
//						    
//					  } catch (FileNotFoundException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//					  }
		    		
		    	  //bitmap to PNG type's ByteArrayOutputStream --------------------8888---xxx   bos -> scaledData
		    	  ByteArrayOutputStream bos = new ByteArrayOutputStream();
		    	  cs.compress(Bitmap.CompressFormat.PNG, 100, bos);//combine border
		    	  byte[] scaledData = bos.toByteArray();
		    		 	    		
		    	  //scaled image to Parse and setup name to pickup file photo(pcf)-----999---xxx    imageFile
		    	  imageFile = new ParseFile("pcf"+appState.numPng+".png", scaledData);
		    	  
		    	  
		    	  //save parseFile to image,ansimage,txtimage-----------------------101010---xxx setImageFile,setAnsImageFile,setTxtImageFile
		     			    
		    	  if(newphoto.equals("New")){// for imageFile
	  
				    	  imageFile.saveInBackground(new SaveCallback() {//save parseFile is not equal to save elem class to parse.com. it's just prepare to save it.
			
					    		  public void done(ParseException e) {
					    				if (e != null) {
					    					Toast.makeText(getActivity(),
					    							"Error saving: " + e.getMessage(),
					    							Toast.LENGTH_LONG).show();
					    				} else {
					    					
					    					if (cs != null){///////
//					    						cs.recycle();
//					    						cs = null;
					    			  	    }/////////////////////
					    					 
					    					((AddElemActivity) getActivity()).getCurrentElem().setImageFile(imageFile);//save to image colmun
					    					//((AddElemActivity) getActivity()).getCurrentElem().setAnsImageFile(imageFile);//save to ansimage colmun
					    					//((AddElemActivity) getActivity()).getCurrentElem().setTxtImageFile(imageFile);//save to txtimage colmun
					    					
					    					if (imageFile != null) {
					    						picPreview.setParseFile(imageFile);
					    						picPreview.loadInBackground(new GetDataCallback() {
					    							@Override
					    							public void done(byte[] data, ParseException e) {
					    								picPreview.setVisibility(View.VISIBLE);
					    							}
					    						});
					    					}
					    					
					    		 
					    				}
					    			}// done(ParseException e)
				    	   });//imageFile.saveInBackground
		    	  
		    	   }else if(newphoto.equals("answer")){// for ansImageFile
		    		   
			    		   imageFile.saveInBackground(new SaveCallback() {//save parseFile is not equal to save elem class to parse.com. it's just prepare to save it.
			    				
					    		  public void done(ParseException e) {
					    				if (e != null) {
					    					Toast.makeText(getActivity(),
					    							"Error saving: " + e.getMessage(),
					    							Toast.LENGTH_LONG).show();
					    				} else {
					    					
					    					if (cs != null){///////
//					    						cs.recycle();
//					    						cs = null;
					    			  	    }/////////////////////
					    					 				    					 
					    					 ParseQuery<Elem> query = new ParseQuery<Elem>("Elem");				 
					    					 query.whereEqualTo("objectId",photolistObiectId);			    
					    					 query.findInBackground(new FindCallback<Elem>() {
					    					        public void done(List<Elem> objects, ParseException e) {
					    					        				        	
					    					        	if (e == null) {
				    						            	if(objects!=null){
					    							        		//objects.get(0).getImageFile();
					    							        		objects.get(0).setAnsImageFile(imageFile);				    							        		
					    							        		objects.get(0).saveInBackground();
					    							        		objects.get(0).saveEventually();
				    							        	}
					    					            } else {
						    					            	Toast.makeText(
																		getActivity().getApplicationContext(),
																		"Error saving: " + e.getMessage(),
																		Toast.LENGTH_SHORT).show();
					    					            }
					    					            
					    					        }
					    					 });
					    					
					    					 
					    					if (imageFile != null) {
					    						picPreview.setParseFile(imageFile);
					    						picPreview.loadInBackground(new GetDataCallback() {
					    							@Override
					    							public void done(byte[] data, ParseException e) {
					    								picPreview.setVisibility(View.VISIBLE);
					    							}
					    						});
					    					}
					    					
					    		 
					    				}
					    			}// done(ParseException e)
				    	   });//imageFile.saveInBackground
		    		       	    		   
		    	   }else{// for txtImageFile
		    		       
		    		   imageFile.saveInBackground(new SaveCallback() {//save parseFile is not equal to save elem class to parse.com. it's just prepare to save it.
		    				
				    		  public void done(ParseException e) {
				    				if (e != null) {
				    					Toast.makeText(getActivity(),
				    							"Error saving: " + e.getMessage(),
				    							Toast.LENGTH_LONG).show();
				    				} else {
				    					
				    					 if (cs != null){///////
//				    						 cs.recycle();
//				    						 cs = null;
				    			  	     }/////////////////////
				    					 				    					 
				    					 ParseQuery<Elem> query = new ParseQuery<Elem>("Elem");				 
				    					 query.whereEqualTo("objectId",photolistObiectId);			    
				    					 query.findInBackground(new FindCallback<Elem>() {
				    					        public void done(List<Elem> objects, ParseException e) {
				    					        				        	
				    					            if (e == null) {
				    						            	if(objects!=null){
					    							        		//objects.get(0).getImageFile();
					    							        		objects.get(0).setTxtImageFile(imageFile);
					    							        		objects.get(0).saveInBackground();
					    							        		objects.get(0).saveEventually();
				    							        	}
				    					            } else {
					    					            	Toast.makeText(
																	getActivity().getApplicationContext(),
																	"Error saving: " + e.getMessage(),
																	Toast.LENGTH_SHORT).show();
				    					            }
				    					            
				    					        }
				    					 });
				    					
				    					
				    					if (imageFile != null) {
				    						picPreview.setParseFile(imageFile);
				    						picPreview.loadInBackground(new GetDataCallback() {
				    							@Override
				    							public void done(byte[] data, ParseException e) {
				    								picPreview.setVisibility(View.VISIBLE);
				    							}
				    						});
				    					}
				    					
				    		 
				    				}
				    			}// done(ParseException e)
			    	   });//imageFile.saveInBackground  
		    		        
		    		       
		    		   
		    	   }// if(newphoto.equals("New"))	 
				 
				 
				 
		  }//requestCode==SELECT_CLOUDPHOTO
			
		
	   }//resultCode==Activity.RESULT_OK
		
    }//onActivityResult
	 
	public static Drawable getAssetImage(Context context, String filename) throws IOException {
	    AssetManager assets = context.getResources().getAssets();
	    InputStream buffer = new BufferedInputStream((assets.open("drawable/" + filename + ".png")));
	    Bitmap bitmap = BitmapFactory.decodeStream(buffer);
	    return new BitmapDrawable(context.getResources(), bitmap);
	}
	
	
 
	
}//AddElemlFragment
