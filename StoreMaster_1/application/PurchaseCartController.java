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
    	
    	Connection connSelectItem = null;
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
		    	
		    	Label lblQuantity = (Label) arrItemDetails.get(1);
		    	String strQuantity = lblQuantity.getText();
		    	int intQuantity = Integer.parseInt(strQuantity);
		    	
		    	Label lblItemPrice = (Label) arrItemDetails.get(2);
		    	String strItemPrice = lblItemPrice.getText();
		    			    	
		    	Label lblID = (Label) arrItemDetails.get(3);
		    	String strID = lblID.getText();
		    	
		    	Label lblCategory = (Label) arrItemDetails.get(4);
		    	String strCategory = lblCategory.getText();
		    	
		    	Label lblPrice = (Label) arrItemDetails.get(5);
		    	String strPrice = lblPrice.getText();
		    	
		    	Label lblDiscount = (Label) arrItemDetails.get(6);
		    	String strDiscount = lblDiscount.getText();
		    					
				//Check if this item existed
				String strSelectItem = "SELECT `id` FROM `items` WHERE `id` = '"+strID+"'";
				connSelectItem = DBinfo.connDB();
				Statement stateItem = connSelectItem.createStatement();
				ResultSet rsItems = stateItem.executeQuery(strSelectItem);
				rsItems.next();
				
				
				if(rsItems.getRow() > 0) {
					//This item existed
										
					//SELECT item quantity
					String strSelectQuantity = "SELECT * FROM `items` WHERE `id` = '"+strID+"'";
					rsItems = stateItem.executeQuery(strSelectQuantity);
					rsItems.next();
					
					int intValidQuantity = rsItems.getInt("quantity");
										
					int intNewValidQuantity = intQuantity + intValidQuantity;
					
					//Update item quantity
					String strUpdateQuantity ="UPDATE `items` set `quantity`=? WHERE `id` =?";  
					
					PreparedStatement psUpQuantity = connSelectItem.prepareStatement(strUpdateQuantity);
					psUpQuantity.setInt(1, intNewValidQuantity);
					psUpQuantity.setString(2, strID);
					psUpQuantity.executeUpdate();
					
					System.out.println("Update item quantity...");
					
					//ex. 2021-01-19 01:28:26
					DateFormat dateFormate = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss a");
					String dateTimt = dateFormate.format(date);
					dateTimt = dateTimt.replace("/","-");
					
					//Insert into purchases
			    	String sqlInsertPurchases = "INSERT INTO `purchases`(`NO`,`ID`,"
			    			+ "`quantity`,`PRICE`,`TIME`,`EMP_USERNAME`) "
			    			+ "VALUES (?,?,?,?,?,?)";
			    	
			    	PreparedStatement psPurchases = connSelectItem.prepareStatement(sqlInsertPurchases);
			    	
			    	psPurchases.setString(1, billNumber);
			    	psPurchases.setString(2, strID);
			    	psPurchases.setString(3, strQuantity);
			    	psPurchases.setString(4, strPrice);
			    	psPurchases.setString(5, dateTimt);
			    	psPurchases.setString(6, username);
			    	psPurchases.executeUpdate();
			    	
			    	System.out.println("Inserted to purchases...");
					
				}else {
					//This medicine not existed

//*************************************************************
//					//Trial with only 2 medicines
//					Connection conn = DBinfo.connDB();
//					Statement stat = conn.createStatement();
//					String str = "SELECT `id` FROM `items`";
//					ResultSet rs = stat.executeQuery(str);
//					
//					int i2 = 0;
//					while(rs.next()) {
//						i2++;
//					}
//					if(i2 > 1) {
//						FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
//				    	Parent root = loader.load();
//				    	RegisterUserController registerUserController = loader.getController();
//				    	  
//						registerUserController.showErr("Error! Trial version with only 2 medicines");
//						return;
//					}
//*************************************************************

					//Insert into items
					String sql="INSERT INTO `items`(`id`,`name`,`price`,`discount`,`quantity`,`category`) "
							 + "VALUES (?,?,?,?,?,?)";
					
					PreparedStatement ps = connSelectItem.prepareStatement(sql);

					ps.setString(1, strID);
					ps.setString(2, strName);
					ps.setString(3, strItemPrice);
					ps.setString(4, strDiscount);
					ps.setString(5, strQuantity);
					ps.setString(6, strCategory);
					ps.executeUpdate();
					
					System.out.println("Inserted to items...");
					
					//ex. 2020-06-25 21:28:26
					DateFormat dateFormate = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss a");
					String dateTimt = dateFormate.format(date);
					dateTimt = dateTimt.replace("/","-");
					
					//Insert into purchases
					String sqlInsertPurchases = "INSERT INTO `purchases`(`NO`,`ID`,"
			    			+ "`quantity`,`PRICE`,`TIME`,`EMP_USERNAME`) "
			    			+ "VALUES (?,?,?,?,?,?)";
			    	
			    	PreparedStatement psPurchases = connSelectItem.prepareStatement(sqlInsertPurchases);
			    	
			    	psPurchases.setString(1, billNumber);
			    	psPurchases.setString(2, strID);
			    	psPurchases.setString(3, strQuantity);
			    	psPurchases.setString(4, strPrice);
			    	psPurchases.setString(5, dateTimt);
			    	psPurchases.setString(6, username);
			    	psPurchases.executeUpdate();
			    	
			    	System.out.println("Inserted to purchases...");
				}
			}
    	}
    	
    	connSelectItem.close();
    	
    	//clear the cart
    	vboxCartItems.getChildren().remove(1, arrNodes.size());
    	countOfItems.setText("0");
    	totalCoast.setText("0.0");
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PurchaseBill.fxml"));
    	Parent root = loader.load();
    	PurchaseBillController purchasebillController = loader.getController();
    	purchasebillController.setBill(billNumber);
    	
    	Stage stage = new Stage();
    	Scene scene = new Scene(root, 252, 516 + ((arrNodes.size() - 1) * 30));
    	stage.setScene(scene);
    	stage.initStyle(StageStyle.TRANSPARENT);
 
    	stage.show();
    }
}