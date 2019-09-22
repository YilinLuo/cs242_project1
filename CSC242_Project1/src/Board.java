import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Board {
	private Type[][] board;
	public int SIZE = 8;
	public boolean board_4X4;

	private enum Type {
		EMPTY, WHITE, BLACK, W_KING, B_KING
	}

	public enum Movement {
		MOVE_SUCCESS, INVALID_DESTINATION, INVALID_PIECE, ADDITIONAL_MOVE, GAME_ENDED
	}

	private int Normal_WhitePcs;
	private int Normal_BlackPcs;
	private int King_BlackPcs;
	private int King_WhitePcs;

	public Board() {
//		 setUpBoard(board_4X4);
	}

	public Board(Type[][] board, boolean board_4X4) {
		Normal_WhitePcs = 0;
		Normal_BlackPcs = 0;
		King_BlackPcs = 0;
		King_WhitePcs = 0;

		if (board_4X4 == true) {
			SIZE = 4;
		}
		this.board = board;
		// update the number of piece based on piece type
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				Type piece = getPiece(i, j);
				if (piece == Type.BLACK)
					Normal_BlackPcs++;
				else if (piece == Type.B_KING)
					King_BlackPcs++;
				else if (piece == Type.WHITE)
					Normal_WhitePcs++;
				else if (piece == Type.W_KING)
					King_WhitePcs++;
			}
		}
	}

	// Method to set up board
	private void setUpBoard(boolean board_4X4) {
		Normal_WhitePcs = 12;
		Normal_BlackPcs = 12;
		King_BlackPcs = 0;
		King_WhitePcs = 0;

		if (board_4X4 == true) {
			Normal_WhitePcs = 2;
			Normal_BlackPcs = 2;
			SIZE = 4;
		}

		board = new Type[SIZE][SIZE];
		// Add piece types
		for (int i = 0; i < board.length; i++) {
			int firstPiece = 0;
			if (i % 2 == 0)
				firstPiece = 1;
			Type pieceType = Type.EMPTY;
			if (i <= 2)
				pieceType = Type.WHITE;
			else if (i >= 5)
				pieceType = Type.BLACK;
			for (int j = firstPiece; j < board[i].length; j += 2) {
				board[i][j] = pieceType;
			}
		}
		addEmptyType();
	}

	// Method to add empty piece type
	private void addEmptyType() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == null)
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

	public Type[][] getBoard() {
		return board;
	}

	public int getTotalWhitePcs() {
		return Normal_WhitePcs + King_WhitePcs;

	}

	public int getTotalBlackPcs() {
		return Normal_BlackPcs + King_BlackPcs;

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

	// returns true if move successful
	public Movement makeMove(Move move, Player.PlayerID playerID) {
		// A player loses if they cannot make a legal move.
		if (move == null) {
			return Movement.GAME_ENDED;
		}

		Point start = move.getStartPoint();
		int startRow = start.x;
		int startCol = start.y;
		Point end = move.getEndPoint();
		int endRow = end.x;
		int endCol = end.y;

		// player can only move own piece diagonally forward to unoccupied square
		if (!isMovingOwnPiece(startRow, startCol, playerID) || getPiece(startRow, startCol) == Type.EMPTY)
			return Movement.INVALID_PIECE;
		List<Move> ProbableMoves = getValidMoves(startRow, startCol, playerID);

		Type current = getPiece(startRow, startCol);
		if (ProbableMoves.contains(move)) {
			boolean captureMove = false;
			if (startRow + 1 == endRow || startRow - 1 == endRow) {
				// remove the orginal piece after 1 movement
				board[startRow][startCol] = Type.EMPTY;
				board[endRow][endCol] = current;
			} else {
				// remove captured pieces from the board
				captureMove = true;
				board[startRow][startCol] = Type.EMPTY;
				board[endRow][endCol] = current;
				Point captured = getCapturedPcs(move);
				Type cap = getPiece(captured);
				if (cap == Type.BLACK)
					Normal_BlackPcs--;
				else if (cap == Type.B_KING)
					King_BlackPcs--;
				else if (cap == Type.WHITE)
					Normal_WhitePcs--;
				else if (cap == Type.W_KING)
					King_WhitePcs--;
				board[captured.x][captured.y] = Type.EMPTY;
			}

			// check the square behind captured piece in the same direction is empty
			if (captureMove) {
				List<Move> additionalMove = getValidCaptureMoves(endRow, endCol, playerID);
				if (additionalMove.isEmpty())
					return Movement.MOVE_SUCCESS;
				return Movement.ADDITIONAL_MOVE;
			}

			// When a piece reaches the farthest row forward and becomes a king
			// update number for piece type
			if (endRow == 0 & playerID == Player.PlayerID.BLACK) {
				board[endRow][endCol] = Type.B_KING;
				Normal_BlackPcs--;
				King_BlackPcs++;
			} else if (endRow == SIZE - 1 & playerID == Player.PlayerID.WHITE) {
				board[endRow][endCol] = Type.W_KING;
				Normal_WhitePcs--;
				King_WhitePcs++;
			}
			return Movement.MOVE_SUCCESS;
		} else
			return Movement.INVALID_DESTINATION;
	}

	// return a list of all valid moves for a player
	public List<Move> getAllValidMoves(Player.PlayerID playerID) {
		Type normal = playerID == Player.PlayerID.BLACK ? Type.BLACK : Type.WHITE;
		Type king = playerID == Player.PlayerID.BLACK ? Type.B_KING : Type.W_KING;
		List<Move> ProbableMoves = new ArrayList<>();
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				Type t = getPiece(i, j);
				if (t == normal || t == king)
					// add valid moves when there is a player's piece
					ProbableMoves.addAll(getValidMoves(i, j, playerID));
			}
		}
		return ProbableMoves;
	}

	// return a list of valid moves at specific location
	public List<Move> getValidMoves(int row, int col, Player.PlayerID playerID) {
		Type type = board[row][col];
		Point startPoint = new Point(row, col);
		if (type == Type.EMPTY)
			throw new IllegalArgumentException();
		List<Move> moves = new ArrayList<>();

		// if there is an unoccupied square diagonally forward, add moves
		// if normal piece type, 2 possible move
		if (type == Type.WHITE || type == Type.BLACK) {
			int rowChange = type == Type.WHITE ? 1 : -1;
			int newRow = row + rowChange;
			// if the piece is in the board after move
			if (newRow >= 0 || newRow < SIZE) {
				int newCol = col + 1;
				if (newCol < SIZE && getPiece(newRow, newCol) == Type.EMPTY)
					moves.add(new Move(startPoint, new Point(newRow, newCol)));
				newCol = col - 1;
				if (newCol >= 0 && getPiece(newRow, newCol) == Type.EMPTY)
					moves.add(new Move(startPoint, new Point(newRow, newCol)));
			}
		} else { // if king piece type, 4 possible moves
			int newRow = row + 1;
			if (newRow < SIZE) {
				int newCol = col + 1;
				if (newCol < SIZE && getPiece(newRow, newCol) == Type.EMPTY)
					moves.add(new Move(startPoint, new Point(newRow, newCol)));
				newCol = col - 1;
				if (newCol >= 0 && getPiece(newRow, newCol) == Type.EMPTY)
					moves.add(new Move(startPoint, new Point(newRow, newCol)));
			}
			newRow = row - 1;
			if (newRow >= 0) {
				int newCol = col + 1;
				if (newCol < SIZE && getPiece(newRow, newCol) == Type.EMPTY)
					moves.add(new Move(startPoint, new Point(newRow, newCol)));
				newCol = col - 1;
				if (newCol >= 0 && getPiece(newRow, newCol) == Type.EMPTY)
					moves.add(new Move(startPoint, new Point(newRow, newCol)));
			}
		}
		moves.addAll(getValidCaptureMoves(row, col, playerID));
		return moves;
	}

	// get the diagonally adjacent square (captured piece)
	private Point getCapturedPcs(Move move) {
		Point captured = new Point((move.getStartPoint().x + move.getEndPoint().x) / 2,
				(move.getStartPoint().y + move.getEndPoint().y) / 2);
		return captured;
	}

	public List<Move> getValidCaptureMoves(int row, int col, Player.PlayerID playerID) {
		List<Move> move = new ArrayList<>();
		Point start = new Point(row, col);

		// Add all probable captured piece in probableCaptured list
		List<Point> probableCaptured = new ArrayList<>();
		if (playerID == Player.PlayerID.WHITE && getPiece(row, col) == Type.WHITE) {
			probableCaptured.add(new Point(row + 2, col + 2));
			probableCaptured.add(new Point(row - 2, col - 2));
		} else if (playerID == Player.PlayerID.BLACK && getPiece(row, col) == Type.BLACK) {
			probableCaptured.add(new Point(row + 2, col + 2));
			probableCaptured.add(new Point(row - 2, col - 2));
		} else if (getPiece(row, col) == Type.B_KING || getPiece(row, col) == Type.W_KING) {
			probableCaptured.add(new Point(row + 2, col + 2));
			probableCaptured.add(new Point(row + 2, row - 2));
			probableCaptured.add(new Point(row - 2, row + 2));
			probableCaptured.add(new Point(col - 2, col - 2));
		}
		// add valid capture moves
		for (int i = 0; i < probableCaptured.size(); i++) {
			Point cap = probableCaptured.get(i);
			Move m = new Move(start, cap);
			if (cap.x < SIZE && cap.x >= 0 && cap.y < SIZE && cap.y >= 0 && getPiece(cap.x, cap.y) == Type.EMPTY
					&& isOpponentPiece(playerID, getPiece(getCapturedPcs(m)))) {
				move.add(m);
			}
		}
		return move;
	}

	// return true if the piece is oppoenet's piece
	private boolean isOpponentPiece(Player.PlayerID current, Type opponentPiece) {
		if (current == Player.PlayerID.BLACK && (opponentPiece == Type.WHITE || opponentPiece == Type.W_KING))
			return true;
		if (current == Player.PlayerID.WHITE && (opponentPiece == Type.BLACK || opponentPiece == Type.B_KING))
			return true;
		return false;
	}

	// return true if it is own pieces
	private boolean isMovingOwnPiece(int row, int col, Player.PlayerID playerID) {
		Type pieceType = getPiece(row, col);
		if (playerID == Player.PlayerID.BLACK && pieceType != Type.BLACK && pieceType != Type.B_KING)
			return false;
		else if (playerID == Player.PlayerID.WHITE && pieceType != Type.WHITE && pieceType != Type.W_KING)
			return false;
		return true;
	}

	// Method to clone the board
	public Board clone() {
		if (board_4X4 == true) {
			SIZE = 4;
		}
		Type[][] newBoard = new Type[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				newBoard[i][j] = board[i][j];
			}
		}
		Board b = new Board(newBoard, board_4X4);
		return b;
	}
	
	// Method to print out the board
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("  ");
        for (int i = 0; i < board.length; i++) {
            b.append(i + " ");
        }
        b.append("\n");
        for (int i = 0; i < board.length; i++) {
            for (int j = -1; j < board[i].length; j++) {
                String a = "";
                if (j == -1)
                    a = i + "";
                else if (board[i][j] == Type.WHITE)
                    a = "w";
                else if (board[i][j] == Type.BLACK)
                    a = "b";
                else if (board[i][j] == Type.W_KING)
                    a = "W";
                else if (board[i][j] == Type.B_KING)
                    a = "B";
                else
                    a = "_";

                b.append(a);
                b.append(" ");
            }
            b.append("\n");
        }
        return b.toString();
    }

	/*
	 * public static void main(String args[]){ int upper_eight = 8; //for 4*4 grid
	 * int size_four = 4; int[][] grid = new int[size_four][size_four];
	 * 
	 * for (int row = 0; row < size_four; row++) { for (int col = 0; col <
	 * size_four; col++) { System.out.printf(" _", grid[row][col]); }
	 * System.out.println(); }
	 * 
	 * 
	 * System.out.println(); System.out.println(); System.out.println();
	 * System.out.println();
	 * 
	 * 
	 * 
	 * 
	 * for(int i=0; i < upper_eight+1;i++) { System.out.print(" " +i ); }
	 * System.out.println(); int size_eight = 9; int[][] grid_eight = new
	 * int[size_eight][size_eight];
	 * 
	 * for (int row = 0; row < size_eight-1; row++) {
	 * 
	 * System.out.printf(Integer.toString(row), grid_eight[0]);
	 * 
	 * for (int col = 0; col < size_eight-1; col++) { if(row ==3 & col== 3) {
	 * System.out.printf(" B", grid_eight[row][col]); } else {
	 * System.out.printf(" _", grid_eight[row][col]); }
	 * 
	 * } System.out.println(); }
	 * 
	 * 
	 * }
	 */
}