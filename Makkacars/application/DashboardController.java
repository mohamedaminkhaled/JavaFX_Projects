package application;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DashboardController {

    @FXML
    BorderPane borderPaneDashboard;
	
	@FXML
    private Label welcomMessage;
    
    @FXML
    ImageView image;
    
    @SuppressWarnings("deprecation")
	@FXML
    void getProfile(MouseEvent event) throws IOException {
		Stage primaryStage = new Stage();
		FXMLLoader loaderModifyUser = new FXMLLoader(getClass().getResource("/AdminPages/ModifyProfile.fxml"));
		Parent root = loaderModifyUser.load();
		ModifyProfileController modifyClientController = loaderModifyUser.getController();
		modifyClientController.setProfile(image.getImage().impl_getUrl());

    	Scene scene = new Scene(root,743,549);
		primaryStage.setResizable(true);
		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.show();
    }
    
    void setWelcomeMessage(String str) {
    	welcomMessage.setText(str);
    }
    
    void setUserImage(String str) {
    	image.setImage(new Image(str));
    }
}