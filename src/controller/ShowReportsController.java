package controller;

import java.io.IOException;
import client.ChatClient;
import client.ClientUI;
import controller.WelcomeToEKrutLoginController.ClientServerCommunication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class allows the user to choose the report they want to view.
 * There are two types of users that can view reports: Area Managers and CEO.
 * To view a report the user will have to choose the report type, and according to the report type to choose the following info about the report:
 *  												the area, the facility in the chosen area, the year of the report, and the month of the report.
 * Some of the reports require the user to set all these, and others require only some of them.
 * If the user filled all the mandatory fields AND the report exist in the data base, the window represented by this class will close and
 * 																							a window displaying the chosen report will open.
 * Also from the window this class represents, the area managers / CEO can go back to their home page, or exit the client side of the system altogether.
 */
public class ShowReportsController {
	
    @FXML
    private Button Exitbtn;

    @FXML
    private Button Backbtn;
    
    @FXML
    private Button ShowReport;
    
    @FXML
    private Button Helpbth;
    
    @FXML
    private ComboBox<String> Facility;

    @FXML
    private ComboBox<String> Month;

    @FXML
    private ComboBox<String> Type;

    @FXML
    private ComboBox<String> Year;
    
    @FXML
    private ComboBox<String> Area;
 
    @FXML
    private Label errorMsg;
    
    private String costumerID = ChatClient.user.getId(), msgForTesting = "";
    ObservableList<String> facilities = FXCollections.observableArrayList(); 
    
    public IClientServerCommunication clientServerCommunication = new ClientServerCommunication();
    
    class ClientServerCommunication implements IClientServerCommunication {

		@Override
		public void sendToServer(Object msg) {
			ClientUI.chat.accept((String) msg);
		}

		@Override
		public Object getServerMsg() {
			return (String) ChatClient.answerMsg;
		}
    }
    
    public void setClientServerCommunication(IClientServerCommunication clientServerCommunication){
		this.clientServerCommunication = clientServerCommunication;
	}

