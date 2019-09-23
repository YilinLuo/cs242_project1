import java.util.ArrayList;
import java.util.List;

public class MiniMaxWithoutPruning extends Board {

	public List<Move> getAllValidMoves(Player.PlayerID playerID){
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
	
	
}
