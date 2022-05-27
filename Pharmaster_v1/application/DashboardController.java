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
    
    @SuppressWarnings("deprecation")
	@FXML
    void getProfile(MouseEvent event) throws IOException {
		Stage primaryStage = new Stage();
		FXMLLoader loaderModifyUser = new FXMLLoader(getClass().getResource("/AdminPages/ModifyUser.fxml"));
		Parent root = loaderModifyUser.load();
		ModifyUserController modifyUserController = loaderModifyUser.getController();
		modifyUserController.jobtitle = "admin";
		modifyUserController.username = username.getText();
		modifyUserController.setProfile(image.getImage().impl_getUrl());

    	Scene scene = new Scene(root,743,549);
		primaryStage.setResizable(true);
		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.show();
    }
        
    @FXML
    void getExpiarythisMonth(MouseEvent event) throws IOException {
    	Stage stage = new Stage();
    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/ViewMedicine.fxml"));
    	Parent root = loaderViewMedicine.load();
    	
    	String gcTimeMore = getTimeMore();
    	String gcTimeLessOrEqual = getTimeLessOrEqual();
    	String strSelectExpiaryThisMonth = "SELECT * FROM `medicines` WHERE (`DOE` >= '"+gcTimeMore+"') AND (`DOE` <= '"+gcTimeLessOrEqual+"')"; 
    	
    	SearchMedicineController searchMedicineController = loaderViewMedicine.getController();
    	searchMedicineController.flowPaneContent.setHgap(10);
		searchMedicineController.flowPaneContent.setVgap(10);
		searchMedicineController.flowPaneContent.setPadding(new Insets(10,0,0,10));
    	searchMedicineController.borderPaneSearch.setPrefSize(1180, 622);
    	
    	//load Cart page on the right of search pane
    	FXMLLoader loaderCart = new FXMLLoader(getClass().getResource("/fxml/Cart.fxml"));
    	Parent rootCart = loaderCart.load();
    	CartController cartController = loaderCart.getController();
    	SellStockController.cartController = cartController;
    	SellStockController.jobtitle = "admin";
    	
    	searchMedicineController.borderPaneSearch.setRight(rootCart);
    	
    	searchMedicineController.key = "getExpiarythisMonth";
    	searchMedicineController.employeeUsername = username.getText();
    	searchMedicineController.getMedicines(strSelectExpiaryThisMonth);
    	
    	Scene scene=new Scene(root,1180,717);
		stage.setScene(scene);
		//stage.initStyle(StageStyle.UTILITY);
		stage.show();
    }

    @FXML
    void getOutOfStock(MouseEvent event) throws IOException {
    	Stage stage = new Stage();
    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/ViewMedicine.fxml"));
    	Parent root = loaderViewMedicine.load();
    	
    	String dateTimt = getDate();
		String strSelectOutOfStock = "SELECT * FROM `medicines` WHERE `DOE` < '"+dateTimt+"'";

    	SearchMedicineController searchMedicineController = loaderViewMedicine.getController();
    	searchMedicineController.key = "getOutOfStock";
    	searchMedicineController.isOutOfStock = true;
    	searchMedicineController.getMedicines(strSelectOutOfStock);
    	
    	Scene scene=new Scene(root,1000,570);
		stage.setScene(scene);
		//stage.initStyle(StageStyle.UTILITY);
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
    void getTotalMedicines(MouseEvent event) throws IOException {
		
		Date date = new Date();
		DateFormat dateFormate = new SimpleDateFormat("yyyy/MM/dd");
		String dateTimt = dateFormate.format(date);
		dateTimt = dateTimt.replace("/","-");
    	
    	String strSelect = "SELECT `id` FROM medicines WHERE `DOE` > '"+dateTimt+"'";

    	Stage stage = new Stage();
    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/ViewMedicine.fxml"));
    	Parent root = loaderViewMedicine.load();
    	
    	SearchMedicineController searchMedicineController = loaderViewMedicine.getController();
    	searchMedicineController.flowPaneContent.setHgap(10);
		searchMedicineController.flowPaneContent.setVgap(10);
		searchMedicineController.flowPaneContent.setPadding(new Insets(10,0,0,10));
    	searchMedicineController.borderPaneSearch.setPrefSize(1180, 622);
    	
    	//load Cart page on the right of search pane
    	FXMLLoader loaderCart = new FXMLLoader(getClass().getResource("/fxml/Cart.fxml"));
    	Parent rootCart = loaderCart.load();
    	CartController cartController = loaderCart.getController();
    	cartController.username = username.getText();
    	SellStockController.cartController = cartController;
    	SellStockController.jobtitle = "admin";
    	
    	searchMedicineController.borderPaneSearch.setRight(rootCart);
    	
    	searchMedicineController.key = "getTotalMedicines";
    	searchMedicineController.employeeUsername = username.getText();
    	searchMedicineController.getMedicines(strSelect);
    	    	
    	Scene scene=new Scene(root,1180,717);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
    }
    
    @FXML
    void getFinished(MouseEvent event) throws IOException {
 	
    	String strSelect = "SELECT `id` FROM quantity WHERE `BOXES` = 0 "
    			+ "AND `STRIPES` = 0 AND `UNITS` = 0 ";

    	Stage stage = new Stage();
    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/ViewMedicine.fxml"));
    	Parent root = loaderViewMedicine.load();
    	
    	SearchMedicineController searchMedicineController = loaderViewMedicine.getController();
    	searchMedicineController.key = "getFinishedMedicines";
    	searchMedicineController.employeeUsername = username.getText();
    	searchMedicineController.getMedicines(strSelect);
    	    	
    	Scene scene=new Scene(root,1000,570);
		stage.setScene(scene);
		stage.show();
    }

    @FXML
    void getTotalSales(MouseEvent event) throws IOException {
    	
    		Stage stage = new Stage();
	    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/ViewMedicine.fxml"));
	    	Parent root = loaderViewMedicine.load();
	    
	    	try {
	    	
		    	SearchMedicineController searchMedicineController = loaderViewMedicine.getController();
		    	searchMedicineController.key = "getTotalSales";
		    	searchMedicineController.datePickerSearch.setVisible(true);
		    	
		    	Statement state;
				ResultSet rs;
				Connection conn = DBinfo.connDB();
				state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
				
				String selectJobTitle = "SELECT `jobtitle` FROM employees WHERE `username` = '"+username.getText()+"'";			
				rs = state.executeQuery(selectJobTitle);
				rs.first();
				if(rs.getString("jobtitle").equals("admin")) {
					searchMedicineController.hboxTop.getChildren().remove(searchMedicineController.btnBills);
					searchMedicineController.hboxTop.getChildren().remove(searchMedicineController.tfSearch);
				}
				
	
				String selectSalesNum = "SELECT DISTINCT `NO` FROM sales";			
				rs = state.executeQuery(selectSalesNum);
				
				while(rs.next()) {
					FXMLLoader loaderBill = new FXMLLoader(getClass().getResource("/fxml/Bill.fxml"));
					Parent root2 = loaderBill.load();
	
					BillController billController = loaderBill.getController();
					billController.BorderPaneBill.setTop(null);
					billController.setBill(rs.getDouble("no"));
					
					billController.BorderPaneBill.setPrefHeight(
							billController.BorderPaneBill.getPrefHeight() + 
							billController.vboxBillContent.getPrefHeight() - 110);
					
					searchMedicineController.flowPaneContent.setOrientation(Orientation.VERTICAL);
					searchMedicineController.flowPaneContent.getChildren().addAll(root2);
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
    void getTotalPurchases(MouseEvent event) throws IOException {
		Stage stage = new Stage();
    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/ViewMedicine.fxml"));
    	Parent root = loaderViewMedicine.load();
    
    	try {
    	
	    	SearchMedicineController searchMedicineController = loaderViewMedicine.getController();
	    	searchMedicineController.key = "getTotalPurchases";
	    	searchMedicineController.datePickerSearch.setVisible(true);
	    	
	    	Statement state;
			ResultSet rs;
			Connection conn = DBinfo.connDB();
			state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
			
			String selectJobTitle = "SELECT `jobtitle` FROM employees WHERE `username` = '"+username.getText()+"'";			
			rs = state.executeQuery(selectJobTitle);
			rs.first();
			if(rs.getString("jobtitle").equals("admin")) {
				searchMedicineController.hboxTop.getChildren().remove(searchMedicineController.btnBills);
				searchMedicineController.hboxTop.getChildren().remove(searchMedicineController.tfSearch);
			}
			

			String selectSalesNum = "SELECT DISTINCT `NO` FROM purchases";			
			rs = state.executeQuery(selectSalesNum);
			
			while(rs.next()) {
				FXMLLoader loaderBill = new FXMLLoader(getClass().getResource("/fxml/PurchaseBill.fxml"));
				Parent root2 = loaderBill.load();

				PurchaseBillController billController = loaderBill.getController();
				billController.BorderPaneBill.setTop(null);
				billController.setBill(rs.getDouble("no"));
				
				billController.BorderPaneBill.setPrefHeight(
						billController.BorderPaneBill.getPrefHeight() + 
						billController.vboxBillContent.getPrefHeight() - 110);
				
				searchMedicineController.flowPaneContent.setOrientation(Orientation.VERTICAL);
				
				
				searchMedicineController.flowPaneContent.getChildren().addAll(root2);
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
    
    void setWelcomeMessage(String str) {
    	welcomMessage.setText(str);
    }
    
    void setUsername(String str) {
    	username.setText(str);
    }
    
    void setUserImage(String str) {
    	image.setImage(new Image(str));
    }
    
    void setTotalMedicine(String str) {
    	totalMedicine.setText(str);
    }
    
    void setExpiaryThisMonth(String str) {
    	expiaryThisMonth.setText(str);
    }
    
    void setOutOfStock(String str) {
    	outOfStock.setText(str);
    }
    
    void setTotalSales(String str) {
    	totalSales.setText(str);
    }
}
