package com.sam.kidmatch;

import java.io.ByteArrayOutputStream;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.SaveCallback;
import com.sam.utility.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SetupAnsActivity extends Activity {
	
	public final int SELECT_URIPHOTO=1;// requestCode for take ans picture
	
	static KidMatchApp appState;
	private Elem elem;
	private Asem asem;
	Context context;
	private Resources res=null;
	
	Bitmap quesbmResizeWH;
	Bitmap cs = null;

	private ImageButton qusSelectButton,ansSelectButton;
	private Button qusSaveButton,ansSaveButton;
	private TextView qusTitle,ansContent;
	private Spinner qusSpinner,ansSpinner;
	private ParseFile qusImageFile,ansImageFile;
	private ParseImageView qusPreview,ansPreview;
	
	private void findviews(){
		
		qusTitle = ((EditText) findViewById(R.id.elem_questiontitle));		
		ansContent = ((EditText) findViewById(R.id.elem_answercontent));
		
		// setup question----
		qusSpinner = ((Spinner) findViewById(R.id.question_spinner));
		ArrayAdapter<CharSequence> qusSpinnerAdapter = ArrayAdapter
				.createFromResource(context, R.array.question_array,
						android.R.layout.simple_spinner_dropdown_item);
		qusSpinner.setAdapter(qusSpinnerAdapter);
		
		// setup answer----
		ansSpinner = ((Spinner) findViewById(R.id.answer_spinner));
		ArrayAdapter<CharSequence> ansSpinnerAdapter = ArrayAdapter
				.createFromResource(context, R.array.answer_array,
						android.R.layout.simple_spinner_dropdown_item);
		ansSpinner.setAdapter(ansSpinnerAdapter);
				
//		//setup image border----
//		borderSpinner = ((Spinner) v.findViewById(R.id.border_spinner));
//		ArrayAdapter<CharSequence> borderspinnerAdapter = ArrayAdapter
//				.createFromResource(getActivity(), R.array.border_array,
//						android.R.layout.simple_spinner_dropdown_item);
//		borderSpinner.setAdapter(borderspinnerAdapter);
//				
//		//setup border updown-----------------------------------------------
//		updownSpinner = ((Spinner) v.findViewById(R.id.border_updownspinner));
//		ArrayAdapter<CharSequence> updownspinnerAdapter = ArrayAdapter
//				.createFromResource(getActivity(), R.array.updown_array,
//						android.R.layout.simple_spinner_dropdown_item);
//		updownSpinner.setAdapter(updownspinnerAdapter);
//		
//		//setup image border view list---------------------------------------
//		borderCustomerSpinner = ((Spinner) v.findViewById(R.id.border_customerspinner));
//		BorderArr = new ArrayList<SpinnerModel>();
//        
//		for (int i = 1; i < 31; i++) {//30 border in drawable
//			
//			final SpinnerModel border = new SpinnerModel();
//			    
//			  /******* Firstly take data in model object ******/
//			border.setBorderName("bd "+i);
//			border.setImage("bd"+i);
//			   			   
//			/******** Take Model Object in ArrayList **********/
//			   BorderArr.add(border);
//		}//for (int i = 1; i < 31; i++)
//		
//		customSpinnerAdapter = new CustomSpinnerAdapter(context, R.layout.spinner_border, BorderArr,res);
//		borderCustomerSpinner.setAdapter(customSpinnerAdapter);
//		
//		borderCustomerSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
//                // your code here
//            	String bordername    = ((TextView) v.findViewById(R.id.bordr_name)).getText().toString();
//            	if(bordername!=null) Log.e("---bordername--","--bordername--"+bordername);
//            	//正則運算-------------------------------
//				String regEx="[^0-9]";//非0-9數字
//				Pattern p = Pattern.compile(regEx);//非0-9數字的pattern
//				Matcher m = p.matcher(bordername);//對字串borderType作pattern運算,後放入m
//				borderNum = m.replaceAll("").trim();//m內非0-9數字的字元以空值取代並壓縮
//				appState = (KidMatchApp) context.getApplicationContext(); 
//				appState.borderNum=borderNum;
//			 	
//            }		 
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }		 
//      });
		//-------------------------------------------------------------------
		
		qusSelectButton = ((ImageButton) findViewById(R.id.selectquestion_button));
		qusSelectButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// for camera picture setting---xxxxx---
//				appState = (KidMatchApp) context.getApplicationContext();
//				if(appState.numPng !=null){
//					appState.numPng++;
//				}else{
//					appState.numPng=0;
//				}
				
				
//				styleType = styleSpinner.getSelectedItem().toString();//get item string
//				if(styleType.equals("square")){
//					isSquare = true;
//					appState = (KidMatchApp) context.getApplicationContext();
//					appState.isSquare=isSquare;
//				}else{
//					isSquare = false;
//					appState = (KidMatchApp) context.getApplicationContext();
//					appState.isSquare=isSquare;
//				}
		 		
//				borderType = borderSpinner.getSelectedItem().toString();//get item string
//				String [] splitBorder = borderType.split(" ");//抓出 border 或  solid
				
//				borderUpDown = updownSpinner.getSelectedItem().toString();
//				//正則運算-------------------------------
//				String regEx="[^0-9]";//非0-9數字
//				Pattern p = Pattern.compile(regEx);//非0-9數字的pattern
//				Matcher m = p.matcher(borderType);//對字串borderType作pattern運算,後放入m
//				borderNum = m.replaceAll("").trim();//m內非0-9數字的字元以空值取代並壓縮
//				appState = (KidMatchApp) context.getApplicationContext(); 
//				appState.borderNum=borderNum;
				
//				if(borderUpDown.equals("border top")){
//					isBorder = true;
//					appState.isBorder=true;
//				}else{
//					isBorder = false;
//					appState.isBorder=false;
//				}
			 
				 
			}
		});//qusSelectButton.setOnClickListener
		
		ansSelectButton = ((ImageButton) findViewById(R.id.selectanswer_button));
		ansSelectButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub								
