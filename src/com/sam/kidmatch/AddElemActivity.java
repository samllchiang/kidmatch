package com.sam.kidmatch;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.parse.ParseFile;
import com.parse.ParseObject;


/*
 * AddElemActivity contains two fragments that handle
 * data entry and capturing a photo of a given Elem.
 * The Activity manages the overall Elem data.
 */
public class AddElemActivity extends Activity {

	private Elem elem;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//elem = new Elem();//***1 original
		//ParseObject asem =ParseObject.create("Elem");//***2 modify1
		elem = (Elem) ParseObject.create("Elem"); //***3 modify2
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		  
		// Begin with main data entry view,
		// AddElemFragment
		setContentView(R.layout.activity_add_elem);
		FragmentManager manager = getFragmentManager();
		Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

		if (fragment == null) {
			fragment = new AddElemlFragment();
			manager.beginTransaction().add(R.id.fragmentContainer, fragment)
					.commit();
		}
		
		//Intent i = getIntent();
		// Get the intent from ListViewAdapter
//		photo = i.getStringExtra("photo");
//
//		// Locate the ImageView in singleitemview.xml
//		ImageView imgphoto = (ImageView) findViewById(R.id.singlephoto);
//
//		// Load image into the ImageView
//		imageLoader.DisplayImage(photo, imgphoto);
		
	}

	public Elem getCurrentElem() {
		return elem;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	

}
