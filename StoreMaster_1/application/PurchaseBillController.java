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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class PurchaseBillController {

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
    VBox vboxBillContent;
    
    @FXML
    VBox vboxContent;
    
    @FXML
    HBox hboxHead;
    
    @FXML
    Label nameMedicine;

    @FXML
    Label nameBoxes;

    @FXML
    Label namePrice;
    
    @FXML
    Label nameUser;

    @FXML
    Label nameTotal;

    @FXML
    Label nameTime;

    @FXML
    Label nameBill;
    
    @FXML
    Label companyName;
    
    private Rectangle printRectangle;
    private NodePrinter printer = new NodePrinter();
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
    
    void setBill(String num) throws SQLException, ClassNotFoundException {
		Connection conn = DBinfo.connDB();
		
    	String selectBill = "SELECT * FROM purchases WHERE `no`= '"+num+"'";
		
    	Statement state = conn.createStatement();
		ResultSet rs = state.executeQuery(selectBill);
		rs.next();
		
		String billNumber = rs.getString("NO");
		String time = rs.getString("time");
		double totalCoast = 0.0;
		
		//Employee name
		String employeeUserame = rs.getString("EMP_USERNAME");
		
		String selectEmployeeName = "SELECT `name` FROM employees WHERE `username`='"+employeeUserame+"'";
		Statement stateEmpName = conn.createStatement();
		ResultSet rsEmpName = stateEmpName.executeQuery(selectEmployeeName);
		rsEmpName.next();
		
		String employeeName = "ÛíÑ ãæÌæÏ";
		
		if(rsEmpName.getRow() > 0) {
			employeeName = rsEmpName.getString("name");
		}
		 						
		do {
			String itemId = rs.getString("id");
			
			//Price
			String strPrice = rs.getString("price");
			
			double price = Double.parseDouble(strPrice);
			totalCoast += price;
			
			String selectMedecinName = "SELECT `name` FROM `items` WHERE `id`='"+itemId+"'";
			Statement stateMedName = conn.createStatement();
			ResultSet rsItemName = stateMedName.executeQuery(selectMedecinName);
			rsItemName.next();
			
			//Medicine name
			String strMedicineName = rsItemName.getString("name");
			
			//Item Quantity
			String strQuantity = rs.getString("quantity");
			
			//add new item to Bill
			Label lblName = new Label(strMedicineName);
			lblName.setPrefWidth(135);
			lblName.setStyle("-fx-font-size: 12; -fx-padding:0 0 0 10;");
			
			Label lblQuantity = new Label(strQuantity);
			lblQuantity.setPrefWidth(55);
			lblQuantity.setStyle("-fx-font-size: 12; -fx-padding:0 0 0 5;");
			
			Label lblPrice = new Label(strPrice);
			lblPrice.setPrefWidth(46);
			lblPrice.setStyle("-fx-font-size: 12;");
				
			HBox newItem = new HBox(5);
			newItem.setPrefWidth(850);
			newItem.setPrefHeight(20);
			
			newItem.getChildren().addAll(lblName, lblQuantity, lblPrice);
			
			vboxBillContent.getChildren().add(newItem);
			vboxBillContent.setPrefHeight(vboxBillContent.getPrefHeight()+20);
			
			vboxContent.setPrefHeight(vboxContent.getPrefHeight()+20);
			
		}while(rs.next());
				
		String strTotalCoast = String.valueOf(totalCoast);
		
		//Select Company name
		Statement stat = conn.createStatement();
		String strSelectUsers = "SELECT `name` FROM `company`";
		ResultSet rs2 = stat.executeQuery(strSelectUsers);
		rs2.next();
		
		this.billNumber.setText(billNumber);
		this.time.setText(time);
		total.setText(strTotalCoast+" EP"); 
		user.setText(employeeName);
		
		conn.close();

    }

    @FXML
    void print(MouseEvent event) throws PrintException, IOException {
        
    	PrinterJob job = PrinterJob.createPrinterJob();
        
        printer.setScale(1.2);
        printer.setPrintRectangle(getPrintRectangle());
        boolean success = printer.print(job, true, paneBillContent);
        if (success) {
          job.endJob();
        }
            	
    }
    
    private Rectangle getPrintRectangle() {
        if (printRectangle == null) {
          printRectangle = new Rectangle(vboxContent.getPrefWidth()*(1.2), vboxContent.getPrefHeight()*(1.2), null);
          printRectangle.setStroke(Color.BLACK);
        }
        return printRectangle;
      }
}
