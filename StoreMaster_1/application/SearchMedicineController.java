package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
    
    @FXML
    ImageView btnAddCategory;
    
//    @FXML
//    Pane paneShowCart;
    
    static boolean isClient = false;
    
    String key = null;
    String companyName;
    String username;
    
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
				FXMLLoader loaderItemCard = new FXMLLoader(getClass().getResource("/UserPages/ItemCard.fxml"));
				Parent root = loaderItemCard.load();

				ItemCardController itemCardController = loaderItemCard.getController();
				itemCardController.employeeUsername = username;
				itemCardController.setItemCard(rs.getString("id"));
				
				if(isClient) {
					itemCardController.vboxEditItem.getChildren().clear();
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
	
    @FXML
    void registerCategory(MouseEvent event) {
    	loadPage("/AdminPages/AddCategory", "«÷«›… ’‰›");
    }
    
    void loadPage(String ui, String title) {
    	Parent root = null;
    	try {
			root=FXMLLoader.load(getClass().getResource(ui+".fxml"));
	    	
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
			Stage stage = new Stage();
			stage.setResizable(true);
			stage.setScene(scene);
			stage.setTitle(title);
			stage.show();
			
    	}catch(Exception e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    void showCart(MouseEvent event) throws ClassNotFoundException, SQLException, IOException {
    	//Set clients names
    	Connection conn = DBinfo.connDB();
		Statement state = conn.createStatement();
    	String strSelectCompanies = "SELECT `name` FROM `clients`";
		ResultSet rs = state.executeQuery(strSelectCompanies);
		
		ArrayList<String> list = new ArrayList<>();
		
		while(rs.next()) {
			list.add(rs.getString("name"));
		}
		
		ObservableList<String> cmboList = FXCollections.observableArrayList(list);    	    	    	
    	
    	//load Cart page on the right of search pane
    	FXMLLoader loaderCart = new FXMLLoader(getClass().getResource("/fxml/Cart.fxml"));
    	Parent rootCart = loaderCart.load();
    	CartController cartController = loaderCart.getController();
    	cartController.username = username;
    	cartController.comboClientName.getItems().addAll(cmboList);
    	
    	AutoCompleteComboBoxListener<String> comboClientNames = new AutoCompleteComboBoxListener<>(cartController.comboClientName);
    	
    	SellStockController.cartController = cartController;
    	SellStockController.jobtitle = "admin";
    	
    	Scene scene2 = new Scene(rootCart);
		Stage stage2 = new Stage();
		stage2.setScene(scene2);
		stage2.setResizable(false);
		stage2.show();
    }
    
    void getCategories(String strSelectSimilarCompanies) throws IOException, ClassNotFoundException, SQLException {
    	
    	FXMLLoader loaderItemCard = new FXMLLoader(getClass().getResource("/UserPages/ItemCard.fxml"));
		Parent rootItemCard = loaderItemCard.load();
    	ItemCardController itemCardController = loaderItemCard.getController();
		
    	if(!isClient) {
    		hboxTop.getChildren().remove(btnBills);
    		itemCardController.vboxEditItem.getChildren().clear();
    	}
    	
    	Statement state;
		ResultSet rs;
		Connection conn = null;
				
		try {
			conn=DBinfo.connDB();
			state=conn.createStatement();
			rs=state.executeQuery(strSelectSimilarCompanies);
			
			while(rs.next()) {
				FXMLLoader loaderCompanyCard = new FXMLLoader(getClass().getResource("/UserPages/CategoryCard.fxml"));
				Parent root = loaderCompanyCard.load();

				CategoryCardController categoryCardController = loaderCompanyCard.getController();
				
				categoryCardController.setCategoryCard(rs.getString("name"));
				
				if(isClient) {
					categoryCardController.vBoxEditCategory.setVisible(false);
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
    	String strSelect = "SELECT `name` FROM `categories`";
    	getCategories(strSelect);
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
    		    	
    	switch (key) {
			
			case "getCategories":
				{
			    	String strSelectSimilarCompanies = "SELECT * FROM `categories` WHERE `name` LIKE '"+searchWord+"%'";
					getCategories(strSelectSimilarCompanies);
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
			
			case "searchCategoryItems":
			{
				String strSearchCompanyMedicines = "SELECT * FROM `items` "
						+ "WHERE `name` LIKE '"+searchWord+"%' OR `id` ='"+searchWord+"'";
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
			
		}
    }
    
    void setTFsearch(String str) {
    	tfSearch.setText(str);
    }
}