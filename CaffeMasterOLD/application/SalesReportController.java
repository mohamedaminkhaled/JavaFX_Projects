package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
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
		
    	String selectBill = "SELECT `item_name`, `discount`, SUM(`quantity`), "
    					  + "SUM(`cost`) FROM `item_sales` "
    					  + "WHERE `time` >= '"+ dateFrom +"' AND `time` <= '"+ dateTo +"' GROUP BY `item_name`, `discount`";
		
    	Statement state = conn.createStatement();
		ResultSet rs = state.executeQuery(selectBill);
		rs.next();
		
		double totalCoast = 0.0;
						
		do {
			//Name
			String itemName = rs.getString("item_name");
			
			//quantity
			String strQuantity = rs.getString("SUM(`quantity`)");
			
			//Price
			String strPrice = rs.getString("SUM(`cost`)");
			
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
			newItem.setPrefHeight(15);
			
			newItem.getChildren().addAll(lblName, lblQuantity,
					lblDiscount, lblPrice);
			
			vboxBillContent.getChildren().add(newItem);
			vboxBillContent.setPrefHeight(vboxBillContent.getPrefHeight()+30);
			
			vboxContent.setPrefHeight(vboxContent.getPrefHeight()+30);
			
		}while(rs.next());
				
		String strTotalCoast = String.valueOf(totalCoast);
		
		Date date = new Date();
		DateFormat dateFormate = new SimpleDateFormat("YYYY/MM/dd");
		String dateTimt = dateFormate.format(date);
		dateTimt = dateTimt.replace("/","-");
		
		this.time.setText(dateTimt);
		total.setText(strTotalCoast+" EP");

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

