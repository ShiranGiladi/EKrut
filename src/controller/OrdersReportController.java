package controller;

import java.io.IOException;
import client.ChatClient;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class handles the Orders Reports - gets the relevant data from the relevant table in the data base and displays it for the user.
 *
 */
public class OrdersReportController {

    @FXML
    private Button Exitbtn;

    @FXML
    private Button Backbtn;

    @FXML
    private BarChart<?, ?> barChart;
    
    @FXML
    private Text txtArea;

    @FXML
    private Text txtValue;
    
    @FXML
    private Label txtTitle;
    
    @FXML
    private PieChart ClientsPie;
    
    private String costumerID = ChatClient.user.getId();
    private String month, year, barChartInfo, pieReg, pieSub, area, popular, productPerArea;

    /**
     * This method gets the relevant data from the data base and displays the report accordingly.
     * The data from the data base includes: the month and year of the report (for the title of the report), 
     * 										 as well as data for the bar chart, and the pie chart.
     * The bar chart shows: - how many "on site" orders have been made in a given month in every facility of a certain area.
     * 						- how many "pick up later" orders have been made in a given month in every facility of a certain area.
     * The pie chart shows the distribution of regular and subscribed clients over the different facilities in a certain area.
     */
    public void initialize() {
    	String[] result = ChatClient.answerMsg.split("\\.");
		month = result[0]; 
		year = result[1]; 
		barChartInfo = result[2];
		pieReg = result[3];
		pieSub = result[4];
		area = result[5];
		popular = result[6];
		
		String [] bar = barChartInfo.split("\\,");
		String [] pieR = pieReg.split("\\,");
		String [] pieS = pieSub.split("\\,");
		String [] popularProduct = popular.split("\\,");
		
		switch(area) {
    	case "North":
    		productPerArea = popularProduct[0];
    		break;
    	case "South":
    		productPerArea = popularProduct[1];
    		break;
    	case "UAE":
    		productPerArea = popularProduct[2];
    		break;
		}
		txtArea.setText("The most popular product in the " + area + " area is:");
		productPerArea = productPerArea.replace("_", " ");
		txtValue.setText(productPerArea);
    	
		//Create the bar chart.
    	barChart.setTitle("Number of Orders in different facilitis in " + month + " " + year);
    	
    	XYChart.Series pickUpLater = new XYChart.Series();
    	XYChart.Series onSite = new XYChart.Series();
    	
    	pickUpLater.setName("Pick Up Later");
    	onSite.setName("On Site");
    	
    	switch(area) {
    	case "North":
    		pickUpLater.getData().add(new XYChart.Data("Haifa",Integer.parseInt(bar[1])));
        	pickUpLater.getData().add(new XYChart.Data("Karmiel",Integer.parseInt(bar[5])));
        	onSite.getData().add(new XYChart.Data("Haifa",Integer.parseInt(bar[3])));
        	onSite.getData().add(new XYChart.Data("Karmiel",Integer.parseInt(bar[7])));
        	break;
    	case "South":
    		pickUpLater.getData().add(new XYChart.Data("Eilat",Integer.parseInt(bar[9])));
        	pickUpLater.getData().add(new XYChart.Data("Be'er Sheva",Integer.parseInt(bar[13])));
        	onSite.getData().add(new XYChart.Data("Eilat",Integer.parseInt(bar[11])));
        	onSite.getData().add(new XYChart.Data("Be'er Sheva",Integer.parseInt(bar[15])));
        	break;
    	case "UAE":
    		pickUpLater.getData().add(new XYChart.Data("Dubai",Integer.parseInt(bar[17])));
        	pickUpLater.getData().add(new XYChart.Data("Abu Dhabi",Integer.parseInt(bar[21])));
        	onSite.getData().add(new XYChart.Data("Dubai",Integer.parseInt(bar[19])));
        	onSite.getData().add(new XYChart.Data("Abu Dhabi",Integer.parseInt(bar[23])));
        	break;
    	}
    	barChart.getData().addAll(pickUpLater, onSite);
    	

    	//Create the pie chart
    	switch(area) {
		case "North":
			ObservableList<PieChart.Data> regulars = FXCollections.observableArrayList(    		
			    new PieChart.Data("Haifa Regular",Integer.parseInt(pieR[1])), new PieChart.Data("Karmiel Regular",Integer.parseInt(pieR[3])),
			    new PieChart.Data("Haifa Subscriber",Integer.parseInt(pieS[1])), new PieChart.Data("Karmiel Subscriber",Integer.parseInt(pieS[3])));
			ClientsPie.setData(regulars);
			break;
		case "South":
			ObservableList<PieChart.Data> regulars2 = FXCollections.observableArrayList(    		
				new PieChart.Data("Eilat Regular",Integer.parseInt(pieR[5])), new PieChart.Data("Be'er Sheva Regular",Integer.parseInt(pieR[7])),
				new PieChart.Data("Eilat Subscriber",Integer.parseInt(pieS[5])), new PieChart.Data("Be'er Sheva Subscriber",Integer.parseInt(pieS[7])));
			ClientsPie.setData(regulars2);
			break;
		case "UAE":
			ObservableList<PieChart.Data> regulars3 = FXCollections.observableArrayList(    		
				new PieChart.Data("Dubai Regular",Integer.parseInt(pieR[9])), new PieChart.Data("Abu Dhabi Regular",Integer.parseInt(pieR[11])),
			    new PieChart.Data("Dubai Subscriber",Integer.parseInt(pieS[9])), new PieChart.Data("Abu Dhabi Subscriber",Integer.parseInt(pieS[11])));
			ClientsPie.setData(regulars3);
			break;
		}
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
