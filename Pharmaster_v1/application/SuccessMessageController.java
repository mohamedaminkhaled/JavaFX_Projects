package application;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SuccessMessageController {
		double x, y;
	
		@FXML
	    private Button btnClose;

	    @FXML
	    private FontAwesomeIconView icon_close;

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
}
