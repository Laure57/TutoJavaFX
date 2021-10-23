package application;

import javafx.application.Application;
import javafx.stage.Stage;

// java --module-path ..\..\javafx-sdk-17.0.0.1\lib --add-modules javafx.controls application.Main


/**
 * Class which run the application 
 * 
 * 
 * @author Laure Roussel
 * 
 */
public class Main extends Application{
	
	/**
     * Method which override the method start() in Application
     * @param stage
     * 				create the application
     */
	public void start(Stage stage){
		try {
			
			
			Graph graph = new Graph();
			graph.start(stage);

			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

}
