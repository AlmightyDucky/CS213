package piece;

import chess.Board;

/**
 * Represents the Pawn piece
 * 
 * @author Robert Barbaro
 * 
 * @author Christopher Gonzalez
 */


public class Pawn extends Piece {
	
	
	/**
	 * If the pawn made its first move, set variable to false
	 */
	private boolean firstMove = true;  // boolean value to check if it is first move
	
	/**
	 * If the pawn moved 2 spaces on first move, variable is set to true
	 */
	private boolean doubleMove = false;  // boolean value which is true if pawn went 2 spaces on first move
	
	/**
	 * @param pieceName input name of piece
	 */
	public Pawn(String pieceName) {
		
		super(pieceName);
	}
	
	/**
	 * @return returns the value of doubleMove
	 */
	public boolean isDoubleMove() {
		
		return doubleMove;
	}
	
	/* (non-Javadoc)
	 * @param move inputed by user
	 * @return return true if move is legal; false if move is illegal
	 */
	public boolean checkMove(String move) {
		
		String[] input = move.split(" ");  // splits the users input
				
		int pieceCol = Character.getNumericValue( input[0].charAt(0) )-10; //  translate input to column index of piece
		int pieceRow = 8 - Character.getNumericValue( input[0].charAt(1) ); //  translate input to row index of piece
		
		int moveCol = Character.getNumericValue( input[1].charAt(0) )-10; //  translate input to column index of move
		int moveRow = 8 - Character.getNumericValue( input[1].charAt(1)); //  translate input to row index of move
		
		
		if(getPieceName().equals("wp")) {  // if pawn being checked is white
			
			if(isPassant(move,true)) {
				
				return true;
			}
			
			
			if((pieceCol - 1 == moveCol  && pieceRow - 1 == moveRow) || (pieceCol + 1 == moveCol && pieceRow - 1 == moveRow)) {  // if pawn move is to take a piece diagonal from its location
				
				if(!Board.isIndexNull(moveRow, moveCol) && Board.getPieceName(moveRow, moveCol).charAt(0) == 'b') {  // check if there is piece to attack and it is opponent's piece
					
					firstMove = false;
					
					return true;
					
				}
					
				return false;	
				
			}
			
			
			if(pieceRow - 1 == moveRow && pieceCol == moveCol) {   // if pawn wants to move one space 
				
				if(Board.isIndexNull(pieceRow-1, pieceCol)) {  // check that there are no pieces in its path
					
					firstMove = false;
					return true;
				}
				
				return false;
				
			}
			
			if(firstMove) {  // if its the pawns first move
				
				if(pieceRow - 2 == moveRow && pieceCol == moveCol) {  // if pawn wants to move two spaces
					
					if(Board.isIndexNull(pieceRow-1, pieceCol) && Board.isIndexNull(pieceRow-2, pieceCol)) {  // check that there are no pieces in its path
						
						firstMove = false;  
						doubleMove = true;
						
						return true;
					}
					
					return false;
										
				}
			}
				
			
		} else {  // if the pawn is black
			
			if(isPassant(move,false)) {
				
				return true;
			}
			
			if((pieceCol + 1 == moveCol  && pieceRow + 1 == moveRow) || (pieceCol - 1 == moveCol && pieceRow + 1 == moveRow)) { // if pawn move is to take a piece diagonal from its location
				
				if(!Board.isIndexNull(moveRow, moveCol) && Board.getPieceName(moveRow, moveCol).charAt(0) == 'w') { // check if there is piece to attack and it is opponent's piece
					
					firstMove = false;
					return true;
					
				}
					
				return false;
				
				
			}
			
			
			if(pieceRow + 1 == moveRow && pieceCol == moveCol) {  // if pawn wants to move one space 
				
				if(Board.isIndexNull(pieceRow+1, pieceCol)) { // check that there are no pieces in its path
					
					firstMove = false;
					return true;
				}
				
				return false;
				
			}
			
			
			
			
			if(firstMove) {  // if its the pawns first move
				
				if(pieceRow + 2 == moveRow && pieceCol == moveCol) { // if pawn wants to move two spaces
					
					if(Board.isIndexNull(pieceRow+1, pieceCol) && Board.isIndexNull(pieceRow+2, pieceCol)) { // check that there are no pieces in its path
						
						firstMove = false;  
						doubleMove = true;
						
						return true;
					}
					
					return false;
										
				}
			}
			
		}
		
		return false;  // if none of the conditions are met; return false
		
	}
	
