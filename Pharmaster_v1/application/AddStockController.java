package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AddStockController {
	
	double x, y;

    @FXML
    private BorderPane addStockPage;

    @FXML
    private FontAwesomeIconView icon_close;

    @FXML
    TextField tfBoxes;

    @FXML
    TextField tfStripes;

    @FXML
    TextField tfUnits;
    
    String medicineID = null;

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
    void confirmAddStock(MouseEvent event) throws IOException, SQLException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerUserController = loader.getController();
    	
    	if(tfBoxes.getText().isEmpty() && tfStripes.getText().isEmpty()
				&& tfUnits.getText().isEmpty()) {
			registerUserController.showErr("Error! Please idenify Quantity");
			return;
		}
    	
    	String strBoxes = tfBoxes.getText().isEmpty() ? 
				"0" : tfBoxes.getText();
		String strStripes = tfStripes.getText().isEmpty() ? 
				"0" : tfStripes.getText();
		String strUnits = tfUnits.getText().isEmpty() ? 
				"0" : tfUnits.getText();
		
		int intBoxes = 0, intStripes = 0, intUnits = 0;
		try {
			intBoxes = Integer.parseInt(strBoxes);
			intStripes = Integer.parseInt(strStripes);
			intUnits = Integer.parseInt(strUnits);
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
			registerUserController.showErr("Error! Please enter neumeric value.");
		}
		
		Connection conn  = DBinfo.connDB();
		
		//SELECT medicine quantity
		String strSelectQuantity = "SELECT * FROM quantity WHERE `id` = '"+medicineID+"'";
		Statement statQuantity = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		ResultSet rsQuantity = statQuantity.executeQuery(strSelectQuantity);
		rsQuantity.first();
		
		int intValidBoxes = rsQuantity.getInt("BOXES");
		int intValidStripes = rsQuantity.getInt("STRIPES");
		int intValidUnits = rsQuantity.getInt("UNITS");
		
		int intNewValidBoxes = intBoxes + intValidBoxes;
		int intNewValidStripes = intStripes + intValidStripes;
		int intNewValidUnits = intUnits + intValidUnits;
		
		//Update medicine quantity
		String strUpdateQuantity ="UPDATE `quantity` set `boxes`=? , `stripes`=?, `units`=? WHERE `id` =?";  
		
		PreparedStatement psUpQuantity = conn.prepareStatement(strUpdateQuantity);
		psUpQuantity.setInt(1, intNewValidBoxes);
		psUpQuantity.setInt(2, intNewValidStripes);
		psUpQuantity.setInt(3, intNewValidUnits);
		psUpQuantity.setString(4, medicineID);
		psUpQuantity.executeUpdate();
		
		System.out.println("Update medicine quantity...");
		
    	Stage oldStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		oldStage.close();
    	
    	//Show success message
    	registerUserController.showSuccess();
    }
}
