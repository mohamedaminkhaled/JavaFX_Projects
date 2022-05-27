package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DashboardController {

    @FXML
    BorderPane borderDshboard;
	
	@FXML
    private Label welcomMessage;
    
    @FXML
    ImageView image;

    @FXML
    private Label username;

    @FXML
    private Label totalMedicine;

    @FXML
    private Button btnTotalMedicine;

    @FXML
    private Label expiaryThisMonth;

    @FXML
    private Button btnExpiaryThisMonth;

    @FXML
    private Label outOfStock;

    @FXML
    private Button btnOutOfStock;

    @FXML
    private Label totalSales;

    @FXML
    private Button btnTotalSales;

    @FXML
    private Label similarCompanies;

    @FXML
    private Button btnsimilarCompanies;
    
    @FXML
    Label totalPurchases;
    
    @FXML
    Label finished;
    
    String immmagePath;
    
    @FXML
    void changeName(MouseEvent event) throws SQLException {
    	Connection conn = null;
    	
    	try {
	    	Stage stage=new Stage();
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/ChangeName.fxml"));
			Parent root = loader.load();
			
			conn=DBinfo.connDB();
			
			//Select client name
			Statement stat = conn.createStatement();
			String strSelectUsers = "SELECT `name` FROM `employees` WHERE `username` = '"+username.getText()+"'";
			ResultSet rs = stat.executeQuery(strSelectUsers);
			rs.next();
			
			ChangeNameController changeNameController = loader.getController();
			changeNameController.setCurrentName(rs.getString("name"));
			changeNameController.adminUserName = username.getText();
			
			Scene scene=new Scene(root,630,266);
			stage.setScene(scene);
			stage.initStyle(StageStyle.TRANSPARENT);
			stage.show();
		
    	} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
	
    }
    
	@FXML
    void getProfile(MouseEvent event) throws IOException {
		Stage primaryStage = new Stage();
		FXMLLoader loaderModifyUser = new FXMLLoader(getClass().getResource("/AdminPages/ModifyUser.fxml"));
		Parent root = loaderModifyUser.load();
		ModifyUserController modifyUserController = loaderModifyUser.getController();
		modifyUserController.jobtitle = "admin";
		modifyUserController.username = username.getText();
		modifyUserController.setProfile(immmagePath);
		
    	Scene scene = new Scene(root,743,549);
		primaryStage.setResizable(true);
		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.show();
    }
        
    @FXML
    void showMenue(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
    	Stage stage = new Stage();
    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/SearchItems.fxml"));
    	Parent root = loaderViewMedicine.load();
    	
    	String strSelectItems = "SELECT * FROM `items` ORDER BY `sold` DESC"; 
    	
    	SearchItemsController searchItemsController = loaderViewMedicine.getController();
    	searchItemsController.flowPaneContent.setHgap(10);
		searchItemsController.flowPaneContent.setVgap(10);
		searchItemsController.flowPaneContent.setPadding(new Insets(10,0,0,10));
    	searchItemsController.borderPaneSearch.setPrefSize(1180, 622);
    	
    	//load Cart page on the right of search pane
    	FXMLLoader loaderCart = new FXMLLoader(getClass().getResource("/fxml/Cart.fxml"));
    	Parent rootCart = loaderCart.load();
    	CartController cartController = loaderCart.getController();
    	SellStockController.cartController = cartController;
    	SellStockController.jobtitle = "admin";
    	cartController.username = username.getText();
    	
    	searchItemsController.borderPaneSearch.setRight(rootCart);
    	
    	searchItemsController.key = "getItems";
    	searchItemsController.employeeUsername = username.getText();
    	searchItemsController.getItems(strSelectItems);
    	
    	Scene scene=new Scene(root,1180,717);
		stage.setScene(scene);
		stage.show();
    }

    @FXML
    void salesReport(MouseEvent event) throws IOException, SQLException {
    	Stage stage = new Stage();
    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/SearchReport.fxml"));
    	Parent root = loaderViewMedicine.load();
    	    	
    	Scene scene=new Scene(root,670,570);
		stage.setScene(scene);
		stage.initStyle(StageStyle.UTILITY);
		stage.show();
    }
    
    @FXML
    void purchaseReport(MouseEvent event) throws IOException, SQLException {
    	Stage stage = new Stage();
    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/SearchPurchaseReport.fxml"));
    	Parent root = loaderViewMedicine.load();
    	    	
    	Scene scene=new Scene(root,670,570);
		stage.setScene(scene);
		stage.initStyle(StageStyle.UTILITY);
		stage.show();
    }
    
    String getTimeMore() {
    	Date date = new Date();
		DateFormat dateFormate = new SimpleDateFormat("YYYY/MM/dd");
		String dateTimt = dateFormate.format(date);
		dateTimt = dateTimt.replace("/","-");
    	return dateTimt;
    }
    
    String getTimeLessOrEqual() {
    	Date date = new Date();
		DateFormat dateFormate = new SimpleDateFormat("yyyy/MM/dd");
		DateFormat dateFormateDay = new SimpleDateFormat("dd");
		String day = dateFormateDay.format(date);
		int intDay = Integer.parseInt(day);
		
    	String strFullDate = dateFormate.format(date);
		String strYear = strFullDate.substring(0, 4);
		int intYear = Integer.parseInt(strYear);
    	
    	@SuppressWarnings("deprecation")
		GregorianCalendar gc2 = new GregorianCalendar(intYear, date.getMonth(), intDay+30);
    	date = gc2.getTime();
    	String gcTimeLessOrEqual = dateFormate.format(date);
    	gcTimeLessOrEqual = gcTimeLessOrEqual.replace("/","-");
    	return gcTimeLessOrEqual;
    }
    
    String getDate() {
    	Date date = new Date();
		DateFormat dateFormate = new SimpleDateFormat("yyyy/MM/dd");
		String dateTimt = dateFormate.format(date);
		dateTimt = dateTimt.replace("/","-");
		return dateTimt;
    }

    @FXML
    void salesBills(MouseEvent event) throws IOException, ClassNotFoundException {
    	
    		Stage stage = new Stage();
	    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/SearchItems.fxml"));
	    	Parent root = loaderViewMedicine.load();
	    
	    	try {
	    	
		    	SearchItemsController searchItemsController = loaderViewMedicine.getController();
		    	searchItemsController.key = "getTotalSales";
		    	searchItemsController.datePickerSearch.setVisible(true);
		    	
		    	Statement state;
				ResultSet rs;
				Connection conn = DBinfo.connDB();
				state=conn.createStatement();
				
				String selectJobTitle = "SELECT `jobtitle` FROM employees WHERE `username` = '"+username.getText()+"'";			
				rs = state.executeQuery(selectJobTitle);
				rs.next();
				if(rs.getString("jobtitle").equals("admin")) {
					searchItemsController.hboxTop.getChildren().remove(searchItemsController.btnBills);
					searchItemsController.hboxTop.getChildren().remove(searchItemsController.tfSearch);
				}
				
	
				String selectSalesNum = "SELECT DISTINCT `bill_number` FROM item_sales "
						+ "ORDER BY `bill_number` DESC LIMIT 20";
				rs = state.executeQuery(selectSalesNum);
				
				while(rs.next()) {
					FXMLLoader loaderBill = new FXMLLoader(getClass().getResource("/fxml/Bill.fxml"));
					Parent root2 = loaderBill.load();
	
					BillController billController = loaderBill.getController();
					billController.BorderPaneBill.setTop(null);
					billController.setBill(rs.getString("bill_number"));
					billController.user.setStyle("-fx-font-size: 12");
					billController.billNumber.setStyle("-fx-font-size: 12");
					billController.billName.setStyle("-fx-font-size: 12");
					billController.discName.setStyle("-fx-font-size: 12");
					billController.orderName.setStyle("-fx-font-size: 12");
					billController.priceName.setStyle("-fx-font-size: 12");
					billController.quantName.setStyle("-fx-font-size: 12");
					billController.timaName.setStyle("-fx-font-size: 12");
					billController.totalName.setStyle("-fx-font-size: 12");
					billController.userName.setStyle("-fx-font-size: 12");
					billController.time.setStyle("-fx-font-size: 12");
					billController.total.setStyle("-fx-font-size: 12");
					
					billController.BorderPaneBill.setPrefHeight(
							billController.BorderPaneBill.getPrefHeight() + 
							billController.vboxBillContent.getPrefHeight() - 110);
					
					searchItemsController.flowPaneContent.setOrientation(Orientation.VERTICAL);
					searchItemsController.flowPaneContent.getChildren().addAll(root2);
				}
				
		} catch (SQLException e) {
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		}
		
    	
    	Scene scene=new Scene(root,1000,570);
		stage.setScene(scene);
		//stage.initStyle(StageStyle.UTILITY);
		stage.show();
    }
    
    @FXML
    void purchasesBills(MouseEvent event) throws IOException, ClassNotFoundException {
		Stage stage = new Stage();
    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/SearchItems.fxml"));
    	Parent root = loaderViewMedicine.load();
    
    	try {
    	
	    	SearchItemsController searchItemsController = loaderViewMedicine.getController();
	    	searchItemsController.key = "getTotalPurchases";
	    	searchItemsController.datePickerSearch.setVisible(true);
	    	
	    	Statement state;
			ResultSet rs;
			Connection conn = DBinfo.connDB();
			state=conn.createStatement();
			
			String selectJobTitle = "SELECT `jobtitle` FROM employees WHERE `username` = '"+username.getText()+"'";			
			rs = state.executeQuery(selectJobTitle);
			rs.next();
			if(rs.getString("jobtitle").equals("admin")) {
				searchItemsController.hboxTop.getChildren().remove(searchItemsController.btnBills);
				searchItemsController.hboxTop.getChildren().remove(searchItemsController.tfSearch);
			}
			

			String selectSalesNum = "SELECT DISTINCT `bill_number` FROM goods_purchses "
					+ "ORDER BY `bill_number` DESC LIMIT 20";			
			rs = state.executeQuery(selectSalesNum);
			
			while(rs.next()) {
				FXMLLoader loaderBill = new FXMLLoader(getClass().getResource("/fxml/PurchaseBill.fxml"));
				Parent root2 = loaderBill.load();

				PurchaseBillController billController = loaderBill.getController();
				billController.BorderPaneBill.setTop(null);
				billController.setBill(rs.getDouble("bill_number"));
				billController.user.setStyle("-fx-font-size: 10");
				billController.billNumber.setStyle("-fx-font-size: 10");
				billController.billName.setStyle("-fx-font-size: 10");
				billController.priceName.setStyle("-fx-font-size: 10");
				billController.quantName.setStyle("-fx-font-size: 10");
				billController.totalName.setStyle("-fx-font-size: 10");
				billController.userName.setStyle("-fx-font-size: 10");
				billController.time.setStyle("-fx-font-size: 10");
				billController.total.setStyle("-fx-font-size: 10");
				billController.goodName.setStyle("-fx-font-size: 10");
				billController.suppName.setStyle("-fx-font-size: 10");
				billController.timeName.setStyle("-fx-font-size: 10");

				billController.BorderPaneBill.setPrefHeight(
						billController.BorderPaneBill.getPrefHeight() + 
						billController.vboxBillContent.getPrefHeight() - 110);
				
				searchItemsController.flowPaneContent.setOrientation(Orientation.VERTICAL);
				searchItemsController.flowPaneContent.getChildren().addAll(root2);
			}
			
		} catch (SQLException e) {
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		}
	
	
	Scene scene=new Scene(root,1000,570);
	stage.setScene(scene);
	//stage.initStyle(StageStyle.UTILITY);
	stage.show();
    }
    
    @FXML
    void paneEnterd(MouseEvent event) {
    	totalSales.setVisible(true);
    }

    @FXML
    void paneExited(MouseEvent event) {
    	totalSales.setVisible(false);
    }
    
    @FXML
    void puurchaseEntered(MouseEvent event) {
    	totalPurchases.setVisible(true);
    }
    
    @FXML
    void purchaseExited(MouseEvent event) {
    	totalPurchases.setVisible(false);
    }
    
    void setWelcomeMessage(String str) {
    	welcomMessage.setText(str);
    }
    
    void setUsername(String str) {
    	username.setText(str);
    }
    
    void setUserImage(String str) {
    	image.setImage(new Image(str));
    }
        
    void setTotalSales(String str) {
    	totalSales.setText(str);
    }
}
