package application;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
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
	public void start(Stage primaryStage) {
		try {
			ArrayList<String> pcMacs = new ArrayList<>(getMacAddress());
			String validMac = new String("00-1B-24-D4-7C-74");
			String validMac2 = new String("00-1C-BF-6B-8F-9D");

			if(pcMacs.contains(validMac) || pcMacs.contains(validMac2)) {
				Parent root=FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
				Scene scene = new Scene(root,484,623);
				scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
				primaryStage.setResizable(true);
				primaryStage.setScene(scene);
				primaryStage.initStyle(StageStyle.TRANSPARENT);
				//primaryStage.setOpacity(0.9);	
				primaryStage.show();
			}else {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
		    	Parent root = loader.load();
		    	RegisterUserController registerUserController = loader.getController();
				registerUserController.showErr("Error! Application can't run on this pc.");
			}
			
			} catch(Exception e) {
				e.printStackTrace();
			}
	}
	
    public static ArrayList<String> getMacAddress() throws UnknownHostException, SocketException {
		ArrayList<String> macs = new ArrayList<String>();
    	final Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
	    while (e.hasMoreElements()) {
	        final byte [] mac = e.nextElement().getHardwareAddress();
	        if (mac != null) {
	            StringBuilder sb = new StringBuilder();
	            for (int i = 0; i < mac.length; i++)
	                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
	            macs.add(sb.toString());
	        }
	    }
		return macs;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
