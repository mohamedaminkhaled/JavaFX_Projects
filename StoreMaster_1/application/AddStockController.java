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
	
    @FXML
    private BorderPane addStockPage;

    @FXML
    private TextField tfQuantity;

    @FXML
    private CheckBox checkReturn;

    @FXML
    private TextField tfReturnAmnt;
    
    String itemId = null;
    
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
    void confirmAddStock(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminPages/RegisterUser.fxml"));
    	Parent root = loader.load();
    	RegisterUserController registerUserController = loader.getController();
    	
    	if(tfQuantity.getText().isEmpty()) {
			registerUserController.showErr("Error! Please idenify Quantity");
			return;
		}
    	
    	String strQuantity = tfQuantity.getText();
    	int intQuantity = 0;
    	
		try {
			intQuantity = Integer.parseInt(strQuantity);
			
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
			registerUserController.showErr("Error! Please enter neumeric value.");
		}
		
		Connection conn  = DBinfo.connDB();
		
		//SELECT medicine quantity
		String strSelectQuantity = "SELECT `quantity` FROM `items` WHERE `id` = '"+itemId+"'";
		Statement statQuantity = conn.createStatement();
		ResultSet rsQuantity = statQuantity.executeQuery(strSelectQuantity);
		rsQuantity.next();
		
		int intValidQuantity = rsQuantity.getInt("quantity");
				
		int intNewValidQuantity = intQuantity + intValidQuantity;
		
		//Update medicine quantity
		String strUpdateQuantity ="UPDATE `items` set `quantity`=? WHERE `id` =?";  
		
		PreparedStatement psUpQuantity = conn.prepareStatement(strUpdateQuantity);
		psUpQuantity.setInt(1, intNewValidQuantity);
		psUpQuantity.setString(2, itemId);
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
