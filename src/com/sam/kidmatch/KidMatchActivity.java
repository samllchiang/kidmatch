package com.sam.kidmatch;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

  
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.sam.login.Constants;
import com.sam.login.FasterAnimationsContainer;
import com.sam.login.LoginActivity;
import com.sam.utility.ImageLoader;

 

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnKeyListener;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;


public class KidMatchActivity extends Activity implements TextToSpeech.OnInitListener,ViewFactory {
	
	int scrW,scrH,scrLength;//Screen Width/Height/Min. Length
	int divLength=6;//element length = Min. Length / divLength
	int bgDivLength=2;
	int tick = 0;
	int dir = 1;
	int STEPX = 300;
	int STEP,STEP2,STEP3,STEP4;
	int STEPY,STEPY2,STEPY3,STEPY4;
	int count=0,asemCount=2000;
	int ID;
	int qusNo;
	int randomNum1,randomNum2,randomNum3,randomNum4,randomNum0;//random parameter
	int numQueryAsem=0;//randomNum0 switch to Asem
	int sizelistQueryElem,sizelistQueryAsem,sizelistQueryBorderElem;//Array size of Uri for query/queryAsem photo
	int sizePhotoUriArraylist,sizeAnsPhotoUriArraylist; //Array size of query/queryAsem photo
	int sizeDefaultphoto=80;
	
	private final int DELAY = 150;
	 
	private static final String TAG = "--** kidmatch ** --"; // for Debug
	//private static final int ANIMATION_INTERVAL = 500;// 200ms
	
	
	int[] defaultphoto;//initial image to play game is different to parse.com    
	int[] sec10Amination;
	int[] sec3Amination;
	int sec10Count;
	int sec3Count;
	int score;
	int highscore;
	
	private static final boolean D = false;
	protected static boolean ivAns1Rotate = true;
	protected static boolean ivAns2Rotate = true;
	protected static boolean ivAns3Rotate = true;
	protected static boolean ivAns4Rotate = true;
	public static boolean valid = false;
	 
	boolean isAutoPlay=true ; 
	boolean bNorth1 = true,bNorth2 = true,bNorth3 = true,bNorth4 = true;
	boolean bLeft1 = true,bLeft2 = true,bLeft3 = true,bLeft4 = true;
	
	// Elem Class store the question photos
	//Asem Class store the answer photos
	
	protected AlertDialog dialogScore,dialogTop10;
	
	private static KidMatchApp appState;
	List<Photo> photoUriArraylist,ansPhotoUriArraylist; //uri array to get uri of selected question photo and answer photo
	List<Elem> listQueryElem;    //selected question photos in Elem class of parse.com
	List<Asem> listQueryAsem;   //select answer photos in Asem class of parse.com
	 
	
	Context context;	
	ImageView ivAns1,ivAns2,ivAns3,ivAns4,ivQus,ivAns0,img_10sec,img_3sec,img_play;	
	MediaPlayer mp;
	Vibrator vr;
	TextToSpeech tts;
	Chronometer chronometer;
	DisplayMetrics dm;
	ActionBar actionBar;
	
	ImageLoader imageLoader;
	ProgressDialog mProgressDialog;
	
	 View contentView, contentViewp,contentViewGo,customerViewScore,customerViewTop10;
	
	SharedPreferences scorePref;
	
	private TextView dialogtxt_score,dialogtxt_score1,dialogtxt_score2,dialogtxt_score3,dialogtxt_user1,dialogtxt_user2,dialogtxt_user3;
	
	//private static Asem asem;
	
	private void createDefaultRandomNum(){
	 
	        //--- randomNum1 ---
			randomNum1 = (int) (Math.floor(sizeDefaultphoto*Math.random()));
			if(randomNum1>sizeDefaultphoto) randomNum1=sizeDefaultphoto;
	 
			//--- randomNum2 ---
			randomNum2 = (int) (Math.floor(sizeDefaultphoto*Math.random()));
			if(randomNum2>sizeDefaultphoto) randomNum2=sizeDefaultphoto;
			
			while(randomNum2==randomNum1){
				randomNum2 = (int) (Math.floor(sizeDefaultphoto*Math.random()));
				if(randomNum2>sizeDefaultphoto) randomNum2=sizeDefaultphoto;
			}
			
			//--- randomNum3 ---
			randomNum3 = (int) (Math.floor(sizeDefaultphoto*Math.random()));
			if(randomNum3>sizeDefaultphoto) randomNum3=sizeDefaultphoto;
			
			while(randomNum3==randomNum1 || randomNum3==randomNum2){
				randomNum3 = (int) (Math.floor(sizeDefaultphoto*Math.random()));
				if(randomNum3>sizeDefaultphoto) randomNum3=sizeDefaultphoto;
			}
			
			//--- randomNum4 ---
			randomNum4 = (int) (Math.floor(sizeDefaultphoto*Math.random()));
			if(randomNum4>sizeDefaultphoto) randomNum4=sizeDefaultphoto;
			
			while(randomNum4==randomNum1 || randomNum4==randomNum2 || randomNum4==randomNum3){
				randomNum4 = (int) (Math.floor(sizeDefaultphoto*Math.random()));
				if(randomNum4>sizeDefaultphoto) randomNum4=sizeDefaultphoto;
			}
	
			 
			//--- randomNum0 ---
			//get a random value from a integer array
			int [] randomNumArray = {randomNum1,randomNum2,randomNum3,randomNum4};
			randomNum0 = randomNumArray[new Random().nextInt(randomNumArray.length)];
			 
			
	}//createDefaultRandomNum()////////////////////////////////////////////////////////
	
	private void createRandomNum(){
		
		do{		
		        
			    //--- randomNum1 ---
				randomNum1 = (int) (Math.floor(sizelistQueryElem*Math.random()));
				if(randomNum1>sizelistQueryElem) randomNum1=sizelistQueryElem;
		 
				//--- randomNum2 ---
				randomNum2 = (int) (Math.floor(sizelistQueryElem*Math.random()));
				if(randomNum2>sizelistQueryElem) randomNum2=sizelistQueryElem;
				
				while(randomNum2==randomNum1){
					randomNum2 = (int) (Math.floor(sizelistQueryElem*Math.random()));
					if(randomNum2>sizelistQueryElem) randomNum2=sizelistQueryElem;
				}
				
				//--- randomNum3 ---
				randomNum3 = (int) (Math.floor(sizelistQueryElem*Math.random()));
				if(randomNum3>sizelistQueryElem) randomNum3=sizelistQueryElem;
				
				while(randomNum3==randomNum1 || randomNum3==randomNum2){
					randomNum3 = (int) (Math.floor(sizelistQueryElem*Math.random()));
					if(randomNum3>sizelistQueryElem) randomNum3=sizelistQueryElem;
				}
				
				//--- randomNum4 ---
				randomNum4 = (int) (Math.floor(sizelistQueryElem*Math.random()));
				if(randomNum4>sizelistQueryElem) randomNum4=sizelistQueryElem;
				
				while(randomNum4==randomNum1 || randomNum4==randomNum2 || randomNum4==randomNum3){
					randomNum4 = (int) (Math.floor(sizelistQueryElem*Math.random()));
					if(randomNum4>sizelistQueryElem) randomNum4=sizelistQueryElem;
				}
		
				
				//--- randomNum4asem ---sizelistQueryAsem---numQueryAsem
				if(sizelistQueryAsem != 0 && numQueryAsem < sizelistQueryAsem){
					randomNum4=numQueryAsem;
		            Log.e("-----numQueryAsem-----","----sizelistQueryAsem--------=   "+sizelistQueryAsem+"----numQueryAsem--------=   "+numQueryAsem);
				}//if(sizelistQueryAsem != 0 && numQueryAsem < sizelistQueryAsem)
				
				//--- randomNum0 ---
				//get a random value from a integer array
				int [] randomNumArray = {randomNum1,randomNum2,randomNum3,randomNum4};
				randomNum0 = randomNumArray[new Random().nextInt(randomNumArray.length)];
				
				if(numQueryAsem >= sizelistQueryAsem) numQueryAsem=sizelistQueryAsem-1;
				
		}while((ansPhotoUriArraylist.get(numQueryAsem)==photoUriArraylist.get(randomNum1)) ||
			   (ansPhotoUriArraylist.get(numQueryAsem)==photoUriArraylist.get(randomNum2)) ||
			   (ansPhotoUriArraylist.get(numQueryAsem)==photoUriArraylist.get(randomNum3)) 
	    );//do-while
		
		numQueryAsem++;	
	}//createRandomNum()//////////////////////////////////////////////////////////////
	
	private void imageLoaderSetup(){
		
				 
        
		appState = (KidMatchApp) context.getApplicationContext();   	 
	    if (appState.isDefaule) {//default-----------------------------------
	    	
	    	createDefaultRandomNum();//default random number
	    	
	    	//imageLoader.DisplayImage(photoUriArraylist.get(randomNum1).getphoto(),ivAns1);
	    	ivAns1.setImageResource(defaultphoto[randomNum1]);
	        LinearLayout.LayoutParams ivAns1_lp = (LinearLayout.LayoutParams)ivAns1.getLayoutParams(); 
	        ivAns1_lp.height=scrLength/divLength;
	        ivAns1_lp.width=scrLength/divLength;
	                
	        
			//imageLoader.DisplayImage(photoUriArraylist.get(randomNum2).getphoto(),ivAns2);
			ivAns2.setImageResource(defaultphoto[randomNum2]);
			LinearLayout.LayoutParams ivAns2_lp = (LinearLayout.LayoutParams)ivAns2.getLayoutParams(); 
	        ivAns2_lp.height=scrLength/divLength;
	        ivAns2_lp.width=scrLength/divLength; 
			
			 
			//imageLoader.DisplayImage(photoUriArraylist.get(randomNum3).getphoto(),ivAns3);
			ivAns3.setImageResource(defaultphoto[randomNum3]);
			LinearLayout.LayoutParams ivAns3_lp = (LinearLayout.LayoutParams)ivAns3.getLayoutParams(); 
	        ivAns3_lp.height=scrLength/divLength;
	        ivAns3_lp.width=scrLength/divLength; 
	        	        
	    			 
			//imageLoader.DisplayImage(photoUriArraylist.get(randomNum4).getphoto(),ivAns4);
			ivAns4.setImageResource(defaultphoto[randomNum4]);
			LinearLayout.LayoutParams ivAns4_lp = (LinearLayout.LayoutParams)ivAns4.getLayoutParams(); 
	        ivAns4_lp.height=scrLength/divLength;
	        ivAns4_lp.width=scrLength/divLength; 
						 
			//imageLoader.DisplayImage(photoUriArraylist.get(randomNum4).getphoto(),ivAns0);
			ivAns0.setImageResource(defaultphoto[randomNum0]);
			FrameLayout.LayoutParams ivAns0_lp = (FrameLayout.LayoutParams)ivAns0.getLayoutParams(); 
	        ivAns0_lp.height=scrLength/bgDivLength;
	        ivAns0_lp.width=scrLength/bgDivLength;					     	
	    	
	    }else{//parse.com-----------------------------------------------------
		
	    	createRandomNum();// parse.com random number
	    	
	    	imageLoader.DisplayImage(photoUriArraylist.get(randomNum1).getphoto(),ivAns1);
	        LinearLayout.LayoutParams ivAns1_lp = (LinearLayout.LayoutParams)ivAns1.getLayoutParams(); 
	        ivAns1_lp.height=scrLength/divLength;
	        ivAns1_lp.width=scrLength/divLength;
	        
			 
			imageLoader.DisplayImage(photoUriArraylist.get(randomNum2).getphoto(),ivAns2);
			LinearLayout.LayoutParams ivAns2_lp = (LinearLayout.LayoutParams)ivAns2.getLayoutParams(); 
	        ivAns2_lp.height=scrLength/divLength;
	        ivAns2_lp.width=scrLength/divLength; 
			
			 
			imageLoader.DisplayImage(photoUriArraylist.get(randomNum3).getphoto(),ivAns3);
			LinearLayout.LayoutParams ivAns3_lp = (LinearLayout.LayoutParams)ivAns3.getLayoutParams(); 
	        ivAns3_lp.height=scrLength/divLength;
	        ivAns3_lp.width=scrLength/divLength;
	        
//	        imageLoader.DisplayImage(photoUriArraylist.get(randomNum4).getphoto(),ivAns4);
//			LinearLayout.LayoutParams ivAns4_lp = (LinearLayout.LayoutParams)ivAns4.getLayoutParams(); 
//	        ivAns4_lp.height=scrLength/divLength;
//	        ivAns4_lp.width=scrLength/divLength; 
//						 
//			imageLoader.DisplayImage(photoUriArraylist.get(randomNum0).getphoto(),ivAns0);
//			FrameLayout.LayoutParams ivAns0_lp = (FrameLayout.LayoutParams)ivAns0.getLayoutParams(); 
//	        ivAns0_lp.height=scrLength/bgDivLength;
//	        ivAns0_lp.width=scrLength/bgDivLength;
	        
	        //--- randomNum4asem ---sizelistQueryAsem---numQueryAsem
	        if((sizelistQueryAsem != 0 && numQueryAsem<sizelistQueryAsem)){ 
			
	        	imageLoader.DisplayImage(ansPhotoUriArraylist.get(randomNum4).getphoto(),ivAns4);
	    		LinearLayout.LayoutParams ivAns4_lp = (LinearLayout.LayoutParams)ivAns4.getLayoutParams(); 
	            ivAns4_lp.height=scrLength/divLength;
	            ivAns4_lp.width=scrLength/divLength;	            	        			 
	        
			}else{
	    			 
				imageLoader.DisplayImage(photoUriArraylist.get(randomNum4).getphoto(),ivAns4);
				LinearLayout.LayoutParams ivAns4_lp = (LinearLayout.LayoutParams)ivAns4.getLayoutParams(); 
		        ivAns4_lp.height=scrLength/divLength;
		        ivAns4_lp.width=scrLength/divLength; 							 				 		
			}
	        
	      //--- randomNum0  
	        if((sizelistQueryAsem != 0 && numQueryAsem<sizelistQueryAsem && randomNum0==randomNum4)){ 
            
	            imageLoader.DisplayImage(ansPhotoUriArraylist.get(randomNum0).getphoto(),ivAns0);
	    		FrameLayout.LayoutParams ivAns0_lp = (FrameLayout.LayoutParams)ivAns0.getLayoutParams(); 
	            ivAns0_lp.height=scrLength/bgDivLength;
	            ivAns0_lp.width=scrLength/bgDivLength;		        			 
	        
			}else{
	    			 							 
				imageLoader.DisplayImage(photoUriArraylist.get(randomNum0).getphoto(),ivAns0);
				FrameLayout.LayoutParams ivAns0_lp = (FrameLayout.LayoutParams)ivAns0.getLayoutParams(); 
		        ivAns0_lp.height=scrLength/bgDivLength;
		        ivAns0_lp.width=scrLength/bgDivLength;		
			 }
	        
	        
	        
	        
	        
	        
	        
	        
        
	    }//if(appState.isDefaule)------default or parse.com
        
        
        //ivAns4.setVisibility(View.VISIBLE);
	}//imageLoaderSetup()
	
	
	private void findViews(){
		
		mp = MediaPlayer.create(context, R.raw.balloon_pop); //語音播放物件
		dm =  new DisplayMetrics(); //實作screen物件
		//取得目前顯示視窗實體 ->dm
		this.getWindowManager().getDefaultDisplay().getRealMetrics(dm);
		scrW = dm.widthPixels;
		scrH = dm.heightPixels;
		scrLength = (scrW<scrH)?scrW:scrH;
		
		//Setup ActionBar-----
		actionBar = getActionBar();		
		//由系統提供之服務,要取得服務權限_ permissions
		vr = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);				 
		//定義 tts 物件		
		tts = new TextToSpeech(context.getApplicationContext(), this);
		 
