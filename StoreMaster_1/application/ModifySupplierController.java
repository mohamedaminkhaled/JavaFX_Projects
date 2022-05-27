package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ModifySupplierController {

    @FXML
    ImageView clientImage;

    @FXML
    TextField tfPhone;

    @FXML
    TextField tfAddrees;

    @FXML
    TextField tfEmail;
    
    String imagePath = "file:/D:/MasterVet/photos/avatar2.jpg";
    String supplierName = null;
    static SupplierCardController supplierCardController;
    
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
    void changeAll(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerUserController = loader.getController();
    	
    	Connection conn = null;
    	try {
			conn=DBinfo.connDB();
						
			//Error message for phone
			if(tfPhone.getText().isEmpty()) {
				registerUserController.showErr("Error! Please enter phone");
				return;
			}
			
			//Set default balance
			if(tfAddrees.getText().isEmpty()) {
				registerUserController.showErr("Error! Please enter phone");
				return;
			}
			
			//Set default loan
			if(tfEmail.getText().isEmpty()) {
				tfEmail.setText("Undefiend!");
			}
	    	
	    	//Create image in photos folder
	    	
	    	//*****************************************//
	    	int lastSlash = imagePath.lastIndexOf("/");
	    	String imageName = imagePath.substring(lastSlash+1);
	    	
	    	Image image = new Image(imagePath);
	    	    	
	    	File outputFile = new File("D:\\MasterVet\\photos\\"+imageName);
	        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
	        try {
	          ImageIO.write(bImage, "jpg", outputFile);
	        } catch (IOException e) {
	          throw new RuntimeException(e);
	        }
	        
	        imagePath = "file:/D:/MasterVet/photos/"+imageName;
	    	
	        //*****************************************//
			
			//Update image immediately
			supplierCardController.supplierImage.setImage(new Image(imagePath));
				
			//After all validations above, update user data
			String strUpdate ="UPDATE `supplier` set `phone`=? "
					+ ", `address`=?, `email`=?, `image`=?  WHERE `name` =?";  
					
			PreparedStatement ps = conn.prepareStatement(strUpdate);
			ps.setString(1, tfPhone.getText());
			ps.setString(2, tfAddrees.getText());
			ps.setString(3, tfEmail.getText());
			ps.setString(4, imagePath);
			ps.setString(5, supplierName);
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
    void changeImage(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {
		
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerUserController = loader.getController();
    	
    	//Create image in photos folder
    	
    	//*****************************************//
    	int lastSlash = imagePath.lastIndexOf("/");
    	String imageName = imagePath.substring(lastSlash+1);
    	
    	Image image = new Image(imagePath);
    	    	
    	File outputFile = new File("D:\\MasterVet\\photos\\"+imageName);
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        try {
          ImageIO.write(bImage, "jpg", outputFile);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        
        imagePath = "file:/D:/MasterVet/photos/"+imageName;
    	
      //*****************************************//
    	
        //Update image immediately
		supplierCardController.supplierImage.setImage(new Image(imagePath));
		
    	Connection conn = null;
    	PreparedStatement ps;
    	try {
			String strUpdate ="UPDATE `supplier` set `image`=? WHERE `name` =?";  
			conn = DBinfo.connDB();
			ps = conn.prepareStatement(strUpdate);
			ps.setString(1, imagePath);
			ps.setString(2, supplierName);
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
			 clientImage.setImage(image);
			 imagePath = new File(path).toURI().toString();

		 }
    }
    
    void setProfile(String image) {
    	clientImage.setImage(new Image(image));
    }
}
