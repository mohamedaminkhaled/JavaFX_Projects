package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ClientCardController {

    @FXML
    private BorderPane BorderPaneMedicineCard;
    
    @FXML
    private Label tfClientId;

    @FXML
    private Label tfClientName;

    @FXML
    private Label tfLicenseNumber;

    @FXML
    private Label tfIdNumber;

    @FXML
    private Label tfAddress;

    @FXML
    private Label tfExitDay;

    @FXML
    private Label tfReturnDay;

    @FXML
    private Label tfCarName;

    @FXML
    private Label tfCarColor;

    @FXML
    private Label tfChassisNumber;

    @FXML
    private Label tfMotorNumber;

    @FXML
    private Label tfPlateNumber;

    @FXML
    private Label tfCounterNumber;

    @FXML
    private Label tfOwnerName;
    
    @FXML
    private Label tfOwnerPhone;

    @FXML
    private Label tfClientPhone1;

    @FXML
    private Label tfClientPhone2;

    @FXML
    private Label tfClientPhone3;

    @FXML
    private Label tfClientPhone4;

    @FXML
    private Label tfGuarantorName;
    
    @FXML
    private Label tfGuarantorId;

    @FXML
    private Label tfGuarantorIdNumber;

    @FXML
    private Label tfGuarantorAddress;

    @FXML
    private Label tfGuarantorPhone1;

    @FXML
    private Label tfGuarantorPhone2;

    @FXML
    private Label tfGuarantorPhone3;

    @FXML
    private Label tfGuarantorPhone4;
    
    @FXML
    private Label tfAmntPayed;

    @FXML
    private Label tfAmntRest;

    @FXML
    private ImageView carImage;

    @FXML
    private ImageView clientIdImage;

    @FXML
    private ImageView clientLicenseImage;

    @FXML
    private ImageView guarantorIdImage;
        
    void setClientCard(String id) throws IOException {
		
		try {			
			//Query to get client data, exit date and return date
			String strSelectClientAndTime = "SELECT client_cars.*, clients.* "
					+ "FROM client_cars INNER JOIN clients "
					+ "ON client_cars.client_id = clients.id "
					+ "WHERE client_cars.client_id = '"+id +"'";
			
			Connection conn=DBinfo.connDB();
			Statement state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ResultSet rs1=state.executeQuery(strSelectClientAndTime);
			rs1.last();
			
			//Get client data, exit date and return date
			String clientName = rs1.getString("name");
			String clientAddress = rs1.getString("address");
			String clientIdNumber = rs1.getString("id_number");
			String strClientIdImage = rs1.getString("id_image");
			String strClientLicenseImage = rs1.getString("license_image");
			String clientLicenseNumber = rs1.getString("license_number");
			String exitDate = rs1.getString("exit_date");
			String returnDate = rs1.getString("return_date");
			String amntPayed = rs1.getString("amnt_payed");
			String amntRest = rs1.getString("amnt_rest");
			
			//System.out.println(clientName + " " + clientAddress);
			
			//Query to get car data
			String strSelectCarChassis = "SELECT `chassis_number` "
					+ "FROM client_cars "
					+ "WHERE client_cars.client_id = '"+id +"'";
			
			ResultSet rsCarChassis = state.executeQuery(strSelectCarChassis);
			rsCarChassis.last();
			
			String strCarChassis = rsCarChassis.getString("chassis_number");
			System.out.println(strCarChassis);
			
			String strSelectCarData = "SELECT * FROM cars "
					+ "WHERE `chassis_number` = '"+strCarChassis +"'";
			
			ResultSet rs2 = state.executeQuery(strSelectCarData);
			rs2.last();
			
			//Get car data
			String carName = rs2.getString("name");
			String chassisNumber = rs2.getString("chassis_number");
			String motorNumber = rs2.getString("motor_number");
			String counterNumber = rs2.getString("counter_number");
			String platesNumber = rs2.getString("plates_number");
			String owner = rs2.getString("owner");
			String ownerPhone = rs2.getString("owner_phone");
			String strCarImage = rs2.getString("image");
			String carColor = rs2.getString("color");
						
			//Query to get guarantor data
			String strSelectGuarantorData = "SELECT * FROM `guarantors` WHERE `client_id` = '"+id+"'";
						
			ResultSet rs3=state.executeQuery(strSelectGuarantorData);
			rs3.last();
			
			//Set guarantor data
			String guarantorName = "áÇ íæÌÏ";
			String guarantorAddress = "áÇ íæÌÏ";
			String guarantorIdNumber = "áÇ íæÌÏ";
			String strGuarantorIdImage = "file:/D:/courses/software%20engineering/Projects/JavaFX_projects/PHARMASTER/Javafx_Workspace/MakkaAgency_src/resources/no-image-found.png";
			
			if(rs3.getRow() > 0) {
				guarantorName = rs3.getString("name");
				guarantorAddress = rs3.getString("address");
				guarantorIdNumber = rs3.getString("id_number");
				strGuarantorIdImage = rs3.getString("id_image");
			}
			
			//Query to get client phones
			String strSelectClientPhones = "SELECT * FROM `client_phones` WHERE `client_id` = '"+id+"'";
			System.out.println(id);
			
			ResultSet rs4 = state.executeQuery(strSelectClientPhones);
			
			ArrayList<String> clientPhones = new ArrayList<>();
			
			while(rs4.next()) {
				clientPhones.add(rs4.getString("phone"));
			}
			
			//Query to get guarantor phones
			String strSelectGuarantorPhones = "SELECT phone FROM guarantor_phones "
					+ "WHERE guarantor_id = "
					+ "(SELECT id FROM guarantors WHERE client_id = '"+id +"')";
			

			ResultSet rs5 = state.executeQuery(strSelectGuarantorPhones);
			
			//Set guarantor phones						
			ArrayList<String> guarantorPhones = new ArrayList<>();
			
			while(rs5.next()) {
				guarantorPhones.add(rs5.getString("phone"));
			}
			
			rs5.last();
			int strRowCount = rs5.getRow();
			System.out.println("strRowCount: " + strRowCount);
			
			if(strRowCount == 0) {
				guarantorPhones.add(0, "áÇ íæÌÏ");
				guarantorPhones.add(1, "áÇ íæÌÏ");
				guarantorPhones.add(2, "áÇ íæÌÏ");
				guarantorPhones.add(3, "áÇ íæÌÏ");
			}
						
			//assign values to client card attributes
			tfClientId.setText(id);
			tfClientName.setText(clientName);
			tfAddress.setText(clientAddress);
			tfIdNumber.setText(clientIdNumber);
			tfLicenseNumber.setText(clientLicenseNumber);
			tfExitDay.setText(exitDate);
			tfReturnDay.setText(returnDate);
			tfAmntPayed.setText(amntPayed);
			tfAmntRest.setText(amntRest);
			tfClientPhone1.setText(clientPhones.get(0));
			tfClientPhone2.setText(clientPhones.get(1));
			tfClientPhone3.setText(clientPhones.get(2));
			tfClientPhone4.setText(clientPhones.get(3));
			
			tfCarName.setText(carName);
			tfChassisNumber.setText(chassisNumber);
			tfCarColor.setText(carColor);
			tfMotorNumber.setText(motorNumber);
			tfCounterNumber.setText(counterNumber);
			tfPlateNumber.setText(platesNumber);
			tfOwnerName.setText(owner);
			tfOwnerPhone.setText(ownerPhone);
			
			tfGuarantorName.setText(guarantorName);
			tfGuarantorAddress.setText(guarantorAddress);
			tfGuarantorIdNumber.setText(guarantorIdNumber);
			tfGuarantorPhone1.setText(guarantorPhones.get(0));
			tfGuarantorPhone2.setText(guarantorPhones.get(1));
			tfGuarantorPhone3.setText(guarantorPhones.get(2));
			tfGuarantorPhone4.setText(guarantorPhones.get(3));
			
			carImage.setImage(new Image(strCarImage));
			clientIdImage.setImage(new Image(strClientIdImage));
			clientLicenseImage.setImage(new Image(strClientLicenseImage));
			guarantorIdImage.setImage(new Image(strGuarantorIdImage));
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    void getDeleteClient(MouseEvent event) throws SQLException, IOException {
    	Connection conn=DBinfo.connDB();
    	
    	//Delete from clients
		String strUpdate1 ="DELETE FROM `clients` WHERE `id` =?";  
		PreparedStatement ps1 = conn.prepareStatement(strUpdate1);
		ps1.setString(1, tfClientId.getText());
		ps1.executeUpdate();
		
		System.out.println("Delete from clients");
		
		//Delete from client_cars
		String strUpdate2 ="DELETE FROM `client_cars` WHERE `client_id` =?";  
		PreparedStatement ps2 = conn.prepareStatement(strUpdate2);
		ps2.setString(1, tfClientId.getText());
		ps2.executeUpdate();
		
		//Delete from client_phones
		String strUpdate3 ="DELETE FROM `client_phones` WHERE `client_id` =?";  
		PreparedStatement ps3 = conn.prepareStatement(strUpdate3);
		ps3.setString(1, tfClientId.getText());
		ps3.executeUpdate();
		
		//Delete from guarantors
		String strUpdate4 ="DELETE FROM `guarantors` WHERE `client_id` =?";  
		PreparedStatement ps4 = conn.prepareStatement(strUpdate4);
		ps4.setString(1, tfClientId.getText());
		ps4.executeUpdate();
		
		//Delete from guarantor_phones
		String strUpdate5 ="DELETE FROM `guarantor_phones` WHERE `guarantor_id` =?";  
		PreparedStatement ps5 = conn.prepareStatement(strUpdate5);
		ps5.setString(1, tfGuarantorId.getText());
		ps5.executeUpdate();
		
		//Show Success message after registration
		showSuccess();
		
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
    void getUpdatePayment(MouseEvent event) throws IOException {
    	
    	Stage stage=new Stage();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/ModifyPayment.fxml"));
		Parent root = loader.load();
		
		ModifyPaymentController modifyPaymentController = loader.getController();
		modifyPaymentController.setAmntRest(tfAmntRest.getText());
		modifyPaymentController.clientID = tfClientId.getText();
	
		Scene scene=new Scene(root,630,266);
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
    	
    }
}
