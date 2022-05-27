package application;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws SQLException {
		Connection conn = null;
		try {
						
			//Query to get mack
			String strSelectMack = "SELECT * FROM `keys`";
						
			conn = DBinfo.connDB();
			Statement state=conn.createStatement();
			ResultSet rs=state.executeQuery(strSelectMack);
			rs.next();
			
			String defaultKey = "nomack"; 
			String pcMack = getMacAddress();
			String dbMack = rs.getString("mack");
			
			if(dbMack.equals(defaultKey)) {
				
				String sql="UPDATE `keys` SET `mack`=?";	
				
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, pcMack);
				ps.executeUpdate();
				
				Parent root=FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
				primaryStage.setResizable(true);
				primaryStage.setScene(scene);
				primaryStage.initStyle(StageStyle.TRANSPARENT);
				primaryStage.show();
			
			} else if(pcMack.equals(dbMack)) {
				
				Parent root=FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
				primaryStage.setResizable(true);
				primaryStage.setScene(scene);
				primaryStage.initStyle(StageStyle.TRANSPARENT);
				primaryStage.show();
			} else {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
		    	Parent root = loader.load();
		    	RegisterUserController registerUserController = loader.getController();
		    	registerUserController.showErr("Error! Application can't run on this pc.");
			}
			
		} catch(Exception e) {
				e.printStackTrace();
		} finally {
			conn.close();
		}
	}
	
    public static String getMacAddress() throws UnknownHostException, SocketException {
		ArrayList<String> pcMacs = new ArrayList<>();
    	final Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
	    while (e.hasMoreElements()) {
	        final byte [] mac = e.nextElement().getHardwareAddress();
	        if (mac != null) {
	            StringBuilder sb = new StringBuilder();
	            for (int i = 0; i < mac.length; i++)
	                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
	            pcMacs.add(sb.toString());
	            System.out.println(sb.toString());
	        }
	    }
		return pcMacs.get(0);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
