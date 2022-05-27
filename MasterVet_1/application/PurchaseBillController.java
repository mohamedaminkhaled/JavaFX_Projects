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
    Label nameStripes;

    @FXML
    Label nameUnits;

    @FXML
    Label nameKg;

    @FXML
    Label nameGm;

    @FXML
    Label nameCm;

    @FXML
    Label namePrice;

    @FXML
    Label nameSupplier;
    
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
			String medicineID = rs.getString("id");
			
			//Price
			String strPrice = rs.getString("price");
			
			//Supplier
			String strSupplier = rs.getString("SUPP_NAME");
			
			double price = Double.parseDouble(strPrice);
			totalCoast += price;
			
			String selectMedecinName = "SELECT `name` FROM medicines WHERE `id`='"+medicineID+"'";
			Statement stateMedName = conn.createStatement();
			ResultSet rsMedName = stateMedName.executeQuery(selectMedecinName);
			rsMedName.next();
			
			//Medicine name
			String strMedicineName = rsMedName.getString("name");
			
			//Unit and Quantity
			String strBoxes = rs.getString("boxes");
			String strStripes = rs.getString("stripes");
			String strUnits = rs.getString("units");
			String strKg = rs.getString("kg");
			String strGm = rs.getString("gms");
			String strCm = rs.getString("cm");
			
			//add new item to Bill
			Label lblName = new Label(strMedicineName);
			lblName.setPrefWidth(123);
			lblName.setStyle("-fx-font-size: 10; -fx-padding:0 0 0 10;");
			
			Label lblBoxes = new Label(strBoxes);
			lblBoxes.setPrefWidth(61);
			lblBoxes.setStyle("-fx-font-size: 10; -fx-padding:0 0 0 5;");
			
			Label lblStripes = new Label(strStripes);
			lblStripes.setPrefWidth(31);
			lblStripes.setStyle("-fx-font-size: 10; -fx-padding:0 0 0 5;");
			
			Label lblUnits = new Label(strUnits);
			lblUnits.setPrefWidth(30);
			lblUnits.setStyle("-fx-font-size: 10; -fx-padding:0 0 0 5;");
			
			Label lblKg = new Label(strKg);
			lblKg.setPrefWidth(62);
			lblKg.setStyle("-fx-font-size: 10; -fx-padding:0 0 0 5;");
			
			Label lblGm = new Label(strGm);
			lblGm.setPrefWidth(54);
			lblGm.setStyle("-fx-font-size: 10; -fx-padding:0 0 0 5;");
			
			Label lblCm = new Label(strCm);
			lblCm.setPrefWidth(51);
			lblCm.setStyle("-fx-font-size: 10; -fx-padding:0 0 0 5;");
			
			Label lblPrice = new Label(strPrice);
			lblPrice.setPrefWidth(51);
			lblPrice.setStyle("-fx-font-size: 10;");
			
			Label lblSupplier = new Label(strSupplier);
			lblSupplier.setPrefWidth(88);
			lblSupplier.setStyle("-fx-font-size: 10;");
			
			HBox newItem = new HBox(5);
			newItem.setPrefWidth(850);
			newItem.setPrefHeight(20);
			
			newItem.getChildren().addAll(lblName, lblBoxes, lblStripes,
					lblUnits, lblKg, lblGm, lblCm, lblPrice, lblSupplier);
			
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
		
		this.companyName.setText(rs2.getString("name"));
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
