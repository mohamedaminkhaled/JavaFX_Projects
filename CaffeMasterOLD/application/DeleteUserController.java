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

public class DeleteUserController {
	   
	String userID = null;
		
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
    
    static UserCardController userCardController;
	
	@FXML
	void cancelDeleteUser(MouseEvent event) {
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.close();
	}

	@FXML
    void deleteUser(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
		Connection conn = null;
		try {
			conn=DBinfo.connDB();
			
			String strUpdate ="DELETE FROM `employees` WHERE `id` =?";  
			PreparedStatement ps = conn.prepareStatement(strUpdate);
			ps.setString(1, userID);
			ps.executeUpdate();
		} catch (SQLException e) {
			ErrorServerNotFound err = new ErrorServerNotFound();
			err.errException(e);
			return;
		} finally {
			conn.close();
		}
		
		userCardController.BorderPaneMedicineCard.setVisible(false);
		
		labelsVBox.getChildren().remove(label1);
		label2.setText("User Deleted Successfully");
		
		buttonsHBox.getChildren().remove(btnOK);
		btnCancel.textProperty().set("Close");
	}
}
