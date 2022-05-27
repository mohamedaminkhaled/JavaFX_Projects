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

public class SearchPurchaseController {

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
    void searchPurchaseReport(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {    	    	
		flowPaneContent.getChildren().clear();
		
		FXMLLoader loaderBill = new FXMLLoader(getClass().getResource("/AdminPages/PurchaseReport.fxml"));
		Parent root = loaderBill.load();

		PurchaseReportController purchaseReportController = loaderBill.getController();
		purchaseReportController.BorderPaneBill.setTop(null);
		purchaseReportController.dateFrom.setText(dateFromSales.getValue().toString());
		purchaseReportController.dateTo.setText(dateToSales.getValue().toString());
		purchaseReportController.setReport(dateFromSales.getValue(), dateToSales.getValue());

		purchaseReportController.BorderPaneBill.setPrefHeight(
				purchaseReportController.BorderPaneBill.getPrefHeight() + 
				purchaseReportController.vboxBillContent.getPrefHeight() - 110);
		
		flowPaneContent.setOrientation(Orientation.VERTICAL);
		flowPaneContent.getChildren().addAll(root);
			
    }
}