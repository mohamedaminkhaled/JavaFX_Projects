package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MedicineCardController {

    @FXML
    private BorderPane BorderPaneMedicineCard;

    @FXML
    private ImageView medicineImage;

    @FXML
    private Label medicineName;

    @FXML
    private Label medicinePrice;

    @FXML
    Button btnDelete;

    @FXML
    Button btnView;

    @FXML
    Button btnSell;
    
    @FXML
    Button btnAdd;
    
    @FXML
    Button btnPrice;

    @FXML
    private Label quantity;

    @FXML
    private Label medicineDateManufact;

    @FXML
    private Label medicineDateExpiary;
    
    @FXML
    private Label medicineID;
    
    @FXML
    private Label boxes;

    @FXML
    private Label stripes;

    @FXML
    private Label units;
    
    @FXML
    HBox hboxButtons;
        
    String employeeUsername;
    
    @FXML
    void getChangePrice(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
    	Stage stage=new Stage();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserPages/ChangePrice.fxml"));
		Parent root = loader.load();
		
		ChangePriceController changePriceController = loader.getController();
		
		changePriceController.setCurrentPrice(medicinePrice.getText());
		changePriceController.medicineID = medicineID.getText();
		
		Scene scene=new Scene(root);
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
    }
        
	@FXML
    void getMedicineDetails(MouseEvent event) throws IOException, ClassNotFoundException, Exception {
    	
    	Stage stage=new Stage();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserPages/MedicineDetails.fxml"));
		Parent root = loader.load();
		
		MedicineDetailsController medicineDetailsController = loader.getController();
		
		medicineDetailsController.setMedicineDetails(medicineID.getText());
		
		Scene scene=new Scene(root,710,467);
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
    }
    
    void setMedicineCard(String id) throws IOException, SQLException, ClassNotFoundException {
		
    	Connection conn = null;
		try {
			
			String strSelect1 = "SELECT medicines.*, quantity.boxes AS VALID_BOXES, "
					+ "quantity.stripes AS VALID_STRIPES, quantity.units AS VALID_UNITS, "
					+ "quantity.kg AS VALID_KG, "
					+ "quantity.gms AS VALID_GMS, quantity.cm AS VALID_CM "
					+ "FROM medicines INNER JOIN quantity "
					+ "ON medicines.id = quantity.id "
					+ "WHERE medicines.ID = '"+id+"'";
			
			conn=DBinfo.connDB();
			Statement state=conn.createStatement();
			ResultSet rs=state.executeQuery(strSelect1);
			rs.next();
			
			//assign values to card attributes
			medicineName.setText(rs.getString("name"));
			
			switch (rs.getString("type")) {		
				case "Powder":{
					boxes.setText(rs.getString("VALID_GMS"));			}
				break;
				
				case "Kgs":{
					boxes.setText(rs.getString("VALID_KG"));			}
				break;
				
				case "Cm":{
					boxes.setText(rs.getString("VALID_CM"));			}
				break;
		
				default:
					boxes.setText(rs.getString("VALID_BOXES"));
					break;
			}
						
			stripes.setText(rs.getString("VALID_STRIPES"));
			units.setText(rs.getString("VALID_UNITS"));
			medicinePrice.setText(rs.getString("price"));
			medicineDateManufact.setText(rs.getString("DOM"));
			medicineDateExpiary.setText(rs.getString("DOE"));
			medicineImage.setImage(new Image(rs.getString("image")));
			medicineID.setText(id);			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
    }
    
    @FXML
    void getAddStock(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {
    	
    	Stage stage=new Stage();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserPages/AddStock.fxml"));
		Parent root = loader.load();
		
		AddStockController addStockController = loader.getController();
		addStockController.medicineID = medicineID.getText();
		
		Connection conn  = DBinfo.connDB();
		
		//SELECT medicine type
		String strSelectType = "SELECT `type` FROM medicines WHERE `id` = '"+medicineID.getText()+"'";
		Statement statType  = conn.createStatement();
		ResultSet rsType  = statType .executeQuery(strSelectType);
		rsType .next();
		
		switch (rsType.getString("type")) {
			case "Tablets":{
				addStockController.tfKg.setEditable(false);
				addStockController.tfCm.setEditable(false);
				addStockController.tfGm.setEditable(false);
			}
			break;
		
			case "Lequid":{
				addStockController.tfStripes.setEditable(false);
				addStockController.tfUnits.setEditable(false);
				addStockController.tfKg.setEditable(false);
				addStockController.tfCm.setEditable(false);
				addStockController.tfGm.setEditable(false);
			}
			break;
			
			case "Bags":{
				addStockController.tfStripes.setEditable(false);
				addStockController.tfKg.setEditable(false);
				addStockController.tfCm.setEditable(false);
				addStockController.tfGm.setEditable(false);
			}
			break;
			
			case "Powder":{
				addStockController.tfStripes.setEditable(false);
				addStockController.tfUnits.setEditable(false);
				addStockController.tfBoxes.setEditable(false);
				addStockController.tfKg.setEditable(false);
				addStockController.tfCm.setEditable(false);
			}
			break;
			
			case "Kgs":{
				addStockController.tfStripes.setEditable(false);
				addStockController.tfUnits.setEditable(false);
				addStockController.tfBoxes.setEditable(false);
				addStockController.tfGm.setEditable(false);
				addStockController.tfCm.setEditable(false);
			}
			break;
			
			case "Cm":{
				addStockController.tfStripes.setEditable(false);
				addStockController.tfUnits.setEditable(false);
				addStockController.tfBoxes.setEditable(false);
				addStockController.tfGm.setEditable(false);
				addStockController.tfKg.setEditable(false);
			}
			break;
	
			default:
				break;
		}
		
		conn.close();
		
		Scene scene=new Scene(root,630,266);
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
    }
    
    @FXML
    void getSellMedicine(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {
		
		String strSelect = "SELECT medicines.type, quantity.boxes AS VALID_BOXES, "
				+ "quantity.stripes AS VALID_STRIPES "
				+ "FROM medicines INNER JOIN quantity "
				+ "ON medicines.id = quantity.id "
				+ "WHERE medicines.ID = '"+medicineID.getText()+"'";
		
		Connection conn=DBinfo.connDB();
		Statement state=conn.createStatement();
		ResultSet rs=state.executeQuery(strSelect);
		rs.next();
		
		int validBoxes = Integer.parseInt(rs.getString("VALID_BOXES"));
		int validStripes = Integer.parseInt(rs.getString("VALID_STRIPES"));

		ObservableList<String> cmboMedicineTypes = null;
		
		switch (rs.getString("type")) {
			case "Tablets":{
				if(validBoxes == 0) {
					if(validStripes == 0) {
						String[] arrTypesStripes = {"Units"};
						cmboMedicineTypes = FXCollections.observableArrayList(arrTypesStripes);
					}else {
						String[] arrTypes = {"Stripes","Units"};
						cmboMedicineTypes = FXCollections.observableArrayList(arrTypes);
					}
					
				}else {
					String[] arrTypes = {"Boxes","Stripes","Units"};
					cmboMedicineTypes = FXCollections.observableArrayList(arrTypes);
				}
			}
			break;
			
			case "Lequid":{
				String[] arrTypes = {"Boxes"};
				cmboMedicineTypes = FXCollections.observableArrayList(arrTypes);
			}
			break;
			
			case "Injection":{
				if(validBoxes == 0) {
					if(validStripes == 0) {
						String[] arrTypesStripes = {"Units"};
						cmboMedicineTypes = FXCollections.observableArrayList(arrTypesStripes);
					}else {
						String[] arrTypes = {"Stripes","Units"};
						cmboMedicineTypes = FXCollections.observableArrayList(arrTypes);
					}
					
				}else {
					String[] arrTypes = {"Boxes","Stripes","Units"};
					cmboMedicineTypes = FXCollections.observableArrayList(arrTypes);
				}
			}
			break;
			
			case "Bags":{
				if(validBoxes == 0) {
					String[] arrTypes = {"Units"};
					cmboMedicineTypes = FXCollections.observableArrayList(arrTypes);
				}else {
					String[] arrTypes = {"Boxes","Units"};
					cmboMedicineTypes = FXCollections.observableArrayList(arrTypes);
				}
			}
			break;
			
			case "Powder":{
				String[] arrTypes = {"gm"};
				cmboMedicineTypes = FXCollections.observableArrayList(arrTypes);
			}
			break;
			
			case "Kgs":{
				String[] arrTypes = {"kg"};
				cmboMedicineTypes = FXCollections.observableArrayList(arrTypes);
			}
			break;
			
			case "Cm":{
				String[] arrTypes = {"Cm"};
				cmboMedicineTypes = FXCollections.observableArrayList(arrTypes);
			}
			break;
	
			default:
				break;
		}
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserPages/SellStock.fxml"));
		Parent root = loader.load();
    	SellStockController sellStockController = loader.getController();
    	sellStockController.comboUnits.getItems().addAll(cmboMedicineTypes);
    	sellStockController.medicineID = medicineID.getText();
    	
    	conn.close();
    	
    	Stage stage = new Stage();
    	Scene scene=new Scene(root,490,220);
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
    }
}
