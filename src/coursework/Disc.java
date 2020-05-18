package coursework;

import java.io.Serializable;

public class Disc implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2548512303474743988L;
	//declare instance variables
	private String colour = "";
	
	public Disc(){}
	public Disc(String colour){
		setColour(colour);
	}//end of constructor

	//getter and setters
	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}
}//end of class
