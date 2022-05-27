package application;

import java.io.File;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    private HBox hboxRegisterClient;

    @FXML
    private HBox hboxRegisterGuarantor;

    @FXML
    private HBox hBoxAddCar;

    @FXML
    private HBox hboxViewClients;

    @FXML
    private HBox hboxBackup;

    @FXML
    private HBox hboxRestore;

    @FXML
    private HBox hboxExitApp;
    
    String adminUsername;
    String adminName;
    
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
			String strSelectImage = "SELECT * FROM agency_managers WHERE `username` = '"+adminUsername+"'";
			rs=state.executeQuery(strSelectImage);
			rs.next();
			String imageURL = rs.getString("image");
			String strAdminName = rs.getString("name");

			System.out.println(imageURL);
			
			adminName = rs.getString("name");
			adminUsername = rs.getString("username");
			
			DashboardController dashboardController = loader.getController();
			
			ModifyProfileController.dashboardController = dashboardController;
			ChangeNameController.dashboardController = dashboardController;
			
			dashboardController.setWelcomeMessage("Welcom Dear Admin  "+adminName);
			dashboardController.setUserImage(imageURL);
			dashboardController.immmagePath = imageURL;
			dashboardController.username = adminUsername;
			
			//View all clients
			String strSelect = "SELECT `client_id` FROM client_cars ORDER BY `client_id` DESC";

	    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/fxml/SearchView.fxml"));
	    	Parent root2 = loaderViewMedicine.load();
	    	
	    	SearchController searchController = loaderViewMedicine.getController();
	    	searchController.key = "getViewUsers";
	    	searchController.getUsers(strSelect);
	    	
	    	dashboardController.borderPaneDashboard.setCenter(root2);
	    	
	    	borderPaneContent.setCenter(root);
			
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
    void getRegisterUser(MouseEvent event) throws SQLException, IOException, ClassNotFoundException {
    	
    	conn=DBinfo.connDB();
		state=conn.createStatement();
    	String strSelectCars = "SELECT * FROM `cars`";
		rs = state.executeQuery(strSelectCars);
		
		ArrayList<String> carsList = new ArrayList<>();
		
		while(rs.next()) {
			carsList.add(rs.getString("chassis_number") + " " + 
					rs.getString("name") + " " + rs.getString("color"));
		}
		
		conn.close();
		
		ObservableList<String> cmboList = FXCollections.observableArrayList(carsList);
		
    	
    	FXMLLoader loaderAddClient = new FXMLLoader(getClass().getResource("/AdminPages/RegisterClient.fxml"));
    	Parent root = loaderAddClient.load();
    	RegisterClientController registerClientController = loaderAddClient.getController();
    	
    	registerClientController.comboCars.getItems().addAll(cmboList);
    	    	
    	borderPaneContent.setCenter(root);
    }
    
    @FXML
    void getRegisterGuarantor(MouseEvent event) throws SQLException, IOException, ClassNotFoundException {
    	conn=DBinfo.connDB();
		state=conn.createStatement();
    	String strSelectClients = "SELECT * FROM `clients`";
		rs = state.executeQuery(strSelectClients);
		
		ArrayList<String> clientsList = new ArrayList<>();
		
		while(rs.next()) {
			clientsList.add(rs.getString("name"));
		}
		
		conn.close();
		
		ObservableList<String> cmboList = FXCollections.observableArrayList(clientsList);
    	
    	FXMLLoader loaderAddGurantor = new FXMLLoader(getClass().getResource("/AdminPages/RegisterGurantor.fxml"));
    	Parent root = loaderAddGurantor.load();
    	RegisterGurantorController registerGuarantorController = loaderAddGurantor.getController();
    	
    	registerGuarantorController.comboClients.getItems().addAll(cmboList);
    	    	
    	borderPaneContent.setCenter(root);
    }
    
    @FXML
    void getAddCar(MouseEvent event) {
    	loadPage("/AdminPages/AddCar");
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
    
    void setAdminUsername(String str) {
    	adminUsername = str;
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