//				Intent intent = new Intent();
//	            intent.setType("image/*");
//	            intent.setAction(Intent.ACTION_GET_CONTENT);
//	            startActivityForResult(Intent.createChooser(intent,"Select Picture"),SELECT_URIPHOTO);				
			}
		});//ansSelectButton.setOnClickListener
	  	
		qusSaveButton = ((Button) findViewById(R.id.savequestion_button));
		qusSaveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
//				Elem elem = ((AddElemActivity) getActivity()).getCurrentElem();
//
//				// When the user clicks "Save," upload the elem to Parse
//				// Add data to the elem object:
//				elem.setTitle(picTitle.getText().toString());				
//				elem.setContent(picContent.getText().toString());
//				// Associate the elem with the current user
//				//elem.setAuthor(ParseUser.getCurrentUser());
//
//				// Add the class
//				elem.setClas(classSpinner.getSelectedItem().toString());
//
//				// If the user added a photo, that data will be
//				// added in the CameraFragment
//				ParseFile imageFile = ((AddElemActivity) getActivity())
//						.getCurrentElem().getImageFile();
//				if (imageFile != null) {
//				 				
//						// Save the photo and return--------
//						elem.saveInBackground(new SaveCallback() {
//		
//							@Override
//							public void done(ParseException e) {
//								if (e == null) {
//									getActivity().setResult(Activity.RESULT_OK);
//									getActivity().finish();
//								} else {
//									Toast.makeText(
//											getActivity().getApplicationContext(),
//											"Error saving: " + e.getMessage(),
//											Toast.LENGTH_SHORT).show();
//								}
//							}
//		
//						});//end of elem.saveInBackground(new SaveCallback()---
//				
//
//				}else{
//						getActivity().finish();
//				}
				
			}//onClick(View v)
		});//qusSaveButton.setOnClickListener

		ansSaveButton = ((Button) findViewById(R.id.saveanswer_button));
		ansSaveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
