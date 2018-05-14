package chess;

import piece.*;

/**
 * Represents the chess game board
 * 
 * @author Robert Barbaro
 * 
 * @author Christopher Gonzalez
 */
public class Board {
	
	/**
	 * 2D array that contains all the pieces for the game board
	 */
	public static Piece[][] gameBoard = new Piece[8][8];
	
	/**
	 * @param row input row of 2D array, from 0 to 7
	 * @param col input column of 2D array, from 0 to 7
	 * @return returns true if index is in bounds and returns false if index is not null
	 */
	public static boolean isIndexInBounds(int row, int col) {
		
		if(!((row >= 0 && row <= 7) &&  (col >= 0 && col <= 7))) {  // checks if input is in bounds
			
			return false;  // not in bounds; return false
		}
		
		return true;
		
	}
	
	
	
	/**
	 * @param row input row of 2D array, from 0 to 7
	 * @param col input column of 2D array, from 0 to 7
	 * @return returns true if index is null and returns false if index is not null
	 */
	public static boolean isIndexNull(int row, int col) {  

		
		if(gameBoard[row][col] == null) { // if index is null return true; else return false
			
			return true;
		}
		
		return false;
		
	}
	
	
	/**
	 * @param row input row of 2D array, from 0 to 7
	 * @param col input column of 2D array, from 0 to 7
	 * @return returns a String of piece name at that index and if index is empty, return empty String
	 */
	public static String getPieceName(int row, int col) {  
		
		if(!((row >= 0 && row <= 7) &&  (col >= 0 && col <= 7))) {  // checks if input is in bounds
			
			return null;
		}
		
		if(gameBoard[row][col] != null) {  // if index is not null return the piece name; else return empty null
			
			return gameBoard[row][col].getPieceName();
			
		} 
		
		return null;
		
	}
	
	/**
	 * @param row input row of 2D array, from 0 to 7
	 * @param col input column of 2D array, from 0 to 7
	 * @return returns piece object that is at index. returns null if index does not contain a piece. 
	 */
	public static Piece getPiece(int row, int col) {
		
		return gameBoard[row][col];  // returns null if there is no piece in location
		
	}
	
	/**
	 * Sets up board with all the chess pieces in their starting position
	 */
	public static void setUpBoard() {
		
		
		for(int i = 0; i < 8; i++) {  // setup row of black pawns
			
			Piece pawn = new Pawn("bp");
			gameBoard[1][i] = pawn;
		}
		
		// setup the rest of black pieces
		gameBoard[0][0] = new Rook("bR");
		gameBoard[0][1] = new Knight("bN");
		gameBoard[0][2] = new Bishop("bB");
		gameBoard[0][3] = new Queen("bQ");
		gameBoard[0][4] = new King("bK");
		gameBoard[0][5] = new Bishop("bB");
		gameBoard[0][6] = new Knight("bN");
		gameBoard[0][7] = new Rook("bR");
		
		
		
		for(int i = 0; i < 8; i++) {  // setup row of white pawns
			
			Piece pawn = new Pawn("wp");
			
			gameBoard[6][i] = pawn;
		}
		
		// setup the rest of white pieces
		gameBoard[7][0] = new Rook("wR");
		gameBoard[7][1] = new Knight("wN");
		gameBoard[7][2] = new Bishop("wB");
		gameBoard[7][3] = new Queen("wQ");
		gameBoard[7][4] = new King("wK");
		gameBoard[7][5] = new Bishop("wB");
		gameBoard[7][6] = new Knight("wN");
		gameBoard[7][7] = new Rook("wR");
		
	}
	
	
	/**
	 *  Prints the current state of the board in ascii characters 
	 */
	public static void printBoard() {
		
		int rowCount = 8;  // holds the current row being printed
		
		boolean printShceme = true;  // boolean value that determines the way the black space should be printed for every other row
		
		for(int i = 0; i < 8; i++) {  // loop through the array row
			
			for(int j = 0; j < 8; j++) {  // loop through the array column
				
				if(gameBoard[i][j] != null) {  // if game piece exists at index, print it
				
					System.out.print(gameBoard[i][j].toString());
					
				} else if(printShceme) {  // print black spaces 
					
					
						if(i % 2 != 0) {
							
							System.out.print("##");
						} else {
							
							System.out.print("  ");
						}
						
						
										
				} else {  // print black spaces
					
					
					if(i % 2 == 0) {
						
						System.out.print("##");
					} else {
						 
						System.out.print("  ");
					}
						
					
				}
				
				System.out.print(" ");
				
				printShceme = !printShceme;  // change print scheme for every other row
				
				
			}
			
			System.out.print(rowCount);  // print out row number at the end of each row
			rowCount--;
			
			System.out.println();
		}
		
		System.out.print(" a");
		
		for(char i = 'b'; i < 105; i++) {  // print the column letter at the bottom of each column
			
			System.out.print("  " + i);
			
		}
		
		System.out.println();
		System.out.println();
	}
	
	
	
