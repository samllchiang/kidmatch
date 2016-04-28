package com.sam.kidmatch;
import com.parse.ParseQueryAdapter;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;


public class SetupQuesActivity extends ListActivity {
	
	static KidMatchApp appState;
	private Elem elem;
	Context context;
	private ParseQueryAdapter<Elem> mainAdapter;
	private ElemListAdapter elemListAdapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//elem = new Elem();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		 
		    getListView().setClickable(true);//instead of setContentView()
			context = this.getApplicationContext(); 

			mainAdapter = new ParseQueryAdapter<Elem>(this, "Elem");
			mainAdapter.setTextKey("title");
			mainAdapter.setImageKey("image");		 

			// Subclass of ParseQueryAdapter
			elemListAdapter = new ElemListAdapter(this);

			// Default view is all elems
			elemListAdapter.loadObjects();
			setListAdapter(elemListAdapter);				
	}//end of onCreate
	
 	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		
	}

 
	public Elem getCurrentElem(){
		return elem;
	}

}
