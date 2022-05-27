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

public class EarningReportController {

    @FXML
    BorderPane BorderPaneBill;

    @FXML
    HBox hboxHead;

    @FXML
    Pane paneBillContent;

    @FXML
    VBox vboxContent;

    @FXML
    Pane paneBillFooter;

    @FXML
    Label time;

    @FXML
    Label dateFrom;

    @FXML
    Label dateTo;

    @FXML
    Label purchases;

    @FXML
    Label withdrawals;

    @FXML
    Label sales;

    @FXML
    Label earnings;
    
    @FXML
    private Label nameTime;

    @FXML
    private Label nameTitle;
    
    @FXML
    private Label nameFrom;
    
    @FXML
    private Label nameTo;
    
    @FXML
    private Label namePurchases;
    
    @FXML
    private Label nameWithdrawals;
    
    @FXML
    private Label nameSales;
    
    @FXML
    private Label nameEarnings;
    
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
		
    	String selectBill = "SELECT `price` FROM `purchases`"
    			+ "WHERE `time` >= '"+ dateFrom +"' AND `time` <= '"+ dateTo +"'";
		
    	Statement state = conn.createStatement();
		ResultSet rs = state.executeQuery(selectBill);
		rs.next();
		
		double totalPurchases = 0.0;
						
		do {
			//Price
			String strPrice = rs.getString("price");
			
			double price = Double.parseDouble(strPrice);
			totalPurchases += price;
			
		}while(rs.next());
		
		//*************************************
		
		String selectWithdrawals = "SELECT `amount` FROM `withdrawals`"
    			+ "WHERE `time` >= '"+ dateFrom +"' AND `time` <= '"+ dateTo +"'";
		
		rs = state.executeQuery(selectWithdrawals);
		rs.next();
		
		double totalWithdrawals = 0.0;
		
		do {
			//Price
			String strPrice = rs.getString("amount");
			
			double price = Double.parseDouble(strPrice);
			totalWithdrawals += price;
			
		}while(rs.next());
		
		//*************************************
				
		String selectSales = "SELECT `new_total` FROM `sales`"
    			+ "WHERE `time` >= '"+ dateFrom +"' AND `time` <= '"+ dateTo +"'";
		
		rs = state.executeQuery(selectSales);
		rs.next();
		
		double totalSales = 0.0;
		
		do {
			//Price
			String strPrice = rs.getString("new_total");
			
			double price = Double.parseDouble(strPrice);
			totalSales += price;
			
		}while(rs.next());
		
		//*************************************
				
		double totalwithdrawalAmount = totalPurchases + totalWithdrawals;
		double earnings = totalSales - totalwithdrawalAmount;
		
		//String strTotalCoast = String.valueOf(totalCoast);
		
		//Display double with max two decimal digits
		DecimalFormat df = new DecimalFormat("#.##");
		String strtotalPurchases = String.valueOf(df.format(totalPurchases));
		String strtotalSales = String.valueOf(df.format(totalSales));
		String strtotalWithdrawals = String.valueOf(df.format(totalWithdrawals));
		String strEarnings = String.valueOf(df.format(earnings));

		
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
		this.dateFrom.setStyle("-fx-font-size: 12");
		this.dateTo.setStyle("-fx-font-size: 12");
		this.earnings.setStyle("-fx-font-size: 12");
		this.purchases.setStyle("-fx-font-size: 12");
		this.sales.setStyle("-fx-font-size: 12");
		this.withdrawals.setStyle("-fx-font-size: 12");
		this.time.setStyle("-fx-font-size: 12");
		this.nameEarnings.setStyle("-fx-font-size: 12");
		this.nameFrom.setStyle("-fx-font-size: 12");
		this.namePurchases.setStyle("-fx-font-size: 12");
		this.nameSales.setStyle("-fx-font-size: 12");
		this.nameTime.setStyle("-fx-font-size: 12");
		this.nameTitle.setStyle("-fx-font-size: 12; -fx-background-color: lightgrey;");
		this.nameTo.setStyle("-fx-font-size: 12");
		this.nameWithdrawals.setStyle("-fx-font-size: 12");
		
		this.purchases.setText(strtotalPurchases + " EP");
		this.sales.setText(strtotalSales + " EP");
		this.withdrawals.setText(strtotalWithdrawals + " EP");
		this.earnings.setText(strEarnings + " EP");
		
		this.time.setText(dateTimt);
		
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
