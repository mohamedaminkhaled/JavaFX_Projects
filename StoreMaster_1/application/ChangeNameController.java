package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ChangeNameController {

    @FXML
    private BorderPane addStockPage;

    @FXML
    private TextField tfCurrentName;

    @FXML
    private TextField tfNewName;
    
    public static AdminController adminController;
    public static ClientController clientController;
    
    String adminUserName = null;
    static String jobTitle;
    
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
    void confirmChangeName(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {
    	if(tfNewName.getText().equals("")) {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterClient.fxml"));
        	Parent root = loader.load();
        	RegisterUserController registerUserController = loader.getController();
    		registerUserController.showErr("Error! „‰ ›÷·ﬂ Õœœ «·«”„ «·ÃœÌœ");
    		return;
    	}
    	
    	Connection conn	= null;
    	try {
    		    		
    		conn=DBinfo.connDB();
			String strUpdate ="UPDATE `employees` SET `name`=? WHERE `username`=?" ;  
			PreparedStatement ps = conn.prepareStatement(strUpdate);
			ps.setString(1, tfNewName.getText());
			ps.setString(2, adminUserName);
			ps.executeUpdate();
			
			adminController.setUserName(tfNewName.getText());
			
			if(jobTitle == "user") {
				clientController.setEmployeeName(tfNewName.getText());
			}
			
		} catch (SQLException e) {
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			e.printStackTrace();
			return;
		} finally {
			conn.close();
		}
		
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerUserController = loader.getController();
    	
    	Stage oldStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		oldStage.close();
    	
    	//Show success message
    	registerUserController.showSuccess();
    }
    
    void setCurrentName(String str) {
    	tfCurrentName.setText(str);
    }
}
