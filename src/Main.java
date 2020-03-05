import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
	
	public static int gridSize = 5;
			
	public static void runTests() throws FileNotFoundException {
		
		//create a grid to run through the algorithm
		boolean[][] startGrid;// = new boolean[gridSize][gridSize];
		
		startGrid = readGridFromFile("data.txt");
		
		
		//print grid
		String str = "";
		for(int i = 0; i < startGrid.length; i++) {
			str += Arrays.toString(startGrid[i]) + "\n";
		}
		
		System.out.println(str);
		
		//start timer
		long startTime = System.currentTimeMillis();
		
		//run algorithm
		LightsOut.Node goal = LightsOut.solveGrid(startGrid);
		
		//stop timer
		long endTime = System.currentTimeMillis();
		long runtime = endTime - startTime;
		
		//print timer results
		System.out.println(runtime + " milliseconds");
		long seconds = runtime/1000;
		System.out.println(seconds + " seconds");
		System.out.println(goal);
	}
	
	public static boolean[][] makeRandomGrid(){
		boolean[][] startGrid = new boolean[gridSize][gridSize];
		Random rnd = new Random();
		
		//initialize startGrid
		for(int i = 0; i < gridSize; i++) {
			for(int j = 0; j < gridSize; j++) {
				int checkNum = rnd.nextInt() % 5;
				if(checkNum < 2)
					startGrid[i][j] = true;
				else
					startGrid[i][j] = false;
			}
		}
		
		return startGrid;
	}
	
	public static boolean[][] readGridFromFile(String filename) throws FileNotFoundException{
		File in = new File(filename);
		Scanner fin = new Scanner(in);
		
		//first line is size
		int size = Integer.parseInt(fin.nextLine());
		boolean[][] grid = new boolean[size][size];
		
		for(int i = 0; i < size; i++) {
			String line = fin.nextLine();
			String[] ary = line.split(" ");
			for(int j = 0; j < size; j++) {
				grid[i][j] = Integer.parseInt(ary[j]) == 1 ? true : false; 
			}
		}
		
		return grid;
	}
}
