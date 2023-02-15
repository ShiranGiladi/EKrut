package controller;

import client.ClientController;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class is in charge of connecting the client side to the system.
 */
public class ClientConnectFormController {
    @FXML
    private Button btnConnect;

    @FXML
    private Button btnExit;

    @FXML
    private Label lblIPServer;

    @FXML
    private TextField txtIPServer;
    
    /**
     * This method start the window "Client Connect Form", from which the client connects to the system
     * @param primaryStage
     * @throws Exception
     */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/GUI/ClientConnectForm.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Client Connect Form");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	/*
	 * This method closes the window "Client Connect Form" (exits the client side).
	 */
    @FXML
	public void getExitBtn(ActionEvent event) throws Exception {
		System.out.println("exit Client Connect");
		((Node)event.getSource()).getScene().getWindow().hide();
	}
    /**
     * This method connects the client to the system. 
     * Once the client clicks on the connect button, and assuming no problems occurred, 
     * the current window, "Client Connect Form", will be closed, and instead the window "OL EK" will open.
     * To connect the client needs to enter the server's IP address.
     * @param event
     * @throws Exception
     */
    public void getbtnConnect(ActionEvent event) throws Exception {
    	ClientController cc = new ClientController(txtIPServer.getText(), 5555);
    	ClientUI.chat = cc;
    	System.out.println(txtIPServer.getText());
		ClientUI.chat.accept("connect");
		
		((Node)event.getSource()).getScene().getWindow().hide();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/OL_EK.fxml"));
    	Parent root = (Parent) loader.load();
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("OL EK");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);		
		primaryStage.show();
    }
}
