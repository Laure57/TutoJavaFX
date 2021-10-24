package application;

import javafx.application.Application;
import javafx.stage.Stage;



// Command line to start the application
// java --module-path ..\..\javafx-sdk-17.0.0.1\lib --add-modules javafx.controls application.Main


/**
 * Class which run the application 
 * 
 * @author Laure Roussel
 */

public class Main extends Application{

	
	/**
	 * Method which overrides the start() method from Application
	 * 
	 * @param stage  Stage (or window) where the application will be visualized
	 */
	
	public void start(Stage stage){
		try {

			// Creation of the Graph 
			Graph graph = new Graph();
			graph.start(stage);


		}catch(Exception e){
			e.printStackTrace();
		}
	}


}
