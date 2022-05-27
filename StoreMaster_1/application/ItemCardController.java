package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.connectcode.Code128Auto;

public class ItemCardController {

    @FXML
    private BorderPane BorderPaneMedicineCard;

    @FXML
    private Label quantity;

    @FXML
    private Label sold;

    @FXML
    private Label price;

    @FXML
    private Label itemName;

    @FXML
    private Label itemId;
    
    @FXML
    private Label discount;
    
    @FXML
    VBox vboxEditItem;
    
    String employeeUsername;
    
    @FXML
    void editItem(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
    	Stage stage=new Stage();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserPages/EditItem.fxml"));
		Parent root = loader.load();
		
		ChangePriceController changePriceController = loader.getController();
		
		changePriceController.setCurrentPrice(price.getText());
		changePriceController.setCurrentName(itemName.getText());
		changePriceController.itemID = itemId.getText();
		
		Scene scene=new Scene(root);
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
    }
    
    @FXML
    void deleteItem(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {
		Connection conn = null;
		try {
			conn=DBinfo.connDB();
			
			String strUpdate ="DELETE FROM `items` WHERE `id` =?";  
			PreparedStatement ps = conn.prepareStatement(strUpdate);
			ps.setString(1, itemId.getText());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		} finally {
			conn.close();
		}
		
		BorderPaneMedicineCard.setVisible(false);
    }
    
    void setItemCard(String id) throws IOException, SQLException, ClassNotFoundException {
		
    	Connection conn = null;
		try {
			
			String strSelect1 = "SELECT * FROM `items`"
					+ "WHERE `id` = '"+id+"'";
			
			conn=DBinfo.connDB();
			Statement state=conn.createStatement();
			ResultSet rs=state.executeQuery(strSelect1);
			rs.next();
			
			//assign values to card attributes
			itemName.setText(rs.getString("name"));
			quantity.setText(rs.getString("quantity"));
			price.setText(rs.getString("price"));
			discount.setText(rs.getString("discount"));
			sold.setText(rs.getString("sold"));
			itemId.setText(rs.getString("id"));
						
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
    }
    
    @FXML
    void addStock(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {
    	Stage stage=new Stage();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserPages/AddStock.fxml"));
		Parent root = loader.load();
		
		AddStockController addStockController = loader.getController();
		addStockController.itemId = itemId.getText();

		
		Scene scene=new Scene(root);
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
    }
    
    @FXML
    void showBarcode(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/ShowBarcode.fxml"));
    	Parent root = loader.load();
    	BarcodeController barcodeController = loader.getController();
    	
    	barcodeController.itemName.setText(itemName.getText());
    	barcodeController.code.setText(itemId.getText());
    	barcodeController.price.setText(price.getText());
    	barcodeController.discount.setText(discount.getText());
    	
    	//Set Bar code
    	Code128Auto code128 = new Code128Auto();
    	String strBarcode = code128.encode(itemId.getText());
    	barcodeController.barcode.setText(strBarcode);    	
    	
    	Scene scene = new Scene(root);
    	Stage primaryStage = new Stage();
		primaryStage .setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.setTitle("ÚÑÖ ÇáÈÇÑßæÏ");
		primaryStage.show();
    }
}
