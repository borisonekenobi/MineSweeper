import java.util.Scanner;

public class Main {
	private static Board board;

	public static void main (String[] args) {
		System.out.println("Welcome to Mine Sweeper!");

		Scanner input = new Scanner(System.in);
		byte length = 16;
		byte width = 16;
		/*do {

		} while (length == null && width == null);*/

		byte numMines = 30;
		/*do {

		} while (numMines == null);*/

		System.out.println("Creating board!");
		board = new Board(length, width, numMines);
		outputBoard();
		while (true) {
			System.out.print("Choose space to reveal: ");
			byte[] position = {(byte) (input.nextByte() - 1), (byte) (input.nextByte() - 1)};
			board.updateBoard(position);
			if (board.lostGame(position)) {
				endGame("You hit a mine!", position);
			}
			if (board.wonGame()) {
				endGame("You survived!", position);
			}
			outputBoard();
		}
	}

	private static void outputBoard () {
		System.out.print(" ");
		for (byte i = 1; i <= board.getLENGTH(); i++) {
			System.out.printf("%3d", i);
		}
		System.out.println();
		byte[][] currentBoard = board.getHiddenBoard();
		for (byte i = 0; i < board.getLENGTH(); i++) {
			System.out.printf("%2d ", i + 1);
			for (byte j = 0; j < board.getWIDTH(); j++) {
				byte aByte = currentBoard[i][j];
				if (aByte == -1) {
					System.out.print("*  ");
				} else if (aByte == 0) {
					System.out.print("\u001B[31m*\u001B[38m  ");
				} else if (aByte == 9) {
					System.out.print("-  ");
				} else if (aByte == 10) {
					System.out.print("\u001B[30m-\u001B[38m  ");
				} else {
					System.out.print(aByte + "  ");
				}
			}
			System.out.println();
		}
	}

	private static void endGame (String endMessage, byte[] pos) {
		board.showMines(pos);
		outputBoard();
		System.out.println(endMessage);
		System.exit(0);
	}
}
