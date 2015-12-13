/**
 * Omar Khashoggi
 * 201369650
 *
 * javac gametree/*.java && javac *.java
 * java TestGameTree
 */

import gametree.*;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Random;

public class TestGameTree {
	/*
	 * Refactoring
	 * Organize and document code
	 * buield?
	 *
	 *  hint: show next possible move if player want
	 *  optional: perfect, somewhat perfect, random strategy
	 */

	public static SimpleNimGameTree tree;
	public static PlayerAI playerAI;
	public static int hintLevel;

	public static boolean playerIsFirst;
	public static boolean currentTurnIsPlayers;
	public static boolean playerWon;
	public static boolean gameIsDone;

	public static Scanner userInput;
	public static Random random;

	public static void main(String[] args) {
		userInput = new Scanner(System.in);
		random = new Random();

		startingUI();
		userInput.close();
	}

	public static void startingUI() {
		tree = null;
		playerAI = null;
		System.out.println();

		String feedback;
		
		//number of elements in tree
		while (tree == null) {
			int elements;
			System.out.print("Enter number of elements (0 to exit): ");
			try {
				elements = userInput.nextInt();
			} catch (InputMismatchException ime) {
				printFeedback("Invalid input. Input an integer");
				userInput.next();
				continue;
			}

			if (elements == 0) {
				exitGame();
			} else {
				try {
					tree = new SimpleNimGameTree(elements);
				} catch (OutOfMemoryError oome) {
					printFeedback("We ran out of heap space. Try entering less elements");
					continue;
				}

				feedback = "Created tree with " + elements + " elements";
				printFeedback(feedback);
			}
		}

		//playing first or second
		boolean validInput = false;
		while (!validInput) {
			System.out.print("Do you want to go first (true/false)? ");
			try {
				playerIsFirst = userInput.nextBoolean();
			} catch (InputMismatchException ime) {
				printFeedback("Invalid input. Input a boolean");
				userInput.next();
				continue;
			}

			validInput = true;
			currentTurnIsPlayers = playerIsFirst;
			feedback = playerIsFirst ? "Player is first" : "Computer is first";
			printFeedback(feedback);
		}

		//computer and hint strategy
		System.out.println(PlayerAI.PERFECT_STRATEGY + ". Perfect strategy (deterministic with minimax, remove 2 if no move is optimal)");
		System.out.println(PlayerAI.DECENT_STRATEGY + ". Decent strategy (each hint is a coin flip between perfect and random)");
		System.out.println(PlayerAI.RANDOM_STRATEGY + ". Random strategy (coin flip)");
		while (playerAI == null) {
			System.out.print("Choose a strategy for the computer and the hint system by inputting a number: ");
			
			int strategy;
			try {
				strategy = userInput.nextInt();
			} catch (InputMismatchException ime) {
				printFeedback("Invalid input. Input an integer");
				userInput.next();
				continue;
			}

			hintLevel = strategy;
			playerAI = new PlayerAI(tree, strategy, !playerIsFirst);
			printFeedback("Your opponent is assembled and awaits you eagerly");
		}

		inGameUI();
	}

	public static void inGameUI() {
		System.out.println();

		final int TAKE_1 = 1;
		final int TAKE_2 = 2;
		final int SHOW_HINT = 3;
		final int OPTIONS = 4;
		
		int moveValue;
		gameIsDone = false;
		while (!gameIsDone) {
			printFeedback("STATE: " + gameState());

			int input = 0;
			if (currentTurnIsPlayers) {
				input = getUserInputInGame();
			} else {
				input = playerAI.makeMove();
			}

			switch (input) {
				case TAKE_1:
					moveValue = tree.removeOne(); printFeedback("PLAYED: Removed one");
					checkMoveValue(moveValue); break;
				case TAKE_2:
					moveValue = tree.removeTwo(); printFeedback("PLAYED: Removed two");
					checkMoveValue(moveValue); break;
				case SHOW_HINT:
					printFeedback("HINT: " + getWrittenHintForPlayer());
					break;
				case OPTIONS:
					optoinsUI();
					break;
				default: break;
			}
			
		}

		String feedback = playerWon ? "Well done" : "Better luck next time";
		printFeedback("END: " + feedback);
		startingUI();
	}