	/**
	 * @param move input the move entered by the user
	 * @param isWhiteTurn true if it is white's turn and false if it is black's turn
	 * @return returns true if it is legal move
	 */
	public static boolean movePiece(String move, boolean isWhiteTurn) {
		
		String[] input = move.split(" ");  // split the input statements in array
		
		
		if(input.length < 2) {  // not enough inputs
			
			return false;
		}
		
		
		if(input[0].length() != 2 || input[1].length() != 2) {  // improper input size
			
			return false;
		}
		
		
		int pieceCol = Character.getNumericValue( input[0].charAt(0) ) - 10; //  translate input to column index of piece
		int pieceRow = 8 - Character.getNumericValue( input[0].charAt(1) ); //  translate input to row index of piece
		
		int moveCol = Character.getNumericValue( input[1].charAt(0) ) - 10; //  translate input to column index of move
		int moveRow = 8 - Character.getNumericValue( input[1].charAt(1)) ; //  translate input to row index of move
		
		if(!isInBounds(move)) {
			
			return false;
		}
		
		if(gameBoard[pieceRow][pieceCol] == null) {  // if there is no piece at index; illegal move
			
			return false;
		}
		
		if(isWhiteTurn && getPieceName(pieceRow,pieceCol).charAt(0) != 'w') { // if it is white turn and piece attempting to be moved is not white
			
			return false;  // cannot move opposite teams piece; return false
		}
		
		if(!isWhiteTurn && getPieceName(pieceRow,pieceCol).charAt(0) != 'b') {  // if it is black turn and piece attempting to be moved is not white
			
			return false;  // cannot move opposite teams piece; return false
		}
		
		if(!gameBoard[pieceRow][pieceCol].checkMove(move)) {  // call checkMove() for individual piece to see if move is legal
			
			return false;
		}
		
		if(gameBoard[pieceRow][pieceCol].isCheck(move)) {
			
			System.out.println("Check");
			System.out.println();
		}
		
		//castling move
		if (getPieceName(pieceRow, pieceCol).charAt(1) == 'K')
		{
			//kingside/short castling
			if (moveCol - pieceCol == 2 && moveRow == pieceRow)
			{
				//move king first
				gameBoard[moveRow][moveCol] = gameBoard[pieceRow][pieceCol];
				gameBoard[pieceRow][pieceCol] = null;
				
				//move rook
				gameBoard[moveRow][moveCol - 1] = gameBoard[moveRow][moveCol + 1];
				gameBoard[moveRow][moveCol + 1] = null;
				return true;
			}
			//queenside/long castling
			if (moveCol - pieceCol == -2 && moveRow == pieceRow)
			{
				//move king first
				gameBoard[moveRow][moveCol] = gameBoard[pieceRow][pieceCol];
				gameBoard[pieceRow][pieceCol] = null;
				
				//move rook
				gameBoard[moveRow][moveCol + 1] = gameBoard[moveRow][moveCol - 2];
				gameBoard[moveRow][moveCol - 2] = null;
				return true;
			}
		}
		
		// if a pawn reaches the other end of the board, they are promoted to either a Queen
		// (by default) or a piece of the user's choosing between Queen, Knight, Rook or Bishop
		if (canBePromoted(pieceCol, pieceRow, moveCol, moveRow))
		{
			if (input.length == 3)
			{
				char promoteTo = input[2].charAt(0);
				switch (Character.toUpperCase(promoteTo))
				{
				case 'Q':
					gameBoard[moveRow][moveCol] 
							= (getPieceName(pieceRow, pieceCol).charAt(0) == 'w') ? new Queen("wQ") : new Queen("bQ");
					gameBoard[pieceRow][pieceCol] = null;
					break;
				case 'N':
					gameBoard[moveRow][moveCol] 
							= (getPieceName(pieceRow, pieceCol).charAt(0) == 'w') ? new Knight("wN") : new Knight("bN");
					gameBoard[pieceRow][pieceCol] = null;
					break;
				case 'R':
					gameBoard[moveRow][moveCol] 
							= (getPieceName(pieceRow, pieceCol).charAt(0) == 'w') ? new Rook("wR") : new Rook("bR");
					gameBoard[pieceRow][pieceCol] = null;
					break;
				case 'B':
					gameBoard[moveRow][moveCol] 
							= (getPieceName(pieceRow, pieceCol).charAt(0) == 'w') ? new Bishop("wB") : new Bishop("bB");
					gameBoard[pieceRow][pieceCol] = null;
					break;
				default:
					return false;
				}
			} else {
				gameBoard[moveRow][moveCol] 
						= (getPieceName(pieceRow, pieceCol).charAt(0) == 'w') ? new Queen("wQ") : new Queen("bQ");
				gameBoard[pieceRow][pieceCol] = null;
			}
			
			return true;
		}
		
		// if move is legal; put piece in new location
		Piece temp =  gameBoard[pieceRow][pieceCol];
		
		gameBoard[pieceRow][pieceCol] = null;
		
		gameBoard[moveRow][moveCol] = temp;
		
		return true;
		
		
	}
	
