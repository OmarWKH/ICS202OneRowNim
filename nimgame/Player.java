/*
 * Omar Khashoggi
 * 201369650
 */

import gametree.*;
import java.util.Random;

/**
 * The class does not actually call removeOne() or removeTwo() in the game tree. It only decides what moves should be made and the Test class makes the move.
 * Making it work this way helps with the hint system in RealPlayer.
 */
public class Player {
	protected SimpleNimGameTree gameTree;
	protected int strategy;
	protected boolean firstPlayer;
	protected Random random;

	public final static int PERFECT_STRATEGY = 1;
	public final static int DECENT_STRATEGY = 2;
	public final static int RANDOM_STRATEGY = 3;

	public Player(SimpleNimGameTree gameTree, int strategy, boolean firstPlayer) {
		this.gameTree = gameTree;
		this.strategy = strategy;
		this.firstPlayer = firstPlayer;

		this.random = new Random();
	}

	public int getNextMove() {
		if (strategy == Player.PERFECT_STRATEGY) {
			return perfectMove();
		} else if (strategy == Player.DECENT_STRATEGY) {
			int coinFlip = random.nextInt(2);
			if (coinFlip == 0) {
				return perfectMove();
			} else {
				return randomMove();
			}
		} else if (strategy == Player.RANDOM_STRATEGY) {
			randomMove();
		}
		
		return 1; //will never run
	}

	private int perfectMove() {
		int bestMove = gameTree.getNextBestMove(firstPlayer);
		if (bestMove == 0) {
			return 2;
		} else {
			return bestMove;
		}
	}

	private int randomMove() {
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