	public static void optoinsUI() {
		System.out.println();

		final int EXIT = 0;
		final int SHOW_STATS = 1;
		final int SHOW_TREE = 2;

		int input = 0;
		input = getUserInputOptions();
		
		switch (input) {
			case EXIT:
				exitGame(); break;
			case SHOW_STATS: printFeedback("TREE INFO: " + treeStatistics()); break;
			case SHOW_TREE: new GameTreeWindow(tree); break;
			default: break;
		}

		System.out.println();
	}

	public static int printInGameChoices() {
		System.out.print("1. Take 1 || ");
		System.out.print("2. Take 2 || ");
		System.out.print("3. Show hint || ");
		System.out.println("4. Options");

		int lastChoice = 4;
		return lastChoice;
	}

	public static int printOptionsChoices() {
		System.out.println("0. Exit");
		System.out.print("1. Show tree statstics || ");
		System.out.println("2. Show game tree");

		int lastChoice = 2;
		return lastChoice;
	}

	public static int getUserInputInGame() {
		int lastChoice = TestGameTree.printInGameChoices();
		
		int input = 0;
		boolean validInput = false;
		while (!validInput) {
			System.out.print("Choose by inputting a number: ");
			try {
				input = userInput.nextInt();
			} catch (InputMismatchException ime) {
				printFeedback("Invalid input. Input an integer");
				userInput.next();
				continue;
			}

			validInput = (1 <= input) && (input <= lastChoice);
			if (!validInput) {
				printFeedback("Invalid input. Input on of the shown options");
			}
		}

		return input;
	}

	public static int getUserInputOptions() {
		int lastChoice = TestGameTree.printOptionsChoices();
		
		int input = -1;
		boolean validInput = false;
		while (!validInput) {
			System.out.print("Choose by inputting a number: ");
			try {
				input = userInput.nextInt();
			} catch (InputMismatchException ime) {
				printFeedback("Invalid input. Input an integer");
				userInput.next();
				continue;
			}

			validInput = (0 <= input) && (input <= lastChoice);
			if (!validInput) {
				printFeedback("Invalid input. Input on of the shown options");
			}
		}

		return input;
	}

	public static String getWrittenHintForPlayer() {
		int hint = getHintForPlayer();

		if (hint == 0) {
			return "Remove either. The only way to win is for your opponent to play imperfectly";
		} else if (hint == 1) {
			return "Remove one";
		} else {
			return "Remove two";
		}
	}

	public static int getHintForPlayer() {
		if (hintLevel == PlayerAI.PERFECT_STRATEGY) {
			return tree.getNextBestMove(playerIsFirst);
		} else if (hintLevel == PlayerAI.DECENT_STRATEGY) {
			int coinFlip = random.nextInt(2);
			if (coinFlip == 0) {
				return tree.getNextBestMove(playerIsFirst);
			} else {
				return getRandomHintForPlayer();
			}
		} else if (hintLevel == PlayerAI.RANDOM_STRATEGY) {
			return getRandomHintForPlayer();
		} else {
			return 1; //will never run
		}
	}

	public static int getRandomHintForPlayer() {
		int possibleMoves = tree.possibleMoves();

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

	public static void checkMoveValue(int moveValue) {
		//change whose turn it is
		currentTurnIsPlayers = !currentTurnIsPlayers;
		if (moveValue == SimpleNimGameTree.FIRST_PLAYER_WON) {
			playerWon = playerIsFirst;
			gameIsDone = true;
		} else if (moveValue == SimpleNimGameTree.SECOND_PLAYER_WON) {
			playerWon = !playerIsFirst;
			gameIsDone = true;
		} else if (moveValue == SimpleNimGameTree.ILLEGAL_MOVE) {
			printFeedback("Illegal move, use the hint optino to show possible moves");
			//change the turn back to the one who made an illegal move
			currentTurnIsPlayers = ! currentTurnIsPlayers;
		}
	}

	public static String gameState() {
		String turn = currentTurnIsPlayers ? "player" : "computer";
		return tree.remainingElements() + " elements left. It's " + turn + "'s turn";
	}

	public static String treeStatistics() {
		return "Tree height is " + tree.getTreeHeight() + " and has " + tree.getNumberOfNodes() + " nodes";
	}

	public static void exitGame() {
		printFeedback("Thanks for playing");
		System.exit(0);
	}

	public static void draw(SimpleNimGameTree tree) {
		GameTreeWindow window = new GameTreeWindow(tree);
	}

	private static void printFeedback(String feedback) {
		try {
			Thread.sleep(250);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		System.out.println("> " + feedback);

		try {
			Thread.sleep(250);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}
}
