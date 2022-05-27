package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ControlOperations {
	
	Connection con=null;
	Statement stat=null;
	ResultSet rs=null;
	
	@FXML
	private Label lblCustomerName;
	
	private String stripFromMain ;
	
	private Date currentDateTime = new Date() ;
	
	public void controlWithdraw(ActionEvent e) {
		try {
			Stage stage=new Stage();
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/submitWithdraw.fxml"));
			Parent root = loader.load();
			
			SubmitWithdrawController submitWithdrawController = loader.getController();
			submitWithdrawController.setStrip(stripFromMain);
			
			Scene scene=new Scene(root,582,313);
			scene.getStylesheets().add(getClass().getResource("/styles/submitWithdraw.css").toExternalForm());
			stage.setScene(scene);
			stage.setTitle("Withdrawal Screen");
			stage.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void controlDeposit(ActionEvent e) {
		try {
			Stage stage=new Stage();
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/submitDeposit.fxml"));
			Parent root = loader.load();

			SubmitDepositController submitDepositController = loader.getController();
			submitDepositController.setStrip(stripFromMain);
			
			Scene scene=new Scene(root,582,313);
			scene.getStylesheets().add(getClass().getResource("/styles/submitWithdraw.css").toExternalForm());
			stage.setScene(scene);
			stage.setTitle("Deposit Screen");
			stage.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void controlCheck(ActionEvent e) {
		try {
			
			String strgetName = "SELECT * " + 
					"	FROM customercard INNER JOIN account " + 
					"		ON customercard.ACCOUNTNUMBER = account.ACCNUMBER" + 
					"        			  INNER JOIN customer" + 
					"        ON account.CUSTOMERID = customer.CUSTOMERID" + 
					"	WHERE customercard.STRIPNUMBER ="+ stripFromMain;
			
			con = DBinfo.DBcon();
			stat = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			rs = stat.executeQuery(strgetName);
			rs.last();
			String name = rs.getString("NAME");
			String bankIDFromQuery = rs.getString("BANKID");
			String accNum = rs.getString("ACCNUMBER");
			String balance = rs.getString("BALANCE");
			String accType = rs.getString("ACCTYPE");
			
			
			String bankNameFromQuery = "SELECT `BANKNAME` FROM BANK WHERE `BANKID` = "+bankIDFromQuery;
			
			rs = stat.executeQuery(bankNameFromQuery);
			rs.last();
			String bankName = rs.getString("BANKNAME");

			Stage stage=new Stage();
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/check.fxml"));
			Parent root = loader.load();
			
			CheckController checkController = loader.getController();
			
			checkController.setBankName(bankName);
			checkController.addToTextArea("Bank Name: "+bankName);
			checkController.addToTextArea("Bank ID: "+bankIDFromQuery);
			checkController.addToTextArea("Name: "+name);
			checkController.addToTextArea("Account number: "+accNum);
			checkController.addToTextArea("Account type: "+accType);
			checkController.addToTextArea("Account Balance: $"+balance);
			
			DateFormat dateFormate = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			String dateTimt = dateFormate.format(currentDateTime);
			checkController.addToTextArea("Current date time: "+dateTimt);
			
			Scene scene=new Scene(root,506,489);
			scene.getStylesheets().add(getClass().getResource("/styles/check.css").toExternalForm());
			stage.setScene(scene);
			stage.setTitle("Check Screen");
			stage.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (SQLException e2) {
			DBinfo.errException(e2);
		}
	}
	
	public void setLblCustmerName(String customerName) {
		lblCustomerName.setText(customerName);
	}
	
	public void setStrip(String stripNum) {
		stripFromMain = stripNum;
	}
}
