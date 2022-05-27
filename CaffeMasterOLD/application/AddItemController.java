package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

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

public class AddItemController {

    @FXML
    private ImageView itemImage;

    @FXML
    private Button btnChoosePicture;

    @FXML
    private TextField tfItemName;

    @FXML
    private Button dtnCancel;

    @FXML
    private Button btnConfirm;

    @FXML
    private TextField tfItemPrice;
    
    @FXML
    private TextField tfSection;
    
    String adminUsername = null;
    String imagePath = "file:/D:/CaffeMaster/photos/noImage.jpg";
    
    private void cleareFields() {
    	tfItemName.setText("");
    	tfItemPrice.setText("");
    	tfSection.setText("");
    	itemImage.setImage(new Image("/resources/noImage.jpg"));
    	String imagePath = "file:/D:/CaffeMaster/photos/noImage.jpg";
    }
    
    @FXML
    void cancelAddCompany(MouseEvent event) throws IOException {
    	cleareFields();
    }

	@FXML
    void confirmAddCompany(MouseEvent event) throws IOException, ClassNotFoundException {    	
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerUserController = loader.getController();
    	
    	//validate user data	
    	//Error message for name
    	if(tfItemName.getText().isEmpty()) {
    		registerUserController.showErr("Error! „‰ ›÷·ﬂ Õœœ «”„ «·„‘—Ê»");
    		return;
    	}
    	
    	//Error message for Address
    	if(tfItemPrice.getText().isEmpty()) {
    		registerUserController.showErr("Error! „‰ ›÷·ﬂ Õœœ ”⁄— «·„‘—Ê»");
    		return;
    	}
    	
    	//Error message for clients
    	if(tfSection.getText().isEmpty()) {
    		registerUserController.showErr("Error! „‰ ›÷·ﬂ «Œ «— «·ﬁ”„");
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
    	  	    	    	    	    	
    	//Item name can't be repeated in Database
    	try {
			Connection conn=DBinfo.connDB();
			Statement stat = conn.createStatement();
			String strSelectUsers = "SELECT `name` FROM `items`";
			ResultSet rs = stat.executeQuery(strSelectUsers);
			rs.next();
			
			if(rs.getRow() != 0) {
				do {
					if(tfItemName.getText().equals(rs.getString("name"))){
						registerUserController.showErr("Error! Â–« «·„‘—Ê» „ÊÃÊœ »«·›⁄·");
						return;
					}
				}
				while(rs.next());
			}
									
			//After all validation tests above, Register user 
			String sql="INSERT INTO `items`(`id`, `name`, `price`, `image`,"
					+ " `emp_username`, `section`, `sold`) "
					+ "VALUES (?,?,?,?,?,?,?)";		
			
			Date date = new Date();
			double itemId = date.getTime();
			String strItemId = String.valueOf(itemId);
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, strItemId);
			ps.setString(2, tfItemName.getText());
			ps.setString(3, tfItemPrice.getText());
			ps.setString(4, imagePath);
			ps.setString(5, adminUsername);
			ps.setString(6, tfSection.getText());
			ps.setInt(7, 0);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
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
			 
			 itemImage.setImage(image);
		 }
    }
}
