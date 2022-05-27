package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MedicineDetailsController {
	
    @FXML
    private BorderPane page_1;

    @FXML
    private FontAwesomeIconView icon_iconize;

    @FXML
    private FontAwesomeIconView icon_fullscreen;

    @FXML
    private FontAwesomeIconView icon_close;

    @FXML
    private Label medicineName;

    @FXML
    private Label medicineID;

    @FXML
    private Label companyName;

    @FXML
    private Label batch;

    @FXML
    private Label dateExpiary;

    @FXML
    private Label dateManufact;

    @FXML
    private Label supplierName;

    @FXML
    private ImageView medicineImage;

    @FXML
    private Label price;
    
    double x, y;
    
    @FXML
    void close(MouseEvent event) {
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	stage.close();
    }
    
    @FXML
    void pressed(MouseEvent event) {
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	x = event.getScreenX() - stage.getX();
    	y = event.getScreenY() - stage.getY();
    }
    
    @FXML
    void dragged(MouseEvent event) {
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	stage.setX(event.getScreenX() -x);
    	stage.setY(event.getScreenY() -y);
    }
    
    @FXML
    void max(MouseEvent event) {
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	stage.setFullScreenExitHint("");
    	if(!stage.isFullScreen()) {
    		stage.setFullScreen(true);
    	}else {
    		stage.setFullScreen(false);
    	}
    }

    @FXML
    void min(MouseEvent event) {
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	stage.setIconified(true);
    }
    
    void setMedicineDetails(String id) throws IOException {
    	Statement state;
		ResultSet rs;
		
		String strSelect = "SELECT medicines.*, companies.NAME AS COMP_NAME, "
				+ "supplier.NAME AS SUPP_NAME FROM medicines "
				+ "INNER JOIN companies ON medicines.EMP_ID = companies.ID "
				+ "INNER JOIN supplier ON medicines.SUPPLIERID = supplier.ID "
				+ "WHERE medicines.ID ='"+id+"'";
		
		try {
			Connection conn=DBinfo.connDB();
			state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs=state.executeQuery(strSelect);
			rs.last();
			
			//assign values to card attributes
			medicineName.setText(rs.getString("name"));
			medicineID.setText(rs.getString("id"));
			dateManufact.setText(rs.getString("DOM"));
			dateExpiary.setText(rs.getString("DOE"));
			price.setText(rs.getString("price"));
			batch.setText(rs.getString("batch"));
			medicineImage.setImage(new Image(rs.getString("image")));
			companyName.setText(rs.getString("COMP_NAME"));
			supplierName.setText(rs.getString("SUPP_NAME"));
						
		} catch (SQLException e) {
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		}
    }
}
