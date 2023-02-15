package controller;

import java.io.IOException;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class functions as a simulation to determine whether the system will be in a OL mode or EK mode.
 * If the chosen mode is OL, the system will open a login window for the user to log into the system itself
 * If the chosen mode is EK, the system will open another simulation window to chose the facility from which the client will make their order (on site).
 * Also from the window this class represents, it is possibly to close and disconnect entirely the client side of the system.
 */
public class OL_EKController {

    @FXML
    private Button ekbtn;

    @FXML
    private Button olbtn;

    @FXML
    private Button Exitbtn;
    
    /**
     * This method closes the current window ("OL EK") and opens the window "Choose Facility" (another simulation window for the user to 
     * chose the facility from which they will make their order on site.
     * @param event
     * @throws IOException
     */
    @FXML
    void getekbtn(ActionEvent event) throws IOException {
    	((Node)event.getSource()).getScene().getWindow().hide();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/ChooseFacility.fxml"));
    	Parent root = (Parent) loader.load();
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Choose Facility");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);		
		primaryStage.show();
    }

    /**
     * This method closes the current window ("OL EK") and opens the window "Welcome To E-Krut - Login" in an OL mode.
     * @param event
     * @throws IOException
     */
    @FXML
    void getolbtn(ActionEvent event) throws IOException {
    	((Node)event.getSource()).getScene().getWindow().hide();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/WelcomeToEKrutLogin.fxml"));
    	Parent root = (Parent) loader.load();
    	WelcomeToEKrutLoginController control = loader.getController();
    	control.setFormat("OL");
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Welcome To E-Krut");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);		
		primaryStage.show();
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
}
