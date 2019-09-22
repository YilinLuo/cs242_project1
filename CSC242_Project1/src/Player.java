
public class Player {
	private PlayerID playerID;
	private String name;

	public Player() {
	}

	public enum PlayerID {
		BLACK, WHITE
	}

	public Player(String name, PlayerID playerID) {
		this.name = name;
		this.playerID = playerID;
	}

	public Player(PlayerID playerID) {
		this.name = playerID.toString();
		this.playerID = playerID;
	}

	public PlayerID getPlayerID() {
		return playerID;
	}

	public Board.Movement makeMove(Move m, Board b) {
		return b.makeMove(m, playerID);
	}

	public String toString() {
		return name + ": " + playerID;
	}

}
