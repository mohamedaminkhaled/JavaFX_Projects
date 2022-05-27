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
    
    String imagepath;
    
    @FXML
    void close(MouseEvent event) throws SQLException {
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
    
    void setDashboard() throws IOException, ClassNotFoundException, SQLException {
    	
    	Date date = new Date();
		DateFormat dateFormate = new SimpleDateFormat("yyyy/MM/dd");
		String dateTimt = dateFormate.format(date);
		dateTimt = dateTimt.replace("/","-");
    	
		//view all medicines
		String strSelect = "SELECT * FROM `items` ORDER BY `sold` DESC";

		FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/SearchItems.fxml"));
		Parent rootViewMedicine = loaderViewMedicine.load();
		
		SearchItemsController searchItemsController = loaderViewMedicine.getController();
		searchItemsController.key = "getItems";
		searchItemsController.isClient = true;
		searchItemsController.employeeUsername = clientUsername;
		searchItemsController.getItems(strSelect);
		searchItemsController.flowPaneContent.setHgap(10);
		searchItemsController.flowPaneContent.setVgap(10);
		searchItemsController.flowPaneContent.setPadding(new Insets(10,0,0,10));
		searchItemsController.borderPaneSearch.setPrefSize(880,570);
		
		borderPaneContent.setCenter(rootViewMedicine);
    }
                
    void setCartItem(Label item) throws IOException {
    	vboxCartItems.getChildren().add(item);
    }
    
    @FXML
    void getBill(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {
    	Date date = new Date();
		double time = date.getTime();
		String strTime = String.valueOf(time);
    	
		Connection conn = null;
		
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
		    	int intQuantity = Integer.parseInt(strQuantity);

		    	Label lblDiscount = (Label) arrItemDetails.get(2);
		    	String strDiscount = lblDiscount.getText();
		    	
		    	Label lblPrice = (Label) arrItemDetails.get(3);
		    	String strPrice = lblPrice.getText();		    	
		    	
				try {
					conn = DBinfo.connDB();
					
					//ex. 2020-06-25 21:28:26
					DateFormat dateFormate = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss a");
					String dateTimt = dateFormate.format(date);
					dateTimt = dateTimt.replace("/","-");
										
					String sql="INSERT INTO `item_sales`(`bill_number`, `time`, `cost`, `quantity`,"
							+ " `discount`, `item_name`, `emp_username`) "
							+ "VALUES (?,?,?,?,?,?,?)";
					
					PreparedStatement ps = conn.prepareStatement(sql);

					ps.setString(1, strTime);
					ps.setString(2, dateTimt);
					ps.setString(3, strPrice);
					ps.setString(4, strQuantity);
					ps.setString(5, strDiscount);
					ps.setString(6, strName);
					ps.setString(7, clientUsername);
					ps.executeUpdate();
					
					System.out.println("Inserted to sales...");
					
					//Update item quantity
					
					//SELECT item sold quantity
					Statement stat = conn.createStatement();
					String strSelectSold = "SELECT `sold` FROM items WHERE `name` = '"+strName+"'";
					ResultSet rs = stat.executeQuery(strSelectSold);
					rs.next();
					
					int intSold = rs.getInt("sold");
					int newSold = intSold + intQuantity;
					
					String strUpdateQuantity ="UPDATE `items` set `sold`=? WHERE `name` =?";  
					
					PreparedStatement psUpQuantity = conn.prepareStatement(strUpdateQuantity);
					psUpQuantity.setInt(1, newSold);
					psUpQuantity.setString(2, strName);
					psUpQuantity.executeUpdate();
					
				}
				catch (SQLException e) {
					e.printStackTrace();
				} finally {
					conn.close();
				}
			}
		}
    	conn.close();
    	
    	//clear the cart
    	vboxCartItems.getChildren().remove(1, arrNodes.size());    	
    	countOfItems.setText("0");
    	totalPrice.setText("0.0");
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Bill.fxml"));
    	Parent root = loader.load();
    	BillController billController = loader.getController();
    	billController.setBill(strTime);
    	
    	Stage stage = new Stage();
    	Scene scene = new Scene(root, 205, 417 + ((arrNodes.size() - 1) * 30));
    	stage.setScene(scene);
    	stage.initStyle(StageStyle.TRANSPARENT);
 
    	stage.show();
    	
    }
    
	@FXML
    void getProfile(MouseEvent event) throws IOException {
		Stage primaryStage = new Stage();
		FXMLLoader loaderModifyUser = new FXMLLoader(getClass().getResource("/AdminPages/ModifyUser.fxml"));
		Parent root = loaderModifyUser.load();
		ModifyUserController modifyUserController = loaderModifyUser.getController();
		modifyUserController.jobtitle = "user";
		modifyUserController.username = clientUsername;
		modifyUserController.setProfile(imagepath);
		
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
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
		primaryStage.setResizable(true);
		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.show();
		
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