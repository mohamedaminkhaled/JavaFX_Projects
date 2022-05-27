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
    private TextField tfCustomPrice;
    
    static String jobtitle = null;
    String itemId = null;
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
    		registerUserController.showErr("Error! Plese identify Quantity");
    		return;
    	}
    	
    	int validQuantity = 0;
    	
    	Connection conn = null;
    	try {
    		String strSelect = "SELECT `quantity` FROM `items` "
					 + "WHERE `id` = '"+itemId+"'";
    		
			conn= DBinfo.connDB();
			
			Statement state = conn.createStatement();
			ResultSet rsQuantity = state.executeQuery(strSelect);
			rsQuantity.next();
			
			validQuantity = Integer.parseInt(rsQuantity.getString("quantity"));
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			conn.close();
		}
    	
    	int intTestQuantity = 0;
		try {
			intTestQuantity = Integer.parseInt(quantity.getText());
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
        	Parent root = loader.load();
        	RegisterUserController registerUserController = loader.getController();
    		registerUserController.showErr("Error! Plese enter numeric value.");
    		return;
		}
		
		if(intTestQuantity > validQuantity) {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
        	Parent root = loader.load();
        	RegisterUserController registerUserController = loader.getController();
    		registerUserController.showErr("Error! Not enough quantity.");
    		return;
    	}
		
		try {
			
			String strSelect = "SELECT * FROM `items` WHERE `id` = '"+itemId+"'";
			conn=DBinfo.connDB();
			Statement state=conn.createStatement();
			ResultSet rs=state.executeQuery(strSelect);
			rs.next();
			
		    String itemName = rs.getString("name");
		    String itemPrice = rs.getString("price");
		    		    
		    //Display double with max two decimal digits
			DecimalFormat df = new DecimalFormat("#.##");

			//increase the count of cart items 
			double intQuantity = Double.parseDouble(quantity.getText());
			String strCount = cartController.countOfItems.getText();
			double intCount = Double.parseDouble(strCount);
			double newCount = intCount + intQuantity;
			cartController.countOfItems.setText(String.valueOf(newCount));
			
			//update total price
			double doublePrice = Double.parseDouble(itemPrice);
			double newAmnt = doublePrice * intQuantity;
			
			Label lblPrice;
			double customAmount = 0.0;
			if(tfCustomPrice.getText().equals("")) {
				lblPrice = new Label(String.valueOf(df.format(newAmnt)));
				lblPrice.setPrefWidth(75);
				lblPrice.setStyle("-fx-font-size: 14; -fx-padding:0 0 0 4;");
			}else {
				double customPrice = Double.parseDouble(tfCustomPrice.getText());
				customAmount = intQuantity * customPrice;
				
				lblPrice = new Label(String.valueOf(df.format(customAmount)));
				lblPrice.setPrefWidth(75);
				lblPrice.setStyle("-fx-font-size: 14; -fx-padding:0 0 0 4;");
			}
							
			String strTotal = cartController.totalCoast.getText();
			double doubleTotal = Double.parseDouble(strTotal);
			double newTotal = doubleTotal + Double.parseDouble(lblPrice.getText());
			String strNewTotal = String.valueOf(df.format(newTotal));
			cartController.totalCoast.setText(strNewTotal);
			
			//add new item to cart
			Label lblName = new Label(rs.getString("name"));
			lblName.setPrefWidth(153);
			lblName.setStyle("-fx-font-size: 14; -fx-padding:0 0 0 4;");
			
			Label lblQuantity = new Label(quantity.getText());
			lblQuantity.setPrefWidth(57);
			lblQuantity.setStyle("-fx-font-size: 14");
			
			Label lblItemId = new Label(itemId);
			lblItemId.setPrefWidth(100);
			lblItemId.setStyle("-fx-font-size: 14");
							
			HBox newItem = new HBox(10);
			newItem.setPrefWidth(420);
			
			newItem.getChildren().addAll(lblName, lblQuantity,
					lblPrice, lblItemId);
			
			Tooltip toolTipRemove = new Tooltip();
			toolTipRemove.setText("Remove");
			toolTipRemove.setFont(new Font(14));
			
			Tooltip.install(newItem, toolTipRemove);
			
			newItem.setOnMouseClicked(e->{
				//Remove item from the cart
				cartController.vboxCartItems.getChildren().remove(newItem);
				
				//Update cart items
				double intQuantity2 = Double.parseDouble(lblQuantity.getText());
				String strCount2 = cartController.countOfItems.getText();
				double intCount2 = Double.parseDouble(strCount2);
				double newCount2 = intCount2 - intQuantity2;
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