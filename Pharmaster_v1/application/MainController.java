package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        
    @FXML
    void close(MouseEvent event) {
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	stage.close();
    }
    
    FXMLLoader loader;
    Parent root;
    @FXML
    void enterLogin(KeyEvent event) throws IOException {
    	if (event.getCode() == KeyCode.ENTER) {
    		getLogin(event);
        }
    	
    	btn_submit.requestFocus();
    }
    
    @FXML
    void login(MouseEvent event) throws IOException {
    	getLogin(event);
    }
    
    void getLogin(Event event) throws IOException {
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
		
		//Error message for empty username
		if(tf_passwoard.getText().isEmpty()) {
			registerUserController.showErr("Error! password can't be empty");
			return;
		}
		
		String userName = tf_username.getText();
		String password = tf_passwoard.getText();
		
		String strSelect = "SELECT * FROM Employees WHERE `username` = '"+userName+"' AND `password` = '"+password+"'";
		
		try {
			conn = DBinfo.connDB();
			state = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = state.executeQuery(strSelect);
			rs.first();

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
			    	clientController.clientUsername = userName;
			    	clientController.setDashboard();
			    	
			    	Date loginDate = new Date();				 //2020-06-25 21:28:26
					DateFormat dateFormate = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss a");
					String strLoginTimt = dateFormate.format(loginDate);
					strLoginTimt = strLoginTimt.replace("/","-");
			    	double loginTime = loginDate.getTime();
			    	
			    	String insertLog = "INSERT INTO log (`no`,`login`,`EMP_ID`) values (?,?,?)";
			    	PreparedStatement psLogin = conn.prepareStatement(insertLog);
			    	psLogin.setDouble(1, loginTime);
			    	psLogin.setString(2, strLoginTimt);
			    	psLogin.setInt(3, empID);
			    	psLogin.executeUpdate();
			    	
			    	clientController.loginTime = loginTime;
			    	
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
			    	
			    	Date loginDate = new Date();						//2020-06-25 21:28:26
					DateFormat dateFormate = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss a");
					String strLoginTimt = dateFormate.format(loginDate);
					strLoginTimt = strLoginTimt.replace("/","-");
			    	double loginTime = loginDate.getTime();
			    	
			    	String insertLog = "INSERT INTO log (`no`,`login`,`EMP_ID`) values (?,?,?)";
			    	PreparedStatement psLogin = conn.prepareStatement(insertLog);
			    	psLogin.setDouble(1, loginTime);
			    	psLogin.setString(2, strLoginTimt);
			    	psLogin.setInt(3, empID);
			    	psLogin.executeUpdate();
			    	
			    	adminController.loginTime = loginTime;
					
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
			
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
		}
    }
}