package controller;

import java.io.IOException;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class functions as a simulation to determine from which facility the user will make their order (on site).
 * Also from the window this class represents, it is possibly to close and disconnect entirely the client side of the system.
 */
public class ChooseFacilityController {

    @FXML
    private Button Backbtn;

    @FXML
    private Button Exitbtn;

    @FXML
    private Button OKbtn;
    
    @FXML
    private Text errorMsg;

    @FXML
    private ComboBox<String> comboBoxFacility;
    
    private String facility;

    /**
     * This method is a simulation!
     * This method determines from which facility the client makes the order (on site).
     * @param event
     */
    @FXML
    void getComboBoxFacility(ActionEvent event) {
    	String s = comboBoxFacility.getSelectionModel().getSelectedItem().toString();
    	comboBoxFacility.setPromptText(s);
    	System.out.println("s=" + s);
    	switch(s) {
    	case "Haifa":
    		facility = "haifa";
    		break;
    	case "Karmiel":
    		facility = "karmiel";
    		break;
    	case "Eilat":
    		facility = "eilat";
    		break;
    	case "Be'er Sheva":
    		facility = "beersheva";
    		break;
    	case "Dubai":
    		facility = "dubai";
    		break;
    	case "Abu Dhabi":
    		facility = "abudhabi";
    		break;
    	default:
    		break;
    	}
    }
    /**
	* This method initializes the combo box in the window from which the client can choose the facility they are ordering from (simulation).
	*/
    public void initialize() {
    	ObservableList<String> list = FXCollections.observableArrayList("Haifa", "Karmiel", "Eilat", "Be'er Sheva" , "Dubai", "Abu Dhabi");
    	comboBoxFacility.setItems(list);
    	comboBoxFacility.setStyle("-fx-font-family: Rockwell; -fx-font-size: 14;");
    }
    /**
     * This method closes and disconnects entirely the client side of the system. 
     * @param event
     */
    @FXML
    void getExitbtn(ActionEvent event) {
    	ClientUI.chat.accept("disconnect");
    	((Node)event.getSource()).getScene().getWindow().hide();
    } 
    /**
	* This method closes the current window ("Choose Facility") and opens the window "OL EK".
	* @param event
	* @throws IOException
	*/ 
    @FXML
    void getBackbtn(ActionEvent event) throws IOException {
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
    /**
     * This method closes the current window ("Choose Facility") and opens the window "Welcome To E-Krut", from which the user can connect 
     * to the system itself.
     * If the user didn't chose a facility to connect to, an appropriate error message will appear on the screen,
     * @param event
     * @throws IOException
     */
    @FXML
    void getOKbtn(ActionEvent event) throws IOException {
    	if(comboBoxFacility.getPromptText() == null) {
			errorMsg.setText("Choose your facility");
		}
    	else {
    		((Node)event.getSource()).getScene().getWindow().hide();
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/WelcomeToEKrutLogin.fxml"));
        	Parent root = (Parent) loader.load();
        	WelcomeToEKrutLoginController control = loader.getController();
        	control.setFormat("EK");
        	control.setFacility(facility);
    		Stage primaryStage = new Stage();
    		Scene scene = new Scene(root);
    		primaryStage.setTitle("Welcome To E-Krut");
    		primaryStage.initStyle(StageStyle.UNDECORATED);
    		primaryStage.setScene(scene);		
    		primaryStage.show();
    	}
    }
}
