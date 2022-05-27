package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    void getMedicineDetails(MouseEvent event) throws IOException {
    	
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
    
    void setMedicineCard(String id) throws IOException {
		
		try {
			String strSelect = "SELECT medicines.*, quantity.boxes AS VALID_BOXES, "
					+ "quantity.stripes AS VALID_STRIPES, quantity.units AS VALID_UNITS "
					+ "FROM medicines INNER JOIN quantity "
					+ "ON medicines.id = quantity.id "
					+ "WHERE medicines.ID = '"+id +"'";
			
			Connection conn=DBinfo.connDB();
			Statement state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ResultSet rs=state.executeQuery(strSelect);
			rs.last();
			
			//assign values to card attributes
			medicineName.setText(rs.getString("name"));
			boxes.setText(rs.getString("VALID_BOXES"));
			stripes.setText(rs.getString("VALID_STRIPES"));
			units.setText(rs.getString("VALID_UNITS"));
			medicinePrice.setText(rs.getString("price"));
			medicineDateManufact.setText(rs.getString("DOM"));
			medicineDateExpiary.setText(rs.getString("DOE"));
			medicineImage.setImage(new Image(rs.getString("image")));
			medicineID.setText(id);			
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    void getAddStock(MouseEvent event) throws IOException, SQLException {
    	
    	Stage stage=new Stage();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserPages/AddStock.fxml"));
		Parent root = loader.load();
		
		AddStockController addStockController = loader.getController();
		addStockController.medicineID = medicineID.getText();
		
		Connection conn  = DBinfo.connDB();
		
		//SELECT medicine type
		String strSelectType = "SELECT `type` FROM medicines WHERE `id` = '"+medicineID.getText()+"'";
		Statement statType  = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		ResultSet rsType  = statType .executeQuery(strSelectType);
		rsType .first();
		
		switch (rsType.getString("type")) {
		case "Lequid":{
			addStockController.tfStripes.setEditable(false);
			addStockController.tfUnits.setEditable(false);
		}
		break;
		
		case "Bags":{
			addStockController.tfStripes.setEditable(false);
		}
		break;

		default:
			break;
		}
		
		Scene scene=new Scene(root,630,266);
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
    }
    
    @FXML
    void getSellMedicine(MouseEvent event) throws IOException, SQLException {
		
		String strSelect = "SELECT medicines.type, quantity.boxes AS VALID_BOXES, "
				+ "quantity.stripes AS VALID_STRIPES "
				+ "FROM medicines INNER JOIN quantity "
				+ "ON medicines.id = quantity.id "
				+ "WHERE medicines.ID = '"+medicineID.getText()+"'";
		
		Connection conn=DBinfo.connDB();
		Statement state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		ResultSet rs=state.executeQuery(strSelect);
		rs.last();
		
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
	
			default:
				break;
		}
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserPages/SellStock.fxml"));
		Parent root = loader.load();
    	SellStockController sellStockController = loader.getController();
    	sellStockController.comboUnits.getItems().addAll(cmboMedicineTypes);
    	sellStockController.medicineID = medicineID.getText();
    	
    	Stage stage = new Stage();
    	Scene scene=new Scene(root,490,220);
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
    }
}
