package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CartController {

    @FXML
    BorderPane borderCart;
	
	@FXML
    Label countOfItems;

    @FXML
    Label totalCoast;
    
    @FXML
    VBox vboxCartItems;
    
    String username;
    
    @FXML
    void getConfirmBill(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {
    	Date date = new Date();
		double time = date.getTime();
		String strTime = String.valueOf(time);
    	
		Connection conn = null;
		
    	ArrayList<HBox> arrItems = new ArrayList<>();
    	
    	ArrayList<Node> arrNodes = new ArrayList<>();
    	arrNodes.addAll(vboxCartItems.getChildren());
    	for (Node node : arrNodes) {
    		arrItems.add((HBox) node);
		}
    	
    	int i = 0;
    	for (HBox hbox : arrItems) {
			//Skip first node 'header'
    		if(i == 0) {
				i++;
				continue;
			}else {
		    	ArrayList<Node> arrItemDetails = new ArrayList<>();
		    	arrItemDetails.addAll(hbox.getChildren());
		    	
		    	Label lblName = (Label) arrItemDetails.get(0);
		    	String strName = lblName.getText();
		    	
		    	Label lblQuantity = (Label) arrItemDetails.get(1);
		    	String strQuantity = lblQuantity.getText();
		    	int intQuantity = Integer.parseInt(strQuantity);

		    	Label lblDiscount = (Label) arrItemDetails.get(2);
		    	String strDiscount = lblDiscount.getText();
		    	
		    	Label lblPrice = (Label) arrItemDetails.get(3);
		    	String strPrice = lblPrice.getText();		    	
		    	
				try {
					conn = DBinfo.connDB();
					
					//ex. 2020-06-25 21:28:26
					DateFormat dateFormate = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss a");
					String dateTimt = dateFormate.format(date);
					dateTimt = dateTimt.replace("/","-");
										
					String sql="INSERT INTO `item_sales`(`bill_number`, `time`, `cost`, `quantity`,"
							+ " `discount`, `item_name`, `emp_username`) "
							+ "VALUES (?,?,?,?,?,?,?)";
					
					PreparedStatement ps = conn.prepareStatement(sql);

					ps.setString(1, strTime);
					ps.setString(2, dateTimt);
					ps.setString(3, strPrice);
					ps.setString(4, strQuantity);
					ps.setString(5, strDiscount);
					ps.setString(6, strName);
					ps.setString(7, username);
					ps.executeUpdate();
					
					System.out.println("Inserted to sales...");
					
					//Update item quantity
					
					//SELECT item sold quantity
					Statement stat = conn.createStatement();
					String strSelectSold = "SELECT `sold` FROM items WHERE `name` = '"+strName+"'";
					ResultSet rs = stat.executeQuery(strSelectSold);
					rs.next();
					
					int intSold = rs.getInt("sold");
					int newSold = intSold + intQuantity;
					
					String strUpdateQuantity ="UPDATE `items` set `sold`=? WHERE `name` =?";  
					
					PreparedStatement psUpQuantity = conn.prepareStatement(strUpdateQuantity);
					psUpQuantity.setInt(1, newSold);
					psUpQuantity.setString(2, strName);
					psUpQuantity.executeUpdate();
					
				}
				catch (SQLException e) {
					e.printStackTrace();
				} finally {
					conn.close();
				}
			}
		}
    	conn.close();
    	
    	//clear the cart
    	vboxCartItems.getChildren().remove(1, arrNodes.size());    	
    	countOfItems.setText("0");
    	totalCoast.setText("0.0");
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Bill.fxml"));
    	Parent root = loader.load();
    	BillController billController = loader.getController();
    	billController.setBill(strTime);
    	
    	Stage stage = new Stage();
    	Scene scene = new Scene(root, 205, 417 + ((arrNodes.size() - 1) * 30));
    	stage.setScene(scene);
    	stage.initStyle(StageStyle.TRANSPARENT);
 
    	stage.show();
    }
}