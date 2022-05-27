package application;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class ErrorServerNotFound {
	
    public void errException(SQLException e) throws IOException {
    	StringBuilder sb = new StringBuilder("Error ");  
		sb.append(e.getErrorCode());
		sb.append(": Server not found");
    	
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerUserController = loader.getController();
		
		registerUserController.showErr(sb.toString());
	}
}
