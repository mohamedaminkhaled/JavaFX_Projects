package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AdminController {
		
    @FXML
    private BorderPane adminPage;
    
    @FXML
    private BorderPane borderPaneShow;

    @FXML
    private Pane adminPane;
    
    @FXML
    private Label lblTitle;

    @FXML
    private Label storeName;
    
    @FXML
    private Label lblUserName;
    
    @FXML
    private HBox hBoxSalesBills;

    @FXML
    private HBox hBoxPurchaseBills;
    
    @FXML
    private HBox hboxRegisterUser;

    @FXML
    private HBox hboxRegisterClient;

    @FXML
    private HBox hBoxAddCompany;
    
    @FXML
    private HBox hBoxSalesBill;

    @FXML
    private HBox hboxViewCompanies;

    @FXML
    private HBox hboxViewSuppliers;

    @FXML
    private HBox hboxViewUsers;

    @FXML
    private HBox hboxWithdraw;

    @FXML
    private HBox hboxSales;

    @FXML
    private HBox hboxPurchases;

    @FXML
    private HBox hboxEarnings;

    @FXML
    private HBox hboxExitApp;

    @FXML
    ImageView userImage;

    String userName = null;
	String userAddress = null;
	String userPhone = null;
	String userEmail = null;
	String immmagePath = null;
    String imageURL;
    
    FXMLLoader loader;
    Stage stage;
    Parent root;
    Statement state;
	ResultSet rs;
	Connection conn;

	double x, y;
	
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
    
    void setEmployeeImage(String str) {
    	userImage.setImage(new Image(str));
    }
    
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
			String strSelectUsers = "SELECT `name` FROM `employees` WHERE `username` = '"+userName+"'";
			ResultSet rs = stat.executeQuery(strSelectUsers);
			rs.next();
			
			ChangeNameController changeNameController = loader.getController();
			changeNameController.setCurrentName(rs.getString("name"));
			changeNameController.adminUserName = userName;
			
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
    void changeStoreName(MouseEvent event) throws SQLException {
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
			
			Scene scene=new Scene(root);
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
    void salesBill(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
    	//Set clients names
    	Connection conn = DBinfo.connDB();
		Statement state = conn.createStatement();
    	String strSelectCompanies = "SELECT `name` FROM `clients`";
		ResultSet rs = state.executeQuery(strSelectCompanies);
		
		ArrayList<String> list = new ArrayList<>();
		
		while(rs.next()) {
			list.add(rs.getString("name"));
		}
		
		ObservableList<String> cmboList = FXCollections.observableArrayList(list);    	    	    	
    	
    	//load Cart page on the right of search pane
    	FXMLLoader loaderCart = new FXMLLoader(getClass().getResource("/fxml/Cart.fxml"));
    	Parent rootCart = loaderCart.load();
    	CartController cartController = loaderCart.getController();
    	cartController.username = userName;
    	cartController.comboClientName.getItems().addAll(cmboList);
    	
    	AutoCompleteComboBoxListener<String> comboClientNames = new AutoCompleteComboBoxListener<>(cartController.comboClientName);
    	
    	SellStockController.cartController = cartController;
    	SellStockController.jobtitle = "admin";
    	
    	Scene scene2 = new Scene(rootCart);
		Stage stage2 = new Stage();
		stage2.setScene(scene2);
		stage2.setTitle("›« Ê—… „»Ì⁄« ");
		stage2.setResizable(false);
		stage2.show();
    }
    
    @FXML
    void getPurchaseBills(MouseEvent event) throws ClassNotFoundException, IOException, SQLException {
		Stage stage = new Stage();
    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/ViewMedicine.fxml"));
    	Parent root = loaderViewMedicine.load();
    	
    	Connection conn = null;
    	try {
    	
	    	SearchMedicineController searchMedicineController = loaderViewMedicine.getController();
	    	searchMedicineController.key = "getTotalPurchases";
	    	searchMedicineController.datePickerSearch.setVisible(true);
	    	searchMedicineController.tfSearch.setPromptText("Search bill number");
	    	searchMedicineController.hboxTop.getChildren().remove(searchMedicineController.btnAddCategory);
	    	
	    	Statement state;
			ResultSet rs;
			conn = DBinfo.connDB();
			state=conn.createStatement();
			
			String selectJobTitle = "SELECT `jobtitle` FROM employees WHERE `username` = '"+userName+"'";			
			rs = state.executeQuery(selectJobTitle);
			rs.next();
			if(rs.getString("jobtitle").equals("admin")) {
				searchMedicineController.hboxTop.getChildren().remove(searchMedicineController.btnBills);
			}
			

			String selectSalesNum = "SELECT DISTINCT `NO` FROM purchases ORDER BY `NO` DESC LIMIT 20";			
			rs = state.executeQuery(selectSalesNum);
			
			while(rs.next()) {
				FXMLLoader loaderBill = new FXMLLoader(getClass().getResource("/fxml/PurchaseBill.fxml"));
				Parent root2 = loaderBill.load();

				PurchaseBillController billController = loaderBill.getController();
				billController.BorderPaneBill.setTop(null);
				billController.setBill(rs.getString("no"));
				
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
	} finally {
		conn.close();
	}
	
	
	Scene scene=new Scene(root);
	stage.setScene(scene);
	stage.setTitle("›Ê« Ì— «·‘—«¡");
	stage.show();
    }

    @FXML
    void getSalesBills(MouseEvent event) throws ClassNotFoundException, IOException, SQLException {
		Stage stage = new Stage();
    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/ViewMedicine.fxml"));
    	Parent root = loaderViewMedicine.load();
    	
    	Connection conn = null;
    	try {
    	
	    	SearchMedicineController searchMedicineController = loaderViewMedicine.getController();
	    	searchMedicineController.key = "getTotalSales";
	    	searchMedicineController.datePickerSearch.setVisible(true);
	    	searchMedicineController.tfSearch.setPromptText("Search bill number");
	    	searchMedicineController.hboxTop.getChildren().remove(searchMedicineController.btnAddCategory);
	    	
	    	Statement state;
			ResultSet rs;
			conn = DBinfo.connDB();
			state=conn.createStatement();
			
			String selectJobTitle = "SELECT `jobtitle` FROM employees WHERE `username` = '"+userName+"'";			
			rs = state.executeQuery(selectJobTitle);
			rs.next();
			if(rs.getString("jobtitle").equals("admin")) {
				searchMedicineController.hboxTop.getChildren().remove(searchMedicineController.btnBills);
			}
			

			String selectSalesNum = "SELECT DISTINCT `NO` FROM sales ORDER BY `NO` DESC LIMIT 20";			
			rs = state.executeQuery(selectSalesNum);
			
			while(rs.next()) {
				FXMLLoader loaderBill = new FXMLLoader(getClass().getResource("/fxml/Bill.fxml"));
				Parent root2 = loaderBill.load();

				BillController billController = loaderBill.getController();
				billController.BorderPaneBill.setTop(null);
				billController.setBill(rs.getString("no"));
				
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
	} finally {
		conn.close();
	}
    
	Scene scene=new Scene(root);
	stage.setScene(scene);
	stage.setTitle("›Ê« Ì— «·»Ì⁄");
	stage.show();
    }
    
    @FXML
    void viewUsers(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
		String strSelect = "SELECT `id` FROM employees";

    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/ViewMedicine.fxml"));
    	Parent root = loaderViewMedicine.load();
    	
    	SearchMedicineController searchMedicineController = loaderViewMedicine.getController();
    	searchMedicineController.key = "getViewUsers";
    	
    	searchMedicineController.hboxTop.getChildren().remove(searchMedicineController.datePickerSearch);
    	searchMedicineController.hboxTop.getChildren().remove(searchMedicineController.btnAddCategory);

    	searchMedicineController.flowPaneContent.setHgap(20);
    	searchMedicineController.flowPaneContent.setVgap(20);
		searchMedicineController.flowPaneContent.setPadding(new Insets(20,0,0,50));
    	searchMedicineController.getUsers(strSelect);
    	
    	Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
		Stage stage = new Stage();
		stage.setResizable(true);
		stage.setScene(scene);
		stage.setTitle("⁄—÷ «·„” Œœ„Ì‰");
		stage.show();
    }
    
    @FXML
    void viewClients(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
    	String strSelect = "SELECT `name` FROM `clients`";

    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/ViewMedicine.fxml"));
    	Parent root = loaderViewMedicine.load();
    	
    	SearchMedicineController searchMedicineController = loaderViewMedicine.getController();
    	searchMedicineController.key = "getViewClients";
    	
    	searchMedicineController.hboxTop.getChildren().remove(searchMedicineController.datePickerSearch);
    	searchMedicineController.hboxTop.getChildren().remove(searchMedicineController.btnAddCategory);

    	searchMedicineController.flowPaneContent.setHgap(20);
    	searchMedicineController.flowPaneContent.setVgap(20);
		searchMedicineController.flowPaneContent.setPadding(new Insets(20,0,0,50));
    	searchMedicineController.getClients(strSelect);
    	
    	Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
		Stage stage = new Stage();
		stage.setResizable(true);
		stage.setScene(scene);
		stage.setTitle("⁄—÷ «·⁄„·«¡");
		stage.show();
    	
    }
    
    @FXML
    void viewCategories(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
    	String strSelect = "SELECT `name` FROM `categories`";

    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/ViewMedicine.fxml"));
    	Parent root = loaderViewMedicine.load();
    	
    	SearchMedicineController searchMedicineController = loaderViewMedicine.getController();
    	searchMedicineController.key = "getCategories";
    	searchMedicineController.username = userName;
    	searchMedicineController.btnAddCategory.setVisible(true);
    	searchMedicineController.hboxTop.getChildren().remove(searchMedicineController.datePickerSearch);
    	searchMedicineController.flowPaneContent.setHgap(20);
    	searchMedicineController.flowPaneContent.setVgap(20);
		searchMedicineController.flowPaneContent.setPadding(new Insets(20,0,0,50));
    	searchMedicineController.getCategories(strSelect);
    	
    	Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
		Stage stage = new Stage();
		stage.setResizable(true);
		stage.setScene(scene);
		stage.setTitle("⁄—÷ «·«’‰«›");
		stage.show();
    }
    
    @FXML
    void getProfile(MouseEvent event) throws IOException {
		Stage primaryStage = new Stage();
		FXMLLoader loaderModifyUser = new FXMLLoader(getClass().getResource("/AdminPages/ModifyUser.fxml"));
		Parent root = loaderModifyUser.load();
		ModifyUserController modifyUserController = loaderModifyUser.getController();
		modifyUserController.jobtitle = "admin";
		modifyUserController.username = userName;
		
		modifyUserController.tfAddress.setText(userAddress);
		modifyUserController.tfPhone.setText(userPhone);
		modifyUserController.tfEmail.setText(userEmail);		
		modifyUserController.setProfile(immmagePath);

    	Scene scene = new Scene(root,743,549);
		primaryStage.setResizable(true);
		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.show();
    }
    
    @FXML
    void addItem(MouseEvent event) throws ClassNotFoundException, SQLException, IOException {
    	//Set company names
    	Connection conn=DBinfo.connDB();
		Statement state=conn.createStatement();
    	String strSelectCompanies = "SELECT `name` FROM `categories`";
		ResultSet rs = state.executeQuery(strSelectCompanies);
		
		ArrayList<String> list = new ArrayList<>();
		
		while(rs.next()) {
			list.add(rs.getString("name"));
		}
		ObservableList<String> cmboList = FXCollections.observableArrayList(list);
		
    	FXMLLoader loaderAddMedicine = new FXMLLoader(getClass().getResource("/AdminPages/AddItem.fxml"));
    	Parent root = loaderAddMedicine.load();
    	AddItemController addMedicineController = loaderAddMedicine.getController();
    	
    	addMedicineController.comboCategoryNames.getItems().addAll(cmboList);
    	
    	AutoCompleteComboBoxListener<String> comboClientNames = new AutoCompleteComboBoxListener<>(addMedicineController.comboCategoryNames);
    	    	
    	//load Cart page on the right of search pane
    	FXMLLoader loaderCart = new FXMLLoader(getClass().getResource("/fxml/PurchaseCart.fxml"));
    	Parent rootCart = loaderCart.load();
    	PurchaseCartController purchaseCartController = loaderCart.getController();
    	purchaseCartController.borderCart.setPrefHeight(
    			addMedicineController.borderAddMedicine.getPrefHeight());
    	purchaseCartController.username = userName;
    	
    	addMedicineController.purchaseCartController = purchaseCartController;
    	addMedicineController.borderAddMedicine.setRight(rootCart);
    	
    	conn.close();
    	
    	Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
		Stage stage = new Stage();
		stage.setResizable(true);
		stage.setScene(scene);
		stage.setTitle("«÷«›… «Ê ‘—«¡ ⁄‰’—");
		stage.show();
    }
    
    @FXML
    void registerUser(MouseEvent event) {
    	loadPage("/AdminPages/RegisterUser", " ”ÃÌ· „” Œœ„");
    }
    
    @FXML
    void registerClient(MouseEvent event) {
    	loadPage("/AdminPages/AddClient", " ”ÃÌ· ⁄„Ì·");
    }
    
    @FXML
    void withdrawals(MouseEvent event) throws ClassNotFoundException, SQLException, IOException {
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
    	
    	conn.close();
    	
    	Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
		Stage stage = new Stage();
		stage.setResizable(true);
		stage.setScene(scene);
		stage.setTitle("„’—Ê›« ");
		stage.show();
    }
    
    @FXML
    void salesReport(MouseEvent event) {
    	loadPage("/UserPages/SearchReport", " ﬁ—Ì— „»Ì⁄« ");
    }
    
    @FXML
    void purchasesReport(MouseEvent event) {
    	loadPage("/UserPages/SearchPurchaseReport", " ﬁ—Ì— ‘—«¡");
    }
    
    @FXML
    void earningReport(MouseEvent event) {
    	loadPage("/UserPages/SearchEarningReport", " ﬁ—Ì— «—»«Õ");
    }
    
    @FXML
    void logout(MouseEvent event) throws IOException, SQLException {
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
    
    void setUserName(String str) {
    	lblUserName.setText(str);
    }
    
    void setStoreName(String str) {
    	storeName.setText(str);
    }
     
    void loadPage(String ui, String title) {
    	Parent root = null;
    	try {
			root=FXMLLoader.load(getClass().getResource(ui+".fxml"));
	    	
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
			Stage stage = new Stage();
			stage.setResizable(true);
			stage.setScene(scene);
			stage.setTitle(title);
			stage.show();
			
    	}catch(Exception e) {
			e.printStackTrace();
		}
    }
}
