package cs242_project1;
import java.awt.Point;
import java.awt.print.*;

public class Board {
	private Type [][] board;
	public int SIZE = 8;
	public boolean four_board;
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
    
    public Board() {
    	//setUpBoard();
    }
    
    public Board(Type[][] board, boolean four_board) {
    	Normal_WhitePcs = 0;
    	Normal_BlackPcs = 0;
    	King_BlackPcs = 0;
    	 King_WhitePcs = 0; 
    	 
    	 this.board = board;
    	 if(four_board == true) {
    		 SIZE = 4;
    	 }
    	 for(int i = 0; i< SIZE; i ++) {
    		 for(int j = 0; j < SIZE; j++) {
    			 Type piece = getPiece(i,j);
    			 if(piece == Type.BLACK)
    				 Normal_BlackPcs++;
    			 else if(piece == Type.B_KING)
    				 King_BlackPcs++;
    			 else if (piece == Type.WHITE)
    				 Normal_WhitePcs++;
    			 else if (piece == Type.W_KING)
    				 King_WhitePcs++;
    		 }
    		 
    	 }
    }
    
    private void boardStart(boolean four_board) {
    	Normal_WhitePcs = 12;
    	Normal_BlackPcs = 12;
    	King_BlackPcs = 0;
    	King_WhitePcs = 0; 
    	
    	if(four_board==true) {
    		Normal_WhitePcs = 2;
    		Normal_BlackPcs = 2; 
    		SIZE = 4;
    	}
    	
    	board = new Type[SIZE][SIZE];
    	for(int i =0; i<board.length;i++) {
    		int start = 0;
    		if (i %2 ==0)
    			start = 1;
    		Type pieceType = Type.EMPTY;
    		if(i <= 2)
    			pieceType = Type.WHITE;
    		else if (i >= 5)
    			pieceType = Type.BLACK;
    		
    		for(int j = start; j < board[i].length; j += 2) {
    			board[i][j] = pieceType;
    		}
    		
    	}
    	mapEmptyTypeOnBoard();
    	 
    }
    
    private void mapEmptyTypeOnBoard() {
    	for(int i = 0; i < board.length; i++) {
    		for(int j =0; j < board[i].length;j++) {
    			if (board[i][j]==null) 
    				board[i][j] = Type.EMPTY;
    			
    		}
    	}
    }
    
    public Type getPiece(int row, int col) {
    	return board[row][col];
    }
    
    public Type getPiece(Point point) {
    	return board[point.x][point.y];
    }
    
    public Type[][] getBoard(){
    	return board;
    }
    
    public int getTotalWhitePcs() {
    	return Normal_WhitePcs  + King_WhitePcs;
    	
    }
    
    public int getTotalBlackPcs() {
    	return Normal_BlackPcs  + King_BlackPcs;
    	
    }
    
    public int getNumBlackKingPcs() {
    	return King_BlackPcs;
    }
    
    public int getNumWhiteKingPcs() {
    	return King_WhitePcs;
    }
    
    public int getNumNormalWhitePcs() {
    	return Normal_WhitePcs + King_WhitePcs;
    }
    
    public int getNumNormalBlackPcs() {
    	return Normal_BlackPcs + King_WhitePcs;
    }
    
    
    
/*	
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
		
		
	}*/
}