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
 * This class is the "home page" of the user "Marketing Manager" (employee).
 * The Marketing Manager can go from the window that this class represents to the window: initiate sale.
 * The Marketing Manager can also log out and get back to the "welcome to E-Krut - login" window, or exit the system altogether.
 */
public class MarketingManagerController {

    @FXML
    private Button Exitbtn;

    @FXML
    private Button InitiateDiscountbtn;

    @FXML
    private Button Logoutbtn;

    @FXML
    private TextField txtName;
    
    @FXML
    private TextField txtStatus;
    
    private String costumerID = ChatClient.user.getId();
    
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
     * This method closes the current window ("Marketing Manager") and opens the window "Init Sale".
     * @param event
     * @throws IOException
     */
    @FXML
    void getInitiateDiscountbtn(ActionEvent event) throws IOException {
    	((Node)event.getSource()).getScene().getWindow().hide();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/InitSale.fxml"));
    	Parent root = (Parent) loader.load();
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Initiate Sale");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);		
		primaryStage.show();
    }
    /**
     * This method enters the user's name and job description in the appropriate fields in the user's home page.
     * @param user
     */
    public void loadDetails(User user) {
		String fullName = user.getFirstName() + " " + user.getLastName();
    	txtName.setText(fullName);
    	txtStatus.setText(user.getJob());
	}
    /**
	 * This method logs out the user (Marketing Manager) from the system.
	 * Once the user clicks on the "logout" button, the current window, "Marketing Manager", will close, and instead, the window 
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
}
