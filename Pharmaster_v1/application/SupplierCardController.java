package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class SupplierCardController {

    @FXML
    private BorderPane borderPaneCompanyCard;

    @FXML
    private Label supplierName;

    @FXML
    private Label supplierAddress;

    @FXML
    private Label supplierPhone;

    @FXML
    private Label supplierEmail;

    @FXML
    private Label companyName;

    @FXML
    private ImageView supplierImage;
    
    void setSupplierCard(int id) throws SQLException {
       	Connection conn=DBinfo.connDB();
    		Statement state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
    				ResultSet.CONCUR_READ_ONLY);
    		String selectCompanies = "SELECT * FROM supplier WHERE `id` ="+id;
    		ResultSet rs = state.executeQuery(selectCompanies);
    		rs.first();
    		
    		supplierName.setText(rs.getString("name"));
    		supplierAddress.setText(rs.getString("address"));
    		supplierPhone.setText(rs.getString("phone"));
    		supplierEmail.setText(rs.getString("email"));
    		supplierImage.setImage(new Image(rs.getString("image")));
    		
    		String selectCompanyName = "SELECT `name` FROM companies WHERE `id` = "+rs.getString("COM_ID");
    		rs = state.executeQuery(selectCompanyName);
    		rs.first();
    		
    		companyName.setText(rs.getString("name"));
    }

}