//				Elem elem = ((AddElemActivity) getActivity()).getCurrentElem();
//
//				// When the user clicks "Save," upload the elem to Parse
//				// Add data to the elem object:
//				elem.setTitle(picTitle.getText().toString());				
//				elem.setContent(picContent.getText().toString());
//				// Associate the elem with the current user
//				//elem.setAuthor(ParseUser.getCurrentUser());
//
//				// Add the class
//				elem.setClas(classSpinner.getSelectedItem().toString());
//
//				// If the user added a photo, that data will be
//				// added in the CameraFragment
//				ParseFile imageFile = ((AddElemActivity) getActivity())
//						.getCurrentElem().getImageFile();
//				if (imageFile != null) {
//				 				
//						// Save the photo and return--------
//						elem.saveInBackground(new SaveCallback() {
//		
//							@Override
//							public void done(ParseException e) {
//								if (e == null) {
//									getActivity().setResult(Activity.RESULT_OK);
//									getActivity().finish();
//								} else {
//									Toast.makeText(
//											getActivity().getApplicationContext(),
//											"Error saving: " + e.getMessage(),
//											Toast.LENGTH_SHORT).show();
//								}
//							}
//		
//						});//end of elem.saveInBackground(new SaveCallback()---
//				
//
//				}else{
//						getActivity().finish();
//				}
				
			}//onClick(View v)
		});//ansSaveButton.setOnClickListener

		// Until the user has taken a photo, hide the preview
		qusPreview = (ParseImageView) findViewById(R.id.elem_preview_qusimage);
		qusPreview.setVisibility(View.INVISIBLE);
		
		ansPreview = (ParseImageView) findViewById(R.id.elem_preview_ansimage);
		ansPreview.setVisibility(View.INVISIBLE);
		
	}//findviews()

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_ans);
		context=this;
		res = this.getResources();
        //findViews();
		
		
	}//onCreate

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}// onResume()

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setup_ans, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}// onOptionsItemSelected
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		//---for pick up  photo from device-----				 
		if(resultCode==Activity.RESULT_OK){			 
			if(requestCode==SELECT_URIPHOTO){
				// for pickup file setting---xxxxx---
				appState = (KidMatchApp) context.getApplicationContext();
				if(appState.numPng !=null){
					appState.numPng++;
				}else{
					appState.numPng=0;
				}
				
				
//				styleType = styleSpinner.getSelectedItem().toString();//get item string
//				if(styleType.equals("square")){
//					isSquare = true;
//					appState = (KidMatchApp) context.getApplicationContext();
//					appState.isSquare=isSquare;
//				}else{
//					isSquare = false;
//					appState = (KidMatchApp) context.getApplicationContext();
//					appState.isSquare=isSquare;
//				}
//				
//  		
//				//get storage filepath uri-------------------------------------111111---xxx      filepath 
//				Uri selectedImageUri = data.getData();
//				String filepath = Utils.getPath(context, selectedImageUri);
//				
//				//transfer to bimap and setup shrink option----file2bitma------222222---xxx      photobm
//				BitmapFactory.Options options = new BitmapFactory.Options();
//        	    options.inJustDecodeBounds = false;  
//        	    options.inSampleSize = 2;
//				
//        	    Bitmap photobm = BitmapFactory.decodeFile(filepath, options);//decodeFile(file2bitmap)
//        	      	   
//	            //fix bitmap size by 400 x 400 for pickup file----------------------333333---xxx    photobmResize	           
// 
//        	    if(photobm.getWidth()<photobm.getHeight()){
//	        	    	photobmResizeWH = Bitmap.createScaledBitmap(photobm,
//								                            				 400,
//								   400* photobm.getHeight() / photobm.getWidth(), false);
//        	    	
//	        	    	rotateAngle=0f;
//        	    }else{
//	        	    	photobmResizeWH = Bitmap.createScaledBitmap(photobm,
//	  	            		        400* photobm.getWidth() / photobm.getHeight(),
//	  	            		                                                  400, false);
//	        	    	rotateAngle=90f;
//        	    }
//        	    
//        	    Bitmap photobmResize = 	photobmResizeWH;           
//	            //rotate bitmap to 90(landscape to portait)-------------------------44444---xxx  photobmResizeRotate
//	            Matrix matrix = new Matrix();
//	    		matrix.postRotate(rotateAngle);
//	    		Bitmap photobmResizeRotate = Bitmap.createBitmap(photobmResize, 0,
//	    				0, photobmResize.getWidth(), photobmResize.getHeight(),
//	    				matrix, true);
//	    		
//	    		//resize border---------------------------------------------------------444---xxx border2Resize/border3Resize
//	    		//bk44-square-border2   bk9-circle-border3
//	    		Bitmap border2 = BitmapFactory.decodeResource(res,R.drawable.bk44);//bk44-square-border2
//	    		Bitmap border3 = BitmapFactory.decodeResource(res,R.drawable.bk9);//bk9-circle-border3
//	    		
//	    		if(isSquare){
//		    		  //square rounded corner----xxxxx
//		    		  int width = border2.getWidth();
//		    		  int height = border2.getHeight();
//		    		  // 設置想要的大小
//		    		  int newWidth  = photobmResizeRotate.getWidth();
//		    		  int newHeight = photobmResizeRotate.getHeight();
//		    		  // 計算缩放比例
//		    		  float scaleWidth  =(((float) newWidth) / width)*1.2f;
//		    		  float scaleHeight =(((float) newHeight) / height)*1.2f;
//		    		  matrix.postScale(scaleWidth, scaleHeight);
//		    		  Bitmap border2Resize = Bitmap.createBitmap(border2, 0, 0, width, height, matrix,true);//bk44-square-border2
//			    
//			    	  //rouded corners image---------------------------------------6666---xxx111  roundCornerImage	    		 
//			    	  Bitmap roundCornerImage = Bitmap.createBitmap(photobmResizeRotate.getWidth(), photobmResizeRotate.getHeight(), Bitmap.Config.ARGB_8888);
//			    	  Canvas canvas = new Canvas(roundCornerImage);
//			    	  int color = 0xff424242;
//			    	  Paint paint = new Paint();
//			    	  Rect rect = new Rect(0, 0, photobmResizeRotate.getWidth(), photobmResizeRotate.getHeight());
//			    	  RectF rectF = new RectF(rect);
//			    	  float roundPx = 20;//rounded angle
//			    		
//			    	  paint.setAntiAlias(true);
//			    	  canvas.drawARGB(0, 0, 0, 0);
//			    	  paint.setColor(color);
//			    	  canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
//			    		
//			    	  paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
//			    	  canvas.drawBitmap(photobmResizeRotate, rect, rect, paint);
//		 	  
//			    	  //combine squareBorder and rotatedScaledphotoImage to cs--------------------------777-----xxxxxx imageCombine
//			  		  //for roundCornerImage pick file-xxx---roundCornerImage.getWidth()+100--b1.2(10,-5)   	   	    	   
//			    	  cs = Bitmap.createBitmap(roundCornerImage.getWidth()+100, roundCornerImage.getHeight()+100, Bitmap.Config.ARGB_8888);//define canvas size
//			    	  Canvas comboImage = new Canvas(cs);
//			    		
//			    	  comboImage.drawBitmap(border2Resize, 10f,0f, null);//b1.2/p+100(10,-5)(10,0)---bottom
//			    	  comboImage.drawBitmap(roundCornerImage, 50f, 50f, null);//(50,50)---top  最上面(photo)加在上面
//    		 }else{ 
//    			      //circle----xxxxx
//		 	          int width = border3.getWidth();
//		 	    	  int height = width;		
//		 	    	  // 設置想要的大小
// 			    	  int newWidth = (photobmResizeRotate.getWidth()>photobmResizeRotate.getHeight())?photobmResizeRotate.getWidth()
// 			    				                                                                       :photobmResizeRotate.getHeight();
// 			    	  int newHeight =newWidth;		
// 			    	  // 計算缩放比例
// 			    	  if(photobmResizeRotate.getWidth()==photobmResizeRotate.getHeight()){
//	 			    	  float scaleWidth  =(((float) newWidth ) / width)*1.3f;//1.3_正方  0.7_長方
//	 			    	  float scaleHeight =(((float) newHeight) /height)*1.3f;
//	 			    	  matrix.postScale(scaleWidth, scaleHeight);
// 			    	  }else{
// 			    		  float scaleWidth  =(((float) newWidth ) / width)*0.7f;//1.3_正方  0.7_長方
//	 			    	  float scaleHeight =(((float) newHeight) /height)*0.7f;
//	 			    	  matrix.postScale(scaleWidth, scaleHeight);
// 			    	  }
// 			    	  
// 			    	  Bitmap border3Resize = Bitmap.createBitmap(border3, 0, 0, width, height, matrix,true);//bk9-circle-border3
// 			    
//    		 
//		    		  //circle image------------------------------------------------6666---xxx222 circleImage
//			    	  BitmapShader shader = new BitmapShader (photobmResizeRotate,  TileMode.CLAMP, TileMode.CLAMP);
//			    	  Paint paint = new Paint();
//			    	  paint.setShader(shader);
//			    		
//			    	  Bitmap circleImage = Bitmap.createBitmap(photobmResizeRotate.getWidth(), photobmResizeRotate.getHeight(), Bitmap.Config.ARGB_8888);
//			    	  Canvas c = new Canvas(circleImage);
//			    	  c.drawCircle(photobmResizeRotate.getWidth()/2, photobmResizeRotate.getHeight()/2, photobmResizeRotate.getWidth()/2, paint);
//			    	  
//			    	  //combine squareBorder and rotatedScaledphotoImage to cs--------------------------777-----xxxxxx imageCombine
//			  		  //for roundCornerImage pick file-xxx---roundCornerImage.getWidth()+100--b1.2(10,-5)   	   	    	   
//			    	  cs = Bitmap.createBitmap(circleImage.getWidth()+100, circleImage.getHeight()+100, Bitmap.Config.ARGB_8888);//define canvas size
//			    	  Canvas comboImage = new Canvas(cs);
//			    	  
//			    	  if(photobmResizeRotate.getWidth()==photobmResizeRotate.getHeight()){
//				    	  comboImage.drawBitmap(border3Resize, -10f,-10f, null);//b1.3/p+100(-10,-10)正方---bottom
//				    	  //comboImage.drawBitmap(border3Resize, -0f,155f, null);//b1.3/p+100(-10,-10)長方---bottom
//				    	  comboImage.drawBitmap(circleImage, 50f, 50f, null);//(50,50)---top  最上面(photo)加在上面
//			    	  }else{
//			    		//comboImage.drawBitmap(border3Resize, -10f,-10f, null);//b1.3/p+100(-10,-10)正方---bottom
//				    	  comboImage.drawBitmap(border3Resize, -0f,155f, null);//b1.3/p+100(-10,-10)長方---bottom
//				    	  comboImage.drawBitmap(circleImage, 50f, 50f, null);//(50,50)---top  最上面(photo)加在上面
//			    	  }
// 				    	  		    	   
//    		 }
//	    	  		
//	    	  //bitmap to PNG type's ByteArrayOutputStream --------------------8888---xxx   bos -> scaledData
//	    	  ByteArrayOutputStream bos = new ByteArrayOutputStream();
//	    	  cs.compress(Bitmap.CompressFormat.PNG, 100, bos);//combine border
//	    	  byte[] scaledData = bos.toByteArray();
//	    		 	    		
//	    	  //scaled image to Parse and setup name to pickup file photo(pcf)-----999---xxx    imageFile
//	    	  imageFile = new ParseFile("pcf"+appState.numPng+".png", scaledData);
//	    	  imageFile.saveInBackground(new SaveCallback() {
//
//		    		  public void done(ParseException e) {
//		    				if (e != null) {
//		    					Toast.makeText(getActivity(),
//		    							"Error saving: " + e.getMessage(),
//		    							Toast.LENGTH_LONG).show();
//		    				} else {
//		    					 
//		    					((AddElemActivity) getActivity()).getCurrentElem().setImageFile(
//		    							imageFile);
//		    					
//		    					if (imageFile != null) {
//		    						picPreview.setParseFile(imageFile);
//		    						picPreview.loadInBackground(new GetDataCallback() {
//		    							@Override
//		    							public void done(byte[] data, ParseException e) {
//		    								picPreview.setVisibility(View.VISIBLE);
//		    							}
//		    						});
//		    					}
//		    		
//		    				}
//		    			}
//	    	  });
	    		
 	    	  
				 
		  }//requestCode==SELECT_URIPHOTO
		
	   }//resultCode==Activity.RESULT_OK
		
    }//onActivityResult
	
	
}//SetupAnsActivity
