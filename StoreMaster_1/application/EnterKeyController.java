package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class EnterKeyController {
	double x, y;
	
	@FXML
    Label errMessage;
	
    @FXML
    Label lblSerial;

    @FXML
    private TextField tfKey;
 
	WindowBasics windowBasics = new WindowBasics();
	 
	@FXML
    void close(MouseEvent event) {
		windowBasics.close(event);
    }
    
    @FXML
    void pressed(MouseEvent event) {
    	windowBasics.pressed(event);
    }
    
    @FXML
    void dragged(MouseEvent event) {
    	windowBasics.dragged(event);
    }
    
    @FXML
    void registerKey(MouseEvent  event) throws SQLException {
    	Connection conn = null;
    	try {
			conn = DBinfo.connDB();
			String sql="UPDATE `company` SET `key`=?";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, tfKey.getText());
			ps.executeUpdate();
			
			Stage oldStage = (Stage)((Node)event.getSource()).getScene().getWindow();
			oldStage.close();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
    	
    }
}
