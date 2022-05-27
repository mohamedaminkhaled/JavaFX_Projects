package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CartController {

    @FXML
    BorderPane borderCart;
	
	@FXML
    Label countOfItems;

    @FXML
    Label totalCoast;
    
    @FXML
    VBox vboxCartItems;
    
    @FXML
    ComboBox<String> comboClientName;

    @FXML
    TextField tfDiscountValue;
        
    @FXML
    private TextField tfBarcode;

    @FXML
    private TextField tfQuantity;

    @FXML
    private TextField tfPrice;
    
    @FXML
    private Button btnAdd;
    
    String username;
    
    @FXML
    void addByEnter(KeyEvent event) throws ClassNotFoundException, IOException, SQLException {
    	if (event.getCode() == KeyCode.ENTER) {
    		addToCart(event);
        }
    	
    	btnAdd.requestFocus();
    }
    
    @FXML
    void btnAddToCart(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {
    	addToCart(event);
    }

    void addToCart(Event event) throws IOException, SQLException, ClassNotFoundException {
    	if(tfQuantity.getText().equals("")) {
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
					 + "WHERE `id` = '"+tfBarcode.getText()+"'";
    		
			conn= DBinfo.connDB();
			
			Statement state = conn.createStatement();
			ResultSet rsQuantity = state.executeQuery(strSelect);
			if(rsQuantity.next()) {
				validQuantity = Integer.parseInt(rsQuantity.getString("quantity"));
			}else {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
	        	Parent root = loader.load();
	        	RegisterUserController registerUserController = loader.getController();
	    		registerUserController.showErr("ÈÇÑßæÏ ÎØÃ");
	    		return;
			}
			
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			conn.close();
		}
    	
    	int intTestQuantity = 0;
		try {
			intTestQuantity = Integer.parseInt(tfQuantity.getText());
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
			
			String strSelect = "SELECT * FROM `items` WHERE `id` = '"+tfBarcode.getText()+"'";
			conn=DBinfo.connDB();
			Statement state=conn.createStatement();
			ResultSet rs=state.executeQuery(strSelect);
			rs.next();
			
		    String itemPrice = rs.getString("price");
		    		    
		    //Display double with max two decimal digits
			DecimalFormat df = new DecimalFormat("#.##");

			//increase the count of cart items 
			double intQuantity = Double.parseDouble(tfQuantity.getText());
			String strCount = countOfItems.getText();
			double intCount = Double.parseDouble(strCount);
			double newCount = intCount + intQuantity;
			countOfItems.setText(String.valueOf(newCount));
			
			//update total price
			double doublePrice = Double.parseDouble(itemPrice);
			double newAmnt = doublePrice * intQuantity;
			
			Label lblPrice;
			double customAmount = 0.0;
			if(tfPrice.getText().equals("")) {
				lblPrice = new Label(String.valueOf(df.format(newAmnt)));
				lblPrice.setPrefWidth(75);
				lblPrice.setStyle("-fx-font-size: 14; -fx-padding:0 0 0 4;");
			}else {
				double customPrice = Double.parseDouble(tfPrice.getText());
				customAmount = intQuantity * customPrice;
				
				lblPrice = new Label(String.valueOf(df.format(customAmount)));
				lblPrice.setPrefWidth(75);
				lblPrice.setStyle("-fx-font-size: 14; -fx-padding:0 0 0 4;");
			}
							
			String strTotal = totalCoast.getText();
			double doubleTotal = Double.parseDouble(strTotal);
			double newTotal = doubleTotal + Double.parseDouble(lblPrice.getText());
			String strNewTotal = String.valueOf(df.format(newTotal));
			totalCoast.setText(strNewTotal);
			
			//add new item to cart
			Label lblName = new Label(rs.getString("name"));
			lblName.setPrefWidth(153);
			lblName.setStyle("-fx-font-size: 14; -fx-padding:0 0 0 4;");
			
			Label lblQuantity = new Label(tfQuantity.getText());
			lblQuantity.setPrefWidth(57);
			lblQuantity.setStyle("-fx-font-size: 14");
			
			Label lblItemId = new Label(tfBarcode.getText());
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
				vboxCartItems.getChildren().remove(newItem);
				
				//Update cart items
				double intQuantity2 = Double.parseDouble(lblQuantity.getText());
				String strCount2 = countOfItems.getText();
				double intCount2 = Double.parseDouble(strCount2);
				double newCount2 = intCount2 - intQuantity2;
				countOfItems.setText(String.valueOf(newCount2));
				
				//Update total cost
				double doublePrice2 = Double.parseDouble(lblPrice.getText());
				double newAmnt2 = 0.0;
				newAmnt2 = doublePrice2 * intQuantity2;
				
				double subPrice = doublePrice2 / intQuantity2;
				double subPrice2 = intQuantity2 * subPrice;
				
				//Calculate price
				String strTotal2 = totalCoast.getText();
				double doubleTotal2 = Double.parseDouble(strTotal2);
				double newTotal2 = doubleTotal2 - subPrice2;
				String strNewTotal2 = String.valueOf(df.format(newTotal2));
				totalCoast.setText(strNewTotal2);
				
			});
			
			newItem.setOnMouseEntered(e->{
				newItem.setStyle("-fx-background-color: red");
			});
			
			newItem.setOnMouseExited(e->{
				newItem.setStyle(null);
			});
															
			//add new item to cart
			vboxCartItems.getChildren().add(newItem);

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
    }
    
    @FXML
    void getConfirmBill(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {
    	Date date = new Date();
		double time = date.getTime();
    	String billNumber = String.valueOf(date.getTime());

    	ArrayList<HBox> arrItems = new ArrayList<>();
    	
    	ArrayList<Node> arrNodes = new ArrayList<>();
    	arrNodes.addAll(vboxCartItems.getChildren());
    	for (Node node : arrNodes) {
    		arrItems.add((HBox) node);
		}
    	
		String clientName = comboClientName.getValue();
		
		FXMLLoader loaderError = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
    	Parent rootError = loaderError.load();
    	RegisterUserController registerUserController = loaderError.getController();
    	
    	if(comboClientName.getValue() != null) {
	    	Connection connTest = DBinfo.connDB();
			try {
				String strSelectComID = "SELECT * FROM clients WHERE `name` = '"+comboClientName.getValue()+"'";
				
				
				Statement stat = connTest.createStatement();
				ResultSet rs = stat.executeQuery(strSelectComID);
				rs.next();
				
				rs.getString("name");
				
			} catch (Exception e) {
				e.printStackTrace();
				registerUserController.showErr("Error! Client name not found");
	    		return;
			} finally {
				connTest.close();
			}
    	}
    			
		try {
			Double.parseDouble(tfDiscountValue.getText());
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
			registerUserController.showErr("Error! Enter numeric value for discount.");
    		return;
		}
    	
    	double discount = 0.0;
		double newTotal = 0.0;
    	
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
		    	int quantity = Integer.parseInt(strQuantity);

		    	Label lblPrice = (Label) arrItemDetails.get(2);
		    	String strPrice = lblPrice.getText();
		    	
		    	Label lblID = (Label) arrItemDetails.get(3);
		    	String strID = lblID.getText();
		    	
		    	Connection conn = null;
				try {
					conn = DBinfo.connDB();
					Statement stat = conn.createStatement();
					
					//ex. 2021-01-19 12:10:26
					DateFormat dateFormate = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss a");
					String dateTimt = dateFormate.format(date);
					dateTimt = dateTimt.replace("/","-");
					
					try {
						discount = Double.parseDouble(tfDiscountValue.getText());
						double total = Double.parseDouble(totalCoast.getText());
						newTotal = total - discount;
					} catch (NumberFormatException e) {
						e.printStackTrace();
						
						registerUserController.showErr("Error! Plese enter numeric value for discount.");
			    		return;
					}
					
					String strSelect = "SELECT * FROM `clients` "
							+ "WHERE `name` = '"+comboClientName.getValue()+"'";
					
					ResultSet rs;
					rs=stat.executeQuery(strSelect);
					
					double newBalance = 0.0;
					double newLoan = 0.0;
					if(rs.next()) {
						String strLoane = rs.getString("loan");
						String strBalance = rs.getString("balance");
						
						double loan = Double.parseDouble(strLoane);
						double balance = Double.parseDouble(strBalance);
						double billTotal = newTotal;
						
						double diffBalance = balance - billTotal;
						
						newLoan = loan;
						
						if(diffBalance < 0) {
							newLoan = loan + (diffBalance * (-1));
						}else {
							newBalance = balance - billTotal;
						}
					}
										
					String sql="INSERT INTO `sales`(`NO`,`id`,`name`,`quantity`,`PRICE`,`discount`,"
							+ "`new_total`,`TIME`,`EMP_USERNAME`,`client_name`,`client_balance`,`client_debit`) "
							+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
					
					PreparedStatement ps = conn.prepareStatement(sql);

					ps.setString(1, billNumber);
					ps.setString(2, strID);
					ps.setString(3, strName);
					ps.setString(4, strQuantity);
					ps.setString(5, strPrice);
					ps.setString(6, String.valueOf(discount));
					ps.setString(7, String.valueOf(newTotal));
					ps.setString(8, dateTimt);
					ps.setString(9, username);
					ps.setString(10, clientName);
					ps.setString(11, String.valueOf(newBalance));
					ps.setString(12, String.valueOf(newLoan));
					ps.executeUpdate();
					
					System.out.println("Inserted to sales...");
					
					//Update item quantity
					int intQuantity = Integer.parseInt(strQuantity);
					
					//SELECT item quantity
					String strSelectQuantity = "SELECT * FROM `items` WHERE `id` = '"+strID+"'";
					ResultSet rsQuantity = stat.executeQuery(strSelectQuantity);
					rsQuantity.next();
					
					int validquantity = Integer.parseInt(rsQuantity.getString("quantity"));
					int newQuantity = validquantity - intQuantity;
					
					int intSold = Integer.parseInt(rsQuantity.getString("sold"));
					int newsold = intSold + intQuantity;
					
					String strUpdateQuantity ="UPDATE `items` set `quantity`=?, `sold`=? WHERE `id` =?";  
					
					PreparedStatement psUpQuantity = conn.prepareStatement(strUpdateQuantity);
					psUpQuantity.setInt(1, newQuantity);
					psUpQuantity.setInt(2, newsold);
					psUpQuantity.setString(3, strName);
					psUpQuantity.executeUpdate();
				
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					conn.close();
				}
			}
		}
    	
    	//clear the cart
    	vboxCartItems.getChildren().remove(1, arrNodes.size());    	
    	countOfItems.setText("0");
    	totalCoast.setText("0.0");
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Bill.fxml"));
    	Parent root = loader.load();
    	BillController billController = loader.getController();
    	billController.setBill(billNumber);
    	
    	Statement state;
		ResultSet rs;
		Connection conn = null;
    	try {
			conn=DBinfo.connDB();
			state=conn.createStatement();
			
			String strSelect = "SELECT * FROM `clients` "
					+ "WHERE `name` = '"+comboClientName.getValue()+"'";
			
			rs=state.executeQuery(strSelect);
			
			if(rs.next()) {
				String strLoane = rs.getString("loan");
				String strBalance = rs.getString("balance");
				
				double loan = Double.parseDouble(strLoane);
				double balance = Double.parseDouble(strBalance);
				double billTotal = newTotal;
				
				double diffBalance = balance - billTotal;
				
				double newBalance = 0.0, newLoan = loan;
				
				if(diffBalance < 0) {
					newLoan = loan + (diffBalance * (-1));
				}else {
					newBalance = balance - billTotal;
				}
				
				System.out.println("diffBalance: "+diffBalance);
				System.out.println("newBalance: "+newBalance);
				System.out.println("newLoan: "+newLoan);
							
				String strUpdateQuantity ="UPDATE `clients` set `loan`=?, `balance`=? WHERE `name` =?";  
				
				PreparedStatement psUpQuantity = conn.prepareStatement(strUpdateQuantity);
				psUpQuantity.setString(1, String.valueOf(newLoan));
				psUpQuantity.setString(2, String.valueOf(newBalance));
				psUpQuantity.setString(3, comboClientName.getValue());
				psUpQuantity.executeUpdate();
			}
						
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		} finally {
			conn.close();
		}
    	
    	comboClientName.setValue(null);
    	tfDiscountValue.setText("0.0");

    	
    	Stage stage = new Stage();
    	Scene scene = new Scene(root, 252, 543 + ((arrNodes.size() - 1) * 20));
    	stage.setScene(scene);
    	stage.initStyle(StageStyle.TRANSPARENT);
 
    	stage.show();
    }
}