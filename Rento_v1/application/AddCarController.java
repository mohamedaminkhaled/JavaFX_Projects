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
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AddCarController {

    @FXML
    private ImageView carImage;

    @FXML
    private Button btnChoosePicture;

    @FXML
    private TextField tfCarName;

    @FXML
    private TextField tfChassisNumber;

    @FXML
    private Button dtnCancel;

    @FXML
    private Button btnConfirm;

    @FXML
    private TextField tfMotorNumber;

    @FXML
    private TextField tfPlateNumber;

    @FXML
    private TextField tfCounterNumber;

    @FXML
    private TextField tfColor;

    @FXML
    private TextField tfOwner;
    
    @FXML
    private TextField tfOwnerPhone;
    
    String imagePath = "file:/D:/Rento/photos/noimage.jpg";
        
    private void cleareFields() {
    	tfCarName.setText("");
    	tfChassisNumber.setText("");
    	tfMotorNumber.setText("");
    	tfCounterNumber.setText("");
    	tfPlateNumber.setText("");
    	tfColor.setText("");
    	tfOwner.setText("");
    	tfOwnerPhone.setText("");
    	carImage.setImage(new Image("/resources/no-image-found.png"));
    	imagePath = "file:/D:/Rento/photos/noimage.jpg";
    }
    
    @FXML
    void cancelAddCar(MouseEvent event) throws IOException {
    	cleareFields();
    }

	@FXML
    void confirmAddCar(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {    	
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterClient.fxml"));
    	Parent root = loader.load();
    	RegisterClientController registerUserController = loader.getController();
    	
    	//validate user data	
    	
    	//Error message for name
    	if(tfCarName.getText().isEmpty()) {
    		registerUserController.showErr("Error! „‰ ›÷·ﬂ «ﬂ » «”„ «·”Ì«—Â");
    		return;
    	}
    	
    	//Error message for chassis number 
    	if(tfChassisNumber.getText().isEmpty()) {
    		registerUserController.showErr("Error! „‰ ›÷·ﬂ «ﬂ » —ﬁ„ «·‘«”ÌÂ ");
    		return;
    	}
    	
    	String strChassisChars = tfChassisNumber.getText();
    	String subStrChassis = null;
    	
    	//Error for chassis number length
    	try {
			 subStrChassis = new String(strChassisChars.substring(0, 17));
		} catch (Exception e1) {
			//e1.printStackTrace();
			registerUserController.showErr("Error!  „‰ ›÷·ﬂ «ﬂ » —ﬁ„ «·‘«”ÌÂ „‰ 17 Œ«‰Â");
    		return;
		}
    	
    	    	
    	//Error message for plate number 
    	if(tfPlateNumber.getText().isEmpty()) {
    		registerUserController.showErr("Error! „‰ ›÷·ﬂ «ﬂ » —ﬁ„ «··ÊÕÂ «·„⁄œ‰ÌÂ");
    		return;
    	}
    	
    	//Error message for car color 
    	if(tfColor.getText().isEmpty()) {
    		registerUserController.showErr("Error! „‰ ›÷·ﬂ Õœœ ·Ê‰ «·”Ì«—Â");
    		return;
    	}
    	
    	//Create image in photos folder
    	
    	//*****************************************//
    	int lastSlash = imagePath.lastIndexOf("/");
    	String imageName = imagePath.substring(lastSlash+1);
    	
    	Image image = new Image(imagePath);
    	    	
    	File outputFile = new File("D:\\Rento\\photos\\"+imageName);
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        try {
          ImageIO.write(bImage, "jpg", outputFile);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        
        imagePath = "file:/D:/Rento/photos/"+imageName;
    	
      //*****************************************//
        
    	//Chassis number can't be repeated
        Connection conn = null;
        try {
			conn=DBinfo.connDB();
			Statement stat = conn.createStatement();
			String strSelectUsers = "SELECT * FROM `cars` WHERE `chassis_number` = '"+subStrChassis+"'";
			ResultSet rs = stat.executeQuery(strSelectUsers);
			rs.next();
			
			if(rs.getRow() > 0){
				registerUserController.showErr("Error! Â–Â «·”Ì«—Â „ÊÃÊœÂ »«·›⁄·");
				return;
			}
			
			String strSelectManager = "SELECT `username` FROM `agency_managers`";
			rs = stat.executeQuery(strSelectManager);
			rs.next();
			
			String managerUsername = rs.getString("username");
			
			//After all validation tests above, Register the car 
			String sql="INSERT INTO `cars`(`name`, `chassis_number`"
					+ ", `motor_number`, `plates_number`,`counter_number`,"
					+ " `color`, `owner`, `owner_phone`, `image`, `manager_username`) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?)";	
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, tfCarName.getText());
			ps.setString(2, subStrChassis);
			ps.setString(3, tfMotorNumber.getText());
			ps.setString(4, tfPlateNumber.getText());
			ps.setString(5, tfCounterNumber.getText());
			ps.setString(6, tfColor.getText());
			ps.setString(7, tfOwner.getText());
			ps.setString(8, tfOwnerPhone.getText());
			ps.setString(9, imagePath);
			ps.setString(10, managerUsername);
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
    void openImageChooser(MouseEvent event) {
    	
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
			 carImage.setImage(image);
		 }
    }
}
