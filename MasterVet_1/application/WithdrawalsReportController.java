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

public class WithdrawalsReportController {

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
    Label nameAmount;

    @FXML
    Label nameType;

    @FXML
    Label nameUser;

    @FXML
    Label nameTime;

    @FXML
    Pane paneBillFooter;

    @FXML
    Label total;

    @FXML
    Label time;

    @FXML
    Label nameTotal;

    @FXML
    Label nameReportTime;

    @FXML
    Label nameTitle;

    @FXML
    Label dateFrom;

    @FXML
    Label nameFrom;

    @FXML
    Label dateTo;

    @FXML
    Label nameTo;
    
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
		
    	String selectBill = "SELECT * FROM `withdrawals` "
    			+ "WHERE `time` >= '"+ dateFrom +"' AND `time` <= '"+ dateTo +"'";
		
    	Statement state = conn.createStatement();
		ResultSet rs = state.executeQuery(selectBill);
		rs.next();
		
		double totalCoast = 0.0;
						
		do {
			//Amount
			String strAmount = rs.getString("amount");
			
			//Type
			String strType = rs.getString("type");
			
			//User
			String strUser = rs.getString("user");
			
			//Time
			String strTime = rs.getString("time");
			
			double amnt = Double.parseDouble(strAmount);
			totalCoast += amnt;
			
			//add new item to Report
			Label lblAmount = new Label(strAmount);
			lblAmount.setPrefWidth(94);
			lblAmount.setStyle("-fx-font-size: 12; -fx-padding:0 0 0 15;");
			
			Label lblType = new Label(strType);
			lblType.setPrefWidth(182);
			lblType.setStyle("-fx-font-size: 12; ");
			
			Label lblUser = new Label(strUser);
			lblUser.setPrefWidth(114);
			lblUser.setStyle("-fx-font-size: 12; ");
			
			Label lblTime = new Label(strTime);
			lblTime.setPrefWidth(164);
			lblTime.setStyle("-fx-font-size: 12; ");
			
			HBox newItem = new HBox(5);
			newItem.setPrefWidth(317);
			newItem.setPrefHeight(20);
			
			newItem.getChildren().addAll(lblAmount, lblType, lblUser, lblTime);
			
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