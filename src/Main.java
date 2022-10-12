import java.util.Scanner;

public class Main {
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	private static Board board;

	public static void main (String[] args) {
		Scanner input = new Scanner(System.in);
		byte length;
		byte width;
		byte numMines;

		System.out.println("Welcome to Mine Sweeper!");

		do {
			try {
				System.out.print("Choose the length and width of your board: ");
				length = input.nextByte();
				width = input.nextByte();
			} catch (Throwable err) {
				System.out.println("Incorrect Input!");
				length = 0;
				width = 0;
				input.next();
			}
		} while (length <= 0 && width <= 0);

		do {
			try {
				System.out.print("Choose the amount of mines on your board: ");
				numMines = input.nextByte();
			} catch (Throwable err) {
				System.out.println("Incorrect Input!");
				numMines = 0;
				input.next();
			}
		} while (numMines <= 0 || numMines > length * width - 1);

		System.out.println("Creating board!");
		board = new Board(length, width, numMines);
		outputBoard();
		while (true) {
			System.out.print("Choose space to reveal: ");
			byte[] position;
			try {
				position = new byte[]{(byte) (input.nextByte() - 1), (byte) (input.nextByte() - 1)};
			} catch (Throwable err) {
				System.out.println("Incorrect Input!");
				input.next();
				continue;
			}
			board.updateBoard(position);
			if (board.lostGame(position)) {
				endGame("You hit a mine!", position);
				break;
			}
			if (board.wonGame()) {
				endGame("You survived!", position);
				break;
			}
			outputBoard();
		}
	}

	private static void outputBoard () {
		System.out.print(" ");
		for (byte i = 1; i <= board.getWIDTH(); i++) {
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
					System.out.print(ANSI_RED + "*  " + ANSI_RESET);
				} else if (aByte == 9) {
					System.out.print("-  ");
				} else if (aByte == 10) {
					System.out.print(ANSI_BLACK + "-  " + ANSI_RESET);
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
	}
}
