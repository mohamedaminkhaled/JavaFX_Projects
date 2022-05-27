package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class LogRowController {

    @FXML
    private Label employeeName;

    @FXML
    private Label login;

    @FXML
    private Label logout;
        
    void setLogRow(double no) {
		Statement state;
		ResultSet rs;
				
		String strSelect = "SELECT * FROM log WHERE `no` = "+no;
		
		try {
			Connection conn = DBinfo.connDB();			
			state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs=state.executeQuery(strSelect);
			rs.last();
			
			String loginTimt = rs.getString("login");
			String logoutTimt = rs.getString("logout");
			
			String strSelectEmployeeName = "SELECT `name` FROM Employees WHERE `id` ="+rs.getString("EMP_ID");
			rs=state.executeQuery(strSelectEmployeeName);
			rs.first();
			
			String selectedEmployeeName = rs.getString("name");
						
			employeeName.setText(selectedEmployeeName);
			login.setText(loginTimt);
			logout.setText(logoutTimt);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

}
