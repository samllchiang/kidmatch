package com.sam.kidmatch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
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
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;
 

public class CameraFragment extends Fragment {

	public static final String TAG = "CameraFragment";

	private static KidMatchApp appState;
	Context context;
	private Resources res=null;
	Bitmap cs = null;
	
	private Camera camera;
	private SurfaceView surfaceView;
	private ParseFile imageFile;
	private ImageButton photoButton;

	public String borderType = "bd1";
	public String borderNum = "1";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_camera, parent, false);

		context = this.getActivity();
		res = this.getResources();
		
		photoButton = (ImageButton) v.findViewById(R.id.camera_photo_button);

		if (camera == null) {
			try {
				camera = Camera.open();
				photoButton.setEnabled(true);
			} catch (Exception e) {
				Log.e(TAG, "No camera with exception: " + e.getMessage());
				photoButton.setEnabled(false);
				Toast.makeText(getActivity(), "No camera detected",
						Toast.LENGTH_LONG).show();
			}
		}

		photoButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (camera == null)
					return;
				camera.takePicture(new Camera.ShutterCallback() {

					@Override
					public void onShutter() {
						// nothing to do
					}

				}, null, new Camera.PictureCallback() {

					@Override
					public void onPictureTaken(byte[] data, Camera camera) {
						saveScaledCombinePhoto(data);
					}

				});

			}
		});

		surfaceView = (SurfaceView) v.findViewById(R.id.camera_surface_view);
		SurfaceHolder holder = surfaceView.getHolder();
		holder.addCallback(new Callback() {

			public void surfaceCreated(SurfaceHolder holder) {
				try {
					if (camera != null) {
						camera.setDisplayOrientation(90);
						camera.setPreviewDisplay(holder);
						camera.startPreview();
					}
				} catch (IOException e) {
					Log.e(TAG, "Error setting up preview", e);
				}
			}

			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				// nothing to do here
			}

			public void surfaceDestroyed(SurfaceHolder holder) {
				// nothing here
			}

		});

		return v;
	}

	/*
	 * ParseQueryAdapter loads ParseFiles into a ParseImageView at whatever size
	 * they are saved. Since we never need a full-size image in our app, we'll
	 * save a scaled one right away.
	 */
