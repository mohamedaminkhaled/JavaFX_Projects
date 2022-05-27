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

public class UserCardController {

    @FXML
    private BorderPane BorderPaneMedicineCard;

    @FXML
    private ImageView userImage;

    @FXML
    private Label name;

    @FXML
    private Label address;

    @FXML
    private Label phone;

    @FXML
    private Label salary;

    @FXML
    private Label email;
    
    String userID = null;
    private String password = null;

    void setUserCard(String id) throws IOException, SQLException, ClassNotFoundException {
		Statement state;
		ResultSet rs;
		Connection conn = null;
		
		String strSelect = "SELECT * FROM employees WHERE `id` = '"+id+"'";
		
		try {
			conn=DBinfo.connDB();
			state=conn.createStatement();
			rs=state.executeQuery(strSelect);
			rs.next();
			
			userID = rs.getString("id");
			password = rs.getString("password");
			
			//assign values to card attributes
			name.setText(rs.getString("name"));
			address.setText(rs.getString("address"));
			phone.setText(rs.getString("phone"));
			email.setText(rs.getString("email"));
			salary.setText(rs.getString("salary"));
			userImage.setImage(new Image(rs.getString("image")));
		
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		} finally {
			conn.close();
		}
    }
    
    @FXML
    void getDeleteUser(MouseEvent event) throws IOException {
    	Stage stage=new Stage();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/DeleteUserConfirmation.fxml"));
		Parent root = loader.load();
		
		DeleteUserController deleteUserController = loader.getController();
		deleteUserController.userID = userID;
		
		Scene scene=new Scene(root,586,177);
		stage.setScene(scene);
		stage.initStyle(StageStyle.UTILITY);
		stage.show();
    }

    @FXML
    void getModifySalary(MouseEvent event) throws IOException {
    	Stage stage=new Stage();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/ModifySalary.fxml"));
		Parent root = loader.load();
		
		ModifySalaryController modifySalaryController = loader.getController();
		modifySalaryController.currentSalary.setText(salary.getText());
		modifySalaryController.userID = userID;
	
		Scene scene=new Scene(root,630,266);
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
    }
    
    @FXML
    void getTakeLoan(MouseEvent event) throws IOException {
    	Stage stage=new Stage();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/TakeLoan.fxml"));
		Parent root = loader.load();
		
		TakeLoanController takeLoanController = loader.getController();
		takeLoanController.userName = name.getText();
	
		Scene scene=new Scene(root,630,266);
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
    }

    @FXML
    void getUserReport(MouseEvent event) throws IOException {
    	Stage stage=new Stage();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserPages/SearchUserReport.fxml"));
		Parent root = loader.load();
		
		SearchUserReportController searchUserReportController = loader.getController();
		searchUserReportController.userName = name.getText();
	
		Scene scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
    }
}
