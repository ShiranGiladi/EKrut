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
 * This class is the "home page" of the user "service representative" (employee).
 * The service representative can go from the window that this class represents to the following windows: - Create New Account
 * 																										  - Update Client's Details
 * 																										  - Update Client's Status
 * These windows represent the service representative's areas of responsibility, and can be reached by clicking the appropriate button.
 * The service representative can also log out and get back to the "welcome to E-Krut - login" window, or exit the system altogether.
 *
 */
public class ServiceRepresentativeController {
    @FXML
    private Button Exitbtn;
    
	@FXML
    private Button CreateNewAccountbtn;

    @FXML
    private Button Logoutbtn;
    
    @FXML
    private Button Helpbth;

    @FXML
    private Button UpdateClientsDetailsbtn;

    @FXML
    private Button UpdateClientsStatusbtn;
    
    @FXML
    private TextField txtName;
    
    @FXML
    private TextField txtStatus;
    
    private String costumerID = ChatClient.user.getId();

    /**
	 * This method logs out the user (service representative) from the system.
	 * Once the user clicks on the "logout" button, the current window, "Service Representative", will close, and instead, the window 
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
     * This method closes the current window ("Service Representative") and opens the window "Create New Account".
     * @param event
     * @throws IOException
     */
    @FXML
    void getCreateNewAccountbtn(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/CreateNewAccount.fxml"));
    	Parent root = (Parent) loader.load();
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Create New Account");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);		
		primaryStage.show();
    }
    /**
     * This method closes the current window ("Service Representative") and opens the window "Update Client's Details".
     * @param event
     * @throws IOException
     */
    @FXML
    void getUpdateClientsDetailsbtn(ActionEvent event) throws IOException {
    	((Node)event.getSource()).getScene().getWindow().hide();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/UpdateClient'sDetails.fxml"));
    	Parent root = (Parent) loader.load();
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Update Client's Details");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);		
		primaryStage.show();
    }
    /**
     * This method closes the current window ("Service Representative") and opens the window "Update Client's Status".
     * @param event
     * @throws IOException
     */
    @FXML
    void getUpdateClientsStatusbtn(ActionEvent event) throws IOException {
    	((Node)event.getSource()).getScene().getWindow().hide();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/UpdateClient'sStatus.fxml"));
    	Parent root = (Parent) loader.load();
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Update Client's Status");
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
     * This method closes entirely the client side of the system and logs out the user that was logged in.
     * @param event
     */
    @FXML
    void getExitbtn(ActionEvent event) {
    	ClientUI.chat.accept("logout " + costumerID);
		((Node) event.getSource()).getScene().getWindow().hide();
    }
    /**
     * This method opens a pop up window, with all the information about the different options that the service representative have in the system.
     * @param event
     */
    @FXML
    void getHelpbth(ActionEvent event) {
		Scene popUp = new Scene(initPane(), 530, 175);
		Stage popUpStage = new Stage();
		popUpStage.setTitle("Options");
		popUpStage.initStyle(StageStyle.UNDECORATED);
		popUpStage.setScene(popUp);
		popUpStage.show();
	}
	/**
	 * this method sets the pop up window with all the information about the different options that the service representative have in the system.
	 * @return the pop up window
	 */
	private Pane initPane() {
	  	Pane pane = new Pane();
	  	pane.setStyle("-fx-background-color: #ffffff; -fx-border-color: #000000;");
		
		Label label = new Label("What are my options?");
		label.setStyle("-fx-font-family: Rockwell; -fx-font-size: 15;");
		label.setLayoutX(18);
		label.setLayoutY(37);
		
		Label label2 = new Label("*  To create a new account for a new client, press \"Create New Account\".");
		label2.setStyle("-fx-font-family: Rockwell; -fx-font-size: 13;");
		label2.setLayoutX(18);
		label2.setLayoutY(73);
		
		Label label3 = new Label("*  To update the the details of an existing client, press \"Update Client's Details\".");
		label3.setStyle("-fx-font-family: Rockwell; -fx-font-size: 13;");
		label3.setLayoutX(18);
		label3.setLayoutY(93);
		
		Label label4 = new Label("*  To update existing client's status to \"subscriber\", press \"Update Client's Status\".");
		label4.setStyle("-fx-font-family: Rockwell; -fx-font-size: 13;");
		label4.setLayoutX(18);
		label4.setLayoutY(113);
		
		Button ok = new Button("Got It!");
		ok.setStyle("-fx-background-color: #94b5fe; -fx-font-family: Rockwell; -fx-font-size: 14;");
		ok.setLayoutX(215);
		ok.setLayoutY(133);
		ok.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				((Node)event.getSource()).getScene().getWindow().hide();
			} 
		});
		pane.getChildren().addAll(label, label2, label3, label4, ok);;
		return pane;
	}
}
