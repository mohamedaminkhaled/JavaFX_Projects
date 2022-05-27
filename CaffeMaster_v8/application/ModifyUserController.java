package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
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
   
    String username;
    String jobtitle = null;
    static ClientController clientController;
    static DashboardController dashboardController;
    
    String imagePath = "file:/D:/CaffeMaster/photos/avatar2.jpg";
    
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
    void changeAll(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerUserController = loader.getController();
    	
    	Connection conn = null;
    	try {
			conn=DBinfo.connDB();
			
			//Error message for Password
			if(tfPassword.getText().isEmpty()) {
				registerUserController.showErr("Error! Please enter password");
				return;
			}
			
			//Username and Password must be found in Database
			Statement stat = conn.createStatement();
			String strSelectUsers = "SELECT `id` FROM `employees` WHERE `username` = '"+username+"' AND `password` = '"+tfPassword.getText()+"'";
			ResultSet rs = stat.executeQuery(strSelectUsers);
			
			if(!(rs.next())) {
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
			
	    	//Create image in photos folder
	    	
	    	//*****************************************//
	    	int lastSlash = imagePath.lastIndexOf("/");
	    	String imageName = imagePath.substring(lastSlash+1);
	    	
	    	Image image = new Image(imagePath);
	    	    	
	    	File outputFile = new File("D:\\CaffeMaster\\photos\\"+imageName);
	        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
	        try {
	          ImageIO.write(bImage, "jpg", outputFile);
	        } catch (IOException e) {
	          throw new RuntimeException(e);
	        }
	        
	        imagePath = "file:/D:/CaffeMaster/photos/"+imageName;
	    	
	      //*****************************************//
			
	    	//Update image immediately
			if(jobtitle == "user") {
				clientController.setEmployeeImage(imagePath);
			}else
				dashboardController.setUserImage(imagePath);
				
			
			//After all validations above, update user data
			String strUpdate ="UPDATE `employees` set `password`=? , `image`=? WHERE `username` =?";  
					
			PreparedStatement ps = conn.prepareStatement(strUpdate);
			ps.setString(1, tfNewPassword.getText());
			ps.setString(2, imagePath);
			ps.setString(3, username);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		} finally {
			conn.close();
		}
		
		//Show Success message after registration
		registerUserController.showSuccess();
		
		Stage oldStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		oldStage.close();
    }
    
	@FXML
    void changeImage(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
		
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerUserController = loader.getController();
    	
    	//Create image in photos folder
    	
    	//*****************************************//
    	int lastSlash = imagePath.lastIndexOf("/");
    	String imageName = imagePath.substring(lastSlash+1);
    	
    	Image image = new Image(imagePath);
    	    	
    	File outputFile = new File("D:\\CaffeMaster\\photos\\"+imageName);
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        try {
          ImageIO.write(bImage, "jpg", outputFile);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        
        imagePath = "file:/D:/CaffeMaster/photos/"+imageName;
    	
      //*****************************************//
    	
    	//Update image immediately
		if(jobtitle == "user") {
			clientController.setEmployeeImage(imagePath);
		}else
			dashboardController.setUserImage(imagePath);
			
    	Connection conn = null;
    	PreparedStatement ps;
    	try {
			String strUpdate ="UPDATE `employees` set `image`=? WHERE `username` =?";  
			conn = DBinfo.connDB();
			ps = conn.prepareStatement(strUpdate);
			ps.setString(1, imagePath);			
			ps.setString(2, username);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		} finally {
			conn.close();
		}
		
		//Show Success message after registration
		registerUserController.showSuccess();
		
		Stage oldStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		oldStage.close();		
    }

    @FXML
    void resetPassword(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerUserController = loader.getController();
    	
    	Connection conn = null;
    	try {
			conn=DBinfo.connDB();
			
			//Error message for Password
			if(tfPassword.getText().isEmpty()) {
				registerUserController.showErr("Error! Please enter password");
				return;
			}
			
			//Username and Password must be found in Database
			Statement stat = conn.createStatement();
			String strSelectUsers = "SELECT `id` FROM `employees` WHERE `username` = '"+username+"' AND `password` = '"+tfPassword.getText()+"'";
			ResultSet rs = stat.executeQuery(strSelectUsers);
			
			if(!(rs.next())) {
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
		} finally {
			conn.close();
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
			 imagePath = new File(path).toURI().toString();
			 
			 userImage.setImage(image);
		 }
    }
    
    void setProfile(String image) {
    	userImage.setImage(new Image(image));
    }
}
