package controller;

import java.io.IOException;
import client.ChatClient;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class allows the service representative to update the details of a certain client.
 * Also from the window this class represents, the service representative can go back to their home page, or exit the system altogether.
 */
public class UpdateClientsDetailsController {
	  @FXML
	  private Button Backbtn;

	  @FXML
	  private Button Exitbtn;

	  @FXML
	  private ComboBox<String> InfoToUpdate;

	  @FXML
	  private Button SaveUpdates;
	  
	  @FXML
	  private TextField txtClientsId;

	  @FXML
	  private TextField txtNewValue;

	  @FXML
	  private TextField txtOldValue;
	  
	  @FXML
	  private Text errorMsg;
	  
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
	   * This method gets from the data base the old value of the information that the service representative wants to update, and shows it
	   * on the screen (so that the service representative can see the old value that they are updating).
	   * In any case of an error, an appropriate error message will appear on the screen, otherwise the old value will appear on the screen.
	   * @param event
	   */
	  @FXML
	  void getInfoToUpdate(ActionEvent event) {
		  if(txtClientsId.getText().isEmpty()) {
			  errorMsg.setText("Please enter an ID first.");
			  return;
		  }
		  String s = InfoToUpdate.getSelectionModel().getSelectedItem().toString();
		  InfoToUpdate.setPromptText(s);
		  String SelectedInfo = InfoToUpdate.getPromptText();
		  switch (SelectedInfo) {
		  case "First Name":
			  ClientUI.chat.accept("InfoToUpdate " + "firstName " + txtClientsId.getText());
			  break;
		  case "Last Name":
			  ClientUI.chat.accept("InfoToUpdate " + "lastName " + txtClientsId.getText());
			  break;
		  case "ID":
			  ClientUI.chat.accept("InfoToUpdate " + "id " + txtClientsId.getText());
			  break;
		  case "Credit Card Number":
			  ClientUI.chat.accept("InfoToUpdate " + "creditCard " + txtClientsId.getText());
			  break;
		  case "Phone Number":
			  ClientUI.chat.accept("InfoToUpdate " + "phone " + txtClientsId.getText());
			  break;
		  case "Email Address":
			  ClientUI.chat.accept("InfoToUpdate " + "email " + txtClientsId.getText());
			  break;
		  default:
			  break;
		  }
		  switch (ChatClient.answerMsg) {
		  case "Error":
			  errorMsg.setText("Failed to find the old value.");
			  break;
		  default:
			  txtOldValue.setText(ChatClient.answerMsg);
			  break;
		  }
	  }
	  /**
	   * This method initializes the combo box in the window with the client's information that the service representative can update.
	   */
	  public void initialize() {
	    ObservableList<String> u = FXCollections.observableArrayList("First Name", "Last Name", "ID", "Credit Card Number", "Phone Number", "Email Address");
	    InfoToUpdate.setItems(u);
	    InfoToUpdate.setStyle("-fx-font-family: Rockwell; -fx-font-size: 14;");
	  }
	  /**
	   * This method gets from the user (the service representative) which information about the client to update, and updates the new value,
	   * that the service representative enters to the data base.
	   * In case the user didn't fill one of the mandatory fields, or filled them incorrectly, an appropriate error message will appear on the screen.
	   * If the update attempt failed an appropriate error message will also appear on the screen.
	   * In case of success, a pop up window will appear on the screen to let the service representative know that the update was successful.
	   * @param event
	   */
	  @FXML
	  void getSaveUpdates(ActionEvent event) {
		  if (InfoToUpdate.getPromptText() == null) {
			  errorMsg.setText("Please choose a field to update.");
			  return;
		  }
		  if (txtClientsId.getText().isEmpty()) {
			  errorMsg.setText("Please enter ID number.");
			  return;
		  }
		  if (txtNewValue.getText().isEmpty()) {
			  errorMsg.setText("Please enter a new value to update.");
			  return;
		  }
		  switch (InfoToUpdate.getPromptText()) {
		  case "First Name":
			  ClientUI.chat.accept("updateClientDetails " + "firstName " + txtClientsId.getText() + " " + txtNewValue.getText());
			  break;
		  case "Last Name":
			  ClientUI.chat.accept("updateClientDetails " + "lastName " + txtClientsId.getText() + " " + txtNewValue.getText());
			  break;
		  case "ID":
			  ClientUI.chat.accept("updateClientDetails " + "id " + txtClientsId.getText() + " " + txtNewValue.getText());
			  break;
		  case "Credit Card Number":
			  ClientUI.chat.accept("updateClientDetails " + "creditCard " + txtClientsId.getText() + " " + txtNewValue.getText());
			  break;
		  case "Phone Number":
			  ClientUI.chat.accept("updateClientDetails " + "phone " + txtClientsId.getText() + " " + txtNewValue.getText());
			  break;
		  case "Email Address":
			  ClientUI.chat.accept("updateClientDetails " + "email " + txtClientsId.getText() + " " + txtNewValue.getText());
			  break;
		  default:
			  break;
		  }
		  switch (ChatClient.answerMsg) {
		  case "Error":
			  errorMsg.setText("Failed to update.");
			  break;
		  case "Update":
			  popUpWindow();
			  txtClientsId.setText(null);
			  txtNewValue.setText(null);
			  txtOldValue.setText(null);
			  InfoToUpdate.setPromptText(null);
			  break;
		  }
	  }
	  /**
	  * This method opens a pop up window, that lets the service representative know that the update was successful.
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
	  * This method sets the pop up window that lets the service representative know that the update was successful.
	  * @return the pop up window
	  */
	private Pane initPane() {
		Pane pane = new Pane();
		pane.setStyle("-fx-background-color: #94b5fe; -fx-border-color: #000000;");
			
		Label label = new Label("Updated successfully.");
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
	  * This method closes entirely the client side of the system and logs out the user that was logged in.
	  * @param event
	  */
	  @FXML
	  void getExitbtn(ActionEvent event) {
		  ClientUI.chat.accept("logout " + costumerID);
		  ((Node) event.getSource()).getScene().getWindow().hide();
	  }
}
