package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AdminController {
		
    @FXML
    Label companyName;

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
    
	double x, y;
    
    String imageURL;
    double loginTime;
    
    FXMLLoader loader;
    Stage stage;
    Parent root;
    Statement state;
	ResultSet rs;
	Connection conn;

    @FXML
    void close(MouseEvent event) throws SQLException, ClassNotFoundException {
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
    void changeCmpanyName(MouseEvent event) throws SQLException {
    	Connection conn = null;
    	
    	try {
	    	Stage stage=new Stage();
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/ChangeCompanyName.fxml"));
			Parent root = loader.load();
			
			conn=DBinfo.connDB();
			
			//Select company name
			Statement stat = conn.createStatement();
			String strSelectUsers = "SELECT `name` FROM `company`";
			ResultSet rs = stat.executeQuery(strSelectUsers);
			rs.next();
			
			ChangeCompanyNameController changeCompanyNameController = loader.getController();
			changeCompanyNameController.setCurrentName(rs.getString("name"));
			
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
    void setDashboard() throws IOException, SQLException, ClassNotFoundException {
		try {
			conn=DBinfo.connDB();
			state=conn.createStatement();
			    	
			loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
			root = loader.load();
			
			// select image of employees
			String strSelectImage = "SELECT * FROM employees WHERE `username` = '"+adminUsername.getText()+"'";
			rs=state.executeQuery(strSelectImage);
			rs.next();
			imageURL = rs.getString("image");
			String strAdminName = rs.getString("name");
			String strAdress = rs.getString("address");
			String strPhone = rs.getString("phone");
			String strEmail = rs.getString("email");
			
			DashboardController dashboardController = loader.getController();
			
			ModifyUserController.dashboardController = dashboardController;
			ChangeNameController.dashboardController = dashboardController;

			dashboardController.setWelcomeMessage("Welcom Dear Admin  "+strAdminName);
			dashboardController.setUsername(adminUsername.getText());
			dashboardController.setUserImage(imageURL);
			dashboardController.immmagePath = imageURL;
			
			dashboardController.userAddress = strAdress;
			dashboardController.userEmail = strEmail;
			dashboardController.userPhone = strPhone;

			Date date = new Date();
			DateFormat dateFormate = new SimpleDateFormat("yyyy/MM/dd");
			String dateTimt = dateFormate.format(date);
			dateTimt = dateTimt.replace("/","-");
			
			// select count of medicines
			String strSelectTotalMedicines = "SELECT * FROM medicines";
			rs=state.executeQuery(strSelectTotalMedicines);
			
			int i =0;
			while(rs.next()) {
				i++;
			}
			
			int totalMedicines = i;
			dashboardController.setTotalMedicine(String.valueOf(totalMedicines));

			// select count of finished medicines
			String strSelectFinishedMedicines = "SELECT * FROM `quantity` WHERE "
					+ "`BOXES` = '0' and `STRIPES` = '0' "
					+ "AND `UNITS` = '0' AND `kg` = '0.0' "
					+ "AND `gms` = '0' AND `cm` = '0'";
			rs=state.executeQuery(strSelectFinishedMedicines);
			
			int i2 =0;
			while(rs.next()) {
				i2++;
			}
			
			int finishedMedicines = i2;
			
			dashboardController.finished.setText(String.valueOf(finishedMedicines));
			
			
			// select medicines out of stock
			String strSelectOutOfStock = "SELECT * FROM `medicines` WHERE `DOE` < '"+dateTimt+"'";
			rs=state.executeQuery(strSelectOutOfStock);
			
			int i3 =0;
			while(rs.next()) {
				i3++;
			}
			
			int intOutOfStock = i3;
			
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
			
			int i4 =0;
			while(rs.next()) {
				i4++;
			}
			
			int intExpiaryThisMonth = i4;
			
			dashboardController.setExpiaryThisMonth(String.valueOf(intExpiaryThisMonth));
			
			// Set total sales
			
			String strSelectBills = "SELECT DISTINCT `no` FROM `sales`";
			rs = state.executeQuery(strSelectBills);
			
			double newTotal = 0.0;
			double sumNewTotal = 0.0;
			
			if(rs.next()) {
				ResultSet rs2; 
				Statement state2 = conn.createStatement();
				String strBillNo = null;
				do {
					strBillNo = rs.getString("no");
					String strSelectDiscounts = "SELECT `new_total` FROM `sales` WHERE `no` = '"+strBillNo+"'";

					rs2 = state2.executeQuery(strSelectDiscounts);
					
					newTotal = Double.parseDouble(rs2.getString("new_total"));
					sumNewTotal += newTotal;
					
				}while(rs.next());
			}
						
//			double totalSales = 0.0;
//			
			//Display double with max two decimal digits
		    DecimalFormat df = new DecimalFormat("#.##");
//			
//			String selectTypeAndPrice = "SELECT * FROM sales";
//			rs = state.executeQuery(selectTypeAndPrice);
//	
//			while(rs.next()) {
//				double doublePrice = rs.getDouble("price");
//				totalSales += doublePrice;
//			}
			dashboardController.setTotalSales(String.valueOf(df.format(sumNewTotal)));
			
			//Set total returns
			double totalReturns = 0.0;
			String selectTypeAndPrice = "SELECT * FROM returns";
			rs = state.executeQuery(selectTypeAndPrice);
	
			while(rs.next()) {
				double doublePrice = Double.parseDouble(rs.getString("amount"));
				totalReturns += doublePrice;
			}
			dashboardController.lblReturns.setText(String.valueOf(df.format(totalReturns)));
			
			
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
		} finally {
			conn.close();
		}
    	
    	borderPaneContent.setCenter(root);
    }
    
    @FXML
    void getViewUsers(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
		String strSelect = "SELECT `id` FROM employees";

    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/ViewMedicine.fxml"));
    	Parent root = loaderViewMedicine.load();
    	
    	SearchMedicineController searchMedicineController = loaderViewMedicine.getController();
    	searchMedicineController.key = "getViewUsers";
    	searchMedicineController.hboxTop.getChildren().remove(searchMedicineController.datePickerSearch);
    	searchMedicineController.flowPaneContent.setHgap(20);
    	searchMedicineController.flowPaneContent.setVgap(20);
		searchMedicineController.flowPaneContent.setPadding(new Insets(20,0,0,50));
    	searchMedicineController.getUsers(strSelect);
    	
    	borderPaneContent.setCenter(root);
    }
    
    @FXML
    void getViewClients(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
    	String strSelect = "SELECT `name` FROM `clients`";

    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/ViewMedicine.fxml"));
    	Parent root = loaderViewMedicine.load();
    	
    	SearchMedicineController searchMedicineController = loaderViewMedicine.getController();
    	searchMedicineController.key = "getViewClients";
    	searchMedicineController.hboxTop.getChildren().remove(searchMedicineController.datePickerSearch);
    	searchMedicineController.flowPaneContent.setHgap(20);
    	searchMedicineController.flowPaneContent.setVgap(20);
		searchMedicineController.flowPaneContent.setPadding(new Insets(20,0,0,50));
    	searchMedicineController.getClients(strSelect);
    	
    	borderPaneContent.setCenter(root);
    }
    
    @FXML
    void getViewCompanies(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
    	String strSelectSimilarCompanies = "SELECT * FROM companies";

    	Stage stage = new Stage();
    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/ViewMedicine.fxml"));
    	Parent root = loaderViewMedicine.load();
    	
    	SearchMedicineController searchMedicineController = loaderViewMedicine.getController();
    	searchMedicineController.key = "getSimilarCompanies";
    	searchMedicineController.hboxTop.getChildren().remove(searchMedicineController.datePickerSearch);
    	searchMedicineController.getCompanies(strSelectSimilarCompanies);
    	
    	borderPaneContent.setCenter(root);
    }

    @FXML
    void getRegisterUser(MouseEvent event) {
    	loadPage("/AdminPages/RegisterUser");
    }
    
    @FXML
    void getRegisterClient(MouseEvent event) {
    	loadPage("/AdminPages/AddClient");
    }
    
    @FXML
    void getAddCompany(MouseEvent event) {
    	loadPage("/AdminPages/AddCompany");
    }

    @FXML
    void getAddSupplier(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {
    	conn=DBinfo.connDB();
		state=conn.createStatement();
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
    	addSupplierController.comboCompanyNames.setEditable(true);
    	
    	AutoCompleteComboBoxListener<String> comboClientNames = new AutoCompleteComboBoxListener<>(addSupplierController.comboCompanyNames);

    	borderPaneContent.setCenter(root);
    	
    	conn.close();
    }
    
    @FXML
    void getAddMedicine(MouseEvent event) throws SQLException, IOException, ClassNotFoundException {
    	
    	//Set company names
    	conn=DBinfo.connDB();
		state=conn.createStatement();
    	String strSelectCompanies = "SELECT `name` FROM `companies`";
		rs = state.executeQuery(strSelectCompanies);
		
		ArrayList<String> list = new ArrayList<>();
		
		while(rs.next()) {
			list.add(rs.getString("name"));
		}
		ObservableList<String> cmboList = FXCollections.observableArrayList(list);
		
		//Set medicine types
		String[] arrTypes = {"Tablets", "Lequid", "Injection", "Bags", "Powder", "Kgs", "Cm"};
		ObservableList<String> cmboMedicineTypes = FXCollections.observableArrayList(arrTypes);

    	FXMLLoader loaderAddMedicine = new FXMLLoader(getClass().getResource("/AdminPages/AddMedicine.fxml"));
    	Parent root = loaderAddMedicine.load();
    	AddMedicineController addMedicineController = loaderAddMedicine.getController();
    	
    	addMedicineController.comboCompanyNames.getItems().addAll(cmboList);
    	
    	AutoCompleteComboBoxListener<String> comboClientNames = new AutoCompleteComboBoxListener<>(addMedicineController.comboCompanyNames);
    	
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
    	
    	conn.close();
    }
    
    @FXML
    void getViewSuppliers(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
		String strSelect = "SELECT `id` FROM supplier";

    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/ViewMedicine.fxml"));
    	Parent root = loaderViewMedicine.load();
    	
    	SearchMedicineController searchMedicineController = loaderViewMedicine.getController();
    	searchMedicineController.key = "getSuppliers";
    	searchMedicineController.hboxTop.getChildren().remove(searchMedicineController.datePickerSearch);
    	searchMedicineController.flowPaneContent.setHgap(20);
    	searchMedicineController.flowPaneContent.setVgap(20);
		searchMedicineController.flowPaneContent.setPadding(new Insets(20,0,0,50));
    	
    	searchMedicineController.getSuppliers(strSelect);
    	
    	borderPaneContent.setCenter(root);
    }
    

    @FXML
    void getWithdraw(MouseEvent event) throws ClassNotFoundException, SQLException, IOException {
    	conn=DBinfo.connDB();
		state=conn.createStatement();
    	String strSelectCompanies = "SELECT `name` FROM `clients`";
		rs = state.executeQuery(strSelectCompanies);
		
		ArrayList<String> list = new ArrayList<>();
		
		while(rs.next()) {
			list.add(rs.getString("name"));
		}
		
		ObservableList<String> cmboList = FXCollections.observableArrayList(list);
		
    	
    	FXMLLoader loaderAddSupplier = new FXMLLoader(getClass().getResource("/AdminPages/Withdraw.fxml"));
    	Parent root = loaderAddSupplier.load();
    	WithdrawController withdrawController = loaderAddSupplier.getController();
    	
    	//addSupplierController.comboCompanyNames = new ComboBox<>();
    	withdrawController.comboUsers.getItems().addAll(cmboList);
    	withdrawController.comboUsers.setEditable(true);
    	
    	AutoCompleteComboBoxListener<String> comboClientNames = new AutoCompleteComboBoxListener<>(withdrawController.comboUsers);

    	borderPaneContent.setCenter(root);
    	
    	conn.close();
    }
    

    @FXML
    void getSalesReport(MouseEvent event) {
    	loadPage("/UserPages/SearchReport");
    }
    
    @FXML
    void getPurchasesReport(MouseEvent event) {
    	loadPage("/UserPages/SearchPurchaseReport");
    }
    
    @FXML
    void getEarningReport(MouseEvent event) {
    	loadPage("/UserPages/SearchEarningReport");
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
    
    void setCompanyName(String str) {
    	companyName.setText(str);
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
