package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SellStockController {

    @FXML
    private BorderPane addStockPage;

    @FXML
    private TextField quantity;

    @FXML
    ComboBox<String> comboUnits;
    
    static String jobtitle = null;
    String itemName = null;
    String employeeUsername = null;
   
    static ClientController clientController;
    static CartController cartController;
    
    double x, y;
    
    @FXML
    void close(MouseEvent event) {
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	stage.close();
    }
    
    @FXML
    void pressed(MouseEvent event) {
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	x = event.getScreenX() - stage.getX();
    	y = event.getScreenY() - stage.getY();
    }
    
    @FXML
    void dragged(MouseEvent event) {
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	stage.setX(event.getScreenX() -x);
    	stage.setY(event.getScreenY() -y);
    }
    
    @FXML
    void confirmSellStock(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {
    	if(quantity.getText().equals("")) {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
        	Parent root = loader.load();
        	RegisterUserController registerUserController = loader.getController();
    		registerUserController.showErr("Error! ãä ÝÖáß ÍÏÏ ÇáÚÏÏ");
    		return;
    	}
    	
    	if(comboUnits.getValue() != null) {
    		
    	}else {
    		comboUnits.setValue("0");
    	}
    	
    	Connection conn = null;	
		try {
			
			String strSelect = "SELECT * FROM items WHERE `name` = '"+itemName+"'";
			conn=DBinfo.connDB();
			Statement state=conn.createStatement();
			ResultSet rs=state.executeQuery(strSelect);
			rs.next();
			
		    String itemPrice = rs.getString("price");
		     
		    //Display double with max two decimal digits
			DecimalFormat df = new DecimalFormat("#.##");
		    			
			if(jobtitle == "user") {
				//increase the count of cart items 
				int intQuantity = Integer.parseInt(quantity.getText());
				String strCount = clientController.countOfItems.getText();
				int intCount = Integer.parseInt(strCount);
				int newCount = intCount + intQuantity;
				clientController.countOfItems.setText(String.valueOf(newCount));
				
				//update total price
				double doublePrice = Double.parseDouble(itemPrice);
				double newAmnt = 0.0;
				newAmnt = doublePrice * intQuantity;
				
				String strDiscount = comboUnits.getValue();
				int intDiscount = Integer.parseInt(strDiscount);
				
				double discVal = newAmnt * intDiscount / 100;
				
				newAmnt = newAmnt - discVal;
				
				//Calculate price
				String strTotal = clientController.totalPrice.getText();
				double doubleTotal = Double.parseDouble(strTotal);
				double newTotal = doubleTotal + newAmnt;
				String strNewTotal = String.valueOf(df.format(newTotal));
				clientController.totalPrice.setText(strNewTotal);
				
				//add new item to cart
				Label lblName = new Label(rs.getString("name"));
				lblName.setPrefWidth(153);
				lblName.setStyle("-fx-font-size: 14; -fx-padding:0 0 0 4;");
				
				Label lblQuantity = new Label(quantity.getText());
				lblQuantity.setPrefWidth(57);
				lblQuantity.setStyle("-fx-font-size: 14");
				
				Label lblDiscount = new Label(comboUnits.getValue()+"%");
				lblDiscount.setPrefWidth(75);
				lblDiscount.setStyle("-fx-font-size: 14");
				
				Label lblPrice = new Label(String.valueOf(df.format(newAmnt)));
				lblPrice.setPrefWidth(75);
				lblPrice.setStyle("-fx-font-size: 14; -fx-padding:0 0 0 4;");
				
				HBox newItem = new HBox(10);
				newItem.setPrefWidth(575);
				
				newItem.getChildren().addAll(lblName, lblQuantity,
						lblDiscount, lblPrice);
				
				Tooltip toolTipRemove = new Tooltip();
				toolTipRemove.setText("Remove");
				toolTipRemove.setFont(new Font(14));
				
				Tooltip.install(newItem, toolTipRemove);
				
				newItem.setOnMouseClicked(e->{					
					
					//Remove item from the cart
					clientController.vboxCartItems.getChildren().remove(newItem);
					
					//Update cart items
					int intQuantity2 = Integer.parseInt(lblQuantity.getText());
					String strCount2 = clientController.countOfItems.getText();
					int intCount2 = Integer.parseInt(strCount2);
					int newCount2 = intCount2 - intQuantity2;
					clientController.countOfItems.setText(String.valueOf(newCount2));
					
					//Update total cost
					double doublePrice2 = Double.parseDouble(lblPrice.getText());
					double newAmnt2 = 0.0;
					newAmnt2 = doublePrice2 * intQuantity2;
					
					double subPrice = doublePrice2 / intQuantity2;
					double subPrice2 = intQuantity2 * subPrice;
					
					//Calculate price
					String strTotal2 = clientController.totalPrice.getText();
					double doubleTotal2 = Double.parseDouble(strTotal2);
					double newTotal2 = doubleTotal2 - subPrice2;
					String strNewTotal2 = String.valueOf(df.format(newTotal2));
					clientController.totalPrice.setText(strNewTotal2);
					
				});
				
				newItem.setOnMouseEntered(e->{
					newItem.setStyle("-fx-background-color: red");
				});
				
				newItem.setOnMouseExited(e->{
					newItem.setStyle(null);
				});
																
				clientController.vboxCartItems.getChildren().add(newItem);
				
			}else {
				//increase the count of cart items 
				int intQuantity = Integer.parseInt(quantity.getText());
				String strCount = cartController.countOfItems.getText();
				int intCount = Integer.parseInt(strCount);
				int newCount = intCount + intQuantity;
				cartController.countOfItems.setText(String.valueOf(newCount));
				
				//update total price
				double doublePrice = Double.parseDouble(itemPrice);
				double newAmnt = 0.0;
				newAmnt = doublePrice * intQuantity;
				
				String strDiscount = comboUnits.getValue();
				int intDiscount = Integer.parseInt(strDiscount);
				
				double discVal = newAmnt * intDiscount / 100;
				
				newAmnt = newAmnt - discVal;
				
				//Calculate price
				String strTotal = cartController.totalCoast.getText();
				double doubleTotal = Double.parseDouble(strTotal);
				double newTotal = doubleTotal + newAmnt;
				String strNewTotal = String.valueOf(df.format(newTotal));
				cartController.totalCoast.setText(strNewTotal);
				
				//add new item to cart
				Label lblName = new Label(rs.getString("name"));
				lblName.setPrefWidth(153);
				lblName.setStyle("-fx-font-size: 14; -fx-padding:0 0 0 4;");
				
				Label lblQuantity = new Label(quantity.getText());
				lblQuantity.setPrefWidth(57);
				lblQuantity.setStyle("-fx-font-size: 14");
				
				Label lblDiscount = new Label(comboUnits.getValue()+"%");
				lblDiscount.setPrefWidth(75);
				lblDiscount.setStyle("-fx-font-size: 14");
				
				Label lblPrice = new Label(String.valueOf(df.format(newAmnt)));
				lblPrice.setPrefWidth(75);
				lblPrice.setStyle("-fx-font-size: 14; -fx-padding:0 0 0 4;");
				
				HBox newItem = new HBox(10);
				newItem.setPrefWidth(575);
				
				newItem.getChildren().addAll(lblName, lblQuantity,
						lblDiscount, lblPrice);
				
				Tooltip toolTipRemove = new Tooltip();
				toolTipRemove.setText("Remove");
				toolTipRemove.setFont(new Font(14));
				
				Tooltip.install(newItem, toolTipRemove);
				
				newItem.setOnMouseClicked(e->{
					//Remove item from the cart
					cartController.vboxCartItems.getChildren().remove(newItem);
					
					//Update cart items
					int intQuantity2 = Integer.parseInt(lblQuantity.getText());
					String strCount2 = cartController.countOfItems.getText();
					int intCount2 = Integer.parseInt(strCount2);
					int newCount2 = intCount2 - intQuantity2;
					cartController.countOfItems.setText(String.valueOf(newCount2));
					
					//Update total cost
					double doublePrice2 = Double.parseDouble(lblPrice.getText());
					double newAmnt2 = 0.0;
					newAmnt2 = doublePrice2 * intQuantity2;
					
					double subPrice = doublePrice2 / intQuantity2;
					double subPrice2 = intQuantity2 * subPrice;
					
					//Calculate price
					String strTotal2 = cartController.totalCoast.getText();
					double doubleTotal2 = Double.parseDouble(strTotal2);
					double newTotal2 = doubleTotal2 - subPrice2;
					String strNewTotal2 = String.valueOf(df.format(newTotal2));
					cartController.totalCoast.setText(strNewTotal2);
					
				});
				
				newItem.setOnMouseEntered(e->{
					newItem.setStyle("-fx-background-color: red");
				});
				
				newItem.setOnMouseExited(e->{
					newItem.setStyle(null);
				});
																
				//add new item to cart
				cartController.vboxCartItems.getChildren().add(newItem);

			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
        	Parent root = loader.load();
        	RegisterUserController registerUserController = loader.getController();
    		registerUserController.showErr("Error! Plese Please enter numeric value");
    		return;
		} finally {
			conn.close();
		}
    	
		Stage oldStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		oldStage.close();
    }
}