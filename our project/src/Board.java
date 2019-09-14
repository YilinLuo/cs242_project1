import java.awt.Point;

public class Board {
	private Type[][] board;
	public int SIZE = 8;

	private int numWN;
	private int numBN;
	private int numWK;
	private int numBK;

	private enum Type {
		EMPTY, WHITE, BLACK, WHITE_KING, BLACK_KING;
	}

	public enum MOVEFEEDBACK {
		COMPLETED, FAILED_INVALID_PIECE, FAILED_INVALID_DESTINATION, MULTIPLE_MOVE, GAME_ENDED;
	}

	public Board(Type[][] board) {
		numWN = 0;
		numBN = 0;
		numWK = 0;
		numWK = 0;

		this.board = board;
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				Type piece = getPiece(i, j);
				if (piece == Type.BLACK)
					numBN++;
				else if (piece == Type.WHITE)
					numWN++;
				else if (piece == Type.BLACK_KING)
					numBK++;
				else if (piece == Type.WHITE_KING)
					numWK++;
			}
		}
	}

	private void setUp8x8() {
		numBN = 12;
		numWN = 12;
		numBK = 0;
		numWK = 0;
		board = new Type[8][8];
		for (int i = 0; i < board.length; i++) {
			int firstPiece = 0;
			if (i % 2 == 0)
				firstPiece = 1;

			Type pieceType = Type.EMPTY;
			if (i <= 2)
				pieceType = Type.WHITE;
			else if (i >= 5)
				pieceType = Type.BLACK;
			// add pieceType
			for (int j = firstPiece; j < board[i].length; j += 2) {
				board[i][j] = pieceType;
			}
		}
		// add empty space
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == null)
					board[i][j] = Type.EMPTY;
			}
		}
	}

	private void setUp4x4() {
		numBN = 2;
		numWN = 2;
		numBK = 0;
		numWK = 0;
		board = new Type[8][8];
		for (int i = 0; i < board.length; i++) {
			int firstPiece = 0;
			if (i % 2 == 0)
				firstPiece = 1;

			Type pieceType = Type.EMPTY;
			if (i == 0)
				pieceType = Type.WHITE;
			else if (i == 3)
				pieceType = Type.BLACK;
			// add pieceType
			for (int j = firstPiece; j < board[i].length; j += 2) {
				board[i][j] = pieceType;
			}
		}
		// add empty space
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

	public int getNumW() {
		return numWN + numWK;
	}

	public int getNumB() {
		return numBN + numBK;
	}

	public int getNumWN() {
		return numWN;
	}

	public int getNumBN() {
		return numBN;
	}

	public int getNumWK() {
		return numWK;
	}

	public int getNumBK() {
		return numBK;
	}

	// Yilin print out broad
//	public static void main(String args[]) {
//		int upper_eight = 8;
//		// for 4*4 grid
//		int size_four = 4;
//		int[][] grid = new int[size_four][size_four];
//
//		for (int row = 0; row < size_four; row++) {
//			for (int col = 0; col < size_four; col++) {
//				System.out.printf(" _", grid[row][col]);
//			}
//			System.out.println();
//		}
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		System.out.println();
//
//		// for 8*8 grid
//
//		for (int i = 0; i < upper_eight + 1; i++) {
//			System.out.print(" " + i);
//		}
//		System.out.println();
//		int size_eight = 9;
//		int[][] grid_eight = new int[size_eight][size_eight];
//
//		for (int row = 0; row < size_eight - 1; row++) {
//
//			System.out.printf(Integer.toString(row), grid_eight[0]);
//			
//			for (int col = 0; col < size_eight - 1; col++) {
//				if (row == 3 & col == 3) {
//					System.out.printf(" B", grid_eight[row][col]);
//				} else {
//					System.out.printf(" _", grid_eight[row][col]);
//				}
//
//			}
//			System.out.println();
//		}
//	}

}
