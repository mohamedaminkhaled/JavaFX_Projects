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

public class SupplierCardController {

    @FXML
    BorderPane borderPaneCompanyCard;

    @FXML
    Label supplierName;

    @FXML
    Label supplierAddress;

    @FXML
    Label supplierPhone;

    @FXML
    Label supplierEmail;

    @FXML
    Label companyName;

    @FXML
    ImageView supplierImage;
    
    String immmagePath;
    
    void setSupplierCard(int id) throws SQLException, ClassNotFoundException {
       	Connection conn=DBinfo.connDB();
		Statement state=conn.createStatement();
		String selectCompanies = "SELECT * FROM supplier WHERE `id` ="+id;
		ResultSet rs = state.executeQuery(selectCompanies);
		rs.next();
		
		supplierName.setText(rs.getString("name"));
		supplierAddress.setText(rs.getString("address"));
		supplierPhone.setText(rs.getString("phone"));
		supplierEmail.setText(rs.getString("email"));
		supplierImage.setImage(new Image(rs.getString("image")));
		immmagePath = rs.getString("image");
		
		String selectCompanyName = "SELECT `name` FROM companies WHERE `id` = "+rs.getString("COM_ID");
		rs = state.executeQuery(selectCompanyName);
		rs.next();
		
		companyName.setText(rs.getString("name"));
		
		conn.close();
    }
    
    @FXML
    void modifySupplier(MouseEvent event) throws IOException {
    	Stage primaryStage = new Stage();
		FXMLLoader loaderModifyUser = new FXMLLoader(getClass().getResource("/AdminPages/ModifySupplier.fxml"));
		Parent root = loaderModifyUser.load();
		ModifySupplierController modifySupplierController = loaderModifyUser.getController();
		modifySupplierController.setProfile(immmagePath);
		modifySupplierController.supplierName = supplierName.getText();
		modifySupplierController.tfAddrees.setText(supplierAddress.getText());
		modifySupplierController.tfPhone.setText(supplierPhone.getText());
		modifySupplierController.tfEmail.setText(supplierEmail.getText());
		
    	Scene scene = new Scene(root,743,549);
		primaryStage.setResizable(true);
		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.show();
    }
}
