package coursework;

import java.io.Serializable;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Grid implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1297433483698841321L;

	LinkedList <Row> theGrid = new LinkedList <Row> ();//create list of row objects

	public Grid(int numOfRows, int numOfColumns){//flexible based on user input
		for(int loop = 0; loop < (numOfRows); loop++){
			Row tempRow = new Row(numOfColumns);
			this.theGrid.add(tempRow);
		}//end of for loop
	}//end of constructor
	
	//getter only - no need for setter in game logic
	public LinkedList<Row> getTheGrid(){
		return this.theGrid;
	}
}//end of class





