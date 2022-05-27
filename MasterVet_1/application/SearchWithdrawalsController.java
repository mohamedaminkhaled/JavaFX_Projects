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

public class SearchWithdrawalsController {

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
    FlowPane flowPaneContent;
    
    boolean isClient = false;
    boolean isOutOfStock = false;
    
    String key = null;
    String companyName;
    String employeeUsername;

    @FXML
    void searWithdrawalsReport(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {    	    	
		flowPaneContent.getChildren().clear();
		
		FXMLLoader loaderBill = new FXMLLoader(getClass().getResource("/AdminPages/WithdrawReport.fxml"));
		Parent root = loaderBill.load();

		WithdrawalsReportController withdrawalsReportController = loaderBill.getController();
		withdrawalsReportController.BorderPaneBill.setTop(null);
		withdrawalsReportController.dateFrom.setText(dateFromSales.getValue().toString());
		withdrawalsReportController.dateTo.setText(dateToSales.getValue().toString());
		
		withdrawalsReportController.nameAmount.setStyle("-fx-font-size: 12");
		withdrawalsReportController.nameFrom.setStyle("-fx-font-size: 12");
		withdrawalsReportController.nameReportTime.setStyle("-fx-font-size: 12");
		withdrawalsReportController.nameTime.setStyle("-fx-font-size: 12");
		withdrawalsReportController.nameTitle.setStyle("-fx-font-size: 12");
		withdrawalsReportController.nameTo.setStyle("-fx-font-size: 12");
		withdrawalsReportController.nameTotal.setStyle("-fx-font-size: 12");
		withdrawalsReportController.nameType.setStyle("-fx-font-size: 12");
		withdrawalsReportController.nameUser.setStyle("-fx-font-size: 12");
		
		withdrawalsReportController.dateFrom.setStyle("-fx-font-size: 12");
		withdrawalsReportController.dateTo.setStyle("-fx-font-size: 12");
		withdrawalsReportController.time.setStyle("-fx-font-size: 12");
		withdrawalsReportController.total.setStyle("-fx-font-size: 12");
		
		withdrawalsReportController.setReport(dateFromSales.getValue(), dateToSales.getValue());
		
		withdrawalsReportController.BorderPaneBill.setPrefHeight(
				withdrawalsReportController.BorderPaneBill.getPrefHeight() + 
				withdrawalsReportController.vboxBillContent.getPrefHeight() - 110);
		
		flowPaneContent.setOrientation(Orientation.VERTICAL);
		flowPaneContent.setPadding(new Insets(20, 100, 20, 100));
		flowPaneContent.getChildren().addAll(root);
			
    }
}