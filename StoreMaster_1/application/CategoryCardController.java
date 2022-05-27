package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CategoryCardController {

    @FXML
    private BorderPane BorderPaneMedicineCard;

    @FXML
    private Label categoryName;
    
    @FXML
    VBox vBoxEditCategory;
    
    @FXML
    private Circle iconDelete;

    @FXML
    private ImageView iconEdit;

    @FXML
    void delete(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
		Connection conn = null;
		try {
			conn=DBinfo.connDB();
			
			String strUpdate ="DELETE FROM `categories` WHERE `name` =?";  
			PreparedStatement ps = conn.prepareStatement(strUpdate);
			ps.setString(1, categoryName.getText());
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

    @FXML
    void edit(MouseEvent event) throws IOException {
    	Stage stage=new Stage();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserPages/EditCategory.fxml"));
		Parent root = loader.load();
		
		EditCategoryController editCategoryController = loader.getController();
		
		editCategoryController.setCurrentName(categoryName.getText());
		editCategoryController.catName = categoryName.getText();
		
		Scene scene=new Scene(root);
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
    }
    
    @FXML
    void getCategoryItems(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
    	Stage stage = new Stage();
    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/ViewMedicine.fxml"));
    	Parent root = loaderViewMedicine.load();
    	
		String strSelectCompanyMedicines = "SELECT * FROM `items` WHERE `category` = '"+categoryName.getText()+"'";
    	
    	SearchMedicineController searchMedicineController = loaderViewMedicine.getController();
    	searchMedicineController.key = "searchCategoryItems";
    	searchMedicineController.getMedicines(strSelectCompanyMedicines);
    	    			
    	searchMedicineController.hboxTop.getChildren().remove(searchMedicineController.datePickerSearch);
    	searchMedicineController.hboxTop.getChildren().remove(searchMedicineController.btnAddCategory);
    	    	
    	Scene scene=new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("ÚÑÖ ÇáÚäÇÕÑ");
		stage.show();
    }
    
    void setCategoryCard(String name) throws SQLException, ClassNotFoundException {
    	
    	Connection conn=DBinfo.connDB();
		Statement state=conn.createStatement();
		String selectCompanies = "SELECT * FROM `categories` WHERE `name` ='"+name+"'";
		ResultSet rs = state.executeQuery(selectCompanies);
		rs.next();
		
		categoryName.setText(rs.getString("name"));
		
		conn.close();
    }
}
