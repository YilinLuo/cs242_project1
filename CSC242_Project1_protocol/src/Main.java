import java.util.Scanner;

public class Main {
	public static boolean board_4X4 = false;
	public static boolean random = false;
	public static boolean Minimax = false;
	public static boolean ABpruning = false;
	public static boolean H_Minimax = false;
	public static boolean playerFirst = false;
	public static int depthLimit;

	public static void main(String[] args) throws InterruptedException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Checker by Ruqin Chang and Yilin Luo");
		System.out.println("Choose your game: \n1.Small 4x4 Checkers \n2.Standard 8x8 Checkers \nYour choice?");
		int input = sc.nextInt();
		if (input == 1)
			board_4X4 = true;
		System.out.println(
				"Choose your opponent: \n1.An agent that plays randomly \n2.An agent that uses MINIMAX \n3.An agent that uses MINIMAX with alpha-beta pruning \n4.An agent that uses H-MINIMAX with a fixed depth cutoff");
		input = sc.nextInt();
		if (input == 1) {
			random = true;
		} else if (input == 2) {
			Minimax = true;
		} else if (input == 3) {
			ABpruning = true;
			System.out.println("Depth limit?");
			input = sc.nextInt();
			depthLimit = input;
		} else if (input == 4) {
			H_Minimax = false;
			System.out.println("Depth limit?");
			input = sc.nextInt();
			depthLimit = input;
		}
		System.out.println("Do you want to play Black(b) or White(w)?");
		if (sc.nextLine() == "b") {
			playerFirst = true;
		} else if (sc.nextLine() == "w") {
			playerFirst = false;
		}
		Player human1 = new Player("Player 1", Player.PlayerID.BLACK);
		Player human2 = new Player("Player 1", Player.PlayerID.WHITE);
		MinimaxAI AI1 = new MinimaxAI(Player.PlayerID.BLACK, depthLimit);
		MinimaxAI AI2 = new MinimaxAI(Player.PlayerID.WHITE, depthLimit);

		Board board = new Board();
		Player current;
		if (playerFirst = true) {
			current = human1;
		} else {
			current = AI1;
		}
		int c = 0;
		System.out.println(board.toString());
		while (c < 1000) {
			c++;
			System.out.println(current.toString() + "'s turn: ");

			Board.Movement movement = null;
			if (current instanceof AI) {
				movement = ((AI) current).makeMove(board);
				System.out.println();
			} else { // make movement based on player's inputs
				String text = sc.nextLine();
				String[] split = text.split(" ");
				Move m;
				if (split.length == 1) {
					m = new Move(Integer.parseInt(text.charAt(0) + ""), Integer.parseInt(text.charAt(1) + ""),
							Integer.parseInt(text.charAt(2) + ""), Integer.parseInt(text.charAt(3) + ""));
				} else {
					int[] ints = new int[split.length];
					for (int i = 0; i < split.length; i++) {
						ints[i] = Integer.parseInt(split[i]);
					}
					m = new Move(ints[0], ints[1], ints[2], ints[3]);

				}
				movement = current.makeMove(m, board);

			}
			// print out movement results
			if (movement == Board.Movement.INVALID_DESTINATION || movement == Board.Movement.INVALID_PIECE) {
				System.out.println("Invalid move.");
			} else if (movement == Board.Movement.MOVE_SUCCESS) {
				System.out.println(board.toString());
				if (board.getTotalBlackPcs() == 0) {
					System.out.println("White wins with " + board.getTotalWhitePcs() + " pieces left.");
					break;
				}
				if (board.getTotalWhitePcs() == 0) {
					System.out.println("Black wins with " + board.getTotalBlackPcs() + " pieces left.");
					break;
				}
				if (playerFirst)
					current = AI2;
				else
					current = human1;
				playerFirst = !playerFirst;
			} else if (movement == Board.Movement.ADDITIONAL_MOVE) {
				System.out.println("Additional Move");
			} else if (movement == Board.Movement.GAME_ENDED) {
				// current player cannot move
				if (current.getPlayerID() == Player.PlayerID.BLACK) {
					System.out.println("White wins.");
				} else {
					System.out.println("Black wins.");
				}
				break;
			}
		}
		System.out.println("Game finished after: " + c + "rounds");
		if (human1 instanceof MinimaxAI) {
			System.out.println("Average time per move: " + ((MinimaxAI) human1).getAverageTimePerMove());
		}

	}
}
