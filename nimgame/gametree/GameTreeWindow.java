/*
 * Omar Khashoggi
 * 201369650
 */

package gametree;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.*;
import javax.swing.ImageIcon;
import java.awt.Component;

public class GameTreeWindow extends JFrame {
	JTree jGameTree;

	public GameTreeWindow(SimpleNimGameTree gameTree) {
		super("Single Row Nim Game Tree");
		setSize(500, 500);

		JScrollPane scroll = new JScrollPane(new JGameTree(gameTree));
		this.add(scroll);
		
		setVisible(true);
	}

	class JGameTree extends JTree {
		SimpleNimGameTree gameTree;

		public JGameTree(SimpleNimGameTree gameTree) {
			super(new JGameNode(gameTree.startingState));
			setCellRenderer(new GameTreeCellRenderer());
		}
	}

	class JGameNode extends DefaultMutableTreeNode {
		public JGameNode(GameNode node) {
			super(node);

			if (node.removeOne != null) {
				this.add(new JGameNode(node.removeOne));
			}
			if (node.removeTwo != null) {
				this.add(new JGameNode(node.removeTwo));
			}
		}
	}

	class GameTreeCellRenderer extends DefaultTreeCellRenderer {
		private ImageIcon player1Icon = new ImageIcon("images/player1.jpg");
		private ImageIcon player2Icon = new ImageIcon("images/player2.jpg");
		private ImageIcon player1endIcon = new ImageIcon("images/player1end.jpg");
		private ImageIcon player2endIcon = new ImageIcon("images/player2end.jpg");
		private ImageIcon beginIcon = new ImageIcon("images/being.jpg");;

		public GameTreeCellRenderer() {
		}

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
		boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {
			JGameNode jNode = (JGameNode)value;
			GameNode node = (GameNode)jNode.getUserObject();

			if (node.currentState == GameNode.PLAYER_ONE) {
				if (leaf) {
					setLeafIcon(player1endIcon);
				} else {
					setOpenIcon(player1Icon);
					setClosedIcon(player1Icon);
				}
			} else if (node.currentState == GameNode.PLAYER_TWO) {
				if (leaf) {
					setLeafIcon(player2endIcon);
				} else {
					setOpenIcon(player2Icon);
					setClosedIcon(player2Icon);
				}
			} else {
				setOpenIcon(beginIcon);
				setClosedIcon(beginIcon);
			}

			super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row, hasFocus);
			return this;
		}
	}
}
