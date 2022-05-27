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

public class AddClientController {

    @FXML
    private ImageView clientImage;

    @FXML
    private Button btnChoosePicture;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfBalance;

    @FXML
    private Button dtnCancel;

    @FXML
    private Button btnConfirm;

    @FXML
    private TextField tfPhone;

    @FXML
    private TextField tfLoan;
    
    String imagePath = "file:/D:/MasterVet/photos/avatar2.jpg";

    private void cleareFields() {
    	tfName.setText("");
    	tfPhone.setText("");
    	tfBalance.setText("");
    	tfLoan.setText("");
    	clientImage.setImage(new Image("/resources/avatar2.jpg"));
        imagePath = "file:/D:/MasterVet/photos/avatar2.jpg";

    }

    @FXML
    void cancelAddClient(MouseEvent event) {
    	cleareFields();
    }

    @FXML
    void confirmAddClient(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerUserController = loader.getController();
    	
    	//validate user data	
    	//Error message for name
    	if(tfName.getText().isEmpty()) {
    		registerUserController.showErr("Error! Please Enter Name.");
    		return;
    	}
    	
    	//Client name can't be repeated in Database
		Connection conn = DBinfo.connDB();
		Statement stat = conn.createStatement();
		String strSelectMedicine = "SELECT `name` FROM `clients`";
		ResultSet rs = stat.executeQuery(strSelectMedicine);
		
		while(rs.next()) {
			if(tfName.getText().equals(rs.getString("name"))){
				registerUserController.showErr("Error! This client name already existed");
				return;
			}
		}
		
//*************************************************************
		//Trial with only 2 clients
		rs = stat.executeQuery(strSelectMedicine);
		int i = 0;
		while(rs.next()) {
			i++;
		}
		if(i > 1) {
			registerUserController.showErr("Error! Trial version with only 2 clients");
			return;
		}
//*************************************************************

    	//Error message for Phone Number
    	if(tfPhone.getText().isEmpty()) {
    		registerUserController.showErr("Error! Please Enter Phone Number");
    		return;
    	}
    	
    	//Set default balance
    	if(tfBalance.getText().isEmpty()) {
    		tfBalance.setText("0.0");
    	}
    	
    	//Set default loan
    	if(tfLoan.getText().isEmpty()) {
    		tfLoan.setText("0.0");
    	}
    	
    	//Set loan or balance but not both
		String strZero = "0.0";
    	if(!(tfLoan.getText().equals(strZero)) && !(tfBalance.getText().equals(strZero))) {
    		registerUserController.showErr("Error! Can't set both balance and loan!");
    		return;
    	}
    	
    	try {
			Double.parseDouble(tfBalance.getText());
			Double.parseDouble(tfLoan.getText());
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
			registerUserController.showErr("Error! Please enter numeric value.");
    		return;
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
    	    	    	    	    	    	
    	try {
			
			//After all validation tests above, Register user 
			String sql="INSERT INTO `clients`(`name`,`phone`,`balance`,`loan`, `image`) "
					+ "VALUES (?,?,?,?,?)";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, tfName.getText());
			ps.setString(2, tfPhone.getText());
			ps.setString(3, tfBalance.getText());
			ps.setString(4, tfLoan.getText());
			ps.setString(5, imagePath);
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

			 clientImage.setImage(image);
		 }
    }
}
