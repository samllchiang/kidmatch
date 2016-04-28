package com.sam.kidmatch;


public class SpinnerModel {
	
	private  String borderName="";
	private  String image=""; 
	 
	
	/*********** Set Methods ******************/
	public void setBorderName(String borderName)
	{
		this.borderName = borderName;
	}
	
	
	
	public void setImage(String image)
	{
		this.image = image;
	}
	
	 
	
	/*********** Get Methods ****************/
	public String getBorderName()
	{
		return this.borderName;
	}
	
	public String getImage()
	{
		return this.image;
	}

	 
}
