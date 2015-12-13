/**
 * Omar Khashoggi
 * 201369650
 */

package gametree;

/**
 * A tree representing a simple version of the game Nim. There is only one pile with any given number of elements.
 * Legal moves: Removing 1 or 2 elements.
 * Win condition: Remove the last element.
 *
 * @author Omar Khashoggi
 */

public class SimpleNimGameTree {
	GameNode startingState;
	GameNode currentState;

	public static final int FIRST_PLAYER_WON = 1;
	public static final int SECOND_PLAYER_WON = -1;
	public static final int GAME_ONGOING = 0; //might change how this works
	public static final int ILLEGAL_MOVE = -42;

	/**
	 * Creates a game tree for a game with the given number of elements and selects the starting state as the current state. The constructor of the node creates the rest of the tree nodes.
	 * 
	 * @param numberOfElements the number of elements when the game starts
	 */
	public SimpleNimGameTree(int numberOfElements) {
		startingState = new GameNode(numberOfElements);
		currentState = startingState;
		assignWinValuesByMinimax();
	}

	/**
	 * Creates a game tree with 10 elements. Uses {@link #SimpleNimGameTree(int)}
	 */
	public SimpleNimGameTree() {
		this(10);
	}

	/**
	 * Moves to the game state where one element is removed from the current game state.
	 *
	 * @return GAME_ONGOING, FIRST_PLAYER_WON, or SECOND_PLAYER_WON
	 */
	public int removeOne() {
		currentState = currentState.removeOne;
		if (currentState.numberOfElements == 0) {
			return currentState.winValue;
		} else {
			return SimpleNimGameTree.GAME_ONGOING;
		}
	}

	/**
	 * Moves to the game state where two elements are removed from the current game state.
	 *
	 * @return GAME_ONGOING, FIRST_PLAYER_WON, SECOND_PLAYER_WON, or ILLEGAL_MOVE 
	 */
	public int removeTwo() {
		if (currentState.numberOfElements == 1) {
			return SimpleNimGameTree.ILLEGAL_MOVE;
		} else {
			currentState = currentState.removeTwo;
			if (currentState.numberOfElements == 0) {
				return currentState.winValue;
			} else {
				return SimpleNimGameTree.GAME_ONGOING;
			}
		}
	}

	public int remainingElements() {
		return currentState.numberOfElements;
	}

	public int possibleMoves() {
		if (currentState.removeOne == null) {
			return 2;
		} else if (currentState.removeTwo == null) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 
	 * @param firstPlayer to know the player the move should be in favor of
	 * @return 0 if both moves have equal values, 1 if removing one is better, 2 if removeing 2 is better
	 */
	public int getNextBestMove(boolean firstPlayer) {
		if (currentState.removeOne == null) {
			return 2;
		} else if (currentState.removeTwo == null) {
			return 1;
		}

		int removeOneValue = currentState.removeOne.winValue;
		int removeTwoValue = currentState.removeTwo.winValue;

		if (removeOneValue == removeTwoValue) {
			return 0;
		}

		if (firstPlayer) {
			return (removeOneValue > removeTwoValue) ? 1 : 2;
		} else {
			return (removeOneValue < removeTwoValue) ? 1 : 2;
		}
	}

	public void assignWinValuesByMinimax() {
		//most positive values are good for player one
		//most negative values are good for player two
		minimax(startingState.removeOne);
		minimax(startingState.removeTwo);
	}

	private int minimax(GameNode node) {
		if (node == null) {
			return 0;
		} else if (node.numberOfElements == 0) {
			return node.winValue;
		} else {
			if (node.currentState == GameNode.PLAYER_ONE) { //.currentState might be confusing
				node.winValue = Math.min(minimax(node.removeOne), minimax(node.removeTwo));
				return node.winValue;
			} else if (node.currentState == GameNode.PLAYER_TWO) {
				node.winValue = Math.max(minimax(node.removeOne), minimax(node.removeTwo));
				return node.winValue;
			} else {
				return 0;
			}
		}
	}

	/**
	 * Finds the height of the tree by passing the starting node to {@link #getMaxHeight(GameNode)}.
	 * 
	 * @return the height of the deepest node which is the height of the tree
	 */
	public int getTreeHeight() {
		return getSubtreeHeight(startingState);
	}

	/**
	 * Recursively goes through the subtree with the passed node as its root to find its height.
	 *
	 * @param currentNode the root of the subtree
	 * @return the height of the subtree
	 */
	private int getSubtreeHeight(GameNode currentNode) {
		if (currentNode == null) {
			return 0;
		} else {
			return 1 + Math.max(getSubtreeHeight(currentNode.removeOne), getSubtreeHeight(currentNode.removeTwo));
		}
	}

	public int getNumberOfNodes() {
		return getNumberOfNodes(startingState); //could store while generating tree, also height
	}

	private int getNumberOfNodes(GameNode node) {
		if (node == null) {
			return 0;
		} else {
			return 1 + getNumberOfNodes(node.removeOne) + getNumberOfNodes(node.removeTwo);
		}
	}

	/**
	 * Finds the level of the given node by looking for it in the tree with a recursive {@link #findLevelHelper(GameNode, GameNode, int) findLevelHelper}. The root is at level 1.
	 *
	 * @param node the node to find the level of
	 * @return the level where the node is, or 0 if node is not found
	 */
	private int findLevelOf(GameNode node) {
		return findLevelHelper(node, startingState, 1);
	}

	/**
	 * A recursive helper method to {@link findLevelOf(GameNode)}. Looks through the tree for the node.
	 *
	 * @param node the node to find the level of
	 * @param currentNode the current node in the recursion
	 * @return the level of node, or 0 if node is not found
	 */
	private int findLevelHelper(GameNode node, GameNode currentNode, int level) {
		if (currentNode == null) {
			return 0;
		} else if (node == currentNode) {
			return level;
		} else {
			return Math.max(findLevelHelper(node, currentNode.removeOne, level+1), findLevelHelper(node, currentNode.removeTwo, level+1));
		}
	}

	private int numberOfNodesInLevel(int level) {
		return (int)Math.pow(2, level-1);
	}
}
