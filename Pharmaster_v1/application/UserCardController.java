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
    private Label username;

    @FXML
    private Label jobTitle;

    @FXML
    private Label salary;
    
    String userID = null;
    private String password = null;

    void setUserCard(String id) throws IOException {
		Statement state;
		ResultSet rs;
		
		String strSelect = "SELECT * FROM employees WHERE `id` = '"+id+"'";
		
		try {
			Connection conn=DBinfo.connDB();
			state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs=state.executeQuery(strSelect);
			rs.last();
			
			userID = rs.getString("id");
			password = rs.getString("password");
			
			//assign values to card attributes
			name.setText(rs.getString("name"));
			username.setText(rs.getString("username"));
			jobTitle.setText(rs.getString("jobtitle"));
			salary.setText(rs.getString("salary"));
			userImage.setImage(new Image(rs.getString("image")));
		
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
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
    void getViewPassword(MouseEvent event) throws IOException {
    	Stage stage=new Stage();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/ViewPassword.fxml"));
		Parent root = loader.load();
		
		PasswordController passwordController = loader.getController();
		passwordController.password.setText(password);
	
		Scene scene=new Scene(root,580,240);
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
    }

}
