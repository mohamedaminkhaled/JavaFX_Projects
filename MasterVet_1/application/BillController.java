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
import javafx.print.PageRange;
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

public class BillController {

    @FXML
    BorderPane BorderPaneBill;

    @FXML
    private Pane paneBillContent;
    
    @FXML
    private Label companyName;

    @FXML
    private Label user;

    @FXML
    private Label total;

    @FXML
    private Label time;
    
    @FXML
    private Label billNumber;
    
    @FXML
    private Label lblDiscount;

    @FXML
    private Label lblNewTotal;

    @FXML
    VBox vboxBillContent;
    
    @FXML
    VBox vboxContent;
    
    @FXML
    HBox hboxHead;
    
    @FXML
    Pane paneBillFooter;
    
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
    
    void setBill(String num) throws SQLException, IOException, ClassNotFoundException {
		Connection conn = DBinfo.connDB();
		
    	String selectBill = "SELECT * FROM sales WHERE `no`= '"+num+"'";
		
    	Statement state = conn.createStatement();
		ResultSet rs = state.executeQuery(selectBill);
		rs.next();
		
		String billNumber = rs.getString("NO");
		String time = rs.getString("time");
		String discount = rs.getString("discount");
		String newTotal = rs.getString("new_total");
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
			
			double price = Double.parseDouble(strPrice);
			totalCoast += price;
			
			String selectMedecinName = "SELECT `name` FROM medicines WHERE `id`='"+medicineID+"'";
			Statement stateMedName = conn.createStatement();
			ResultSet rsMedName = stateMedName.executeQuery(selectMedecinName);
			rsMedName.next();
			
			//Medicine name
			String strMedicineName = rsMedName.getString("name");
			
			//Unit and Quantity
			String strUnit = "";
			String strQuantity = "";
			String strBoxes = rs.getString("boxes");
			String strStripes = rs.getString("stripes");
			String strUnits = rs.getString("units");
			String strKg = rs.getString("kg");
			String strGm = rs.getString("gms");
			String strCm = rs.getString("cm");
			
			if(strBoxes.equals(new String("0")) && strStripes.equals(new String("0"))
			   && strUnits.equals(new String("0")) && strGm.equals(new String("0")) 
			   && strCm.equals(new String("0"))) {
				strUnit = "Kg";
				strQuantity = strKg;
			}
			
			if(strBoxes.equals(new String("0")) && strStripes.equals(new String("0"))
			   && strUnits.equals(new String("0")) && strKg.equals(new String("0")) 
			   && strCm.equals(new String("0"))) {
				strUnit = "gm";
				strQuantity = strGm;
			}
			
			if(strBoxes.equals(new String("0")) && strStripes.equals(new String("0"))
			   && strUnits.equals(new String("0")) && strKg.equals(new String("0")) 
			   && strGm.equals(new String("0"))) {
				strUnit = "cm";
				strQuantity = strCm;
			}
			
			if(strBoxes.equals(new String("0")) && strStripes.equals(new String("0"))
			   && strCm.equals(new String("0")) && strKg.equals(new String("0")) 
			   && strGm.equals(new String("0"))) {
				strUnit = "unit";
				strQuantity = strUnits;
			}
			
			if(strBoxes.equals(new String("0")) && strUnits.equals(new String("0"))
			   && strCm.equals(new String("0")) && strKg.equals(new String("0")) 
			   && strGm.equals(new String("0"))) {
				strUnit = "stripes";
				strQuantity = strStripes;
			}
			
			if(strStripes.equals(new String("0")) && strUnits.equals(new String("0"))
			   && strCm.equals(new String("0")) && strKg.equals(new String("0")) 
			   && strGm.equals(new String("0"))) {
				strUnit = "box";
				strQuantity = strBoxes;
			}
			
			//add new item to Bill
			Label lblName = new Label(strMedicineName);
			lblName.setPrefWidth(185);
			lblName.setStyle("-fx-font-size: 14; -fx-padding:0 0 0 10;");
			
			Label lblQuantity = new Label(strQuantity);
			lblQuantity.setPrefWidth(55);
			lblQuantity.setStyle("-fx-font-size: 14");
			
			Label lblUnit = new Label(strUnit);
			lblUnit.setPrefWidth(59);
			lblUnit.setStyle("-fx-font-size: 14");
			
			Label lblPrice = new Label(strPrice);
			lblPrice.setPrefWidth(64);
			lblPrice.setStyle("-fx-font-size: 14");
			
			HBox newItem = new HBox(5);
			newItem.setPrefWidth(355);
			newItem.setPrefHeight(20);
			
			newItem.getChildren().addAll(lblName, lblQuantity,
					lblUnit, lblPrice);
			
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
		lblDiscount.setText(discount+" EP");
		lblNewTotal.setText(newTotal+" EP");
		user.setText(employeeName);
		
		conn.close();

    }

    @FXML
    void print(MouseEvent event) throws PrintException, IOException {
        
    	PrinterJob job = PrinterJob.createPrinterJob();
        
        printer.setScale(1);
        printer.setPrintRectangle(getPrintRectangle());
        boolean success = printer.print(job, true, paneBillContent);
        if (success) {
          job.endJob();
        }
            	
    }
    
    private Rectangle getPrintRectangle() {
        if (printRectangle == null) {
          printRectangle = new Rectangle(600, 1000, null);
          printRectangle.setStroke(Color.BLACK);
        }
        return printRectangle;
      }
}
