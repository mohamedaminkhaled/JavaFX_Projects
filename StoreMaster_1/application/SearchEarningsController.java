package application;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class SearchEarningsController {

    @FXML
    private BorderPane borderPaneSearch;

    @FXML
    private HBox hboxTop;

    @FXML
    private DatePicker dateFromSales;

    @FXML
    private DatePicker dateToSales;

    @FXML
    private Button btnSearch;

    @FXML
    private FlowPane flowPaneContent;
    
    boolean isClient = false;
    boolean isOutOfStock = false;
    
    String key = null;
    String companyName;
    String employeeUsername;

    @FXML
    void searchEarningReport(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {    	    	
		flowPaneContent.getChildren().clear();
		
		FXMLLoader loaderBill = new FXMLLoader(getClass().getResource("/AdminPages/EarningReport.fxml"));
		Parent root = loaderBill.load();
		
		EarningReportController earningReportController = loaderBill.getController();
		earningReportController.BorderPaneBill.setTop(null);
		earningReportController.dateFrom.setText(dateFromSales.getValue().toString());
		earningReportController.dateTo.setText(dateToSales.getValue().toString());
		earningReportController.setReport(dateFromSales.getValue(), dateToSales.getValue());
		
		flowPaneContent.setOrientation(Orientation.VERTICAL);
		flowPaneContent.setPadding(new Insets(20, 0,20,300));
		flowPaneContent.getChildren().addAll(root);
			
    }
}