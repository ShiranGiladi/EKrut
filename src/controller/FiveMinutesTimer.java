package controller;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import client.ClientUI;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FiveMinutesTimer {
	
	private long currTime, endTime;
	private Lock lock = new ReentrantLock();
	private String costumerID;
	private Stage primaryStage;
	
	public void setTimer(Stage primaryStage, String costumerID) {
		this.costumerID = costumerID;
		this.primaryStage = primaryStage;
	}
	
	public void startTimer() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				currTime = System.currentTimeMillis();
				endTime = currTime + 300000;
								
				while (currTime < endTime) {
					lock.lock();
					currTime = System.currentTimeMillis();
					lock.unlock();
				}
				
				Platform.runLater(() -> {
					primaryStage.close();
					ClientUI.chat.accept("logout " + costumerID);
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/OL_EK.fxml"));
			    	Parent root = null;
					try {
						root = (Parent) loader.load();
					} catch (IOException e) {
						e.printStackTrace();
					}
					Stage primaryStage = new Stage();
					Scene scene = new Scene(root);
					primaryStage.setTitle("OL EK");
					primaryStage.initStyle(StageStyle.UNDECORATED);
					primaryStage.setScene(scene);		
					primaryStage.show();
				});
			}
		});
		thread.start();
	}

	public void resetTimer() {
		lock.lock();
		currTime = System.currentTimeMillis();
		endTime = currTime + 300000;
		lock.unlock();
	}
}
