package piece;

import chess.Board;

/**
 * Represents the King piece
 * 
 * @author Robert Barbaro
 * 
 * @author Christopher Gonzalez
 */
public class King extends Piece{
	
	/**
	 * used for castling - if the king has moved already or not
	 */
	boolean hasMoved = false;
	
	/**
	 * piece's current location
	 */
	int currentCol = -1;
	int currentRow = -1;
	
	/**
	 * location the piece wants to move to
	 */
	int destinationCol = -1;
	int destinationRow = -1;
	
	public King(String pieceName) {
		
		super(pieceName);
	}
	
	/**
	 * @param move inputed by user
	 * @return returns true if the king makes a legal move (one space around itself);
	 * 			returns false if the king makes an illegal move
	 */
	@Override
	public boolean checkMove(String move) {
		String[] input = move.split(" ");  // splits the users input
			
		currentCol = Character.getNumericValue(input[0].charAt(0)) - 10; //  translate input to column index of piece
		currentRow = 8 - Character.getNumericValue(input[0].charAt(1)); //  translate input to row index of piece
		
		destinationCol = Character.getNumericValue(input[1].charAt(0)) - 10; //  translate input to column index of move
		destinationRow = 8 - Character.getNumericValue(input[1].charAt(1)); //  translate input to row index of move
		
		//checks if there is a piece of the same color at the King's destination - if so, it is an illegal move
		if (getPieceName().equals("wK"))
		{
			if (!Board.isIndexNull(destinationRow, destinationCol) && Board.getPieceName(destinationRow, destinationCol).charAt(0) == 'w')
			{
				return false;
			}
		} else if (getPieceName().equals("bK"))
		{
			if (!Board.isIndexNull(destinationRow, destinationCol) && Board.getPieceName(destinationRow, destinationCol).charAt(0) == 'b')
			{
				return false;
			}
		}
		
		/*castling move*/
		if (castlingMove())
		{
			hasMoved = true;
			return true;
		}
		
		/*cardinal directions*/
		if (destinationCol == currentCol && (destinationRow == currentRow + 1 || destinationRow == currentRow - 1)) //move up or down
		{
			hasMoved = true;
			return true;
		}
		
		if ((destinationCol == currentCol - 1 || destinationCol == currentCol + 1) && destinationRow == currentRow) //move left or right
		{
			hasMoved = true;
			return true;
		}
		
		/*diagonals*/
		if (destinationCol == currentCol - 1 && (destinationRow == currentRow + 1 || destinationRow == currentRow - 1)) //move left, then up or down
		{
			hasMoved = true;
			return true;
		}
		
		if (destinationCol == currentCol + 1 && (destinationRow == currentRow + 1 || destinationRow == currentRow - 1)) //move right, then up or down
		{
			hasMoved = true;
			return true;
		}
		
		return false;
	}
	
