package application;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

/**
 * Class which create the content of the application
 * 
 * 
 * @author Laure Roussel
 * 
 */

public class Graph{


	/**
	 * Method which create the interface of the application
	 * 
	 * @param primaryStage
	 * 				Stage
	 * 
	 */
	public void start(Stage primaryStage) {
		try {

			BorderPane root = new BorderPane();
			root.setCenter(createMainContent());
			Scene scene = new Scene(root,650,650);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Demo JavaFX");
			primaryStage.show();



		}catch(Exception e){e.printStackTrace();}
	}


	
	
	/**
	 * Method which create the main content of the application
	 * 
	 * @return g
	 * 			 the main content of the application including a chart and different buttons 
	 */
	private Node createMainContent(){
		Group g = new Group();

		// Number of data than are shown on the charts 
		final int WINDOW_SIZE = 10;

		//defining the axes
		final CategoryAxis xAxis = new CategoryAxis(); // we plot against time
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Time/s");
		xAxis.setStyle("-fx-font: normal 15px 'sansserif' ");
		xAxis.setAnimated(false); // axis animations are removed
		yAxis.setLabel("Value");
		yAxis.setStyle("-fx-font: normal 15px 'sansserif' ");
		yAxis.setAnimated(false); // axis animations are removed

		//creating the line chart with two axis created above
		final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
		lineChart.setTitle("Data received in realtime");
		lineChart.setAnimated(false); // disable animations
		lineChart.setStyle("-fx-font: Times New Roman bold 15px 'serif' ");

		//defining a series to display data
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName("Data Series");

		// add series to chart
		lineChart.getData().add(series);


		// this is used to display time in HH:mm:ss format
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

		// setup a scheduled executor to periodically put data into the chart
		ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();


		// create a button server to turn on the server
		Button serv = new Button("Server");

		serv.setOnAction(action->{

			// creation of the server
			Server server = new Server();
			server.start();



			// put dummy data onto graph per second
			scheduledExecutorService.scheduleAtFixedRate(() -> {

				// creation of a list which content are the data send by the client
				List<Double> data = new ArrayList<>();
				data.addAll(server.getValuesY());

				// verification that the list data isn't empty 
				if(data.size()!=0) {

					// transform the last element of data into an integer
					int idata = data.get(data.size()-1).intValue();

					// Update the chart
					Platform.runLater(() -> {
						// get current time
						Date now = new Date();

						// put random number with current time
						series.getData().add(new XYChart.Data<>(simpleDateFormat.format(now),idata));

						// if there is more than WINDOW_SIZE element on the chart the last element 
						// will be removed 
						if (series.getData().size() > WINDOW_SIZE)
							series.getData().remove(0);
					});

				}}, 0, 1, TimeUnit.SECONDS); // the Charts is update every seconds

		});


		// create a button client to send data in local
		Button cli = new Button("Client");
		
		cli.setOnAction(action->{

			// creation of the client
			final Client client = new Client();
			client.start();
		});
	
		
		// Adding text
		Text textserver = new Text(" Start the server : ");
		textserver.setFont(Font.font("Times New Roman" ,FontWeight.BOLD,FontPosture.REGULAR, 20));
		
		Text textclient = new Text(" Start to send 10 random data : ");
		textclient.setFont(Font.font("Times New Roman" ,FontWeight.BOLD,FontPosture.REGULAR, 20));
	
		Text text = new Text("Version: 1.0,\n Made by Laure Roussel with the remerciement of A.Auzmendi, T.Steinmetz, A.Bourgain-Wilbal, L.Driessens"); 
		text.setTextAlignment(TextAlignment.CENTER);
		
		
		//Styling buttons 
	    serv.setStyle("-fx-font: normal bold 15px 'sansserif' ;-fx-background-color: orangered; -fx-text-fill: white;"); 
	    cli.setStyle("-fx-font: normal bold 15px 'sasnserif' ;-fx-background-color: orangered; -fx-text-fill: white;"); 
		
	    
		// create a grid to organize the layout of the interface
		GridPane grid = new GridPane();
		grid.setGridLinesVisible(false);
		GridPane grid2 = new GridPane();
		grid2.setGridLinesVisible(false);
		
		
		grid.setPadding(new Insets(10, 10, 10, 10)); 
		grid2.setAlignment(Pos.CENTER);
		grid2.setPadding(new Insets(10, 10, 10, 10)); 
        
		
		grid2.add(textserver, 0, 0);
		grid2.add(serv, 1, 0);
		grid2.add(textclient, 0, 1);
		grid2.add(cli, 1, 1);

		
		grid.add(grid2, 0, 4);
		grid.add(lineChart, 0, 0);
		grid.add(text,0,10);
		
		grid.setHgap(10);
        grid.setVgap(10);
		grid2.setHgap(10);
        grid2.setVgap(10);
	                

		g.getChildren().add(grid);

		return g ;
	}


}

