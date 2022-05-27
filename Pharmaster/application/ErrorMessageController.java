package application;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ErrorMessageController {
	double x, y;
	
	@FXML
    Label errMessage;
	
	 @FXML
	 private Button btn_close;

	 @FXML
	 private FontAwesomeIconView icon_close;
	 
	 WindowBasics windowBasics = new WindowBasics();
	 
	@FXML
    void close(MouseEvent event) {
		windowBasics.close(event);
    }
    
    @FXML
    void pressed(MouseEvent event) {
    	windowBasics.pressed(event);
    }
    
    @FXML
    void dragged(MouseEvent event) {
    	windowBasics.dragged(event);
    }
}
