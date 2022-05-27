package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PurchaseCartController {

    @FXML
    BorderPane borderCart;
	
	@FXML
    Label countOfItems;

    @FXML
    Label totalCoast;
    
    @FXML
    VBox vboxCartItems;
    
    static String username;
    
    static AdminController adminController;
    
    @FXML
    void getConfirmBill(MouseEvent event) throws IOException, SQLException {
    	Date date = new Date();
		double time = date.getTime();
    	
    	ArrayList<HBox> arrItems = new ArrayList<>();
    	
    	ArrayList<Node> arrNodes = new ArrayList<>();
    	arrNodes.addAll(vboxCartItems.getChildren());
    	for (Node node : arrNodes) {
    		arrItems.add((HBox) node);
		}
    	
    	int i = 0;
    	for (HBox hbox : arrItems) {
			//Skip first node 'header'
    		if(i == 0) {
				i++;
				continue;
			}else {
		    	ArrayList<Node> arrItemDetails = new ArrayList<>();
		    	arrItemDetails.addAll(hbox.getChildren());
		    	
		    	Label lblName = (Label) arrItemDetails.get(0);
		    	String strName = lblName.getText();
		    	
		    	Label lblBoxes = (Label) arrItemDetails.get(1);
		    	String strBoxes = lblBoxes.getText();
		    	int intBoxes = Integer.parseInt(strBoxes);
		    	
		    	Label lblStripes = (Label) arrItemDetails.get(2);
		    	String strStripes = lblStripes.getText();
		    	int intStripes = Integer.parseInt(strStripes);
		    	
		    	Label lblUnits = (Label) arrItemDetails.get(3);
		    	String strUnits = lblUnits.getText();
		    	int intUnits = Integer.parseInt(strUnits);
		    	
		    	Label lblPrice = (Label) arrItemDetails.get(4);
		    	String strPrice = lblPrice.getText();
		    	
		    	Label lblBoxPrice = (Label) arrItemDetails.get(5);
		    	String strBoxPrice = lblBoxPrice.getText();
		    	
		    	Label lblID = (Label) arrItemDetails.get(6);
		    	String strID = lblID.getText();
		    	
		    	Label lblType = (Label) arrItemDetails.get(7);
		    	String strType = lblType.getText();
		    	
		    	Label lblMedStripes = (Label) arrItemDetails.get(8);
		    	String strMedStripes = lblMedStripes.getText();
		    	
		    	Label lblMedUnits = (Label) arrItemDetails.get(9);
		    	String strMedUnits = lblMedUnits.getText();
		    	
		    	Label lblCompany = (Label) arrItemDetails.get(10);
		    	String strCompany = lblCompany.getText();
		    	
		    	Label lblSupplier = (Label) arrItemDetails.get(11);
		    	String strSupplier = lblSupplier.getText();
		    	
		    	Label lblDOM = (Label) arrItemDetails.get(12);
		    	String strDOM = lblDOM.getText();
		    	
		    	Label lblDOE = (Label) arrItemDetails.get(13);
		    	String strDOE = lblDOE.getText();
		    	
		    	Label lblBatch = (Label) arrItemDetails.get(14);
		    	String strBatch = lblBatch.getText();
		    	
		    	Label lblImg = (Label) arrItemDetails.get(15);
		    	String strImg = lblImg.getText();
		    	
		    	//SELECT supplier ID
				String strSelectSuppID = "SELECT `id` FROM supplier WHERE `name` = '"+strSupplier+"'";
				Connection connAddMedeicine = DBinfo.connDB();
				Statement statAddMedeicine = connAddMedeicine.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);;
				ResultSet rsAddMedeicine = statAddMedeicine.executeQuery(strSelectSuppID);
				rsAddMedeicine.first();
				
				String selectedSupplier = rsAddMedeicine.getString("id");
				
				//SELECT Employee ID
				String strSelectEmpID = "SELECT `id` FROM Employees WHERE `username` = '"+username+"'";
				rsAddMedeicine = statAddMedeicine.executeQuery(strSelectEmpID);
				rsAddMedeicine.first();
				
				String selectedEmployee = rsAddMedeicine.getString("id");
				
				//SELECT Company ID
				String strSelectCombID = "SELECT `id` FROM companies WHERE `name` = '"+strCompany+"'";
				rsAddMedeicine = statAddMedeicine.executeQuery(strSelectCombID);
				rsAddMedeicine.first();
				
				String selectedCompany = rsAddMedeicine.getString("id");
				
				//Set Bill number
		    	String billNumber = String.valueOf(date.getTime());
				
				//Check if this medicine existed
				String strSelectMedicine = "SELECT `id` FROM medicines WHERE `id` = '"+strID+"'";
				rsAddMedeicine = statAddMedeicine.executeQuery(strSelectMedicine);
				rsAddMedeicine.first();
				
				if(rsAddMedeicine.getRow() > 0) {
					//This medicine existed
										
					//SELECT medicine quantity
					String strSelectQuantity = "SELECT * FROM quantity WHERE `id` = '"+strID+"'";
					rsAddMedeicine = statAddMedeicine.executeQuery(strSelectQuantity);
					rsAddMedeicine.first();
					
					int intValidBoxes = rsAddMedeicine.getInt("BOXES");
					int intValidStripes = rsAddMedeicine.getInt("STRIPES");
					int intValidUnits = rsAddMedeicine.getInt("UNITS");
					
					int intNewValidBoxes = intBoxes + intValidBoxes;
					int intNewValidStripes = intStripes + intValidStripes;
					int intNewValidUnits = intUnits + intValidUnits;
					
					//Update medicine quantity
					String strUpdateQuantity ="UPDATE `quantity` set `boxes`=? , `stripes`=?, `units`=? WHERE `id` =?";  
					
					PreparedStatement psUpQuantity = connAddMedeicine.prepareStatement(strUpdateQuantity);
					psUpQuantity.setInt(1, intNewValidBoxes);
					psUpQuantity.setInt(2, intNewValidStripes);
					psUpQuantity.setInt(3, intNewValidUnits);
					psUpQuantity.setString(4, strID);
					psUpQuantity.executeUpdate();
					
					System.out.println("Update medicine quantity...");
					
					//Insert into purchases
			    	String sqlInsertPurchases = "INSERT INTO `purchases`(`NO`,`ID`,"
			    			+ "`BOXES`, `STRIPES`,`UNITS`,`SUPP_NAME`,`PRICE`,`EMP_USERNAME`) "
			    			+ "VALUES (?,?,?,?,?,?,?,?)";
			    	
			    	PreparedStatement psPurchases = connAddMedeicine.prepareStatement(sqlInsertPurchases);
			    	
			    	psPurchases.setString(1, billNumber);
			    	psPurchases.setString(2, strID);
			    	psPurchases.setString(3, strBoxes);
			    	psPurchases.setString(4, strStripes);
			    	psPurchases.setString(5, strUnits);
			    	psPurchases.setString(6, strSupplier);
			    	psPurchases.setString(7, strPrice);
			    	psPurchases.setString(8, username);
			    	psPurchases.executeUpdate();
			    	
			    	System.out.println("Inserted to purchases...");
					
				}else {
					//This medicine not existed
					
					//then insert it into medicines
					String sqlInsertMedicine = "INSERT INTO `medicines`(`id`,`name`,`batch`, `type`,`STRIPES`,`UNITS`,"
							+ "`price`, `DOM`, `DOE`, `image`,`SUPPLIERID`,`EMP_ID`,`COM_ID`) "
							+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
					PreparedStatement ps;
					ps = connAddMedeicine.prepareStatement(sqlInsertMedicine);
					
					ps.setString(1, strID);
					ps.setString(2, strName);
					ps.setString(3, strBatch);
					ps.setString(4, strType);
					ps.setString(5, strMedStripes);
					ps.setString(6, strMedUnits);
					ps.setString(7, strBoxPrice);
					ps.setString(8, strDOM);
					ps.setString(9, strDOE);
					ps.setString(10, strImg);
					ps.setString(11, selectedSupplier);
					ps.setString(12, selectedEmployee);
					ps.setString(13, selectedCompany);
					ps.executeUpdate();
					
					System.out.println("Inserted to medicines...");
					
					//Insert into quantity
					String sqlInsertQuantity = "INSERT INTO `quantity`(`ID`,`BOXES`,`STRIPES`, `UNITS`) "
							+ "VALUES (?,?,?,?)";
					PreparedStatement psQuantity = connAddMedeicine.prepareStatement(sqlInsertQuantity);
					psQuantity.setString(1, strID);
					psQuantity.setString(2, strBoxes);
					psQuantity.setString(3, strStripes);
					psQuantity.setString(4, strUnits);
					psQuantity.executeUpdate();
					
					System.out.println("Inserted to quantity...");
		
			    	//Insert into purchases
					String sqlInsertPurchases = "INSERT INTO `purchases`(`NO`,`ID`,"
			    			+ "`BOXES`, `STRIPES`,`UNITS`,`SUPP_NAME`,`PRICE`,`EMP_USERNAME`) "
			    			+ "VALUES (?,?,?,?,?,?,?,?)";
			    	
			    	PreparedStatement psPurchases = connAddMedeicine.prepareStatement(sqlInsertPurchases);
			    	
			    	psPurchases.setString(1, billNumber);
			    	psPurchases.setString(2, strID);
			    	psPurchases.setString(3, strBoxes);
			    	psPurchases.setString(4, strStripes);
			    	psPurchases.setString(5, strUnits);
			    	psPurchases.setString(6, strSupplier);
			    	psPurchases.setString(7, strPrice);
			    	psPurchases.setString(8, username);
			    	psPurchases.executeUpdate();
			    	
			    	System.out.println("Inserted to purchases...");
				}
			}
		}
    	
    	//clear the cart
    	vboxCartItems.getChildren().remove(1, arrNodes.size());
    	countOfItems.setText("0");
    	totalCoast.setText("0.0");
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PurchaseBill.fxml"));
    	Parent root = loader.load();
    	PurchaseBillController purchasebillController = loader.getController();
    	purchasebillController.setBill(time);
    	
    	Stage stage = new Stage();
    	Scene scene = new Scene(root, 643, 340 + ((arrNodes.size() - 1) * 30));
    	stage.setScene(scene);
    	stage.initStyle(StageStyle.TRANSPARENT);
 
    	stage.show();
    	
    }

}