package application;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class SearchSalesController {

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
    void searchSalesReport(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {    	    	
		flowPaneContent.getChildren().clear();
		
		FXMLLoader loaderBill = new FXMLLoader(getClass().getResource("/fxml/SalesReport.fxml"));
		Parent root = loaderBill.load();

		SalesReportController salesReportController = loaderBill.getController();
		salesReportController.BorderPaneBill.setTop(null);
		salesReportController.setReport(dateFromSales.getValue(), dateToSales.getValue());
		
		salesReportController.discName.setStyle("-fx-font-size: 12");
		salesReportController.orderName.setStyle("-fx-font-size: 12");
		salesReportController.priceName.setStyle("-fx-font-size: 12");
		salesReportController.quantName.setStyle("-fx-font-size: 12");
		salesReportController.timaName.setStyle("-fx-font-size: 12");
		salesReportController.totalName.setStyle("-fx-font-size: 12");
		salesReportController.time.setStyle("-fx-font-size: 12");
		salesReportController.total.setStyle("-fx-font-size: 12");
		
		
		salesReportController.BorderPaneBill.setPrefHeight(
				salesReportController.BorderPaneBill.getPrefHeight() + 
				salesReportController.vboxBillContent.getPrefHeight() - 110);
		
		flowPaneContent.setOrientation(Orientation.VERTICAL);
		flowPaneContent.getChildren().addAll(root);
			
    }
}