//	private void saveScaledPhoto(byte[] data) {
//
//		// Resize photo from camera byte array
//		Bitmap photoImage = BitmapFactory.decodeByteArray(data, 0, data.length);
//		Bitmap photoImageScaled = Bitmap.createScaledBitmap(photoImage, 300, 300
//				* photoImage.getHeight() / photoImage.getWidth(), false);
//
//		// Override Android default landscape orientation and save portrait
//		Matrix matrix = new Matrix();
//		matrix.postRotate(90);
//		Bitmap rotatedScaledphotoImage = Bitmap.createBitmap(photoImageScaled, 0,
//				0, photoImageScaled.getWidth(), photoImageScaled.getHeight(),
//				matrix, true);
//
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		rotatedScaledphotoImage.compress(Bitmap.CompressFormat.PNG, 100, bos);
//
//		byte[] scaledData = bos.toByteArray();
//
//		// Save the scaled image to Parse
//		imageFile = new ParseFile("elem_image.jpg", scaledData);
//		imageFile.saveInBackground(new SaveCallback() {
//
//			public void done(ParseException e) {
//				if (e != null) {
//					Toast.makeText(getActivity(),
//							"Error saving: " + e.getMessage(),
//							Toast.LENGTH_LONG).show();
//				} else {
//					addPhotoToElemAndReturn(imageFile);
//				}
//			}
//		});
//	}

	
	public void saveScaledCombinePhoto(byte[] data) {
		
		appState = (KidMatchApp) context.getApplicationContext();
		if(appState.numPng !=null){
			appState.numPng++;
		}else{
			appState.numPng=0;
		}
        		
		
		//Bitmap border1 = BitmapFactory.decodeResource(res,R.drawable.bd27);
		//根据资源名称和目录获取R.java中对应的资源ID
		int bdid = getResources().getIdentifier("bq" + appState.borderNum, "drawable", this.context.getPackageName());
		Bitmap border1 = BitmapFactory.decodeResource(res,bdid);
		
		// Fix size's photo from camera byte array----------400 x 400 for camera----111----xxxxxx camera photoImage  
		 
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDither=false; //Disable Dithering mode
	    options.inPurgeable=true; //Tell to gc that whether it needs free memory, the Bitmap can be cleared
	    options.inInputShareable=true; //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
	    options.inTempStorage=new byte[32 * 1024]; 
	    options.inJustDecodeBounds = false;  
	    options.inSampleSize = 2;//***
		
		Bitmap photoImage = BitmapFactory.decodeByteArray(data, 0, data.length,options);
		
		
		// Resize photo from camera byte array----------400 x 400 for camera-------222-----xxxxxx Resize photoImageScaled
//		Bitmap photoImageScaled = Bitmap.createScaledBitmap(photoImage,
//				                                                   400,
//				   400* photoImage.getHeight() / photoImage.getWidth(), false);
		
		Bitmap photoImageScaled = Bitmap.createScaledBitmap(photoImage,
				   400* photoImage.getWidth() / photoImage.getHeight(),
				                                                   400, false);
		
		if (photoImage != null){///////
			photoImage.recycle();
			photoImage = null;
  	        }///////////////////////////

		// Override Android default landscape orientation and save portrait------333----xxxx rotate rotatedScaledphotoImage 
		Matrix matrix = new Matrix();
		matrix.postRotate(90);
		Bitmap rotatedScaledphotoImage = Bitmap.createBitmap(photoImageScaled, 0,
				0, photoImageScaled.getWidth(), photoImageScaled.getHeight(),
				matrix, true);
		
		if (photoImageScaled != null){///////
			photoImageScaled.recycle();
			photoImageScaled = null;
  	        }///////////////////////////
		 
		//resize border---------------------------------------------------------444---xxx border2Resize/border3Resize		
		appState = (KidMatchApp) context.getApplicationContext();
		if(appState.isSquare){//bk44-square-border2-----------------------------444---xxx border2Resize/border3Resize
			Bitmap border2 = BitmapFactory.decodeResource(res,R.drawable.aa44);//bk44-square-border2
			int width = border2.getWidth();
			int height = border2.getHeight();
			// 設置想要的大小
			int newWidth = rotatedScaledphotoImage.getWidth();
			int newHeight = rotatedScaledphotoImage.getHeight();
			// 計算缩放比例
			float scaleWidth  =(((float) newWidth) / width)*1.2f;
			float scaleHeight =(((float) newHeight) / height)*1.2f;
			// 取得想要缩放的matrix參數
			// Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 得到新的圖片
			Bitmap border2Resize = Bitmap.createBitmap(border2, 0, 0, width, height, matrix,true);//bk44-square-border2
									
			//square rouded corners image---------------------------------------------------666.1------xxxxx  roundCornerImage		 
			Bitmap roundCornerImage = Bitmap.createBitmap(rotatedScaledphotoImage.getWidth(), rotatedScaledphotoImage.getHeight(), Bitmap.Config.ARGB_4444);
			Canvas canvas = new Canvas(roundCornerImage);
			int color = 0xff424242;
			Paint paint = new Paint();
			Rect rect = new Rect(0, 0, rotatedScaledphotoImage.getWidth(), rotatedScaledphotoImage.getHeight());
			RectF rectF = new RectF(rect);
			float roundPx = 80;//rounded angle
			
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(rotatedScaledphotoImage, rect, rect, paint);
		 	
			//combine border and rotatedScaledphotoImage to cs--------------------------777-----xxxxxx imageCombine 
			//cs = Bitmap.createBitmap(border1.getWidth(), border1.getHeight(), Bitmap.Config.ARGB_4444);//define canvas size		 
			//cs = Bitmap.createBitmap(roundCornerImage.getWidth()+400, roundCornerImage.getHeight()+400, Bitmap.Config.ARGB_4444);//define canvas size 	 
			//for roundCornerImage picture-xxx---roundCornerImage.getWidth()+100--b1.2(10,-5)
			cs = Bitmap.createBitmap(roundCornerImage.getWidth()+100, roundCornerImage.getHeight()+100, Bitmap.Config.ARGB_4444);//define canvas size 		  
			Canvas comboImage = new Canvas(cs);
			comboImage.drawBitmap(border2Resize, 10f,-5f, null);//b1.2/p+100(10,-5)---bottom
			comboImage.drawBitmap(roundCornerImage, 50f, 50f, null);//---top  最上面(photo)加在上面
			
			if (rotatedScaledphotoImage != null){//////
				rotatedScaledphotoImage.recycle();
				rotatedScaledphotoImage = null;
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

		
		 //}else if(isUserDefine){//user define for drawing---///2\\\
			 
		
		}else{//bk9-circle-border3---------------------------------------------444---xxx border2Resize/border3Resize
			Bitmap border3 = BitmapFactory.decodeResource(res,R.drawable.aa9);//bk9-circle-border3
			int width = border3.getWidth();
			int height = width;		
			// 設置想要的大小
			int newWidth = (rotatedScaledphotoImage.getWidth()>rotatedScaledphotoImage.getHeight())?rotatedScaledphotoImage.getWidth()
					                                                                               :rotatedScaledphotoImage.getHeight();
			int newHeight =newWidth;		
			// 計算缩放比例
			float scaleWidth  =(((float) newWidth) / width)*1.0f;
			float scaleHeight =(((float) newHeight) / height)*1.0f;
			// Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 得到新的圖片		 
			Bitmap border3Resize = Bitmap.createBitmap(border3, 0, 0, width, height, matrix,true);//bk9-circle-border3 
		 
			//circle image-------------------------------------------------------------666.2----------xxxx circleImage
			BitmapShader shader = new BitmapShader (rotatedScaledphotoImage,  TileMode.CLAMP, TileMode.CLAMP);
			Paint paint = new Paint();
			paint.setShader(shader);
			
			Bitmap circleImage = Bitmap.createBitmap(rotatedScaledphotoImage.getWidth(), rotatedScaledphotoImage.getHeight(), Bitmap.Config.ARGB_4444);
			Canvas c = new Canvas(circleImage);
			c.drawCircle(rotatedScaledphotoImage.getWidth()/2, rotatedScaledphotoImage.getHeight()/2, rotatedScaledphotoImage.getWidth()/2, paint);
				    		 		
			//combine border and rotatedScaledphotoImage to cs--------------------------777-----xxxxxx imageCombine
			//for circleImage picture-xxx
			cs = Bitmap.createBitmap(circleImage.getWidth()+100, circleImage.getHeight()+100, Bitmap.Config.ARGB_4444);//define canvas size 		  
			Canvas comboImage = new Canvas(cs);
			comboImage.drawBitmap(border3Resize, -20f,50f, null);//b1.2/p+100(10,-5)---bottom
			comboImage.drawBitmap(circleImage, 50f, 50f, null);//---top  最上面(photo)加在上面
			
			if (rotatedScaledphotoImage != null){///////
				rotatedScaledphotoImage.recycle();
				rotatedScaledphotoImage = null;
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
		
	    }
		
		
		//transfer bitmap to PNG type's ByteArrayOutputStream and to bytefosdata---888----xxxxx bos scaledData
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
        cs.compress(Bitmap.CompressFormat.PNG, 100, bos);
		byte[] scaledData = bos.toByteArray();
		
		appState = (KidMatchApp) context.getApplicationContext();
		appState.cs = cs;

		// Save the scaled image to Parse and setup name to camera photo(cam)------999----xxxxx    imageFile
		imageFile = new ParseFile("cam"+appState.numPng+".png", scaledData);
		imageFile.saveInBackground(new SaveCallback() {

			public void done(ParseException e) {
				if (e != null) {
					Toast.makeText(getActivity(),
							"Error saving: " + e.getMessage(),
							Toast.LENGTH_LONG).show();
				} else {
										
					 
					
					addPhotoToElemAndReturn(imageFile);
				}
			}
		});
	}//saveScaledCombinePhoto

	
	
	/*
	 * Once the photo has saved successfully, we're ready to return to the
	 * AddElemFragment. When we added the CameraFragment to the back stack, we
	 * named it "AddElemFragment". Now we'll pop fragments off the back stack
	 * until we reach that Fragment.
	 */
	private void addPhotoToElemAndReturn(ParseFile imageFile) {
		((AddElemActivity) getActivity()).getCurrentElem().setImageFile(
				imageFile);
		FragmentManager fm = getActivity().getFragmentManager();
		fm.popBackStack("AddElemFragment",
				FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (camera == null) {
			try {
				camera = Camera.open();
				photoButton.setEnabled(true);
			} catch (Exception e) {
				Log.i(TAG, "No camera: " + e.getMessage());
				photoButton.setEnabled(false);
				Toast.makeText(getActivity(), "No camera detected",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onPause() {
		if (camera != null) {
			camera.stopPreview();
			camera.release();
		}
		
		if (cs != null){///////
//			cs.recycle();
//			cs = null;
  	    }/////////////////////
		
		super.onPause();
	}

	 

}
