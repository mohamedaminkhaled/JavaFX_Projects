package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import net.connectcode.Code128Auto;

public class AddItemController {
	
    @FXML
    BorderPane borderAddMedicine;

    @FXML
    private TextField tfItemId;

    @FXML
    private TextField tfName;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnAdd;

    @FXML
    ComboBox<String> comboCategoryNames;

    @FXML
    private TextField tfQuantity;

    @FXML
    private TextField tfPrice;

    @FXML
    private TextField tfPurchasePrice;
    
    @FXML
    private TextField tfDiscount;
    
    String username;
    
    PurchaseCartController purchaseCartController;

    void cleareFields() {
    	tfItemId.setText("");
    	tfName.setText("");
    	comboCategoryNames.setValue(null);
    	tfQuantity.setText("");
    	tfPrice.setText("");
    	tfDiscount.setText("");
    	tfPurchasePrice.setText("");
    }
    
    @FXML
    void cancelAddMedicine(MouseEvent event) {
    	cleareFields();
    }
    
	@FXML
    void confirmAddMedicine(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerUserController = loader.getController();
    	  	
		System.out.println("*****>>>>"+username);
    	
    	//Error message for medicine id
    	if(tfItemId.getText().isEmpty()) {
    		registerUserController.showErr("Error! Please Enter Barcode");
    		return;
    	}
    	
    	Connection conn = null;
    	try {
			//ItemId can't be repeated in Database
			conn=DBinfo.connDB();
			Statement stat = conn.createStatement();
			String strSelectMedicine = "SELECT `id` FROM `items`";
			ResultSet rs = stat.executeQuery(strSelectMedicine);
			
			while(rs.next()) {
				if(tfItemId.getText().equals(rs.getString("id"))){
					registerUserController.showErr("Error! Â–« «·»«—ﬂÊœ „ÊÃÊœ »«·›⁄·");
					return;
				}
			}
			
//*************************************************************
			//Trial with only 3 items
			rs = stat.executeQuery(strSelectMedicine);
			int i = 0;
			while(rs.next()) {
				i++;
			}
			if(i > 2) {
				registerUserController.showErr("Error! Trial version with only 3 items");
				return;
			}
//*************************************************************

			//Error message for item name
			if(tfName.getText().isEmpty()) {
				registerUserController.showErr("Error! „‰ ›÷·ﬂ «œŒ· «”„ «·”·⁄Â");
				return;
			}
			
			//Set default discount
			if(tfDiscount.getText().isEmpty()) {
				tfDiscount.setText("0");
			}
			
			//Error message for company name
			if(comboCategoryNames.getValue() != null) {
	    		
	    	}else {
	    		registerUserController.showErr("Error! „‰ ›÷·ﬂ Õœœ «·’‰›");
	    		return;
	    	}
			
			try {
				String strSelectComID = "SELECT `id` FROM `categories` WHERE `name` = '"+comboCategoryNames.getValue()+"'";
				rs = stat.executeQuery(strSelectComID);
				rs.next();
				
				rs.getString("id");
				
			} catch (Exception e) {
				e.printStackTrace();
				registerUserController.showErr("Error! Â–« «·’‰› €Ì— „ÊÃÊœ");
	    		return;
			}
			
			//Error message for price
			if(tfPrice.getText().isEmpty()) {
				registerUserController.showErr("Error! „‰ ›÷·ﬂ Õœœ «·”⁄—");
				return;
			}
			
			String strQuantity = tfQuantity.getText().isEmpty() ? 
					"0" : tfQuantity.getText();
			
			try {
				Integer.parseInt(strQuantity);
				
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
				registerUserController.showErr("Error! Please enter numeric values for quantity.");
				return;
			}
			
			//After all validation tests above, Register the Medicine
			try {
				String sql="INSERT INTO `items`(`id`,`name`,`price`,`discount`,`quantity`,`category`) "
						 + "VALUES (?,?,?,?,?,?)";
				
				PreparedStatement ps = conn.prepareStatement(sql);

				ps.setString(1, tfItemId.getText());
				ps.setString(2, tfName.getText());
				ps.setString(3, tfPrice.getText());
				ps.setString(4, tfDiscount.getText());
				ps.setString(5, tfQuantity.getText());
				ps.setString(6, comboCategoryNames.getValue().toString());
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}			
			
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		} finally {
			conn.close();
		}
				
		showBarcode();
		
		//clear all fields
		cleareFields();
    }
	
	void showBarcode() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/ShowBarcode.fxml"));
    	Parent root = loader.load();
    	BarcodeController barcodeController = loader.getController();
    	
    	barcodeController.itemName.setText(tfName.getText());
    	barcodeController.code.setText(tfItemId.getText());
    	barcodeController.price.setText(tfPrice.getText());
    	barcodeController.discount.setText(tfDiscount.getText());
    	
    	//Set Bar code
    	Code128Auto code128 = new Code128Auto();
    	String strBarcode = code128.encode(tfItemId.getText());
    	barcodeController.barcode.setText(strBarcode);    	
    	
    	Scene scene = new Scene(root);
    	Stage primaryStage = new Stage();
		primaryStage .setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
    
