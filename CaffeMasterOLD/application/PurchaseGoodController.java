package application;

import java.io.IOException;
import java.text.DecimalFormat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class PurchaseGoodController {
	
    @FXML
    BorderPane borderAddMedicine;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfSupplier;

    @FXML
    private Button btnCancel;

    @FXML
    private TextField tfQuantity;

    @FXML
    private TextField tfPrice;
    
    String username;
    
    PurchaseCartController purchaseCartController;

    void cleareFields() {
    	tfName.setText("");
    	tfSupplier.setText("");
    	tfQuantity.setText("");
    	tfPrice.setText("");
    }
    
    @FXML
    void cancelAddMedicine(MouseEvent event) {
    	cleareFields();
    }
    
	@FXML
    void confirmBuyMedicine(MouseEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerUserController = loader.getController();
		
    	try {
    		
    		//Error message for good name
        	if(tfName.getText().isEmpty()) {
        		registerUserController.showErr("Error! ãä ÝÖáß ÇßÊÈ ÇÓã ÇáÈÖÇÚå");
        		return;
        	}
        	
        	//Error message for supplier
        	if(tfSupplier.getText().isEmpty()) {
        		tfSupplier.setText("ÛíÑ ãÚÑæÝ");
        	}
        	
        	//Error message for quantity
        	if(tfQuantity.getText().isEmpty()) {
        		registerUserController.showErr("Error! ãä ÝÖáß ÇßÊÈ Çáßãíå");
        		return;
        	}
        	
        	//Error message for price
        	if(tfPrice.getText().isEmpty()) {
        		registerUserController.showErr("Error! ãä ÝÖáß ÇßÊÈ ÓÚÑ ÇáÈÖÇÚå");
        		return;
        	}
			
		    String itemPrice = tfPrice.getText();
		     
		    //Display double with max two decimal digits
			DecimalFormat df = new DecimalFormat("#.##");
    	
	    	//increase the count of cart items 
			String strCount = purchaseCartController.countOfItems.getText();
			int intCount = Integer.parseInt(strCount);
			int newCount = intCount + 1;
			purchaseCartController.countOfItems.setText(String.valueOf(newCount));
			
			//update total price
			double doublePrice = Double.parseDouble(itemPrice);
			
			//Calculate price
			String strTotal = purchaseCartController.totalCoast.getText();
			double doubleTotal = Double.parseDouble(strTotal);
			double newTotal = doubleTotal + doublePrice;
			String strNewTotal = String.valueOf(df.format(newTotal));
			purchaseCartController.totalCoast.setText(strNewTotal);
			
			//add new item to cart
			Label lblName = new Label(tfName.getText());
			lblName.setPrefWidth(153);
			lblName.setStyle("-fx-font-size: 14; -fx-padding:0 0 0 4;");
			
			Label lblQuantity = new Label(tfQuantity.getText());
			lblQuantity.setPrefWidth(57);
			lblQuantity.setStyle("-fx-font-size: 14");
			
			Label lblPrice = new Label(tfPrice.getText());
			lblPrice.setPrefWidth(75);
			lblPrice.setStyle("-fx-font-size: 14; -fx-padding:0 0 0 4;");
			
			Label lblSupplier = new Label(tfSupplier.getText());
			lblSupplier.setPrefWidth(153);
			lblSupplier.setStyle("-fx-font-size: 14; -fx-padding:0 0 0 4;");
			
			HBox newItem = new HBox(10);
			newItem.setPrefWidth(575);
			
			newItem.getChildren().addAll(lblName, lblQuantity, lblPrice, lblSupplier);
			
			Tooltip toolTipRemove = new Tooltip();
			toolTipRemove.setText("Remove");
			toolTipRemove.setFont(new Font(14));
			
			Tooltip.install(newItem, toolTipRemove);
			
			newItem.setOnMouseClicked(e->{
				//Remove item from the cart
				purchaseCartController.vboxCartItems.getChildren().remove(newItem);
				
				//Update cart items
				String strCount2 = purchaseCartController.countOfItems.getText();
				int intCount2 = Integer.parseInt(strCount2);
				int newCount2 = intCount2 - 1;
				purchaseCartController.countOfItems.setText(String.valueOf(newCount2));
				
				//Update total cost
				double doublePrice2 = Double.parseDouble(lblPrice.getText());
								
				//Calculate price
				String strTotal2 = purchaseCartController.totalCoast.getText();
				double doubleTotal2 = Double.parseDouble(strTotal2);
				double newTotal2 = doubleTotal2 - doublePrice2;
				String strNewTotal2 = String.valueOf(df.format(newTotal2));
				purchaseCartController.totalCoast.setText(strNewTotal2);
				
			});
			
			newItem.setOnMouseEntered(e->{
				newItem.setStyle("-fx-background-color: red");
			});
			
			newItem.setOnMouseExited(e->{
				newItem.setStyle(null);
			});
															
			//add new item to cart
			purchaseCartController.vboxCartItems.getChildren().add(newItem);
    	
    	} catch (NumberFormatException e) {
			e.printStackTrace();
    		registerUserController.showErr("Error! Plese Please enter numeric value");
    		return;
		}
    	
		//clear all fields
		cleareFields();
    }
}
