package application;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ModifyUserController {

    @FXML
    private ImageView userImage;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private PasswordField tfNewPassword;

    @FXML
    private PasswordField tfConfirmPassword;

    @FXML
    private FontAwesomeIconView icon_close;
    
    String username;
    String jobtitle = null;
    static ClientController clientController;
    static DashboardController dashboardController;
    
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
    
    @SuppressWarnings("deprecation")
	@FXML
    void changeAll(MouseEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerUserController = loader.getController();
    	
    	try {
			Connection conn=DBinfo.connDB();
			
			//Error message for Password
			if(tfPassword.getText().isEmpty()) {
				registerUserController.showErr("Error! Please enter password");
				return;
			}
			
			//Username and Password must be found in Database
			Statement stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			String strSelectUsers = "SELECT `id` FROM `employees` WHERE `username` = '"+username+"' AND `password` = '"+tfPassword.getText()+"'";
			ResultSet rs = stat.executeQuery(strSelectUsers);
			
			if(!(rs.first())) {
				registerUserController.showErr("Error! You entered wrong password");
				return;
			}
			
			//Error message for New password
			if(tfNewPassword.getText().isEmpty()) {
				registerUserController.showErr("Error! Please enter new password");
				return;
			}
			
			//Error message for confirm password
			if(tfConfirmPassword.getText().isEmpty()) {
				registerUserController.showErr("Error! Please confirm password");
				return;
			}
			
			//Password and Confirm password must be typical
			if(! tfNewPassword.getText().equals(tfConfirmPassword.getText())) {
				registerUserController.showErr("Error! Password and new password must be typical");
				return;
			}
			
			//Update image immediately
			if(jobtitle == "user") {
				clientController.employeeImage.setImage(new Image(userImage.getImage().impl_getUrl()));
			}else
				dashboardController.image.setImage(new Image(userImage.getImage().impl_getUrl()));
				
			
			//After all validations above, update user data
			String strUpdate ="UPDATE `employees` set `password`=? , `image`=? WHERE `username` =?";  
					
			PreparedStatement ps = conn.prepareStatement(strUpdate);
			ps.setString(1, tfNewPassword.getText());
			ps.setString(2, userImage.getImage().impl_getUrl());
			ps.setString(3, username);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		}
		
		//Show Success message after registration
		registerUserController.showSuccess();
		
		Stage oldStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		oldStage.close();
    }
    
    @SuppressWarnings("deprecation")
	@FXML
    void changeImage(MouseEvent event) throws IOException {
		
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerUserController = loader.getController();
    	
    	//Update image immediately
		if(jobtitle == "user") {
			clientController.employeeImage.setImage(new Image(userImage.getImage().impl_getUrl()));
		}else
			dashboardController.image.setImage(new Image(userImage.getImage().impl_getUrl()));
		
    	Connection conn;
    	PreparedStatement ps;
    	Statement state;
    	ResultSet rs;
    	try {
			String strUpdate ="UPDATE `employees` set `image`=? WHERE `username` =?";  
			conn = DBinfo.connDB();
			ps = conn.prepareStatement(strUpdate);
			ps.setString(1, userImage.getImage().impl_getUrl());
			ps.setString(2, username);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		}
		
		//Show Success message after registration
		registerUserController.showSuccess();
		
		Stage oldStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		oldStage.close();		
    }

    @FXML
    void resetPassword(MouseEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerUserController = loader.getController();
    	
    	try {
			Connection conn=DBinfo.connDB();
			
			//Error message for Password
			if(tfPassword.getText().isEmpty()) {
				registerUserController.showErr("Error! Please enter password");
				return;
			}
			
			//Username and Password must be found in Database
			Statement stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			String strSelectUsers = "SELECT `id` FROM `employees` WHERE `username` = '"+username+"' AND `password` = '"+tfPassword.getText()+"'";
			ResultSet rs = stat.executeQuery(strSelectUsers);
			
			if(!(rs.first())) {
				registerUserController.showErr("Error! You entered wrong password");
				return;
			}
			
			//Error message for New password
			if(tfNewPassword.getText().isEmpty()) {
				registerUserController.showErr("Error! Please enter new password");
				return;
			}
			
			//Error message for confirm password
			if(tfConfirmPassword.getText().isEmpty()) {
				registerUserController.showErr("Error! Please confirm new password");
				return;
			}
			
			//Password and Confirm password must be typical
			if(! tfNewPassword.getText().equals(tfConfirmPassword.getText())) {
				registerUserController.showErr("Error! Password and new password must be typical");
				return;
			}
						    			    	
			//After all validations above, update user data
			String strUpdate ="UPDATE `employees` set `password`=? WHERE `username` =?";  
					
			PreparedStatement ps = conn.prepareStatement(strUpdate);
			ps.setString(1, tfNewPassword.getText());
			ps.setString(2, username);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		}
		
		//Show Success message after registration
		registerUserController.showSuccess();
		
		Stage oldStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		oldStage.close();
    }
    
    @FXML
    void imageChooser(MouseEvent event) {
    	FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new
			        FileChooser.ExtensionFilter("jpg", "*.jpg"),
		            new FileChooser.ExtensionFilter("JPGE", "*.JPGE"),
		            new FileChooser.ExtensionFilter("png", "*.png"));
		
		Stage stage = new Stage();
		File selectedFile = fileChooser.showOpenDialog(stage);
	     
		 if(selectedFile != null) {
			 String path = selectedFile.getAbsolutePath();		     
			 path = path.replace("\\","/");
			 Image image = new Image(new File(path).toURI().toString());
			 userImage.setImage(image);
		 }
    }
    
    void setProfile(String image) {
    	userImage.setImage(new Image(image));
    }
}
