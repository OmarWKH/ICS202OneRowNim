/*
 * Omar Khashoggi
 * 201369650
 */

package gametree;

/**
 * This is a node in SimpleNimGameTree, a tree represnting a simple version of Nim. The node represents the game state and contains the number of elements, as well as a references to the nodes representing the two following states: removing 1 or removing 2 elements.
 *
 * @author: Omar Khashoggi
 */
class GameNode {
	final int numberOfElements;
	final GameNode removeOne;
	final GameNode removeTwo;
	final int currentState; //takes static values in GameNode
	int winValue; //takes static values in SimpleNimGameTree

	//being = game just began (for root)
	//player_one = player one just played and caused the current number of elements, so if elememnts = 0, then player one made it so and he/she won
	//player_two = same as player one but for two
	static final int BEGIN = 10;
	static final int PLAYER_ONE = 1;
	static final int PLAYER_TWO = -1;
	//private static final int END = -10;

	/**
	 * Creates a node with the given number of elements and creates its children (following states if one or two elements are removed). Stops creating children if their value will be less 0.
	 *
	 * @param numberOfElements the number of elements during the game state the node represents
	 */
	GameNode(int numberOfElements) {
		this(numberOfElements, 1);
	}

	GameNode(int numberOfElements, int level) {
		this.numberOfElements = numberOfElements;
		
		boolean levelIsEven = (level % 2 == 0);

		if (level == 1) {
			this.currentState = GameNode.BEGIN;
			this.winValue = SimpleNimGameTree.GAME_ONGOING;
		} else {
			this.currentState = levelIsEven ? GameNode.PLAYER_ONE : GameNode.PLAYER_TWO;
			if (numberOfElements == 0) {
				this.winValue = (this.currentState == GameNode.PLAYER_ONE) ? SimpleNimGameTree.FIRST_PLAYER_WON : SimpleNimGameTree.SECOND_PLAYER_WON;
			} else {
				this.winValue = SimpleNimGameTree.GAME_ONGOING;
			}
		}

		if (numberOfElements-1 >= 0) {
			removeOne = new GameNode(numberOfElements-1, level+1);
		} else {
			removeOne = null;
		}

		if (numberOfElements-2 >= 0) {
			removeTwo = new GameNode(numberOfElements-2, level+1);
		} else {
			removeTwo = null;
		}
	}

	/**
	 * A node is a leaf if both it's children are null
	 *
	 * @return whether node is a leaf or not
	 */
	boolean isLeaf() {
		return (removeOne == null) && (removeTwo == null);
	}

	public String toString() {
		return numberOfElements + " (" + winValue + ")";
	}
}
