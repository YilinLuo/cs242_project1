package cs242_project1;
import java.awt.print.*;

public class Board {
	
	private enum Type {
		EMPTY, WHITE, BLACK, W_KING, B_KING 
	}
	
	public enum Game {
		MOVE_SUCCESS, INVALID_DESTINATION, INVALID_PIECE, DOUBLE_JUMP, GAME_ENDED
	}
	
	private int Normal_WhitePcs;
    private int Normal_BlackPcs;
    private int King_BlackPcs;
    private int King_WhitePcs;
	
	public static void main(String args[]){
		
		
		
		
		
		
		int upper_eight = 8;
		//for 4*4 grid
		int size_four = 4;
		int[][] grid = new int[size_four][size_four];
		
		for (int row = 0; row < size_four; row++) {
		    for (int col = 0; col < size_four; col++) {
		        System.out.printf(" _", grid[row][col]);
		    }
		    System.out.println();
		}
		
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		
		/*
		 * 
		 *                        for 8*8 grid
		 *
		 */
		
		for(int i=0; i < upper_eight+1;i++) {
			System.out.print(" " +i );
		}
		System.out.println();
		int size_eight = 9;
		int[][] grid_eight = new int[size_eight][size_eight];
		
		for (int row = 0; row < size_eight-1; row++) {
			
			System.out.printf(Integer.toString(row), grid_eight[0]);
			
		    for (int col = 0; col < size_eight-1; col++) {
		    	if(row ==3 & col== 3) {
		    		  System.out.printf(" B", grid_eight[row][col]);
		    	}  else {
		        System.out.printf(" _", grid_eight[row][col]);
		    }
		        
		    }
		    System.out.println();
		}
		
		
	}
	
	
	
}