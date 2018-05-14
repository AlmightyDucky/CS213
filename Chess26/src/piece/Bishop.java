package piece;

import chess.Board;

/**
 * Represents the Bishop piece
 * 
 * @author Robert Barbaro
 * 
 * @author Christopher Gonzalez
 */

public class Bishop extends Piece{
	
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
	
	public Bishop(String pieceName) {
		
		super(pieceName);
	}

	/**
	 * @param move inputed by user
	 * @return returns true if the bishop makes a legal move (moving diagonally);
	 * 			returns false if the bishop makes an illegal move (moving vertical or horizontal)
	 */
	@Override
	public boolean checkMove(String move) {
		String[] input = move.split(" ");  // splits the users input
		
		currentCol = Character.getNumericValue(input[0].charAt(0)) - 10; //  translate input to column index of piece
		currentRow = 8 - Character.getNumericValue(input[0].charAt(1)); //  translate input to row index of piece
		
		destinationCol = Character.getNumericValue(input[1].charAt(0)) - 10; //  translate input to column index of move
		destinationRow = 8 - Character.getNumericValue(input[1].charAt(1)); //  translate input to row index of move
		
		//if (somehow) one of the parts required for a move was not entered in
		if (destinationCol == -1 || currentCol == -1 || destinationRow == -1 || currentRow == -1)
		{
			return false;
		}
		
		//if the player tries to move the bishop vertically or horizontally
		if (destinationCol == currentCol || destinationRow == currentRow)
		{
			return false;
		}
		
		//player must move diagonally the same number of rows and columns
		//(example: can't move 1 column left and 2 rows up)
		if (Math.abs(destinationCol - currentCol) != Math.abs(destinationRow - currentRow))
		{
			return false;
		}
		
		//if there is a piece on the space we want to move to, check if we can capture it and if the capture is legal
		//(example: can't capture own pieces)
		if (!Board.isIndexNull(destinationRow, destinationCol))
		{
			if(isThereAPieceIHaveToLeapOver())
			{
				return false;
			}
			
			if(!captureCheck())
			{
				return false;
			}
		} else {
			if(isThereAPieceIHaveToLeapOver())
			{
				return false;
			}
		}

		return true;
	}
	
