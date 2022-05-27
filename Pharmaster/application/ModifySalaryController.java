package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ModifySalaryController {

    @FXML
    private BorderPane addStockPage;

    @FXML
    private FontAwesomeIconView icon_close;

    @FXML
    TextField currentSalary;

    @FXML
    private TextField newSalary;

    String userID = null;
    double x, y;
    
    @FXML
    void close(MouseEvent event) {
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	stage.close();
    }
    
    @FXML
    void pressed(MouseEvent event) {
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	x = event.getScreenX() - stage.getX();
    	y = event.getScreenY() - stage.getY();
    }
    
    @FXML
    void dragged(MouseEvent event) {
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	stage.setX(event.getScreenX() -x);
    	stage.setY(event.getScreenY() -y);
    }

    @FXML
    void confirmModifySalary(MouseEvent event) throws IOException {
    	
    	if(newSalary.getText().equals("")) {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
        	Parent root = loader.load();
        	RegisterUserController registerUserController = loader.getController();
    		registerUserController.showErr("Error! Enter New Salary");
    		return;
    	}
    		
    	try {
			Connection conn=DBinfo.connDB();
			
			int sold = 0;
			String strSold = String.valueOf(sold);
			
			String strUpdate ="UPDATE `employees` SET `salary`=? WHERE `id`=?" ;  
			PreparedStatement ps = conn.prepareStatement(strUpdate);
			ps.setString(1, newSalary.getText());
			ps.setString(2, userID);
			ps.executeUpdate();
		} catch (SQLException e) {
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		}
		
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerUserController = loader.getController();
    	
    	Stage oldStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		oldStage.close();
    	
    	//Show success message
    	registerUserController.showSuccess();
    }
    
    void setCurrentPrice(String str) {
    	currentSalary.setText(str);
    }
}
