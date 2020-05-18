package coursework;

import java.io.Serializable;

public class Square implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 12589999780498621L;
	//declare instance variables
	private boolean empty = true; 
	private Disc theDisc = new Disc();

	public Square(){
		setEmpty();//automatically set empty when newly initialised 
	}//end of constructor
	
	//getters and setters
	public void setEmpty(){
		this.empty = true;
	}

	public boolean isEmpty() {
		return this.empty;
	}
	
	public Disc getTheDisc() {
		return this.theDisc;
	}
	
	public void setFilled(String discColour) {
		this.theDisc.setColour(discColour);
		this.empty = false; 
	}
	
	public String returnState(){//returns string representation of square state
		String state = "";
		if(isEmpty()){
			state = "0";
		} else if(getTheDisc().getColour() == "yellow"){
			state = "Y";
		} else if(getTheDisc().getColour() == "red"){
			state = "R";
		}//end of if/else statements
		return state;
	}//end of return state
}//end of class
