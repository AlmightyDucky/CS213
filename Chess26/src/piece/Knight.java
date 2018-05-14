package piece;

import chess.Board;

/**
 * Represents the Knight piece
 * 
 * @author Robert Barbaro
 * 
 * @author Christopher Gonzalez
 */

public class Knight extends Piece {
	
	/**
	 * @param pieceName input the name of the piece
	 */
	public Knight(String pieceName) {
		
		super(pieceName);
	}

	/* (non-Javadoc)
	 * @param move inputed by user
	 * @return return true if move is legal; false if move is illegal
	 */
	@Override
	public boolean checkMove(String move) { 
		
		String[] input = move.split(" ");  // splits the users input
		
		int pieceCol = Character.getNumericValue( input[0].charAt(0) )-10; //  translate input to column index of piece
		int pieceRow = 8 - Character.getNumericValue( input[0].charAt(1) ); //  translate input to row index of piece
		
		int moveCol = Character.getNumericValue( input[1].charAt(0) )-10; //  translate input to column index of move
		int moveRow = 8 - Character.getNumericValue( input[1].charAt(1)); //  translate input to row index of move
		
		if((pieceCol + 2 == moveCol) || (pieceCol - 2 == moveCol)) {  // check if knight is moving either left or right by 2 spaces
			
			if((pieceRow - 1 == moveRow) || (pieceRow + 1 == moveRow)) {  // check if knight is moving up or down by 1 space
				
				return checkMoveLoc(move);  // return true if location is null or contains piece that is of opposite color
			}
		}
		
		if((pieceRow + 2 == moveRow) || (pieceRow - 2 == moveRow)) {  // check if knight is moving either up or down by 2 spaces
			
			if((pieceCol + 1 == moveCol) || (pieceCol - 1 == moveCol)) {  // check if knight is moving left or right by 1 space
				
				return checkMoveLoc(move);  // return true if location is null or contains piece this of opposite color
			}
		}
		
		
		return false;  // return false if move is illegal
	}
	
	/**
	 * @param move inputed by user
	 * @return if move location is empty or contains piece of opposite color return true; else return false
	 */
	public boolean checkMoveLoc(String move) {
		
		String[] input = move.split(" ");  // splits the users input
		
		int pieceCol = Character.getNumericValue( input[0].charAt(0) )-10; //  translate input to column index of piece
		int pieceRow = 8 - Character.getNumericValue( input[0].charAt(1) ); //  translate input to row index of piece
		
		int moveCol = Character.getNumericValue( input[1].charAt(0) )-10; //  translate input to column index of move
		int moveRow = 8 - Character.getNumericValue( input[1].charAt(1)); //  translate input to row index of move
		
		if(Board.isIndexNull(moveRow, moveCol)) {  // if location does not contain a piece
			
			return true;  // legal move; return true
		}
		
		if(Board.getPieceName(moveRow, moveCol).charAt(0) != Board.getPieceName(pieceRow, pieceCol).charAt(0)) {  // if piece is opposite color
			
			return true;  // Knight is attempting to capture piece of opposing team; legal move
		}
		
		
		return false;  // illegal move; return false
	}

	/* (non-Javadoc)
	 * @see piece.Piece#isCheck(java.lang.String)
	 */
	@Override
	public boolean isCheck(String move) {   
		
		String[] input = move.split(" ");  // splits the users input
				
		int moveCol = Character.getNumericValue( input[1].charAt(0) )-10; //  translate input to column index of move
		int moveRow = 8 - Character.getNumericValue( input[1].charAt(1)); //  translate input to row index of move
		
		
		if(Board.isIndexInBounds(moveRow - 2, moveCol + 1) && !Board.isIndexNull(moveRow - 2, moveCol + 1)) {  // checks 2 spaces up and 1 space to the right

			if(Board.getPieceName(moveRow - 2, moveCol + 1).charAt(0) != getPieceName().charAt(0) && Board.getPieceName(moveRow - 2, moveCol + 1).charAt(1) == 'K') {
				// check if piece is opposite color; check if it is king
				
				return true;
			}	
		}
		
		if(Board.isIndexInBounds(moveRow - 2, moveCol - 1) && !Board.isIndexNull(moveRow - 2, moveCol - 1)) {  // checks 2 spaces up and 1 space to the left
			
			if(Board.getPieceName(moveRow - 2, moveCol - 1).charAt(0) != getPieceName().charAt(0) && Board.getPieceName(moveRow - 2, moveCol - 1).charAt(1) == 'K') {
				// check if piece is opposite color; check if it is king
				
				return true;
			}	
		}
		
		if(Board.isIndexInBounds(moveRow + 2, moveCol + 1) && !Board.isIndexNull(moveRow + 2, moveCol + 1)) {  // checks 2 spaces down and 1 space to the right
			
			if(Board.getPieceName(moveRow + 2, moveCol + 1).charAt(0) != getPieceName().charAt(0) && Board.getPieceName(moveRow + 2, moveCol + 1).charAt(1) == 'K') {
				// check if piece is opposite color; check if it is king
				
				return true;
			}
		}
		
		if(Board.isIndexInBounds(moveRow + 2, moveCol - 1) && !Board.isIndexNull(moveRow + 2, moveCol - 1)) {  // checks 2 spaces down and 1 space to the left
			
			if(Board.getPieceName(moveRow + 2, moveCol - 1).charAt(0) != getPieceName().charAt(0) && Board.getPieceName(moveRow + 2, moveCol - 1).charAt(1) == 'K') {
				// check if piece is opposite color; check if it is king
				
				return true;
			}
		}
		
		
		if(Board.isIndexInBounds(moveRow - 1, moveCol - 2) && !Board.isIndexNull(moveRow - 1, moveCol - 2)) {  // checks 2 spaces left and 1 space up
			
			if(Board.getPieceName(moveRow - 1, moveCol - 2).charAt(0) != getPieceName().charAt(0) && Board.getPieceName(moveRow - 1, moveCol - 2).charAt(1) == 'K') {
				// check if piece is opposite color; check if it is king
				
				return true;
			}
		}
		
		if(Board.isIndexInBounds(moveRow + 1, moveCol - 2) && !Board.isIndexNull(moveRow + 1, moveCol - 2)) {  // checks 2 spaces left and 1 space down
			
			if(Board.getPieceName(moveRow + 1, moveCol - 2).charAt(0) != getPieceName().charAt(0) && Board.getPieceName(moveRow + 1, moveCol - 2).charAt(1) == 'K') {
				// check if piece is opposite color; check if it is king
				
				return true;
			}
		}
		
		if(Board.isIndexInBounds(moveRow - 1, moveCol + 2) && !Board.isIndexNull(moveRow - 1, moveCol + 2)) {  // checks 2 spaces right and 1 space up
			
			if(Board.getPieceName(moveRow - 1, moveCol + 2).charAt(0) != getPieceName().charAt(0) && Board.getPieceName(moveRow - 1, moveCol + 2).charAt(1) == 'K') {
				// check if piece is opposite color; check if it is king
				
				return true;
			}
		}
	
		if(Board.isIndexInBounds(moveRow + 1, moveCol + 2) && !Board.isIndexNull(moveRow + 1, moveCol + 2)) {  // checks 2 spaces right and 1 space down
			
			if(Board.getPieceName(moveRow + 1, moveCol + 2).charAt(0) != getPieceName().charAt(0) && Board.getPieceName(moveRow + 1, moveCol + 2).charAt(1) == 'K') {
				// check if piece is opposite color; check if it is king
				
				return true;
			}
		}

		return false;
	}

}
