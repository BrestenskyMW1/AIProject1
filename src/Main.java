import java.util.Random;

public class Main {
	
	public static int gridSize = 5;
			
	public static void main(String[]args) {
		
		//create a grid to run through the algorithm
		boolean[][] startGrid = new boolean[gridSize][gridSize];
		Random rnd = new Random();
		
		//initialize startGrid
		for(int i = 0; i < gridSize; i++) {
			for(int j = 0; j < gridSize; j++) {
				int checkNum = rnd.nextInt() % 5;
				if(checkNum < 2)
					startGrid[i][j] = true;
				else
					startGrid[i][j] = true;
			}
		}
		
		//start timer
		long startTime = System.currentTimeMillis();
		
		//run algorithm
		LightsOut.solveGrid(startGrid);
		
		//stop timer
		long endTime = System.currentTimeMillis();
		long runtime = endTime - startTime;
		
		//print timer results
		System.out.println(runtime + " milliseconds");
		long seconds = runtime/1000;
		System.out.println(seconds + " seconds");
	}
}
