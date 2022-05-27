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
		
		loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterClient.fxml"));
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
		
		String strSelect = "SELECT * FROM agency_managers WHERE `username` = '"+userName+"' AND `password` = '"+password+"'";
		
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
			}else {
								
				loader = new FXMLLoader(getClass().getResource("/fxml/Admin.fxml"));
				root = loader.load();
		    	
		    	AdminController adminController = loader.getController();
		    			    	
		    	adminController.setAdminUsername(userName);
		    	adminController.setDashboard();
		    					
				scene = new Scene(root,1180,800);
				scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
				stage.setResizable(true);
				stage.setScene(scene);
				stage.initStyle(StageStyle.TRANSPARENT);
				
				Stage oldStage = (Stage)((Node)event.getSource()).getScene().getWindow();
				oldStage.close();
				stage.show();
			}
												
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
		}
    }
}