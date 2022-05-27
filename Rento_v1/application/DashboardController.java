package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    
    String immmagePath;
    String username = null;
    
	@FXML
    void getProfile(MouseEvent event) throws IOException {
		Stage primaryStage = new Stage();
		FXMLLoader loaderModifyUser = new FXMLLoader(getClass().getResource("/AdminPages/ModifyProfile.fxml"));
		Parent root = loaderModifyUser.load();
		ModifyProfileController modifyClientController = loaderModifyUser.getController();
		modifyClientController.setProfile(immmagePath);
		modifyClientController.imagePath = immmagePath;
		modifyClientController.username = username;
		
    	Scene scene = new Scene(root,743,549);
		primaryStage.setResizable(true);
		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.show();
    }
	
    @FXML
    void changeName(MouseEvent event) throws SQLException {
    	Connection conn = null;
    	
    	try {
	    	Stage stage=new Stage();
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/ChangeName.fxml"));
			Parent root = loader.load();
			
			conn=DBinfo.connDB();
			
			//Select client name
			Statement stat = conn.createStatement();
			String strSelectUsers = "SELECT `name` FROM `agency_managers` WHERE `username` = '"+username+"'";
			ResultSet rs = stat.executeQuery(strSelectUsers);
			rs.next();
			
			ChangeNameController changeNameController = loader.getController();
			changeNameController.setCurrentName(rs.getString("name"));
			changeNameController.adminUserName = username;
			
			Scene scene=new Scene(root,630,266);
			stage.setScene(scene);
			stage.initStyle(StageStyle.TRANSPARENT);
			stage.show();
		
    	} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
	
    }
    
    void setWelcomeMessage(String str) {
    	welcomMessage.setText(str);
    }
    
    void setUserImage(String str) {
    	image.setImage(new Image(str));
    }
}