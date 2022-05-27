package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    void getConfirmBill(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {
    	Date date = new Date();
		double time = date.getTime();
		
		//Set Bill number
    	String billNumber = String.valueOf(date.getTime());
		
    	ArrayList<HBox> arrItems = new ArrayList<>();
    	
    	ArrayList<Node> arrNodes = new ArrayList<>();
    	arrNodes.addAll(vboxCartItems.getChildren());
    	for (Node node : arrNodes) {
    		arrItems.add((HBox) node);
		}
    	
    	Connection connAddMedeicine = null;
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
		    	
		    	Label lblKg = (Label) arrItemDetails.get(4);
		    	String strKg = lblKg.getText();
		    	double kgs = Double.parseDouble(strKg);
		    	
		    	Label lblGm = (Label) arrItemDetails.get(5);
		    	String strGm = lblGm.getText();
		    	int intGm = Integer.parseInt(strGm);
		    	
		    	Label lblCm = (Label) arrItemDetails.get(6);
		    	String strCm = lblCm.getText();
		    	int intCm = Integer.parseInt(strCm);
		    	
		    	Label lblPrice = (Label) arrItemDetails.get(7);
		    	String strPrice = lblPrice.getText();
		    	
		    	Label lblBoxPrice = (Label) arrItemDetails.get(8);
		    	String strBoxPrice = lblBoxPrice.getText();
		    	
		    	Label lblID = (Label) arrItemDetails.get(9);
		    	String strID = lblID.getText();
		    	
		    	Label lblType = (Label) arrItemDetails.get(10);
		    	String strType = lblType.getText();
		    	
		    	Label lblMedStripes = (Label) arrItemDetails.get(11);
		    	String strMedStripes = lblMedStripes.getText();
		    	
		    	Label lblMedUnits = (Label) arrItemDetails.get(12);
		    	String strMedUnits = lblMedUnits.getText();
		    	
		    	Label lblCompany = (Label) arrItemDetails.get(13);
		    	String strCompany = lblCompany.getText();
		    	
		    	Label lblSupplier = (Label) arrItemDetails.get(14);
		    	String strSupplier = lblSupplier.getText();
		    	
		    	Label lblDOM = (Label) arrItemDetails.get(15);
		    	String strDOM = lblDOM.getText();
		    	
		    	Label lblDOE = (Label) arrItemDetails.get(16);
		    	String strDOE = lblDOE.getText();
		    	
		    	Label lblBatch = (Label) arrItemDetails.get(17);
		    	String strBatch = lblBatch.getText();
		    	
		    	Label lblImg = (Label) arrItemDetails.get(18);
		    	String strImg = lblImg.getText();
		    	
		    	//SELECT supplier ID
				String strSelectSuppID = "SELECT `id` FROM supplier WHERE `name` = '"+strSupplier+"'";
				connAddMedeicine = DBinfo.connDB();
				Statement statAddMedeicine = connAddMedeicine.createStatement();
				ResultSet rsAddMedeicine = statAddMedeicine.executeQuery(strSelectSuppID);
				rsAddMedeicine.next();
				
				String selectedSupplier = rsAddMedeicine.getString("id");
				
				//SELECT Employee ID
				String strSelectEmpID = "SELECT `id` FROM Employees WHERE `username` = '"+username+"'";
				rsAddMedeicine = statAddMedeicine.executeQuery(strSelectEmpID);
				rsAddMedeicine.next();
				
				String selectedEmployee = rsAddMedeicine.getString("id");
				
				//SELECT Company ID
				String strSelectCombID = "SELECT `id` FROM companies WHERE `name` = '"+strCompany+"'";
				rsAddMedeicine = statAddMedeicine.executeQuery(strSelectCombID);
				rsAddMedeicine.next();
				
				String selectedCompany = rsAddMedeicine.getString("id");
				
				//Check if this medicine existed
				String strSelectMedicine = "SELECT `id` FROM medicines WHERE `id` = '"+strID+"'";
				rsAddMedeicine = statAddMedeicine.executeQuery(strSelectMedicine);
				rsAddMedeicine.next();
				
				if(rsAddMedeicine.getRow() > 0) {
					//This medicine existed
										
					//SELECT medicine quantity
					String strSelectQuantity = "SELECT * FROM quantity WHERE `id` = '"+strID+"'";
					rsAddMedeicine = statAddMedeicine.executeQuery(strSelectQuantity);
					rsAddMedeicine.next();
					
					int intValidBoxes = rsAddMedeicine.getInt("BOXES");
					int intValidStripes = rsAddMedeicine.getInt("STRIPES");
					int intValidUnits = rsAddMedeicine.getInt("UNITS");
					
					String strValidKg = rsAddMedeicine.getString("kg");
					double validKg = Double.parseDouble(strValidKg);
					int intValidCm = rsAddMedeicine.getInt("cm");
					int intValidGm = rsAddMedeicine.getInt("gms");
					
					int intNewValidBoxes = intBoxes + intValidBoxes;
					int intNewValidStripes = intStripes + intValidStripes;
					int intNewValidUnits = intUnits + intValidUnits;
					
					double newValidKg = validKg + kgs;
					String strNewValidKg = String.valueOf(newValidKg);
					int intNewValidCm = intValidCm + intCm;
					int intNewValidGm = intValidGm + intGm;
					
					//Update medicine quantity
					String strUpdateQuantity ="UPDATE `quantity` set `boxes`=? , `stripes`=?, "
							+ "`units`=?, `kg`=?, `gms`=?, `cm`=? WHERE `id` =?";  
					
					PreparedStatement psUpQuantity = connAddMedeicine.prepareStatement(strUpdateQuantity);
					psUpQuantity.setInt(1, intNewValidBoxes);
					psUpQuantity.setInt(2, intNewValidStripes);
					psUpQuantity.setInt(3, intNewValidUnits);
					psUpQuantity.setString(4, strNewValidKg);
					psUpQuantity.setInt(5, intNewValidGm);
					psUpQuantity.setInt(6, intNewValidCm);
					psUpQuantity.setString(7, strID);
					psUpQuantity.executeUpdate();
					
					System.out.println("Update medicine quantity...");
					
					//ex. 2021-01-19 01:28:26
					DateFormat dateFormate = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss a");
					String dateTimt = dateFormate.format(date);
					dateTimt = dateTimt.replace("/","-");
					
					//**************************************
					
					String strSelect = "SELECT * FROM `clients` "
							+ "WHERE `name` = '"+strSupplier+"'";
					
					rsAddMedeicine = statAddMedeicine.executeQuery(strSelect);
					
					String strBalance = null;
					double newBalance = 0.0;
					double newLoan = 0.0;
					
					if(rsAddMedeicine.next()) {
						strBalance = rsAddMedeicine.getString("balance");
						String strLoane = rsAddMedeicine.getString("loan");
						
						double balance = Double.parseDouble(strBalance);
						double loan = Double.parseDouble(strLoane);
						double billTotal = Double.parseDouble(strPrice);
						
						double diffLoan = loan - billTotal;
						
						newBalance = balance;
						
						if(diffLoan < 0) {
							newBalance = balance + (diffLoan * (-1));
						}else {
							newLoan = loan - billTotal;
						}
					}
					
					//****************************************
					
					//Insert into purchases
			    	String sqlInsertPurchases = "INSERT INTO `purchases`(`NO`,`ID`,"
			    			+ "`BOXES`, `STRIPES`,`UNITS`,`kg`,`gms`,`cm`,`SUPP_NAME`,`PRICE`,"
			    			+ "`TIME`,`EMP_USERNAME`,`client_balance`,`client_debit`) "
			    			+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			    	
			    	PreparedStatement psPurchases = connAddMedeicine.prepareStatement(sqlInsertPurchases);
			    	
			    	psPurchases.setString(1, billNumber);
			    	psPurchases.setString(2, strID);
			    	psPurchases.setString(3, strBoxes);
			    	psPurchases.setString(4, strStripes);
			    	psPurchases.setString(5, strUnits);
			    	psPurchases.setString(6, strKg);
			    	psPurchases.setString(7, strGm);
			    	psPurchases.setString(8, strCm);
			    	psPurchases.setString(9, strSupplier);
			    	psPurchases.setString(10, strPrice);
			    	psPurchases.setString(11, dateTimt);
			    	psPurchases.setString(12, username);
			    	psPurchases.setString(13, String.valueOf(newBalance));
			    	psPurchases.setString(14, String.valueOf(newLoan));
			    	psPurchases.executeUpdate();
			    	
			    	System.out.println("Inserted to purchases...");
					
				}else {
					//This medicine not existed

//*************************************************************
					//Trial with only 2 medicines
					Connection conn = DBinfo.connDB();
					Statement stat = conn.createStatement();
					String str = "SELECT `id` FROM `medicines`";
					ResultSet rs = stat.executeQuery(str);
					
					int i2 = 0;
					while(rs.next()) {
						i2++;
					}
					if(i2 > 1) {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
				    	Parent root = loader.load();
				    	RegisterUserController registerUserController = loader.getController();
				    	  
						registerUserController.showErr("Error! Trial version with only 2 medicines");
						return;
					}
//*************************************************************

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
					String sqlInsertQuantity = "INSERT INTO `quantity`(`ID`,`BOXES`,`STRIPES`, `UNITS`, "
							+ "`kg`, `gms`, `cm`) "
							+ "VALUES (?,?,?,?,?,?,?)";
					PreparedStatement psQuantity = connAddMedeicine.prepareStatement(sqlInsertQuantity);
					psQuantity.setString(1, strID);
					psQuantity.setString(2, strBoxes);
					psQuantity.setString(3, strStripes);
					psQuantity.setString(4, strUnits);
					psQuantity.setString(5, strKg);
					psQuantity.setString(6, strGm);
					psQuantity.setString(7, strCm);
					psQuantity.executeUpdate();
					
					System.out.println("Inserted to quantity...");
					
					//ex. 2020-06-25 21:28:26
					DateFormat dateFormate = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss a");
					String dateTimt = dateFormate.format(date);
					dateTimt = dateTimt.replace("/","-");
					
					//**************************************
					
					String strSelect = "SELECT * FROM `clients` "
							+ "WHERE `name` = '"+strSupplier+"'";
					
					rsAddMedeicine = statAddMedeicine.executeQuery(strSelect);
					
					String strBalance = null;
					double newBalance = 0.0;
					double newLoan = 0.0;
					
					if(rsAddMedeicine.next()) {
						strBalance = rsAddMedeicine.getString("balance");
						String strLoane = rsAddMedeicine.getString("loan");
						
						double balance = Double.parseDouble(strBalance);
						double loan = Double.parseDouble(strLoane);
						double billTotal = Double.parseDouble(strPrice);
						
						double diffLoan = loan - billTotal;
						
						newBalance = balance;
						
						if(diffLoan < 0) {
							newBalance = balance + (diffLoan * (-1));
						}else {
							newLoan = loan - billTotal;
						}
					}
										
					//****************************************
					
					//Insert into purchases
			    	String sqlInsertPurchases = "INSERT INTO `purchases`(`NO`,`ID`,"
			    			+ "`BOXES`, `STRIPES`,`UNITS`,`kg`,`gms`,`cm`,`SUPP_NAME`,`PRICE`,"
			    			+ "`TIME`,`EMP_USERNAME`,`client_balance`,`client_debit`) "
			    			+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			    	
			    	PreparedStatement psPurchases = connAddMedeicine.prepareStatement(sqlInsertPurchases);
			    	
			    	psPurchases.setString(1, billNumber);
			    	psPurchases.setString(2, strID);
			    	psPurchases.setString(3, strBoxes);
			    	psPurchases.setString(4, strStripes);
			    	psPurchases.setString(5, strUnits);
			    	psPurchases.setString(6, strKg);
			    	psPurchases.setString(7, strGm);
			    	psPurchases.setString(8, strCm);
			    	psPurchases.setString(9, strSupplier);
			    	psPurchases.setString(10, strPrice);
			    	psPurchases.setString(11, dateTimt);
			    	psPurchases.setString(12, username);
			    	psPurchases.setString(13, String.valueOf(newBalance));
			    	psPurchases.setString(14, String.valueOf(newLoan));
			    	psPurchases.executeUpdate();
			    	
			    	System.out.println("Inserted to purchases...");
				}
			
				try {
					Statement state = connAddMedeicine.createStatement();
					
					String strSelect = "SELECT * FROM `clients` "
							+ "WHERE `name` = '"+strSupplier+"'";
					
					ResultSet rs = state.executeQuery(strSelect);
					
					String strBalance = null;
					if(rs.next()) {
						strBalance = rs.getString("balance");
						String strLoane = rs.getString("loan");
						
						double balance = Double.parseDouble(strBalance);
						double loan = Double.parseDouble(strLoane);
						double billTotal = Double.parseDouble(strPrice);
						
						double diffLoan = loan - billTotal;
						
						double newBalance = balance, newLoan = 0.0;
						
						if(diffLoan < 0) {
							newBalance = balance + (diffLoan * (-1));
						}else {
							newLoan = loan - billTotal;
						}
						
						System.out.println("diffLoan: "+diffLoan);
						System.out.println("newBalance: "+newBalance);
						System.out.println("newLoan: "+newLoan);
						
						String strUpdateQuantity = "UPDATE `clients` set `balance`=?, `loan`=? WHERE `name` =?";  
						
						PreparedStatement psUpQuantity = connAddMedeicine.prepareStatement(strUpdateQuantity);
						psUpQuantity.setString(1, String.valueOf(newBalance));
						psUpQuantity.setString(2, String.valueOf(newLoan));
						psUpQuantity.setString(3, strSupplier);
						psUpQuantity.executeUpdate();
					}
										
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
    	}
    	
    	connAddMedeicine.close();
    	
    	//clear the cart
    	vboxCartItems.getChildren().remove(1, arrNodes.size());
    	countOfItems.setText("0");
    	totalCoast.setText("0.0");
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PurchaseBill.fxml"));
    	Parent root = loader.load();
    	PurchaseBillController purchasebillController = loader.getController();
    	purchasebillController.setBill(billNumber);
    	
    	Stage stage = new Stage();
    	Scene scene = new Scene(root, 595, 340 + ((arrNodes.size() - 1) * 30));
    	stage.setScene(scene);
    	stage.initStyle(StageStyle.TRANSPARENT);
 
    	stage.show();
    }
}