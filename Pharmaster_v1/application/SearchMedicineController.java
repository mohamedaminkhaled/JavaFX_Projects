package application;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class SearchMedicineController {

    @FXML
    BorderPane borderPaneSearch;

    @FXML
    TextField tfSearch;

    @FXML
    private Button btnSearch;
    
    @FXML
    Button btnBills;    
    
    @FXML
    FlowPane flowPaneContent;
    
    @FXML
    DatePicker datePickerSearch;
    
    @FXML
    Button btnBack;
    
    @FXML
    HBox hboxTop;
    
    boolean isClient = false;
    boolean isOutOfStock = false;
    
    String key = null;
    String companyName;
    String employeeUsername;
    
    void getMedicines(String strSelect) throws IOException {

    	Statement state;
		ResultSet rs;
				
		try {
			Connection conn=DBinfo.connDB();
			state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs=state.executeQuery(strSelect);
			
			if(!isClient) {
				hboxTop.getChildren().remove(btnBills);
			}
			
			while(rs.next()) {
				FXMLLoader loaderMedicineCard = new FXMLLoader(getClass().getResource("/UserPages/MedicineCard.fxml"));
				Parent root = loaderMedicineCard.load();

				MedicineCardController medicineCardController = loaderMedicineCard.getController();
				medicineCardController.employeeUsername = employeeUsername;
				medicineCardController.setMedicineCard(rs.getString("id"));
				
				if(key == "getFinishedMedicines") {
					medicineCardController.btnSell.setDisable(true);
					hboxTop.getChildren().remove(btnBills);
				}
				
				if(key == "getOutOfStock") {
					medicineCardController.btnSell.setDisable(true);
					medicineCardController.btnAdd.setDisable(true);
				}
				
				if(key == "searchCompanyMedicines") {
					medicineCardController.btnSell.setDisable(true);
					medicineCardController.btnAdd.setDisable(true);
				}
				
				if(isClient) {
					medicineCardController.btnAdd.setDisable(true);
					medicineCardController.btnView.setDisable(true);
					//medicineCardController.hboxButtons.getChildren().removeAll(medicineCardController.btnDelete, medicineCardController.btnView);
				}
				flowPaneContent.getChildren().addAll(root);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		} 
    }
    
	void getUsers(String strSelect) throws IOException {
    	
		if(!isClient) {
    		hboxTop.getChildren().remove(btnBills);
    	}
		
		Statement state;
		ResultSet rs;
				
		try {
			Connection conn=DBinfo.connDB();
			state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs=state.executeQuery(strSelect);
			
			while(rs.next()) {
				FXMLLoader loaderMedicineCard = new FXMLLoader(getClass().getResource("/AdminPages/UserCard.fxml"));
				Parent root = loaderMedicineCard.load();

				UserCardController userCardController = loaderMedicineCard.getController();
				
				userCardController.setUserCard(rs.getString("id"));
				
				flowPaneContent.getChildren().addAll(root);
			}
		} catch (SQLException e) {
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		} 
	}
    
    void getCompanies(String strSelectSimilarCompanies) throws IOException {
    	
    	if(!isClient) {
    		hboxTop.getChildren().remove(btnBills);
    	}
    	
    	Statement state;
		ResultSet rs;
				
		try {
			Connection conn=DBinfo.connDB();
			state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs=state.executeQuery(strSelectSimilarCompanies);
			
			while(rs.next()) {
				FXMLLoader loaderCompanyCard = new FXMLLoader(getClass().getResource("/UserPages/CompanyCards.fxml"));
				Parent root = loaderCompanyCard.load();

				CompanyCardController companyCardController = loaderCompanyCard.getController();
				
				companyCardController.setCompanyCard(rs.getString("name"));
				flowPaneContent.getChildren().addAll(root);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		} 
    }
    
    void getSuppliers(String strSelectSuppliers) throws IOException {
    	
    	if(!isClient) {
    		hboxTop.getChildren().remove(btnBills);
    	}
    	
    	Statement state;
		ResultSet rs;
				
		try {
			Connection conn=DBinfo.connDB();
			state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs=state.executeQuery(strSelectSuppliers);
			
			while(rs.next()) {
				FXMLLoader loaderSupplierCard = new FXMLLoader(getClass().getResource("/AdminPages/SupplierCard.fxml"));
				Parent root = loaderSupplierCard.load();

				SupplierCardController supplierCardController = loaderSupplierCard.getController();
				
				supplierCardController.setSupplierCard(rs.getInt("id"));
				flowPaneContent.getChildren().addAll(root);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		} 
    }
        
    void getLog(String selectLog) throws IOException {
    	
    	hboxTop.getChildren().removeAll(btnBills, tfSearch);
    	datePickerSearch.setVisible(true);
    	
    	Statement state;
		ResultSet rs;
		
		flowPaneContent.setPadding(new Insets(10,10,10,10));
		flowPaneContent.setHgap(3);
		flowPaneContent.setVgap(3);
		
		FXMLLoader loaderLogHeder = new FXMLLoader(getClass().getResource("/AdminPages/LogHeder.fxml"));
		Parent root = loaderLogHeder.load();
		flowPaneContent.getChildren().addAll(root);
				
		try {
			Connection conn=DBinfo.connDB();
			state = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = state.executeQuery(selectLog);
			
			while(rs.next()) {
				FXMLLoader loaderLogRow = new FXMLLoader(getClass().getResource("/AdminPages/LogRow.fxml"));
				root = loaderLogRow.load();

				LogRowController logRowController = loaderLogRow.getController();
				logRowController.setLogRow(rs.getDouble("no"));
				
				flowPaneContent.getChildren().addAll(root);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		}
    }
    
    @FXML
    void backToMedicines(MouseEvent event) throws IOException {
    	btnBack.setVisible(false);
    	hboxTop.getChildren().add(2, btnBills);
    	hboxTop.getChildren().add(1, tfSearch);
    	datePickerSearch.setVisible(false);
    	flowPaneContent.getChildren().clear();
    	key = "getTotalMedicines";
		flowPaneContent.setOrientation(Orientation.HORIZONTAL);
    	
    	//view all medicines
    	String strSelect = "SELECT `id` FROM medicines";
    	getMedicines(strSelect);
    }
    
    //this method for user
    @FXML
    void getViewBills(MouseEvent event) throws IOException {
    	
    	hboxTop.getChildren().remove(btnBills);
    	hboxTop.getChildren().remove(tfSearch);
    	datePickerSearch.setVisible(true);
    	btnBack.setVisible(true);
    	key = "getTotalSales";
    	    	
    	Statement state;
		ResultSet rs;
		
		flowPaneContent.getChildren().clear();
				
		try {
			String selectSalesNum = "SELECT `NO` FROM sales";
			
			Connection conn=DBinfo.connDB();
			state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = state.executeQuery(selectSalesNum);
			
			while(rs.next()) {
				FXMLLoader loaderBill = new FXMLLoader(getClass().getResource("/fxml/Bill.fxml"));
				Parent root = loaderBill.load();

				BillController billController = loaderBill.getController();
				billController.BorderPaneBill.setTop(null);
				billController.setBill(rs.getDouble("no"));
				
				billController.BorderPaneBill.setPrefHeight(
						billController.BorderPaneBill.getPrefHeight() + 
						billController.vboxBillContent.getPrefHeight() - 110);
				
				flowPaneContent.setOrientation(Orientation.VERTICAL);
				flowPaneContent.getChildren().addAll(root);
			}
		} catch (SQLException e) {
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		} 
    }
    
    @FXML
    void enterSearch(KeyEvent event) throws IOException {
    	
    	if (event.getCode() == KeyCode.ENTER) {
    		getSearchMedicine(event);
        }
    	
    	tfSearch.requestFocus();
    }
    
    @FXML
    void serchMedicine(MouseEvent event) throws IOException {
    	getSearchMedicine(event);
    }
    
    @FXML
    void typeSearch(KeyEvent event) throws IOException {
    	
    }
    
    @FXML
    void enterSearchDate(KeyEvent event) throws IOException {
    	if (event.getCode() == KeyCode.ENTER) {
    		getSearchMedicine(event);
        }
    	
    	datePickerSearch.requestFocus();
    }
    
    void getSearchMedicine(Event event) throws IOException {
    	String searchWord = tfSearch.getText();
    	flowPaneContent.getChildren().clear();
    	
    	FXMLLoader loaderDashboard = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
    	Parent root = loaderDashboard.load();
    	DashboardController dashboardController = loaderDashboard.getController();
    	    	
    	switch (key) {
			case "getExpiarythisMonth":
				{
					String gcTimeMore = dashboardController.getTimeMore();
			    	String gcTimeLessOrEqual = dashboardController.getTimeLessOrEqual();
			    	String strSelectExpiaryThisMonth = "SELECT * FROM `medicines` WHERE (`DOE` >= '"+gcTimeMore+"') AND (`DOE` <= '"+gcTimeLessOrEqual+"') AND (`name` LIKE '"+searchWord+"%')"; 
					getMedicines(strSelectExpiaryThisMonth);
				}
			break;
			
			case "getSimilarCompanies":
				{
			    	String strSelectSimilarCompanies = "SELECT * FROM companies WHERE `name` LIKE '"+searchWord+"%'";
					getCompanies(strSelectSimilarCompanies);
				}
			break;
			
			case "getOutOfStock":
				{
					String dateTimt = dashboardController.getDate();
					String strSelectOutOfStock = "SELECT * FROM `medicines` WHERE `DOE` < '"+dateTimt+"' AND `name` LIKE '"+searchWord+"%'";
					getMedicines(strSelectOutOfStock);
				}
			break;
			
			case "getTotalSales":
				{
					if(datePickerSearch.getValue() != null) {
				    	Statement state;
						ResultSet rs;
						String year = String.valueOf(datePickerSearch.getValue().getYear());
						int month = datePickerSearch.getValue().getMonthValue();
						
						String strMonth = null;
						if(month < 10)
							 strMonth = "0"+String.valueOf(month);
						else
							strMonth = String.valueOf(month);
						
						int day = datePickerSearch.getValue().getDayOfMonth();
						
						String strDay = null;
						if(day < 10)
							strDay = String.valueOf(day);
						else
							strDay = String.valueOf(day);
						
						String date = year+"-"+strMonth+"-"+strDay;
						System.out.println(date);
						
						try {
							String selectSalesNum = "SELECT DISTINCT `NO` FROM sales WHERE `time` LIKE '"+date+"%'";
							
							Connection conn=DBinfo.connDB();
							state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
									ResultSet.CONCUR_READ_ONLY);
							rs = state.executeQuery(selectSalesNum);
							
							while(rs.next()) {
								FXMLLoader loaderBill = new FXMLLoader(getClass().getResource("/fxml/Bill.fxml"));
								Parent root2 = loaderBill.load();
	
								BillController billController = loaderBill.getController();
								billController.BorderPaneBill.setTop(null);
								billController.setBill(rs.getDouble("no"));
								
								billController.BorderPaneBill.setPrefHeight(
										billController.BorderPaneBill.getPrefHeight() + 
										billController.vboxBillContent.getPrefHeight() - 110);
								
								flowPaneContent.setOrientation(Orientation.VERTICAL);
								
								flowPaneContent.getChildren().addAll(root2);
							}
						} catch (SQLException e) {
							e.printStackTrace();
							ErrorServerNotFound err = new ErrorServerNotFound();
							err.errException(e);
							return;
						}
					}
				}
			break;
			
			case "getTotalPurchases":
			{
				if(datePickerSearch.getValue() != null) {
			    	Statement state;
					ResultSet rs;
					String year = String.valueOf(datePickerSearch.getValue().getYear());
					int month = datePickerSearch.getValue().getMonthValue();
					
					String strMonth = null;
					if(month < 10)
						 strMonth = "0"+String.valueOf(month);
					else
						strMonth = String.valueOf(month);
					
					int day = datePickerSearch.getValue().getDayOfMonth();
					
					String strDay = null;
					if(day < 10)
						strDay = String.valueOf(day);
					else
						strDay = String.valueOf(day);
					
					String date = year+"-"+strMonth+"-"+strDay;
					System.out.println(date);
					
					try {
						String selectSalesNum = "SELECT DISTINCT `NO` FROM purchases WHERE `time` LIKE '"+date+"%'";
						
						Connection conn=DBinfo.connDB();
						state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
								ResultSet.CONCUR_READ_ONLY);
						rs = state.executeQuery(selectSalesNum);
						
						while(rs.next()) {
							FXMLLoader loaderBill = new FXMLLoader(getClass().getResource("/fxml/PurchaseBill.fxml"));
							Parent root2 = loaderBill.load();

							PurchaseBillController billController = loaderBill.getController();
							billController.BorderPaneBill.setTop(null);
							billController.setBill(rs.getDouble("no"));
							
							billController.BorderPaneBill.setPrefHeight(
									billController.BorderPaneBill.getPrefHeight() + 
									billController.vboxBillContent.getPrefHeight() - 110);
							
							flowPaneContent.setOrientation(Orientation.VERTICAL);
							
							flowPaneContent.getChildren().addAll(root2);
						}
					} catch (SQLException e) {
						e.printStackTrace();
						ErrorServerNotFound err = new ErrorServerNotFound();
						err.errException(e);
						return;
					}
				}
			}
			break;
			
			case "searchCompanyMedicines":
			{
				String strSearchCompanyMedicines = "SELECT * FROM `medicines` "
						+ "WHERE `name` LIKE '"+searchWord+"%' AND COM_ID = "
						+ "(SELECT `ID` FROM companies WHERE `NAME` = '"+companyName+"')";
				getMedicines(strSearchCompanyMedicines);
			}
			break;
			
			case "getViewUsers":
			{
				String strSelect = "SELECT `id` FROM employees WHERE `name` LIKE '"+searchWord+"%'";
				getUsers(strSelect);
			}
			break;
			
			case "getSuppliers":
			{
				String strSelect = "SELECT `id` FROM supplier WHERE `name` LIKE '"+searchWord+"%'";
				getSuppliers(strSelect);
			}
			break;
			
			case "getFinishedMedicines":
			{
		    	String strSelect = "SELECT `id` FROM quantity WHERE "
		    			+ "`BOXES` = 0 AND `STRIPES` = 0 AND `UNITS` = 0 "
		    			+ "AND `id` IN (SELECT `ID` FROM medicines WHERE `name` LIKE '"+searchWord+"%')";
				getMedicines(strSelect);
			}
			break;
			
			case "getViewLog":
			{
				if(datePickerSearch.getValue() != null) {
					String year = String.valueOf(datePickerSearch.getValue().getYear());
					int month = datePickerSearch.getValue().getMonthValue();
					
					String strMonth = null;
					if(month < 10)
						 strMonth = "0"+String.valueOf(month);
					else
						strMonth = String.valueOf(month);
					
					int day = datePickerSearch.getValue().getDayOfMonth();
					
					String strDay = null;
					if(day < 10)
						strDay = String.valueOf(day);
					else
						strDay = String.valueOf(day);
					
					String date = year+"-"+strMonth+"-"+strDay;
									
					String selectLogs = "SELECT `NO` FROM log WHERE `login` LIKE '"+date+"%'";
					getLog(selectLogs);
				}else {
					String selectLogs = "SELECT `NO` FROM log";
					getLog(selectLogs);
				}
			}
			break;
				
			default:
				{
					String dateTimt = dashboardController.getDate();
					String strSelectOutOfStock = "SELECT * FROM `medicines` WHERE `DOE` > '"+dateTimt+"' AND `name` LIKE '"+searchWord+"%'";
					getMedicines(strSelectOutOfStock);
				}
			break;
		}
    }
    
    void setTFsearch(String str) {
    	tfSearch.setText(str);
    }
}