	/**
	 * @return returns true if there is a piece the bishop can capture
	 * 			returns false if there is a piece of the same color at the destination
	 */
	private boolean captureCheck()
	{
		if (Board.getPieceName(destinationRow, destinationCol).charAt(0) == 'b'
				&& Board.getPieceName(currentRow, currentCol).charAt(0) == 'w')
		{
			return true;
		} else if (Board.getPieceName(destinationRow, destinationCol).charAt(0) == 'w'
				&& Board.getPieceName(currentRow, currentCol).charAt(0) == 'b')
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * @return returns true if there is a piece in the way
	 * 			returns false if there is no piece in the way
	 */
	private boolean isThereAPieceIHaveToLeapOver()
	{
		int colIndex = 0;
		int rowIndex = 0;
		
		/*determine which way piece is moving and check if there is another piece along the way*/
		if (destinationCol - currentCol < 0)
		{
			colIndex = currentCol - 1;
			
			if (destinationRow - currentRow < 0)
			{
				//moving upper left
				rowIndex = currentRow - 1;
				
				while (rowIndex != destinationRow)
				{
					if(!Board.isIndexNull(rowIndex, colIndex))
					{
						return true;
					}
					rowIndex --;
					colIndex --;
				}
			} else {
				//moving lower left
				rowIndex = currentRow + 1;
				
				while (rowIndex != destinationRow)
				{
					if(!Board.isIndexNull(rowIndex, colIndex))
					{
						return true;
					}
					rowIndex ++;
					colIndex --;
				}
			}
		} else {
			colIndex = currentCol + 1;
			
			if (destinationRow - currentRow < 0)
			{
				//moving upper right
				rowIndex = currentRow - 1;
				
				while (rowIndex != destinationRow)
				{
					if(!Board.isIndexNull(rowIndex, colIndex))
					{
						return true;
					}
					rowIndex --;
					colIndex ++;
				}
			} else {
				//moving lower right
				rowIndex = currentRow + 1;
				
				while (rowIndex != destinationRow)
				{
					if(!Board.isIndexNull(rowIndex, colIndex))
					{
						return true;
					}
					rowIndex ++;
					colIndex ++;
				}
			}
		}
		return false;
	}
	
	/**
	 * @param move inputed by user
	 * @return returns true if the bishop puts the king in check
	 * 			returns false if the bishop does not put the king in check
	 */
	@Override
	public boolean isCheck(String move) {
		String[] input = move.split(" ");  // splits the users input
		
		currentCol = Character.getNumericValue(input[0].charAt(0)) - 10; //  translate input to column index of piece
		currentRow = 8 - Character.getNumericValue(input[0].charAt(1)); //  translate input to row index of piece
		
		destinationCol = Character.getNumericValue(input[1].charAt(0)) - 10; //  translate input to column index of move
		destinationRow = 8 - Character.getNumericValue(input[1].charAt(1)); //  translate input to row index of move
		
		int colIndex = 0;
		int rowIndex = 0;
		
		/*determine which way piece is moving and check if there is a king there*/
		//check upper left
		colIndex = destinationCol - 1;
		rowIndex = destinationRow - 1;
		while (Board.isIndexInBounds(rowIndex, colIndex))
		{
			if (!Board.isIndexNull(rowIndex, colIndex))
			{
				if ((Board.getPieceName(currentRow, currentCol).charAt(0) == 'w' && Board.getPieceName(rowIndex, colIndex) == "bK")
						|| (Board.getPieceName(currentRow, currentCol).charAt(0) == 'b' && Board.getPieceName(rowIndex, colIndex) == "wK"))
				{
					return true;
				} else {
					break;
				}
			}
			rowIndex ++;
			colIndex ++;
		}
		
		//check lower left
		colIndex = destinationCol - 1;
		rowIndex = destinationRow + 1;
		while (Board.isIndexInBounds(rowIndex, colIndex))
		{
			if (!Board.isIndexNull(rowIndex, colIndex))
			{
				if ((Board.getPieceName(currentRow, currentCol).charAt(0) == 'w' && Board.getPieceName(rowIndex, colIndex) == "bK")
						|| (Board.getPieceName(currentRow, currentCol).charAt(0) == 'b' && Board.getPieceName(rowIndex, colIndex) == "wK"))
				{
					return true;
				} else {
					break;
				}
			}
			rowIndex ++;
			colIndex ++;
		}
		
		//check upper right
		colIndex = destinationCol + 1;
		rowIndex = destinationRow - 1;
		while (Board.isIndexInBounds(rowIndex, colIndex))
		{
			if (!Board.isIndexNull(rowIndex, colIndex))
			{
				if ((Board.getPieceName(currentRow, currentCol).charAt(0) == 'w' && Board.getPieceName(rowIndex, colIndex) == "bK")
						|| (Board.getPieceName(currentRow, currentCol).charAt(0) == 'b' && Board.getPieceName(rowIndex, colIndex) == "wK"))
				{
					return true;
				} else {
					break;
				}
			}
			rowIndex ++;
			colIndex ++;
		}
		
		//check lower right
		colIndex = destinationCol + 1;
		rowIndex = destinationRow + 1;	
		while (Board.isIndexInBounds(rowIndex, colIndex))
		{
			if (!Board.isIndexNull(rowIndex, colIndex))
			{
				if ((Board.getPieceName(currentRow, currentCol).charAt(0) == 'w' && Board.getPieceName(rowIndex, colIndex) == "bK")
						|| (Board.getPieceName(currentRow, currentCol).charAt(0) == 'b' && Board.getPieceName(rowIndex, colIndex) == "wK"))
				{
					return true;
				} else {
					break;
				}
			}
			rowIndex ++;
			colIndex ++;
		}

		return false;
	}

}