	/**
	 * @param move inputed by user
	 * @param isWhiteMove insert true if it is white's turn, false if it is black's turn
	 * @return return true if pawn is making an en passant move
	 */
	public boolean isPassant(String move, boolean isWhiteMove) {
		
		String[] input = move.split(" ");  // splits the users input
		
		int pieceCol = Character.getNumericValue( input[0].charAt(0) )-10; //  translate input to column index of piece
		int pieceRow = 8 - Character.getNumericValue( input[0].charAt(1) ); //  translate input to row index of piece
		
		int moveCol = Character.getNumericValue( input[1].charAt(0) )-10; //  translate input to column index of move
		int moveRow = 8 - Character.getNumericValue( input[1].charAt(1)); //  translate input to row index of move
		
		
		if(isWhiteMove) {
			
			if(Board.isIndexInBounds(moveRow, moveCol) && Board.isIndexNull(moveRow, moveCol) && pieceRow - 1 == moveRow && pieceCol + 1 == moveCol) {
				// if index is in bounds; if index being moved to is null; if moving 1 space up and 1 space right
			
				if(pieceRow == 3 && !Board.isIndexNull(pieceRow, pieceCol + 1) && Board.getPieceName(pieceRow, pieceCol + 1).equals("bp")) {
					// if white pawn is on appropriate row; check if there is a piece to the right; if piece to the right is black pawn
					
					Pawn blackPawn = (Pawn)Board.getPiece(pieceRow, pieceCol + 1);
				
					if(blackPawn.isDoubleMove()) {  // if black pawn moved 2 spaces first move
					
					
						Board.setIndexNull(pieceRow, pieceCol + 1);  // capture pawn
						return true;
					
					}	
				}	
			}
			
			if(Board.isIndexInBounds(moveRow, moveCol) && Board.isIndexNull(moveRow, moveCol) && pieceRow - 1 == moveRow && pieceCol - 1 == moveCol) {
				// if index is in bounds; if index being moved to is null; if moving 1 space up and 1 space left
			
				if(pieceRow == 3 && !Board.isIndexNull(pieceRow, pieceCol - 1) && Board.getPieceName(pieceRow, pieceCol - 1).equals("bp")) {
					// if white pawn is on appropriate row; check if there is a piece to the left; if piece to the left is black pawn
					
					Pawn blackPawn = (Pawn)Board.getPiece(pieceRow, pieceCol - 1);
				
					if(blackPawn.isDoubleMove()) {  // if black pawn moved 2 spaces first move
					
					
						Board.setIndexNull(pieceRow, pieceCol - 1); // capture pawn
						return true;
					
					}	
				}	
			}
			
		
		} else {  // black move
			
			if(Board.isIndexInBounds(moveRow, moveCol) && Board.isIndexNull(moveRow, moveCol) && pieceRow + 1 == moveRow && pieceCol - 1 == moveCol) {
				// if index is in bounds; if index being moved to is null; if moving 1 space down and 1 space left
			
				if(pieceRow == 4 && !Board.isIndexNull(pieceRow, pieceCol - 1) && Board.getPieceName(pieceRow, pieceCol - 1).equals("wp")) {
					// if black pawn is on appropriate row; check if there is a piece to the left; if piece to the left is white pawn
					
					Pawn whitePawn = (Pawn)Board.getPiece(pieceRow, pieceCol - 1);
				
					if(whitePawn.isDoubleMove()) {  // if white pawn moved 2 spaces first move
					
					
						Board.setIndexNull(pieceRow, pieceCol - 1);  // capture pawn
						return true;
					
					}	
				}	
			}
			
			if(Board.isIndexInBounds(moveRow, moveCol) && Board.isIndexNull(moveRow, moveCol) && pieceRow + 1 == moveRow && pieceCol + 1 == moveCol) {
				// if index is in bounds; if index being moved to is null; if moving 1 space down and 1 space right
			
				if(pieceRow == 4 && !Board.isIndexNull(pieceRow, pieceCol + 1) && Board.getPieceName(pieceRow, pieceCol + 1).equals("wp")) {
					// if black pawn is on appropriate row; check if there is a piece to the right; if piece to the right is white pawn
					
					Pawn whitePawn = (Pawn)Board.getPiece(pieceRow, pieceCol + 1);
				
					if(whitePawn.isDoubleMove()) {  // if white pawn moved 2 spaces first move
					
					
						Board.setIndexNull(pieceRow, pieceCol + 1);  // capture pawn
						return true;
					
					}	
				}	
			}
			
			
		}
		
		
		return false; 
	}

	/* (non-Javadoc)
	 * @see piece.Piece#isCheck(java.lang.String)
	 */
	@Override
	public boolean isCheck(String move) {
		
		String[] input = move.split(" ");  // splits the users input
		
		int moveCol = Character.getNumericValue( input[1].charAt(0) )-10; //  translate input to column index of move
		int moveRow = 8 - Character.getNumericValue( input[1].charAt(1)); //  translate input to row index of move
		
		
		if(getPieceName().equals("wp")) {  // if pawn is white
			
			
			if(Board.isIndexInBounds(moveRow - 1, moveCol + 1) && !Board.isIndexNull(moveRow - 1, moveCol + 1) && Board.getPieceName(moveRow - 1, moveCol + 1).equals("bK")) {
				//Check one space up and one space right; check if the index is on board; check if it contains piece; if it contains piece check if it is black king
					
				return true;
				
			}
			
			if(Board.isIndexInBounds(moveRow - 1, moveCol - 1) && !Board.isIndexNull(moveRow - 1, moveCol - 1) && Board.getPieceName(moveRow - 1, moveCol - 1).equals("bK")) {
				//Check one space up and one space left; check if the index is on board; check if it contains piece; if it contains piece check if it is black king
				
				return true;  // returns true; king is in check
			
			}
			
		} else { // if pawn is black
			
			if(Board.isIndexInBounds(moveRow + 1, moveCol - 1) && !Board.isIndexNull(moveRow + 1, moveCol - 1) && Board.getPieceName(moveRow + 1, moveCol - 1).equals("wK")) {
				//Check one space down and one space left; check if the index is on board; check if it contains piece; if it contains piece check if it is white king
				
				return true;
			}
			
			if(Board.isIndexInBounds(moveRow + 1, moveCol + 1) && !Board.isIndexNull(moveRow + 1, moveCol + 1) && Board.getPieceName(moveRow + 1, moveCol + 1).equals("wK")) {
				//Check one space down and one space right; check if the index is on board; check if it contains piece; if it contains piece check if it is white king
			
				return true;
			}
			
		}
			
			
			
		return false;
	}
}