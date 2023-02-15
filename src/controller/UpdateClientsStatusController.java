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
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class allows the service representative to update the status of a certain client from regular client to subscriber.
 * Also from the window this class represents, the service representative can go back to their home page, or exit the system altogether.
 */
public class UpdateClientsStatusController {
    @FXML
    private Button Exitbtn;
    
	@FXML
	private Button Backbtn;

	@FXML
	private Button UpgradeToSubscriber;
	
	@FXML
    private Text errorMsg;
	
	@FXML
	private TextField txtClientId;
	
	private String costumerID = ChatClient.user.getId();

	/**
	* This method closes the current window ("Update Clients Details") and opens the window "Service Representative".
	* @param event
	* @throws IOException
	*/ 
	@FXML
	void getBackbtn(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/ServiceRepresentative.fxml"));
		Parent root = (Parent) loader.load();
		ServiceRepresentativeController control = loader.getController();
    	control.loadDetails(ChatClient.user);
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Service Representative");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.show();
	 }
	/**
	 * This method upgrades the client's status from regular client to subscriber.
	 * In case of an error or if the attempt to update the status fails, an appropriate message will appear on the screen.
	 * In case of success, a pop up window will appear on the screen to let the service representative know that the update was successful.
	 * @param event
	 */
	 @FXML
	 void getUpgradeToSubscriber(ActionEvent event) {
		 if (txtClientId.getText().isEmpty()) {
			 errorMsg.setText("Please enter an id number.");
			 return;
		 }
		 ClientUI.chat.accept("updateClientStatus " + txtClientId.getText());
		 switch(ChatClient.answerMsg) {
		 case "Error":
			 errorMsg.setText("Failed to update.");
			 break;
		 case "Update":
			 popUpWindow();
			 break;
		 default:
			 break;
		 }
	 }
	 /**
	  * This method opens a pop up window, that lets the service representative know that the status update was successful.
	  */
	 private void popUpWindow() {
		Scene popUp = new Scene(initPane(), 370, 155);
		Stage popUpStage = new Stage();
		popUpStage.setTitle("Updated Successfully");
		popUpStage.initStyle(StageStyle.UNDECORATED);
		popUpStage.setScene(popUp);
		popUpStage.show();
	}
	/**
	* This method sets the pop up window that lets the service representative know that the status update was successful.
	* @return the pop up window
	*/	
	private Pane initPane() {
		Pane pane = new Pane();
		pane.setStyle("-fx-background-color: #94b5fe; -fx-border-color: #000000;");
			
		Label label = new Label("Updated Successfully.");
		label.setStyle("-fx-font-family: Rockwell; -fx-font-size: 18;");
		label.setLayoutX(62);
		label.setLayoutY(48);
			
		Button ok = new Button("OK");
		ok.setStyle("-fx-background-color: #122753; -fx-text-fill: #ffffff; -fx-font-family: Rockwell; -fx-font-size: 14;");
		ok.setLayoutX(165);
		ok.setLayoutY(102);
		ok.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				((Node)event.getSource()).getScene().getWindow().hide();
			}
		});
			
		pane.getChildren().addAll(label, ok);;
		return pane;
	}
	/**
	This method closes entirely the client side of the system and logs out the user that was logged in.
	* @param event
	*/
	@FXML
	void getExitbtn(ActionEvent event) {
		ClientUI.chat.accept("logout " + costumerID);
		((Node) event.getSource()).getScene().getWindow().hide();
    }
}
