package cs242_project1;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MinimaxAI extends Player implements AI{

	
	private Point capture;
	private int depth; 
    private long timeMakeDecision;
    private double numberOfMoves;
    private int prune =0;
    
    public MinimaxAI(PlayerID playerId, int depth) {
    	super("MinimaxAI", playerId);
    	this.depth = depth;
    	this.timeMakeDecision = 0;
    }
    
    public Board.Movement makeMove(Board board){
    	numberOfMoves++;
    	long startTime= System.nanoTime();
    	Move m = minimaxStart(board, depth, getPlayerID(),true);
    	timeMakeDecision += System.nanoTime() - startTime;
    	Board.Movement game = board.makeMove(m, getPlayerID());
    	if(game == Board.Movement.ADDITIONAL_MOVE)
    		capture = m.getEndPoint();
    	return game;
    	
    }
    
    private Move minimaxStart(Board board, int depth, PlayerID playerID, boolean maximizingPlayer) {
    	double alpha = Double.NEGATIVE_INFINITY;
    	double beta = Double.POSITIVE_INFINITY;
    	
    	List<Move> possibleMoves;
    	if(capture ==null)
    		possibleMoves = board.getAllValidMoves(playerID);
    	else
    	{
    		possibleMoves = board.getValidCaptureMoves(capture.x, capture.y, playerID);
    		capture = null;
    	}
    	
    	List<Double> heuristics = new ArrayList<>();
    	if(possibleMoves.isEmpty())
    		return null;
    	Board transitionBoard = null;
    	for(int i = 0; i < possibleMoves.size();i++) {
    		transitionBoard = board.clone();
    		transitionBoard.makeMove(possibleMoves.get(i), playerID);
    		heuristics.add(minimax(transitionBoard, depth-1,reverseWithOpponent(playerID), !maximizingPlayer, alpha,beta));
    		
    	}
    	
    	double maxHeuristics = Double.NEGATIVE_INFINITY;
    	
    	Random rand = new Random();
    	for(int i = heuristics.size()-1; i >=0; i--) {
    		if(heuristics.get(i) >= maxHeuristics) {
    			maxHeuristics = heuristics.get(i);
    			
    		}
    	}
    	for(int i =0; i< heuristics.size();i++) {
    		if(heuristics.get(i)< maxHeuristics)
    		{
    			heuristics.remove(i);
    			possibleMoves.remove(i);
    			i--;
    		}
    	}
    	return possibleMoves.get(rand.nextInt(possibleMoves.size()));	
    	
    }
    
    private double minimax(Board board, int depth, PlayerID playerID, boolean maximizingPlayer, double alpha, double beta) {
    	if(depth == 0) {
    		return getHeuristic(board);
    	}
    	List<Move> possibleMoves = board.getAllValidMoves(playerID);
    	
    	double initial = 0;
    	Board transitionBoard = null;
    	if(maximizingPlayer)
    	{
    		initial = Double.NEGATIVE_INFINITY;
    		for(int i =0; i< possibleMoves.size(); i++) {
    			transitionBoard = board.clone();
    			transitionBoard.makeMove(possibleMoves.get(i), playerID);
    			
    			double result = minimax(transitionBoard, depth -1, reverseWithOpponent(playerID), !maximizingPlayer, alpha, beta );
    			
    			initial = Math.max(result, initial);
    			alpha = Math.max(alpha, initial);
    			
    			if(alpha >= beta)
    				break; 
    		}
    	}
    	
    	else
    	{
    		initial = Double.POSITIVE_INFINITY;
    		for(int i = 0; i< possibleMoves.size(); i++) {
    			transitionBoard = board.clone();
    			transitionBoard.makeMove(possibleMoves.get(i), playerID);
    			
    			double result = minimax(transitionBoard, depth-1, reverseWithOpponent(playerID), !maximizingPlayer, alpha, beta);
    			
    			initial = Math.min(result,initial);
    			alpha = Math.min(alpha, initial);
    			
    			if(alpha >= beta)
    				break;
    		}
    	}
    	
    	return initial;
    }
    
    private double getHeuristic(Board b) {
    	double kingWeight = 2.0;
    	double result = 0 ; 
    	if(getPlayerID() == PlayerID.WHITE) {
    		result = b.getNumWhiteKingPcs() * kingWeight + b.getNumNormalWhitePcs() - b.getNumBlackKingPcs()*kingWeight - b.getNumNormalBlackPcs();
    	}
    	else
    		result = b.getNumBlackKingPcs()* kingWeight + b.getNumNormalBlackPcs() - b.getNumWhiteKingPcs()*kingWeight - b.getNumNormalWhitePcs();
    	return result;
    }
    
    private PlayerID reverseWithOpponent(PlayerID playerID) {
    	if(playerID == PlayerID.BLACK)
    		return PlayerID.WHITE;
    	return PlayerID.BLACK;
    }
    
	
}
