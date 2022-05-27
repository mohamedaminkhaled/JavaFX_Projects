package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SellStockController {

    @FXML
    private BorderPane addStockPage;

    @FXML
    private FontAwesomeIconView icon_close;

    @FXML
    private TextField quantity;

    @FXML
    ComboBox<String> comboUnits;
    
    static String jobtitle = null;
    String medicineID = null;
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
    void confirmSellStock(MouseEvent event) throws IOException, SQLException {
    	if(quantity.getText().equals("")) {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
        	Parent root = loader.load();
        	RegisterUserController registerUserController = loader.getController();
    		registerUserController.showErr("Error! Plese identify Quantity");
    		return;
    	}
    	
    	if(comboUnits.getValue() != null) {
    		
    	}else {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
        	Parent root = loader.load();
        	RegisterUserController registerUserController = loader.getController();
    		registerUserController.showErr("Error! Plese identify Unit");
    		return;
    	}
    	
    	int validBoxes = 0, validStripes = 0, validUnits = 0 ,
    			boxStripes = 0 , stripUnits = 0;
    	
    	try {
			String strSelect = "SELECT medicines.STRIPES AS MED_STRIPES, "
					+ "medicines.UNITS AS MED_UNITS, quantity.* "
					+ "FROM medicines INNER JOIN quantity "
					+ "ON medicines.id = quantity.id "
					+ "WHERE medicines.ID = '"+medicineID+"'";
			Connection conn = DBinfo.connDB();
			
			Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ResultSet rsQuantity = state.executeQuery(strSelect);
			rsQuantity.last();
			
			boxStripes = Integer.parseInt(rsQuantity.getString("MED_STRIPES"));
			stripUnits = Integer.parseInt(rsQuantity.getString("MED_UNITS"));
			validBoxes = Integer.parseInt(rsQuantity.getString("boxes"));
			validStripes = Integer.parseInt(rsQuantity.getString("stripes"));
			validUnits = Integer.parseInt(rsQuantity.getString("units"));

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
    	
    	int intTestQuantity = 0;
		try {
			intTestQuantity = Integer.parseInt(quantity.getText());
		} catch (NumberFormatException e1) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
        	Parent root = loader.load();
        	RegisterUserController registerUserController = loader.getController();
    		registerUserController.showErr("Error! Plese enter numeric value.");
    		return;
		}
    	
    	switch (comboUnits.getValue()) {
	    	case "Boxes":{
				if(intTestQuantity > validBoxes) {
		        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
		        	Parent root = loader.load();
		        	RegisterUserController registerUserController = loader.getController();
		    		registerUserController.showErr("Error! Not enough quantity.");
		    		return;
		    	}
			}
			break;
    		
    		case "Stripes":{
				if(intTestQuantity >= boxStripes) {
		        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
		        	Parent root = loader.load();
		        	RegisterUserController registerUserController = loader.getController();
		    		registerUserController.showErr("Error! Enter valid number of stripes.");
		    		return;
		    	}
				
				if(intTestQuantity > validStripes) {
					if(validBoxes == 0) {
			        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
			        	Parent root = loader.load();
			        	RegisterUserController registerUserController = loader.getController();
			    		registerUserController.showErr("Error! Not enough quantity.");
			    		return;
			    	}
				}
			}
			break;
			
			case "Units":{
				if(intTestQuantity >= stripUnits) {
		        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
		        	Parent root = loader.load();
		        	RegisterUserController registerUserController = loader.getController();
		    		registerUserController.showErr("Error! Enter valid number of units.");
		    		return;
		    	}
				
				if(intTestQuantity > validUnits) {
					if(validStripes == 0) {
						if(validBoxes == 0) {
							FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
				        	Parent root = loader.load();
				        	RegisterUserController registerUserController = loader.getController();
				    		registerUserController.showErr("Error! Not enough quantity.");
				    		return;
						}
			    	}
				}
			}
			break;
	
			default:
				break;
    	}
    	
    	
    	
		try {
			
			String strSelect = "SELECT * FROM medicines WHERE `id` = '"+medicineID+"'";
			Connection conn=DBinfo.connDB();
			Statement state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ResultSet rs=state.executeQuery(strSelect);
			rs.last();
			
		    String medicineName = rs.getString("name");
		    String medicinePrice = rs.getString("price");
		    String medicineType = rs.getString("type");
		    
		    int intStripes = rs.getInt("stripes");
		    int intUnits = rs.getInt("units");
		    
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
				double doubleStripPrice = 0.0;
				double doubleUnitPrice = 0.0;
				double doublePrice = Double.parseDouble(medicinePrice);
				double newAmnt = 0.0;

				//Calculate price
				switch (medicineType) {
				case "Tablets":{
					doubleStripPrice = doublePrice / intStripes;
					doubleUnitPrice = doubleStripPrice / intUnits;
					
					switch (comboUnits.getValue()) {
						case "Boxes":{
							newAmnt = doublePrice * intQuantity;
						}
						break;
						
						case "Stripes":{
							newAmnt = doubleStripPrice * intQuantity;
						}
						break;
						
						case "Units":{
							newAmnt = doubleUnitPrice * intQuantity;
						}
						break;
	
						default:
							break;
					}
				}
				break;
				
				case "Lequid":{
					newAmnt = doublePrice * intQuantity;
				}
				break;
				
				case "Injection":{
					doubleStripPrice = doublePrice / intStripes;
					doubleUnitPrice = doubleStripPrice / intUnits;
					
					switch (comboUnits.getValue()) {
						case "Boxes":{
							newAmnt = doublePrice * intQuantity;
						}
						break;
						
						case "Stripes":{
							newAmnt = doubleStripPrice * intQuantity;
						}
						break;
						
						case "Units":{
							newAmnt = doubleUnitPrice * intQuantity;
						}
						break;
	
						default:
							break;
					}
				}
				break;
				
				case "Bags":{
					doubleUnitPrice = doublePrice / intUnits;
					
					switch (comboUnits.getValue()) {
						case "Boxes":{
							newAmnt = doublePrice * intQuantity;
						}
						break;
						
						case "Units":{
							newAmnt = doubleUnitPrice * intQuantity;
						}
						break;
	
						default:
							break;
					}
				}
				break;

				default:
					break;
				}
								
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
				
				Label lblUnit = new Label(comboUnits.getValue());
				lblUnit.setPrefWidth(75);
				lblUnit.setStyle("-fx-font-size: 14");
				
				Label lblPrice = new Label(String.valueOf(df.format(newAmnt)));
				lblPrice.setPrefWidth(75);
				lblPrice.setStyle("-fx-font-size: 14; -fx-padding:0 0 0 4;");
				
				Label lblID = new Label(medicineID);
				lblID.setPrefWidth(186);
				lblID.setStyle("-fx-font-size: 14");
								
				HBox newItem = new HBox(10);
				newItem.setPrefWidth(575);
				
				newItem.getChildren().addAll(lblName, lblQuantity,
						lblUnit, lblPrice, lblID);
				
				Tooltip toolTipRemove = new Tooltip();
				toolTipRemove.setText("Remove");
				toolTipRemove.setFont(new Font(14));
				
				Tooltip.install(newItem, toolTipRemove);
				
				newItem.setOnMouseClicked(e->{					
					//Remove item from the cart
					clientController.vboxCartItems.getChildren().remove(newItem);
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
				double doubleStripPrice = 0.0;
				double doubleUnitPrice = 0.0;
				double doublePrice = Double.parseDouble(medicinePrice);
				double newAmnt = 0.0;

				//Calculate price
				switch (medicineType) {
				case "Tablets":{
					doubleStripPrice = doublePrice / intStripes;
					doubleUnitPrice = doubleStripPrice / intUnits;
					
					switch (comboUnits.getValue()) {
						case "Boxes":{
							newAmnt = doublePrice * intQuantity;
						}
						break;
						
						case "Stripes":{
							newAmnt = doubleStripPrice * intQuantity;
						}
						break;
						
						case "Units":{
							newAmnt = doubleUnitPrice * intQuantity;
						}
						break;
	
						default:
							break;
					}
				}
				break;
				
				case "Lequid":{
					newAmnt = doublePrice * intQuantity;
				}
				break;
				
				case "Injection":{
					doubleStripPrice = doublePrice / intStripes;
					doubleUnitPrice = doubleStripPrice / intUnits;
					
					switch (comboUnits.getValue()) {
						case "Boxes":{
							newAmnt = doublePrice * intQuantity;
						}
						break;
						
						case "Stripes":{
							newAmnt = doubleStripPrice * intQuantity;
						}
						break;
						
						case "Units":{
							newAmnt = doubleUnitPrice * intQuantity;
						}
						break;
	
						default:
							break;
					}
				}
				break;
				
				case "Bags":{
					doubleUnitPrice = doublePrice / intUnits;
					
					switch (comboUnits.getValue()) {
						case "Boxes":{
							newAmnt = doublePrice * intQuantity;
						}
						break;
						
						case "Units":{
							newAmnt = doubleUnitPrice * intQuantity;
						}
						break;
	
						default:
							break;
					}
				}
				break;

				default:
					break;
				}
				
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
				
				Label lblUnit = new Label(comboUnits.getValue());
				lblUnit.setPrefWidth(75);
				lblUnit.setStyle("-fx-font-size: 14");
				
				Label lblPrice = new Label(String.valueOf(df.format(newAmnt)));
				lblPrice.setPrefWidth(75);
				lblPrice.setStyle("-fx-font-size: 14; -fx-padding:0 0 0 4;");
				
				Label lblID = new Label(medicineID);
				lblID.setPrefWidth(186);
				lblID.setStyle("-fx-font-size: 14");
								
				HBox newItem = new HBox(10);
				newItem.setPrefWidth(575);
				
				newItem.getChildren().addAll(lblName, lblQuantity,
						lblUnit, lblPrice, lblID);
				
				Tooltip toolTipRemove = new Tooltip();
				toolTipRemove.setText("Remove");
				toolTipRemove.setFont(new Font(14));
				
				Tooltip.install(newItem, toolTipRemove);
				
				newItem.setOnMouseClicked(e->{
					//Remove item from the cart
					cartController.vboxCartItems.getChildren().remove(newItem);
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
		}
    	
		Stage oldStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		oldStage.close();
    }
}