package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainController {
	
	double x, y;

    @FXML
    private AnchorPane login;

    @FXML
    private Label register;

    @FXML
    private Label exit;

    @FXML
    private TextField tf_username;

    @FXML
    private PasswordField tf_passwoard;

    @FXML
    private Button btn_submit;
    
    Stage stage;
        
    @FXML
    void close(MouseEvent event) {
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
    
    FXMLLoader loader;
    Parent root;
    @FXML
    void enterLogin(KeyEvent event) throws IOException, ClassNotFoundException {
    	if (event.getCode() == KeyCode.ENTER) {
    		getLogin(event);
        }
    	
    	btn_submit.requestFocus();
    }
    
    @FXML
    void login(MouseEvent event) throws IOException, ClassNotFoundException {
    	getLogin(event);
    }
    
    void getLogin(Event event) throws IOException, ClassNotFoundException {
    	Stage stage = new Stage();
    	Scene scene;
    	
    	Connection conn;
		Statement state;
		ResultSet rs;
		
		loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
    	root = loader.load();
    	RegisterUserController registerUserController = loader.getController();
		
		//Error message for empty username
		if(tf_username.getText().isEmpty()) {
			registerUserController.showErr("Error! username can't be empty");
			return;
		}
		
		//Error message for empty password
		if(tf_passwoard.getText().isEmpty()) {
			registerUserController.showErr("Error! password can't be empty");
			return;
		}
		
		String userName = tf_username.getText();
		String password = tf_passwoard.getText();
		
		String strSelect = "SELECT * FROM employees WHERE `username` = '"+userName+"' AND `password` = '"+password+"'";
		
		try {
			conn = DBinfo.connDB();
			System.out.println("connected...");
			state = conn.createStatement();
			System.out.println("after statement...");
			rs = state.executeQuery(strSelect);
			System.out.println("query executed...");
			rs.next();
			System.out.println("rs.next()...");

			//test if ResultSet is empty
			if(rs.getRow() == 0) {
				registerUserController.showErr("Error! username or password is wrong");
				return;
			}
			
			String str = new String(rs.getString("jobtitle"));
			String strUser = new String("user");
			String strAdmin = "admin";
			
			String name = rs.getString("name");
			Date date = new Date();
			System.out.println(date.getTime()); //6/24/2020 11:30  Date.getTime() => 1592990875462
			
			if(str.equals(strUser)) {
				System.out.println(str);
				System.out.println(name);
				System.out.println(userName);
			}else {
				System.out.println(strAdmin);
				System.out.println(name);
				System.out.println(userName);
			}
			
	    	int empID = rs.getInt("id");
						
			if(str.equals(strUser)) {

					loader = new FXMLLoader(getClass().getResource("/fxml/Client.fxml"));
			    	root = loader.load();

			    	String imgURL = rs.getString("image");

			    	ClientController clientController = loader.getController();
			    	SellStockController.clientController = clientController;
			    	SellStockController.jobtitle = "user";
			    	ModifyUserController.clientController = clientController;
			    	
			    	clientController.employeeName.setText(name);
			    	clientController.setEmployeeImage(imgURL);
			    	clientController.imagepath = imgURL;
			    	clientController.clientUsername = userName;
			    	clientController.setDashboard();
			    	
					scene = new Scene(root,1200,780);					
					scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
					stage.setResizable(true);
					stage.setScene(scene);
					stage.initStyle(StageStyle.TRANSPARENT);
					
					Stage oldStage = (Stage)((Node)event.getSource()).getScene().getWindow();
					oldStage.close();
					
					stage.show();
				}else if(str.equals(strAdmin)){
					loader = new FXMLLoader(getClass().getResource("/fxml/Admin.fxml"));
					root = loader.load();
			    	
			    	AdminController adminController = loader.getController();
			    	
			    	PurchaseCartController.adminController = adminController;
			    	PurchaseCartController.username = userName;
			    	
			    	adminController.setAdminName(name);
			    	adminController.setAdminUsername(userName);
			    	adminController.setDashboard();
					
					scene = new Scene(root,1180,750);
					scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
					stage.setResizable(true);
					stage.setScene(scene);
					stage.initStyle(StageStyle.TRANSPARENT);
					
					Stage oldStage = (Stage)((Node)event.getSource()).getScene().getWindow();
					oldStage.close();
					stage.show();
			}else {
		    	registerUserController.showErr("Error! this user doesn't exist.");
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
		}
    }
}