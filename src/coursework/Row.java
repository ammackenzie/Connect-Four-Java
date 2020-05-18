package coursework;

import java.io.Serializable;
import java.util.LinkedList;

public class Row implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4199893321935639958L;
	
	LinkedList <Square> theRow = new LinkedList <Square> ();//populates row with squares
	
	public Row(int numOfColumns){
		for(int loop = 0; loop < (numOfColumns); loop++){
			Square tempSquare = new Square();
			this.theRow.add(tempSquare);
		}//end of for loop
	}//end of master constructor
	
	//getter only - no need for setter in game logic
	public LinkedList<Square> getTheRow(){
		return this.theRow;
	}
}//end of class
 