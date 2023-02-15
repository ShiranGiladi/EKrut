package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.User;
import server.ServerUI;
import server.mysqlConnection;

public class ServerPortFrameController  {
	
	@FXML
	private Button btnExit = null;
	
	@FXML
	private Button btnStart = null;
	
    @FXML
    private Button btnImportUsers;
	
	@FXML
	private Label lbllist;
	
    @FXML
    private TableView<User> connectionTable;
    
    @FXML
    private TableColumn<User, String> hostnameCol;
    
    @FXML
    private TableColumn<User, String> ipCol;
    
    @FXML
    private TableColumn<User, String> connectedStatusCol;
    
    @FXML
    private TextField txtIPServer;
    
	@FXML
	private TextField portxt;
	
    @FXML
    private Label lblPassword;
    
    @FXML
    private PasswordField txtPassword;
    
    String temp="";
	
	public static ObservableList<User> users = FXCollections.observableArrayList();
	
	private String getport() {
		return portxt.getText();
	}
	
	public void Listen(ActionEvent event) throws Exception {
		String p;
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		
		p=getport();
		if(p.trim().isEmpty()) {
			System.out.println("You must enter a port number");
			Parent root1=FXMLLoader.load(getClass().getResource("/GUI/ServerPort.fxml"));
			Scene scene =new Scene(root1);
			primaryStage.setTitle("Server");
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setScene(scene);
			primaryStage.show();
					
		}
		else
		{
			String dbPassword = txtPassword.getText();
			ServerUI.runServer(p, dbPassword, this);
		}
		portxt.setDisable(true);
	    ipCol.setCellValueFactory(new PropertyValueFactory<User,String>("ip"));
	    hostnameCol.setCellValueFactory(new PropertyValueFactory<User,String>("hostname"));
	    connectedStatusCol.setCellValueFactory(new PropertyValueFactory<User,String>("connected"));
		connectionTable.setItems(users);
    	InetAddress Address = InetAddress.getLocalHost();
    	txtIPServer.setText(Address.getHostAddress());
    	btnImportUsers.setDisable(false);
    	createReport();
	}
	/**
     * This method start the window "Server Port Form".
     * @param primaryStage
     * @throws Exception
     */
	public void start(Stage primaryStage) throws Exception {	
		Parent root = FXMLLoader.load(getClass().getResource("/GUI/ServerPort.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Server");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	/**
     * This method closes entirely the server side of the system.
     * @param event
     */
	public void getExitBtn(ActionEvent event) throws Exception {
		System.out.println("exit");
		System.exit(0);			
	}
	
	public void refreshTable() {
		connectionTable.refresh();
	}
	
	/**import users simulation from csv file to data base,using List<List<String>> to save all the users**/
    @FXML
    void ImportUsers(ActionEvent event) {
    	List<List<String>> records = new ArrayList<>();
    	try (Scanner scanner = new Scanner(new File("users.csv"));) {
    	    while (scanner.hasNextLine()) {
    	        records.add(getRecordFromLine(scanner.nextLine()));
    	    }
    	} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	for(List<String> user : records) {
    		if(user.equals(records.get(0)))
    			continue;
    		try {
				mysqlConnection.addUser(user.get(0), user.get(1), user.get(2), user.get(3),
						user.get(4), user.get(5), user.get(6), user.get(7), user.get(8), 
						user.get(9), user.get(10), user.get(11), user.get(12));
				if(user.get(5).equals("Client")) {
					mysqlConnection.addClient(user.get(13), user.get(6), 
							user.get(7), user.get(14), user.get(15), user.get(1), user.get(2),
							user.get(0), user.get(9), user.get(3), user.get(4), user.get(18), 
							user.get(19), user.get(17), user.get(16));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    }
    
    /**spilt the fields when the scanner get to comma**/
    private List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }
    
    private void createReport() throws SQLException, InterruptedException {
    	Thread.sleep(1500);
    	LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String currentDate = myDateObj.format(myFormatObj);
		LocalDateTime theDayBefore = myDateObj.minusDays(1);
		String dayBefore = theDayBefore.format(myFormatObj);
		String[] currDate = currentDate.split("\\-");
		String[] daybefore = dayBefore.split("\\-");
		
		if(checkDateForCreateReport(currDate[1], daybefore[1])) {
			if(!checkIfThereIsAlreadyReport("monthlycostumersyreports", dayBefore))
				createReportForTheMonth("monthlycostumersyreports");
			if(!checkIfThereIsAlreadyReport("monthlydeliveriesreports", dayBefore))
				createReportForTheMonth("monthlydeliveriesreports");
			if(!checkIfThereIsAlreadyReport("monthlyordersyreports", dayBefore))
				createReportForTheMonth("monthlyordersyreports");
			if(!checkIfThereIsAlreadyReport("monthlyinventoryreports", dayBefore))
				createReportForTheMonth("monthlyinventoryreports");
		}
    }
    
    //return true if the current month different from the month day before
    public boolean checkDateForCreateReport(String currMonth, String monthDayBefore) {
    	if(currMonth.equals(monthDayBefore))
    		return false;
    	return true;
    }
    
    //return true if there is report already exists for this month
    public boolean checkIfThereIsAlreadyReport(String reportType, String dateReport) throws SQLException {
    	if(mysqlConnection.checkIfThereIsReport(reportType, dateReport))
    		return true;
    	return false;
    }
    
    public void createReportForTheMonth(String reportType) throws SQLException {
    	switch(reportType) {
    	case "monthlycostumersyreports":
    		mysqlConnection.addCostumerReport();
    		break;
    	case "monthlydeliveriesreports":
    		mysqlConnection.createDeliveryOrder();
    		break;
    	case "monthlyordersyreports":
    		mysqlConnection.createOrderReport();
    		break;
    	case "monthlyinventoryreports":
    		mysqlConnection.createInventoryReport();
    		break;
    	default:
    		break;
    	}
    }
}