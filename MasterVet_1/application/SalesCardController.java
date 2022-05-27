package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class SalesCardController {

    @FXML
    private BorderPane BorderPaneMedicineCard;

    @FXML
    private ImageView medicineImage;

    @FXML
    private Label medicineName;

    @FXML
    private Label medicinePrice;

    @FXML
    private Label quantitySold;

    @FXML
    private Label medicineSales;
    
    void setSalesCard(String id) throws IOException, SQLException, ClassNotFoundException {
		Statement state;
		ResultSet rs;
		
		String strSelect = "SELECT * FROM medicines WHERE `id` = '"+id+"'";
		
		Connection conn = null;
		try {
			conn=DBinfo.connDB();
			state=conn.createStatement();
			rs=state.executeQuery(strSelect);
			rs.next();
			
			int price = rs.getInt("price");
			int sold = rs.getInt("sold");
			int totalSales = (price * sold);
			
			//assign values to card attributes
			medicineName.setText(rs.getString("name"));
			medicinePrice.setText(rs.getString("price"));
			quantitySold.setText(rs.getString("sold"));
			medicineSales.setText(String.valueOf(totalSales));
			medicineImage.setImage(new Image(rs.getString("image")));
		} catch (SQLException e) {
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		} finally {
			conn.close();
		}
    }
}