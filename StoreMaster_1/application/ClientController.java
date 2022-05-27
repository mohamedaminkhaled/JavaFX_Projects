package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ClientController {
	
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
    
    String userName = null;
	String userAddress = null;
	String userPhone = null;
	String userEmail = null;
    String clientUsername;
    String imagepath;
    
	double x, y;
    
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
    
    void setEmployeeName(String str) {
    	employeeName.setText(str);
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
    void showCart(MouseEvent event) throws ClassNotFoundException, SQLException, IOException {
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
    	cartController.username = clientUsername;
    	cartController.comboClientName.getItems().addAll(cmboList);
    	
    	AutoCompleteComboBoxListener<String> comboClientNames = new AutoCompleteComboBoxListener<>(cartController.comboClientName);
    	
    	SellStockController.cartController = cartController;
    	SellStockController.jobtitle = "admin";
    	
    	Scene scene2 = new Scene(rootCart);
		Stage stage2 = new Stage();
		stage2.setScene(scene2);
		stage2.setResizable(false);
		stage2.setTitle("›« Ê—… „»Ì⁄« ");
		stage2.show();
    }
    
    void setDashboard() throws IOException, ClassNotFoundException, SQLException {
    	
    	Date date = new Date();
		DateFormat dateFormate = new SimpleDateFormat("yyyy/MM/dd");
		String dateTimt = dateFormate.format(date);
		dateTimt = dateTimt.replace("/","-");
    	
		//view all medicines
		String strSelect = "SELECT `name` FROM `categories`";

		FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/ViewMedicine.fxml"));
		Parent rootViewMedicine = loaderViewMedicine.load();
		
		SearchMedicineController searchMedicineController = loaderViewMedicine.getController();
		searchMedicineController.key = "getCategories";
		searchMedicineController.isClient = true;
		searchMedicineController.username = clientUsername;
		searchMedicineController.getCategories(strSelect);
		searchMedicineController.flowPaneContent.setHgap(10);
		searchMedicineController.flowPaneContent.setVgap(10);
		searchMedicineController.flowPaneContent.setPadding(new Insets(10,0,0,10));
		searchMedicineController.borderPaneSearch.setPrefSize(880,500);
		searchMedicineController.hboxTop.getChildren().remove(searchMedicineController.datePickerSearch);
		searchMedicineController.hboxTop.getChildren().remove(searchMedicineController.btnAddCategory);
		
		borderPaneContent.setCenter(rootViewMedicine);
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
		
		modifyUserController.tfAddress.setText(userAddress);
		modifyUserController.tfPhone.setText(userPhone);
		modifyUserController.tfEmail.setText(userEmail);		
		
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