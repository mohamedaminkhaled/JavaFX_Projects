package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ItemCardController {

    @FXML
    private BorderPane borderPaneCompanyCard;

    @FXML
    private ImageView itemImage;
    
    @FXML
    private Button btnItemName;

    @FXML
    private Label itemPrice;

    @FXML
    private Label tfSold;
    
    @FXML
    Circle btnRemove;
    
    @FXML
    ImageView btnEdit;

    @FXML
    void sellItem(MouseEvent event) throws IOException {
    	ObservableList<String> cmboMedicineTypes = null;
    	
    	String[] arrTypes = {"0","5","10","15","20","25","30","35",
    			"40","45","50","55","60","65","70","75","80","85",
    			"90","95","100"};
		cmboMedicineTypes = FXCollections.observableArrayList(arrTypes);
    	
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserPages/SellStock.fxml"));
		Parent root = loader.load();
    	SellStockController sellStockController = loader.getController();
    	sellStockController.comboUnits.getItems().addAll(cmboMedicineTypes);
    	sellStockController.itemName = btnItemName.getText();
    	
    	Stage stage = new Stage();
    	Scene scene=new Scene(root,490,220);
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
    }
    
    void setItemCard(String name) throws SQLException, ClassNotFoundException {
    	
    	Connection conn=DBinfo.connDB();
		Statement state=conn.createStatement();
		String selectCompanies = "SELECT * FROM items WHERE `name` ='"+name+"'";
		ResultSet rs = state.executeQuery(selectCompanies);
		rs.next();
		
		tfSold.setText(rs.getString("sold"));
		btnItemName.setText(rs.getString("name"));
		itemPrice.setText(rs.getString("price"));
		itemImage.setImage(new Image(rs.getString("image")));
		
		conn.close();
    }
    
    @FXML
    void editItem(MouseEvent event) throws IOException {
    	Stage stage=new Stage();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/ModifyPrice.fxml"));
		Parent root = loader.load();
		
		ModifyPriceController modifyPriceController = loader.getController();
		modifyPriceController.currentSalary.setText(itemPrice.getText());
		modifyPriceController.itemName = btnItemName.getText();
	
		Scene scene=new Scene(root,630,266);
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
    }
    
    @FXML
    void removeItem(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
    	Connection conn = null;
    	try {
			conn=DBinfo.connDB();
			
			String strUpdate ="DELETE FROM `items` WHERE `name` =?";  
			PreparedStatement ps = conn.prepareStatement(strUpdate);
			ps.setString(1, btnItemName.getText());
			ps.executeUpdate();
		} catch (SQLException e) {
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		} finally {
			conn.close();
		}
    	
    	borderPaneCompanyCard.setVisible(false);
    }
    
    @FXML
    void openImageChooser(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
    	
    	FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new
			        FileChooser.ExtensionFilter("jpg", "*.jpg"),
		            new FileChooser.ExtensionFilter("JPGE", "*.JPGE"),
		            new FileChooser.ExtensionFilter("png", "*.png"));
		
		Stage stage = new Stage();
		File selectedFile = fileChooser.showOpenDialog(stage);
	     
		 if(selectedFile != null) {
			 String path = selectedFile.getAbsolutePath();		     
			 path = path.replace("\\","/");
			 Image image = new Image(new File(path).toURI().toString());
			 itemImage.setImage(image);
			 
			 String imagePath = new File(path).toURI().toString();
			 
	    	//Create image in photos folder
	    	
	    	//*****************************************//
	    	int lastSlash = imagePath.lastIndexOf("/");
	    	String imageName = imagePath.substring(lastSlash+1);
	    	
	    	Image image2 = new Image(imagePath);
	    	    	
	    	File outputFile = new File("D:\\BlackCaffe\\photos\\"+imageName);
	        BufferedImage bImage = SwingFXUtils.fromFXImage(image2, null);
	        try {
	          ImageIO.write(bImage, "jpg", outputFile);
	        } catch (IOException e) {
	          throw new RuntimeException(e);
	        }
	        
	        imagePath = "file:/D:/BlackCaffe/photos/"+imageName;
	    	
	      //*****************************************//
	        Connection conn = null;
			 try {
				conn=DBinfo.connDB();
				
				String strUpdate ="UPDATE `items` SET `image`=? WHERE `name`=?" ;  
				PreparedStatement ps = conn.prepareStatement(strUpdate);
				ps.setString(1, imagePath);
				ps.setString(2, btnItemName.getText());
				ps.executeUpdate();
			} catch (SQLException e) {
				ErrorServerNotFound err = new ErrorServerNotFound();
				err.errException(e);
				return;
			} finally {
				 conn.close();
			}
		 }
    }
}

