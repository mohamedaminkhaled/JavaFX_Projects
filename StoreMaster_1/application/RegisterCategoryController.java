package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class RegisterCategoryController {

    @FXML
    private TextField tfName;
        
    private void cleareFields() {
    	tfName.setText("");
    }
    
    @FXML
    void cancelAddCompany(MouseEvent event) throws IOException {
    	cleareFields();
    }

	@FXML
    void confirmAddCompany(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {    	
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerUserController = loader.getController();
    	
    	//validate user data	
    	//Error message for name
    	if(tfName.getText().isEmpty()) {
    		registerUserController.showErr("Error! Please Enter Name.");
    		return;
    	}
    	    	    	    	    	    	
    	//Category name can't be repeated in Database
    	Connection conn = null;
    	try {
			conn=DBinfo.connDB();
			Statement stat = conn.createStatement();
			String strSelectUsers = "SELECT `name` FROM `categories`";
			ResultSet rs = stat.executeQuery(strSelectUsers);
			rs.next();
			
			if(rs.getRow() != 0) {
				do {
					if(tfName.getText().equals(rs.getString("name"))){
						registerUserController.showErr("Error! Category Name already existed");
						return;
					}
				}
				while(rs.next());
			}
			
//*************************************************************
			//Trial with only 3 categories
			rs = stat.executeQuery(strSelectUsers);
			int i = 0;
			while(rs.next()) {
				i++;
			}
			if(i > 2) {
				registerUserController.showErr("Error! Trial version with only 3 categories");
				return;
			}
//*************************************************************
					
			//After all validation tests above, Register user 
			String sql="INSERT INTO `categories`(`name`) "
					+ "VALUES (?)";		
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, tfName.getText());
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

}
