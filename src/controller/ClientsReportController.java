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
 * This class handles the Clients Reports - gets the relevant data from the relevant table in the data base and displays it for the user.
 *
 */
public class ClientsReportController {
    @FXML
    private Button Exitbtn;

    @FXML
    private Button Backbtn;

    @FXML
    private BarChart<?, ?> Histogram;
    
    private String costumerID = ChatClient.user.getId();
    private String month, year, facility, chart;
    
    /**
     * This method gets the relevant data from the data base and displays the report accordingly.
     * The data from the data base includes: the month, year, and facility of the report (for the title of the report), 
     * 										 as well as data for the histogram.
     * The report shows how many clients made 0-5 orders in a given month, 6-10 orders, and so on. 
     */
    public void initialize() {
		String[] result = ChatClient.answerMsg.split("\\.");
		month = result[0]; 
		year = result[1]; 
		facility = result[2];
		chart = result[3];
    	
    	String[] data = chart.split("\\,");

    	Histogram.setTitle("Clients Activity in " + facility + " in " + month + " " + year);
    	
    	XYChart.Series orders = new XYChart.Series();
    	orders.setName("Orders");
    	
    	orders.getData().add(new XYChart.Data("0-5 orders",Integer.parseInt(data[0])));
    	orders.getData().add(new XYChart.Data("6-10 orders",Integer.parseInt(data[1])));
    	orders.getData().add(new XYChart.Data("11-15 orders",Integer.parseInt(data[2])));
    	orders.getData().add(new XYChart.Data("16-20 orders",Integer.parseInt(data[3])));
    	orders.getData().add(new XYChart.Data("21-25 orders",Integer.parseInt(data[4])));
    	orders.getData().add(new XYChart.Data("26-30 orders",Integer.parseInt(data[5])));
    	orders.getData().add(new XYChart.Data("31+ orders",Integer.parseInt(data[6])));
    	
    	Histogram.getData().addAll(orders); 	
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
