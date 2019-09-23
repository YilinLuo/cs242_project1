import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class MinimaxAI extends Player implements AI {

	private Point capture;
	private int depth;
	private long timeElapsed;
	private double numMoves;
	private int prune = 0;

	public MinimaxAI(PlayerID playerId, int depth) {
		super("MinimaxAI", playerId);
		this.depth = depth;
		this.timeElapsed = 0;
	}
/*
 * 
 * 
 * MINIMAXINITIAL
 * 
 * 
 * 
 * 
 */
	public Integer miniMaxInitial(Board board, int depth, PlayerID playerID, boolean maximizingPlayer, int val){
		List<Move> possibleMoves = board.getAllValidMoves(playerID);
		
		Board transitionBoard = null;
		int best = 0;
		if (maximizingPlayer) {
			best = Integer.MIN_VALUE;
			// Recur for possible moves
			for (int i = 0; i < possibleMoves.size(); i++) {
				transitionBoard = board.clone();
				transitionBoard.makeMove(possibleMoves.get(i), playerID);
				//before using possible moves, check if movement is null, then put into utilityFunction
				//check if movement is own piece
				if (!board.isMovingOwnPiece(possibleMoves.get(i).getStartPoint().x, possibleMoves.get(i).getStartPoint().y, playerID))
					break;
				int score = transitionBoard.utilityFunction(possibleMoves.get(i), playerID);
				best = (int) Math.max(score, best);
				val = miniMaxInitial(transitionBoard, depth - 1, reverseWithOpponent(playerID), !maximizingPlayer, best);
				
				
			}
			return best;
		}
		else{
			best = Integer.MAX_VALUE;
			// Recur for possible moves
			for (int i = 0; i < possibleMoves.size(); i++) {
				transitionBoard = board.clone();
				transitionBoard.makeMove(possibleMoves.get(i), playerID);

				if (!board.isMovingOwnPiece(possibleMoves.get(i).getStartPoint().x, possibleMoves.get(i).getStartPoint().y, playerID))
					break;
				int score = transitionBoard.utilityFunction(possibleMoves.get(i), playerID);
				best = Math.min(score, best);
				val = miniMaxInitial(transitionBoard, depth - 1, reverseWithOpponent(playerID), !maximizingPlayer,best);
				
			
			}
		}
		return val;
		
	}

	public Board.Movement makeMove(Board board) {
		numMoves++;
		long startTime = System.nanoTime();
		Move m = AB_Pruning(board, depth, getPlayerID(), true);
		timeElapsed += System.nanoTime() - startTime;
		Board.Movement movement = board.makeMove(m, getPlayerID());
		if (movement == Board.Movement.ADDITIONAL_MOVE)
			capture = m.getEndPoint();
		return movement;
	}

	public String getAverageTimePerMove() {
		return timeElapsed / numMoves * Math.pow(10, -6) + " milliseconds";
	}

	private Move AB_Pruning(Board board, int depth, PlayerID playerID, boolean maximizingPlayer) {
		double alpha = Double.NEGATIVE_INFINITY;
		double beta = Double.POSITIVE_INFINITY;

		List<Move> possibleMoves;
		if (capture == null)
			possibleMoves = board.getAllValidMoves(playerID);
		else {
			possibleMoves = board.getValidCaptureMoves(capture.x, capture.y, playerID);
			capture = null;
		}

		List<Double> heuristics = new ArrayList<>();
		if (possibleMoves.isEmpty())
			return null;
		Board transitionBoard = null;
		for (int i = 0; i < possibleMoves.size(); i++) {
			transitionBoard = board.clone();
			transitionBoard.makeMove(possibleMoves.get(i), playerID);
			heuristics.add(
					minimax(transitionBoard, depth - 1, reverseWithOpponent(playerID), !maximizingPlayer, alpha, beta));
		}

		double maxHeuristics = Double.NEGATIVE_INFINITY;

		Random rand = new Random();
		for (int i = heuristics.size() - 1; i >= 0; i--) {
			if (heuristics.get(i) >= maxHeuristics) {
				maxHeuristics = heuristics.get(i);
			}
		}
		for (int i = 0; i < heuristics.size(); i++) {
			if (heuristics.get(i) < maxHeuristics) {
				heuristics.remove(i);
				possibleMoves.remove(i);
				i--;
			}
		}
		return possibleMoves.get(rand.nextInt(possibleMoves.size()));
	}

	// return optimal value for current player
	private double minimax(Board board, int depth, PlayerID playerID, boolean maximizingPlayer, double alpha,
			double beta) {
		// Terminating condition
		if (depth == 0)
			return getHeuristic(board);

		List<Move> possibleMoves = board.getAllValidMoves(playerID);
		double best = 0;
		Board transitionBoard = null;

		if (maximizingPlayer) {
			best = Double.NEGATIVE_INFINITY;
			// Recur for possible moves
			for (int i = 0; i < possibleMoves.size(); i++) {
				transitionBoard = board.clone();
				transitionBoard.makeMove(possibleMoves.get(i), playerID);

				double val = minimax(transitionBoard, depth - 1, reverseWithOpponent(playerID), !maximizingPlayer,
						alpha, beta);

				best = Math.max(val, best);
				alpha = Math.max(alpha, best);

				// Alpha Beta Pruning
				if (alpha >= beta)
					break;
			}
			return best;
		} else {
			best = Double.POSITIVE_INFINITY;
			// Recur for possible moves
			for (int i = 0; i < possibleMoves.size(); i++) {
				transitionBoard = board.clone();
				transitionBoard.makeMove(possibleMoves.get(i), playerID);

				double val = minimax(transitionBoard, depth - 1, reverseWithOpponent(playerID), !maximizingPlayer,
						alpha, beta);

				best = Math.min(val, best);
				alpha = Math.min(alpha, best);
				// Alpha Beta Pruning
				if (alpha >= beta)
					break;
			}
		}
		return best;
	}

	private double getHeuristic(Board b) {
		double kingWeight = 2.0;
		double val = 0;
		if (getPlayerID() == PlayerID.WHITE) {
			val = b.getNumWhiteKingPcs() * kingWeight + b.getNumNormalWhitePcs() - b.getNumBlackKingPcs() * kingWeight
					- b.getNumNormalBlackPcs();
		} else
			val = b.getNumBlackKingPcs() * kingWeight + b.getNumNormalBlackPcs() - b.getNumWhiteKingPcs() * kingWeight
					- b.getNumNormalWhitePcs();
		return val;
	}

	private PlayerID reverseWithOpponent(PlayerID playerID) {
		if (playerID == PlayerID.BLACK)
			return PlayerID.WHITE;
		return PlayerID.BLACK;
	}
	
    /*
     * 
     * 
     * 
     * MiniMax ONLY 
     * 
     * 
     * 
     */
    static int minimax(int depth, int nodeIndex, boolean  isMax, 
            int scores[], int h) 
{ 
    // Terminating condition. i.e leaf node is reached 
    if (depth == h) 
        return scores[nodeIndex]; 
  
    // If current move is maximizer, find the maximum attainable 
    // value 
    if (isMax) 
    return Math.max(minimax(depth+1, nodeIndex*2, false, scores, h), 
            minimax(depth+1, nodeIndex*2 + 1, false, scores, h)); 
  
    // Else (If current move is Minimizer), find the minimum 
    // attainable value 
    else
        return Math.min(minimax(depth+1, nodeIndex*2, true, scores, h), 
            minimax(depth+1, nodeIndex*2 + 1, true, scores, h)); 
} 
  
// A utility function to find Log n in base 2 
 static int log2(int n) 
{ 
return (n==1)? 0 : 1 + log2(n/2); 
} 
  
// Driver code 
  
    public static void main (String[] args) { 
            // The number of elements in scores must be 
    // a power of 2. 
    int scores[] = {3, 5, 2, 9, 12, 5, 23, 23}; 
    int n = scores.length; 
    int h = log2(n); 
    int res = minimax(0, 0, true, scores, h); 
    System.out.println( "The optimal value is : "  +res);  
          
    } 

}
