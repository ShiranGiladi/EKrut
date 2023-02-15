package controller;

import java.io.IOException;
import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.User;

/**
 * This class is the "home page" of the user "area manager" (employee).
 * The area manager can go from the window that this class represents to the following windows: - View Reports
 * 																								- Set and update the threshold level of products
 * 																								- Approve new clients
 * 																								- send an executive orders to the operations worker 
 * These windows represent the area manager's areas of responsibility, and can be reached by clicking the appropriate button.
 * The area manager can also log out and get back to the "welcome to E-Krut - login" window, or exit the system altogether.
 */
public class AreaManagerController {
    @FXML
    private Button Exitbtn;
    
	@FXML
    private Button InventoryManagement;

    @FXML
    private Button Logoutbtn;
    
    @FXML
    private Button Helpbth;

    @FXML
    private Button Reports;

    @FXML
    private Button UsersManagement;

    @FXML
    private Button sendExecutiveOrderBtn;
    
    @FXML
    private TextField txtFacility;

    @FXML
    private TextField txtName;
    
    @FXML
    private TextField txtStatus;

    
    private String costumerID = ChatClient.user.getId();
    
    /**
     * This method closes the current window ("Area Manager") and opens the window "Threshold Level".
     * @param event
     * @throws IOException
     */
    @FXML
    void getInventoryManagement(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/ThresholdLevel.fxml"));
    	Parent root = (Parent) loader.load();
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Inventory Management");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);		
		primaryStage.show();
    }
    /**
	 * This method logs out the user (area manager) from the system.
	 * Once the user clicks on the "logout" button, the current window, "Area Manager", will close, and instead, the window 
	 * "Welcome To E-Krut Login" will open. 
	 * @param event
	 * @throws IOException
	 */
    @FXML
    void getLogoutbtn(ActionEvent event) throws IOException {
    	ClientUI.chat.accept("logout " + costumerID);
		//System.out.println("Client logout");
    	((Node)event.getSource()).getScene().getWindow().hide();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/WelcomeToEKrutLogin.fxml"));
    	Parent root = (Parent) loader.load();
    	WelcomeToEKrutLoginController control = loader.getController();
    	control.setFormat(ChatClient.user.getSystemFormat());
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("OL_EK");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);		
		primaryStage.show();
    }
    /**
    * This method closes the current window ("Area Manager") and opens the window "Clients To Approve".
    * @param event
    * @throws IOException
    */
    @FXML
    void getUsersManagement(ActionEvent event) throws IOException {
    	((Node)event.getSource()).getScene().getWindow().hide();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/ClientsToApprove.fxml"));
    	Parent root = (Parent) loader.load();
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Users Management");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);		
		primaryStage.show();
    }
    /**
     * This method closes the current window ("Area Manager") and opens the window "Show Reports".
     * @param event
     * @throws IOException
     */
    @FXML
    void getViewReports(ActionEvent event) throws IOException {
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
     * This method closes the current window ("Area Manager") and opens the window "Send Executive Order".
     * @param event
     * @throws IOException
     */
    @FXML
    void getSendExecutiveOrder(ActionEvent event) throws IOException {
    	((Node)event.getSource()).getScene().getWindow().hide();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/SendExecutiveOrder.fxml"));
    	Parent root = (Parent) loader.load();
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Send Executive Order");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);		
		primaryStage.show();
    }
    /**
     * This method enters the user's name, job description, and the area they are in charge of, in the appropriate fields in the user's home page.
     * @param user
     */
    public void loadDetails(User user) {
    	String fullName = user.getFirstName() + " " + user.getLastName();
    	txtName.setText(fullName);
    	txtStatus.setText(user.getJob());
    	txtFacility.setText(user.getNote());
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
    /**
     * This method opens a pop up window, with all the information about the different options that the area manager have in the system.
     * @param event
     */
    @FXML
    void getHelpbth(ActionEvent event) {
		Scene popUp = new Scene(initPane(), 530, 242);
		Stage popUpStage = new Stage();
		popUpStage.setTitle("Options");
		popUpStage.initStyle(StageStyle.UNDECORATED);
		popUpStage.setScene(popUp);
		popUpStage.show();
	}
	/**
	 * this method sets the pop up window with all the information about the different options that the area manager have in the system.
	 * @return the pop up window
	 */
	private Pane initPane() {
	  	Pane pane = new Pane();
	  	pane.setStyle("-fx-background-color: #ffffff; -fx-border-color: #000000;");
		
		Label label = new Label("What are my options?");
		label.setStyle("-fx-font-family: Rockwell; -fx-font-size: 15;");
		label.setLayoutX(18);
		label.setLayoutY(37);
		
		Label label2 = new Label("* To watch monthly reports, press \"View Reports\".");
		label2.setStyle("-fx-font-family: Rockwell; -fx-font-size: 13;");
		label2.setLayoutX(18);
		label2.setLayoutY(73);
		
		Label label3 = new Label("* To set and update the threshold level of products in a facility in your area,");
		label3.setStyle("-fx-font-family: Rockwell; -fx-font-size: 13;");
		label3.setLayoutX(18);
		label3.setLayoutY(93);
		
		Label label4 = new Label(" press \"Inventory Mangement\".");
		label4.setStyle("-fx-font-family: Rockwell; -fx-font-size: 13;");
		label4.setLayoutX(26);
		label4.setLayoutY(113);
		
		Label label5 = new Label("* To approve new clients, press \"Users Management\".");
		label5.setStyle("-fx-font-family: Rockwell; -fx-font-size: 13;");
		label5.setLayoutX(18);
		label5.setLayoutY(133);
		
		Label label6 = new Label("* To send an executive orders to the operations worker (for inventory update),");
		label6.setStyle("-fx-font-family: Rockwell; -fx-font-size: 13;");
		label6.setLayoutX(18);
		label6.setLayoutY(153);
		
		Label label7 = new Label(" press \"Send Executive Order\".");
		label7.setStyle("-fx-font-family: Rockwell; -fx-font-size: 13;");
		label7.setLayoutX(26);
		label7.setLayoutY(173);
		
		Button ok = new Button("Got It!");
		ok.setStyle("-fx-background-color: #94b5fe; -fx-font-family: Rockwell; -fx-font-size: 14;");
		ok.setLayoutX(220);
		ok.setLayoutY(200);
		ok.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				((Node)event.getSource()).getScene().getWindow().hide();
			} 
		});
		pane.getChildren().addAll(label, label2, label3, label4, label5, label6, label7, ok);;
		return pane;
	}
}