	@FXML
    void confirmBuyMedicine(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerUserController = loader.getController();
    	
    	Date purchaceTime = new Date();
    	String time = String.valueOf(purchaceTime.getTime());
    	System.out.println("time>>>> "+purchaceTime.getTime());
    	
		System.out.println("username>>>> "+username);
    	
    	//Error message for medicine id
    	if(tfItemId.getText().isEmpty()) {
    		registerUserController.showErr("Error! „‰ ›÷·ﬂ «œŒ· «·»«— ﬂÊœ");
    		return;
    	}
    	
    	Connection conn = null;
    	try {
			//ItemId can't be repeated in Database
			conn=DBinfo.connDB();
			Statement stat = conn.createStatement();
			ResultSet rs;
			
			//Error message for item name
			if(tfName.getText().isEmpty()) {
				registerUserController.showErr("Error! „‰ ›÷·ﬂ «œŒ· «”„ «·”·⁄Â");
				return;
			}
			
			//Set default discount
			if(tfDiscount.getText().isEmpty()) {
				tfDiscount.setText("0");
			}
			
			//Error message for category name
			if(comboCategoryNames.getValue() != null) {
	    		
	    	}else {
	    		registerUserController.showErr("Error! „‰ ›÷·ﬂ Õœœ «·’‰›");
	    		return;
	    	}
			
			try {
				String strSelectComID = "SELECT `id` FROM `categories` WHERE `name` = '"+comboCategoryNames.getValue()+"'";
				rs = stat.executeQuery(strSelectComID);
				rs.next();
				
				rs.getString("id");
				
			} catch (Exception e) {
				e.printStackTrace();
				registerUserController.showErr("Error! Â–« «·’‰› €Ì— „ÊÃÊœ");
	    		return;
			}
			
			//Error message for price
			if(tfPrice.getText().isEmpty()) {
				registerUserController.showErr("Error! „‰ ›÷·ﬂ Õœœ «·”⁄—");
				return;
			}
			
			//Error message for purchase price
			if(tfPurchasePrice.getText().isEmpty()) {
				registerUserController.showErr("Error! „‰ ›÷·ﬂ Õœœ ”⁄— «·‘—«¡");
				return;
			}
			
			String strQuantity = tfQuantity.getText();
			
			try {
				Integer.parseInt(strQuantity);
				
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
				registerUserController.showErr("Error! Please enter numeric values for quantity.");
				return;
			}
			
			//Update count of items on Purchase Cart
			String strCount = purchaseCartController.countOfItems.getText();
			int intCount = Integer.parseInt(strCount);
			int intQuantity = Integer.parseInt(strQuantity);
			int newCount = intCount + intQuantity;
			purchaseCartController.countOfItems.setText(String.valueOf(newCount));
			
			//Display double with max two decimal digits
			DecimalFormat df = new DecimalFormat("#.##");
			
			//Update total price on Purchase Cart
			double doubleExistedPrice = Double.parseDouble(purchaseCartController.totalCoast.getText());
			double doublePrice = Double.parseDouble(tfPurchasePrice.getText());
			double doubleTotalCoast = doublePrice * intQuantity;
						
			try {
				 
				 double newTotalCoast = doubleExistedPrice + doubleTotalCoast;
				 String strNewTotalCoast = String.valueOf(df.format(newTotalCoast));
				 
				 purchaseCartController.totalCoast.setText(strNewTotalCoast);
				 
			} catch (NumberFormatException e) {
				e.printStackTrace();
				registerUserController.showErr("Error! Please enter numeric values.");
				return;
			}
			
			//add new item to cart
			Label lblName = new Label(tfName.getText());
			lblName.setPrefWidth(153);
			lblName.setStyle("-fx-font-size: 14; -fx-padding:0 0 0 4;");
			
			Label lblQuantity = new Label(tfQuantity.getText());
			lblQuantity.setPrefWidth(78);
			lblQuantity.setStyle("-fx-font-size: 14");
			
			Label lblItemPrice = new Label(tfPrice.getText());
			lblItemPrice.setPrefWidth(60);
			lblItemPrice.setStyle("-fx-font-size: 14");
			
			Label lblPrice = new Label(String.valueOf(df.format(doubleTotalCoast)));
			lblPrice.setPrefWidth(70);
			lblPrice.setStyle("-fx-font-size: 14");
			
			Label lblId = new Label(tfItemId.getText());
			lblId.setPrefWidth(100);
			lblId.setStyle("-fx-font-size: 14");
			
			Label lblCategory = new Label(comboCategoryNames.getValue());
			lblCategory.setPrefWidth(153);
			lblCategory.setStyle("-fx-font-size: 14");
			
			Label lblDiscount = new Label(tfDiscount.getText());
			lblDiscount.setPrefWidth(60);
			lblDiscount.setStyle("-fx-font-size: 14");
							
			HBox newItem = new HBox(10);
			
			newItem.getChildren().addAll(lblName, lblQuantity, lblItemPrice,
					 lblId, lblCategory, lblPrice, lblDiscount);
			
			//add new item to cart
			purchaseCartController.vboxCartItems.getChildren().add(newItem);
			
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
    	
    	showBarcode();
    	
		//clear all fields
		cleareFields();
    }
}
