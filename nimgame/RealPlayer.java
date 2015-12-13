/*
 * Omar Khashoggi
 * 201369650
 */

import gametree.*;

import java.util.Scanner;
import java.util.InputMismatchException;

public class RealPlayer extends Player {
	private Scanner userInput;

	public RealPlayer(SimpleNimGameTree gameTree, int strategy, boolean firstPlayer, Scanner userInput) {
		super(gameTree, strategy, firstPlayer);

		this.userInput = userInput;
	}

	public int getUserInputInGame() {
		int lastChoice = TestGameTree.printInGameChoices();
		
		int input = 0;
		boolean validInput = false;
		while (!validInput) {
			System.out.print("Choose by inputting a number: ");
			try {
				input = userInput.nextInt();
			} catch (InputMismatchException ime) {
				TestGameTree.printFeedback("Invalid input. Input an integer");
				userInput.next();
				continue;
			}

			validInput = (1 <= input) && (input <= lastChoice);
			if (!validInput) {
				TestGameTree.printFeedback("Invalid input. Input on of the shown options");
			}
		}

		return input;
	}

	public String getWrittenHint() {
		int hint = getNextMove();

		if (hint == 0) {
			return "Remove either. The only way to win is for your opponent to play imperfectly";
		} else if (hint == 1) {
			return "Remove one";
		} else {
			return "Remove two";
		}
	}
}
