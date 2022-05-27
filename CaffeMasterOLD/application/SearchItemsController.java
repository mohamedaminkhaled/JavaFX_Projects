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

public class SearchItemsController {

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
    
    void getItems(String strSelect) throws IOException, ClassNotFoundException, SQLException {

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
				FXMLLoader loaderMedicineCard = new FXMLLoader(getClass().getResource("/UserPages/ItemCard.fxml"));
				Parent root = loaderMedicineCard.load();

				ItemCardController itemCardController = loaderMedicineCard.getController();
				
				if(isClient) {
					//Remove delete and update
					itemCardController.btnRemove.setVisible(false);
					itemCardController.btnEdit.setVisible(false);
				}
				
				itemCardController.setItemCard(rs.getString("name"));
				
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
				
				userCardController.setUserCard(rs.getInt("id"));
				DeleteUserController.userCardController = userCardController;
				
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
    void backToItems(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
    	btnBack.setVisible(false);
    	hboxTop.getChildren().add(2, btnBills);
    	hboxTop.getChildren().add(1, tfSearch);
    	datePickerSearch.setVisible(false);
    	flowPaneContent.getChildren().clear();
		flowPaneContent.setOrientation(Orientation.HORIZONTAL);

    	key = "getTotalMedicines";
    	
    	//view all medicines
    	String strSelect = "SELECT `name` FROM items";
    	getItems(strSelect);
    }
    
    //this method for user
    @FXML
    void getViewBills(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
    	
    	hboxTop.getChildren().remove(btnBills);
    	hboxTop.getChildren().remove(tfSearch);
    	datePickerSearch.setVisible(true);
    	btnBack.setVisible(true);
    	key = "getTotalSales";
    	    	
    	Statement state;
		ResultSet rs;
		Connection conn = null;
		
		flowPaneContent.getChildren().clear();
				
		try {
			String selectSalesNum = "SELECT DISTINCT `bill_number` FROM item_sales "
					+ "ORDER BY `bill_number` DESC LIMIT 20";
			
			conn=DBinfo.connDB();
			state=conn.createStatement();
			rs = state.executeQuery(selectSalesNum);
			
			while(rs.next()) {
				FXMLLoader loaderBill = new FXMLLoader(getClass().getResource("/fxml/Bill.fxml"));
				Parent root = loaderBill.load();

				BillController billController = loaderBill.getController();
				billController.BorderPaneBill.setTop(null);
				billController.setBill(rs.getString("bill_number"));
				billController.user.setStyle("-fx-font-size: 12");
				billController.billNumber.setStyle("-fx-font-size: 12");
				billController.billName.setStyle("-fx-font-size: 12");
				billController.discName.setStyle("-fx-font-size: 12");
				billController.orderName.setStyle("-fx-font-size: 12");
				billController.priceName.setStyle("-fx-font-size: 12");
				billController.quantName.setStyle("-fx-font-size: 12");
				billController.timaName.setStyle("-fx-font-size: 12");
				billController.totalName.setStyle("-fx-font-size: 12");
				billController.userName.setStyle("-fx-font-size: 12");
				billController.time.setStyle("-fx-font-size: 12");
				billController.total.setStyle("-fx-font-size: 12");
				
				
				billController.BorderPaneBill.setPrefHeight(
						billController.BorderPaneBill.getPrefHeight() + 
						billController.vboxBillContent.getPrefHeight() - 110);
				
				flowPaneContent.setOrientation(Orientation.VERTICAL);
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
    void enterSearch(KeyEvent event) throws IOException, ClassNotFoundException, SQLException {
    	
    	if (event.getCode() == KeyCode.ENTER) {
    		getSearchItems(event);
        }
    	
    	tfSearch.requestFocus();
    }
    
    @FXML
    void serchItems(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
    	getSearchItems(event);
    }
    
    @FXML
    void typeSearch(KeyEvent event) throws IOException {
    	
    }
    
    @FXML
    void enterSearchDate(KeyEvent event) throws IOException, ClassNotFoundException, SQLException {
    	if (event.getCode() == KeyCode.ENTER) {
    		getSearchItems(event);
        }
    	
    	datePickerSearch.requestFocus();
    }
    
    void getSearchItems(Event event) throws IOException, ClassNotFoundException, SQLException {
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
					getItems(strSelectExpiaryThisMonth);
				}
			break;
			
			case "getItems":
				{
					String strSelect = "SELECT * FROM items WHERE `name` LIKE '"+searchWord+"%'  ORDER BY `sold` DESC";
					getItems(strSelect);
				}
			break;
			
			case "getTotalSales":
				{
					if(datePickerSearch.getValue() != null) {
				    	Statement state;
						ResultSet rs;
						Connection conn = null;
						
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
							String selectSalesNum = "SELECT DISTINCT `bill_number` FROM item_sales "
									+ "WHERE `time` LIKE '"+date+"%' ORDER BY `time` DESC";
							
							conn=DBinfo.connDB();
							state=conn.createStatement();
							rs = state.executeQuery(selectSalesNum);
							
							while(rs.next()) {
								FXMLLoader loaderBill = new FXMLLoader(getClass().getResource("/fxml/Bill.fxml"));
								Parent root2 = loaderBill.load();
	
								BillController billController = loaderBill.getController();
								billController.BorderPaneBill.setTop(null);
								billController.setBill(rs.getString("bill_number"));
								billController.user.setStyle("-fx-font-size: 12");
								billController.billNumber.setStyle("-fx-font-size: 12");
								billController.billName.setStyle("-fx-font-size: 12");
								billController.discName.setStyle("-fx-font-size: 12");
								billController.orderName.setStyle("-fx-font-size: 12");
								billController.priceName.setStyle("-fx-font-size: 12");
								billController.quantName.setStyle("-fx-font-size: 12");
								billController.timaName.setStyle("-fx-font-size: 12");
								billController.totalName.setStyle("-fx-font-size: 12");
								billController.userName.setStyle("-fx-font-size: 12");
								billController.time.setStyle("-fx-font-size: 12");
								billController.total.setStyle("-fx-font-size: 12");
								
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
						}finally {
							conn.close();
						}
					}
				}
			break;
			
			case "getTotalPurchases":
			{
				if(datePickerSearch.getValue() != null) {
			    	Statement state;
					ResultSet rs;
					Connection conn = null;
					
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
						String selectSalesNum = "SELECT DISTINCT `bill_number` FROM goods_purchses "
								+ "WHERE `time` LIKE '"+date+"%' ORDER BY `time` DESC";
						
						conn=DBinfo.connDB();
						state=conn.createStatement();
						rs = state.executeQuery(selectSalesNum);
						
						while(rs.next()) {
							FXMLLoader loaderBill = new FXMLLoader(getClass().getResource("/fxml/PurchaseBill.fxml"));
							Parent root2 = loaderBill.load();

							PurchaseBillController billController = loaderBill.getController();
							billController.BorderPaneBill.setTop(null);
							billController.setBill(rs.getDouble("bill_number"));
							billController.user.setStyle("-fx-font-size: 12");
							billController.billNumber.setStyle("-fx-font-size: 12");
							billController.billName.setStyle("-fx-font-size: 12");
							billController.priceName.setStyle("-fx-font-size: 10");
							billController.quantName.setStyle("-fx-font-size: 10");
							billController.totalName.setStyle("-fx-font-size: 12");
							billController.userName.setStyle("-fx-font-size: 12");
							billController.time.setStyle("-fx-font-size: 12");
							billController.total.setStyle("-fx-font-size: 12");
							billController.goodName.setStyle("-fx-font-size: 10");
							billController.suppName.setStyle("-fx-font-size: 10");
							billController.timeName.setStyle("-fx-font-size: 10");

							
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
					} finally {
						conn.close();
					}
				}
			}
			break;
			
			case "getViewUsers":
			{
				String strSelect = "SELECT `id` FROM employees WHERE `name` LIKE '"+searchWord+"%'";
				getUsers(strSelect);
			}
			break;
			
			case "getFinishedMedicines":
			{
		    	String strSelect = "SELECT `id` FROM quantity WHERE "
		    			+ "`BOXES` = 0 AND `STRIPES` = 0 AND `UNITS` = 0 "
		    			+ "AND `id` IN (SELECT `ID` FROM medicines WHERE `name` LIKE '"+searchWord+"%')";
				getItems(strSelect);
			}
			break;
							
			default:
				{
					
				}
			break;
		}
    }
    
    void setTFsearch(String str) {
    	tfSearch.setText(str);
    }
}