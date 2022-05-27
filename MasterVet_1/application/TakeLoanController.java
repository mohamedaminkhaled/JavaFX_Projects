package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TakeLoanController {

    @FXML
    private BorderPane addStockPage;
    
    @FXML
    private TextField tfLoanValue;

    String userName = null;
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
    void submitLoan(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {
    	
    	if(tfLoanValue.getText().equals("")) {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
        	Parent root = loader.load();
        	RegisterUserController registerUserController = loader.getController();
    		registerUserController.showErr("Error! Enter loan value");
    		return;
    	}
    	
    	Connection conn = null;
    	try {
			conn=DBinfo.connDB();
			
			//ex. 2021-01-19 12:10:26
			DateFormat dateFormate = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss a");
			Date date = new Date();
			String dateTimt = dateFormate.format(date );
			dateTimt = dateTimt.replace("/","-");
			
			//After all validation tests above, Register user 
			String sql="INSERT INTO `user_loans`(`user_name`,`loan`,`time`) "
					+ "VALUES (?,?,?)";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userName);
			ps.setString(2, tfLoanValue.getText());
			ps.setString(3, dateTimt);
			ps.executeUpdate();
		} catch (SQLException e) {
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
}
