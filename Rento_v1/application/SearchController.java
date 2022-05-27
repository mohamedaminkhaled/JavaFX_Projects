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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class SearchController {

    @FXML
    private BorderPane borderPaneSearch;

    @FXML
    private HBox hboxTop;

    @FXML
    private DatePicker datePickerSearch;
    
    @FXML
    private TextField tfSearchName;

    @FXML
    private Button btnSearch;
    
    @FXML
    private Button btnSearch1;

    @FXML
    private FlowPane flowPaneContent;
        
    String key = null;
    
    String searchType = null;
    
	void getUsers(String strSelect) throws IOException, ClassNotFoundException, SQLException {
		Statement state;
		ResultSet rs;
		
		Connection conn	= null;
		try {
			conn=DBinfo.connDB();
			state=conn.createStatement();
			rs=state.executeQuery(strSelect);
			
			while(rs.next()) {
				FXMLLoader loaderClientCard = new FXMLLoader(getClass().getResource("/fxml/ClientCard.fxml"));
				Parent root = loaderClientCard.load();
				
				ClientCardController clientCardController = loaderClientCard.getController();
				
				if(searchType == "name") {
					clientCardController.setClientCard(rs.getString("id"));
				}else {
					clientCardController.setClientCard(rs.getString("client_id"));
				}
				
				flowPaneContent.getChildren().addAll(root);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}
    
    @FXML
    void serchClient(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
    	getSearchClients(event);
    }
    
    @FXML
    void searchByClientName(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
    	getSearchClientsByName(event);
    }
    
    @FXML
    void enterSearchDate(KeyEvent event) throws IOException, ClassNotFoundException, SQLException {
    	if (event.getCode() == KeyCode.ENTER) {
    		getSearchClients(event);
        }
    	
    	datePickerSearch.requestFocus();
    }
    
    void getSearchClients(Event event) throws IOException, ClassNotFoundException, SQLException {
    	if(datePickerSearch.getValue() != null) {
    		flowPaneContent.getChildren().clear();
	    	String dateTimt = datePickerSearch.getValue().toString();
			
			String strSelect = "SELECT `client_id` FROM client_cars "
					+ "WHERE `exit_date` LIKE '"+ dateTimt + "%' ORDER BY `client_id` DESC";
			
			searchType = "date";
			getUsers(strSelect);
    	}
    }
    
    void getSearchClientsByName(Event event) throws IOException, ClassNotFoundException, SQLException {
    	if(!tfSearchName.getText().isEmpty()) {
    		flowPaneContent.getChildren().clear();
			
			String strSelect = "SELECT `id` FROM clients "
					+ "WHERE `name` LIKE '"+ tfSearchName.getText() + "%' ORDER BY `id` DESC";
			
			searchType = "name";
			getUsers(strSelect);
    	}
    }
}