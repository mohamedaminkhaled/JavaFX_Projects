package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AddStockController {
	
	double x, y;

    @FXML
    private BorderPane addStockPage;

    @FXML
    TextField tfBoxes;

    @FXML
    TextField tfStripes;

    @FXML
    TextField tfUnits;
    
    @FXML
    TextField tfKg;

    @FXML
    TextField tfCm;

    @FXML
    TextField tfGm;
    
    @FXML
    CheckBox checkReturn;
    
    @FXML
    TextField tfReturnAmnt;
    
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
    void confirmAddStock(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerUserController = loader.getController();
    	
    	if(tfBoxes.getText().isEmpty() && tfStripes.getText().isEmpty()
		   && tfUnits.getText().isEmpty() && tfKg.getText().isEmpty()
		   && tfCm.getText().isEmpty() && tfGm.getText().isEmpty()) {
			registerUserController.showErr("Error! Please idenify Quantity");
			return;
		}
    	
    	String strBoxes = tfBoxes.getText().isEmpty() ? 
				"0" : tfBoxes.getText();
		String strStripes = tfStripes.getText().isEmpty() ? 
				"0" : tfStripes.getText();
		String strUnits = tfUnits.getText().isEmpty() ? 
				"0" : tfUnits.getText();
		String strKg = tfKg.getText().isEmpty() ? 
				"0" : tfKg.getText();
		String strCm = tfCm.getText().isEmpty() ? 
				"0" : tfCm.getText();
		String strGm = tfGm.getText().isEmpty() ? 
				"0" : tfGm.getText();
		
		int intBoxes = 0, intStripes = 0, intUnits = 0,
				intCm = 0, intGm = 0;
		double kgs = 0.0;
		try {
			intBoxes = Integer.parseInt(strBoxes);
			intStripes = Integer.parseInt(strStripes);
			intUnits = Integer.parseInt(strUnits);
			intCm = Integer.parseInt(strCm);
			intGm = Integer.parseInt(strGm);
			kgs = Double.parseDouble(strKg);
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
			registerUserController.showErr("Error! Please enter neumeric value.");
		}
		
		Connection conn  = DBinfo.connDB();
		
		//SELECT medicine quantity
		String strSelectQuantity = "SELECT * FROM quantity WHERE `id` = '"+medicineID+"'";
		Statement statQuantity = conn.createStatement();
		ResultSet rsQuantity = statQuantity.executeQuery(strSelectQuantity);
		rsQuantity.next();
		
		int intValidBoxes = rsQuantity.getInt("BOXES");
		int intValidStripes = rsQuantity.getInt("STRIPES");
		int intValidUnits = rsQuantity.getInt("UNITS");
		
		String strValidKg = rsQuantity.getString("kg");
		double validKg = Double.parseDouble(strValidKg);
		int intValidCm = rsQuantity.getInt("cm");
		int intValidGm = rsQuantity.getInt("gms");
		
		int intNewValidBoxes = intBoxes + intValidBoxes;
		int intNewValidStripes = intStripes + intValidStripes;
		int intNewValidUnits = intUnits + intValidUnits;
		
		double newValidKg = validKg + kgs;
		String strNewValidKg = String.valueOf(newValidKg);
		int intNewValidCm = intValidCm + intCm;
		int intNewValidGm = intValidGm + intGm;
		
		//Update medicine quantity
		String strUpdateQuantity ="UPDATE `quantity` set `boxes`=? , `stripes`=?, "
				+ "`units`=?, `kg`=?, `gms`=?, `cm`=? WHERE `id` =?";  
		
		PreparedStatement psUpQuantity = conn.prepareStatement(strUpdateQuantity);
		psUpQuantity.setInt(1, intNewValidBoxes);
		psUpQuantity.setInt(2, intNewValidStripes);
		psUpQuantity.setInt(3, intNewValidUnits);
		psUpQuantity.setString(4, strNewValidKg);
		psUpQuantity.setInt(5, intNewValidGm);
		psUpQuantity.setInt(6, intNewValidCm);
		psUpQuantity.setString(7, medicineID);
		psUpQuantity.executeUpdate();
		
		System.out.println("Update medicine quantity...");
		
		if(checkReturn.isSelected()) {
			//ex. 2021-01-19 12:10:26
			DateFormat dateFormate = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss a");
			Date date = new Date();
			String dateTimt = dateFormate.format(date );
			dateTimt = dateTimt.replace("/","-");
			
			String sql="INSERT INTO `returns`(`amount`,`time`) "
					+ "VALUES (?,?)";
			
			PreparedStatement ps = conn.prepareStatement(sql);
	
			ps.setString(1, tfReturnAmnt.getText());
			ps.setString(2, dateTimt);
			
			ps.executeUpdate();
		}
		
		conn.close();
		
    	Stage oldStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		oldStage.close();
    	
    	//Show success message
    	registerUserController.showSuccess();
    }
}
