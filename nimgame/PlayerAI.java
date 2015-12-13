/**
 * Omar Khashoggi
 * 201369650
 */

import gametree.*;
import java.util.Random;

public class PlayerAI {
	private SimpleNimGameTree gameTree;
	private int strategy;
	private boolean firstPlayer;
	private Random random;

	public final static int PERFECT_STRATEGY = 1;
	public final static int DECENT_STRATEGY = 2;
	public final static int RANDOM_STRATEGY = 3;

	public PlayerAI(SimpleNimGameTree gameTree, int strategy, boolean firstPlayer) {
		this.gameTree = gameTree;
		this.strategy = strategy;
		this.firstPlayer = firstPlayer;

		this.random = new Random();
	}

	public int makeMove() {
		if (strategy == PlayerAI.PERFECT_STRATEGY) {
			return makePerfectMove();
		} else if (strategy == PlayerAI.DECENT_STRATEGY) {
			int coinFlip = random.nextInt(2);
			if (coinFlip == 0) {
				return makePerfectMove();
			} else {
				return makeRandomMove();
			}
		} else if (strategy == PlayerAI.RANDOM_STRATEGY) {
			makeRandomMove();
		}
		
		return 1; //will never run
	}

	private int makePerfectMove() {
		int bestMove = gameTree.getNextBestMove(firstPlayer);
		if (bestMove == 0) {
			return 2;
		} else {
			return bestMove;
		}
	}

	private int makeRandomMove() {
		int possibleMoves = gameTree.possibleMoves();

		if (possibleMoves != 0) {
			return possibleMoves; //only one move is possible
		} else {
			int coinFlip = random.nextInt(2);
			if (coinFlip == 0) {
				return 1;
			} else {
				return 2;
			}
		}
	}
}