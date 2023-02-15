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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.User;

/**
 * This class is the "home page" of the user "Deliveries Operator" (employee).
 * The deliveries operator can go from the window that this class represents to the following windows: - Deliveries Management
 * 																									   - Deliveries Status
 * The deliveries operator can also log out and get back to the "welcome to E-Krut - login" window, or exit the system altogether.
 */
public class DeliveriesOperatorController {

    @FXML
    private Button Exitbtn;

    @FXML
    private Button DeliveriesManagementbtn;

    @FXML
    private Button DeliveriesStatusUpdatebtn;

    @FXML
    private Button Logoutbtn;

    @FXML
    private TextField txtArea;

    @FXML
    private TextField txtName;
    
    @FXML
    private TextField txtStatus;
    
    private String costumerID = ChatClient.user.getId();
    
    /**
     * This method closes the current window ("Deliveries Operator") and opens the window "Deliveries Management".
     * @param event
     * @throws IOException
     */
    @FXML
    void getDeliveriesManagementbtn(ActionEvent event) throws IOException {
    	((Node)event.getSource()).getScene().getWindow().hide();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/DeliveriesManagement.fxml"));
    	Parent root = (Parent) loader.load();
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Deliveries Management");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);		
		primaryStage.show();
    }
    /**
     * This method closes the current window ("Deliveries Operator") and opens the window "Deliveries Status".
     * @param event
     * @throws IOException
     */
    @FXML
    void getDeliveriesStatusUpdatebtn(ActionEvent event) throws IOException {
    	((Node)event.getSource()).getScene().getWindow().hide();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/DeliveriesStatus.fxml"));
    	Parent root = (Parent) loader.load();
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Deliveries Status");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);		
		primaryStage.show();
    }
    /**
	 * This method logs out the user (Deliveries Operator) from the system.
	 * Once the user clicks on the "logout" button, the current window, "Deliveries Operator", will close, and instead, the window 
	 * "Welcome To E-Krut Login" will open. 
	 * @param event
	 * @throws IOException
	 */
    @FXML
    void getLogoutbtn(ActionEvent event) throws IOException {
    	ClientUI.chat.accept("logout " + costumerID);
    	((Node)event.getSource()).getScene().getWindow().hide();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/WelcomeToEKrutLogin.fxml"));
    	Parent root = (Parent) loader.load();
    	WelcomeToEKrutLoginController control = loader.getController();
    	control.setFormat(ChatClient.user.getSystemFormat());
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Welcome To E-Krut");
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
    	txtArea.setText(ChatClient.user.getNote());
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
