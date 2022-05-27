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

public class SearchClientReportController {

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
        
    String strClientName = null;

    @FXML
    void setClientReport(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
    	flowPaneContent.getChildren().clear();
    	flowPaneContent.setPadding(new Insets(20,0,0,100));
		
		FXMLLoader loaderBill = new FXMLLoader(getClass().getResource("/AdminPages/ClientReport.fxml"));
		Parent root = loaderBill.load();

		ClientSalesReportController clientSalesReportController = loaderBill.getController();
		clientSalesReportController.BorderPaneBill.setTop(null);
		clientSalesReportController.strClientName = strClientName;
		clientSalesReportController.dateFrom.setText(dateFromSales.getValue().toString());
		clientSalesReportController.dateTo.setText(dateToSales.getValue().toString());
		clientSalesReportController.setReport(dateFromSales.getValue(), dateToSales.getValue());
		
		clientSalesReportController.BorderPaneBill.setPrefHeight(
				clientSalesReportController.BorderPaneBill.getPrefHeight() + 
				clientSalesReportController.vboxBillContent.getPrefHeight() - 110);
		
		flowPaneContent.setOrientation(Orientation.VERTICAL);
		flowPaneContent.getChildren().addAll(root);
    }
}