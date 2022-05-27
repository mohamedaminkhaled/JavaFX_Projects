package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.CallableStatement;
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
import java.util.GregorianCalendar;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private FontAwesomeIconView icon_iconize;

    @FXML
    private FontAwesomeIconView icon_fullscreen;

    @FXML
    private FontAwesomeIconView icon_close;

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
    	
    	Date date = new Date();						//2020-06-25 21:28:26
		DateFormat dateFormate = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss a");
		String logoutTimt = dateFormate.format(date);
		logoutTimt = logoutTimt.replace("/","-");
		
		String updateLogout ="UPDATE `log` SET `logout`=?  WHERE `no`=?" ;  
		conn=DBinfo.connDB();
		PreparedStatement ps = conn.prepareStatement(updateLogout);
		ps.setString(1, logoutTimt);
		ps.setDouble(2, loginTime);
		ps.executeUpdate();
    	
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
    void setDashboard() throws IOException {
		try {
			conn=DBinfo.connDB();
			state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			    	
			loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
			root = loader.load();
			
			// select image of employees
			String strSelectImage = "SELECT `image` FROM employees WHERE `username` = '"+adminUsername.getText()+"'";
			rs=state.executeQuery(strSelectImage);
			rs.first();
			imageURL = rs.getString("image");
			
			DashboardController dashboardController = loader.getController();
			
			ModifyUserController.dashboardController = dashboardController;
			
			dashboardController.setWelcomeMessage("Welcom Dear Admin  "+adminName.getText());
			dashboardController.setUsername(adminUsername.getText());
			dashboardController.setUserImage(imageURL);
			
			Date date = new Date();
			DateFormat dateFormate = new SimpleDateFormat("yyyy/MM/dd");
			String dateTimt = dateFormate.format(date);
			dateTimt = dateTimt.replace("/","-");
			
			// select count of medicines
			String strSelectTotalMedicines = "SELECT * FROM medicines WHERE `DOE` > '"+dateTimt+"'";
			rs=state.executeQuery(strSelectTotalMedicines);
			rs.last();
			int totalMedicines = rs.getRow();
			dashboardController.setTotalMedicine(String.valueOf(totalMedicines));

			// select count of finished medicines
			String strSelectFinishedMedicines = "SELECT * FROM `quantity` WHERE `BOXES` = 0 and `STRIPES` = 0 AND `UNITS` = 0";
			rs=state.executeQuery(strSelectFinishedMedicines);
			rs.last();
			int finishedMedicines = rs.getRow();
			
			dashboardController.finished.setText(String.valueOf(finishedMedicines));
			
			
			// select medicines out of stock
			String strSelectOutOfStock = "SELECT * FROM `medicines` WHERE `DOE` < '"+dateTimt+"'";
			rs=state.executeQuery(strSelectOutOfStock);
			rs.last();
			int intOutOfStock = rs.getRow();
			
			dashboardController.setOutOfStock(String.valueOf(intOutOfStock));
			
			String strFullDate = dateFormate.format(date);
			String strYear = strFullDate.substring(0, 4);
			int intYear = Integer.parseInt(strYear);

			DateFormat dateFormateDay = new SimpleDateFormat("dd");
			String day = dateFormateDay.format(date);
			int intDay = Integer.parseInt(day);
			
			GregorianCalendar gc2 = new GregorianCalendar(intYear, date.getMonth(), intDay+30);
			date = gc2.getTime();
			String gcTimeLessOrEqual = dateFormate.format(date);
			gcTimeLessOrEqual = gcTimeLessOrEqual.replace("/","-");
			
			
			String strSelectExpiaryThisMonth = "SELECT * FROM `medicines` WHERE (`DOE` >= '"+dateTimt+"') AND (`DOE` <= '"+gcTimeLessOrEqual+"')"; 
			rs=state.executeQuery(strSelectExpiaryThisMonth);
			rs.last();
			int intExpiaryThisMonth = rs.getRow();
			
			dashboardController.setExpiaryThisMonth(String.valueOf(intExpiaryThisMonth));
			
			// Set total sales
			double totalSales = 0.0;
			
			//Display double with max two decimal digits
		    DecimalFormat df = new DecimalFormat("#.##");
			
			String selectTypeAndPrice = "SELECT * FROM sales";
			rs = state.executeQuery(selectTypeAndPrice);
	
			while(rs.next()) {
				double doublePrice = rs.getDouble("price");
				totalSales += doublePrice;
			}
			dashboardController.setTotalSales(String.valueOf(df.format(totalSales)));
			
			// Set total purchases			
			String selectTypePrice = "SELECT * FROM purchases";
			rs = state.executeQuery(selectTypePrice);
			
			double doubleTotalPurchases = 0.0;
			
			while(rs.next()) {
				double doublePrice = rs.getDouble("price");
				doubleTotalPurchases += doublePrice;
			}
			
			dashboardController.totalPurchases.setText(String.valueOf(df.format(doubleTotalPurchases)));

		
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		}
    	
    	borderPaneContent.setCenter(root);
    }
    
    @FXML
    void getViewUsers(MouseEvent event) throws IOException {
		String strSelect = "SELECT `id` FROM employees";

    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/ViewMedicine.fxml"));
    	Parent root = loaderViewMedicine.load();
    	
    	SearchMedicineController searchMedicineController = loaderViewMedicine.getController();
    	searchMedicineController.key = "getViewUsers";
    	searchMedicineController.getUsers(strSelect);
    	
    	borderPaneContent.setCenter(root);
    }
    
    @FXML
    void getViewCompanies(MouseEvent event) throws IOException {
    	String strSelectSimilarCompanies = "SELECT * FROM companies";

    	Stage stage = new Stage();
    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/ViewMedicine.fxml"));
    	Parent root = loaderViewMedicine.load();
    	
    	SearchMedicineController searchMedicineController = loaderViewMedicine.getController();
    	searchMedicineController.key = "getSimilarCompanies";
    	searchMedicineController.getCompanies(strSelectSimilarCompanies);
    	
    	borderPaneContent.setCenter(root);
    }

    @FXML
    void getSystemLog(MouseEvent event) throws IOException {
    	String strSelect = "SELECT `no` FROM log";

    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/ViewMedicine.fxml"));
    	Parent root = loaderViewMedicine.load();
    	
    	SearchMedicineController searchMedicineController = loaderViewMedicine.getController();
    	searchMedicineController.key = "getViewLog";
    	searchMedicineController.getLog(strSelect);
    	
    	borderPaneContent.setCenter(root);
    }

    @FXML
    void getRegisterUser(MouseEvent event) {
    	loadPage("/AdminPages/RegisterUser");
    }
    
    @FXML
    void getAddCompany(MouseEvent event) {
    	loadPage("/AdminPages/AddCompany");
    }

    @FXML
    void getAddSupplier(MouseEvent event) throws IOException, SQLException {
    	conn=DBinfo.connDB();
		state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
    	String strSelectCompanies = "SELECT `name` FROM `companies`";
		rs = state.executeQuery(strSelectCompanies);
		
		ArrayList<String> list = new ArrayList<>();
		
		while(rs.next()) {
			list.add(rs.getString("name"));
		}
		
		ObservableList<String> cmboList = FXCollections.observableArrayList(list);
		
    	
    	FXMLLoader loaderAddSupplier = new FXMLLoader(getClass().getResource("/AdminPages/AddSupplier.fxml"));
    	Parent root = loaderAddSupplier.load();
    	AddSupplierController addSupplierController = loaderAddSupplier.getController();
    	
    	//addSupplierController.comboCompanyNames = new ComboBox<>();
    	addSupplierController.comboCompanyNames.getItems().addAll(cmboList);
    	    	
    	borderPaneContent.setCenter(root);
    }
    
    @FXML
    void getAddMedicine(MouseEvent event) throws SQLException, IOException {
    	
    	//Set company names
    	conn=DBinfo.connDB();
		state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
    	String strSelectCompanies = "SELECT `name` FROM `companies`";
		rs = state.executeQuery(strSelectCompanies);
		
		ArrayList<String> list = new ArrayList<>();
		
		while(rs.next()) {
			list.add(rs.getString("name"));
		}
		ObservableList<String> cmboList = FXCollections.observableArrayList(list);
		
		//Set medicine types
		String[] arrTypes = {"Tablets", "Lequid", "Injection", "Bags"};
		ObservableList<String> cmboMedicineTypes = FXCollections.observableArrayList(arrTypes);

    	FXMLLoader loaderAddMedicine = new FXMLLoader(getClass().getResource("/AdminPages/AddMedicine.fxml"));
    	Parent root = loaderAddMedicine.load();
    	AddMedicineController addMedicineController = loaderAddMedicine.getController();
    	
    	addMedicineController.comboCompanyNames.getItems().addAll(cmboList);
    	addMedicineController.comboType.getItems().addAll(cmboMedicineTypes);
    	addMedicineController.username = (adminUsername.getText());
    	
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
    void getViewSuppliers(MouseEvent event) throws IOException {
		String strSelect = "SELECT `id` FROM supplier";

    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/ViewMedicine.fxml"));
    	Parent root = loaderViewMedicine.load();
    	
    	SearchMedicineController searchMedicineController = loaderViewMedicine.getController();
    	searchMedicineController.key = "getSuppliers";
    	searchMedicineController.getSuppliers(strSelect);
    	
    	borderPaneContent.setCenter(root);
    }
    
    @FXML
    void backup(MouseEvent event) throws SQLException, IOException {
    	//Backup database using batch file
    	Process p =  Runtime.getRuntime().exec("cmd /c mysqlautobackup.bat", null, new File("C:\\xampp\\mysql\\bin"));
    }
    
    @FXML
    void restore(MouseEvent event) throws IOException {
    	//Restore database using batch file
    	Process p =  Runtime.getRuntime().exec("cmd /c mysqlautorestore.bat", null, new File("C:\\xampp\\mysql\\bin"));
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
		//primaryStage.setOpacity(0.9);	
		primaryStage.show();
		
		Date date = new Date();						//2020-06-25 21:28:26
		DateFormat dateFormate = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss a");
		String logoutTimt = dateFormate.format(date);
		logoutTimt = logoutTimt.replace("/","-");
		
		String updateLogout ="UPDATE `log` SET `logout`=?  WHERE `no`=?" ;  
		PreparedStatement ps = conn.prepareStatement(updateLogout);
		ps.setString(1, logoutTimt);
		ps.setDouble(2, loginTime);
		ps.executeUpdate();
		
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
