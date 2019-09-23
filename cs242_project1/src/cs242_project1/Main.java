package cs242_project1;

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
		System.out.println("Choose your game: \n1. Small 4x4 Checkers \n2.Standard 8x8 Checkers \nYour choice?");
		int input = sc.nextInt();
		if(input == 1)
			board_4X4 = true;
		System.out.println("Choose your opponent: /n1. An agent that plays randomly /n2. An agent that uses MINIMAX /n3. An agent that uses MINIMAX with alpha-beta pruning /n4. An agent that uses H-MINIMAX with a fixed depth cutoff");
		input = sc.nextInt();
		if(input == 1) {
			random = true;
		} else if (input == 2) {
			Minimax = true;
		} else if(input==3) {
			ABpruning = true;
		} else if(input==4) {
			H_Minimax = false;
			System.out.println("Depth limit?");
			input = sc.nextInt();
			depthLimit = input;
		}
		System.out.println("Do you want to play Black(b) or White(w)?");
		if (sc.nextLine() == "b") {
			Player player = new Player("Player 1", Player.PlayerID.BLACK);
			Player current = player;
			MinimaxAI playerAI = new MinimaxAI(Player.PlayerID.WHITE, depthLimit);
		}else if(sc.nextLine()=="w") {
			Player player = new Player("Player 1", Player.PlayerID.WHITE);
			MinimaxAI playerAI = new MinimaxAI(Player.PlayerID.BLACK, depthLimit);
			Player current = playerAI;
		}
		
		Board board = new Board();
		Board.Movement movement = null;
//		if (curent instanceof AI)
		
	}
}
		
//	Player player = new Player("Player 1", Player.PlayerID.BLACK);
//	MinimaxAI playerAI = new MinimaxAi(Player.PlayerID.WHITE, 6);
//	boolean playerFirst = true;
//
//	int blackWin = 0;
//	int whiteWin = 0;
//
//	for(
//	int t = 0;t<total;t++)
//	{
//		Board board = new Board();
//		Player current = player;
//		if (!playerFirst)
//			current = playerAI;
//		int round = 0;
//		while (round < 1000) {
//			round++;
//			print(current.toString() + "'s turn: ");
//
//			Board.Movement movement = null;
//			if (current instanceof AI) {
//				movement = ((AI) current).makeMove(board);
//				println();
//			} else { // make movement based on player's inputs