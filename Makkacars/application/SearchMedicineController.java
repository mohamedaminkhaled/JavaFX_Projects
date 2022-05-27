package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class SearchMedicineController {

    @FXML
    private BorderPane borderPaneSearch;

    @FXML
    private HBox hboxTop;

    @FXML
    private DatePicker datePickerSearch;

    @FXML
    private Button btnSearch;

    @FXML
    private FlowPane flowPaneContent;
        
    String key = null;
    
	void getUsers(String strSelect) throws IOException {
		Statement state;
		ResultSet rs;
				
		try {
			Connection conn=DBinfo.connDB();
			state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs=state.executeQuery(strSelect);
			
			while(rs.next()) {
				FXMLLoader loaderClientCard = new FXMLLoader(getClass().getResource("/UserPages/ClientCard.fxml"));
				Parent root = loaderClientCard.load();

				ClientCardController clientCardController = loaderClientCard.getController();
				
				clientCardController.setClientCard(rs.getString("client_id"));
				
				//flowPaneContent.setOrientation(Orientation.VERTICAL);
				flowPaneContent.getChildren().addAll(root);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
    
    @FXML
    void serchMedicine(MouseEvent event) throws IOException {
    	getSearchClients(event);
    }
    
    @FXML
    void enterSearchDate(KeyEvent event) throws IOException {
    	if (event.getCode() == KeyCode.ENTER) {
    		getSearchClients(event);
        }
    	
    	datePickerSearch.requestFocus();
    }
    
    void getSearchClients(Event event) throws IOException {
    	if(datePickerSearch.getValue() != null) {
	    	
    		flowPaneContent.getChildren().clear();
	    	String dateTimt = datePickerSearch.getValue().toString();
			
			String strSelect = "SELECT `client_id` FROM client_cars "
					+ "WHERE `exit_date` LIKE '"+ dateTimt + "%' ORDER BY `client_id` DESC";
			getUsers(strSelect);
    	}
    }
}