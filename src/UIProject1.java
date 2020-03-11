import java.io.FileNotFoundException;
import java.util.Arrays;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class UIProject1 extends Application{
	public static int gridSize = 5;
	public static CheckBox[][] cboxArray;
	public static Label calcPopup;
	
	@Override
	public void start(Stage primaryStage) throws FileNotFoundException, Exception  {
		
		GridPane root = new GridPane();
		Scene scene = new Scene(root, 240, 110);
		cboxArray = new CheckBox[gridSize][gridSize];
		boolean[][] testGrid = Main.readGridFromFile("data.txt");
		
		//create and initialize check boxes based on starting array
		for(int i = 0; i < gridSize; i++) {
			for(int j = 0; j < gridSize; j++) {
				cboxArray[i][j] = new CheckBox();
				cboxArray[i][j].setSelected(testGrid[i][j]);
				GridPane.setRowIndex(cboxArray[i][j],i);
                GridPane.setColumnIndex(cboxArray[i][j],j);    
				root.getChildren().add(cboxArray[i][j]);
			}
		}
		
		//create and wire the run button
		Button btn = new Button("Start Algorithm");
	    btn.setOnAction((e) -> {
	    	Runnable task = new Runnable() {
	    		public void run() {
	    			buttonClick(readFromCheckBoxes());
	    			showText(false);
	    		}
	    	};
	    	showText(true);
			Thread bgthread = new Thread(task);
			bgthread.setDaemon(true);
			bgthread.start();
		});
		//btn.setOnAction((e) -> updateCheckBoxes(invertedTest));
	    GridPane.setRowIndex(btn,gridSize/2);
        GridPane.setColumnIndex(btn,gridSize+1); 
	    root.getChildren().add(btn);
	    
	    calcPopup = new Label("Calculating...");
	    
	    GridPane.setRowIndex(calcPopup, (gridSize/2) + 1);
        GridPane.setColumnIndex(calcPopup, gridSize+1); 
        
	    root.getChildren().add(calcPopup);
	    calcPopup.setVisible(false);
	    
	    //set the scene and run the app
	    primaryStage.setTitle("Lights Out!");
	    primaryStage.setScene(scene);
	    primaryStage.show();
		
	}
	public static void main(String[] args) {
	    launch(args);
	}
	public static void buttonClick(boolean[][] testGrid) {
		try {
			LightsOut.Node goal = Main.runTests(gridSize, testGrid);
			for(LightsOut.Action button : goal.pressed) {
				//for each action, print out a board and wait
				Integer[] uAction = button.getAction();
				//For each surrounding cell, if in range, toggle it
				for(int i = uAction[0] -1; i <= uAction[0]+1; i++) {
					for(int j = uAction[1] -1; j <= uAction[1]+1; j++) {
						if( (i >= 0 && i < testGrid.length) && (j >= 0 && j < testGrid.length)) {
							testGrid[i][j] = !testGrid[i][j];
						}
					}	
				}
				System.out.println(Arrays.toString(testGrid));
				UIProject1.updateCheckBoxes(testGrid);
				//wait two seconds
				Thread.sleep(2000);
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void showText(Boolean bool) {
		calcPopup.setVisible(bool);
	}
	
	//Call this to (hopefully) update the application window with new vals
	public static void updateCheckBoxes(boolean[][] newArray) throws InterruptedException {
		for(int i = 0; i < gridSize; i++) {
			for(int j = 0; j < gridSize; j++) {
				cboxArray[i][j].setSelected(newArray[i][j]);
			}
		}
		//wait two seconds
		Thread.sleep(2000);
	}
	
	public static boolean[][] readFromCheckBoxes() {
		boolean[][] newArray = new boolean[gridSize][gridSize];
		for(int i = 0; i < gridSize; i++) {
			for(int j = 0; j < gridSize; j++) {
				newArray[i][j] = cboxArray[i][j].isSelected();
			}
		}
		return newArray;
	}
}
