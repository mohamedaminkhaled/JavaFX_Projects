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

public class SearchUserReportController {

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
        
    String userName;
    String employeeUsername;

    @FXML
    void searchSalesReport(MouseEvent event) throws IOException, SQLException, ClassNotFoundException {    	    	
		flowPaneContent.getChildren().clear();
		flowPaneContent.setPadding(new Insets(20,0,0,250));
		
		FXMLLoader loaderBill = new FXMLLoader(getClass().getResource("/AdminPages/UserReport.fxml"));
		Parent root = loaderBill.load();

		UserReportController userReportController = loaderBill.getController();
		userReportController.BorderPaneBill.setTop(null);
		userReportController.userName = userName;
		userReportController.dateFrom.setText(dateFromSales.getValue().toString());
		userReportController.dateTo.setText(dateToSales.getValue().toString());
		userReportController.setReport(dateFromSales.getValue(), dateToSales.getValue());
		
		userReportController.lblUser.setStyle("-fx-font-size: 12;");
		userReportController.dateFrom.setStyle("-fx-font-size: 12;");
		userReportController.dateTo.setStyle("-fx-font-size: 12;");
		userReportController.time.setStyle("-fx-font-size: 12;");
		userReportController.total.setStyle("-fx-font-size: 12;");
		
		userReportController.nameFrom.setStyle("-fx-font-size: 12;");
		userReportController.nameTo.setStyle("-fx-font-size: 12;");
		userReportController.nameLoan.setStyle("-fx-font-size: 12;");
		userReportController.nameTime.setStyle("-fx-font-size: 12;");
		userReportController.nameTimeHrad.setStyle("-fx-font-size: 12;");
		userReportController.nameTotal.setStyle("-fx-font-size: 12;");
		userReportController.nameUser.setStyle("-fx-font-size: 12;");
		
		userReportController.BorderPaneBill.setPrefHeight(
				userReportController.BorderPaneBill.getPrefHeight() + 
				userReportController.vboxBillContent.getPrefHeight() - 110);
		
		flowPaneContent.setOrientation(Orientation.VERTICAL);
		flowPaneContent.getChildren().addAll(root);
			
    }
}