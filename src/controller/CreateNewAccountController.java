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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.Clients;

/**
 * This class allows the service representative to create a client account for a client.
 * The client data is already in the system (the system gets it from an external "user management" system).
 * Also from the window this class represents, the service representative can go back to their home page, or exit the system altogether.
 */
public class CreateNewAccountController {
	
    @FXML
    private Button Exitbtn;
    
	@FXML
	private Button Backbtn;
	
	@FXML
	private Button CreateBtn;
	
	@FXML
    private TableView<Clients> CreateNewAccount;
	  
    @FXML
    private TableColumn<Clients, String> Create;

    @FXML
    private TableColumn<Clients, String> CreditCard;

    @FXML
    private TableColumn<Clients, String> Email;

    @FXML
    private TableColumn<Clients, String> FirstName;

    @FXML
    private TableColumn<Clients, String> ID;

    @FXML
    private TableColumn<Clients, String> LastName;

    @FXML
    private TableColumn<Clients, String> Password;

    @FXML
    private TableColumn<Clients, String> PhoneNumber;

    @FXML
    private TableColumn<Clients, String> Username;
    
    @FXML
    private Text errorMsg;
    
    private String costumerID = ChatClient.user.getId();
    
    /**
     * This method initializes the table that shows on the screen.
     */
    public void initialize() {
    	Username.setCellValueFactory(new PropertyValueFactory<Clients, String>("username"));
		Password.setCellValueFactory(new PropertyValueFactory<Clients, String>("password"));
		FirstName.setCellValueFactory(new PropertyValueFactory<Clients, String>("firstName"));
		LastName.setCellValueFactory(new PropertyValueFactory<Clients, String>("lastName"));
		ID.setCellValueFactory(new PropertyValueFactory<Clients, String>("id"));
		CreditCard.setCellValueFactory(new PropertyValueFactory<Clients, String>("creditCard"));
		PhoneNumber.setCellValueFactory(new PropertyValueFactory<Clients, String>("phoneNumber"));
		Email.setCellValueFactory(new PropertyValueFactory<Clients, String>("email"));
		ObservableList<Clients> returnList = getClients();
		if (!returnList.isEmpty()) {
			CreateNewAccount.setItems(returnList);
			CreateNewAccount.refresh();
		}
    }
    /**
     * This method fills the table with the client's data from the data base.
     * @return
     */
    public ObservableList<Clients> getClients(){
    	ObservableList<Clients> clients = FXCollections.observableArrayList();
		ClientUI.chat.accept("getUsersWithNoAccount");
		System.out.println("answerMsg=" + ChatClient.answerMsg);
		if (!ChatClient.answerMsg.equals("Empty")) {
			String[] result = ChatClient.answerMsg.split("\\,");
			for (int i = 0; i < result.length; i += 8)
				clients.add(new Clients(result[i], result[i + 1], result[i + 2], result[i + 3], result[i + 4],
						result[i + 5], result[i + 6], result[i + 7]));
		}
		return clients;
    }
    /**
     * This method creates an account for the client (the client's data is already in the system from the external "user management" system).
     * In case of any error, an appropriate error message will appear on the screen,
     * In case of success, a pop up window will appear on the screen to let the service representative know that the account was created successful.
     * @param event
     */
	@FXML
	void getCreatebtn(ActionEvent event) {
		Clients c = CreateNewAccount.getSelectionModel().getSelectedItem();
		if (c == null) {
			errorMsg.setText("Please choose an account.");
			return;
		}
		ClientUI.chat.accept("CreateAccount " + c.getId());
		switch (ChatClient.answerMsg) {
		case "Error":
			errorMsg.setText("Failed to create account.");
			break;
		case "Update":
			ObservableList<Clients> returnList = getClients();
			if (returnList.isEmpty()) {
				CreateNewAccount.getItems().clear();
			} else {
				CreateNewAccount.setItems(returnList);
			}
			CreateNewAccount.refresh();			
			popUpWindow();
			break;
		}
	}
	/**
	* This method opens a pop up window, that lets the service representative know that the client's account was created successful.
	*/
	private void popUpWindow() {
		Scene popUp = new Scene(initPane(), 370, 155);
		Stage popUpStage = new Stage();
		popUpStage.setTitle("Account created successfully");
		popUpStage.initStyle(StageStyle.UNDECORATED);
		popUpStage.setScene(popUp);
		popUpStage.show();
	}
	/**
	* This method sets the pop up window that lets the service representative know that the client's account was created successful.
	* @return the pop up window
	*/	
    private Pane initPane() {
		Pane pane = new Pane();
		pane.setStyle("-fx-background-color:  #94b5fe; -fx-border-color:  #000000;");
		
		Label label = new Label("Account created successfully.");
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
	* This method closes the current window ("Update Clients Details") and opens the window "Service Representative".
	* @param event
	* @throws IOException
	*/ 
	@FXML
	void getBackbtn(ActionEvent event) throws IOException {	
		((Node)event.getSource()).getScene().getWindow().hide();
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
	This method closes entirely the client side of the system and logs out the user that was logged in.
	* @param event
	*/
    @FXML
    void getExitbtn(ActionEvent event) {
    	ClientUI.chat.accept("logout " + costumerID);
		((Node) event.getSource()).getScene().getWindow().hide();
    }
}
