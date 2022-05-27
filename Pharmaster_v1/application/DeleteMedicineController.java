package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DeleteMedicineController {
	   
	String medicineID = null;
		
	@FXML
	private VBox labelsVBox;

    @FXML
    private Label label1;

    @FXML
    private Label label2;
    
    @FXML
    private HBox buttonsHBox;

    @FXML
    private Button btnOK;

    @FXML
    private Button btnCancel;
	
	@FXML
	void cancelDeleteMedicine(MouseEvent event) {
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.close();
	}

	@FXML
    void deleteMedicine(MouseEvent event) throws IOException {
		try {
			Connection conn=DBinfo.connDB();
			
			String strUpdate ="DELETE FROM `medicines` WHERE `id` =?";  
			PreparedStatement ps = conn.prepareStatement(strUpdate);
			ps.setString(1, medicineID);
			ps.executeUpdate();
		} catch (SQLException e) {
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		}
		
		labelsVBox.getChildren().remove(label1);
		label2.setText("Medicine Deleted Successfully");
		
		buttonsHBox.getChildren().remove(btnOK);
		btnCancel.textProperty().set("Close");
	}
}
