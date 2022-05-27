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
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RegisterGurantorController {

    @FXML
    private ImageView idImage;
    
    @FXML
    private TextField tfName;

    @FXML
    private TextField tfAddress;
    
    @FXML
    private TextField tfIdNumber;

    @FXML
    private TextField tfPhone1;

    @FXML
    private TextField tfPhone2;

    @FXML
    private TextField tfPhone3;

    @FXML
    private TextField tfPhone4;

    @FXML
    ComboBox<String> comboClients;
    
    String imagePath = "file:/D:/Rento/photos/noimage.jpg";
    
    private void cleareFields() {
    	tfName.setText("");
    	tfAddress.setText("");
    	tfIdNumber.setText("");
    	tfPhone1.setText("");
    	tfPhone2.setText("");
    	tfPhone3.setText("");
    	tfPhone4.setText("");
    	comboClients.setValue(null);
    	idImage.setImage(new Image("/resources/no-image-found.png"));
    	imagePath = "file:/D:/Rento/photos/noimage.jpg";
    }
    
    @FXML
    void cancelRegisterGuarantor(MouseEvent event) throws IOException {
    	cleareFields();
    }

	@FXML
    void confirmRegisterGuarantor(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {    	
    	//validate user data
    	
    	//Error message for name
    	if(tfName.getText().isEmpty()) {
    		showErr("Error! „‰ ›÷·ﬂ «œŒ· «·«”„");
    		return;
    	}

    	//Error message for clients
    	if(comboClients.getValue() != null) {
    		
    	}else {
    		showErr("Error! „‰ ›÷·ﬂ «Œ «— «·⁄„Ì·");
    		return;
    	}
    	
    	//Error message for Phone Number
    	if(tfPhone1.getText().isEmpty() && tfPhone2.getText().isEmpty()
    			&& tfPhone3.getText().isEmpty() && tfPhone4.getText().isEmpty()) {
    		showErr("Error! „‰ ›÷·ﬂ «œŒ· ⁄·Ï «·«ﬁ· —ﬁ„ „Ê»«Ì· Ê«Õœ");
    		return;
    	}
    	
    	//Create image in photos folder
    	Date date = new Date();
    	
    	//*****************************************//
    	int lastSlash = imagePath.lastIndexOf("/");
    	String imageName = imagePath.substring(lastSlash+1);
    	
    	Image image = new Image(imagePath);
    	    	
    	File outputFile = new File("D:\\Rento\\photos\\"+date.getTime()+"_"+imageName);
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        try {
          ImageIO.write(bImage, "jpg", outputFile);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        
        imagePath = "file:/D:/Rento/photos/"+date.getTime()+"_"+imageName;
    	
      //*****************************************//
     	Connection conn = null;   	    	
    	try {
			conn=DBinfo.connDB();
			Statement stat = conn.createStatement();
			
			//After all validation tests above, Register user 
			String sql="INSERT INTO `guarantors`(`name`, `address`, `id_image`,"
					+ "`id_number`, `client_id`) "
					+ "VALUES (?,?,?,?,?)";
			
			String strClientName = comboClients.getValue();
			String strSelectClentId = "SELECT id from clients WHERE name = '"+strClientName+"'";
			
			ResultSet rs1 = stat.executeQuery(strSelectClentId);
			rs1.next();
			
			double clientId = rs1.getDouble("id");
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, tfName.getText());
			ps.setString(2, tfAddress.getText());
			ps.setString(3, imagePath);
			ps.setString(4, tfIdNumber.getText());
			ps.setDouble(5, clientId);
			ps.executeUpdate();
			
			//insert into guarantor_phones
			Statement state=conn.createStatement();
	    	String strSelectClientId = "SELECT id FROM `guarantors`";
	    	ResultSet rs = state.executeQuery(strSelectClientId);
	    	rs.next();
	    	
	    	String strGuarantorId = rs.getString("id");
			
			String strPhone1 = tfPhone1.getText().isEmpty() ? "·« ÌÊÃœ" : tfPhone1.getText();
			String strPhone2 = tfPhone2.getText().isEmpty() ? "·« ÌÊÃœ" : tfPhone2.getText();
			String strPhone3 = tfPhone3.getText().isEmpty() ? "·« ÌÊÃœ" : tfPhone3.getText();
			String strPhone4 = tfPhone4.getText().isEmpty() ? "·« ÌÊÃœ" : tfPhone4.getText();
			
			String[] strPhones = {strPhone1, strPhone2, strPhone3, strPhone4};
			
			for(int i = 0; i < 4; i++) {
				String sqlInsertClientPhones="INSERT INTO `guarantor_phones`(`guarantor_id`, `phone`) "
					+ "VALUES (?,?)";		
			
				PreparedStatement ps3 = conn.prepareStatement(sqlInsertClientPhones);
				ps3.setInt(1, Integer.parseInt(strGuarantorId));
				ps3.setString(2, strPhones[i]);
				ps3.executeUpdate();
			}
			
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
			 imagePath = new File(path).toURI().toString();
			 idImage.setImage(image);
			 
		 }
    }
}
