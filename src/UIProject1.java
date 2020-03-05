import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class UIProject1 extends Application{
	public static int gridSize = 5;
	
	@Override
	public void start(Stage primaryStage) throws FileNotFoundException, Exception  {
		
		StackPane root = new StackPane();
		Scene scene = new Scene(root, 300, 300);
		CheckBox[][] cboxArray = new CheckBox[gridSize][gridSize];
		boolean[][] testGrid = Main.readGridFromFile("data.txt");
		
		for(int i = 0; i < gridSize; i++) {
			for(int j = 0; j < gridSize; j++) {
				cboxArray[i][j] = new CheckBox();
				cboxArray[i][j].setSelected(testGrid[i][j]);
				//root.getChildren().add(cboxArray[i][j]); //not appearing in window
			}
		}
		
		Button btn = new Button("Start Algorithm");
	    btn.setOnAction((e) -> Main.runTests(gridSize, testGrid));
	    root.getChildren().add(btn);
	    
	    primaryStage.setTitle("Lights Out!");
	    primaryStage.setScene(scene);
	    primaryStage.show();
		
	}
	public static void main(String[] args) {
	    launch(args);
	}
}
