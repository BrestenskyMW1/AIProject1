import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class UIProject1 extends Application{
	public static int gridSize = 5;
	public static CheckBox[][] cboxArray;
	public static TextField calcPopup;
	
	@Override
	public void start(Stage primaryStage) throws FileNotFoundException, Exception  {
		
		GridPane root = new GridPane();
		Scene scene = new Scene(root, 300, 300);
		cboxArray = new CheckBox[gridSize][gridSize];
		boolean[][] testGrid = Main.readGridFromFile("data.txt");
		/*boolean[][] invertedTest = new boolean[gridSize][gridSize];
		for(int i = 0; i < gridSize; i++) {
			for(int j = 0; j < gridSize; j++) {
				invertedTest[i][j] = !testGrid[i][j];
			}
		}*/
		
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
			try {
				Main.runTests(gridSize, testGrid);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		});
		//btn.setOnAction((e) -> updateCheckBoxes(invertedTest));
	    GridPane.setRowIndex(btn,gridSize/2);
        GridPane.setColumnIndex(btn,gridSize+1); 
	    root.getChildren().add(btn);
	    
	    calcPopup = new TextField("Calculating...");
	    calcPopup.setVisible(false);
	    GridPane.setRowIndex(calcPopup, (gridSize/2) + 1);
        GridPane.setColumnIndex(calcPopup, gridSize+1); 
	    root.getChildren().add(calcPopup);
	    
	    //set the scene and run the app
	    primaryStage.setTitle("Lights Out!");
	    primaryStage.setScene(scene);
	    primaryStage.show();
		
	}
	public static void main(String[] args) {
	    launch(args);
	}
	
	public static void showText(Boolean bool) {
		calcPopup.setVisible(bool);
	}
	
	//Call this to (hopefully) update the application window with new vals
	public static void updateCheckBoxes(boolean[][] newArray) throws InterruptedException {
		for(int i = 0; i < gridSize; i++) {
			for(int j = 0; j < gridSize; j++) {
				cboxArray[i][j].setSelected(newArray[i][j]);
				GridPane.setRowIndex(cboxArray[i][j],i);
                GridPane.setColumnIndex(cboxArray[i][j],j);    

			}
		}
		//wait two seconds
		Thread.sleep(2000);
	}
}
