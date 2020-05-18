package coursework;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Game {
	//default grid size variables
	private final int STANDARDCOLUMNS = 7;
	private final int STANDARDROWS = 6;

	//current grid size variables
	private int numOfColumns = STANDARDCOLUMNS;
	private int numOfRows = STANDARDROWS;

	//player colour choice variables
	private String player1 = "";
	private String player2 = "";

	//declare images
	ImageIcon connnect4 = new ImageIcon("connect4.png");
	ImageIcon red = new ImageIcon("redDisk.png");
	ImageIcon yellow = new ImageIcon("yellowDisk.png");
	ImageIcon icon4 = new ImageIcon("4icon.png");

	Grid newGrid = new Grid(numOfRows, numOfColumns); //create new grid of standard size

	public void newGame() throws IOException{
		//declare choice variables
		int newGame = 0;
		int loadGame = 1;
		int customGame = 2;
		int choice = welcomeMessage(); //display welcome and start up options

		//if else statements depending on user start up choice - new game / load game /  custom game
		if(choice == newGame) {//new standard game
			setPlayer1(player1Choice());//prompts player 1 for colour choice
			assignPlayer2Colour(getPlayer1()); //assigns player 2 the opposite colour
			setNewGrid(getNumOfRows(), getNumOfColumns());
		}  else if(choice == loadGame) {//selected load game
			reloadData();//run reload method
		} else if(choice == customGame) {//selected custom game
			createCustomGrid(); //gets custom user values and edits newGrid 
		}//end of if/else statements
		runGameBody();//runs logic for game
	}// end of newGame()

	public void runGameBody() throws IOException {
		//declare variables
		int choiceAfterVictory;
		int choiceAfterGameOver;
		boolean decision;
		//while loop runs game
		while(!gameWon() && !gameOverNoWinner()) { //while game has not ended
			int player1Choice = player1ColumnChoice();//prompt player1 for choice
			//check if column is full
			if(checkIfColumnFull(player1Choice)) {
				player1Choice = columnIsFull(player1Choice);
			}//if column choice is full then prompt for new entry

			fillColumn(player1Choice, getPlayer1());//fill column with player1 colour

			//check if game is over
			if(gameWon() || gameOverNoWinner()) {
				if(gameWon()) {//there is a winner
					choiceAfterVictory = displayVictory();
					if (choiceAfterVictory == 0) {
						newGame();//player selected new game option
						break;
					} else {
						break; //player selected exit option
					}
				}else {//grid is full
					choiceAfterGameOver = noWinner();
					if (choiceAfterGameOver == 0) {
						clearBoard();
						newGame();  //player selected new game option
						break;
					} else {
						break; //player selected exit option
					}
				}//end of victory condition if statement
			} else { //game has not been won yet so continue:
				int player2Choice = player2ColumnChoice();

				//check if column is full
				if(checkIfColumnFull(player2Choice)) {
					player2Choice = columnIsFull(player2Choice);
				}// if column choice is full and prompt for new entry

				fillColumn(player2Choice, getPlayer2());//fill chosen column with player 2 colour

				//check again if game is over
				if(gameWon() ||gameOverNoWinner()) {
					if(gameWon()) {//there is a winner
						choiceAfterVictory = displayVictory();
						if (choiceAfterVictory == 0) {
							clearBoard();
							newGame();//player selected new game option
							break;
						} else {
							break; //player selected exit option
						}
					}else {//grid is full
						choiceAfterGameOver = noWinner();
						if (choiceAfterGameOver == 0) {
							clearBoard();
							newGame();  //player selected new game option
							break;
						} else {
							break; //player selected exit option
						}
					}//end of if/else statement
				}//end of victory condition if statement
				decision = endOfRoundOptions(); //returns a boolean - true if player selects save and exit
				if(decision) {
					saveGame();
					clearBoard();
					break;
				} //otherwise do nothing
			} //end of else statement
		}//end of while loop
	}//end of runGame()

	//****** player choice and change grid state methods ********
	public String player1Choice() {
		String player1 = "";
		String player2 = "";
		String[] options = {"Red (\uD83D\uDD34)", "Yellow (\uD83D\uDD35)"};
		String output = "";
		int player1Choice = JOptionPane.showOptionDialog(null, "Player 1, please pick your colour", "Colour Choice", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, this.icon4, options, options[0]);
		System.out.println(player1Choice);
		if(player1Choice == 0) {
			player1 = "red";//player clicked red
			player2 = "yellow";
		} else {
			player1 = "yellow";//player clicked yellow
			player2 = "red";
		}
		output += "Player 1 has chosen " + player1 + "(\uD83D\uDD34)" + "\nPlayer 2, your color is " + player2 + "(\uD83D\uDD35)" + "\nHave fun!";
		JOptionPane.showMessageDialog(null, output, "Colours Decided!", JOptionPane.INFORMATION_MESSAGE, this.icon4);
		return player1;
	}//end of player1Choice()

	public void assignPlayer2Colour(String player1) {
		if(player1 == "red") {
			setPlayer2("yellow");
		} else {
			setPlayer2("red");
		}//assigns player 2 the opposite colour
	}//end of assignPlayer2Colour()

	public int welcomeMessage() {
		String output = "";
		output += "Welcome to Connect 4!" +"\nThe rules are simple, the first player to connect four in a row wins!";
		output += "\nFor each of your turns, select the column you would like to drop your color counter into.";
		output += "\nThe game ends when one of you has won, or the play grid is full";
		String[] options = {"New Game(standard)", "Load Game", "Custom game"};
		int choice = JOptionPane.showOptionDialog(null, output, "Welcome to Connect Four!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, this.connnect4, options, null);
		return choice;
	}//end of welcomeMessage

	public int player1ColumnChoice() {
		//get state of grid
		String output = gridStateToString();
		//display colour based on choice
		String diskChar = "";
		String title = "Player 1 - ";
		ImageIcon disk = new ImageIcon();
		if (getPlayer1() == "red") {
			disk = red;
			diskChar = "\uD83D\uDD34";
			title += "Red";
		} else {
			disk = yellow;
			diskChar = "\uD83D\uDD35";
			title += "Yellow";
		}//capitalise player choices for sake of display and show their chosen disk representation

		String[] options = new String[getNumOfColumns()];
		for(int counter = 0; counter < getNumOfColumns(); counter++) {
			options[counter] = Integer.toString(counter+1);
		}//populates options based on size of grid
		output += "\n   Player 1(" + diskChar + ") - Select your column!";
		int columnChoice = JOptionPane.showOptionDialog(null, output, title, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, disk, options, null);

		System.out.println(columnChoice);
		return columnChoice;
	}//end of player1ColumnChoice

	public int player2ColumnChoice() {
		//get state of grid
		String output = gridStateToString();
		//display colour based on choice
		String diskChar = "";
		String title = "Player 2 - ";
		ImageIcon disk = new ImageIcon();
		if (getPlayer2() == "red") {//remind player of choice in display
			disk = red;
			diskChar = "\uD83D\uDD34";
			title += "Red"; 
		} else {
			disk = yellow;
			diskChar = "\uD83D\uDD35";
			title += "Yellow";
		}//capitalise player choices for sake of display and show their chosen disk representation

		String[] options = new String[getNumOfColumns()];
		//for loop to create options
		for(int counter = 0; counter < getNumOfColumns(); counter++) {
			options[counter] = Integer.toString(counter+1);
		}//create as many button options as there are columns
		output += "\n   Player 2(" + diskChar + ") - Select your column!";
		int columnChoice = JOptionPane.showOptionDialog(null, output, title, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, disk, options, null);
		System.out.println(columnChoice);
		return columnChoice; 
	}//end of player2ColumnChoice

	public int currentColumnCount() {
		int columnCount = getNewGrid().getTheGrid().peek().getTheRow().size();
		return columnCount;
	}//access current grid object to find number of columns

	public int columnIsFull(int choice) {
		//get board state
		String output = gridStateToString();
		//set message icon
		ImageIcon icon4 = new ImageIcon("4icon.png");
		String newOutput = output + "\nColumn " + (choice + 1) + " is full! Please select another column";//create output string
		String[] options = new String[getNumOfColumns()];//create new String array as long as the numOfColumns

		//for loop to create options buttons
		for(int counter = 0; counter < getNumOfColumns(); counter++) {
			options[counter] = Integer.toString(counter+1);
		}//create as many button options as there are columns
		int newChoice = JOptionPane.showOptionDialog(null, newOutput, "Column is Full!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, icon4, options, options[0]);

		while(newChoice == choice || checkIfColumnFull(newChoice)) {
			String newOutput2 = output + "\nColumn " + (newChoice + 1) + " is full! Please select another column";
			newChoice = JOptionPane.showOptionDialog(null, newOutput2, "Column is Full!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, icon4, options, options[0]);
		}//end of validation loop

		return newChoice;
	}//end of columnIsFull

	public boolean checkIfColumnFull(int column){
		boolean columnFull = false;
		if (!getNewGrid().getTheGrid().get(0).getTheRow().get(column).isEmpty()) { //selects specified column to check top square state
			columnFull = true;//if top square is filled returns true
		}
		return columnFull;
	}//end of checkIfColumnFull

	public void fillColumn(int column, String colour){
		//only called after checkIfColumnFull so no need for validation here
		int rowIndex = 0; 
		//iterate over rows in grid
		for(Row tempRow : getNewGrid().getTheGrid()){
			boolean state = tempRow.getTheRow().get(column).isEmpty();//access state of desired square
			if(state){
				//do nothing if square empty
				if(rowIndex == getNumOfRows() - 1){
					fillSquareInColumn(rowIndex, column, colour);
				}//triggers if reaches bottom of column
			} else{
				fillSquareInColumn(rowIndex - 1, column, colour);//triggers if it finds a filled square and fills square above it
				break;//break the loop
			}//end of if/else statement
			rowIndex++;
		}//end of loop
	}//end of fillColumn()

	public void fillSquareInColumn(int rowIndex, int column, String colour) {//tidy up method to avoid repeating lengthy line below
		getNewGrid().getTheGrid().get(rowIndex).getTheRow().get(column).setFilled(colour);
	}//end of fillSquareInColumn

	//checks if grid is full
	public boolean gameOverNoWinner() {
		boolean isGameOver = false;
		String rowState = "";
		for (Square tempSquare : getNewGrid().getTheGrid().get(0).getTheRow()) {
			rowState += tempSquare.returnState();
		}//stores the state of the squares in the top row

		if (rowState.contains("0")) {
			return isGameOver;
		} else {
			isGameOver = true;
			return isGameOver;
		}//checks if there are any free squares left in top row, if not it's game over
	}//end of gameOverNoWinner()

	public boolean endOfRoundOptions() {
		//get grid state
		String output = gridStateToString();
		boolean decision = false;
		String[] options = {"Next Round!", "Save and Exit"};
		int choice = JOptionPane.showOptionDialog(null, output, "Game State", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, this.icon4, options, options[0]);
		System.out.println("Player choice: " + choice);
		//if statement based on choice
		if(choice == 0) {//user chose next round
			return decision;
		} else{//user chose save and exit
			decision = true;
		}
		return decision;
	}//end endOfRoundOptions()

	public int displayVictory() {
		String output = gridStateToString();
		String winner = whoWon(getPlayer1());
		//shows window declaring who won
		output += "\n" + winner + " has won!";
		String[] options = {"New Game!", "Exit"};
		int choice = JOptionPane.showOptionDialog(null, output, "Winner!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, this.icon4, options, options[0]);
		return choice;
	}//end of displayVictory()

	public int noWinner() {
		//get grid state
		String output = gridStateToString();
		output += "\n" + "Game Over! No winner this time! Would you like to play again?";
		String[] options = {"New Game!", "Exit"};//options for player
		int choice = JOptionPane.showOptionDialog(null, output, "Game Over!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, this.icon4, options, options[0]);

		return choice;
	}//end of noWinner()

	//methods for custom game option
	public int customChoiceColumns() {
		String output = "What size of grid would you like?\nSelect number of columns!";
		int choice = Integer.parseInt(JOptionPane.showInputDialog(output));

		while(choice < 1) {
			choice = Integer.parseInt(JOptionPane.showInputDialog("Too little! " + output));
		}//end of validation loop

		return choice;
	}//end of customChoiceColumns()

	public int customChoiceRows() {
		String output = "Now select the number of rows!";
		int choice = Integer.parseInt(JOptionPane.showInputDialog(output));

		while(choice < 1) {
			choice = Integer.parseInt(JOptionPane.showInputDialog("Too little! " + output));
		}
		return choice;
	}//end of customChoiceRows()

	public void createCustomGrid() {
		//prompt for and store custom sizes
		setNumOfColumns(customChoiceColumns());
		setNumOfRows(customChoiceRows());
		setPlayer1(player1Choice());
		if(getPlayer1() == "red") {
			setPlayer2("yellow");
		} else {
			setPlayer2("red");
		}//store player colour choices

		//edit newGrid with custom values
		setNewGrid(numOfRows, numOfColumns);
	}//end of createCustomGrid()

	public void clearBoard() {
		//resets standard values
		setNumOfColumns(getSTANDARDCOLUMNS());
		setNumOfRows(getSTANDARDROWS());
		setNewGrid(getNumOfRows(), getNumOfColumns());
	}//end of clearBoard

	//******** save and load game methods
	public void saveGame(){
		//create one LinkedList to store string representations of our necessary data
		LinkedList<String> squareStates = new LinkedList<String>();

		//convert necessary data to string
		String stringColumns = Integer.toString(getNumOfColumns()); 
		String stringRows = Integer.toString(getNumOfRows());

		squareStates.addAll(saveSquareStatesString());//saves square states in string form
		squareStates.push(stringColumns);
		squareStates.push(stringRows);
		squareStates.push(getPlayer1());
		//store players colour assignments, size of grid, and states of squares in order

		try {
			FileOutputStream fileOut = new FileOutputStream("gameState.ser");//save to one file
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(squareStates);
			out.close();
			fileOut.close();

		} catch(IOException i){
			i.printStackTrace();
		}
	}//end of saveGame()

	//De-serialize data
	public LinkedList<String> loadGame(){
		//create new LinkedList to store load data
		LinkedList<String> loadedStates = new LinkedList<String>();

		try {
			FileInputStream fileIn = new FileInputStream("gameState.ser");//access save file with data
			ObjectInputStream in = new ObjectInputStream(fileIn);
			loadedStates = (LinkedList<String>) in.readObject();//store in our new list
			in.close();
			fileIn.close();//close data file
		} catch(IOException IOException){
			IOException.printStackTrace();
		} catch(ClassNotFoundException CNFException){
			CNFException.printStackTrace();
		}//exceptions in case of error
		return loadedStates;
	}//end of loadGame()

	public void reloadData() {
		//declare variables
		LinkedList<String> loadState = loadGame();
		setPlayer1(loadState.removeFirst());

		//reload player colour choices
		if(getPlayer1().equals("red")) { 
			setPlayer1("red");
			setPlayer2("yellow");
		} else if(getPlayer1().equals("yellow")) {
			setPlayer1("yellow");
			setPlayer2("red");
		}//reassign using if statements as .removeFirst() creates a reference not a string

		//reassign saved grid size 
		setNumOfRows(Integer.parseInt(loadState.removeFirst()));
		setNumOfColumns(Integer.parseInt(loadState.removeFirst()));
		setNewGrid(getNumOfRows(), getNumOfColumns());
		reloadFromSquare(loadState);//edit newGrid with save square states
	}//end of reloadData()

	//saves states all all squares as a string character
	public LinkedList<String> saveSquareStatesString() {//for use when saving game
		LinkedList<String> squareStates = new LinkedList<String>();
		for(Row tempRow: getNewGrid().getTheGrid()){
			for(Square tempSquare: tempRow.getTheRow()) {
				squareStates.add(tempSquare.returnState());
			}//end of inner for loop
		}//end of for loop
		return squareStates;
	}//end of saveSquareStatesString()

	//loads all saved square states 
	public void reloadFromSquare(LinkedList<String> squareStates) {//for use when loading game
		for(Row tempRow : getNewGrid().getTheGrid()) {
			for(Square tempSquare : tempRow.getTheRow()) {//iterates over all squares in new grid to change their states based on saved List
				if(squareStates.getFirst().equals("R")) {
					tempSquare.setFilled("red");
				} else if(squareStates.getFirst().equals("Y")) {
					tempSquare.setFilled("yellow");
				} //end of if/else
				squareStates.pop();
			}//end of inner for loop
		}//end of for loop
	}//end of reloadFromSquare()

	//***************************************

	//methods to return string representation of grid
	public String gridStateToString(){//returns string representation of grid
		String gridState = "";
		String fullGridState = "";
		for(Row tempRow: getNewGrid().getTheGrid()){
			gridState += "\n";
			for(Square tempSquare: tempRow.getTheRow()) {
				String temp = tempSquare.returnState();
				if(temp.equals("0")) {
					gridState += "\uD83D\uDD33"; //empty square character
				}else if(temp.equals("R")) {
					gridState += "\uD83D\uDD34"; // black circle character
				} else if(temp.equals("Y")) {
					gridState += "\uD83D\uDD35"; //striped circle character
				}
				gridState += "     ";//appropriate spacing
			};
		}//end of for loop
		fullGridState += finaliseGridStateToString(gridState);
		return fullGridState;
	}//end of checkGrid()

	public String finaliseGridStateToString(String gridState) {
		String columnNumbers = " ";
		for(int counter = 0; counter < getNumOfColumns(); counter++) {
			if(counter < 9) {
				columnNumbers += Integer.toString(counter+1);
				columnNumbers += "      ";//appropriate spacing
			} else {
				columnNumbers += Integer.toString(counter+1);
				columnNumbers += "    ";//less spacing needed for double digit numbers
			}
		}//end of for loop
		columnNumbers += gridState;
		return columnNumbers;
	}//end of finaliseGridStateToString()

	//********win conditions**********
	public boolean gameWon(){
		return checkRows() || checkColumns() || checkDiagonally();//triggers if any below win condition is true
	}//end of gameWon()

	//checks for horizontal victory
	public boolean checkRows(){
		//declare variables
		String squareStates = rowsToString();//returns string representation of all row square states - see below
		String redWins = "RRRR";
		String yellowWins = "YYYY";
		boolean winner = false;
		//check for winner
		if(squareStates.contains(redWins) || squareStates.contains(yellowWins)){//triggers if "RRRR" or "YYYY" found in square states
			winner = true;
		} 
		return winner;
	}//end of checkRows()

	//checks for vertical victory
	public boolean checkColumns(){
		//declare variables
		String squareStates = columnsToString();//returns string representation of all column square states - see below
		boolean winner = false;
		String redWins = "RRRR";
		String yellowWins = "YYYY";
		//check for winner
		if(squareStates.contains(redWins) || squareStates.contains(yellowWins)){
			winner = true;
		} 
		return winner;
	}//end of checkColumns()

	public String singleColumnToString(int rowIndex, int columnIndex) {
		String rowState = "";
		rowState += getNewGrid().getTheGrid().get(rowIndex).getTheRow().get(columnIndex).returnState();
		return rowState;
	}//end of columnToString()


	//checks for diagonal victory
	public boolean checkDiagonally(){
		//declare variables
		String squareStates = diagonallyToString();//returns string representation of all diagonal square states - see below
		boolean winner = false;
		String redWins = "RRRR";
		String yellowWins = "YYYY";
		if(squareStates.contains(redWins) || squareStates.contains(yellowWins)){//if there is a winner
			winner = true;
		} 
		return winner;
	}//end of checkDiagonally()

	//*********win responses*********

	//checks what player won when win condition has triggered
	public String whoWon(String player1) {
		String winner = "";
		String squareStates = "";
		if(checkRows()) { //if it was a horizontal victory
			squareStates = rowsToString();
			winner = assignWinner(squareStates, player1); // assign winner based on colour player 1 picked
		} else if(checkColumns()) { //if a vertical victory
			squareStates += columnsToString();
			winner = assignWinner(squareStates, player1); //assign winner
		} else if(checkDiagonally()) {
			squareStates += diagonallyToString();
			winner = assignWinner(squareStates, player1);//assign winner
		}//end of if-else statements
		return winner;
	}//end of whoWon()

	//utility function for use in whoWon()
	public String assignWinner(String squareStates, String player1) {
		String winner = "";
		String redWins = "RRRR";
		String yellowWins = "YYYY";
		if(squareStates.contains(redWins)){//triggers if "RRRR" found in square states
			if(player1 == "red") {
				winner = "Player 1";
			} else {
				winner = "Player 2";
			}//end of inner if/else
		} else if (squareStates.contains(yellowWins)) {//triggers if "YYYY" found in square states
			if(player1 == "yellow") {
				winner = "Player 1";
			} else {
				winner = "Player 2";
			}//end of inner if/else
		}//end of if/else statement
		return winner;
	}//end of assignWinner()

	//****methods to return string states of win cons*****
	public String rowsToString() {
		String squareStates = "";
		for(Row tempRow : getNewGrid().getTheGrid()){//saves all horizontal states to a string
			squareStates += ".";
			for(Square tempSquare : tempRow.getTheRow()){
				squareStates += tempSquare.returnState();
			}//end of inner loop
		} //end of for loop
		return squareStates;
	}//end of rowsToString()

	public String columnsToString() {
		String squareStates = "";
		for(int columnIndex = 0; columnIndex < getNumOfColumns(); columnIndex ++){//saves all square states to a string
			squareStates += ".";
			int rowIndex = 0;
			for(Row tempRow : getNewGrid().getTheGrid()){
				squareStates += singleColumnToString(rowIndex, columnIndex);
				rowIndex++;
			}//end of inner for loop
		}//end of for loop
		return squareStates;
	}//end of columnsToString()

	public String diagonallyToString() {//used in diagonal win condition
		//for determining who won after a diagonal victory
		String squareStates = "";
		//check diagonals to the right
		for(int modifier = 1; modifier < getNumOfColumns() - 1; modifier ++) {
			for(int counter = 0; counter < (getNumOfColumns() - 1); counter++){//scalable works for any size of grid due to this. references
				int squareIndex = counter;
				squareStates += ".";
				for(int rowIndex = (getNumOfRows() - modifier); rowIndex >= 0; rowIndex--){
					squareStates += singleColumnToString(rowIndex, squareIndex);
					squareIndex++;
					if(squareIndex >= getNumOfColumns()){//to avoid index going out of bounds
						break;
					}
				}//end of inner loop
			}//end of right loop

			//check diagonals to the left
			for(int counter = getNumOfColumns() -1; counter > -1; counter--){//scalable works for any size of grid due to this. references
				int squareIndex = counter;
				squareStates += ".";
				for(int rowIndex = (getNumOfRows()-modifier); rowIndex >= 0; rowIndex--){
					squareStates += singleColumnToString(rowIndex, squareIndex);;
					squareIndex--;
					if(squareIndex <0){//avoid index going out of bounds
						break;
					}
				}//end of inner loop
			}//end of left loop
		}//loop checks left and right for every row index - scalable and works for any size of grid
		return squareStates;
	}//end of diagonallyToString()

	//getters and setters

	public String getPlayer1() {
		return this.player1;
	}

	public void setPlayer1(String colour) {
		this.player1 = colour;
	}

	public String getPlayer2() {
		return this.player2;
	}

	public void setPlayer2(String colour) {
		this.player2 = colour;
	}

	public int getNumOfColumns() {
		return this.numOfColumns;
	}

	public void setNumOfColumns(int numOfColumns) {
		this.numOfColumns = numOfColumns;
	}

	public int getNumOfRows() {
		return this.numOfRows;
	}

	public void setNumOfRows(int numOfRows) {
		this.numOfRows = numOfRows;
	}

	public Grid getNewGrid() {
		return newGrid;
	}

	public void setNewGrid(int row, int columns) {
		this.newGrid = new Grid(row, columns);
	}

	//getters only for final ints
	public int getSTANDARDCOLUMNS() {
		return this.STANDARDCOLUMNS;
	}

	public int getSTANDARDROWS() {
		return this.STANDARDROWS;
	}
	//end of getters and setters
}//end of Game class
