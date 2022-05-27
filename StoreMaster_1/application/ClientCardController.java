package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ClientCardController {

    @FXML
    private BorderPane BorderPaneMedicineCard;

    @FXML
    ImageView ClientImage;

    @FXML
    private Label name;

    @FXML
    private Label phone;

    @FXML
    private Label loan;

    @FXML
    private Label balance;
    
    String immmagePath;

    void setClientCard(String clientName) throws IOException, SQLException, ClassNotFoundException {
		Statement state;
		ResultSet rs;
		Connection conn = null;
		
		try {
			conn=DBinfo.connDB();
			state=conn.createStatement();
			
			String strSelect = "SELECT * FROM `clients` WHERE `name` = '"+clientName+"'";
			
			rs=state.executeQuery(strSelect);
			rs.next();
						
			//assign values to card attributes
			name.setText(rs.getString("name"));
			phone.setText(rs.getString("phone"));
			balance.setText(rs.getString("balance"));
			loan.setText(rs.getString("loan"));
			ClientImage.setImage(new Image(rs.getString("image")));
			immmagePath = rs.getString("image");
			
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		} finally {
			conn.close();
		}
    }
    
    @FXML
    void getDeleteClient(MouseEvent event) throws IOException {
    	Stage stage=new Stage();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/DeleteClientConfirmation.fxml"));
		Parent root = loader.load();
		
		DeleteClientController deleteClientController = loader.getController();
		deleteClientController.clientName = name.getText();
		
		Scene scene=new Scene(root,586,177);
		stage.setScene(scene);
		stage.initStyle(StageStyle.UTILITY);
		stage.show();
    }

    @FXML
    void getModifyClient(MouseEvent event) throws IOException {
    	Stage primaryStage = new Stage();
		FXMLLoader loaderModifyUser = new FXMLLoader(getClass().getResource("/AdminPages/ModifyClient.fxml"));
		Parent root = loaderModifyUser.load();
		ModifyClientController modifyClientController = loaderModifyUser.getController();
		modifyClientController.setProfile(immmagePath);
		modifyClientController.clientName = name.getText();
		modifyClientController.tfPhone.setText(phone.getText());
		
    	Scene scene = new Scene(root);
		primaryStage.setResizable(true);
		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.show();
    }
    
    @FXML
    void getClientReport(MouseEvent event) throws IOException {
    	Stage stage=new Stage();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserPages/SearchClientReport.fxml"));
		Parent root = loader.load();
		
		SearchClientReportController searchClientReportController = loader.getController();
		searchClientReportController.strClientName = name.getText();
	
		Scene scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
    }
}
