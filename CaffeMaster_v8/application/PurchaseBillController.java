package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    Label goodName;

    @FXML
    Label quantName;

    @FXML
    Label priceName;

    @FXML
    Label suppName;
    
    @FXML
    Label userName;

    @FXML
    Label totalName;

    @FXML
    Label timeName;

    @FXML
    Label billName;

    @FXML
    VBox vboxBillContent;
    
    @FXML
    VBox vboxContent;
    
    @FXML
    HBox hboxHead;
    
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
    
    void setBill(double num) throws SQLException, ClassNotFoundException {
		Connection conn = DBinfo.connDB();
		
    	String selectBill = "SELECT * FROM goods_purchses WHERE `bill_number`="+num;
		
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
			//Good name
			String goodName= rs.getString("good_name");
			
			//Supplier
			String strSupplier = rs.getString("supplier");
			
			//Price
			double price = rs.getDouble("cost");
			totalCoast += price;
			
			//Quantity
			String strQuantity= rs.getString("quantity");
			
			//add new item to Bill
			Label lblName = new Label(goodName);
			lblName.setPrefWidth(103);
			lblName.setStyle("-fx-font-size: 10; -fx-padding:0 0 0 10;");
			
			Label lblQuantity = new Label(strQuantity);
			lblQuantity.setPrefWidth(85);
			lblQuantity.setStyle("-fx-font-size: 10");
			
			Label lblPrice = new Label(String.valueOf(price));
			lblPrice.setPrefWidth(79);
			lblPrice.setStyle("-fx-font-size: 10");
			
			Label lblSupplier = new Label(strSupplier);
			lblSupplier.setPrefWidth(96);
			lblSupplier.setStyle("-fx-font-size: 10");
			
			HBox newItem = new HBox(5);
			newItem.setPrefWidth(190);
			newItem.setPrefHeight(30);
			
			newItem.getChildren().addAll(lblName, lblQuantity,
					lblPrice, lblSupplier);
			
			vboxBillContent.getChildren().add(newItem);
			vboxBillContent.setPrefHeight(vboxBillContent.getPrefHeight()+30);
			
			vboxContent.setPrefHeight(vboxContent.getPrefHeight()+30);
			
		}while(rs.next());
				
		String strTotalCoast = String.valueOf(totalCoast);
		
		this.billNumber.setText(String.format("%.0f", billNumber));
		this.time.setText(time);
		total.setText(strTotalCoast+" EP"); 
		user.setText(employeeName);

    }

    @FXML
    void print(MouseEvent event) {
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, 
        PageOrientation.PORTRAIT, 0,0,0,0);
        PrinterJob job = PrinterJob.createPrinterJob();
        
        job.printPage(pageLayout, paneBillContent);
        job.endJob();

    }
}
