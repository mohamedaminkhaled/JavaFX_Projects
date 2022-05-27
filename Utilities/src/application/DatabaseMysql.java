package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class DatabaseMysql {
	
    @FXML
    private ImageView medicineImage;

    @FXML
    private Button btnImageChooser;

    @FXML
    private TextField tfMedicineID;

    @FXML
    private TextField tfMedicineName;

    @FXML
    private DatePicker datepickerManufact;

    @FXML
    private DatePicker datepickerExpire;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnAdd;

    @FXML
    ComboBox<String> comboSupplierNames;

    @FXML
    ComboBox<String> comboCompanyNames;

    @FXML
    private DatePicker datepickerBatch;
    
    @FXML
    ComboBox<String> comboType;
    
    @FXML
    private TextField tfStripsDetails;

    @FXML
    private TextField tfUnitDetails;

    @FXML
    private TextField tfStripes;

    @FXML
    private TextField tfUnits;

    @FXML
    private TextField tfBoxes;

    @FXML
    private TextField tfPrice;
    
    String username;
    
    void sqlJoinThreeTables() {
		try {
			
			String id = "";
			String strSelect = "SELECT medicines.*, companies.NAME AS COMP_NAME, "
					+ "supplier.NAME AS SUPP_NAME FROM medicines "
					+ "INNER JOIN companies ON medicines.EMP_ID = companies.ID "
					+ "INNER JOIN supplier ON medicines.SUPPLIERID = supplier.ID "
					+ "WHERE medicines.ID ='"+id +"'";
			
			Connection conn=DBinfo.connDB();
			Statement state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ResultSet rs=state.executeQuery(strSelect);
			rs.last();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
	
	void insertPrepairedStatement() {
		try {
			
			Connection conn = DBinfo.connDB();
			Statement stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			
			//SELECT company ID
			String strSelectCombID = "SELECT `id` FROM companies WHERE `name` = '"+comboCompanyNames.getValue()+"'";
			ResultSet rs = stat.executeQuery(strSelectCombID);
			rs.first();
			
			String selectedCompany = rs.getString("id");
			
			//SELECT supplier ID
			String strSelectSuppID = "SELECT `id` FROM supplier WHERE `name` = '"+comboSupplierNames.getValue()+"'";
			rs = stat.executeQuery(strSelectSuppID);
			rs.first();
			
			String selectedSupplier = rs.getString("id");
			
			//SELECT Employee ID
			String strSelectEmpID = "SELECT `id` FROM Employees WHERE `username` = '"+username+"'";
			rs = stat.executeQuery(strSelectEmpID);
			rs.first();
			
			String selectedEmployee = rs.getString("id");
			
			String sql="INSERT INTO `medicines`(`id`,`name`,`batch`, `type`,`STRIPES`,`UNITS`,"
					+ "`price`, `DOM`, `DOE`, `image`,`SUPPLIERID`,`EMP_ID`,`COM_ID`) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, tfMedicineID.getText());
			ps.setString(2, tfMedicineName.getText());
			ps.setString(3, datepickerBatch.getValue().toString());
			ps.setString(4, comboType.getValue());
			ps.setString(5, tfStripsDetails.getText().isEmpty() ? "0" : tfStripsDetails.getText());
			ps.setString(6, tfUnitDetails.getText().isEmpty() ? "0" : tfUnitDetails.getText());
			ps.setString(7, tfPrice.getText());
			ps.setString(8, datepickerManufact.getValue().toString());
			ps.setString(9, datepickerExpire.getValue().toString());
			ps.setString(10, medicineImage.getImage().impl_getUrl());
			ps.setString(11, selectedSupplier);
			ps.setString(12, selectedEmployee);
			ps.setString(13, selectedCompany);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
