package com.sam.kidmatch;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/********* Adapter class extends with BaseAdapter and implements with OnClickListener ************/
public class CustomSpinnerAdapter extends ArrayAdapter<String>{
	
	private Context activity;
    private ArrayList borderList;
    public Resources res;
    SpinnerModel borderLocation=null;
    LayoutInflater inflater;
	
    /*************  CustomAdapter Constructor *****************/
	public CustomSpinnerAdapter(
			              Context context, 
			              int textViewResourceId,   
			              ArrayList objects,
			              Resources resLocal
			             ) 
	 {
        super(context, textViewResourceId, objects);
        
        /********** Take passed values **********/
        activity = context;
        borderList = objects;
        res = resLocal;
   
        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	  }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

    	/********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.spinner_border, parent, false);
        
        /***** Get each Model object from Arraylist ********/
        borderLocation = null;
        borderLocation = (SpinnerModel) borderList.get(position);
        
        TextView border_name        = (TextView)row.findViewById(R.id.bordr_name);         
        ImageView border_image = (ImageView)row.findViewById(R.id.border_image);
               
        // Set values for spinner each row 
    	border_name.setText(borderLocation.getBorderName());       
    	border_image.setImageResource(res.getIdentifier("com.sam.kidmatch:drawable/"+borderLocation.getImage(),null,null));
        System.gc();         
        return row;
      }
 }
