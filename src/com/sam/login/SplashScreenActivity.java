package com.sam.login;

 
 

import java.util.logging.MemoryHandler;

import com.sam.kidmatch.KidMatchActivity;
import com.sam.kidmatch.KidMatchApp;
import com.sam.kidmatch.R;
import com.sam.kidmatch.SettingActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
 

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class SplashScreenActivity extends Activity {

	
	private Boolean isRedirectReg = true;
	private Boolean isRedirectBoot = true;
	  
	private static final int ANIMATION_INTERVAL = 1000;// 200ms
	
	private ImageView eye_a0;//light Device
	private ImageView egg_ay0;//rotate Device
	private ImageView mouse0;//song Device
	private ImageView hand0;//vibrate Device
	private ImageButton btn_tooth, btn_reg;
	private static KidMatchApp appState; 
	
	int count1;
	int count2;
	int count3;
	
	int[] eyeAmination;
	int[] handAmination;
	int[] mouseAmination;
	int[] eggAmination;
	
	Context context;
	
	private AnimationDrawable anim_eye,anim_mouse,anim_hand,anim_egg;
	private FasterAnimationsContainer eyeAnimationsContainer,mouseAnimationsContainer,handAnimationsContainer,eggAnimationsContainer;
	
	private void findviews(){//for xml type animation which's too many photo induced OOME. 
		
		eye_a0   = (ImageView)findViewById(R.id.eye_a0);
		anim_eye = (AnimationDrawable) eye_a0.getDrawable();
		anim_eye.setOneShot(false);
								
        mouse0   = (ImageView)findViewById(R.id.mouse0);
        anim_mouse = (AnimationDrawable) mouse0.getDrawable();
        anim_mouse.setOneShot(false);
        
        hand0   = (ImageView)findViewById(R.id.hand0);
        anim_hand = (AnimationDrawable) hand0.getDrawable();
        anim_hand.setOneShot(false);
        
        egg_ay0 = (ImageView)findViewById(R.id.egg_ay0);
        anim_egg = (AnimationDrawable) egg_ay0.getDrawable();
        anim_egg.setOneShot(false);
        
        btn_tooth = (ImageButton) findViewById(R.id.btn_tooth);
        btn_reg = (ImageButton) findViewById(R.id.btn_reg);
	}
	
	private void findviews000(){//for runnable method 
		
		eye_a0   = (ImageView)findViewById(R.id.eye_a0);		 							
        mouse0   = (ImageView)findViewById(R.id.mouse0);       
        hand0    = (ImageView)findViewById(R.id.hand0);     
        egg_ay0  = (ImageView)findViewById(R.id.egg_ay0);
         
        
        btn_tooth = (ImageButton) findViewById(R.id.btn_tooth);
        btn_reg = (ImageButton) findViewById(R.id.btn_reg);
	}
	
	// For eyePlay 計時器----------------------------------------------
			private Handler mHandler  = new Handler();
			private Runnable eyePlay = new Runnable() {
				public void run() {
					count1++; 
					// TODO Auto-generated method stub
					eye_a0.setImageResource(eyeAmination[count1%eyeAmination.length]); 
					mHandler.postDelayed(this, 500);
				}
			};//Runnable eyePlay
	// For handPlay 計時器----------------------------------------------				 
				private Runnable handPlay = new Runnable() {
					public void run() {
						count2++; 
						// TODO Auto-generated method stub
						hand0.setImageResource(handAmination[count2%handAmination.length]); 
						mHandler.postDelayed(this, 500);
					}
				};//Runnable handPlay		
	// For mousePlay 計時器----------------------------------------------				 
	private Runnable mousePlay = new Runnable() {
		public void run() {
			count3++; 
			// TODO Auto-generated method stub
			mouse0.setImageResource(mouseAmination[count3%mouseAmination.length]); 
			mHandler.postDelayed(this, 500);
		}
	};//Runnable mousePlay
				
				
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 

		setContentView(R.layout.splash_matchgame);
		context = this; 
		findviews000();
		
		//eye_a0--------------------get data from R.array.eye--------------------        
        TypedArray ar = context.getResources().obtainTypedArray(R.array.eye);
        int sizeEyeAmination = ar.length();
        eyeAmination = new int[sizeEyeAmination];//array need to disclear length to complete object.
        for (int i = 0; i < sizeEyeAmination; i++)
        	eyeAmination[i] = ar.getResourceId(i, 0);
        ar.recycle();  
    
        mHandler.postDelayed(eyePlay, 500);//triggle after 0.5sec
        
        // eye animation-------       
//      eyeAnimationsContainer = FasterAnimationsContainer.getInstance(eye_a0);
//		eyeAnimationsContainer.addAllFrames(eyeAmination,ANIMATION_INTERVAL);
//		eyeAnimationsContainer.start();
        
        //hand0--------------------get data from R.array.hand--------------------
        TypedArray ar1 = context.getResources().obtainTypedArray(R.array.hand);
        int sizeHandAmination = ar1.length();
        handAmination = new int[sizeHandAmination];//array need to disclear length to complete object.
        for (int i = 0; i < sizeHandAmination; i++)
        	handAmination[i] = ar1.getResourceId(i, 0);
        ar1.recycle(); 
              
        mHandler.postDelayed(handPlay, 1000);//triggle after 1sec
        
        // hand animation-------        
//      handAnimationsContainer = FasterAnimationsContainer.getInstance(hand0);
//		handAnimationsContainer.addAllFrames(handAmination,ANIMATION_INTERVAL);
//		handAnimationsContainer.start();
        
        //mouse0--------------------get data from R.array.mouse--------------------
        TypedArray ar2 = context.getResources().obtainTypedArray(R.array.mouse);
        int sizeMouseAmination = ar2.length();
        mouseAmination = new int[sizeMouseAmination];//array need to disclear length to complete object.
        for (int i = 0; i < sizeHandAmination; i++)
        	mouseAmination[i] = ar2.getResourceId(i, 0);
        ar2.recycle();
               
        mHandler.postDelayed(mousePlay, 1500);//triggle after 1.5sec
        
        // mouse animation-------       
//      mouseAnimationsContainer = FasterAnimationsContainer.getInstance(mouse0);
//		mouseAnimationsContainer.addAllFrames(mouseAmination,ANIMATION_INTERVAL);		
//		mouseAnimationsContainer.start();
        
        //egg_ay0--------------------get data from R.array.egg--------------------
        TypedArray ar3 = context.getResources().obtainTypedArray(R.array.egg);
        int sizeEggAmination = ar3.length();
        eggAmination = new int[sizeEggAmination];//array need to disclear length to complete object.
        for (int i = 0; i < sizeEggAmination; i++)
        	eggAmination[i] = ar3.getResourceId(i, 0);
        ar3.recycle();
        
		// egg animation------- use for switching large size picture      
        eggAnimationsContainer = FasterAnimationsContainer.getInstance(egg_ay0);
		eggAnimationsContainer.addAllFrames(eggAmination,ANIMATION_INTERVAL);
		eggAnimationsContainer.start();
        ///////////////////////////////////////////////////////////////////////////////
        
        
//		anim_eye.start();//开始动画
//        anim_mouse.start();
//        anim_hand.start();
//        anim_egg.start();
        
        scheduleRedirect();
       
	}// onCreate
	
	
    //使用onWindowFocusChanged會變形-----picture too small*****