    /**
	  * This method closes the current window ("Show Reports") and opens a window according to the user that open the current window:
	  * 																										"Area Manager" or "CEO".
	  * (There are two types of users that can view reports: area managers and CEO. If, for instance, the CEO opened 
	  * the "Show Reports" window, then this method will open back the "CEO" window. Same goes with the area manager).
	  * @param event
	  * @throws IOException
	  */ 
	@FXML
    void getBackbtn(ActionEvent event) throws IOException {
		//return to "Area Manager" window
		((Node)event.getSource()).getScene().getWindow().hide();
		if(ChatClient.user.getJob().equals("AreaManager")) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/AreaManager.fxml")); 
	    	Parent root = (Parent) loader.load();
	    	AreaManagerController control = loader.getController();
	    	control.loadDetails(ChatClient.user);
			Stage primaryStage = new Stage();
			Scene scene = new Scene(root);
			primaryStage.setTitle("Area Manager");
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setScene(scene);		
			primaryStage.show();
		}
		//return to "CEO" window
		else {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/CEO.fxml")); 
	    	Parent root = (Parent) loader.load();
			Stage primaryStage = new Stage();
			CEOController control = loader.getController();
	    	control.loadDetails(ChatClient.user);
			Scene scene = new Scene(root);
			primaryStage.setTitle("CEO");
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setScene(scene);		
			primaryStage.show();
		}
    }
	/**
	 * This method gets the relevant data from the data base, according to the different options that the user chose in the combo boxes on the screen.
	 * Then the method will close the current window ("Show Report") and open a report window according to the chosen report type.
	 * In case the user failed to filled one or more of the mandatory fields, necessary for the report, an appropriate error message will appear
	 * 																																on the screen.
	 * In case the wanted report not available in the data base, an appropriate error message will appear on the screen.
	 * Otherwise, the current window will close and a window that displays the chosen report will open.
	 * @param event
	 * @throws IOException
	 */
    @FXML
    void getShowReport(ActionEvent event) throws IOException {
    	String type = Type.getPromptText();
    	String year = Year.getPromptText();
    	String month = Month.getPromptText();
    	String facility = Facility.getPromptText();
    	String area = Area.getPromptText();
    	if(checkDetailsForShowReport(type, year, month, facility, area)) {
    		if(type.equals("Deliveries")) {
    			((Node)event.getSource()).getScene().getWindow().hide();
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/DeliveriesReport.fxml"));
	            Parent root = (Parent) loader.load();
	        	Stage primaryStage = new Stage();
	        	Scene scene = new Scene(root);
	        	primaryStage.setTitle("Deliveries Report");
	        	primaryStage.initStyle(StageStyle.UNDECORATED);
	        	primaryStage.setScene(scene);		
	        	primaryStage.show();
    		}
    		else if(type.equals("Orders")) {
    			((Node)event.getSource()).getScene().getWindow().hide();
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/OrdersReport.fxml"));
	            Parent root = (Parent) loader.load();
	        	Stage primaryStage = new Stage();
	        	Scene scene = new Scene(root);
	        	primaryStage.setTitle("Orders Report");
	        	primaryStage.initStyle(StageStyle.UNDECORATED);
	        	primaryStage.setScene(scene);		
	        	primaryStage.show();
    		}
    		else if(type.equals("Clients")) {
    			((Node)event.getSource()).getScene().getWindow().hide();
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/ClientsReport.fxml"));
	            Parent root = (Parent) loader.load();
	        	Stage primaryStage = new Stage();
	        	Scene scene = new Scene(root);
	        	primaryStage.setTitle("Clients Report");
	        	primaryStage.initStyle(StageStyle.UNDECORATED);
	        	primaryStage.setScene(scene);		
	        	primaryStage.show();
    		}
    		else if(type.equals("Inventory")) {
    			((Node)event.getSource()).getScene().getWindow().hide();
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/InventoryReport.fxml"));
	            Parent root = (Parent) loader.load();
	        	Stage primaryStage = new Stage();
	        	Scene scene = new Scene(root);
	        	primaryStage.setTitle("Inventory Report");
	        	primaryStage.initStyle(StageStyle.UNDECORATED);
	        	primaryStage.setScene(scene);		
	        	primaryStage.show();
    		}
    	}
    }
    
    public boolean checkDetailsForShowReport(String type, String year, String month, String facility, String area) {
    	//check that the user filled all the mandatory fields.
    	if (type == null) {
    		try {
    			errorMsg.setText("You must choose report type.");
    		} catch(Exception e) {}
    		msgForTesting = "You must choose report type.";
    		return false;
    	}
    	else if (type == "Orders" && (year == null || month == null || area == null)) {
    		try {
    			errorMsg.setText("You must choose Year AND Month AND Area.");
    		} catch(Exception e) {}
    		msgForTesting = "You must choose Year AND Month AND Area.";
    		return false;
    	}
    	else if ((type == "Inventory" || type == "Clients") && (year == null || month == null || facility == null || area == null)){
    		try {
    			errorMsg.setText("You must fill all fields above.");
    		} catch(Exception e) {}
    		msgForTesting = "You must fill all fields above.";
    		return false;
    	}
    	else if (type == "Deliveries" && (year == null || month == null)) {
    		try {
    			errorMsg.setText("You must choose Year AND Month.");
    		} catch(Exception e) {}
    		msgForTesting = "You must choose Year AND Month.";
    		return false;
    	}
    	else {
        	//Orders report - check if available in data base. If not, show an appropriate error message, otherwise open report window 
    		if (type == "Orders" && year != null && month != null && area != null) {
    			clientServerCommunication.sendToServer("checktShowReports " + type + " " + month + " " + year + " " + null + " " + area);
    			msgForTesting = "Orders";
        	}
        	//Deliveries report - check if available in data base. If not, show an appropriate error message, otherwise open report window 
    		else if (type == "Deliveries" && year != null && month != null) {
    			clientServerCommunication.sendToServer("checktShowReports " + type + " " + month + " " + year + " " + null + " " + null);
    			msgForTesting = "Deliveries";
    		}
        	//Clients report - check if available in data base. If not, show an appropriate error message, otherwise open report window 
    		else if (type == "Clients" && year != null && month != null && facility != null && area != null) {
    			clientServerCommunication.sendToServer("checktShowReports " + type + " " + month + " " + year + " " + facility + " " + area);
    			msgForTesting = "Clients";
    		}
        	//Inventory report - check if available in data base. If not, show an appropriate error message, otherwise open report window 
    		else if (type == "Inventory" && year != null && month != null && facility != null && area != null) {
    			clientServerCommunication.sendToServer("checktShowReports " + type + " " + month + " " + year + " " + facility + " " + area);
    			msgForTesting = "Inventory";
    		}
    		
    		if(clientServerCommunication.getServerMsg().equals("Error")) {
    			try {
    				errorMsg.setText("Report not available.");
        		} catch(Exception e) {}
    			msgForTesting = "Report not available.";
    			return false;
    		}
    		else {
	    		return true;
    		}
    	}
    }
    
    /**
     * This method gets the chosen facility (for the report) from the "Facility" combo box.
     * @param event
     */
    @FXML
    void getFacility(ActionEvent event) {
    	String f = Facility.getSelectionModel().getSelectedItem().toString();
    	Facility.setPromptText(f);
    }
    /**
     * This method gets the chosen month (for the report) from the "Month" combo box.
     * @param event
     */
    @FXML
    void getMonth(ActionEvent event) {
    	String m = Month.getSelectionModel().getSelectedItem().toString();
    	Month.setPromptText(m);
    }
    /**
     * This method gets the chosen type (for the report) from the "Type" combo box.
     * @param event
     */
    @FXML
    void getType(ActionEvent event) {
    	String t = Type.getSelectionModel().getSelectedItem().toString();
    	Type.setPromptText(t);
    }
    /**
     * This method gets the chosen year (for the report) from the "Year" combo box.
     * @param event
     */
    @FXML
    void getYear(ActionEvent event) {
    	String y = Year.getSelectionModel().getSelectedItem().toString();
    	Year.setPromptText(y);
    }
    /**
     * This method gets the chosen area (for the report) from the "Area" combo box, and according to the chosen area sets the facility combo box.
     * @param event
     */
    @FXML
    void getArea(ActionEvent event) {
    	String a = Area.getSelectionModel().getSelectedItem().toString();
    	Area.setPromptText(a);
    	Facility.setDisable(false);
    	switch(Area.getPromptText()) {
    	case "North":
    		facilities.clear();
    		facilities.addAll("Haifa", "Karmiel");
    		break;
    	case "South":
    		facilities.clear();
    		facilities.addAll("Eilat", "Be'er Sheva");
    		break;
    	case "UAE":
    		facilities.clear();
    		facilities.addAll("Dubai", "Abu Dhabi");
    		break;
    	}
    }
    /**
	* This method initializes the combo boxes in the window, from which the user can choose, to determine which report to watch.
	* The user will have to choose the report type, and according to the report type to choose the following info about the report:
	* 								the area, the facility in the chosen area, the year of the report, and the month of the report.
	* Some of the reports require the user to set all the combo boxes on the screen, and others require only some of them.
	*/
    public void initialize() {
    	
    	switch(ChatClient.user.getNote()) {
    	case "north":
    		Area.setEditable(false);
    		Area.setPromptText("North");
    		facilities.addAll("Haifa", "Karmiel");
    		break;
    	case "south":
    		Area.setEditable(false);
    		Area.setPromptText("South");
    		facilities.addAll("Eilat", "Be'er Sheva");
    		break;
    	case "uae":
    		Area.setEditable(false);
    		Area.setPromptText("UAE");
    		facilities.addAll("Dubai", "Abu Dhabi");
    		break;
    	case "all":
    		ObservableList<String> a = FXCollections.observableArrayList("North", "South", "UAE");
        	Area.setItems(a);
        	Facility.setDisable(true);
        	System.out.println(Area.getPromptText());
    		break;
    	}
    	Facility.setItems(facilities);
    	Facility.setStyle("-fx-font-family: Rockwell");
    	
    	Area.setStyle("-fx-font-family: Rockwell");
    	
    	ObservableList<String> m = FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", 
    															"July", "August", "September", "October", "November", "December");
    	Month.setItems(m);
    	Month.setStyle("-fx-font-family: Rockwell");
    	
    	ObservableList<String> y = FXCollections.observableArrayList("2020", "2021", "2022");
    	Year.setItems(y);
    	Year.setStyle("-fx-font-family: Rockwell");
    	
    	ObservableList<String> t = FXCollections.observableArrayList("Orders", "Inventory", "Clients", "Deliveries");
    	Type.setItems(t);
    	Type.setStyle("-fx-font-family: Rockwell");
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
    
    @FXML
    void getHelpbth(ActionEvent event) {
    	Scene popUp = new Scene(initPane(), 400, 310);
		Stage popUpStage = new Stage();
		popUpStage.setTitle("HOW DOES IT WORK?");
		popUpStage.initStyle(StageStyle.UNDECORATED);
		popUpStage.setScene(popUp);
		popUpStage.show();
    }
		
    private Pane initPane() {
		Pane pane = new Pane();
		pane.setStyle("-fx-background-color:  #ffffff; -fx-border-color:  #000000;");
		
		Label label = new Label("HOW DOES IT WORK?");
		label.setStyle("-fx-font-family: Rockwell; -fx-font-size: 18;");
		label.setLayoutX(22);
		label.setLayoutY(39);
		
		Label label2 = new Label("* Inventory Report:");
		label2.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16;");
		label2.setLayoutX(22);
		label2.setLayoutY(68);
		
		Label label3 = new Label("Necessary input: Type, Area, Facility, Month, Year");
		label3.setStyle("-fx-font-family: Rockwell; -fx-font-size: 14;");
		label3.setLayoutX(41);
		label3.setLayoutY(88);
		
		Label label4 = new Label("* Orders Report:");
		label4.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16;");
		label4.setLayoutX(22);
		label4.setLayoutY(118);
		
		Label label5 = new Label("Necessary input: Type, Area, Month, Year");
		label5.setStyle("-fx-font-family: Rockwell; -fx-font-size: 14;");
		label5.setLayoutX(41);
		label5.setLayoutY(138);
		
		Label label6 = new Label("* Deliveries Report:");
		label6.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16;");
		label6.setLayoutX(22);
		label6.setLayoutY(168);
		
		Label label7 = new Label("Necessary input: Type, Month, Year");
		label7.setStyle("-fx-font-family: Rockwell; -fx-font-size: 14;");
		label7.setLayoutX(41);
		label7.setLayoutY(188);
		
		Label label8 = new Label("* Clients Report:");
		label8.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16;");
		label8.setLayoutX(22);
		label8.setLayoutY(218);
		
		Label label9 = new Label("Necessary input: Type, Area, Facility, Month, Year");
		label9.setStyle("-fx-font-family: Rockwell; -fx-font-size: 14;");
		label9.setLayoutX(41);
		label9.setLayoutY(238);
		
		Button ok = new Button("Got It!");
		ok.setStyle("-fx-background-color: #94b5fe; -fx-font-family: Rockwell; -fx-font-size: 14;");
		ok.setLayoutX(146);
		ok.setLayoutY(262);
		ok.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				((Node)event.getSource()).getScene().getWindow().hide();
			}
		});
		pane.getChildren().addAll(label, label2, label3, label4, label5, label6, label7, label8, label9, ok);;
		return pane;
	}
}
