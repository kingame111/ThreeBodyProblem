package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
		Scene scene = new Scene(loader.load(), 600, 400);
		stage.setTitle("Hello FXML");
		stage.setScene(scene);
		stage.show();
	}

}




