package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AdminController {
	
	double x, y;

    @FXML
    private BorderPane adminPage;

    @FXML
    private BorderPane borderPaneContent;

    @FXML
    private HBox hboxDashboard;

    @FXML
    private HBox hboxRegisterUser;

    @FXML
    private HBox hboxModifyUser;

    @FXML
    private HBox hboxViewUsers;
    
    @FXML
    private HBox hBoxAddCompany;

    @FXML
    private HBox hBoxAddSupplier;

    @FXML
    private HBox hboxExitApp;

    @FXML
    private Label adminName;

    @FXML
    private Label adminUsername;
    
    String imageURL;
    double loginTime;
    
    FXMLLoader loader;
    Stage stage;
    Parent root;
    Statement state;
	ResultSet rs;
	Connection conn;

    @FXML
    void close(MouseEvent event) throws SQLException {    	
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	stage.close();
    }
    
    @FXML
    void pressed(MouseEvent event) {
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	x = event.getScreenX() - stage.getX();
    	y = event.getScreenY() - stage.getY();
    }
    
    @FXML
    void dragged(MouseEvent event) {
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	stage.setX(event.getScreenX() -x);
    	stage.setY(event.getScreenY() -y);
    }
    
    @FXML
    void max(MouseEvent event) {
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	stage.setFullScreenExitHint("");
    	if(!stage.isFullScreen()) {
    		stage.setFullScreen(true);
    	}else {
    		stage.setFullScreen(false);
    	}
    }

    @FXML
    void min(MouseEvent event) {
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	stage.setIconified(true);
    }
    
    @FXML
    void setDashboard() throws IOException, ClassNotFoundException, SQLException {
		try {
			conn=DBinfo.connDB();
			state=conn.createStatement();
			    	
			loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
			root = loader.load();
			
			// select image of employees
			String strSelectImage = "SELECT `image` FROM employees WHERE `username` = '"+adminUsername.getText()+"'";
			rs=state.executeQuery(strSelectImage);
			rs.next();
			imageURL = rs.getString("image");
			
			DashboardController dashboardController = loader.getController();
			
			ModifyUserController.dashboardController = dashboardController;
			
			dashboardController.setWelcomeMessage("Welcom Dear Admin  "+adminName.getText());
			dashboardController.setUsername(adminUsername.getText());
			dashboardController.setUserImage(imageURL);
			dashboardController.immmagePath = imageURL;
			dashboardController.setUsername(adminUsername.getText());
		
			// Set total sales
			double totalSales = 0.0;
			
			//Display double with max two decimal digits
		    DecimalFormat df = new DecimalFormat("#.##");
			
			String selectTypeAndPrice = "SELECT * FROM item_sales";
			rs = state.executeQuery(selectTypeAndPrice);
	
			while(rs.next()) {
				double doublePrice = rs.getDouble("cost");
				totalSales += doublePrice;
			}
			dashboardController.setTotalSales(String.valueOf(df.format(totalSales)));
			
			// Set total purchases			
			String selectTypePrice = "SELECT * FROM goods_purchses";
			rs = state.executeQuery(selectTypePrice);
			
			double doubleTotalPurchases = 0.0;
			
			while(rs.next()) {
				double doublePrice = rs.getDouble("cost");
				doubleTotalPurchases += doublePrice;
			}
			
			dashboardController.totalPurchases.setText(String.valueOf(df.format(doubleTotalPurchases)));

		
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		} finally {
			conn.close();
		}
    	
    	borderPaneContent.setCenter(root);
    }
    
    @FXML
    void getViewUsers(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
		String strSelect = "SELECT `id` FROM employees";

    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/SearchItems.fxml"));
    	Parent root = loaderViewMedicine.load();
    	
    	SearchItemsController searchItemsController = loaderViewMedicine.getController();
    	searchItemsController.key = "getViewUsers";
    	searchItemsController.flowPaneContent.setHgap(20);
		searchItemsController.flowPaneContent.setVgap(20);
		searchItemsController.flowPaneContent.setPadding(new Insets(20,0,0,50));
    	searchItemsController.getUsers(strSelect);
    	
    	borderPaneContent.setCenter(root);
    }
    
    @FXML
    void getPurchseGoods(MouseEvent event) throws IOException {

    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/AdminPages/PurchaseGood.fxml"));
    	Parent root = loaderViewMedicine.load();
    	
    	PurchaseGoodController addMedicineController = loaderViewMedicine.getController();
    	addMedicineController.username = adminUsername.getText();
    	
    	//load Cart page on the right of search pane
    	FXMLLoader loaderCart = new FXMLLoader(getClass().getResource("/fxml/PurchaseCart.fxml"));
    	Parent rootCart = loaderCart.load();
    	PurchaseCartController purchaseCartController = loaderCart.getController();
    	purchaseCartController.username = adminUsername.getText();
    	purchaseCartController.borderCart.setPrefHeight(
    			addMedicineController.borderAddMedicine.getPrefHeight());
    	
    	addMedicineController.purchaseCartController = purchaseCartController;
    	addMedicineController.borderAddMedicine.setRight(rootCart);
    	
    	
    	borderPaneContent.setCenter(root);
    }
    
    @FXML
    void getRegisterUser(MouseEvent event) {
    	loadPage("/AdminPages/RegisterUser");
    }
    
    @FXML
    void getAddItem(MouseEvent event) throws IOException, SQLException {
    	    	
    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/AdminPages/AddItem.fxml"));
    	Parent root = loaderViewMedicine.load();
    	
    	AddItemController addCompanyController = loaderViewMedicine.getController();
    	addCompanyController.adminUsername = adminUsername.getText();

    	borderPaneContent.setCenter(root);
    }
     
    @FXML
    void backup(MouseEvent event) throws SQLException, IOException {

    	File dbFile = new File("caffemaster_db.sqlite");
    	File outputFile = new File("D:\\CaffeMaster\\Backups\\caffemaster_db.sqlite");
    	
    	outputFile.delete();
    	
    	Files.copy(dbFile.toPath(), outputFile.toPath());
    	
    }
    
    @FXML
    void restore(MouseEvent event) throws IOException {
    	
    	File outputFile = new File("caffemaster_db.sqlite");
    	File dbFile = new File("D:\\CaffeMaster\\Backups\\caffemaster_db.sqlite");
    	
    	outputFile.delete();
    	
    	Files.copy(dbFile.toPath(), outputFile.toPath());
    	
    }
    
    @FXML
    void getLogOut(MouseEvent event) throws IOException, SQLException {
		Stage primaryStage = new Stage();
    	Parent root=FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
		Scene scene = new Scene(root,448,652);
		scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
		primaryStage.setResizable(true);
		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		//primaryStage.setOpacity(0.9);	
		primaryStage.show();
		
		Stage oldStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		oldStage.close();
    }
  
    void setAdminName(String str) {
    	adminName.setText(str);
    }
    
    void setAdminUsername(String str) {
    	adminUsername.setText(str);
    }
     
    void loadPage(String ui) {
    	Parent root = null;
    	try {
			root=FXMLLoader.load(getClass().getResource(ui+".fxml"));
			
    	}catch(Exception e) {
			e.printStackTrace();
		}
    	borderPaneContent.setCenter(root);
    }

}
