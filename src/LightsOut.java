/**
 * Heuristic: lights still on
 * F: number of lights toggled (4-9)
 * 
 * 	Admissible?: the heuristic will represent the number of lights on, and even perfect turn-off presses will require
 * 				toggles=lights, possibly more. The heuristic is admissible.
 * Consistent?: a consistent heuristic obeys the triangle inequality. In this implementation, a node's heuristic is the number of
 * 				lights on. All switches will decrease the lights on by up to the cost of the action, or less. Any less
 * 				are additional turned-on lights. This means that an action can never decrease the heuristic by an
 * 				unequal amount, obeying the triangle inequality. The heuristic is consistent.
 * 
 * Additionally: no node need be pressed more than once, by the nature of the lights-out problem. This allows us to restrict
 * 				the number of presses in the search tree by recording the press.
 * 
 */

import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.Arrays;

public class LightsOut {
	
	public static class Action{
		private Integer[] action; // underlying action
		
		public Action(Integer[] action) {
			this.action = new Integer[] {action[0], action[1]};
		}
		
		/**
		 * Gets the underlying action array
		 * @return The 2-element action array
		 */
		public Integer[] getAction() {
			return this.action;
		}
		
		/**
		 * Hashcode Override
		 */
		@Override
		public int hashCode() {
			return 7 + 23 * this.action[0].hashCode() + 31 * this.action[1].hashCode();
		}
		
		@Override
		public boolean equals(Object other) {
			if(!(other instanceof Action)) return false;
			Action a = (Action) other;
			return a.action[0] == this.action[0] && a.action[1] == this.action[1]; 
		}
		
		@Override
		public String toString() {
			return String.format("[%d,%d]", action[0], action[1]);
		}
	}
	
	
	
	static class Node implements Comparable{
		private boolean[][] grid; //the current grid state
		HashSet<Action> pressed; //the previously pressed lights
		public boolean isSolution; //is this a solution state
		private int costSoFar; //the cost so far
		public int h; //heuristic
		public int f; //combined cost
		public int g; //cost so far
		
		public Node(boolean[][] grid, Action action, HashSet<Action> previouslyPressed, int costSoFar) {
			this.grid = grid;
			this.pressed = new HashSet<Action>();
			this.pressed.addAll(previouslyPressed); //need not be deep copy, not going to edit anything already in it
			
			//if we have an action, calculate all fields
			if(action != null) {
				//add this state's action
				this.pressed.add(action);
				//press the button
				this.pressButton(action, grid);
				//take action
				isSolution = isSolutionState();
				//unpress it
				this.pressButton(action, grid);
			} else {
				isSolution = isSolutionState();
			}
			
			this.costSoFar = costSoFar;
			this.h = h();
			this.g = g(action);
			this.f = this.g + this.h;
		}
		
		/**
		 * Presses the given action button in the board
		 * @param action The action to take on the board
		 * @param inputGrid the grid to manipulate, manipulates in-place
		 */
		private void pressButton(Action action, boolean[][] inputGrid) {
			Integer[] uAction = action.getAction();
			//For each surrounding cell, if in range, toggle it
			for(int i = uAction[0] -1; i <= uAction[0]+1; i++) {
				for(int j = uAction[1] -1; j <= uAction[1]+1; j++) {
					if( (i >= 0 && i < inputGrid.length) && (j >= 0 && j < inputGrid.length)) {
						inputGrid[i][j] = !inputGrid[i][j];
					}
				}	
			}
		}
		
		/**
		 * Takes the current grid and checks for a solution state
		 * @return if the board is a solution
		 */
		private boolean isSolutionState() {
			for(int i = 0; i < grid.length; i++) {
				for(int j = 0; j < grid.length; j++) {
					if(grid[i][j]) {
						return false;
					}
				}
			}
			return true;
			
		}
		
		/**
		 * A heuristic calculator for the board state
		 * @return The grid heuristic for the number of lights turned on
		 */
		private int h() {
			int h = 0;
			for(int i = 0; i < grid.length; i++) {
				for(int j = 0; j < grid.length; j++) {
					if(grid[i][j]) h++;
				}
			}
			return h;
		}
		
