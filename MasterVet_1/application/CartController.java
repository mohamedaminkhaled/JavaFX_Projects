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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    
    String username;
    
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
		    	
		    	Label lblQuantity = (Label) arrItemDetails.get(1);
		    	String strQuantity = lblQuantity.getText();
		    	double intQuantity = Double.parseDouble(strQuantity);

		    	Label lblUnit = (Label) arrItemDetails.get(2);
		    	String strUnit = lblUnit.getText();
		    	
		    	Label lblPrice = (Label) arrItemDetails.get(3);
		    	String strPrice = lblPrice.getText();
		    	
		    	Label lblID = (Label) arrItemDetails.get(4);
		    	String strID = lblID.getText();
		    	
		    	
		    	String strBoxes = "0";
		    	String strStripes = "0";
		    	String strUnits = "0";
		    	String strKg = "0";
		    	String strGm = "0";
		    	String strCm = "0";
		    	
		    	switch (strUnit) {
					case "Boxes":{
						strBoxes = strQuantity;
					}
					break;
					
					case "Stripes":{
						strStripes = strQuantity;
					}
					break;
					
					case "Units":{
						strUnits = strQuantity;
					}
					break;
					
					case "kg":{
						strKg = strQuantity;
					}
					break;
					
					case "gm":{
						strGm = strQuantity;
					}
					break;
					
					case "Cm":{
						strCm = strQuantity;
					}
					break;
	
					default:
						break;
				}
		    	
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
										
					String sql="INSERT INTO `sales`(`NO`,`ID`,`BOXES`, `STRIPES`,"
							+ "`UNITS`,`kg`,`gms`,`cm`,`PRICE`,`discount`,"
							+ "`new_total`,`TIME`,`EMP_USERNAME`,`client_name`,`client_balance`,`client_debit`) "
							+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					
					PreparedStatement ps = conn.prepareStatement(sql);

					ps.setString(1, billNumber);
					ps.setString(2, strID);
					ps.setString(3, strBoxes);
					ps.setString(4, strStripes);
					ps.setString(5, strUnits);
					ps.setString(6, strKg);
					ps.setString(7, strGm);
					ps.setString(8, strCm);
					ps.setString(9, strPrice);
					ps.setString(10, String.valueOf(discount));
					ps.setString(11, String.valueOf(newTotal));
					ps.setString(12, dateTimt);
					ps.setString(13, username);
					ps.setString(14, clientName);
					ps.setString(15, String.valueOf(newBalance));
					ps.setString(16, String.valueOf(newLoan));
					ps.executeUpdate();
					
					System.out.println("Inserted to sales...");
					
					//Update medicine quantity
					
					//SELECT medicine type
					String strSelectSuppID = "SELECT `type` FROM medicines WHERE `id` = '"+strID+"'";
					rs = stat.executeQuery(strSelectSuppID);
					rs.next();
			    	
					switch (rs.getString("type")) {
						case "Tablets":{
						    switch (strUnit) {
								case "Boxes":{
									int boxes = Integer.parseInt(strQuantity);
									
									//SELECT medicine boxes
									String strSelectQuantity = "SELECT `BOXES` FROM quantity WHERE `id` = '"+strID+"'";
									ResultSet rsQuantity = stat.executeQuery(strSelectQuantity);
									rsQuantity.next();
									
									int validBoxes = Integer.parseInt(rsQuantity.getString("boxes"));
									int newBoxes = validBoxes - boxes;
									
									String strUpdateQuantity ="UPDATE `quantity` set `boxes`=? WHERE `id` =?";  
									
									PreparedStatement psUpQuantity = conn.prepareStatement(strUpdateQuantity);
									psUpQuantity.setInt(1, newBoxes);
									psUpQuantity.setString(2, strID);
									psUpQuantity.executeUpdate();
								}
								break;
								
								case "Stripes":{
									int stripes = Integer.parseInt(strQuantity);
									
									//SELECT valid boxes, Stripes
									String strSelectQuantity = "SELECT `BOXES`, `STRIPES` FROM quantity WHERE `id` = '"+strID+"'";
									ResultSet rsQuantity = stat.executeQuery(strSelectQuantity);
									rsQuantity.next();
									
									int validBoxes = Integer.parseInt(rsQuantity.getString("BOXES"));
									int validStripes = Integer.parseInt(rsQuantity.getString("STRIPES"));
									
									//SELECT medicine Stripes, units
									String strSelectQuantityDetails = "SELECT `STRIPES` FROM medicines WHERE `id` = '"+strID+"'";
									ResultSet rsQuantityDetails = stat.executeQuery(strSelectQuantityDetails);
									rsQuantityDetails.next();
									
									int boxStrips = Integer.parseInt(rsQuantityDetails.getString("STRIPES"));
									
									if(stripes > validStripes) {
										int difference = stripes - validStripes;
										int newValidBoxes = validBoxes - 1;
										int newStripes = boxStrips - difference;
											
										String strUpdateQuantity ="UPDATE `quantity` set `boxes`=?,`stripes`=? WHERE `id` =?";  
											
										PreparedStatement psUpQuantity = conn.prepareStatement(strUpdateQuantity);
										psUpQuantity.setInt(1, newValidBoxes);
										psUpQuantity.setInt(2, newStripes);
										psUpQuantity.setString(3, strID);
										psUpQuantity.executeUpdate();										
										
									}else {
										int newValidStripes = validStripes - stripes;
										
										String strUpdateQuantity ="UPDATE `quantity` set `stripes`=? WHERE `id` =?";  
										
										PreparedStatement psUpQuantity = conn.prepareStatement(strUpdateQuantity);
										psUpQuantity.setInt(1, newValidStripes);
										psUpQuantity.setString(2, strID);
										psUpQuantity.executeUpdate();
									}
									
									
								}
								break;
								
								case "Units":{
									int units = Integer.parseInt(strQuantity);
									
									//SELECT valid boxes, Stripes, units
									String strSelectQuantity = "SELECT `BOXES`, `STRIPES`,`UNITS` FROM quantity WHERE `id` = '"+strID+"'";
									ResultSet rsQuantity = stat.executeQuery(strSelectQuantity);
									rsQuantity.next();
									
									int validBoxes = Integer.parseInt(rsQuantity.getString("BOXES"));
									int validStripes = Integer.parseInt(rsQuantity.getString("STRIPES"));
									int validUnits = Integer.parseInt(rsQuantity.getString("UNITS"));

									//SELECT medicine Stripes, units
									String strSelectQuantityDetails = "SELECT `STRIPES`,`UNITS` FROM medicines WHERE `id` = '"+strID+"'";
									ResultSet rsQuantityDetails = stat.executeQuery(strSelectQuantityDetails);
									rsQuantityDetails.next();
									
									int boxStrips = Integer.parseInt(rsQuantityDetails.getString("STRIPES"));
									int stripUnits = Integer.parseInt(rsQuantityDetails.getString("UNITS"));
									
									if(units > validUnits) {
										if(validStripes > 0) {
											int difference = units - validUnits;
											int newValidStripes = validStripes - 1; 
											int newValidUnits = stripUnits - difference; 
											
											String strUpdateQuantity ="UPDATE `quantity` set `STRIPES`=?, `UNITS`=? WHERE `id` =?";  
											
											PreparedStatement psUpQuantity = conn.prepareStatement(strUpdateQuantity);
											psUpQuantity.setInt(1, newValidStripes);
											psUpQuantity.setInt(2, newValidUnits);
											psUpQuantity.setString(3, strID);
											psUpQuantity.executeUpdate();
										}else {
											int difference = units - validUnits;
											int newValidBoxes = validBoxes - 1;
											int newValidStripes = boxStrips - 1;
											int newValidUnits = stripUnits - difference; 
											
											String strUpdateQuantity ="UPDATE `quantity` set `BOXES`=?, `STRIPES`=?, `UNITS`=? WHERE `id` =?";  
											
											PreparedStatement psUpQuantity = conn.prepareStatement(strUpdateQuantity);
											psUpQuantity.setInt(1, newValidBoxes);
											psUpQuantity.setInt(2, newValidStripes);
											psUpQuantity.setInt(3, newValidUnits);
											psUpQuantity.setString(4, strID);
											psUpQuantity.executeUpdate();
										}
									}else {
										int newValidUnits = validUnits - units; 
										
										String strUpdateQuantity ="UPDATE `quantity` set `UNITS`=? WHERE `id` =?";  
										
										PreparedStatement psUpQuantity = conn.prepareStatement(strUpdateQuantity);
										psUpQuantity.setInt(1, newValidUnits);
										psUpQuantity.setString(2, strID);
										psUpQuantity.executeUpdate();
									}
								}
								break;
				
								default:
									break;
							}
						}
						break;
						
						case "Lequid":{
							int boxes = Integer.parseInt(strQuantity);
							
							//SELECT medicine boxes
							String strSelectQuantity = "SELECT `BOXES` FROM quantity WHERE `id` = '"+strID+"'";
							ResultSet rsQuantity = stat.executeQuery(strSelectQuantity);
							rsQuantity.next();
							
							int validBoxes = Integer.parseInt(rsQuantity.getString("boxes"));
							int newBoxes = validBoxes - boxes;
							
							String strUpdateQuantity ="UPDATE `quantity` set `boxes`=? WHERE `id` =?";  
							
							PreparedStatement psUpQuantity = conn.prepareStatement(strUpdateQuantity);
							psUpQuantity.setInt(1, newBoxes);
							psUpQuantity.setString(2, strID);
							psUpQuantity.executeUpdate();
						}
						break;
						
						case "Injection":{
						    switch (strUnit) {
								case "Boxes":{
									int boxes = Integer.parseInt(strQuantity);
									
									//SELECT medicine boxes
									String strSelectQuantity = "SELECT `BOXES` FROM quantity WHERE `id` = '"+strID+"'";
									ResultSet rsQuantity = stat.executeQuery(strSelectQuantity);
									rsQuantity.next();
									
									int validBoxes = Integer.parseInt(rsQuantity.getString("boxes"));
									int newBoxes = validBoxes - boxes;
									
									String strUpdateQuantity ="UPDATE `quantity` set `boxes`=? WHERE `id` =?";  
									
									PreparedStatement psUpQuantity = conn.prepareStatement(strUpdateQuantity);
									psUpQuantity.setInt(1, newBoxes);
									psUpQuantity.setString(2, strID);
									psUpQuantity.executeUpdate();
								}
								break;
								
								case "Stripes":{
									int stripes = Integer.parseInt(strQuantity);
									
									//SELECT valid boxes, Stripes
									String strSelectQuantity = "SELECT `BOXES`, `STRIPES` FROM quantity WHERE `id` = '"+strID+"'";
									ResultSet rsQuantity = stat.executeQuery(strSelectQuantity);
									rsQuantity.next();
									
									int validBoxes = Integer.parseInt(rsQuantity.getString("BOXES"));
									int validStripes = Integer.parseInt(rsQuantity.getString("STRIPES"));
									
									//SELECT medicine Stripes, units
									String strSelectQuantityDetails = "SELECT `STRIPES` FROM medicines WHERE `id` = '"+strID+"'";
									ResultSet rsQuantityDetails = stat.executeQuery(strSelectQuantityDetails);
									rsQuantityDetails.first();
									
									int boxStrips = Integer.parseInt(rsQuantityDetails.getString("STRIPES"));
									
									if(stripes > validStripes) {
										int difference = stripes - validStripes;
										int newValidBoxes = validBoxes - 1;
										int newStripes = boxStrips - difference;
											
										String strUpdateQuantity ="UPDATE `quantity` set `boxes`=?,`stripes`=? WHERE `id` =?";  
											
										PreparedStatement psUpQuantity = conn.prepareStatement(strUpdateQuantity);
										psUpQuantity.setInt(1, newValidBoxes);
										psUpQuantity.setInt(2, newStripes);
										psUpQuantity.setString(3, strID);
										psUpQuantity.executeUpdate();										
										
									}else {
										int newValidStripes = validStripes - stripes;
										
										String strUpdateQuantity ="UPDATE `quantity` set `stripes`=? WHERE `id` =?";  
										
										PreparedStatement psUpQuantity = conn.prepareStatement(strUpdateQuantity);
										psUpQuantity.setInt(1, newValidStripes);
										psUpQuantity.setString(2, strID);
										psUpQuantity.executeUpdate();
									}
								}
								break;
								
								case "Units":{
									int units = Integer.parseInt(strQuantity);
									
									//SELECT valid boxes, Stripes, units
									String strSelectQuantity = "SELECT `BOXES`, `STRIPES`,`UNITS` FROM quantity WHERE `id` = '"+strID+"'";
									ResultSet rsQuantity = stat.executeQuery(strSelectQuantity);
									rsQuantity.next();
									
									int validBoxes = Integer.parseInt(rsQuantity.getString("BOXES"));
									int validStripes = Integer.parseInt(rsQuantity.getString("STRIPES"));
									int validUnits = Integer.parseInt(rsQuantity.getString("UNITS"));

									//SELECT medicine Stripes, units
									String strSelectQuantityDetails = "SELECT `STRIPES`,`UNITS` FROM medicines WHERE `id` = '"+strID+"'";
									ResultSet rsQuantityDetails = stat.executeQuery(strSelectQuantityDetails);
									rsQuantityDetails.next();
									
									int boxStrips = Integer.parseInt(rsQuantityDetails.getString("STRIPES"));
									int stripUnits = Integer.parseInt(rsQuantityDetails.getString("UNITS"));
									
									if(units > validUnits) {
										if(validStripes > 0) {
											int difference = units - validUnits;
											int newValidStripes = validStripes - 1; 
											int newValidUnits = stripUnits - difference; 
											
											String strUpdateQuantity ="UPDATE `quantity` set `STRIPES`=?, `UNITS`=? WHERE `id` =?";  
											
											PreparedStatement psUpQuantity = conn.prepareStatement(strUpdateQuantity);
											psUpQuantity.setInt(1, newValidStripes);
											psUpQuantity.setInt(2, newValidUnits);
											psUpQuantity.setString(3, strID);
											psUpQuantity.executeUpdate();
										}else {
											int difference = units - validUnits;
											int newValidBoxes = validBoxes - 1;
											int newValidStripes = boxStrips - 1;
											int newValidUnits = stripUnits - difference; 
											
											String strUpdateQuantity ="UPDATE `quantity` set `BOXES`=?, `STRIPES`=?, `UNITS`=? WHERE `id` =?";  
											
											PreparedStatement psUpQuantity = conn.prepareStatement(strUpdateQuantity);
											psUpQuantity.setInt(1, newValidBoxes);
											psUpQuantity.setInt(2, newValidStripes);
											psUpQuantity.setInt(3, newValidUnits);
											psUpQuantity.setString(4, strID);
											psUpQuantity.executeUpdate();
										}
									}else {
										int newValidUnits = validUnits - units; 
										
										String strUpdateQuantity ="UPDATE `quantity` set `UNITS`=? WHERE `id` =?";  
										
										PreparedStatement psUpQuantity = conn.prepareStatement(strUpdateQuantity);
										psUpQuantity.setInt(1, newValidUnits);
										psUpQuantity.setString(2, strID);
										psUpQuantity.executeUpdate();
									}
								}
								break;
				
								default:
									break;
							}
						}
						break;
						
						case "Bags":{
							switch (strUnit) {
							case "Boxes":{
								int boxes = Integer.parseInt(strQuantity);
								
								//SELECT medicine boxes
								String strSelectQuantity = "SELECT `BOXES` FROM quantity WHERE `id` = '"+strID+"'";
								ResultSet rsQuantity = stat.executeQuery(strSelectQuantity);
								rsQuantity.next();
								
								int validBoxes = Integer.parseInt(rsQuantity.getString("boxes"));
								int newBoxes = validBoxes - boxes;
								
								String strUpdateQuantity ="UPDATE `quantity` set `boxes`=? WHERE `id` =?";  
								
								PreparedStatement psUpQuantity = conn.prepareStatement(strUpdateQuantity);
								psUpQuantity.setInt(1, newBoxes);
								psUpQuantity.setString(2, strID);
								psUpQuantity.executeUpdate();
							}
							break;
							
							case "Units":{
								int units = Integer.parseInt(strQuantity);
								
								//SELECT valid boxes, Stripes
								String strSelectQuantity = "SELECT `BOXES`, `UNITS` FROM quantity WHERE `id` = '"+strID+"'";
								ResultSet rsQuantity = stat.executeQuery(strSelectQuantity);
								rsQuantity.next();
								
								int validBoxes = Integer.parseInt(rsQuantity.getString("BOXES"));
								int validUnits = Integer.parseInt(rsQuantity.getString("UNITS"));
								
								//SELECT medicine Stripes, units
								String strSelectQuantityDetails = "SELECT `UNITS` FROM medicines WHERE `id` = '"+strID+"'";
								ResultSet rsQuantityDetails = stat.executeQuery(strSelectQuantityDetails);
								rsQuantityDetails.next();
								
								int boxUnits = Integer.parseInt(rsQuantityDetails.getString("UNITS"));
								
								if(units > validUnits) {
									int difference = units - validUnits;
									int newValidBoxes = validBoxes - 1;
									int newUnits = boxUnits - difference;
										
									String strUpdateQuantity ="UPDATE `quantity` set `boxes`=?, `units`=? WHERE `id` =?";  
										
									PreparedStatement psUpQuantity = conn.prepareStatement(strUpdateQuantity);
									psUpQuantity.setInt(1, newValidBoxes);
									psUpQuantity.setInt(2, newUnits);
									psUpQuantity.setString(3, strID);
									psUpQuantity.executeUpdate();										
									
								}else {
									int newValidUnits = validUnits - units;
									
									String strUpdateQuantity ="UPDATE `quantity` set `units`=? WHERE `id` =?";  
									
									PreparedStatement psUpQuantity = conn.prepareStatement(strUpdateQuantity);
									psUpQuantity.setInt(1, newValidUnits);
									psUpQuantity.setString(2, strID);
									psUpQuantity.executeUpdate();
								}
							}
							break;
							default:
								break;
							}
						}
						break;
						
						case "Powder":{
							int gms = Integer.parseInt(strQuantity);
							
							//SELECT medicine boxes
							String strSelectQuantity = "SELECT `gms` FROM quantity WHERE `id` = '"+strID+"'";
							ResultSet rsQuantity = stat.executeQuery(strSelectQuantity);
							rsQuantity.next();
							
							int validGms = Integer.parseInt(rsQuantity.getString("gms"));
							int newGms = validGms - gms;
							
							String strUpdateQuantity ="UPDATE `quantity` set `gms`=? WHERE `id` =?";  
							
							PreparedStatement psUpQuantity = conn.prepareStatement(strUpdateQuantity);
							psUpQuantity.setInt(1, newGms);
							psUpQuantity.setString(2, strID);
							psUpQuantity.executeUpdate();
						}
						break;
						
						case "Kgs":{
							double kgs = Double.parseDouble(strQuantity);
							
							//SELECT medicine boxes
							String strSelectQuantity = "SELECT `kg` FROM quantity WHERE `id` = '"+strID+"'";
							ResultSet rsQuantity = stat.executeQuery(strSelectQuantity);
							rsQuantity.next();
							
							double validKgs = Double.parseDouble(rsQuantity.getString("kg"));
							double newKgs = validKgs - kgs;
							
							String strUpdateQuantity ="UPDATE `quantity` set `kg`=? WHERE `id` =?";  
							
							PreparedStatement psUpQuantity = conn.prepareStatement(strUpdateQuantity);
							psUpQuantity.setString(1, String.valueOf(newKgs));
							psUpQuantity.setString(2, strID);
							psUpQuantity.executeUpdate();
						}
						break;
						
						case "Cm":{
							int cm = Integer.parseInt(strQuantity);
							
							//SELECT medicine boxes
							String strSelectQuantity = "SELECT `cm` FROM quantity WHERE `id` = '"+strID+"'";
							ResultSet rsQuantity = stat.executeQuery(strSelectQuantity);
							rsQuantity.next();
							
							int validCm = Integer.parseInt(rsQuantity.getString("cm"));
							int newCm = validCm - cm;
							
							String strUpdateQuantity ="UPDATE `quantity` set `cm`=? WHERE `id` =?";  
							
							PreparedStatement psUpQuantity = conn.prepareStatement(strUpdateQuantity);
							psUpQuantity.setInt(1, newCm);
							psUpQuantity.setString(2, strID);
							psUpQuantity.executeUpdate();
						}
						break;
		
						default:
							break;
					}
				
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
    	Scene scene = new Scene(root, 383, 398 + ((arrNodes.size() - 1) * 20));
    	stage.setScene(scene);
    	stage.initStyle(StageStyle.TRANSPARENT);
 
    	stage.show();
    }
}