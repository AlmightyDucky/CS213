package piece;

/**
 * Represents abstract class for all the pieces
 * 
 * @author Robert Barbaro
 * 
 * @author Christopher Gonzalez
 */
public abstract class Piece {
	
	/**
	 *  Holds the name of the piece
	 */
	private String pieceName;  // holds the name of piece
	
	/**
	 * @param pieceName input the name of piece
	 */
	public Piece(String pieceName) {  
		
		this.pieceName = pieceName;
	}
	
	/**
	 * @param move inputed by user
	 * @return returns true if move is legal, false if move is illegal
	 */
	public abstract boolean checkMove(String move);
	
	
	/**
	 * @param move inputed by user
	 * @return return true if the piece puts King of the opposite color in check
	 */
	public abstract boolean isCheck(String move);
	
	/**
	 * @return returns the name of piece
	 */
	public String getPieceName() {
		
		return pieceName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		
		return pieceName;
	}
}
