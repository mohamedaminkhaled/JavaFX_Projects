package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
		    	
		    	Label lblQuantity = (Label) arrItemDetails.get(1);
		    	String strQuantity = lblQuantity.getText();
		    	
		    	Label lblPrice = (Label) arrItemDetails.get(2);
		    	String strPrice = lblPrice.getText();
		    	
		    	Label lblSupplier = (Label) arrItemDetails.get(3);
		    	String strSupplier = lblSupplier.getText();
		    	
				//Set Bill number
		    	String billNumber = String.valueOf(date.getTime());
				
				//then insert it into medicines
				String sqlInsertMedicine = "INSERT INTO `goods`(`id`,`name`,`supplier`, "
						+ "`quantity`,`cost`,`manager_username`) "
						+ "VALUES (?,?,?,?,?,?)";
				
				Date date2 = new Date();
				double itemId = date2.getTime();
				
				Connection connAddMedeicine = DBinfo.connDB();
				PreparedStatement ps = connAddMedeicine .prepareStatement(sqlInsertMedicine);
				
				ps.setDouble(1, itemId);
				ps.setString(2, strName);
				ps.setString(3, strSupplier);
				ps.setString(4, strQuantity);
				ps.setDouble(5, Double.parseDouble(strPrice));
				ps.setString(6, username);
				ps.executeUpdate();
				
				System.out.println("Inserted to goods...");
				
				//ex. 2020-06-25 21:28:26
				DateFormat dateFormate = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss a");
				String dateTimt = dateFormate.format(date);
				dateTimt = dateTimt.replace("/","-");
				
		    	//Insert into purchases
				String sqlInsertPurchases = "INSERT INTO `goods_purchses`(`bill_number`, `time`, `quantity`,"
		    			+ "`cost`, `good_name`,`emp_username`, `supplier`) "
		    			+ "VALUES (?,?,?,?,?,?,?)";
		    	
		    	PreparedStatement psPurchases = connAddMedeicine.prepareStatement(sqlInsertPurchases);
		    	
		    	psPurchases.setDouble(1, Double.parseDouble(billNumber));
		    	psPurchases.setString(2, dateTimt);
		    	psPurchases.setString(3, strQuantity);
		    	psPurchases.setDouble(4, Double.parseDouble(strPrice));
		    	psPurchases.setString(5, strName);
		    	psPurchases.setString(6, username);
		    	psPurchases.setString(7, strSupplier);
		    	psPurchases.executeUpdate();
		    	
		    	System.out.println("Inserted to goods_purchases...");
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
    	Scene scene = new Scene(root, 205, 417 + ((arrNodes.size() - 1) * 30));
    	stage.setScene(scene);
    	stage.initStyle(StageStyle.TRANSPARENT);
    	
    	stage.show();
    	
    }

}