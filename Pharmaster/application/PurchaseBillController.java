package application;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
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

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PurchaseBillController {

    @FXML
    BorderPane BorderPaneBill;

    @FXML
    private FontAwesomeIconView icon_close;

    @FXML
    private Pane paneBillContent;

    @FXML
    private Label user;

    @FXML
    private Label total;

    @FXML
    private Label time;
    
    @FXML
    private Label billNumber;

    @FXML
    VBox vboxBillContent;
    
    @FXML
    VBox vboxContent;
    
    @FXML
    HBox hboxHead;
    
    double x, y;

    @FXML
    void close(MouseEvent event) {
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	stage.close();
    }
    
    @FXML
    void pressed(MouseEvent event) {
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	x = event.getScreenX() - stage.getX();
    	y = event.getScreenY() - stage.getY();
    }

    @FXML
    void dragged(MouseEvent event) {
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	stage.setX(event.getScreenX() -x);
    	stage.setY(event.getScreenY() -y);
    }
    
    void setBill(double num) throws SQLException {
		Connection conn = DBinfo.connDB();
		
    	String selectBill = "SELECT * FROM purchases WHERE `no`="+num;
		
    	Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = state.executeQuery(selectBill);
		rs.first();
		
		String billNumber = rs.getString("NO");
		String time = rs.getString("time");
		double totalCoast = 0.0;
		
		//Employee name
		String employeeUserame = rs.getString("EMP_USERNAME");
		
		String selectEmployeeName = "SELECT `name` FROM employees WHERE `username`='"+employeeUserame+"'";
		Statement stateEmpName = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		ResultSet rsEmpName = stateEmpName.executeQuery(selectEmployeeName);
		rsEmpName.first();
		 
		String employeeName = rsEmpName.getString("name");
						
		do {
			String medicineID = rs.getString("id");
			
			//Price
			String strPrice = rs.getString("price");
			
			//Supplier
			String strSupplier = rs.getString("SUPP_NAME");
			
			double price = Double.parseDouble(strPrice);
			totalCoast += price;
			
			String selectMedecinName = "SELECT `name` FROM medicines WHERE `id`='"+medicineID+"'";
			Statement stateMedName = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ResultSet rsMedName = stateMedName.executeQuery(selectMedecinName);
			rsMedName.first();
			
			//Medicine name
			String strMedicineName = rsMedName.getString("name");
			
			//Unit and Quantity
			String strBoxes = rs.getString("boxes");
			String strStripes = rs.getString("stripes");
			String strUnits = rs.getString("units");
			
			//add new item to Bill
			Label lblName = new Label(strMedicineName);
			lblName.setPrefWidth(185);
			lblName.setStyle("-fx-font-size: 14; -fx-padding:0 0 0 10;");
			
			Label lblBoxes = new Label(strBoxes);
			lblBoxes.setPrefWidth(97);
			lblBoxes.setStyle("-fx-font-size: 14");
			
			Label lblStripes = new Label(strStripes);
			lblStripes.setPrefWidth(44);
			lblStripes.setStyle("-fx-font-size: 14");
			
			Label lblUnits = new Label(strUnits);
			lblUnits.setPrefWidth(36);
			lblUnits.setStyle("-fx-font-size: 14");
			
			Label lblPrice = new Label(strPrice);
			lblPrice.setPrefWidth(96);
			lblPrice.setStyle("-fx-font-size: 14");
			
			Label lblSupplier = new Label(strSupplier);
			lblSupplier.setPrefWidth(173);
			lblSupplier.setStyle("-fx-font-size: 14");
			
			HBox newItem = new HBox(5);
			newItem.setPrefWidth(616);
			newItem.setPrefHeight(30);
			
			newItem.getChildren().addAll(lblName, lblBoxes, lblStripes,
					lblUnits, lblPrice, lblSupplier);
			
			vboxBillContent.getChildren().add(newItem);
			vboxBillContent.setPrefHeight(vboxBillContent.getPrefHeight()+30);
			
			vboxContent.setPrefHeight(vboxContent.getPrefHeight()+30);
			
		}while(rs.next());
				
		String strTotalCoast = String.valueOf(totalCoast);
		
		this.billNumber.setText(billNumber);
		this.time.setText(time);
		total.setText(strTotalCoast+" EP"); 
		user.setText(employeeName);

    }

    @FXML
    void print(MouseEvent event) {

    }
    
    @FXML
    void getPDF(MouseEvent event) {
 	   
 	   PDDocument doc = new PDDocument();
	   PDPage page = new PDPage();
	   File file = null;
 	   
 	   try {
		   WritableImage nodeshot = vboxContent.snapshot(
				   new SnapshotParameters(), null);
		   file = new File("chart.png");
		   
           try {
        	   ImageIO.write(SwingFXUtils.fromFXImage(
        			   nodeshot, null), "png", file);
			} catch (IOException e) {
				e.printStackTrace();
			}
           
           PDImageXObject pdimage;
           PDPageContentStream content;		        	           	   
           pdimage = PDImageXObject.createFromFile("chart.png",doc);
           content = new PDPageContentStream(doc, page);
           final PDRectangle mediaBox = page.getMediaBox();
           
           // create an abstract pathname (File object) 
           File f = new File("D:\\PharmasterBills");
           f.mkdir();
           
           File f2 = new File("D:\\PharmasterBills\\PurchaseBills");
           f2.mkdir();
           
           
           
	       content.drawImage(pdimage, 175, mediaBox.getHeight()-pdimage.getHeight()+20
	    		   , pdimage.getWidth()-350,pdimage.getHeight()-(pdimage.getHeight()/3));
	       PDFont font = PDType1Font.SYMBOL;
	       content.setFont(font, 12);
	       content.close();
	       doc.addPage(page);
	       doc.save("D:\\PharmasterBills\\PurchaseBills\\"+billNumber.getText()+"_pdf_file.pdf");
	       doc.close();
	       file.delete();
    	}catch (IOException ex) {
            Logger.getLogger(NodeToPdf.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
