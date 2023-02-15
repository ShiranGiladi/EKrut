package controller;

import java.io.IOException;
import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class handles the Deliveries Reports - gets the relevant data from the relevant table in the data base and displays it for the user.
 *
 */
public class DeliveriesReportController {
    @FXML
    private Button Backbtn;

    @FXML
    private Button Exitbtn;

    @FXML
    private BarChart<?, ?> barChart;

    private String costumerID = ChatClient.user.getId();
    private String month, year, chart;
    
    /**
     * This method gets the relevant data from the data base and displays the report accordingly.
     * The data from the data base includes: the month and year of the report (for the title of the report), 
     * 										 as well as data for the bar chart.
     * The report shows how many deliveries were made in each area in a given month. 
     */
    public void initialize() {
    	String[] result = ChatClient.answerMsg.split("\\.");
		month = result[0]; 
		year = result[1]; 
		chart = result[2];
		
		String[] data = chart.split("\\,");
		
		barChart.setTitle("Deliveries in " + month + " " + year);
    	
		XYChart.Series delivery = new XYChart.Series();
		delivery.setName("Deliveries");
		
		delivery.getData().add(new XYChart.Data("North", Integer.parseInt(data[1])));
		delivery.getData().add(new XYChart.Data("South", Integer.parseInt(data[3])));
		delivery.getData().add(new XYChart.Data("UAE", Integer.parseInt(data[5])));
		
		barChart.getData().addAll(delivery);
    }
    /**
     * This method closes the current window ("Clients Report") and opens the window "Show Reports" 
     * (from which the user can choose to watch a different report). 
     * @param event
     * @throws IOException
     */
    @FXML
    void getBackbtn(ActionEvent event) throws IOException {
    	((Node)event.getSource()).getScene().getWindow().hide();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/ShowReports.fxml"));
    	Parent root = (Parent) loader.load();
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Show Reports");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);		
		primaryStage.show();
    }
    /**
     * This method closes entirely the client side of the system and logs out the user that was logged in.
     * @param event
     */
    @FXML
    void getExitbtn(ActionEvent event) {
    	ClientUI.chat.accept("logout " + costumerID);
		((Node) event.getSource()).getScene().getWindow().hide();
    }

}
