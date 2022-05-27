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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class WithdrawController {

    @FXML
    private TextField tfAmount;

    @FXML
    private Button dtnCancel;

    @FXML
    private Button btnConfirm;

    @FXML
    private TextField tfType;

    @FXML
    ComboBox<String> comboUsers;
    
    String imagePath = "file:/D:/MasterVet/photos/avatar2.jpg";

    private void cleareFields() {
    	tfAmount.setText("");
    	tfType.setText("");
    	comboUsers.setValue(null);
    }

    @FXML
    void cancelWithdraw(MouseEvent event) {
    	cleareFields();
    }

    @FXML
    void confirmWithdraw(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerUserController = loader.getController();
    	
    	//validate user data	
    	//Error message for name
    	if(tfAmount.getText().isEmpty()) {
    		registerUserController.showErr("Error! Please Enter amaount.");
    		return;
    	}
    	
    	try {
			Double.parseDouble(tfAmount.getText());
		} catch (NumberFormatException e2) {
			e2.printStackTrace();
			registerUserController.showErr("Error! Please Enter numeric value.");
    		return;
		}
    	    	
    	//Error message for Phone Number
    	if(tfType.getText().isEmpty()) {
    		registerUserController.showErr("Error! Please identify type.");
    		return;
    	}
    	
    	String clientName = null;
    	
    	if(comboUsers.getValue() != null) {
    		clientName = comboUsers.getValue();
    	}else {
    		clientName = "No user";
    	}
    	
    	//ex. 2021-01-19 12:10:26
    	Date date = new Date();
		DateFormat dateFormate = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss a");
		String dateTimt = dateFormate.format(date);
		dateTimt = dateTimt.replace("/","-");
    	
		Connection conn = null;
    	try {
    		conn = DBinfo.connDB();
    		
			//After all validation tests above, Register user 
			String sql="INSERT INTO `withdrawals`(`amount`,`type`,`user`,`time`) "
					+ "VALUES (?,?,?,?)";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, tfAmount.getText());
			ps.setString(2, tfType.getText());
			ps.setString(3, clientName);
			ps.setString(4, dateTimt);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		} finally {
			conn.close();
		}
		
		//clear all fields
		cleareFields();
    	
    	//Show Success message after registration
		registerUserController.showSuccess();
    }
    
    @FXML
    void getWithdrawReport(MouseEvent event) throws IOException {
    	Stage stage=new Stage();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserPages/SearchWithdrawalsReport.fxml"));
		Parent root = loader.load();
		
		SearchWithdrawalsController searchWithdrawalsController = loader.getController();
		
		Scene scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
    }
}
