import java.util.Arrays;
import java.util.Random;

public class Board {
	private final Random RANDOM = new Random();
	private final byte NUM_MINES;
	private final byte LENGTH;
	private final byte WIDTH;
	private final byte[][] HIDDEN_BOARD;
	private final byte[][] VISIBLE_BOARD;
	private boolean isFirstMove = true;


	public Board (byte length, byte width, byte numMines) {
		this.LENGTH = length;
		this.WIDTH = width;
		this.NUM_MINES = numMines;
		this.HIDDEN_BOARD = new byte[LENGTH][WIDTH];
		this.VISIBLE_BOARD = new byte[LENGTH][WIDTH];

		for (byte[] bytes : HIDDEN_BOARD) {
			Arrays.fill(bytes, (byte) 9);
		}

		for (byte[] bytes : VISIBLE_BOARD) {
			Arrays.fill(bytes, (byte) 9);
		}
	}

	public byte getLENGTH () {
		return LENGTH;
	}

	public byte getWIDTH () {
		return WIDTH;
	}

	public byte[][] getHiddenBoard () {
		return HIDDEN_BOARD;
	}

	public void updateBoard (byte[] pos) {
		if (isFirstMove) {
			do {
				createVisibleBoard();
			} while (VISIBLE_BOARD[pos[0]][pos[1]] == 9);
			isFirstMove = false;
			updateBoard(pos);
		} else {
			HIDDEN_BOARD[pos[0]][pos[1]] = VISIBLE_BOARD[pos[0]][pos[1]];
			revealNeighbouringValues(pos);
		}
	}

	private void createVisibleBoard () {
		for (byte i = 0; i < NUM_MINES; i++) {
			byte[] pos;
			do {
				pos = new byte[] {(byte) (RANDOM.nextInt(1, VISIBLE_BOARD.length) - 1), (byte) (RANDOM.nextInt(1, VISIBLE_BOARD[0].length) - 1)};
			} while (VISIBLE_BOARD[pos[0]][pos[1]] == 0);
			VISIBLE_BOARD[pos[0]][pos[1]] = 0;
		}

		for (byte i = 0; i < LENGTH; i++) {
			for (byte j = 0; j < WIDTH; j++) {
				VISIBLE_BOARD[i][j] = checkSurroundings(i, j);
			}
		}
	}

	private void revealNeighbouringValues(byte[] pos) {
		if (VISIBLE_BOARD[pos[0]][pos[1]] != 10) return;
		for (byte i = (byte) (pos[0] - 1); i < pos[0] + 2; i++) {
			if (i < 0 || i >= LENGTH) continue;
			for (byte j = (byte) (pos[1] - 1); j < pos[1] + 2; j++) {
				if (j < 0 || j >= WIDTH) continue;

				if (HIDDEN_BOARD[i][j] == 9) {
					HIDDEN_BOARD[i][j] = VISIBLE_BOARD[i][j];
					if (HIDDEN_BOARD[i][j] == 10) {
						revealNeighbouringValues(new byte[] {i, j});
					}
				} else if (VISIBLE_BOARD[i][j] > 0 || VISIBLE_BOARD[i][j] <= 8) {
					HIDDEN_BOARD[i][j] = VISIBLE_BOARD[i][j];
				}
			}
		}
	}

	public boolean wonGame() {
		for (byte i = 0; i < LENGTH; i++) {
			for (byte j = 0; j < WIDTH; j++) {
				if (HIDDEN_BOARD[i][j] == 9 && VISIBLE_BOARD[i][j] != 0) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean lostGame(byte[] pos) {
		return VISIBLE_BOARD[pos[0]][pos[1]] == 0;
	}

	private byte checkSurroundings(byte posX, byte posY) {
		if (VISIBLE_BOARD[posX][posY] == 0) return 0;
		byte count = 0;
		for (byte i = -1; i < 2; i++) {
			for (byte j = -1; j < 2; j++) {
				if (isBomb((byte) (posX + i), (byte) (posY + j))) count++;
			}
		}
		return count == 0 ? 10 : count;
	}

	private boolean isBomb(byte posX, byte posY) {
		if (posX < 0 || posX >= LENGTH) return false;
		if (posY < 0 || posY >= WIDTH) return false;
		return VISIBLE_BOARD[posX][posY] == 0;
	}
}
