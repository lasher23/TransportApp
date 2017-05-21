package tech.bison.transportapp;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tech.bison.transportapp.view.RootLayoutController;

public class TransportApp extends Application {
	private RootLayoutController controller;

	/**
	 * Entry Point to the Application
	 * 
	 * @param The
	 *            {@link javafx.stage.Stage} for changing the GUI
	 */
	@Override
	public void start(Stage primaryStage) {
		initRootLayout();
		primaryStage.setScene(new Scene(controller.getRootPane()));
		primaryStage.show();

	}

	private void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("view/RootLayout.fxml"));
			loader.load();
			controller = (RootLayoutController) loader.getController();
		} catch (IOException e) {
			// never happens, I ensure that correct Path is set
		}
	}

	/**
	 * Application starts search Main Thread and create new Applicatoion
	 * 
	 * @param args
	 *            possible console arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
