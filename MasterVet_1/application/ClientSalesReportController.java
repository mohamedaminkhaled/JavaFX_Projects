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
import javafx.fxml.FXMLLoader;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ClientSalesReportController {

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
    Label clientName;
    
    @FXML
    Label lblTotalDiscounts;

    @FXML
    Label companyName;
    
    String strClientName = null;
    
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
				
		Statement state = conn.createStatement();
		
		String strSelectBills = "SELECT DISTINCT `no` FROM sales "
								+ "WHERE `time` >= '"+ dateFrom +"' "
								+ "AND `time` <= '"+ dateTo +"' "
								+ "AND `client_name` = '"+strClientName+"'";
		
		ResultSet rs = state.executeQuery(strSelectBills);
		
		double discount = 0.0;
		double totalDiscounts = 0.0;
		double totalCoast = 0.0;
		
		if(rs.next()) {
			Statement state2 = conn.createStatement();
			String strBillNo = null;
			do {
				strBillNo = rs.getString("no");
				String strSelectDiscounts = "SELECT * FROM `sales` WHERE `no` = '"+strBillNo+"'";

				ResultSet rs2 = state2.executeQuery(strSelectDiscounts);
				
				discount = Double.parseDouble(rs2.getString("discount"));
				totalDiscounts += discount;
				
				//******************************
				
				//Price
				String strTotal = rs2.getString("new_total");
				
				//Bill number
				String strBillNumber = rs2.getString("no");
				
				//Client balance
				String strClientBalance = rs2.getString("client_balance");
				
				//Client debit
				String strClientDebit = rs2.getString("client_debit");
				
				//Time
				String strTime = rs2.getString("time");
				
				// Bill type
				String billType = "Sales";
				
				double price = Double.parseDouble(strTotal);
				totalCoast += price;
				
				//add new item to Bill
				Label lblBillNumber = new Label(strBillNumber);
				lblBillNumber.setPrefWidth(135);
				lblBillNumber.setStyle("-fx-font-size: 10; -fx-padding:0 0 0 10;");
								
				Label lblBillType = new Label(billType);
				lblBillType.setPrefWidth(80);
				lblBillType.setStyle("-fx-font-size: 10; -fx-padding:0 0 0 10;");
				
				Label lblBillTotal = new Label(strTotal);
				lblBillTotal.setPrefWidth(100);
				lblBillTotal.setStyle("-fx-font-size: 10; -fx-padding:0 0 0 20;");
				
				Label lblClientBalance = new Label(strClientBalance);
				lblClientBalance.setPrefWidth(80);
				lblClientBalance.setStyle("-fx-font-size: 10; -fx-padding:0 0 0 5;");
				
				Label lblClientDebit = new Label(strClientDebit);
				lblClientDebit.setPrefWidth(80);
				lblClientDebit.setStyle("-fx-font-size: 10; -fx-padding:0 0 0 5;");
				
				Label lblTime = new Label(strTime);
				lblTime.setPrefWidth(150);
				lblTime.setStyle("-fx-font-size: 10;");
				
				HBox newItem = new HBox(5);
				newItem.setPrefWidth(317);
				newItem.setPrefHeight(20);
				
				newItem.getChildren().addAll(lblBillNumber, lblBillType
						, lblBillTotal, lblClientBalance, lblClientDebit, lblTime);
				
				vboxBillContent.getChildren().add(newItem);
				vboxBillContent.setPrefHeight(vboxBillContent.getPrefHeight()+20);
				
				vboxContent.setPrefHeight(vboxContent.getPrefHeight()+20);
				
				//*****************************************
				
				newItem.setOnMouseClicked(e->{
					try {
						
						FXMLLoader loaderBill = new FXMLLoader(getClass().getResource("/fxml/Bill.fxml"));
						Parent root = loaderBill.load();

						BillController billController = loaderBill.getController();
						billController.setBill(lblBillNumber.getText());
						
						billController.BorderPaneBill.setPrefHeight(
								billController.BorderPaneBill.getPrefHeight() + 
								billController.vboxBillContent.getPrefHeight() - 40);
						
						Stage stage = new Stage();
				    	Scene scene = new Scene(root);
				    	stage.setScene(scene);
				    	stage.initStyle(StageStyle.TRANSPARENT);
				 
				    	stage.show();
						
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				});
				
				newItem.setOnMouseEntered(e->{
					newItem.setStyle("-fx-background-color: lightblue");
				});
				
				newItem.setOnMouseExited(e->{
					newItem.setStyle(null);
				});
				//*********************************************
				
			}while(rs.next());
		}
		
		//********************************************
		
		String strSelectPurchasesBills = "SELECT DISTINCT `no` FROM `purchases` "
				+ "WHERE `time` >= '"+ dateFrom +"' "
				+ "AND `time` <= '"+ dateTo +"' "
				+ "AND `supp_name` = '"+strClientName+"'";

		ResultSet rsPurchases = state.executeQuery(strSelectPurchasesBills);
		
		if(rsPurchases.next()) {
			Statement state2 = conn.createStatement();
			String strBillNo = null;
			do {
				strBillNo = rsPurchases.getString("no");
				String strSelectDiscounts = "SELECT * FROM `purchases` WHERE `no` = '"+strBillNo+"'";
				
				ResultSet rs2 = state2.executeQuery(strSelectDiscounts);
								
				//******************************
				
				//Price
				String strTotal = rs2.getString("price");
				
				//Bill number
				String strBillNumber = rs2.getString("no");
				
				//Client balance
				String strClientBalance = rs2.getString("client_balance");
				
				//Client debit
				String strClientDebit = rs2.getString("client_debit");
				
				//Time
				String strTime = rs2.getString("time");
				
				// Bill type
				String billType = "Purchases";
				
				double price = Double.parseDouble(strTotal);
				totalCoast += price;
				
				//add new item to Bill
				Label lblBillNumber = new Label(strBillNumber);
				lblBillNumber.setPrefWidth(135);
				lblBillNumber.setStyle("-fx-font-size: 10; -fx-padding:0 0 0 10;");
				
				Label lblBillType = new Label(billType);
				lblBillType.setPrefWidth(80);
				lblBillType.setStyle("-fx-font-size: 10; -fx-padding:0 0 0 10;");
				
				Label lblBillTotal = new Label(strTotal);
				lblBillTotal.setPrefWidth(100);
				lblBillTotal.setStyle("-fx-font-size: 10; -fx-padding:0 0 0 20;");
				
				Label lblClientBalance = new Label(strClientBalance);
				lblClientBalance.setPrefWidth(80);
				lblClientBalance.setStyle("-fx-font-size: 10; -fx-padding:0 0 0 5;");
				
				Label lblClientDebit = new Label(strClientDebit);
				lblClientDebit.setPrefWidth(80);
				lblClientDebit.setStyle("-fx-font-size: 10; -fx-padding:0 0 0 5;");
				
				Label lblTime = new Label(strTime);
				lblTime.setPrefWidth(150);
				lblTime.setStyle("-fx-font-size: 10;");
				
				HBox newItem = new HBox(5);
				newItem.setPrefWidth(317);
				newItem.setPrefHeight(20);
				
				newItem.getChildren().addAll(lblBillNumber, lblBillType
						, lblBillTotal, lblClientBalance, lblClientDebit, lblTime);
				
				vboxBillContent.getChildren().add(newItem);
				vboxBillContent.setPrefHeight(vboxBillContent.getPrefHeight()+20);
				
				vboxContent.setPrefHeight(vboxContent.getPrefHeight()+20);
				
				//*****************************************
				
				newItem.setOnMouseClicked(e->{
					try {
						
						FXMLLoader loaderBill = new FXMLLoader(getClass().getResource("/fxml/PurchaseBill.fxml"));
						Parent root = loaderBill.load();

						PurchaseBillController billController = loaderBill.getController();
						billController.setBill(lblBillNumber.getText());
						
						billController.BorderPaneBill.setPrefHeight(
								billController.BorderPaneBill.getPrefHeight() + 
								billController.vboxBillContent.getPrefHeight() - 110);
						
						Stage stage = new Stage();
				    	Scene scene = new Scene(root);
				    	stage.setScene(scene);
				    	stage.initStyle(StageStyle.TRANSPARENT);
				 
				    	stage.show();
						
						
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				});
				
				newItem.setOnMouseEntered(e->{
					newItem.setStyle("-fx-background-color: lightblue");
				});
				
				newItem.setOnMouseExited(e->{
					newItem.setStyle(null);
				});
				//*********************************************
			
			}while(rs.next());
		}
		
		//********************************************
		
		//Select Company name
		Statement stat = conn.createStatement();
		String strSelectUsers = "SELECT `name` FROM `company`";
		ResultSet rs3 = stat.executeQuery(strSelectUsers);
		rs3.next();
								
		//Display double with max two decimal digits
		DecimalFormat df = new DecimalFormat("#.##");
		String strTotalCoast = String.valueOf(df.format(totalCoast));
				
		//ex. 2021-01-19 12:10:26
		Date date = new Date();
		DateFormat dateFormate = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss a");
		String dateTimt = dateFormate.format(date);
		dateTimt = dateTimt.replace("/","-");
				
		this.companyName.setText(rs3.getString("name"));
		this.clientName.setText(strClientName);
		this.time.setText(dateTimt);
		lblTotalDiscounts.setText(String.valueOf(totalDiscounts)+" EP");
		
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