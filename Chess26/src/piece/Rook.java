package piece;

import chess.Board;

/**
 * Represents the Rook piece
 * 
 * @author Robert Barbaro
 * 
 * @author Christopher Gonzalez
 */


public class Rook extends Piece{
	
	/**
	 * used for castling - if the rook has moved already or not
	 */
	boolean hasMoved = false;
	
	/**
	 * @param pieceName input the name of the piece
	 */
	public Rook(String pieceName) {
		
		super(pieceName);
	}

	/* (non-Javadoc)
	 * @see piece.Piece#checkMove(java.lang.String)
	 */
	@Override
	public boolean checkMove(String move) {
		
		String[] input = move.split(" ");  // splits the users input
		
		int pieceCol = Character.getNumericValue( input[0].charAt(0) )-10; //  translate input to column index of piece
		int pieceRow = 8 - Character.getNumericValue( input[0].charAt(1) ); //  translate input to row index of piece
		
		int moveCol = Character.getNumericValue( input[1].charAt(0) )-10; //  translate input to column index of move
		int moveRow = 8 - Character.getNumericValue( input[1].charAt(1)); //  translate input to row index of move
		
		
		if(pieceCol != moveCol && pieceRow != moveRow) {  // if rook is not moving horizontally or vertically
			
			return false;  // illegal move; return false
		}
		
		if(pieceCol == moveCol) {  // if the piece is moving vertically 
			
			if(!checkVertical(pieceRow,moveRow,pieceCol)) {  // check if there are any pieces in path
				
				return false;  // if there are pieces in path; return false
			}
			
			
		}
		
		if(pieceRow == moveRow) {  // if the piece is moving horizontally
			
			if(!checkHorizontal(pieceCol,moveCol,pieceRow)) {  // check if there are any pieces in path
				
				return false;  // if there are pieces in path; return false
			}
			
		}
		
		
		if(Board.isIndexNull(moveRow,moveCol)) {  // if the location being moved to is empty
			
			hasMoved = true;
			return true;  // legal move; return true
		}
		
		if(Board.getPieceName(moveRow, moveCol).charAt(0) != Board.getPieceName(pieceRow, pieceCol).charAt(0)) {
			
		// Check to see if piece that the rook is attempting to capture is opposing color
			hasMoved = true;
			return true;  // Rook is trying to capture piece of the opposing team; legal move
		}
		
		
		return false;  
	}
	
	/**
	 * @param pieceRow input the row the piece is on
	 * @param moveRow input row the piece is attempting to move to
	 * @param col input column piece is on
	 * @return returns true if there are no pieces in rook's path
	 */
	public boolean checkVertical(int pieceRow, int moveRow, int col) {
		
		int vChecker;  
		
		if(pieceRow > moveRow) {  
			
			vChecker = pieceRow - 1; 
			
			while(vChecker > moveRow) {  
				
				if(!Board.isIndexNull(vChecker, col)) {
					
					return false;
				}
				
				vChecker--;
			}
			
		}  else {
			
			vChecker = pieceRow + 1;
			
			while(vChecker < moveRow) {
				
				if(!Board.isIndexNull(vChecker, col)) {
					
					return false;
				}
				
				vChecker++;
			}
			
		}
			
		return true;  
	}
	
	/**
	 * @param pieceCol input the column the piece is on
	 * @param moveCol input column the piece is attempting to move to
	 * @param row input row piece is on
	 * @return returns true if there are no pieces in rook's path
	 */
	public boolean checkHorizontal(int pieceCol, int moveCol, int row) {
		
		int vChecker;
		
		if(pieceCol > moveCol) {
			
			vChecker = pieceCol - 1;
			
			while(vChecker > moveCol) {
				
				if(!Board.isIndexNull(row, vChecker)) {
					
					return false;
				}
				
				vChecker--;
			}
			
		}  else {
			
			vChecker = pieceCol + 1;
			
			while(vChecker < moveCol) {
				
				if(!Board.isIndexNull(row, vChecker)) {
					
					return false;
				}
				
				vChecker++;
			}
			
		}
					
		return true;  

	}

	/* (non-Javadoc)
	 * @see piece.Piece#isCheck(java.lang.String)
	 */
	@Override
	public boolean isCheck(String move) {
		
		String[] input = move.split(" ");  // splits the users input
		
		int moveCol = Character.getNumericValue( input[1].charAt(0) )-10; //  translate input to column index of move
		int moveRow = 8 - Character.getNumericValue( input[1].charAt(1)); //  translate input to row index of move
		
		
		int col = moveCol - 1;  // set column to check left of piece
		
		while(Board.isIndexInBounds(moveRow,col)) {  // check horizontally to the left
			
			if(!Board.isIndexNull(moveRow, col) && getPieceName().charAt(0) != Board.getPieceName(moveRow, col).charAt(0) && Board.getPieceName(moveRow, col).charAt(1) == 'K') {
				// check if index is null; check if the piece is opposite color, check if piece is King
				
				return true;  // returns true if it king
				
			} 

			if(!Board.isIndexNull(moveRow, col)){  // if there is a piece; stop checking
				
				break;
			}
			
			col--;  // go left one column
		}
		
		col = moveCol + 1;  // set column to check right of piece
		
		while(Board.isIndexInBounds(moveRow,col)) {  // check horizontally to the right
			
			if(!Board.isIndexNull(moveRow, col) && getPieceName().charAt(0) != Board.getPieceName(moveRow, col).charAt(0) && Board.getPieceName(moveRow, col).charAt(1) == 'K') {
				// check if index is null; check if the piece is opposite color, check if piece is King
				
				return true; // returns true if it king
				
			} 

			if(!Board.isIndexNull(moveRow, col)){  // if there is a piece; stop checking
				
				break;
			}
			
			col++; // go right one column
		}
		
		int row = moveRow - 1; // set row to check upward
		
		while(Board.isIndexInBounds(row,moveCol)) {  // check vertical going upward
			
			if(!Board.isIndexNull(row, moveCol) && getPieceName().charAt(0) != Board.getPieceName(row, moveCol).charAt(0) && Board.getPieceName(row, moveCol).charAt(1) == 'K') {
				// check if index is null; check if the piece is opposite color, check if piece is King
				
				return true;  // returns true if it king
				
			} 

			if(!Board.isIndexNull(moveCol, row)){ // if there is a piece; stop checking
				
				break;
			}
			
			row--; // go up one column
		}
		
		row = moveRow + 1; // set row to check downward
		
		while(Board.isIndexInBounds(row,moveCol)) {  // check vertical going downard
			
			if(!Board.isIndexNull(row, moveCol) && getPieceName().charAt(0) != Board.getPieceName(row, moveCol).charAt(0) && Board.getPieceName(row, moveCol).charAt(1) == 'K') {
				// check if index is null; check if the piece is opposite color, check if piece is King
				
				return true; // returns true if it king
				
			} 
 
			if(!Board.isIndexNull(row, moveCol)){ // if there is a piece; stop checking
				
				break;
			}
			
			row++; // go down one column
		}

		

		return false;  // there is no king in immediate path; return false
	}

	/**
	 * @return true if the king has moved; false if the king hasn't moved
	 */
	public boolean getHasMoved()
	{
		return hasMoved;
	}
	

	/**
	 * @param state boolean value which changes the hasMoved field
	 */
	public void setHasMoved(boolean state)
	{
		hasMoved = state;
	}
}
