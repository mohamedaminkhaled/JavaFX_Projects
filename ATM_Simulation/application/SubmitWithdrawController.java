package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SubmitWithdrawController {
	
	Connection con=null;
	Statement stat=null;
	ResultSet rs=null;
	
	@FXML
	private Label lblSuccessOrFailed;
	@FXML
	private Label lblNewBalance;
	@FXML
	private TextField tfAmnt;
	
	private String stripFromMain ;
	
	public void controlSubmitWithdraw(ActionEvent e) {
		try {
			System.out.println("Welcom to controlSubmitWithdraw");
			
			String strip = stripFromMain;
			System.out.println(strip);
						
			String strAmnt = tfAmnt.getText();
			System.out.println("tfAmnt: "+strAmnt);
			
			String bankIDFromStrip = strip.substring(0, 4);
			String strAccNum = "SELECT `ACCOUNTNUMBER` FROM CUSTOMERCARD WHERE `STRIPNUMBER` = "+strip;
			
			System.out.println("After substring!");

			con = DBinfo.DBcon();
			System.out.println("Connected!");
			stat = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			rs = stat.executeQuery(strAccNum);
			rs.last();
			
			String accNumFromQuery = rs.getString("ACCOUNTNUMBER");
			
			double amnt = Double.parseDouble(strAmnt);
			
			System.out.println("strip: "+strip);
			System.out.println("bankIDFromStrip: "+bankIDFromStrip);
			System.out.println("accNumFromQuery: "+accNumFromQuery);
			
			String strGetBalance = "SELECT `BALANCE` FROM ACCOUNT WHERE `BANKID` = "+bankIDFromStrip+" AND `ACCNUMBER` = "+accNumFromQuery;
			
			rs = stat.executeQuery(strGetBalance);
			rs.last();
			System.out.println("BALANCE ROWS"+rs.getRow());
			
			String strBalance = rs.getString("BALANCE");
			double balance = Double.parseDouble(strBalance);
			
			double newBalnce = balance - amnt;
			
			String strUpdateBalance = "UPDATE ACCOUNT SET `BALANCE` = "+newBalnce+" WHERE `ACCNUMBER` = "+accNumFromQuery;
			stat.executeUpdate(strUpdateBalance);
			
			if(amnt > balance) {
				lblSuccessOrFailed.setText("Sorry: Not enough funds!");
			}else {
				lblSuccessOrFailed.setText("Operation performed successfully");
				lblNewBalance.setText("Your new balance: "+ newBalnce);
			}	
		}  catch (SQLException e1) {
			DBinfo.errException(e1);
		}
	}
	
	public void setStrip(String stripNum) {
		stripFromMain = stripNum;
	}
}
