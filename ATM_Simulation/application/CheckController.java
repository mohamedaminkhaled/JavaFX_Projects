package application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class CheckController {
	
	@FXML
	private TextArea textArea;
	@FXML
	private Label lblBankName;
		
	public void addToTextArea(String text) {
		textArea.appendText(text);
		textArea.appendText("\n");
	}
	
	public void setBankName(String bankName) {
		lblBankName.setText(bankName);
	}
}
