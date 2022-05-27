package application;

import java.io.File;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ModifyProfileController {

    @FXML
    private ImageView userImage;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private PasswordField tfNewPassword;

    @FXML
    private PasswordField tfConfirmPassword;
 
    final String username = "makkacars";
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
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterClient.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerClientController = loader.getController();
    	
    	try {
			Connection conn=DBinfo.connDB();
			
			//Error message for Password
			if(tfPassword.getText().isEmpty()) {
				registerClientController.showErr("Error! „‰ ›÷·ﬂ «œŒ· ﬂ·„… «·„—Ê—");
				return;
			}
			
			//Username and Password must be found in Database
			Statement stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			String strSelectUsers = "SELECT * FROM `agency_managers` WHERE `username` = '"+username+"' AND `password` = '"+tfPassword.getText()+"'";
			ResultSet rs = stat.executeQuery(strSelectUsers);
			
			if(!(rs.first())) {
				registerClientController.showErr("Error! ﬂ·„… „—Ê— Œ«ÿ∆Â");
				return;
			}
			
			//Error message for New password
			if(tfNewPassword.getText().isEmpty()) {
				registerClientController.showErr("Error! „‰ ›÷·ﬂ «Œ· ﬂ·„… «·„—Ê— «·ÃœÌœÂ");
				return;
			}
			
			//Error message for confirm password
			if(tfConfirmPassword.getText().isEmpty()) {
				registerClientController.showErr("Error! »—Ã«¡  √ﬂÌœ ﬂ·„… «·„—Ê—");
				return;
			}
			
			//Password and Confirm password must be typical
			if(! tfNewPassword.getText().equals(tfConfirmPassword.getText())) {
				registerClientController.showErr("Error! ﬂ·„«  «·„—Ê— €Ì— „ ÿ«»ﬁÂ");
				return;
			}
			
			//Update image immediately
			dashboardController.image.setImage(new Image(userImage.getImage().impl_getUrl()));
			
			//After all validations above, update manager data
			String strUpdate ="UPDATE `agency_managers` set `password`=? , `image`=? WHERE `username` =?";  
					
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
		registerClientController.showSuccess();
		
		Stage oldStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		oldStage.close();
    }
    
    @SuppressWarnings("deprecation")
	@FXML
    void changeImage(MouseEvent event) throws IOException {
		
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterClient.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerClientController = loader.getController();
    	
    	//Update image immediately
		dashboardController.image.setImage(new Image(userImage.getImage().impl_getUrl()));
		
    	Connection conn;
    	PreparedStatement ps;
    	Statement state;
    	ResultSet rs;
    	try {
			String strUpdate ="UPDATE `agency_managers` set `image`=? WHERE `username` =?";  
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
		registerClientController.showSuccess();
		
		Stage oldStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		oldStage.close();		
    }

    @FXML
    void resetPassword(MouseEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterClient.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerClientController = loader.getController();
    	
    	try {
			Connection conn=DBinfo.connDB();
			
			//Error message for Password
			if(tfPassword.getText().isEmpty()) {
				registerClientController.showErr("Error! „‰ ›÷·ﬂ «œŒ· ﬂ·„… «·„—Ê—");
				return;
			}
			
			//Username and Password must be found in Database
			Statement stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			String strSelectUsers = "SELECT * FROM `agency_managers` WHERE `username` = '"+username+"' AND `password` = '"+tfPassword.getText()+"'";
			ResultSet rs = stat.executeQuery(strSelectUsers);
			
			if(!(rs.first())) {
				registerClientController.showErr("Error! ﬂ·„… „—Ê— Œ«ÿ∆Â");
				return;
			}
			
			//Error message for New password
			if(tfNewPassword.getText().isEmpty()) {
				registerClientController.showErr("Error! „‰ ›÷·ﬂ «Œ· ﬂ·„… «·„—Ê— «·ÃœÌœÂ");
				return;
			}
			
			//Error message for confirm password
			if(tfConfirmPassword.getText().isEmpty()) {
				registerClientController.showErr("Error! »—Ã«¡  √ﬂÌœ ﬂ·„… «·„—Ê—");
				return;
			}
			
			//Password and Confirm password must be typical
			if(! tfNewPassword.getText().equals(tfConfirmPassword.getText())) {
				registerClientController.showErr("Error! ﬂ·„«  «·„—Ê— €Ì— „ ÿ«»ﬁÂ");
				return;
			}
						    			    	
			//After all validations above, update user data
			String strUpdate ="UPDATE `agency_managers` set `password`=? WHERE `username` =?";  
					
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
		registerClientController.showSuccess();
		
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
