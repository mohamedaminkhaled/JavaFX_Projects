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

public class RegisterUserController {

    @FXML
    private ImageView userImage;

    @FXML
    private Button btnChoosePicture;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfAddress;

    @FXML
    private TextField tfUserName;

    @FXML
    private Button dtnCancel;

    @FXML
    private Button btnConfirm;

    @FXML
    private PasswordField tfPasswoard;

    @FXML
    private PasswordField tfConfirmPasswoard;

    @FXML
    private HBox radioGroup;

    @FXML
    private RadioButton radioMale;

    @FXML
    private ToggleGroup gender;

    @FXML
    private RadioButton radioFemale;

    @FXML
    private HBox radioGroup1;

    @FXML
    private RadioButton radioAdmin;

    @FXML
    private ToggleGroup jobTitle;

    @FXML
    private RadioButton radioClient;

    @FXML
    private TextField tfPhone;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfSalary;
    
    private void cleareFields() {
    	tfName.setText("");
    	tfAddress.setText("");
    	tfPhone.setText("");
    	tfEmail.setText("");
    	radioMale.setSelected(false);
    	radioFemale.setSelected(false);
    	radioAdmin.setSelected(false);
    	radioClient.setSelected(false);
    	tfSalary.setText("");
    	tfUserName.setText("");
    	tfPasswoard.setText("");
    	tfConfirmPasswoard.setText("");
    	userImage.setImage(new Image("/resources/avatar2.jpg"));
    }
    
    @FXML
    void cancelRegisterUser(MouseEvent event) throws IOException {
    	cleareFields();
    }

    @SuppressWarnings("deprecation")
	@FXML
    void confirmRegisterUser(MouseEvent event) throws IOException {    	
    	//validate user data
    	//Error message for name
    	if(tfName.getText().isEmpty()) {
    		showErr("Error! Please Enter Name.");
    		return;
    	}
    	
    	//Error message for Address
    	if(tfAddress.getText().isEmpty()) {
    		showErr("Error! Please Enter Address");
    		return;
    	}
    	
    	//Error message for Phone Number
    	if(tfPhone.getText().isEmpty()) {
    		showErr("Error! Please Enter Phone Number");
    		return;
    	}
    	
    	
    	//Error message for Gender
    	if(!(radioFemale.isSelected()) && !(radioMale.isSelected())) {
    		showErr("Error! your gender must be selected");
    		return;
    	}
    	
    	//Error message for Job title
    	if(!(radioAdmin.isSelected()) && !(radioClient.isSelected())) {
    		showErr("Error! your job title must be selected");
    		return;
    	}
    	
    	//Error message for Salary
    	if(tfSalary.getText().isEmpty()) {
    		showErr("Error! Please Enter Salary");
    		return;
    	}
    	
    	//Error message for Username
    	if(tfUserName.getText().isEmpty()) {
    		showErr("Error! Please Enter Username");
    		return;
    	}
    	    	    	    	    	
    	//Username can't be repeated in Database
    	try {
			Connection conn=DBinfo.connDB();
			Statement stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			String strSelectUsers = "SELECT `username` FROM `employees`";
			ResultSet rs = stat.executeQuery(strSelectUsers);
			rs.first();
			
			do {
				if(tfUserName.getText().equals(rs.getString("username"))){
					showErr("Error! Username already existed");
					return;
				}
			}
			while(rs.next());
			
			//Error message for Password
			if(tfPasswoard.getText().isEmpty()) {
				showErr("Error! Please Enter Password");
				return;
			}
			
			//Error message for confirm Password
			if(tfConfirmPasswoard.getText().isEmpty()) {
				showErr("Error! Please Confirm Password");
				return;
			}

			if(!(tfConfirmPasswoard.getText().equals(tfPasswoard.getText()))) {
				showErr("Error! Pssword and Confirm Password must be tepical");
				return;
			}
			
			//After all validation tests above, Register user 
			String sql="INSERT INTO `employees`(`name`, `address`, `phone`, `email`,"
					+ "`gender`, `jobtitle`, `salary`, `username`, `password`, `image`) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?)";		
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, tfName.getText());
			ps.setString(2, tfAddress.getText());
			ps.setString(3, tfPhone.getText());
			
			if(!tfEmail.getText().isEmpty())
				ps.setString(4, tfEmail.getText());
			else
				ps.setString(4, "undefined!");
			
			ps.setString(5, radioMale.isSelected() ? "male" : "femaile");
			ps.setString(6, radioAdmin.isSelected() ? "admin" : "user");
			ps.setString(7, tfSalary.getText());
			ps.setString(8, tfUserName.getText());
			ps.setString(9, tfPasswoard.getText());
			ps.setString(10, userImage.getImage().impl_getUrl());
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
		showSuccess();
    }
     
    void showErr(String message) throws IOException {
		Stage stage = new Stage();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ErrorMessage.fxml"));
    	Parent root = loader.load();
    	
    	ErrorMessageController errorMessageController = loader.getController();
    	
		Scene scene = new Scene(root,598,203);					
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		errorMessageController.errMessage.setText(message);
		stage.show();
	}
    
    void showSuccess() throws IOException {    	
    	Stage stage = new Stage();
    	FXMLLoader loaderSuccess = new FXMLLoader(getClass().getResource("/fxml/SuccessMessage.fxml"));
		Parent root = loaderSuccess.load();
    	    	
		Scene scene = new Scene(root,584,210);					
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
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
			 userImage.setImage(image);
		 }
    }
}
