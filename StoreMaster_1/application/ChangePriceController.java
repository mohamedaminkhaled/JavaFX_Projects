package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ChangePriceController {
    @FXML
    private BorderPane addStockPage;

    @FXML
    private TextField price;

    @FXML
    private TextField name;

    String itemID = null;
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
    void confirmChangePrice(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
    	
    	if(price.getText().equals("")) {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
        	Parent root = loader.load();
        	RegisterUserController registerUserController = loader.getController();
    		registerUserController.showErr("Error! „‰ ›÷·ﬂ Õœœ «·”⁄—");
    		return;
    	}
    	
    	try {
			double dPrice = Double.parseDouble(price.getText());
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
        	Parent root = loader.load();
        	RegisterUserController registerUserController = loader.getController();
    		registerUserController.showErr("Error! „‰ ›÷·ﬂ Õœœ ﬁÌ„Â —ﬁ„ÌÂ ··”⁄—");
    		return;
		}
    	
    	if(name.getText().equals("")) {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
        	Parent root = loader.load();
        	RegisterUserController registerUserController = loader.getController();
    		registerUserController.showErr("Error! „‰ ›÷·ﬂ Õœœ «·«”„");
    		return;
    	}
    	
    	Connection conn = null;
    	try {
			conn=DBinfo.connDB();
			
			String strUpdate ="UPDATE `items` SET `price`=?,`name`=? WHERE `id`=?" ;  
			PreparedStatement ps = conn.prepareStatement(strUpdate);
			ps.setString(1, price.getText());
			ps.setString(2, name.getText());
			ps.setString(3, itemID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
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
    
    void setCurrentPrice(String str) {
    	price.setText(str);
    }
    
    void setCurrentName(String str) {
    	name.setText(str);
    }
}
