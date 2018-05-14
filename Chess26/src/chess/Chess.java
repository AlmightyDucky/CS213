package chess;

import java.io.*;
import java.util.*;

import piece.*;


/**
 * Main function which contains the chess game loop
 * 
 * @author Robert Barbaro
 * 
 * @author Christopher Gonzalez
 */

public class Chess {
	
	public static void main(String[] args) {
		
		Scanner sn = new Scanner(System.in);  // reads input from user
		
		//Board.fakeSetUp();
		Board.setUpBoard();  // sets up the game board
		
		Board.printBoard();  // print current state of game board
		
		boolean isWhiteTurn = true;  // if it is white's turn true; if it is black's turn false
		boolean drawInitiated = false;
		
		String move;
		int attempts;
		
		while(true) {
			
			
			attempts = 0;
			
			do {
				
				if(attempts > 0) {
					
					System.out.println("Illegal move, try again"); 
					
					System.out.println();
					System.out.println();
				}
					
				if(isWhiteTurn) {  // prompt user to input move
					
					System.out.print("White's move: ");
					move = sn.nextLine();  // user inputs move
					
					System.out.println();
					System.out.println();
					
				} else {
					
					System.out.print("Black's move: ");
					move = sn.nextLine(); // user inputs move
					
					System.out.println();
					System.out.println();
				}
				
				
				if(drawInitiated) { 
					
					if(Board.isDrawAccepted(move)) {
						
						System.out.println("Draw");
						sn.close();
						return;
						
					} 
					
				}
								
				if(Board.isResigning(move, isWhiteTurn)) {
					
					sn.close();
					return;
				}	
				
				attempts++;
				
			} while(!Board.movePiece(move,isWhiteTurn));
			
			
			
			if(drawInitiated == true) {  // opposing team did not agree to draw; draw is uninitiated 
				
				drawInitiated = false;
			}
			
			if(Board.isDrawInitiated(move)) { 
				
				drawInitiated = true;
			}
			
			if(Board.isKingCaptured()) { 
				
				sn.close();
				return;  // ends game loop
			}
			
			if(Board.isCheckmate())
			{
				sn.close();
				return;
			}
			
			Board.printBoard();
			
			isWhiteTurn = !isWhiteTurn;  // switch turns
			
			
		}  // END OF GAME LOOP
		
	}
}