		/**
		 * Gets the action cost for a node
		 * @param action The action taken
		 * @return the cost of the action taken
		 */
		private int c(Action action) {
			if(action == null) return 0;
			boolean lr = false; //is on sides
			boolean tb = false; //is top/bottom
			Integer[] uAction = action.getAction();
			//check edge cases, else in center
			lr = uAction[0] == 0 || uAction[0] == grid.length -1;
			tb = uAction[1] == 0 || uAction[1] == grid.length -1;
			
			if(lr && tb) return 4;
			else if (lr || tb) return 6;
			else return 9;
		}
		
		/**
		 * Calculates the g value in the A* algorithm
		 * @param action Action to be taken
		 * @return the g value of the given node
		 */
		private int g(Action action) {
			return costSoFar + c(action);
		}
		
		/**
		 * Prints string representation of the object
		 */
		public String toString() {
			String ret = "Buttons to Press: ";
			for(Action button : pressed) {
				ret += button;
			}
			//stats
			ret += String.format("\tg = %d\th = %d\tf = %d", g, h, f);
			
			return ret;
		}
		
		/**
		 * Runs the pressed buttons on the input grid
		 * @param grid The input grid to press buttons on
		 * @return the resulting solution grid
		 */
		public boolean[][] runSteps(boolean[][] grid){
			boolean[][] localGrid = new boolean[grid.length][grid.length];
			
			//make copy
			for(int i = 0; i < grid.length; i++) {
				localGrid[i] = Arrays.copyOf(grid[i], grid.length);
			}
			
			//press all buttons
			for(Action button : pressed) {
				this.pressButton(button, localGrid);
			}
			
			return localGrid;
		}
		
		/**
		 * Determines if the action is already in the set
		 * @param action The action to check in the set
		 * @return whether the action is in the set or not
		 */
		public boolean hasActionInSet(Action action) {
			return pressed.contains(action);
		}
		
		/**
		 * 
		 */
		@Override
		public int hashCode() {
			int hash = 7;
			hash += 31 * pressed.hashCode();
			return hash;
		}
		
		@Override
		public boolean equals(Object other) {
			if(!(other instanceof Node)) return false;
			Node n = (Node) other;
			return this.pressed.equals(n.pressed);
		}

		@Override
		public int compareTo(Object other) {
			if(!(other instanceof Node)) return 0;
			Node n = (Node) other;
			return f - n.f;
		}
	}
	
	/**
	 * Implements A* search the Lights-Out problem
	 * The 2D state matrix is assumed to be 0-indexed
	 * A solution is a matrix in which all entries are off
	 * @param start The 2D start matrix for the problem
	 */
	public static Node solveGrid(boolean[][] start){
		//initialize fronteir
		PriorityQueue<Node> frontier = new PriorityQueue<Node>(); //by default, front of queue is minimum
		frontier.add(new Node(start, null, new HashSet<Action>(), 0));
		//initialize explored set
		HashSet<Node> explored = new HashSet<Node>();
		//is found
		boolean found = false;
		Node goal = null;
		
		//main algorithm
		do {
			//remove a leaf node, done by priority queue
			Node leaf = frontier.remove();
			
			//print it
//			System.out.println("LEAF POPPED: " + leaf.toString());
			
			//if goal, end
			if(leaf.isSolution) {
				found = true;
				goal = leaf;
				break;
			}
			
			//not goal, add to explored
			explored.add(leaf);
			
			//expand resulting nodes and add them to the frontier if not already explored
			//Rules: press a button if not yet pressed by parent, generate parent's grid ahead of time
			boolean[][] parentGrid = leaf.runSteps(start);
			for(int i = 0; i < start.length; i++) {
				for(int j = 0; j < start.length; j++) {
					//if not pressed by parent, add new child
					Action action = new Action(new Integer[] {i,j});
					if(!leaf.hasActionInSet(action)) {
						Node child = new Node(parentGrid, action, leaf.pressed, leaf.g);
						
						//now add globally iff not explored, by definition won't be better/worse
						if(!explored.contains(child)) {
							frontier.add(child);
						}
					}
				}
			}
			
			//Print queue
//			System.out.println("--------\nFRONTIER:\n" + frontier.toString() + "\n");
			
			//stop looping if frontier is empty
		} while (!frontier.isEmpty());
		
		
		//Print end salute, found or not
		if(!found) { 
			System.out.println("No path was found.");
			return null;
		}
		else { 
			System.out.println("A path was found.");
			return goal;
		}
	}
}