package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
				Parent root=FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
				Scene scene = new Scene(root,448,652);
				scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
				primaryStage.setResizable(true);
				primaryStage.setScene(scene);
				primaryStage.initStyle(StageStyle.TRANSPARENT);
				primaryStage.show();
			
			} catch(Exception e) {
				e.printStackTrace();
			}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
