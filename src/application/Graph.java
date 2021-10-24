package application;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;



/**
 * Class that sets and creates the user interface of the application
 * 
 * @author Laure Roussel
 */

public class Graph{


	/**
	 *  Method that sets up the application window
	 * 
	 * @param primaryStage 	Stage (or window) where the content of this class will be visualized
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
	 * @return g - Group containing the main content of the application, including a chart and different buttons 
	 */
	private Node createMainContent(){
		Group g = new Group();

		// Number of data shown on the charts 
		final int WINDOW_SIZE = 10;

		// Definition of the axes
		final CategoryAxis xAxis = new CategoryAxis(); // we plot against time
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Time/s");
		xAxis.setStyle("-fx-font: normal 15px 'sansserif' ");
		xAxis.setAnimated(false); // axis animations are removed
		yAxis.setLabel("Value");
		yAxis.setStyle("-fx-font: normal 15px 'sansserif' ");
		yAxis.setAnimated(false); // axis animations are removed

		// Creation of the line chart with the two axes created above
		final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
		lineChart.setTitle("Data received in realtime");
		lineChart.setAnimated(false); // disable animations
		lineChart.setStyle("-fx-font: Times New Roman bold 15px 'serif' ");

		// Definition of a series to display data
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName("Data Series");

		// Addition of the series to the chart
		lineChart.getData().add(series);


		// This is used to display time in HH:mm:ss format
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

		// Set up a scheduled executor to periodically put data into the chart
		ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();


		// Creation of a "Server" button to turn on the server on press
		Button serv = new Button("Server");

		serv.setOnAction(action->{

			// Creation of the server thread
			Server server = new Server();
			server.start();



			// Put dummy data onto graph per second
			scheduledExecutorService.scheduleAtFixedRate(() -> {

				// Creation of a list whose content are the data sent by the client
				List<Double> data = new ArrayList<>();
				data.addAll(server.getValuesY());

				// Verification that the "data" list isn't empty 
				if(data.size()!=0) {

					// Transform the last element of data into an integer
					int idata = data.get(data.size()-1).intValue();

					// Update the chart
					Platform.runLater(() -> {
						// Get current time
						Date now = new Date();

						// Put idata alongside the current time
						series.getData().add(new XYChart.Data<>(simpleDateFormat.format(now),idata));

						// If there is more than WINDOW_SIZE element on the chart, 
						// the last element will be removed 
						if (series.getData().size() > WINDOW_SIZE)
							series.getData().remove(0);
					});

				}}, 0, 1, TimeUnit.SECONDS); // the Charts is updated every seconds

		});


		// Create a "Client" button to send data locally when pressed on
		Button cli = new Button("Client");

		cli.setOnAction(action->{

			// Creation of the client thread
			final Client client = new Client();
			client.start();
		});


		// Adding text
		Text textserver = new Text(" Start the server : ");
		textserver.setFont(Font.font("Times New Roman" ,FontWeight.BOLD,FontPosture.REGULAR, 20));

		Text textclient = new Text(" Start to send 10 random data : ");
		textclient.setFont(Font.font("Times New Roman" ,FontWeight.BOLD,FontPosture.REGULAR, 20));

		Text text = new Text("Version: 1.0,\n Made by Laure Roussel with the remerciement of A.Auzmendi, T.Steinmetz, A.Bourgain-Wilbal and L.Driessens"); 
		text.setTextAlignment(TextAlignment.CENTER);


		// Stylizing buttons 
		serv.setStyle("-fx-font: normal bold 15px 'sansserif' ;-fx-background-color: orangered; -fx-text-fill: white;"); 
		cli.setStyle("-fx-font: normal bold 15px 'sasnserif' ;-fx-background-color: orangered; -fx-text-fill: white;"); 


		// Creating grids to organize the layout of the interface
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

