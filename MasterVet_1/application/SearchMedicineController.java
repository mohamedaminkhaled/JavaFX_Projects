package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
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
    
    void getMedicines(String strSelect) throws IOException, ClassNotFoundException, SQLException {

    	Statement state;
		ResultSet rs;
		Connection conn = null;
				
		try {
			conn=DBinfo.connDB();
			state=conn.createStatement();
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
								
				if(key == "searchCompanyMedicines") {
					medicineCardController.btnSell.setDisable(true);
					medicineCardController.btnAdd.setDisable(true);
				}
				
				if(isClient) {
					medicineCardController.btnAdd.setVisible(false);
					medicineCardController.btnView.setVisible(false);
					medicineCardController.btnPrice.setVisible(false);
				}
				flowPaneContent.getChildren().addAll(root);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		} finally {
			conn.close();
		}
    }
    
	void getUsers(String strSelect) throws IOException, ClassNotFoundException, SQLException {
    	
		if(!isClient) {
    		hboxTop.getChildren().remove(btnBills);
    	}
		
		Statement state;
		ResultSet rs;
		Connection conn = null;
				
		try {
			conn=DBinfo.connDB();
			state=conn.createStatement();
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
		} finally {
			conn.close();
		}
	}
	
	void getClients(String strSelect) throws IOException, ClassNotFoundException, SQLException {
    	
		if(!isClient) {
    		hboxTop.getChildren().remove(btnBills);
    	}
		
		Statement state;
		ResultSet rs;
		Connection conn = null;
				
		try {
			conn=DBinfo.connDB();
			state=conn.createStatement();
			rs=state.executeQuery(strSelect);
			
			while(rs.next()) {
				FXMLLoader loaderMedicineCard = new FXMLLoader(getClass().getResource("/AdminPages/ClientCard.fxml"));
				Parent root = loaderMedicineCard.load();

				ClientCardController clientCardController = loaderMedicineCard.getController();
				ModifyClientController.clientCardController = clientCardController;
				clientCardController.setClientCard(rs.getString("name"));
				
				flowPaneContent.getChildren().addAll(root);
			}
		} catch (SQLException e) {
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		} finally {
			conn.close();
		}
	}
    
    void getCompanies(String strSelectSimilarCompanies) throws IOException, ClassNotFoundException, SQLException {
    	
    	if(!isClient) {
    		hboxTop.getChildren().remove(btnBills);
    	}
    	
    	Statement state;
		ResultSet rs;
		Connection conn = null;
				
		try {
			conn=DBinfo.connDB();
			state=conn.createStatement();
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
		} finally {
			conn.close();
		}
    }
    
    void getSuppliers(String strSelectSuppliers) throws IOException, SQLException, ClassNotFoundException {
    	
    	if(!isClient) {
    		hboxTop.getChildren().remove(btnBills);
    	}
    	
    	Statement state;
		ResultSet rs;
		Connection conn = null;
		
		try {
			conn=DBinfo.connDB();
			state=conn.createStatement();
			rs=state.executeQuery(strSelectSuppliers);
			
			while(rs.next()) {
				FXMLLoader loaderSupplierCard = new FXMLLoader(getClass().getResource("/AdminPages/SupplierCard.fxml"));
				Parent root = loaderSupplierCard.load();

				SupplierCardController supplierCardController = loaderSupplierCard.getController();
				supplierCardController.setSupplierCard(rs.getInt("id"));
				ModifySupplierController.supplierCardController = supplierCardController;
				flowPaneContent.getChildren().addAll(root);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		} finally {
			conn.close();
		}
    }
    
    @FXML
    void backToMedicines(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
    	btnBack.setVisible(false);
    	hboxTop.getChildren().remove(datePickerSearch);
    	hboxTop.getChildren().add(2, btnBills);
    	//datePickerSearch.setVisible(false);
    	flowPaneContent.getChildren().clear();
    	key = "getTotalMedicines";
		flowPaneContent.setOrientation(Orientation.HORIZONTAL);
    	
    	//view all medicines
    	String strSelect = "SELECT `id` FROM medicines";
    	getMedicines(strSelect);
    }
    
    //this method for user
    @FXML
    void getViewBills(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
    	
    	hboxTop.getChildren().remove(btnBills);
    	hboxTop.getChildren().add(1, datePickerSearch);
    	datePickerSearch.setVisible(true);
    	btnBack.setVisible(true);
    	key = "getTotalSales";
    	    	
    	Statement state;
		ResultSet rs;
		Connection conn = null;
		
		flowPaneContent.getChildren().clear();
				
		try {
			String selectSalesNum = "SELECT DISTINCT `NO` FROM sales";
			
			conn=DBinfo.connDB();
			state=conn.createStatement();
			rs = state.executeQuery(selectSalesNum);
			
			while(rs.next()) {
				FXMLLoader loaderBill = new FXMLLoader(getClass().getResource("/fxml/Bill.fxml"));
				Parent root = loaderBill.load();

				BillController billController = loaderBill.getController();
				billController.BorderPaneBill.setTop(null);
				billController.setBill(rs.getString("no"));
				
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
		} finally {
			conn.close();
		}
    }
    
    @FXML
    void enterSearch(KeyEvent event) throws IOException, ClassNotFoundException, SQLException {
    	
    	if (event.getCode() == KeyCode.ENTER) {
    		getSearchMedicine(event);
        }
    	
    	tfSearch.requestFocus();
    }
    
    @FXML
    void serchMedicine(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
    	getSearchMedicine(event);
    }
    
    @FXML
    void typeSearch(KeyEvent event) throws IOException {
    	
    }
    
    @FXML
    void enterSearchDate(KeyEvent event) throws IOException, ClassNotFoundException, SQLException {
    	if (event.getCode() == KeyCode.ENTER) {
    		getSearchMedicine(event);
        }
    	
    	datePickerSearch.requestFocus();
    }
    
    void getSearchMedicine(Event event) throws IOException, ClassNotFoundException, SQLException {
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
					Statement state;
					ResultSet rs;
					
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
						System.out.println(date);
						
						try {
							String selectSalesNum = "SELECT DISTINCT `NO` FROM sales WHERE `time` LIKE '"+date+"%'";
							
							Connection conn=DBinfo.connDB();
							state=conn.createStatement();
							rs = state.executeQuery(selectSalesNum);
							
							while(rs.next()) {
								FXMLLoader loaderBill = new FXMLLoader(getClass().getResource("/fxml/Bill.fxml"));
								Parent root2 = loaderBill.load();
	
								BillController billController = loaderBill.getController();
								billController.BorderPaneBill.setTop(null);
								billController.setBill(rs.getString("no"));
								
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
					
					if(tfSearch.getText() != null) {
						try {
							String selectSalesNum = "SELECT `NO` FROM sales WHERE `no` = '"+tfSearch.getText()+"'";
							
							Connection conn=DBinfo.connDB();
							state=conn.createStatement();
							rs = state.executeQuery(selectSalesNum);
							
							if(rs.next()) {
								FXMLLoader loaderBill = new FXMLLoader(getClass().getResource("/fxml/Bill.fxml"));
								Parent root2 = loaderBill.load();
								
								BillController billController = loaderBill.getController();
								billController.BorderPaneBill.setTop(null);
								billController.setBill(rs.getString("no"));
								
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
				Statement state;
				ResultSet rs;
				
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
					System.out.println(date);
					
					try {
						String selectSalesNum = "SELECT DISTINCT `NO` FROM purchases WHERE `time` LIKE '"+date+"%'";
						
						Connection conn=DBinfo.connDB();
						state=conn.createStatement();
						rs = state.executeQuery(selectSalesNum);
						
						while(rs.next()) {
							FXMLLoader loaderBill = new FXMLLoader(getClass().getResource("/fxml/PurchaseBill.fxml"));
							Parent root2 = loaderBill.load();

							PurchaseBillController billController = loaderBill.getController();
							billController.BorderPaneBill.setTop(null);
							billController.setBill(rs.getString("no"));
							
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
				
				if(tfSearch.getText() != null) {					
					try {
						String selectSalesNum = "SELECT `NO` FROM purchases WHERE `no` = '"+tfSearch.getText()+"'";
						
						Connection conn=DBinfo.connDB();
						state=conn.createStatement();
						rs = state.executeQuery(selectSalesNum);
						
						if(rs.next()) {
							FXMLLoader loaderBill = new FXMLLoader(getClass().getResource("/fxml/PurchaseBill.fxml"));
							Parent root2 = loaderBill.load();
	
							PurchaseBillController billController = loaderBill.getController();
							billController.BorderPaneBill.setTop(null);
							billController.setBill(rs.getString("no"));
							
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
			
			case "getViewClients":
			{
				String strSelect = "SELECT `name` FROM `clients` WHERE `name` LIKE '"+searchWord+"%'";
				getClients(strSelect);
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
			
				
			default:
				{
					String dateTimt = dashboardController.getDate();
					String strSelectOutOfStock = "SELECT * FROM `medicines` WHERE `name` LIKE '"+searchWord+"%'";
					getMedicines(strSelectOutOfStock);
				}
			break;
		}
    }
    
    void setTFsearch(String str) {
    	tfSearch.setText(str);
    }
}