		//定義 FrameLayout 物件	--------text describe------------
        contentView = findViewById(R.id.fullscreen_text_question);
        contentViewp = findViewById(R.id.fullscreen_text_clickplay);
        contentViewGo = findViewById(R.id.fullscreen_text_ready2go);
        
        ////dialogScore 客製化視窗，不可用 context 必須用  SettingActivity.this 否則當掉 ------- customerViewScore
        LayoutInflater factory = LayoutInflater.from(KidMatchActivity.this);
        customerViewScore = factory.inflate(R.layout.dialog_score,null);
        dialogScore = new AlertDialog.Builder(KidMatchActivity.this).create();
        dialogtxt_score = (TextView) customerViewScore.findViewById(R.id.dialogtxt_score);
        dialogScore.setView(customerViewScore);       
       
		dialogScore.setOnKeyListener(new OnKeyListener(){ //設定使用倒回鍵會結束dialog---backup key to dismiss()
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if(KeyEvent.KEYCODE_BACK==keyCode)
					dialogScore.dismiss();
				return false;
			}
		});
	////dialogTop10 客製化視窗，不可用 context 必須用  SettingActivity.this 否則當掉 ------- customerViewTop10
        LayoutInflater factory1 = LayoutInflater.from(KidMatchActivity.this);
        customerViewTop10 = factory1.inflate(R.layout.dialog_top10list,null);
        dialogTop10 = new AlertDialog.Builder(KidMatchActivity.this).create();
        dialogtxt_score1 = (TextView) customerViewTop10.findViewById(R.id.dialogtxt_score1);
        dialogtxt_score2 = (TextView) customerViewTop10.findViewById(R.id.dialogtxt_score2);
        dialogtxt_score3 = (TextView) customerViewTop10.findViewById(R.id.dialogtxt_score3);
        dialogtxt_user1 = (TextView) customerViewTop10.findViewById(R.id.dialogtxt_user1);
        dialogtxt_user2 = (TextView) customerViewTop10.findViewById(R.id.dialogtxt_user2);
        dialogtxt_user3 = (TextView) customerViewTop10.findViewById(R.id.dialogtxt_user3);
        dialogTop10.setView(customerViewTop10);       
       
		dialogTop10.setOnKeyListener(new OnKeyListener(){ //設定使用倒回鍵會結束dialog---backup key to dismiss()
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if(KeyEvent.KEYCODE_BACK==keyCode)
					dialogTop10.dismiss();
				return false;
			}
		});//dialogTop10.setOnKeyListener
		
        //setting preferences for Score---------------------------------------xxx scorePref
        scorePref = context.getSharedPreferences("PrefScore", Context.MODE_PRIVATE);
        Editor editor = scorePref.edit();
        //editor.putInt("score", score);
        //editor.commit();
        //getting preferences
        //scorePref = context.getSharedPreferences("PrefScore", Context.MODE_PRIVATE);
        //score5Pref = scorePref.getInt("key", 0); //0 is the default value if get nothing
        
        
        
        //--------------------get data from R.array.defaultphoto---------xxx defaultphoto[i]        
        TypedArray ar = context.getResources().obtainTypedArray(R.array.defaultphoto);
        sizeDefaultphoto = ar.length();
        defaultphoto = new int[sizeDefaultphoto];//array need to disclear length to complete object.
        for (int i = 0; i < sizeDefaultphoto; i++)
        	defaultphoto[i] = ar.getResourceId(i, 0);
        ar.recycle();
        // Do stuff with resolved reference array, resIds[]...
        ///for (int i = 0; i < len; i++){
        //    Log.e (TAG, "Res Id " + i + " is " + Integer.toHexString(defaultphoto[i])); 
        ///}
        
        //img_10sec--------------------get data from R.array.sec10---------sec10Amination[i]
        img_10sec  = (ImageView)findViewById(R.id.img_10sec);
        FrameLayout.LayoutParams img_10sec_lp = (FrameLayout.LayoutParams)img_10sec.getLayoutParams(); 
        img_10sec_lp.height=scrLength/bgDivLength;
        img_10sec_lp.width=scrLength/bgDivLength;
        
        
        TypedArray ar10 = context.getResources().obtainTypedArray(R.array.sec10);
        int sizesec10Amination = ar10.length();
        sec10Amination = new int[sizesec10Amination];//array need to disclear length to complete object.
        for (int i = 0; i < sizesec10Amination; i++)
        	sec10Amination[i] = ar10.getResourceId(i, 0);
        ar10.recycle();
         
        //img_3sec--------------------get data from R.array.sec3----------sec3Amination[i]
        img_3sec  = (ImageView)findViewById(R.id.img_3sec);
        FrameLayout.LayoutParams img_3sec_lp = (FrameLayout.LayoutParams)img_3sec.getLayoutParams(); 
        img_3sec_lp.height=scrLength/bgDivLength;
        img_3sec_lp.width=scrLength/bgDivLength;
        
        
        TypedArray ar3s = context.getResources().obtainTypedArray(R.array.sec3);
        int sizesec3Amination = ar3s.length();
        sec3Amination = new int[sizesec3Amination];//array need to disclear length to complete object.
        for (int i = 0; i < sizesec3Amination; i++)
        	sec3Amination[i] = ar3s.getResourceId(i, 0);
        ar3s.recycle();
        
        img_play  = (ImageView)findViewById(R.id.img_play);
        FrameLayout.LayoutParams img_play_lp = (FrameLayout.LayoutParams)img_play.getLayoutParams(); 
        img_play_lp.height=scrLength/divLength;
        img_play_lp.width=scrLength/divLength;
        ///////////////////////////////////////////////////////////////////////////////
        appState = (KidMatchApp) context.getApplicationContext();   	 
        if (appState.isDefaule) {// default ----------------------------------------------------
  		  
        	createDefaultRandomNum();//default random number
 			//------------------------------------------------------------------------
 		 
 	        // Load image into ivAns1 for imageView setting
 	        ivAns1 = (ImageView) findViewById(R.id.img_ans1); 
 	        ivAns1.setImageResource(defaultphoto[randomNum1]);
 	        //imageLoader.DisplayImage(photoUriArraylist.get(randomNum1).getphoto(),ivAns1);
 	        LinearLayout.LayoutParams ivAns1_lp = (LinearLayout.LayoutParams)ivAns1.getLayoutParams(); 
 	        ivAns1_lp.height=scrLength/divLength;
 	        ivAns1_lp.width=scrLength/divLength;
 	        
 			ivAns2 = (ImageView) findViewById(R.id.img_ans2);
 			ivAns2.setImageResource(defaultphoto[randomNum2]);
 			//imageLoader.DisplayImage(photoUriArraylist.get(randomNum2).getphoto(),ivAns2);
 			LinearLayout.LayoutParams ivAns2_lp = (LinearLayout.LayoutParams)ivAns2.getLayoutParams(); 
 	        ivAns2_lp.height=scrLength/divLength;
 	        ivAns2_lp.width=scrLength/divLength; 
 			
 			ivAns3 = (ImageView) findViewById(R.id.img_ans3);
 			ivAns3.setImageResource(defaultphoto[randomNum3]);
 			//imageLoader.DisplayImage(photoUriArraylist.get(randomNum3).getphoto(),ivAns3);
 			LinearLayout.LayoutParams ivAns3_lp = (LinearLayout.LayoutParams)ivAns3.getLayoutParams(); 
 	        ivAns3_lp.height=scrLength/divLength;
 	        ivAns3_lp.width=scrLength/divLength; 
  	           
 	        ivAns4 = (ImageView) findViewById(R.id.img_ans4);
 	        ivAns4.setImageResource(defaultphoto[randomNum4]);
 			//imageLoader.DisplayImage(ansPhotoUriArraylist.get(0).getphoto(),ivAns4);
 			LinearLayout.LayoutParams ivAns4_lp = (LinearLayout.LayoutParams)ivAns4.getLayoutParams(); 
 	        ivAns4_lp.height=scrLength/divLength;
 	        ivAns4_lp.width=scrLength/divLength; 
 			
 			ivAns0 = (ImageView) findViewById(R.id.img_ans0);
 			ivAns0.setImageResource(defaultphoto[randomNum0]);
 			//imageLoader.DisplayImage(ansPhotoUriArraylist.get(0).getphoto(),ivAns0);		
 			FrameLayout.LayoutParams ivAns0_lp = (FrameLayout.LayoutParams)ivAns0.getLayoutParams(); 
 	        ivAns0_lp.height=scrLength/bgDivLength;
 	        ivAns0_lp.width=scrLength/bgDivLength; 
 	        
 	        //ivAns0.setImageResource(defaultphoto[1]);
        	
        	
        }else{// parse.com-----------------------------------------------------------------------      	 
 		        	
        	imageLoader = new ImageLoader(context);       
            photoUriArraylist = new ArrayList<Photo>();//Elem Class for Element       
            ansPhotoUriArraylist = new ArrayList<Photo>();//Asem Class for Answer
        	
        	try {
				//Begin of Query for Elem---------------------------------------------xxxElem
				ParseQuery<Elem> query = new ParseQuery<Elem>("Elem");//***1			
				appState = (KidMatchApp) context.getApplicationContext();
				if(appState.value1==null){
					//appState.value1="face,math,lang,shap,clor" ;//by Default
					appState.value1="face" ;//by Default
				}			
				String [] splitClass =  appState.value1.split(",");
				  
				query.whereContainedIn("class", Arrays.asList(splitClass));			
				//query.whereEqualTo("class","face");//***2			
				query.orderByAscending("position");//***3
				
				listQueryElem = query.find();
				sizelistQueryElem = listQueryElem.size();
	
				for (ParseObject elem : listQueryElem) {
					
					ParseFile image = (ParseFile) elem.get("image");//***4
					Photo map = new Photo();
					map.setphoto(image.getUrl());
					photoUriArraylist.add(map);
				 		     
				    elem.put("position", count++);//initial setup to  ordering number!!!
				    elem.put("isChecked", false);//initial setup to false!!!
				    elem.saveEventually();
				}//for (ParseObject elem : listQueryElem)
				//End of Query for Elem-----------------------------------------------xxxElem
				//Begin of Query for Asem---------------------------------------------xxxAsem
				ParseQuery<Asem> queryAsem = new ParseQuery<Asem>("Asem");//***1			 
				queryAsem.orderByAscending("createAt");//***3
				
				listQueryAsem = queryAsem.find();
				sizelistQueryAsem = listQueryAsem.size();
				
				int numasem=0;
				for (ParseObject asem : listQueryAsem) {
					
					ParseFile image = (ParseFile) asem.get("image");//***4
					Photo map = new Photo();
					map.setphoto(image.getUrl());
					ansPhotoUriArraylist.add(map);
									
				    numasem++;			     
				    //asem.put("position", count++);//initial setup to  ordering number!!!
				    //asem.put("isChecked", false);//initial setup to false!!!
				    asem.saveEventually();
				}//for (ParseObject elem : listQueryAsem)
				//End of Query for Asem-----------------------------------------------xxxAsem			 
			     
			} catch (ParseException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			
			sizePhotoUriArraylist = photoUriArraylist.size();
			sizeAnsPhotoUriArraylist = ansPhotoUriArraylist.size();
		  
			createRandomNum();// parse.com random number
			//------------------------------------------------------------------------
		 
	        // Load image into ivAns1 for imageView setting
	        ivAns1 = (ImageView) findViewById(R.id.img_ans1); 
	        imageLoader.DisplayImage(photoUriArraylist.get(randomNum1).getphoto(),ivAns1);
	        LinearLayout.LayoutParams ivAns1_lp = (LinearLayout.LayoutParams)ivAns1.getLayoutParams(); 
	        ivAns1_lp.height=scrLength/divLength;
	        ivAns1_lp.width=scrLength/divLength;
	        
			ivAns2 = (ImageView) findViewById(R.id.img_ans2);
			imageLoader.DisplayImage(photoUriArraylist.get(randomNum2).getphoto(),ivAns2);
			LinearLayout.LayoutParams ivAns2_lp = (LinearLayout.LayoutParams)ivAns2.getLayoutParams(); 
	        ivAns2_lp.height=scrLength/divLength;
	        ivAns2_lp.width=scrLength/divLength; 
			
			ivAns3 = (ImageView) findViewById(R.id.img_ans3);
			imageLoader.DisplayImage(photoUriArraylist.get(randomNum3).getphoto(),ivAns3);
			LinearLayout.LayoutParams ivAns3_lp = (LinearLayout.LayoutParams)ivAns3.getLayoutParams(); 
	        ivAns3_lp.height=scrLength/divLength;
	        ivAns3_lp.width=scrLength/divLength; 
	        //ivAns4
	        if(ansPhotoUriArraylist.get(0).getphoto() != null){							
		        ivAns4 = (ImageView) findViewById(R.id.img_ans4);
				imageLoader.DisplayImage(ansPhotoUriArraylist.get(randomNum4).getphoto(),ivAns4);
				LinearLayout.LayoutParams ivAns4_lp = (LinearLayout.LayoutParams)ivAns4.getLayoutParams(); 
		        ivAns4_lp.height=scrLength/divLength;
		        ivAns4_lp.width=scrLength/divLength;
	        }else{	        
	        	ivAns4 = (ImageView) findViewById(R.id.img_ans4);
				imageLoader.DisplayImage(photoUriArraylist.get(randomNum4).getphoto(),ivAns4);
				LinearLayout.LayoutParams ivAns4_lp = (LinearLayout.LayoutParams)ivAns4.getLayoutParams(); 
		        ivAns4_lp.height=scrLength/divLength;
		        ivAns4_lp.width=scrLength/divLength;		         								  
	        }
	        //ivAns0
	        if(ansPhotoUriArraylist.get(0).getphoto() != null && randomNum0==randomNum4){			
	        	ivAns0 = (ImageView) findViewById(R.id.img_ans0);
				imageLoader.DisplayImage(ansPhotoUriArraylist.get(randomNum0).getphoto(),ivAns0);		
				FrameLayout.LayoutParams ivAns0_lp = (FrameLayout.LayoutParams)ivAns0.getLayoutParams(); 
		        ivAns0_lp.height=scrLength/bgDivLength;
		        ivAns0_lp.width=scrLength/bgDivLength; 	        		        	 
	        }else{	        
	        	ivAns0 = (ImageView) findViewById(R.id.img_ans0);
				imageLoader.DisplayImage(photoUriArraylist.get(randomNum0).getphoto(),ivAns0);
				FrameLayout.LayoutParams ivAns0_lp = (FrameLayout.LayoutParams)ivAns0.getLayoutParams(); 
		        ivAns0_lp.height=scrLength/bgDivLength;
		        ivAns0_lp.width=scrLength/bgDivLength;
	        }
       
        }//if (appState.isDefaule)--login to parse.com or play default
        
	}// findViews
	
	
	//method 1 ----moveChrometer
	private void moveChrometer(){
		
		//Moving Method 1-----chrometer----------------------------------------------
				chronometer = (Chronometer) findViewById(R.id.chronometer1);
				chronometer.setVisibility(View.INVISIBLE);
				chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){

					@Override
					public void onChronometerTick(Chronometer chronometer) {
						long time = System.currentTimeMillis();
						String date = new Date(time).toString();
						 tick=1;
						if(tick>0) {
							//ivAns1-------------------------------------
							STEP = (int) (STEPX*Math.random());
							STEPY = (int) (STEPX*Math.random());
							int ivAns1Angle = (int) (Math.random() * 45);
							Matrix matrix1 = new Matrix(); 
							if(ivAns1.getX()+dir*STEP <0 && dir == -1) {
								dir = 1;
							}
							else if(ivAns1.getX() + ivAns1.getWidth() + dir*STEP > scrW && dir == 1) {
								dir = -1;
							}
							ivAns1.setX(ivAns1.getX() + dir*STEP);
							
							if(ivAns1.getY()+dir*STEPY <0 && dir == -1) {
								dir = 1;
							}
							else if(ivAns1.getY() + ivAns1.getHeight()*1. + dir*STEPY > scrH && dir == 1) {
								dir = -1;
							}
							ivAns1.setY((ivAns1.getY() + dir*STEPY));
							
							if (ivAns1Rotate){
								ivAns1Rotate = false;
								int rotateDirection = (int) (Math.random() * 2);
								if (rotateDirection == 0){
									matrix1.postRotate((float) (-1)*ivAns1Angle, 26, 43);
									ivAns1.setRotation((float) (-1)*ivAns1Angle);
									ivAns1.setScaleX((float) 1.2);
									ivAns1.setScaleY((float) 1.2);
								} else {
									matrix1.postRotate((float) ivAns1Angle, 26, 43);
									ivAns1.setRotation((float) ivAns1Angle);
									ivAns1.setScaleX((float) 0.8);
									ivAns1.setScaleY((float) 0.8);
								}
							} else {
								ivAns1Rotate = true;
								matrix1.postRotate((float) 0, 20, 20);
								ivAns1.setRotation((float) 0);
							}
							
							 
							
							//ivAns2--------------------------------------------
							STEP2 = (int) (STEPX*Math.random());
							STEPY2 = (int) (STEPX*Math.random());
							int ivAns2Angle = (int) (Math.random() * 45);
							Matrix matrix2 = new Matrix();
							if(ivAns2.getX()+dir*STEP2 <0 && dir == -1) {
								dir = 1;
							}
							else if(ivAns2.getX() + ivAns2.getWidth() + dir*STEP2 > scrW && dir == 1) {
								dir = -1;
							}
							ivAns2.setX(ivAns2.getX() + dir*STEP2);
							
							if(ivAns2.getY()+dir*STEPY2 <0 && dir == -1) {
								dir = 1;
							}
							else if(ivAns2.getY() + ivAns2.getHeight()*1. + dir*STEPY2 > scrH && dir == 1) {
								dir = -1;
							}
							ivAns2.setY((ivAns2.getY() + dir*STEPY2));
							
							if (ivAns2Rotate){
								ivAns2Rotate = false;
								int rotateDirection2 = (int) (Math.random() * 2);
								if (rotateDirection2 == 0){
									matrix2.postRotate((float) (-1)*ivAns2Angle, 26, 43);
									ivAns2.setRotation((float) (-1)*ivAns2Angle);
									ivAns2.setScaleX((float) 1.2);
									ivAns2.setScaleY((float) 1.2);
								} else {
									matrix2.postRotate((float) ivAns2Angle, 26, 43);
									ivAns2.setRotation((float) ivAns2Angle);
									ivAns2.setScaleX((float) 0.8);
									ivAns2.setScaleY((float) 0.8);
								}
							} else {
								ivAns2Rotate = true;
								matrix2.postRotate((float) 0, 20, 20);
							}
							
							
							
							//ivAns3-----------------------------------------------
							STEP3 = (int) (STEPX*Math.random());
							STEPY3 = (int) (STEPX*Math.random());
							int ivAns3Angle = (int) (Math.random() * 45);
							Matrix matrix3 = new Matrix();
							if(ivAns3.getX()+dir*STEP3 <0 && dir == -1) {
								dir = 1;
							}
							else if(ivAns3.getX() + ivAns3.getWidth() + dir*STEP3 > scrW && dir == 1) {
								dir = -1;
							}
							ivAns3.setX(ivAns3.getX() + dir*STEP3);
							
							if(ivAns3.getY()+dir*STEPY3 <0 && dir == -1) {
								dir = 1;
							}
							else if(ivAns3.getY() + ivAns3.getHeight()*1. + dir*STEPY3 > scrH && dir == 1) {
								dir = -1;
							}
							ivAns3.setY((ivAns3.getY() + dir*STEPY3));
							
							if (ivAns3Rotate){
								ivAns3Rotate = false;
								int rotateDirection3 = (int) (Math.random() * 2);
								if (rotateDirection3 == 0){
									matrix3.postRotate((float) (-1)*ivAns3Angle, 26, 43);
									ivAns3.setRotation((float) (-1)*ivAns3Angle);
									ivAns3.setScaleX((float) 1.2);
									ivAns3.setScaleY((float) 1.2);
								} else {
									matrix3.postRotate((float) ivAns3Angle, 26, 43);
									ivAns3.setRotation((float) ivAns3Angle);
									ivAns3.setScaleX((float) 0.8);
									ivAns3.setScaleY((float) 0.8);
								}
							} else {
								ivAns3Rotate = true;
								matrix3.postRotate((float) 0, 20, 20);
							}
							
							
							
							//ivAns4-----------------------------------------------
							STEP4 = (int) (STEPX*Math.random());
							STEPY4 = (int) (STEPX*Math.random());
							int ivAns4Angle = (int) (Math.random() * 45);
							Matrix matrix4 = new Matrix();
							if(ivAns4.getX()+dir*STEP4 <0 && dir == -1) {
								dir = 1;
							}
							else if(ivAns4.getX() + ivAns4.getWidth() + dir*STEP4 > scrW && dir == 1) {
								dir = -1;
							}
							ivAns4.setX(ivAns4.getX() + dir*STEP4);
							
							if(ivAns4.getY()+dir*STEPY4 <0 && dir == -1) {
								dir = 1;
							}
							else if(ivAns4.getY() + ivAns4.getHeight()*1. + dir*STEPY4 > scrH && dir == 1) {
								dir = -1;
							}
							ivAns4.setY((ivAns4.getY() + dir*STEPY4)); 
							
							if (ivAns4Rotate){
								ivAns4Rotate = false;
								int rotateDirection4 = (int) (Math.random() * 2);
								if (rotateDirection4 == 0){
									matrix4.postRotate((float) (-1)*ivAns4Angle, 26, 43);
									ivAns4.setRotation((float) (-1)*ivAns4Angle);
									ivAns4.setScaleX((float) 1.2);
									ivAns4.setScaleY((float) 1.2);
								} else {
									matrix4.postRotate((float) ivAns4Angle, 26, 43);
									ivAns4.setRotation((float) ivAns4Angle);
									ivAns4.setScaleX((float) 0.8);
									ivAns4.setScaleY((float) 0.8);
								}
							} else {
								ivAns4Rotate = true;
								matrix4.postRotate((float) 0, 20, 20);
							}
							
						}
						tick++;
					}});
				chronometer.start();
		
	}// moveChrometer
	
	//method 2 ----moveChrometerSlowTrans
	private void moveChrometerSlowTrans(){
		
	  chronometer = (Chronometer) findViewById(R.id.chronometer1);
	  chronometer.setVisibility(View.INVISIBLE);
	  chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
				

					@Override
					public void onChronometerTick(Chronometer chronometer) {
						long time = System.currentTimeMillis();
						String date = new Date(time).toString();
						tick = 1;
 			
						if (tick > 0) {
							// ivAns1--------------------------------------------------
							//Original position***
							int currentX1 = (int) ivAns1.getX();//current positionX
							int currentY1 = (int) ivAns1.getY();//current positionY

							int x1 = (int) (Math.random() * (ivAns1.getWidth() / 3));//incrementX positive
							int y1 = (int) (Math.random() * (ivAns1.getHeight() / 5));//incrementY positive
							int x1_direction = (int) (Math.random() * 2);//incrementX nagetive
							int y1_direction = (int) (Math.random() * 2);//incrementY nagetive
							// decide x direction and adjust boundary
							if (x1_direction == 0) {
								if ((currentX1 - x1) < 0) {
									ivAns1.setX(0);
								} else {
									ivAns1.setX(currentX1 - x1);
								}
							} else {
								if ((currentX1 + x1) > (scrW - ivAns1.getWidth())) {
									ivAns1.setX(currentX1);
								} else {
									ivAns1.setX(currentX1 + x1);
								}
							}
							// decide y direction and adjust boundary
							if (y1_direction == 0) {
								if ((currentY1 - y1) < 0) {
									ivAns1.setY(currentY1);
								} else {
									ivAns1.setY(currentY1 - y1);
								}
							} else {
								if ((currentY1 + y1) > (scrH - ivAns1.getHeight() )) {
									ivAns1.setY(currentY1);
								} else {
									ivAns1.setY(currentY1 + y1);
								}
							}
                          //Second position***altered position---- 
							//check altered position
							currentX1 = (int) ivAns1.getX();
							currentY1 = (int) ivAns1.getY();
							if(D)Log.d (TAG, " scrW  scrH ="+scrW+"  "+scrH);
							if(D)Log.d (TAG, "ivAns1 (x y)="+currentX1+"  "+currentY1);
									
							//random rotate image angle 					
							int ivAns1Angle = (int) (Math.random() * 45);
							Matrix matrix1 = new Matrix();
//							//overlap/combine images
//							Bitmap cc1 = BitmapFactory.decodeResource(getResources(), 
//									R.drawable.border1);
//							Bitmap ss1 = BitmapFactory.decodeResource(getResources(),
//									resQuery[randomNum1]);
//							Bitmap bm1 = combineImages(cc1,ss1);
//							//
					        // random rotate the combined images and resize it	
							if (ivAns1Rotate){
								ivAns1Rotate = false;
								int rotateDirection = (int) (Math.random() * 2);
								if (rotateDirection == 0){
									matrix1.postRotate((float) (-1)*ivAns1Angle, 26, 43);
									ivAns1.setRotation((float) (-1)*ivAns1Angle);
									ivAns1.setScaleX((float) 1.2);
									ivAns1.setScaleY((float) 1.2);
								} else {
									matrix1.postRotate((float) ivAns1Angle, 26, 43);
									ivAns1.setRotation((float) ivAns1Angle);
									ivAns1.setScaleX((float) 0.8);
									ivAns1.setScaleY((float) 0.8);
								}
							} else {
								ivAns1Rotate = true;
								matrix1.postRotate((float) 0, 20, 20);
								ivAns1.setRotation((float) 0);
							}
					
							//ivAns1.setImageMatrix(matrix1);
							//ivAns1.setRotation(ivAns1Angle);
							
							//prepare to rotate combined images
//							int width1 = bm1.getWidth();//bm1=iivAns1
//							int height1 = bm1.getHeight();
							// 设置想要的大小
//							int newWidth1 = cc1.getWidth();
//							int newHeight1 = cc1.getHeight();
//							// 计算缩放比例
//							float scaleWidth1 = ((float) newWidth1) / width1;
//							float scaleHeight1 = ((float) newHeight1) / height1;
//							// 取得想要缩放的matrix参数			
//							matrix1.postScale(scaleWidth1, scaleHeight1);
//				
//						     // recreate the new Bitmap
//					        Bitmap resizedBitmap = Bitmap.createBitmap(bm1, 0, 0, 
//					        		width1, height1, matrix1,true);
//							//
//							ivAns1.setImageBitmap(resizedBitmap);
//							
							// ivAns2--------------------------------------------------
							int currentX2 = (int) ivAns2.getX();
							int currentY2 = (int) ivAns2.getY();
							int x2 = (int) (Math.random() * (ivAns2.getWidth() / 3));
							int y2 = (int) (Math.random() * (ivAns2.getHeight() / 5));
							int x2_direction = (int) (Math.random() * 2);
							int y2_direction = (int) (Math.random() * 2);
							// decide x direction and adjust boundary
							if (x2_direction != 0) {
								if ((currentX2 - x2) < 0) {
									ivAns2.setX(currentX2);
								} else {
									ivAns2.setX(currentX2 - x2);
								}
							} else {
								if ((currentX2 + x2) > (scrW - ivAns2.getWidth())) {
									ivAns2.setX(currentX2);
								} else {
									ivAns2.setX(currentX2 + x2);
								}
							}							
							
							// decide y direction and adjust boundary
							if (y2_direction != 0) {
								if ((currentY2 - y2) < 0) {
									ivAns2.setY(currentY2);
								} else {
									ivAns2.setY(currentY2 - y2);
								}
							} else {
								if ((currentY2 + y2) > (scrH - ivAns2.getHeight())) {
									ivAns2.setY(currentY2);
								} else {
									ivAns2.setY(currentY2 + y2);
								}
							}
							//check altered position
							currentX2 = (int) ivAns2.getX();
							currentY2 = (int) ivAns2.getY();
							if(D)Log.d (TAG, "     2 (x y)="+currentX2+"  "+currentY2);
							//
							//random decide the rotate image angle 					
							int ivAns2Angle = (int) (Math.random() * 45);
							Matrix matrix2 = new Matrix();
//							//overlap/combine images
//							Bitmap cc2 = BitmapFactory.decodeResource(getResources(), 
//									R.drawable.border2);
//							Bitmap ss2 = BitmapFactory.decodeResource(getResources(),
//									resQuery[randomNum2]);
//							Bitmap bm2 = combineImages(cc2,ss2);
//							//
					        // random rotate the combined images and resize it													
							if (ivAns2Rotate){
								ivAns2Rotate = false;
								int rotateDirection2 = (int) (Math.random() * 2);
								if (rotateDirection2 == 0){
									matrix2.postRotate((float) (-1)*ivAns2Angle, 26, 43);
									ivAns2.setRotation((float) (-1)*ivAns2Angle);
									ivAns2.setScaleX((float) 1.2);
									ivAns2.setScaleY((float) 1.2);
								} else {
									matrix2.postRotate((float) ivAns2Angle, 26, 43);
									ivAns2.setRotation((float) ivAns2Angle);
									ivAns2.setScaleX((float) 0.8);
									ivAns2.setScaleY((float) 0.8);
								}
							} else {
								ivAns2Rotate = true;
								matrix2.postRotate((float) 0, 20, 20);
							}
//							//prepare to rotate combined images
//							int width2 = bm2.getWidth();
//							int height2 = bm2.getHeight();
//							// 设置想要的大小
//							int newWidth2 = cc2.getWidth();
//							int newHeight2 = cc2.getHeight();
//							// 计算缩放比例
//							float scaleWidth2 = ((float) newWidth2) / width2;
//							float scaleHeight2 = ((float) newHeight2) / height2;
//							// 取得想要缩放的matrix参数			
//							matrix2.postScale(scaleWidth2, scaleHeight2);
//				
//						     // recreate the new Bitmap
//					        Bitmap resizedBitmap2 = Bitmap.createBitmap(bm2, 0, 0, 
//					        		width2, height2, matrix2,true);
//							//
//							ivAns2.setImageBitmap(resizedBitmap2);
//							
							
							// ivAns3-----------------------------------------------------
							int currentX3 = (int) ivAns3.getX();
							int currentY3 = (int) ivAns3.getY();

							int x3 = (int) (Math.random() * (ivAns3.getWidth() / 3));
							int y3 = (int) (Math.random() * (ivAns3.getHeight() / 5));
							int x3_direction = (int) (Math.random() * 2);
							int y3_direction = (int) (Math.random() * 2);
							// decide x direction and adjust boundary
							if (x3_direction == 0) {
								if ((currentX3 - x3) < 0) {
									ivAns3.setX(currentX3);
								} else {
									ivAns3.setX(currentX3 - x3);
								}
							} else {
								if ((currentX3 + x3) > (scrW - ivAns3.getWidth())) {
									ivAns3.setX(currentX3);
								} else {
									ivAns3.setX(currentX3 + x3);
								}
							}
						
							// decide y direction and adjust boundary
							if (y3_direction == 0) {
								if ((currentY3 - y3) < 0) {
									ivAns3.setY(currentY3);
								} else {
									ivAns3.setY(currentY3 - y3);
								}
							} else {
								if ((currentY3 + y3) > (scrH - ivAns3.getHeight())) {
									ivAns3.setY(currentY3);
								} else {
									ivAns3.setY(currentY3 + y3);
								}
							}
							//check altered position
							currentX3 = (int) ivAns3.getX();
							currentY3 = (int) ivAns3.getY();
							if(D)Log.d (TAG, "     3 (x y)="+currentX3+"  "+currentY3);
							//
							//random decide the rotate image angle 					
							int ivAns3Angle = (int) (Math.random() * 45);
							Matrix matrix3 = new Matrix();
//							//overlap/combine images
//							Bitmap cc3 = BitmapFactory.decodeResource(getResources(), 
//									R.drawable.border3);
//							Bitmap ss3 = BitmapFactory.decodeResource(getResources(),
//									resQuery[randomNum3]);
//							Bitmap bm3 = combineImages(cc3,ss3);
//							//
					        // random rotate the combined images and resize it													
							if (ivAns3Rotate){
								ivAns3Rotate = false;
								int rotateDirection3 = (int) (Math.random() * 2);
								if (rotateDirection3 == 0){
									matrix3.postRotate((float) (-1)*ivAns3Angle, 26, 43);
									ivAns3.setRotation((float) (-1)*ivAns3Angle);
									ivAns3.setScaleX((float) 1.2);
									ivAns3.setScaleY((float) 1.2);
								} else {
									matrix3.postRotate((float) ivAns3Angle, 26, 43);
									ivAns3.setRotation((float) ivAns3Angle);
									ivAns3.setScaleX((float) 0.8);
									ivAns3.setScaleY((float) 0.8);
								}
							} else {
								ivAns3Rotate = true;
								matrix3.postRotate((float) 0, 20, 20);
							}
//							//prepare to rotate combined images
//							int width3 = bm3.getWidth();
//							int height3 = bm3.getHeight();
//							// 设置想要的大小
//							int newWidth3 = cc3.getWidth();
//							int newHeight3 = cc3.getHeight();
//							// 计算缩放比例
//							float scaleWidth3 = ((float) newWidth3) / width3;
//							float scaleHeight3 = ((float) newHeight3) / height3;
//							// 取得想要缩放的matrix参数			
//							matrix3.postScale(scaleWidth3, scaleHeight3);
//				
//						     // recreate the new Bitmap
//					        Bitmap resizedBitmap3 = Bitmap.createBitmap(bm3, 0, 0, 
//					        		width3, height3, matrix3,true);
//							//
//							ivAns3.setImageBitmap(resizedBitmap3);
							//
							// ivAns4-----------------------------------------------------
							int currentX4 = (int) ivAns4.getX();
							int currentY4 = (int) ivAns4.getY();
							int x4 = (int) (Math.random() * (ivAns4.getWidth() / 3));
							int y4 = (int) (Math.random() * (ivAns4.getHeight() / 5));
							int x4_direction = (int) (Math.random() * 2);
							int y4_direction = (int) (Math.random() * 2);
							// decide x direction and adjust boundary
							if (x4_direction != 0) {
								if ((currentX4 - x4) < 0) {
									ivAns4.setX(currentX4);
								} else {
									ivAns4.setX(currentX4 - x4);
								}
							} else {
								if ((currentX4 + x4) > (scrW - ivAns4.getWidth())) {
									ivAns4.setX(currentX4);
								} else {
									ivAns4.setX(currentX4 + x4);
								}
							}
							// decide y direction and adjust boundary
							if (y4_direction != 0) {
								if ((currentY4 - y4) < 0) {
									ivAns4.setY(currentY4);
								} else {
									ivAns4.setY(currentY4 - y4);
								}
							} else {
								if ((currentY4 + y4) > (scrH - ivAns4.getHeight())) {
									ivAns4.setY(currentY4);
								} else {
									ivAns4.setY(currentY4 + y4);
								}
							}
							//check altered position
							currentX4 = (int) ivAns4.getX();
							currentY4 = (int) ivAns4.getY();
							if(D)Log.d (TAG, "     4 (x y)="+currentX4+"  "+currentY4);
							if(D)Log.d(TAG," ");
							//
							//random decide the rotate image angle 					
							int ivAns4Angle = (int) (Math.random() * 45);
							Matrix matrix4 = new Matrix();
//							//overlap/combine images
//							Bitmap cc4 = BitmapFactory.decodeResource(getResources(), 
//									R.drawable.border4);
//							Bitmap ss4 = BitmapFactory.decodeResource(getResources(),
//									resQuery[randomNum4]);
//							Bitmap bm4 = combineImages(cc4,ss4);
//							//
					        // random rotate the combined images and resize it	
							if (ivAns4Rotate){
								ivAns4Rotate = false;
								int rotateDirection4 = (int) (Math.random() * 2);
								if (rotateDirection4 == 0){
									matrix4.postRotate((float) (-1)*ivAns4Angle, 26, 43);
									ivAns4.setRotation((float) (-1)*ivAns4Angle);
									ivAns4.setScaleX((float) 1.2);
									ivAns4.setScaleY((float) 1.2);
								} else {
									matrix4.postRotate((float) ivAns4Angle, 26, 43);
									ivAns4.setRotation((float) ivAns4Angle);
									ivAns4.setScaleX((float) 0.8);
									ivAns4.setScaleY((float) 0.8);
								}
							} else {
								ivAns4Rotate = true;
								matrix4.postRotate((float) 0, 20, 20);
							}
//							//prepare to rotate combined images
//							int width4 = bm4.getWidth();
//							int height4 = bm4.getHeight();
//							// 设置想要的大小
//							int newWidth4 = cc4.getWidth();
//							int newHeight4 = cc4.getHeight();
//							// 计算缩放比例
//							float scaleWidth4 = ((float) newWidth4) / width4;
//							float scaleHeight4 = ((float) newHeight4) / height4;
//							// 取得想要缩放的matrix参数			
//							matrix4.postScale(scaleWidth4, scaleHeight4);
//				
//						     // recreate the new Bitmap
//					        Bitmap resizedBitmap4 = Bitmap.createBitmap(bm4, 0, 0, 
//					        		width4, height4, matrix4,true);
//							//
//							ivAns4.setImageBitmap(resizedBitmap4);
//							
						}//if (tick > 0)
						tick++;
					}
				});
	  chronometer.start();
		
	}//moveChrometerSlowTrans()
	
	
	//method 3 ----moveChrometerSlowTransBloder
		private void moveChrometerSlowTransBloder(){
			
			chronometer = (Chronometer) findViewById(R.id.chronometer1);
			  chronometer.setVisibility(View.INVISIBLE);
			  chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
						

							@Override
							public void onChronometerTick(Chronometer chronometer) {
								long time = System.currentTimeMillis();
								String date = new Date(time).toString();
								tick = 1;
		 			
								if (tick > 0) {
									// ivAns1--------------------------------------------------
									//Original position***
									int currentX1 = (int) ivAns1.getX();//current positionX
									int currentY1 = (int) ivAns1.getY();//current positionY

									int x1 = (int) (Math.random() * (ivAns1.getWidth() / 3));//incrementX positive
									int y1 = (int) (Math.random() * (ivAns1.getHeight() / 5));//incrementY positive
									int x1_direction = (int) (Math.random() * 2);//incrementX nagetive
									int y1_direction = (int) (Math.random() * 2);//incrementY nagetive
									// decide x direction and adjust boundary
									if (x1_direction == 0) {
										if ((currentX1 - x1) < 0) {
											ivAns1.setX(0);
										} else {
											ivAns1.setX(currentX1 - x1);
										}
									} else {
										if ((currentX1 + x1) > (scrW - ivAns1.getWidth())) {
											ivAns1.setX(currentX1);
										} else {
											ivAns1.setX(currentX1 + x1);
										}
									}
									// decide y direction and adjust boundary
									if (y1_direction == 0) {
										if ((currentY1 - y1) < 0) {
											ivAns1.setY(currentY1);
										} else {
											ivAns1.setY(currentY1 - y1);
										}
									} else {
										if ((currentY1 + y1) > (scrH - ivAns1.getHeight() )) {
											ivAns1.setY(currentY1);
										} else {
											ivAns1.setY(currentY1 + y1);
										}
									}
		                          //Second position***altered position---- 
									//check altered position
									currentX1 = (int) ivAns1.getX();
									currentY1 = (int) ivAns1.getY();
									if(D)Log.d (TAG, " scrW  scrH ="+scrW+"  "+scrH);
									if(D)Log.d (TAG, "ivAns1 (x y)="+currentX1+"  "+currentY1);
											
									//random rotate image angle 					
									int ivAns1Angle = (int) (Math.random() * 45);
									Matrix matrix1 = new Matrix();
									//overlap/combine images
//									Bitmap cc1 = BitmapFactory.decodeResource(getResources(), 
//											R.drawable.bd1);
//									Bitmap ss1 = BitmapFactory.decodeResource(getResources(),
//											resQuery[randomNum1]);
//									Bitmap bm1 = combineImages(cc1,ss1);
//									//
							        // random rotate the combined images and resize it	
									if (ivAns1Rotate){
										ivAns1Rotate = false;
										int rotateDirection = (int) (Math.random() * 2);
										if (rotateDirection == 0){
											matrix1.postRotate((float) (-1)*ivAns1Angle, 26, 43);
											ivAns1.setRotation((float) (-1)*ivAns1Angle);
											ivAns1.setScaleX((float) 1.2);
											ivAns1.setScaleY((float) 1.2);
										} else {
											matrix1.postRotate((float) ivAns1Angle, 26, 43);
											ivAns1.setRotation((float) ivAns1Angle);
											ivAns1.setScaleX((float) 0.8);
											ivAns1.setScaleY((float) 0.8);
										}
									} else {
										ivAns1Rotate = true;
										matrix1.postRotate((float) 0, 20, 20);
										ivAns1.setRotation((float) 0);
									}
							
									 
									 
									// ivAns2--------------------------------------------------
									int currentX2 = (int) ivAns2.getX();
									int currentY2 = (int) ivAns2.getY();
									int x2 = (int) (Math.random() * (ivAns2.getWidth() / 3));
									int y2 = (int) (Math.random() * (ivAns2.getHeight() / 5));
									int x2_direction = (int) (Math.random() * 2);
									int y2_direction = (int) (Math.random() * 2);
									// decide x direction and adjust boundary
									if (x2_direction != 0) {
										if ((currentX2 - x2) < 0) {
											ivAns2.setX(currentX2);
										} else {
											ivAns2.setX(currentX2 - x2);
										}
									} else {
										if ((currentX2 + x2) > (scrW - ivAns2.getWidth())) {
											ivAns2.setX(currentX2);
										} else {
											ivAns2.setX(currentX2 + x2);
										}
									}							
									
									// decide y direction and adjust boundary
									if (y2_direction != 0) {
										if ((currentY2 - y2) < 0) {
											ivAns2.setY(currentY2);
										} else {
											ivAns2.setY(currentY2 - y2);
										}
									} else {
										if ((currentY2 + y2) > (scrH - ivAns2.getHeight())) {
											ivAns2.setY(currentY2);
										} else {
											ivAns2.setY(currentY2 + y2);
										}
									}
									//check altered position
									currentX2 = (int) ivAns2.getX();
									currentY2 = (int) ivAns2.getY();
									if(D)Log.d (TAG, "     2 (x y)="+currentX2+"  "+currentY2);
									//
									//random decide the rotate image angle 					
									int ivAns2Angle = (int) (Math.random() * 45);
									Matrix matrix2 = new Matrix();
//									//overlap/combine images
//									Bitmap cc2 = BitmapFactory.decodeResource(getResources(), 
//											R.drawable.border2);
//									Bitmap ss2 = BitmapFactory.decodeResource(getResources(),
//											resQuery[randomNum2]);
//									Bitmap bm2 = combineImages(cc2,ss2);
//									//
							        // random rotate the combined images and resize it													
									if (ivAns2Rotate){
										ivAns2Rotate = false;
										int rotateDirection2 = (int) (Math.random() * 2);
										if (rotateDirection2 == 0){
											matrix2.postRotate((float) (-1)*ivAns2Angle, 26, 43);
											ivAns2.setRotation((float) (-1)*ivAns2Angle);
											ivAns2.setScaleX((float) 1.2);
											ivAns2.setScaleY((float) 1.2);
										} else {
											matrix2.postRotate((float) ivAns2Angle, 26, 43);
											ivAns2.setRotation((float) ivAns2Angle);
											ivAns2.setScaleX((float) 0.8);
											ivAns2.setScaleY((float) 0.8);
										}
									} else {
										ivAns2Rotate = true;
										matrix2.postRotate((float) 0, 20, 20);
									}
//									//prepare to rotate combined images
//									int width2 = bm2.getWidth();
//									int height2 = bm2.getHeight();
//									// 设置想要的大小
//									int newWidth2 = cc2.getWidth();
//									int newHeight2 = cc2.getHeight();
//									// 计算缩放比例
//									float scaleWidth2 = ((float) newWidth2) / width2;
//									float scaleHeight2 = ((float) newHeight2) / height2;
//									// 取得想要缩放的matrix参数			
//									matrix2.postScale(scaleWidth2, scaleHeight2);
//						
//								     // recreate the new Bitmap
//							        Bitmap resizedBitmap2 = Bitmap.createBitmap(bm2, 0, 0, 
//							        		width2, height2, matrix2,true);
//									//
//									ivAns2.setImageBitmap(resizedBitmap2);
//									
									
									// ivAns3-----------------------------------------------------
									int currentX3 = (int) ivAns3.getX();
									int currentY3 = (int) ivAns3.getY();

									int x3 = (int) (Math.random() * (ivAns3.getWidth() / 3));
									int y3 = (int) (Math.random() * (ivAns3.getHeight() / 5));
									int x3_direction = (int) (Math.random() * 2);
									int y3_direction = (int) (Math.random() * 2);
									// decide x direction and adjust boundary
									if (x3_direction == 0) {
										if ((currentX3 - x3) < 0) {
											ivAns3.setX(currentX3);
										} else {
											ivAns3.setX(currentX3 - x3);
										}
									} else {
										if ((currentX3 + x3) > (scrW - ivAns3.getWidth())) {
											ivAns3.setX(currentX3);
										} else {
											ivAns3.setX(currentX3 + x3);
										}
									}
								
									// decide y direction and adjust boundary
									if (y3_direction == 0) {
										if ((currentY3 - y3) < 0) {
											ivAns3.setY(currentY3);
										} else {
											ivAns3.setY(currentY3 - y3);
										}
									} else {
										if ((currentY3 + y3) > (scrH - ivAns3.getHeight())) {
											ivAns3.setY(currentY3);
										} else {
											ivAns3.setY(currentY3 + y3);
										}
									}
									//check altered position
									currentX3 = (int) ivAns3.getX();
									currentY3 = (int) ivAns3.getY();
									if(D)Log.d (TAG, "     3 (x y)="+currentX3+"  "+currentY3);
									//
									//random decide the rotate image angle 					
									int ivAns3Angle = (int) (Math.random() * 45);
									Matrix matrix3 = new Matrix();
//									//overlap/combine images
//									Bitmap cc3 = BitmapFactory.decodeResource(getResources(), 
//											R.drawable.border3);
//									Bitmap ss3 = BitmapFactory.decodeResource(getResources(),
//											resQuery[randomNum3]);
//									Bitmap bm3 = combineImages(cc3,ss3);
//									//
							        // random rotate the combined images and resize it													
									if (ivAns3Rotate){
										ivAns3Rotate = false;
										int rotateDirection3 = (int) (Math.random() * 2);
										if (rotateDirection3 == 0){
											matrix3.postRotate((float) (-1)*ivAns3Angle, 26, 43);
											ivAns3.setRotation((float) (-1)*ivAns3Angle);
											ivAns3.setScaleX((float) 1.2);
											ivAns3.setScaleY((float) 1.2);
										} else {
											matrix3.postRotate((float) ivAns3Angle, 26, 43);
											ivAns3.setRotation((float) ivAns3Angle);
											ivAns3.setScaleX((float) 0.8);
											ivAns3.setScaleY((float) 0.8);
										}
									} else {
										ivAns3Rotate = true;
										matrix3.postRotate((float) 0, 20, 20);
									}
//									//prepare to rotate combined images
//									int width3 = bm3.getWidth();
//									int height3 = bm3.getHeight();
//									// 设置想要的大小
//									int newWidth3 = cc3.getWidth();
//									int newHeight3 = cc3.getHeight();
//									// 计算缩放比例
//									float scaleWidth3 = ((float) newWidth3) / width3;
//									float scaleHeight3 = ((float) newHeight3) / height3;
//									// 取得想要缩放的matrix参数			
//									matrix3.postScale(scaleWidth3, scaleHeight3);
//						
//								     // recreate the new Bitmap
//							        Bitmap resizedBitmap3 = Bitmap.createBitmap(bm3, 0, 0, 
//							        		width3, height3, matrix3,true);
//									//
//									ivAns3.setImageBitmap(resizedBitmap3);
									//
									// ivAns4-----------------------------------------------------
									int currentX4 = (int) ivAns4.getX();
									int currentY4 = (int) ivAns4.getY();
									int x4 = (int) (Math.random() * (ivAns4.getWidth() / 3));
									int y4 = (int) (Math.random() * (ivAns4.getHeight() / 5));
									int x4_direction = (int) (Math.random() * 2);
									int y4_direction = (int) (Math.random() * 2);
									// decide x direction and adjust boundary
									if (x4_direction != 0) {
										if ((currentX4 - x4) < 0) {
											ivAns4.setX(currentX4);
										} else {
											ivAns4.setX(currentX4 - x4);
										}
									} else {
										if ((currentX4 + x4) > (scrW - ivAns4.getWidth())) {
											ivAns4.setX(currentX4);
										} else {
											ivAns4.setX(currentX4 + x4);
										}
									}
									// decide y direction and adjust boundary
									if (y4_direction != 0) {
										if ((currentY4 - y4) < 0) {
											ivAns4.setY(currentY4);
										} else {
											ivAns4.setY(currentY4 - y4);
										}
									} else {
										if ((currentY4 + y4) > (scrH - ivAns4.getHeight())) {
											ivAns4.setY(currentY4);
										} else {
											ivAns4.setY(currentY4 + y4);
										}
									}
									//check altered position
									currentX4 = (int) ivAns4.getX();
									currentY4 = (int) ivAns4.getY();
									if(D)Log.d (TAG, "     4 (x y)="+currentX4+"  "+currentY4);
									if(D)Log.d(TAG," ");
									//
									//random decide the rotate image angle 					
									int ivAns4Angle = (int) (Math.random() * 45);
									Matrix matrix4 = new Matrix();
//									//overlap/combine images
//									Bitmap cc4 = BitmapFactory.decodeResource(getResources(), 
//											R.drawable.border4);
//									Bitmap ss4 = BitmapFactory.decodeResource(getResources(),
//											resQuery[randomNum4]);
//									Bitmap bm4 = combineImages(cc4,ss4);
//									//
							        // random rotate the combined images and resize it	
									if (ivAns4Rotate){
										ivAns4Rotate = false;
										int rotateDirection4 = (int) (Math.random() * 2);
										if (rotateDirection4 == 0){
											matrix4.postRotate((float) (-1)*ivAns4Angle, 26, 43);
											ivAns4.setRotation((float) (-1)*ivAns4Angle);
											ivAns4.setScaleX((float) 1.2);
											ivAns4.setScaleY((float) 1.2);
										} else {
											matrix4.postRotate((float) ivAns4Angle, 26, 43);
											ivAns4.setRotation((float) ivAns4Angle);
											ivAns4.setScaleX((float) 0.8);
											ivAns4.setScaleY((float) 0.8);
										}
									} else {
										ivAns4Rotate = true;
										matrix4.postRotate((float) 0, 20, 20);
									}
//									//prepare to rotate combined images
//									int width4 = bm4.getWidth();
//									int height4 = bm4.getHeight();
//									// 设置想要的大小
//									int newWidth4 = cc4.getWidth();
//									int newHeight4 = cc4.getHeight();
//									// 计算缩放比例
//									float scaleWidth4 = ((float) newWidth4) / width4;
//									float scaleHeight4 = ((float) newHeight4) / height4;
//									// 取得想要缩放的matrix参数			
//									matrix4.postScale(scaleWidth4, scaleHeight4);
//						
//								     // recreate the new Bitmap
//							        Bitmap resizedBitmap4 = Bitmap.createBitmap(bm4, 0, 0, 
//							        		width4, height4, matrix4,true);
//									//
//									ivAns4.setImageBitmap(resizedBitmap4);
//									
								}//if (tick > 0)
								tick++;
							}
						});
			  chronometer.start();
				
			}//moveChrometerSlowTransBloder()
	

	//method 4 ----moveThead
		
		private void move1(){
	 
			//ivAns1-------------------------------------
			STEP = (int) (STEPX*Math.random());
			STEPY = (int) (STEPX*Math.random());
			int ivAns1Angle = (int) (Math.random() * 45);
			Matrix matrix1 = new Matrix(); 
			if(ivAns1.getX()+dir*STEP <0 && dir == -1) {
				dir = 1;
			}
			else if(ivAns1.getX() + ivAns1.getWidth() + dir*STEP > scrW && dir == 1) {
				dir = -1;
			}
			ivAns1.setX(ivAns1.getX() + dir*STEP);
			
			if(ivAns1.getY()+dir*STEPY <0 && dir == -1) {
				dir = 1;
			}
			else if(ivAns1.getY() + ivAns1.getHeight()*1. + dir*STEPY > scrH && dir == 1) {
				dir = -1;
			}
			ivAns1.setY((ivAns1.getY() + dir*STEPY));
			
			if (ivAns1Rotate){
				ivAns1Rotate = false;
				int rotateDirection = (int) (Math.random() * 2);
				if (rotateDirection == 0){
					matrix1.postRotate((float) (-1)*ivAns1Angle, 26, 43);
					ivAns1.setRotation((float) (-1)*ivAns1Angle);
					ivAns1.setScaleX((float) 1.2);
					ivAns1.setScaleY((float) 1.2);
				} else {
					matrix1.postRotate((float) ivAns1Angle, 26, 43);
					ivAns1.setRotation((float) ivAns1Angle);
					ivAns1.setScaleX((float) 0.8);
					ivAns1.setScaleY((float) 0.8);
				}
			} else {
				ivAns1Rotate = true;
				matrix1.postRotate((float) 0, 20, 20);
				ivAns1.setRotation((float) 0);
			}
			ivAns1.invalidate();//display reflash 			
			//ivAns2--------------------------------------------
			STEP2 = (int) (STEPX*Math.random());
			STEPY2 = (int) (STEPX*Math.random());
			int ivAns2Angle = (int) (Math.random() * 45);
			Matrix matrix2 = new Matrix();
			if(ivAns2.getX()+dir*STEP2 <0 && dir == -1) {
				dir = 1;
			}
			else if(ivAns2.getX() + ivAns2.getWidth() + dir*STEP2 > scrW && dir == 1) {
				dir = -1;
			}
			ivAns2.setX(ivAns2.getX() + dir*STEP2);
			
			if(ivAns2.getY()+dir*STEPY2 <0 && dir == -1) {
				dir = 1;
			}
			else if(ivAns2.getY() + ivAns2.getHeight()*1. + dir*STEPY2 > scrH && dir == 1) {
				dir = -1;
			}
			ivAns2.setY((ivAns2.getY() + dir*STEPY2));
			
			if (ivAns2Rotate){
				ivAns2Rotate = false;
				int rotateDirection2 = (int) (Math.random() * 2);
				if (rotateDirection2 == 0){
					matrix2.postRotate((float) (-1)*ivAns2Angle, 26, 43);
					ivAns2.setRotation((float) (-1)*ivAns2Angle);
					ivAns2.setScaleX((float) 1.2);
					ivAns2.setScaleY((float) 1.2);
				} else {
					matrix2.postRotate((float) ivAns2Angle, 26, 43);
					ivAns2.setRotation((float) ivAns2Angle);
					ivAns2.setScaleX((float) 0.8);
					ivAns2.setScaleY((float) 0.8);
				}
			} else {
				ivAns2Rotate = true;
				matrix2.postRotate((float) 0, 20, 20);
			}
			ivAns2.invalidate();//display reflash						
			//ivAns3-----------------------------------------------
			STEP3 = (int) (STEPX*Math.random());
			STEPY3 = (int) (STEPX*Math.random());
			int ivAns3Angle = (int) (Math.random() * 45);
			Matrix matrix3 = new Matrix();
			if(ivAns3.getX()+dir*STEP3 <0 && dir == -1) {
				dir = 1;
			}
			else if(ivAns3.getX() + ivAns3.getWidth() + dir*STEP3 > scrW && dir == 1) {
				dir = -1;
			}
			ivAns3.setX(ivAns3.getX() + dir*STEP3);
			
			if(ivAns3.getY()+dir*STEPY3 <0 && dir == -1) {
				dir = 1;
			}
			else if(ivAns3.getY() + ivAns3.getHeight()*1. + dir*STEPY3 > scrH && dir == 1) {
				dir = -1;
			}
			ivAns3.setY((ivAns3.getY() + dir*STEPY3));
			
			if (ivAns3Rotate){
				ivAns3Rotate = false;
				int rotateDirection3 = (int) (Math.random() * 2);
				if (rotateDirection3 == 0){
					matrix3.postRotate((float) (-1)*ivAns3Angle, 26, 43);
					ivAns3.setRotation((float) (-1)*ivAns3Angle);
					ivAns3.setScaleX((float) 1.2);
					ivAns3.setScaleY((float) 1.2);
				} else {
					matrix3.postRotate((float) ivAns3Angle, 26, 43);
					ivAns3.setRotation((float) ivAns3Angle);
					ivAns3.setScaleX((float) 0.8);
					ivAns3.setScaleY((float) 0.8);
				}
			} else {
				ivAns3Rotate = true;
				matrix3.postRotate((float) 0, 20, 20);
			}
			ivAns3.invalidate();//display reflash
			//ivAns4-----------------------------------------------
			STEP4 = (int) (STEPX*Math.random());
			STEPY4 = (int) (STEPX*Math.random());
			int ivAns4Angle = (int) (Math.random() * 45);
			Matrix matrix4 = new Matrix();
			if(ivAns4.getX()+dir*STEP4 <0 && dir == -1) {
				dir = 1;
			}
			else if(ivAns4.getX() + ivAns4.getWidth() + dir*STEP4 > scrW && dir == 1) {
				dir = -1;
			}
			ivAns4.setX(ivAns4.getX() + dir*STEP4);
			
			if(ivAns4.getY()+dir*STEPY4 <0 && dir == -1) {
				dir = 1;
			}
			else if(ivAns4.getY() + ivAns4.getHeight()*1. + dir*STEPY4 > scrH && dir == 1) {
				dir = -1;
			}
			ivAns4.setY((ivAns4.getY() + dir*STEPY4)); 
			
			if (ivAns4Rotate){
				ivAns4Rotate = false;
				int rotateDirection4 = (int) (Math.random() * 2);
				if (rotateDirection4 == 0){
					matrix4.postRotate((float) (-1)*ivAns4Angle, 26, 43);
					ivAns4.setRotation((float) (-1)*ivAns4Angle);
					ivAns4.setScaleX((float) 1.2);
					ivAns4.setScaleY((float) 1.2);
				} else {
					matrix4.postRotate((float) ivAns4Angle, 26, 43);
					ivAns4.setRotation((float) ivAns4Angle);
					ivAns4.setScaleX((float) 0.8);
					ivAns4.setScaleY((float) 0.8);
				}
			} else {
				ivAns4Rotate = true;
				matrix4.postRotate((float) 0, 20, 20);
			}						 
			ivAns4.invalidate();//display reflash
			
		}//move1()
		
	
		private void move2(){
			
			  
			// ivAns1--------------------------------------------------
			//Original position***
			int currentX1 = (int) ivAns1.getX();//current positionX
			int currentY1 = (int) ivAns1.getY();//current positionY

			int x1 = (int) (Math.random() * (ivAns1.getWidth() / 3));//incrementX positive
			int y1 = (int) (Math.random() * (ivAns1.getHeight() / 5));//incrementY positive
			int x1_direction = (int) (Math.random() * 2);//incrementX nagetive
			int y1_direction = (int) (Math.random() * 2);//incrementY nagetive
			// decide x direction and adjust boundary
			if (x1_direction == 0) {
				if ((currentX1 - x1) < 0) {
					ivAns1.setX(0);
				} else {
					ivAns1.setX(currentX1 - x1);
				}
			} else {
				if ((currentX1 + x1) > (scrW - ivAns1.getWidth())) {
					ivAns1.setX(currentX1);
				} else {
					ivAns1.setX(currentX1 + x1);
				}
			}
			// decide y direction and adjust boundary
			if (y1_direction == 0) {
				if ((currentY1 - y1) < 0) {
					ivAns1.setY(currentY1);
				} else {
					ivAns1.setY(currentY1 - y1);
				}
			} else {
				if ((currentY1 + y1) > (scrH - ivAns1.getHeight() )) {
					ivAns1.setY(currentY1);
				} else {
					ivAns1.setY(currentY1 + y1);
				}
			}
            //Second position***altered position---- 
			//check altered position
			currentX1 = (int) ivAns1.getX();
			currentY1 = (int) ivAns1.getY();
			if(D)Log.d (TAG, " scrW  scrH ="+scrW+"  "+scrH);
			if(D)Log.d (TAG, "ivAns1 (x y)="+currentX1+"  "+currentY1);
					
			//random rotate image angle 					
			int ivAns1Angle = (int) (Math.random() * 45);
			Matrix matrix1 = new Matrix();
 
			if (ivAns1Rotate){
				ivAns1Rotate = false;
				int rotateDirection = (int) (Math.random() * 2);
				if (rotateDirection == 0){
					matrix1.postRotate((float) (-1)*ivAns1Angle, 26, 43);
					ivAns1.setRotation((float) (-1)*ivAns1Angle);
					ivAns1.setScaleX((float) 1.2);
					ivAns1.setScaleY((float) 1.2);
				} else {
					matrix1.postRotate((float) ivAns1Angle, 26, 43);
					ivAns1.setRotation((float) ivAns1Angle);
					ivAns1.setScaleX((float) 0.8);
					ivAns1.setScaleY((float) 0.8);
				}
			} else {
				ivAns1Rotate = true;
				matrix1.postRotate((float) 0, 20, 20);
				ivAns1.setRotation((float) 0);
			}
			ivAns1.invalidate();//display reflash
			// ivAns2--------------------------------------------------
			int currentX2 = (int) ivAns2.getX();
			int currentY2 = (int) ivAns2.getY();
			int x2 = (int) (Math.random() * (ivAns2.getWidth() / 3));
			int y2 = (int) (Math.random() * (ivAns2.getHeight() / 5));
			int x2_direction = (int) (Math.random() * 2);
			int y2_direction = (int) (Math.random() * 2);
			// decide x direction and adjust boundary
			if (x2_direction != 0) {
				if ((currentX2 - x2) < 0) {
					ivAns2.setX(currentX2);
				} else {
					ivAns2.setX(currentX2 - x2);
				}
			} else {
				if ((currentX2 + x2) > (scrW - ivAns2.getWidth())) {
					ivAns2.setX(currentX2);
				} else {
					ivAns2.setX(currentX2 + x2);
				}
			}							
			
			// decide y direction and adjust boundary
			if (y2_direction != 0) {
				if ((currentY2 - y2) < 0) {
					ivAns2.setY(currentY2);
				} else {
					ivAns2.setY(currentY2 - y2);
				}
			} else {
				if ((currentY2 + y2) > (scrH - ivAns2.getHeight())) {
					ivAns2.setY(currentY2);
				} else {
					ivAns2.setY(currentY2 + y2);
				}
			}
			//check altered position
			currentX2 = (int) ivAns2.getX();
			currentY2 = (int) ivAns2.getY();
			if(D)Log.d (TAG, "     2 (x y)="+currentX2+"  "+currentY2);
			//
			//random decide the rotate image angle 					
			int ivAns2Angle = (int) (Math.random() * 45);
			Matrix matrix2 = new Matrix();
 												
			if (ivAns2Rotate){
				ivAns2Rotate = false;
				int rotateDirection2 = (int) (Math.random() * 2);
				if (rotateDirection2 == 0){
					matrix2.postRotate((float) (-1)*ivAns2Angle, 26, 43);
					ivAns2.setRotation((float) (-1)*ivAns2Angle);
					ivAns2.setScaleX((float) 1.2);
					ivAns2.setScaleY((float) 1.2);
				} else {
					matrix2.postRotate((float) ivAns2Angle, 26, 43);
					ivAns2.setRotation((float) ivAns2Angle);
					ivAns2.setScaleX((float) 0.8);
					ivAns2.setScaleY((float) 0.8);
				}
			} else {
				ivAns2Rotate = true;
				matrix2.postRotate((float) 0, 20, 20);
			}									 
			ivAns2.invalidate();//display reflash
			// ivAns3-----------------------------------------------------
			int currentX3 = (int) ivAns3.getX();
			int currentY3 = (int) ivAns3.getY();

			int x3 = (int) (Math.random() * (ivAns3.getWidth() / 3));
			int y3 = (int) (Math.random() * (ivAns3.getHeight() / 5));
			int x3_direction = (int) (Math.random() * 2);
			int y3_direction = (int) (Math.random() * 2);
			// decide x direction and adjust boundary
			if (x3_direction == 0) {
				if ((currentX3 - x3) < 0) {
					ivAns3.setX(currentX3);
				} else {
					ivAns3.setX(currentX3 - x3);
				}
			} else {
				if ((currentX3 + x3) > (scrW - ivAns3.getWidth())) {
					ivAns3.setX(currentX3);
				} else {
					ivAns3.setX(currentX3 + x3);
				}
			}
		
			// decide y direction and adjust boundary
			if (y3_direction == 0) {
				if ((currentY3 - y3) < 0) {
					ivAns3.setY(currentY3);
				} else {
					ivAns3.setY(currentY3 - y3);
				}
			} else {
				if ((currentY3 + y3) > (scrH - ivAns3.getHeight())) {
					ivAns3.setY(currentY3);
				} else {
					ivAns3.setY(currentY3 + y3);
				}
			}
			//check altered position
			currentX3 = (int) ivAns3.getX();
			currentY3 = (int) ivAns3.getY();
			if(D)Log.d (TAG, "     3 (x y)="+currentX3+"  "+currentY3);
			//
			//random decide the rotate image angle 					
			int ivAns3Angle = (int) (Math.random() * 45);
			Matrix matrix3 = new Matrix();
 												
			if (ivAns3Rotate){
				ivAns3Rotate = false;
				int rotateDirection3 = (int) (Math.random() * 2);
				if (rotateDirection3 == 0){
					matrix3.postRotate((float) (-1)*ivAns3Angle, 26, 43);
					ivAns3.setRotation((float) (-1)*ivAns3Angle);
					ivAns3.setScaleX((float) 1.2);
					ivAns3.setScaleY((float) 1.2);
				} else {
					matrix3.postRotate((float) ivAns3Angle, 26, 43);
					ivAns3.setRotation((float) ivAns3Angle);
					ivAns3.setScaleX((float) 0.8);
					ivAns3.setScaleY((float) 0.8);
				}
			} else {
				ivAns3Rotate = true;
				matrix3.postRotate((float) 0, 20, 20);
			}
			ivAns3.invalidate();//display reflash 
			// ivAns4-----------------------------------------------------
			int currentX4 = (int) ivAns4.getX();
			int currentY4 = (int) ivAns4.getY();
			int x4 = (int) (Math.random() * (ivAns4.getWidth() / 3));
			int y4 = (int) (Math.random() * (ivAns4.getHeight() / 5));
			int x4_direction = (int) (Math.random() * 2);
			int y4_direction = (int) (Math.random() * 2);
			// decide x direction and adjust boundary
			if (x4_direction != 0) {
				if ((currentX4 - x4) < 0) {
					ivAns4.setX(currentX4);
				} else {
					ivAns4.setX(currentX4 - x4);
				}
			} else {
				if ((currentX4 + x4) > (scrW - ivAns4.getWidth())) {
					ivAns4.setX(currentX4);
				} else {
					ivAns4.setX(currentX4 + x4);
				}
			}
			// decide y direction and adjust boundary
			if (y4_direction != 0) {
				if ((currentY4 - y4) < 0) {
					ivAns4.setY(currentY4);
				} else {
					ivAns4.setY(currentY4 - y4);
				}
			} else {
				if ((currentY4 + y4) > (scrH - ivAns4.getHeight())) {
					ivAns4.setY(currentY4);
				} else {
					ivAns4.setY(currentY4 + y4);
				}
			}
			//check altered position
			currentX4 = (int) ivAns4.getX();
			currentY4 = (int) ivAns4.getY();
			if(D)Log.d (TAG, "     4 (x y)="+currentX4+"  "+currentY4);
			if(D)Log.d(TAG," ");
			//
			//random decide the rotate image angle 					
			int ivAns4Angle = (int) (Math.random() * 45);
			Matrix matrix4 = new Matrix();
 	
			if (ivAns4Rotate){
				ivAns4Rotate = false;
				int rotateDirection4 = (int) (Math.random() * 2);
				if (rotateDirection4 == 0){
					matrix4.postRotate((float) (-1)*ivAns4Angle, 26, 43);
					ivAns4.setRotation((float) (-1)*ivAns4Angle);
					ivAns4.setScaleX((float) 1.2);
					ivAns4.setScaleY((float) 1.2);
				} else {
					matrix4.postRotate((float) ivAns4Angle, 26, 43);
					ivAns4.setRotation((float) ivAns4Angle);
					ivAns4.setScaleX((float) 0.8);
					ivAns4.setScaleY((float) 0.8);
				}
			} else {
				ivAns4Rotate = true;
				matrix4.postRotate((float) 0, 20, 20);
			}
			ivAns4.invalidate();//display reflash
			
		}//move2()
			
		private void move3() {
			// ivAns1--------------------------------------------------
			//movingx screen shift = 240
			int imageY1 = (int) ivAns1.getY();
			int ivAns1Angle = (int) (Math.random() * 45);
			Matrix matrix1 = new Matrix();
			
			if(bNorth1 && ivAns1.getY() - ivAns1.getHeight() / 2 +240< 0)			 
				bNorth1 = false;
			else if(!bNorth1 && ivAns1.getY() + ivAns1.getHeight() / 2 +240> scrH)			 
				bNorth1 = true;
			
			if(bNorth1)
				imageY1-=18;
			else
				imageY1+=10;
			ivAns1.setY(imageY1);
			
			//movingx screen shift = 100
			int imageX1 = (int) ivAns1.getX();
			if(bLeft1 && ivAns1.getX() - ivAns1.getWidth() / 2 +100< 0)
				bLeft1 = false;
			else if(!bLeft1 && ivAns1.getX() + ivAns1.getWidth() / 2 +100> scrW)
				bLeft1 = true;
			
			if(bLeft1)
				imageX1-=12;
			else
				imageX1+=14;			
			ivAns1.setX(imageX1);
			
			if (ivAns1Rotate){
				ivAns1Rotate = false;
				int rotateDirection = (int) (Math.random() * 2);
				if (rotateDirection == 0){
					matrix1.postRotate((float) (-1)*ivAns1Angle, 26, 43);
					ivAns1.setRotation((float) (-1)*ivAns1Angle);
					ivAns1.setScaleX((float) 1.2);
					ivAns1.setScaleY((float) 1.2);
				} else {
					matrix1.postRotate((float) ivAns1Angle, 26, 43);
					ivAns1.setRotation((float) ivAns1Angle);
					ivAns1.setScaleX((float) 0.8);
					ivAns1.setScaleY((float) 0.8);
				}
			} else {
				ivAns1Rotate = true;
				matrix1.postRotate((float) 0, 20, 20);
				ivAns1.setRotation((float) 0);
			}
			ivAns1.invalidate();
			// ivAns2--------------------------------------------------
			//movingx screen shift = 240
			int imageY2 = (int) ivAns2.getY();
			int ivAns2Angle = (int) (Math.random() * 45);
			Matrix matrix2 = new Matrix();
			
			if(bNorth2 && ivAns2.getY() - ivAns2.getHeight() / 2 +240< 0)			 
				bNorth2 = false;
			else if(!bNorth2 && ivAns2.getY() + ivAns2.getHeight() / 2 +240> scrH)			 
				bNorth2 = true;
			
			if(bNorth2)
				imageY2-=10;
			else
				imageY2+=16;
			ivAns2.setY(imageY2);
			
			//movingx screen shift = 100
			int imageX2 = (int) ivAns2.getX();
			if(bLeft2 && ivAns2.getX() - ivAns2.getWidth() / 2 +100< 0)
				bLeft2 = false;
			else if(!bLeft2 && ivAns2.getX() + ivAns2.getWidth() / 2 +100> scrW)
				bLeft2 = true;
			
			if(bLeft2)
				imageX2-=18;
			else
				imageX2+=20;			
			ivAns2.setX(imageX2);
			
			if (ivAns2Rotate){
				ivAns2Rotate = false;
				int rotateDirection2 = (int) (Math.random() * 2);
				if (rotateDirection2 == 0){
					matrix2.postRotate((float) (-1)*ivAns2Angle, 26, 43);
					ivAns2.setRotation((float) (-1)*ivAns2Angle);
					ivAns2.setScaleX((float) 1.2);
					ivAns2.setScaleY((float) 1.2);
				} else {
					matrix2.postRotate((float) ivAns2Angle, 26, 43);
					ivAns2.setRotation((float) ivAns2Angle);
					ivAns2.setScaleX((float) 0.8);
					ivAns2.setScaleY((float) 0.8);
				}
			} else {
				ivAns2Rotate = true;
				matrix2.postRotate((float) 0, 20, 20);
			}
			ivAns2.invalidate();
			// ivAns3--------------------------------------------------
			//movingx screen shift = 240
			int imageY3 = (int) ivAns3.getY();
			int ivAns3Angle = (int) (Math.random() * 45);
			Matrix matrix3 = new Matrix();
			
			if(bNorth3 && ivAns3.getY() - ivAns3.getHeight() / 2 +240< 0)			 
				bNorth3 = false;
			else if(!bNorth3 && ivAns3.getY() + ivAns3.getHeight() / 2 +240> scrH)			 
				bNorth3 = true;
			
			if(bNorth3)
				imageY3-=20;
			else
				imageY3+=18;
			ivAns3.setY(imageY3);
			
			//movingx screen shift = 100
			int imageX3 = (int) ivAns3.getX();
			if(bLeft3 && ivAns3.getX() - ivAns3.getWidth() / 2 +100< 0)
				bLeft3 = false;
			else if(!bLeft3 && ivAns3.getX() + ivAns3.getWidth() / 2 +100> scrW)
				bLeft3 = true;
			
			if(bLeft3)
				imageX3-=16;
			else
				imageX3+=10;			
			ivAns3.setX(imageX3);
			
			if (ivAns3Rotate){
				ivAns3Rotate = false;
				int rotateDirection3 = (int) (Math.random() * 2);
				if (rotateDirection3 == 0){
					matrix3.postRotate((float) (-1)*ivAns3Angle, 26, 43);
					ivAns3.setRotation((float) (-1)*ivAns3Angle);
					ivAns3.setScaleX((float) 1.2);
					ivAns3.setScaleY((float) 1.2);
				} else {
					matrix3.postRotate((float) ivAns3Angle, 26, 43);
					ivAns3.setRotation((float) ivAns3Angle);
					ivAns3.setScaleX((float) 0.8);
					ivAns3.setScaleY((float) 0.8);
				}
			} else {
				ivAns3Rotate = true;
				matrix3.postRotate((float) 0, 20, 20);
			}
			ivAns3.invalidate();
			// ivAns4--------------------------------------------------
			//movingx screen shift = 240
			int imageY4 = (int) ivAns4.getY();
			int ivAns4Angle = (int) (Math.random() * 45);
			Matrix matrix4 = new Matrix();
			
			if(bNorth4 && ivAns4.getY() - ivAns4.getHeight() / 2 +240< 0)			 
				bNorth4 = false;
			else if(!bNorth4 && ivAns4.getY() + ivAns4.getHeight() / 2 +240> scrH)			 
				bNorth4 = true;
			
			if(bNorth4)
				imageY4-=12;
			else
				imageY4+=14;
			ivAns4.setY(imageY4);
			
			//movingx screen shift = 100
			int imageX4 = (int) ivAns4.getX();
			if(bLeft4 && ivAns4.getX() - ivAns4.getWidth() / 2 +100< 0)
				bLeft4 = false;
			else if(!bLeft4 && ivAns4.getX() + ivAns4.getWidth() / 2 +100> scrW)
				bLeft4 = true;
			
			if(bLeft4)
				imageX4-=18;
			else
				imageX4+=10;			
			ivAns4.setX(imageX4);
			
			if (ivAns4Rotate){
				ivAns4Rotate = false;
				int rotateDirection4 = (int) (Math.random() * 2);
				if (rotateDirection4 == 0){
					matrix4.postRotate((float) (-1)*ivAns4Angle, 26, 43);
					ivAns4.setRotation((float) (-1)*ivAns4Angle);
					ivAns4.setScaleX((float) 1.2);
					ivAns4.setScaleY((float) 1.2);
				} else {
					matrix4.postRotate((float) ivAns4Angle, 26, 43);
					ivAns4.setRotation((float) ivAns4Angle);
					ivAns4.setScaleX((float) 0.8);
					ivAns4.setScaleY((float) 0.8);
				}
			} else {
				ivAns4Rotate = true;
				matrix4.postRotate((float) 0, 20, 20);
			}
			ivAns4.invalidate();
			
	    }//move3()
		
	private Handler mHandler  = new Handler();	
	// For autoPlay 計時器----------------------------------------------	
	private Runnable autoPlay = new Runnable() {
			public void run() {
				imageLoaderSetup();
				mHandler.postDelayed(this, Constants.AUTO_PLAY_TIME);
			}//Run Program(this+imageLoaderSetup) with every 5 sec.
	};// Runnable autoPlay
	// For sec3Play 計時器----------------------------------------------				 
		private Runnable sec3Play = new Runnable() {
				public void run() {
						sec3Count++; 
						// TODO Auto-generated method stub
						img_3sec.setImageResource(sec3Amination[sec3Count%sec3Amination.length]);
						if(sec3Count < 4){// only doing one time
							ivAns1.setVisibility(View.INVISIBLE);
							ivAns2.setVisibility(View.INVISIBLE);
							ivAns3.setVisibility(View.INVISIBLE);
							ivAns4.setVisibility(View.INVISIBLE);
							ivAns0.setVisibility(View.GONE);
							img_10sec.setVisibility(View.INVISIBLE);
							img_3sec.setVisibility(View.VISIBLE);
							contentView.setVisibility(View.INVISIBLE);
							contentViewp.setVisibility(View.INVISIBLE);
							contentViewGo.setVisibility(View.VISIBLE);
							img_play.setVisibility(View.GONE);
							
							mHandler.postDelayed(this, 1000);//if don't excute this then quite mHandle						
						}else{
							score=0;
							ivAns0.setVisibility(View.VISIBLE);
							img_3sec.setVisibility(View.GONE);
							img_play.setVisibility(View.GONE);
							contentView.setVisibility(View.VISIBLE);
							contentViewGo.setVisibility(View.GONE);							
						}			 
				}
		};//Runnable sec3Play
	// For sec10Play 計時器----------------------------------------------				 
	private Runnable sec10Play = new Runnable() {
			public void run() {
					sec10Count++; 
					// TODO Auto-generated method stub
					img_10sec.setImageResource(sec10Amination[sec10Count%sec10Amination.length]);
					if(sec10Count < 11){// only doing one time
						img_10sec.setVisibility(View.VISIBLE);
						mHandler.postDelayed(this, 1000);//if don't excute this then quite mHandle						
					}else{
						//sec10Count=0;
						img_10sec.setVisibility(View.INVISIBLE);
						contentView.setVisibility(View.INVISIBLE);
						contentViewp.setVisibility(View.VISIBLE);
						dialogtxt_score.setText(" Right Count :  "+String.valueOf(score));
						dialogScore.show();
						
						//write data to top10list------
						dialogtxt_score1.setText(String.valueOf(score));
					}			 
			}
	};//Runnable sec10Play
	//For move running 計時器--------------------------------------------		 
		private Runnable move = new Runnable() {
			public void run() {		 
				if(sec10Count < 11){// only doing one time
						ivAns1.setVisibility(View.VISIBLE);
						ivAns2.setVisibility(View.VISIBLE);
						ivAns3.setVisibility(View.VISIBLE);
						ivAns4.setVisibility(View.VISIBLE);
						ivAns0.setVisibility(View.VISIBLE);
						img_play.setVisibility(View.GONE);
						move3();
						mHandler.postDelayed(this, 150);
				}else{
						//sec10Count=0;
						ivAns1.setVisibility(View.INVISIBLE);
						ivAns2.setVisibility(View.INVISIBLE);
						ivAns3.setVisibility(View.INVISIBLE);
						ivAns4.setVisibility(View.INVISIBLE);
						ivAns0.setVisibility(View.INVISIBLE);
						img_play.setVisibility(View.VISIBLE);
						//mHandler.removeCallbacks(sec10Play);
				}	
			}//Run moveM1.
	};//Runnable move
	
		 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kid_match);
       
     	context=this;
        findViews();
        mHandler.postDelayed(sec3Play, 0);
        mHandler.postDelayed(sec10Play, 2500);           
    	mHandler.postDelayed(move, 2600);
    	
    	//play imageButton --------
    	img_play.setOnClickListener(new ImageView.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sec3Count=0;
				sec10Count=0;
				
				mHandler.postDelayed(sec3Play, 0);
		        mHandler.postDelayed(sec10Play, 2500);           
		    	mHandler.postDelayed(move, 2600);
				 
				 
			}});
    	
    	
		//Response Method 1-----------------------------------------------------------
		ivAns1.setOnClickListener(new ImageView.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				count++;
				//v.setVisibility(View.INVISIBLE);
				mp.start();
				vr.vibrate(500);
				if(randomNum1!=randomNum0){
				     tts.speak("Answer is wrong ", TextToSpeech.QUEUE_FLUSH, null);
			    }else{
			    	 tts.speak(" good job! ", TextToSpeech.QUEUE_FLUSH, null);
			    	  
			    	 ivAns1.setImageResource(R.drawable.bc30);
			    	 score++;//take score number to myprefscore
			    	 Log.e("----score----","-----score----"+score);
				     imageLoaderSetup();
			    }
				 
		}});//ivAns1
		
		ivAns2.setOnClickListener(new ImageView.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				count++;
				//v.setVisibility(View.INVISIBLE);
				mp.start();
				vr.vibrate(500); 
				if(randomNum2!=randomNum0){
				     tts.speak("Answer is wrong ", TextToSpeech.QUEUE_FLUSH, null);
			    }else{
			    	 tts.speak(" good job! ", TextToSpeech.QUEUE_FLUSH, null);
			    	  
			    	 ivAns2.setImageResource(R.drawable.bc30);
			    	 score++;//take score number to myprefscore
			    	 Log.e("----score----","-----score----"+score);
			    	 imageLoaderSetup();
			    } 
		}});//ivAns2
		
		ivAns3.setOnClickListener(new ImageView.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				count++;
				//v.setVisibility(View.INVISIBLE);
				mp.start();
				vr.vibrate(500); 
				if(randomNum3!=randomNum0){
				     tts.speak("Answer is wrong ", TextToSpeech.QUEUE_FLUSH, null);
			    }else{
			    	 tts.speak(" good job! ", TextToSpeech.QUEUE_FLUSH, null);
			    	  
			    	 ivAns3.setImageResource(R.drawable.bc30);
			    	 score++;//take score number to myprefscore
			    	 Log.e("----score----","-----score----"+score);
			    	 imageLoaderSetup();
			    } 
		}});//ivAns3
		
		ivAns4.setOnClickListener(new ImageView.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				count++;
				//v.setVisibility(View.INVISIBLE);
				//ivAns4.setImageResource();
				//ivAns4.setVisibility(1);
				mp.start();
				vr.vibrate(500); 
				if(randomNum4!=randomNum0){
				     tts.speak("Answer is wrong ", TextToSpeech.QUEUE_FLUSH, null);
			    }else{
			    	 tts.speak(" good job! ", TextToSpeech.QUEUE_FLUSH, null);			    	 				     
			    	  
			    	 ivAns4.setImageResource(R.drawable.bc30);
			    	 score++;//take score number to myprefscore			    	 
			    	 imageLoaderSetup();
			    }
		}});//ivAns4
		 
		//Toast.makeText(context,"score=   "+score, 3);
    }//end of oncreate


    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();	 
		mHandler.postDelayed(autoPlay,500);
		
		img_play.setOnClickListener(new ImageView.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sec3Count=0;
				sec10Count=0;
				
				mHandler.postDelayed(sec3Play, 0);
		        mHandler.postDelayed(sec10Play, 2500);           
		    	mHandler.postDelayed(move, 2600);
				 
				 
			}});
		
	}
    
    @Override
    protected void onPause() {
    	mHandler.removeCallbacks(autoPlay);
    	mHandler.removeCallbacks(move);
    	mHandler.removeCallbacks(sec10Play);
    	mHandler.removeCallbacks(sec3Play);
    	
        super.onPause();
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.kid_match, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    	 switch (item.getItemId()) {
         case R.id.settings:
        	 Toast.makeText(context, "setting",3).show();
        	 
        	Intent it = new Intent(this.getApplication() , SettingActivity.class);
  			startActivity(it); 
             return true;
             
         case R.id.play_type:
        	 Toast.makeText(context, "play type",3).show();
         	        	 
//        	 android.os.Process.killProcess(android.os.Process.myPid());
//        	 System.exit(0);
//        	 finish();
        	 return true;
         	
         case R.id.top10list: 
         	Toast.makeText(context, "Score",3).show();
         	dialogTop10.show();
         	return true;
         	
         case R.id.new_game: 
          	Toast.makeText(context, "new game",3).show();
          	
          	imageLoaderSetup();          	 
          	return true;	
         	
         case R.id.login: 
           	Toast.makeText(context, "login",3).show();
           	
           	Intent it1 = new Intent(this.getApplication() , LoginActivity.class);
  			startActivity(it1);          	 
           	return true;
    	 
    	 }//switch
         return false;
    }
    
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.settings);
        appState = (KidMatchApp) context.getApplicationContext();
    	 
        if (!appState.isDefaule) {
            item.setEnabled(true);//true
            item.getIcon().setAlpha(255);
            //appState.isDefaule=true;
        } else {
            // disabled
            item.setEnabled(false);//false***
            item.getIcon().setAlpha(50);         
        }
		return true;//if false menu item invisible
    }
    

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub--FROM TO TextToSpeech
		
	}

	@Override
	public View makeView() {
		// TODO Auto-generated method stub--from to ViewFactory
		ImageView v = new ImageView(context);
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(300, 300);
		v.setScaleType(ScaleType.FIT_CENTER);
		v.setLayoutParams(layoutParams);
		return v;
	}

	
	@Override
	protected void onRestart() {// return KidMatchActivity and Reload Program
		// TODO Auto-generated method stub
		super.onRestart();
		finish();
		startActivity(getIntent());
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
//		System.gc();
//		sec10AnimationsContainer.stop();
		super.onDestroy();
		 
	}
	
	//-------Create the post------------------
	
	private void createRelationParseObject() {
		
		// Create the post
		ParseObject myAsem = new ParseObject("Asem");
		myAsem.put("asemClass", "clor");
		myAsem.put("asemContent", "Where should we go for lunch?");
		// Create the comment
		ParseObject answer = new ParseObject("Answer");
		answer.put("asemContent1", "Let's do Sushirrito.");
		// Add a relation between the Post and Comment
		answer.put("parent", myAsem);
		// This will save both myPost and myComment
		answer.saveInBackground();
		
//		Log.e("-----Create the post-----", "---Create the post----");
		    
	}
	
	//-------ParseRelation relation------------------
	
	private void addNewItemToAsemList() {
		 
		//ParseObject asem = new ParseObject("Asem");		 
//		 asem.saveInBackground(new SaveCallback () {
//
//             @Override
//             public void done(ParseException e) {
//                 if (e == null) {
//                     setResult(RESULT_OK);
//                     finish();
//                 } else {
//                     Toast.makeText(getApplicationContext(),
//                             "Error saving: " + e.getMessage(),
//                             Toast.LENGTH_SHORT)
//                             .show();
//                 }
//             }
//
//         });
		
		//Create Object of Asem and add Item  
		ParseObject asem =ParseObject.create("Asem");
		//asem.put("asemClass", "clor");
		((Asem) asem).setClas("lang");
		((Asem) asem).setIsChecked(true);
		((Asem) asem).setContent("lang lang lang");
		((Asem) asem).setPosition(4000);
		asem.saveInBackground();
	 	
	}//addNewItemToAsemList()
	
	
	private void updateAsemList() {
		// Create query for objects of type "Asem"
		ParseQuery<Asem> query = new ParseQuery<Asem>("Asem");//***1
		//ParseQuery<Asem> query = ParseQuery.getQuery("Asem");
		//query.whereEqualTo("isChecked", true);
		query.orderByDescending("updateAt");
		
//		listQueryAsem = query.find();
//		sizelistQueryAsem = listQueryAsem.size(); 
		 
		ansPhotoUriArraylist = new ArrayList<Photo>();
		 
		
		 
		query.findInBackground(new FindCallback<Asem>() {

			@Override
			public void done(List<Asem> photoList, ParseException e) {
				if (e == null) {
					// If there are results, update the list of photos
					// and notify the adapter
					//photos.clear();
					
					//photoList.add((Asem) Asem.createWithoutData("asemClass", "lang"));
					
					 
					for (ParseObject photo : photoList) {
						 
//		                Boolean   booischecked = (Boolean)   photo.get("isChecked");
//						Integer   intPosition  = (Integer)   photo.get("asemPosition");
//						String    txtClass     = (String)    photo.get("asemClass");
//						String    txtContent   = (String)    photo.get("asemContent");
//						ParseFile image        = (ParseFile) photo.get("image");
						
						photo.put("isChecked", false);//initial setup to false!!!	
						photo.put("asemPosition", 1000);//initial setup to  ordering number!!!
						photo.put("asemClass", "face");//initial setup to  ordering number!!!
						photo.put("asemContent", "sammmmmm");//initial setup to  ordering number!!!
						//photo.add("asemClass", 4);
						try {
							photo.fetch();
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						
						
						photo.saveInBackground();
						//photo.saveEventually();
						
//						Log.e("---Asem---", "---asemPosition--- "+photo.get("asemPosition"));
//						Log.e("---Asem---", "---asemContent--- "+photo.get("asemContent"));
//						Log.e("---Asem---", "---isChecked--- "+photo.get("isChecked"));
//						Log.e("---Asem---", "---asemClass--- "+photo.get("asemClass"));
						
						
//						Photo map = new Photo();
//						if(image!=null){
//						map.setphoto(image.getUrl());
//						ansPhotoUriArraylist.add(map);
//						}
					 
						
//						Log.e("---photo retrieval---", "------for (ParseObject photo : photoList)------ ");
					}//for (ParseObject photo : photoList)
						//	((ArrayAdapter<String>) getListAdapter())
						//			.notifyDataSetChanged();
				} else {
					Log.e("photo retrieval", "Error: " + e.getMessage());
				}

			}

		});
		
		
		

	}//updateAsemList()
	
  
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == Activity.RESULT_OK) {
			// If a new photo has been added, update the list of photos		 
			
		}
		
	}//onActivityResult
		
}//end of KidMatchActivity
