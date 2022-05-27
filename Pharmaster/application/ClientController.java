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

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ClientController {
	
	double x, y;

    @FXML
    private BorderPane client_page;

    @FXML
    private FontAwesomeIconView icon_iconize;

    @FXML
    private FontAwesomeIconView icon_fullscreen;

    @FXML
    private FontAwesomeIconView icon_close;

    @FXML
    BorderPane borderPaneContent;

    @FXML
    Label employeeName;

    @FXML
    ImageView employeeImage;
    
    @FXML
    private Button btnProfile;
    
    @FXML
    BorderPane borderPaneCart;
    
    @FXML
    Label countOfItems;

    @FXML
    Label totalPrice;

    @FXML
    VBox vboxCartItems;
        
    String clientUsername;
    double loginTime;
    
    @FXML
    void close(MouseEvent event) throws SQLException {
    	
    	Date date = new Date();						//2020-06-25 21:28:26
		DateFormat dateFormate = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss a");
		String logoutTimt = dateFormate.format(date);
		logoutTimt = logoutTimt.replace("/","-");
		
		String updateLogout ="UPDATE `log` SET `logout`=?  WHERE `no`=?" ;  
		Connection conn=DBinfo.connDB();
		PreparedStatement ps = conn.prepareStatement(updateLogout);
		ps.setString(1, logoutTimt);
		ps.setDouble(2, loginTime);
		ps.executeUpdate();
    	
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
    void max(MouseEvent event) {
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	stage.setFullScreenExitHint("");
    	if(!stage.isFullScreen()) {
    		stage.setFullScreen(true);
    	}else {
    		stage.setFullScreen(false);
    	}
    }

    @FXML
    void min(MouseEvent event) {
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	stage.setIconified(true);
    }
    
    void setEmployeeImage(String str) {
    	employeeImage.setImage(new Image(str));
    }
    
    void setDashboard() throws IOException {
    	
    	Date date = new Date();
		DateFormat dateFormate = new SimpleDateFormat("yyyy/MM/dd");
		String dateTimt = dateFormate.format(date);
		dateTimt = dateTimt.replace("/","-");
    	
		//view all medicines
		String strSelect = "SELECT `id` FROM medicines WHERE `DOE` > '"+dateTimt+"'";

		FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/ViewMedicine.fxml"));
		Parent rootViewMedicine = loaderViewMedicine.load();
		
		SearchMedicineController searchMedicineController = loaderViewMedicine.getController();
		searchMedicineController.key = "getTotalMedicines";
		searchMedicineController.isClient = true;
		searchMedicineController.employeeUsername = clientUsername;
		searchMedicineController.getMedicines(strSelect);
		searchMedicineController.flowPaneContent.setHgap(10);
		searchMedicineController.flowPaneContent.setVgap(10);
		searchMedicineController.flowPaneContent.setPadding(new Insets(10,0,0,10));
		searchMedicineController.borderPaneSearch.setPrefSize(880,570);
		
		borderPaneContent.setCenter(rootViewMedicine);
    }
                
    void setCartItem(Label item) throws IOException {
    	vboxCartItems.getChildren().add(item);
    }
    
    @FXML
    void getBill(MouseEvent event) throws IOException, SQLException {
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
		    	
		    	Label lblQuantity = (Label) arrItemDetails.get(1);
		    	String strQuantity = lblQuantity.getText();
		    	int intQuantity = Integer.parseInt(strQuantity);

		    	Label lblUnit = (Label) arrItemDetails.get(2);
		    	String strUnit = lblUnit.getText();
		    	
		    	Label lblPrice = (Label) arrItemDetails.get(3);
		    	String strPrice = lblPrice.getText();
		    	
		    	Label lblID = (Label) arrItemDetails.get(4);
		    	String strID = lblID.getText();
		    	
		    	String billNumber = String.valueOf(date.getTime());
		    	String username = clientUsername;
		    	
		    	String strBoxes = "0";
		    	String strStripes = "0";
		    	String strUnits = "0";
		    	
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
	
					default:
						break;
				}
		    	
				try {
					Connection conn = DBinfo.connDB();
					Statement stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
					
					String sql="INSERT INTO `sales`(`NO`,`ID`,`BOXES`, `STRIPES`,"
							+ "`UNITS`,`PRICE`,`EMP_USERNAME`) "
							+ "VALUES (?,?,?,?,?,?,?)";
					
					PreparedStatement ps = conn.prepareStatement(sql);

					ps.setString(1, billNumber);
					ps.setString(2, strID);
					ps.setString(3, strBoxes);
					ps.setString(4, strStripes);
					ps.setString(5, strUnits);
					ps.setString(6, strPrice);
					ps.setString(7, username);
					ps.executeUpdate();
					
					System.out.println("Inserted to sales...");
					
					//Update medicine quantity
					
					//SELECT medicine type
					String strSelectSuppID = "SELECT `type` FROM medicines WHERE `id` = '"+strID+"'";
					ResultSet rs = stat.executeQuery(strSelectSuppID);
					rs.first();
			    	
					switch (rs.getString("type")) {
						case "Tablets":{
						    switch (strUnit) {
								case "Boxes":{
									int boxes = Integer.parseInt(strQuantity);
									
									//SELECT medicine boxes
									String strSelectQuantity = "SELECT `BOXES` FROM quantity WHERE `id` = '"+strID+"'";
									ResultSet rsQuantity = stat.executeQuery(strSelectQuantity);
									rsQuantity.first();
									
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
									rsQuantity.first();
									
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
									rsQuantity.first();
									
									int validBoxes = Integer.parseInt(rsQuantity.getString("BOXES"));
									int validStripes = Integer.parseInt(rsQuantity.getString("STRIPES"));
									int validUnits = Integer.parseInt(rsQuantity.getString("UNITS"));

									//SELECT medicine Stripes, units
									String strSelectQuantityDetails = "SELECT `STRIPES`,`UNITS` FROM medicines WHERE `id` = '"+strID+"'";
									ResultSet rsQuantityDetails = stat.executeQuery(strSelectQuantityDetails);
									rsQuantityDetails.first();
									
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
						
						case "Lequids":{
							int boxes = Integer.parseInt(strQuantity);
							
							//SELECT medicine boxes
							String strSelectQuantity = "SELECT `BOXES` FROM quantity WHERE `id` = '"+strID+"'";
							ResultSet rsQuantity = stat.executeQuery(strSelectQuantity);
							rsQuantity.first();
							
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
									rsQuantity.first();
									
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
									rsQuantity.first();
									
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
									rsQuantity.first();
									
									int validBoxes = Integer.parseInt(rsQuantity.getString("BOXES"));
									int validStripes = Integer.parseInt(rsQuantity.getString("STRIPES"));
									int validUnits = Integer.parseInt(rsQuantity.getString("UNITS"));

									//SELECT medicine Stripes, units
									String strSelectQuantityDetails = "SELECT `STRIPES`,`UNITS` FROM medicines WHERE `id` = '"+strID+"'";
									ResultSet rsQuantityDetails = stat.executeQuery(strSelectQuantityDetails);
									rsQuantityDetails.first();
									
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
								rsQuantity.first();
								
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
								rsQuantity.first();
								
								int validBoxes = Integer.parseInt(rsQuantity.getString("BOXES"));
								int validUnits = Integer.parseInt(rsQuantity.getString("UNITS"));
								
								//SELECT medicine Stripes, units
								String strSelectQuantityDetails = "SELECT `UNITS` FROM medicines WHERE `id` = '"+strID+"'";
								ResultSet rsQuantityDetails = stat.executeQuery(strSelectQuantityDetails);
								rsQuantityDetails.first();
								
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
		
						default:
							break;
					}
				
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		}
    	//clear the cart
    	vboxCartItems.getChildren().remove(1, arrNodes.size());
    	countOfItems.setText("0");
    	totalPrice.setText("0.0");
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Bill.fxml"));
    	Parent root = loader.load();
    	BillController billController = loader.getController();
    	billController.setBill(time);
    	
    	Stage stage = new Stage();
    	Scene scene = new Scene(root, 383, 340 + ((arrNodes.size() - 1) * 30));
    	stage.setScene(scene);
    	stage.initStyle(StageStyle.TRANSPARENT);
 
    	stage.show();
    	
    }
    
    @SuppressWarnings("deprecation")
	@FXML
    void getProfile(MouseEvent event) throws IOException {
		Stage primaryStage = new Stage();
		FXMLLoader loaderModifyUser = new FXMLLoader(getClass().getResource("/AdminPages/ModifyUser.fxml"));
		Parent root = loaderModifyUser.load();
		ModifyUserController modifyUserController = loaderModifyUser.getController();
		modifyUserController.jobtitle = "user";
		modifyUserController.username = clientUsername;
		modifyUserController.setProfile(employeeImage.getImage().impl_getUrl());
		
		Scene scene = new Scene(root,743,549);
		primaryStage.setResizable(true);
		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.show();
    }
    
    @FXML
    void getLogOut(MouseEvent event) throws IOException, SQLException {
		Stage primaryStage = new Stage();
    	Parent root=FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
		Scene scene = new Scene(root,484,623);
		scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
		primaryStage.setResizable(true);
		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.show();
		
		Date date = new Date();						//2020-06-25 21:28:26
		DateFormat dateFormate = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss a");
		String logoutTimt = dateFormate.format(date);
		logoutTimt = logoutTimt.replace("/","-");
		
		String updateLogout ="UPDATE `log` SET `logout`=?  WHERE `no`=?" ;  
		Connection conn = DBinfo.connDB();
		PreparedStatement ps = conn.prepareStatement(updateLogout);
		ps.setString(1, logoutTimt);
		ps.setDouble(2, loginTime);
		ps.executeUpdate();
		
		Stage oldStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		oldStage.close();
		
    }
    
    public void errException(SQLException e) {
		System.out.println("Error: "+e.getMessage());
		System.out.println("code: "+e.getErrorCode());
		System.out.println("state: "+e.getSQLState());
		System.out.println("message: "+e.getLocalizedMessage());
		
	}
        
}