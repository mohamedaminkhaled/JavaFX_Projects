package application;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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
    private ImageView idImage;

    @FXML
    private Button btnChoosePicture;

    @FXML
    private ImageView licenseImage;

    @FXML
    private Button btnChoosePicture1;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfAddress;
    
    @FXML
    private TextField tfIdNumber;
    
    @FXML
    private TextField tfLicenseNumber;

    @FXML
    private Button dtnCancel;

    @FXML
    private Button btnConfirm;

    @FXML
    private TextField tfPhone1;

    @FXML
    private TextField tfPhone2;

    @FXML
    private TextField tfPhone3;

    @FXML
    private TextField tfPhone4;

    @FXML
    private DatePicker datePickerExit;

    @FXML
    ComboBox<String> comboCars;

    @FXML
    private TextField tfHoursExit;

    @FXML
    private TextField tfMinExit;

    @FXML
    private RadioButton exitAM;

    @FXML
    private ToggleGroup exitTimeGrup;

    @FXML
    private RadioButton exitPM;

    @FXML
    private DatePicker datePickerReturn;

    @FXML
    private TextField tfHoursReturn;

    @FXML
    private TextField tfMinReturn;

    @FXML
    private RadioButton returnAM;

    @FXML
    private ToggleGroup exitTimeGrup1;

    @FXML
    private RadioButton returnPM;

    @FXML
    private TextField amntpayed;

    @FXML
    private TextField amntRest;
    
    private void cleareFields() {
    	tfName.setText("");
    	tfAddress.setText("");
    	tfIdNumber.setText("");
    	tfLicenseNumber.setText("");
    	tfPhone1.setText("");
    	tfPhone2.setText("");
    	tfPhone3.setText("");
    	tfPhone4.setText("");
    	datePickerExit.setValue(null);
    	datePickerReturn.setValue(null);
    	tfHoursExit.setText("");
    	tfMinExit.setText("");
    	exitAM.setSelected(false);
    	exitPM.setSelected(false);
    	tfHoursReturn.setText("");
    	tfMinReturn.setText("");
    	returnAM.setSelected(false);
    	returnPM.setSelected(false);
    	amntpayed.setText("");
    	amntRest.setText("");
    	comboCars.setValue(null);
    	idImage.setImage(new Image("/resources/no-image-found.png"));
    	licenseImage.setImage(new Image("/resources/no-image-found.png"));
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
    		showErr("Error! „‰ ›÷·ﬂ «œŒ· «·«”„");
    		return;
    	}
    	
    	//Error message for Address
    	if(tfAddress.getText().isEmpty()) {
    		showErr("Error! „‰ ›÷·ﬂ «œŒ· «·⁄‰Ê«‰");
    		return;
    	}
    	
    	//Error message for Phone Number
    	if(tfPhone1.getText().isEmpty() && tfPhone2.getText().isEmpty()
    			&& tfPhone3.getText().isEmpty() && tfPhone4.getText().isEmpty()) {
    		showErr("Error! „‰ ›÷·ﬂ «œŒ· ⁄·Ï «·«ﬁ· —ﬁ„ „Ê»«Ì· Ê«Õœ");
    		return;
    	}
    	
    	//Error message for cars
    	if(comboCars.getValue() != null) {
    		
    	}else {
    		showErr("Error! „‰ ›÷·ﬂ «Œ «— «·”Ì«—Â");
    		return;
    	}
    	
    	//Error message exit date
		if(!(datePickerExit.getValue() != null)) {
			showErr("Error! „‰ ›÷·ﬂ Õœœ  «—ÌŒ «·Œ—ÊÃ");
			return;
		}
		
		//Error message return date
		if(!(datePickerReturn.getValue() != null)) {
			showErr("Error! „‰ ›÷·ﬂ Õœœ  «—ÌŒ «·⁄ÊœÂ");
			return;
		}

    	//Error message for exit time AM/PM
    	if(!(exitAM.isSelected()) && !(exitPM.isSelected())) {
    		showErr("Error! AM/PM „‰ ›÷·ﬂ Õœœ «·Êﬁ ");
    		return;
    	}
    	
    	//Error message for return time AM/PM
    	if(!(exitAM.isSelected()) && !(exitPM.isSelected())) {
    		showErr("Error! AM/PM „‰ ›÷·ﬂ Õœœ «·Êﬁ ");
    		return;
    	}
    	
    	//Error message for exit hour
    	if(tfHoursExit.getText().isEmpty()) {
    		showErr("Error! „‰ ›÷·ﬂ Õœœ ”«⁄… «·Œ—ÊÃ");
    		return;
    	}
    	
    	//Error message for exit minute
    	if(tfMinExit.getText().isEmpty()) {
    		tfMinExit.setText("00");
    	}
    	
    	//Error message for return hour
    	if(tfHoursExit.getText().isEmpty()) {
    		showErr("Error! „‰ ›÷·ﬂ Õœœ ”«⁄… «·⁄ÊœÂ");
    		return;
    	}
    	
    	//Error message for exit minute
    	if(tfMinReturn.getText().isEmpty()) {
    		tfMinReturn.setText("00");
    	}
    	
    	//Error message for amount payed
    	if(amntpayed.getText().isEmpty()) {
    		showErr("Error! „‰ ›÷·ﬂ Õœœ «·„»·€ «·„œ›Ê⁄");
    		return;
    	}
    	
    	//Error message for amount rest
    	if(amntRest.getText().isEmpty()) {
    		amntRest.setText("00");
    	}
    	
    	String strComboCars = comboCars.getValue();
    	String chassisNumber = strComboCars.substring(0, 17);
    	    	    	    	    	
    	try {
			Connection conn=DBinfo.connDB();
			Statement stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			
			Date date = new Date();
			double clientId = date.getTime();
			
			
			//After all validation tests above, Register user 
			String sql="INSERT INTO `clients`(`id`, `name`, `address`, `id_number`, "
					+ "`license_number`, `id_image`,"
					+ " `license_image`,`car_chassis`) "
					+ "VALUES (?,?,?,?,?,?,?,?)";		
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setDouble(1, clientId);
			ps.setString(2, tfName.getText());
			ps.setString(3, tfAddress.getText());
			ps.setString(4, tfIdNumber.getText());
			ps.setString(5, tfLicenseNumber.getText());
			ps.setString(6, idImage.getImage().impl_getUrl());
			ps.setString(7, licenseImage.getImage().impl_getUrl());
			ps.setString(8, chassisNumber);
			ps.executeUpdate();
			
			//Prepare data for insert
			String strAmntPayed = amntpayed.getText();
			String strAmntRest = amntRest.getText();
			
			String strExitTime = exitAM.isSelected() ? "AM" : "PM";
			String strReturnTime = returnAM.isSelected() ? "AM" : "PM";
			String strExitDate = datePickerExit.getValue().toString();
			String strReturnDate = datePickerReturn.getValue().toString();
			String strExitHour = tfHoursExit.getText();
			String strReturnHour = tfHoursReturn.getText();
			String strExitMin = tfMinExit.getText();
			String strReturnMin = tfMinReturn.getText();
			
			String strExit = strExitDate + " " + strExitHour + " " + strExitMin + " " + strExitTime;
			String strReturn = strReturnDate + " " + strReturnHour + " " + strReturnMin + " " + strReturnTime;
						
			//Insert into client_cars table
			String sqlInsertClientCars="INSERT INTO `client_cars`(`amnt_payed`, `amnt_rest`, `exit_date`, "
					+ "`return_date`, `client_id`, `chassis_number`) "
					+ "VALUES (?,?,?,?,?,?)";		
			
			PreparedStatement ps2 = conn.prepareStatement(sqlInsertClientCars);
			ps2.setDouble(1, Double.parseDouble(strAmntPayed));
			ps2.setDouble(2, Double.parseDouble(strAmntRest));
			ps2.setString(3, strExit);
			ps2.setString(4, strReturn);
			ps2.setDouble(5, clientId);
			ps2.setString(6, chassisNumber);
			ps2.executeUpdate();
			
			//insert into client_phones
			String strPhone1 = tfPhone1.getText().isEmpty() ? "·« ÌÊÃœ" : tfPhone1.getText();
			String strPhone2 = tfPhone2.getText().isEmpty() ? "·« ÌÊÃœ" : tfPhone2.getText();
			String strPhone3 = tfPhone3.getText().isEmpty() ? "·« ÌÊÃœ" : tfPhone3.getText();
			String strPhone4 = tfPhone4.getText().isEmpty() ? "·« ÌÊÃœ" : tfPhone4.getText();
			
			String[] strPhones = {strPhone1, strPhone2, strPhone3, strPhone4};
			
			for(int i = 0; i < 4; i++) {
				String sqlInsertClientPhones="INSERT INTO `client_phones`(`client_id`, `phone`) "
					+ "VALUES (?,?)";		
			
				PreparedStatement ps3 = conn.prepareStatement(sqlInsertClientPhones);
				ps3.setDouble(1, clientId);
				ps3.setString(2, strPhones[i]);
				ps3.executeUpdate();
			}
			
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
			 
			 String strEventSource = event.getSource().toString();
			 if(strEventSource.contains("btnIdImage")) {
				 idImage.setImage(image);
			 }else {
				 licenseImage.setImage(image);
			 }
		 }
    }
}