	/**
	 * @return true if the king is performing a legal castling move; false if the castling move is illegal
	 */
	private boolean castlingMove()
	{
		if (!hasMoved)
		{
			//kingside/short castling
			if (destinationCol - currentCol == 2 && destinationRow == currentRow)
			{
				if (Board.getPiece(destinationRow, destinationCol + 1) instanceof Rook)
				{
					if (!((Rook) Board.getPiece(destinationRow, destinationCol + 1)).getHasMoved())
					{
						if (Board.isIndexNull(destinationRow, destinationCol - 1) && Board.isIndexNull(destinationRow, destinationCol))
						{
							if (Board.getPieceName(currentRow, currentCol).charAt(0) == Board.getPieceName(destinationRow, destinationCol + 1).charAt(0))
							{
								((Rook) Board.getPiece(destinationRow, destinationCol + 1)).setHasMoved(true);
								return true;
							}
						}
					}
				}
			}
				
			//queenside/long castling
			if (destinationCol - currentCol == -2 && destinationRow == currentRow)
			{
				if (Board.getPiece(destinationRow, destinationCol - 2) instanceof Rook)
				{
					if (!((Rook) Board.getPiece(destinationRow, destinationCol - 2)).getHasMoved())
					{
						if (Board.isIndexNull(destinationRow, destinationCol - 1) && Board.isIndexNull(destinationRow, destinationCol) && Board.isIndexNull(destinationRow, destinationCol + 1))
						{
							if (Board.getPieceName(currentRow, currentCol).charAt(0) == Board.getPieceName(destinationRow, destinationCol - 2).charAt(0))
							{
								((Rook) Board.getPiece(destinationRow, destinationCol - 2)).setHasMoved(true);
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * @param move inputed by user
	 * @return returns true if the king puts the opposite king in check
	 * 			returns false if the king does not put the opposite king in check
	 */
	@Override
	public boolean isCheck(String move) {
		/*cardinal directions*/
		//up
		if (Board.isIndexInBounds(destinationRow - 1, destinationCol) && !Board.isIndexNull(destinationRow - 1, destinationCol))
		{
			if(Board.getPieceName(destinationRow - 1, destinationCol).charAt(0) != getPieceName().charAt(0) 
					&& Board.getPieceName(destinationRow - 1, destinationCol).charAt(1) == 'K') {	
				return true;
			}
		}
		
		//down
		if (Board.isIndexInBounds(destinationRow + 1, destinationCol) && !Board.isIndexNull(destinationRow + 1, destinationCol))
		{
			if(Board.getPieceName(destinationRow + 1, destinationCol).charAt(0) != getPieceName().charAt(0) 
					&& Board.getPieceName(destinationRow + 1, destinationCol).charAt(1) == 'K') {	
				return true;
			}
		}
		
		//left
		if (Board.isIndexInBounds(destinationRow, destinationCol - 1) && !Board.isIndexNull(destinationRow, destinationCol - 1))
		{
			if(Board.getPieceName(destinationRow, destinationCol - 1).charAt(0) != getPieceName().charAt(0) 
					&& Board.getPieceName(destinationRow, destinationCol - 1).charAt(1) == 'K') {	
				return true;
			}
		}
		
		//right
		if (Board.isIndexInBounds(destinationRow, destinationCol + 1) && !Board.isIndexNull(destinationRow, destinationCol + 1))
		{
			if(Board.getPieceName(destinationRow, destinationCol + 1).charAt(0) != getPieceName().charAt(0) 
					&& Board.getPieceName(destinationRow, destinationCol + 1).charAt(1) == 'K') {	
				return true;
			}
		}
		
		/*diagonals*/
		//upper left
		if (Board.isIndexInBounds(destinationRow - 1, destinationCol - 1) && !Board.isIndexNull(destinationRow - 1, destinationCol - 1))
		{
			if(Board.getPieceName(destinationRow - 1, destinationCol - 1).charAt(0) != getPieceName().charAt(0) 
					&& Board.getPieceName(destinationRow - 1, destinationCol - 1).charAt(1) == 'K') {	
				return true;
			}
		}
		
		//lower left
		if (Board.isIndexInBounds(destinationRow + 1, destinationCol - 1) && !Board.isIndexNull(destinationRow + 1, destinationCol - 1))
		{
			if(Board.getPieceName(destinationRow + 1, destinationCol - 1).charAt(0) != getPieceName().charAt(0) 
					&& Board.getPieceName(destinationRow + 1, destinationCol - 1).charAt(1) == 'K') {	
				return true;
			}
		}
		
		//upper right
		if (Board.isIndexInBounds(destinationRow - 1, destinationCol + 1) && !Board.isIndexNull(destinationRow - 1, destinationCol + 1))
		{
			if(Board.getPieceName(destinationRow - 1, destinationCol + 1).charAt(0) != getPieceName().charAt(0) 
					&& Board.getPieceName(destinationRow - 1, destinationCol + 1).charAt(1) == 'K') {	
				return true;
			}
		}
		
		//lower right
		if (Board.isIndexInBounds(destinationRow + 1, destinationCol + 1) && !Board.isIndexNull(destinationRow + 1, destinationCol + 1))
		{
			if(Board.getPieceName(destinationRow + 1, destinationCol + 1).charAt(0) != getPieceName().charAt(0) 
					&& Board.getPieceName(destinationRow + 1, destinationCol + 1).charAt(1) == 'K') {	
				return true;
			}
		}

		return false;
	}
	
	/**
	 * @return true if the king has moved; false if the king hasn't moved
	 */
	public boolean getHasMoved()
	{
		return hasMoved;
	}
}