//	@Override
//	public void onWindowFocusChanged(boolean hasFocus) {
//	  // TODO Auto-generated method stub
//	  super.onWindowFocusChanged(hasFocus);
//		
//	  //将动画资源文件设置为ImageView的背景
//	  eye_a0.setBackgroundResource(R.animator.eye);
//	  mouse0.setBackgroundResource(R.animator.mouse);
//	  hand0.setBackgroundResource(R.animator.hand);
//	  egg_ay0.setBackgroundResource(R.animator.egg);
//		
//	  //获取ImageView背景,此时已被编译成AnimationDrawable
//      AnimationDrawable anim_eye = (AnimationDrawable) eye_a0.getBackground();
//      AnimationDrawable anim_mouse = (AnimationDrawable) mouse0.getBackground();
//      AnimationDrawable anim_hand = (AnimationDrawable) hand0.getBackground();
//      AnimationDrawable anim_egg = (AnimationDrawable) egg_ay0.getBackground();
//        
//      anim_eye.start();//开始动画
//      anim_mouse.start();
//      anim_hand.start();
//      anim_egg.start();
//	
//	}//onWindowFocusChanged
	
	

	private void scheduleRedirect() {
		// Calls login activity after splash screen timeout
		final Activity current = this;
		
		appState = (KidMatchApp) context.getApplicationContext();		 
		appState.isDefaule=true;
				  
				
		btn_reg.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isRedirectReg = false;
			}
		});
		
		btn_tooth.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isRedirectBoot = false;
			}
		});
		
		new Handler().postDelayed(new Runnable() {
			public void run() {
				
//				 anim_eye.stop();
//		         anim_mouse.stop();
//		         anim_hand.stop();
//		         anim_egg.stop();
				 //eyeAnimationsContainer.stop(); 
		         System.gc();
				
				 if (!isRedirectReg) {
						Intent intent = new Intent(current, LoginActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
								| Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
				 }else if(!isRedirectBoot){
					    Intent intent = new Intent(current, KidMatchActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
								| Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
				 }else{
						Intent intent = new Intent(current,	KidMatchActivity.class);			
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
					            | Intent.FLAG_ACTIVITY_NEW_TASK); 
						startActivity(intent);
				 }
				 
				 overridePendingTransition(0,0);
				
			}
		}, Constants.SPLASH_SCREEN_TIMEOUT);
	}//scheduleRedirect()
	
 
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacks(eyePlay);
		mHandler.removeCallbacks(handPlay);
		mHandler.removeCallbacks(mousePlay);
//		eyeAnimationsContainer.stop();
//		handAnimationsContainer.stop();
//		mouseAnimationsContainer.stop();
		eggAnimationsContainer.stop(); 
		
		
		System.gc();
	}


}