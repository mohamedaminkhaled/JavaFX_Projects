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

public class CompanyCardController {

    @FXML
    private BorderPane borderPaneCompanyCard;

    @FXML
    private Label companyName;

    @FXML
    private Label companyAddress;

    @FXML
    private Label companyPhone;

    @FXML
    private Label companyEmail;

    @FXML
    private ImageView companyImage;
    
    @FXML
    void getCompanyMedicines(MouseEvent event) throws IOException {
    	Stage stage = new Stage();
    	FXMLLoader loaderViewMedicine = new FXMLLoader(getClass().getResource("/UserPages/ViewMedicine.fxml"));
    	Parent root = loaderViewMedicine.load();
    	
		String strSelectCompanyMedicines = "SELECT * FROM `medicines` WHERE `COM_ID` = (SELECT `id` FROM companies WHERE `name` ='"+companyName.getText()+"')";
    	
    	SearchMedicineController searchMedicineController = loaderViewMedicine.getController();
    	searchMedicineController.companyName = companyName.getText();
    	searchMedicineController.key = "searchCompanyMedicines";
    	searchMedicineController.getMedicines(strSelectCompanyMedicines);
    	
    	Scene scene=new Scene(root,1000,570);
		stage.setScene(scene);
		stage.show();
    }
    
    void setCompanyCard(String name) throws SQLException {
    	
    	Connection conn=DBinfo.connDB();
		Statement state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		String selectCompanies = "SELECT * FROM companies WHERE `name` ='"+name+"'";
		ResultSet rs = state.executeQuery(selectCompanies);
		rs.first();
		
		companyName.setText(rs.getString("name"));
		companyAddress.setText(rs.getString("address"));
		companyPhone.setText(rs.getString("phone"));
		companyEmail.setText(rs.getString("email"));
		companyImage.setImage(new Image(rs.getString("image")));
    }

}

