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

public class ModifyPaymentController {

    @FXML
    private BorderPane addStockPage;

    @FXML
    TextField tfAmntRest;

    @FXML
    private TextField tfAmntSub;

    String clientID = null;
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
    void updatePayment(MouseEvent event) throws IOException {
    	
    	if(tfAmntSub.getText().equals("")) {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterClient.fxml"));
        	Parent root = loader.load();
        	RegisterUserController registerUserController = loader.getController();
    		registerUserController.showErr("Error! „‰ ›÷·ﬂ Õœœ ﬁÌ„… «·Œ’„");
    		return;
    	}
    	    		
    	try {
    		
	    	//Query to get guarantor data
			String strSelectClientAndTime = "SELECT * FROM `client_cars` WHERE `client_id` = '"+clientID+"'";
						
			Connection conn=DBinfo.connDB();
			Statement state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ResultSet rs1=state.executeQuery(strSelectClientAndTime);
			rs1.last();
	    	
			double amntPayed = rs1.getDouble("amnt_payed");
			double amntRest = rs1.getDouble("amnt_rest");
			double rest = Double.parseDouble(tfAmntRest.getText());
			double sub = Double.parseDouble(tfAmntSub.getText());
			
			if(sub < 0 || sub > amntRest) {
	        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterClient.fxml"));
	        	Parent root = loader.load();
	        	RegisterUserController registerUserController = loader.getController();
	    		registerUserController.showErr("Error!  √ﬂœ „‰ ﬁÌ„… «·Œ’„");
	    		return;
	    	}
			
			double newAmntRest = amntRest - sub;
			double newAmntPayed = amntPayed + sub;
						
			String strUpdate ="UPDATE `client_cars` SET `amnt_payed`=?, `amnt_rest`=? WHERE `client_id`=?" ;  
			PreparedStatement ps = conn.prepareStatement(strUpdate);
			ps.setDouble(1, newAmntPayed);
			ps.setDouble(2, newAmntRest);
			ps.setString(3, clientID);
			ps.executeUpdate();
		} catch (SQLException e) {
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		}
		
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterClient.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerUserController = loader.getController();
    	
    	Stage oldStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		oldStage.close();
    	
    	//Show success message
    	registerUserController.showSuccess();
    }
    
    void setAmntRest(String str) {
    	tfAmntRest.setText(str);
    }
    
}
