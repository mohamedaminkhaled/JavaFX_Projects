package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Mycontroller {
	
	Connection con=null;
	Statement stat=null;
	ResultSet rs=null;
	
	@FXML
	private Label lblCardNumber;
	@FXML
	private Label lblPIN;
	@FXML
	public TextField tfCardNumber;
	@FXML
	private TextField tfPIN;
	@FXML
	private Label lblErrMain; 
	@FXML
	private Label lblSuccessOrFailed;
	@FXML
	private Label lblNewBalance;
	@FXML
	private TextField tfAmnt;
	
	public void controlGenerateCard(ActionEvent e) throws IOException {
		try {
			con = DBinfo.DBcon();
		
			System.out.println("Connected!");
		
			stat = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		
			String sqlPIN = "SELECT `PIN`, `STRIPNUMBER` FROM `CARD`";
			
			rs = stat.executeQuery(sqlPIN);
			
			rs.last();
			int rsLength = rs.getRow();
			
			rs.absolute(1);
			
			ArrayList<String> arrPINs = new ArrayList<>();
			ArrayList<String> arrSTRIP= new ArrayList<>();
			
			int i = 1;
			
			while(i <= rsLength) {
				rs.absolute(i++);
				arrPINs.add(rs.getString("PIN"));
				arrSTRIP.add(rs.getString("STRIPNUMBER"));
			}
			
			ArrayList<Integer> index= new ArrayList<>();
			int j = 0;
			while(j < rsLength) {
				index.add(j++);
			}
			
			Collections.shuffle(index);
			int randIndex = index.get(0);
			
			lblCardNumber.setText(arrSTRIP.get(randIndex));
			lblPIN.setText(arrPINs.get(randIndex));
			
			
		} catch(SQLException se) {
			DBinfo.errException(se);
		}
		
		}
	
	public void validateCard(ActionEvent e) {
		
		try {
			
			String strValidateStrip = "SELECT `BANKID` FROM CARD WHERE `STRIPNUMBER`= "+tfCardNumber.getText();
			String strValidatePIN = "SELECT `STRIPNUMBER` FROM CARD WHERE `PIN`= "+tfPIN.getText();
			String strgetName = "SELECT customercard.STRIPNUMBER, account.ACCNUMBER, customer.NAME" + 
					"	FROM customercard INNER JOIN account " + 
					"		ON customercard.ACCOUNTNUMBER = account.ACCNUMBER" + 
					"        			  INNER JOIN customer" + 
					"        ON account.CUSTOMERID = customer.CUSTOMERID" + 
					"	WHERE customercard.STRIPNUMBER ="+ tfCardNumber.getText();
			
			con = DBinfo.DBcon();
			System.out.println("Connected");
			stat = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			rs = stat.executeQuery(strValidateStrip);
			
			rs.last();
			System.out.println("success "+rs.getRow());
			
			if(rs.getRow() != 0) {
				rs = stat.executeQuery(strValidatePIN);
				rs.last();
				System.out.println(rs.getRow());
				if(rs.getRow() != 0) {
					lblErrMain.setText("");
					
					rs = stat.executeQuery(strgetName);
					rs.last();
					String name = rs.getString("NAME");
					System.out.println(name);
					
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/welcom.fxml"));
					Parent root = loader.load();
					//Parent root=FXMLLoader.load(getClass().getResource("welcom.fxml"));
					
					ControlOperations contOp = loader.getController();
					contOp.setLblCustmerName(name);
					contOp.setStrip(tfCardNumber.getText());
					
					Stage welcomStage=new Stage();
					Scene scene=new Scene(root,627,350);
					scene.getStylesheets().add(getClass().getResource("/styles/welcom.css").toExternalForm());
					welcomStage.setScene(scene);
					welcomStage.setTitle("Welcom Screen");
					welcomStage.show();
				}else {
					lblErrMain.setText("Incorrect PIN");
				}
			}else {
				lblErrMain.setText("Sorry This Card Not Valid!");
			}
			
		} catch (SQLException e2) {
			DBinfo.errException(e2);
		} catch (IOException e1) {
			e1.printStackTrace();
		}	
	}	
}
