package piece;

import chess.Board;


/**
 * Represents the Queen piece
 * 
 * @author Robert Barbaro
 * 
 * @author Christopher Gonzalez
 */

public class Queen extends Piece{
	
	/**
	 * piece's current column location
	 */
	int currentCol = -1;
	
	/**
	 * piece's current row location
	 */
	int currentRow = -1;
	
	/**
	 * column the piece wants to move to
	 */
	int destinationCol = -1;
	
	/**
	 * row the piece wants to move to
	 */
	int destinationRow = -1;
	
	/**
	 * @param pieceName name of the piece
	 */
	public Queen(String pieceName) {
		
		super(pieceName);
	}

	/**
	 * @param move input from user
	 * @return true if move is legal; false if move is illegal
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
		
		//if moving diagonally, must move the same number of rows and columns
		//(example: can't move 1 column left and 2 rows up)
		if (Math.abs(destinationCol - currentCol) > 0 && Math.abs(destinationRow - currentRow) > 0)
		{
			if (Math.abs(destinationCol - currentCol) != Math.abs(destinationRow - currentRow))
			{
				return false;
			}
		}

		//if moving diagonally, check if there is a piece on the space we want to move to and see if we can capture it and if the capture is legal
		//(example: can't capture own pieces)
		if ((currentCol != destinationCol) && (currentRow != destinationRow))
		{
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
		}
		
		// if the piece is moving vertically 
		if(currentCol == destinationCol)
		{
			// check if there are any pieces in path
			if(!checkVertical())
			{
				return false;  // if there are pieces in path; return false
			}
		}
		
		// if the piece is moving horizontally
		if(currentRow == destinationRow)
		{
			 // check if there are any pieces in path
			if(!checkHorizontal())
			{
				return false;  // if there are pieces in path; return false
			}
			
		}
		
		 // if the location being moved to is empty
		if(Board.isIndexNull(destinationRow,destinationCol))
		{
			return true;  // legal move; return true
		}
		
		// Check to see if piece that the queen is attempting to capture is opposing color	
		if(Board.getPieceName(destinationRow, destinationCol).charAt(0) == Board.getPieceName(currentRow, currentCol).charAt(0))
		{
			return false;  // Queen is trying to capture piece of the same team; illegal move
		}
		
		return true;
	}
		
	/**
	 * @return true if there is a piece of a different color on spot queen is moving to; false if the piece is the same color
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
		
		public boolean checkVertical()
		{
			int vChecker;
			
			if(currentRow > destinationRow)
			{  
				vChecker = currentRow - 1; 
				
				while(vChecker > destinationRow)
				{  
					if(!Board.isIndexNull(vChecker, currentCol))
					{
						return false;
					}
					
					vChecker--;
				}
			} else {
				vChecker = currentRow + 1;
				
				while(vChecker < destinationRow)
				{
					if(!Board.isIndexNull(vChecker, currentCol))
					{
						return false;
					}
					
					vChecker++;
				}
			}
			return true;  
		}
		
		public boolean checkHorizontal() {
			
			int vChecker;
			
			if(currentCol > destinationCol)
			{
				vChecker = currentCol - 1;
				
				while(vChecker > destinationCol)
				{
					if(!Board.isIndexNull(currentRow, vChecker))
					{
						return false;
					}
					vChecker--;
				}
				
			} else {
				vChecker = currentCol + 1;
				
				while(vChecker < destinationCol)
				{
					if(!Board.isIndexNull(currentRow, vChecker))
					{
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
		
		currentCol = Character.getNumericValue(input[0].charAt(0)) - 10; //  translate input to column index of piece
		currentRow = 8 - Character.getNumericValue(input[0].charAt(1)); //  translate input to row index of piece
		
		destinationCol = Character.getNumericValue(input[1].charAt(0)) - 10; //  translate input to column index of move
		destinationRow = 8 - Character.getNumericValue(input[1].charAt(1)); //  translate input to row index of move
		
		//check up, down, left, or right for the king
		if (checkCardinals())
		{
			return true;
		}
		
		//check up, down, left, or right for the king
		if (checkDiagonals())
		{
			return true;
		}

		return false;
	}
	
	/**
	 * @return true if a king has been found vertically or horizontally; false if a king hasn't been found vertically or horizontally
	 */
	private boolean checkCardinals()
	{
		int colIndex = destinationCol - 1;  // set column to check left of piece
		
		while(Board.isIndexInBounds(destinationRow,colIndex)) {  // check horizontally to the left
			
			if(!Board.isIndexNull(destinationRow, colIndex) 
					&& getPieceName().charAt(0) != Board.getPieceName(destinationRow, colIndex).charAt(0) 
					&& Board.getPieceName(destinationRow, colIndex).charAt(1) == 'K') {
				// check if index is null; check if the piece is opposite color, check if piece is King
				
				return true;  // returns true if it king
				
			} 

			if(!Board.isIndexNull(destinationRow, colIndex)){  // if there is a piece; stop checking
				
				break;
			}
			
			colIndex--;  // go left one column
		}
		
		colIndex = destinationCol + 1;  // set column to check right of piece
		
		while(Board.isIndexInBounds(destinationRow,colIndex)) {  // check horizontally to the right
			
			if(!Board.isIndexNull(destinationRow, colIndex) 
					&& getPieceName().charAt(0) != Board.getPieceName(destinationRow, colIndex).charAt(0) 
					&& Board.getPieceName(destinationRow, colIndex).charAt(1) == 'K') {
				// check if index is null; check if the piece is opposite color, check if piece is King
				
				return true; // returns true if it king
				
			} 

			if(!Board.isIndexNull(destinationRow, colIndex)){  // if there is a piece; stop checking
				
				break;
			}
			
			colIndex++; // go right one column
		}
		
		int rowIndex = destinationRow - 1; // set row to check upward
		
		while(Board.isIndexInBounds(rowIndex,destinationCol)) {  // check vertical going upward
			
			if(!Board.isIndexNull(rowIndex, destinationCol) 
					&& getPieceName().charAt(0) != Board.getPieceName(rowIndex, destinationCol).charAt(0) 
					&& Board.getPieceName(rowIndex, destinationCol).charAt(1) == 'K') {
				// check if index is null; check if the piece is opposite color, check if piece is King
				
				return true;  // returns true if it king
				
			} 

			if(!Board.isIndexNull(destinationCol, rowIndex)){ // if there is a piece; stop checking
				
				break;
			}
			
			rowIndex--; // go up one column
		}
		
		rowIndex = destinationRow + 1; // set row to check downward
		
		while(Board.isIndexInBounds(rowIndex,destinationCol)) {  // check vertical going downard
			
			if(!Board.isIndexNull(rowIndex, destinationCol) 
					&& getPieceName().charAt(0) != Board.getPieceName(rowIndex, destinationCol).charAt(0) 
					&& Board.getPieceName(rowIndex, destinationCol).charAt(1) == 'K') {
				// check if index is null; check if the piece is opposite color, check if piece is King
				
				return true; // returns true if it king
				
			} 
 
			if(!Board.isIndexNull(rowIndex, destinationCol)){ // if there is a piece; stop checking
				
				break;
			}
			
			rowIndex++; // go down one column
		}

		return false;  // there is no king in immediate path; return false
	}
	
	/**
	 * @return true if a king has been found diagonally; false if a king hasn't been found diagonally
	 */
	private boolean checkDiagonals()
	{		
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
