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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AddCompanyController {

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfAddress;

    @FXML
    private Button dtnCancel;

    @FXML
    private Button btnConfirm;

    @FXML
    private TextField tfPhone;

    @FXML
    private TextField tfEmail;

    @FXML
    private ImageView companyImage;

    @FXML
    private Button btnChoosePicture;
    
    private void cleareFields() {
    	tfName.setText("");
    	tfAddress.setText("");
    	tfPhone.setText("");
    	tfEmail.setText("");
    	companyImage.setImage(new Image("/resources/noCompany.jpg"));
    }
    
    @FXML
    void cancelAddCompany(MouseEvent event) throws IOException {
    	cleareFields();
    }

    @SuppressWarnings("deprecation")
	@FXML
    void confirmAddCompany(MouseEvent event) throws IOException {    	
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerUserController = loader.getController();
    	
    	//validate user data	
    	//Error message for name
    	if(tfName.getText().isEmpty()) {
    		registerUserController.showErr("Error! Please Enter Name.");
    		return;
    	}
    	
    	//Error message for Address
    	if(tfAddress.getText().isEmpty()) {
    		registerUserController.showErr("Error! Please Enter Address");
    		return;
    	}
    	
    	//Error message for Phone Number
    	if(tfPhone.getText().isEmpty()) {
    		registerUserController.showErr("Error! Please Enter Phone Number");
    		return;
    	}
    	    	    	    	    	    	
    	//Company name can't be repeated in Database
    	try {
			Connection conn=DBinfo.connDB();
			Statement stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			String strSelectUsers = "SELECT `name` FROM `companies`";
			ResultSet rs = stat.executeQuery(strSelectUsers);
			rs.first();
			
			if(rs.getRow() != 0) {
				do {
					if(tfName.getText().equals(rs.getString("name"))){
						registerUserController.showErr("Error! Company Name already existed");
						return;
					}
				}
				while(rs.next());
			}
									
			//After all validation tests above, Register user 
			String sql="INSERT INTO `companies`(`name`, `address`, `phone`, `email`, `image`) "
					+ "VALUES (?,?,?,?,?)";		
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, tfName.getText());
			ps.setString(2, tfAddress.getText());
			ps.setString(3, tfPhone.getText());
			
			if(!tfEmail.getText().isEmpty())
				ps.setString(4, tfEmail.getText());
			else
				ps.setString(4, "undefined!");
			
			ps.setString(5, companyImage.getImage().impl_getUrl());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
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
			 companyImage.setImage(image);
		 }
    }
}
