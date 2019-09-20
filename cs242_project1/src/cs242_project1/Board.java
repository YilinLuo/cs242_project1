package cs242_project1;
import java.awt.Point;
import java.awt.print.*;
import java.util.ArrayList;
import java.util.List;

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
    
    //returns true if move successful
    public Game makeMove(Move move, Player.PlayerID playerID) {
    	if(move == null) {
    		return Game.GAME_ENDED;
    	}
    	
    	Point start = move.getStartPoint();
    	int startRow = start.x;
    	int startCol = start.y;
    	Point end = move.getEndPoint();
    	int endRow = end.x;
    	int endCol =  end.y;
    	
    	//only the piece on belonging side is moved, empty space cannot be moved
    	if(!isMovingOwnPiece(startRow, startCol, playerID)|| getPiece(startRow, startCol) == Type.EMPTY)
    		return Game.INVALID_PIECE;
    	List<Move> possibleMoves = getValidMoves(startRow, startCol, playerID);
    	
		return null;
    	
    }
    
    public List<Move> getAllValidMoves(Player.PlayerID playerID){
    	Type normal = playerID == Player.PlayerID.BLACK? Type.BLACK : Type.WHITE;
    	Type king = playerID == Player.PlayerID.BLACK? Type.B_KING : Type.W_KING;
    	
    	List<Move> possibleMoves = new ArrayList<>();
    	for(int i=0; i<SIZE;i++) {
    		for (int j = 0; j<SIZE; j++) {
    			Type t = getPiece(i,j);
    			if(t == normal || t ==king)
    				possibleMoves.addAll(getValidMoves(i,j,playerID));
    		}
    	}
    	return possibleMoves;
    }
    
    public List<Move> getValidMoves(int row, int col, Player.PlayerID playerID){
    	Type type = board[row][col];
    	Point startPoint = new Point(row,col);
    	if(type == Type.EMPTY) 
    		throw new IllegalArgumentException();
    	List<Move> moves = new ArrayList<>();
    	
    	//if king, 4 possible moves; if normal, 2. 
    	if(type == Type.WHITE || type == Type.BLACK) {
    		int rowChange = type == Type.WHITE ? 1 : -1;
    		
    		int newRow = row + rowChange;
    		if(newRow >= 0 || newRow< SIZE) {
    			int newCol = col +1;
    			if(newCol<SIZE && getPiece(newRow, newCol) == Type.EMPTY)
    				moves.add(new Move(startPoint, new Point(newRow, newCol)));
    			newCol = col -1;
    			if (newCol >=0 && getPiece(newRow, newCol) == Type.EMPTY)
    				moves.add(new Move(startPoint, new Point(newRow, newCol)));
    		}
    	}
    	
    	//case where it is a king
    	else {
    		// 4 possible moves
    		int newRow = row +1;
    		if (newRow < SIZE) {
    			int newCol = col +1;
    			if(newCol< SIZE && getPiece(newRow, newCol)== Type.EMPTY)
    				moves.add(new Move(startPoint, new Point(newRow, newCol)));
    			newCol = col-1;
    			if(newCol >= 0 && getPiece(newRow, newCol)== Type.EMPTY)
    				moves.add(new Move(startPoint, new Point(newRow, newCol)));
    				
    		}
    	}
    	
    	moves.addAll(getValidSkipMoves(row,col,playerID));
    	return moves;
    }
    
    public List<Move> getValidSkipMoves(int row, int col, Player.PlayerID playerID){
    	List<Move> move = new ArrayList<>();
    	Point start = new Point (row,col);
    	
    	List<Point> possibilities = new ArrayList<>();
    	
    	if(playerID == Player.PlayerID.WHITE && getPiece(row,col) == Type.WHITE) {
    		possibilities.add(new Point(row+2,col+2));
    		possibilities.add(new Point(row-2,col-2));
    	}
    	
    	else if(playerID == Player.PlayerID.BLACK && getPiece(row,col) == Type.BLACK) {
    		possibilities.add(new Point(row+2, col+2));
    		possibilities.add(new Point(row-2, col-2));
    	}
    	else if(getPiece(row,col) == Type.B_KING || getPiece(row,col)==Type.W_KING) {
    		possibilities.add(new Point(row+2,col+2));
    		possibilities.add(new Point(row+2,row-2));
    		possibilities.add(new Point(row-2,row+2));
    		possibilities.add(new Point(col-2,col-2));
    	}
    	for(int i=0; i< possibilities.size(); i++) {
    		Point temp = possibilities.get(i);
    		Move m = new Move(start,temp);
    		if(temp.x<SIZE&&temp.x>=0 && temp.y<SIZE && temp.y>=0 && getPiece(temp.x, temp.y) == Type.EMPTY && isOpponentPiece(playerID, getPiece(findMidSquare(m)))) {
    			move.add(m);
    		}
    	}
    }
    
    private boolean isMovingOwnPiece(int row, int col, Player.PlayerID playerID) {
    	Type pieceType = getPiece(row, col);
    	if(playerID == Player.PlayerID.BLACK && pieceType != Type.BLACK && pieceType != Type.B_KING)
    		return false;
    	else if (playerID == Player.PlayerID.WHITE && pieceType != Type.WHITE && pieceType != Type.W_KING)
    		return false;
    	return true;
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