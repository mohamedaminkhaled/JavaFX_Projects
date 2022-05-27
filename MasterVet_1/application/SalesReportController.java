package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import javax.print.PrintException;

import javafx.fxml.FXML;
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

public class SalesReportController {

    @FXML
    BorderPane BorderPaneBill;

    @FXML
    HBox hboxHead;

    @FXML
    Pane paneBillContent;

    @FXML
    VBox vboxContent;

    @FXML
    VBox vboxBillContent;

    @FXML
    Label orderName;

    @FXML
    Label quantName;

    @FXML
    Label discName;

    @FXML
    Label priceName;

    @FXML
    Label total;

    @FXML
    Label time;

    @FXML
    Label totalName;

    @FXML
    Label timaName;
    
    @FXML
    Label dateFrom;

    @FXML
    Label dateTo;
    
    @FXML
    Label lblDiscounts;

    @FXML
    Label lblNewTotal;
    
    @FXML
    Label lblReturns;
    
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
    
    void setReport(LocalDate dateFrom, LocalDate dateTo) throws SQLException, IOException, ClassNotFoundException {
		Connection conn = DBinfo.connDB();
		
		System.out.println(dateFrom);
		dateTo = dateTo.plusDays(1);
		
    	String selectBill = "SELECT medicines.name, sum(`boxes`), sum(sales.stripes) AS sum_stripes, sum(sales.units) AS sum_units, sum(`kg`), "
    					  + "sum(`gms`), sum(`cm`), sum(sales.price) AS sum_price FROM medicines INNER JOIN sales "
    					  + "on medicines.id = sales.id WHERE `time` >= '"+ dateFrom +"' AND `time` <= '"+ dateTo +"' GROUP BY medicines.name";
		
    	Statement state = conn.createStatement();
		ResultSet rs = state.executeQuery(selectBill);
		rs.next();
		
		double totalCoast = 0.0;
						
		do {
			//Name
			String itemName = rs.getString("name");
			
			//quantity
			String strBoxes = rs.getString("SUM(`boxes`)");
			String strStripes = rs.getString("sum_stripes");
			String strUnits = rs.getString("sum_units");
			String strKg = rs.getString("SUM(`kg`)");
			String strGm = rs.getString("SUM(`gms`)");
			String strCm = rs.getString("SUM(`cm`)");
			
			//Price
			String strPrice = rs.getString("sum_price");
			
			double price = Double.parseDouble(strPrice);
			totalCoast += price;
			
			//add new item to Bill
			Label lblName = new Label(itemName);
			lblName.setPrefWidth(147);
			lblName.setStyle("-fx-font-size: 10; -fx-padding:0 0 0 10;");
			
			Label lblBoxes = new Label(strBoxes);
			lblBoxes.setPrefWidth(71);
			lblBoxes.setStyle("-fx-font-size: 10; -fx-padding:0 0 0 5;");
			
			Label lblStripes = new Label(strStripes);
			lblStripes.setPrefWidth(46);
			lblStripes.setStyle("-fx-font-size: 10; -fx-padding:0 0 0 5;");
			
			Label lblUnits = new Label(strUnits);
			lblUnits.setPrefWidth(36);
			lblUnits.setStyle("-fx-font-size: 10; -fx-padding:0 0 0 5;");
			
			Label lblKg = new Label(strKg);
			lblKg.setPrefWidth(52);
			lblKg.setStyle("-fx-font-size: 10; -fx-padding:0 0 0 5;");
			
			Label lblGm = new Label(strGm);
			lblGm.setPrefWidth(56);
			lblGm.setStyle("-fx-font-size: 10; -fx-padding:0 0 0 5;");
			
			Label lblCm = new Label(strCm);
			lblCm.setPrefWidth(45);
			lblCm.setStyle("-fx-font-size: 10; -fx-padding:0 0 0 5;");
			
			Label lblPrice = new Label(strPrice);
			lblPrice.setPrefWidth(80);
			lblPrice.setStyle("-fx-font-size: 10;");
			
			HBox newItem = new HBox(5);
			newItem.setPrefWidth(317);
			newItem.setPrefHeight(20);
			
			newItem.getChildren().addAll(lblName, lblBoxes, lblStripes, lblUnits,
					lblKg, lblGm, lblCm, lblPrice);
			
			vboxBillContent.getChildren().add(newItem);
			vboxBillContent.setPrefHeight(vboxBillContent.getPrefHeight()+20);
			
			vboxContent.setPrefHeight(vboxContent.getPrefHeight()+20);
			
		}while(rs.next());
		
		String strSelectBills = "SELECT DISTINCT `no` FROM `sales`";
		rs = state.executeQuery(strSelectBills);
		
		double discount = 0.0;
		double totalDiscounts = 0.0;
		
		if(rs.next()) {
			ResultSet rs2; 
			Statement state2 = conn.createStatement();
			String strBillNo = null;
			do {
				strBillNo = rs.getString("no");
				String strSelectDiscounts = "SELECT `discount` FROM `sales` WHERE `no` = '"+strBillNo+"'";

				rs2 = state2.executeQuery(strSelectDiscounts);
				
				discount = Double.parseDouble(rs2.getString("discount"));
				totalDiscounts += discount;
				
			}while(rs.next());
		}
		
		double newTotal = totalCoast - totalDiscounts;
		
		//Set total returns
		double totalReturns = 0.0;
		String selectTypeAndPrice = "SELECT * FROM returns WHERE `time` >= '"+ dateFrom +"' AND `time` <= '"+ dateTo +"'";
		rs = state.executeQuery(selectTypeAndPrice);

		while(rs.next()) {
			double doublePrice = Double.parseDouble(rs.getString("amount"));
			totalReturns += doublePrice;
		}
						
		//String strTotalCoast = String.valueOf(totalCoast);
		
		//Display double with max two decimal digits
		DecimalFormat df = new DecimalFormat("#.##");
		String strTotalCoast = String.valueOf(df.format(totalCoast));
		String strTotalReturns = String.valueOf(df.format(totalReturns));
		
		//ex. 2021-01-19 12:10:26
		Date date = new Date();
		DateFormat dateFormate = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss a");
		String dateTimt = dateFormate.format(date);
		dateTimt = dateTimt.replace("/","-");
		
		//Select Company name
		Statement stat = conn.createStatement();
		String strSelectUsers = "SELECT `name` FROM `company`";
		ResultSet rs2 = stat.executeQuery(strSelectUsers);
		rs2.next();
		
		this.companyName.setText(rs2.getString("name"));
		this.time.setText(dateTimt);
		total.setText(strTotalCoast+" EP");
		lblDiscounts.setText(String.valueOf(totalDiscounts)+" EP");
		lblNewTotal.setText(String.valueOf(newTotal)+" EP");
		lblReturns.setText(strTotalReturns+" EP");
		
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