	/**
	 * @param row input row of 2D array, from 0 to 7
	 * @param col input column of 2D array, from 0 to 7
	 */
	public static void setIndexNull(int row, int col) {
		
		gameBoard[row][col] = null;
	}
	
	/**
	 * @param move move inputed by user
	 * @param isWhiteTurn true if it is white's turn and false if it is black's turn
	 * @return returns true if resign is inputed by user
	 */
	public static boolean isResigning(String move, boolean isWhiteTurn) {
		
		String[] input = move.split(" ");  // splits the input
		
		if(input[0].equals("resign")) {
			
			if(isWhiteTurn) {
				
				System.out.println("Black wins");
				
			} else {
				
				System.out.println("White wins");
				
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * @param move inputed by user
	 * @return returns true if user inputs "draw?"
	 */
	public static boolean isDrawInitiated(String move) {
		
		String[] input = move.split(" ");  // splits the input
		
		if(input.length >= 3) {
			
			if(input[2].equals("draw?")) {
				
				return true;
				
			}
		}
		
		return false;
	}
	
	/**
	 * @param move inputed by user
	 * @return returns true if "draw" is inputed by user
	 */
	public static boolean isDrawAccepted(String move) {
		
		String[] input = move.split(" ");  // splits the input
		
		if(input[0].equals("draw")) {
			
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * @return returns true either the white king or black king is no longer on board
	 */
	public static boolean isKingCaptured() {
		
		boolean whiteKing = false;  // value tells whether the king was found on board
		boolean blackKing = false;
		
				
		for(int row = 0; row < 8; row++) {
			for(int col = 0; col < 8; col++) {
					
				if(!Board.isIndexNull(row, col) && gameBoard[row][col].getPieceName().equals("bK")) {
						
					blackKing = true;
				}
					
				if(!Board.isIndexNull(row, col) && gameBoard[row][col].getPieceName().equals("wK")) {
						
					whiteKing = true;
				}
			}
		}
			
		
		if(!whiteKing) {
			
			System.out.println("Black wins");
			return true;
		}
		
		if(!blackKing) {
			
			System.out.println("White wins");
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * @return true if a king is in checkmate; false if no kings are in checkmate
	 */
	public static boolean isCheckmate()
	{
		int bKingRow = -1;
		int bKingCol = -1;
		boolean bKingInCheck = false;
		
		int wKingRow = -1;
		int wKingCol = -1;
		boolean wKingInCheck = false;
		
		//find the kings
		for(int row = 0; row < 8; row++)
		{
			for(int col = 0; col < 8; col++)
			{
				if(!Board.isIndexNull(row, col) && gameBoard[row][col].getPieceName().equals("bK")) {
					bKingRow = row;
					bKingCol = col;
				}
					
				if(!Board.isIndexNull(row, col) && gameBoard[row][col].getPieceName().equals("wK")) {
					wKingRow = row;
					wKingCol = col;
				}
			}
		}
		
		//if the kings aren't found for some reason (should never happen)
		if (bKingRow == -1 && bKingCol == -1)
		{
			System.out.println("White wins");
			return true;
		} else if (wKingRow == -1 && wKingCol == -1)
		{
			System.out.println("Black wins");
			return true;
		}
		
		bKingInCheck = isKingInCheck('w');
		wKingInCheck = isKingInCheck('b');

		//place temporary kings to represent possible spaces king can move and check if they're under check as well
		if (bKingInCheck)
		{
			Piece tempKing = gameBoard[bKingRow][bKingCol];
			gameBoard[bKingRow][bKingCol] = null;
			for(int row = bKingRow - 1; row < bKingRow + 2; row++)
			{
				for (int col = bKingCol - 1; col < bKingCol + 2; col++)
				{
					if (Board.isIndexInBounds(row, col) && Board.isIndexNull(row, col))
					{
						gameBoard[row][col] = tempKing;
						
						//king is not in checkmate, reset state to before checkmate check
						if (!isKingInCheck('w'))
						{
							gameBoard[row][col] = null;
							gameBoard[bKingRow][bKingCol] = tempKing;
							return false;
						}
						
						gameBoard[row][col] = null;
					}
				}
			}
			gameBoard[bKingRow][bKingCol] = tempKing;
			
			System.out.println();
			System.out.println("Checkmate");
			System.out.println();
			System.out.println("White wins");
			System.out.println();
			
			return true;
		} else if (wKingInCheck) {
			Piece tempKing = gameBoard[wKingRow][wKingCol];
			gameBoard[wKingRow][wKingCol] = null;
			for(int row = wKingRow - 1; row < wKingRow + 2; row++)
			{
				for (int col = wKingCol - 1; col < wKingCol + 2; col++)
				{
					if (Board.isIndexInBounds(row, col) && Board.isIndexNull(row, col))
					{
						gameBoard[row][col] = tempKing;
						
						//king is not in checkmate, reset state to before checkmate check
						if (!isKingInCheck('b'))
						{
							gameBoard[row][col] = null;
							gameBoard[wKingRow][wKingCol] = tempKing;
							return false;
						}
						
						gameBoard[row][col] = null;
					}
				}
			}
			gameBoard[wKingRow][wKingCol] = tempKing;
			
			System.out.println();
			System.out.println("Checkmate");
			System.out.println();
			System.out.println("Black wins");
			System.out.println();
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * @param color input color of the piece
	 * @return true if the king is in check; false if the king isn't in check
	 */
	private static boolean isKingInCheck(char color)
	{
		for(int row = 0; row < 8; row++)
		{
			for(int col = 0; col < 8; col++)
			{
				if (!Board.isIndexNull(row, col) && gameBoard[row][col].getPieceName().charAt(1) != 'K')
				{
					//check around piece's current position to see if a king is in check from the piece
					String move = convertIndexToChar(col) + "" + (8 - row) + " " + convertIndexToChar(col) + "" + (8 - row);
					if (gameBoard[row][col].isCheck(move))
					{
						if (gameBoard[row][col].getPieceName().charAt(0) == color)
						{
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * @param move inputed by user
	 * @return returns true if the move is on the game board; false if move is out of bounds
	 */
	public static boolean isInBounds(String move) {   // checks if move is on game board
		
		String[] input = move.split(" ");  // splits the input
		
		char pieceChar = input[0].charAt(0); // gets the column letter for piece
		char moveChar = input[1].charAt(0);  // gets the column letter for move
		
		int pieceNum = Character.getNumericValue( input[0].charAt(1) );  // gets the row number for piece
		int moveNum = Character.getNumericValue( input[1].charAt(1) );  // gets the row number for move
		
		if(!((pieceChar >= 97 && pieceChar <= 104) &&  (moveChar >= 97 && moveChar <= 104))) {  // checks if input letters are not between a - h using ascii
			
			return false;
		}
		
		if(!((pieceNum >= 1 && pieceNum <= 8) &&  (moveNum >= 1 && moveNum <= 8))) {  // checks if the input numbers are not between 1 - 8
			
			return false;
		}
		

		return true;  // returns true if row or column is in bounds
	}
	
	/**
	 * @param currentCol current column of piece
	 * @param currentRow current row of piece
	 * @param destinationCol column where piece is being moved
	 * @param destinationRow row where piece is being moved
	 * @return returns true if a pawn is eligible for promotion; false if the pawn is ineligible for promotion
	 */
	private static boolean canBePromoted(int currentCol, int currentRow, int destinationCol, int destinationRow)
	{
		if (getPieceName(currentRow, currentCol).charAt(1) == 'p')
		{
			if (getPieceName(currentRow, currentCol).charAt(0) == 'w')
			{
				if (destinationRow == 0)
				{
					return true;
				}
			} else if (getPieceName(currentRow, currentCol).charAt(0) == 'b')
			{
				if (destinationRow == 7)
				{
					return true;
				}
			}
		}

		return false;
	}
	
	/**
	 * @param index insert column index
	 * @return the 'column' index of the 2d array converted into a letter that
	 * 		corresponds to a column on the board
	 */
	private static char convertIndexToChar(int index)
	{
		char column = ' ';
		
		switch (index)
		{
			case 0:
				column = 'a';
				break;
			case 1:
				column = 'b';
				break;
			case 2:
				column = 'c';
				break;
			case 3:
				column = 'd';
				break;
			case 4:
				column = 'e';
				break;
			case 5:
				column = 'f';
				break;
			case 6:
				column = 'g';
				break;
			case 7:
				column = 'h';
				break;
		}
		
		return column;
	}

}
