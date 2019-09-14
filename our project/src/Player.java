
public class Player {
	private Side side;
	private String name;

	public enum Side {
		BLACK, WHITE;
	}

	public Player() {
	}

	public Player(String name, Side side) {
		this.name = name;
		this.side = side;
	}

	// public Player(Side side) {
	// this.name = side.toString();
	// this.side = side;
	// }
	public Side getSide() {
		return side;
	}

}
