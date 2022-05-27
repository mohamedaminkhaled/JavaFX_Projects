package application;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AddMedicineController {
	
    @FXML
    BorderPane borderAddMedicine;
	
    @FXML
    private ImageView medicineImage;

    @FXML
    private Button btnImageChooser;

    @FXML
    private TextField tfMedicineID;

    @FXML
    private TextField tfMedicineName;

    @FXML
    private DatePicker datepickerManufact;

    @FXML
    private DatePicker datepickerExpire;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnAdd;

    @FXML
    ComboBox<String> comboSupplierNames;

    @FXML
    ComboBox<String> comboCompanyNames;

    @FXML
    private DatePicker datepickerBatch;
    
    @FXML
    ComboBox<String> comboType;
    
    @FXML
    private TextField tfStripsDetails;

    @FXML
    private TextField tfUnitDetails;

    @FXML
    private TextField tfStripes;

    @FXML
    private TextField tfUnits;

    @FXML
    private TextField tfBoxes;

    @FXML
    private TextField tfPrice;
    
    @FXML
    private TextField tfPurchasePrice;
    
    String username;
    
    PurchaseCartController purchaseCartController;

    void cleareFields() {
    	tfMedicineID.setText("");
    	tfMedicineName.setText("");
    	comboCompanyNames.setValue(null);
    	comboSupplierNames.setValue(null);
    	datepickerManufact.setValue(null);
    	datepickerExpire.setValue(null);
    	datepickerBatch.setValue(null);
    	comboType.setValue(null);
    	tfBoxes.setText("");
    	tfStripes.setText("");
    	tfUnits.setText("");
    	tfStripsDetails.setText("");
    	tfUnitDetails.setText("");
    	tfPrice.setText("");
    	tfPurchasePrice.setText("");
    	medicineImage.setImage(new Image("/resources/medicine1.jpg"));
    }
    
    @FXML
    void cancelAddMedicine(MouseEvent event) {
    	cleareFields();
    }
    
    @FXML
    void setSuppliers(ActionEvent event) throws SQLException {
    	String strSelectSuppliers = "SELECT `name` FROM `supplier` WHERE `COM_ID` = (SELECT `id` FROM companies WHERE `name` ='"+comboCompanyNames.getValue()+"')";
    	
    	//comboSupplierNames.setValue(null);
    	comboSupplierNames.getItems().clear();
    	
    	Connection conn=DBinfo.connDB();
		Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = state.executeQuery(strSelectSuppliers);
		
		ArrayList<String> listSuppliers = new ArrayList<>();

		while(rs.next()) {
			listSuppliers.add(rs.getString("name"));
		}
		ObservableList<String> cmboListSuppliers = FXCollections.observableArrayList(listSuppliers);
		
    	comboSupplierNames.getItems().addAll(cmboListSuppliers);

    }

    @SuppressWarnings("deprecation")
	@FXML
    void confirmAddMedicine(MouseEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerUserController = loader.getController();
    	  	
		System.out.println("*****>>>>"+username);
    	
    	//Error message for medicine id
    	if(tfMedicineID.getText().isEmpty()) {
    		registerUserController.showErr("Error! Please Enter Barcode");
    		return;
    	}
    	
    	try {
			//MedicineID can't be repeated in Database
			Connection conn=DBinfo.connDB();
			Statement stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			String strSelectMedicine = "SELECT `id` FROM `medicines`";
			ResultSet rs = stat.executeQuery(strSelectMedicine);
			
			while(rs.next()) {
				if(tfMedicineID.getText().equals(rs.getString("id"))){
					registerUserController.showErr("Error! This medicine already existed");
					return;
				}
			}
			
			//Error message for medicine name
			if(tfMedicineName.getText().isEmpty()) {
				registerUserController.showErr("Error! Please Enter Medicine Name");
				return;
			}
			
			//Error message for company name
			if(comboCompanyNames.getValue() != null) {
	    		
	    	}else {
	    		registerUserController.showErr("Error! Please Select Company");
	    		return;
	    	}
			
			//Error message for Supplier name
			if(comboSupplierNames.getValue() != null) {
	    		
	    	}else {
	    		registerUserController.showErr("Error! Please Select Supplier");
	    		return;
	    	}
			
			//Error message batch
			if(!(datepickerBatch.getValue() != null)) {
				registerUserController.showErr("Error! Please idenify batch");
				return;
			}
				
			//Error message for manufacturing date
			if(!(datepickerManufact.getValue() != null)) {
				registerUserController.showErr("Error! Please idenify Date of Manufacture");
				return;
			}
			
			//Error message expiry date
			if(!(datepickerExpire.getValue() != null)) {
				registerUserController.showErr("Error! Please idenify Date of Expiary");
				return;
			}
			
			//Error message expiry date
			if((datepickerExpire.getValue().isEqual(datepickerManufact.getValue()))
					|| (datepickerExpire.getValue().isBefore(datepickerManufact.getValue()))) {
				registerUserController.showErr("Error! Date of Expiary must be after Date of Manufacture");
				return;
			}
						
			//Error message for medicine types
			switch (comboType.getValue()) {
			case "Tablets":{
					if(tfBoxes.getText().isEmpty() && tfStripes.getText().isEmpty()
							&& tfUnits.getText().isEmpty()) {
						registerUserController.showErr("Error! Please idenify Quantity");
						return;
					}
					
					if(tfStripsDetails.getText().isEmpty() || tfStripsDetails.getText().isEmpty()) {
						registerUserController.showErr("Error! Please idenify Quantity Details");
						return;
					}
				}
				break;
			
			case "Lequid":{
				if(tfBoxes.getText().isEmpty()) {
					registerUserController.showErr("Error! Please idenify Quantity");
					return;
				}
			}
			break;
			
			case "Injection":{
				if(tfBoxes.getText().isEmpty() && tfStripes.getText().isEmpty()
						&& tfUnits.getText().isEmpty()) {
					registerUserController.showErr("Error! Please idenify Quantity");
					return;
				}
				
				if(tfStripsDetails.getText().isEmpty() || tfStripsDetails.getText().isEmpty()) {
					registerUserController.showErr("Error! Please idenify Quantity Details");
					return;
				}
			}
			break;
			
			case "Bags":{
				if(tfBoxes.getText().isEmpty() && tfUnits.getText().isEmpty()) {
					registerUserController.showErr("Error! Please idenify Quantity");
					return;
				}
				
				if(tfUnitDetails.getText().isEmpty()) {
					registerUserController.showErr("Error! Please idenify Quantity Details");
					return;
				}
				
				tfStripes.setEditable(false);
				tfStripsDetails.setEditable(false);
			}
			break;

			default:
				break;
			}
			
			//Error message for price
			if(tfPrice.getText().isEmpty()) {
				registerUserController.showErr("Error! Please idenify price");
				return;
			}
			
			//SELECT company ID
			String strSelectCombID = "SELECT `id` FROM companies WHERE `name` = '"+comboCompanyNames.getValue()+"'";
			rs = stat.executeQuery(strSelectCombID);
			rs.first();
			
			String selectedCompany = rs.getString("id");
			
			//SELECT supplier ID
			String strSelectSuppID = "SELECT `id` FROM supplier WHERE `name` = '"+comboSupplierNames.getValue()+"'";
			rs = stat.executeQuery(strSelectSuppID);
			rs.first();
			
			String selectedSupplier = rs.getString("id");
			
			//SELECT Employee ID
			String strSelectEmpID = "SELECT `id` FROM Employees WHERE `username` = '"+username+"'";
			rs = stat.executeQuery(strSelectEmpID);
			rs.first();
			
			String selectedEmployee = rs.getString("id");
			
			//After all validation tests above, Register the Medicine
			try {
				String sql="INSERT INTO `medicines`(`id`,`name`,`batch`, `type`,`STRIPES`,`UNITS`,"
						+ "`price`, `DOM`, `DOE`, `image`,`SUPPLIERID`,`EMP_ID`,`COM_ID`) "
						+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
				
				PreparedStatement ps = conn.prepareStatement(sql);

				ps.setString(1, tfMedicineID.getText());
				ps.setString(2, tfMedicineName.getText());
				ps.setString(3, datepickerBatch.getValue().toString());
				ps.setString(4, comboType.getValue());
				ps.setString(5, tfStripsDetails.getText().isEmpty() ? "0" : tfStripsDetails.getText());
				ps.setString(6, tfUnitDetails.getText().isEmpty() ? "0" : tfUnitDetails.getText());
				ps.setString(7, tfPrice.getText());
				ps.setString(8, datepickerManufact.getValue().toString());
				ps.setString(9, datepickerExpire.getValue().toString());
				ps.setString(10, medicineImage.getImage().impl_getUrl());
				ps.setString(11, selectedSupplier);
				ps.setString(12, selectedEmployee);
				ps.setString(13, selectedCompany);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			//Insert into quantity
			try {
				switch (comboType.getValue()) {
				case "Tablets":{
						String sqlTypes="INSERT INTO `quantity`(`ID`,`BOXES`,`STRIPES`, `UNITS`) "
							+ "VALUES (?,?,?,?)";
						PreparedStatement psTypes = conn.prepareStatement(sqlTypes);
						psTypes.setString(1, tfMedicineID.getText());
						psTypes.setString(2, tfBoxes.getText().isEmpty() ? "0" : tfBoxes.getText());
						psTypes.setString(3, tfStripes.getText().isEmpty() ? "0" : tfStripes.getText());
						psTypes.setString(4, tfUnits.getText().isEmpty() ? "0" : tfUnits.getText());
						psTypes.executeUpdate();
						
					}
					break;
				
				case "Lequid":{
					String sqlTypes="INSERT INTO `quantity`(`ID`,`BOXES`,`STRIPES`, `UNITS`) "
						+ "VALUES (?,?,?,?)";
					PreparedStatement psTypes = conn.prepareStatement(sqlTypes);
					psTypes.setString(1, tfMedicineID.getText());
					psTypes.setString(2, tfBoxes.getText().isEmpty() ? "0" : tfBoxes.getText());
					psTypes.setString(3, "0");
					psTypes.setString(4, "0");
					psTypes.executeUpdate();
					
				}
				break;
				
				case "Injection":{
					String sqlTypes="INSERT INTO `quantity`(`ID`,`BOXES`,`STRIPES`, `UNITS`) "
						+ "VALUES (?,?,?,?)";
					PreparedStatement psTypes = conn.prepareStatement(sqlTypes);
					psTypes.setString(1, tfMedicineID.getText());
					psTypes.setString(2, tfBoxes.getText().isEmpty() ? "0" : tfBoxes.getText());
					psTypes.setString(3, tfStripes.getText().isEmpty() ? "0" : tfStripes.getText());
					psTypes.setString(4, tfUnits.getText().isEmpty() ? "0" : tfUnits.getText());
					psTypes.executeUpdate();
					
				}
				break;
				
				case "Bags":{
					String sqlTypes="INSERT INTO `quantity`(`ID`,`BOXES`,`stripes`,`UNITS`) "
						+ "VALUES (?,?,?,?)";
					PreparedStatement psTypes = conn.prepareStatement(sqlTypes);
					psTypes.setString(1, tfMedicineID.getText());
					psTypes.setString(2, tfBoxes.getText().isEmpty() ? "0" : tfBoxes.getText());
					psTypes.setString(3, "0");
					psTypes.setString(4, tfUnits.getText().isEmpty() ? "0" : tfUnits.getText());
					psTypes.executeUpdate();
					
				}
				break;

				default:
					break;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		}
		
		//clear all fields
		cleareFields();
		
		//Show success message
		registerUserController.showSuccess();
    }
    
    @SuppressWarnings("deprecation")
	@FXML
    void confirmBuyMedicine(MouseEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerUserController = loader.getController();
    	
    	Date purchaceTime = new Date();
    	String time = String.valueOf(purchaceTime.getTime());
    	System.out.println("time>>>> "+purchaceTime.getTime());
    	
		System.out.println("username>>>> "+username);
    	
    	//Error message for medicine id
    	if(tfMedicineID.getText().isEmpty()) {
    		registerUserController.showErr("Error! Please Enter Barcode");
    		return;
    	}
    	
    	try {
			//MedicineID can't be repeated in Database
			Connection conn=DBinfo.connDB();
			Statement stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			String strSelectMedicine = "SELECT * FROM `medicines` WHERE `id` = '"+tfMedicineID.getText()+"'";
			ResultSet rs = stat.executeQuery(strSelectMedicine);
			rs.first();
			
			Boolean existed = false;
			if(rs.getRow() > 0) {
				existed = true;
			}
			
			//Error message for medicine name
			if(tfMedicineName.getText().isEmpty()) {
				registerUserController.showErr("Error! Please Enter Medicine Name");
				return;
			}
			
			//Error message for company name
			if(comboCompanyNames.getValue() != null) {
	    		
	    	}else {
	    		registerUserController.showErr("Error! Please Select Company");
	    		return;
	    	}
			
			//Error message for Supplier name
			if(comboSupplierNames.getValue() != null) {
	    		
	    	}else {
	    		registerUserController.showErr("Error! Please Select Supplier");
	    		return;
	    	}
			
			//Error message batch
			if(!(datepickerBatch.getValue() != null)) {
				registerUserController.showErr("Error! Please idenify batch");
				return;
			}
				
			//Error message for manufacturing date
			if(!(datepickerManufact.getValue() != null)) {
				registerUserController.showErr("Error! Please idenify Date of Manufacture");
				return;
			}
			
			//Error message expiry date
			if(!(datepickerExpire.getValue() != null)) {
				registerUserController.showErr("Error! Please idenify Date of Expiary");
				return;
			}
			
			//Error message expiry date
			if((datepickerExpire.getValue().isEqual(datepickerManufact.getValue()))
					|| (datepickerExpire.getValue().isBefore(datepickerManufact.getValue()))) {
				registerUserController.showErr("Error! Date of Expiary must be after Date of Manufacture");
				return;
			}
			
			//Error message for medicine types
			switch (comboType.getValue()) {
			case "Tablets":{
					if(tfBoxes.getText().isEmpty() && tfStripes.getText().isEmpty()
							&& tfUnits.getText().isEmpty()) {
						registerUserController.showErr("Error! Please idenify Quantity");
						return;
					}
					
					if(tfStripsDetails.getText().isEmpty() || tfStripsDetails.getText().isEmpty()) {
						registerUserController.showErr("Error! Please idenify Quantity Details");
						return;
					}
				}
				break;
			
			case "Lequid":{
				if(tfBoxes.getText().isEmpty()) {
					registerUserController.showErr("Error! Please idenify Quantity");
					return;
				}
			}
			break;
			
			case "Injection":{
				if(tfBoxes.getText().isEmpty() && tfStripes.getText().isEmpty()
						&& tfUnits.getText().isEmpty()) {
					registerUserController.showErr("Error! Please idenify Quantity");
					return;
				}
				
				if(tfStripsDetails.getText().isEmpty() || tfStripsDetails.getText().isEmpty()) {
					registerUserController.showErr("Error! Please idenify Quantity Details");
					return;
				}
			}
			break;
			
			case "Bags":{
				if(tfBoxes.getText().isEmpty() && tfUnits.getText().isEmpty()) {
					registerUserController.showErr("Error! Please idenify Quantity");
					return;
				}
				
				if(tfUnitDetails.getText().isEmpty()) {
					registerUserController.showErr("Error! Please idenify Quantity Details");
					return;
				}
			}
			break;

			default:
				break;
			}
			
			//Error message for price
			if(tfPrice.getText().isEmpty()) {
				registerUserController.showErr("Error! Please idenify price");
				return;
			}
			
			//Error message for price
			if(tfPurchasePrice.getText().isEmpty()) {
				registerUserController.showErr("Error! Please idenify Purchase price");
				return;
			}
			
			String strBoxes = tfBoxes.getText().isEmpty() ? 
					"0" : tfBoxes.getText();
			String strStripes = tfStripes.getText().isEmpty() ? 
					"0" : tfStripes.getText();
			String strUnits = tfUnits.getText().isEmpty() ? 
					"0" : tfUnits.getText();
			
			int intBoxes = 0, intStripes = 0, intUnits = 0;
			try {
				intBoxes = Integer.parseInt(strBoxes);
				intStripes = Integer.parseInt(strStripes);
				intUnits = Integer.parseInt(strUnits);
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			}
			
			int intQuantity = intBoxes + intStripes + intUnits;
			
			//Update count of items on Purchase Cart
			String strCount = purchaseCartController.countOfItems.getText();
			int intCount = Integer.parseInt(strCount);
			int newCount = intCount + intQuantity;
			purchaseCartController.countOfItems.setText(String.valueOf(newCount));
			
			//Display double with max two decimal digits
			DecimalFormat df = new DecimalFormat("#.##");
			
			//Update total price on Purchase Cart
			double doubleExistedPrice = Double.parseDouble(purchaseCartController.totalCoast.getText());
			
			int intBoxStripes = 0, intStripeUnits = 0;
			
			double doublePrice = 0.0, doubleStripPrice = 0.0, 
					doubleUnitPrice = 0.0, doubleTotalCoast = 0.0;
			double newTotalCoast = 0.0;
			String strNewTotalCoast = "";
			
			try {
				 String strBoxStripes = tfStripsDetails.getText().isEmpty() ? 
						 "0" : tfStripsDetails.getText();
				 String strStripeUnits = tfUnitDetails.getText().isEmpty() ? 
						 "0" : tfUnitDetails.getText();
				 
				 intBoxStripes = Integer.parseInt(strBoxStripes);
				 intStripeUnits = Integer.parseInt(strStripeUnits);
				 doublePrice = Double.parseDouble(tfPurchasePrice.getText());
				 
				 switch (comboType.getValue()) {
					case "Tablets":{
						doubleStripPrice = doublePrice / intBoxStripes;
						doubleUnitPrice = doubleStripPrice / intStripeUnits;
						
						doubleTotalCoast = doublePrice * intBoxes +
								doubleStripPrice * intStripes + 
								doubleUnitPrice * intUnits;
					}
					break;
					
					case "Lequid":{
						doubleTotalCoast = doublePrice * intBoxes;
					}
					break;
					
					case "Injection":{
						doubleStripPrice = doublePrice / intBoxStripes;
						doubleUnitPrice = doubleStripPrice / intStripeUnits;
						
						doubleTotalCoast = doublePrice * intBoxes +
								doubleStripPrice * intStripes + 
								doubleUnitPrice * intUnits;
					}
					break;
					
					case "Bags":{
						doubleUnitPrice = doublePrice / intStripeUnits;
						
						doubleTotalCoast = doublePrice * intBoxes +
								doubleUnitPrice * intUnits;
					}
					break;

					default:
						break;
				}
				 
				 newTotalCoast = doubleExistedPrice + doubleTotalCoast;
				 strNewTotalCoast = String.valueOf(df.format(newTotalCoast));
				 
				 purchaseCartController.totalCoast.setText(strNewTotalCoast);
				 
			} catch (NumberFormatException e) {
				e.printStackTrace();
				registerUserController.showErr("Error! Please enter numeric values.");
				return;
			}
			
			//add new item to cart
			Label lblName = new Label(tfMedicineName.getText());
			lblName.setPrefWidth(153);
			lblName.setStyle("-fx-font-size: 14; -fx-padding:0 0 0 4;");
			
			Label lblBoxes = new Label(String.valueOf(intBoxes));
			lblBoxes.setPrefWidth(78);
			lblBoxes.setStyle("-fx-font-size: 14");
			
			Label lblStripes = new Label(String.valueOf(intStripes));
			lblStripes.setPrefWidth(46);
			lblStripes.setStyle("-fx-font-size: 14");
			
			Label lblUnits = new Label(String.valueOf(intUnits));
			lblUnits.setPrefWidth(40);
			lblUnits.setStyle("-fx-font-size: 14");
			
			Label lblPrice = new Label(String.valueOf(df.format(doubleTotalCoast)));
			lblPrice.setPrefWidth(139);
			lblPrice.setStyle("-fx-font-size: 14; -fx-padding:0 0 0 4;");
				
			Label lblBoxPrice = new Label(tfPrice.getText());
			lblBoxPrice.setPrefWidth(139);
			lblBoxPrice.setStyle("-fx-font-size: 14; -fx-padding:0 0 0 4;");
			
			Label lblID = new Label(tfMedicineID.getText());
			lblID.setPrefWidth(186);
			lblID.setStyle("-fx-font-size: 14");
			
			Label lblType = new Label(comboType.getValue());
			lblType.setPrefWidth(103);
			lblType.setStyle("-fx-font-size: 14");
			
			Label lblBoxStripes = new Label(String.valueOf(intBoxStripes));
			lblBoxStripes.setPrefWidth(74);
			lblBoxStripes.setStyle("-fx-font-size: 14");
			
			Label lblStripeUnits = new Label(String.valueOf(intStripeUnits));
			lblStripeUnits.setPrefWidth(67);
			lblStripeUnits.setStyle("-fx-font-size: 14");
			
			Label lblCompany = new Label(comboCompanyNames.getValue());
			lblCompany.setPrefWidth(136);
			lblCompany.setStyle("-fx-font-size: 14");
			
			Label lblSupplier = new Label(comboSupplierNames.getValue());
			lblSupplier.setPrefWidth(153);
			lblSupplier.setStyle("-fx-font-size: 14");
			
			Label lblDOM = new Label(datepickerManufact.getValue().toString());
			lblDOM.setPrefWidth(153);
			lblDOM.setStyle("-fx-font-size: 14");
			
			Label lblDOE = new Label(datepickerExpire.getValue().toString());
			lblDOE.setPrefWidth(153);
			lblDOE.setStyle("-fx-font-size: 14");
			
			Label lblBatch = new Label(datepickerBatch.getValue().toString());
			lblBatch.setPrefWidth(153);
			lblBatch.setStyle("-fx-font-size: 14");
			
			Label lblImg = new Label(medicineImage.getImage().impl_getUrl());
			lblImg.setPrefWidth(171);
			lblImg.setStyle("-fx-font-size: 14");
							
			HBox newItem = new HBox(10);
			newItem.setPrefWidth(2040);
			
			newItem.getChildren().addAll(lblName, lblBoxes,lblStripes,
					lblUnits, lblPrice, lblBoxPrice, lblID, lblType, 
					lblBoxStripes, lblStripeUnits, lblCompany, 
					lblSupplier, lblDOM, lblDOE, lblBatch, lblImg);
			
			//add new item to cart
			purchaseCartController.vboxCartItems.getChildren().add(newItem);
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	
		//clear all fields
		cleareFields();
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
			 medicineImage.setImage(image);
		 }
    }
}
