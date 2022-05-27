package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.print.PrintException;

import javafx.fxml.FXML;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BillController {

    @FXML
    BorderPane BorderPaneBill;

    @FXML
    private Pane paneBillContent;

    @FXML
    Label user;

    @FXML
    Label total;

    @FXML
    Label time;
    
    @FXML
    Label billNumber;
    
    @FXML
    Label orderName;

    @FXML
    Label quantName;

    @FXML
    Label discName;

    @FXML
    Label priceName;
    
    @FXML
    Label userName;

    @FXML
    Label totalName;

    @FXML
    Label timaName;

    @FXML
    Label billName;

    @FXML
    VBox vboxBillContent;
    
    @FXML
    VBox vboxContent;
    
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
    
    void setBill(String num) throws SQLException, IOException, ClassNotFoundException {
		Connection conn = DBinfo.connDB();
		
    	String selectBill = "SELECT * FROM item_sales WHERE `bill_number`= '"+num+"'";
		
    	Statement state = conn.createStatement();
		ResultSet rs = state.executeQuery(selectBill);
		rs.next();
		
		double billNumber = rs.getDouble("bill_number");
		String time = rs.getString("time");
		double totalCoast = 0.0;
		
		//Employee name
		String employeeUserame = rs.getString("emp_username");
		
		String selectEmployeeName = "SELECT `name` FROM employees WHERE `username`='"+employeeUserame+"'";
		Statement stateEmpName = conn.createStatement();
		ResultSet rsEmpName = stateEmpName.executeQuery(selectEmployeeName);
		rsEmpName.next();
		
		String employeeName = "ÛíÑ ãæÌæÏ";
		
		if(rsEmpName.getRow() > 0) {
			employeeName = rsEmpName.getString("name");
		}
						
		do {
			//Name
			String itemName = rs.getString("item_name");
			
			//quantity
			String strQuantity = rs.getString("quantity");
			
			//Price
			String strPrice = rs.getString("cost");
			
			double price = Double.parseDouble(strPrice);
			totalCoast += price;
			
				
			//Discount
			String strDiscount = rs.getString("discount");
			
			//add new item to Bill
			Label lblName = new Label(itemName);
			lblName.setPrefWidth(121);
			lblName.setStyle("-fx-font-size: 12; -fx-padding:0 0 0 10;");
			
			Label lblQuantity = new Label(strQuantity);
			lblQuantity.setPrefWidth(55);
			lblQuantity.setStyle("-fx-font-size: 12");
			
			Label lblDiscount = new Label(strDiscount);
			lblDiscount.setPrefWidth(59);
			lblDiscount.setStyle("-fx-font-size: 12");
			
			Label lblPrice = new Label(strPrice);
			lblPrice.setPrefWidth(64);
			lblPrice.setStyle("-fx-font-size: 12");
			
			HBox newItem = new HBox(5);
			newItem.setPrefWidth(317);
			newItem.setPrefHeight(30);
			
			newItem.getChildren().addAll(lblName, lblQuantity,
					lblDiscount, lblPrice);
			
			vboxBillContent.getChildren().add(newItem);
			vboxBillContent.setPrefHeight(vboxBillContent.getPrefHeight()+30);
			
			vboxContent.setPrefHeight(vboxContent.getPrefHeight()+30);
			
		}while(rs.next());
		
		conn.close();
				
		String strTotalCoast = String.valueOf(totalCoast);
		
		this.billNumber.setText(String.format("%.0f", billNumber));
		this.time.setText(time);
		total.setText(strTotalCoast+" EP"); 
		user.setText(employeeName);

    }

    @FXML
    void print(MouseEvent event) throws PrintException, IOException {

        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, 
        PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);
        PrinterJob job = PrinterJob.createPrinterJob();
        
        job.printPage(pageLayout, paneBillContent);
        job.endJob();
            	
    }
}

