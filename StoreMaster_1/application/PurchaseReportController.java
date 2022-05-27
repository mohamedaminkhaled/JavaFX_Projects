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

public class PurchaseReportController {

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
    Label goodName;

    @FXML
    Label quantName;

    @FXML
    Label priceName;

    @FXML
    Label suppName;

    @FXML
    Pane paneBillFooter;

    @FXML
    Label total;

    @FXML
    Label time;

    @FXML
    Label totalName;

    @FXML
    Label timeName;
    
    @FXML
    Label dateFrom;

    @FXML
    Label dateTo;
    
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
		
    	String selectBill = "SELECT items.name, sum(purchases.quantity) AS sum_quantity,"
    			+ " sum(purchases.price) AS sum_price FROM items INNER JOIN purchases "
    					  + "on items.id = purchases.id WHERE `time` >= '"+ dateFrom +"' AND `time` <= '"+ dateTo +"' GROUP BY items.name";
		
    	Statement state = conn.createStatement();
		ResultSet rs = state.executeQuery(selectBill);
		rs.next();
		
		double totalCoast = 0.0;
						
		do {
			//Name
			String itemName = rs.getString("name");
			
			//quantity
			String strQuantity = rs.getString("sum_quantity");
			
			//Price
			String strPrice = rs.getString("sum_price");
			
			double price = Double.parseDouble(strPrice);
			totalCoast += price;
			
			//add new item to Bill
			Label lblName = new Label(itemName);
			lblName.setPrefWidth(130);
			lblName.setStyle("-fx-font-size: 12; -fx-padding:0 0 0 10;");
			
			Label lblQuantity = new Label(strQuantity);
			lblQuantity.setPrefWidth(49);
			lblQuantity.setStyle("-fx-font-size: 12; -fx-padding:0 0 0 5;");
			
			Label lblPrice = new Label(strPrice);
			lblPrice.setPrefWidth(59);
			lblPrice.setStyle("-fx-font-size: 12;");
			
			HBox newItem = new HBox(5);
			newItem.setPrefWidth(317);
			newItem.setPrefHeight(20);
			
			newItem.getChildren().addAll(lblName, lblQuantity, lblPrice);
			
			vboxBillContent.getChildren().add(newItem);
			vboxBillContent.setPrefHeight(vboxBillContent.getPrefHeight()+20);
			
			vboxContent.setPrefHeight(vboxContent.getPrefHeight()+20);
			
		}while(rs.next());
						
		//String strTotalCoast = String.valueOf(totalCoast);
		
		//Display double with max two decimal digits
		DecimalFormat df = new DecimalFormat("#.##");
		String strTotalCoast = String.valueOf(df.format(totalCoast));
		
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

